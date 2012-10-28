/**
 * 
 */
package org.act.index.manager;

import java.net.Inet4Address;
import org.act.index.security.MD5;
import org.act.index.server.IMetaService;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;  
import java.rmi.server.UnicastRemoteObject;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.act.index.common.Address;
import org.act.index.common.Configure;
import org.act.index.common.CoreInfo;
import org.act.index.common.ServerInfo;
import org.act.index.common.UserInfo;
import org.act.index.common.constdef.ErrorMessage;
import org.act.index.common.constdef.GlobalMessage;
import org.act.index.common.sysparam.ManagerParam;
import org.act.index.common.sysparam.PublicParam;
import org.act.index.common.sysparam.ServerParam;
import org.act.index.hibernate.user.User;
import org.act.index.hibernate.user.UserDAO;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.solr.common.util.Hash;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author yeyue
 *
 */
public class IndexManager extends UnicastRemoteObject implements IIndexManager, Runnable{
	private static final Logger LOG = Logger.getLogger(IndexManager.class);
	
	private MetaManager metaManager;
	private RuntimeGlobalInfo globalInfo;
	
	// threads
	private CheckServerThread checkServerThread;
	private CheckCoreThread checkCoreThread;
	private Thread balanceThread;
	private Thread replicateThread;
	private Thread timeoutThread;
	private ThreadPoolExecutor mainTaskThreadPool;
	private int mainTaskQueueSize;
	private HeartManager heartManager;

	/**
	 * 
	 */
	public IndexManager() throws RemoteException{
		// TODO Auto-generated constructor stub
		metaManager = new MetaManager();
		checkServerThread = new CheckServerThread();
		checkCoreThread = new CheckCoreThread();
		globalInfo = new RuntimeGlobalInfo();
	}
	
	/**
	 * @return the metaManager
	 */
	public MetaManager getMetaManager() {
		return metaManager;
	}

	/**
	 * @param metaManager the metaManager to set
	 */
	public void setMetaManager(MetaManager metaManager) {
		this.metaManager = metaManager;
	}

	public int start() {
		int ret = GlobalMessage.ISS_SUCCESS;
		if (initGlobalInfo() != GlobalMessage.ISS_SUCCESS)
			return GlobalMessage.ISS_ERROR;
		
		if ((ret = metaManager.init()) != GlobalMessage.ISS_SUCCESS)
			return ret;
		
		syncUserData();
		// start threads
	    initHandleThreads();

	    return GlobalMessage.ISS_SUCCESS;
	}
	
