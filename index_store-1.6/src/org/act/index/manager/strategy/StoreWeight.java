/**
 * 
 */
package org.act.index.manager.strategy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.act.index.common.ServerInfo;
import org.act.index.manager.LayoutManager;
import org.act.index.manager.MetaManager;
import org.act.index.manager.ServerCollect;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 * 
 */
public class StoreWeight {
	private static final Logger LOG = Logger.getLogger(StoreWeight.class);
	public static final double CAPACITY_WEIGHT = 0.3;
	public static final double CPURATIO_WEIGHT = 0.3;
	public static final double CORECOUNT_WEIGHT = 0.4;
	private BaseStrategy strategy;
	// private HashMap<Integer, ServerCollect> weights;
	//private MultiKeyMap weights;
	private HashMap<Integer, Double> weights;
	private MetaManager metaManager;

	// TODO

	public StoreWeight() {

	}

	public StoreWeight(BaseStrategy strategy, HashMap<Integer, Double> weights) {
		this.strategy = strategy;
		this.weights = weights;
	}

	public HashMap<Integer, Double> getWeights() {
		return weights;
	}

	/*public void op(ServerCollect serverCollect) {
		int weight = strategy.calc(serverCollect);
		LOG.debug("weight(" + weight + ").");
		if (weight > 0) {
			weights.put(weight, serverCollect);
		}
	}*/
	
	public void op(ServerCollect serverCollect) {
		double weight = strategy.calc(serverCollect);
		LOG.debug("weight(" + weight + ").");
		if (weight > 0) {
			weights.put(serverCollect.getServerInfo().getServerId(), weight);
		}
	}
}
