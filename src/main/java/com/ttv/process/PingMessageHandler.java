package com.ttv.process;

import com.ttv.bean.MessageData;
import com.ttv.bean.SwapEnvelope;

public class PingMessageHandler  extends MessageHandler {

	public PingMessageHandler(SwapEnvelope envelope,MessageData message, Session session) {
		super(envelope,message, session);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void processMessage(){
		// Put Message Repy
		SwapEnvelope envelope = SwapEnvelopeFactory.builderEnvelopeReply(message.app_client_id,message.fuID, message.tuID, message.pID, message.time);
		session.getChannel().writeAndFlush(envelope);
	}

}
