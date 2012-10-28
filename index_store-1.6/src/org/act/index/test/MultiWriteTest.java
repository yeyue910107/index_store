/**
 * 
 */
package org.act.index.test;

import java.io.File;
import java.io.IOException;

import org.act.index.client.IndexClient;
import org.act.index.common.MailDoc;
import org.act.index.common.MailQuery;
import org.act.index.util.DocFactory;
import org.act.index.util.QueryStringFactory;
import org.act.index.util.XmlParser;
import org.apache.commons.io.FileUtils;

import gnu.getopt.Getopt;

/**
 * @author Administrator
 *
 */
public class MultiWriteTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int c;
		String manager = "192.168.7.16:10000";
		int docNum = 100;
		int length = 102400;
		int per = 100;
		int threads = 0;
		boolean direct = false;
		Getopt getopt = new Getopt(null, args, "m:n:l:p:t:d:");
		while ((c = getopt.getopt()) != -1) {
			switch (c) {
			case 'm':
				manager = getopt.getOptarg();
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
			case 't':
				threads = Integer.parseInt(getopt.getOptarg());
				break;
			case 'd':
				direct = (((Integer.parseInt(getopt.getOptarg())) == 1) ? true : false);
				break;
			default:
				break;
			}
		}
		//IIndexManager manager = (IIndexManager)Naming.lookup(url);
		//System.out.println("error.");
			//manager.createUser(username, passsword)
		//ClientSession session = new ClientSession();
		for (int i = 0; i < threads; i++) {
			new WriteThread(manager, "user" + i, i, docNum, length, per, direct).start();
		}
	}

}

class WriteThread extends Thread {
	private long userId;
	private String manager;
	private int length;
	private String path;
	private int docNum;
	private int per;
	private boolean direct;
	
	public WriteThread(String manager, String path, long userId, int docNum, int length, int per, boolean direct) {
		this.manager = manager;
		this.path = path;
		this.userId = userId;
		this.docNum = docNum;
		this.length = length;
		this.per = per;
		this.direct = direct;
	}
	
	public void run() {
		IndexClient client = new IndexClient(manager, userId);
		File dir = new File(path);
		try {
			FileUtils.forceMkdir(dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!direct) {
			DocFactory docFactory = new DocFactory(docNum, length, per, path);
			docFactory.createMailDocs(userId);
		}
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
