/*
 * Copyright (c) 2010 LCN, EE school, KTH
 *
 */

package peersim.EP2400.resourcealloc.controls;

import peersim.EP2400.resourcealloc.base.Application;
import peersim.EP2400.resourcealloc.base.ApplicationsManager;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;

/**
 * Base class for application placement initializer.
 * @author rerng007
 *
 */
public abstract class PlacementInitializer implements Control{
	// ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    /**
     * The number of applications
     * 
     * @config
     */
    private static final String PAR_APPSCOUNT = "apps_count";


    /**
     * The Max CPU demand
     * 
     * @config
     */
    private static final String PAR_MAX_APPS_DEMAND= "max_application_demand";
    private static final String PAR_FRACTION_EXPECTED_DEMAND = "fraction_expected_demand";

    
    /**
     * The protocol to operate on.
     * 
     * @config
     */
    private static final String PAR_PROT = "protocol";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------
  
    /**
     * Fraction of expected application demand to CPU capacity
     */
    protected double f_expected;
	
    
    /**
     * Number of application
     */
    protected int appsCount;
    
    
    
    
    /**
     * Max Application Demand
     * 
     */
    protected double maxAppsDemand;
    
    /** Protocol identifier, obtained from config property {@link #PAR_PROT}. */
    protected final int protocolID;
    
   
    // ------------------------------------------------------------------------
    // Initialization
    // ------------------------------------------------------------------------
    /**
     * Standard constructor that reads the configuration parameters. Invoked by
     * the simulation engine.
     * 
     * @param prefix
     *            the configuration prefix for this class.
     */
    public PlacementInitializer(String prefix) {
        protocolID  = Configuration.getPid(prefix + "." + PAR_PROT);
        appsCount   = Configuration.getInt(prefix + "." + PAR_APPSCOUNT);
        maxAppsDemand = Configuration.getDouble(prefix + "." + PAR_MAX_APPS_DEMAND);
        f_expected    = Configuration.getDouble(prefix + "." + PAR_FRACTION_EXPECTED_DEMAND);
    }

  
    
	@Override
	public boolean execute()
	{
		// Create applications according to application count
		for(int i=1; i <= appsCount; i++)
		{
			double expectedCPUDemand = Math.min(maxAppsDemand, 2 * CommonState.r.nextDouble() * f_expected * maxAppsDemand);
			double initialCPUDemand  = expectedCPUDemand;
			
			Application a = new Application(i, expectedCPUDemand, initialCPUDemand );
			ApplicationsManager.getInstance().applications().add(a);
		}
		return false;
	}
}
