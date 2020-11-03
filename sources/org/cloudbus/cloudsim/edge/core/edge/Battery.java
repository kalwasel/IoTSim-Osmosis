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

package org.cloudbus.cloudsim.edge.core.edge;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public class Battery {
	private double maxCapacity;
	private double currentCapacity;
	private double batterySensingRate;
	private double batterySendingRate;

	
	public double getMaxCapacity() {
		return maxCapacity;
	}
	
	public void setMaxCapacity(double maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public double getBatterySensingRate() {
		return batterySensingRate;
	}

	public void setBatterySensingRate(double batterySensingRate) {
		this.batterySensingRate = batterySensingRate;
	}
	
	public void setBatterySendingRate(double batterySendingRate) {
		this.batterySendingRate = batterySendingRate;
	}
	
	public double getBatterySendingRate() {
		return batterySendingRate;
	}
	
	public double getCurrentCapacity() {
		return currentCapacity;
	}
	public void setCurrentCapacity(double currentCapacity) {
		this.currentCapacity = currentCapacity;
	}
	
	public double getBatteryTotalConsumption(){
		if(this.currentCapacity < 0){
			this.currentCapacity = 0;
		}
		double consum = this.maxCapacity - this.currentCapacity;
		return consum;
	}
}
