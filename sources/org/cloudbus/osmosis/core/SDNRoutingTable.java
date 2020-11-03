/*
 * Title:        BigDataSDNSim 1.0
 * Description:  BigDataSDNSim enables the simulating of MapReduce, big data management systems (YARN), 
 * 				 and software-defined networking (SDN) within cloud environments.
 * 
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2020, Newcastle University (UK) and Saudi Electronic University (Saudi Arabia) 
 * 
 */

package org.cloudbus.osmosis.core;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.sdn.Link;
import org.cloudbus.cloudsim.sdn.NetworkNIC;
import org.cloudbus.cloudsim.sdn.SDNHost;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since BigDataSDNSim 1.0
 */

public class SDNRoutingTable {

	int srcVmID; 
	int destVmId; 
	NetworkNIC srcHost;
	NetworkNIC destHost;
	List<NetworkNIC> routes;  
	List<List<NetworkNIC>> routesList; 
	Multimap<Integer, List<NetworkNIC>> routeMap;
	Table<Integer, Integer, List<Link>> links =  HashBasedTable.create(); 
	
	
	public SDNRoutingTable(NetworkNIC srcHost, NetworkNIC destHost, List<NetworkNIC> routes) {		
		this.srcHost = srcHost;
		this.destHost = destHost;
		this.routes = routes;
		routesList = new ArrayList<List<NetworkNIC>>();
		routeMap = HashMultimap.create();
	}	
	public List<NetworkNIC> getRoute(NetworkNIC src, NetworkNIC dest){
		
		if(srcHost.equals(src) && destHost.equals(dest)){
			return routes;	
		}		
		return null;
	}
	
	public SDNRoutingTable getTable(NetworkNIC src, NetworkNIC dest){
		if(srcHost.equals(src) && destHost.equals(dest)){
			return this;	
		}		
		return null;
	}
		
	public void updateSDNTable(List<NetworkNIC> route){
		this.routes = route;
	}
	
	public void addSDNMutipleRoutes(List<NetworkNIC> route){
		List<NetworkNIC> nodes = new ArrayList<>();
		nodes = route;		
		this.routesList.add(nodes);		
	}
	public void addLinks(int src, int dest, List<Link> links) {
		this.links.put(src, dest, links);
	}	
	
	public List<Link> getLinks(int src, int dest){
		return links.get(src, dest);
	}
}
