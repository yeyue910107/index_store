/**
 * 
 */
package org.act.index.manager;

import org.act.index.common.UserInfo;
import org.apache.log4j.Logger;

/**
 * @author yeyue
 *
 */
public class UserCollect {
	private static final Logger LOG = Logger.getLogger(UserCollect.class);
	private UserInfo userInfo;
	private int coreId;
	
	/**
	 * 
	 */
	public UserCollect() {
		// TODO Auto-generated constructor stub
	}

	public UserCollect(long userId) {
		userInfo = new UserInfo(userId);
	}
	
	public UserCollect(long userId, int coreId) {
		userInfo = new UserInfo(userId);
		this.coreId = coreId;
	}
	
	/**
	 * @return the userInfo
	 */
	public UserInfo getUserInfo() {
		return userInfo;
	}

	/**
	 * @param userInfo the userInfo to set
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
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

	public void show() {
		LOG.info("user:" + userInfo.getUserId());
		LOG.info("core:" + coreId);
	}
}
