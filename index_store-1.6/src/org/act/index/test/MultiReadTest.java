/**
 * 
 */
package org.act.index.test;

import java.util.Calendar;

import gnu.getopt.Getopt;

import org.act.index.client.IndexClient;
import org.act.index.common.MailQuery;
import org.act.index.util.QueryStringFactory;

/**
 * @author Administrator
 *
 */
public class MultiReadTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		int c;
		String manager = "192.168.7.16:10000";
		//int userId = -1;
		int reqs = 0;
		int length = 3;
		int threads = 0;
		Getopt getopt = new Getopt(null, args, "m:r:l:t:");
		while ((c = getopt.getopt()) != -1) {
			switch (c) {
			case 'm':
				manager = getopt.getOptarg();
				break;
			case 'r':
				reqs = Integer.parseInt(getopt.getOptarg());
				break;
			case 'l':
				length = Integer.parseInt(getopt.getOptarg());
				break;
			case 't':
				threads = Integer.parseInt(getopt.getOptarg());
				break;
			default:
				break;
			}
		}
		//IIndexManager manager = (IIndexManager)Naming.lookup(url);
		//System.out.println("error.");
			//manager.createUser(username, passsword)
		//ClientSession session = new ClientSession();
		long before = Calendar.getInstance().getTimeInMillis();
		for (int i = 0; i < threads; i++) {
			new ReadThread(manager, i, reqs, length).start();
		}
		System.out.println((Calendar.getInstance().getTimeInMillis() - before) + "s");
	}

}

class ReadThread extends Thread {
	private long userId;
	private int reqs;
	private String manager;
	private int length;
	
	public ReadThread(String manager, long userId, int reqs, int length) {
		this.manager = manager;
		this.userId = userId;
		this.reqs = reqs;
		this.length = length;
	}
	
	public void run() {
		IndexClient client = new IndexClient(manager, userId);
		QueryStringFactory qsFactory = new QueryStringFactory(length);
		for (int i = 0; i < reqs; i++) {
			MailQuery query = new MailQuery(qsFactory.makeRandomString().toString());
			client.search(query);
		}
	}
}
