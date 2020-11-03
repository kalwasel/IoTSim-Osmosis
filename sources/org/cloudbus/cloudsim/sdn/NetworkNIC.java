/*
 * Title:        CloudSimSDN
 * Description:  SDN extension for CloudSim
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2015, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.sdn;

import java.util.List;

/**
 * To represent Network Interface Controller (NIC)
 *  
 * @author Jungmin Son
 * @author Rodrigo N. Calheiros
 * @since CloudSimSDN 1.0
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since BigDataSDNSim 1.0
 */

public interface NetworkNIC {
	int getAddress();
//	public long getBandwidth();
	
	public void clearVMRoutingTable();
	public void addRoute(int srcVM, int destVM, int flowId, NetworkNIC to);	
	public NetworkNIC getVMRoute(int srcVM, int destVM, int flowId);	
	public void removeVMRoute(int srcVM, int destVM, int flowId);	

	public void addLink(Link l);
	public void setAdjancentNodes(List<NetworkNIC> nodes);
	public List<NetworkNIC> getAdjancentNodes();
	public void updateNetworkUtilization();
	
	public NetworkNIC updateVMRoute(int srcVM, int destVM, int flowId, NetworkNIC to);

	String getName();

	public void addRoute(NetworkNIC srcHost, NetworkNIC desthost, int flowId, NetworkNIC nextNode);
}
