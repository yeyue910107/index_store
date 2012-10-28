/**
 * 
 */
package org.act.index.common;

/**
 * @author Administrator
 *
 */
public class MonitorInfo {
	 /** 可使用内存. */
    private long totalMemory;
    
    /**  剩余内存. */
    private long freeMemory;
    
    /** 最大可使用内存. */
    private long maxMemory;
    
    /** 操作系统. */
    private String osName;
    
    /** 总的物理内存. */
    private long totalMemorySize;
    
    /** 剩余的物理内存. */
    private long freePhysicalMemorySize;
    
    /** 已使用的物理内存. */
    private long usedMemory;
    
    /** 总的磁盘容量. */
    private long totalDiskSize;
    
    /** 剩余的磁盘容量. */
    private long freeDiskSize;
    
    /** 已使用的磁盘容量. */
    private long usedDiskSize;
    
    /** 线程总数. */
    private int totalThread;
    
    /** cpu使用率. */
    private double cpuRatio;

	/**
	 * @return the totalMemory
	 */
	public long getTotalMemory() {
		return totalMemory;
	}

	/**
	 * @param totalMemory the totalMemory to set
	 */
	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	/**
	 * @return the freeMemory
	 */
	public long getFreeMemory() {
		return freeMemory;
	}

	/**
	 * @param freeMemory the freeMemory to set
	 */
	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	/**
	 * @return the maxMemory
	 */
	public long getMaxMemory() {
		return maxMemory;
	}

	/**
	 * @param maxMemory the maxMemory to set
	 */
	public void setMaxMemory(long maxMemory) {
		this.maxMemory = maxMemory;
	}

	/**
	 * @return the osName
	 */
	public String getOsName() {
		return osName;
	}

	/**
	 * @param osName the osName to set
	 */
	public void setOsName(String osName) {
		this.osName = osName;
	}

	/**
	 * @return the totalMemorySize
	 */
	public long getTotalMemorySize() {
		return totalMemorySize;
	}

	/**
	 * @param totalMemorySize the totalMemorySize to set
	 */
	public void setTotalMemorySize(long totalMemorySize) {
		this.totalMemorySize = totalMemorySize;
	}

	/**
	 * @return the freePhysicalMemorySize
	 */
	public long getFreePhysicalMemorySize() {
		return freePhysicalMemorySize;
	}

	/**
	 * @param freePhysicalMemorySize the freePhysicalMemorySize to set
	 */
	public void setFreePhysicalMemorySize(long freePhysicalMemorySize) {
		this.freePhysicalMemorySize = freePhysicalMemorySize;
	}

	/**
	 * @return the usedMemory
	 */
	public long getUsedMemory() {
		return usedMemory;
	}

	/**
	 * @param usedMemory the usedMemory to set
	 */
	public void setUsedMemory(long usedMemory) {
		this.usedMemory = usedMemory;
	}

	/**
	 * @return the totalDiskSize
	 */
	public long getTotalDiskSize() {
		return totalDiskSize;
	}

	/**
	 * @param totalDiskSize the totalDiskSize to set
	 */
	public void setTotalDiskSize(long totalDiskSize) {
		this.totalDiskSize = totalDiskSize;
	}

	/**
	 * @return the freeDiskSize
	 */
	public long getFreeDiskSize() {
		return freeDiskSize;
	}

	/**
	 * @param freeDiskSize the freeDiskSize to set
	 */
	public void setFreeDiskSize(long freeDiskSize) {
		this.freeDiskSize = freeDiskSize;
	}

	/**
	 * @return the usedDiskSiza
	 */
	public long getUsedDiskSize() {
		return usedDiskSize;
	}

	/**
	 * @param usedDiskSiza the usedDiskSiza to set
	 */
	public void setUsedDiskSize(long usedDiskSize) {
		this.usedDiskSize = usedDiskSize;
	}

	/**
	 * @return the totalThread
	 */
	public int getTotalThread() {
		return totalThread;
	}

	/**
	 * @param totalThread the totalThread to set
	 */
	public void setTotalThread(int totalThread) {
		this.totalThread = totalThread;
	}

	/**
	 * @return the cpuRatio
	 */
	public double getCpuRatio() {
		return cpuRatio;
	}

	/**
	 * @param cpuRatio the cpuRatio to set
	 */
	public void setCpuRatio(double cpuRatio) {
		this.cpuRatio = cpuRatio;
	}
}
