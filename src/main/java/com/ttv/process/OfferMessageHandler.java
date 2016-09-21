package com.ttv.process;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;

import com.ttv.bean.MessageData;
import com.ttv.bean.Offer;
import com.ttv.bean.Product;
import com.ttv.bean.SwapEnvelope;
import com.ttv.bean.SwapType;
import com.ttv.bean.User;
import com.ttv.chat.SwapChatSeverHandler;
import com.ttv.dao.SwapHubDAO;
import com.ttv.util.ChatUtil;


public class OfferMessageHandler extends MessageHandler {
	final static Logger logger = Logger.getLogger(SwapChatSeverHandler.class);
	
	public OfferMessageHandler(SwapEnvelope envelope,MessageData message, Session session) {
		super(envelope, message, session);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void processMessage(){
		// Save Message
		saveMessage();
		// Put Message Repy
		SwapEnvelope envelope = SwapEnvelopeFactory.builderEnvelopeReply(message.app_client_id,message.fuID, message.tuID, message.pID, message.time);
		session.getChannel().writeAndFlush(envelope);
		//offer
		SwapHubDAO swapHubDAO = new SwapHubDAO();
		Product oProduct = swapHubDAO.getProductByID(message.pID);
		int seller_id = oProduct.user_id;
		int buyer_id = message.tuID==seller_id?message.fuID:message.tuID;
		
		Offer offer =new Offer(seller_id,buyer_id,message.pID,message.p_swap_id,message.price, message.type,message.quantity);
		processOffer(offer);

		// Send Message To UserF
		sendMessageFUser();
		
		// Send Inform
		if(message.type==SwapType.OFFER_BUY_AGREE||message.type==SwapType.OFFER_SWAP_AGREE){
			String contentInform = "Sản phẩm của bạn được chuyển sang trạng thái đã bán. "
					+ "Nếu giao dịch giữa bạn và người mua không thành công. Bạn hãy hủy trả giá này để chuyển sản phẩm sang trạng thái bán hàng.";
					
			SwapEnvelope envelopeInform = SwapEnvelopeFactory.builderEnvelopeInform(message.app_client_id,message.tuID, message.fuID, message.pID,contentInform, Calendar.getInstance().getTimeInMillis());
			session.getChannel().writeAndFlush(envelopeInform);
			sendMessageInformFUser(envelopeInform);
		}
		
		// Send Message To UserT
		sendMessageTUser();
		
		// Send Inform
		if(message.type==SwapType.OFFER_BUY_AGREE||message.type==SwapType.OFFER_SWAP_AGREE){
			String contentInform = "Chúc mừng bạn đã mua được sản phẩm. Hãy tới tận nơi để kiểm tra kỹ trước khi trả tiền, tránh trả tiền trước khi nhận hàng.";
			SwapEnvelope envelopeInform = SwapEnvelopeFactory.builderEnvelopeInform(message.app_client_id,message.fuID, message.tuID, message.pID,contentInform, Calendar.getInstance().getTimeInMillis());
			sendMessageInformTUser(envelopeInform);
		}
	}
	
	protected void sendMessageInformFUser(SwapEnvelope envelopeInform){
		String app_clien_ids = AppClientManager.getInstall().getAppClientIDs(message.fuID);
		String[] arrAppClient = null;
		if(app_clien_ids!=null&&!"".equalsIgnoreCase(app_clien_ids))
		 arrAppClient = app_clien_ids.split(",");
	  	
		int i = 0;
	  	if(arrAppClient!=null)
		 while (i<arrAppClient.length) {
			 Session session2 = SessionManager.getInstall().getSession(Integer.parseInt(arrAppClient[i]));
				 if(session2!=null&&session.getApp_client_id()!=Integer.parseInt(arrAppClient[i])){
					 session2.sendMessage(envelopeInform);
				 }
				 i++;
			}
	  
	}
	
	protected void sendMessageInformTUser(SwapEnvelope envelopeInform){
		String app_clien_ids = AppClientManager.getInstall().getAppClientIDs(message.tuID);
		String[] arrAppClient = null;
		if(app_clien_ids!=null&&!"".equalsIgnoreCase(app_clien_ids))
		 arrAppClient = app_clien_ids.split(",");
		
		
	  	int i = 0;int kq = 0;
	  	if(arrAppClient!=null)
		while (i<arrAppClient.length) {
			 Session session = SessionManager.getInstall().getSession(Integer.parseInt(arrAppClient[i]));
				 if(session!=null){
					 session.sendMessage(envelopeInform);
					 kq++;
				 }
				 i++;
		}
	   
	  // Save QUEUE 
	   if(kq==0) {
		   String contentInform = "Chúc mừng bạn đã mua được sản phẩm. Hãy tới tận nơi để kiểm tra kỹ trước khi trả tiền, tránh trả tiền trước khi nhận hàng.";
		   MessageData messageData = MessageData.buileMessageInform(message.fuID, message.tuID, message.getpID(), contentInform, envelopeInform.getType(), Calendar.getInstance().getTimeInMillis());
			 
			SwapHubDAO swapHubDAO = new SwapHubDAO();
			swapHubDAO.saveMessageQueue(messageData);
			 
			User user = swapHubDAO.getUserBuyID(message.fuID);
			String content = contentInform;
			saveNotify(message.pID,message.tuID,message.fuID,content
						,user.avatar_url,"",4,"");
		 }
	}
	
	
	private void processOffer(Offer offer){
		SwapHubDAO swapHubDAO = new SwapHubDAO();
		if(offer.message_type==SwapType.OFFER_BUY||offer.message_type==SwapType.OFFER_SWAP){
			Offer offer2 = swapHubDAO.getOffer(offer.seller_id, offer.buyer_id, offer.product_id);
			if(offer2==null )
				swapHubDAO.saveOffer(offer);
			else {
				offer.id = offer2.id;
				swapHubDAO.updateOffer(offer);
			}
		}else if(offer.message_type==SwapType.OFFER_BUY_AGREE||offer.message_type==SwapType.OFFER_BUY_DENY||offer.message_type==SwapType.OFFER_BUY_CANCEL
				||offer.message_type==SwapType.OFFER_SWAP_AGREE||offer.message_type==SwapType.OFFER_SWAP_DENY||offer.message_type==SwapType.OFFER_SWAP_CANCEL){
			Offer offer2 = swapHubDAO.getOfferChangeTypeMessage(offer.seller_id, offer.buyer_id, offer.product_id);
			if(offer2==null )swapHubDAO.saveOffer(offer); else
				swapHubDAO.updateOffer(offer2.id,offer.message_type);
			
		}
		
		deleteCacheOffer(offer.seller_id, offer.buyer_id);
		// Put offer info  on product
		 if(offer.message_type==SwapType.OFFER_BUY_AGREE||offer.message_type==SwapType.OFFER_SWAP_AGREE){
			  
			  JSONObject data_offer = new JSONObject();
			  data_offer.put("pID", message.pID);
			  data_offer.put("seller_id", offer.seller_id);
			  data_offer.put("buyer_id", offer.buyer_id);
			  User buyUser = swapHubDAO.getUserBuyID(offer.buyer_id);
			  data_offer.put("buyer", buyUser.fullname);
			  User sellerUser = swapHubDAO.getUserBuyID(offer.seller_id);
			  data_offer.put("seller", sellerUser.fullname);
		      data_offer.put("price", new Double(message.price));
		      int type_offer = offer.message_type==SwapType.OFFER_BUY_AGREE ? 1: 2;
		      if(type_offer==2){
		    	  data_offer.put("p_swap_ids", message.p_swap_id);
		      }
		      data_offer.put("type_offer", type_offer);
		      
			 swapHubDAO.subQuantityAndOfferProduct(offer.product_id, data_offer.toString());
			 deleteCacheProduct(offer.product_id);
			 
			 if(offer.message_type==SwapType.OFFER_SWAP_AGREE){
				 String[] arrPWID = message.p_swap_id.split(",");
				 
				 for (String pwID : arrPWID) {
					 swapHubDAO.subQuantityAndOfferProduct(Integer.parseInt(pwID), data_offer.toString());
					 deleteCacheProduct(Integer.parseInt(pwID));
				}
			 }
			 
			 // Send Notify
			 swapHubDAO.pushNotifySold(offer.product_id, offer.buyer_id);
		 }
		// Put offer info  on product
		 if(offer.message_type==SwapType.OFFER_BUY_CANCEL||offer.message_type==SwapType.OFFER_SWAP_CANCEL){
			 swapHubDAO.addQuantityAndOfferProduct(offer.product_id, "");
			 deleteCacheProduct(offer.product_id);
			 
			 if(offer.message_type==SwapType.OFFER_SWAP_CANCEL){
				 String[] arrPWID = message.p_swap_id.split(",");
				 for (String pwID : arrPWID) {
					 swapHubDAO.addQuantityAndOfferProduct(Integer.parseInt(pwID), "");
					 deleteCacheProduct(Integer.parseInt(pwID));
				}
			 }
			 
		 }
	}      
	  
	private void deleteCacheProduct(int product_id){
		try {
			SwapHubDAO swapHubDAO = new SwapHubDAO();
			/*MemcachedClient jmemcache = new MemcachedClient(
					new InetSocketAddress("10.8.8.173", 11211));
			*/
			String address = "10.8.8.173:11211";
			MemcachedClient jmemcache = new MemcachedClient(new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Retry).build(), AddrUtil.getAddresses(address));
			
			Product product = swapHubDAO.getProductByID(product_id);
			String querykey =ChatUtil.md5("KEY.getProductSwap." + product.user_id); 
			jmemcache.delete(querykey);
			querykey =ChatUtil.md5("KEY.getProductDetail."+product_id); 
			jmemcache.delete(querykey);
			querykey =ChatUtil.md5("KEY.getProductSwapOfUser." + product.user_id); 
			jmemcache.delete(querykey);
			
			// $querykey =md5("KEY.getProductCate.".$cate_id.".".$page);      
			int i = 1;
			while(i<=10){
				querykey =ChatUtil.md5("KEY.getProductOfUser." + product.user_id+"."+i); 
				jmemcache.delete(querykey);
				i++;
			}
			
			i = 1;
			while(i<=100){
				querykey =ChatUtil.md5("KEY.getProductCate." + product.cate_id+"."+i); 
				jmemcache.delete(querykey);
				querykey =ChatUtil.md5("KEY.getProductCate." + product.cate_parent_id+"."+i); 
				jmemcache.delete(querykey);
				i++;
			}
			
			// $querykey =md5("KEY.getProductOfUser." .$user_id.".".$page);   
			logger.info("Delete Cache -->"+"KEY.getProductSwap." + product.user_id);
			logger.info("Delete Cache -->"+"KEY.getProductDetail." + product_id);
			logger.info("Delete Cache -->"+"KEY.getProductSwapOfUser." + product.user_id);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void deleteCacheOffer(int seller_id,int buyer_id){
			try {
				/*MemcachedClient jmemcache = new MemcachedClient(
						new InetSocketAddress("10.8.8.173", 11211));*/
				
				String address = "10.8.8.173:11211";
				MemcachedClient jmemcache = new MemcachedClient(new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Retry).build(), AddrUtil.getAddresses(address));
				String querykey =ChatUtil.md5("KEY.getMyOfferBuy." + seller_id); 
				jmemcache.delete(querykey);
				
				querykey =ChatUtil.md5("KEY.getMyOfferBuy."+buyer_id); 
				jmemcache.delete(querykey);
				
				querykey =ChatUtil.md5("KEY.getMyOfferSell."+seller_id); 
				jmemcache.delete(querykey);
				querykey =ChatUtil.md5("KEY.getMyOfferSell."+buyer_id); 
				jmemcache.delete(querykey);
				
				
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	
}