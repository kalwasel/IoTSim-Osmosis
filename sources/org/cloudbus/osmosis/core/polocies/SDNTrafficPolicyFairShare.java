/*
 * Title:        BigDataSDNSim 1.0
 * Description:  BigDataSDNSim enables the simulating of MapReduce, big data management systems (YARN), 
 * 				 and software-defined networking (SDN) within cloud environments.
 * 
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2020, Newcastle University (UK) and Saudi Electronic University (Saudi Arabia) 
 * 
 */

package org.cloudbus.osmosis.core.polocies;

import java.util.ArrayList;
import java.util.List;
import org.cloudbus.osmosis.core.Flow;


/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since BigDataSDNSim 1.0
 */

public class SDNTrafficPolicyFairShare extends SDNTrafficSchedulingPolicy {

	protected List<Flow> packetList;

	public SDNTrafficPolicyFairShare(){
		packetList = new ArrayList<Flow>();		
		setPolicyName("FairShair");
	}
	
	@Override
	public void setFlowPriority(Flow pkt) {
		packetList.add(pkt);
	}

	@Override
	public void setFlowPriority(List<Flow> pkts) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Flow getFlowPrioritySingle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Flow> getFlowPriorityList() {
		return packetList;
	}

	@Override
	public int checkAllQueueSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean removeFlowFromList(Flow pkt) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeFlowFromList(List<Flow> pkts) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Flow> splitFlow(Flow flow) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFirstAppInQueue(int appId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFirstAppInQueue() {
		// TODO Auto-generated method stub
		return 0;
	}

}
