package org.cloudbus.osmosis.core.polocies;

import org.cloudbus.cloudsim.edge.core.edge.Mobility;

public interface MovingPolicy {
	public void updateLocation(Mobility mobility);
}
