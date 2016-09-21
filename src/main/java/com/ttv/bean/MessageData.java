package com.ttv.bean;

import io.netty.example.securechat.Envelope;

import java.util.Calendar;

import com.ttv.chat.ChatConstant;

import net.sf.json.JSONObject;

public class MessageData {
	public int id;
	public int app_client_id;
	public int fuID;
	public int tuID;
	public String content="";
	
	public int pID=0;
	public String p_swap_id="";
	public double price=0;
	public int quantity=0;
	public SwapType type;
	
	public long time_receive;
	public long time;
	
	public String file="";
	public long file_size = 0;
	
	private String fuName;
	private String tuName;
	private String fuAvatar;
	private String tuAvatar;
	
	public String getP_swap_id() {
		return p_swap_id;
	}

	public void setP_swap_id(String p_swap_id) {
		this.p_swap_id = p_swap_id;
	}

	
	public MessageData() {
	
	}
	
	public static MessageData buileMessageText(int _fuID,int _tuID,int _pID,String _content,SwapType _type,long _time) {
		MessageData messageData = new MessageData();
		messageData.fuID = _fuID;
		messageData.tuID = _tuID;
		messageData.pID = _pID;
		messageData.content = _content;
		messageData.type = _type;
		messageData.time = _time;
		messageData.time_receive  = Calendar.getInstance().getTimeInMillis();
		return messageData;
	}
	
	public static MessageData buileMessageInform(int _fuID,int _tuID,int _pID,String _content,SwapType _type,long _time) {
		MessageData messageData = new MessageData();
		messageData.fuID = _fuID;
		messageData.tuID = _tuID;
		messageData.pID = _pID;
		messageData.content = _content;
		messageData.type = _type;
		messageData.time = _time;
		messageData.time_receive  = Calendar.getInstance().getTimeInMillis();
		return messageData;
	}
	
	public static MessageData builderMessageFile(int _fuID,int _tuID,int _pID,String _content,SwapType _type,String _file,long _file_size,long _time) {
		MessageData messageData = new MessageData();
		messageData.fuID = _fuID;
		messageData.tuID = _tuID;
		messageData.pID = _pID;
		messageData.content = _content;
		messageData.type = _type;
		messageData.file = _file;
		messageData.file_size = _file_size;
		messageData.time = _time;
		messageData.time_receive  = Calendar.getInstance().getTimeInMillis();
		return messageData;
	}
	
	public static MessageData builderMessageOfferBuy (int _fuID,int _tuID,int _pID,String _content,SwapType _type,double _price,int _quantity,long _time) {
		MessageData messageData = new MessageData();
		messageData.fuID = _fuID;
		messageData.tuID = _tuID;
		messageData.pID = _pID;
		messageData.content = _content;
		messageData.type = _type;
		messageData.price = _price;
		messageData.quantity = _quantity;
		messageData.time = _time;
		messageData.time_receive  = Calendar.getInstance().getTimeInMillis();
		return messageData;
	}
	
	public static MessageData builderMessageOfferSwap(int _fuID,int _tuID,int _pID,String _content,SwapType _type,double _price,int _quantity,String _p_swap_id,long _time) {
		MessageData messageData = new MessageData();
		messageData.fuID = _fuID;
		messageData.tuID = _tuID;
		messageData.pID = _pID;
		messageData.content = _content;
		messageData.type = _type;
		messageData.price = _price;
		messageData.quantity = _quantity;
		messageData.p_swap_id = _p_swap_id;
		messageData.time = _time;
		messageData.time_receive  = Calendar.getInstance().getTimeInMillis();
		return messageData;
	}
	
	public int getpID() {
		return pID;
	}

