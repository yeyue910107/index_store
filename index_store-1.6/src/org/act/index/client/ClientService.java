/**
 * 
 */
package org.act.index.client;

import gnu.getopt.Getopt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.naming.directory.DirContext;

import jline.ArgumentCompletor;
import jline.ClassNameCompletor;
import jline.Completor;
import jline.ConsoleReader;
import jline.FileNameCompletor;
import jline.NullCompletor;
import jline.SimpleCompletor;
import jline.WindowsTerminal;

import org.act.index.common.MailDoc;
import org.act.index.common.MailQuery;
import org.act.index.common.UserInfo;
import org.act.index.common.constdef.GlobalMessage;
import org.act.index.manager.IndexManager;
import org.act.index.util.XmlParser;
import org.apache.commons.io.FileUtils;

/**
 * @author Administrator
 * 
 */
public class ClientService {
	public static final int CMD_NOP = 0;
	public static final int CMD_UNKNOWN = 1;
	public static final int CMD_HELP = 2;
	public static final int CMD_QUIT = 3;
	public static final int CMD_PUT = 4;
	public static final int CMD_GET = 5;
	public static final int CMD_DEL = 6;
	public static final int CMD_RENAME = 7;
	public static final int CMD_LOGIN = 8;
	public static final int CMD_LOGOUT = 9;
	public static final int CMD_BATCH = 10;
	public static final int CMD_EXPBLK = 11;
	public static final int CMD_UNEXPBLK = 12;
	public static final int CMD_COMPACTBLK = 13;
	public static final int CMD_NEWFILENAME = 14;
	public static final int CMD_REPAIR_LOSEBLOCK = 15;
	public static final int CMD_REPAIR_GROUP = 16;
	public static final int CMD_SET_RUN_PARAM = 17;
	public static final int CMD_UNLOADBLK = 18;
	public static final int CMD_VISIT_COUNT_BLK = 19;
	public static final int CMD_LIST_FILE = 20;
	public static final int CMD_ADDBLK = 21;
	public static final int CMD_UNDEL = 22;
	public static final int CMD_CHECK_FILEINFO = 23;
	public static final int CMD_UREMOVE = 24;
	public static final int CMD_REPAIR_CRC = 25;
	public static final int CMD_LIST_BLOCK = 26;
	public static final int CMD_REMOVE_BLOCK = 27;
	public static final int CMD_HIDE = 28;
	public static final int CMD_CLEAR_REPL_INFO = 29;
	public static final int CMD_GET_REPL_INFO = 30;
	public static final int CMD_ACCESS_STAT_INFO = 31;
	public static final int CMD_SET_ACL_FLAG = 32;
	public static final int CMD_GET_SCALE_IMAGE = 33;
	public static final int CMD_REG = 34;

	private static String managerAddr;
	private static IndexClient client;
	private static HashMap<String, Integer> cmdMap;

	public int init() {
		client = new IndexClient(managerAddr);
		cmdMap = new HashMap<String, Integer>();
		cmdMap.put("help", CMD_HELP);
		cmdMap.put("quit", CMD_QUIT);
		cmdMap.put("exit", CMD_QUIT);
		cmdMap.put("put", CMD_PUT);
		cmdMap.put("get", CMD_GET);
		cmdMap.put("del", CMD_DEL);
		cmdMap.put("rename", CMD_RENAME);
		cmdMap.put("login", CMD_LOGIN);
		cmdMap.put("logout", CMD_LOGOUT);
		cmdMap.put("reg", CMD_REG);
		return GlobalMessage.ISS_SUCCESS;
	}

	public void start() {
		// return GlobalMessage.ISS_SUCCESS;
	}

	public static void usage(String name) {
		System.out
				.println("\nUsage: " + name + " -s manager ip port [-i] [-h]");
		System.out.println("  -s    manager ip port");
		System.out.println("  -i    directly execute the command");
		System.out.println("  -h    help\n");
	}

	public static int showHelp() {
		System.out.println("\nsupported command"
				+ "\nexit|quit                      quit console"
				+ "\ncreate user_name               create a new user"
				+ "\nadd mail_doc                   add a new mail doc to iss"
				+ "\nquery query_string             query by query string"
				+ "\nhelp                           show help info\n\n");
		/*
		 * System.out.println("\nsupported command" +
		 * "\nexit|quit                      quit console" +
		 * "\nput local_file [tfs_file]      upload a local file to tfs" +
		 * "\nget tfs_file local_file        download a remote file to local disk"
		 * + "\nrm tfs_file                    delete a remote file" +
		 * "\nrename from_file to_file       rename a remote file" +
		 * "\nnewfilename                    generate a new tfs file name" +
		 * 
		 * "\nstat tfs_file                  get the file info from tfs" +
		 * "\nstatblk                        get the block info" +
		 * "\nlsf                            list the files in the block" +
		 * "\ncfi                            check a remote file" +
		 * "\nvcblk                          list the most used blocks" +
		 * 
		 * "\n@|batch                        batch handle cmds\n" +
		 * 
		 * "\ncompact                        [Attention!] force to compact a block"
		 * + "\nparam                          [Attention!] set the params" +
		 * "\nrepairblk                      repair the back block" +
		 * "\nrepairgrp                      repair the group of a block" +
		 * "\nremoveblock                    [Attention!] remove a block(includes the meta infos)"
		 * +
		 * "\nlistblock                      list the dataservers the block allocated on"
		 * +
		 * 
		 * "\nhelp                           show help info\n\n");
		 */
		return GlobalMessage.ISS_SUCCESS;
	}

