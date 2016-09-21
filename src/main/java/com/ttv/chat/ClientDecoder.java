package com.ttv.chat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.securechat.Version;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import com.ttv.bean.SwapEnvelope;
import com.ttv.bean.SwapType;

public class ClientDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf,
			List<Object> out) throws Exception {
		if (buf.readableBytes() < 14) {
			return;
		}

		buf.markReaderIndex();
		SwapEnvelope envelope = new SwapEnvelope();
		// Version
		envelope.setVersion(Version.fromByte(buf.readByte()));
		// Type
		envelope.setType(SwapType.fromByte(buf.readByte()));
		// From User ID
		int fUID = buf.readInt();envelope.setfUID(fUID);
		// To User ID
		int tUID = buf.readInt();
		envelope.settUID(tUID);
		// Lenght
		int length = buf.readInt();

		if (buf.readableBytes() < length) {
			buf.resetReaderIndex();
			return;
		}

		envelope.setPayload(buf.readBytes(length).array());
		envelope.setLength(envelope.getPayload().length);
		out.add(envelope);

	}

}