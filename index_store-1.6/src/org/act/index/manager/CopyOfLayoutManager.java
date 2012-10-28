/**
 * 
 */
package org.act.index.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.act.index.common.CoreInfo;
import org.act.index.common.ServerInfo;
import org.act.index.common.UserInfo;
import org.act.index.common.sysparam.*;
import org.apache.log4j.Logger;

/**
 * @author yeyue
 *
 */
public class CopyOfLayoutManager {
	private static final Logger LOG = Logger.getLogger(CopyOfLayoutManager.class);
	//private static final ReadWriteLock RWLOCK = new ReentrantReadWriteLock();
	
	private List<ServerCollect> serverList;
	private List<CoreCollect> coreList;
	private List<UserCollect> userList;
	private HashSet<Integer> writableCoreSet;
	private GlobalInfo globalInfo;
	private int maxCoreId;
	private int maxServerId;
	private int maxUserId;
	private int serverNum;
	private int userNum;
	private int aliveServerNum;
	private int validUserNum;
	private int validCoreNum;
	
	/**
	 * 
	 */
	public CopyOfLayoutManager() {
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * @return the serverList
	 */
	public List<ServerCollect> getServerList() {
		return serverList;
	}


	/**
	 * @param serverList the serverList to set
	 */
	public void setServerList(List<ServerCollect> serverList) {
		this.serverList = serverList;
	}


	/**
	 * @return the coreList
	 */
	public List<CoreCollect> getCoreList() {
		return coreList;
	}


	/**
	 * @param coreList the coreList to set
	 */
	public void setCoreList(List<CoreCollect> coreList) {
		this.coreList = coreList;
	}


	/**
	 * @return the userList
	 */
	public List<UserCollect> getUserList() {
		return userList;
	}


	/**
	 * @param userList the userList to set
	 */
	public void setUserList(List<UserCollect> userList) {
		this.userList = userList;
	}


	/**
	 * @return the writableCoreSet
	 */
	public HashSet<Integer> getWritableCoreSet() {
		return writableCoreSet;
	}


	/**
	 * @param writableCoreSet the writableCoreSet to set
	 */
	public void setWritableCoreSet(HashSet<Integer> writableCoreSet) {
		this.writableCoreSet = writableCoreSet;
	}


	/**
	 * @return the globalInfo
	 */
	public GlobalInfo getGlobalInfo() {
		return globalInfo;
	}


	/**
	 * @param globalInfo the globalInfo to set
	 */
	public void setGlobalInfo(GlobalInfo globalInfo) {
		this.globalInfo = globalInfo;
	}


	/**
	 * @return the maxCoreId
	 */
	public int getMaxCoreId() {
		return maxCoreId;
	}


	/**
	 * @param maxCoreId the maxCoreId to set
	 */
	public void setMaxCoreId(int maxCoreId) {
		this.maxCoreId = maxCoreId;
	}


	/**
	 * @return the maxServerId
	 */
	public int getMaxServerId() {
		return maxServerId;
	}


	/**
	 * @param maxServerId the maxServerId to set
	 */
	public void setMaxServerId(int maxServerId) {
		this.maxServerId = maxServerId;
	}


	/**
	 * @return the maxUserId
	 */
	public int getMaxUserId() {
		return maxUserId;
	}


	/**
	 * @param maxUserId the maxUserId to set
	 */
	public void setMaxUserId(int maxUserId) {
		this.maxUserId = maxUserId;
	}


	/**
	 * @return the serverNum
	 */
	public int getServerNum() {
		return serverNum;
	}


	/**
	 * @param serverNum the serverNum to set
	 */
	public void setServerNum(int serverNum) {
		this.serverNum = serverNum;
	}


	/**
	 * @return the userNum
	 */
	public int getUserNum() {
		return userNum;
	}


	/**
	 * @param userNum the userNum to set
	 */
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}


	/**
	 * @return the aliveServerNum
	 */
	public int getAliveServerNum() {
		return aliveServerNum;
	}


