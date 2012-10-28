/**
 * 
 */
package org.act.index.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.act.index.common.constdef.ErrorMessage;
import org.act.index.common.constdef.GlobalMessage;
import org.apache.solr.common.util.Hash;

/**
 * @author Administrator
 *
 */
public class Configure {
	public static final int CONFIG_PUBLIC = 0;
	public static final int CONFIG_MANAGER = 1;
	public static final int CONFIG_SERVER = 2;
	public static final int CONFIG_CLIENT = 3;
	public static final int CONFIG_ADMINSERVER = 4;
	public static final int CONFIG_MYSQLSYNC = 5;
	//public 
	public static final String CONF_SN_PUBLIC = "public";
	public static final String CONF_SN_MANAGER = "manager";
	public static final String CONF_SN_SERVER = "server";
	public static final String CONF_SN_CLIENT = "client";
	public static final String CONF_SN_ADMINSERVER = "adminserver";

	public static final String CONF_CLUSTER_ID = "cluster_id";
	public static final String CONF_LOCK_FILE = "lock_file";
	public static final String CONF_LOG_FILE = "log_file";
	public static final String CONF_LOG_SIZE = "log_size";
	public static final String CONF_LOG_NUM = "log_num";
	public static final String CONF_LOG_LEVEL = "log_level";
	public static final String CONF_WORK_DIR = "work_dir";
	public static final String CONF_SOLR_HOME = "solr_home";

	public static final String CONF_PORT = "port";
	public static final String CONF_THREAD_COUNT = "thread_count";
	public static final String CONF_IP_ADDR = "ip_addr";
	public static final String CONF_DEV_NAME = "dev_name";
	public static final String CONF_CORE_MAX_SIZE = "core_max_size";
	public static final String CONF_CORE_MAX_COUNT_PER_SERVER = "core_max_count_per_server";
	public static final String CONF_CORE_USE_RATIO = "core_max_use_ratio";
	public static final String CONF_HEART_INTERVAL = "heart_interval";
	public static final String CONF_HEART_MAX_QUEUE_SIZE = "heart_max_queue_size";
	public static final String CONF_HEART_THREAD_COUNT	= "heart_thread_count";
	public static final String CONF_MAX_REPLICATION  = "max_replication";
	public static final String CONF_MIN_REPLICATION = "min_replication";
	public static final String CONF_USE_CAPACITY_RATIO = "use_capacity_ratio";

	  //adminserver
	public static final String CONF_MANAGER_SCRIPT = "manager_script";
	public static final String CONF_SERVER_SCRIPT = "server_script";
	public static final String CONF_MANAGER_FKILL_WAITTIME = "manager_fkill_waittime";
	public static final String CONF_SERVER_FKILL_WAITTIME = "server_fkill_waittime";
	public static final String CONF_CHECK_COUNT = "check_count";
	public static final String CONF_SERVER_INDEX_LIST = "server_index_list";

	  //manager
	public static final String CONF_IP_ADDR_LIST = "ip_addr_list";
	public static final String CONF_GROUP_MASK = "group_mask";
	public static final String CONF_OWNER_CHECK_INTERVAL = "owner_check_interval";
	//public static final String CONF_BLOCK_CHUNK_NUM = "block_chunk_num";
	public static final String CONF_TASK_MAX_QUEUE_SIZE = "task_max_queue_size";
	public static final String CONF_TASK_PRECENT_SEC_SIZE = "task_percent_sec_size";
	public static final String CONF_MAX_WRITE_USER_COUNT = "max_write_user_count";
	public static final String CONF_ADD_PRIMARY_CORE_COUNT = "add_primary_core_count";
	public static final String CONF_BALANCE_CHECK_INTERVAL = "balance_check_interval";
	public static final String CONF_REDUNDANT_CHECK_INTERVAL = "redundant_check_interval";
	public static final String CONF_SAFE_MODE_TIME = "safe_mode_time";
	public static final String CONF_KEEP_ALIVE_TIME = "keep_alive_time";

	public static final String CONF_COMPACT_DELETE_RATIO="compact_delete_ratio";
	public static final String CONF_COMPACT_PRESERVE_TIME="compact_preserve_time";
	public static final String CONF_COMPACT_HOUR_RANGE ="compact_hour_range";
	public static final String CONF_COMPACT_CHECK_INTERVAL ="compact_check_interval";
	public static final String CONF_COMPACT_MAX_LOAD ="compact_max_load";

	public static final String CONF_REPL_CHECK_INTERVAL = "repl_check_interval";
	public static final String CONF_REPL_MAX_COUNT = "repl_max_count_per_server";
	public static final String CONF_REPL_MAX_TIME = "repl_max_time";
	public static final String CONF_REPL_WAIT_TIME = "repl_wait_time";
	public static final String CONF_REPLICATE_THREADCOUNT = "replicate_threadcount";

	public static final String CONF_OPLOG_SYSNC_MAX_SLOTS_NUM = "oplog_sync_max_slots_num";
	public static final String CONF_OPLOGSYNC_THREAD_NUM = "oplog_sync_thread_num";

