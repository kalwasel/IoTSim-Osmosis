package org.cloudbus.cloudsim.edge.iot.protocol;


import org.cloudbus.cloudsim.edge.iot.protocol.IoTProtocol;



public class XMPPProtocol extends IoTProtocol {
	private static final float BATTERY_DRAINAGE_RATE=1.50f;
	private static final float TRANSIMISON_SPEED=3.00f;

    public XMPPProtocol() {
    		super("XMPP",BATTERY_DRAINAGE_RATE,TRANSIMISON_SPEED);
    }

}