/**
 * 
 */
package org.act.index.manager;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.act.index.common.*;
import org.act.index.common.sysparam.*;
import org.act.index.common.constdef.*;
import org.act.index.manager.strategy.WriteStrategy;
import org.act.index.server.IMetaService;
import org.act.index.server.MetaService;

/**
 * @author yeyue
 * 
 */
public class MetaManager {
	private static final Logger LOG = Logger.getLogger(MetaManager.class);

	private LayoutManager layoutManager;
	private IndexManager indexManager;
	private Calendar zonesec;
	private Calendar lastRotateLogTime;
	private int currentWritingIndex;
	/**
	 * 
	 */
	public MetaManager() {
		// TODO Auto-generated constructor stub
		layoutManager = new LayoutManager();
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#init()
	 */
	public int init() {
		layoutManager.init();
		layoutManager.calcMaxCoreId();
		return GlobalMessage.ISS_SUCCESS;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#getLayoutManager()
	 */
	public LayoutManager getLayoutManager() {
		return layoutManager;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#setLayoutManager(org.act.index.manager.ILayoutManager)
	 */
	public void setLayoutManager(LayoutManager layoutManager) {
		this.layoutManager = layoutManager;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#checkpoint()
	 */
	public boolean checkpoint() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#reportCores(int, java.util.ArrayList)
	 */
	public int reportCores(int serverId, ArrayList<CoreInfo> cores) {
		//NsRuntimeGlobalInformation * ngi = fs_name_system_->get_ns_global_info();
		
		ServerCollect serverCollect = layoutManager.getServerCollect(serverId);
		if (serverCollect == null || !serverCollect.isAlive()) {
			LOG.error("dataserver(" + serverId + ") report, cannot find server collect.");
			return ErrorMessage.EXIT_SERVER_NOT_FOUND;
		}
		// check whether server info has been reported
		int serverListSize = layoutManager.getServerNum();
		int coreSize = cores.size();
		boolean forceBeMaster = false;
		boolean first = false;
		ArrayList<Integer> expireServerList = new ArrayList<Integer>();
		
		layoutManager.releaseServerRelation(serverId);
		for (int i = 0; i < coreSize; i++) {
			// check core version, rebuilding metadata.
			CoreInfo newCoreInfo = cores.get(i);
			if (newCoreInfo.getCoreId() == -1) {
				LOG.warn("server(" + serverId + ") reprot, core = -1");
				continue;
			}
			expireServerList.clear();
			first = false;
			forceBeMaster = false;
			CoreCollect coreCollect = layoutManager.getCoreCollect(newCoreInfo.getCoreId());
			if (coreCollect == null) {
				LOG.info("core(" + newCoreInfo.getCoreId() + 
						") not found in server(" + serverId + "), must be create");
				coreCollect = layoutManager.createCoreCollect();
				first = true;
			}
			HashSet<Integer> serverSet = coreCollect.getServerSet();
			int currentCoreServerSize = serverSet.size();
			// TODO check core info
			CoreInfo coreInfo = coreCollect.getCoreInfo();
	          /*if ((current_block_ds_size > SYSPARAM_NAMESERVER.min_replication_)
	              && (find(ds_list->begin(), ds_list->end(), ds_id) == ds_list->end()))
	          {
	            if ((block_info->file_count_ > new_block_info.file_count_)
	                || (block_info->file_count_ <= new_block_info.file_count_
	                  && block_info->size_ != new_block_info.size_))
	            {
	              TBSYS_LOG(WARN, "block info not match");
	              if (ngi->owner_role_ == NS_ROLE_MASTER)
	                register_expire_block(expires, ds_id, block_info->block_id_);
	              continue;
	            }
	          }*/
			
		
		// check version
	          /*if (__gnu_cxx::abs(block_info->version_ - new_block_info.version_) > 2)
	          {
	            if (block_info->version_ > new_block_info.version_)
	            {
	              if (current_block_ds_size > 0)
	              {
	                TBSYS_LOG(WARN, "block(%u) in dataserver(%s) version error(%d:%d)",
	                    new_block_info.block_id_, tbsys::CNetUtil::addrToString(ds_id).c_str(),
	                    block_info->version_, new_block_info.block_id_);
	                if (ngi->owner_role_ == NS_ROLE_MASTER)
	                  register_expire_block(expires, ds_id, block_info->block_id_);
	                continue;
	              }
	              else
	              {
	                TBSYS_LOG(WARN, "block(%u) in dataserver(%s) version error(%d:%d), but not found dataserver",
	                    new_block_info.block_id_, tbsys::CNetUtil::addrToString(ds_id).c_str(),
	                    block_info->version_, new_block_info.block_id_);
	                memcpy(const_cast<BlockInfo*> (block_info), const_cast<BlockInfo*> (&new_block_info), BLOCKINFO_SIZE);
	              }
	            }
	            else if (block_info->version_ < new_block_info.version_)
	            {
	              int32_t old_version = block_info->version_;
	              memcpy(const_cast<BlockInfo*>(block_info), const_cast<BlockInfo*> (&new_block_info), BLOCKINFO_SIZE);
	              if (!first)
	              {
	                TBSYS_LOG(WARN, "block(%u) in dataserver(%s) version error(%d:%d),replace ns version, current dataserver size(%u)",
	                    new_block_info.block_id_, tbsys::CNetUtil::addrToString(ds_id).c_str(),
	                    old_version, new_block_info.version_, ds_list->size());
	                if (ngi->owner_role_ == NS_ROLE_MASTER)
	                {
	                  expire_ds_list = *ds_list;
	                  for (uint32_t k = 0; k < expire_ds_list.size(); ++k)
	                  {
	                    uint64_t other_server_id = expire_ds_list.at(k);
	                    register_expire_block(expires, other_server_id, block_info->block_id_);
	                    int32_t ret = ptr->release(new_block_info.block_id_, other_server_id);
	                    TBSYS_LOG(WARN, "release relation dataserver(%s), block(%u), result(%d)",
	                        tbsys::CNetUtil::addrToString(other_server_id).c_str(), block_info->block_id_, ret);
	                  }
	                }
	              }
	            }
	          }
	          else if (block_info->version_ < new_block_info.version_)
	          {
	            memcpy(const_cast<BlockInfo*> (block_info), const_cast<BlockInfo*> (&new_block_info), BLOCKINFO_SIZE);
	          }

	          if ((block_collect != NULL)
	              && (block_collect->is_full()) 
	              && (ds_list_size > 0) 
	              && (new_block_info.block_id_ % ds_list_size == ds_list_size - 1))
	          {
	            force_be_master = true;
	          }
	          TBSYS_LOG(DEBUG, "force_be_master(%s), is_full(%s), ds_list_size(%u), mode=%d", force_be_master ? "true": "false",
	              block_collect->is_full() ? "true" : "fase", ds_list_size, new_block_info.block_id_ % ds_list_size);
	        }*/
		// release the core in expire server
			for (int j = 0; j < expireServerList.size(); j++) {
				if (serverId == expireServerList.get(j))
					continue;
				serverCollect = layoutManager.getServerCollect(expireServerList.get(j));
				if (serverCollect != null)
					serverCollect.release(newCoreInfo.getCoreId());
				LOG.warn("release relation server(" + serverId + 
						"), core(" + newCoreInfo.getCoreId() +").");
			}
			layoutManager.buildServerCoreRelation(newCoreInfo.getCoreId(), serverId, forceBeMaster);
			LOG.debug("server("+ serverId + ") report: block(" + 
					newCoreInfo.getCoreId() + "), version("+ 
					newCoreInfo.getVersion() + "), usercount("+ 
					newCoreInfo.getUserCount() + "), size("+ 
					newCoreInfo.getSize() + "), seqno(" + 
					newCoreInfo.getSeqNo() + "), server count(" + 
					layoutManager.getCoreCollect(newCoreInfo.getCoreId()).getServerSet().size() + "), core count(" + 
					serverCollect.getCoreSet().size() + "), writable cores(" + 
					serverCollect.getWritableCoreSet().size() + "), primary write core count(" + 
					serverCollect.getPrimaryCoreSet() + "), total writable block count(" + 
					layoutManager.getWritableCoreSet().size() + ").");
		}
	    //meta_mgr_.sort();
		return GlobalMessage.ISS_SUCCESS;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#joinServer(org.act.index.common.ServerInfo, boolean)
	 */
	public boolean joinServer(ServerInfo serverInfo, boolean isnew) {
		if (!layoutManager.updateServerInfo(serverInfo, isnew)) {
			LOG.error("index server " + serverInfo.getServerId()
					+ " update failed.");
			return false;
		}
		//System.out.println("c" + serverInfo.getServerId());
		//layoutManager.updateGlobalInfo(serverInfo, isnew);
		return true;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#leaveServer(int)
	 */
	public boolean leaveServer(int serverId) {
		if (layoutManager.releaseServerRelation(serverId)
				&& layoutManager.removeServerCollect(serverId))
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#addNewUser(int)
	 */
	public UserCollect addNewUser(int coreId) {
		int electCoreId;
		if (coreId != -1) {
			CoreCollect coreCollect = layoutManager.getCoreCollect(coreId);
			if (coreCollect != null && layoutManager.coreWritable(coreCollect)) {
				electCoreId = coreId;
			}
			else {
				LOG.debug("add new user: core " + coreId + " not writable.");
				return null;
			}
		}
		// elect write core
		HashSet<Integer> failServers = new HashSet<Integer>();
		
		electCoreId = electWriteCore(failServers);
		System.out.println("add new user.");
		if (electCoreId == -1) {
			LOG.error("there is no any core can be writable.");
			return null;
		}

		//electCoreId = 0;
		//System.out.println("add new user.");
		UserCollect userCollect = layoutManager.createUserCollect();
		System.out.println("create user collect.");
		userCollect.setCoreId(electCoreId);
		long userId = userCollect.getUserInfo().getUserId();
		System.out.println(userCollect.getUserInfo().getUserId());
		LOG.info("add user:" + userId + " on core:" + electCoreId + " succeed.");
		return userCollect;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#addNewCore(int)
	 */
	public CoreCollect addNewCore(int serverId) {
		List<Integer> electServerList = new ArrayList<Integer>();
		if (serverId != -1) {
			ServerCollect serverCollect = layoutManager.getServerCollect(serverId);
			if (serverCollect != null && layoutManager.serverWritable(serverCollect)) {
				electServerList.add(serverId);
			}
			else {
				LOG.debug("add new core: server " + serverId + " not writable.");
				return null;
			}
		}
		int electServerId = WriteStrategy.electWriteServer(layoutManager);
		LOG.debug("elect server:" + electServerId);
		if (electServerId == -1)
			return null;
		CoreCollect coreCollect = layoutManager.createCoreCollect();
		coreCollect.setCreatingFlag(true);
		//return coreCollect;
		CoreInfo coreInfo = coreCollect.getCoreInfo();
		int coreId = coreInfo.getCoreId();
		ServerCollect electServer = layoutManager.getServerCollect(electServerId);
		//Address addr = electServer.getServerAddr();
		try {
			String url = "rmi://" + electServer.getServerInfo().getServerIp() + 
			":" + electServer.getServerInfo().getServerPort() + 
			"/meta@server" + electServerId;
			IMetaService metaService = (IMetaService)Naming.lookup(url);
			metaService.createCore(coreId);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOG.info("add core:" + coreId + " on server:" + electServerId + " failed.");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOG.info("add core:" + coreId + " on server:" + electServerId + " failed.");
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOG.info("add core:" + coreId + " on server:" + electServerId + " failed.");
		}
		layoutManager.buildServerCoreRelation(coreId, electServerId, false);
		// 
		/*int needServerSize = ManagerParam.MIN_REPLICATION - electServerList.size();
		if (needServerSize > 0) {
			// TODO elect write server
			int electSize = WriteStrategy.electWriteServer(layoutManager, needServerSize, electServerList);
			if (electSize < ManagerParam.MIN_REPLICATION) {
				LOG.error("there is no any server can be writable.");
				//return null;
			}
		}
		
		CoreCollect coreCollect = layoutManager.createCoreCollect();
		coreCollect.setCreatingFlag(true);
		//return coreCollect;
		CoreInfo coreInfo = coreCollect.getCoreInfo();
		int coreId = coreInfo.getCoreId();
		
		ArrayList<Integer> addServerList = new ArrayList<Integer>();
		// TODO send add new core message to Server
		
		for (int i = 0; i < electServerList.size(); i++) {
			int electServerId = electServerList.indexOf(i);
			LOG.debug("elect server:" + electServerId);
			ServerCollect electServer = layoutManager.getServerCollect(electServerId);
			//Address addr = electServer.getServerAddr();
			try {
				String url = "rmi://" + electServer.getServerInfo().getServerIp() + 
				":" + electServer.getServerInfo().getServerPort() + 
				"/meta@server" + electServerId;
				IMetaService metaService = (IMetaService)Naming.lookup(url);
				metaService.createCore(coreId);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOG.info("add core:" + coreId + " on server:" + electServerId + " failed.");
				continue;
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOG.info("add core:" + coreId + " on server:" + electServerId + " failed.");
				continue;
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOG.info("add core:" + coreId + " on server:" + electServerId + " failed.");
				continue;
			}
			addServerList.add(electServerId);
			LOG.info("add core:" + coreId + " on server:" + electServerId + " succeed.");
		}
		
		for (int i = 0; i < addServerList.size(); i++) {
			layoutManager.buildServerCoreRelation(coreId, addServerList.get(i), false);
		}*/
		return coreCollect;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#readCoreInfo(int, java.util.HashSet)
	 */
	public int readCoreInfo(int coreId, HashSet<Integer> serverSet) {
		if (layoutManager.getServerNum() == 0) {
			LOG.error("server not found.");
			return ErrorMessage.EXIT_NO_SERVER;
		}
		
		ArrayList<ServerCollect> serverCollects = new ArrayList<ServerCollect>();
		CoreCollect coreCollect = layoutManager.getCoreCollect(coreId);
		if (coreCollect == null) {
			LOG.error("core " + coreId + " not found.");
			return ErrorMessage.EXIT_CORE_NOT_FOUND;
		}
		serverSet.addAll(coreCollect.getServerSet());
		LOG.info("server set size:" + serverSet.size());
		//int serverSetSize = serverSet.size();
		
		/*ServerCollect serverCollect = null;
		Iterator<Integer> iterator = serverSet.iterator();
		while (iterator.hasNext()) {
			serverCollect = layoutManager.getServerCollect(iterator.next());
			if (serverCollect != null)
				serverCollects.add(serverCollect);
		}
		
		if (serverCollects.size() == 0) {
			LOG.error("server not found by core " + coreId);
			return ErrorMessage.EXIT_NO_SERVER;
		}
		
		// choose server
		int avgLoad = 0;
		int32_t average_load = accumulate(ds_collect_list.begin(), ds_collect_list.end(), 0, AddLoad())
        / ds_collect_list.size();
		int serverCollectsSize = serverCollects.size();
		for (int i = 0; i < serverCollectsSize; i++) {
			if (serverCollects.get(i).getServerInfo().getCurrentLoad() < avgLoad * 2)
				serverSet.add(serverCollects.get(i).getServerInfo().getServerId());
		}*/
		return GlobalMessage.ISS_SUCCESS;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#electWriteCore(java.util.HashSet)
	 */
	/*public int electWriteCore(HashSet<Integer> failServerSet) {
		int coreId = -1;
		int secondCoreId = -1;
		int sencondIndex = -1;

		HashSet<Integer> writableCoreSet = layoutManager.getWritableCoreSet();
		int writableCoreSetSize = writableCoreSet.size();
		int failServerSize = failServerSet.size();

		LOG.debug("current writable core set size(" + writableCoreSetSize + 
				"), failed server size(" + failServerSize + ").");
		ServerCollect serverCollect = new ServerCollect();
		if (writableCoreSetSize > 0) {
			if (currentWritingIndex >= writableCoreSetSize)
				currentWritingIndex = 0;
			Iterator<Integer> iter1 = writableCoreSet.iterator();
			while (iter1.hasNext()) {
				coreId = iter1.next();
				Iterator<Integer> iter2 = failServerSet.iterator();
				while (iter2.hasNext()) {
					serverCollect = layoutManager.getServerCollect(iter2.next());
					if (serverCollect == null)
						continue;
					HashSet<Integer> cores = serverCollect.getCoreSet();
					if (cores.contains(coreId)) {
						coreId = 0;
						++currentWritingIndex;
						if (currentWritingIndex >= writableCoreSetSize)
							currentWritingIndex = 0;
						LOG.info("core(" + coreId + ") in failed server.");
						break;
					}
				}
				++currentWritingIndex;
				if (currentWritingIndex >= writableCoreSetSize)
					currentWritingIndex = 0;
			}
		}
		if (coreId == -1) {
			if (secondCoreId == -1) {
				LOG.error("there's no any writable core id(" + writableCoreSetSize + 
						"), index(" + currentWritingIndex + ")");
			}
			else {
				coreId = secondCoreId;
				Random random = new Random();
				currentWritingIndex = (sencondIndex + random.nextInt()) % writableCoreSetSize;
				LOG.error("core(" + coreId + 
						"), there's no any free core, return a busy one, index(" + currentWritingIndex + ")");
			}
		}
		return coreId;
	}*/

	public int electWriteCore(HashSet<Integer> failServerSet) {
		int coreId = -1;

		HashSet<Integer> writableCoreSet = layoutManager.getWritableCoreSet();
		int writableCoreSetSize = writableCoreSet.size();
		int failServerSize = failServerSet.size();

		LOG.debug("current writable core set size(" + writableCoreSetSize + 
				"), failed server size(" + failServerSize + ").");
		if (writableCoreSetSize > 0) {
			Random random = new Random();
			int index = random.nextInt() % writableCoreSetSize;
			Iterator<Integer> iter1 = writableCoreSet.iterator();
			while (iter1.hasNext() && index > 0) {
				iter1.next();
				index--;
			}
			coreId = iter1.next();
		}
		if (coreId == -1) {
			LOG.error("there's no any writable core id(" + writableCoreSetSize + 
				"), index(" + currentWritingIndex + ")");
		}
		return coreId;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#writeCoreInfo(int, int, java.util.HashSet)
	 */
	public int writeCoreInfo(int coreId, int mode, HashSet<Integer> serverSet) {
		if (layoutManager.getAliveServerNum() == 0) {
			LOG.error("server not found.");
			return ErrorMessage.EXIT_NO_SERVER;
		}
		
		if (mode == GetCoreType.CORE_CREATE) {
			HashSet<Integer> failServers = new HashSet<Integer>();
			coreId = electWriteCore(failServers);
			if (coreId == 0) {
				LOG.error("elect write core failed.");
				return ErrorMessage.EXIT_NO_CORE;
			}
		}
		CoreCollect coreCollect = layoutManager.getCoreCollect(coreId);
		
		if (mode == GetCoreType.CORE_CREATE) {
			if (coreCollect == null) {
				coreCollect = addNewCore(-1);
				if (coreCollect == null) {
					LOG.error("add new core falied, server not found.");
					return ErrorMessage.EXIT_NO_SERVER;
				}
			}
			else if (coreCollect.getServerSet().size() == 0) {
				if (coreCollect.isCreating()) {
					LOG.error("core " + coreId + " found meta data, but creating by another thread, must be return.");
					return ErrorMessage.EXIT_NO_CORE;
				}
				else {
					LOG.error("core " + coreId + " found meta data, but no server old it.");
					return ErrorMessage.EXIT_NO_SERVER;
				}
			} 
		}
		
		if (coreCollect == null) {
			LOG.error("add new core " + coreId + " failed, server not found.");
			return ErrorMessage.EXIT_NO_CORE;
		}
		if (coreCollect.getServerSet().size() == 0) {
			LOG.error("add new core " + coreId + " failed, server not found.");
			return ErrorMessage.EXIT_NO_SERVER;
		}
		return GlobalMessage.ISS_SUCCESS;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#updateCoreInfo(org.act.index.common.CoreInfo, int, boolean)
	 */
	public int updateCoreInfo(CoreInfo coreInfo, int serverId, boolean addnew) {
		ServerCollect serverCollect = layoutManager.getServerCollect(serverId);
		if (serverCollect != null) {
			// TODO setLastUpdateTime
			serverCollect.getServerInfo().setLastUpdateTime(Calendar.getInstance());
		}

		int coreId = coreInfo.getCoreId();
		CoreCollect coreCollect = layoutManager.getCoreCollect(coreId);
		//boolean isnew = false;
		if (coreCollect == null && addnew) {
			coreCollect = layoutManager.createCoreCollect();
			//isnew = true;
		}

		if (coreCollect == null) {
			LOG.error("update core infomation error, core " + coreId
					+ " not found.");
			return ErrorMessage.EXIT_CORE_NOT_FOUND;
		}
		/*if (coreCollect.getCoreInfo().getVersion() < coreInfo.getVersion()) {
			layoutManager.getCoreList().set(coreId, coreCollect);
		} else {
			LOG.error("update core infomation error, core " + coreId
					+ "is not new version.");
			return false;
		}*/
		//if (isnew && serverCollect != null) {
		if (serverCollect != null && serverCollect.isAlive()) {
			if (!layoutManager.buildServerCoreRelation(coreId, serverId, false)) {
				return GlobalMessage.ISS_ERROR;
			}
			LOG.info("build server(" + serverId + ") core(" + coreId + ") relation.");
		}
		if (coreCollect.isFull()) {
			if (!layoutManager.releaseCoreWriteRelation(coreId)) {
				return GlobalMessage.ISS_ERROR;
			}
			LOG.info("release core(" + coreId + ") write relation.");
		}
		
		return GlobalMessage.ISS_SUCCESS;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#removeCoreInfo(int, int)
	 */
	public int removeCoreInfo(int coreId, int serverId) {
		ServerCollect serverCollect = layoutManager.getServerCollect(serverId);
		if (serverCollect != null) {
			// TODO setLastUpdateTime
			serverCollect.getServerInfo().setLastUpdateTime(Calendar.getInstance());
			serverCollect.leaveCore(coreId);
		}
		CoreCollect coreCollect = layoutManager.getCoreCollect(coreId);
		if (coreCollect != null) {
			coreCollect.leave(serverId, coreCollect.getMasterServer() == serverId);
		}
		layoutManager.releaseServerCoreRelation(coreId, serverId);
		return GlobalMessage.ISS_SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#updateServerInfo(org.act.index.common.ServerInfo, boolean)
	 */
	public boolean updateServerInfo(ServerInfo serverInfo, boolean isnew) {
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#updateGlobalInfo(org.act.index.common.ServerInfo, boolean)
	 */
	public boolean updateGlobalInfo(ServerInfo serverInfo, boolean isnew) {
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#writeCommit(org.act.index.common.CoreInfo, int, boolean, boolean)
	 */
	public int writeCommit(CoreInfo coreInfo, int serverId, boolean status,
			boolean neednew) {
		
		return -1;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#checkPrimaryWritableCore(int, int, boolean)
	 */
	public int checkPrimaryWritableCore(int serverId, int addCoreCount,
			boolean promote) {
		ServerCollect serverCollect = layoutManager.getServerCollect(serverId);
		int needAddCoreCount = 0;
		if (serverCollect != null) {
			if (serverCollect.isDiskFull())
				return 0;
			
			// check whether need to add core
			int current = serverCollect.getPrimaryCoreSet().size();
			if (current >= ManagerParam.MAX_WRITE_USER_COUNT) {
				LOG.info("check primary writable core in server(" + serverId + 
						")current primary core count(" + current + 
						") >= max write user count(" + ManagerParam.MAX_WRITE_USER_COUNT + 
						"), no need to add new core.");
				return 0;
			}
			needAddCoreCount = Math.min(addCoreCount, ManagerParam.MAX_WRITE_USER_COUNT - current);
			
			LOG.info("check primary writable core in server(" + serverId + 
					")current primary core count(" + current + 
					"), need add core count(" + needAddCoreCount + ").");
			
			if (needAddCoreCount > 0) {
				if (promote)
					promotePrimaryWritableCore(serverCollect, needAddCoreCount);
				
				// if core is still not enough, then add new core
				for (int i = 0; i < needAddCoreCount; i++) {
					addNewCore(serverId);
				}
			}
		}
		return addCoreCount;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#promotePrimaryWritableCore(org.act.index.manager.ServerCollect, int)
	 */
	public int promotePrimaryWritableCore(ServerCollect serverCollect, int need) {
		if (serverCollect == null) {
			LOG.error("server not exist, server collect is null.");
			return ErrorMessage.EXIT_NO_SERVER;
		}
		
		HashSet<Integer> writableCores = serverCollect.getWritableCoreSet();
		HashSet<Integer> primaryWritableCores = serverCollect.getPrimaryCoreSet();
		if (writableCores.size() < primaryWritableCores.size()) {
			LOG.warn("not found primary writable core, writable core size(" + writableCores.size() + 
					") <= primary writable core size(" + primaryWritableCores.size() + ").");
			return ErrorMessage.EXIT_NO_CORE;
			
		}
		CoreCollect coreCollect = new CoreCollect();
		boolean promoted = false;
		Iterator<Integer> iterator = writableCores.iterator();
		while (iterator.hasNext()) {
			// if writable core is still available
			int coreId = iterator.next();
			if (!primaryWritableCores.contains(coreId)) {
				coreCollect = layoutManager.getCoreCollect(coreId);
				
				if (coreCollect != null && !coreCollect.isFull() && 
					coreCollect.getMasterServer() == -1 && 
					coreCollect.getServerSet().size() >= ManagerParam.MIN_REPLICATION) {
					coreCollect.setMasterServer(serverCollect.getServerInfo().getServerId());
					if (layoutManager.coreWritable(coreCollect)) {
						promoted = true;
						need--;
					}
				}
				// join to primary writable core
				if (promoted) {
					serverCollect.joinPrimaryCore(coreId);
				}
			}
		}
		return GlobalMessage.ISS_SUCCESS;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#getCorebyUser(long)
	 */
	public CoreCollect getCorebyUser(long userId) {
		int coreId = layoutManager.getUserCollect(userId).getCoreId();
		return layoutManager.getCoreCollect(coreId);
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IMetaManager#getServerByCore(int)
	 */
	public ServerCollect getServerByCore(int coreId) {
		int serverId = layoutManager.getCoreCollect(coreId).getMasterServer();
		return layoutManager.getServerCollect(serverId);
	}

}


