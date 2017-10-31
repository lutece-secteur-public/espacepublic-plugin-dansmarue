package fr.paris.lutece.plugins.dansmarue.business.dao;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.DashboardPeriod;
import fr.paris.lutece.portal.service.plugin.Plugin;

public interface IDashboardPeriodDAO {
	
	/**
	 * Inserts into the database a new dashboard period
	 * @param dashboardPeriod the dashboard period to insert
	 * @return
	 */
	 public Long insert( DashboardPeriod dashboardPeriod, Plugin plugin );

	 /**
	  * Removes from the data base a dashboard period
	  * @param lId the id of the dashboard period to remove
	  */
	 public void remove( long lId, Plugin plugin );

	 /**
	  * Loads a dashboard period from its id 
	  * @param lId the id of the dashboard period to load
	  * @return
	  * 	  The dashboard period matching the id
	  * 	  null otherwise
	  */
	 public DashboardPeriod load( long lId );

     /**
     * Updates a SignalementDashboardPeriod
     * 
     * @param dashboardPeriod the dashboard period element to update
     */
     void update( DashboardPeriod dashboardPeriod, Plugin plugin );

     /**
      * Gets all dashboard search period criterias
      * @return
      * 	List of dashboard perido criterias
      */
     List<DashboardPeriod> getDashboardPeriodCriterias();
     
     /**
      * Get all dashboard periods
      * @return
      * 	List of all dashboard periods
      */
     List<DashboardPeriod> getAllDashboardPeriods();
     
}
