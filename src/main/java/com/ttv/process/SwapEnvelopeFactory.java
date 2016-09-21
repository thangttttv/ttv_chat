package com.ttv.process;

import io.netty.example.securechat.Version;
import net.sf.json.JSONObject;

import com.ttv.bean.SwapEnvelope;
import com.ttv.bean.SwapType;


public class SwapEnvelopeFactory {
	
	public  static SwapEnvelope builderEnvelopeReply(int appID,int fuID,int tuID,int pID,long time){
		String payload = "";
		JSONObject formDetailsJson = new JSONObject();
        formDetailsJson.put("pID",pID);
        formDetailsJson.put("time",time);
        payload=formDetailsJson.toString();
	        
		SwapEnvelope envelope = new SwapEnvelope(Version.VERSION1,SwapType.REPLY,appID,fuID,tuID,payload.getBytes().length,payload.getBytes());
		return envelope;
	}
	
	public  static SwapEnvelope builderEnvelopePing(int appID,int fuID,int tuID,int pID,long time){
		String payload = "";
		JSONObject formDetailsJson = new JSONObject();
        formDetailsJson.put("pID",pID);
        formDetailsJson.put("time",time);
        payload=formDetailsJson.toString();
	        
		SwapEnvelope envelope = new SwapEnvelope(Version.VERSION1,SwapType.PING,appID,fuID,tuID,payload.getBytes().length,payload.getBytes());
		return envelope;
	}
	
	public  static SwapEnvelope builderEnvelopeText(int appID,int fuID,int tuID,int pID,String content,long time){
		String payload = "";
		JSONObject formDetailsJson = new JSONObject();
		formDetailsJson.put("pID",pID);
		formDetailsJson.put("content",content);
        formDetailsJson.put("time",time);
        payload=formDetailsJson.toString();
        SwapEnvelope envelope = new SwapEnvelope(Version.VERSION1,SwapType.TEXT,appID,fuID,tuID,payload.getBytes().length,payload.getBytes());
		return envelope;
	}
	
	public  static SwapEnvelope builderEnvelopeInform(int appID,int fuID,int tuID,int pID,String content,long time){
		String payload = "";
		JSONObject formDetailsJson = new JSONObject();
		formDetailsJson.put("pID",pID);
		formDetailsJson.put("content",content);
        formDetailsJson.put("time",time);
        payload=formDetailsJson.toString();
        SwapEnvelope envelope = new SwapEnvelope(Version.VERSION1,SwapType.INFORM,appID,fuID,tuID,payload.getBytes().length,payload.getBytes());
		return envelope;
	}
	
	public  static SwapEnvelope builderEnvelopeFile(int appID,int fuID,int tuID,int pID,String content,String file,long file_size,long time){
		String payload = "";
		JSONObject formDetailsJson = new JSONObject();
		formDetailsJson.put("pID",pID);
		formDetailsJson.put("content",content);
		formDetailsJson.put("image",file);
		formDetailsJson.put("size",file_size);
        formDetailsJson.put("time",time);
        payload=formDetailsJson.toString();
        SwapEnvelope envelope = new SwapEnvelope(Version.VERSION1,SwapType.FILE,appID,fuID,tuID,payload.getBytes().length,payload.getBytes());
		return envelope;
	}
	
	
	public static SwapEnvelope builderEnvelopeBuy(int appID,int fuID,int tuID,int pID,String content,SwapType type,double price,int quantity,long time) {
		String payload = "";
		JSONObject formDetailsJson = new JSONObject();
		formDetailsJson.put("pID",pID);
		formDetailsJson.put("content",content);
		formDetailsJson.put("price",price);
		formDetailsJson.put("quantity",quantity);
        formDetailsJson.put("time",time);
        payload=formDetailsJson.toString();
        SwapEnvelope envelope = new SwapEnvelope(Version.VERSION1,type,appID,fuID,tuID,payload.getBytes().length,payload.getBytes());
		return envelope;
	}
	
	public static SwapEnvelope builderMessageSwap(int appID,int fuID,int tuID,int pID,String content,SwapType type,double price,int quantity,String p_swap_id,long time) {
		String payload = "";
		JSONObject formDetailsJson = new JSONObject();
		formDetailsJson.put("pID",pID);
		formDetailsJson.put("content",content);
		formDetailsJson.put("price",price);
		formDetailsJson.put("quantity",quantity);
		formDetailsJson.put("p_swap_id",p_swap_id);
        formDetailsJson.put("time",time);
        payload=formDetailsJson.toString();
        SwapEnvelope envelope = new SwapEnvelope(Version.VERSION1,type,appID,fuID,tuID,payload.getBytes().length,payload.getBytes());
		return envelope;
	}
	
}
