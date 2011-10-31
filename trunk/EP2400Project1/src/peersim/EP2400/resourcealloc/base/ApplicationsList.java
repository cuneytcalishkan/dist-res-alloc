/*
 * Copyright (c) 2010 LCN, EE school, KTH
 *
 */

package peersim.EP2400.resourcealloc.base;

import java.util.ArrayList;

/**
 * A class for representing placement set for each server. For simplicity, this is represented by ArrayList
 * @author Rerngvit Yanggratoke
 */
public class ApplicationsList extends ArrayList<Application> {

	/**
	 * Flag to indicate whether the totalDemandCache is updated or not
	 */
	private boolean totalDemandUpdated;
	
	/**
	 * Total application demand cache for speedy retrieval of total CPU demand.
	 */
	private double totalDemandCache;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2896826171558618679L;

	/**
	 * Constructor which initialize the list and total demand.
	 */
	public ApplicationsList()
	{
		totalDemandUpdated = true;
		totalDemandCache   = 0.0;
		
	}
	
	/**
	 * add application to this placement set. This will update the totalDemand cache.
	 */
	public boolean add(Application a)
	{
		
		boolean result = super.add(a);
		totalDemandUpdated = true;
		return result;
		
	}
	
	/**
	 * remove application from this placement set. This will update the totalDemand cache.
	 * @param a
	 */
	public boolean remove(Application a)
	{
		boolean result = super.remove(a);
		totalDemandUpdated = true;
		return result;
	}
	
	
	/**
	 * Retrieve total CPU demand from this placement. The result will come from cache if it is possible.
	 * Please note that the result from this method is valid if and only if 
	 * the user of the class add and remove object via the add and remove method overrided in this class.
	 * @return
	 */
	public double totalCPUDemand()
	{
		//if (!totalDemandUpdated) return totalDemandCache;
		
		double totalDemand = 0;
		for( Application a: this)
		{
			totalDemand += a.getCPUDemand();
		}
		
		totalDemandCache = totalDemand;
		totalDemandUpdated = false;
		
		
		return totalDemandCache;
	}

	
	
}
