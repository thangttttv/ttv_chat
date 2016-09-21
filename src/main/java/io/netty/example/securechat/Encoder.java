package io.netty.example.securechat;


import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class Encoder extends MessageToByteEncoder<Envelope> {

    // constructors ---------------------------------------------------------------------------------------------------

    Encoder() {
    }

    // public static methods ------------------------------------------------------------------------------------------

    public static Encoder getInstance() {
        return InstanceHolder.INSTANCE;
    }


    // private classes ------------------------------------------------------------------------------------------------

    private static final class InstanceHolder {
        private static final Encoder INSTANCE = new Encoder();
    }
    
    @Override
	protected void encode(ChannelHandlerContext arg0, Envelope envelope,
			ByteBuf out) throws Exception {
    	
		/*System.out.println(new String("Encode"));
		System.out.println("Size:"+envelope.getPayload().length);
		System.out.println("Payload Encode:"+envelope.getPayload().length);*/

		// TODO Auto-generated method stub
		 // version(1b) + type(1b) + payload length(4b) + payload(nb)
        //int size = toBytes(envelope.getPayload().length).length + envelope.getPayload().length;
        
        out.writeByte(envelope.getVersion().getByteValue());
        out.writeByte(envelope.getType().getByteValue());
        out.writeInt(envelope.getLength());
        out.writeBytes(envelope.getPayload());
       
        
     /*   ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.put(envelope.getVersion().getByteValue());
        buffer.put(envelope.getType().getByteValue());
        buffer.put(toBytes(envelope.getPayload().length));
        buffer.put(envelope.getPayload());
        buffer.flip();*/

     /*   ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(envelope.getVersion().getByteValue());
        baos.write(envelope.getType().getByteValue());
        baos.write(toBytes(envelope.getPayload().length));
        baos.write(envelope.getPayload());

        out.writeBytes(baos.toByteArray());*/
        
        System.out.println("Encode lenght:"+envelope.getPayload().length);
        //System.out.println("Data :"+new String(baos.toByteArray()));
	}

    
    byte[] toBytes(int i)
    {
      byte[] result = new byte[4];

      result[0] = (byte) (i >> 24);
      result[1] = (byte) (i >> 16);
      result[2] = (byte) (i >> 8);
      result[3] = (byte) (i /*>> 0*/);

      return result;
    }
	
	
}
