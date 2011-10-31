/*
 * Copyright (c) 2010 LCN, EE school, KTH
 *
 */

package peersim.EP2400.resourcealloc.controls;

import peersim.config.Configuration;
import peersim.core.Control;

/**
 * Base class for application demand generator.
 * @author Rerngvit Yanggratoke
 *
 */
public abstract class DemandGenerator  implements Control{

	 /**
     * The protocol to operate on.
     * 
     * @config
     */
    private static final String PAR_PROT = "protocol";

	
    /** Protocol identifier, obtained from config property {@link #PAR_PROT}. */
    protected final int protocolID;
   
	
	 public DemandGenerator(String prefix) {
		  protocolID  = Configuration.getPid(prefix + "." + PAR_PROT);
	      
	   }
	@Override
	public abstract boolean execute();
}
