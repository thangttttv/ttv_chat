package com.ttv.chat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.securechat.Version;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.ttv.bean.SwapEnvelope;
import com.ttv.bean.SwapType;
import com.ttv.util.FileUtil;

public class SwapDecoder extends ByteToMessageDecoder {
	
	   private  ByteArrayOutputStream dataFile = null;
	   private long fileLenght = 0;
	   private String fileHeader = "";
	
	   @Override
	   protected void decode(ChannelHandlerContext ctx,
	                           ByteBuf buf, List<Object> out) throws Exception {
		if (buf.readableBytes() < 14) {
		        return;
		}
		
		//buf.markReaderIndex();
		SwapEnvelope envelope = new SwapEnvelope();
		// Version
		envelope.setVersion(Version.fromByte(buf.readByte()));
		// Type
		envelope.setType(SwapType.fromByte(buf.readByte())); 
		// From User ID
		int fUID = buf.readInt();envelope.setfUID(fUID);
		// To User ID
		int tUID = buf.readInt();envelope.settUID(tUID);
		// Lenght
		int length = buf.readInt();
		
		System.out.println("Header:Type"+envelope.getType()+"-lenght:"+length);
		
		if(envelope.getType()==SwapType.FILE&&(dataFile==null||dataFile.size()<fileLenght)){
			if(dataFile==null){
				dataFile = new ByteArrayOutputStream();
				fileHeader = new String(buf.readBytes(length).array());
				JSONObject json = (JSONObject) JSONSerializer.toJSON(fileHeader);
				fileLenght = json.getLong("size");
				
				System.out.println("fileHeader:"+fileHeader);
				
			}else{
				// file data
				System.out.println("Lengt:"+length+"-"+buf.readableBytes());
				dataFile.write(buf.readBytes(buf.readableBytes()).array());
				System.out.println("Lengt:"+length+"-"+buf.readableBytes()+"- Total Lenght:"+dataFile.size()+"/"+fileLenght);
			}
			
			if(dataFile.size()>=fileLenght){
				String file_url = saveFile(tUID);
				dataFile = null;
				fileLenght = 0;
				
				envelope.setFile(file_url);
				envelope.setPayload(fileHeader.getBytes());
				envelope.setLength(fileHeader.getBytes().length);
				out.add(envelope);
				fileHeader = "";
			}
		}else{
			
			    
			 if (buf.readableBytes() < length) {
			        buf.resetReaderIndex();
			        return;
			  }
			     
			 envelope.setPayload(buf.readBytes(length).array());
			 envelope.setLength(envelope.getPayload().length);
			 System.out.println("Payload:"+new String(envelope.getPayload()));
			 out.add(envelope);
		}
	   }
	   
	   private String saveFile(int fUID){
		    SimpleDateFormat formatter = new SimpleDateFormat("/yyyy/MMdd/");
			String date= formatter.format(Calendar.getInstance().getTime());
			String file_folder = ChatConstant.PATH_IMAGE_ROOT+date;
			
			String file_name = fUID+"_"+Calendar.getInstance().getTimeInMillis()+".jpg";
			String file_path  = file_folder+file_name;
			System.out.println(file_name);
			String file_url =ChatConstant.DOMAIN_WEB + ChatConstant.PATH_IMAGE_WEB_ROOT+date+file_name;
			System.out.println(file_path);
			try {
				FileUtil.mkdirs(file_folder);
				FileOutputStream fileOutputStream = new FileOutputStream(file_path);
				fileOutputStream.write(dataFile.toByteArray());
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(file_url);
			return file_url;
	   }
	   
}