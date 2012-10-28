/**
 * 
 */
package org.act.index.client;

import java.util.ArrayList;
import java.util.List;

import org.act.index.common.MailDoc;
import org.act.index.common.MailQuery;
import org.act.index.common.UserInfo;

/**
 * @author Administrator
 *
 */
public interface IIndexClient {
	
	public UserInfo register(String username, String password);
	
	public long login(String username, String password);
	
	public int add(MailDoc doc);
	
	public int add(List<MailDoc> docs);
	
	public int delete(int mailId);
	
	public int delete(MailQuery query);
	
	public MailDoc search(int mailId);
	
	public List<MailDoc> search(MailQuery query);
	
	public int update(MailDoc doc);
	
	public int update(ArrayList<MailDoc> docs);

}
