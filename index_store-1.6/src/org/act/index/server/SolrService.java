/**
 * 
 */
package org.act.index.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.act.index.client.ClientSession;
import org.act.index.common.Address;
import org.act.index.common.constdef.GlobalMessage;
import org.act.index.common.sysparam.PublicParam;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.request.DirectXmlRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.apache.solr.core.SolrConfig;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.ReplicationHandler;
import org.apache.solr.handler.RequestHandlerBase;

import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.update.UpdateHandler;
import org.apache.solr.handler.admin.*;

/**
 * @author yeyue
 * 
 */
public class SolrService extends UnicastRemoteObject implements ISolrService {
	private static final Logger LOG = Logger.getLogger(SolrService.class);

	private SolrServer server;

	public SolrService() throws RemoteException {

	}

	/**
	 * @param server
	 */
	public SolrService(SolrServer server) throws RemoteException {
		super();
		this.server = server;
	}

	/**
	 * @return the server
	 */
	public SolrServer getServer() {
		return server;
	}

	/**
	 * @param server
	 *            the server to set
	 */
	public void setServer(EmbeddedSolrServer server) {
		this.server = server;
	}

	
	
	public int createIndex(long userId, HashSet<Address> addrs,
			SolrInputDocument doc, boolean needRepl) throws RemoteException {
		try {
			if (doc == null)
				LOG.error("doc is null.");
			UpdateResponse rsp = server.add(doc);
			server.commit();
			LOG.info("userId:" + userId + " create time:" + rsp.getQTime() + "ms");
			if (needRepl)
				new ReplCreateThread(userId, addrs, null, doc).start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}

	public int createIndexForQTime(long userId, HashSet<Address> addrs,
			SolrInputDocument doc) throws RemoteException {
		try {
			if (doc == null)
				LOG.error("doc is null.");
			UpdateResponse rsp = server.add(doc);
			server.commit();
			// rsp.
			new ReplCreateThread(userId, addrs, null, doc).start();
			LOG.info("userId:" + userId + " create time:" + rsp.getQTime() + "ms");
			return rsp.getQTime();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}
	
	public int createIndex(long userId, HashSet<Address> addrs,
			List<SolrInputDocument> docs, boolean needRepl) throws RemoteException {
		try {
			server.add(docs);
			server.commit();
			if (needRepl)
				new ReplCreateThread(userId, addrs, docs, null).start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}

	public int delete(long userId, HashSet<Address> addrs, SolrQuery query, boolean needRepl)
			throws RemoteException {
		try {
			//UpdateResponse rsp = new UpdateResponse();
			//rsp = server.deleteByQuery(query.getQuery());
			server.deleteByQuery(query.getQuery());
			server.commit();
			if (needRepl)
				new ReplDeleteThread(userId, addrs, query).start();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return GlobalMessage.ISS_SUCCESS;
	}

	public int delete(long userId, HashSet<Address> addrs, int indexId, boolean needRepl)
			throws RemoteException {
		/*
		 * SolrConfig config; SolrCore core; CoreAdminAction action;
		 * //RequestHandlerBase base; CoreAdminRequest request;
		 * //CoreAdminResponse response =
		 * request.createCore(String.valueOf(indexID), instanceDir, server,
		 * configFile, schemaFile)
		 * 
		 * DirectXmlRequest request2; UpdateHandler handler; ReplicationHandler
		 * replicationHandler; RequestHandlerBase base; SolrServer server;
		 */
		//UpdateResponse rsp = new UpdateResponse();
		try {
			//rsp = server.deleteById(String.valueOf(indexId));
			server.deleteById(String.valueOf(indexId));
			server.commit();
		} catch (SolrServerException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return GlobalMessage.ISS_SUCCESS;
	}

	/*
	 * public SolrDocumentList search(int userID, SolrQuery query) throws
	 * IndexServiceException { QueryResponse rsp = new QueryResponse(); try {
	 * rsp = server.query(query); } catch (SolrServerException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return
	 * rsp.getResults(); }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.act.index.server.ISolrService#search(int,
	 * org.apache.solr.client.solrj.SolrQuery)
	 */
	@Override
	public SolrDocumentList search(long userID, SolrQuery query)
			throws RemoteException {
		// TODO Auto-generated method stub
		QueryResponse rsp = new QueryResponse();
		try {
			rsp = server.query(query);
			LOG.info("userId:" + userID + " create time:" + rsp.getQTime() + "ms");
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rsp.getResults();
	}

	public int searchForQTime(long userID, SolrQuery query)
			throws RemoteException {
		// TODO Auto-generated method stub
		QueryResponse rsp = new QueryResponse();
		try {
			rsp = server.query(query);
			//LOG.info("userId:" + userId + " create time:" + rsp.getQTime() + "ms");
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rsp.getQTime();
	}

	public UpdateResponse commit(boolean waitFlush, boolean waitSearcher)
			throws RemoteException {
		UpdateResponse rsp = new UpdateResponse();
		try {
			rsp = server.commit(waitFlush, waitSearcher);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rsp;
	}

	public UpdateResponse commit() throws RemoteException {
		UpdateResponse rsp = new UpdateResponse();
		try {
			rsp = server.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rsp;
	}

	public UpdateResponse optimize(boolean waitFlush, boolean waitSearcher)
			throws RemoteException {
		UpdateResponse rsp = new UpdateResponse();
		try {
			rsp = server.optimize(waitFlush, waitSearcher);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return rsp;
	}

	public UpdateResponse optimize() throws RemoteException {
		UpdateResponse rsp = new UpdateResponse();
		try {
			rsp = server.optimize();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return rsp;
	}
	
	class ReplCreateThread extends Thread {
		private long userId;
		private HashSet<Address> addrs;
		private List<SolrInputDocument> docs;
		private SolrInputDocument doc;

		public ReplCreateThread(long userId, HashSet<Address> addrs,
				List<SolrInputDocument> docs, SolrInputDocument doc) {
			this.userId = userId;
			this.addrs = addrs;
			this.docs = docs;
			this.doc = doc;
		}

		public void run() {
			LOG.debug("begin replicate.");
			for (Address addr : addrs) {
				LOG.debug("core:" + addr.getCoreId() + "@server:" + addr.getServerId());
				if (!addr.isMaster()) {
					String name = "core" + addr.getCoreId() + "@server"
							+ addr.getServerId();
					// String port = String.valueOf(master.getAddr().getPort() +
					// master.getCoreId());
					String port = String.valueOf(addr.getAddr().getPort()
							+ addr.getCoreId() + 1);
					String url = "rmi://" + addr.getAddr().getAddress().getHostAddress() + ":"
							+ port + "/" + name;
					LOG.debug(url);
					try {
						ISolrService solrService = (ISolrService) Naming
								.lookup(url);
						if (doc != null)
							solrService.createIndex(userId, addrs, doc, false);
						else if (docs != null)
							solrService.createIndex(userId, addrs, docs, false);
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
				}
			}
		}
	}
	
	class ReplDeleteThread extends Thread {
		private long userId;
		private HashSet<Address> addrs;
		private SolrQuery query;

		public ReplDeleteThread(long userId, HashSet<Address> addrs, SolrQuery query) {
			this.userId = userId;
			this.addrs = addrs;
			this.query = query;
		}

		public void run() {
			LOG.debug("begin delete replicate.");
			for (Address addr : addrs) {
				LOG.debug("core:" + addr.getCoreId() + "@server:" + addr.getServerId());
				if (!addr.isMaster()) {
					String name = "core" + addr.getCoreId() + "@server"
							+ addr.getServerId();
					// String port = String.valueOf(master.getAddr().getPort() +
					// master.getCoreId());
					String port = String.valueOf(addr.getAddr().getPort()
							 + addr.getCoreId() + 1);
					String url = "rmi://" + addr.getAddr().getAddress().getHostAddress() + ":"
							+ port + "/" + name;
					System.out.println(url);
					try {
						ISolrService solrService = (ISolrService) Naming
								.lookup(url);
						solrService.delete(userId, addrs, query, false);
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
				}
			}
		}
	}
}


