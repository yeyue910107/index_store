/**
 * 
 */
package org.act.index.manager;

import java.util.Timer;
import java.util.TimerTask;

import org.act.index.common.constdef.GlobalMessage;
import javax.jms.Message;
import java.lang.reflect.*;
/**
 * @author yeyue
 *
 */
public class HeartManager implements Runnable{
	private MetaManager metaManager;
	private int maxQueueSize;
	private ReplicateLauncher replicateLauncher;
	/**
	 * 
	 */
	public HeartManager() {
		// TODO Auto-generated constructor stub

	}
	
	public int init(int threadCount, int maxQueueSize) {
		this.maxQueueSize = maxQueueSize;
		this.run();
		return GlobalMessage.ISS_SUCCESS;
	}

	public int execute() {
		return GlobalMessage.ISS_SUCCESS;
	}
	
	public int push() {
		return GlobalMessage.ISS_SUCCESS;
	}

	public int joinServer() {
		
		return GlobalMessage.ISS_SUCCESS;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}

class CheckOwnerIsMasterTimerTask extends TimerTask {
	private MetaManager metaManager;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}

class MasterHeartTimeTask extends TimerTask {
	private MetaManager metaManager;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}

class SlaveHeartTimeTask extends TimerTask {
	private MetaManager metaManager;
	private Timer timer;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
}