package com.ttv.process;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class AppClientManager {
	private static AppClientManager appClientManager = null;
	public static HashMap<Integer, String> mapAppClient = new HashMap<Integer, String>();
	private final  Logger logger = Logger.getLogger(AppClientManager.class);
	
	public static AppClientManager getInstall()
	{
		if(appClientManager == null)
			appClientManager = new AppClientManager();
		return appClientManager;
	}
	
	private AppClientManager(){
		
	}
	
	public String getAppClientIDs(int user_id){
		String app_client_ids = mapAppClient.get(user_id);
		return app_client_ids;
	}
	
	public synchronized void removeAppClientID(int user_id,int app_client_id){
		String app_client_ids = mapAppClient.get(user_id);
		logger.info("Remove UserID:"+user_id+" App_client_ids :" + app_client_id);
		if(app_client_ids!=null){
			app_client_ids = app_client_ids.replaceAll(app_client_id+",", "");
			if(!"".equalsIgnoreCase(app_client_ids))
			mapAppClient.put(user_id, app_client_ids);
			else mapAppClient.remove(user_id);
		}
	}
	
	public synchronized void addAppClientID(int user_id,int app_client_id){
		String app_client_ids = mapAppClient.get(user_id);
		logger.info("---->Chuoi app_client id ban dau cua user:"+user_id+" App_client_id:" + app_client_ids);
		logger.info("ADD UserID:"+user_id+" App_client_id:" + app_client_id);
		if(app_client_ids==null)
			app_client_ids = app_client_id+",";
			else {
				app_client_ids = app_client_ids.replaceAll(app_client_id+",", "");
				app_client_ids +=app_client_id +",";
			}
		logger.info("---->Chuoi app_client id sau do cua user:"+user_id+" App_client_id:" + app_client_ids);
		mapAppClient.put(user_id, app_client_ids);
	}
}