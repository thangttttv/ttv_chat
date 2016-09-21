package io.netty.example.securechat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;



public class IntegerHeaderFrameDecoder4 extends ByteToMessageDecoder {
		
	   private  ByteArrayOutputStream dataFile = null;
	
	   @Override
	   protected void decode(ChannelHandlerContext ctx,
	                           ByteBuf buf, List<Object> out) throws Exception {
		if (buf.readableBytes() < 6) {
		        return;
		}
		
		buf.markReaderIndex();
		Envelope message = new Envelope();
		// Version
		message.setVersion(Version.fromByte(buf.readByte()));
		// Type
		message.setType(Type.fromByte(buf.readByte())); 
		// Lengt
		int length = buf.readInt();
		
		/*if(message.getType()==Type.RESPONSE){
			 message.setPayload(buf.readBytes(buf.readableBytes()).array());
			 message.setLength(message.getPayload().length);
			
			 System.out.println("File Name:"+new String(message.getPayload(),StandardCharsets.UTF_8 ));
			 System.out.println("File Size:"+length);
			 out.add(message);
			 ctx.channel().pipeline().remove("decoder");
			 ctx.channel().pipeline().addLast("decoder", new IntegerHeaderFrameDecoder3(length));
			 
			
    		 return;
    	}else{ 
    				 if (buf.readableBytes() < length) {
    				        buf.resetReaderIndex();
    				        return;
    				  }
    				     
    				 message.setPayload(buf.readBytes(length).array());
    				 message.setLength(length);
    				 out.add(message);
    				 System.out.println("IntegerHeaderFrameDecoder2 payload:"+new String(message.getPayload(),StandardCharsets.UTF_8 ));
    	}
		 */
		
		if(message.getType()==Type.RESPONSE&&(dataFile==null||dataFile.size()<length)){
			if(dataFile==null)
			dataFile = new ByteArrayOutputStream();
			System.out.println("Receive da :"+buf.readableBytes());
			dataFile.write(buf.readBytes(buf.readableBytes()).array());
			if(dataFile.size()>=length){
				
				System.out.println("file da day:"+dataFile.size());
				FileOutputStream fileOut = new FileOutputStream(new File(
						"C:/Projects/Android/cmt2.jpg"));
				fileOut.write(dataFile.toByteArray());
				fileOut.close();
				dataFile = null;
			}
		}else{
			
			    
			 if (buf.readableBytes() < length) {
			        buf.resetReaderIndex();
			        return;
			  }
			     
			 message.setPayload(buf.readBytes(length).array());
			 message.setLength(message.getPayload().length);
			 out.add(message);
			 System.out.println("String payload:"+new String(message.getPayload(),StandardCharsets.UTF_8 ));
		}
	    
	 
	   }
	 }
