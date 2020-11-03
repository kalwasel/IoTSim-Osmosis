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

import org.cloudbus.cloudsim.edge.core.edge.EdgeLet;

/**
 * Track every transaction from an IoT device until it reaches the cloud and got processed. 
 * This class is used to reporting OsmesisSDN results
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public class WorkflowInfo {
	private int workflowId;	// create a new ID every time an IoT device generates data
	private int appId;
	private String appName;
	
	private Flow iotDeviceFlow;
	
	private Flow edgeToCloudFlow;
	private EdgeLet edgeLet;
	private EdgeLet cloudLet;
	
	private String sourceDatacenterName;
	private String DestinationDatacenterName;
	private double startTime;
	private double finishTime;

	public int getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(int workflowId) {
		this.workflowId = workflowId;
	}
	
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Flow getIotDeviceFlow() {
		return iotDeviceFlow;
	}
	public void setIotDeviceFlow(Flow iotDeviceFlow) {
		this.iotDeviceFlow = iotDeviceFlow;
	}
	public Flow getEdgeToCloudFlow() {
		return edgeToCloudFlow;
	}
	public void setEdgeToCloudFlow(Flow edgeToCloudFlow) {
		this.edgeToCloudFlow = edgeToCloudFlow;
	}
	public EdgeLet getEdgeLet() {
		return edgeLet;
	}
	public void setEdgeLet(EdgeLet edgeLet) {
		this.edgeLet = edgeLet;
	}
	public EdgeLet getCloudLet() {
		return cloudLet;
	}
	public void setCloudLet(EdgeLet cloudLet) {
		this.cloudLet = cloudLet;
	}
	public String getSourceDCName() {
		return this.sourceDatacenterName;
	}
	public void setSourceDCName(String sourceDatacenterName) {
		this.sourceDatacenterName = sourceDatacenterName;
	}
	public String getDestinationDCName() {
		return DestinationDatacenterName;
	}
	public void setDestinationDCName(String DestinationDatacenterName) {
		this.DestinationDatacenterName = DestinationDatacenterName;
	}
	
	public double getSartTime() {
		// TODO Auto-generated method stub
		return this.startTime;
	}
	
	public void setStartTime(double startTime){
		this.startTime = startTime;
	}
	public void setFinishTime(double finishTime) {
		this.finishTime = finishTime;
		
	}
	
	public double getFinishTime() {
		return finishTime;
	}	
}
