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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.edge.core.edge.EdgeDevice;
import org.cloudbus.cloudsim.edge.core.edge.ConfiguationEntity.HostEntity;
import org.cloudbus.cloudsim.edge.core.edge.ConfiguationEntity.LinkEntity;
import org.cloudbus.cloudsim.edge.core.edge.ConfiguationEntity.SwitchEntity;
import org.cloudbus.cloudsim.sdn.SDNHost;
import org.cloudbus.cloudsim.sdn.Switch;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public class CloudDatacenter extends OsmesisDatacenter {

		public static int cloudletsNumers = 0;
		int lastProcessTime;	

		public CloudDatacenter(String name,
								DatacenterCharacteristics characteristics,
								VmAllocationPolicy vmAllocationPolicy,
								List<Storage> storageList,
								double schedulingInterval,
								SDNController sdnController) throws Exception {
			super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);		
			this.sdnController = sdnController;		
		}

		public void addVm(Vm vm){
			getVmList().add(vm);
			if (vm.isBeingInstantiated()) vm.setBeingInstantiated(false);
			vm.updateVmProcessing(CloudSim.clock(), getVmAllocationPolicy().getHost(vm).getVmScheduler().getAllocatedMipsForVm(vm));
		}
		
		@Override
		public void processOtherEvent(SimEvent ev){
			switch(ev.getTag()){
				default: System.out.println("Unknown event recevied by SDNDatacenter. Tag:"+ev.getTag());
			}
		}
		
		public Map<String, Integer> getVmNameIdTable() {
			return this.sdnController.getVmNameIdTable();
		}
		public Map<String, Integer> getFlowNameIdTable() {
			return this.sdnController.getFlowNameIdTable();
		}

		@Override
		public String toString() {
			return this.getClass().getSimpleName() + "{" +
					"id=" + this.getId() +
					", name=" + this.getName() +
					'}';
		}
		
	public void initCloudTopology(List<HostEntity> hostEntites, List<SwitchEntity> switchEntites, List<LinkEntity> linkEntites) {
		 topology  = new Topology();		 
		 sdnhosts = new ArrayList<SDNHost>();
		 switches= new ArrayList<Switch>();
		 
		Hashtable<String,Integer> nameIdTable = new Hashtable<String, Integer>();
					    		    		    
		for(HostEntity hostEntity : hostEntites){															
			long pes =  hostEntity.getPes();
			long mips = hostEntity.getMips();
			int ram = hostEntity.getRam();
			long storage = hostEntity.getStorage();					
			long bw = hostEntity.getBw(); 																				
			String hostName = hostEntity.getName();					
			Host host = createHost(OsmosisBuilder.hostId, ram, bw, storage, pes, mips);
			host.setDatacenter(this);
			SDNHost sdnHost = new SDNHost(host, hostName);
			nameIdTable.put(hostName, sdnHost.getAddress());					
			OsmosisBuilder.hostId++;					
			this.topology.addNode(sdnHost);
			this.hosts.add(host);
			this.sdnhosts.add(sdnHost);			
		}
	
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

	@Override
	public void initEdgeTopology(List<EdgeDevice> devices, List<SwitchEntity> switchEntites,
			List<LinkEntity> linkEntites) {
		// TODO Auto-generated method stub
		
	}		
}
