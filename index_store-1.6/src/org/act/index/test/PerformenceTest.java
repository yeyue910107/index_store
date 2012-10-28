/**
 * 
 */
package org.act.index.test;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.act.index.client.ClientSession;
import org.act.index.client.IndexClient;
import org.act.index.manager.IIndexManager;

/**
 * @author Administrator
 *
 */
public class PerformenceTest {
	public static int USER_NUM = 100;
	public static int CORE_NUM = 100;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

}

class ClientThread extends Thread {
	private int period;
	private IndexClient client;
	
	public ClientThread() {
		
	}
	
	public ClientThread(long userId, int period) {
		this.period = period;
		this.client = new IndexClient(userId);
	}
	
	public void run() {
		
	}
}
