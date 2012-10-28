/**
 * 
 */
package org.act.index.client;

import java.util.HashMap;

/**
 * @author yeyue
 *
 */
public class ClientSessionPool {
	private HashMap<Integer, ClientSession> sessionMap;
	
	/**
	 * 
	 */
	public ClientSessionPool() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the sessionMap
	 */
	public HashMap<Integer, ClientSession> getSessionMap() {
		return sessionMap;
	}

	/**
	 * @param sessionMap the sessionMap to set
	 */
	public void setSessionMap(HashMap<Integer, ClientSession> sessionMap) {
		this.sessionMap = sessionMap;
	}

	/*public ClientSession getSession(String managerIpPortString) {
		if (sessionMap.containsKey(managerIpPortString)) {
			return sessionMap.get(managerIpPortString);
		}
		ClientSession session = new ClientSession(managerIpPortString);
		session.init();
		sessionMap.put(managerIpPortString, session);

		return session;
	}*/
	
	public ClientSession getSession(int userId) {
		if (sessionMap.containsKey(userId)) {
			return sessionMap.get(userId);
		}
		ClientSession session = new ClientSession(userId);
		session.init();
		sessionMap.put(userId, session);
		return session;
	}
	
	public void release(ClientSession session) {
		if (session != null) {
			sessionMap.remove(session.getUserId());
		}
	}
	
	public void clear() {
		sessionMap.clear();
	}
}
