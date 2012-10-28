/**
 * 
 */
package org.act.index.test;

import gnu.getopt.Getopt;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.act.index.client.ClientSession;
import org.act.index.client.IndexClient;
import org.act.index.common.MailDoc;
import org.act.index.common.constdef.GlobalMessage;
import org.act.index.manager.IIndexManager;
import org.act.index.server.ServerService;
import org.act.index.util.DocFactory;
import org.act.index.util.XmlParser;
import org.apache.log4j.Logger;
import org.apache.solr.common.SolrInputDocument;
import org.apache.commons.io.FileUtils;

/**
 * @author Administrator
 *
 */
public class WriteTest {
	//private static final Logger LOG = Logger.getLogger(WriteTest.class);
	//public static int USER_NUM = 100;
	//public static int CORE_NUM = 100;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int c;
		String manager = "192.168.7.16:10000";
		String path = "";
		int userId = -1;
		int docNum = 100;
		int length = 102400;
		int per = 100;
		Getopt getopt = new Getopt(null, args, "m:d:u:n:l:p:");
		while ((c = getopt.getopt()) != -1) {
			switch (c) {
			case 'm':
				manager = getopt.getOptarg();
				break;
			case 'd':
				path = getopt.getOptarg();
				break;
			case 'u':
				userId = Integer.parseInt(getopt.getOptarg());
				break;
			case 'n':
				docNum = Integer.parseInt(getopt.getOptarg());
				break;
			case 'l':
				length = Integer.parseInt(getopt.getOptarg());
				break;
			case 'p':
				per = Integer.parseInt(getopt.getOptarg());
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
		File dir = new File(path);
		try {
			FileUtils.forceMkdir(dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DocFactory docFactory = new DocFactory(docNum, length, per, path);
		docFactory.createMailDocs(userId);
		XmlParser xmlParser = new XmlParser();
		xmlParser.init();
		if (dir.isDirectory()) {
			//List<MailDoc> docs = new ArrayList<MailDoc>();
			File[] files = dir.listFiles();
		
			for (File file : files) {
				if (file.getAbsolutePath().endsWith(".xml")) {
					MailDoc doc = new MailDoc();
					xmlParser.parserXmlDoc(file.getAbsolutePath(), doc);
					client.add(doc);
				}
			}
		}
	}

}
