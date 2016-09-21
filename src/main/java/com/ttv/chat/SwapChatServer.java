package com.ttv.chat;

import java.util.Calendar;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.ttv.dao.DBConfig;
import com.ttv.dao.SwapHubDAO;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

//https://github.com/mitallast/netty-queue/blob/master/src/main/java/org/mitallast/queue/common/netty/NettyServer.java

public class SwapChatServer {
	static int PORT = DBConfig.port;
	final static Logger logger = Logger.getLogger(SwapChatServer.class);

	public static void main(String[] args) throws Exception {
		DBConfig.loadProperties();
		BasicConfigurator.configure();
		PORT = DBConfig.port;
		SelfSignedCertificate ssc = new SelfSignedCertificate();
		SslContext sslCtx = SslContextBuilder.forServer(ssc.certificate(),
				ssc.privateKey()).build();

		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					//.childOption(ChannelOption.SO_SNDBUF, 1045678)
					//.childOption(ChannelOption.SO_RCVBUF, 1045678)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new SwapChatServerInitializer(sslCtx));
			logger.info("Server Open Connect On Port:"+PORT);
			b.bind(PORT).sync().channel().closeFuture().sync();
			
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				SwapHubDAO swapHubDAO = new SwapHubDAO();
				swapHubDAO.saveSMSAlert();
				logger.info("Server Stotp At:"+Calendar.getInstance().getTime().toString());
			}
		});
	}
}
