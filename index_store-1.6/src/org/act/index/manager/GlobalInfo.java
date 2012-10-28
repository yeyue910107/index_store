/**
 * 
 */
package org.act.index.manager;

/**
 * @author yeyue
 *
 */
public class GlobalInfo {
	private int useCapacity;
	private int totalCapacity;
	private int totalCoreCount;
	private double totalLoad;
	private double maxLoad;
	private int maxCoreCount;
	private int aliveServerCount;
	/**
	 * 
	 */
	public GlobalInfo() {
		// TODO Auto-generated constructor stub
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
	 * @return the totalCoreCount
	 */
	public int getTotalCoreCount() {
		return totalCoreCount;
	}
	/**
	 * @param totalCoreCount the totalCoreCount to set
	 */
	public void setTotalCoreCount(int totalCoreCount) {
		this.totalCoreCount = totalCoreCount;
	}
	/**
	 * @return the totalLoad
	 */
	public double getTotalLoad() {
		return totalLoad;
	}
	/**
	 * @param totalLoad the totalLoad to set
	 */
	public void setTotalLoad(double totalLoad) {
		this.totalLoad = totalLoad;
	}
	/**
	 * @return the maxLoad
	 */
	public double getMaxLoad() {
		return maxLoad;
	}
	/**
	 * @param maxLoad the maxLoad to set
	 */
	public void setMaxLoad(double maxLoad) {
		this.maxLoad = maxLoad;
	}
	/**
	 * @return the maxCoreCount
	 */
	public int getMaxCoreCount() {
		return maxCoreCount;
	}
	/**
	 * @param maxCoreCount the maxCoreCount to set
	 */
	public void setMaxCoreCount(int maxCoreCount) {
		this.maxCoreCount = maxCoreCount;
	}
	/**
	 * @return the aliveServerCount
	 */
	public int getAliveServerCount() {
		return aliveServerCount;
	}
	/**
	 * @param aliveServerCount the aliveServerCount to set
	 */
	public void setAliveServerCount(int aliveServerCount) {
		this.aliveServerCount = aliveServerCount;
	}
	
	public void reset() {
		useCapacity = 0;
		totalCapacity = 0;
		totalCoreCount = 0;
		totalLoad = 0;
		maxLoad = 0;
		maxCoreCount = 0;
		aliveServerCount = 0;
	}
}
