/**
 * 
 */
package org.act.index.server;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.act.index.common.Configure;
import org.act.index.common.MonitorInfo;
import org.act.index.common.ServerInfo;
import org.act.index.common.sysparam.PublicParam;
import org.act.index.common.sysparam.ServerParam;
import org.act.index.manager.IndexManager;
import org.act.index.server.backup.BackupServer;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.apache.solr.core.CoreContainer.Initializer;
import org.apache.solr.handler.admin.CoreAdminHandler;
import org.xml.sax.SAXException;

/**
 * @author yeyue
 * 
 */
public class IndexServer {
	// private static EmbeddedSolrServer server;
	private static final Logger LOG = Logger.getLogger(IndexServer.class);
	
	private HashMap<Integer, SolrServer> solrServers;
	private MetaService metaService;
	private BackupServer backupServer;
	private CoreContainer coreContainer;
	private CoreAdminRequest coreAdmin;
	private ServerInfo serverInfo;
	private InetSocketAddress serverAddr;
	private String url;
	private SolrServer solrServer;

	public IndexServer() {
		Initializer initializer = new CoreContainer.Initializer();
		try {
			coreContainer = initializer.initialize();
			solrServers = new HashMap<Integer, SolrServer>();
			solrServer = new EmbeddedSolrServer(coreContainer, "meta");
			metaService = new MetaService(this);
			serverAddr = new InetSocketAddress(Inet4Address.getLocalHost()
					.getHostAddress(), PublicParam.CONFIG.getIntValue(
					Configure.CONFIG_SERVER, Configure.CONF_PORT, 0));
			backupServer = new BackupServer(serverAddr.getPort() - 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*serverInfo = new ServerInfo(serverAddr.getAddress().getHostAddress(),
				serverAddr.getPort());*/
		serverInfo = new ServerInfo(serverAddr.getAddress().getHostAddress(), serverAddr.getPort());
		LOG.debug("server ip:" + serverInfo.getServerIp());
		url = ServerParam.SOLR_HOME;
	}

	public IndexServer(int serverId) {
		Initializer initializer = new CoreContainer.Initializer();
		try {
			coreContainer = initializer.initialize();
			solrServers = new HashMap<Integer, SolrServer>();
			solrServer = new EmbeddedSolrServer(coreContainer, "meta");
			metaService = new MetaService(this);
			serverAddr = new InetSocketAddress(Inet4Address.getLocalHost()
					.getHostAddress(), PublicParam.CONFIG.getIntValue(
					Configure.CONFIG_SERVER, Configure.CONF_PORT, 0));
			backupServer = new BackupServer(serverAddr.getPort() - 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*serverInfo = new ServerInfo(serverId, serverAddr.getAddress()
				.getHostAddress(), serverAddr.getPort());*/
		serverInfo = new ServerInfo(serverId, serverAddr.getAddress().getHostAddress(), serverAddr.getPort());
		LOG.debug("server ip:" + serverInfo.getServerIp());
		url = ServerParam.SOLR_HOME;
	}

	public IndexServer(CoreContainer coreContainer) {
		this.coreContainer = coreContainer;
	}

	/**
	 * @return the solrServer
	 */
	public SolrServer getSolrServer(int coreId) {
		// CoreAdminHandler handler;

		return solrServers.get(coreId);
	}

	/**
	 * @param solrServer
	 *            the solrServer to set
	 */
	public void updateSolrServer(int coreId, SolrServer solrServer) {
		solrServers.put(coreId, solrServer);
	}

	public SolrServer addSolrServer(int coreId) {
		SolrServer server = new EmbeddedSolrServer(coreContainer, "" + coreId);
		solrServers.put(coreId, server);
		return server;
	}

	public boolean removeSolrServer(int coreId) {
		if (solrServers.remove(coreId) != null)
			return true;
		return false;
	}

	/**
	 * @return the serverAddr
	 */
	public InetSocketAddress getServerAddr() {
		return serverAddr;
	}

	/**
	 * @param serverAddr
	 *            the serverAddr to set
	 */
	public void setServerAddr(InetSocketAddress serverAddr) {
		this.serverAddr = serverAddr;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the coreAdmin
	 */
	public CoreAdminRequest getCoreAdmin() {
		return coreAdmin;
	}

	/**
	 * @param coreAdmin
	 *            the coreAdmin to set
	 */
	public void setCoreAdmin(CoreAdminRequest coreAdmin) {
		this.coreAdmin = coreAdmin;
		// coreAdmin.
	}

	/**
	 * @return the serverInfo
	 */
	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	/**
	 * @param serverInfo
	 *            the serverInfo to set
	 */
	public void setServerInfo(ServerInfo serverInfo) {
		this.serverInfo = serverInfo;
	}

	/**
	 * @return the coreContainer
	 */
	public CoreContainer getCoreContainer() {
		return coreContainer;
	}

	/**
	 * @param coreContainer
	 *            the coreContainer to set
	 */
	public void setCoreContainer(CoreContainer coreContainer) {
		this.coreContainer = coreContainer;
	}

	/**
	 * @return the metaService
	 */
	public MetaService getMetaService() {
		return metaService;
	}

	/**
	 * @param metaService
	 *            the metaService to set
	 */
	public void setMetaService(MetaService metaService) {
		this.metaService = metaService;
	}

	/**
	 * @return the backupServer
	 */
	public BackupServer getBackupServer() {
		return backupServer;
	}

	/**
	 * @param backupServer
	 *            the backupServer to set
	 */
	public void setBackupServer(BackupServer backupServer) {
		this.backupServer = backupServer;
	}

	/**
	 * @return the solrServer
	 */
	public SolrServer getSolrServer() {
		return solrServer;
	}

	/**
	 * @param solrServer
	 *            the solrServer to set
	 */
	public void setSolrServer(SolrServer solrServer) {
		this.solrServer = solrServer;
	}

	/**
	 * @return the solrServers
	 */
	public HashMap<Integer, SolrServer> getSolrServers() {
		return solrServers;
	}

	/**
	 * @param solrServers the solrServers to set
	 */
	public void setSolrServers(HashMap<Integer, SolrServer> solrServers) {
		this.solrServers = solrServers;
	}

	public void getStatus(MonitorInfo monitorInfo) {
		this.serverInfo.setCoreCount(this.coreContainer.getCores().size());
		this.serverInfo.setCurrentLoad(monitorInfo.getCpuRatio());
		this.serverInfo.setUseCapacity((int) (monitorInfo.getUsedDiskSize()));
		this.serverInfo
				.setTotalCapacity((int) (monitorInfo.getTotalDiskSize()));
		this.serverInfo.setLastUpdateTime(Calendar.getInstance());
	}
}
