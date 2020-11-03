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

package org.cloudbus.cloudsim.osmesis.examples.uti;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.osmosis.core.Flow;
import org.cloudbus.osmosis.core.OsmesisAppDescription;
import org.cloudbus.osmosis.core.OsmesisAppsParser;
import org.cloudbus.osmosis.core.OsmesisBroker;
import org.cloudbus.osmosis.core.WorkflowInfo;


/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public class PrintResults {	
		
	public void printOsmesisNetwork() {
		
		List<WorkflowInfo> tags = new ArrayList<>();
		for(OsmesisAppDescription app : OsmesisAppsParser.appList){
			for(WorkflowInfo workflowTag : OsmesisBroker.workflowTag){
				workflowTag.getAppId();
				if(app.getAppID() == workflowTag.getAppId()){
					tags.add(workflowTag);
				}
			}
			printOsmesisApp(tags);		
			tags.clear();
		}
		
		Log.printLine();				
		Log.printLine("=========================== Osmesis Overall Apps Results ========================");
		Log.printLine(String.format("%1s %19s %32s %24s %22s  %25s  %22s %16s %14s %19s %25s"				
				, "App_Name"
				, "IoTDeviceDrained"
				, "IoTDeviceBatteryConsumption"
				, "TotalIoTGeneratedData"
				, "TotalEdgeLetSizes"
				, "TotalMELGeneratedData"
				, "TotalCloudLetSizes"						  
				, "StartTime" 
				, "EndTime" 
				, "SimluationTime"
				, "appTotalRunningTmie"));

		for(OsmesisAppDescription app : OsmesisAppsParser.appList){
			for(WorkflowInfo workflowTag : OsmesisBroker.workflowTag){
				workflowTag.getAppId();
				if(app.getAppID() == workflowTag.getAppId()){
					tags.add(workflowTag);
				}
			}			
			printAppStat(app, tags);
			tags.clear();
		}
		printAppWorkflowConfigration();
	}
	
	private void printAppWorkflowConfigration() {
		Log.printLine();				
		Log.printLine("=========================== Osmesis Workflow Configrations ========================");
		Log.printLine(String.format("%1s %17s %26s  %13s  %30s %13s %21s %23s %12s %21s"				
				,"App_Name"   				
				, "DataRate_Sec"
				, "StopDataGeneration_Sec"
				, "IoTDevice"
				, "IoTDeviceOutputData_Mb"
				, "MELName"
				, "OsmesisEdgelet_MI"  				 				
				, "MELOutputData_Mb"
				, "VmName"  				   
				, "OsmesisCloudlet_MI" 
				));
		for(OsmesisAppDescription app : OsmesisAppsParser.appList){
			Log.printLine(String.format("%1s %15s %21s  %25s  %18s %22s %15s %24s %18s %14s"				
					, app.getAppName()   				
					, app.getDataRate()
					, app.getStopDataGenerationTime()
					, app.getIoTDeviceName()
					, app.getIoTDeviceOutputSize()
					, app.getMELName()
					, app.getOsmesisEdgeletSize()  				 				
					, app.getMELOutputSize()
					, app.getVmName()  				   
					, app.getOsmesisCloudletSize() 
					));							
		}		
	}

	private void printAppStat(OsmesisAppDescription app, List<WorkflowInfo> tags) {		 	
		String appName = app.getAppName();
		String isIoTDeviceDrained = app.getIoTDeviceBatteryStatus();
		double iotDeviceTotalConsumption = app.getIoTDeviceBatteryConsumption();
		long TotalIoTGeneratedData = 0;
		long TotalEdgeLetSizes = 0;
		long TotalMELGeneratedData = 0;
		long TotalCloudLetSizes = 0;
		double StartTime = 0;
		double EndTime = 0;
		double SimluationTime = 0;
		double appTotalRunningTmie = 0;
		

		
		StartTime = app.getAppStartTime();
		EndTime = tags.get(tags.size()-1).getCloudLet().getFinishTime();
		SimluationTime = EndTime - StartTime;			
		
		WorkflowInfo firstWorkflow = tags.get(0);
		WorkflowInfo secondWorkflow = tags.get(1);
		
		if(firstWorkflow.getFinishTime() > secondWorkflow.getSartTime()){
			appTotalRunningTmie = EndTime - StartTime;			
		} else {
			for(WorkflowInfo workflowTag : tags){
				appTotalRunningTmie += workflowTag.getFinishTime() - workflowTag.getSartTime(); 
			}
		}
		
		for(WorkflowInfo workflowTag : tags){
			TotalIoTGeneratedData += workflowTag.getIotDeviceFlow().getSize(); 
			TotalEdgeLetSizes += workflowTag.getEdgeLet().getCloudletLength(); 
			TotalMELGeneratedData += workflowTag.getEdgeToCloudFlow().getSize();
			TotalCloudLetSizes += workflowTag.getCloudLet().getCloudletLength();			   
		}

		Log.printLine(String.format("%1s %15s %28s %26s %26s  %22s  %25s %18s %17s %15s %22s"
				, appName
				, isIoTDeviceDrained
				,  new DecimalFormat("0.00").format(iotDeviceTotalConsumption) 
				, TotalIoTGeneratedData 
				, TotalEdgeLetSizes 
				, TotalMELGeneratedData 
				, TotalCloudLetSizes    
				,  new DecimalFormat("0.000").format(StartTime) 
				,  new DecimalFormat("0.000").format(EndTime) 
				,  new DecimalFormat("0.000").format(SimluationTime)
				,  new DecimalFormat("0.000").format(appTotalRunningTmie)				
				));
	}
	
	public double detemineMaxValue(double oldValue, double newValue){
		if(oldValue < newValue){
			oldValue  = newValue;	
		}
		return oldValue;
	}

	public void printNetworkStatistics(Flow flow){				
			Log.printLine(String.format("%1s %21s %12s %19s %15s %12s %11s %22s %16s %22s "
					, flow.getFlowId()
					, "" 					
					, flow.getAppName() 
					, ""
					, flow.getAppNameSrc() 
					, flow.getAppNameDest()
					, flow.getSize()  					
					, new DecimalFormat("0.0000").format(flow.getStartTime()) 
					, new DecimalFormat("0.0000").format(flow.getFinishTime() -  flow.getStartTime())   
					, new DecimalFormat("0.0000").format(flow.getFinishTime())));		
	}
	
	public void printOsmesisApp(List<WorkflowInfo> tags) {
		Log.printLine();				
		Log.printLine("=========================== Osmesis App Results ========================");
		Log.printLine(String.format("%1s %11s %18s %17s %17s %19s %20s %35s %37s %21s %33s %21s %23s %28s %20s %30s %25s %10s"
				,"App_ID"
				,"AppName"
				,"Transaction"
				,"StartTime"
				,"FinishTime"
				,"IoTDeviceName"
				,"MELName"
				,"DataSizeIoTDeviceToMEL_Mb"  
				,"TransmissionTimeIoTDeviceToMEL"
				,"EdgeLetMISize"
				,"EdgeLetProccessingTimeByMEL"				  
				,"DestinationVmName"
				,"DataSizeMELToVM_Mb"
				,"TransmissionTimeMELToVM"  
				,"CloudLetMISize"
				,"CloudLetProccessingTimeByVM"  						
				, "TransactionTotalTime"
				, "   "));
	
		double transactionTotalTime;
		for(WorkflowInfo workflowTag : tags){			
			transactionTotalTime =  workflowTag.getIotDeviceFlow().getTransmissionTime() + workflowTag.getEdgeLet().getActualCPUTime()
					+ workflowTag.getEdgeToCloudFlow().getTransmissionTime() + workflowTag.getCloudLet().getActualCPUTime();
			Log.printLine(String.format("%1s %15s %15s %18s %18s %21s %23s %21s %34s %32s %25s %30s %18s %26s %23s %24s %28s"
					, workflowTag.getAppId()
					, workflowTag.getAppName()
					, workflowTag.getWorkflowId()	
					, workflowTag.getSartTime()
					, new DecimalFormat("0.0000").format(workflowTag.getFinishTime())
					, workflowTag.getIotDeviceFlow().getAppNameSrc() 
					, workflowTag.getIotDeviceFlow().getAppNameDest() + " (" +workflowTag.getSourceDCName() + ")"
					, workflowTag.getIotDeviceFlow().getSize()
					, new DecimalFormat("0.0000").format(workflowTag.getIotDeviceFlow().getTransmissionTime())
					, workflowTag.getEdgeLet().getCloudletLength()
					, new DecimalFormat("0.0000").format(workflowTag.getEdgeLet().getActualCPUTime())
					, workflowTag.getEdgeToCloudFlow().getAppNameDest() + " (" +workflowTag.getDestinationDCName() + ")"
					, workflowTag.getEdgeToCloudFlow().getSize()
					, new DecimalFormat("0.0000").format(workflowTag.getEdgeToCloudFlow().getTransmissionTime())
					, workflowTag.getCloudLet().getCloudletLength()
					, new DecimalFormat("0.0000").format(workflowTag.getCloudLet().getActualCPUTime())
					, new DecimalFormat("0.0000").format(transactionTotalTime)));
		}
	}	
}
