/**
 * 
 */
package org.act.index.test;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import gnu.getopt.Getopt;

import org.act.index.client.IndexClient;
import org.act.index.common.MailQuery;
import org.act.index.manager.IIndexManager;
import org.act.index.util.QueryStringFactory;

/**
 * @author Administrator
 *
 */
public class CreateCoreTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
				manager.createCore();
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