	public static final String CONF_MAX_WAIT_WRITE_LEASE = "max_wait_write_lease";
	public static final String CONF_CLEANUP_LEASE_HOUR_RANGE = "cleanup_lease_hour_range";
	public static final String CONF_CLEANUP_LEASE_THRESHOLD = "cleanup_lease_threshold";
	public static final String CONF_CLEANUP_LEASE_COUNT = "cleanup_lease_count";

	  //dataserver
	public static final String CONF_CHECK_INTERVAL = "check_interval";
	public static final String CONF_DATA_THREAD_COUNT = "data_thread_count";
	public static final String CONF_EXPIRE_COMPACTBLOCK_TIME = "expire_compactblock_time";
	public static final String CONF_SERVER_DEAD_TIME = "server_dead_time";
	public static final String CONF_SERVER_THREAD_COUNT = "server_thread_count";
	public static final String CONF_EXPIRE_DATAFILE_TIME = "expire_datafile_time";
	public static final String CONF_EXPIRE_CLONEDBLOCK_TIME = "expire_clonedblock_time";
	public static final String CONF_VISIT_STAT_INTERVAL ="dump_visit_stat_interval";
	public static final String CONF_IO_WARN_TIME = "max_io_warning_time";
	public static final String CONF_REMOVE_PRESERVE_TIME = "remove_preserve_time";
	public static final String CONF_SLAVE_NSIP = "slave_nsip";
	public static final String CONF_SLAVE_NSPORT = "slave_nsport";
	public static final String CONF_SYNC_RETRY_COUNT = "sync_retry_count";
	public static final String CONF_MOUNT_POINT_NAME = "mount_name";             //mount point name
	public static final String CONF_MOUNT_MAX_USESIZE = "mount_maxsize";
	public static final String CONF_BASE_FS_TYPE = "base_filesystem_type";
	public static final String CONF_AVG_SEGMENT_SIZE = "avg_file_size";
	public static final String CONF_SUPERBLOCK_START = "superblock_reserve";    //"0"
	public static final String CONF_MAINBLOCK_SIZE = "mainblock_size";        //"67108864"
	public static final String CONF_EXTBLOCK_SIZE = "extblock_size";          //"33554432"
	public static final String CONF_BLOCKTYPE_RATIO = "block_ratio";           //"2"
	public static final String CONF_BLOCK_VERSION = "fs_version";            //"1"
	public static final String CONF_HASH_SLOT_RATIO = "hash_slot_ratio";
	public static final String CONF_WRITE_SYNC_FLAG = "write_sync_flag";
	public static final String CONF_DATA_FILE_NUMS = "max_data_file_nums";
	public static final String CONF_MAX_CRCERROR_NUMS = "max_crc_error_nums";
	public static final String CONF_MAX_EIOERROR_NUMS = "max_eio_error_nums_";
	public static final String CONF_READ_CACHE_SIZE = "read_cache_size";
	public static final String CONF_BACKUP_PATH = "backup_path";
	public static final String CONF_BACKUP_TYPE = "backup_type";
	public static final String CONF_EXPIRE_CHECKBLOCK_TIME = "expire_checkblock_time";
	public static final String CONF_MAX_CPU_USAGE = "max_cpu_usage";
	
	private HashMap<String, String> publicConfigMap;
	private HashMap<String, String> managerConfigMap;
	private HashMap<String, String> serverConfigMap;
	private HashMap<String, String> clientConfigMap;
	private boolean isLoad;
	private String configFileName;
	
	public Configure() {
		publicConfigMap = new HashMap<String, String>();
		publicConfigMap.put(CONF_CORE_MAX_SIZE, "67108864");
		publicConfigMap.put(CONF_USE_CAPACITY_RATIO, "80");
		publicConfigMap.put(CONF_MIN_REPLICATION, "3");
		publicConfigMap.put(CONF_MAX_REPLICATION, "5");
		
		managerConfigMap = new HashMap<String, String>();
		managerConfigMap.put(CONF_PORT, "3100");
		managerConfigMap.put(CONF_THREAD_COUNT, "10");
		managerConfigMap.put(CONF_LOG_FILE, "manager.log");
		
		serverConfigMap = new HashMap<String, String>();
		clientConfigMap = new HashMap<String, String>();
		isLoad = false;
		configFileName = "";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String result = CONF_SN_PUBLIC + "\n";
		for (String key : publicConfigMap.keySet()) {
			result += "    " + key + " = " + publicConfigMap.get(key) + "\n";
		}
		result += "\n" + CONF_SN_MANAGER + "\n";
		for (String key : managerConfigMap.keySet()) {
			result += "    " + key + " = " + managerConfigMap.get(key) + "\n";
		}
		result += "\n" + CONF_SN_SERVER + "\n";
		for (String key : serverConfigMap.keySet()) {
			result += "    " + key + " = " + serverConfigMap.get(key) + "\n";
		}
		result += "\n" + CONF_SN_CLIENT + "\n";
		for (String key : clientConfigMap.keySet()) {
			result += "    " + key + " = " + clientConfigMap.get(key) + "\n";
		}
		result += "\n";
		return result;
	}
	
