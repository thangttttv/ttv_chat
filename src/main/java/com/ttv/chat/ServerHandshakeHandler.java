package com.ttv.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.ttv.bean.MessageData;
import com.ttv.bean.SwapEnvelope;
import com.ttv.bean.SwapType;
import com.ttv.bean.User;
import com.ttv.dao.SwapHubDAO;
import com.ttv.process.AppClientManager;
import com.ttv.process.Session;
import com.ttv.process.SessionManager;
import com.ttv.process.SwapEnvelopeFactory;

//IdleStateHandler

public class ServerHandshakeHandler extends SimpleChannelInboundHandler<SwapEnvelope>  {
	
	  private final long timeoutInMillis;
	  private final AtomicBoolean handshakeComplete;
	  private final AtomicBoolean handshakeFailed;
	  private final Object handshakeMutex = new Object();
	  private final CountDownLatch latch = new CountDownLatch(1);
	  private int user_id;
	
	// constructors -----------------------------------------------------------

    public ServerHandshakeHandler(long timeoutInMillis) {
        this.timeoutInMillis = timeoutInMillis;
        this.handshakeComplete = new AtomicBoolean(false);
        this.handshakeFailed = new AtomicBoolean(false);
    }
	    
	@Override
	public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
		//Channel incoming = ctx.channel();
        // Fire up the handshake handler timeout checker.
        // Wait X seconds for the handshake then disconnect.
        new Thread() {

            @Override
            public void run() {
                try {
                    latch.await(timeoutInMillis, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e1) {
                    System.out.println("+++ SERVER-HS :: Handshake timeout checker: " +
                        "interrupted!");
                    e1.printStackTrace();
                }

                if (handshakeFailed.get()) {
                	System.out.println("+++ SERVER-HS :: (pre-synchro) Handshake timeout " +
                        "checker: discarded (handshake failed)");
                    return;
                }

                if (handshakeComplete.get()) {
                	System.out.println("+++ SERVER-HS :: (pre-synchro) Handshake timeout " +
                        "checker: discarded (handshake complete)");
                    return;
                }

                synchronized (handshakeMutex) {
                    if (handshakeFailed.get()) {
                    	System.out.println("+++ SERVER-HS :: (synchro) Handshake timeout " +
                            "checker: already failed.");
                        return;
                    }

                    if (!handshakeComplete.get()) {
                    	System.out.println("+++ SERVER-HS :: (synchro) Handshake timeout " +
                            "checker user_id:"+user_id+" timed out, killing connection.");
                        ctx.close();
                    } else {
                    	System.out.println("+++ SERVER-HS :: (synchro) Handshake timeout " +
                            "checker: discarded (handshake OK)");
                    	
                    }
                }
            }
        }.start();
	}

	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, SwapEnvelope msg)
			throws Exception {
		SwapHubDAO swapHubDAO = new SwapHubDAO();
		String mLogin = new String(msg.getPayload());
		JSONObject user = (JSONObject) JSONSerializer.toJSON(mLogin);
		User userLogin = swapHubDAO.getUserLogin(user.getInt("id"),user.getString("username"));
		
		if (userLogin != null) {
			// Add Channel
			Session session = new Session(-1, user.getInt("app_client_id"),
					user.getInt("id"), ctx.channel());
			SessionManager.getInstall().addSession(
					user.getInt("app_client_id"), session);
			// Add App Client ID
			AppClientManager.getInstall().addAppClientID(user.getInt("id"),
					user.getInt("app_client_id"));
			user_id = user.getInt("id");
			
			//ctx.channel().config().setOption(ChannelOption.SO_RCVBUF,10000);
			
			this.handshakeComplete.set(true);
	        this.handshakeFailed.set(false);	
	        ctx.channel().pipeline().remove(this);
			ctx.channel().pipeline().addLast(new SwapChatSeverHandler(user.getInt("id"),user.getInt("app_client_id"),user.getString("username")));
			
		} else {
			ctx.channel().close();
			ctx.close();
		}
		
	}

}
