/*
 * Title:         IoTSim-SDWAN 1.0
 * Description:  IoTSim-SDWAN facilitates the modeling, simulating, and evaluating of new algorithms, policies, and designs
 * 				 in the context of SD-WAN ecosystems and SDN-enabled multiple cloud datacenters.
 * 
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2020, Newcastle University (UK) and Saudi Electronic University (Saudi Arabia) 
 * 
 */

package org.cloudbus.cloudsim.sdwan;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-SDWAN 1.0
 * 
**/

public class ApplicationLayer {
	private int sourceAppId;
	private int destAppId;
	
	public ApplicationLayer(int sourceAppId, int destAppId){
		
	}
	
	public int getSourceAppId() {
		return sourceAppId;
	}
	public void setSourceAppId(int sourceAppId) {
		this.sourceAppId = sourceAppId;
	}
	public int getDestAppId() {
		return destAppId;
	}
	public void setDestAppId(int destAppId) {
		this.destAppId = destAppId;
	}
}
