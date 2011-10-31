/*
 * Copyright (c) 2010 LCN, EE school, KTH
 *
 */

package peersim.EP2400.resourcealloc.base;


/**
 * Singleton class to manage the list of all applications
 * @author Rerngvit Yanggratoke
 *
 */
public class ApplicationsManager {

	/**
	 * Singleton instance
	 */
	private static ApplicationsManager instance;
	
	/**
	 * This list represents A or all applications in the data center.
	 */
	private ApplicationsList appsList;
	
	/**
	 * Simple Constructor which initialize the instance variables.
	 */
	public ApplicationsManager()
	{
		appsList = new ApplicationsList();
	}
	
	/**
	 * Accessor for the Singleton instance.
	 * @return
	 */
	public static ApplicationsManager getInstance()
	{
		if (instance == null)
		{
			instance = new ApplicationsManager();
		}
		
		return instance;
		
		
	}
	
	
	
	public ApplicationsList applications()
	{
		return appsList;
		
	}
	
}
