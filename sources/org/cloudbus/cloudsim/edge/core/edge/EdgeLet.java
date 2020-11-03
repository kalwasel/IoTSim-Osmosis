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

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.edge.core.edge.EdgeLet;
import org.cloudbus.osmosis.core.WorkflowInfo;

public class EdgeLet extends Cloudlet {
	private int osmesisAppId;
	private int id;	
	private String datacenterName;
	private WorkflowInfo workflowTag;
	
	public EdgeLet( 
			final int cloudletId,
            final long cloudletLength,
            final int pesNumber,
            final long cloudletFileSize,
            final long cloudletOutputSize,
            final UtilizationModel utilizationModelCpu,
            final UtilizationModel utilizationModelRam,
            final UtilizationModel utilizationModelBw) {
		super(cloudletId, cloudletLength, pesNumber, cloudletFileSize, cloudletOutputSize, utilizationModelCpu,
				utilizationModelRam, utilizationModelBw);
	}
	
	public int getEdgeDatacenterId() {
		return id;
	}

	public void setEdgeDatacenterId(int id) {
		this.id = id;
	}

	public void setDcName(String datacenterName) {
		this.datacenterName = datacenterName;
	}
	
	public String getDcName() {
		return this.datacenterName;
	}
	
	public void setOsmesisAppId(int appId){
		this.osmesisAppId = appId;
	}
	public int getOsmesisAppId(){
		return this.osmesisAppId;
	}

	private boolean isFinal; // used to check if it is the received cloud let 
	public void isFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}
	
	public boolean getIsFinal(){
		return this.isFinal;
	}

	public WorkflowInfo getWorkflowTag() {
		return workflowTag;
	}

	public void setWorkflowTag(WorkflowInfo workflowTag) {
		this.workflowTag = workflowTag;
	}
}
