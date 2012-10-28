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
import org.act.index.common.constdef.GlobalMessage;
import org.act.index.common.sysparam.*;
import org.apache.log4j.Logger;

import sun.util.logging.resources.logging;

/**
 * @author yeyue
 *
 */
public class LayoutManager {
	private static final Logger LOG = Logger.getLogger(LayoutManager.class);
	//private static final ReadWriteLock RWLOCK = new ReentrantReadWriteLock();
	
	private List<ServerCollect> serverList;
	private List<CoreCollect> coreList;
	private List<UserCollect> userList;
	private HashSet<Integer> writableCoreSet;
	private GlobalInfo globalInfo;
	private int maxCoreId;
	private int maxServerId;
	private long maxUserId;
	private int serverNum;
	private long userNum;
	private int aliveServerNum;
	private long validUserNum;
	private int validCoreNum;
	private int currentElectSeq;
	
	/**
	 * 
	 */
	public LayoutManager() {
		// TODO Auto-generated constructor stub
		userList = new ArrayList<UserCollect>();
		serverList = new ArrayList<ServerCollect>();
		coreList = new ArrayList<CoreCollect>();
		writableCoreSet = new HashSet<Integer>();
		maxUserId = -1;
		maxServerId = -1;
		maxCoreId = -1;
		aliveServerNum = 0;
		currentElectSeq = 0;
		globalInfo = new GlobalInfo();
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#getServerList()
	 */
	public List<ServerCollect> getServerList() {
		return serverList;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#setServerList(java.util.List)
	 */
	public void setServerList(List<ServerCollect> serverList) {
		this.serverList = serverList;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#getCoreList()
	 */
	public List<CoreCollect> getCoreList() {
		return coreList;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#setCoreList(java.util.List)
	 */
	public void setCoreList(List<CoreCollect> coreList) {
		this.coreList = coreList;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#getUserList()
	 */
	public List<UserCollect> getUserList() {
		return userList;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#setUserList(java.util.List)
	 */
	public void setUserList(List<UserCollect> userList) {
		this.userList = userList;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#getWritableCoreSet()
	 */
	public HashSet<Integer> getWritableCoreSet() {
		return writableCoreSet;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#setWritableCoreSet(java.util.HashSet)
	 */
	public void setWritableCoreSet(HashSet<Integer> writableCoreSet) {
		this.writableCoreSet = writableCoreSet;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#getGlobalInfo()
	 */
	public GlobalInfo getGlobalInfo() {
		return globalInfo;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#setGlobalInfo(org.act.index.manager.GlobalInfo)
	 */
	public void setGlobalInfo(GlobalInfo globalInfo) {
		this.globalInfo = globalInfo;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#getMaxCoreId()
	 */
	public int getMaxCoreId() {
		return maxCoreId;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#setMaxCoreId(int)
	 */
	public void setMaxCoreId(int maxCoreId) {
		this.maxCoreId = maxCoreId;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#getMaxServerId()
	 */
	public int getMaxServerId() {
		return maxServerId;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#setMaxServerId(int)
	 */
	public void setMaxServerId(int maxServerId) {
		this.maxServerId = maxServerId;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#getMaxUserId()
	 */
	public long getMaxUserId() {
		return maxUserId;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#setMaxUserId(long)
	 */
	public void setMaxUserId(long maxUserId) {
		this.maxUserId = maxUserId;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#getServerNum()
	 */
	public int getServerNum() {
		return serverNum;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#setServerNum(int)
	 */
	public void setServerNum(int serverNum) {
		this.serverNum = serverNum;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#getUserNum()
	 */
	public long getUserNum() {
		return userNum;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#setUserNum(long)
	 */
	public void setUserNum(long userNum) {
		this.userNum = userNum;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#getAliveServerNum()
	 */
	public int getAliveServerNum() {
		return aliveServerNum;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#setAliveServerNum(int)
	 */
	public void setAliveServerNum(int aliveServerNum) {
		this.aliveServerNum = aliveServerNum;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#getCurrentElectSeq()
	 */
	public int getCurrentElectSeq() {
		return currentElectSeq;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#setCurrentElectSeq(int)
	 */
	public void setCurrentElectSeq(int currentElectSeq) {
		this.currentElectSeq = currentElectSeq;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#init()
	 */
	public int init() {
		coreList = new ArrayList<CoreCollect>();
		return GlobalMessage.ISS_SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#getCoreCollect(int)
	 */
	public CoreCollect getCoreCollect(int coreId) {
		//LOG.debug("get core collect:" + coreId);
		if (coreId < 0 || coreId >= coreList.size())
			return null;
		return coreList.get(coreId);
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#createCoreCollect()
	 */
	public CoreCollect createCoreCollect() {
		++maxCoreId;
		CoreCollect coreCollect = new CoreCollect(maxCoreId);
		synchronized (coreList) {
			coreList.add(maxCoreId, coreCollect);
			LOG.info("add core:" + coreCollect.getCoreInfo().getCoreId() + " maxCoreId:" + maxCoreId + " core list size:" + coreList.size());
		}
		
		return coreCollect;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#createCoreCollect(int)
	 */
	public CoreCollect createCoreCollect(int coreId) {
		int size = coreList.size();
		if (coreId < 0 || coreId >= size) {
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
	
    /* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#removeCoreCollect(int)
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
	
    /* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#getServerCollect(int)
	 */
	public ServerCollect getServerCollect(int serverId) {
		if (serverId  < 0 || serverId >= serverList.size())
			return null;
		ServerCollect serverCollect = serverList.get(serverId);
		if (serverCollect != null && serverCollect.isAlive()) {
			return serverCollect;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#getServerCollect(int, java.lang.Boolean)
	 */
	public ServerCollect getServerCollect(int serverId, Boolean renew) {
		if (renew) {
			if (serverId < 0 || serverId >= serverList.size()) {
				return createServerCollect();
			}
			ServerCollect serverCollect = serverList.get(serverId);
			if (serverCollect != null) {
				return serverCollect;
			}
		}
		return serverList.get(serverId);
		//return createServerCollect(serverId);
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#createServerCollect()
	 */
	public ServerCollect createServerCollect() {
		++maxServerId;
		ServerCollect serverCollect = new ServerCollect(maxServerId);
		
		synchronized (serverList) {
			serverList.add(maxServerId, serverCollect);
		}
		serverNum++;
		aliveServerNum++;
		return serverCollect;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#createServerCollect(int)
	 */
	public ServerCollect createServerCollect(int serverId) {
		int size = serverList.size();
		if (serverId < 0 || serverId >= size) {
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
		serverNum++;
		aliveServerNum++;
		return serverCollect;
	}
	
    /* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#removeServerCollect(int)
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

	
    /* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#getUserCollect(long)
	 */
	public UserCollect getUserCollect(long userId) {
		return userList.get((int)userId);
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#createUserCollect()
	 */
	public UserCollect createUserCollect() {
		++maxUserId;
		UserCollect userCollect = new UserCollect(maxUserId);
		
		synchronized (userList) {
			userList.add((int)maxUserId, userCollect);
		}
		
		return userCollect;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#createUserCollect(int)
	 */
	public UserCollect createUserCollect(long userId) {
		int size = userList.size();
		if (userId < 0 || userId >= size) {
			LOG.error("user id out of range.");
			return null;
		}
		if (userList.get((int)userId) != null) {
			LOG.error("user " + userId + " already exists.");
			return null;
		}
		UserCollect userCollect = new UserCollect(userId);
		synchronized (userList) {
			userList.set((int)userId, userCollect);
		}
		return userCollect;
	}
	
	public UserCollect createUserCollect(long userId, int coreId) {
		int size = userList.size();
		if (userId < 0) {
			LOG.error("user id out of range.");
			return null;
		}
		if (userId < size && userList.get((int)userId) != null) {
			LOG.error("user " + userId + " already exists.");
			return null;
		}
		UserCollect userCollect = new UserCollect(userId, coreId);
		synchronized (userList) {
			userList.add((int)userId, userCollect);
		}
		return userCollect;
	}
	
    /* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#removeUserCollect(int)
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
	
    /* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#removeWritableCore(int)
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
	
    /* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#isWritable(int)
	 */
	public boolean isWritable(int coreId) {
		return (writableCoreSet.contains(coreId) ? true : false);
	}
	
    /* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#addWritableCore(int)
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

    /* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#buildServerCoreRelation(int, int, boolean)
	 */
	public boolean buildServerCoreRelation(int coreId, int serverId, boolean force) {
		boolean serverWritable = false;
		boolean coreWritable = false;
		boolean canBeMaster = false;
		ServerCollect serverCollect = null;
		CoreCollect coreCollect = null;
		
		//check index server
		LOG.debug("check index server.");
		serverCollect = serverList.get(serverId);
		if (serverCollect == null || !serverCollect.isAlive())
			return false;
		//LOG.debug("get server:" + serverCollect.getServerInfo().getServerId());
		serverWritable = serverCollect.getPrimaryCoreSet().size() < ManagerParam.MAX_WRITE_USER_COUNT;
		//System.out.println("max write user count:" + ManagerParam.MAX_WRITE_USER_COUNT);
		//ds_wriList = server_collect->get_primary_wriList_block_list()->size()
        //< static_cast<uint32_t> (SYSPARAM_NAMESERVER.max_write_file_count_);
		coreCollect = getCoreCollect(coreId);
		if (coreCollect == null)
			return false;
		//LOG.debug("get core:" + coreCollect.getCoreInfo().getCoreId());
		coreWritable = !(coreCollect.isFull());
		if (coreWritable && serverWritable && coreCollect.getMasterServer() == -1) {
			canBeMaster = true;
		}
		coreCollect.join(serverId, canBeMaster || force);
		/*LOG.debug("core writable:" + coreWritable);
		LOG.debug("can be master:" + canBeMaster);
		LOG.debug("is writable:" + isWritable(coreCollect));*/
		if (coreWritable && isWritable(coreCollect)) {
			addWritableCore(coreId);
			//LOG.debug("add writable core(" + coreId + ").");
		}
	
		serverCollect.joinCore(coreId);
		if (coreWritable) {
			serverCollect.joinWritableCore(coreId);
			//LOG.debug("join writable core(" + coreId + ").");
		}
		if (canBeMaster) {
			serverCollect.joinPrimaryCore(coreId);
			//LOG.debug("join primary core(" + coreId + ").");
		}
		return true;
	}

    /* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#releaseServerCoreRelation(int, int)
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
	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#releaseCoreWriteRelation(int)
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
	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#releaseServerRelation(int)
	 */
	public boolean releaseServerRelation(int serverId) {
		ServerCollect serverCollect = serverList.get(serverId);
		if (serverCollect == null)
			return false;
		// copy all cores belongs to this index server
		HashSet<Integer> cores = serverCollect.getCoreSet();
		CoreCollect coreCollect;
		
		// release any of cores relation with this index server
		Iterator<Integer> iterator = cores.iterator();
		while (iterator.hasNext()) {
			int coreId = iterator.next();
			coreCollect = coreList.get(coreId);
			coreCollect.leave(serverId, (serverId == coreCollect.getMasterServer()));
			removeWritableCore(coreId);
		}

		// clean all cores list & writable core list & primary writable core list
		
		// serverCollect object nerver deleted, so no need to refind
		serverCollect.clearAll();
		return true;
	}
	
    /* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#isWritable(org.act.index.manager.CoreCollect)
	 */
    public boolean isWritable(CoreCollect coreCollect) {
    	/*TBSYS_LOG(DEBUG, "block(%u) is writable, is full(%s) , master_ds(%" PRI64_PREFIX "u)",
    	          block_collect->get_block_info()->block_id_, block_collect->is_full() ? "true" : "false",
    	          block_collect->get_master_ds());*/
    	if (coreCollect != null && !coreCollect.isFull() && coreCollect.getMasterServer() != -1) {
    		HashSet<Integer> servers = coreCollect.getServerSet();
    		if (aliveServerNum >= ManagerParam.MIN_REPLICATION && servers.size() < ManagerParam.MIN_REPLICATION) {
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
    
    // if core collect is writable, add to the list
    /* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#coreWritable(org.act.index.manager.CoreCollect)
	 */
    public boolean coreWritable(CoreCollect coreCollect) {
    	if (coreCollect == null) {
    		return false;
    	}
    	if (isWritable(coreCollect)) {
    		return addWritableCore(coreCollect.getCoreInfo().getCoreId());
    	}
    	return false;
    }

    /* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#serverWritable(org.act.index.manager.ServerCollect)
	 */
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
    /* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#updateServerInfo(org.act.index.common.ServerInfo, boolean)
	 */
    public boolean updateServerInfo(ServerInfo serverInfo, boolean isnew) {
    	// TODO server write lock
    	int serverId = serverInfo.getServerId();
    	ServerCollect serverCollect = getServerCollect(serverId, isnew);
    	if (serverCollect == null) {
    		return false;
    	}
    	//System.out.println("hello");
    	//System.out.println("a" + serverCollect.getServerInfo().getServerId());
    	serverInfo.setServerId(serverCollect.getServerInfo().getServerId());
    	serverCollect.setServerInfo(serverInfo);
    	//serverInfo = serverCollect.getServerInfo();
    	//System.out.println(serverInfo.getServerIp() + ":" + serverInfo.getServerPort());
    	if (serverInfo == null)
    		return false;
		//serverCollect.setServerInfo(serverInfo);
    	// TODO set lastUpdateTime
    	LOG.debug("update server:" + serverId);
    	serverInfo.setLastUpdateTime(Calendar.getInstance());
    	/*if (isnew) {
    		aliveServerNum++;
    	}*/
    	return true;
    }

    // update server status info
    /* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#updateGlobalInfo(org.act.index.common.ServerInfo, boolean)
	 */
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
    /* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#checkServer(long, java.util.HashSet, java.util.HashSet)
	 */
    public int checkServer(long serverDeadTime, HashSet<Integer> deadServerSet, HashSet<Integer> writableServerSet) {
    	deadServerSet.clear();
    	writableServerSet.clear();
    	
    	long now = Calendar.getInstance().getTimeInMillis() - serverDeadTime;
    	int checkWriteCount = Math.max(1, ManagerParam.MAX_WRITE_USER_COUNT);
    	int aliveServerCount = 0;
    	//check_write_count = std::max(1, SYSPARAM_NAMESERVER.max_write_file_count_);
    	globalInfo.reset();
		ServerInfo serverInfo;
		
		for (ServerCollect serverCollect : serverList) {
			if (serverCollect == null || !serverCollect.isAlive())
				continue;
			serverInfo = serverCollect.getServerInfo();
			if (serverInfo.getLastUpdateTime().getTimeInMillis() < now) {
				deadServerSet.add(serverInfo.getServerId());
			}
			else {
				globalInfo.setUseCapacity(globalInfo.getUseCapacity() + serverInfo.getUseCapacity());
	    		globalInfo.setTotalLoad(globalInfo.getTotalLoad() + serverInfo.getCurrentLoad());
	    		globalInfo.setTotalCapacity(globalInfo.getTotalCapacity() + serverInfo.getTotalCapacity());
	    		globalInfo.setTotalCoreCount(globalInfo.getTotalCoreCount() + serverInfo.getCoreCount());
	    		globalInfo.setUseCapacity(globalInfo.getUseCapacity() + 1);
	    		if (serverInfo.getCurrentLoad() > globalInfo.getMaxLoad())
	        		globalInfo.setMaxLoad(serverInfo.getCurrentLoad());
	        	
	        	if (serverInfo.getCoreCount() > globalInfo.getMaxCoreCount())
	        		globalInfo.setMaxCoreCount(serverInfo.getCoreCount());
	        	if (serverCollect.getPrimaryCoreSet().size() < checkWriteCount)
	        		writableServerSet.add(serverInfo.getServerId());
	        	aliveServerCount++;
	        }
		}
    	
    	// write to GlobalInfo
    	//globalInfo = statInfo;
		globalInfo.setAliveServerCount(aliveServerCount);
    	return deadServerSet.size();
    }

    // calculate the max core id
	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#calcMaxCoreId()
	 */
	public int calcMaxCoreId() {
		int size = coreList.size();
		int i;
		for (i = size - 1; i >= 0 && coreList.get(i) == null; i--)
			;
		return i;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.manager.ILayoutManager#getAvailCoreId()
	 */
	public int getAvailCoreId() {
		return 0;
	}
}
