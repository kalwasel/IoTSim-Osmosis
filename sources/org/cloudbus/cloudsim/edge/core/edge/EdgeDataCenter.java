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

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.core.predicates.PredicateType;
import org.cloudbus.cloudsim.edge.core.edge.EdgeDevice;
import org.cloudbus.cloudsim.edge.core.edge.ConfiguationEntity.HostEntity;
import org.cloudbus.cloudsim.edge.core.edge.ConfiguationEntity.LinkEntity;
import org.cloudbus.cloudsim.edge.core.edge.ConfiguationEntity.SwitchEntity;
import org.cloudbus.osmosis.core.Flow;
import org.cloudbus.osmosis.core.OsmesisBroker;
import org.cloudbus.osmosis.core.OsmesisDatacenter;
import org.cloudbus.osmosis.core.OsmosisTags;
import org.cloudbus.osmosis.core.Topology;
import org.cloudbus.cloudsim.sdn.SDNHost;
import org.cloudbus.cloudsim.sdn.Switch;


public class EdgeDataCenter extends OsmesisDatacenter{
	
	private List<Flow> flowList = new ArrayList<>(); 
	private List<Flow> flowListHis = new ArrayList<>(); 
	
	public EdgeDataCenter(String name, DatacenterCharacteristics characteristics,
			VmAllocationPolicy vmAllocationPolicy, List<Storage> storageList, double schedulingInterval)
			throws Exception {
		super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);
	}

	@Override
	public void processEvent(SimEvent ev) {
		// TODO Auto-generated method stub

		super.processEvent(ev);

	}

	@Override
	public void processOtherEvent(SimEvent ev) {
		int tag = ev.getTag();
		switch (tag) {
			
		case OsmosisTags.TRANSMIT_IOT_DATA:
			this.transferIoTData(ev);
			break;
			
		case OsmosisTags.INTERNAL_EVENT:
			updateFlowTransmission();
			break;			
			
		case OsmosisTags.BUILD_ROUTE:
			sendMelDataToClouds(ev);
			break;
			
		default:			
			System.out.println("Unknown event recevied by SDNDatacenter. Tag:"+ev.getTag());		
			break;
		}
	}	

	private void sendMelDataToClouds(SimEvent ev) {
		Flow flow  = (Flow) ev.getData();
		sendNow(this.getSdnController().getId(), OsmosisTags.BUILD_ROUTE, flow);
	}

	public void updateFlowTransmission() {		
		LinkedList<Flow> finshedFlows = new LinkedList<>();
		for(Flow flow : this.flowList){
			boolean isCompleted = flow.updateTransmission();						
			if(isCompleted){
				finshedFlows.add(flow);	
			}			
		}
		
		if(finshedFlows.size() != 0){
			this.flowList.removeAll(finshedFlows);
			
			for(Vm vm : this.getVmList()){
				MicroELement mel = (MicroELement) vm;				
				this.removeFlows(mel, finshedFlows);
				mel.updateAssociatedIoTDevices();
			}
			for(Flow flow : finshedFlows){
				int tag = OsmosisTags.Transmission_ACK;		
				sendNow(OsmesisBroker.brokerID, tag, flow);
			}				
		}		
		determineEarliestFinishingFlow();
	}
	
	private void removeFlows(MicroELement mel, LinkedList<Flow> finshedFlows) {
		LinkedList<Flow> removedList = new LinkedList<>();
		for (Flow flow : mel.getFlowList()){
			for (Flow removedFlow : finshedFlows){
				if(flow.getFlowId() == removedFlow.getFlowId()){
					removedList.add(flow);
				}
			}
		}
		mel.removeFlows(removedList);
		removedList.clear();
	}

	private void transferIoTData(SimEvent ev) {		
		updateFlowTransmission();
		
		Flow flow = (Flow) ev.getData();		
		flow.setDatacenterName(this.getName());
		
		this.flowList.add(flow);
		flowListHis.add(flow);
		
		Vm vm = null;
		for(Vm getVm : this.getVmList()){
			if(getVm.getId() == flow.getDestination()){		
				vm = getVm;
				break;
			}
		}	
		
		if(vm != null){
			MicroELement mel = (MicroELement) vm;
			mel.addFlow(flow);
			mel.updateAssociatedIoTDevices();
		}
		flow.setPreviousTime(CloudSim.clock());
		determineEarliestFinishingFlow();
	}

	private void determineEarliestFinishingFlow() {
		
		CloudSim.cancelAll(getId(), new PredicateType(OsmosisTags.INTERNAL_EVENT));
		double eft = Double.MAX_VALUE;
		double finishingTime;
		
		if(flowList.size() != 0) {
			for(Flow flow : this.flowList){			
				finishingTime = flow.FinishingTime();
				if(finishingTime < eft){
					eft = finishingTime;
				}
			}
			send(this.getId(), eft,  OsmosisTags.INTERNAL_EVENT);
		}
	}

	@Override
	public void initCloudTopology(List<HostEntity> hostEntites, List<SwitchEntity> switchEntites,
			List<LinkEntity> linkEntites) {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void initEdgeTopology(List<EdgeDevice> devices, List<SwitchEntity> switchEntites, List<LinkEntity> linkEntites){
		this.hosts.addAll(devices); 
		topology  = new Topology();		 
		sdnhosts = new ArrayList<SDNHost>();
		switches= new ArrayList<Switch>();
		
		Hashtable<String,Integer> nameIdTable = new Hashtable<String, Integer>();
					    		    		    
		for(EdgeDevice device : devices){
												
			String hostName = device.getDeviceName();					
			SDNHost sdnHost = new SDNHost(device, hostName);
			nameIdTable.put(hostName, sdnHost.getAddress());											
			this.topology.addNode(sdnHost);		
			this.sdnhosts.add(sdnHost);			
		}
	
		for(SwitchEntity switchEntity : switchEntites){							
			long iops = switchEntity.getIops();
			String switchName = switchEntity.getName();
			String swType = switchEntity.getType();
			Switch sw = null;
			sw = new Switch(switchName,swType, iops);					
			if(sw != null) {
				nameIdTable.put(switchName, sw.getAddress());
				this.topology.addNode(sw);
				this.switches.add(sw);
			}
		}
			
		for(LinkEntity linkEntity : linkEntites){									
				String src = linkEntity.getSource();  
				String dst = linkEntity.getDestination();				
				long bw = linkEntity.getBw();
				int srcAddress = nameIdTable.get(src);
				if(dst.equals("")){
					System.out.println("Null!");			
				}
				int dstAddress = nameIdTable.get(dst);
				topology.addLink(srcAddress, dstAddress, bw);
		}
	}

}