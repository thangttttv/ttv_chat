package org.dean.example.nettychat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Dean on 2014/6/25.
 */
public class ChatClientHandler extends SimpleChannelInboundHandler<String> {

    public void messageReceived(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println(s);
    }

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, String arg1)
			throws Exception {
		System.out.println(arg1);
		
	}

}
