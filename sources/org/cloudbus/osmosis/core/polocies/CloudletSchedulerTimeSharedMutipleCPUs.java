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

import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.ResCloudlet;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since BigDataSDNSim 1.0
 */

public class CloudletSchedulerTimeSharedMutipleCPUs extends CloudletSchedulerTimeShared {

	private int requiredCPUs;
	
	protected double getCapacity(List<Double> mipsShare) {
		double capacity = 0.0;
		int cpus = 0;
		for (Double mips : mipsShare) {
			capacity += mips;
			if (mips > 0.0) {
				cpus++;
			}
		}
		currentCPUs = cpus;

		int pesInUse = 0;
		for (ResCloudlet rcl : getCloudletExecList()) {
			pesInUse += rcl.getNumberOfPes();
		}

		if (pesInUse > currentCPUs) {
			capacity /= pesInUse;
		} else {
			/*
			 * Use requiredCPUs to allow the cloudlet to be executed on more than one CPU
			 */
			double mipsPerCPU = capacity / currentCPUs;
			capacity =  mipsPerCPU * requiredCPUs; 
		}
		return capacity;
	}
	
	@Override
	public double cloudletSubmit(Cloudlet cloudlet, double fileTransferTime) {		
//		requiredCPUs = task.getCPUNo(); // requiredCPUs for every mapper and reducer 
		requiredCPUs = 1; // requiredCPUs for every mapper and reducer
		ResCloudlet rcl = new ResCloudlet(cloudlet);
		rcl.setCloudletStatus(Cloudlet.INEXEC);
		for (int i = 0; i < cloudlet.getNumberOfPes(); i++) {
			rcl.setMachineAndPeId(0, i);
		}
		getCloudletExecList().add(rcl);
		double extraSize = getCapacity(getCurrentMipsShare()) * fileTransferTime;		
		double length = (double) (cloudlet.getCloudletLength() + extraSize);
		cloudlet.setCloudletLength(length);
		return cloudlet.getCloudletLength() / getCapacity(getCurrentMipsShare());
	}
	
}