	public int parseValue(char[] str, StringBuffer key, StringBuffer val) {
		int i, j, name, value;
		if (str == null || str.length == 0) {
			return -1;
		}
		i = 0;
		while (str[i] == ' ' || str[i] == '\t' ||
				str[i] == '\r' || str[i] == '\n')
			i++;
		j = str.length;
		while (str[j - 1] == ' ' || str[j - 1] == '\t' ||
				str[j - 1] == '\r' || str[j - 1] == '\n') {
			j--;
			str[j] = '\0';
		}
		if (str[i] == '#' || str[i] == '\0')
			return -1;
		j = String.valueOf(str).indexOf('=');
		if (j == -1)
			return -2;
		String[] key_value = String.valueOf(str).split("=");
		
		/*name = i;
		value = j;
		while (str[j - 1] == ' ') {
			j--;
			str[j] = '\0';
		}
		while (str[value] == ' ')
			value++;
		
		i = String.valueOf(str).substring(value).indexOf('#');
		while (str[i - 1] <= ' ') {
			i--;
			str[i] = '\0';
		}
		if (name == str.length || str[name] == '\0')
			return -2;*/
		
		key.append(new String(key_value[0].trim()));
		val.append(new String(key_value[1].trim()));
		
		return GlobalMessage.ISS_SUCCESS;
	}
	
	public int load(String fileName) {
		String data;
		StringBuffer key, value;
		int ret, line = 0;
		
		FileReader fr;
		try {
			fr = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("config file '" + fileName + "' not exists.");
			isLoad = true;
			return ErrorMessage.EXIT_OPEN_FILE_ERROR;
		}
		BufferedReader br = new BufferedReader(fr);
		
		clearConfigMap();
		HashMap<String, String> map = publicConfigMap;
		do {
			line++;
			try {
				data = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("config file '" + fileName + "' read error.");
				return ErrorMessage.EXIT_FILE_OP_ERROR;
			}
			if (data == null)
				break;
			else if (data.compareTo(CONF_SN_PUBLIC) == 0) {
				map = publicConfigMap;
				continue;
			}
			else if (data.compareTo(CONF_SN_MANAGER) == 0) {
				map = managerConfigMap;
				continue;
			}
			else if (data.compareTo(CONF_SN_SERVER) == 0) {
				map = serverConfigMap;
				continue;
			}
			else if (data.compareTo(CONF_SN_CLIENT) == 0) {
				map = clientConfigMap;
				continue;
			}
			key = new StringBuffer();
			value = new StringBuffer();
			
			ret = parseValue(data.toCharArray(), key, value);
			if (ret == 2) {
				System.out.println("parse error: Line: "+ line + ", " + data);
				return ErrorMessage.EXIT_GENERAL_ERROR;
			}
			if (ret < 0) {
				continue;
			}
			if (map.containsKey(key))
				map.remove(key);
			map.put(key.toString(), value.toString());
		}
		while (data != null);
		
		isLoad = true;
		configFileName = fileName;
		return GlobalMessage.ISS_SUCCESS;
	}
 
    public int checkLoad() {
    	if (isLoad) {
    		String env = System.getenv("ISS_CONF_FILE");
    		if (env != null)
    			load(env);
    		isLoad = true;
    	}
    	return GlobalMessage.ISS_SUCCESS;
    }
    
    public void clearConfigMap() {
    	publicConfigMap.clear();
        managerConfigMap.clear();
        serverConfigMap.clear();
        clientConfigMap.clear();
    }
    
    public String getStringValue(int section, String key, String temp) {
    	HashMap<String, String> map;
    	switch (section) {
    		case CONFIG_PUBLIC:
    			map = publicConfigMap;
    			break;
    		case CONFIG_MANAGER:
    			map = managerConfigMap;
    			break;
    		case CONFIG_SERVER:
    			map = serverConfigMap;
    			break;
    		case CONFIG_CLIENT:
    			map = clientConfigMap;
    		default:
    			return temp;
		}
    	if (map.containsKey(key)) {
    		return map.get(key);
    	}
    	else {
			return temp;
		}
    }
    
    public int getIntValue(int section, String key, int temp) {
    	String str = getStringValue(section, key, null);
    	if (str == null || str.charAt(0) == '\0')
    		return temp;
    	int i = 0, len = str.length();
    	while (i < len) {
    		if (str.charAt(i) < '0' || str.charAt(i) > '9')
    			return temp;
    		i++;
    	}
    	return Integer.parseInt(str);
    }
    
    public HashMap<String, String> getDefineKey() {
    	HashMap<String, String> map = new HashMap<String, String>();
    	for (String key : publicConfigMap.keySet()) {
			map.put(key, "1");
		}
    	for (String key : managerConfigMap.keySet()) {
			map.put(key, "1");
		}
    	for (String key : serverConfigMap.keySet()) {
			map.put(key, "1");
		}
    	for (String key : clientConfigMap.keySet()) {
			map.put(key, "1");
		}
        return map;
    }
}
