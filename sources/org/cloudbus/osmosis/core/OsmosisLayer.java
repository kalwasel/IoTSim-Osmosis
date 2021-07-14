package org.cloudbus.osmosis.core;
/**
 *
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 *
 * This class stores the graph of an osmotic application
 * (e.g., from IoT layer to edge 1 to edge 2 to cloud 1)
 **/
public class OsmosisLayer {
    String sourceName; // IoT device, MEL 1, etc.
    String destName; // IoT device, MEL 1, etc.

    private int sourceId;
    private int destId;


    long osmoticLetSize; // source MEL processing size
    long osmoticPktSize; // source MEL transmission size

    String sourceLayerName; // IoT layer, edge datacenter 1, etc.
    String destLayerName; //  IoT layer, edge datacenter 1, etc.



    private int sourceLayerId;
    private int destLayerId;

    public OsmosisLayer(String sourceName, String sourceLayerName, long osmoticLetSize, long osmoticPktSize, String destName, String destLayerName) {
    this.setSourceName(sourceName);
    this.setSourceLayerName(sourceLayerName);
    this.setOsmoticLetSize(osmoticLetSize);
    this.setOsmoticPktSize(osmoticPktSize);
    this.setDestName(destName);
    this.setDestLayerName(destLayerName);
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public void setOsmoticLetSize(long osmoticLetSize) {
        this.osmoticLetSize = osmoticLetSize;
    }

    public void setOsmoticPktSize(long osmoticPktSize) {
        this.osmoticPktSize = osmoticPktSize;
    }

    public void setSourceLayerName(String sourceLayerName) {
        this.sourceLayerName = sourceLayerName;
    }

    public void setDestLayerName(String destLayerName) {
        this.destLayerName = destLayerName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getDestName() {
        return destName;
    }

    public long getOsmoticLetSize() {
        return osmoticLetSize;
    }

    public long getOsmoticPktSize() {
        return osmoticPktSize;
    }

    public String getSourceLayerName() {
        return sourceLayerName;
    }

    public String getDestLayerName() {
        return destLayerName;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public void setDestId(int destId) {
        this.destId = destId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public int getDestId() {
        return destId;
    }

    public void setSourceLayerId(int sourceLayerId) {
        this.sourceLayerId = sourceLayerId;
    }

    public void setDestLayerId(int destLayerId) {
        this.destLayerId = destLayerId;
    }

    public int getSourceLayerId() {
        return sourceLayerId;
    }

    public int getDestLayerId() {
        return destLayerId;
    }
}
