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
import java.util.HashMap;

import java.util.List;
import java.util.Map;


import org.cloudbus.cloudsim.sdn.Link;
import org.cloudbus.cloudsim.sdn.NetworkNIC;
import org.cloudbus.cloudsim.sdn.SDNHost;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;



import org.cloudbus.osmosis.core.Flow;
import org.cloudbus.osmosis.core.SDNRoutingTable;
/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since BigDataSDNSim 1.0
 */

public class SDNRoutingLoadBalancing extends SDNRoutingPolicy {
	int[][] nodeGraphDistance;
	double[][] nodeGraphBandwidth;
	Map<NetworkNIC, Integer> nodeToInt = new HashMap<>();
	Map<Integer, NetworkNIC> intToNode = new HashMap<>();
	Table<NetworkNIC, NetworkNIC, Link> selectedLink = HashBasedTable.create();


	protected Table<Integer, Integer, List<NetworkNIC>> path =  HashBasedTable.create(); // src, and dest
	protected Table<Integer, Integer, List<Link>> links =  HashBasedTable.create(); // srcvm and destvm

	public SDNRoutingLoadBalancing() {
		setPolicyName("ShortestPathMaxBw");
	}

	/*
	 * You must create a routing table if there is no previous communication bween srcVm and destVm
	 */
	
	public List<NetworkNIC> getRoute(int source, int dest){
		List<NetworkNIC> routeFound = path.get(source, dest);
		if(routeFound != null)
			return routeFound;
		
		return null;
	}
	
	public List<Link> getLinks(int source, int dest){
		List<Link> linksFound = links.get(source, dest);
		if(linksFound != null)
			return linksFound;
		
		return null;
	}

	private int minDistanceMaxBw(int distance[], double bandwdith[], Boolean visited[], int nodeNum)
	{
		int minDistance = Integer.MAX_VALUE;
		double maxBandwdith = Double.MIN_VALUE; // we do not care about the fraction of bw! 
		int minIndex = -1;

		for (int u = 0; u < nodeNum; u++){
			if (visited[u] == false){
				if(distance[u] <= minDistance && bandwdith[u] >= maxBandwdith){ // select the one with min distance and max BW				
					minDistance = distance[u];   
					maxBandwdith = bandwdith[u];
					minIndex = u;
				} 
				else if(distance[u] <= minDistance){ // select the one with min distance and discards max bw 
					minDistance = distance[u]; // select the one with max BW  
					maxBandwdith = bandwdith[u];
					minIndex = u;
				}
			}		
		}
//		NetworkNIC NewNode = intToNode.get(minIndex);
//		System.out.println("selected: " +  NewNode);
		return minIndex;
	}
	protected List<NetworkNIC> biuldRoute(int biultRoute[], NetworkNIC src, NetworkNIC dest,Flow pkt){
		List<NetworkNIC> nodeLists = new ArrayList<>();
		List<Link> linkList = new ArrayList<>();

		NetworkNIC currentNode = dest;
		NetworkNIC nextNode = null;

		boolean routeBuilt = false;
		/*
		 * Build from dest to src! 
		 * We cannot build from src to dest since src does not have previous node/vertex
		 */
		Link link = null;
		while(!routeBuilt){	
			nodeLists.add(currentNode);	
			if(currentNode.equals(src)){
				routeBuilt = true;			
			}
			int nodeIndex = biultRoute[nodeToInt.get(currentNode)];
			nextNode = intToNode.get(nodeIndex);
			link = selectedLink.get(currentNode, nextNode);
			//System.out.println(link.);
			//link = nodeToLink.get(currentNode);					
			linkList.add(link);		
			currentNode = intToNode.get(nodeIndex);			
		}		
		
		path.put(pkt.getOrigin(), pkt.getDestination(), nodeLists);
		links.put(pkt.getOrigin(), pkt.getDestination(), linkList);
		
		return nodeLists;
	}
	
	@Override
	public void updateSDNNetworkGraph() {
		int nodeSize = getNodeList().size();
		nodeGraphDistance = new int[nodeSize][nodeSize];
		nodeGraphBandwidth = new double[nodeSize][nodeSize];

		for(int i = 0; i< getNodeList().size();i++){
			NetworkNIC srcNode = getNodeList().get(i); 
			nodeToInt.put(srcNode, i);
			intToNode.put(i,srcNode);
			for(int k = 0; k < getNodeList().size(); k++){
					NetworkNIC destNode = getNodeList().get(k); 									
					nodeGraphDistance[i][k] = getDistanceWeight(srcNode, destNode);	// this can be used for link failure 
					nodeGraphBandwidth[i][k] = getBwWeight(srcNode, destNode);;
			}
		}
	}
	private int getDistanceWeight(NetworkNIC srcNode, NetworkNIC destNode){
		List<Link> links = topology.getNodeTONodelinks(srcNode, destNode);		
		if(links == null)
			return 0;
		
		return 1;
	}
	
