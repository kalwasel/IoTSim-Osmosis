/*
 * Title:        CloudSimSDN
 * Description:  SDN extension for CloudSim
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2015, The University of Melbourne, Australia
 */
package org.cloudbus.cloudsim.sdn;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;

import org.cloudbus.osmosis.core.Flow;

/**
 * This represents switches that maintain routing information.
 * Note that all traffic estimation is calculated within NOS class, not in Switch class.
 * Energy consumption of Switch is calculated in this class by utilization history.
 * 
 * 
 * @author Jungmin Son
 * @author Rodrigo N. Calheiros
 * @since CloudSimSDN 1.0
 */
public class Switch extends SimEntity implements NetworkNIC{
	List<NetworkNIC> adjuNodes = new ArrayList<>(); 

	Map<Flow,Double> delayQueuing;
	
	private static double POWER_CONSUMPTION_IDLE = 66.7;
	private static double POWER_CONSUMPTION_PER_ACTIVE_PORT = 1; 
	/* based on CARPO: Correlation-Aware Power Optimization in Data Center Networks by Xiaodong Wang et al. */
		
	long iops;
	double previousTime;
	int rank = -1;
	int currentupports=0;
	int currentdownports=0;
	private String swType;

	public String getSwType() {
		return swType;
	}


	ArrayList<Link> links = new ArrayList<Link>();

	ForwardingTable forwardingTable;

	Hashtable<Flow,Long> processingTable;
					
	public Switch(String name, String switchType, long iops) {
		super(name);		
		this.swType = switchType;
		this.iops = iops;
		this.previousTime = 0.0;

		this.forwardingTable = new ForwardingTable();
		this.processingTable = new Hashtable<Flow,Long>();
	}
	
	@Override
	public void startEntity() {}
	
	@Override
	public void shutdownEntity() {}

	@Override
	public void processEvent(SimEvent ev) {
		int tag = ev.getTag();
		
		switch(tag){
			default: System.out.println("Unknown event received by "+super.getName()+". Tag:"+ev.getTag());
		}
	}

	public void addLink(Link l){
		this.links.add(l);
	}
	
	
	/************************************************
	 *  Calculate Utilization history
	 ************************************************/
	private List<HistoryEntry> utilizationHistories = null;
	private static double powerOffDuration = 0; //if switch was idle for 1 hours, it's turned off.

	// HistoryEntry is a nested class (class within another class) 
	public class HistoryEntry {
		public double startTime;
		public int numActivePorts;
		HistoryEntry(double t, int n) { startTime=t; numActivePorts=n;}
	}
	public List<HistoryEntry> getUtilizationHisotry() {
		return utilizationHistories;
	}
	
	public double getUtilizationEnergyConsumption() {
		
		double total=0;
		double lastTime=0;
		int lastPort=0;
		if(this.utilizationHistories == null)
			return 0;
		
		for(HistoryEntry h:this.utilizationHistories) {
			double duration = h.startTime - lastTime;
			double power = calculatePower(lastPort);
			double energyConsumption = power * duration;
			
			// Assume that the host is turned off when duration is long enough
			if(duration > powerOffDuration && lastPort == 0)
				energyConsumption = 0;
			
			total += energyConsumption;
			lastTime = h.startTime;
			lastPort = h.numActivePorts;
		}
		return total/3600;	// transform to Whatt*hour from What*seconds
	}
	public void updateNetworkUtilization() {
		this.addUtilizationEntry();
	}

	public void addUtilizationEntryTermination(double finishTime) {
		if(this.utilizationHistories != null)
			this.utilizationHistories.add(new HistoryEntry(finishTime, 0));		
	}

	private void addUtilizationEntry() {
		double time = CloudSim.clock();
		int totalActivePorts = getTotalActivePorts();
		if(utilizationHistories == null)
			utilizationHistories = new ArrayList<HistoryEntry>();
		else {
			HistoryEntry hist = this.utilizationHistories.get(this.utilizationHistories.size()-1);
			if(hist.numActivePorts == totalActivePorts) {
				return;
			}
		}		
		this.utilizationHistories.add(new HistoryEntry(time, totalActivePorts));
	}
	private double calculatePower(int numActivePort) {
		double power = POWER_CONSUMPTION_IDLE + POWER_CONSUMPTION_PER_ACTIVE_PORT * numActivePort;
		return power;
	}
	private int getTotalActivePorts() {
		int num = 0;
		for(Link l:this.links) {
			if(l.isActive())
				num++;
		}
		return num;
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
		this.forwardingTable.addRule(src, dest, flowId, to);
	}
	
	@Override
	public NetworkNIC getVMRoute(int src, int dest, int flowId){
		NetworkNIC route= this.forwardingTable.getRoute(src, dest, flowId);
		if(route == null) {
//			this.printVMRoute();
			System.err.println("SDNSwitch.getRoute() ERROR: Cannot find route:" + 
					NetworkOperatingSystem.debugVmIdName.get(src) + "->"+
					NetworkOperatingSystem.debugVmIdName.get(dest) + ", flow ="+flowId);
		}
			
		return route;
	}
	
	@Override
	public void removeVMRoute(int src, int dest, int flowId){
		forwardingTable.removeRule(src, dest, flowId);
	}

	
	public String toString() {
		return "Switch: "+this.getName();
	}


	@Override
	public NetworkNIC updateVMRoute(int srcVM, int destVM, int flowId, NetworkNIC to) {
		// TODO Auto-generated method stub
		this.forwardingTable.addRule(srcVM, destVM, flowId, to);
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
		this.forwardingTable.addRule(srcHost.getAddress(), desthost.getAddress(), flowId, nextNode);
	}

}
