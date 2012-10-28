/**
 * 
 */
package org.act.index.common;

import java.io.Serializable;

/**
 * @author yeyue
 *
 */
public class UserInfo implements Serializable {
	private Long userId;
	
	public UserInfo() {
		
	}

	/**
	 * @param userId
	 */
	public UserInfo(long userId) {
		super();
		this.userId = userId;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
}
