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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.edge.core.edge.ConfiguationEntity.LinkEntity;
import org.cloudbus.cloudsim.edge.core.edge.ConfiguationEntity.SwitchEntity;
import org.cloudbus.cloudsim.sdn.Link;
import org.cloudbus.cloudsim.sdn.NetworkNIC;
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

public class SDWANController extends SDNController {
	
	private List<OsmesisDatacenter> osmesisDatacentres; 	
	private Map<OsmesisDatacenter, List<Integer>> datacenterVmList;
	protected Topology topology;
	
	public SDWANController(String name, SDNTrafficSchedulingPolicy sdnPolicy, SDNRoutingPolicy sdnRouting){
		super(name, sdnPolicy,sdnRouting);
		this.datacenterName = "WAN_Layer";
	}

	public void addAllDatacenters(List<OsmesisDatacenter> osmesisDatacentres) {
		this.osmesisDatacentres = new ArrayList<>();		
		this.osmesisDatacentres = osmesisDatacentres;
		
		this.datacenterVmList = new HashMap<>();
		for(OsmesisDatacenter dc : this.osmesisDatacentres){
			List<Integer> list = new ArrayList<>();
			for(Vm vm : dc.getVmList()){
				list.add(vm.getId());
			}
			datacenterVmList.put(dc, list);
		}		
	}
		
	public List<OsmesisDatacenter> getOsmesisDatacentres() {
		return osmesisDatacentres;
	}
	
	private OsmesisDatacenter findDatacenter(int vmId){
		OsmesisDatacenter datacenter = null;		
		
		for(OsmesisDatacenter dc : this.osmesisDatacentres){
			if(datacenterVmList.get(dc).contains(vmId)){
				datacenter = dc;	
			}
		}
		return datacenter;
	}
	
	public void startTransmitting(Flow flow) {				

		int srcVm = flow.getOrigin();
		int dstVm = flow.getDestination();
		
		OsmesisDatacenter srcDC = findDatacenter(srcVm);
		OsmesisDatacenter destDC = findDatacenter(dstVm);		
		
		NetworkNIC srchost = srcDC.getGateway();
		NetworkNIC dsthost = destDC.getGateway();
		
		int flowId = flow.getFlowId();
		
		if (srchost != null)			
		{		
			List<NetworkNIC> route = new ArrayList<>();	
			route = sdnRoutingPoloicy.getRoute(flow.getOrigin(), flow.getDestination());			
			if(route == null){			
				buildSDNForwardingTableVmBased(srchost, dsthost, flowId, flow);
			}
				 												
			List<NetworkNIC> endToEndRoute = sdnRoutingPoloicy.getRoute(flow.getOrigin(), flow.getDestination());					
			flow.setNodeOnRouteList(endToEndRoute);
			
			List<Link> links = sdnRoutingPoloicy.getLinks(flow.getOrigin(), flow.getDestination());
			flow.setLinkList(links);
			
			sendNow(destDC.getSdnController().getId(), OsmosisTags.BUILD_ROUTE, flow);		
			return;			
												
		} 		 	
	}

	protected boolean buildSDNForwardingTableVmBased(NetworkNIC srcHost, NetworkNIC desthost, int flowId, Flow flow) {		

		List<NetworkNIC> route = new ArrayList<>();					
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
			currentNode.addRoute(srcHost, desthost, flowId, nextNode);
		}	
		return true;			
	}

	public void initSdWANTopology(List<SwitchEntity> switchEntites, List<LinkEntity> linkEntites,  List<Switch> datacenterGateway) {
		topology  = new Topology();		 		 
		switches= new ArrayList<Switch>();
		 
		Hashtable<String,Integer> nameIdTable = new Hashtable<String, Integer>();
					    		    		   	
		for(SwitchEntity switchEntity : switchEntites){							
			long iops = switchEntity.getIops();
			String switchName = switchEntity.getName();
			String switchType = switchEntity.getType();
			Switch sw = null;
			sw = new Switch(switchName, switchType, iops);					
			if(sw != null) {
				nameIdTable.put(switchName, sw.getAddress());
				this.topology.addNode(sw);
				this.switches.add(sw);
			}
		}
		
		if(datacenterGateway != null){
					
			for(Switch datacenterGW : datacenterGateway){										
				if(datacenterGateway != null) {
					nameIdTable.put(datacenterGW.getName(), datacenterGW.getAddress());
					this.topology.addNode(datacenterGW);
					this.switches.add(datacenterGW);
				}
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
		this.sdnRoutingPoloicy.setNodeList(topology.getAllNodes(), topology);	
	}
}
