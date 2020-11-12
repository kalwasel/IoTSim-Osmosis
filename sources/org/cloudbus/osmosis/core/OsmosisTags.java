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

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public class OsmosisTags {
	
	private static final int Osmosis_BASE = 80000000;

	public static final int SDN_INTERNAL_EVENT = Osmosis_BASE + 1; 	
	public static final int MAP_REDUCE_CREATE = Osmosis_BASE + 2;	
	public static final int MAPPER_FINISHED_EXECUTION = Osmosis_BASE + 3;
	public static final int REDUCER_FINISHED_EXECUTION = Osmosis_BASE + 4;	
	public static final int BUILD_ROUTE = Osmosis_BASE + 5;	
	public static final int EXECUTE_REDUCER_CLOUDLET = Osmosis_BASE + 6;	
	public static final int NODE_MANAGER_INTERNAL_HEART_BEAT = Osmosis_BASE + 7;	
	public static final int INTERNAL_MAPPER_HEART_BEAT = Osmosis_BASE + 8;	
	public static final int INTERNAL_REDUCER_HEART_BEAT = Osmosis_BASE + 9;	
	public static final int EXECUTE_MAP_TASK = Osmosis_BASE + 10;	
	public static final int EXECUTE_REDUCE_TASK = Osmosis_BASE + 11;
	public static final int SDN_INTERNAL_LOAD_BALANCING = Osmosis_BASE + 12;
	public static final int INTERNAL_SDN_BW_Reservation = Osmosis_BASE + 13;
	public static final int TRANSFER_BLOCKS =  Osmosis_BASE + 14;	
	public static final int Transmission_ACK = Osmosis_BASE + 15;
	public static final int MAP_REDUCE_APP_FINISHED = Osmosis_BASE + 16;
	public static final int PKT_TRANSMISSION_ACK =  Osmosis_BASE + 17;
	public static final int TRANSMIT_IOT_DATA = Osmosis_BASE + 18;
	public static final int INTERNAL_EVENT = Osmosis_BASE + 19;
	public static final int START_TRANSMISSION = Osmosis_BASE + 20;
	public static final int Transmission_SDWAN_ACK = Osmosis_BASE + 21;
	public static final int GENERATE_OSMESIS = Osmosis_BASE + 22;
	public static final int BUILD_ROUTE_GREEN = Osmosis_BASE + 23;	

	
	public static final int SENSING = Osmosis_BASE + 24;
	public static final int MOVING = Osmosis_BASE + 25;
	public static final int updateIoTBW = Osmosis_BASE + 26;

}
