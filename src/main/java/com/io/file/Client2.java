package com.io.file;

import java.io.File;
import java.io.IOException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedWriteHandler;


public class Client2 {


    private Bootstrap bootstrap;

    private boolean connected;

    /**
     * Port number of the socket server.
     */
    private final int port;

    /**
     * Host name of the socket server.
     */
    private final String hostName;


    private NioEventLoopGroup nioEventLoopGroup;

    private ChannelFuture futureChannel;


    /**
     * Initialize the socket details
     * 
     */
    public Client2(final String hostName, final int port) {
        this.hostName = hostName;
        this.port = port;
        connected = false;
    }

    /**
     * Connects to the host and port.
     * 
     * 
     */
    public void connect() {

        this.bootstrap = new Bootstrap();
        nioEventLoopGroup = new NioEventLoopGroup();

        this.bootstrap.group(nioEventLoopGroup).channel(NioSocketChannel.class)
        .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast(new ChunkedWriteHandler());
            }
        });

        // Make the connection attempt.
        try {
            futureChannel = bootstrap.connect(hostName, port).sync();
            connected = true;       

        } catch (InterruptedException e) {

        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void close() {
        futureChannel.channel().closeFuture();
        nioEventLoopGroup.shutdownGracefully();
    }


    public void send() {
       // futureChannel.channel().writeAndFlush(file);
        
        File file  = new File("file1.zip");
        ChunkedFile chunkedFile;
        try {
            chunkedFile = new ChunkedFile(file);
            futureChannel.channel().writeAndFlush(chunkedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public static void main(String[] args) {
    	Client2 client2 = new Client2("localhost",1902);
    	client2.connect();
    	if(client2.connected) client2.send();
    	
	}
    

}
