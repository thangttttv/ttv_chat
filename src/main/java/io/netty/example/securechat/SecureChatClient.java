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

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Simple SSL chat client modified from {@link TelnetClient}.
 */
public final class SecureChatClient {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8992"));

    public static void main(String[] args) throws Exception {
        // Configure SSL.
        final SslContext sslCtx = SslContextBuilder.forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new SecureChatClientInitializer(sslCtx));

            // Start the connection attempt.
            Channel ch = b.connect(HOST, PORT).sync().channel();

            // Read commands from the stdin.
            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (;;) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }

                // Sends the received line to the server.
                String chatContent = line + "\r\n";
                String stringUtf8 = new String(chatContent.getBytes(),StandardCharsets.UTF_8 );
                Envelope envelope = new Envelope(Version.VERSION1, Type.REQUEST, (stringUtf8).getBytes(),(stringUtf8).getBytes().length);
               
                
                //lastWriteFuture = ch.writeAndFlush(line + "\r\n");
                if(!chatContent.contains("img"))
                	lastWriteFuture = ch.writeAndFlush(envelope);
                else
                	SecureChatClient.sendFile(ch);
                
                // If user typed the 'bye' command, wait until the server closes
                // the connection.
                if ("bye".equals(line.toLowerCase())) {
                    ch.closeFuture().sync();
                    break;
                }
            }

            // Wait until all messages are flushed before closing the channel.
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }
        } finally {
            // The connection is closed automatically on shutdown.
            group.shutdownGracefully();
        }
    }
    
    
    public static void sendFile(Channel ch ) throws IOException {
		File file = new File("C:/Projects/Android/hoa1.jpg");
		FileInputStream fileIn = new FileInputStream(file);
		int bytes = 0;
		byte[] buffer = new byte[8192];
		int len;
		
		Envelope envelope = new Envelope(Version.VERSION1, Type.RESPONSE, file.getName().getBytes(),(int)file.length());
		ch.writeAndFlush(envelope);
		
		while ((len = fileIn.read(buffer)) > 0) {
			envelope = new Envelope(Version.VERSION1, Type.RESPONSE, buffer,len);
			System.out.println("Transfer completed, " + len + " bytes sent");
			ch.writeAndFlush(envelope);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bytes += len;
		}
		System.out.println("Transfer completed, " + bytes + " bytes sent");
		fileIn.close();
	}
}
