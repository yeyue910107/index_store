/**
 * 
 */
package org.act.index.common.sysparam;

import org.act.index.common.Configure;
import org.act.index.common.constdef.GlobalMessage;
import org.apache.solr.core.Config;

/**
 * @author Administrator
 *
 */
public class PublicParam {
	public static Configure CONFIG = new Configure();
	
	public static int loadManager(String file)
    {
		int ret = CONFIG.load(file);
		if (ret != GlobalMessage.ISS_SUCCESS)
			return ret;
		ManagerParam.MIN_REPLICATION = CONFIG.getIntValue(Configure.CONFIG_PUBLIC, Configure.CONF_MIN_REPLICATION, 0);
		ManagerParam.MAX_REPLICATION = CONFIG.getIntValue(Configure.CONFIG_PUBLIC, Configure.CONF_MAX_REPLICATION, 0);
		ManagerParam.HEART_INTERVAL = CONFIG.getIntValue(Configure.CONFIG_PUBLIC, Configure.CONF_HEART_INTERVAL, 0);
		ManagerParam.REPLICATE_CHECK_INTERVAL = CONFIG.getIntValue(Configure.CONFIG_MANAGER, Configure.CONF_REPL_CHECK_INTERVAL, 0);
		ManagerParam.SERVER_DEAD_TIME = CONFIG.getIntValue(Configure.CONFIG_MANAGER, Configure.CONF_SERVER_DEAD_TIME, 0);
		ManagerParam.MAX_WRITE_USER_COUNT = CONFIG.getIntValue(Configure.CONFIG_MANAGER, Configure.CONF_MAX_WRITE_USER_COUNT, 0);
		ManagerParam.MAX_CORE_COUNT_PER_SERVER = CONFIG.getIntValue(Configure.CONFIG_PUBLIC, Configure.CONF_CORE_MAX_COUNT_PER_SERVER, 0);
		return GlobalMessage.ISS_SUCCESS;
    }
	
	public static int loadServer(String file, int index) {
		int ret = CONFIG.load(file);
		if (ret != GlobalMessage.ISS_SUCCESS)
			return ret;
		ServerParam.SOLR_HOME = CONFIG.getStringValue(Configure.CONFIG_SERVER, Configure.CONF_SOLR_HOME, null);
		ServerParam.LOCAL_SERVER_PORT = CONFIG.getIntValue(Configure.CONFIG_SERVER, Configure.CONF_PORT, 0);
		ServerParam.MAX_CORE_COUNT_PER_SERVER = CONFIG.getIntValue(Configure.CONFIG_PUBLIC, Configure.CONF_CORE_MAX_COUNT_PER_SERVER, 0);
		ServerParam.HEART_INTERVAL = CONFIG.getIntValue(Configure.CONFIG_PUBLIC, Configure.CONF_HEART_INTERVAL, 0);
		return GlobalMessage.ISS_SUCCESS;
	}
}
