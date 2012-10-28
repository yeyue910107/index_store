/**
 * 
 */
package org.act.index.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.client.solrj.beans.Field;

/**
 * @author yeyue
 *
 */
public class MailDoc implements Serializable {
	@Field("mail id")
	private int mailId;
	
	@Field("user id")
	private int userId;
	
	private String from;
	
	private String to;
	
	private String cc;
	
	private String bcc;
	
	private String subject;
	
	private String body;
	
	private Calendar datetime;
	
	public MailDoc() {
		
	}

	/**
	 * @param mailId
	 */
	public MailDoc(int mailId) {
		super();
		this.mailId = mailId;
	}

	public MailDoc(String fileName) {
		
	}
	
	/**
	 * @return the mailId
	 */
	public int getMailId() {
		return mailId;
	}

	/**
	 * @param mailId the mailId to set
	 */
	public void setMailId(int mailId) {
		this.mailId = mailId;
	}
	
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the cc
	 */
	public String getCc() {
		return cc;
	}

	/**
	 * @param cc the cc to set
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}

	/**
	 * @return the bcc
	 */
	public String getBcc() {
		return bcc;
	}

	/**
	 * @param bcc the bcc to set
	 */
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the datetime
	 */
	public Calendar getDatetime() {
		return datetime;
	}

	/**
	 * @param datetime the datetime to set
	 */
	public void setDatetime(Calendar datetime) {
		this.datetime = datetime;
	}

	public static SolrInputDocument converse(MailDoc mailDoc) {
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("mailId", mailDoc.getMailId());
		doc.addField("userId", mailDoc.getUserId());
		doc.addField("from", mailDoc.getFrom());
		doc.addField("to", mailDoc.getTo());
		doc.addField("cc", mailDoc.getCc());
		doc.addField("bcc", mailDoc.getBcc());
		doc.addField("subject", mailDoc.getSubject());
		doc.addField("body", mailDoc.getBody());
		doc.addField("datetime", mailDoc.getDatetime());
		
		return doc;
	}
	
	public static List<SolrInputDocument> converse(List<MailDoc> mailDocs) {
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		
		for (MailDoc mailDoc : mailDocs) {
			docs.add(MailDoc.converse(mailDoc));
		}
		return docs;
	}
	
	public static List<MailDoc> parse(SolrDocumentList docs) {
		List<MailDoc> mailDocs = new ArrayList<MailDoc>();
		if (docs != null) {
			for (SolrDocument doc : docs) {
				MailDoc mailDoc = new MailDoc(Integer.parseInt((String)doc.getFieldValue("mailId")));
				mailDoc.setUserId(Integer.parseInt((String)doc.getFieldValue("userId")));
				mailDoc.setFrom((String)doc.getFieldValue("from"));
				mailDoc.setTo((String)doc.getFieldValue("to"));
				mailDoc.setCc((String)doc.getFieldValue("cc"));
				mailDoc.setBcc((String)doc.getFieldValue("bcc"));
				mailDoc.setSubject((String)doc.getFieldValue("subject"));
				mailDoc.setBody((String)doc.getFieldValue("body"));
				mailDocs.add(mailDoc);
			}
		}
		return mailDocs;
	}
	
	public void print() {
		System.out.println("mailId:" + mailId);
		System.out.println("userId:" + userId);
		System.out.println("from:" + from);
		System.out.println("to:" + to);
		System.out.println("cc:" + cc);
		System.out.println("bcc:" + bcc);
		System.out.println("subject:" + subject);
		System.out.println("body:" + body);
		System.out.println("*************");
	}
}
