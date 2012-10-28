/**
 * 
 */
package org.act.index.manager;

import java.util.List;

/**
 * @author Administrator
 *
 */
public interface Launcher {
	
	boolean check(CoreCollect coreCollect);
	
	int buildPlan(List<?> cores);
}
