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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-SDWAN 1.0
 * 
**/

public class OsmesisAppsParser {
	public static List<OsmesisAppDescription> appList = new ArrayList<>();
	public static void startParsingExcelAppFile(String appFileName){
			
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
				double DataRate; // in seconds 
				double StopDataGenerationTime; // in seconds 
				String IoTDeviceName;
				long IoTDeviceOutput; // in Mb 
				String MELName; // mel resides in an edge device 
				long OsmesisEdgeletSize; //MI
				long MELOutput; // Mb
				String VmName;
				long OsmesisCloudlet; // MI
												
				while ((line = br.readLine()) != null) {					
					String[] splitLine = line.split(",");
					Queue<String> lineitems = new LinkedList<String>(Arrays.asList(splitLine));
					// app0,49,740773,1,2339561,1,627471,0,0,0,None
					if(lineitems.isEmpty()){
						break;
					}
					
					OsmesisAppName = lineitems.poll();
					
					appID = Integer.parseInt(lineitems.poll());
					DataRate = Double.parseDouble(lineitems.poll());
					StopDataGenerationTime = Double.parseDouble(lineitems.poll());
					IoTDeviceName = lineitems.poll();					
					IoTDeviceOutput = 	Long.parseLong(lineitems.poll());
					MELName =   lineitems.poll();		
					OsmesisEdgeletSize =	Long.parseLong(lineitems.poll());
					MELOutput =	Long.parseLong(lineitems.poll());
					VmName = lineitems.poll();		
					OsmesisCloudlet = Long.parseLong(lineitems.poll());								
					
					OsmesisAppDescription appComposition = new OsmesisAppDescription(OsmesisAppName, appID, DataRate, StopDataGenerationTime,
							IoTDeviceName,IoTDeviceOutput, MELName, OsmesisEdgeletSize, MELOutput, VmName, OsmesisCloudlet);
										
					appList.add(appComposition);
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
