package io.netty.example.securechat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

//http://netty.io/4.0/api/io/netty/handler/codec/ReplayingDecoder.html

public class IntegerHeaderFrameDecoder
     extends ReplayingDecoder<MyDecoderState> {

  private int length;

  public IntegerHeaderFrameDecoder() {
    // Set the initial state.
    super(MyDecoderState.READ_LENGTH);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx,
                          ByteBuf buf, List<Object> out) throws Exception {
    switch (state()) {
    case READ_LENGTH:
      length = buf.readInt();
      checkpoint(MyDecoderState.READ_CONTENT);
    case READ_CONTENT:
      ByteBuf frame = buf.readBytes(length);
      checkpoint(MyDecoderState.READ_LENGTH);
      
      Envelope envelope = new Envelope(Version.VERSION1, Type.REQUEST, frame.array(),frame.array().length);
	  out.add(envelope);
	  System.out.println("String payload:"+new String(envelope.getPayload(),StandardCharsets.UTF_8 ));
      break;
    default:
      throw new Error("Shouldn't reach here.");
    }
  }
}



