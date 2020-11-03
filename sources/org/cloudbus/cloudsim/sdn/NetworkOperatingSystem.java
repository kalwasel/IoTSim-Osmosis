/*
 * Title:        CloudSimSDN
 * Description:  SDN extension for CloudSim
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2015, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.sdn;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;

import org.cloudbus.cloudsim.core.CloudSim;

import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.core.predicates.PredicateType;

import org.cloudbus.osmosis.core.OsmosisTags;
import org.cloudbus.osmosis.core.Topology;

/**
 * NOS calculates and estimates network behaviour. It also mimics SDN Controller functions.  
 * It manages channels between switches, and assigns packages to channels and control their completion
 * Once the transmission is completed, forward the packet to the destination.
 * 
 * @author Jungmin Son
 * @author Rodrigo N. Calheiros
 * @since CloudSimSDN 1.0
 */
public abstract class NetworkOperatingSystem extends SimEntity {

	protected String physicalTopologyFileName; 
	protected Topology topology;

//	protected Hashtable<String, Channel> channelTable;
	protected List<Host> hosts;
	protected List<SDNHost> sdnhosts;
	protected List<Switch> switches= new ArrayList<Switch>();

	
	protected List<? extends Vm> vmList;

	protected Map<String, Integer> vmNameIdTable = new HashMap<String, Integer>();;
	Map<String, Integer> flowNameIdTable;
	public static Map<Integer, String> debugVmIdName = new HashMap<Integer, String>();
	public static Map<Integer, String> debugFlowIdName = new HashMap<Integer, String>();	
	
	
	// Resolution of the result.
	public static double minTimeBetweenEvents = 0.001;	// in sec
	public static int resolutionPlaces = 5;
	public static int timeUnit = 1;	// 1: sec, 1000: msec

	public NetworkOperatingSystem(String name) {
		super(name);				
//		this.channelTable = new Hashtable<String, Channel>();		
	}
	
	public static double getMinTimeBetweenNetworkEvents() {
	    return minTimeBetweenEvents* timeUnit;
	}
	
	public static double round(double value) {
		int places = resolutionPlaces;
	    if (places < 0) throw new IllegalArgumentException();

		if(timeUnit >= 1000) value = Math.floor(value*timeUnit);
		
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.CEILING);
	    return bd.doubleValue();
	}
		
	@Override
	public void startEntity() {}

	@Override
	public void shutdownEntity() {}
	
	@Override
	public void processEvent(SimEvent ev) {
		int tag = ev.getTag();
		
		switch(tag){
//			case OsmosisTags.SDN_INTERNAL_EVENT: 
//				internalFlowProcess(); 
//				break;

			default:
				System.out.println("NOS --> Unknown event received by "+super.getName()+". Tag:"+ev.getTag());
		}
	}	

//	protected void internalFlowProcess() {
//		if(updateFlowProcessing()) {
//			sendInternalEvent();
//		}
//	}
	
//	protected void sendInternalEvent() {
//		CloudSim.cancelAll(getId(), new PredicateType(OsmosisTags.SDN_INTERNAL_EVENT));
//		if(channelTable.size() != 0) {
//			// More to process. Send event again		
//			double delay = this.nextFinishTime();		
//			//Log.printLine(CloudSim.clock() + ": " + getName() + ".sendInternalEvent(): next finish time: "+ delay);
//			System.out.println(this.getName() + " khaled: " + CloudSim.clock());
//			send(this.getId(), delay, OsmosisTags.SDN_INTERNAL_EVENT);
//		}		
//	}
//
//	private double nextFinishTime() {
//		double earliestEft = Double.POSITIVE_INFINITY;
//		for(Channel ch:channelTable.values()){
//			
//			double eft = ch.nextFinishTime();
//			if (eft<earliestEft){
//				earliestEft=eft;
//			}
//		}
		
//		if(earliestEft == Double.POSITIVE_INFINITY) {
//			throw new IllegalArgumentException("NOS.nextFinishTime(): next finish time is infinite!");
//		}
//		return earliestEft;
//		
//	}

