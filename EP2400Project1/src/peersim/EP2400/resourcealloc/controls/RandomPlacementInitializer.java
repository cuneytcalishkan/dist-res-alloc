/*
 * Copyright (c) 2010 LCN, EE school, KTH
 *
 */

package peersim.EP2400.resourcealloc.controls;

import peersim.EP2400.resourcealloc.base.Application;
import peersim.EP2400.resourcealloc.base.ApplicationsManager;
import peersim.EP2400.resourcealloc.base.DistributedPlacementProtocol;
import peersim.core.*;

/**
 * This class provides a simple random initializer where applications are
 * randomly allocated to each node
 * 
 * @author Rerngvit Yanggratoke
 */
public class RandomPlacementInitializer extends PlacementInitializer {
	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	public RandomPlacementInitializer(String prefix) {
		super(prefix);
	}

	public boolean execute() {
		super.execute();

		for (Application a : ApplicationsManager.getInstance().applications()) {
			int nsize = Network.size();
			int nodeIndex = CommonState.r.nextInt(nsize);
			DistributedPlacementProtocol p = ((DistributedPlacementProtocol) Network
					.get(nodeIndex).getProtocol(protocolID));
			p.allocateApplication(a);

		}

		return false;
	}

}
