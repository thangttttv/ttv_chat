package com.ttv.process;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class SessionManager {
	private static SessionManager sessionManager = null;
	public static HashMap<Integer, Session> mappSession = new HashMap<Integer, Session>();
	private final  Logger logger = Logger.getLogger(SessionManager.class);
	
	public static SessionManager getInstall() {
		if (sessionManager == null)
			sessionManager = new SessionManager();
		return sessionManager;
	}

	private SessionManager() {

	}

	public Session getSession(int app_client_id) {
		Session session = mappSession.get(app_client_id);
		return session;
	}

	public synchronized void removeChannel(int app_client_id) {
		logger.info("Remove Session App Client ID:" + app_client_id);
		mappSession.remove(app_client_id);
	}

	public synchronized void addSession(int app_client_id, Session session) {
		mappSession.put(app_client_id, session);
		logger.info("ADD Session App Client ID:" + app_client_id+" Total size client join:"+mappSession.size());
	}
}
