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
import org.cloudbus.cloudsim.edge.core.edge.Mobility.Location;
import lombok.Data;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

@Data
public class ConfiguationEntity {
	private LogEntity logEntity;
	private boolean trace_flag;	
	
	private List<EdgeDataCenterEntity> edgeDatacenter;
	private List<CloudDataCenterEntity> cloudDatacenter;
	private List<WanEntity> sdwan;
//    private List<ConnectionEntity> connections;
	
	@Data
	public static class LogEntity {
		private String logLevel;
		private boolean saveLogToFile;
		private String logFilePath;
		private boolean append;
	}
	
	@Data
	public static class EdgeDataCenterEntity {
		private String name;
		private String type;
		private double schedulingInterval;		
		private VmAllcationPolicyEntity vmAllocationPolicy;
		private EdgeDatacenterCharacteristicsEntity characteristics;
		private List<EdgeDeviceEntity> hosts;		
		private List<MELEntities> MELEntities;		
		private List<ControllerEntity> controllers;
		private List<SwitchEntity> switches;		
		private List<LinkEntity> links;
		private List<IotDeviceEntity> ioTDevices;
		private List<WirelessConnections> IoT_MEL_Wireless_Connections;
	}

	@Data
	public static class CloudDataCenterEntity {
	    private String name;		
		private String type;
	    private String vmAllocationPolicy;	    	   	    
	    private List<HostEntity> hosts;
	    private List<VMEntity> VMs;
	    private List<ControllerEntity> controllers;
	    private List<SwitchEntity> switches;
	    private List<LinkEntity> links;
	}
	
	@Data
	public class WanEntity {
	    private ControllerEntity controllers;
	    private List<LinkEntity> links;
	    private List<SwitchEntity> switches;
	}

	@Data
	public static class VmAllcationPolicyEntity {

		String className;
		List<HostEntity> hostEntities;
	}

	@Data
	public static class EdgeDeviceEntity {
		String name;		
		long storage;
		int pes;
		int ramSize;
		int mips;
		int bwSize;					
	}
	
	@Data
	public static class HostEntity{
	    private String name;
	    private long pes;
	    private long mips;
	    private Integer ram;
	    private Long storage;	  
	    private long bw;
	}
	
	@Data 
	public static class VMEntity{
		String name; 		
		int pes;
		double mips;
		int ram;
		double storage;
		private long bw;
		String cloudletPolicy; 
	}
	
	@Data
	public class ControllerEntity{
	    private String name;
	    private String trafficPolicy;
	    private String routingPolicy;
	}
	
	@Data
	public class SwitchEntity{
	    private String type;  // enum
	    private String name;
	    private String controller;
	    private Long iops;	    

	    public boolean isGateway(){
	        return this.type.equals("gateway");
	    }
	}
	
	@Data
	public class LinkEntity{
	    private String source;
	    private String destination;
	    private long bw;
	}
	
	@Data
	public static class EdgeDatacenterCharacteristicsEntity {
		String architecture;
		String os;
		String vmm;
		double cost;
		double timeZone;
		double costPerSec;
		double costPerMem;
		double costPerStorage;
		double costPerBw;			
	}

	@Data
	public static class MELEntities {
		String name;
		String host;		
		int mips;
		int ram; // vm memory (MB)
		long bw;
		int pesNumber; // number of cpus
		String vmm; // VMM name
		String cloudletSchedulerClassName;		
		float datasizeShrinkFactor;

	}
	
	@Data
	public static class IotDeviceEntity {
		private MobilityEntity mobilityEntity;				
		public String ioTClassName;		
		String name;
		double data_frequency;
		double dataGenerationTime;
		int complexityOfDataPackage;
		int dataSize;
		NetworkModelEntity networkModelEntity; // e.g. Wifi and xmpp
		double max_battery_capacity;
		double battery_sensing_rate;
		double battery_sending_rate;		
		double processingAbility;
		EdgeLetEntity dataTemplate;
		double bw;

	}

	@Data
	public static class WirelessConnections {
		private int vmId;
		private int assigmentIoTId; 
	}
	
	@Data
	public static class EdgeLetEntity {

		int cloudletId;
		long cloudletLength;
		int pesNumber;
		long cloudletFileSize;
		long cloudletOutputSize;
		String utilizationModelCpu;
		String utilizationModelRam;
		String utilizationModelBw;

	}

	@Data
	public static class NetworkModelEntity {

		private String networkType;
		private String communicationProtocol;

	}

	@Data
	public static class MobilityEntity {
		private boolean movable;
		private double velocity;
		private MovingRangeEntity range;
		private double signalRange;
		private Location location;

		public MobilityEntity(Location location) {
			super();
			this.location = location;
		}

		public MobilityEntity() {
			super();
		}
	}

	public static class MovingRangeEntity {
		public int beginX;
		public int endX;

		public MovingRangeEntity() {
			super();
		}

		public MovingRangeEntity(int beginX, int endX) {
			super();
			this.beginX = beginX;
			this.endX = endX;
		}

	}

}
