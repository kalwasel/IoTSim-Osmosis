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

public class UDP extends NetworkMeasurement {	

	private double tranHeaderSize = 10; // transport header size =  20
	private double netHeaderSize = 12 ; // network header size  = 20
	private double dlHeaderSize = 12; // data link header size   = 26
	
	@Override
	public double getAveragePktSize() {
		// TODO Auto-generated method stub
		return averagePktSize;
	}


	@Override
	public void setAveragePktSize(double averagePktSize) {
		this.averagePktSize = averagePktSize;
		
	}


	@Override
	public double getAveragefrSize() {
		return averagefrSize;
	}


	@Override
	public void setAveragefrSize(double averagefrSize) {
		this.averagefrSize = averagefrSize;
	}
	

	@Override
	public double getTranHeaderSize() {
		return tranHeaderSize;
	}

	@Override
	public void setTranHeaderSize(double tranHeaderSize) {
		this.tranHeaderSize = tranHeaderSize;
	}

	@Override
	public double getNetHeaderSize() {
		return netHeaderSize;
	}

	@Override
	public void setNetHeaderSize(double netHeaderSize) {
		this.netHeaderSize = netHeaderSize;
	}

	@Override
	public double getDlHeaderSize() {
		return dlHeaderSize;
	}

	@Override
	public void setDlHeaderSize(double dlHeaderSize) {
		this.dlHeaderSize = dlHeaderSize;
	}

	@Override
	public double getDelays(){
		return procDelay + queDelay + propDelay + tranDelay;
	}

	@Override
	public double getTotalHeaders(){
		return dlHeaderSize + tranHeaderSize + netHeaderSize;
	}
	
}
