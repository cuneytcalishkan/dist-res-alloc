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
public class ChangingLoadRandomDemandGenerator extends DemandGenerator {

	private double maxApplicationDemand;
	private int r_max;
	private static final String PAR_MAX_APPS_DEMAND = "max_application_demand";
	private static final String PAR_R_MAX = "r_max";
	
	public ChangingLoadRandomDemandGenerator(String prefix) {
		super(prefix);
		maxApplicationDemand = Configuration.getDouble(prefix + "."
				+ PAR_MAX_APPS_DEMAND);
		r_max = Configuration.getInt(prefix + "."
				+ PAR_R_MAX);
		
	}

	
	
	@Override
	public boolean execute() {
		int cycle = (int) CommonState.getTime();
		if (cycle % r_max == 0) 
		{
		String demandChangeMsg = String
						.format("CPU Demands for all applications are changed for next cycle");
				System.out.println(demandChangeMsg);
				ApplicationsList appsList = ApplicationsManager.getInstance()
						.applications();
				
		int epoch = cycle / r_max;
				
		double effectiveF        = 0.25 * ( 0.1 + epoch * 1.4 / 99);
		
		for (Application a : appsList) 
		   {
				double nextCPUDemand = Math.min(maxApplicationDemand, effectiveF * maxApplicationDemand * 2 * CommonState.r.nextDouble());
				a.setCPUDemand(nextCPUDemand);
				
				
				
				double expectedCPUDemand = Math.min(maxApplicationDemand, 2 * CommonState.r.nextDouble() * effectiveF * maxApplicationDemand);
				a.setExpectedCPUDemand(expectedCPUDemand);
				
			}
		}
				
		return false;
	}

}
