/**
 * 
 */
package org.act.index.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;


/**
 * @author Administrator
 *
 */
public class QueryStringFactory {

	private int line;
	private int length;
	
	public QueryStringFactory() {
		// TODO Auto-generated constructor stub
		
	}
	
	public QueryStringFactory(int length) {
		// TODO Auto-generated constructor stub
		this.length = length;
	}
	
	public QueryStringFactory(int line, int length) {
		this.line = line;
		this.length = length;
	}
	
	public StringBuffer makeRandomString() {
		String radStr = "abcdefghijklmnopqrstuvwxyz";
		StringBuffer generateRandStr = new StringBuffer();
		Random rand = new Random();

		for (int i = 0; i < length; i++) {
			int randNum = rand.nextInt(26);
			generateRandStr.append(radStr.substring(randNum,randNum + 1));
		}
		return generateRandStr;
	} 

	
	public void createcsv(String csvName, StringBuffer stringBuf) {
		try {
			File f = new File(csvName);
			FileOutputStream fos = new FileOutputStream(f);
			DataOutputStream out = new DataOutputStream(fos);
			out.writeBytes(stringBuf.toString()); 
		}
		catch (IOException e) {
			System.out.println("IOException.");
		}
	}
	
	public StringBuffer createcsvText() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < line; i++)
			buffer.append(makeRandomString() + "\r\n");
		
		return buffer;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String uuid = UUID.randomUUID().toString();
		//String radString = new CSVFactory().makeRandomString(500);
		//System.out.println(uuid);
		//System.out.println(radString);
		//CSVFactory factory = new CSVFactory();
		//factory.createcsv("csv1.xml", factory.createcsvText("1", "user1", "title1", 1024));
		
		QueryStringFactory factory = new QueryStringFactory(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		factory.createcsv(args[2], factory.createcsvText());
		//CSVFactory factory = new CSVFactory(10, 8);
		//factory.createcsv("query.csv", factory.createcsvText());
	}

}
