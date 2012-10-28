/**
 * 
 */
package org.act.index.test;


import java.io.File;

import org.act.index.server.MetaService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.CoreContainer.Initializer;

/**
 * @author Administrator
 *
 */
public class SolrTest {

	private static Initializer initializer = null;
	private static CoreContainer coreContainer = null;
	private static EmbeddedSolrServer server = null;
	/*static {
		try {
			System.setProperty("solr.solr.home", "D:\\Program Files\\Apache Software Foundation\\solr-tomcat\\solr\\multicore");
			initializer = new CoreContainer.Initializer();
			coreContainer = initializer.initialize();
			server = new EmbeddedSolrServer(coreContainer, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	public void query() throws Exception {
		try {
			String url = "D:\\Program Files\\Apache Software Foundation\\solr-tomcat\\solr\\multicore";
			System.setProperty("solr.solr.home", url);
			File f = new File(url, "solr.xml");
			coreContainer = new CoreContainer();
			//initializer = new CoreContainer.Initializer();
			//coreContainer = initializer.initialize();
			coreContainer.load(url, f);
			coreContainer.setPersistent(true);
			System.out.println(coreContainer.getCores().size());

			server = new EmbeddedSolrServer(coreContainer, "core0");
			SolrQuery q = new SolrQuery();
			q.setQuery("*:*");
			QueryResponse rsp = server.query(q);
			
			System.out.println(server.query(q).getResults().getNumFound());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			coreContainer.shutdown();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SolrTest test = new SolrTest();
		try {
			test.query();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

