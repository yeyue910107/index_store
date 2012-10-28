/**
 * 
 */
package org.act.index.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.act.index.common.*;
import org.act.index.common.sysparam.*;
import org.act.index.common.constdef.*;

/**
 * @author yeyue
 * 
 */
public class CopyOfMetaManager {
	private static final Logger LOG = Logger.getLogger(CopyOfMetaManager.class);

	private LayoutManager layoutManager;
	private IndexManager indexManager;
	private Calendar zonesec;
	private Calendar lastRotateLogTime;
	private int currentWritingIndex;
	/**
	 * 
	 */
	public CopyOfMetaManager() {
		// TODO Auto-generated constructor stub
	}

	public void init() {

	}

	public boolean checkpoint() {
		return true;
	}

	public boolean reportCores(int serverId, ArrayList<CoreInfo> cores) {
		return true;
	}

	public boolean joinServer(ServerInfo serverInfo, boolean isnew) {
		if (!layoutManager.updateServerInfo(serverInfo, isnew)) {
			LOG.error("index server " + serverInfo.getServerId()
					+ " update failed.");
			return false;
		}
		layoutManager.updateGlobalInfo(serverInfo, isnew);
		return true;
	}

	public boolean leaveServer(int serverId) {
		if (layoutManager.releaseServerRelation(serverId)
				&& layoutManager.removeServerCollect(serverId))
			return true;
		else
			return false;
	}

	public CoreCollect addNewCore(int serverId) {
		ArrayList<Integer> electServerList = new ArrayList<Integer>();
		if (serverId == -1) {
			ServerCollect serverCollect = layoutManager.getServerCollect(serverId);
			if (serverCollect != null && layoutManager.serverWritable(serverCollect)) {
				electServerList.add(serverId);
			}
			else {
				LOG.error("add new core: server " + serverId + " not writable.");
				return null;
			}
		}
		// 
		int needServerSize = ManagerParam.MIN_REPLICATION - electServerList.size();
		if (needServerSize > 0) {
			// TODO elect write server
			//elect_write_ds(meta_mgr_, need_ds_size, elect_ds_list);
			if (electServerList.size() < ManagerParam.MIN_REPLICATION) {
				LOG.error("there is no any server can be writable.");
				return null;
			}
		}
		
		CoreCollect coreCollect = layoutManager.createCoreCollect();
		CoreInfo coreInfo = coreCollect.getCoreInfo();
		int coreId = coreInfo.getCoreId();
		
		ArrayList<Integer> addServerList = new ArrayList<Integer>();
		// TODO send add new core message to Server
		
	      /*for (uint32_t i = 0; i < elect_ds_list.size(); ++i)
	      {
	        TBSYS_LOG(DEBUG, "dataserver(%s)", tbsys::CNetUtil::addrToString(elect_ds_list[i]).c_str());
	        NewBlockMessage nbmsg;
	        nbmsg.add_new_id(new_block_id);
	        if (send_message_to_server(elect_ds_list[i], &nbmsg, NULL) == TFS_SUCCESS)
	        {
	          add_success_ds_list.push_back(elect_ds_list[i]);
	          TBSYS_LOG(INFO, "add block:%u on server:%s succeed", new_block_id, tbsys::CNetUtil::addrToString(
	                elect_ds_list[i]).c_str());
	        }
	        else
	        {
	          TBSYS_LOG(INFO, "add block:%u on server:%s failed", new_block_id, tbsys::CNetUtil::addrToString(
	                elect_ds_list[i]).c_str());
	        }
	      }*/

	      // TODO rollback;
	      /*if (add_success_ds_list.size() == 0)
	      {
	        oplog_sync_mgr_.log(block_info, OPLOG_REMOVE, add_success_ds_list);
	        ptr->mutex_.wrlock();
	        ptr->remove(new_block_id);
	        ptr->mutex_.unlock();
	        TBSYS_LOG(ERROR, "add block(%u) failed, rollback", new_block_id);
	        return NULL;
	      }*/
		for (int i = 0; i < addServerList.size(); i++) {
			layoutManager.buildServerCoreRelation(coreId, addServerList.get(i), false);
		}
		
		return coreCollect;
	}

