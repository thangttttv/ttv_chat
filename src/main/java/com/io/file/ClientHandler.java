package com.io.file;



import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.example.securechat.Envelope;

public class ClientHandler extends SimpleChannelInboundHandler<Object> {
	public void messageReceived(ChannelHandlerContext ctx, String msg) {
	    System.err.println(msg+" THIS IS PRINTED");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	    cause.printStackTrace();
	    ctx.close();
	}

	protected void channelRead0(ChannelHandlerContext arg0, String arg1)
	        throws Exception {
	     System.err.println(arg1);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
	        throws Exception {

	 //  Channel chanbuf = ctx.channel().read();
	    ByteBuf chan = ctx.read().alloc().buffer();
	    byte[] bytes = new byte[chan.readableBytes()];
	    chan.readBytes(bytes);
	     FileOutputStream out = new FileOutputStream("C:/Projects/Android/vtc_swaphub2.sql");
	     ObjectOutputStream oout = new ObjectOutputStream(out);
	     System.out.println("HEY"+chan.readableBytes());

	     oout.write(bytes, 0, bytes.length);

	     oout.close();
	    }

}
