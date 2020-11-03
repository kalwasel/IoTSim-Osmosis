package org.cloudbus.cloudsim.edge.iot.protocol;


import org.cloudbus.cloudsim.edge.iot.protocol.IoTProtocol;

public class AMQPProtocol extends IoTProtocol {
	public static final float BATTERY_DRAINAGE_RATE=1.0f;
	public static final float TRANSIMISON_SPEED=1.0f;

    public AMQPProtocol() {	
    	super("XMPP",BATTERY_DRAINAGE_RATE,TRANSIMISON_SPEED);    	
    }
}