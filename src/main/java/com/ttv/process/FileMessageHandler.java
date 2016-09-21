package com.ttv.process;

import com.ttv.bean.MessageData;
import com.ttv.bean.SwapEnvelope;

public class FileMessageHandler extends MessageHandler {

	public FileMessageHandler(SwapEnvelope envelope,MessageData message,Session session) {
		super(envelope,message,session);
	}
	
	@Override
	protected void sendMessageFUser(){
		String app_clien_ids = AppClientManager.getInstall().getAppClientIDs(message.fuID);
		String[] arrAppClient = app_clien_ids.split(",");
	  	int i = 0;
	  		
		 while (i<arrAppClient.length) {
			 	 Session session = SessionManager.getInstall().getSession(Integer.parseInt(arrAppClient[i]));
				 session.sendMessage(envelope);
				 i++;
			}
	}
	
	@Override
	public void processMessage(){
		// save file
		// Save Message
		saveMessage();
		// Send Message To UserF
		sendMessageFUser();
		// Send Message To UserT
		sendMessageTUser();
	}
}