	/**
	 * @param aliveServerNum the aliveServerNum to set
	 */
	public void setAliveServerNum(int aliveServerNum) {
		this.aliveServerNum = aliveServerNum;
	}


	public void init() {
		coreList = new ArrayList<CoreCollect>();
	}
	
	public CoreCollect getCoreCollect(int coreId) {
		return coreList.get(coreId);
	}
	
	/**
     * create new CoreCollect object
     * @return if not found, return NULL
     * ** write lock **
     */
	public CoreCollect createCoreCollect() {
		++maxCoreId;
		CoreCollect coreCollect = new CoreCollect(maxCoreId);
		synchronized (coreList) {
			coreList.add(maxCoreId, coreCollect);
		}
		
		return coreCollect;
	}
	
	/**
     * create new CoreCollect object base on coreId
     * @return if not found, return NULL
     * ** write lock **
     */
	public CoreCollect createCoreCollect(int coreId) {
		int size = coreList.size();
		if (coreId < 0 && coreId >= size) {
			LOG.error("core id out of range.");
			return null;
		}
		if (coreList.get(coreId) != null) {
			LOG.error("core " + coreId + " already exists.");
			return null;
		}
		CoreCollect coreCollect = new CoreCollect(coreId);
		synchronized (coreList) {
			coreList.set(coreId, coreCollect);
		}
		return coreCollect;
	}
	
    /**
     * remove a CoreCollect object by coreId
     * ** write lock **
     */
	public boolean removeCoreCollect(int coreId) {
		if (coreId >= 0 && coreId <= maxCoreId) {
			synchronized (coreList) {
				coreList.set(coreId, null);
			}
			return true;
		}
		LOG.error("core " + coreId + " not exsits.");
		return false;
	}
	
    /**
     * find ServerCollect object by serverId
     * @return if not found or ServerCollect is dead, return NULL
     */
	public ServerCollect getServerCollect(int serverId) {
		ServerCollect serverCollect = serverList.get(serverId);
		if (serverCollect != null && serverCollect.isAlive()) {
			return serverCollect;
		}
		return null;
	}
	
	public ServerCollect createServerCollect() {
		++maxServerId;
		ServerCollect serverCollect = new ServerCollect(maxServerId);
		
		synchronized (serverList) {
			serverList.add(maxServerId, serverCollect);
		}
		
		return serverCollect;
	}
	
	public ServerCollect createServerCollect(int serverId) {
		int size = serverList.size();
		if (serverId < 0 && serverId >= size) {
			LOG.error("server id out of range.");
			return null;
		}
		if (serverList.get(serverId) != null) {
			LOG.error("server " + serverId + " already exists.");
			return null;
		}
		ServerCollect serverCollect = new ServerCollect(serverId);
		synchronized (serverList) {
			serverList.set(serverId, serverCollect);
		}
		return serverCollect;
	}
	
    /**
     * remove ServerCollect object base on serverId
     * this op don't erase object actually, only set it's dead state.
     * @return true if exist, otherwise false.
     */
	public boolean removeServerCollect(int serverId) {
		ServerCollect serverCollect = serverList.get(serverId);
		if (serverCollect != null && serverCollect.isAlive()) {
			serverCollect.dead();
			aliveServerNum--;
			return true;
		}
		return false;
	}

	
    /**
     * find userCollect object by userId
     * @return if not found, return NULL
     */
	public UserCollect getUserCollect(int userId) {
		return userList.get(userId);
	}
	
	public UserCollect createUserCollect() {
		++maxUserId;
		UserCollect userCollect = new UserCollect(maxUserId);
		
		synchronized (userList) {
			userList.add(maxUserId, userCollect);
		}
		
		return userCollect;
	}
	
