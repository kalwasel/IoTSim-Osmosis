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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.edge.core.edge.EdgeLet;
import org.cloudbus.cloudsim.edge.iot.IoTDevice;
import org.cloudbus.osmosis.core.OsmosisLayer;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public class OsmesisBroker extends DatacenterBroker {

	private EdgeSDNController edgeController;
	private List<Cloudlet> edgeletList = new ArrayList<>();
	private List<OsmesisAppDescription> appList; 	
	private Map<String, Integer> iotDeviceNameToId = new HashMap<>();
	private Map<Integer, List<? extends Vm>> mapVmsToDatacenter  = new HashMap<>();;
	public static int brokerID;
	private Map<String, Integer> iotVmIdByName = new HashMap<>();
	public static List<WorkflowInfo> workflowTag = new ArrayList<>();
	private List<OsmesisDatacenter> datacenters = new ArrayList<>();


	public OsmesisBroker(String name) {
		super(name);
		this.appList = new ArrayList<>();
		brokerID = this.getId();
	}

	public EdgeSDNController getEdgeSDNController() {
		return edgeController;
	}

	public void setEdgeSDNController(EdgeSDNController controller) {
		this.edgeController = controller;
	}

	@Override
	public void processEvent(SimEvent ev) {
		switch (ev.getTag()) {
		case CloudSimTags.RESOURCE_CHARACTERISTICS_REQUEST:
			this.processResourceCharacteristicsRequest(ev);
			break;

		case CloudSimTags.RESOURCE_CHARACTERISTICS:
			this.processResourceCharacteristics(ev);
			break;

		case CloudSimTags.VM_CREATE_ACK:
			this.processVmCreate(ev);
			break;

		case OsmosisTags.GENERATE_OSMESIS:
			generateIoTData(ev);
			break;

		case OsmosisTags.Transmission_ACK:
			askMelToProccessData(ev);
			break;

		case CloudSimTags.CLOUDLET_RETURN:
			processCloudletReturn(ev);
			break;

		case OsmosisTags.Transmission_SDWAN_ACK:
			proccessReceivedData(ev);
			break;

		case CloudSimTags.END_OF_SIMULATION:
			this.shutdownEntity();
			break;

		default:
			break;
		}
	}
	protected void processCloudletReturn(SimEvent ev)
	{
		Cloudlet cloudlet = (Cloudlet) ev.getData();
		getCloudletReceivedList().add(cloudlet);
		EdgeLet edgeLet = (EdgeLet) ev.getData();
		edgeLet.getWorkflowTag().setFinishTime(CloudSim.clock());
		askMelToSendData(ev); // send to another edge datacenter
	}

	private void askMelToProccessData(SimEvent ev) {

		Flow flow = (Flow) ev.getData();
		// it can be any edge let size, it is fine...
		int appId = flow.getOsmesisAppId();
		OsmesisAppDescription app = getAppById(appId);

		long edgeletSize = flow.getOsmesisEdgeletSize(); // the size can be for edgelet or cloudlet
		EdgeLet edgeLet = generateEdgeLet(edgeletSize);

		edgeLet.setVmId(flow.getDestination());
		edgeletList.add(edgeLet);
		edgeLet.setOsmesisAppId(appId);
		edgeLet.setWorkflowTag(flow.getWorkflowTag());
		edgeLet.getWorkflowTag().addEdgeLet(edgeLet);
		this.setCloudletSubmittedList(edgeletList);

		sendNow(flow.getDatacenterId(), CloudSimTags.CLOUDLET_SUBMIT, edgeLet);
	}

	private EdgeLet generateEdgeLet(long length) {
		long fileSize = 30;
		long outputSize = 1;
		EdgeLet edgeLet = new EdgeLet(OsmosisBuilder.edgeLetId, length, 1, fileSize, outputSize, new UtilizationModelFull(), new UtilizationModelFull(),
				new UtilizationModelFull());
		edgeLet.setUserId(this.getId());
		OsmosisBuilder.edgeLetId++;
		return edgeLet;
	}

	protected void proccessReceivedData(SimEvent ev) {
		Flow flow = (Flow) ev.getData();
		askMelToProccessData(ev); // proccess data in a MEL
	}

	private void askMelToSendData(SimEvent ev) {
		EdgeLet edgeLet = (EdgeLet) ev.getData();
		int osmesisAppId = edgeLet.getOsmesisAppId();
		OsmesisAppDescription app = getAppById(osmesisAppId);

		OsmosisLayer layer = edgeLet.getWorkflowTag().getOsmosisLayer();

		if(edgeLet.getWorkflowTag().checkOsmosisLayer()){
			// it is in stage 3, which there is no further processing
			return;
		}
		String destDCName = layer.getDestLayerName();
		int sourceDcId = this.getDatacenterIdByName(layer.getSourceLayerName());
		int destDcId = this.getDatacenterIdByName(layer.getDestLayerName());
		String sourceName = layer.getSourceName();
		String destName = layer.getDestName();
		int sourceId = this.getVmIdByName(sourceName); // e.g. MEL 1
		int destId = this.getVmIdByName(destName); // e.g. MEL 2
		long flowSize = layer.getOsmoticPktSize();

		// get next layer osmosislet size
		OsmosisLayer nextLayer = edgeLet.getWorkflowTag().getOsmosisNextLayer();
		long edgeletSize = nextLayer.getOsmoticLetSize();

		int id = OsmosisBuilder.flowId ;

		Flow flow  = new Flow(sourceName, destName, sourceId , destId, id, null);
		flow.setAppName(app.getAppName());
		flow.addPacketSize(flowSize);
		flow.setSubmitTime(CloudSim.clock());
		flow.setDatacenterId(destDcId);
		flow.setOsmesisAppId(osmesisAppId);
		flow.setWorkflowTag(edgeLet.getWorkflowTag());

		flow.getWorkflowTag().setOsmosisFlow(flow);

		flow.getWorkflowTag().setDCName(destDCName);

		flow.setOsmesisEdgeletSize(edgeletSize); // renewable example
		OsmosisBuilder.flowId++;
		sendNow(sourceDcId, OsmosisTags.BUILD_ROUTE, flow);
	}

	private OsmesisAppDescription getAppById(int osmesisAppId) {
		OsmesisAppDescription osmesis = null;
		for(OsmesisAppDescription app : this.appList){
			if(app.getAppID() == osmesisAppId){
				osmesis = app;
			}
		}
		return osmesis;
	}
	
	public void submitVmList(List<? extends Vm> list, int datacenterId) {		
		mapVmsToDatacenter.put(datacenterId, list);
		getVmList().addAll(list);
	}
	
	protected void createVmsInDatacenter(int datacenterId) {		
		int requestedVms = 0;
		List<? extends Vm> vmList = mapVmsToDatacenter.get(datacenterId);
		if(vmList != null){
			for (int i = 0; i < vmList.size(); i++) {
				Vm vm = vmList.get(i);		
					sendNow(datacenterId, CloudSimTags.VM_CREATE_ACK, vm);				
					requestedVms++;
			}
		}
		getDatacenterRequestedIdsList().add(datacenterId);
		setVmsRequested(requestedVms);
		setVmsAcks(0);
	}

	@Override
	protected void processOtherEvent(SimEvent ev) {

	}

	@Override
	public void processVmCreate(SimEvent ev) {
		super.processVmCreate(ev);
		if (allRequestedVmsCreated()) {
			for(OsmesisAppDescription app : this.appList){
				OsmosisLayer IoTLayer = app.getIoTLayer();

				int iotDeviceID = getiotDeviceIdByName(IoTLayer.getSourceName()); // IoT device name
				int melId = getVmIdByName(IoTLayer.getDestName());

				int edgeDatacenterId = this.getDatacenterIdByVmId(melId);

				IoTLayer.setSourceId(iotDeviceID);
				IoTLayer.setDestId(melId);
				IoTLayer.setDestLayerId(edgeDatacenterId);


				app.setIoTDeviceId(iotDeviceID);
				app.setMelId(melId);
				app.setEdgeDcId(edgeDatacenterId);
				if(app.getAppStartTime() == -1){
					app.setAppStartTime(CloudSim.clock());
				}
				double dealy = app.getDataRate();
				send(this.getId(), dealy, OsmosisTags.GENERATE_OSMESIS, app);
			}
		}
	}

	static int count =0;
	private void generateIoTData(SimEvent ev){
		OsmesisAppDescription app = (OsmesisAppDescription) ev.getData();
		count++;
		if(CloudSim.clock() <= app.getStopDataGenerationTime() && !app.getIsIoTDeviceDied()){
			System.out.println("Generating data " + count);
			sendNow(app.getIoTDeviceId(), OsmosisTags.SENSING, app);
			double dealy = app.getDataRate();
			send(this.getId(), dealy, OsmosisTags.GENERATE_OSMESIS, app);
		} else {
			
		}
	}
			
	private boolean allRequestedVmsCreated() {
		return this.getVmsCreatedList().size() == getVmList().size() - getVmsDestroyed();
	}

	public void submitOsmesisApps(List<OsmesisAppDescription> appList) {
		this.appList = appList;		
	}

	public int getiotDeviceIdByName(String melName){
		return this.iotDeviceNameToId.get(melName);
	}
	
	public void setIoTDevices(List<IoTDevice> devices) {	
		for(IoTDevice device : devices){
			iotDeviceNameToId.put(device.getName(), device.getId());
		}
	}
	
	public void mapVmNameToId(Map<String, Integer> melNameToIdList) {
	this.iotVmIdByName.putAll(melNameToIdList);		
	}
	
	public int getVmIdByName(String name){
		return this.iotVmIdByName.get(name);
	}


	public void setDatacenters(List<OsmesisDatacenter> osmesisDatacentres) {
		this.datacenters = osmesisDatacentres;		
	}
	
	private int getDatacenterIdByVmId(int vmId){
		int dcId = 0;
		for(OsmesisDatacenter dc :datacenters){
			for(Vm vm : dc.getVmList()){
				if(vm.getId() == vmId){
					dcId = dc.getId();					
				}
			}
		}
		return dcId;
	}
	
	private String getDatacenterNameById(int id){
		String name = "";
		for(OsmesisDatacenter dc :datacenters){			
			if(dc.getId() == id){
				name = dc.getName();
			}
		}
		return name;
	}

	private int getDatacenterIdByName(String name){
		int id =-1;
		for(OsmesisDatacenter dc :datacenters){
			if(dc.getName().equals(name)){
				id = dc.getId();
			}
		}
		return id;
	}

}
