package com.ttv.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.nio.charset.StandardCharsets;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;

import com.ttv.bean.MessageData;
import com.ttv.bean.SwapEnvelope;
import com.ttv.bean.SwapType;
import com.ttv.bean.User;
import com.ttv.dao.SwapHubDAO;
import com.ttv.process.AppClientManager;
import com.ttv.process.FileMessageHandler;
import com.ttv.process.OfferMessageHandler;
import com.ttv.process.PingMessageHandler;
import com.ttv.process.Session;
import com.ttv.process.SessionManager;
import com.ttv.process.SwapEnvelopeFactory;
import com.ttv.process.TextMessageHandler;

public class SwapChatSeverHandler extends	SimpleChannelInboundHandler<SwapEnvelope> {

	static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	private int user_id;
	private int app_client_id;
	private String username;
	final static Logger logger = Logger.getLogger(SwapChatSeverHandler.class);
	
	public SwapChatSeverHandler(){
		
	}
	
	public SwapChatSeverHandler(int user_id,int app_client_id,String username){
		this.user_id = user_id;
		this.app_client_id = app_client_id;
		this.username = username;
	}
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		logger.info(incoming.remoteAddress()+ " has joined");
		channels.add(ctx.channel());
		
		SwapHandlerMessageQueue handlerMessageQueue = new SwapHandlerMessageQueue(user_id, ctx.channel());
		handlerMessageQueue.start();
		
		
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		channels.remove(ctx.channel());
		SessionManager.getInstall().removeChannel(app_client_id);
		AppClientManager.getInstall().removeAppClientID(user_id, app_client_id);
		logger.info("App_client_id:"+app_client_id+" User_id:"+user_id+ " left join.");
		logger.info("Total channel:" + channels.size());
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, SwapEnvelope envelope) throws Exception {
		switch (envelope.getType()) {
		case LOGIN:
			processLogin(ctx, envelope);
			break;
		case TEXT:
			processText(ctx, envelope);
			break;
		case FILE:
			processFile(ctx, envelope);
			break;
		case OFFER_BUY:
			processOfferBuy(ctx, envelope);
			break;
		case OFFER_BUY_AGREE:
			processOfferBuy(ctx, envelope);
			break;
		case OFFER_BUY_CANCEL:
			processOfferBuy(ctx, envelope);
			break;
		case OFFER_BUY_DENY:
			processOfferBuy(ctx, envelope);
			break;
		case OFFER_SWAP:
			processOfferSwap(ctx, envelope);
			break;
		case OFFER_SWAP_AGREE:
			processOfferSwap(ctx, envelope);
			break;
		case OFFER_SWAP_CANCEL:
			processOfferSwap(ctx, envelope);
			break;
		case OFFER_SWAP_DENY:
			processOfferSwap(ctx, envelope);
			break;
		case REPLY:
			break;
		case PING:
			processPing(ctx,envelope);
			break;
		case LOGOUT:
			ctx.close(); 
			break;
		default:
			break;
		}

		// Close the connection if the client has sent 'bye'.
		/*
		 * if ("bye".equals(strMsg.toLowerCase())) { ctx.close(); }
		 */
	}

	private void processLogin(ChannelHandlerContext ctx, SwapEnvelope msg) {
		SwapHubDAO swapHubDAO = new SwapHubDAO();
		String mLogin = new String(msg.getPayload());
		JSONObject user = (JSONObject) JSONSerializer.toJSON(mLogin);
		User userLogin = swapHubDAO.getUserLogin(user.getInt("id"),
				user.getString("username"));
		if (userLogin != null) {
			// Add Channel
			Session session = new Session(-1, user.getInt("app_client_id"),
					user.getInt("id"), ctx.channel());
			SessionManager.getInstall().addSession(
					user.getInt("app_client_id"), session);
			// Add App Client ID
			AppClientManager.getInstall().addAppClientID(user.getInt("id"),
					user.getInt("app_client_id"));
			this.user_id = user.getInt("id");
			this.app_client_id = user.getInt("app_client_id");
			this.username = user.getString("username");
			logger.info("Logined:user_id" + user_id+", App_Client_ID:"+app_client_id+" Username:"+username);
		} else {
			channels.remove(ctx.channel());
			ctx.channel().close();
			ctx.close();
			logger.info("Login Fail");
		}
	}

	private void processText(ChannelHandlerContext ctx, SwapEnvelope envelope) {
		Session session = SessionManager.getInstall().getSession(app_client_id);
		if (session == null) {
			ctx.close();
			return;
		}

		String payload = new String(envelope.getPayload(),
				StandardCharsets.UTF_8);
		JSONObject json = (JSONObject) JSONSerializer.toJSON(payload);
		int pID = json.getInt("pID");
		String content = json.getString("content");
		long time = json.getLong("time");

		MessageData messageData = MessageData.buileMessageText(user_id,
				envelope.gettUID(), pID, content, envelope.getType(), time);

		TextMessageHandler textMessageHandler = new TextMessageHandler(
				envelope, messageData, session);
		textMessageHandler.processMessage();
	}

	private void processOfferBuy(ChannelHandlerContext ctx,
			SwapEnvelope envelope) {
		Session session = SessionManager.getInstall().getSession(app_client_id);
		if (session == null) {
			ctx.close();
			return;
		}
		
		String payload = new String(envelope.getPayload(),
				StandardCharsets.UTF_8);
		
		System.out.println("PL:"+payload);
		
		JSONObject json = (JSONObject) JSONSerializer.toJSON(payload);
		int pID = json.getInt("pID");
		String content = json.getString("content");
		double price = json.getDouble("price");
		int quantity = json.getInt("quantity");
		long time = json.getLong("time");

		MessageData messageData = MessageData.builderMessageOfferBuy(user_id,
				envelope.gettUID(), pID, content, envelope.getType(), price,
				quantity, time);

		OfferMessageHandler offerMessageHandler = new OfferMessageHandler(
				envelope, messageData, session);
		offerMessageHandler.processMessage();
	}

	private void processOfferSwap(ChannelHandlerContext ctx,
			SwapEnvelope envelope) {
		Session session = SessionManager.getInstall().getSession(app_client_id);
		if (session == null) {
			ctx.close();
			return;
		}

		String payload = new String(envelope.getPayload(),
				StandardCharsets.UTF_8);
		JSONObject json = (JSONObject) JSONSerializer.toJSON(payload);

		int pID = json.getInt("pID");
		String content = json.getString("content");
		double price = json.getDouble("price");
		int quantity = json.getInt("quantity");
		long time = json.getLong("time");
		String psID = json.getString("psID");

		MessageData messageData = MessageData.builderMessageOfferSwap(user_id,
				envelope.gettUID(), pID, content, envelope.getType(), price,
				quantity, psID, time);

		OfferMessageHandler offerMessageHandler = new OfferMessageHandler(
				envelope, messageData, session);
		offerMessageHandler.processMessage();
	}

	private void processFile(ChannelHandlerContext ctx, SwapEnvelope envelope) {
		Session session = SessionManager.getInstall().getSession(app_client_id);
		if (session == null) {
			ctx.close();
			return;
		}
		String payload = new String(envelope.getPayload(),StandardCharsets.UTF_8);
		JSONObject json = (JSONObject) JSONSerializer.toJSON(payload);
		// File Header
		int pID = json.getInt("pID");
		long fileSize = json.getLong("size");
		long time = json.getLong("time");

		MessageData messageData = MessageData.builderMessageFile(user_id,
				envelope.gettUID(), pID, "", envelope.getType(),
				envelope.getFile(), fileSize, time);

		SwapEnvelope envelopeFile = SwapEnvelopeFactory.builderEnvelopeFile(
				envelope.getAppID(), envelope.getfUID(), envelope.gettUID(),
				pID, "", envelope.getFile(), fileSize, time);
		FileMessageHandler fileMessageHandler = new FileMessageHandler(
				envelopeFile, messageData, session);
		fileMessageHandler.processMessage();
	}
	
	
	private void processPing(ChannelHandlerContext ctx, SwapEnvelope envelope) {
		Session session = SessionManager.getInstall().getSession(app_client_id);
		if (session == null) {
			ctx.close();
			return;
		}

		String payload = new String(envelope.getPayload(),
				StandardCharsets.UTF_8);
		JSONObject json = (JSONObject) JSONSerializer.toJSON(payload);
		int pID = json.getInt("pID");
		String content = json.getString("content");
		long time = json.getLong("time");

		MessageData messageData = MessageData.buileMessageText(user_id,
				envelope.gettUID(), pID, content, envelope.getType(), time);

		PingMessageHandler pingMessageHandler = new PingMessageHandler(
				envelope, messageData, session);
		pingMessageHandler.processMessage();
	}

	
	 @Override 
	 public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		 try {
			 SessionManager.getInstall().removeChannel(app_client_id);
			 AppClientManager.getInstall().removeAppClientID(user_id, app_client_id);
			 System.out.println("thoat:"+channels.size()); 
		 }
		 catch (Exception e) {
		 //TODO: handle exception 
		 }
		 super.channelUnregistered(ctx);
	}
	 
	/*
	 * @Override public void channelActive(final ChannelHandlerContext ctx) { //
	 * Once session is secured, send a greeting and register the channel to the
	 * global channel // list so the channel received the messages from others.
	 * ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener( new
	 * GenericFutureListener<Future<Channel>>() {
	 * 
	 * @Override public void operationComplete(Future<Channel> future) throws
	 * Exception { System.out.println( "Welcome to " +
	 * InetAddress.getLocalHost().getHostName() + " secure chat service!\n");
	 * channels.add(ctx.channel()); } }); }
	 */

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		System.out.println("Error, Exiting.");
		cause.printStackTrace();
		ctx.close();
	}
}
