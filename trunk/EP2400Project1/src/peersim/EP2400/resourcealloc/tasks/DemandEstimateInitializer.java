package peersim.EP2400.resourcealloc.tasks;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

public class DemandEstimateInitializer implements Control {

	/**
	 * The protocol to operate on.
	 * 
	 * @config
	 */
	private static final String PAR_PROT = "protocol";

	/** Protocol identifier, obtained from config property {@link #PAR_PROT}. */
	protected final int protocolID;

	public DemandEstimateInitializer(String prefix) {
		protocolID = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	@Override
	public boolean execute() {

		for (int i = 0; i < Network.size(); i++) {

			DistributedResourceAllocation p = (DistributedResourceAllocation) Network
					.get(i).getProtocol(protocolID);
			p.setLoadEstimate(p.getTotalDemand());
		}

		return false;
	}

}
