/**
 * 
 */
package org.act.index.manager;


import java.util.Calendar;
import java.util.HashSet;
import org.act.index.common.CoreInfo;
import org.act.index.common.sysparam.ManagerParam;
import org.apache.log4j.Logger;
import org.apache.solr.core.SolrCore;

/**
 * @author yeyue
 *
 */
public class CoreCollect {
	private static final Logger LOG = Logger.getLogger(CoreCollect.class);
	private HashSet<Integer> userSet;
	private HashSet<Integer> serverSet;
	private int masterServer;
	private CoreInfo coreInfo;
	private Calendar lastJoinTime;
	private Calendar lastLeaveTime;
	private boolean isCreating;
	private SolrCore solrCore;
	
	/**
	 * 
	 */
	public CoreCollect() {
		// TODO Auto-generated constructor stub
		coreInfo = new CoreInfo();
		userSet = new HashSet<Integer>();
		serverSet = new HashSet<Integer>();
		masterServer = -1;
		
	}

	public CoreCollect(int coreId) {
		// TODO Auto-generated constructor stub
		coreInfo = new CoreInfo(coreId);
		userSet = new HashSet<Integer>();
		serverSet = new HashSet<Integer>();
		masterServer = -1;
	}
	
	//TODO getters and setters

	/**
	 * @return the serverSet
	 */
	public HashSet<Integer> getServerSet() {
		return serverSet;
	}

	/**
	 * @param serverSet the serverSet to set
	 */
	public void setServerSet(HashSet<Integer> serverSet) {
		this.serverSet = serverSet;
	}
	
	/**
	 * @return the coreInfo
	 */
	public CoreInfo getCoreInfo() {
		return coreInfo;
	}

	/**
	 * @param coreInfo the coreInfo to set
	 */
	public void setCoreInfo(CoreInfo coreInfo) {
		this.coreInfo = coreInfo;
	}
	
	/**
	 * @return the masterServer
	 */
	public int getMasterServer() {
		return masterServer;
	}

	/**
	 * @param masterServer the masterServer to set
	 */
	public void setMasterServer(int masterServer) {
		this.masterServer = masterServer;
	}
	
	public boolean isCreating() {
		return isCreating;
	}

	public void setCreatingFlag(boolean flag) {
		this.isCreating = flag;
	}
	
	public boolean join(int serverId, boolean isMaster) {
		//if (!serverSet.contains(serverId)) {
			synchronized (this) {
				serverSet.add(serverId);
				if (isMaster) {
					masterServer = serverId;
					LOG.debug("core:" + coreInfo.getCoreId() + " set master server:" + serverId);
				}
				// TODO lastJoinTime
				lastJoinTime = Calendar.getInstance();
			}
			return true;
		//}
		//return false;
	}

	public boolean leave(int serverId, boolean isMaster) {
		if (serverSet.contains(serverId)) {
			synchronized (this) {
				serverSet.remove(serverId);
				LOG.debug("core:" + coreInfo.getCoreId() + "leave server:" + serverId);
				if (isMaster) {
					masterServer = -1;
					LOG.debug("core:" + coreInfo.getCoreId() + " remove master server:" + serverId);
				}
				// TODO lastLeaveTime
				lastLeaveTime = Calendar.getInstance();
			}
			return true;
		}
		return false;
	}
	
	public boolean addUser(int userId) {
		if (!userSet.contains(userId)) {
			synchronized (userSet) {
				userSet.add(userId);
			}
			return true;
		}
		return false;
	}
	
	public boolean removeUser(int userId) {
		if (userSet.contains(userId)) {
			synchronized (userSet) {
				userSet.remove(userId);
			}
			return true;
		}
		return false;
	}

	public boolean isFull() {
		// TODO
		return false;
	}
	
	public boolean isFull(int size) {
		return size > ManagerParam.MAX_CORE_SIZE;
	}
	
	public void show() {
		LOG.info("core:" + coreInfo.getCoreId());
		LOG.info("coreInfo:");
		LOG.info("\tcore id:" + coreInfo.getCoreId());
		LOG.info("\tuser count:" + coreInfo.getUserCount());
		LOG.info("\tcore size:" + coreInfo.getSize());
		LOG.info("\tmaster server:" + masterServer);
		LOG.info("users:");
		for (int i : userSet) {
			LOG.info("\t" + i);
		}
		LOG.info("writable cores:");
		for (int i : serverSet) {
			LOG.info("\t" + i);
		}
	}
}
