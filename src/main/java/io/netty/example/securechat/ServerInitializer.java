package io.netty.example.securechat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

	  public ServerInitializer(SslContext sslCtx) {
		// TODO Auto-generated constructor stub
	}

	@Override
	    protected void initChannel(SocketChannel ch) throws Exception {
	        ChannelPipeline p = ch.pipeline();

	        p.addLast("encoder", new ObjectEncoder());
	        p.addLast("decoder",
	            new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
	        p.addLast("chunkedWriteHandler", new ChunkedWriteHandler());// added
	      //  p.addLast("protocolhead", new ProtocolHeadServerHandler());
	        p.addLast("filerequestserverhandler", new FileRequestServerHandler());
	    }

}
