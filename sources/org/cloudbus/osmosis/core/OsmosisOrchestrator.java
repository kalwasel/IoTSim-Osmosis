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
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.core.predicates.PredicateType;
import org.cloudbus.cloudsim.sdn.Channel;
import org.cloudbus.cloudsim.sdn.Link;
import org.cloudbus.cloudsim.sdn.NetworkNIC;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public class OsmosisOrchestrator extends SimEntity {

	public static List<Flow> flowList = new ArrayList<>();
	private List<CloudDatacenter> datacentres;
    private List<SDNController> controllers;

	protected Hashtable<String, Channel> channelTable;
	
	private List<Channel> channelsHistory = new ArrayList<>();
	
    public void setDatacenters(List<CloudDatacenter> datacentres){
    	this.datacentres = datacentres;
	
    }
    
    public List<CloudDatacenter> getDatacentres() {
        return datacentres;
    }	
	
	public OsmosisOrchestrator() {
		super("Osmesis_Orchestrator");
		this.channelTable = new Hashtable<String, Channel>();	
		// TODO Auto-generated constructor stub
	}
			
	public List<SDNController> getSdnControllers() {
		return this.controllers;
	}
	
	public void setSdnControllers(List<SDNController> sdnControllers) {
		this.controllers= sdnControllers;
	}
		
	public void findDest(){
	   
   }

	@Override
	public void startEntity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processEvent(SimEvent ev) {
		int tag = ev.getTag();
		
		switch(tag){		
		case OsmosisTags.START_TRANSMISSION:		
			Flow flow = (Flow) ev.getData();			
			transmitFlow(flow);
			break;
			case OsmosisTags.SDN_INTERNAL_EVENT: 
			internalFlowProcess(); 
			break;
		default: 
			System.out.println(this.getName() + ": Unknown event received by "+super.getName()+". Tag:"+ev.getTag());
			break;
		}
	}

	private void transmitFlow(Flow flow) {		
		createChannel(flow);
	}
	
	protected void createChannel(Flow flow) { 	
		flowList.add(flow);
		flow.setStartTime(CloudSim.clock());
		int flowId = flow.getFlowId();			
		updateFlowProcessing();		
		Channel channel = flow.getChannel(); 
		int src = flow.getOrigin(); 
		int dst = flow.getDestination();

		if(channel == null) {	
			List<NetworkNIC> nodes = flow.getNodeOnRouteList();			
			List<Link> links = flow.getLinkList();			
			channel = new Channel(flowId, src, dst, nodes, links);
		}
				
		this.channelTable.put(getKey(src, dst, flowId), channel);		
		channel.initialize();
		adjustAllChannels(); // all channel get an equal among of BW   		
		
		this.channelsHistory.add(channel);
		channel.addFlowToList(flow);			
		channel.addTransmission(flow);

		sendInternalEvent();	
	}
	
	protected void internalFlowProcess() {
		if(updateFlowProcessing()) {
			sendInternalEvent();
		}
	}
	
	public boolean updateFlowProcessing() {		
		boolean needSendEvent = false;			
		LinkedList<Channel> completeChannels = new LinkedList<Channel>();
		for(Channel ch:channelTable.values()){
			boolean isCompleted = ch.updateFlowProcessing();						
			needSendEvent = needSendEvent || isCompleted;
			completeChannels.add(ch);
		}
		
		if(completeChannels.size() != 0) {
			updateChannel();
			processCompleteFlows(completeChannels);		
		}

		return needSendEvent;
	}
	
	protected void processCompleteFlows(List<Channel> channels){
		for(Channel ch:channels) {												
			for (Flow flow : ch.getFinishedFlows()){
				removeCompletedFlows(flow);
				flow.setTransmissionTime(CloudSim.clock());
			}
		}
	}
	
	protected void removeCompletedFlows(Flow flow ){				
		flow.setFinishTime(CloudSim.clock());				
		sendNow(OsmesisBroker.brokerID, OsmosisTags.Transmission_SDWAN_ACK, flow);							
	}
	
	private Channel removeChannel(String key) {
		Channel ch = this.channelTable.remove(key);		
		ch.terminate();
		adjustAllChannels();	
		return ch;
	}
		
	protected void adjustAllChannels() {
		for(Channel ch:this.channelTable.values()) {
			ch.adjustSharedBandwidthAlongLink();				
		}
	}
		
	private void updateChannel() {
		List<String> removeCh = new ArrayList<String>();  
		for(String key:this.channelTable.keySet()) {
			Channel ch = this.channelTable.get(key);
			if(ch.getActiveTransmissionNum() == 0) {
				// No more job in channel. Delete
				removeCh.add(key);
			}
		}
		
		for(String key:removeCh) {
			removeChannel(key);
		}
	}
	
	private String getKey(int origin, int destination) {
		return origin+"-"+destination;
	}
	
	protected String getKey(int origin, int destination, int appId) {
		//System.out.println(getKey(origin,destination)+"-"+appId); 
		// value --> 4-5--1
		return getKey(origin,destination)+"-"+appId;
	}
	
	protected Channel findChannel(int from, int to, int channelId) {
		// check if there is a pre-configured channel for this application
		Channel channel = channelTable.get(getKey(from,to, channelId));

		if (channel == null) {
			//there is no channel for specific flow, find the default channel for this link
			channel = channelTable.get(getKey(from,to));
		}
		return channel;
	}
	
	protected void sendInternalEvent() {
		CloudSim.cancelAll(getId(), new PredicateType(OsmosisTags.SDN_INTERNAL_EVENT));
		if(channelTable.size() != 0) {
			// More to process. Send event again		
			double delay = this.nextFinishTime();					
			send(this.getId(), delay, OsmosisTags.SDN_INTERNAL_EVENT);
		}		
	}

	private double nextFinishTime() {
		double earliestEft = Double.POSITIVE_INFINITY;
		for(Channel ch:channelTable.values()){
			
			double eft = ch.nextFinishTime();
			if (eft<earliestEft){
				earliestEft=eft;
			}
		}
		
		if(earliestEft == Double.POSITIVE_INFINITY) {
			throw new IllegalArgumentException("NOS.nextFinishTime(): next finish time is infinite!");
		}
		return earliestEft;
		
	}

	@Override
	public void shutdownEntity() {
		// TODO Auto-generated method stub
		
	}
}
