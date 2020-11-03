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

package org.cloudbus.cloudsim.edge.iot.protocol;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public  abstract class IoTProtocol   {

    protected String name;    
    protected float batteryConsumptionSpeed;
    protected float transmissionSpeed;

    public IoTProtocol(String name,	float batteryConsumptionSpeed, float transmissionSpeed) {		
		this.name = name;	
		this.batteryConsumptionSpeed = batteryConsumptionSpeed;	
		this.transmissionSpeed = transmissionSpeed;
	}
	
	public float getBatteryDrainageRate() {
		return batteryConsumptionSpeed;
	}
	
	public String getProtocolName() {
		return this.name;
	}	
}