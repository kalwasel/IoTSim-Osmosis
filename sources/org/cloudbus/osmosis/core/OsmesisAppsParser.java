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

package org.cloudbus.osmosis.core;

import org.cloudbus.osmosis.core.OsmosisLayer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public class OsmesisAppsParser {
	public static List<OsmesisAppDescription> appList = new ArrayList<>();
	public static void startParsingExcelAppFile(String appFileName) {

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(appFileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String line;
		try {
			@SuppressWarnings("unused")
			String head = br.readLine();

			String OsmesisAppName;
			int appID;
			int NumOfLayer;
			double DataRate; // in seconds
			double StopDataGenerationTime; // in seconds

			String sourceName; // IoT device, MEL 1, etc.
			String destName; // IoT device, MEL 1, etc.

			long osmoticLetSize; // source MEL processing size
			long osmoticPktSize; // source MEL transmission size

			String sourceLayerName; // IoT layer, edge datacenter 1, etc.
			String destLayerName; //  IoT layer, edge datacenter 1, etc.


			String IoTDeviceName;
			String IoTLayerName; // this allows to have multiple IoT layer
			long OsmesisIoTLet_MI; // IoT device processing size
			long IoTDeviceOutput; // in Mb
			String MELName; // mel resides in an edge device
			long OsmesisEdgeletSize; //MI
			long MELOutput; // Mb

			// renewable energy
			String MELName_2; // mel resides in another edge datacenter
			long OsmesisEdgeletSize_2; //MI
			long MELOutput_2; // Mb

			String VmName;
			long OsmesisCloudlet; // MI
//				List<String> melDatacenter_Name = new ArrayList<>();
			Map<String, String> melDatacenter_Name = new HashMap<>();
			while ((line = br.readLine()) != null) {
				String[] splitLine = line.split(",");
				Queue<String> lineitems = new LinkedList<String>(Arrays.asList(splitLine));
				// app0,49,740773,1,2339561,1,627471,0,0,0,None
				if (lineitems.isEmpty()) {
					break;
				}

				OsmesisAppName = lineitems.poll();

				appID = Integer.parseInt(lineitems.poll());
				NumOfLayer = Integer.parseInt(lineitems.poll());
				DataRate = Double.parseDouble(lineitems.poll());
				StopDataGenerationTime = Double.parseDouble(lineitems.poll());
				sourceName = lineitems.poll(); // IoT device, mel 1, mel 2, etc.
				sourceLayerName = lineitems.poll(); // IoT, edge datacenter 1, etc.
				List<OsmosisLayer> layers = new ArrayList<>(); // NumOfLayer (IoT, edges, clouds)
				for (int i = 1; i <= NumOfLayer; i++) {

					if(i == NumOfLayer){
						osmoticLetSize = Long.parseLong(lineitems.poll());
						OsmosisLayer layer = new OsmosisLayer(sourceName, sourceLayerName, osmoticLetSize, 0, null, null);
						layers.add(layer);
						break;
					}

					osmoticLetSize = Long.parseLong(lineitems.poll());
					osmoticPktSize = Long.parseLong(lineitems.poll());
					destName = lineitems.poll(); // IoT device, mel 1, mel 2, etc.
					destLayerName = lineitems.poll(); // IoT, edge datacenter 1, etc.
					OsmosisLayer layer = new OsmosisLayer(sourceName, sourceLayerName, osmoticLetSize, osmoticPktSize, destName, destLayerName);
					layers.add(layer);
					sourceName = destName; // IoT device, mel 1, mel 2, etc.
					sourceLayerName = destLayerName; // IoT, edge datacenter 1, etc.
				}
				OsmesisAppDescription appComposition = new OsmesisAppDescription(OsmesisAppName, appID, DataRate, StopDataGenerationTime,
						layers);

				appList.add(appComposition);

//					IoTDeviceName = lineitems.poll();
//					IoTLayerName = lineitems.poll();
//					OsmesisIoTLet_MI = Long.parseLong(lineitems.poll());
//					IoTDeviceOutput = 	Long.parseLong(lineitems.poll());
//					MELName =   lineitems.poll();
////					melDatacenter_Name.add(lineitems.poll()); // edge datacenter 1
//					melDatacenter_Name.put(MELName,lineitems.poll());// edge datacenter 1
//					OsmesisEdgeletSize =	Long.parseLong(lineitems.poll());
//					MELOutput =	Long.parseLong(lineitems.poll());
//
//					// renewable energy
//					MELName_2 =   lineitems.poll();
////					melDatacenter_Name.add(lineitems.poll()); // edge datacenter 2
//					melDatacenter_Name.put(MELName_2,lineitems.poll());// edge datacenter 2
//					OsmesisEdgeletSize_2 =	Long.parseLong(lineitems.poll());
//					MELOutput_2 =	Long.parseLong(lineitems.poll());
//
//					VmName = lineitems.poll();
////					melDatacenter_Name.add(lineitems.poll()); // cloud datacenter 3
//					melDatacenter_Name.put(VmName,lineitems.poll());// cloud datacenter 3
//
//					OsmesisCloudlet = Long.parseLong(lineitems.poll());

//					OsmesisAppDescription appComposition = new OsmesisAppDescription(OsmesisAppName, appID, DataRate, StopDataGenerationTime,
//							IoTDeviceName,IoTDeviceOutput, MELName, OsmesisEdgeletSize, MELOutput,
//							MELName_2, OsmesisEdgeletSize_2, MELOutput_2,
//							VmName, OsmesisCloudlet, melDatacenter_Name);
//
//					appList.add(appComposition);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
