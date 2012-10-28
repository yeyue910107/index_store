/**
 * 
 */
package org.act.index.server;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import org.act.index.common.Address;
import org.act.index.common.Configure;
import org.act.index.common.CoreInfo;
import org.act.index.common.MonitorInfo;
import org.act.index.common.ServerInfo;
import org.act.index.common.constdef.ErrorMessage;
import org.act.index.common.constdef.GetCoreType;
import org.act.index.common.constdef.GlobalMessage;
import org.act.index.common.sysparam.PublicParam;
import org.act.index.common.sysparam.ServerParam;
import org.act.index.manager.IIndexManager;
import org.act.index.manager.LayoutManager;
import org.act.index.server.backup.FileSelection;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.admin.CoreAdminHandler;
import org.apache.commons.io.*;

/**
 * @author Administrator
 * 
 */
public class MetaService extends UnicastRemoteObject implements IMetaService,
		Runnable {
	private static final Logger LOG = Logger.getLogger(MetaService.class);
	private IndexServer indexServer;
	private boolean stop;
	private Address managerAddr;

	private HeartThread heartThread = new HeartThread();

	public MetaService() throws RemoteException {

	}

	public MetaService(IndexServer indexServer) throws RemoteException {
		this.indexServer = indexServer;
	}

	public int init() {
		// get manager ip list and port from CONF FILE
		String iplist = PublicParam.CONFIG.getStringValue(
				Configure.CONFIG_MANAGER, Configure.CONF_IP_ADDR_LIST, null);
		int managerPort = PublicParam.CONFIG.getIntValue(
				Configure.CONFIG_MANAGER, Configure.CONF_PORT, 0);
		int serverPort = PublicParam.CONFIG.getIntValue(
				Configure.CONFIG_SERVER, Configure.CONF_PORT, 0);
		if (iplist == null || managerPort <= 0) {
			LOG
					.error("init manager ip is null or manager port <= 0, must be exit.");
			return ErrorMessage.EXIT_GENERAL_ERROR;
		}
		LOG.debug("manager list(" + iplist + ", manager port(" + managerPort
				+ ").");
		List<Address> addrs = new ArrayList<Address>();

		String[] ips = iplist.split("\\|");
		for (String string : ips) {
			addrs.add(new Address(string, managerPort));
		}
		if (addrs.size() != 2) {
			LOG.debug("must have to manager, check you manager list");
			return ErrorMessage.EXIT_GENERAL_ERROR;
		}
		String ipAddr = PublicParam.CONFIG.getStringValue(
				Configure.CONFIG_MANAGER, Configure.CONF_IP_ADDR, null);
		// managerAddr = new Address(ipAddr, port);
		managerAddr = new Address(ipAddr, managerPort);

		String localIP = "";
		try {
			localIP = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Address localAddr = new Address(localIP, serverPort);
		int i;
		for (i = 0; i < ips.length && localIP.compareTo(ips[i]) != 0; i++) {
			;
		}
		if (i == ips.length) {
			LOG
					.error("local ip(" + localIP
							+ ") not in ip list, must be exit.");
			return ErrorMessage.EXIT_GENERAL_ERROR;
		}
		return GlobalMessage.ISS_SUCCESS;
	}

	public void run() {
		doHeart();
	}

	public void stop() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.act.index.server.IMetaService#getServer()
	 */
	@Override
	public ServerInfo getServer() {
		// TODO Auto-generated method stub
		// SolrServer server;
		// server.
		MonitorService monitorService = new MonitorService();
		MonitorInfo monitorInfo = monitorService.getMonitorInfo();
		indexServer.getStatus(monitorInfo);
		return indexServer.getServerInfo();
		// return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.act.index.server.IMetaService#getCore()
	 */
	@Override
	public TreeMap<Integer, CoreInfo> getCore() {
		// TODO Auto-generated method stub
		TreeMap<Integer, CoreInfo> cores = new TreeMap<Integer, CoreInfo>();

		List<SolrCore> solrCores = (ArrayList<SolrCore>) indexServer
				.getCoreContainer().getCores();

		for (SolrCore solrCore : solrCores) {
			if (solrCore.getName().compareTo("meta") != 0) {
				CoreInfo coreInfo = new CoreInfo(solrCore);
				cores.put(Integer.parseInt(solrCore.getName()), coreInfo);
				//LOG.info("get core:" + coreInfo.getCoreId());
			}
		}
		return cores;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.act.index.server.IMetaService#createCore(int)
	 */
	@Override
	public int createCore(int coreId) {
		// TODO Auto-generated method stub
		try {
			if (indexServer.getSolrServers().containsKey(coreId))
				return GlobalMessage.ISS_ERROR;
			String dir = indexServer.getUrl() + "/core"
					+ String.valueOf(coreId);
			//LOG.debug("dir:" + dir);
			indexServer.addSolrServer(coreId);
			FileUtils.forceMkdir(new File(dir));
			FileUtils.forceMkdir(new File(dir + "/conf"));
			String configPath = dir + "/conf/solrconfig.xml";
			String schemaPath = dir + "/conf/schema.xml";
			createSolrConfig(configPath);
			createSchema(schemaPath, coreId);
			//indexServer.addSolrServer(coreId);
			//CoreAdminRequest.createCore(String.valueOf(coreId), "core"
			//		+ String.valueOf(coreId), indexServer.getSolrServer());
			CoreAdminRequest.createCore(String.valueOf(coreId), "core"
					+ String.valueOf(coreId), indexServer.getSolrServer());
			//CoreAdminRequest.createCore("10", "core10", server, configPath, schemaPath);
			FileUtils.forceMkdir(new File(dir + "/data"));
			FileUtils.forceMkdir(new File(dir + "/data/index"));
			SolrService service = new SolrService(indexServer.getSolrServer(coreId));
			//solrServices.put(coreId, service);
			//int port = ServerParam.LOCAL_SERVER_PORT + serverId * ServerParam.MAX_CORE_COUNT_PER_SERVER + coreId + 1;
			int serverId = indexServer.getServerInfo().getServerId();
			String ip = Inet4Address.getLocalHost().getHostAddress();
			int port = indexServer.getServerAddr().getPort() + coreId + 1;
			LocateRegistry.createRegistry(port);
			String name = "rmi://" + ip + ":" + port + "/core" + coreId + "@server" + serverId;
			Naming.rebind(name, service);
			LOG.info("rmi:solrserver( " + name + ") is ready!");
		} catch (SolrServerException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return GlobalMessage.ISS_SUCCESS;
	}

	public int createCoreWithNoSegment(int coreId) {
		// TODO Auto-generated method stub
		try {
			if (indexServer.getSolrServers().containsKey(coreId))
				return GlobalMessage.ISS_ERROR;
			String dir = indexServer.getUrl() + "/core"
					+ String.valueOf(coreId);
			//LOG.debug("dir:" + dir);
			indexServer.addSolrServer(coreId);
			FileUtils.forceMkdir(new File(dir));
			FileUtils.forceMkdir(new File(dir + "/conf"));
			String configPath = dir + "/conf/solrconfig.xml";
			String schemaPath = dir + "/conf/schema.xml";
			createSolrConfig(configPath);
			createSchema(schemaPath, coreId);
			//indexServer.addSolrServer(coreId);
			//CoreAdminRequest.createCore(String.valueOf(coreId), "core"
			//		+ String.valueOf(coreId), indexServer.getSolrServer());
			CoreAdminRequest.createCore(String.valueOf(coreId), "core"
					+ String.valueOf(coreId), indexServer.getSolrServer());
			//CoreAdminRequest.createCore("10", "core10", server, configPath, schemaPath);
			//FileUtils.forceMkdir(new File(dir + "/data"));
			//FileUtils.forceMkdir(new File(dir + "/data/index"));
			FileUtils.cleanDirectory(new File(dir + "/data/index"));
			SolrService service = new SolrService(indexServer.getSolrServer(coreId));
			//solrServices.put(coreId, service);
			//int port = ServerParam.LOCAL_SERVER_PORT + serverId * ServerParam.MAX_CORE_COUNT_PER_SERVER + coreId + 1;
			int serverId = indexServer.getServerInfo().getServerId();
			String ip = Inet4Address.getLocalHost().getHostAddress();
			int port = indexServer.getServerAddr().getPort() + coreId + 1;
			LocateRegistry.createRegistry(port);
			String name = "rmi://" + ip + ":" + port + "/core" + coreId + "@server" + serverId;
			Naming.rebind(name, service);
			LOG.info("rmi:solrserver( " + name + ") is ready!");
		} catch (SolrServerException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return GlobalMessage.ISS_SUCCESS;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.act.index.server.IMetaService#replicateCore(int)
	 */
	@Override
	public int replicateCore(Address dst, int coreId) {
		String url = "rmi://" + dst.getAddr().getAddress().getHostAddress() + 
		":" + dst.getAddr().getPort() + 
		"/meta@server" + dst.getServerId();
		try {
			System.out.println("core:" + coreId + "@server:" + dst.getServerId() + " begin replicating.");
			IMetaService metaService = (IMetaService)Naming.lookup(url);
			metaService.createCoreWithNoSegment(coreId);
			//String path = ServerParam.SOLR_HOME + "/core" + coreId + "/data/index/";
			//FileSelection dir = new FileSelection(path, coreId);
			//dir.doBackup(dst.getAddr().getAddress(), dst.getAddr().getPort() - 1);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return GlobalMessage.ISS_SUCCESS;
	}
	
	public int backupCore(Address dst, int coreId) {
		String name = "core" + coreId + "@server" + dst.getServerId();
		int port = dst.getAddr().getPort() + coreId + 1;
		String url = "rmi://" + dst.getAddr().getAddress().getHostAddress() + 
		":" + port + "/" + name;
		String path = ServerParam.SOLR_HOME + "/core" + coreId + "/data/index/";
		FileSelection dir = new FileSelection(path, coreId);
		dir.doBackup(dst.getAddr().getAddress(), dst.getAddr().getPort() - 1);
		try {
			ISolrService solrService = (ISolrService)Naming.lookup(url);
			solrService.commit();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return GlobalMessage.ISS_SUCCESS;
	}
	
	private int createSolrConfig(String path) {
		File solrConfig = new File(path);
		String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<!--\n"
				+ "Licensed to the Apache Software Foundation (ASF) under one or more\n"
				+ "contributor license agreements.  See the NOTICE file distributed with\n"
				+ "this work for additional information regarding copyright ownership.\n"
				+ "The ASF licenses this file to You under the Apache License, Version 2.0\n"
				+ "(the \"License\"); you may not use this file except in compliance with\n"
				+ "the License.  You may obtain a copy of the License at\n"
				+ "http://www.apache.org/licenses/LICENSE-2.0\n"
				+ "Unless required by applicable law or agreed to in writing, software\n"
				+ "distributed under the License is distributed on an \"AS IS\" BASIS,\n"
				+ "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"
				+ "See the License for the specific language governing permissions and\n"
				+ "limitations under the License.\n-->\n"
				+ "<!--\nThis is a stripped down config file used for a simple example...\n"
				+ "It is *not* a good example to work from.\n-->"
				+ "<config>\n<updateHandler class=\"solr.DirectUpdateHandler2\" />\n"
				+ "<requestDispatcher handleSelect=\"true\" >\n"
				+ "<requestParsers enableRemoteStreaming=\"false\" multipartUploadLimitInKB=\"2048\" />\n"
				+ "</requestDispatcher>\n"
				+ "<requestHandler name=\"standard\" class=\"solr.StandardRequestHandler\" default=\"true\" />\n"
				+ "<requestHandler name=\"/update\" class=\"solr.XmlUpdateRequestHandler\" />\n"
				+ "<requestHandler name=\"/admin/\" class=\"org.apache.solr.handler.admin.AdminHandlers\" />\n"
				+ "<!-- config for the admin interface -->\n<admin>\n<defaultQuery>solr</defaultQuery>\n</admin>\n</config>";
		try {
			FileUtils.writeStringToFile(solrConfig, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return GlobalMessage.ISS_SUCCESS;
	}

	/*private int createSchema(String path, int coreId) {
		File schema = new File(path);
		String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<!--\n"
				+ "Licensed to the Apache Software Foundation (ASF) under one or more\n"
				+ "contributor license agreements.  See the NOTICE file distributed with\n"
				+ "this work for additional information regarding copyright ownership.\n"
				+ "The ASF licenses this file to You under the Apache License, Version 2.0\n"
				+ "(the \"License\"); you may not use this file except in compliance with\n"
				+ "the License.  You may obtain a copy of the License at\n"
				+ "http://www.apache.org/licenses/LICENSE-2.0\n"
				+ "Unless required by applicable law or agreed to in writing, software\n"
				+ "distributed under the License is distributed on an \"AS IS\" BASIS,\n"
				+ "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"
				+ "See the License for the specific language governing permissions and\n"
				+ "limitations under the License.\n-->\n"
				+ "<schema name=\"core"
				+ coreId
				+ "\" version=\"1.1\">"
				+ "<types><fieldtype name=\"string\"  class=\"solr.StrField\" sortMissingLast=\"true\" omitNorms=\"true\"/></types>"
				+ "<fields>\n<!-- general -->\n"
				+ "<field name=\"mailId\"      type=\"string\"   indexed=\"true\"  stored=\"true\"  multiValued=\"false\" required=\"true\"/>\n"
				+ "<field name=\"userId\"    type=\"string\"   indexed=\"true\"  stored=\"true\"  multiValued=\"false\" />\n"
				+ "<field name=\"from\"    type=\"string\"   indexed=\"true\"  stored=\"true\"  multiValued=\"false\" />\n"
				+ "<field name=\"to\"   type=\"string\"   indexed=\"true\"  stored=\"true\"  multiValued=\"false\" />\n"
				+ "<field name=\"cc\"   type=\"string\"   indexed=\"true\"  stored=\"true\"  multiValued=\"false\" />\n"
				+ "<field name=\"bcc\"   type=\"string\"   indexed=\"true\"  stored=\"true\"  multiValued=\"false\" />\n"
				+ "<field name=\"subject\"   type=\"string\"   indexed=\"true\"  stored=\"true\"  multiValued=\"false\" />\n"
				+ "<field name=\"body\"   type=\"string\"   indexed=\"true\"  stored=\"true\"  multiValued=\"false\" />\n</fields>"
				+ "<!-- field to use to determine and enforce document uniqueness. -->\n"
				+ "<uniqueKey>mailId</uniqueKey>\n"
				+ "<!-- field for the QueryParser to use when an explicit fieldname is absent -->\n"
				+ "<defaultSearchField>body</defaultSearchField>\n"
				+ "<!-- SolrQueryParser configuration: defaultOperator=\"AND|OR\" -->"
				+ "<solrQueryParser defaultOperator=\"OR\"/>\n</schema>";
		try {
			FileUtils.writeStringToFile(schema, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return GlobalMessage.ISS_SUCCESS;
	}*/

	private int createSchema(String path, int coreId) {
	File schema = new File(path);
	String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<!--\n"
			+ "Licensed to the Apache Software Foundation (ASF) under one or more\n"
			+ "contributor license agreements.  See the NOTICE file distributed with\n"
			+ "this work for additional information regarding copyright ownership.\n"
			+ "The ASF licenses this file to You under the Apache License, Version 2.0\n"
			+ "(the \"License\"); you may not use this file except in compliance with\n"
			+ "the License.  You may obtain a copy of the License at\n"
			+ "http://www.apache.org/licenses/LICENSE-2.0\n"
			+ "Unless required by applicable law or agreed to in writing, software\n"
			+ "distributed under the License is distributed on an \"AS IS\" BASIS,\n"
			+ "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"
			+ "See the License for the specific language governing permissions and\n"
			+ "limitations under the License.\n-->\n"
			+ "<schema name=\"core"
			+ coreId
			+ "\" version=\"1.1\">"
			+ "<types><fieldtype name=\"string\"  class=\"solr.StrField\" sortMissingLast=\"true\" omitNorms=\"true\"/>\n"
			+ "<fieldType name=\"date\" class=\"solr.DateField\" sortMissingLast=\"true\" omitNorms=\"true\"/>\n"
			+ "<fieldType name=\"text\" class=\"solr.TextField\" positionIncrementGap=\"100\"><analyzer><tokenizer class=\"solr.CJKTokenizerFactory\"/></analyzer></fieldType></types>\n"
			+ "<fields>\n<!-- general -->\n"
			+ "<field name=\"mailId\"      type=\"string\"   indexed=\"true\"  stored=\"true\"  multiValued=\"false\" required=\"true\"/>\n"
			+ "<field name=\"userId\"    type=\"string\"   indexed=\"true\"  stored=\"true\"  multiValued=\"false\" />\n"
			+ "<field name=\"from\"    type=\"string\"   indexed=\"true\"  stored=\"true\"  multiValued=\"false\" />\n"
			+ "<field name=\"to\"   type=\"string\"   indexed=\"true\"  stored=\"true\"  multiValued=\"false\" />\n"
			+ "<field name=\"cc\"   type=\"string\"   indexed=\"true\"  stored=\"true\"  multiValued=\"false\" />\n"
			+ "<field name=\"bcc\"   type=\"string\"   indexed=\"true\"  stored=\"true\"  multiValued=\"false\" />\n"
			+ "<field name=\"subject\"   type=\"text\"   indexed=\"true\"  stored=\"true\"  multiValued=\"false\" />\n"
			+ "<field name=\"body\"   type=\"text\"   indexed=\"true\"  stored=\"true\"  multiValued=\"false\" />\n"
			+ "<field name=\"datetime\"   type=\"date\"   indexed=\"true\"  stored=\"true\"  multiValued=\"false\" default=\"NOW\" />\n</fields>"
			+ "<!-- field to use to determine and enforce document uniqueness. -->\n"
			+ "<uniqueKey>mailId</uniqueKey>\n"
			+ "<!-- field for the QueryParser to use when an explicit fieldname is absent -->\n"
			+ "<defaultSearchField>body</defaultSearchField>\n"
			+ "<!-- SolrQueryParser configuration: defaultOperator=\"AND|OR\" -->"
			+ "<solrQueryParser defaultOperator=\"OR\"/>\n</schema>";
	try {
		FileUtils.writeStringToFile(schema, data);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return GlobalMessage.ISS_SUCCESS;
}
	
	public void doHeart() {
		heartThread.start();
	}

	public void doCheck() {

	}

	class HeartThread extends Thread {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				ServerInfo serverInfo = getServer();
				TreeMap<Integer, CoreInfo> cores = getCore();
				String url = "rmi:/" + managerAddr.getAddr().toString()
				+ "/manager";
				System.out.println(url);
				IIndexManager manager = (IIndexManager) Naming.lookup(url);
				serverInfo = manager.getServerStatus(serverInfo, true);
				System.out.println("meta service server:" + serverInfo.getServerId());
				indexServer.setServerInfo(serverInfo);
				manager.getCoreStatus(serverInfo.getServerId(), cores);
				while (!stop) {
					try {
						sleep(ServerParam.HEART_INTERVAL);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					serverInfo = getServer();
					cores = getCore();
					serverInfo = manager.getServerStatus(serverInfo, false);
					indexServer.setServerInfo(serverInfo);
					// System.out.println("index server:" +
					// indexServer.getServerInfo().getServerId());
					//synchronized (cores) {
						manager.getCoreStatus(serverInfo.getServerId(), cores);
						// LOG.info("get core status:" + )
					//}
					if (serverInfo == null && !serverInfo.getStatus())
						break;
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
			}
				// System.out.println("hello");;
				/*
				 * try { sleep(ServerParam.HEART_INTERVAL); } catch
				 * (InterruptedException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */
		}
	}

	class CheckThread extends Thread {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
		}
	}

	@Override
	public int createCore(HashSet<Integer> cores) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CoreInfo getCore(int coreId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int removeCore(HashSet<Integer> cores) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeCore(int coreId) {
		// TODO Auto-generated method stub
		try {
			CoreAdminRequest.unloadCore(String.valueOf(coreId), indexServer
					.getSolrServer(coreId));
			indexServer.removeSolrServer(coreId);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
