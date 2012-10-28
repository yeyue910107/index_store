/**
 * 
 */
package org.act.index.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.act.index.common.Address;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;


/**
 * @author Administrator
 *
 */
public interface ISolrService extends Remote {
	
	public SolrServer getServer() throws RemoteException;;
	
	public int createIndex(long userId, HashSet<Address> addrs, SolrInputDocument doc, boolean needRepl) throws RemoteException;
	
	public int createIndex(long userId, HashSet<Address> addrs, List<SolrInputDocument> docs, boolean needRepl) throws RemoteException;

	public int delete(long userId, HashSet<Address> addrs, SolrQuery query, boolean needRepl) throws RemoteException;

	public int delete(long userId, HashSet<Address> addrs, int indexId, boolean needRepl) throws RemoteException;

	public SolrDocumentList search(long userId, SolrQuery query) throws RemoteException;

	public UpdateResponse commit(boolean waitFlush, boolean waitSearcher) throws RemoteException;

	public UpdateResponse commit() throws RemoteException;
	
	public int createIndexForQTime(long userId, HashSet<Address> addrs, SolrInputDocument doc) throws RemoteException;
	
	public int searchForQTime(long userID, SolrQuery query) throws RemoteException;
	
	//public UpdateResponse optimize(boolean waitFlush, boolean waitSearcher) throws IndexServiceException;

	//public UpdateResponse optimize() throws IndexServiceException;
}
