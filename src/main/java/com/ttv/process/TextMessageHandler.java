package com.ttv.process;

import org.apache.log4j.Logger;

import com.ttv.bean.MessageData;
import com.ttv.bean.SwapEnvelope;



public class TextMessageHandler extends MessageHandler {
	
	final static Logger logger = Logger.getLogger(MessageHandler.class);
	
	public TextMessageHandler(SwapEnvelope envelope,MessageData message, Session session) {
		super(envelope,message, session);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void processMessage(){
		// Save Message
		saveMessage();
		// Put Message Repy
		logger.info("Reply to user :"+message.app_client_id+"-"+message.fuID);
		SwapEnvelope envelope = SwapEnvelopeFactory.builderEnvelopeReply(message.app_client_id,message.fuID, message.tuID, message.pID, message.time);
		session.getChannel().writeAndFlush(envelope);
		// Send Message To UserF
		sendMessageFUser();
		// Send Message To UserT
		sendMessageTUser();
		
	}

}
