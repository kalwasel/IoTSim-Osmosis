/*
 * Title:         IoTSim-SDWAN 1.0
 * Description:  Ifacilitates the modeling, simulating, and evaluating of new algorithms, policies, and designs in the context of SD-WAN ecosystems and SDN-enabled multiple cloud datacenters.
 * 			     
 * 
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2020, Newcastle University (UK) and Saudi Electronic University (Saudi Arabia) 
 * 
 */
package org.cloudbus.cloudsim.sdwan;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-SDWAN 1.0
 * 
**/

public abstract class NetworkMeasurement {
	protected double procDelay = 0.0001; // Processing delay in seconds 
	protected double queDelay = 0.0001; // Queing delay in seconds 
	protected double propDelay = 0.0001; // propagation delay in seconds 
	protected double tranDelay = 0.0001; // transmission delay  in seconds
	
	protected double averagePktSize; // average payload size of packets  
	protected double averagefrSize; // average payload size of frames 
	
	public abstract double getAveragePktSize();
	public abstract void setAveragePktSize(double averagePktSize);
	
	public abstract  double getAveragefrSize();
	public abstract void setAveragefrSize(double averagefrSize);
	
	public abstract double getTranHeaderSize();	
	public abstract void setTranHeaderSize(double tranHeaderSize);
	
	public abstract double getNetHeaderSize();
	public abstract void setNetHeaderSize(double netHeaderSize);
	public abstract double getDlHeaderSize();
	public abstract void setDlHeaderSize(double dlHeaderSize);
		
	public abstract double getTotalHeaders();
	
	public double getDelays(){
		return procDelay + queDelay + propDelay + tranDelay;
	}
}
