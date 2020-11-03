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

package org.cloudbus.cloudsim.edge.iot.network;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public class EdgeNetwork {
	private double speed;
	private String networkType;

	public EdgeNetwork(String networkType){
		this.networkType = networkType;
		setEdgeNetworkType(networkType);
	}
	
	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}
	
	private void setEdgeNetworkType(String networkType){		
		switch (networkType) {
		case "wifi":
			speed = 100; // Mbps
			break;
		case "wlan":
			speed = 100; // Mbps
			break;
		case "4g":
			speed = 100; // Mbps
			break;
		case "3g":
			speed = 100; // Mbps
			break;
		case "bluetooth":
			speed = 100; // Mbps
			break;
		case "lan":
			speed = 100; // Mbps
			break;				
		}
	}
		
	public double getSpeedRate(){
		return this.speed;
	}
}