	public int stop() {
		return GlobalMessage.ISS_SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	public int doCheckServer() {
		return 0;
	}
	
	public int doCheckCores() {
		return 0;
	}
	
	public int doTimeout() {
		return 0;
	}
	
	public int doBalance() {
		return 0;
	}

	// check the dead server list and the writable server list
	/*public int checkServer() {
		HashSet<Integer> deadServerSet, writableServerSet;
		deadServerSet = new HashSet<Integer>();
		writableServerSet = new HashSet<Integer>();
		int expireTime = 0; 
		// TODO
	    //int32_t expire_time = now - SYSPARAM_NAMESERVER.ds_dead_time_ * 4;
	    
		// get the list of the dead server and available server.
	    metaManager.getLayoutManager().checkServer(ManagerParam.SERVER_DEAD_TIME, deadServerSet, writableServerSet);
		
	    // check the status of the dead server
	    // if the info is null or server is still alive, then skip
	    // else mark the server and exclude it from the balance
	    int serverId = -1;
	    ServerCollect serverCollect;
	    ServerInfo serverInfo;
	    Iterator<Integer> iterator = deadServerSet.iterator();
	    while (iterator.hasNext()) {
	    	serverId = iterator.next();
	    	serverCollect = metaManager.getLayoutManager().getServerCollect(serverId);
	    	if (serverCollect == null)
	    		continue;
	    	serverInfo = serverCollect.getServerInfo();
	    	if (serverInfo.getLastUpdateTime() > expireTime && serverCollect.isAlive())
	    		continue;
	    	// TODO
	    	if (ns_global_info_.destroy_flag_ == NS_DESTROY_FLAGS_YES)
	  	      break;
	    	LOG.info("server:(" + serverId + ") is down.");
	    	metaManager.leaveServer(serverId);
	    	
	    	// TODO
	    	if (ns_global_info_.owner_role_ == NS_ROLE_MASTER)
	  	      replicate_thread_.inc_stop_balance_count();
	    }
	    
	    if (writableServerSet.size() == 0)
	    	return GlobalMessage.ISS_SUCCESS;
	    
	    // select a writable server randomly, and check its available cores which were used for write primary.
	    Random random = new Random();
	    //Random random = new Random(seed);
	    int writableServerSetSize = writableServerSet.size();
	    int startIndex = random.nextInt() % writableServerSetSize;
	    Object[] writableServerArray = writableServerSet.toArray();
	    for (int i = 0; i < writableServerSetSize; i++) {
	    	serverId = (Integer)writableServerArray[startIndex++ % writableServerSetSize];
	    	metaManager.checkPrimaryWritableCore(serverId, ManagerParam.ADD_PRIMARY_CORE_COUNT, true);
	    	// TODO
	    	if ((i >= 0x0A) | (ns_global_info_.destroy_flag_ == NS_DESTROY_FLAGS_YES))
	  	      break;
	    }
	    
	    return GlobalMessage.ISS_SUCCESS;
	}*/
	
	/* (non-Javadoc)
	 * @see org.act.index.manager.IIndexManager#locate(int, java.util.HashSet, org.act.index.common.Address)
	 */
	@Override
	public HashSet<Address> locate(long userId)
			throws RemoteException {
		// TODO Auto-generated method stub
		HashSet<Integer> serverSet = new HashSet<Integer>();
		CoreCollect coreCollect = metaManager.getCorebyUser(userId);
		int coreId = coreCollect.getCoreInfo().getCoreId();
		LOG.info("core id:" + coreId);
		metaManager.readCoreInfo(coreId, serverSet);
		ServerCollect masterServer = metaManager.getServerByCore(coreId);
		int master = masterServer.getServerInfo().getServerId();
		LOG.info("server set size:" + serverSet.size());
		Iterator<Integer> iterator = serverSet.iterator();
		HashSet<Address> addrs = new HashSet<Address>();
		while (iterator.hasNext()) {
			int serverId = iterator.next();
			ServerCollect serverCollect = metaManager.getLayoutManager().getServerCollect(serverId);
			String ip = serverCollect.getServerInfo().getServerIp();
			int port = serverCollect.getServerInfo().getServerPort();
			//ServerCollect serverCollect = metaManager.getLayoutManager().getServerCollect(serverId);
			Address address = new Address(ip, port, serverId, coreId);
			LOG.info(ip + ":" + port + "/" + "core" + coreId + "@server" + serverId);
			if (serverId == master) {
				LOG.info("master server(" + serverId + ").");
				address.setMaster(true);
			}
			addrs.add(address);
		}
		//ServerCollect masterServer = metaManager.getServerByCore(coreId);
		//master = new Address(masterServer.getServerAddr().getServerAddr(), coreId);
		//return GlobalMessage.ISS_SUCCESS;
		return addrs;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IIndexManager#createUser()
	 */
	@Override
	public UserInfo createUser(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		//metaManager.getLayoutManager().createUserCollect();
		//System.out.println("create user.");
		if (getUser(username) != null)
			//LOG.debug("user")
			return null;
		UserCollect userCollect = metaManager.addNewUser(-1);
		if (userCollect == null)
			return null;
		UserDAO userdao = new UserDAO();
		Session session = userdao.getSession();
		Transaction tx = session.beginTransaction();
		userdao.save(new User(userCollect.getUserInfo().getUserId(), username, MD5.md5(password), userCollect.getCoreId()));
		tx.commit();
		session.close();
		return userCollect.getUserInfo();
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.manager.IIndexManager#getUser(java.lang.String)
	 */
	@Override
	public User getUser(String username) {
		// TODO Auto-generated method stub
		UserDAO userdao = new UserDAO();
		Session session = userdao.getSession();
		List users = userdao.findByUserName(username);
		session.close();
		if (users.size() > 0)
			return (User)users.get(0);
		return null;
	}

	private List<User> getAllUsers() {
		UserDAO userdao = new UserDAO();
		Session session = userdao.getSession();
		List<User> users = userdao.findAll();
		session.close();
		return users;
	}
	
	private void syncUserData() {
		List<User> users = getAllUsers();
		int size = users.size();
		//List<UserCollect> userList = new ArrayList<UserCollect>(size);
		//LOG.debug("size:" + size);
		//metaManager.getLayoutManager().setUserList(userList); 
		//LOG.debug("size:" + userList.size());
		//LOG.debug("size:" + metaManager.getLayoutManager().getUserList().size());
		for (User user : users) {
			metaManager.getLayoutManager().createUserCollect(user.getUserId(), user.getCoreId());
		}
		metaManager.getLayoutManager().setMaxUserId(size - 1);
	}
	/* (non-Javadoc)
	 * @see org.act.index.manager.IIndexManager#getCoreStatus(java.util.HashMap)
	 */
	@Override
	public int getCoreStatus(int serverId, TreeMap<Integer, CoreInfo> cores) throws RemoteException{
		// TODO Auto-generated method stub
		//System.out.println(cores.size());
		for (Integer key : cores.keySet()) {
			if (metaManager.updateCoreInfo(cores.get(key), serverId, true) != GlobalMessage.ISS_SUCCESS)
				return GlobalMessage.ISS_ERROR;
		}
		return GlobalMessage.ISS_SUCCESS;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IIndexManager#getServerStatus(org.act.index.common.ServerInfo)
	 */
	@Override
	public ServerInfo getServerStatus(ServerInfo serverInfo, boolean isnew) throws RemoteException {
		// TODO Auto-generated method stub		
		metaManager.joinServer(serverInfo, isnew);
			//return GlobalMessage.ISS_SUCCESS;
			//System.out.println("hello");
			//System.out.println("d" + serverInfo.getServerId());
			//return serverInfo;	
		return serverInfo;
		//return GlobalMessage.ISS_ERROR;
	}

	
	/* (non-Javadoc)
	 * @see org.act.index.manager.IIndexManager#createCore(int, int)
	 */
	@Override
	public int createCore(int serverId, int coreId)
			throws RemoteException {
		// TODO Auto-generated method stub
		
		return coreId;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IIndexManager#createCore(int)
	 */
	@Override
	public int createCore(int serverId) throws RemoteException {
		// TODO Auto-generated method stub
		CoreCollect core = metaManager.addNewCore(serverId);
		return core.getCoreInfo().getCoreId();
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IIndexManager#createCore()
	 */
	@Override
	public int createCore() throws RemoteException {
		// TODO Auto-generated method stub
		//metaManager.addNewCore(serverId)
		CoreCollect core = metaManager.addNewCore(-1);
		return core.getCoreInfo().getCoreId();
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IIndexManager#deleteCore(int, int)
	 */
	@Override
	public int deleteCore(int serverId, int coreId) throws RemoteException {
		// TODO Auto-generated method stub
		ServerCollect server = metaManager.getLayoutManager().getServerCollect(serverId);
		if (server != null && server.isAlive() && !server.isDiskFull()) {
			String url = "rmi://" + server.getServerInfo().getServerIp() + 
				":" + server.getServerInfo().getServerPort() + 
				"/meta@server" + serverId;
			try {
				IMetaService metaService = (IMetaService)Naming.lookup(url);
				CoreCollect core = metaManager.getLayoutManager().getCoreCollect(coreId);
				if (core != null) {
					metaManager.removeCoreInfo(coreId, serverId);
					metaService.removeCore(coreId);
				}
				return GlobalMessage.ISS_SUCCESS;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return GlobalMessage.ISS_ERROR;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IIndexManager#deleteCore(int)
	 */
	@Override
	public int deleteCore(int coreId) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IIndexManager#joinServer()
	 */
	@Override
	public int joinServer() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IIndexManager#leaveServer()
	 */
	@Override
	public int leaveServer() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IIndexManager#updateCore()
	 */
	@Override
	public int updateCore() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.act.index.manager.IIndexManager#writeCommit()
	 */
	@Override
	public int writeCommit() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	private int initGlobalInfo() {
		// get manager ip list and port from CONF FILE
		String iplist = PublicParam.CONFIG.getStringValue(Configure.CONFIG_MANAGER, Configure.CONF_IP_ADDR_LIST, null);
		int port = PublicParam.CONFIG.getIntValue(Configure.CONFIG_MANAGER, Configure.CONF_PORT, 0);
		if (iplist == null || port <= 0) {
			LOG.error("init manager ip is null or manager port <= 0, must be exit.");
			return ErrorMessage.EXIT_GENERAL_ERROR;
		}
		LOG.debug("manager list(" + iplist + "), manager port(" + port + ").");
		List<Address> addrs = new ArrayList<Address>();
		
		String[] ips = iplist.split("\\|");
		for (String string : ips) {
			addrs.add(new Address(string, port));
		}
		if (addrs.size() != 2) {
			LOG.debug("must have to manager, check you manager list");
			return ErrorMessage.EXIT_GENERAL_ERROR;
		}
		
		/*String localIP = "";
		try {
			localIP = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Address localAddr = new Address(localIP, port);*/
		String localIP = PublicParam.CONFIG.getStringValue(Configure.CONFIG_MANAGER, Configure.CONF_IP_ADDR, null);
		int i;
		for (i = 0; i < ips.length && localIP.compareTo(ips[i]) != 0; i++) {
			;
		}
		if (i == ips.length) {
			LOG.error("local ip(" + localIP + ") not in ip list, must be exit.");
			return ErrorMessage.EXIT_GENERAL_ERROR;
		}
	    // get local ip from CONF FILE
	   /* const char *dev_name = CONFIG.get_string_value(CONFIG_NAMESERVER, CONF_DEV_NAME);
	    uint32_t local_ip = Func::get_local_addr(dev_name);
	    if (dev_name == NULL || local_ip == 0)
	    {
	      TBSYS_LOG(ERROR, "get dev name is null or local ip == 0 , must be exit");
	      return EXIT_GENERAL_ERROR;
	    }

	    uint64_t local_ns_id = 0;
	    IpAddr* adr = (IpAddr*) (&local_ns_id);
	    adr->ip_ = local_ip;
	    adr->port_ = ns_port;

	    if (find(ns_ip_list.begin(), ns_ip_list.end(), local_ns_id) == ns_ip_list.end())
	    {
	      TBSYS_LOG(ERROR, "local ip(%s) not in ip_list(%s) , must be exit",
	          tbsys::CNetUtil::addrToString(local_ns_id).c_str(), ns_ip);
	      return EXIT_GENERAL_ERROR;
	    }*/
	    // set manager global info
		
	    /*for (vector<uint64_t>::iterator it = ns_ip_list.begin(); it != ns_ip_list.end(); ++it)
	    {
	      if (local_ns_id == *it)
	      ns_global_info_.owner_ip_port_ = *it;
	      else
	      ns_global_info_.other_side_ip_port_ = *it;
	    }*/

		String ip = PublicParam.CONFIG.getStringValue(Configure.CONFIG_MANAGER, Configure.CONF_IP_ADDR, null);
	    /*ns_global_info_.switch_time_ = 0;
	    ns_global_info_.owner_status_ = NS_STATUS_UNINITIALIZE;

	    const char *ip = CONFIG.get_string_value(CONFIG_NAMESERVER, CONF_IP_ADDR);
	    ns_global_info_.vip_ = Func::get_addr(ip);

	    if (Func::is_local_addr(ns_global_info_.vip_))
	    ns_global_info_.owner_role_ = NS_ROLE_MASTER;
	    else
	    ns_global_info_.owner_role_ = NS_ROLE_SLAVE;

	    TBSYS_LOG(DEBUG, "i %s the master server", ns_global_info_.owner_role_ == NS_ROLE_MASTER ? "am" : "am not");

	    ns_global_info_.other_side_role_ = NS_ROLE_NONE;
	    return TFS_SUCCESS;*/
		return GlobalMessage.ISS_SUCCESS;
	}
	
	private void initHandleThreads() {
		checkServerThread.start();
		checkCoreThread.start();
	}
	
	private void initHandleTaskAndHeartThreads () {
		// start thread for main task queue
		int threadCount = PublicParam.CONFIG.getIntValue(Configure.CONFIG_MANAGER, Configure.CONF_THREAD_COUNT, 1);
		mainTaskQueueSize = PublicParam.CONFIG.getIntValue(Configure.CONFIG_MANAGER, Configure.CONF_TASK_MAX_QUEUE_SIZE, 100);
		mainTaskThreadPool = new ThreadPoolExecutor(0, mainTaskQueueSize, Integer.parseInt(Configure.CONF_KEEP_ALIVE_TIME), 
				TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(threadCount), new ThreadPoolExecutor.DiscardOldestPolicy());
		//mainTaskThreadPool.
	    //int32_t thead_count = CONFIG.get_int_value(CONFIG_NAMESERVER, CONF_THREAD_COUNT, 1);
	    //main_task_queue_thread_.setThreadParameter(thead_count, this, NULL);
	    //main_task_queue_thread_.start();
		
	    // start thread for handling heartbeat from server
		int heartThreadCount = PublicParam.CONFIG.getIntValue(Configure.CONFIG_MANAGER, Configure.CONF_HEART_THREAD_COUNT, 2);
		int heartMaxQueueSize = PublicParam.CONFIG.getIntValue(Configure.CONFIG_MANAGER, Configure.CONF_HEART_MAX_QUEUE_SIZE, 10);
		heartManager.init(heartThreadCount, heartMaxQueueSize);
		LOG.info("manager start: " + globalInfo.getOwnerIpPort());
	    //int32_t heart_thread_count = CONFIG.get_int_value(CONFIG_NAMESERVER, CONF_HEART_THREAD_COUNT, 2);
	    //int32_t heart_max_queue_size = CONFIG.get_int_value(CONFIG_NAMESERVER, CONF_HEART_MAX_QUEUE_SIZE, 10);
	    //heart_mgr_.initialize(heart_thread_count, heart_max_queue_size);

	    //main_task_queue_size_ = CONFIG.get_int_value(CONFIG_NAMESERVER, CONF_TASK_MAX_QUEUE_SIZE, 100);
	    //TBSYS_LOG(INFO, "fsnamesystem::start: %s", tbsys::CNetUtil::addrToString(ns_global_info_.owner_ip_port_).c_str());
	}
	
	private int checkServer(Calendar now) {
		HashSet<Integer> deadServerSet = new HashSet<Integer>();
		HashSet<Integer> writableServerSet = new HashSet<Integer>();
		long expireTime = now.getTimeInMillis() - ManagerParam.SERVER_DEAD_TIME;
		
		// get the list of the dead server and available server
		metaManager.getLayoutManager().checkServer(ManagerParam.SERVER_DEAD_TIME, deadServerSet, writableServerSet);
	    // check the status of the dead server
		// if the info is null or server is still alive, then skip
		// else mark the server exclude it from the balance
		int serverId = -1;
		ServerCollect serverCollect = new ServerCollect();
		ServerInfo serverInfo = new ServerInfo();
		Iterator<Integer> iterator = deadServerSet.iterator();
		while (iterator.hasNext()) {
			serverId = iterator.next();
			serverCollect = metaManager.getLayoutManager().getServerCollect(serverId);
			if (serverCollect == null)
				continue;
			serverInfo = serverCollect.getServerInfo();
			if (serverInfo.getLastUpdateTime().getTimeInMillis() > expireTime && serverCollect.isAlive())
				continue;
			if (globalInfo.getDestroyFlag())
				break;
			LOG.info("server:(" + serverId + ") is down.");
			metaManager.leaveServer(serverId);
			//if (ns_global_info_.owner_role_ == NS_ROLE_MASTER)
			//      replicate_thread_.inc_stop_balance_count();
		}
		
		int writableSize = writableServerSet.size();
	    if (writableSize == 0)
	    	return GlobalMessage.ISS_SUCCESS;
	    
	    // select a writable server randomly, and check its available cores which were used for write primary.
	    Random random = new Random();
	    int rand = random.nextInt();
	    int startIndex = Math.abs(rand) % writableSize;
	    LOG.debug("startIndex:" + startIndex + " rand:" + rand + " writableSize:" + writableSize);
	    Object[] writableServers = writableServerSet.toArray();
	    for (int i = 0; i < writableSize; i++) {
	    	serverId = (Integer)writableServers[startIndex++ % writableSize];
	    	metaManager.checkPrimaryWritableCore(serverId, ManagerParam.ADD_PRIMARY_CORE_COUNT, true);
	    	if (globalInfo.getDestroyFlag())
	    		break;
	    }
	    return GlobalMessage.ISS_SUCCESS;
	}
	
	// check whether it's the time to do task
	private static boolean checkTaskInterval(Calendar now, Calendar lastCheckTime,long interval) {
		if (now.getTimeInMillis() - lastCheckTime.getTimeInMillis() >= interval) {
			lastCheckTime = now;
			return true;
		}
		return false;
	}
	
	class CheckServerThread extends Thread {

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (!globalInfo.getDestroyFlag()) {
				checkServer(Calendar.getInstance());
				try {
					sleep(ManagerParam.HEART_INTERVAL);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	class CheckCoreThread extends Thread {

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (!globalInfo.getDestroyFlag()) {
				//checkServer();
				try {
					//LOG.debug("replicate check interval:" + ManagerParam.REPLICATE_CHECK_INTERVAL);
					sleep(ManagerParam.REPLICATE_CHECK_INTERVAL);
					int size = metaManager.getLayoutManager().getAliveServerNum();
					LOG.debug("alive server num:" + size + " min replication:" + ManagerParam.MIN_REPLICATION);
					if (size < ManagerParam.MIN_REPLICATION)
						continue;
					List<CoreCollect> cores = metaManager.getLayoutManager().getCoreList();
					LOG.debug("check cores.");
					for (CoreCollect core : cores) {
						if (core != null && !Replicate.check(core)) {
							LOG.debug("check core:" + core.getCoreInfo().getCoreId());
							new Replicate(metaManager, core).start();
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/*List<Integer> loseCores = new ArrayList<Integer>();
		    Calendar now = Calendar.getInstance();
		    int serverSize = 0;
		    int maxReplPlanCores = 0;
			boolean needCheckReplica = false;
			int CHECK_TASK = 2;
			Calendar[] lastCheckTime = {Calendar.getInstance(), Calendar.getInstance()};
			boolean[] needCheck = {false, false};
			long[] checkInterval = {ManagerParam.HEART_INTERVAL, ManagerParam.REPLICATE_CHECK_INTERVAL};
			
			while (!globalInfo.getDestroyFlag()) {
				//checkServer();
				try {
					sleep(ManagerParam.REPLICATE_CHECK_INTERVAL);
					if (globalInfo.getDestroyFlag())
						break;
					now = Calendar.getInstance();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//calc time interval
				needCheckReplica = false;
				for (int i = 0; i < CHECK_TASK; i++) {
					needCheck[i] = checkTaskInterval(now, lastCheckTime[i], checkInterval[i]);
					if (i != 0 && needCheck[i])
						needCheckReplica = true;
				}
				
				// check all dead servers lost heartbeat
				if (!needCheckReplica)
					continue;
				
				// reset check parameters
				serverSize = metaManager.getLayoutManager().getAliveServerNum();
				maxReplPlanCores = serverSize * ManagerParam.REPLICATE_MAX_COUNT_PER_SERVER;
				loseCores.clear();
				
				//start scanner
				//ReplicateExecutor replExecutor = new ReplicateExecutor(metaManager);
				for (CoreCollect core : metaManager.getLayoutManager().getCoreList()) {
					if (core != null && !Replicate.check(core)) {
						new Replicate(metaManager, core).start();
					}
				}
			}*/
		}
	}
}

class OwnerCheckTimerTask extends TimerTask {
	private IndexManager indexManager;
	private int mainTaskQueueSize;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}