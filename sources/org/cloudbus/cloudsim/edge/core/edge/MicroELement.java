/*
 * Title:        IoTSim-Osmosis 1.0
 * Description:  IoTSim-Osmosis enables the testing and validation of osmotic computing applications 
 * 			     over heterogeneous edge-cloud SDN-aware environments.
 * 
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2020, Newcastle University (UK) and Saudi Electronic University (Saudi Arabia) 
 * 
 */

package org.cloudbus.cloudsim.edge.core.edge;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.edge.core.edge.MicroELement;
import org.cloudbus.osmosis.core.Flow;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public class MicroELement extends Vm {
	
	private int edgeDatacenterId;	

	public MicroELement(int edgeDatacenterId, int id, int userId, double mips, int numberOfPes, int ram, long bw, String vmm,
			CloudletScheduler cloudletScheduler,float shrinkingFactor) {
		super(id, userId, mips, numberOfPes, ram, bw, 2048,vmm, cloudletScheduler);
		this.edgeDatacenterId = edgeDatacenterId;		
	}		
	
	public int getEdgeDatacenterId() {
		return edgeDatacenterId;
	}

	public void updateAssociatedIoTDevices() {		
		int numOfFlows = flowList.size();
		if(numOfFlows == 0){
			numOfFlows = 1;
		}
		double bandwidth = this.getBw() / numOfFlows; // the updated bw 		
		for(Flow getFlow : this.flowList){
			getFlow.updateBandwidth(bandwidth);
		}		
	}

	private List<Flow> flowList = new ArrayList<>(); 

	List<Flow> flowListHis =  new ArrayList<>();

	public List<Flow> getFlowList() {
		return flowList;
	}
	
	public void addFlow(Flow flow) {
		flowList.add(flow);	
		flowListHis.add(flow);	
	}

	public void removeFlows(LinkedList<Flow> removedList) {
		this.flowList.removeAll(removedList);
		
	}

}
