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

package org.cloudbus.cloudsim.sdwan;

import org.cloudbus.cloudsim.Vm;

/**
 * This interface represents the behaviours and characteristics 
 * of transport layer defined in TCP/IP architecture. 
 * This interface should contain the common shared behaviours of TCP and UDP. 
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-SDWAN 1.0
 * 
**/

public interface TransportLayer {
	public Vm getVmSource();
	public Vm getVmDest();
	public void setVmSource(Vm vm);
	public void setVmDes(Vm vm);
}
