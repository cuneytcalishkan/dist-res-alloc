/*
 * Copyright (c) 2010 LCN, EE school, KTH
 *
 */

package peersim.EP2400.resourcealloc.controls;

import peersim.EP2400.resourcealloc.base.Application;
import peersim.EP2400.resourcealloc.base.ApplicationsList;
import peersim.EP2400.resourcealloc.base.ApplicationsManager;
import peersim.config.Configuration;
import peersim.core.CommonState;

/**
 * Class for application demand generator. This generator runs every r_max cycles.
 * @author rerng007
 *
 */
public class RandomDemandGenerator extends DemandGenerator {

	private double maxApplicationDemand;
	private int r_max;
	private static final String PAR_MAX_APPS_DEMAND = "max_application_demand";
	private static final String PAR_R_MAX = "r_max";
	
	public RandomDemandGenerator(String prefix) {
		super(prefix);
		maxApplicationDemand = Configuration.getDouble(prefix + "."
				+ PAR_MAX_APPS_DEMAND);
		r_max = Configuration.getInt(prefix + "."
				+ PAR_R_MAX);
		
	}

	
	
	@Override
	public boolean execute() {

		
		int cycle = (int) CommonState.getTime();

		if (cycle % r_max == 0) {

				String demandChangeMsg = String
						.format("CPU Demands for all applications are changed for next cycle");
				System.out.println(demandChangeMsg);
				ApplicationsList appsList = ApplicationsManager.getInstance()
						.applications();
				
				for (Application a : appsList) {
					double nextCPUDemand = Math.min(maxApplicationDemand, a.getExpectedCPUDemand() * 2 * CommonState.r.nextDouble());
					a.setCPUDemand(nextCPUDemand);
				}
				

			
		}
		
		return false;
	}

}
