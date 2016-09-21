package com.ttv.process;

import com.ttv.bean.MessageData;
import com.ttv.bean.SwapEnvelope;
import com.ttv.bean.User;
import com.ttv.dao.SwapHubDAO;

public abstract class MessageHandler {
	MessageData message = null;
	SwapEnvelope envelope = null;
	protected Session session;
	//private final  Logger logger = Logger.getLogger(MessageHandler.class);
	
	public MessageHandler(SwapEnvelope envelope,MessageData message,Session session){
		this.envelope = envelope;
		this.message = message;
		this.session = session;
	}
	
	protected void saveMessage()
	{
		SwapHubDAO swapHubDAO = new SwapHubDAO();
		swapHubDAO.saveMessage(message);
	}
	
	protected void saveMessageQueue()
	{
		SwapHubDAO swapHubDAO = new SwapHubDAO();
		swapHubDAO.saveMessageQueue(message);
	}
	
	protected void saveNotify(int object_id,int to_user,int from_user,String content
			,String icon,String url,int object_type,String create_user)
	{
		SwapHubDAO swapHubDAO = new SwapHubDAO();
		int notify_id = swapHubDAO.checkNotify(object_id, object_type, to_user,from_user);
		if(notify_id==0)
			swapHubDAO.saveNotify( object_id, to_user, from_user, content
				, icon, url, object_type, create_user);
		else 
			swapHubDAO.updateNotify(notify_id, content);
	}
	
	protected void sendMessageFUser(){
		String app_clien_ids = AppClientManager.getInstall().getAppClientIDs(message.fuID);
		String[] arrAppClient = null;
		if(app_clien_ids!=null&&!"".equalsIgnoreCase(app_clien_ids))
		 arrAppClient = app_clien_ids.split(",");
	  	
		int i = 0;
	  	if(arrAppClient!=null)
		 while (i<arrAppClient.length) {
			 Session session2 = SessionManager.getInstall().getSession(Integer.parseInt(arrAppClient[i]));
				 if(session2!=null&&session.getApp_client_id()!=Integer.parseInt(arrAppClient[i])){
					 session2.sendMessage(envelope);
				 }
				 i++;
			}
	  
	}
	
	protected void sendMessageTUser(){
		String app_clien_ids = AppClientManager.getInstall().getAppClientIDs(message.tuID);
		String[] arrAppClient = null;
		if(app_clien_ids!=null&&!"".equalsIgnoreCase(app_clien_ids))
		 arrAppClient = app_clien_ids.split(",");
		
		
	  	int i = 0;int kq = 0;
	  	if(arrAppClient!=null)
		while (i<arrAppClient.length) {
			 Session session = SessionManager.getInstall().getSession(Integer.parseInt(arrAppClient[i]));
				 if(session!=null){
					 session.sendMessage(envelope);
					 kq++;
				 }
				 i++;
		}
	   
	  // Save QUEUE
	   if(kq==0) {
			 saveMessageQueue();
			 SwapHubDAO swapHubDAO = new SwapHubDAO();
			 User user = swapHubDAO.getUserBuyID(message.fuID);
			 String content = user.fullname+" gửi tin nhắn: "+message.content;
			 saveNotify(message.pID,message.tuID,message.fuID,content
						,user.avatar_url,"",4,"");
		 }
	}

	public void processMessage(){
		
	}
}