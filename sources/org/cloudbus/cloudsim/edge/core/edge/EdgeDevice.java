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



import java.util.List;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.edge.utils.LogUtil;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public class EdgeDevice extends Host {
	
	private String deviceName;	

	private boolean enabled;		
	
	public EdgeDevice(int id, String deviceName, RamProvisioner ramProvisioner, BwProvisioner bwProvisioner,
			long storage, List<? extends Pe> peList, VmScheduler vmScheduler) {
		
		super(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
		this.deviceName = deviceName;
		this.enabled = true;
	}
	

	public String getDeviceName() {
		return deviceName;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