	private double getBwWeight(NetworkNIC srcNode, NetworkNIC destNode){			
		List<Link> links = topology.getNodeTONodelinks(srcNode, destNode);
		double bw = 0;
		Link linkWithHighestBW = null;	
		// links == null, then nodes are not adjacent! 
		if(links == null)
			return 0;		
		/*
		 * Sometimes two nodes are connected via two links; therefore, find the max BW among the links! 
		 * 
		 */	
		int numberChannel;
		for(Link l : links){
			numberChannel = l.getChannelCount();
			if (numberChannel ==0 || srcNode instanceof SDNHost || destNode instanceof SDNHost){ // i think you may need to look the logic again!
				numberChannel = 1; // we cannot divide by 0
			} else {  
				numberChannel++; // 1 for exisiting one , and one for this one  
			}
			double currentBw = l.getBw()/numberChannel;  				
			if(currentBw > bw){
				// link bw does not change, instead you need to get the bw and number of channel on the link			
				bw = currentBw;  				
				linkWithHighestBW = l;
			}
		}
		selectedLink.put(srcNode, destNode, linkWithHighestBW); // you must store this one and return it to the SDN controller 
		return bw;
	}

	
	@Override
	public NetworkNIC getNode(SDNHost srcHost, NetworkNIC node, SDNHost desthost, String destApp) {
		return null;
	}


	@Override
	public List<NetworkNIC> buildRoute(NetworkNIC srcHost, NetworkNIC destHost, Flow pkt) {
//		System.out.println("Packet: " + pkt.getFlowId() + " - Find Shortest Path and Max BW between " + pkt.getAppNameSrc() +" and " + pkt.getAppNameDest() );
		updateSDNNetworkGraph();

		int graphSize  = nodeGraphDistance.length; // u
		
		int distance[] = new int[graphSize]; 
		double bandwidth[] = new double[graphSize]; 
		Boolean visited[] = new Boolean[graphSize];
		int previousNode[] = new int[graphSize]; 
		Map<Integer, List<NetworkNIC>> parent = new HashMap<>();
		List<NetworkNIC> listParent;
		for(int i =0; i < graphSize; i++){
		    distance[i] = Integer.MAX_VALUE; // to find min distance 
//			bandwidth[i] = Double.MIN_VALUE; // to find max bw
			bandwidth[i] = Double.MIN_VALUE; // to find max bw 
			visited[i] = false;
		}
		
		int nodeIndex = nodeToInt.get(srcHost); // to map nodes to their integer indexes 
		
		distance[nodeIndex] = 0; // Distance of a source node from itself is always 0
		bandwidth[nodeIndex] = -1; // Bandwidth of a source node from itself is always -1
		previousNode[nodeIndex] = -1;
		// Find shortest path for all vertices
		for (int count = 0; count < graphSize-1; count++)
		{
			int currentSelectedNode = minDistanceMaxBw(distance, bandwidth, visited, graphSize);			
			visited[currentSelectedNode] = true; // Mark the picked node as processed			

			for (int adjacentNode = 0; adjacentNode < graphSize; adjacentNode++){ // Update the distance and bw values of the adjacent vertices of the picked vertex						
				if(visited[adjacentNode] == false){ // has been visited and we check its distance to all other nodes whenever possible  
					if(nodeGraphDistance[currentSelectedNode][adjacentNode] != 0){ // u and i must be adjucent 
						if(distance[currentSelectedNode] != Integer.MAX_VALUE){ // if it is infinte, it means we selected the wrong node to biuld our routing from!
							if(distance[currentSelectedNode] + nodeGraphDistance[currentSelectedNode][adjacentNode] <= distance[adjacentNode]
									&& nodeGraphBandwidth[currentSelectedNode][adjacentNode] > bandwidth[adjacentNode]
									){ // distance i will infinte if it has not been reached by any other nodes
								distance[adjacentNode] = distance[currentSelectedNode] + nodeGraphDistance[currentSelectedNode][adjacentNode];
							    previousNode[adjacentNode] = currentSelectedNode; 	
								NetworkNIC parentNode = intToNode.get(currentSelectedNode);
								listParent  = parent.get(adjacentNode);
							    if(listParent == null){
							    	listParent = new ArrayList<>();
								    listParent.add(parentNode);
								    parent.put(adjacentNode, listParent);								    
							    }else{
								    listParent.add(parentNode);
								    parent.put(adjacentNode, listParent);
							    }					
									distance[adjacentNode] = distance[currentSelectedNode] + nodeGraphDistance[currentSelectedNode][adjacentNode];
									previousNode[adjacentNode] = currentSelectedNode; 								
									// you must select the least bw along the route to avoid congestion and packet loss
									bandwidth[adjacentNode] = nodeGraphBandwidth[currentSelectedNode][adjacentNode]; 
							} 
						}
					}
				}		
			}
		}
		
//		System.out.println(bandwidth);
		//List<Node> routeBuilt = biuldRoute(previousNode, srcHost, destHost);
		List<NetworkNIC> routeBuilt = biuldRoute(previousNode, srcHost, destHost, pkt);
//		System.out.println(routeBuilt);
		this.nodeGraphDistance = null;
		this.nodeGraphBandwidth = null;
		return routeBuilt;		
	}

	@Override
	public List<SDNRoutingTable> constructRoutes(NetworkNIC node, NetworkNIC desthost, NetworkNIC srcHost) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
