/**
 * 
 */
package org.act.index.manager.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.act.index.common.sysparam.ManagerParam;
import org.act.index.manager.GlobalInfo;
import org.act.index.manager.LayoutManager;
import org.act.index.manager.ServerCollect;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 * 
 */
public class WriteStrategy extends BaseStrategy {
	private static final Logger LOG = Logger.getLogger(WriteStrategy.class);

	public WriteStrategy() {

	}

	public WriteStrategy(int seq, GlobalInfo globalInfo) {
		super(seq, globalInfo);
	}

	/*
	 * public int calc(ServerCollect serverCollect) { if
	 * (serverCollect.isDiskFull()) { LOG.debug("server(" +
	 * serverCollect.getServerInfo().getServerId() +
	 * ") is full, cannot join elect list."); return 0; } if
	 * (!check(serverCollect)) {
	 * LOG.debug("check failed, cannot join elect list."); return 0; } return
	 * serverCollect.getSeqno(); }
	 */

	public double calc(ServerCollect serverCollect) {
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

	public static int electWriteServer(LayoutManager layoutManager) {
		List<ServerCollect> serverList = layoutManager.getServerList();
		HashMap<Integer, Double> weights = new HashMap<Integer, Double>();
		BaseStrategy strategy = new WriteStrategy();
		for (ServerCollect server : serverList) {
			if (server != null && server.isAlive())
				weights.put(server.getServerInfo().getServerId(), strategy
						.calc(server));
		}
		Iterator iterator = weights.keySet().iterator();
		double minWeight = Double.MAX_VALUE;
		int electServerId = -1;
		while (iterator.hasNext()) {
			int serverId = (Integer)iterator.next(); 
			double weight = (Double)weights.get(serverId);
			if (weight < minWeight) {
				electServerId = serverId;
				minWeight = weight;
			}
		}
		return electServerId;
	}

	public static int electWriteServer(LayoutManager layoutManager,
			int electCount, List<Integer> electServerList) {
		List<ServerCollect> serverList = layoutManager.getServerList();
		HashMap<Integer, Double> weights = new HashMap<Integer, Double>();
		BaseStrategy strategy = new WriteStrategy();
		for (ServerCollect server : serverList) {
			if (server != null)
				weights.put(server.getServerInfo().getServerId(), strategy
						.calc(server));
		}
		Map.Entry<Integer, Double>[] sortedWeights = getSortedHashmapByValue(weights);
		for (Map.Entry<Integer, Double> entry : sortedWeights) {
			if (electCount == 0)
				break;
			electServerList.add(entry.getKey());
			electCount--;
		}
		return electServerList.size();
	}

	public static Map.Entry[] getSortedHashmapByValue(Map h) {
		Set set = h.entrySet();
		Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set
				.size()]);
		Arrays.sort(entries, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				Double key1 = Double.valueOf(((Map.Entry) arg0).getValue()
						.toString());
				Double key2 = Double.valueOf(((Map.Entry) arg1).getValue()
						.toString());
				return key1.compareTo(key2);
			}
		});

		return entries;
	}
	/*
	 * public static int electWriteServer(LayoutManager layoutManager, int
	 * electCount, List<Integer> electServerList) { WriteStrategy strategy = new
	 * WriteStrategy(layoutManager.getCurrentElectSeq(),
	 * layoutManager.getGlobalInfo()); int electSeq =
	 * layoutManager.getCurrentElectSeq(); int ret = electServer(strategy,
	 * layoutManager, electCount, electSeq, electServerList);
	 * layoutManager.setCurrentElectSeq(electSeq); return ret; }
	 * 
	 * public static int electServer(BaseStrategy strategy, LayoutManager meta,
	 * int electCount, int electSeq, List<Integer> electServerList) {
	 * //MultiKeyMap weights = new MultiKeyMap(); HashMap<Integer, Double>
	 * weights = new HashMap<Integer, Double>(); StoreWeight store = new
	 * StoreWeight(strategy, weights); List<ServerCollect> serverList =
	 * meta.getServerList(); int size = serverList.size(); for (int i = 0; i <
	 * size; i++) { if (serverList.get(i) != null) {
	 * store.op(serverList.get(i)); } }
	 * 
	 * int result = electServerNormal(weights, electCount, electSeq,
	 * electServerList); if (electSeq <= 0) { electSeq = 1; for (int i = 0; i <
	 * size; i++) { if (serverList.get(i) != null) { serverList.get(i).elect(1);
	 * } } } return result; }
	 * 
	 * public static int electServerNormal(HashMap<Integer, Double> weights, int
	 * electCount, int electSeq, List<Integer> electServerList) { if (electCount
	 * == 0) { LOG.debug("current elect count(" + electCount +
	 * ") <= 0, must be return."); return 0; } int needElectCount = electCount;
	 * Iterator iterator = weights.keySet().iterator(); while
	 * (iterator.hasNext()) { Object serverId = iterator.next(); ServerCollect
	 * serverCollect = (ServerCollect)weights.get(serverId);
	 * electServerList.add(serverCollect.getServerInfo().getServerId()); if
	 * (electSeq > 0) { serverCollect.elect(++electSeq); } --needElectCount; }
	 * LOG.debug("current elect count(" + (electCount - needElectCount) + ")");
	 * return electCount - needElectCount; }
	 */
}