	public UserCollect createUserCollect(int userId) {
		int size = userList.size();
		if (userId < 0 && userId >= size) {
			LOG.error("user id out of range.");
			return null;
		}
		if (userList.get(userId) != null) {
			LOG.error("user " + userId + " already exists.");
			return null;
		}
		UserCollect userCollect = new UserCollect(userId);
		synchronized (userList) {
			userList.set(userId, userCollect);
		}
		return userCollect;
	}
	
    /**
     * remove userCollect object base on userId
     * @return true if exist, otherwise false.
     */
	public boolean removeUserCollect(int userId) {
		if (userId >= 0 && userId <= maxUserId) {
			synchronized (userList) {
				userList.set(userId, null);
			}
			return true;
		}
		LOG.error("user " + userId + " not exsits.");
		return false;
	}
	
    /**
     * remove coreId from writableCoreSet, make it unwritable
     * @return true if exist in writableCoreSet.
     */
	public boolean removeWritableCore(int coreId) {
		if (writableCoreSet.contains(coreId)) {
			synchronized (writableCoreSet) {
				writableCoreSet.remove(coreId);
			}
			return true;
		}
		return false;
	}
	
    /**
     * check coreId if exist writableCoreSet
     * @return true if exist writableCoreSet
     */
	public boolean isWritable(int coreId) {
		return (writableCoreSet.contains(coreId) ? true : false);
	}
	
    /**
     * add coreId into writableCoreSet, make it writable.
     */
	public boolean addWritableCore(int coreId) {
		if (!writableCoreSet.contains(coreId)) {
			synchronized (writableCoreSet) {
				writableCoreSet.add(coreId);
			}
			return true;
		}
		return false;
	}

    /**
     * build relation between index server and core
     * @param master, if true force serverId be master even if core full or server full
     */
	public boolean buildServerCoreRelation(int coreId, int serverId, boolean force) {
		boolean serverWritable = false;
		boolean coreWritable = false;
		boolean canBeMaster = false;
		ServerCollect serverCollect = null;
		CoreCollect coreCollect = null;
		
		//check index server
		
		serverCollect = serverList.get(serverId);
		if (serverCollect == null || !serverCollect.isAlive())
			return false;
		
		//ds_wriList = server_collect->get_primary_wriList_block_list()->size()
        //< static_cast<uint32_t> (SYSPARAM_NAMESERVER.max_write_file_count_);
		coreCollect = getCoreCollect(coreId);
		if (coreCollect == null)
			return false;
		coreWritable = !(coreCollect.isFull());
		if (coreWritable && serverWritable && coreCollect.getMasterServer() == -1) {
			canBeMaster = true;
		}
		coreCollect.join(serverId, canBeMaster || force);
		
		if (coreWritable && isWritable(coreCollect)) {
			addWritableCore(coreId);
		}
	
		serverCollect.joinCore(coreId);
		if (coreWritable)
			serverCollect.joinWritableCore(coreId);
		if (canBeMaster)
			serverCollect.joinPrimaryCore(coreId);
		return true;
	}

    /**
     * release relation between index server and core
     * oppsite function to buildServerCoreRelation
     */
	public boolean releaseServerCoreRelation(int coreId, int serverId) {
		ServerCollect serverCollect = null;

		serverCollect = serverList.get(serverId);
		if (serverCollect == null) {
			return false;
		}

			
		CoreCollect coreCollect = getCoreCollect(coreId);
		if (coreCollect == null)
			return false;
		coreCollect.leave(serverId, (serverId == coreCollect.getMasterServer()));
		if (!isWritable(coreCollect)) {
			removeWritableCore(coreId);
		}
		
		serverCollect.release(coreId);
		return true;
	}

