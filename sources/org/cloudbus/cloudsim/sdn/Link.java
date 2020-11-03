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


/**
 * This is physical link between hosts and switches to build physical topology.
 * Links have latency and bandwidth.
 *  
 * @author Jungmin Son
 * @author Rodrigo N. Calheiros
 * @since CloudSimSDN 1.0
 * 
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since BigDataSDNSim 1.0
 */

public class Link {
	// bi-directional link (one link = both ways)
	NetworkNIC highOrder; // order starts from core SW, to aggr SW, to edge SW, to Hosts
	NetworkNIC lowOrder;  // order starts from Hosts, to edge SW, to aggr SW, to core SW
	double bw;
	
	
	double availableBW; // if it is 0 for all links, choose any one randomly
	
	private List<Channel> upChannels;
	private List<Channel> downChannels;

	private List<Channel> allChannels;

	public Link(NetworkNIC highOrder, NetworkNIC lowOrder, double bw) {
		this.highOrder = highOrder;
		this.lowOrder = lowOrder;
		// bw = 10^9 = 1.0E9 = 1000000000
//		this.upBW = this.downBW = bw;
		this.bw = bw;
		this.availableBW = bw;
		
		this.upChannels = new ArrayList<Channel>();
		this.downChannels = new ArrayList<Channel>();

		this.allChannels = new ArrayList<Channel>();
	}

	public NetworkNIC getHighOrder() {
		return highOrder;
	}

	public NetworkNIC getLowOrder() {
		return lowOrder;
	}
	
	public NetworkNIC getOtherNode(NetworkNIC from) {
		if(highOrder.equals(from))
			return lowOrder;
		
		return highOrder;
	}
	
	public double getBw() {
		return bw;
	}
	

	public int getChannelCount() {
		return this.allChannels.size();
	}

	public boolean addChannel(Channel ch) {
		allChannels.add(ch);
		return true;
	}
	
	public boolean removeChannel(Channel ch) {
		boolean ret = this.allChannels.remove(ch);
		return ret;
	}
	
	public double getFreeBandwidth() {
		double freeBw = this.availableBW/getChannelCount();		
		return freeBw;
	}
	
	public int getChannelNo() {				
		return getChannelCount();
	}

	
	public boolean isActive() {
		if(this.upChannels.size() >0 || this.downChannels.size() >0)
			return true;
		if (allChannels.size() > 0)
			return true;
		
		return false;
		
	}
	
}
