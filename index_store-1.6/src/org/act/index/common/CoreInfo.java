/**
 * 
 */
package org.act.index.common;

import java.io.File;
import java.io.Serializable;

import org.apache.solr.core.SolrCore;
import org.apache.commons.io.FileUtils;

/**
 * @author yeyue
 *
 */
public class CoreInfo implements Serializable {
	private int coreId;
	private int userCount;
	private long size;
	private int seqNo;
	private String version;


	public CoreInfo() {
		
	}

	/**
	 * @param coreId
	 */
	public CoreInfo(int coreId) {
		super();
		this.coreId = coreId;
	}

	public CoreInfo(SolrCore core) {
		this.coreId = Integer.parseInt(core.getName());
		this.version = core.getVersion();
		File dataDir = new File(core.getDataDir());
		size = FileUtils.sizeOfDirectory(dataDir);
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
	 * @return the userCount
	 */
	public int getUserCount() {
		return userCount;
	}

	/**
	 * @param userCount the userCount to set
	 */
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * @return the seqNo
	 */
	public int getSeqNo() {
		return seqNo;
	}

	/**
	 * @param seqNo the seqNo to set
	 */
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
}
