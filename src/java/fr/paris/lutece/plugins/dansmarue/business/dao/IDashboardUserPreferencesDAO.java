package fr.paris.lutece.plugins.dansmarue.business.dao;

import java.util.List;

public interface IDashboardUserPreferencesDAO {
	
	/**
	 * Inserts into the database a user dashboard preferences
	 * @param idUser the user id
	 * @param idState the state id to add (will be displayed in the user dashboard)
	 */
	 public void insert( Integer idUser , Integer idState);

	 /**
	  * Removes from the database a user dashboard preferences
	  * @param idUser the user id
	  * @param idState the state id to remove (will be hidden in the user dashboard)
	  */
	 public void remove( Integer idUser, Integer idState );
	 
	 /**
	  * Finds for a user, all its dashboars displayed states
	  * @param idUser the user id
	  * @return List of states that must be displayed in the user dashboard
	  * 		If no entry found, returns an empty list
	  */
	 public List<Integer> findUserDashboardStates(Integer idUser);
     
}
