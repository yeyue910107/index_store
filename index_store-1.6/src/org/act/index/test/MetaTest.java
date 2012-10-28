/**
 * 
 */
package org.act.index.test;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.act.index.common.UserInfo;
import org.act.index.manager.CoreCollect;
import org.act.index.manager.IIndexManager;

/**
 * @author Administrator
 *
 */
public class MetaTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//String url = "rmi://192.168.1.172:10000/manager";
			String url = "rmi://192.168.7.16:10000/manager";
			IIndexManager manager = (IIndexManager)Naming.lookup(url);
			//System.out.println("error.");
			manager.createCore();
			//manager.deleteCore(0, 11);
			//System.out.println("core:" + core.getCoreInfo().getCoreId());
		} catch (MalformedURLException e) {
			// TODO: handle exception
			System.out.println("MalformedURLException.");
			e.printStackTrace();
		}
		catch (RemoteException e) {
			// TODO: handle exception
			System.out.println("RemoteException.");
			e.printStackTrace();
		}
		catch (NotBoundException e) {
			// TODO: handle exception
			System.out.println("NotBoundException.");
			e.printStackTrace();
		}
	}

}
