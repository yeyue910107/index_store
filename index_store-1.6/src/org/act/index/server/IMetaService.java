/**
 * 
 */
package org.act.index.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.HashMap;
import java.util.TreeMap;

import org.act.index.common.Address;
import org.act.index.common.CoreInfo;
import org.act.index.common.ServerInfo;

/**
 * @author Administrator
 *
 */
public interface IMetaService extends Remote{
	public ServerInfo getServer()  throws RemoteException;
	public int createCore(HashSet<Integer> cores) throws RemoteException;
	public int createCore(int coreId) throws RemoteException;
	public int createCoreWithNoSegment(int coreId) throws RemoteException;
	public int replicateCore(Address dst, int coreId) throws RemoteException;
	public int backupCore(Address dst, int coreId) throws RemoteException;
	public int removeCore(HashSet<Integer> cores) throws RemoteException;
	public int removeCore(int coreId) throws RemoteException;
	public CoreInfo getCore(int coreId) throws RemoteException;
	public TreeMap<Integer, CoreInfo> getCore() throws RemoteException;
}
