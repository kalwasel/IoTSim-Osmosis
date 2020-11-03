/*
 * Title:        CloudSimSDN
 * Description:  SDN extension for CloudSim
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2015, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.osmesis.examples.uti;

import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.sdn.SDNHost;
//import org.cloudbus.cloudsim.sdn.Activity;
//import org.cloudbus.cloudsim.sdn.Processing;
//import org.cloudbus.cloudsim.sdn.Request;
import org.cloudbus.cloudsim.sdn.Switch;
//import org.cloudbus.cloudsim.sdn.Transmission;
import org.cloudbus.cloudsim.sdn.Switch.HistoryEntry;
import org.cloudbus.cloudsim.sdn.power.PowerUtilizationHistoryEntry;
import org.cloudbus.cloudsim.sdn.power.PowerUtilizationInterface;

/**
 * This class is to print out logs into console.
 * 
 * @author Jungmin Son
 * @since CloudSimSDN 1.0
 */
public class LogPrinter {
	public static void printEnergyConsumption(String dcName, List<SDNHost> hostList, List<Switch> switchList, double finishTime) {
		double hostEnergyConsumption = 0, switchEnergyConsumption = 0;
		if(hostList != null){
		Log.printLine("========== Host Power Consumption ===========");		
			for(SDNHost sdnHost:hostList) {
				Host host = sdnHost.getHost();
				PowerUtilizationInterface scheduler =  (PowerUtilizationInterface) host.getVmScheduler();
				scheduler.addUtilizationEntryTermination(finishTime);
				
				double energy = scheduler.getUtilizationEnergyConsumption();
				Log.printLine(dcName + ": " + sdnHost.getName() +": "+energy);
				hostEnergyConsumption+= energy;		
//				printHostUtilizationHistory(scheduler.getUtilizationHisotry());		
			}
		}

		Log.printLine("========== Switch Power Consumption ===========");
		for(Switch sw:switchList) {
			sw.addUtilizationEntryTermination(finishTime);
			double energy = sw.getUtilizationEnergyConsumption();
			Log.printLine(dcName + ": " + sw.getName()+": "+energy);
			switchEnergyConsumption+= energy;

//			printSwitchUtilizationHistory(sw.getUtilizationHisotry());

		}
		Log.printLine("========== Total Power Consumption ===========");
		Log.printLine("Host energy consumed: "+hostEnergyConsumption);
		Log.printLine("Switch energy consumed: "+switchEnergyConsumption);
		Log.printLine("Total energy consumed: "+(hostEnergyConsumption+switchEnergyConsumption));
		
	}

	private static void printHostUtilizationHistory(
			List<PowerUtilizationHistoryEntry> utilizationHisotry) {
		if(utilizationHisotry != null)
			for(PowerUtilizationHistoryEntry h:utilizationHisotry) {
				Log.printLine(h.startTime+", "+h.usedMips);
			}
	}
	private static void printSwitchUtilizationHistory(List<HistoryEntry> utilizationHisotry) {
		if(utilizationHisotry != null)
			for(HistoryEntry h:utilizationHisotry) {
				Log.printLine(h.startTime+", "+h.numActivePorts);
			}
	}
	
	static public String indent = ",";
	static public String tabSize = "10";
	static public String fString = 	"%"+tabSize+"s"+indent;
	static public String fInt = 	"%"+tabSize+"d"+indent;
	static public String fFloat = 	"%"+tabSize+".3f"+indent;
	
	public static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;

		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		
		Log.print(String.format(fString, "Cloudlet_ID"));
		Log.print(String.format(fString, "STATUS" ));
		Log.print(String.format(fString, "DataCenter_ID"));
		Log.print(String.format(fString, "VM_ID"));
		Log.print(String.format(fString, "Length"));
		Log.print(String.format(fString, "Time"));
		Log.print(String.format(fString, "Start Time"));
		Log.print(String.format(fString, "Finish Time"));
		Log.print("\n");

		//DecimalFormat dft = new DecimalFormat("######.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			printCloudlet(cloudlet);
		}
	}
	
	private static void printCloudlet(Cloudlet cloudlet) {
		Log.print(String.format(fInt, cloudlet.getCloudletId()));

		if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
			Log.print(String.format(fString, "SUCCESS"));
			Log.print(String.format(fInt, cloudlet.getResourceId()));
			Log.print(String.format(fInt, cloudlet.getVmId()));
			Log.print(String.format(fInt, cloudlet.getCloudletLength()));
			Log.print(String.format(fFloat, cloudlet.getActualCPUTime()));
			Log.print(String.format(fFloat, cloudlet.getExecStartTime()));
			Log.print(String.format(fFloat, cloudlet.getFinishTime()));
			Log.print("\n");
		}
		else {
			Log.printLine("FAILED");
		}
	}	
}
