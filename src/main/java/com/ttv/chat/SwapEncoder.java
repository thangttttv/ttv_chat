package com.ttv.chat;

import com.ttv.bean.SwapEnvelope;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class SwapEncoder extends MessageToByteEncoder<SwapEnvelope> {

    // constructors ---------------------------------------------------------------------------------------------------

	SwapEncoder() {
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
    	// version
        out.writeByte(envelope.getVersion().getByteValue());
        // type
        out.writeByte(envelope.getType().getByteValue());
        System.out.println("Encode:"+envelope.getType());
        // form user id
        out.writeInt(envelope.getfUID());
        // to user id  
        out.writeInt(envelope.gettUID());
        // lenght
        out.writeInt(envelope.getLength());
        // data
        out.writeBytes(envelope.getPayload());
	}

    
   
}