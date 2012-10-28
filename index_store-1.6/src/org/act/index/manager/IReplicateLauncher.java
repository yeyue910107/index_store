/**
 * 
 */
package org.act.index.manager;

import java.util.List;
import org.act.index.common.Address;

/**
 * @author Administrator
 *
 */
public interface IReplicateLauncher {
	
	/**
     * scans core server map, find core which replica less than min_replication_
     * 1) 1 replica, replicating immediately.
     * 2) >1 replica, but < min_replication, build replicate plan.
     * @param coreCollect : check core replica size whether need replicate.
     * @return true : need build global plan 
     */
	boolean check(CoreCollect coreCollect);
	
	/**
	 * build a list of cores which need to replicate
	 * @param cores
	 * @return
	 */
	int buildPlan(List<Integer> cores);
	
	
	int execute(int coreId);
	
	int execute(List<Integer> replicatePlan);
	
	int doReplicate(Address src, Address dst, int coreId);
}
