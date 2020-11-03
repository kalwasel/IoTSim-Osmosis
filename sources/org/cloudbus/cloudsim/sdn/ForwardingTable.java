/*
 * Title:        CloudSimSDN
 * Description:  SDN extension for CloudSim
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2015, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.sdn;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;


/**
 * ForwardingRule class is to represent a forwarding table in each switch.
 * This is for VM routing, not host routing. Addresses used here are the addresses of VM.
 *  
 * @author Jungmin Son
 * @author Rodrigo N. Calheiros
 * @since CloudSimSDN 1.0
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since BigDataSDNSim 1.0
 */
 
public class ForwardingTable {
	
	Table<Integer, Integer, Map<Integer,NetworkNIC>> table;
	
	Table<Integer, Integer, Map<Integer,NetworkNIC>> endToEndtable; 
	
	public ForwardingTable(){
		this.table = HashBasedTable.create();		
		this.endToEndtable = HashBasedTable.create();
	}
	
	public void clear(){
		table.clear();
	}
	
	public void addRule(int src, int dest, int flowId, NetworkNIC to){
		Map<Integer, NetworkNIC> map = table.get(src, dest);		
		if(map == null){
			map = new HashMap<Integer, NetworkNIC>();
		}		
		map.put(flowId, to);		
		table.put(src, dest, map);

		Map<Integer, NetworkNIC> flowMap = endToEndtable.get(flowId, src);
		if(flowMap == null){
			flowMap = new HashMap<Integer, NetworkNIC>();
		}
		flowMap.put(dest, to);
		endToEndtable.put(flowId, src, flowMap);
	}
	
	public void removeRule(int src, int dest, int flowId){
		Map<Integer, NetworkNIC> map = table.get(src, dest);

		if(map == null)
			return;
		map.remove(flowId);
		if(map.isEmpty())
			table.remove(src, dest);
		else
			table.put(src, dest, map);
	}

	public NetworkNIC getRoute(int src, int dest, int flowId) {
		Map<Integer, NetworkNIC> map = table.get(src, dest);
		if(map==null)
			return null;
		
		return map.get(flowId);
	}
}
