package com.ttv.chat;

import java.util.List;

import com.ttv.bean.MessageData;
import com.ttv.bean.SwapEnvelope;
import com.ttv.bean.SwapType;
import com.ttv.dao.SwapHubDAO;
import com.ttv.process.SwapEnvelopeFactory;

import io.netty.channel.Channel;

public class SwapHandlerMessageQueue extends Thread {
	private int user_id;
	private Channel channel;
	
	public SwapHandlerMessageQueue(int user_id,Channel channel){
		this.user_id = user_id;
		this.channel = channel;
	}
	
	public void run() {
		SwapHubDAO swapHubDAO = new SwapHubDAO();
		List<MessageData> listOffLive =  swapHubDAO.getListMessageOffLive(user_id);
		for (MessageData messageData : listOffLive) {
				if(messageData.getType()==SwapType.TEXT){
					SwapEnvelope envelope = SwapEnvelopeFactory.builderEnvelopeText(messageData.app_client_id, messageData.fuID, messageData.tuID, messageData.pID, messageData.content, messageData.time);
					channel.writeAndFlush(envelope);
				}else if(messageData.getType()==SwapType.FILE){
					SwapEnvelope envelope = SwapEnvelopeFactory.builderEnvelopeFile(messageData.app_client_id, messageData.fuID, messageData.tuID, messageData.pID, messageData.content, messageData.file, messageData.file_size, messageData.time);
					channel.writeAndFlush(envelope);
				}else if(messageData.getType()==SwapType.OFFER_BUY||
						messageData.getType()==SwapType.OFFER_BUY_AGREE||messageData.getType()==SwapType.OFFER_BUY_CANCEL||
						messageData.getType()==SwapType.OFFER_BUY_DENY){
						SwapEnvelope envelope = SwapEnvelopeFactory.builderEnvelopeBuy(messageData.app_client_id, messageData.fuID, messageData.tuID, messageData.pID, messageData.content, messageData.type, messageData.price, messageData.quantity, messageData.time);
						channel.writeAndFlush(envelope);
				}else if(messageData.getType()==SwapType.OFFER_SWAP||
						messageData.getType()==SwapType.OFFER_SWAP_AGREE||messageData.getType()==SwapType.OFFER_BUY_CANCEL||
						messageData.getType()==SwapType.OFFER_BUY_DENY){
						SwapEnvelope envelope = SwapEnvelopeFactory.builderMessageSwap(messageData.app_client_id, messageData.fuID, messageData.tuID, messageData.pID, messageData.content, messageData.type, messageData.price, messageData.quantity, messageData.p_swap_id, messageData.time);
						channel.writeAndFlush(envelope);
				}
				swapHubDAO.deleteMessageQueue(messageData.id);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
