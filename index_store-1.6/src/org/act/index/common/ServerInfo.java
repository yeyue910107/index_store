/**
 * 
 */
package org.act.index.common;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Calendar;

/**
 * @author yeyue
 *
 */
public class ServerInfo implements Serializable{
	private int serverId;
	private int useCapacity;
	private int totalCapacity;
	private double currentLoad;
	private int coreCount;
	private Calendar lastUpdateTime;
	private Calendar startupTime;
	private Calendar currentTime;
	private boolean status;
	private String serverIp;
	private int serverPort;

	/**
	 * 
	 */
	public ServerInfo() {
		// TODO Auto-generated constructor stub
		serverId = -1;
	}

	/**
	 * @param serverId
	 */
	public ServerInfo(int serverId) {
		super();
		this.serverId = serverId;
	}

	public ServerInfo(String ip, int port) {
		super();
		this.serverId = -1;
		this.serverIp = ip;
		this.serverPort = port;
	}
	
	public ServerInfo(int serverId, String ip, int port) {
		super();
		this.serverId = serverId;
		this.serverIp = ip;
		this.serverPort = port;
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
	 * @return the useCapacity
	 */
	public int getUseCapacity() {
		return useCapacity;
	}

	/**
	 * @param useCapacity the useCapacity to set
	 */
	public void setUseCapacity(int useCapacity) {
		this.useCapacity = useCapacity;
	}

	/**
	 * @return the totalCapacity
	 */
	public int getTotalCapacity() {
		return totalCapacity;
	}

	/**
	 * @param totalCapacity the totalCapacity to set
	 */
	public void setTotalCapacity(int totalCapacity) {
		this.totalCapacity = totalCapacity;
	}

	/**
	 * @return the currentLoad
	 */
	public double getCurrentLoad() {
		return currentLoad;
	}

	/**
	 * @param currentLoad the currentLoad to set
	 */
	public void setCurrentLoad(double currentLoad) {
		this.currentLoad = currentLoad;
	}

	/**
	 * @return the coreCount
	 */
	public int getCoreCount() {
		return coreCount;
	}

	/**
	 * @param coreCount the coreCount to set
	 */
	public void setCoreCount(int coreCount) {
		this.coreCount = coreCount;
	}

	/**
	 * @return the lastUpdateTime
	 */
	public Calendar getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * @param lastUpdateTime the lastUpdateTime to set
	 */
	public void setLastUpdateTime(Calendar lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * @return the startupTime
	 */
	public Calendar getStartupTime() {
		return startupTime;
	}

	/**
	 * @param startupTime the startupTime to set
	 */
	public void setStartupTime(Calendar startupTime) {
		this.startupTime = startupTime;
	}

	/**
	 * @return the currentTime
	 */
	public Calendar getCurrentTime() {
		return currentTime;
	}

	/**
	 * @param currentTime the currentTime to set
	 */
	public void setCurrentTime(Calendar currentTime) {
		this.currentTime = currentTime;
	}

	/**
	 * @return the status
	 */
	public boolean getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	/**
	 * @return the serverIp
	 */
	public String getServerIp() {
		return serverIp;
	}

	/**
	 * @param serverIp the serverIp to set
	 */
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	/**
	 * @return the serverPort
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

}
