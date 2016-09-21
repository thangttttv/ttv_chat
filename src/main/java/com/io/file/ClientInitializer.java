package com.io.file;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.example.securechat.Encoder;
import io.netty.example.securechat.IntegerHeaderFrameDecoder2;
import io.netty.example.securechat.SecureChatClient;
import io.netty.example.securechat.SecureChatClientHandler;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public ClientInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
    	 ChannelPipeline pipeline = ch.pipeline();

    	    pipeline.addLast(sslCtx.newHandler(ch.alloc(), "localhost", 1902));

    	    //pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
    	   // pipeline.addLast(new StringDecoder());
    	    //pipeline.addLast(new StringEncoder());
    	   // pipeline.addLast(new ChunkedWriteHandler());
    	    pipeline.addLast(new ChunkedWriteHandler());
    	    pipeline.addLast(new ClientHandler());
    }
}