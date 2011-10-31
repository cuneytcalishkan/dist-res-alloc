/*
 * Copyright (c) 2010 LCN, EE school, KTH
 *
 */

package peersim.EP2400.resourcealloc.controls;
import peersim.EP2400.resourcealloc.base.Application;
import peersim.EP2400.resourcealloc.base.ApplicationsManager;
import peersim.EP2400.resourcealloc.base.DistributedPlacementProtocol;
import peersim.config.Configuration;
import peersim.core.*;

/**
 * This class provides random initializer where applications are randomly allocated to each node
 * but not allocated more than A_max parameter
 * @author Rerngvit Yanggratoke
 */
public class AMaxPlacementInitializer extends PlacementInitializer{
	/**
	 * A_max parameter as discussed in the project description.
	 */
	private int A_max;
	
	protected static final String PAR_A_MAX = "A_max";
    public AMaxPlacementInitializer(String prefix) {
		super(prefix);
		A_max = Configuration.getInt(prefix + "." + PAR_A_MAX);
	}

	
    public boolean execute() {
    	super.execute();
    	
    	
    	for (Application a : ApplicationsManager.getInstance().applications())
    	{
    		DistributedPlacementProtocol p;
    		
    		int nsize = Network.size();
    		do{
    		int nodeIndex =  CommonState.r.nextInt(nsize);
    		
    		p = ((DistributedPlacementProtocol) Network.get(nodeIndex).getProtocol(protocolID));
    		
    		if (p.appsCount() < A_max) break;
    		
    		} while(true);
    		p.allocateApplication(a);
    		
    		
    	}
    	
    	
    	return false;
    }

}
