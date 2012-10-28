/**
 * 
 */
package org.act.index.common;

import java.util.Collection;

/**
 * @author Administrator
 *
 */
public class DataPipe<Container extends Collection<Object>, Executor> extends Thread {
	
	private Container container;
	private Executor executor;
	private boolean speed;
	private boolean waitForFinish;
	private int overage;
	private boolean executing;
	private Thread thread;
	
	public DataPipe() {
		
	}
	
	public boolean push(Container data, int maxQueueLen, boolean blocking) {
		if (!isAlive())
			return false;
		container.add(data);
	    return true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public void start() {
		
	}
	
	void setThreadParameter(Executor executor) {
		this.executor = executor;
	}
}
