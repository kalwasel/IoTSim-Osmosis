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

import org.cloudbus.cloudsim.Vm;

/**
 * This class represents a TCP protocol that logically operates 
 * in the transport layer of TCP/IP architecture. Data is passed from
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-SDWAN 1.0
 * 
**/

public class TCPremove implements TransportLayer{
	/*
	 * Each data Header size of each segment created 
	 */
	private double headerSize;
	private double windowSize;
	private double rtt; // round trip time of a packet 

	
	private Vm sourceVm;
	private Vm destVm;
	
	public TCPremove(double headerSize, double windowSize) {
		super();		
		this.headerSize = headerSize;
		this.windowSize = windowSize; 
	}
	
	public double getHeaderSize() {
		return headerSize;
	}
	public void setHeaderSize(double headerSize) {
		this.headerSize = headerSize;
	}
	public double getWindowSize() {
		return windowSize;
	}
	public void setWindowSize(double windowSize) {
		this.windowSize = windowSize;
	}
	public double getRtt() {
		return rtt;
	}
	public void setRtt(double rtt) {
		this.rtt = rtt;
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