	public boolean readCoreInfo(int coreId, HashSet<Integer> serverSet) {
		if (layoutManager.getServerNum() == 0) {
			LOG.error("server not found.");
			return false;
		}
		
		ArrayList<ServerCollect> serverCollects = new ArrayList<ServerCollect>();
		CoreCollect coreCollect = layoutManager.getCoreCollect(coreId);
		if (coreCollect == null) {
			LOG.error("core " + coreId + " not found.");
			return false;
		}
		HashSet<Integer> serverIdSet = coreCollect.getServerSet();
		int serverIdSetSize = serverIdSet.size();
		
		ServerCollect serverCollect = null;
		Iterator<Integer> iterator = serverIdSet.iterator();
		while (iterator.hasNext()) {
			serverCollect = layoutManager.getServerCollect(iterator.next());
			if (serverCollect != null)
				serverCollects.add(serverCollect);
		}
		
		if (serverCollects.size() == 0) {
			LOG.error("server not found by core " + coreId);
			return false;
		}
		
		// choose server
		int avgLoad = 0;
		/*int32_t average_load = accumulate(ds_collect_list.begin(), ds_collect_list.end(), 0, AddLoad())
        / ds_collect_list.size();*/
		int serverCollectsSize = serverCollects.size();
		for (int i = 0; i < serverCollectsSize; i++) {
			if (serverCollects.get(i).getServerInfo().getCurrentLoad() < avgLoad * 2)
				serverIdSet.add(serverCollects.get(i).getServerInfo().getServerId());
		}
		return true;
	}

	public int electWriteCore(HashSet<Integer> failServerSet) {
		int coreId = 0;
		int secondCoreId = 0;
		int sencondIndex = 0;

		HashSet<Integer> writableCoreSet = layoutManager.getWritableCoreSet();
		int writableCoreSetSize = writableCoreSet.size();
		int failServerSize = failServerSet.size();

		/*TBSYS_LOG(DEBUG, "current writable block list size(%u), failed dataserver size(%u)",
		          writable_block_list_size, fail_ds_size);

		      ServerCollect* ds_collect = NULL;
		      if (writable_block_list_size > 0)
		      {
		        if (current_writing_index_ >= writable_block_list_size)
		          current_writing_index_ = 0;

		        for (int32_t i = 0; (block_id == 0) && (i < writable_block_list_size); ++i)
		        {
		          block_id = writable_block_list.at(current_writing_index_);

		          //block is in use, turn to next
		          if (lease_mgr_.has_valid_lease(block_id))
		          {
		            ++current_writing_index_;
		            if (current_writing_index_ >= writable_block_list_size)
		              current_writing_index_ = 0;
		            second_block_id = block_id;
		            second_index = current_writing_index_;
		            block_id = 0;
		            continue;
		          }

		          for (uint32_t j = 0; j < fail_ds_size; ++j)
		          {
		            ds_collect = meta_mgr_.get_ds_collect(fail_ds.at(j));
		            if (ds_collect == NULL)
		              continue;

		            const std::set<uint32_t>& blocks = ds_collect->get_block_list();

		            if (blocks.find(block_id) != blocks.end())
		            {
		              block_id = 0;
		              ++current_writing_index_;
		              if (current_writing_index_ >= writable_block_list_size)
		                current_writing_index_ = 0;
		              TBSYS_LOG(INFO, "block(%u) in failed dataserer(%s)",
		                  block_id, tbsys::CNetUtil::addrToString(fail_ds.at(j)).c_str());
		              break;
		            }
		          }
		          ++current_writing_index_;
		          if (current_writing_index_ >= writable_block_list_size)
		            current_writing_index_ = 0;
		        }
		      }

		      if (block_id == 0)
		      {
		        if (second_block_id == 0)
		        {
		          TBSYS_LOG(ERROR, "there's no any writable block id(%u), index(%d)",
		              writable_block_list_size, current_writing_index_);
		        }
		        else
		        {
		          block_id = second_block_id;
		          current_writing_index_ = (second_index + rand()) % writable_block_list_size;
		          TBSYS_LOG(WARN, "block(%u) thers's no free writable block, return a busy one, index(%d)",
		              block_id, current_writing_index_);
		        }
		      }
		      return block_id;*/
		
		return -1;
	}

