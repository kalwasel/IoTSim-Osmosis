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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.osmosis.core.Flow;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since BigDataSDNSim 1.0
 */

public abstract class SDNMapReduceSchedulingPolicy {
	private String policyName;	
	
	public abstract void setFlowPriority(Flow flow);
	public abstract void setFlowPriority(List<Flow> flows);
	
	public abstract Flow getFlowPrioritySingle();
	public abstract List<Flow> getFlowPriorityList();
	
	public abstract int checkAllQueueSize();
		
	public abstract void setFirstAppInQueue(int appId);
	public abstract int getFirstAppInQueue();	
	
	public abstract boolean removeFlowFromList(Flow flow);	
	public abstract void removeFlowFromList(List<Flow> flows);

	private Map<Flow, Double> appFlowSubmitTime = new HashMap<Flow, Double>();
	private Map<Flow, Double> appFlowStartTime = new HashMap<Flow, Double>();

	public abstract List<Flow> splitFlow(Flow flow) ;
	
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	
	public void setAppFlowSubmitTime(Flow flow, double flowSubmitTime) {
		this.appFlowSubmitTime.put(flow, flowSubmitTime);
	}
	
	public void setAppFlowStartTime(Flow flow, double flowStartTime) {
		this.appFlowStartTime.put(flow, flowStartTime);
	}
	
}
