/*
 * Copyright (c) 2010 LCN, EE school, KTH
 *
 */


package peersim.EP2400.resourcealloc.base;

/**
 * Class to represent an application run in the datacenter
 * @author Rerngvit Yanggratoke
 *
 */
public class Application {
	/**
	 * Unique Identifier for each Application
	 */
	private int ID;
	
	public int getID() {
		return ID;
	}


	/**
	 * expected CPU demand for this application
	 */
	private double expectedCPUDemand;
	
	
	public double getExpectedCPUDemand() {
		return expectedCPUDemand;
	}
	
	public void setExpectedCPUDemand(double expectedCPUDemand) {
		this.expectedCPUDemand = expectedCPUDemand;
	}


	/**
	 * CPU demand of an application
	 */
	private double CPUDemand;
	

	
	
	
	/**
	 * The protocol that this application is allocated to
	 */
	private DistributedPlacementProtocol distPlacementProtocol;
	
	
	/**
	 * Accessor for currently allocated distPlacementProtocol
	 * @return
	 */
	public DistributedPlacementProtocol getDistPlacementProtocol() {
		return distPlacementProtocol;
	}

	/**
	 * Getter for currently allocated distPlacementProtocol
	 * @return
	 */
	public void setDistPlacementProtocol(
			DistributedPlacementProtocol distPlacementProtocol) {
		this.distPlacementProtocol = distPlacementProtocol;
	}


	/**
	 * A simple validator whether the applications were allocated twice or not allocated at all
	 */
	private int allocationCountValidator;
	
	
	/**
	 * Accessor to allocationCountValidator
	 * @return
	 */
	public int getAllocationCountValidator() {
		return allocationCountValidator;
	}

	/**
	 * Increment the validator
	 */
	public void incrementValidator()
	{
		allocationCountValidator++;
	}
	
	/**
	 * decrement the validator
	 */
	public void decrementValidator()
	{
		allocationCountValidator--;
	}
	

	/**
	 * Accessor for current CPU demand
	 * @return
	 */
	public double getCPUDemand() {
		return CPUDemand;
	}

	/**
	 * Setter for the CPU demand
	 * @param cpuDemand
	 */
	public void setCPUDemand(double cpuDemand) {
		// update the value in the protocol
		
		CPUDemand = cpuDemand;
		 
	}

	/**
	 * check function whether an application pass the constraint.
	 * @return
	 */
	 public boolean  isPassConstraints()
	 {
		 // it is valid if and only if the allocationCountValidator is exactly 1
		 // In other words, there is exactly one machine allocate this application.
		 return allocationCountValidator == 1;
		 
		 
	 }
	
	 
	
	
	/**
	 * Constructor based on input CPU demand
	 * @param CPUDemand cpu demand for this application instance
	 */
	public Application(int ID, double expectedCPUDemand, double CPUDemand)
	{
		this.ID        = ID;
		this.expectedCPUDemand = expectedCPUDemand;
		this.CPUDemand = CPUDemand;
		this.distPlacementProtocol = null;
		this.allocationCountValidator = 0;
	}
	
	
	
	
	
	
	
}
