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

package org.cloudbus.osmosis.core;


import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.edge.core.edge.ConfiguationEntity.LinkEntity;
import org.cloudbus.cloudsim.edge.core.edge.ConfiguationEntity.SwitchEntity;
import org.cloudbus.cloudsim.sdn.Channel;
import org.cloudbus.cloudsim.sdn.Link;
import org.cloudbus.cloudsim.sdn.NetworkOperatingSystem;
import org.cloudbus.cloudsim.sdn.NetworkNIC;
import org.cloudbus.cloudsim.sdn.SDNHost;
import org.cloudbus.cloudsim.sdn.Switch;

import org.cloudbus.osmosis.core.polocies.SDNTrafficSchedulingPolicy;
import org.cloudbus.osmosis.core.polocies.SDNRoutingPolicy;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public class SDNController extends NetworkOperatingSystem {
	
	
	protected SDNRoutingPolicy sdnRoutingPoloicy;
	private SDNTrafficSchedulingPolicy sdnSchedulingPolicy;	
	
	private OsmosisOrchestrator orchestrator;

	protected String datacenterName; 
	private OsmesisBroker edgeDatacenterBroker;	
	
	private Datacenter datacenter;
	private Switch gateway;
    private SDNController wanController;
	
    protected String name;
    
	public SDNController(String name, SDNTrafficSchedulingPolicy sdnPolicy, SDNRoutingPolicy sdnRouting) {				
		super(name);
		this.sdnSchedulingPolicy = sdnPolicy;
		this.sdnRoutingPoloicy = sdnRouting;
	}
	
	public void setWanOorchestrator(OsmosisOrchestrator orchestrator) {
		this.orchestrator = orchestrator;
	}
	
	@Override
	public void processEvent(SimEvent ev) {
		int tag = ev.getTag();
		Flow flow;
		switch(tag){
		
		case OsmosisTags.BUILD_ROUTE:		
			 flow = (Flow) ev.getData();			
			scheduleFlow(flow);
			break;
					
		case OsmosisTags.BUILD_ROUTE_GREEN:		
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) ev.getData();
			 startTransmittingGreenEnergy(list);
			break;
			
		default: System.out.println(this.getName() + ": Unknown event received by "+super.getName()+". Tag:"+ev.getTag());
		}
	}
	
	protected void startTransmittingGreenEnergy(List<Object> list) {

	}

	private void scheduleFlow(Flow flow){				 					
		startTransmitting(flow);	
	}
	
	public void startTransmitting(Flow flow) {				
		
		int srcVm = flow.getOrigin();
		int dstVm = flow.getDestination();

		NetworkNIC srchost = findSDNHost(srcVm);
		NetworkNIC dsthost = findSDNHost(dstVm);
		int flowId = flow.getFlowId();
		
		if (srchost == null)			
		{			
			srchost = this.getGateway(); // packets coming from outside the datacenter			
		} 
									
		if(srchost.equals(dsthost)) {
			Log.printLine(CloudSim.clock() + ": " + getName() + ": Source SDN Host is same as destination. No need for routing!");			
			srchost.addRoute(srcVm, dstVm, flowId, dsthost);
			List<NetworkNIC> listNodes = new ArrayList<NetworkNIC>();					
			listNodes.add(srchost);			
			getSdnSchedulingPolicy().setAppFlowStartTime(flow, flow.getSubmitTime()); // no transmission 
					
			removeCompletedFlows(flow);
			return;
		} 
		
		List<NetworkNIC> route = new ArrayList<>();	
		route = sdnRoutingPoloicy.getRoute(flow.getOrigin(), flow.getDestination());
		
		if(route == null){			
			buildSDNForwardingTableVmBased(srcVm, dstVm, flowId, flow);
		}
		
		NetworkNIC destinationHhost = findSDNHost(dstVm);
		if(destinationHhost == null){
			List<NetworkNIC> endToEndRoute = sdnRoutingPoloicy.getRoute(flow.getOrigin(), flow.getDestination());					
			flow.setNodeOnRouteList(endToEndRoute);
			
			List<Link> links = sdnRoutingPoloicy.getLinks(flow.getOrigin(), flow.getDestination());
			flow.setLinkList(links);
			
			sendNow(this.getWanController().getId(), OsmosisTags.BUILD_ROUTE, flow);
			return;
		}

		List<NetworkNIC> endToEndRoute = sdnRoutingPoloicy.getRoute(flow.getOrigin(), flow.getDestination());					
		flow.setNodeOnRouteList(endToEndRoute);		
		List<Link> links = sdnRoutingPoloicy.getLinks(flow.getOrigin(), flow.getDestination());
		flow.setLinkList(links);
		
		sendNow(this.getWanOorchestrator().getId(), OsmosisTags.START_TRANSMISSION, flow);																
	}

	protected void processCompleteFlows(List<Channel> channels){
		for(Channel ch:channels) {												
			for (Flow flow : ch.getFinishedFlows()){
				removeCompletedFlows(flow);
			}
		}
	}
	
	protected void removeCompletedFlows(Flow flow ){				
						
	}
		 
	protected boolean buildSDNForwardingTableVmBased(int srcVm, int dstVm, int flowId, Flow flow) {		

		NetworkNIC desthost = findSDNHost(dstVm);		
		if(desthost == null){
			/*
			 * If desthost is null, it means the destination resides in a different datacenter.
			 * Send the packet to the gateway.
			 */
			desthost = this.getGateway(); 
			flow.setLabelPlace("outside");
		} 

		List<NetworkNIC> route = new ArrayList<>();				
		NetworkNIC srcHost = findSDNHost(srcVm);
		
		if (srcHost == null)			
		{			
			srcHost = this.getGateway(); // packets coming from outside the datacenter			
		} 
		
		route = sdnRoutingPoloicy.buildRoute(srcHost, desthost, flow); 			
				
		NetworkNIC currentNode = null;
		NetworkNIC nextNode = null;

		int iterate = route.size()-1;
		for(int i = iterate; i >= 0; i--){
			currentNode = route.get(i); 
			if(currentNode.equals(desthost)){
				break;
			}else{
				nextNode = route.get(i-1);	
			}			
			currentNode.addRoute(srcVm, dstVm, flowId, nextNode);
		}	
		return true;			
	}
	 	
	public SDNRoutingPolicy getSdnRoutingPoloicy() {
		return this.sdnRoutingPoloicy;
	}

	
	public SDNTrafficSchedulingPolicy getSdnSchedulingPolicy() {
		return sdnSchedulingPolicy;
	}

	public void addVmsToSDNhosts(List<? extends Vm> vmList){
		this.vmList = vmList;
		
		for (Vm vm : this.vmList){
			NetworkOperatingSystem.debugVmIdName.put(vm.getId(),vm.getVmName());
		}		
	}
	
	public void setTopology(Topology topology, List<Host> hosts, List<SDNHost> sdnhosts, List<Switch> switches){	
		this.topology = topology;
		this.hosts = hosts;
		this.sdnhosts = sdnhosts;
		this.switches = switches;
		this.sdnRoutingPoloicy.setNodeList(topology.getAllNodes(), topology);		
		this.sdnRoutingPoloicy.buildNodeRelations(topology);
		for(Switch sw : switches){
			if(sw.getSwType().equals("gateway")){
				this.gateway = sw;
			}
		}
	}				
	
	public void setEdgeDataCenterBroker(OsmesisBroker edgeDataCenterBroker) {
		edgeDatacenterBroker = edgeDataCenterBroker;
	}
	
	public OsmosisOrchestrator getWanOorchestrator() {
		return this.orchestrator;
	}
	
	public OsmesisBroker getEdgeDataCenterBroker() {
		return edgeDatacenterBroker;
	}

    public void setName(String name){
    	this.name = name;
    }
    
	public void setDatacenter(Datacenter dc) {		
		this.datacenter = dc;
		this.datacenterName = this.getDatacenter().getName();
	}	
	
	public Datacenter getDatacenter() {
		return datacenter;
	}

    public void setWanController(SDNController wanController) {
        this.wanController = wanController;
    }

    public SDNController getWanController() {
        return wanController;
    }

	public Switch getGateway() {
		return this.gateway;
	}

	public void addAllDatacenters(List<OsmesisDatacenter> osmesisDatacentres) {

	}

	public void initSdWANTopology(List<SwitchEntity> switches, List<LinkEntity> wanLinks, List<Switch> datacenterGateways) {
		
	}
}