	public static int mainLoop(IndexClient client) {
		List<String> param = new ArrayList<String>();
		Scanner scanner = new Scanner(System.in);
		String buffer = "";
		while (true) {
			System.out.print("ISS> ");
			if (scanner.hasNext()) {
				buffer = scanner.nextLine();
			}
			int cmd = parseCmd(buffer, param);
			if (cmd == CMD_QUIT)
				break;
			switchCmd(cmd, client, param);
		}
		return GlobalMessage.ISS_SUCCESS;
	}

	/*
	 * public static int mainLoop(IndexClient client) { Character mask = null;
	 * String trigger = null;
	 * 
	 * try { ConsoleReader reader; reader = new ConsoleReader();
	 * 
	 * reader.setBellEnabled(false); //reader.setDebug(new PrintWriter( //new
	 * FileWriter("writer.debug", true)));
	 * 
	 * 
	 * if ((args == null) || (args.length == 0)) { usage(); return; }
	 * 
	 * 
	 * List<SimpleCompletor> completors = new LinkedList<SimpleCompletor>();
	 * 
	 * 
	 * if (args.length > 0) { if (args[0].equals("none")) { } else if
	 * (args[0].equals("files")) { completors.add(new FileNameCompletor()); }
	 * else if (args[0].equals("classes")) { completors.add(new
	 * ClassNameCompletor()); } else if (args[0].equals("dictionary")) {
	 * completors.add(new SimpleCompletor(new GZIPInputStream(
	 * Example.class.getResourceAsStream("english.gz")))); } else if
	 * (args[0].equals("simple")) { completors.add(new SimpleCompletor(new
	 * String[] { "foo", "bar", "baz" })); } else { usage(); return; } }
	 * 
	 * if (args.length == 3) { mask = new Character(args[2].charAt(0)); trigger
	 * = args[1]; }
	 * 
	 * WindowsTerminal terminal = (WindowsTerminal)
	 * WindowsTerminal.getTerminal(); terminal.setDirectConsole(false);
	 * //terminal. //terminal.
	 * 
	 * completors.add(new SimpleCompletor(new String[] { "", "bar", "baz" }));
	 * reader.addCompletor(new ArgumentCompletor(completors)); PrintWriter out =
	 * new PrintWriter(System.out); String line = reader.readLine("ISS>");
	 * 
	 * 
	 * while (line != null) { //out.println("======>\"" + line + "\"");
	 * out.flush();
	 * 
	 * // If we input the special word then we will mask // the next line. if
	 * ((trigger != null) && (line.compareTo(trigger) == 0)) { line =
	 * reader.readLine("password> ", mask); } if (line.equalsIgnoreCase("quit")
	 * || line.equalsIgnoreCase("exit")) { break; } line =
	 * reader.readLine("ISS>"); } } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return
	 * GlobalMessage.ISS_SUCCESS; }
	 */

	public static int parseCmd(String key, List<String> param) {
		int cmd = CMD_NOP;
		if (key == null || key.isEmpty() || key.compareTo("") == 0)
			return GlobalMessage.ISS_ERROR;
		key = key.trim();
		String[] token;
		if (!key.startsWith("get"))
			token = key.split(" ");
		else {
			token = key.split(" ", 2);
		}
		// System.out.println(token[0]);
		if (!cmdMap.containsKey(token[0]))
			return CMD_UNKNOWN;
		// System.out.println(token[0]);
		cmd = cmdMap.get(token[0]);
		param.clear();
		for (int i = 1; i < token.length; i++) {
			if (token[i] == null || token[i].isEmpty()
					|| token[i].compareTo("") == 0)
				return cmd;
			param.add(token[i]);
		}
		return cmd;
	}

	public static int switchCmd(int cmd, IndexClient client, List<String> param) {
		int ret = GlobalMessage.ISS_SUCCESS;
		switch (cmd) {
		case CMD_HELP:
			ret = showHelp();
			break;
		case CMD_UNKNOWN:
			System.out.println("unknown command.");
			break;
		case CMD_PUT:
			ret = putDoc(client, param);
			break;
		case CMD_GET:
			ret = getDoc(client, param);
			break;
		case CMD_DEL:
			ret = del(client, param);
			break;
		case CMD_REG:
			ret = reg(client, param);
			break;
		case CMD_LOGIN:
			ret = login(client, param);
			break;
		case CMD_LOGOUT:
			ret = logout(client);
			break;
		default:
			break;
		}
		return ret;
	}

