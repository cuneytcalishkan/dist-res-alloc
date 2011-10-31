package peersim.EP2400.resourcealloc.tasks;

import java.io.FileWriter;
import java.io.IOException;

import peersim.EP2400.resourcealloc.base.Application;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.util.IncrementalStats;

/**
 * Template class for Performance observer according to metrics discussed in the
 * project description
 */
public class PerformanceObserver implements Control {

	/**
	 * The protocol to operate on.
	 * 
	 * @config
	 */
	private static final String PAR_PROT = "protocol";

	/**
	 * The number of applications
	 * 
	 * @config
	 */
	private static final String PAR_APPSCOUNT = "apps_count";

	private static final String PAR_R_MAX = "r_max";

	/** Protocol identifier, obtained from config property {@link #PAR_PROT}. */
	private final int pid;

	private final String prefix;
	private final int r_max;

	/**
	 * Number of application
	 */
	protected int appsCount;

	/**
	 * Standard constructor that reads the configuration parameters. Invoked by
	 * the simulation engine.
	 * 
	 * @param prefix
	 *            the configuration prefix identifier for this class.
	 */
	public PerformanceObserver(String prefix) {
		this.prefix = prefix;
		pid = Configuration.getPid(prefix + "." + PAR_PROT);
		appsCount = Configuration.getInt(prefix + "." + PAR_APPSCOUNT);
		r_max = Configuration.getInt(prefix + "." + PAR_R_MAX);

	}

	@Override
	public boolean execute() {

		int cycle = (int) CommonState.getTime();
		double S = 0;
		double V = 0;
		double C = 0;
		double R = 0;
		int satisfied = 0;
		int activeServers = 0;
		int newApps = 0;
		IncrementalStats is = new IncrementalStats();
		IncrementalStats isal = new IncrementalStats();
		for (int i = 0; i < Network.size(); i++) {
			DistributedResourceAllocation protocol = (DistributedResourceAllocation) Network
					.get(i).getProtocol(pid);
			isal.add(protocol.getLoadEstimate());
			for (Application a : protocol.applicationsList()) {
				if (a.getCPUDemand() <= a.getExpectedCPUDemand())
					satisfied++;
			}
			is.add(protocol.getTotalDemand());
			if (protocol.appsCount() > 0)
				activeServers++;
			newApps += protocol.getNewApps();
		}
		S = (double) satisfied / appsCount;
		V = is.getStD() / is.getAverage();
		R = (double) activeServers / Network.size();
		C = (double) newApps / appsCount;
		// TODO Implement your code for task 1.2 here
		try {
			FileWriter fw1 = new FileWriter("sim-results/cycles.csv", true);
			FileWriter fw2 = new FileWriter("sim-results/epochs.csv", true);

			fw1.write(cycle + "," + V + "," + S + "," + R + "," + C + ","
					+ (Network.size() - activeServers) + "\n");
			if (cycle % r_max == 0) {
				fw2.write((cycle + 1) / r_max + "," + V + "," + S + "," + R
						+ "," + C + "\n");
			}
			fw1.close();
			fw2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

}
