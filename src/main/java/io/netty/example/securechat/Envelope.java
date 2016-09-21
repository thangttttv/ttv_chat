package io.netty.example.securechat;


import java.io.Serializable;
import java.nio.ByteBuffer;



public class Envelope implements Serializable {

    // internal vars --------------------------------------------------------------------------------------------------

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Version version;
    private Type type;
    private int length;
    private byte[] payload;

    // constructors ---------------------------------------------------------------------------------------------------

    public Envelope() {
    }

    public Envelope(Version version, Type type, byte[] payload,int length) {
        this.version = version;
        this.type = type;
        this.payload = payload;
        this.length = length;
    }

    public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    // low level overrides --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Envelope{")
                .append("version=").append(version)
                .append(", type=").append(type)
                .append(", payload=").append(payload == null ? null : payload.length + "bytes")
                .append('}').toString();
    }
    
    public ByteBuffer toByteBuffer() {
    	  ByteBuffer buffer = ByteBuffer.allocate(2+4+"f".getBytes().length);
          buffer.put(this.version.getByteValue());
          buffer.put(this.type.getByteValue());
          buffer.putInt(3);
          buffer.put("f".getBytes());
          buffer.flip();
          return buffer;
	}
}
