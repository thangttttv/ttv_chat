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

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.ttv.bean.SwapEnvelope;
import com.ttv.bean.SwapType;
import com.ttv.util.FileUtil;

public class SwapDecoder3 extends ByteToMessageDecoder {
	
	   private  ByteArrayOutputStream dataFile = null;
	   private long fileLenght = 0;
	   private String fileHeader = "";
	   private boolean isFileData = false;
	   private SwapEnvelope envelope = null;
	   private long totalDataFileReceive = 0;
	   final static Logger logger = Logger.getLogger(SwapDecoder3.class);
	   
	   @Override
	   protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
		/*if (buf.readableBytes() < 14) {
		        return;
		}*/
		logger.info("Total bytes receive:"+buf.readableBytes());

		if(!isFileData){
			logger.info("- Begin Decoder:-----------------------------");
			//buf.markReaderIndex();
			envelope = new SwapEnvelope();
			// Version
			envelope.setVersion(Version.fromByte(buf.readByte()));
			logger.info("Version:"+envelope.getVersion());
			// Type
			envelope.setType(SwapType.fromByte(buf.readByte())); 
			logger.info("Type:"+envelope.getType());
			// From User ID
			int fUID = buf.readInt();envelope.setfUID(fUID);
			logger.info("FUID:"+envelope.getfUID());
			// To User ID
			int tUID = buf.readInt();envelope.settUID(tUID);
			logger.info("TUID:"+envelope.gettUID());
			// Lenght
			int length = buf.readInt();
			logger.info("Length:"+length);
			
			if(envelope.getType()==SwapType.FILE){
				dataFile = new ByteArrayOutputStream();
				fileHeader = new String(buf.readBytes(length).array());
				JSONObject json = (JSONObject) JSONSerializer.toJSON(fileHeader);
				fileLenght = json.getLong("size");
				isFileData = true;
				logger.info("FileHeader:"+fileHeader);
			}else{
				 if (buf.readableBytes() < length) {
				        buf.resetReaderIndex();
				        return;
				  }
				     
				 envelope.setPayload(buf.readBytes(buf.readableBytes()).array());
				 envelope.setLength(envelope.getPayload().length);
				 logger.info("Payload:"+new String(envelope.getPayload()));
				 out.add(envelope);
				 logger.info("- End Decoder:-----------------------------");
			}
		}else{
			totalDataFileReceive +=buf.readableBytes();
			dataFile.write(buf.readBytes(buf.readableBytes()).array());
			logger.info("- Total Lenght:"+dataFile.size()+"/"+fileLenght);
			logger.info("- Total totalDataFileReceive:"+totalDataFileReceive);

			if(dataFile.size()>=fileLenght){
				String file_url = saveFile(envelope.getfUID());
				dataFile = null;
				fileLenght = 0;
				isFileData = false;
				totalDataFileReceive = 0;
				
				envelope.setFile(file_url);
				envelope.setPayload(fileHeader.getBytes());
				envelope.setLength(fileHeader.getBytes().length);
				out.add(envelope);
				logger.info("- End Decoder:-----------------------------");
				fileHeader = "";
			}
			
		}
	   }
	   
	   private String saveFile(int fUID){
		    SimpleDateFormat formatter = new SimpleDateFormat("/yyyy/MMdd/");
			String date= formatter.format(Calendar.getInstance().getTime());
			String file_folder = ChatConstant.PATH_IMAGE_ROOT+date;
			
			String file_name = fUID+"_"+Calendar.getInstance().getTimeInMillis()+".jpg";
			String file_path  = file_folder+file_name;
			
			String file_url =ChatConstant.DOMAIN_WEB + ChatConstant.PATH_IMAGE_WEB_ROOT+date+file_name;
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
			return file_url;
	   }
	   
}