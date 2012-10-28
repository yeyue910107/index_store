/**
 * 
 */
package org.act.index.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

public class DocFactory {
	private int docNum;
	private int max;
	private int maxDocNumPerUser;
	private String dir;
	public DocFactory() {
		
	}
	
	public DocFactory(int docNum, int max) {
		this.docNum = docNum;
		this.max = max;
		this.maxDocNumPerUser = 100;
	}
	
	public DocFactory(int docNum, int max, int maxDocNumPerUser) {
		this.docNum = docNum;
		this.max = max;
		this.maxDocNumPerUser = maxDocNumPerUser;
	}
	
	public DocFactory(int docNum, int max, int maxDocNumPerUser, String dir) {
		this.docNum = docNum;
		this.max = max;
		this.maxDocNumPerUser = maxDocNumPerUser;
		this.dir = dir;
	}
	
	public StringBuffer makeRandomString(long length) {
		String radStr = "abcde fgh,ijk lmnop qrs,tuv wx.yz\n";
		StringBuffer generateRandStr = new StringBuffer();
		Random rand = new Random();

		for (int i = 0; i < length; i++) {
			int randNum = rand.nextInt(34);
			generateRandStr.append(radStr.substring(randNum,randNum + 1));
		}
		return generateRandStr;
	} 

	
	public void createDoc(String docName, StringBuffer stringBuf) {
		try {
			File f = new File(docName);
			FileOutputStream fos = new FileOutputStream(f);
			DataOutputStream out = new DataOutputStream(fos);
			out.writeBytes(stringBuf.toString()); 
		}
		catch (IOException e) {
			System.out.println("IOException.");
		}
	}
	
	public StringBuffer createDocText(String id, String user, String title, long contentLen) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n");
		buffer.append("<add>\r\n\t" + "<doc>\r\n\t\t" + 
				"<field name=\"id\">" + id + "</field>\r\n\t\t" + 
				"<field name=\"user\">" + user + "</field>\r\n\t\t" + 
				"<field name=\"title\">" + title + "</field>\r\n\t\t" + 
				"<field name=\"content\">" + makeRandomString(contentLen) + "</field>\r\n" + 
        		"\t</doc>\r\n</add>");
		
		return buffer;
	}
	
	public StringBuffer createDocText(long mailId, long userId, String subject, long contentLen) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n");
		buffer.append("<mails>\r\n\t" + "<mail>\r\n\t\t" + 
				"<field name=\"mailId\">" + mailId + "</field>\r\n\t\t" + 
				"<field name=\"userId\">" + userId + "</field>\r\n\t\t" + 
				"<field name=\"from\">" + "user" + userId + "@iss.com" + "</field>\r\n\t\t" + 
				"<field name=\"to\">" + "another" + userId + "@iss.com" + "</field>\r\n\t\t" + 
				"<field name=\"cc\">" + "vip@iss.com" + "</field>\r\n\t\t" + 
				"<field name=\"bcc\">" + "vvip@iss.com" + "</field>\r\n\t\t" + 
				"<field name=\"subject\">" + subject + "</field>\r\n\t\t" + 
				"<field name=\"body\">" + makeRandomString(contentLen) + "</field>\r\n\t\t" + 
				"<field name=\"dateTime\">" + Calendar.getInstance().getTimeInMillis() + "</field>\r\n\t\t" + 
        		"\t</mail>\r\n</mails>");
		
		return buffer;
	}
	
	public void createBatchDocs() {
		createBatchDocs(1);
	}
	
	public void createBatchDocs(int begin) {
		Random random = new Random();
		int len;
		
		for (int i = begin; i < begin + docNum; i++) {
			len = random.nextInt(max);
			this.createDoc("mail" + i + ".xml", this.createDocText("" + i, "user" + i, "title" + i, len));
		}
	}
	
	public void createMailDocs(long userId) {
		Random random = new Random();
		int len;
		
		for (int i = (int)(userId * maxDocNumPerUser); i < userId * maxDocNumPerUser + docNum; i++) {
			len = random.nextInt(max);
			this.createDoc(dir + "/mail" + i + ".xml", this.createDocText(i, userId, "user" + userId + ":mail" + i, len));
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String uuid = UUID.randomUUID().toString();
		//String radString = new DocFactory().makeRandomString(500);
		//System.out.println(uuid);
		//System.out.println(radString);
		//DocFactory factory = new DocFactory();
		//factory.createDoc("doc1.xml", factory.createDocText("1", "user1", "title1", 1024));
		
		DocFactory factory = new DocFactory(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		factory.createBatchDocs(Integer.parseInt(args[2]));
	}

}

