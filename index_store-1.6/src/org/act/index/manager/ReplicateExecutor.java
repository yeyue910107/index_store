/**
 * 
 */
package org.act.index.manager;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections.MultiMap;
import org.act.index.common.Address;
import org.act.index.common.ReplicateCore;
import org.act.index.common.sysparam.ManagerParam;

/**
 * @author Administrator
 *
 */
public class ReplicateExecutor {
	private HashMap<Integer, ReplicateCore> replicatingMap;
	private MetaManager metaManager;
	private MultiMap srcServerCounter;
	private MultiMap dstServerCounterMap;
	
	public ReplicateExecutor(MetaManager metaManager) {
		this.metaManager = metaManager;
	}
	
	
	/*public int execute(int coreId) {
		LayoutManager layout=metaManager.getLayoutManager();
		CoreCollect cc=layout.getCoreCollect(coreId);//获取损坏coreid对应的corecollect
		if(cc==null)
		{
			return 0;
		}
		
		HashSet<Integer> ss=cc.getServerSet();//获取损坏coreid对应的ServerSet
		int masterServer=cc.getMasterServer();
		int slave1=-1;
		int slave2=-1;
		
		if(ss.size()==2)//说明有一个Server损坏了
		{  
			if(!ss.contains(masterServer)) //说明master损坏了
			{
				Iterator  it=ss.iterator();
				slave1=Integer.parseInt(it.next().toString());
				slave2=Integer.parseInt(it.next().toString());
				if(layout.getServerCollect(slave1).getServerInfo().getCurrentLoad()<layout.getServerCollect(slave2).getServerInfo().getCurrentLoad())
				{
					masterServer=slave1;
				}
				else
				{
					masterServer=slave2;
				}			 				
			}
				Address src=new Address(layout.getServerCollect(masterServer).getServerAddr(), coreId);
				
				int serverId=storeWeight.getLeastWeightServerId(masterServer,slave1,slave2 ); //目的serverId由负载均衡决定， 要求此地址非masterserver,非slave
			
				Address dst= new Address(layout.getServerCollect(serverId).getServerAddr(), coreId);
				doReplicate(src,dst,coreId);			 
				ss.add(serverId);//插入新的serverId		
				cc.setMasterServer(masterServer);  
		}
		
		else if(ss.size()==1) //有两个Server损坏
		{
		   if(!ss.contains(masterServer))	//说明master损坏了
		   {
				Iterator  it=ss.iterator();
				masterServer=Integer.parseInt(it.next().toString()); //将活着的slave作为master
		   }
		   
				int serverId=storeWeight.getLeastWeightServerId(masterServer,-1,-1 ); //目的serverId由负载均衡决定， 要求此地址非masterserver,非slave
                Address src=new Address(layout.getServerCollect(masterServer).getServerAddr(), coreId);	
				Address dst= new Address(layout.getServerCollect(serverId).getServerAddr(), coreId);
				doReplicate(src,dst,coreId);
				ss.add(serverId);
				serverId=storeWeight.getLeastWeightServerId(masterServer,serverId,-1 );
				dst= new Address(layout.getServerCollect(serverId).getServerAddr(), coreId);
				doReplicate(src,dst,coreId);
				ss.add(serverId);
				cc.setMasterServer(masterServer);  					 
		}
		
		return 1;
	}*/
	
	public int execute(List<ReplicateCore> replicatePlan) {
		return 0;
	}
	
	public int complete(ReplicateCore core, boolean success) {
		return 0;
	}
	
	public int doReplicate(Address src, Address dst, int coreId) {
		return 0;
	}
	
	public int check(int coreId, Calendar maxTime, boolean complete, boolean timeout) {
		return 0;
	}
	
	public boolean isReplicatingCore(ReplicateCore core, int count) {
		return true;
	}
	
	public boolean isReplicatingCore(int coreId) {
		return true;
	}

	public void clearAll() {
		
	}
}
