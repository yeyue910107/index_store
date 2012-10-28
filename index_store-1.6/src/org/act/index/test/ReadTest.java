/**
 * 
 */
package org.act.index.test;

import gnu.getopt.Getopt;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.act.index.client.ClientSession;
import org.act.index.client.IndexClient;
import org.act.index.common.MailDoc;
import org.act.index.common.MailQuery;
import org.act.index.manager.IIndexManager;
import org.act.index.server.ServerService;
import org.act.index.util.QueryStringFactory;
import org.act.index.util.XmlParser;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 *
 */
public class ReadTest {
	//private static final Logger LOG = Logger.getLogger(ReadTest.class);
	//public static int USER_NUM = 100;
	//public static int CORE_NUM = 100;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int c;
		String manager = "192.168.7.16:10000";
		int userId = -1;
		int reqs = 0;
		int length = 3;
		Getopt getopt = new Getopt(null, args, "m:u:r:l:");
		while ((c = getopt.getopt()) != -1) {
			switch (c) {
			case 'm':
				manager = getopt.getOptarg();
				break;
			case 'u':
				userId = Integer.parseInt(getopt.getOptarg());
				break;
			case 'r':
				reqs = Integer.parseInt(getopt.getOptarg());
				break;
			case 'l':
				length = Integer.parseInt(getopt.getOptarg());
				break;
			default:
				break;
			}
		}
		//IIndexManager manager = (IIndexManager)Naming.lookup(url);
		//System.out.println("error.");
			//manager.createUser(username, passsword)
		//ClientSession session = new ClientSession();
		IndexClient client = new IndexClient(manager, userId);
		QueryStringFactory qsFactory = new QueryStringFactory(length);
		for (int i = 0; i < reqs; i++) {
			MailQuery query = new MailQuery(qsFactory.makeRandomString().toString());
			client.search(query);
		}
	}

}
