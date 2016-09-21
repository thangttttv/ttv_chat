package com.ttv.chat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;

public class SwapChatServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public SwapChatServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // Add SSL handler first to encrypt and decrypt everything.
        // In this example, we use a bogus certificate in the server side
        // and accept any invalid certificates in the client side.
        // You will need something more complicated to identify both
        // and server in the real world.
        //pipeline.addLast("frameDecoder", new DelimiterBasedFrameDecoder(80960,Delimiters.nulDelimiter()));
        //pipeline.addLast(sslCtx.newHandler(ch.alloc()));
       // pipeline.channel().config().setOption(ChannelOption.SO_RCVBUF,10000);
        pipeline.addLast("encoder", new SwapEncoder());
        pipeline.addLast("decoder", new SwapDecoder3());
        
        // and then business logic.
        pipeline.addLast("idleStateHandler", new IdleStateHandler(60, 30, 0));
        pipeline.addLast("myHandler", new MyHandler());
        pipeline.addLast(new ServerHandshakeHandler(15000));
        
    }
}
