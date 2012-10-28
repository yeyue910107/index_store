/**
 * 
 */
package org.act.index.manager;

/**
 * @author Administrator
 *
 */
public class RuntimeGlobalInfo {
    public static final int MANAGER_ROLE_NONE = 0;
    public static final int MANAGER_ROLE_MASTER = 1;
    public static final int MANAGER_ROLE_SLAVE = 2;
	
	private int ownerIpPort;
    private int otherSideIpPort;
    //time_t switch_time_;
    private int vip;
    private boolean destroyFlag;
    private int ownerRole;
    private int otherSideRole;
    //NsStatus owner_status_;
    //NsStatus other_side_status_;
    //NsSyncDataFlag sync_oplog_flag_;
    //tbutil::Time last_owner_check_time_;
    //tbutil::Time last_push_owner_check_packet_time_;
    
	/**
	 * @return the ownerIpPort
	 */
	public int getOwnerIpPort() {
		return ownerIpPort;
	}
	
	/**
	 * @param ownerIpPort the ownerIpPort to set
	 */
	public void setOwnerIpPort(int ownerIpPort) {
		this.ownerIpPort = ownerIpPort;
	}
	
	/**
	 * @return the otherSideIpPort
	 */
	public int getOtherSideIpPort() {
		return otherSideIpPort;
	}
	
	/**
	 * @param otherSideIpPort the otherSideIpPort to set
	 */
	public void setOtherSideIpPort(int otherSideIpPort) {
		this.otherSideIpPort = otherSideIpPort;
	}
	
	/**
	 * @return the vip
	 */
	public int getVip() {
		return vip;
	}
	
	/**
	 * @param vip the vip to set
	 */
	public void setVip(int vip) {
		this.vip = vip;
	}
	
	/**
	 * @return the ownerRole
	 */
	public int getOwnerRole() {
		return ownerRole;
	}
	
	/**
	 * @param ownerRole the ownerRole to set
	 */
	public void setOwnerRole(int ownerRole) {
		this.ownerRole = ownerRole;
	}
	
	/**
	 * @return the otherSideRole
	 */
	public int getOtherSideRole() {
		return otherSideRole;
	}
	
	/**
	 * @param otherSideRole the otherSideRole to set
	 */
	public void setOtherSideRole(int otherSideRole) {
		this.otherSideRole = otherSideRole;
	}

	/**
	 * @return the destroyFlag
	 */
	public boolean getDestroyFlag() {
		return destroyFlag;
	}

	/**
	 * @param destroyFlag the destroyFlag to set
	 */
	public void setDestroyFlag(boolean destroyFlag) {
		this.destroyFlag = destroyFlag;
	}
    
    
}
