/**
 * 
 */
package org.act.index.manager;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.util.HashSet;

import org.act.index.common.Address;
import org.act.index.common.ServerInfo;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;


/**
 * @author yeyue
 *
 */
public class ServerCollect {
	private static final Logger LOG = Logger.getLogger(ServerCollect.class);
	
	private HashSet<Integer> coreSet;
	private HashSet<Integer> writableCoreSet;
	private HashSet<Integer> primaryCoreSet;
	private ServerInfo serverInfo;
	private boolean alive;

	private int seqno;
	//private SolrServer solrServer;
	
	
	/**
	 * 
	 */
	public ServerCollect() {
		// TODO Auto-generated constructor stub
		serverInfo = new ServerInfo();
		coreSet = new HashSet<Integer>();
		writableCoreSet = new HashSet<Integer>();
		primaryCoreSet = new HashSet<Integer>();
		seqno = 1;
		alive = true;
	}

	public ServerCollect(int serverId) {
		serverInfo = new ServerInfo(serverId);
		coreSet = new HashSet<Integer>();
		writableCoreSet = new HashSet<Integer>();
		primaryCoreSet = new HashSet<Integer>();
		seqno = 1;
		alive = true;
	}

	/**
	 * @return the coreSet
	 */
	public HashSet<Integer> getCoreSet() {
		return coreSet;
	}

	/**
	 * @param coreSet the coreSet to set
	 */
	public void setCoreSet(HashSet<Integer> coreSet) {
		this.coreSet = coreSet;
	}
	
	/**
	 * @return the serverInfo
	 */
	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	/**
	 * @param serverInfo the serverInfo to set
	 */
	public void setServerInfo(ServerInfo serverInfo) {
		this.serverInfo = serverInfo;
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
	 * @return the primaryCoreSet
	 */
	public HashSet<Integer> getPrimaryCoreSet() {
		return primaryCoreSet;
	}

	/**
	 * @param primaryCoreSet the primaryCoreSet to set
	 */
	public void setPrimaryCoreSet(HashSet<Integer> primaryCoreSet) {
		this.primaryCoreSet = primaryCoreSet;
	}

	/**
	 * @return the seqno
	 */
	public int getSeqno() {
		return seqno; 
	}

	/**
	 * @param seqno the seqno to set
	 */
	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}


	public void reset() {
		alive = true;
		serverInfo = new ServerInfo();
		coreSet.clear();
		writableCoreSet.clear();
		primaryCoreSet.clear();
	}
	
	/**
	 * @return the alive
	 */
	public boolean isAlive() {
		return alive;
	}

	public void dead() {
		alive = false;
	}
	
	public void joinCore(int coreId) {
		synchronized (coreSet) {
			coreSet.add(coreId);
		}
	}
	
	public void leaveCore(int coreId) {
		synchronized (coreSet) {
			coreSet.remove(coreId);
		}
	}
	
	public void joinWritableCore(int coreId) {
		synchronized (writableCoreSet) {
			writableCoreSet.add(coreId);
		}
	}
	
	public void leaveWritableCore(int coreId) {
		synchronized (writableCoreSet) {
			writableCoreSet.remove(coreId);
		}
	}
	
	public boolean joinPrimaryCore(int coreId) {
		if (!primaryCoreSet.contains(coreId)) {
			synchronized (primaryCoreSet) {
				primaryCoreSet.add(coreId);
			}
			return true;
		}
		return false;
	}
	
	public boolean leavePrimaryCore(int coreId) {
		if (primaryCoreSet.contains(coreId)) {
			synchronized (primaryCoreSet) {
				primaryCoreSet.remove(coreId);
			}
			return true;
		}
		return false;
	}
	
	public void release(int coreId) {
		leaveCore(coreId);
		leaveWritableCore(coreId);
		leavePrimaryCore(coreId);
	}
	
	public void clearAll() {
		synchronized (this) {
			coreSet.clear();
			writableCoreSet.clear();
			primaryCoreSet.clear();
		}
	}
	
	public int getMaxCoreId() {
		// TODO
		return 0;
	}
	
	public boolean isDiskFull() {
		return false;
	}
	
	public int elect(int seq) {
		this.seqno = seq;
		return seq;
	}
	
	public void show() {
		LOG.info("server:" + serverInfo.getServerId());
		LOG.info("serverInfo:");
		LOG.info("\tserver id:" + serverInfo.getServerId());
		LOG.info("\tserver ip:" + serverInfo.getServerIp());
		LOG.info("\tserver port:" + serverInfo.getServerPort());
		LOG.info("\tcore count:" + serverInfo.getCoreCount());
		LOG.info("cores:");
		for (int i : coreSet) {
			LOG.info("\t" + i);
		}
		LOG.info("writable cores:");
		for (int i : writableCoreSet) {
			LOG.info("\t" + i);
		}
		LOG.info("primary writable core set:");
		for (int i : primaryCoreSet) {
			LOG.info("\t" + i);
		}
	}
}
