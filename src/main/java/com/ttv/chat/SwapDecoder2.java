package com.ttv.chat;

import java.util.List;

import com.ttv.bean.SwapEnvelope;
import com.ttv.bean.SwapType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.securechat.Version;
import io.netty.handler.codec.ReplayingDecoder;


public class SwapDecoder2 extends ReplayingDecoder<SwapDecoder2.DecodingState> {

    // internal vars --------------------------------------------------------------------------------------------------

    private SwapEnvelope message;

    // constructors ---------------------------------------------------------------------------------------------------

    public SwapDecoder2() {
        this.reset();
    }

    // ReplayingDecoder -----------------------------------------------------------------------------------------------

    @Override
    protected void decode(ChannelHandlerContext ctx,
            ByteBuf buf, List<Object> out)
            throws Exception {
        // notice the switch fall-through
        switch (state()) {
            case VERSION:
                this.message.setVersion(Version.fromByte(buf.readByte()));
                checkpoint(DecodingState.TYPE);
            case TYPE:
                this.message.setType(SwapType.fromByte(buf.readByte()));
                checkpoint(DecodingState.FUSERID);
                
            case FUSERID:
                int fUID = buf.readInt();
                System.out.println(fUID);
                if (fUID <= 0) {
                    throw new Exception("Invalid content FUSERID");
                }
                this.message.setfUID(fUID);
                checkpoint(DecodingState.TUSERID);
            case TUSERID:
                int tUID = buf.readInt();
                if (tUID <= 0) {
                    throw new Exception("Invalid content TUSERID");
                }
                this.message.settUID(tUID);
                checkpoint(DecodingState.PAYLOAD_LENGTH);
            case PAYLOAD_LENGTH:
                int size = buf.readInt();
                if (size <= 0) {
                    throw new Exception("Invalid content size");
                }
                // pre-allocate content buffer
                byte[] content = new byte[size];
                this.message.setPayload(content);
                checkpoint(DecodingState.PAYLOAD);
            case PAYLOAD:
                // drain the channel buffer to the message content buffer
                // I have no idea what the contents are, but I'm sure you'll figure out how to turn these
                // bytes into useful content.
            	buf.readBytes(this.message.getPayload(), 0,
                                 this.message.getPayload().length);

                // This is the only exit point of this method (except for the two other exceptions that
                // should never occur).
                // Whenever there aren't enough bytes, a special exception is thrown by the channel buffer
                // and automatically handled by netty. That's why all conditions in the switch fall through
                try {
                    // return the instance var and reset this decoder state after doing so.
                	out.add(message);
                } finally {
                    this.reset();
                }
            default:
                throw new Exception("Unknown decoding state: " + state());
        }
    }

    // private helpers ------------------------------------------------------------------------------------------------

    private void reset() {
        checkpoint(DecodingState.VERSION);
        this.message = new SwapEnvelope();
    }

    // private classes ------------------------------------------------------------------------------------------------

    public enum DecodingState {

        // constants --------------------------------------------------------------------------------------------------

        VERSION,
        TYPE,
        FUSERID,
        TUSERID,
        PAYLOAD_LENGTH,
        PAYLOAD,
    }
}