/**
 * 
 */
package org.act.index.common.constdef;

/**
 * @author Administrator
 *
 */
public class ErrorMessage {
	public final static int EXIT_GENERAL_ERROR = -1000;
    public final static int EXIT_CONFIG_ERROR = -1001;
    public final static int EXIT_UNKNOWN_MSGTYPE = -1002;

    public final static int EXIT_FILE_OP_ERROR = -2000;
    public final static int EXIT_OPEN_FILE_ERROR = -2001;
    public final static int EXIT_INVALID_FD = -2002;
    public final static int EXIT_RECORD_SIZE_ERROR = -2003;
    public final static int EXIT_READ_FILE_ERROR = -2004;
    public final static int EXIT_WRITE_FILE_ERROR = -2005;
    public final static int EXIT_FILESYSTEM_ERROR = -2006;
    public final static int EXIT_FILE_FORMAT_ERROR = -2007;
    public final static int EXIT_SLOTS_OFFSET_SIZE_ERROR = -2008;

    public final static int EXIT_NETWORK_ERROR = -3000;
    public final static int EXIT_IOCTL_ERROR = -3001;
    public final static int EXIT_CONNECT_ERROR = -3002;
    public final static int EXIT_SENDMSG_ERROR = -3003;
    public final static int EXIT_RECVMSG_ERROR = -3004;

    public final static int EXIT_TFS_ERROR = -5000;
    public final static int EXIT_NO_CORE = -5001;
    public final static int EXIT_NO_SERVER = -5002;
    public final static int EXIT_CORE_NOT_FOUND = -5003;
    public final static int EXIT_SERVER_NOT_FOUND = -5004;
    public final static int EXIT_CANNOT_GET_LEASE = -5005;// lease not found
    public final static int EXIT_COMMIT_ERROR = -5006;
    public final static int EXIT_LEASE_EXPIRED = -5007;
    public final static int EXIT_BINLOG_ERROR = -5008;
    public final static int EXIT_NO_REPLICATE = -5009;
    public final static int EXIT_CORE_BUSY = -5010;

    public final static int MYSQL_ERROR_BASE = -6000;
    public final static int MYSQL_CONNECT_ERROR = -6001;

    public final static int EXIT_WRITE_OFFSET_ERROR = -8001; // write offset error
    public final static int EXIT_READ_OFFSET_ERROR = -8002; // read offset error
    public final static int EXIT_COREID_ZERO_ERROR = -8003; // CORE id is zero, fatal error
    public final static int EXIT_CORE_EXHAUST_ERROR = -8004; // CORE is used up, fatal error
    public final static int EXIT_PHYSICALCORE_NUM_ERROR = -8005; // need extend too much physcial CORE when extend CORE
    public final static int EXIT_NO_LOGICCORE_ERROR = -8006; // can't find logic CORE
    public final static int EXIT_POINTER_NULL = -8007; // input point is null
    public final static int EXIT_CREATE_FILEID_ERROR = -8008; // cat find unused fileid in limited times
    public final static int EXIT_COREID_CONFLICT_ERROR = -8009; // CORE id conflict
    public final static int EXIT_CORE_EXIST_ERROR = -8010; // LogicCORE already Exists
    public final static int EXIT_COMPACT_CORE_ERROR = -8011; // compact CORE error
    public final static int EXIT_DISK_OPER_INCOMPLETE = -8012; // read or write length is less than required
    public final static int EXIT_DATA_FILE_ERROR = -8013; // datafile is NULL  / crc / getdata error
    public final static int EXIT_DATAFILE_OVERLOAD = -8014; // too much data file
    public final static int EXIT_DATAFILE_EXPIRE_ERROR = -8015; // data file is expired
    public final static int EXIT_FILE_INFO_ERROR = -8016; // file flag or id error when read file
    public final static int EXIT_RENAME_FILEID_SAME_ERROR = -8017; // fileid is same in rename file
    public final static int EXIT_FILE_STATUS_ERROR = -8018; // file status error(in unlinkfile)
    public final static int EXIT_FILE_ACTION_ERROR = -8019; // action is not defined(in unlinkfile)
    public final static int EXIT_FS_NOTINIT_ERROR = -8020; // file system is not inited
    public final static int EXIT_BITMAP_CONFLICT_ERROR = -8021; // file system's bit map conflict
    public final static int EXIT_PHYSIC_UNEXPECT_FOUND_ERROR = -8022; // physical CORE is already exist in file system
    public final static int EXIT_CORE_SETED_ERROR = -8023;
    public final static int EXIT_INDEX_ALREADY_LOADED_ERROR = -8024; // index is loaded when create or load
    public final static int EXIT_META_NOT_FOUND_ERROR = -8025; // meta not found in index
    public final static int EXIT_META_UNEXPECT_FOUND_ERROR = -8026; // meta found in index when insert
    public final static int EXIT_META_OFFSET_ERROR = -8027; // require offset is out of index size
    public final static int EXIT_BUCKET_CONFIGURE_ERROR = -8028; // bucket size is conflict with before
    public final static int EXIT_INDEX_UNEXPECT_EXIST_ERROR = -8029; // index already exist when create index
    public final static int EXIT_INDEX_EMPTY_ERROR = -8030; // index is empty , but index is created
    public final static int EXIT_CORE_DS_VERSION_ERROR = -8031; // ds version error
    public final static int EXIT_CORE_NS_VERSION_ERROR = -8332; // ns version error
    public final static int EXIT_PHYSIC_CORE_OFFSET_ERROR = -8033; // offset is out of physical CORE size
    public final static int EXIT_READ_FILE_SIZE_ERROR = -8034; // file size is little than fileinfo
    public final static int EXIT_DS_CONNECT_ERROR = -8035; // connect to ds fail
    public final static int EXIT_CORE_CHECKER_OVERLOAD = -8036; // too much CORE checker
    public final static int EXIT_FALLOCATE_NOT_IMPLEMENT = -8037; // fallocate is not implement

    public final static int EXIT_SYSTEM_ERROR = -10000;
    public final static int EXIT_REGISTER_OPLOG_SYNC_ERROR = -12000;
    public final static int EXIT_MAKEDIR_ERROR = -13000;
}
