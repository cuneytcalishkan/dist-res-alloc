package peersim.EP2400.resourcealloc.tasks;

import java.util.Collections;
import java.util.Comparator;

import peersim.EP2400.resourcealloc.base.Application;
import peersim.EP2400.resourcealloc.base.ApplicationsList;
import peersim.EP2400.resourcealloc.base.DistributedPlacementProtocol;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Linkable;
import peersim.core.Node;

/**
 * Template class for distributed application placement.
 * 
 */
public class DistributedResourceAllocation extends DistributedPlacementProtocol {

	private double loadEstimate;
	private int newApps = 0;

	public DistributedResourceAllocation(String prefix) {
		super(prefix);
	}

	public DistributedResourceAllocation(String prefix,
			double cpu_capacity_value) {
		super(prefix, cpu_capacity_value);
	}

	public void nextCycle(Node node, int protocolID) {
		int linkableID = FastConfig.getLinkable(protocolID);
		Linkable linkable = (Linkable) node.getProtocol(linkableID);

		int degree = linkable.degree();
		int nbIndex = CommonState.r.nextInt(degree);
		Node peer = linkable.getNeighbor(nbIndex);
		// The selected peer could be inactive
		if (!peer.isUp())
			return;

		DistributedResourceAllocation n_prime = (DistributedResourceAllocation) peer
				.getProtocol(protocolID);
		// Agree on the same system load value by converging to the average
		// system load
		double loadPrime = n_prime.passiveLoadEstimator(getLoadEstimate());
		this.setLoadEstimate((loadPrime + getLoadEstimate()) / 2);
		setNewApps(0);
		// send and receive message by method call. This follows the
		// cycle-driven simulation approach.
		ApplicationsList A_n_prime = n_prime.passiveThread(this
				.applicationsList());
		this.updatePlacement(A_n_prime);
	}

	/**
	 * Sets the current load estimation about the system to the average of the
	 * calling peer's estimation and its own estimation
	 * 
	 * @param load
	 *            Current load estimation of the calling peer about the system
	 * @return Current load estimation of this peer about the system
	 */
	public double passiveLoadEstimator(double load) {
		double tempLoad = getLoadEstimate();
		setLoadEstimate((tempLoad + load) / 2);
		return tempLoad;
	}

	public ApplicationsList passiveThread(ApplicationsList A_n_prime) {
		ApplicationsList tempA_n = this.applicationsList();
		this.updatePlacement(A_n_prime);
		return tempA_n;
	}

	public void updatePlacement(ApplicationsList A_n_prime) {
		// TODO Implement your code for task 2 here
		double peerLoad = A_n_prime.totalCPUDemand();
		double totalDemand = getTotalDemand();
		double cpuCapacity = getCpuCapacity();
		double var = Math.abs(peerLoad - totalDemand);
		// Overload scenario
		if (loadEstimate >= cpuCapacity) {
			// Check who has more load, so that it can share with the other
			Application appToSwitch = null;
			if (peerLoad == totalDemand)
				return;
			else if (peerLoad > totalDemand) {
				appToSwitch = eliminateAppsGTVar(A_n_prime, var);
				if (appToSwitch != null) {
					allocateApplication(appToSwitch);
					incrementNewApps();
				}
			} else {
				appToSwitch = eliminateAppsGTVar(applicationsList(), var);
				if (appToSwitch != null) {
					deallocateApplication(appToSwitch);
				}
			}
		} else {
			// TODO underload scenario
			ApplicationsList appsToSwitch = null;
			if (peerLoad == cpuCapacity)
				return;
			else if (peerLoad > cpuCapacity) {// Peer is overloaded
				if (totalDemand < cpuCapacity) {// Node is underloaded
					var = cpuCapacity - totalDemand;
					appsToSwitch = getAppsToSwitch(A_n_prime, var);
					for (Application app : appsToSwitch) {
						allocateApplication(app);
						incrementNewApps();
					}
				}// else Node is overloaded
			} else { // Peer is underloaded
				if (totalDemand == cpuCapacity)
					return;
				else if (totalDemand > cpuCapacity) { // Node is overloaded
					var = cpuCapacity - peerLoad;
					appsToSwitch = getAppsToSwitch(applicationsList(), var);
					for (Application app : appsToSwitch) {
						deallocateApplication(app);
					}
				} else { // Node is underloaded
					if (peerLoad == totalDemand)
						return;
					else if (peerLoad < totalDemand) {
						var = cpuCapacity - totalDemand;
						appsToSwitch = getAppsToSwitch(A_n_prime, var);
						for (Application app : appsToSwitch) {
							allocateApplication(app);
							incrementNewApps();
						}
					} else {
						var = cpuCapacity - peerLoad;
						appsToSwitch = getAppsToSwitch(applicationsList(), var);
						for (Application app : appsToSwitch) {
							deallocateApplication(app);
						}
					}
				}
			}
		}
	}

	private ApplicationsList getAppsToSwitch(ApplicationsList A_n_prime,
			double var) {
		ApplicationsList appsToSwitch = new ApplicationsList();
		Collections.sort(A_n_prime, new AppDemandComparator());
		double sum = 0;
		boolean loop = true;
		int i = 0;
		while ((i < A_n_prime.size()) && loop) {
			if ((sum + A_n_prime.get(i).getCPUDemand()) <= var) {
				appsToSwitch.add(A_n_prime.get(i));
				sum += A_n_prime.get(i).getCPUDemand();
				i++;
				incrementNewApps();
			} else
				loop = false;
		}
		return appsToSwitch;
	}

	private Application eliminateAppsGTVar(ApplicationsList apps, double var) {
		Application result = null;
		Collections.sort(apps, new AppDemandComparator());
		Collections.reverse(apps);
		for (Application app : apps) {
			if (app.getCPUDemand() <= var / 2) {
				result = app;
				break;
			}
		}
		if ((result == null) && (!apps.isEmpty())) {
			result = apps.get(apps.size() - 1);
		}
		return result;
	}

	public Object clone() {
		DistributedResourceAllocation proto = new DistributedResourceAllocation(
				this.prefix, this.cpuCapacity);
		return proto;
	}

	public double getLoadEstimate() {
		return loadEstimate;
	}

	public void setLoadEstimate(double loadEstimate) {
		this.loadEstimate = loadEstimate;
	}

	public int getNewApps() {
		return newApps;
	}

	public void setNewApps(int newApps) {
		this.newApps = newApps;
	}

	private void incrementNewApps() {
		this.newApps++;
	}

	class AppDemandComparator implements Comparator<Application> {

		@Override
		public int compare(Application o1, Application o2) {
			if (o1.getCPUDemand() < o2.getCPUDemand())
				return -1;
			else if (o1.getCPUDemand() > o2.getCPUDemand())
				return 1;
			return 0;
		}

	}

}
