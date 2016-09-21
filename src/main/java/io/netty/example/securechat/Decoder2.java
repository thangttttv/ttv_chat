package io.netty.example.securechat;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class Decoder2 extends ByteToMessageDecoder {

	   @Override
	   protected void decode(ChannelHandlerContext ctx,
	                           ByteBuf buf, List<Object> out) throws Exception {

	     if (buf.readableBytes() < 6) {
	        return;
	     }

	     buf.markReaderIndex();
	     buf.readByte();
	     buf.readByte();
	     int length = buf.readInt();

	     if (buf.readableBytes() < length-2) {
	        buf.resetReaderIndex();
	        return;
	     }
	     
	     byte[] data =buf.readBytes(length-2).array(); 
	     Envelope envelope = new Envelope(Version.VERSION1, Type.REQUEST, data,data.length);
	     System.out.println("String payload:"+new String(envelope.getPayload()));
	     out.add(envelope);
	   }
	 }
	 