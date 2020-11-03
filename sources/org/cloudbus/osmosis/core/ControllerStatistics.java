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

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public class ControllerStatistics {
	
	private String appName;	
	private int flowId;	
	private int source ; // --> mapper, reducer, HDFS, VM etc. 
	private int destination; 	// --> mapper, reducer, HDFS, VM etc.
	private long flowSize;		
	private double startTime = -1; 
	private double finishTime = -1;	 
	private String srcName;   
	private String destName;					
	private String datacenterName;
	private String controllerName;

	public ControllerStatistics(String appName, int flowId, int source, int destination, long flowSize,
			String srcName, String destName, String datacenterName, String controllerName) {		
		this.appName = appName;
		this.flowId = flowId;
		this.source = source;
		this.destination = destination;
		this.flowSize = flowSize;
		this.srcName = srcName;
		this.destName = destName;
		this.datacenterName = datacenterName;
		this.controllerName = controllerName;
	}

	
	public ControllerStatistics(){
		
	}
	
	public String getDatacenterName() {
		return datacenterName;
	}

	public void setDatacenterName(String datacenterName) {
		this.datacenterName = datacenterName;
	}	

	public void setAppName(String appName){
		this.appName = appName;
	}
	public String getAppName(){
		return this.appName;
	}
	
	public int getSource() {
		return source;
	}

	public int getDestination() {
		return destination;
	}

	public long getSize() {
		return flowSize;
	}
	
	public void setFlowSize(long size) {
		this.flowSize = size;
	}

	public int getFlowId() {
		return flowId;
	}
	
	public void setFlowId(int flowId) {
		this.flowId = flowId;
	}
	
	public void setStartTime(double time) {
		this.startTime = time;
	}
	
	public void setFinishTime(double time) {
		this.finishTime = time;	
	}
	
	public double getStartTime() {
		return this.startTime;
	}
	public double getFinishTime() {
		return this.finishTime;
	}

	public String getSrcName() {
		return srcName;
	}

	public String getDestName() {
		return destName;
	}


	public String getControllerName() {
		return this.controllerName;
	}
}
