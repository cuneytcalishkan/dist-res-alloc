package peersim.EP2400.resourcealloc.tasks;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;

public class DemandEstimateController implements Control {

	/**
	 * The protocol to operate on.
	 * 
	 * @config
	 */
	private static final String PAR_PROT = "protocol";
	private static final String PAR_R_MAX = "r_max";
	/** Protocol identifier, obtained from config property {@link #PAR_PROT}. */
	protected final int protocolID;

	protected final int r_max;

	public DemandEstimateController(String prefix) {
		protocolID = Configuration.getPid(prefix + "." + PAR_PROT);
		r_max = Configuration.getInt(prefix + "." + PAR_R_MAX);
	}

	@Override
	public boolean execute() {
		int cycle = (int) CommonState.getTime();
		if ((cycle + 1) % r_max == 0)
			for (int i = 0; i < Network.size(); i++) {

				DistributedResourceAllocation p = (DistributedResourceAllocation) Network
						.get(i).getProtocol(protocolID);
				p.setLoadEstimate(p.getTotalDemand());
				p.setNewApps(0);
			}

		return false;
	}

}