	public void setpID(int pID) {
		this.pID = pID;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuatity() {
		return quantity;
	}

	public void setQuatity(int quatity) {
		this.quantity = quatity;
	}

	public SwapType getType() {
		return type;
	}

	public void setType(SwapType type) {
		this.type = type;
	}

	public long getTime_receive() {
		return time_receive;
	}

	public void setTime_receive(long time_receive) {
		this.time_receive = time_receive;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	
	public int getFuID() {
		return fuID;
	}
	
	public void setFuID(int fuID) {
		this.fuID = fuID;
	}
	public int getTuID() {
		return tuID;
	}
	
	public void setTuID(int tuID) {
		this.tuID = tuID;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getFuName() {
		return fuName;
	}
	
	public void setFuName(String fuName) {
		this.fuName = fuName;
	}
	
	public String getTuName() {
		return tuName;
	}
	
	public void setTuName(String tuName) {
		this.tuName = tuName;
	}
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public String getFuAvatar() {
		return fuAvatar;
	}
	
	public void setFuAvatar(String fuAvatar) {
		this.fuAvatar = fuAvatar;
	}
	
	public String getTuAvatar() {
		return tuAvatar;
	}
	
	public void setTuAvatar(String tuAvatar) {
		this.tuAvatar = tuAvatar;
	}
	
	public String toJSonString(){
		String strJson = "";
		   JSONObject formDetailsJson = new JSONObject();
	        formDetailsJson.put("fuID", fuID);
	        formDetailsJson.put("tuID", tuID);
	        formDetailsJson.put("fuName",fuName);
	        formDetailsJson.put("tuName",tuName);
	        formDetailsJson.put("fuAvatar",fuAvatar);
	        formDetailsJson.put("tuAvatar",tuAvatar);
	        formDetailsJson.put("time",time);
	        formDetailsJson.put("content",content);
	        strJson=formDetailsJson.toString();
		return strJson;
	}
	
	public String toJSonStringMessageText(){
		String strJson = "";
		   JSONObject formDetailsJson = new JSONObject();
	        formDetailsJson.put("fuID", fuID);
	        formDetailsJson.put("tuID", tuID);
	        formDetailsJson.put("pID",pID);
	        formDetailsJson.put("content",content);
	        formDetailsJson.put("time",time);
	        strJson=formDetailsJson.toString();
		return strJson;
	}
	
	public String toJSonStringMessageFileHeader(){
		String strJson = "";
		   JSONObject formDetailsJson = new JSONObject();
	        formDetailsJson.put("fuID", fuID);
	        formDetailsJson.put("tuID", tuID);
	        formDetailsJson.put("pID",pID);
	        formDetailsJson.put("image",ChatConstant.DOMAIN_WEB+file);
	        formDetailsJson.put("size",file_size);
	        formDetailsJson.put("time",time);
	        strJson=formDetailsJson.toString();
		return strJson;
	}
	
	public String toJSonStringMessageOfferBuy(){
		String strJson = "";
		   JSONObject formDetailsJson = new JSONObject();
	        formDetailsJson.put("fuID", fuID);
	        formDetailsJson.put("tuID", tuID);
	        formDetailsJson.put("pID",pID);
	        formDetailsJson.put("content",content);
	        formDetailsJson.put("price",price);
	        formDetailsJson.put("quantity",quantity);
	        formDetailsJson.put("time",time);
	        strJson=formDetailsJson.toString();
		return strJson;
	}
	
	public String toJSonStringMessageOfferSwap(){
		String strJson = "";
		   JSONObject formDetailsJson = new JSONObject();
	        formDetailsJson.put("fuID", fuID);
	        formDetailsJson.put("tuID", tuID);
	        formDetailsJson.put("pID",pID);
	        formDetailsJson.put("psID",p_swap_id);
	        formDetailsJson.put("content",content);
	        formDetailsJson.put("price",price);
	        formDetailsJson.put("quantity",quantity);
	        formDetailsJson.put("time",time);
	        strJson=formDetailsJson.toString();
	        return strJson;
	}	
	
	public Envelope toEnvelope(){
		Envelope envelope = new Envelope();
		return envelope;
	}
	
	public static void main(String[] args) {
		MessageData chatMessage = MessageData.buileMessageText(1, 2, 3, "Xin chao", SwapType.TEXT, Calendar.getInstance().getTimeInMillis());
		System.out.println(chatMessage.toJSonString());
	}
}