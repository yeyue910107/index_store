/**
 * 
 */
package org.act.index.server;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.act.index.common.Configure;
import org.act.index.common.constdef.GlobalMessage;
import org.act.index.common.sysparam.PublicParam;
import org.act.index.common.sysparam.ServerParam;
import org.act.index.manager.IndexManager;
import org.act.index.manager.LayoutManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.apache.solr.core.CoreContainer.Initializer;
import org.xml.sax.SAXException;

import gnu.getopt.Getopt;

/**
 * @author Administrator
 *
 */
public class ServerService {
	private static final Logger LOG = Logger.getLogger(ServerService.class);
	private static HashMap<Integer, SolrService> solrServices;
	private static IndexServer indexServer;
	
	public int init() {
		int ret = GlobalMessage.ISS_ERROR;
		ret = regService();
		LOG.info("init.");
		return ret;
	}
	
	
	public int regService() {
		solrServices = new HashMap<Integer, SolrService>();
		int serverId = indexServer.getServerInfo().getServerId();
		String ip = "127.0.0.1";
		try {
			ip = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (SolrCore core: indexServer.getCoreContainer().getCores()) {
			if (core.getName().compareTo("meta") == 0)
				continue;
			try {
				int coreId = Integer.parseInt(core.getName());
				indexServer.addSolrServer(coreId);
				SolrService service = new SolrService(indexServer.getSolrServer(coreId));
				solrServices.put(coreId, service);
				//int port = ServerParam.LOCAL_SERVER_PORT + serverId * ServerParam.MAX_CORE_COUNT_PER_SERVER + coreId + 1;
				int port = ServerParam.LOCAL_SERVER_PORT + coreId + 1;
				LocateRegistry.createRegistry(port);
				String name = "rmi://" + ip + ":" + port + "/core" + coreId + "@server" + serverId;
				Naming.rebind(name, service);
				LOG.info("rmi:solrserver( " + name + ") is ready!");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				LOG.error("RemoteException.");
				e.printStackTrace();
			}
			catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				LOG.error("MalformedURLException.");
				e.printStackTrace();
			}
		}
		try {
			//int port = ServerParam.LOCAL_SERVER_PORT + serverId * ServerParam.MAX_CORE_COUNT_PER_SERVER;
			//int port = ServerParam.LOCAL_SERVER_PORT + coreId;
			int port = ServerParam.LOCAL_SERVER_PORT;
			LocateRegistry.createRegistry(port);
			String name = "rmi://" + ip + ":" + port + "/meta@server" + serverId;
			Naming.rebind(name, indexServer.getMetaService());
			LOG.info("rmi:solrserver( " + name + ") is ready!");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return GlobalMessage.ISS_SUCCESS;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int i;
		boolean isDaemon = false, isHelp = false;
		String configFilePath = "configure.conf";
		int serverIndex = -1;
		
		// get input argument
		Getopt getopt = new Getopt(null, args, "f:i:dvh");
		while ((i = getopt.getopt()) != -1) {
			switch (i) {
			case 'f':
				configFilePath = getopt.getOptarg();
				break;
			case 'i':
				serverIndex = Integer.parseInt(getopt.getOptarg());
				break;
			case 'd':
				isDaemon = true;
				break;
			case 'h':
			default:
				isHelp = true;
				break;
			}
		}
		
		if (configFilePath.isEmpty() || configFilePath.compareTo(" ") == 0 || isHelp) {
			//System.out.println("\nUsage: " + args[0] + " -f conf_file -d -h");
			System.out.println("\nUsage: -f conf_file -i server_index -d -v -h");
			System.out.println("  -f conf_file       config file path");
			System.out.println("  -i server_index    server index number");
			System.out.println("  -d                 run daemon");
			System.out.println("  -h                 help\n");
			//return GlobalMessage.ISS_SUCCESS;
		}
		
		int ret = GlobalMessage.ISS_ERROR;
		if ((ret = PublicParam.loadServer(configFilePath, serverIndex)) != GlobalMessage.ISS_SUCCESS) {
			System.out.println("load index server failed:" + configFilePath);
			//return ret;
		}
		
		// check directory
		String solrHome = ServerParam.SOLR_HOME;
		if (solrHome.length() == 0) {
			LOG.error("Directory " + Configure.CONFIG_SERVER + "." + Configure.CONF_SOLR_HOME);
			//return GlobalMessage.ISS_ERROR;
		}

		//System.out.println(serverIndex);
		//String url = "D:\\Program Files\\Apache Software Foundation\\solr-tomcat\\solr\\multicore";
		System.setProperty("solr.solr.home", solrHome);
		File f = new File(solrHome, "solr.xml");
		indexServer = new IndexServer();
		//Initializer initializer = new CoreContainer.Initializer();
		//indexServer.setCoreContainer(new CoreContainer());
		//indexServer.setSolrServer(new EmbeddedSolrServer(indexServer.getCoreContainer(), "0"));
		
		//initializer = new CoreContainer.Initializer();
		//coreContainer = initializer.initialize();
		try {
			indexServer.getCoreContainer().load(solrHome, f);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		indexServer.getCoreContainer().setPersistent(true);
		indexServer.getMetaService().init();
		indexServer.getMetaService().run();
		//System.out.println("server service server:" + indexServer.getServerInfo().getServerId());
		while (indexServer.getServerInfo().getServerId() == -1)
			//count++;
			System.out.println("server service is starting...");
		System.out.println("server:" + indexServer.getServerInfo().getServerId() + " start.");
		ServerService service = new ServerService();
		service.init();
		indexServer.getBackupServer().run();
		//System.out.println("hello.");
		//indexServer.getCoreContainer().remove("core2");
		/*try {
			
			CoreAdminRequest.createCore("core2", "core2", indexServer.getSolrServer(), url + "\\core2\\conf\\solrconfig.xml", url + "\\core2\\conf\\schema.xml");
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(indexServer.getCoreContainer().getCores().size());*/
		//return GlobalMessage.ISS_SUCCESS;
		  /*//set log level
		  TBSYS_LOGGER.setLogLevel(CONFIG.get_string_value(CONFIG_PUBLIC, CONF_LOG_LEVEL));
		  TBSYS_LOGGER.setMaxFileSize(CONFIG.get_int_value(CONFIG_PUBLIC, CONF_LOG_SIZE, 1024 * 1024 * 1024));
		  TBSYS_LOGGER.setMaxFileIndex(CONFIG.get_int_value(CONFIG_PUBLIC, CONF_LOG_NUM, 10));
		  
		  //check directory
		  string work_dir = SYSPARAM_DATASERVER.work_dir_;
		  if (work_dir.size() == 0)
		  {
		    TBSYS_LOG(ERROR, "Directory %s.%s is undefined\n", CONFIG_DATASERVER, CONF_WORK_DIR);
		    return TFS_ERROR;
		  }
			
			//check storage dir
		  string storage_dir;
		  storage_dir.assign(work_dir);
		  storage_dir.append("/storage");
		  if (Func::make_directory(const_cast<char *>(storage_dir.c_str())) == TFS_ERROR)
		  {
		    TBSYS_LOG(ERROR, "Can not create directory: %s.\n", storage_dir.c_str());
		    return TFS_ERROR;
		  }

			//check Temp dir
		  storage_dir.assign(work_dir);
		  storage_dir.append("/tmp");
		  if (Func::make_directory(const_cast<char *>(storage_dir.c_str())) == TFS_ERROR)
		  {
		    TBSYS_LOG(ERROR, "Can not create directory: %s.\n", storage_dir.c_str());
		    return TFS_ERROR;
		  }

			//check mirror dir
		  storage_dir.assign(work_dir);
		  storage_dir.append("/mirror");
		  if (Func::make_directory(const_cast<char *>(storage_dir.c_str())) == TFS_ERROR)
		  {
		    TBSYS_LOG(ERROR, "Can not create directory: %s.\n", storage_dir.c_str());
		    return TFS_ERROR;
		  }

			//check log file
		  string log_file = SYSPARAM_DATASERVER.log_file_;
		  if (log_file.size() != 0 && access(log_file.c_str(), R_OK) == 0)
		  {
		    char dest_log_file[PATH_MAX];
		    sprintf(dest_log_file, "%s.%s", log_file.c_str(), Func::time_to_str(time(NULL), 1).c_str());
		    rename(log_file.c_str(), dest_log_file);
		  }*/

			/*//启动data server前先检查pid
		  string pid_file = SYSPARAM_DATASERVER.pid_file_;
		  //check pid
		  int pid = 0;
		  if ((pid = tbsys::CProcess::existPid(pid_file.c_str())))
		  {
		    cerr << "dataserver " << server_index << " has already run. Pid: " << pid << endl;
		    return TFS_ERROR;
		  }

		  //start daemon
		  pid = 0;
		  if (is_daemon)
		  {
		    pid = tbsys::CProcess::startDaemon(pid_file.c_str(), log_file.c_str());
		  }

		  //child ...  创建data server，启动之.
		  if (pid == 0)
		  {
		    signal(SIGPIPE, SIG_IGN);
		    signal(SIGHUP, SIG_IGN);
		    signal(SIGINT, data_server_signal_handler);
		    signal(SIGTERM, data_server_signal_handler);
		    signal(40, data_server_signal_handler);

		    s_data_server = new DataServer(server_index);
		    ret = s_data_server->start();
		    unlink(pid_file.c_str());
		    tbsys::gDelete(s_data_server);
		    return ret;
		  }*/
	}

}
