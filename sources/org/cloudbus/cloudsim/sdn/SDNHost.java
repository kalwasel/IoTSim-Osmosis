/*
 * Title:        CloudSimSDN
 * Description:  SDN extension for CloudSim
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2015, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.sdn;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;


/**
 * Extended class of Host to support SDN.
 * Added function includes data transmission after completion of Cloudlet compute processing.
 * 
 * @author Jungmin Son
 * @author Rodrigo N. Calheiros
 * @since CloudSimSDN 1.0
 */
public class SDNHost extends SimEntity implements NetworkNIC {
	Host host;	
	ForwardingTable forwardingTable;
	List<NetworkNIC> adjuNodes = new ArrayList<>(); 
	String hostName; 
	
	public SDNHost(Host host, String name){
		super(name);		
		this.host=host;	
		this.hostName = name;
		this.forwardingTable = new ForwardingTable();
	}
	
	public Host getHost(){
		return host;
	}

	@Override
	public void startEntity(){}
	
	@Override
	public void shutdownEntity(){}

	@Override
	public void processEvent(SimEvent ev) {
		int tag = ev.getTag();
		switch(tag){

			default: System.out.println("Unknown event received by "+super.getName()+". Tag:"+ev.getTag());
		}
	}
	

	
	/******* Routeable interface implementation methods ******/

	@Override
	public int getAddress() {
		return super.getId();
	}

	@Override
	public void clearVMRoutingTable(){
		this.forwardingTable.clear();
	}

	@Override
	public void addRoute(int src, int dest, int flowId, NetworkNIC to){
		forwardingTable.addRule(src, dest, flowId, to);
	}
	
	@Override
	public NetworkNIC getVMRoute(int src, int dest, int flowId){
		NetworkNIC route= this.forwardingTable.getRoute(src, dest, flowId);
		if(route == null) {
			System.err.println("SDNHost: ERROR: Cannot find route:" + src + "->"+dest + ", flow ="+flowId);
		}
			
		return route;
	}
	
	@Override
	public void removeVMRoute(int src, int dest, int flowId){
		forwardingTable.removeRule(src, dest, flowId);
	}

	
	public String toString() {
		return "SDNHost: "+this.getName();
	}

	@Override
	public void addLink(Link l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNetworkUtilization() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NetworkNIC updateVMRoute(int srcVM, int destVM, int flowId, NetworkNIC to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAdjancentNodes(List<NetworkNIC> nodes) {
		// TODO Auto-generated method stub
		adjuNodes.addAll(nodes);
	}

	@Override
	public List<NetworkNIC> getAdjancentNodes() {
		return this.adjuNodes;
		
	}

	@Override
	public void addRoute(NetworkNIC srcHost, NetworkNIC desthost, int flowId, NetworkNIC nextNode) {
		// TODO Auto-generated method stub
		
	}

}
