/**
 * 
 */
package org.act.index.manager;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Calendar;

import gnu.getopt.Getopt;

import org.act.index.common.Address;
import org.act.index.common.Configure;
import org.act.index.common.constdef.ErrorMessage;
import org.act.index.common.constdef.GlobalMessage;
import org.act.index.common.sysparam.ManagerParam;
import org.act.index.common.sysparam.PublicParam;
import org.act.index.server.IndexServer;

/**
 * @author Administrator
 *
 */
public class ManagerService extends Thread {
	private static IndexManager manager;
	private static Monitor monitor;
	private Address managerAddr;
	
	public int init() {
		try {
			manager = new IndexManager();
			monitor = new Monitor(manager.getMetaManager().getLayoutManager());
			String ip = PublicParam.CONFIG.getStringValue(Configure.CONFIG_MANAGER, Configure.CONF_IP_ADDR, null);
			int port = PublicParam.CONFIG.getIntValue(Configure.CONFIG_MANAGER, Configure.CONF_PORT, 0);
			
			managerAddr = new Address(ip, port);
			String url = "rmi:/" + managerAddr.getAddr().toString() + "/manager";
			//echo
			System.out.println(url);
			LocateRegistry.createRegistry(port);
			Naming.rebind(url, manager);
			System.out.println("rmi:manager is ready!");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return GlobalMessage.ISS_SUCCESS;
	}
	
	public void start() {
		//return GlobalMessage.ISS_SUCCESS;
		manager.start();
		//monitor.start();
	}
	
	/*public void stop() {
		
	}*/
	
	public static int inIPList(int localIP) {
		String ipList = PublicParam.CONFIG.getStringValue(Configure.CONFIG_MANAGER, Configure.CONF_IP_ADDR_LIST, null);
		//ProcessBuilder pb = new ProcessBuilder(command)
		//const char *ip_list = CONFIG.get_string_value(CONFIG_NAMESERVER, CONF_IP_ADDR_LIST);
		return 0;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int i;
		boolean isDaemon = false, isHelp = false;
		String configFilePath = "configure.conf";
		
		// get input argument
		Getopt getopt = new Getopt(null, args, "f:dh");
		while ((i = getopt.getopt()) != -1) {
			switch (i) {
			case 'f':
				configFilePath = getopt.getOptarg();
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
			System.out.println("\nUsage: " + args[0] + " -f conf_file -d -h");
			System.out.println("  -f conf_file   config file path");
			System.out.println("  -d             run daemon");
			System.out.println("  -h             help\n");
			//return GlobalMessage.ISS_SUCCESS;
		}
		
		//load config file
		int ret = GlobalMessage.ISS_SUCCESS;
		if ((ret = PublicParam.loadManager(configFilePath)) != GlobalMessage.ISS_SUCCESS) {
			System.out.println("load index manager failed:" + configFilePath);
			return ;
		}
		
		ManagerService managerService = new ManagerService();
		managerService.init();
		managerService.start();
		while (true) {
			try {
				sleep(ManagerParam.HEART_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(manager.getMetaManager().getLayoutManager().getServerList().size());
			//System.out.println(manager.getMetaManager().getLayoutManager().getCoreList().size());
			
			//Calendar.getInstance();
		}
		  // load config file
		  /*int ret = TFS_SUCCESS;
		  if ((ret = SysParam::instance().load(conf_file_path.c_str())) != TFS_SUCCESS)
		  {
		    fprintf(stderr, "SysParam::load failed:%s", conf_file_path.c_str());
		    return ret;
		  }
		  TBSYS_LOGGER.setLogLevel(CONFIG.get_string_value(CONFIG_PUBLIC, CONF_LOG_LEVEL, "debug"));
			TBSYS_LOGGER.setMaxFileSize(CONFIG.get_int_value(CONFIG_PUBLIC, CONF_LOG_SIZE, 0x40000000));
			TBSYS_LOGGER.setMaxFileIndex(CONFIG.get_int_value(CONFIG_PUBLIC, CONF_LOG_NUM, 0x0A));
		  const char *ip = CONFIG.get_string_value(CONFIG_NAMESERVER, CONF_IP_ADDR);
		  const char *dev_name = CONFIG.get_string_value(CONFIG_NAMESERVER, CONF_DEV_NAME);
		  uint64_t local_ip = Func::get_local_addr(dev_name);
		  if (Func::get_addr(ip) != local_ip && in_ip_list(local_ip) == 0)
		  {
		    TBSYS_LOG(ERROR, "ip '%s' is not local ip, local ip: %s", ip, tbsys::CNetUtil::addrToString(local_ip).c_str());
		    return EXIT_GENERAL_ERROR;
		  }

		  const char *work_dir = CONFIG.get_string_value(CONFIG_NAMESERVER, CONF_WORK_DIR);
		  if (work_dir == NULL)
		  {
		    TBSYS_LOG(ERROR, "directory not found");
		    return EXIT_CONFIG_ERROR;
		  }
		  if (!DirectoryOp::create_full_path(work_dir))
		  {
		    TBSYS_LOG(ERROR, "create directory(%s) failed", work_dir);
		    return ret;
		  }

		  char *pid_file_path = CONFIG.get_string_value(CONFIG_NAMESERVER, CONF_LOCK_FILE);
		  int32_t pid = 0;
		  if ((pid = tbsys::CProcess::existPid(pid_file_path)))
		  {
		    fprintf(stderr, "Program has been running: pid(%d)", pid);
		    return EXIT_SYSTEM_ERROR;
		  }

		  const char *log_file_path = CONFIG.get_string_value(CONFIG_NAMESERVER, CONF_LOG_FILE);
		  if (access(log_file_path, R_OK) == 0)
		  {
		    char dest_log_file_path[256];
		    sprintf(dest_log_file_path, "%s.%s", log_file_path, Func::time_to_str(time(NULL), 1).c_str());
		    rename(log_file_path, dest_log_file_path);
		  }

		  // start daemon process
		  pid = 0;
		  if (is_daemon)
		  {
		    pid = tbsys::CProcess::startDaemon(pid_file_path, log_file_path);
		  }

		  // start service
		  if (pid == 0)
		  {
		    signal(SIGPIPE, SIG_IGN);
		    signal(SIGHUP, SIG_IGN);
		    signal(SIGINT, SIG_IGN);
		    signal(SIGTERM, SIG_IGN);
		    signal(SIGUSR1, SIG_IGN);
		    signal(40, SIG_IGN);

		    try
		    {
		      ARG_NEW(g_tfs_ns_service_, Service);
		      ret = g_tfs_ns_service_->start();
		    }
		    catch (std::exception& ex)
		    {
		      TBSYS_LOG(WARN, "Catch execption (%s), must be exit...", ex.what());
		      g_tfs_ns_service_->stop();
		    }
		    catch (...)
		    {
		      TBSYS_LOG(WARN, "Catch unknow execption, must be exit...");
		      g_tfs_ns_service_->stop();
		    }
		    tbsys::gDelete(g_tfs_ns_service_);
		  }
		  return ret;*/
	}

}
