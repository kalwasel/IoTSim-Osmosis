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

import org.cloudbus.osmosis.core.polocies.SDNTrafficSchedulingPolicy;
import org.cloudbus.osmosis.core.polocies.SDNRoutingPolicy;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public class CloudSDNController extends SDNController {	
	
	public CloudSDNController(String name, SDNTrafficSchedulingPolicy sdnPolicy, SDNRoutingPolicy sdnRouting){
		super(name, sdnPolicy,sdnRouting);		
	}
}