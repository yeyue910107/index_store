/**
 * 
 */
package org.act.index.manager;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.act.index.common.Address;
import org.act.index.common.ReplicateCore;
import org.act.index.common.ServerInfo;
import org.act.index.common.constdef.GlobalMessage;
import org.act.index.common.sysparam.ManagerParam;
import org.act.index.manager.strategy.ReplicateStrategy;
import org.act.index.server.IMetaService;
import org.apache.commons.collections.MultiMap;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 * 
 */
public class Replicate extends Thread {
	private static final Logger LOG = Logger.getLogger(Replicate.class);
	private MetaManager metaManager;
	private CoreCollect core;

	public Replicate(MetaManager metaManager, CoreCollect core) {
		this.metaManager = metaManager;
		this.core = core;
	}

	public static boolean check(CoreCollect coreCollect) {
		int size = coreCollect.getServerSet().size();
		LOG.debug("server set size:" + size + " min replicateion:" + ManagerParam.MIN_REPLICATION);
		if(size < ManagerParam.MIN_REPLICATION) //有server损坏(master或slave损坏)
			return false;
		return true;
	}
	
	public int execute() {
		int coreId = core.getCoreInfo().getCoreId();
		LayoutManager layout = metaManager.getLayoutManager();
		CoreCollect cc = layout.getCoreCollect(coreId);// 获取损坏coreid对应的corecollect
		if (cc == null) {
			return GlobalMessage.ISS_ERROR;
		}

		HashSet<Integer> ss = cc.getServerSet();// 获取损坏coreid对应的ServerSet
		int masterServer = cc.getMasterServer();
		int slave1 = -1;
		int slave2 = -1;

		if (ss.size() == 2)// 说明有一个Server损坏了
		{
			if (!ss.contains(masterServer)) // 说明master损坏了
			{
				Iterator<Integer> it = ss.iterator();
				slave1 = it.next();
				slave2 = it.next();
				if (layout.getServerCollect(slave1).getServerInfo()
						.getCurrentLoad() < layout.getServerCollect(slave2)
						.getServerInfo().getCurrentLoad()) {
					masterServer = slave1;
					//cc.setMasterServer(slave1);
				} else {
					masterServer = slave2;
					//cc.setMasterServer(slave2);
				}
				LOG.debug("new master server:" + masterServer);
			}
			else {
				Iterator<Integer> it = ss.iterator();
				slave1 = it.next();
				if (slave1 == masterServer)
					slave1 = it.next();
				//slave2 = it.next();
			}
			ServerInfo srcInfo = layout.getServerCollect(masterServer)
					.getServerInfo();
			Address src = new Address(srcInfo.getServerIp(), srcInfo
					.getServerPort(), masterServer, coreId);

			int serverId = ReplicateStrategy.getLeastWeightServerId(metaManager.getLayoutManager(), masterServer,
					slave1, slave2); // 目的serverId由负载均衡决定，
			// 要求此地址非masterserver,非slave
			LOG.debug("new slave server:" + serverId);
			// TODO error
			ServerInfo dstInfo = layout.getServerCollect(serverId)
					.getServerInfo();
			Address dst = new Address(dstInfo.getServerIp(), dstInfo
					.getServerPort(), serverId, coreId);
			doReplicate(src, dst, coreId);
			//ss.add(serverId);// 插入新的serverId
			cc.join(serverId, false);
			//cc.setMasterServer(masterServer);
			layout.buildServerCoreRelation(coreId, masterServer, true);
		}

		else if (ss.size() == 1) // 有两个Server损坏
		{
			if (!ss.contains(masterServer)) // 说明master损坏了
			{
				Iterator<Integer> it = ss.iterator();
				masterServer = it.next(); // 将活着的slave作为master
				LOG.debug("new master server:" + masterServer);
			}

			int serverId = ReplicateStrategy.getLeastWeightServerId(metaManager.getLayoutManager(), masterServer, -1,
					-1); // 目的serverId由负载均衡决定， 要求此地址非masterserver,非slave
			LOG.debug("new slave server:" + serverId);
			ServerInfo srcInfo = layout.getServerCollect(masterServer)
					.getServerInfo();
			Address src = new Address(srcInfo.getServerIp(), srcInfo
					.getServerPort(), masterServer, coreId);

			ServerInfo dstInfo = layout.getServerCollect(serverId)
					.getServerInfo();
			Address dst = new Address(dstInfo.getServerIp(), dstInfo
					.getServerPort(), serverId, coreId);
			doReplicate(src, dst, coreId);
			//ss.add(serverId);
			cc.join(serverId, false);
			serverId = ReplicateStrategy.getLeastWeightServerId(metaManager.getLayoutManager(), masterServer,
					serverId, -1);
			LOG.debug("new slave server:" + serverId);
			dstInfo = layout.getServerCollect(serverId).getServerInfo();
			dst = new Address(dstInfo.getServerIp(), dstInfo.getServerPort(),
					serverId, coreId);
			doReplicate(src, dst, coreId);
			//ss.add(serverId);
			//cc.setMasterServer(masterServer);
			cc.join(serverId, false);
			layout.buildServerCoreRelation(coreId, masterServer, true);
		}

		return GlobalMessage.ISS_SUCCESS;
	}

	public int doReplicate(Address src, Address dst, int coreId) {
		String url = "rmi://" + src.getAddr().getAddress().getHostAddress() + 
		":" + src.getAddr().getPort() + 
		"/meta@server" + src.getServerId();
		LOG.debug(url);
		//String name = "core" + src.getCoreId() + "@server" + src.getServerId();
		//String port = String.valueOf(master.getAddr().getPort() + master.getCoreId());
		//String port = String.valueOf(src.getAddr().getPort() + src.getServerId() * ManagerParam.MAX_CORE_COUNT_PER_SERVER + src.getCoreId() + 1);
		//String url = "rmi://" + src.getAddr().getHostName() + ":" + port + "/" + name;
		try {
			IMetaService metaService = (IMetaService)Naming.lookup(url);
			metaService.replicateCore(dst, coreId);
			metaService.backupCore(dst, coreId);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return GlobalMessage.ISS_SUCCESS;
	}
	
	public void run() {
		execute();
	}
}
