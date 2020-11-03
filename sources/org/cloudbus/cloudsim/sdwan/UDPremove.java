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
 * This class represents a UDP protocol that logically operates 
 * in the transport layer of TCP/IP architecture. 
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-SDWAN 1.0
 * 
**/

public class UDPremove implements TransportLayer{
	private double headerSize;
	private double datagramSize;
	private Vm sourceVm;
	private Vm destVm;

	public UDPremove(double headerSize, double datagramSize){
		this.headerSize = headerSize;
		this.datagramSize = datagramSize;
	}	
	
	public double getHeaderSize() {
		return headerSize;
	}

	public void setHeaderSize(double headerSize) {
		this.headerSize = headerSize;
	}

	public double getDatagramSize() {
		return datagramSize;
	}

	public void setDatagramSize(double datagramSize) {
		this.datagramSize = datagramSize;
	}

	@Override
	public Vm getVmSource() {
		return this.sourceVm;
	}

	@Override
	public Vm getVmDest() {
		return this.destVm;
	}

	@Override
	public void setVmSource(Vm vm) {
		this.sourceVm = vm;
	}

	@Override
	public void setVmDes(Vm vm) {
		this.destVm = vm;
	}

}
