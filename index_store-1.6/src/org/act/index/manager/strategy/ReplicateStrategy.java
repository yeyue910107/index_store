/**
 * 
 */
package org.act.index.manager.strategy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MultiMap;
import org.apache.log4j.Logger;
import org.act.index.common.Address;
import org.act.index.common.ServerInfo;
import org.act.index.common.sysparam.ManagerParam;
import org.act.index.manager.LayoutManager;
import org.act.index.manager.ServerCollect;

/**
 * @author Administrator
 *
 */
public class ReplicateStrategy extends BaseStrategy{
	private static final Logger LOG = Logger.getLogger(ReplicateStrategy.class);
	private int copyCount;
	private MultiMap counter;
	
	public static int incServerCount(MultiMap counter_, int serverId) {
		return 0;
	}
	
	public static int decServerCount(MultiMap counter_, int serverId) {
		return 0;
	}
	
	public static int getServerCount(MultiMap counter_, int serverId) {
		return 0;
	}
	
	public static Address getServerAddr(int serverId) {
		return null;
	}
	
	public int calcDst(ServerCollect serverCollect) {
		return 0;
	}
	
	public int calcSrc(ServerCollect serverCollect) {
		return 0;
	}

	@Override
	public double calc(ServerCollect serverCollect) {
		// TODO Auto-generated method stub
		double capacityLoad = (double)(serverCollect.getServerInfo().getUseCapacity()) / serverCollect.getServerInfo().getTotalCapacity();
		double cpuRatioLoad = serverCollect.getServerInfo().getCurrentLoad();
		//double coreCountLoad = serverCollect.getServerInfo().getCoreCount() / ManagerParam.MAX_CORE_COUNT_PER_SERVER;
		double coreCountLoad = (double)(serverCollect.getCoreSet().size()) / ManagerParam.MAX_CORE_COUNT_PER_SERVER;
		double weight = capacityLoad * StoreWeight.CAPACITY_WEIGHT + cpuRatioLoad * StoreWeight.CPURATIO_WEIGHT + coreCountLoad * StoreWeight.CORECOUNT_WEIGHT;
		LOG.info("server:" + serverCollect.getServerInfo().getServerId() + 
				" capacity load:" + capacityLoad + 
				" cpu ratio load:" + cpuRatioLoad + 
				" core count load:" + coreCountLoad + 
				" weight:" + weight);
		return weight;
	}
	
	public static HashMap<Integer, Double> setWeights(LayoutManager layout) {
		HashMap<Integer, Double> weights = new HashMap<Integer, Double>();
		BaseStrategy strategy = new ReplicateStrategy();
		//StoreWeight store = new StoreWeight(strategy, weights);
		List<ServerCollect> serverList = layout.getServerList();
		for (ServerCollect sc : serverList) {
			if (sc != null && sc.isAlive()) {
				ServerInfo si = sc.getServerInfo();
				weights.put(si.getServerId(), strategy.calc(sc));
			}
		}
		return weights;
	}

	public static int getLeastWeightServerId(LayoutManager layout, int excludeServerId1,
			int excludeServerId2, int excludeServerId3) {
		HashMap<Integer, Double> weights = setWeights(layout);
		Iterator iter = weights.entrySet().iterator();
		double leastWeight = Double.MAX_VALUE;
		int leastWeightServerId = -1;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			int serverId = (Integer)key;
			if ((serverId != excludeServerId1) && (serverId != excludeServerId2)
					&& (serverId != excludeServerId3)) {
				Object value = entry.getValue();
				if ((Double)value < leastWeight) {
					leastWeight = (Double)value;
					leastWeightServerId = serverId;
				}
			}
		}

		return leastWeightServerId;
	}
}
