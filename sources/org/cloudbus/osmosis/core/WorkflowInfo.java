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
import org.cloudbus.osmosis.core.OsmosisLayer;

import java.util.ArrayList;
import java.util.List;

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


	private List<Flow> osmosisFlow = new ArrayList<>(); // 0 = iotDeviceFlow, 1 = edgeToedgeFlow, 2 = edgeToCloudFlow, etc.

	private List<EdgeLet> osmosisletList = new ArrayList<>();
	private List<String> melDatacenters = new ArrayList<>();

	private double startTime;
	private double finishTime;
	private OsmesisAppDescription app;

	private int numOfLayer = 0;

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

	public Flow getOsmosisFlow(int index) {
		return 	this.osmosisFlow.get(index);
	}
	public void setOsmosisFlow(Flow flow) {
		this.osmosisFlow.add(flow);
	}

	public List<Flow> getFlowLists(){
		return this.osmosisFlow;
	}

	public void addEdgeLet(EdgeLet edgeLet) {
		osmosisletList.add(edgeLet);
	}

	public EdgeLet getOsmosislet(int index){
		return this.osmosisletList.get(index);
	}

	public int getOsmosisLetSize(){
		return this.osmosisletList.size();
	}

	public void setDCName(String dcName) {
		this.melDatacenters.add(dcName);
	}

	public String getDCName(int num) {
		return this.melDatacenters.get(num);
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

	public void setApp(OsmesisAppDescription app) {
		this.app = app;
	}

	public OsmosisLayer getOsmosisLayer() {
		this.numOfLayer += 1; // always start with 1 (edge layer)
		return this.app.getOsmosisLayer(this.numOfLayer);
	}

	public OsmosisLayer getOsmosisNextLayer() {
		int nextLayer = this.numOfLayer +1;
		return this.app.getOsmosisLayer(nextLayer);
	}

	public boolean checkOsmosisLayer(){
		if((this.numOfLayer +1) == this.app.getOsmosisLayerSize()){
			return true;
		}
		return false;
	}
}
