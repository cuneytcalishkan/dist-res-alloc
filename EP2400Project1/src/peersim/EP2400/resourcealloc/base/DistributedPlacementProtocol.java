/*
 * Copyright (c) 2010 LCN, EE school, KTH
 * 
 */

package peersim.EP2400.resourcealloc.base;


import peersim.config.Configuration;
import peersim.core.*;
import peersim.cdsim.CDProtocol;
/**
 * <p>
 * This class implements a base for the gossip-based application placement protocol.
 * </p>
 * @author Rerngvit Yanggratoke
 */
public class DistributedPlacementProtocol implements CDProtocol{

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

	protected String prefix;
	
	/**
	 * List of running of applications
	 */
	private ApplicationsList A_n;
	
	protected static final String PAR_CPU_CAPACITY = "cpu_capacity";
	
	
	
	
	
	/**
	 * CPU final capacity value set from PlacementInitializer
	 */
	protected final double cpuCapacity;
	

	/**
	 * Accessor for the CPU capacity. This is the the same for all servers.
	 * @return
	 */
    public double getCpuCapacity() {
		return cpuCapacity;
	}


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
    public DistributedPlacementProtocol(String prefix) {
        this.prefix = prefix;
        // get cpu capacity value from the config file, default 100.0
        cpuCapacity = Configuration.getDouble(prefix + "." + PAR_CPU_CAPACITY);
        A_n      = new ApplicationsList();
    }

    public DistributedPlacementProtocol(String prefix, double cpu_capacity_value) {
        
        this.prefix = prefix;
        // get cpu capacity value from the config file, default 100.0
        this.cpuCapacity = cpu_capacity_value;
        A_n      = new ApplicationsList();
    }
    
    public void nextCycle(Node node, int protocolID)
    {
    	
    }

    /**
     * Public call for adding new application to this distributed application placement protocol
     * @param a Application to add
     */
   public void allocateApplication(Application a)
   {
	 //  System.out.println(" Allocating application ID :" + a.getID());
	   A_n.add(a);
	   a.setDistPlacementProtocol(this);
	   a.incrementValidator();
	 
   }
   
   
    
   
   /**
    * Public function for removing application from this distributed application placement protocol
    * @param a Application to remove
    */
   public void deallocateApplication(Application a)
   {
	   A_n.remove(a);
	   a.decrementValidator();
   }
   

   /**
    * Read-Only copy of applicationList
    * @param a Application to add
    */
  public ApplicationsList applicationsList()
  {   
	  
	  
	  return (ApplicationsList) A_n.clone();
       
      
  }
   
   /**
    * Total number of applications allocated to this protocol at the moment
    * @param a 
    */
   public int appsCount()
   {
	   return A_n.size();
   }
   
   /**
    * Total CPU demand from this distributed application placement protocol.
    * @return
    */
   public double getTotalDemand()
   {
	   
	   return A_n.totalCPUDemand();
   }
 
   public Object clone()
   {
	   DistributedPlacementProtocol proto = new DistributedPlacementProtocol(this.prefix, this.cpuCapacity);
		return proto;
   }
   

}
