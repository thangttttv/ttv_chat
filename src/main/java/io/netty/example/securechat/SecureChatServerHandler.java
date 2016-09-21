/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.securechat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.nio.charset.Charset;

/**
 * Handles a server-side channel.
 */
public class SecureChatServerHandler extends SimpleChannelInboundHandler<Envelope> {

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        // Once session is secured, send a greeting and register the channel to the global channel
        // list so the channel received the messages from others.
        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                new GenericFutureListener<Future<Channel>>() {
                    @Override
                    public void operationComplete(Future<Channel> future) throws Exception {
                      /*  ctx.writeAndFlush(
                                "Welcome to " + InetAddress.getLocalHost().getHostName() + " secure chat service!\n");
                        ctx.writeAndFlush(
                                "Your session is protected by " +
                                        ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite() +
                                        " cipher suite.\n");*/
                    	System.out.println(
                                "Welcome to " + InetAddress.getLocalHost().getHostName() + " secure chat service!\n");
                        channels.add(ctx.channel());
                    }
        });
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Envelope msg) throws Exception {
        // Send the received message to all channels but the current one.
    	 String strMsg = new String(msg.getPayload());
    	 System.out.println("channelRead0:"+strMsg);
    	
    	 
        for (Channel c: channels) {
            if (c != ctx.channel()) {
                //c.writeAndFlush("[" + ctx.channel().remoteAddress() + "] " + msg + '\n');
            	String out = ("[" + ctx.channel().remoteAddress() + "] " + strMsg + '\n');
            	msg.setPayload(out.getBytes());
            	msg.setLength(out.getBytes().length);
            	c.writeAndFlush( msg);
            	Thread.sleep(10);
            	System.out.println(out);
            } else {
              //  c.writeAndFlush("[you] " + msg + '\n');
            	String out = ("[you] " + strMsg + '\n');
            	msg.setPayload(out.getBytes());
            	msg.setLength(out.getBytes().length);
            	c.writeAndFlush( msg);
            	Thread.sleep(10);
            	System.out.println(out);
            }
        }

        // Close the connection if the client has sent 'bye'.
       
        if ("bye".equals(strMsg.toLowerCase())) {
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
    
  /*  @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg1) throws Exception {
    	System.out.println("dao");
    	System.out.println(msg1.toString());
    }*/
}
