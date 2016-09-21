package com.ttv.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.example.securechat.Version;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

import net.sf.json.JSONObject;

import com.ttv.bean.SwapEnvelope;
import com.ttv.bean.SwapType;

public class SwapChatClient {

    static final String HOST = System.getProperty("host", "localhost");
    static final int PORT = Integer.parseInt(System.getProperty("port", "1901"));

    public static void main(String[] args) throws Exception {
        // Configure SSL.
        final SslContext sslCtx = SslContextBuilder.forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.SO_SNDBUF, 1045678)
			 .option(ChannelOption.SO_RCVBUF, 1045678)
             .handler(new SwapChatClientInitializer(sslCtx));

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
                int appID = 1;
                int fUID = 1;
                int tUID = 2;
                String username = "thangtt";
                SwapEnvelope envelope = new SwapEnvelope(Version.VERSION1, SwapType.TEXT,appID,fUID,tUID,(stringUtf8).getBytes().length,(stringUtf8).getBytes());
                if(chatContent.contains("login"))
                {
                	JSONObject formDetailsJson = new JSONObject();
          	        formDetailsJson.put("app_client_id", appID);
          	        formDetailsJson.put("id", fUID);
          	        formDetailsJson.put("username",username);
          	        String strLogin=formDetailsJson.toString();
          	        System.out.println(strLogin);
                	envelope = new SwapEnvelope(Version.VERSION1, SwapType.LOGIN,appID,fUID,0,strLogin.getBytes().length,strLogin.getBytes());
                	lastWriteFuture = ch.writeAndFlush(envelope);
                }
                else if(chatContent.contains("img")) 
                	SwapChatClient.sendFile(appID,fUID,tUID,ch);
                else {
                	JSONObject formDetailsJson = new JSONObject();
          	        formDetailsJson.put("pID", 10);
          	        formDetailsJson.put("content", chatContent);
          	        formDetailsJson.put("time",Calendar.getInstance().getTimeInMillis());
          	        String jsonChat=formDetailsJson.toString();
          	        envelope = new SwapEnvelope(Version.VERSION1, SwapType.TEXT,appID,fUID,tUID,(jsonChat).getBytes().length,(jsonChat).getBytes());
                	lastWriteFuture = ch.writeAndFlush(envelope);
                }
                
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
    
    
    public static void sendFile(int appID,int fUID,int tUID,Channel ch ) throws IOException {
		File file = new File("C:/Projects/Android/hinhnen.jpg");
		FileInputStream fileIn = new FileInputStream(file);
		int bytes = 0;
		byte[] buffer = new byte[8192];
		int len;
		  JSONObject formDetailsJson = new JSONObject();
	        formDetailsJson.put("pID", 10);
	        formDetailsJson.put("content", "");
	        formDetailsJson.put("time",Calendar.getInstance().getTimeInMillis());
	        formDetailsJson.put("file_name",file.getName());
	        formDetailsJson.put("size",file.length());
	        String payloadHeaderFile=formDetailsJson.toString();
	        
        
		SwapEnvelope envelope = new SwapEnvelope(Version.VERSION1, SwapType.FILE,appID,fUID,tUID,payloadHeaderFile.getBytes().length, payloadHeaderFile.getBytes());
		ch.writeAndFlush(envelope);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while ((len = fileIn.read(buffer)) > 0) {
			envelope = new SwapEnvelope(Version.VERSION1, SwapType.FILE,appID,fUID,tUID,len, buffer);
			System.out.println("Transfer completed, " + len + " bytes sent");
			ch.pipeline().remove("encoder");
			ch.pipeline().addLast("encoder",new SwapEncoderF());
			ch.writeAndFlush(envelope);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bytes += len;
			int sizebb = (int) ((file.length()-bytes)<8192?(file.length()-bytes):8192);
			System.out.println("sizebb" + sizebb );
			buffer = new byte[sizebb];
		}
		ch.pipeline().remove("encoder");
		ch.pipeline().addLast("encoder",new SwapEncoder());
		System.out.println("Transfer completed, " + bytes + " bytes sent");
		fileIn.close();
	}
}