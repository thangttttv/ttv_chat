package com.io.file;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.example.securechat.ServerInitializer;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class Server {
	public static void main(String[] args) throws SSLException, CertificateException {
		SelfSignedCertificate ssc = new SelfSignedCertificate();
	    SslContext sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());

	    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	    EventLoopGroup workerGroup = new NioEventLoopGroup();
	    try {

	        ServerBootstrap b = new ServerBootstrap();
	        b.group(bossGroup, workerGroup)
	         .channel(NioServerSocketChannel.class)
	         .handler(new LoggingHandler(LogLevel.INFO))
	         .childHandler(new ServerInitializer(sslCtx));

	        b.bind(1902).sync().channel().closeFuture().sync();

	    } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        bossGroup.shutdownGracefully();
	        workerGroup.shutdownGracefully();
	    }
	}
}
