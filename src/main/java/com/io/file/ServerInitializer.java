package com.io.file;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.example.securechat.Encoder;
import io.netty.example.securechat.IntegerHeaderFrameDecoder2;
import io.netty.example.securechat.SecureChatClient;
import io.netty.example.securechat.SecureChatClientHandler;
import io.netty.example.securechat.SecureChatClientInitializer;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public ServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
    	  ChannelPipeline pipeline = ch.pipeline();

    	    pipeline.addLast(sslCtx.newHandler(ch.alloc()));

    	    //pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
    	  //  pipeline.addLast(new StringDecoder());
    	   // pipeline.addLast(new StringEncoder());


    	    pipeline.addLast(new ServerHandler());
    }
}