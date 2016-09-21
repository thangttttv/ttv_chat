package com.io.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FileRegion;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedStream;

public class Client {
	
	public ChannelFuture futureChannel;
	
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
	    
	 
	public static void main(String[] args) throws SSLException, InterruptedException {
		final SslContext sslCtx = SslContextBuilder.forClient()
	            .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
        b.group(group)
         .channel(NioSocketChannel.class)
         .handler(new ClientInitializer(sslCtx));

        Channel ch = b.connect("localhost", 1902).sync().channel();
        
        
        RandomAccessFile raf = null;
        long length = -1;
        try {
            raf = new RandomAccessFile("C:/Projects/Android/Netty_Chat_Client.jar", "r");
            length = raf.length();
        } catch (Exception e) {
            ch.writeAndFlush("ERR: " + e.getClass().getSimpleName() + ": " + e.getMessage() + '\n');
            return;
        } finally {
            if (length < 0 && raf != null) {
                try {
					raf.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        
        
        File file  = new File("C:/Projects/Android/Netty_Chat_Client.jar");
        ChunkedFile chunkedFile;
        try {
            chunkedFile = new  ChunkedFile(file,  8192);
            //ch.writeAndFlush(chunkedFile);
          //  ch.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
            //ch.write(new DefaultFileRegion(raf.getChannel(), 0, length));
            //ChannelFuture writeFuture = ch.write(new ChunkedFile(raf, 0, length, 8192));
           // writeFuture = ch.write(new ChunkedStream(new FileInputStream(file))); // Use a ChunkedStream instead of a ChunkedFile
           // transfer(ch,  file) ;
            
            FileInputStream in = new FileInputStream(file); 
            FileRegion region = new DefaultFileRegion(
            in.getChannel(), 0, file.length()); 
            ch.writeAndFlush(region)
            .addListener(new ChannelFutureListener() { 
            @Override
            public void operationComplete(ChannelFuture future)
            throws Exception {
            if (!future.isSuccess()) {
            Throwable cause = future.cause(); 
            // Do something
            }
            }
            });
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*ChannelFuture lastWriteFuture = null;

         if (lastWriteFuture != null) {
            lastWriteFuture.sync();
        }*/
	}
	
	 public static void transfer(Channel channel, File file) throws FileNotFoundException {
	        FileInputStream in = new FileInputStream(file);
	        FileRegion region = new DefaultFileRegion(in.getChannel(), 0, file.length());

	        channel.writeAndFlush(region).addListener(new ChannelFutureListener() {
	            @Override
	            public void operationComplete(ChannelFuture future) throws Exception {
	                if (!future.isSuccess()) {
	                    Throwable cause = future.cause();
	                    // Do something
	                }
	            }
	        });
	    }
	
}	
