/**
 * 
 */
package org.act.index.manager;

import java.util.List;
import java.util.Calendar;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 *
 */
public class Monitor extends Thread {
	private static final Logger LOG = Logger.getLogger(Monitor.class);
	private LayoutManager layout;
	
	public Monitor(LayoutManager layout) {
		this.layout = layout;
	}
	
	public void showServerList() {
		for (ServerCollect server : layout.getServerList()) {
			server.show();
		}
	}
	
	public void showCoreList() {
		for (CoreCollect core : layout.getCoreList()) {
			core.show();
		}
	}
	
	public void showUserList() {
		for (UserCollect user : layout.getUserList()) {
			user.show();
		}
	}

	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			showServerList();
			showCoreList();
			showUserList();
			try {
				sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
