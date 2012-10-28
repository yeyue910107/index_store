/**
 * 
 */
package org.act.index.common;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * @author Administrator
 *
 */
public class Address implements Serializable {
	private InetSocketAddress addr;
	private int coreId;
	private int serverId;
	private String url;
	private boolean isMaster;
	
	public Address() {
		// TODO Auto-generated constructor stub
	}
	
	public Address(String ip, int port) {
		this.addr = new InetSocketAddress(ip, port);
		this.url = addr.toString();
	}
	
	public Address(InetSocketAddress addr, int coreId) {
		super();
		this.addr = addr;
		this.coreId = coreId;
		this.url = addr.toString();
		this.isMaster = false;
	}
	
	public Address(String ip, int port, int serverId, int coreId) {
		super();
		this.addr = new InetSocketAddress(ip, port);
		this.serverId = serverId;
		this.coreId = coreId;
		this.isMaster = false;
	}
	
	public Address(String ip, int port, int serverId, int coreId, boolean isMaster) {
		super();
		this.addr = new InetSocketAddress(ip, port);
		this.serverId = serverId;
		this.coreId = coreId;
		this.isMaster = isMaster;
	}
	
	public Address(int serverId, int coreId) {
		super();
		this.serverId = serverId;
		this.coreId = coreId;
		this.isMaster = false;
	}
	
	/**
	 * @return the coreId
	 */
	public int getCoreId() {
		return coreId;
	}

	/**
	 * @param coreId the coreId to set
	 */
	public void setCoreId(int coreId) {
		this.coreId = coreId;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the isMaster
	 */
	public boolean isMaster() {
		return isMaster;
	}

	/**
	 * @param isMaster the isMaster to set
	 */
	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return url;
	}

	/**
	 * @return the serverId
	 */
	public int getServerId() {
		return serverId;
	}

	/**
	 * @param serverId the serverId to set
	 */
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	/**
	 * @return the addr
	 */
	public InetSocketAddress getAddr() {
		return addr;
	}

	/**
	 * @param addr the addr to set
	 */
	public void setAddr(InetSocketAddress addr) {
		this.addr = addr;
	}
}