    /*
     * core is full, need to be release write relation,
     * and remove from writable list.
     */
	public boolean releaseCoreWriteRelation(int coreId) {
		HashSet<Integer> servers;
		int master = -1;
		
		CoreCollect coreCollect = getCoreCollect(coreId);
		if (coreCollect == null)
			return false;
		
		if (!isWritable(coreCollect)) {
			removeWritableCore(coreId);
		}
		
		master = coreCollect.getMasterServer();
		servers = coreCollect.getServerSet();
		
		ServerCollect serverCollect;
		Iterator<Integer> iterator = servers.iterator();
		while (iterator.hasNext()) {
			serverCollect = serverList.get(iterator.next());
			if (serverCollect != null) {
				serverCollect.leaveWritableCore(coreId);
				if (serverCollect.getServerInfo().getServerId() == master) {
					serverCollect.leavePrimaryCore(coreId);
				}
			}
		}
		return true;
	}

	/*
     * remove from server list.
     */
	public boolean releaseServerRelation(int serverId) {
		ServerCollect serverCollect = null;

		serverCollect = serverList.get(serverId);
		if (serverCollect == null)
			return false;
		// copy all cores belongs to this index server
		HashSet<Integer> cores = serverCollect.getCoreSet();
		CoreCollect coreCollect;
		
		// release any of cores relation with this index server
		Iterator<Integer> iterator = cores.iterator();
		while (iterator.hasNext()) {
			coreCollect = coreList.get(iterator.next());
			coreCollect.leave(serverId, (serverId == coreCollect.getMasterServer()));
			removeWritableCore(iterator.next());
		}

		// clean all cores list & wriList core list & primary wriList core list
		
		// serverCollect object nerver deleted, so no need to refind
		serverCollect.clearAll();
		return true;
	}
	
    /**
     * to check a core if writable satisfied two conditions.
     * 1: related to common::SYSPARAM_NAMESERVER.min_replication_ DataServerStatInfos.
     * 2: all related servers has empty space
     */
    public boolean isWritable(CoreCollect coreCollect) {
    	/*TBSYS_LOG(DEBUG, "block(%u) is writable, is full(%s) , master_ds(%" PRI64_PREFIX "u)",
    	          block_collect->get_block_info()->block_id_, block_collect->is_full() ? "true" : "false",
    	          block_collect->get_master_ds());*/
    	if (coreCollect != null && !coreCollect.isFull() && coreCollect.getMasterServer() != 0) {
    		HashSet<Integer> servers = coreCollect.getServerSet();
    		if (serverList.size() < ManagerParam.MIN_REPLICATION) {
    			LOG.debug("core(" + coreCollect.getCoreInfo().getCoreId() + 
    					")copy count(" + servers.size() + 
    					") < min replication(" + ManagerParam.MIN_REPLICATION +
    					"), cannot join writable core list.");
    			return false;
    		}
    		
            ServerCollect serverCollect = null;
            Iterator<Integer> iterator = servers.iterator();
    		while (iterator.hasNext()) {
    			serverCollect = serverList.get(iterator.next());
    			if (serverCollect == null || serverCollect.isDiskFull()) {
    				LOG.debug("server full, cannot join core.");
    				return false;
    			}
    		}
    		return true;
    	}
    	return false;
    }
    
    // if core collect is wriList, add to the list
    public boolean coreWritable(CoreCollect coreCollect) {
    	if (coreCollect == null) {
    		return false;
    	}
    	if (isWritable(coreCollect)) {
    		return addWritableCore(coreCollect.getCoreInfo().getCoreId());
    	}
    	return false;
    }

    public boolean serverWritable(ServerCollect serverCollect) {
    	if (serverCollect == null || serverCollect.isDiskFull()) {
    		return false;
    	}
    	/*if ((ds_map_.size() <= 0) && (global_info_.use_capacity_ > 0) && (server_collect->get_ds()->use_capacity_
    	          >= (global_info_.use_capacity_ / alive_ds_size_) * 2)) */{
    		//return false;
    	}
    	return true;
    }