	public boolean writeCoreInfo(int coreId, int mode, HashSet<Integer> serverSet) {
		if (layoutManager.getAliveServerNum() == 0) {
			LOG.error("server not found.");
			return false;
		}
		
		if (mode == GetCoreType.CORE_CREATE) {
			/*VINT64 fail_ds;
	        block_id = elect_write_block(fail_ds);
	        if (block_id == 0)
	        {
	          TBSYS_LOG(ERROR, "elect write block faild...");
	          return EXIT_NO_BLOCK;
	        }*/
		}
		CoreCollect coreCollect = layoutManager.getCoreCollect(coreId);
		
		if (mode == GetCoreType.CORE_CREATE) {
			if (coreCollect == null) {
				coreCollect = addNewCore(-1);
				if (coreCollect == null) {
					LOG.error("add new core falied, server not found.");
					return false;
				}
			}
			else if (coreCollect.getServerSet().size() == 0) {
				if (coreCollect.isCreating()) {
					LOG.error("core " + coreId + " found meta data, but creating by another thread, must be return.");
					return false;
				}
				else {
					LOG.error("core " + coreId + " found meta data, but no server old it.");
					return false;
				}
			} 
		}
		
		if (coreCollect == null) {
			LOG.error("add new core " + coreId + " failed, server not found.");
			return false;
		}
		if (coreCollect.getServerSet().size() == 0) {
			LOG.error("add new core " + coreId + " failed, server not found.");
			return false;
		}
	      //version = block_collect->get_block_info()->version_;

	      //ds_list.assign(block_collect->get_ds()->begin(), block_collect->get_ds()->end());

	      //ptr->mutex_.unlock();

	      // TODO register a lease for write..
	      /*if (!(mode & BLOCK_NOLEASE))
	      {
	        lease_id = lease_mgr_.register_lease(block_id);
	        if (lease_id == WriteLease::INVALID_LEASE)
	        {
	          TBSYS_LOG(ERROR, "lease not found by block id(%u)", block_id);
	          return EXIT_CANNOT_GET_LEASE;
	        }
	      }*/
	      //std::string dsList = OpLogSyncManager::printDsList(ds_list);
	      //TBSYS_LOG(DEBUG, "elect current write block(%d), dsList(%s)", block_id, dsList.c_str());
		return true;
	}

	public boolean updateCoreInfo(CoreInfo coreInfo, int serverId, boolean addnew) {
		ServerCollect serverCollect = layoutManager.getServerCollect(serverId);
		if (serverCollect != null) {
			// TODO setLastUpdateTime
			serverCollect.getServerInfo().setLastUpdateTime(Calendar.getInstance());
		}

		int coreId = coreInfo.getCoreId();
		CoreCollect coreCollect = layoutManager.getCoreCollect(coreId);
		boolean isnew = false;
		if (coreCollect == null && addnew) {
			coreCollect = layoutManager.createCoreCollect();
			isnew = true;
		}

		if (coreCollect == null) {
			LOG.error("update core infomation error, core " + coreId
					+ " not found.");
			return false;
		}
		/*if (coreCollect.getCoreInfo().getVersion() < coreInfo.getVersion()) {
			layoutManager.getCoreList().set(coreId, coreCollect);
		} else {
			LOG.error("update core infomation error, core " + coreId
					+ "is not new version.");
			return false;
		}*/
		if (isnew && serverCollect != null) {
			layoutManager.buildServerCoreRelation(coreId, serverId, true);
		}
		if (coreCollect.isFull()) {
			layoutManager.releaseCoreWriteRelation(coreId);
		}

		return true;
	}

	public boolean updateServerInfo(ServerInfo serverInfo, boolean isnew) {
		
		return true;
	}

	public boolean updateGlobalInfo(ServerInfo serverInfo, boolean isnew) {
		return true;
	}

	public int writeCommit(CoreInfo coreInfo, int serverId, boolean status,
			boolean neednew) {
		return -1;
	}

	public int checkPrimaryWritableCore(int serverId, int addCoreCount,
			boolean promote) {
		return 0;
	}

	public int promotePrimaryWritableCore(ServerCollect serverCollect, int need) {
		return 0;
	}

	public CoreCollect getCorebyUser(int userId) {
		int coreId = layoutManager.getUserCollect(userId).getCoreId();
		
		return layoutManager.getCoreCollect(coreId);
	}

	public ServerCollect getServerByCore(int coreId) {
		int serverId = layoutManager.getCoreCollect(coreId).getMasterServer();
		return layoutManager.getServerCollect(serverId);
	}

}


