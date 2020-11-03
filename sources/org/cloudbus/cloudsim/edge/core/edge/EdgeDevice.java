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



import java.util.List;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.edge.utils.LogUtil;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public class EdgeDevice extends Host {
	
	private String deviceName;	
	private Battery battery;
	private int max_IoTDevice_capacity;	
	private double batterySensingRate;
	private double batterySendingRate;

	private boolean enabled;		
	
	public EdgeDevice(int id, String deviceName, RamProvisioner ramProvisioner, BwProvisioner bwProvisioner,
			long storage, List<? extends Pe> peList, VmScheduler vmScheduler,
			int max_IoTDevice_capacity, double max_battery_capacity,
			double batterySensingRate, double batterySendingRate, double current_battery_capacity) {
		
		super(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
		this.deviceName = deviceName;
		this.enabled = true;
		this.max_IoTDevice_capacity = max_IoTDevice_capacity;
		this.setBatterySensingRate(batterySensingRate);
		this.setBatterySendingRate(batterySendingRate);
		this.battery = new Battery();
	}
	

	public String getDeviceName() {
		return deviceName;
	}

	public double getCurrentBatteryCapacity() {
		return this.battery.getCurrentCapacity();
	}

	public double getMaxBatteryCapacity() {
		return this.battery.getMaxCapacity();
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getMax_IoTDevice_capacity() {
		return this.max_IoTDevice_capacity;
	}

	public void setMax_IoTDevice_capacity(int max_IoTDevice_capacity) {
		this.max_IoTDevice_capacity = max_IoTDevice_capacity;
	}


	public void update_geolocation() {

	}

	// TODO
	public void updateBatteryByProcessingCloudLet() {
		this.battery.setCurrentCapacity(this.battery.getCurrentCapacity() - this.battery.getBatterySendingRate());
		if (this.battery.getCurrentCapacity() <= 0) {
			LogUtil.info("Edge Device " + this.getId() + "( vm " + this.getVmList().get(0).getId() + " )"
					+ "'s battery has drained");
			this.setEnabled(false);
		}
	}

	public void updateBatteryByProcessingCloudLetAndSend(double fileSize, double shrinkFactor,
			double drangeRateForProcess, double drangeRateForSending) {

		double updateByProcess = fileSize * (1 - shrinkFactor) * drangeRateForProcess;
		double updateBySending = fileSize * shrinkFactor * drangeRateForSending;

		LogUtil.info("Edge Device " + this.getId() + " -  " + this.getVmList().get(0).getId() + " ( updateByProcess = "
				+ updateByProcess + " )" + " ( updateBySending = " + updateBySending + " )");
		this.battery.setCurrentCapacity(this.battery.getCurrentCapacity() - (updateByProcess + updateBySending));

		if (this.battery.getCurrentCapacity() <= 0) {
			LogUtil.info("Edge Device " + this.getId() + "( vm " + this.getVmList().get(0).getId() + " )"
					+ "'s battery has drained");
			this.setEnabled(false);
			CloudSim.terminateSimulation();
		}

	}

	public void updateBatteryByProcessingCloudLetAndSend2(double fileSize, double shrinkFactor,
			double drangeRateForProcess, double drangeRateForSending) {
		double updateByProcess = fileSize * (1 - shrinkFactor) * drangeRateForProcess;
		double updateBySending = fileSize * shrinkFactor * drangeRateForSending;

		LogUtil.info("Edge Device " + this.getId() + " -  " + this.getVmList().get(0).getId() + " ( updateByProcess = "
				+ updateByProcess + " )" + " ( updateBySending = " + updateBySending + " )");
		this.battery.setCurrentCapacity(this.battery.getCurrentCapacity() - (updateByProcess + updateBySending));

		if (this.battery.getCurrentCapacity() <= 0) {
			LogUtil.info("Edge Device " + this.getId() + "( vm " + this.getVmList().get(0).getId() + " )"
					+ "'s battery has drained");
			this.setEnabled(false);
		}
	}

	public double getBatterySensingRate() {
		return batterySensingRate;
	}

	public void setBatterySensingRate(double batterySensingRate) {
		this.batterySensingRate = batterySensingRate;
	}

	public double getBatterySendingRate() {
		return batterySendingRate;
	}

	public void setBatterySendingRate(double batterySendingRate) {
		this.batterySendingRate = batterySendingRate;
	}
}
