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

package org.cloudbus.osmosis.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.edge.core.edge.EdgeLet;
import org.cloudbus.osmosis.core.OsmosisLayer;
;


/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-SDWAN 1.0
 * 
**/

public class OsmesisAppDescription {

	private String appName;
	private int workflowId;	
	
	private double DataRate;
	private double StopDataGenerationTime;

	private int appID;


	List<OsmosisLayer> layers;
	List<OsmosisLayer> trackLayers;

	private long IoTDeviceOutputSize; // in Mb
	private long MELOutputSize; // Mb
	
	private long OsmesisEdgeletSize; //MI
	private long OsmesisCloudletSize; // MI
	
	private String IoTDeviceName;

	private String MELName; // mel resides in an edge device
	private String VmName;

	private int iotDeviceID;
	private int melId;	 
	private int vmCloudId; 
	
	private int edgeDcId;
	private int cloudDcId;
	
//	private String edgeDatacenterName;
//	private String cloudDatacenterName;
//	private List<String> datacentersName;
	private Map<String, String> datacentersName;

	private double startTime = -1;
	private double endTime;	 	
		
	private List<EdgeLet> edgeLetList;

	private boolean isIoTDeviceDied;

	private double IoTDeviceBatteryConsumption;	

	// renewable energy
	private String MELName_2;
	private long OsmesisEdgeletSize_2; //MI
	private long MELOutputSize_2; // Mb

//	public OsmesisAppDescription(String appName, int appID, double DataRate, double StopDataGenerationTime,
//								 String ioTDeviceName, long ioTDeviceOutput, String MELName, long osmesisEdgeletSize,
//								 long MELOutput, String MELName_2, long osmesisEdgeletSize_2,
//								 long MELOutput_2, String vmName, long osmesisCloudletSize, Map<String, String> melDatacenter_Name) {
//		this.appName = appName;
//		this.DataRate = DataRate;
//		this.StopDataGenerationTime = StopDataGenerationTime;
//		this.IoTDeviceName = ioTDeviceName;
//		this.appID = appID;
//		this.IoTDeviceOutputSize = ioTDeviceOutput;
//		this.MELName = MELName;
//		this.OsmesisEdgeletSize = osmesisEdgeletSize;
//		this.MELOutputSize = MELOutput;
//
//		// renewable energy
//		this.MELName_2 = MELName_2;
//		this.OsmesisEdgeletSize_2 = osmesisEdgeletSize_2;
//		this.MELOutputSize_2 = MELOutput_2;
//
//		this.VmName = vmName;
//		this.OsmesisCloudletSize = osmesisCloudletSize;
//		this.edgeLetList = new ArrayList<>();
//		this.isIoTDeviceDied = false;
//		this.datacentersName = melDatacenter_Name;
//	}

	public OsmesisAppDescription(String osmesisAppName, int appID, double dataRate, double stopDataGenerationTime, List<OsmosisLayer> layers) {
		this.appName = osmesisAppName;
		this.appID = appID;
		this.DataRate = dataRate;
		this.StopDataGenerationTime = stopDataGenerationTime;
		this.layers = layers;
	}

	public double getStopDataGenerationTime() {
		return StopDataGenerationTime;
	}
	
	public int getVmCloudId() {
		return vmCloudId;
	}

	public void setVmCloudId(int vmCloudId) {
		this.vmCloudId = vmCloudId;
	}
	
	public List<EdgeLet> getEdgeLetList() {
		return edgeLetList;
	}

	public void setEdgeLetList(EdgeLet edgeLet) {
		this.edgeLetList.add(edgeLet); 
	}

	
	public String getAppName() {
		return appName;
	}

	
	public double getDataRate() {
		return DataRate;
	}
	
	public String getIoTDeviceName() {
		return IoTDeviceName;
	}

	public int getAppID() {
		return appID;
	}
	
	public long getIoTDeviceOutputSize() {
		return IoTDeviceOutputSize;
	}
	
	public String getMELName() {
		return MELName;
	}
	
	public long getOsmesisEdgeletSize() {
		return OsmesisEdgeletSize;
	}
	
	public long getMELOutputSize() {
		return MELOutputSize;
	}
	
	public String getVmName() {
		return VmName;
	}
	
	public long getOsmesisCloudletSize() {
		return OsmesisCloudletSize;
	}
	
	public double getAppStartTime() {
		return startTime;
	}
	public void setAppStartTime(double startTime) {
		this.startTime = startTime;
	}
		
	public double getEndTime() {
		return endTime;
	}

	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}


	public int getIoTDeviceId(){
		return this.iotDeviceID;
	}
	
	public void setIoTDeviceId(int iotDeviceID) {
		this.iotDeviceID = iotDeviceID;		
	}

	public int getMelId(){
		return this.melId;
	}
	public void setMelId(int melId) {
		this.melId = melId;
		
	}

	public int getWorkflowId() {
		return workflowId;
	}

	public int addWorkflowId(int workflowId) {
		this.workflowId += workflowId;
		return this.workflowId;
	}

	public String getDatacenterName(String melName) {
//		return this.datacentersName.get(melName);
		return "";
	}


//	public String getDatacenterName(int index) {
//		return this.datacentersName.get(index);
//	}
//
//	public void setDatacenterName(String edgeDatacenterName) {
////		this.edgeDatacenterName = edgeDatacenterName;
//	this.datacentersName.add(edgeDatacenterName);
//	}

//	public String getCloudDatacenterName() {
//		return cloudDatacenterName;
//	}
//
//	public void setCloudDatacenterName(String cloudDatacenterName) {
//		this.cloudDatacenterName = cloudDatacenterName;
//	}

	public int getEdgeDcId() {
		return edgeDcId;
	}

	public void setEdgeDcId(int edgeDcId) {
		this.edgeDcId = edgeDcId;
	}

	public int getCloudDcId() {
		return cloudDcId;
	}

	public void setCloudDcId(int cloudDcId) {
		this.cloudDcId = cloudDcId;
	}

	public void setIoTDeviceDied(boolean b) {
		this.isIoTDeviceDied = b;
	}
	
	public boolean getIsIoTDeviceDied(){
		return this.isIoTDeviceDied;
	}
	
	public String getIoTDeviceBatteryStatus(){
		if(this.isIoTDeviceDied){
			return "Yes";
		}
		return "No";
	}

	public void setIoTBatteryConsumption(double batteryTotalConsumption) {
		this.IoTDeviceBatteryConsumption = batteryTotalConsumption;
	}
	
	public double getIoTDeviceBatteryConsumption() {
		return IoTDeviceBatteryConsumption;
	}

	public String getMELName_2() {
		return MELName_2;
	}

	public long getOsmesisEdgeletSize_2() {
		return OsmesisEdgeletSize_2;
	}

	public long getMELOutputSize_2() {
		return MELOutputSize_2;
	}


	public OsmosisLayer getOsmosisLayer(int index) {
		return layers.get(index);
	}

	public int getOsmosisLayerSize() {
		return this.layers.size();
	}

//	public OsmosisLayer getLayer() {
//		int index;
//		if(trackLayers == null) {
//			this.trackLayers = new ArrayList<>();
//			index = this.trackLayers.size();
//		} else {
//				index = this.trackLayers.size() + 1;
//		}
//		trackLayers.add(layers.get(index));
//		return layers.get(index);
//	}

	public OsmosisLayer getIoTLayer() {
		return layers.get(0);
	}

}