    // update server status info
    public boolean updateServerInfo(ServerInfo serverInfo, boolean isnew) {
    	// TODO server write lock
    	int serverId = serverInfo.getServerId();
    	ServerCollect serverCollect = getServerCollect(serverId);
    	if (serverCollect == null)
    		return false;
    	serverInfo = serverCollect.getServerInfo();
    	if (serverInfo == null)
    		return false;
		serverCollect.setServerInfo(serverInfo);
    	// TODO set lastUpdateTime
    	serverInfo.setLastUpdateTime(Calendar.getInstance());
    	
    	if (isnew) {
    		aliveServerNum++;
    	}
    	return true;
    	
    }

    // update server status info
    public void updateGlobalInfo(ServerInfo serverInfo, boolean isnew) {
    	// TODO global write lock
    	if (isnew) {
    		globalInfo.setUseCapacity(globalInfo.getUseCapacity() + serverInfo.getUseCapacity());
    		globalInfo.setTotalLoad(globalInfo.getTotalLoad() + serverInfo.getCurrentLoad());
    		globalInfo.setTotalCapacity(globalInfo.getTotalCapacity() + serverInfo.getTotalCapacity());
    		globalInfo.setTotalCoreCount(globalInfo.getTotalCoreCount() + serverInfo.getCoreCount());
    		globalInfo.setUseCapacity(globalInfo.getUseCapacity() + 1);
    	}
    	if (serverInfo.getCurrentLoad() > globalInfo.getMaxLoad())
    		globalInfo.setMaxLoad(serverInfo.getCurrentLoad());
    	
    	if (serverInfo.getCoreCount() > globalInfo.getMaxCoreCount())
    		globalInfo.setMaxCoreCount(serverInfo.getCoreCount());
    }

    // get dead server set and writable server set
    public int checkServer(Calendar serverDeadTime, HashSet<Integer> deadServerSet, HashSet<Integer> writableServerSet) {
    	deadServerSet.clear();
    	writableServerSet.clear();
    	
    	GlobalInfo statInfo = new GlobalInfo();
    	
    	long now = Calendar.getInstance().getTimeInMillis() - serverDeadTime.getTimeInMillis();
    	int checkWriteCount = 0;
    	//check_write_count = std::max(1, SYSPARAM_NAMESERVER.max_write_file_count_);

		ServerInfo serverInfo;
		
		for (ServerCollect serverCollect : serverList) {
			if (serverCollect == null || !serverCollect.isAlive())
				continue;
			serverInfo = serverCollect.getServerInfo();
			if (serverInfo.getLastUpdateTime().getTimeInMillis() < now) {
				deadServerSet.add(serverInfo.getServerId());
			}
			else {
				statInfo.setUseCapacity(statInfo.getUseCapacity() + serverInfo.getUseCapacity());
	    		statInfo.setTotalLoad(statInfo.getTotalLoad() + serverInfo.getCurrentLoad());
	    		statInfo.setTotalCapacity(statInfo.getTotalCapacity() + serverInfo.getTotalCapacity());
	    		statInfo.setTotalCoreCount(statInfo.getTotalCoreCount() + serverInfo.getCoreCount());
	    		statInfo.setUseCapacity(statInfo.getUseCapacity() + 1);
	    		if (serverInfo.getCurrentLoad() > statInfo.getMaxLoad())
	        		statInfo.setMaxLoad(serverInfo.getCurrentLoad());
	        	
	        	if (serverInfo.getCoreCount() > statInfo.getMaxCoreCount())
	        		statInfo.setMaxCoreCount(serverInfo.getCoreCount());
	        	if (serverCollect.getPrimaryCoreSet().size() < checkWriteCount)
	        		writableServerSet.add(serverInfo.getServerId());
	        	}
		}
    	
    	// write to GlobalInfo
    	globalInfo = statInfo;
    	return deadServerSet.size();
    }

    // calculate the max core id
	public int calcMaxCoreId() {
		int size = coreList.size();
		int i;
		for (i = size - 1; i >= 0 && coreList.get(i) == null; i--)
			;
		return i;
	}
	
	public int getAvailCoreId() {
		return 0;
	}
}
