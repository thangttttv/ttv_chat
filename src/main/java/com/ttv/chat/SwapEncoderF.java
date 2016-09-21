package com.ttv.chat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.ttv.bean.SwapEnvelope;

public class SwapEncoderF extends MessageToByteEncoder<SwapEnvelope> {

    // constructors ---------------------------------------------------------------------------------------------------

	SwapEncoderF() {
    }

    // public static methods ------------------------------------------------------------------------------------------

    public static SwapEncoder getInstance() {
        return InstanceHolder.INSTANCE;
    }


    // private classes ------------------------------------------------------------------------------------------------

    private static final class InstanceHolder {
        private static final SwapEncoder INSTANCE = new SwapEncoder();
    }
    
    @Override
	protected void encode(ChannelHandlerContext arg0, SwapEnvelope envelope,
			ByteBuf out) throws Exception {
        out.writeBytes(envelope.getPayload());
	}

    
   
}