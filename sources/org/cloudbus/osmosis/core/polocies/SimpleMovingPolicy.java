package org.cloudbus.osmosis.core.polocies;

import org.cloudbus.cloudsim.edge.core.edge.Mobility;
import org.cloudbus.cloudsim.edge.core.edge.Mobility.Location;

public class SimpleMovingPolicy implements MovingPolicy{

	/**
	 * moving a straight line, when reach the end, it will reverse the direction.
	 */
	@Override
	public void updateLocation(Mobility mobility) {

		Location location = mobility.location;
		location.x += mobility.velocity;
		mobility.totalMovingDistance+=Math.abs(mobility.velocity);
		if((location.x>=mobility.range.endX) || (location.x<=mobility.range.beginX)) {
			mobility.velocity=-mobility.velocity;
			if(location.x>mobility.range.endX) {
				location.x=mobility.range.endX;
			}
			if(location.x<mobility.range.beginX) {
				location.x=mobility.range.beginX;
			}

		}

	}

}
