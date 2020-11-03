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
import org.cloudbus.osmosis.core.Flow;
import org.cloudbus.osmosis.core.SDNRoutingTable;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;


/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since BigDataSDNSim 1.0
 */


public class SDNRoutingTraditionalShortestPath extends SDNRoutingPolicy {
	int[][] nodeGraphDistance;
	Map<NetworkNIC, Integer> nodeToInt = new HashMap<>();
	Map<Integer, NetworkNIC> intToNode = new HashMap<>();
	Table<NetworkNIC, NetworkNIC, Link> selectedLink = HashBasedTable.create(); 	

	protected Map<Flow, List<NetworkNIC>> path = new HashMap<Flow, List<NetworkNIC>>(); // srcvm and destvm
	protected Map<Flow, List<Link>> links = new HashMap<Flow, List<Link>>(); // srcvm and destvm
	protected Table<Integer, Integer, List<NetworkNIC>> path2 =  HashBasedTable.create(); // src, and dest
	protected Table<Integer, Integer, List<Link>> links2 =  HashBasedTable.create(); // srcvm and destvm

	
	
	public SDNRoutingTraditionalShortestPath() {
		setPolicyName("ShortestPathFirst");
	}

	/*
	 * You must create a routing table if there is no previous communication between srcVm and destVm
	 */
	
	public List<NetworkNIC> getRoute(Flow pkt){
		//List<Node> routeFound = route.get(srcHost, destHost);
		List<NetworkNIC> routeFound = path.get(pkt);
		if(routeFound != null)
			return routeFound;
		
		return null;
	}
	public List<Link> getLinks(Flow pkt){
		List<Link> linksFound = links.get(pkt);
		if(linksFound != null)
			return linksFound;
		
		return null;
	}


	
	private int minDistanceMaxBw(int distance[], Boolean visited[], int nodeNum)
	{
		int minDistance = Integer.MAX_VALUE;
		int minIndex = -1;

		for (int u = 0; u < nodeNum; u++){
			if (visited[u] == false){
				if(distance[u] <= minDistance){ // select the one with min distance and discards max bw 
					minDistance = distance[u]; // select the one with max BW  
					minIndex = u;
				}
			}		
		}
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
		path2.put(pkt.getOrigin(), pkt.getDestination(), nodeLists);
		links2.put(pkt.getOrigin(), pkt.getDestination(), linkList);


		path.put(pkt, nodeLists);
		links.put(pkt, linkList);	
		return nodeLists;
	}
	
	@Override
	public void updateSDNNetworkGraph() {
		int nodeSize = getNodeList().size();
		nodeGraphDistance = new int[nodeSize][nodeSize];
		for(int i = 0; i< getNodeList().size();i++){
			NetworkNIC srcNode = getNodeList().get(i); 
			nodeToInt.put(srcNode, i);
			intToNode.put(i,srcNode);
			for(int k = 0; k < getNodeList().size(); k++){
					NetworkNIC destNode = getNodeList().get(k); 									
					nodeGraphDistance[i][k] = getDistanceWeight(srcNode, destNode);	// this can be used for link failure 
					
			}
		}
	}
	private int getDistanceWeight(NetworkNIC srcNode, NetworkNIC destNode){
		List<Link> links = topology.getNodeTONodelinks(srcNode, destNode);		

		if(links == null)
			return 0;
		
		selectedLink.put(srcNode, destNode, links.get(0)); // you must store this one and return it to the SDN controller 
		return 1;
	}
	
	@Override
	public NetworkNIC getNode(SDNHost srcHost, NetworkNIC node, SDNHost desthost, String destApp) {
		return null;
	}

	@Override
	public List<NetworkNIC> buildRoute(NetworkNIC srcHost, NetworkNIC destHost, Flow pkt) {
		updateSDNNetworkGraph();

		int graphSize  = nodeGraphDistance.length; // u
		
		int distance[] = new int[graphSize]; 
		Boolean visited[] = new Boolean[graphSize];
		int previousNode[] = new int[graphSize]; 
		
		for(int i =0; i < graphSize; i++){
		    distance[i] = Integer.MAX_VALUE; // to find min distance 
			visited[i] = false;
		}
		
		int nodeIndex = nodeToInt.get(srcHost); // to map nodes to their integer indexes 
		
		distance[nodeIndex] = 0; // Distance of source vertex from itself is always 0
		previousNode[nodeIndex] = -1;
		// Find shortest path for all vertices
		for (int count = 0; count < graphSize-1; count++)
		{
			int u = minDistanceMaxBw(distance, visited, graphSize);			
			visited[u] = true; // Mark the picked vertex as processed			

			for (int i = 0; i < graphSize; i++){ // Update the distance and bw values of the adjacent vertices of the picked vertex						
				if(visited[i] == false){ // has been visited and we check its distance to all other nodes whenever possible  
					if(nodeGraphDistance[u][i] != 0){ // u and i must be adjucent 
						if(distance[u] != Integer.MAX_VALUE){ // if it is infinte, it means we selected the wrong node to biuld our routing from!
							if(distance[u] + nodeGraphDistance[u][i] < distance[i]){ // distance i will infinte if it has not been reached by any other nodes
								distance[i] = distance[u] + nodeGraphDistance[u][i];
							    previousNode[i] = u; 	
							} 
						}
					}
				}		
			}
		}
		List<NetworkNIC> routeBuilt = biuldRoute(previousNode, srcHost, destHost, pkt);
		System.out.println(routeBuilt);
		return routeBuilt;		
	}

	public List<NetworkNIC> getRoute(int source, int dest){
		List<NetworkNIC> routeFound = path2.get(source, dest);
		if(routeFound != null)
			return routeFound;
		
		return null;
	}

	@Override
	public List<Link> getLinks(int source, int dest){
		List<Link> linksFound = links2.get(source, dest);
		if(linksFound != null)
			return linksFound;
		
		return null;
	}

	@Override
	public List<SDNRoutingTable> constructRoutes(NetworkNIC node, NetworkNIC desthost, NetworkNIC srcHost) {
		// TODO Auto-generated method stub
		return null;
	}

}
