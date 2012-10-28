package org.act.index.hibernate.user;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User extends org.act.index.common.UserInfo implements
		java.io.Serializable {

	// Fields

	private Long userId;
	private String userName;
	private String password;
	private Integer coreId;

	// Constructors

	/** default constructor */
	public User() {
	}

	/** full constructor */
	public User(Long userId, String userName, String password, Integer coreId) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.coreId = coreId;
	}

	// Property accessors

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getCoreId() {
		return this.coreId;
	}

	public void setCoreId(Integer coreId) {
		this.coreId = coreId;
	}

}