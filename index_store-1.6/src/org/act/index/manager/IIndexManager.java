/**
 * 
 */
package org.act.index.manager;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import org.act.index.common.Address;
import org.act.index.common.CoreInfo;
import org.act.index.common.ServerInfo;
import org.act.index.common.UserInfo;
import org.act.index.hibernate.user.User;

/**
 * @author Administrator
 *
 */
public interface IIndexManager extends Remote {
	public HashSet<Address> locate(long userId) throws RemoteException;
	
	public ServerInfo getServerStatus(ServerInfo serverInfo, boolean isnew) throws RemoteException;
	
	public int getCoreStatus(int serverId, TreeMap<Integer, CoreInfo> cores) throws RemoteException;
	
	public int writeCommit() throws RemoteException;
	
	public UserInfo createUser(String username, String passsword) throws RemoteException;
	
	public User getUser(String username) throws RemoteException;
	
	//public int deleteUser() throws RemoteException;
	
	//public int updateUser() throws RemoteException;
	
	public int createCore() throws RemoteException;
	
	public int createCore(int serverId) throws RemoteException;
	
	public int createCore(int serverId, int coreId) throws RemoteException;
	
	public int deleteCore(int serverId, int coreId) throws RemoteException;
	
	public int deleteCore(int coreId) throws RemoteException;
	
	public int updateCore() throws RemoteException;
	
	public int joinServer() throws RemoteException;
	
	public int leaveServer() throws RemoteException;

}
