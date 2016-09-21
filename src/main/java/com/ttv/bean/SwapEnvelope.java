package com.ttv.bean;

import io.netty.example.securechat.Version;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class SwapEnvelope implements Serializable {

	// internal vars
	// --------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Version version;
	private SwapType type;
	private int appID; // App client id
	private int fUID; // from user id
	private int tUID; // to user id
	private int length; // size payload
	private byte[] payload;
	private String file;

	// constructors
	// ---------------------------------------------------------------------------------------------------
	public SwapEnvelope(Version version, SwapType type, int appID,int fUID,int tUID, int length, byte[] payload) {
		this.version = version;
		this.type = type;
		this.appID = appID;
		this.fUID = fUID;
		this.tUID = tUID;
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

	public SwapType getType() {
		return type;
	}

	public void setType(SwapType type) {
		this.type = type;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	public int getAppID() {
		return appID;
	}

	public void setAppID(int appID) {
		this.appID = appID;
	}

	public int getfUID() {
		return fUID;
	}

	public void setfUID(int fUID) {
		this.fUID = fUID;
	}

	public int gettUID() {
		return tUID;
	}

	public void settUID(int tUID) {
		this.tUID = tUID;
	}
	

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public SwapEnvelope() {
	}

	// low level overrides
	// --------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return new StringBuilder().append("Envelope{").append("version=")
				.append(version).append(", type=").append(type)
				.append(", payload=")
				.append(payload == null ? null : payload.length + "bytes")
				.append('}').toString();
	}

	public ByteBuffer toByteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(2 + 4 + "f".getBytes().length);
		buffer.put(this.version.getByteValue());
		buffer.put(this.type.getByteValue());
		buffer.putInt(3);
		buffer.put("f".getBytes());
		buffer.flip();
		return buffer;
	}
}