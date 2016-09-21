package com.io.file;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.nio.channels.FileChannel;

public class ServerHandler extends SimpleChannelInboundHandler {
	static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
	    // Once session is secured, send a greeting and register the channel to the global channel
	    // list so the channel received the messages from others.
	    ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
	            new GenericFutureListener<Future<Channel>>() {
	                private RandomAccessFile toFile;

					@Override
	                public void operationComplete(Future<Channel> future) throws Exception {

	                toFile = new RandomAccessFile("C:/Projects/Android/vtc_swaphub.sql", "rw");
	                    toFile.length();
	                if(future.isSuccess())
	                    {
	                        FileChannel toChannel = toFile.getChannel();
	                        for (Channel c: channels) {
	                        if(c != ctx.channel())
	                        {
	                        c.writeAndFlush(new DefaultFileRegion(toChannel, 0, toFile.length()));
	                        }
	                        }
	                    System.err.print("HIoperationComplete");    
	                    }
	                    ctx.writeAndFlush(
	                            "Welcome to " + InetAddress.getLocalHost().getHostName() + " secure chat service!\n");
	                    ctx.writeAndFlush(
	                            "Your session is protected by " + ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite() +
	                            " cipher suite.\n");

	                    channels.add(ctx.channel());
	                }
	            });
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
