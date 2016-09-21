package com.ttv.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

import com.ttv.bean.SwapEnvelope;

public class SwapChatClientHandler extends SimpleChannelInboundHandler<SwapEnvelope> {

	@Override
	public void channelRead0(ChannelHandlerContext ctx, SwapEnvelope msg)
			throws Exception {
		//
		String strMsg = new String(msg.getPayload(), Charset.forName("UTF-8"));
		System.err.println("ND-->"+ msg.getType() + strMsg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
