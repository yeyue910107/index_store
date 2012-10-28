/**
 * 
 */
package org.act.index.test;

import gnu.getopt.Getopt;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.act.index.manager.IIndexManager;

/**
 * @author Administrator
 *
 */
public class CreateUserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		int c;
		String url = "192.168.1.172:10000";
		int count = 0;
		Getopt getopt = new Getopt(null, args, "m:c:");
		while ((c = getopt.getopt()) != -1) {
			switch (c) {
			case 'm':
				url = getopt.getOptarg();
				break;
			case 'c':
				count = Integer.parseInt(getopt.getOptarg());
				break;
			default:
				break;
			}
		}
		url = "rmi://" + url + "/manager";
		try {
			IIndexManager manager = (IIndexManager)Naming.lookup(url);
			for (int i = 0; i < count; i++) {
				Thread.sleep(1000);
				manager.createUser("user" + i, "123456");
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
