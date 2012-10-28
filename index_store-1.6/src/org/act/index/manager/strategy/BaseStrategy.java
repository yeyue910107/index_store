/**
 * 
 */
package org.act.index.manager.strategy;

import java.util.List;

import org.act.index.common.ServerInfo;
import org.act.index.common.sysparam.ManagerParam;
import org.act.index.common.sysparam.PublicParam;
import org.act.index.manager.GlobalInfo;
import org.act.index.manager.LayoutManager;
import org.act.index.manager.ServerCollect;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 *
 */
public abstract class BaseStrategy {
	private static final Logger LOG = Logger.getLogger(BaseStrategy.class);
	protected int seqno;
	protected int primaryWritableCoreCount;
	protected int use;
	protected double load;
	private GlobalInfo globalInfo;
	private int electSeqno;
	
	/**
	 * 
	 */
	public BaseStrategy() {
		// TODO Auto-generated constructor stub
	}
	
	public BaseStrategy(int seq, GlobalInfo globalInfo) {
		this.seqno = seq;
		this.globalInfo = globalInfo;
		this.primaryWritableCoreCount = 0;
		this.use = 0;
		this.load = 0;
		this.electSeqno = 0;
	}
	
	//public abstract void normalize(ServerCollect serverCollect);
	
	//public abstract int check(ServerCollect serverCollect);
	
	public static int percent(long v, long total) {
		if (total == 0)
	        total = v;
		double a = 100 * ((double)v / total);
		double b = 100;
		return (int)Math.max(a, b);
	}
	
	public static double percent(double v, double total) {
		if (total == 0)
	        total = v;
		double a = 100 * (v / total);
		double b = 100;
		return Math.max(a, b);
	}
	
	public static boolean checkAverage(double currentLoad, double totalLoad, int useCapacity, int totalUseCapacity, int aliveServerCount) {
		if (aliveServerCount == 0) {
			LOG.debug("alive server not found.");
			return false;
		}
		double avgLoad = totalLoad / aliveServerCount;
		int avgUse = totalUseCapacity / aliveServerCount;
		return (((currentLoad < avgLoad * 2) ||(totalLoad == 0)) && ((useCapacity <= avgUse * 2) || (totalUseCapacity == 0)));
	}

	public boolean check(ServerCollect serverCollect) {
		if (!serverCollect.isAlive()) {
			LOG.debug("dataserver(" + serverCollect.getServerInfo().getServerId() + ") is dead, cannot join.");
			return false;
		}
		ServerInfo serverInfo = serverCollect.getServerInfo();
		return checkAverage(serverInfo.getCurrentLoad(), globalInfo.getTotalLoad(), serverInfo.getUseCapacity(), globalInfo.getTotalCapacity(), globalInfo.getAliveServerCount());
		
	}
	
	public void normalize(ServerCollect serverCollect) {
		int primaryWritableCoreCount = serverCollect.getPrimaryCoreSet().size();
		ServerInfo serverInfo = serverCollect.getServerInfo();
		primaryWritableCoreCount = percent(primaryWritableCoreCount, ManagerParam.MAX_WRITE_USER_COUNT);
		seqno = percent(serverCollect.getSeqno(), electSeqno);
		if (globalInfo.getMaxCoreCount() == 0)
			use = percent(serverInfo.getUseCapacity(), serverInfo.getTotalCapacity());
		else
			use = percent(serverInfo.getCoreCount(), globalInfo.getMaxCoreCount());
		if (globalInfo.getMaxLoad() < 10)
			load = percent(serverInfo.getCurrentLoad(), 3000);
		else
			load = percent(serverInfo.getCurrentLoad(), globalInfo.getMaxLoad());
	}
	
	//public abstract int calc(ServerCollect serverCollect);
	public abstract double calc(ServerCollect serverCollect);
}
