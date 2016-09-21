package com.io.file;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;

public class Server2 {

    private EventLoopGroup eventLoopGroup;
    private EventLoopGroup slaveEventLoopGroup;
    private int packagePort;
    private ChannelHandler fileReqHandler;


    public void start() {
        eventLoopGroup = new NioEventLoopGroup();
        slaveEventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap server = null;

        server = new ServerBootstrap();
        server.group(eventLoopGroup, slaveEventLoopGroup)
        .channel(NioServerSocketChannel.class) // (3)
        .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ChunkedWriteHandler());
                ch.pipeline().addLast(new FileChunkReqWriteHandler());
            }
        })
        .option(ChannelOption.SO_BACKLOG, 128)          // (5)
        .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

        try {
            server.bind(getPackagePort()).sync();
        } catch (InterruptedException e) {
           
        }        
    }

    public void shutdown()  {
        eventLoopGroup.shutdownGracefully();
        slaveEventLoopGroup.shutdownGracefully();        
    }

    public int getPackagePort() {
        return packagePort;
    }

    public void setPackagePort(int packagePort) {
        this.packagePort = packagePort;
    }

    public ChannelHandler getFileReqHandler() {
        return fileReqHandler;
    }

    public void setFileReqHandler(ChannelHandler fileReqHandler) {
        this.fileReqHandler = fileReqHandler;
    }

    
    public static void main(String[] args) {
    	Server2 server2 = new Server2();
    	server2.setPackagePort(1902);
    	server2.start();
    	
	}
}
