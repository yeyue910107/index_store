/**
 * 
 */
package org.act.index.manager;

import java.util.List;

import org.act.index.common.Address;
import org.act.index.common.constdef.GlobalMessage;
import org.act.index.common.sysparam.ManagerParam;
import org.act.index.common.sysparam.PublicParam;

/**
 * @author Administrator
 *
 */
public class ReplicateLauncher {
	private MetaManager metaManager;
	private ReplicateExecutor executor;
	
	/**
     * scans core server map, find core which replica less than min_replication_
     * 1) 1 replica, replicating immediately.
     * 2) >1 replica, but < min_replication, build replicate plan.
     * @param coreCollect : check core replica size whether need replicate.
     * @return true : need build global plan 
     */
	public boolean check(CoreCollect coreCollect) {
		if(coreCollect.getServerSet().size() < ManagerParam.MIN_REPLICATION) //ÓÐserverËð»µ(master»òslaveËð»µ)
			return false;
		return true;
	}
	
	/**
	 * build a list of cores which need to replicate
	 * @param cores
	 * @return
	 */
	public int buildPlan(List<Integer> cores) {
		return 0;
	}
	
	public int emergencyReplicate(int coreId) {
		return 0;
	}
	
	public int balance() {
		return GlobalMessage.ISS_SUCCESS;
	}
}
