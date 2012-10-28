/**
 * 
 */
package org.act.index.client;

import java.util.List;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.act.index.common.Address;
import org.act.index.common.MailDoc;
import org.act.index.common.MailQuery;
import org.act.index.common.UserInfo;
import org.act.index.common.constdef.GlobalMessage;
import org.act.index.hibernate.user.User;
import org.act.index.manager.IIndexManager;
import org.act.index.manager.LayoutManager;
import org.act.index.server.ISolrService;
import org.act.index.server.IndexServiceException;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrInputDocument;

import sun.util.logging.resources.logging;

/**
 * @author yeyue
 *
 */
public class ClientSession {
	private static final Logger LOG = Logger.getLogger(ClientSession.class);
	
	private String managerIpPortString;
	private String managerHost;
	private int managerPort;
	private long userId;
	private String url;
	
	/**
	 * 
	 */
	public ClientSession() {
		// TODO Auto-generated constructor stub
		this.userId = -1;
	}
	
	/**
	 * @param managerIpPortString
	 */
	public ClientSession(String managerIpPortString) {
		super();
		this.managerIpPortString = managerIpPortString;
		this.userId = -1;
	}
	
	public ClientSession(long userId) {
		super();
		this.userId = userId;
	}
	
	public ClientSession(String managerIpPortString, long userId) {
		this.managerIpPortString = managerIpPortString;
		this.userId = userId;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#init()
	 */
	public int init() {
		if (managerIpPortString.isEmpty() || managerIpPortString.compareTo(" ") == 0) {
			LOG.error("manager ip port(" + managerIpPortString.toString() + ") invalid.");
			return GlobalMessage.ISS_ERROR;
		}
		InetSocketAddress addr = InetSocketAddress.createUnresolved(managerHost, managerPort);
		if (addr == null) {
			LOG.error("manager ip port(" + managerIpPortString.toString() + ") invalid.");
			return GlobalMessage.ISS_ERROR;
		}
		//url = "rmi://" + managerIpPortString + "/IndexManager";
		return GlobalMessage.ISS_SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#getManagerIpPortString()
	 */
	public String getManagerIpPortString() {
		return managerIpPortString;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#setManagerIpPortString(java.lang.String)
	 */
	public void setManagerIpPortString(String managerIpPortString) {
		this.managerIpPortString = managerIpPortString;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#getManagerIPort()
	 */
	public int getManagerIPort() {
		return managerPort;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#setManagerPort(int)
	 */
	public void setManagerPort(int managerPort) {
		this.managerPort = managerPort;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#getManagerHost()
	 */
	public String getManagerHost() {
		return managerHost;
	}

	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#setManagerHost(java.lang.String)
	 */
	public void setManagerHost(String managerHost) {
		this.managerHost = managerHost;
	}

	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#getUserId()
	 */
	public long getUserId() {
		return userId;
	}

	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#setUserId(long)
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#getManagerPort()
	 */
	public int getManagerPort() {
		return managerPort;
	}

	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#destroy()
	 */
	public void destroy() {
		
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#createUserInfo(java.lang.String, java.lang.String)
	 */
	public UserInfo createUserInfo(String username, String password) {
		try {
			String url = "rmi://" + managerIpPortString + "/manager";
			IIndexManager manager = (IIndexManager)Naming.lookup(url);
			//System.out.println("error.");
			UserInfo userInfo = manager.createUser(username, password);
			return userInfo;
			//System.out.println(userInfo.getUserId());
		} catch (MalformedURLException e) {
			// TODO: handle exception
			LOG.error("MalformedURLException.");
		}
		catch (RemoteException e) {
			// TODO: handle exception
			LOG.error(userId + "RemoteException.");
		}
		catch (NotBoundException e) {
			// TODO: handle exception
			LOG.error("NotBoundException.");
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#getUser(java.lang.String)
	 */
	public User getUser(String username) {
		try {
			String url = "rmi://" + managerIpPortString + "/manager";
			IIndexManager manager = (IIndexManager)Naming.lookup(url);
			//System.out.println("error.");
			User user = manager.getUser(username);
			return user;
			//System.out.println(userInfo.getUserId());
		} catch (MalformedURLException e) {
			// TODO: handle exception
			LOG.error("MalformedURLException.");
		}
		catch (RemoteException e) {
			// TODO: handle exception
			LOG.error(userId + "RemoteException.");
		}
		catch (NotBoundException e) {
			// TODO: handle exception
			LOG.error("NotBoundException.");
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#getCoreInfo(long, java.util.HashSet, org.act.index.common.Address)
	 */
	public HashSet<Address> getCoreInfo(long userId, HashSet<Address> addrs, Address master) {
		try {
			String url = "rmi://" + managerIpPortString + "/manager";
			IIndexManager manager = (IIndexManager)Naming.lookup(url);
			addrs = manager.locate(userId);
			//LOG.info("addrs size:" + addrs.size());
			Iterator<Address> iterator = addrs.iterator();
			
			while (iterator.hasNext()) {
				Address addr = iterator.next();
				//LOG.info("core" + addr.getCoreId() + "@server" + addr.getServerId());
				if (addr.isMaster()) {
					master.setAddr(addr.getAddr());
					master.setServerId(addr.getServerId());
					master.setCoreId(addr.getCoreId());
					//LOG.info("core" + master.getCoreId() + "@server" + master.getServerId());
				}		
			}
			//System.out.println(master.getCoreId())
			return addrs;
		}
		catch (MalformedURLException e) {
			// TODO: handle exception
			LOG.error("MalformedURLException.");
		}
		catch (RemoteException e) {
			// TODO: handle exception
			LOG.error(userId + "RemoteException.");
		}
		catch (NotBoundException e) {
			// TODO: handle exception
			LOG.error("NotBoundException.");
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#createIndex(long, org.act.index.common.MailDoc, java.util.HashSet, org.act.index.common.Address)
	 */
	public int createIndex(long userId, MailDoc mailDoc, HashSet<Address> addrs, Address master) {
		try {
			System.out.println("create index.");
			String name = "core" + master.getCoreId() + "@server" + master.getServerId();
			//String port = String.valueOf(master.getAddr().getPort() + master.getCoreId());
			String port = String.valueOf(master.getAddr().getPort() + master.getCoreId() + 1);
			String url = "rmi://" + master.getAddr().getAddress().getHostAddress() + ":" + port + "/" + name;
			//System.out.println(url);
			//LOG.debug(url);
			ISolrService solrService = (ISolrService)Naming.lookup(url);
			SolrInputDocument doc = MailDoc.converse(mailDoc);
			return solrService.createIndex(userId, addrs, doc, true);
			// TODO
		}
		catch (MalformedURLException e) {
			// TODO: handle exception
			LOG.error("MalformedURLException.");
		}
		catch (RemoteException e) {
			// TODO: handle exception
			LOG.error("RemoteException.");
		}
		catch (NotBoundException e) {
			// TODO: handle exception
			LOG.error("NotBoundException.");
		} 
		return GlobalMessage.ISS_ERROR;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#createIndex(long, java.util.List, java.util.HashSet, org.act.index.common.Address)
	 */
	public int createIndex(long userId, List<MailDoc> mailDocs, HashSet<Address> addrs, Address master) {
		try {
			String name = "core" + master.getCoreId() + "@server" + master.getServerId();
			String port = String.valueOf(master.getAddr().getPort() + master.getCoreId() + 1);
			String url = "rmi://" + master.getAddr().getAddress().getHostAddress() + ":" + port + "/" + name;
			//System.out.println(url);
			//LOG.debug(url);
			ISolrService solrService = (ISolrService)Naming.lookup(url);
			List<SolrInputDocument> doc = MailDoc.converse(mailDocs);
			return solrService.createIndex(userId, addrs, doc, true);
			// TODO
		}
		catch (MalformedURLException e) {
			// TODO: handle exception
			LOG.error("MalformedURLException.");
		}
		catch (RemoteException e) {
			// TODO: handle exception
			LOG.error("RemoteException.");
		}
		catch (NotBoundException e) {
			// TODO: handle exception
			LOG.error("NotBoundException.");
		}
		return GlobalMessage.ISS_ERROR;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#deleteIndex(long, org.act.index.common.MailQuery, java.util.HashSet, org.act.index.common.Address)
	 */
	public int deleteIndex(long userId, MailQuery mailQuery, HashSet<Address> addrs, Address master) {
		try {
			String name = "core" + master.getCoreId() + "@server" + master.getServerId();
			String port = String.valueOf(master.getAddr().getPort() + master.getCoreId() + 1);
			String url = "rmi://" + master.getAddr().getAddress().getHostAddress() + ":" + port + "/" + name;
			//System.out.println(url);
			ISolrService solrService = (ISolrService)Naming.lookup(url);
			SolrQuery query = MailQuery.converse(userId, mailQuery);
			return solrService.delete(userId, addrs, query, true);
		} catch (MalformedURLException e) {
			// TODO: handle exception
			LOG.error("MalformedURLException.");
		}
		catch (RemoteException e) {
			// TODO: handle exception
			LOG.error("RemoteException.");
		}
		catch (NotBoundException e) {
			// TODO: handle exception
			LOG.error("NotBoundException.");
		}
		return GlobalMessage.ISS_ERROR;
	}
	
	/* (non-Javadoc)
	 * @see org.act.index.client.IClientSession#search(org.act.index.common.MailQuery, java.util.HashSet, org.act.index.common.Address)
	 */
	public List<MailDoc> search(MailQuery mailQuery, HashSet<Address> addrs, Address master) {
		List<MailDoc> mailDocs = new ArrayList<MailDoc>();
		if (addrs == null) {
			LOG.error("No available addrs.");
			return null;
		}
		int size = addrs.size();
		Address electAddr = master;
		if (size > 1) {
			for (Address addr : addrs) {
				if (!addr.isMaster()) {
					electAddr = addr;
					break;
				}
			}
		}
		try {
			String name = "core" + electAddr.getCoreId() + "@server" + electAddr.getServerId();
			String port = String.valueOf(electAddr.getAddr().getPort() + electAddr.getCoreId() + 1);
			String url = "rmi://" + electAddr.getAddr().getAddress().getHostAddress() + ":" + port + "/" + name;
			//System.out.println(url);
			ISolrService solrService = (ISolrService)Naming.lookup(url);
			//LOG.info("search on " + name);
			SolrQuery query = MailQuery.converse(userId, mailQuery);
			mailDocs = new ArrayList<MailDoc>();
			mailDocs = MailDoc.parse(solrService.search(userId, query));
			return mailDocs;
		}
		catch (MalformedURLException e) {
			// TODO: handle exception
			LOG.error("MalformedURLException.");
		}
		catch (RemoteException e) {
			// TODO: handle exception
			LOG.error("RemoteException.");
		}
		catch (NotBoundException e) {
			// TODO: handle exception
			LOG.error("NotBoundException.");
		}
		return mailDocs;
	}
}