package peersim.EP2400.resourcealloc.tasks;

import peersim.EP2400.resourcealloc.base.ApplicationsList;
import peersim.EP2400.resourcealloc.base.DistributedPlacementProtocol;
import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Linkable;
import peersim.core.Node;

/**
 * Template class for distributed laod balancing with additional constraint.
 * @author Rerngvit Yanggratoke
 *
 */
public class AdvancedDistributedResourceAllocation extends DistributedPlacementProtocol implements CDProtocol{

	/**
	 * A_max parameter as discussed in the project description.
	 */
	private int A_max;
	
	protected static final String PAR_A_MAX = "A_max";
	
	
	public AdvancedDistributedResourceAllocation(String prefix) {
		super(prefix);
		
		A_max = Configuration.getInt(prefix + "." + PAR_A_MAX);
		
	}
	
	public AdvancedDistributedResourceAllocation(String prefix, int A_max,  double cpu_capacity_value) {
		super(prefix, cpu_capacity_value);
		this.A_max = A_max;
	}
	
	/**
	 * Validate function to simply check whether each server has applications more than A_max or not.
	 */
	private void validate()
	{
		if (this.appsCount() > this.A_max)
		{
			// validation fail
			String errorMessage = String.format(" This node has %d applications when A_max is %d" , this.appsCount(), this.A_max);
			System.out.println(errorMessage);
			System.out.println("Terminating now");
			System.exit(1);
		}
		
	}
	

	public void nextCycle(Node node, int protocolID) {
		
		int linkableID = FastConfig.getLinkable(protocolID);
        Linkable linkable = (Linkable) node.getProtocol(linkableID);
        
        int degree  = linkable.degree();
        int nbIndex = CommonState.r.nextInt(degree);
        Node peer = linkable.getNeighbor(nbIndex);
          // The selected peer could be inactive
          if (!peer.isUp())
             return;
          
          AdvancedDistributedResourceAllocation n_prime = (AdvancedDistributedResourceAllocation) peer.getProtocol(protocolID);
          ApplicationsList A_n_prime = n_prime.passiveThread(this.applicationsList());
          
          this.updatePlacement(A_n_prime);
        
         validate();
    }
	

	public ApplicationsList passiveThread(ApplicationsList A_n_prime)
	{
		ApplicationsList tempA_n = this.applicationsList();
		this.updatePlacement(A_n_prime);
		return tempA_n;
	}
	
	
	
	public void updatePlacement(ApplicationsList A_n_prime)
	{
		//TODO implement your code for optional task 4 here
		
	}
	
	public Object clone() {
		AdvancedDistributedResourceAllocation proto = new AdvancedDistributedResourceAllocation(
				this.prefix, this.A_max, this.cpuCapacity);
		return proto;
	}

	

}
