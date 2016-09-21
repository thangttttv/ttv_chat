package io.netty.example.securechat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

public class IntegerHeaderFrameDecoder3 extends ReplayingDecoder<Void> {

	
	public int totalLen = 0;
	public ByteArrayOutputStream dataFile = null;
	public IntegerHeaderFrameDecoder3(int fileLen){
		totalLen = fileLen;
	}
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
			// TODO Auto-generated method stub
			/* byte[] data =  in.readBytes(in.readInt()).array();
			 Envelope envelope = new Envelope(Version.VERSION1, Type.REQUEST, data,data.length);
		     out.add(envelope);
		     System.out.println("String payload:"+new String(envelope.getPayload(),StandardCharsets.UTF_8 ));*/
		
			// Version
			Version.fromByte(in.readByte());
			// Type
			Type.fromByte(in.readByte()); 
			// Length
			int length = in.readInt();
			
			if(dataFile==null)
			dataFile = new ByteArrayOutputStream();
			
			System.out.println("IntegerHeaderFrameDecoder3 Receive size :"+length);
			dataFile.write(in.readBytes(length).array());
			
			if(dataFile.size()>=totalLen){
				
				System.out.println("file da day:"+dataFile.size());
				FileOutputStream fileOut = new FileOutputStream(new File(
						"C:/Projects/Android/cmt2"+Calendar.getInstance().getTimeInMillis()+".jpg"));
				fileOut.write(dataFile.toByteArray());
				fileOut.close();
				dataFile = null;
				
				//ctx.channel().pipeline().remove("decoder3");
				//ctx.channel().pipeline().addLast("decoder", new IntegerHeaderFrameDecoder2());
				
				
				Envelope envelope = new Envelope(Version.VERSION1, Type.RESPONSE, "Nhan xong file".getBytes(),"Nhan xong file".length());
				out.add(envelope);
				 
			}
		
	}
}
