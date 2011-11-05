package peersim.EP2400.resourcealloc.tasks;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

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
		double cpuDemand = 0;
		double cpuCapacity = 0;
		DecimalFormat df = new DecimalFormat("####.######");
		DecimalFormat cf = new DecimalFormat();
		cf.setMinimumIntegerDigits(4);
		cf.setMaximumFractionDigits(0);
		cf.setGroupingUsed(false);
		df.setMinimumFractionDigits(6);
		IncrementalStats is = new IncrementalStats();
		IncrementalStats isal = new IncrementalStats();

		for (int i = 0; i < Network.size(); i++) {
			DistributedResourceAllocation protocol = (DistributedResourceAllocation) Network
					.get(i).getProtocol(pid);
			cpuDemand = protocol.getTotalDemand();
			cpuCapacity = protocol.getCpuCapacity();

			isal.add(protocol.getLoadEstimate());
			is.add(protocol.getTotalDemand());
			for (Application a : protocol.applicationsList()) {
				if (a.getCPUDemand() <= getAllocatedCpu(cpuCapacity,
						a.getCPUDemand(), cpuDemand))
					satisfied++;
			}
			if (protocol.appsCount() > 0)
				activeServers++;
			if ((cycle + 1) % r_max == 0) {
				newApps += protocol.getNewApps();
			}
		}
		S = (double) satisfied / appsCount;
		V = is.getStD() / is.getAverage();
		R = (double) activeServers / Network.size();
		C = (double) newApps / appsCount;

		try {
			FileWriter fw1 = new FileWriter("sim-results/cycles.tsv", true);
			FileWriter fw2 = new FileWriter("sim-results/epochs.tsv", true);
			FileWriter fw3 = new FileWriter("sim-results/cyclon_output.tsv",
					true);

			fw1.write(cf.format(cycle + 1) + "\t" + df.format(V) + "\t"
					+ df.format(S) + "\t" + df.format(R) + "\n");
			if ((cycle + 1) % r_max == 0) {
				fw2.write(cf.format((cycle / r_max) + 1) + "\t" + df.format(V)
						+ "\t" + df.format(S) + "\t" + df.format(R) + "\t"
						+ df.format(C) + "\n");
			}
			// fw3.write(cf.format(cycle + 1) + "\t" + df.format(isal.getVar())
			// + "\n");
			fw1.close();
			fw2.close();
			fw3.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	private double getAllocatedCpu(double cpuCapacity, double appDemand,
			double totalCpuDemand) {
		return (appDemand / totalCpuDemand) * cpuCapacity;
	}

}
