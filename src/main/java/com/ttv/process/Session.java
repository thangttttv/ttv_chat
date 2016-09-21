package com.ttv.process;

import io.netty.channel.Channel;

public class Session {
	private int ID = -1;
	private int app_client_id;
	private int user_id;
	private Channel channel;

	public Session(int ID, int app_client_id, int user_id,Channel channel) {
		this.ID = ID;
		this.app_client_id = app_client_id;
		this.user_id = user_id;
		this.channel = channel;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getApp_client_id() {
		return app_client_id;
	}

	public void setApp_client_id(int app_client_id) {
		this.app_client_id = app_client_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public void sendMessage(Object object){
		channel.writeAndFlush(object);
	}
	
	
}
