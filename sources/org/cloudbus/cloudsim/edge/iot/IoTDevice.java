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

package org.cloudbus.cloudsim.edge.iot;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.edge.core.edge.Battery;
import org.cloudbus.cloudsim.edge.core.edge.Mobility;
import org.cloudbus.cloudsim.edge.iot.network.EdgeNetworkInfo;
import org.cloudbus.cloudsim.edge.utils.LogUtil;
import org.cloudbus.osmosis.core.Flow;
import org.cloudbus.osmosis.core.OsmesisAppDescription;
import org.cloudbus.osmosis.core.OsmesisBroker;
import org.cloudbus.osmosis.core.OsmosisBuilder;
import org.cloudbus.osmosis.core.OsmosisTags;
import org.cloudbus.osmosis.core.WorkflowInfo;
import org.cloudbus.osmosis.core.polocies.MovingPolicy;
import org.cloudbus.osmosis.core.OsmosisLayer;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public abstract class IoTDevice extends SimEntity {

	public static int cloudLetId = 0;
	private double runningTime = 0;	
	public String sensed_data_type;
	protected Battery battery;
	private EdgeNetworkInfo networkModel;	
	private MovingPolicy movingPolicy;
	private Mobility mobility;
	int connectingEdgeDeviceId = -1;
	private boolean enabled;
	public abstract boolean updateBatteryBySensing();
	public abstract boolean updateBatteryByTransmission();
	private double bw;
	private double usedBw; 

	
	private List<Flow> flowList = new ArrayList<>(); 
	
	public IoTDevice( String name, EdgeNetworkInfo networkModel, double bandwidth) {
		super(name);
		this.battery = new Battery();	
		this.networkModel = networkModel;
		this.enabled = true;		
		this.bw = bandwidth;
	}
	
	@Override
	public void startEntity() {		
	}

	@Override
	public void processEvent(SimEvent ev) {
		int tag = ev.getTag();
		switch (tag) {
		case OsmosisTags.SENSING:
			this.sensing(ev);
			break;
			
		case  OsmosisTags.updateIoTBW:
			this.removeFlow(ev);
			break;

		case OsmosisTags.MOVING:
			/*
			 * To do list 
			 */
			break;
		}
	}
	
	public double getRunningTime() {
		return this.runningTime;
	}

	public void setRunningTime(double runningTime) {
		this.runningTime = runningTime;
	}

	
	public Mobility getMobility() {
		return this.mobility;
	}
	
	public void setMobility(Mobility location) {
		this.mobility = location;
	}
	
	public Battery getBattery() {
		return this.battery;
	}	

	public MovingPolicy getMovingPolicy() {
		return this.movingPolicy;
	}

	public void setMovingPolicy(MovingPolicy movingPolicy) {
		this.movingPolicy = movingPolicy;
	}

	public void setEdgeDeviceId(int id) {
		this.connectingEdgeDeviceId = id;
	}

	public EdgeNetworkInfo getNetworkModel() {
		return this.networkModel;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {

		this.enabled = enabled;
	}
	
	private void sensing(SimEvent ev) {
		OsmesisAppDescription app = (OsmesisAppDescription) ev.getData();

		// if the battery is drained,
		this.updateBatteryBySensing();
		boolean died = this.updateBatteryByTransmission();
		app.setIoTBatteryConsumption(this.battery.getBatteryTotalConsumption());
		if (died) {
			app.setIoTDeviceDied(true);
			LogUtil.info(this.getClass().getSimpleName() + " running time is " + CloudSim.clock());

			this.setEnabled(false);
			LogUtil.info(this.getClass().getSimpleName()+" " + this.getId() + "'s battery has been drained");
			this.runningTime = CloudSim.clock();			
			return;
		}

		Flow flow = this.createFlow(app);

		OsmosisLayer IoTLayer = app.getIoTLayer(); // get IoT layer

		WorkflowInfo workflowTag = new WorkflowInfo();
		workflowTag.setStartTime(CloudSim.clock());
		workflowTag.setAppId(app.getAppID());
		workflowTag.setAppName(app.getAppName());
		workflowTag.setOsmosisFlow(flow);
		workflowTag.setWorkflowId(app.addWorkflowId(1));
//		workflowTag.setDCName(app.getDatacenterName(app.getMELName()));
//		workflowTag.setDCName(app.getDatacenterName(app.getMELName()));
		workflowTag.setDCName(IoTLayer.getDestLayerName()); // mel 1
//		workflowTag.setDestinationDCName(app.getDatacenterName(1));
//		workflowTag.setNumOfLayer(0); // always starts with 0 (IoT layer)
		workflowTag.setApp(app); // always starts with 0 (IoT layer)
		flow.setWorkflowTag(workflowTag);
		OsmesisBroker.workflowTag.add(workflowTag);
//		flow.addPacketSize(app.getIoTDeviceOutputSize());
		flow.addPacketSize(IoTLayer.getOsmoticPktSize());
		updateBandwidth();
//		System.out.println("IoT device is sending data #" + workflowTag.getWorkflowId());
//		if(workflowTag.getWorkflowId() == 49){
//			int cc=1;
//		}
		sendNow(flow.getDatacenterId(), OsmosisTags.TRANSMIT_IOT_DATA, flow);		
	}

	private Flow createFlow(OsmesisAppDescription app) {
//		int melId = app.getMelId();
//		int datacenterId = -1;
//		datacenterId = app.getEdgeDcId();
		int id = OsmosisBuilder.flowId ;

		OsmosisLayer IoTLayer = app.getIoTLayer(); // get IoT layer

		String destDCName = IoTLayer.getDestLayerName();
//		int sourceDcId = this.getDatacenterIdByName(IoTLayer.getSourceLayerName());
//		int destDcId = this.getDatacenterIdByName(IoTLayer.getDestLayerName());
//		String sourceName = IoTLayer.getSourceName();
		String destName = IoTLayer.getDestName();

		int sourceId = IoTLayer.getSourceId(); // e.g. MEL 1
		int destId = IoTLayer.getDestId(); // e.g. MEL 2
		int datacenterId = IoTLayer.getDestLayerId();

		long flowSize = IoTLayer.getOsmoticPktSize();

		OsmosisLayer edgeLayer = app.getOsmosisLayer(1); // get IoT layer
		long edgeletSize = edgeLayer.getOsmoticLetSize();


		Flow flow  = new Flow(this.getName(), destName, this.getId(), destId, id, null);
		flow.setOsmesisAppId(app.getAppID());
		flow.setAppName(app.getAppName());		
		flow.addPacketSize(flowSize);
		flow.setSubmitTime(CloudSim.clock());
		flow.setDatacenterId(datacenterId);
		flow.setOsmesisEdgeletSize(edgeletSize);
		OsmosisBuilder.flowId++;
		flowList.add(flow);
		return flow;
	}
	
	public void setBw(double bw) {
		this.bw = bw;
	}
	
	public double getBw() {
		return bw;
	}
	
	public double getUsedBw() {
		return usedBw;
	}
	
	public void removeFlow(SimEvent ev) {
		
		Flow flow  = (Flow) ev.getData();
		this.flowList.remove(flow);
		
		updateBandwidth();	
	}
	
	private void updateBandwidth(){			
		this.usedBw = this.getBw() / this.flowList.size(); // the updated bw 		
		for(Flow getFlow : this.flowList){
			getFlow.updateSourceBw(this.usedBw);
		}	
	}
}