package io.netty.example.securechat;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Sharable

public class Decoder extends MessageToMessageDecoder<ByteBuf> {

	// internal vars
	// --------------------------------------------------------------------------------------------------

	//private Envelope message;

	// constructors
	// ---------------------------------------------------------------------------------------------------
	
	public Decoder() {
		//this.reset();
	}

	// ReplayingDecoder
	// -----------------------------------------------------------------------------------------------
	@Override
	protected void decode(ChannelHandlerContext arg0, ByteBuf buffer,
			List<Object> messageOut) throws Exception {
		try {
			// TODO Auto-generated method stub
			Envelope message = new Envelope();
			buffer.markReaderIndex();
			int read = buffer.readableBytes();
			System.out.println("decode readAble:"+read);
			//while(read>4){
				//buffer.markReaderIndex();
				// Version
			message.setVersion(Version.fromByte(buffer.readByte()));
				// Type
			message.setType(Type.fromByte(buffer.readByte()));
				// Lenght
				int size = buffer.readInt();
				System.out.println("decode lengh:"+size);
				if (size <= 0) {
					
					throw new Exception("Invalid content size");
				}
				// pre-allocate content buffer
				byte[] content = new byte[read-6];
				message.setPayload(content);
				// Payload
				// drain the channel buffer to the message content buffer
				// I have no idea what the contents are, but I'm sure you'll figure out
				// how to turn these
				// bytes into useful content.
				buffer.readBytes(message.getPayload(), 0,
						message.getPayload().length);
				System.out.println("Dencode payload size:"+message.getPayload().length);
				System.out.println("Dencode payload:"+new String(message.getPayload()));
				messageOut.add(message);
			//}
		} catch (Exception e) {
			System.out.println("Loi");
			//e.printStackTrace();
			// TODO: handle exception
		}finally{
			buffer.resetReaderIndex();
		}
		
	}

	
	// private helpers
	// ------------------------------------------------------------------------------------------------

/*	private void reset() {
		this.message = new Envelope();
	}*/

	// private classes
	// ------------------------------------------------------------------------------------------------

	public enum DecodingState {

		// constants
		// --------------------------------------------------------------------------------------------------

		VERSION, TYPE, PAYLOAD_LENGTH, PAYLOAD,
	}

}