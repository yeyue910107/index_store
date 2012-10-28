/**
 * 
 */
package org.act.index.client;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;


import org.act.index.common.Address;
import org.act.index.common.MailDoc;
import org.act.index.common.MailQuery;
import org.act.index.common.UserInfo;
import org.act.index.common.constdef.GlobalMessage;
import org.act.index.hibernate.user.User;
import org.act.index.security.MD5;
import org.apache.log4j.Logger;

/**
 * @author yeyue
 * 
 */
public class IndexClient implements IIndexClient{
	private static final Logger LOG = Logger.getLogger(IndexClient.class);
	private long userId;
	private String userName;
	private ClientSession session;
	public IndexClient() {
		session = new ClientSession();
		this.userId = -1;
	}
	
	/**
	 * @param userId
	 */
	public IndexClient(long userId) {
		super();
		this.userId = userId;
	}

	public IndexClient(String manager) {
		session = new ClientSession(manager);
		this.userId = -1;
	}

	public IndexClient(String manager, long userId) {
		session = new ClientSession(manager, userId);
		this.userId = userId;
	}
	
	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
		session.setUserId(userId);
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/* (non-Javadoc)
	 * @see org.act.index.client.IIndexClient#register()
	 */
	@Override
	public UserInfo register(String username, String passward) {
		// TODO Auto-generated method stub
		
		return session.createUserInfo(username, passward);
	}

	/* (non-Javadoc)
	 * @see org.act.index.client.IIndexClient#login(java.lang.String, java.lang.String)
	 */
	@Override
	public long login(String username, String password) {
		User user = session.getUser(username);
		if (user == null) {
			System.out.println("user " + username + " not exists.");
			return -1;
		}
		//System.out.println(MD5.md5(password) + "\t" + user.getPassword());
		if (MD5.md5(password).compareTo(user.getPassword()) != 0) {
			System.out.println("password is incorrect.");
			return -1;
		}
		// TODO Auto-generated method stub
		return user.getUserId();
	}

	/* (non-Javadoc)
	 * @see org.act.index.client.IIndexClient#add(org.act.index.common.MailDoc)
	 */
	@Override
	public int add(MailDoc doc) {
		// TODO Auto-generated method stub
		HashSet<Address> addrs = new HashSet<Address>();
		Address master = new Address();
		addrs = session.getCoreInfo(userId, addrs, master);
		session.createIndex(userId, doc, addrs, master);
		return GlobalMessage.ISS_SUCCESS;
	}

	/* (non-Javadoc)
	 * @see org.act.index.client.IIndexClient#add(java.util.List)
	 */
	@Override
	public int add(List<MailDoc> docs) {
		// TODO Auto-generated method stub
		HashSet<Address> addrs = new HashSet<Address>();
		Address master = new Address();
		addrs = session.getCoreInfo(userId, addrs, master);
		return session.createIndex(userId, docs, addrs, master);
	}

	
	/* (non-Javadoc)
	 * @see org.act.index.client.IIndexClient#delete(int)
	 */
	@Override
	public int delete(int mailId) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.act.index.client.IIndexClient#delete(org.act.index.common.MailQuery)
	 */
	@Override
	public int delete(MailQuery query) {
		// TODO Auto-generated method stub
		HashSet<Address> addrs = new HashSet<Address>();
		Address master = new Address();
		
		addrs = session.getCoreInfo(userId, addrs, master);
		return session.deleteIndex(userId, query, addrs, master);
	}

	/* (non-Javadoc)
	 * @see org.act.index.client.IIndexClient#search(org.act.index.common.MailQuery)
	 */
	@Override
	public List<MailDoc> search(MailQuery query) {
		// TODO Auto-generated method stub
		HashSet<Address> addrs = new HashSet<Address>();
		Address master = new Address();
		List<MailDoc> docs = new ArrayList<MailDoc>();
		long before = Calendar.getInstance().getTimeInMillis();
		addrs = session.getCoreInfo(userId, addrs, master);
		docs = session.search(query, addrs, master);
		//LOG.info("userId:" + userId +  " search time:" + (Calendar.getInstance().getTimeInMillis() - before) + "ms.");
		LOG.info("" + (Calendar.getInstance().getTimeInMillis() - before));
		
		return docs;
	}

	/* (non-Javadoc)
	 * @see org.act.index.client.IIndexClient#search(int)
	 */
	@Override
	public MailDoc search(int mailId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.act.index.client.IIndexClient#update(java.util.ArrayList)
	 */
	@Override
	public int update(ArrayList<MailDoc> docs) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.act.index.client.IIndexClient#update(org.act.index.common.MailDoc)
	 */
	@Override
	public int update(MailDoc doc) {
		// TODO Auto-generated method stub
		return 0;
	}

}
