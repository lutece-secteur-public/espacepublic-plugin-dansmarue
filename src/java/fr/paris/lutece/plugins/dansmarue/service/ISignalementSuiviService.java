package fr.paris.lutece.plugins.dansmarue.service;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementSuivi;
import fr.paris.lutece.plugins.dansmarue.business.entities.SiraUser;

public interface ISignalementSuiviService {
	/**
	 * Inserts into the database a new suivi signalement
	 * @param signalementSuivi the suivi signalement to insert
	 * @return
	 */
	 public Long insert( SignalementSuivi signalementSuivi );

	 /**
	  * Removes from the data base a suivi signalement
	  * @param lId the id of the suivi signalement to remove
	  */
	 public void remove( long lId );

	 /**
	  * Loads a suivi signalement from its id 
	  * @param lId the id of the suivi signalement to load
	  * @return
	  * 	  The suivi signalement matching the id
	  * 	  null otherwise
	  */
	 public SignalementSuivi load( long lId );

     /**
     * Updates a SignalementSuivi
     * 
     * @param signalementSuivi the signalement suivi to update
     */
     void update( SignalementSuivi signalementSuivi );
     
     /**
      * Search all users which subscribed to this anomalie
      * @param idSignalement
      * @return
      */
     List<SiraUser> findUsersMobilesByIdSignalement(long idSignalement);

     /**
      * Removes a suivi by its properties
      * @param signalementSuivi the suivi to remove
      */
     void remove(SignalementSuivi signalementSuivi);
     
     /**
      * Finds all followers mails of a signalement
      * @param idSignalement 
      * 		The signalement from which the followers must be found
      * @return
      * 		List of mail of all signalement followers
      */
     List<String> findUsersMailByIdSignalement(long idSignalement);

     /**
      * Counts the number of followers of the signalement
      * @param idSignalement
      * 		The signalement id from which we want to get the followers count
      * @return
      * 		The number of followers, for this signalement
      */
 	Integer getNbFollowersByIdSignalement(int idSignalement);
 	 
 	/**
     * Removes all signalement following
     * @param idSignalement
     * 		The signalement id from which the following must be removed
     */
	void removeByIdSignalement(long idSignalement);
 	
 	/**
     * Finds a signalement suivi, by an signalement id and a user guid
     * @param idSignalement
     * @param guid
     * @return the id of the signalement suivi item
     * 		-1 otherwise
     */
    long findByIdSignalementAndGuid(long idSignalement, String guid);
 	
}
