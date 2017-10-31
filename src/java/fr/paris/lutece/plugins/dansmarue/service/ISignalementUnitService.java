package fr.paris.lutece.plugins.dansmarue.service;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementUnit;

public interface ISignalementUnitService {
	
	/**
	 * Inserts into the database, the unit signalement
	 * @param unitSignalement
	 * @return
	 */
    Integer insert( SignalementUnit unitSignalement);
    
    /**
     * Removes unit signalement properties
     * @param lId
     */
    void remove( long lId );
    
    /**
     * Loads the unit signalement
     * @param lId
     * @return
     */
    SignalementUnit load( Integer lId );
    
    /**
     * Updates a unit signalement
     * @param unitSignalement
     */
    void store( SignalementUnit unitSignalement );

    /**
     * Gets all units ids which are visible 
     * @return list containing the id of visible units
     */
	List<Integer> getVisibleUnitsIds();
	
	/**
     * Gets all units ids
     * @return list containing the id of units
     */
	List<Integer> getAllUnitsIds();

	/**
	 * Save units defined as visible
	 * @param visibleUnitsIds
	 */
	void saveVisibleUnits(List<Integer> visibleUnitsIds);
}
