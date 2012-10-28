/**
 * 
 */
package org.act.index.common;

import org.apache.solr.client.solrj.SolrQuery;

/**
 * @author Administrator
 *
 */
public class MailQuery {
	private String queryString;
	
	public MailQuery() {
		
	}
	
	public MailQuery(String queryString) {
		this.queryString = queryString;
	}
	
	public static SolrQuery converse(MailQuery mailQuery) {
		SolrQuery query = new SolrQuery(mailQuery.queryString);

		return query;
	}
	
	public static SolrQuery converse(long userId, MailQuery mailQuery) {
		SolrQuery query = new SolrQuery("userId:" + userId + " AND " + mailQuery.queryString);

		return query;
	}
}
