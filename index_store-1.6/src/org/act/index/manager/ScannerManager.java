/**
 * 
 */
package org.act.index.manager;

import java.util.HashMap;
import java.util.List;

import org.act.index.common.constdef.GlobalMessage;

/**
 * @author Administrator
 *
 */
public class ScannerManager implements MetaScanner{
	private MetaManager metaManager;
	private HashMap<Integer, Scanner> scanners;
	boolean destroy;
	
	/**
	 * 
	 */
	public ScannerManager() {
		super();
	}

	public int run() {
		return GlobalMessage.ISS_SUCCESS;
	}
	
	@Override
	public int process(CoreCollect coreCollect) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean addScanner(int id, Scanner scanner) {
		return true;
	}
}

class Scanner {
	private Launcher launcher;
	private List<Integer> result;
	private int limits;
	private boolean isCheck;
}