//	public boolean updateFlowProcessing() {		
//		boolean needSendEvent = false;			
//		LinkedList<Channel> completeChannels = new LinkedList<Channel>();
//		for(Channel ch:channelTable.values()){
//			boolean isCompleted = ch.updateFlowProcessing();						
//			needSendEvent = needSendEvent || isCompleted;
//			completeChannels.add(ch);
//		}
//		
//		if(completeChannels.size() != 0) {
//			updateChannel();
//			processCompleteFlows(completeChannels);		
//		}
//
//		return needSendEvent;
//	}
	
	protected void processCompleteFlows(List<Channel> channels){}
		
	public Map<String, Integer> getVmNameIdTable() {
		return this.vmNameIdTable;
	}
	public Map<String, Integer> getFlowNameIdTable() {
		return this.flowNameIdTable;
	}
	
//	protected Channel findChannel(int from, int to, int channelId) {
//		// check if there is a pre-configured channel for this application
//		Channel channel = channelTable.get(getKey(from,to, channelId));
//
//		if (channel == null) {
//			//there is no channel for specific flow, find the default channel for this link
//			channel = channelTable.get(getKey(from,to));
//		}
//		return channel;
//	}
	
//	protected void addChannel(int src, int dst, int chId, Channel ch) {
//		this.channelTable.put(getKey(src, dst, chId), ch);		
//		ch.initialize();
//		adjustAllChannels(); // all channel get an equal among of BW   		
//	}
	
//	private Channel removeChannel(String key) {
//		Channel ch = this.channelTable.remove(key);		
//		ch.terminate();
//		adjustAllChannels();	
//		return ch;
//	}
//		
//	protected void adjustAllChannels() {
//		for(Channel ch:this.channelTable.values()) {
//			ch.adjustSharedBandwidthAlongLink();				
//		}
//	}
//		
//	private void updateChannel() {
//		List<String> removeCh = new ArrayList<String>();  
//		for(String key:this.channelTable.keySet()) {
//			Channel ch = this.channelTable.get(key);
//			if(ch.getActiveTransmissionNum() == 0) {
//				// No more job in channel. Delete
//				removeCh.add(key);
//			}
//		}
//		
//		for(String key:removeCh) {
//			removeChannel(key);
//		}
//	}
//	
//	private String getKey(int origin, int destination) {
//		return origin+"-"+destination;
//	}
//	
//	protected String getKey(int origin, int destination, int appId) {
//		//System.out.println(getKey(origin,destination)+"-"+appId); 
//		// value --> 4-5--1
//		return getKey(origin,destination)+"-"+appId;
//	}



	public List<Host> getHostList() {
		return this.hosts;				
	}
	
	public List<Switch> getSwitchList() {
		return this.switches;
	}

	protected Vm findVm(int vmId) {
		for(Vm vm:vmList) {
			if(vm.getId() == vmId)
				return vm;
		}
		return null;
	}
	
	protected SDNHost findSDNHost(Host host) {
		for(SDNHost sdnhost:sdnhosts) {
			if(sdnhost.getHost().equals(host)) {
				return sdnhost;
			}
		}
		return null;
	}
	
	protected SDNHost findSDNHost(int vmId) {
		Vm vm = findVm(vmId);
		if(vm == null)
			return null;
		
		for(SDNHost sdnhost:sdnhosts) {
			if(sdnhost.getHost().equals(vm.getHost())) {
				return sdnhost;
			}
		}
		//System.err.println("NOS.findSDNHost: Host is not found for VM:"+ vmId);
		return null;
	}
	
	public int getHostAddressByVmId(int vmId) {
		Vm vm = findVm(vmId);
		if(vm == null) {
			Log.printLine(CloudSim.clock() + ": " + getName() + ": Cannot find VM with vmId = "+ vmId);
			return -1;
		}
		
		Host host = vm.getHost();
		SDNHost sdnhost = findSDNHost(host);
		if(sdnhost == null) {
			Log.printLine(CloudSim.clock() + ": " + getName() + ": Cannot find SDN Host with vmId = "+ vmId);
			return -1;
		}
		
		return sdnhost.getAddress();
	}
}