	public static int getDoc(IndexClient client, List<String> param) {
		if (!isLogin(client))
			return GlobalMessage.ISS_ERROR;
		int size = param.size();
		// System.out.println(param.get(0));
		List<MailDoc> docs = client.search(new MailQuery(param.get(0)));
		System.out.println("number of docs:" + docs.size());
		for (MailDoc mailDoc : docs) {
			mailDoc.print();
		}
		return GlobalMessage.ISS_SUCCESS;
	}

	public static int putDoc(IndexClient client, List<String> param) {
		if (!isLogin(client))
			return GlobalMessage.ISS_ERROR;
		int size = param.size();
		int ret = GlobalMessage.ISS_ERROR;
		XmlParser xmlParser = new XmlParser();
		xmlParser.init();
		for (int i = 0; i < size; i++) {
			File dir = new File(param.get(0));
			if (dir.isFile()) {
				MailDoc doc = new MailDoc();
				// System.out.println(param.get(0));
				xmlParser.parserXmlDoc(param.get(0), doc);
				client.add(doc);
				ret = GlobalMessage.ISS_SUCCESS;
			} else if (dir.isDirectory()) {
				List<MailDoc> docs = new ArrayList<MailDoc>();
				File[] files = dir.listFiles();
				for (File file : files) {
					if (file.getAbsolutePath().endsWith(".xml")) {
						MailDoc doc = new MailDoc();
						xmlParser.parserXmlDoc(file.getAbsolutePath(), doc);
						docs.add(doc);
					}
				}
				client.add(docs);
				ret = GlobalMessage.ISS_SUCCESS;
			}
		}
		if (ret == GlobalMessage.ISS_SUCCESS) {
			System.out.println(param.get(0) + " create index success.");
		}
		return ret;
	}

	public static int reg(IndexClient client, List<String> param) {
		if (client.getUserId() >= 0) {
			System.out.println("you have logged in as " + client.getUserName());
			return GlobalMessage.ISS_ERROR;
		}
		int size = param.size();
		UserInfo userInfo = client.register(param.get(0), param.get(1));
		// System.out.println("hello");
		if (userInfo != null) {
			System.out.println(param.get(0) + " register success.");
			return GlobalMessage.ISS_SUCCESS;
		}
		System.out.println(param.get(0) + " register failed.");
		return GlobalMessage.ISS_ERROR;
	}

	public static int login(IndexClient client, List<String> param) {
		if (client.getUserId() >= 0) {
			System.out.println("you have logged in as " + client.getUserName());
			return GlobalMessage.ISS_ERROR;
		}
		int size = param.size();
		long userId = client.login(param.get(0), param.get(1));
		if (userId >= 0) {
			System.out.println(param.get(0) + " log in success.");
			client.setUserId(userId);
			client.setUserName(param.get(0));
			return GlobalMessage.ISS_SUCCESS;
		}
		System.out.println(param.get(0) + " log in failed.");
		return GlobalMessage.ISS_ERROR;
	}

	public static int del(IndexClient client, List<String> param) {
		if (!isLogin(client))
			return GlobalMessage.ISS_ERROR;
		int size = param.size();
		int ret = client.delete(new MailQuery(param.get(0)));
		if (ret == GlobalMessage.ISS_SUCCESS) {
			System.out.println(param.get(0) + " delete success.");
			return ret;
		}
		System.out.println(param.get(0) + " delete failed.");
		return ret;
	}

	public static int logout(IndexClient client) {
		if (!isLogin(client))
			return GlobalMessage.ISS_ERROR;
		System.out.println(client.getUserName() + " logout.");
		client.setUserId(-1L);
		return GlobalMessage.ISS_SUCCESS;
	}

	public static boolean isLogin(IndexClient client) {
		if (client.getUserId() < 0) {
			System.out.println("please log in.");
			return false;
		}
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int i;
		boolean directly = false, isHelp = false;

		// get input argument
		Getopt getopt = new Getopt(null, args, "s:ih");
		while ((i = getopt.getopt()) != -1) {
			switch (i) {
			case 's':
				managerAddr = getopt.getOptarg();
				break;
			case 'i':
				directly = true;
				break;
			case 'h':
			default:
				isHelp = true;
				break;
			}
		}

		if (managerAddr.isEmpty() || managerAddr.compareTo(" ") == 0 || isHelp) {
			usage(args[0]);
		}

		ClientService clientService = new ClientService();
		clientService.init();
		mainLoop(clientService.client);
	}

}
