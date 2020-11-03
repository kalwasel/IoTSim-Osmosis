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

package org.cloudbus.osmosis.core.polocies;

import java.util.ArrayList;
import java.util.Collection;

import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.sdn.Link;
import org.cloudbus.cloudsim.sdn.NetworkNIC;
import org.cloudbus.cloudsim.sdn.SDNHost;
import org.cloudbus.osmosis.core.Flow;
import org.cloudbus.osmosis.core.SDNRoutingTable;
import org.cloudbus.osmosis.core.Topology;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since BigDataSDNSim 1.0
 */

public abstract class SDNRoutingPolicy {
	private List<NetworkNIC> nodeList = new ArrayList<NetworkNIC>();
	protected List<SDNRoutingTable> sdnRoutingTables = new ArrayList<>();
	protected Topology topology;
	private String policyName;

	public abstract NetworkNIC getNode(SDNHost srcHost, NetworkNIC node, SDNHost desthost, String destApp);
	public abstract void updateSDNNetworkGraph();	
	public abstract List<NetworkNIC> buildRoute(NetworkNIC srcHost, NetworkNIC destHost, Flow pkt);
	public abstract List<NetworkNIC> getRoute(int source, int dest);
	public abstract List<Link> getLinks(int source, int dest);
	public abstract  List<SDNRoutingTable> constructRoutes(NetworkNIC node, NetworkNIC desthost, NetworkNIC srcHost);

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public void buildNodeRelations(Topology topology) {	
		for (NetworkNIC nd : getNodeList()) {
			List<NetworkNIC> adjuNodes = new ArrayList<>();
			List<Link> nodeAdjacentLinks = new ArrayList<>();
			// get all adjacent links of nd
			nodeAdjacentLinks.addAll(topology.getAdjacentLinks(nd));
			for (Link l : nodeAdjacentLinks) {
				NetworkNIC node = l.getOtherNode(nd);
				if (!adjuNodes.contains(node)) {
					adjuNodes.add(node);
				}
			}
			// remove nd from the list because it cannot be adjacent to itself!
			nd.setAdjancentNodes(adjuNodes);
		}
	}
	protected Multimap<NetworkNIC, LinkedList<NetworkNIC>> nodesOnRoute = HashMultimap.create();

	public List<NetworkNIC> getNodeList() {
		return nodeList;
	}
	
	public void setNodeList(Collection<NetworkNIC> nodeList, Topology topology) {
		this.nodeList.addAll(nodeList);
		this.topology = topology;
	}
	
	
	public SDNRoutingTable getSDNRoutingTable(NetworkNIC srcHost, NetworkNIC desthost) {			
		SDNRoutingTable  table = null;
		for (SDNRoutingTable  tb : sdnRoutingTables){
			table = tb.getTable(srcHost, desthost);
		}
		return table;
	}
	
	public List<SDNRoutingTable> getSDNRoutingTables(NetworkNIC srcHost, NetworkNIC desthost) {			
		List<SDNRoutingTable>  table = new ArrayList<>();
		for (SDNRoutingTable  tb : sdnRoutingTables){
			SDNRoutingTable tab = tb.getTable(srcHost, desthost);
			if(tab != null){
				table.add(tab);
			}
		}
		return table;
	}	
}
