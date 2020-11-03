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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.edge.core.edge.EdgeDevice;
import org.cloudbus.cloudsim.edge.core.edge.ConfiguationEntity.HostEntity;
import org.cloudbus.cloudsim.edge.core.edge.ConfiguationEntity.LinkEntity;
import org.cloudbus.cloudsim.edge.core.edge.ConfiguationEntity.SwitchEntity;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.sdn.SDNHost;
import org.cloudbus.cloudsim.sdn.Switch;
import org.cloudbus.cloudsim.sdn.example.policies.VmSchedulerTimeSharedEnergy;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public abstract class OsmesisDatacenter extends Datacenter{

	private Switch gateway;
	private String dcType;
	private Map<String, Integer> vmNameToId = new HashMap<>();
	
	protected Topology topology;
	protected SDNController sdnController;
	protected List<Host> hosts = new ArrayList<>();	
	protected List<SDNHost> sdnhosts;
	protected List<Switch> switches;
	public static int cloudletsNumers = 0;
	int lastProcessTime; 	

	public abstract void initCloudTopology(List<HostEntity> hostEntites, List<SwitchEntity> switchEntites, List<LinkEntity> linkEntites);

	public abstract void initEdgeTopology(List<EdgeDevice> devices, List<SwitchEntity> switchEntites, List<LinkEntity> linkEntites);
		
	public OsmesisDatacenter(String name, DatacenterCharacteristics characteristics,
			VmAllocationPolicy vmAllocationPolicy, List<Storage> storageList, double schedulingInterval)
			throws Exception {
		super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);
	}
	
	public List<Host> getHosts() {
		return hosts;
	}
	
	public String getDcType() {
		return dcType;
	}

	public void setDcType(String dcType) {
		this.dcType = dcType;
	}
	
	public SDNController getSdnController() {
		return sdnController;
	}
	
	public void setSdnController(SDNController sdnController) {
		this.sdnController = sdnController;
	}

	public void setHostsList(List<Host> hosts){
		this.hosts = hosts;
	}
	
	public void feedSDNWithTopology(SDNController controller){
		controller.setTopology(topology, hosts, sdnhosts, switches);
	}
	
	@Override
	public void processOtherEvent(SimEvent ev){
		switch(ev.getTag()){	
			default:
				System.out.println("Unknown event recevied by SDNDatacenter. Tag:"+ev.getTag());
				break;
		}
	}	
		
	protected Host createHost(int hostId, int ram, long bw, long storage, long pes, double mips) {
		LinkedList<Pe> peList = new LinkedList<Pe>();
		int peId=0;
		for(int i=0;i<pes;i++) peList.add(new Pe(peId++,new PeProvisionerSimple(mips)));
		
		RamProvisioner ramPro = new RamProvisionerSimple(ram);
		BwProvisioner bwPro = new BwProvisionerSimple(bw);
		VmScheduler vmScheduler = new VmSchedulerTimeSharedEnergy(peList);		
		Host newHost = new Host(hostId, ramPro, bwPro, storage, peList, vmScheduler);
		
		return newHost;		
	}

	public void setGateway(Switch gateway) {		
		this.gateway = gateway;
	}	
	
	public Switch getGateway() {
		return gateway;
	}

	public void mapVmNameToID(int id, String vmName) {
		vmNameToId.put(vmName, id);
	}
	
	public int getVmIdByName(String melName){
		return this.vmNameToId.get(melName);
	}
	
	public Map<String, Integer> getVmNameToIdList(){
		return this.vmNameToId;
	}
	
	public List<SDNHost> getSdnhosts() {
		return sdnhosts;
	}

	public void setSdnhosts(List<SDNHost> sdnhosts) {
		this.sdnhosts = sdnhosts;
	}
}
