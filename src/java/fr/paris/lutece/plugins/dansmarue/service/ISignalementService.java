package fr.paris.lutece.plugins.dansmarue.service;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementDashboardFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementFilter;
import fr.paris.lutece.plugins.dansmarue.business.exceptions.AlreadyFollowedException;
import fr.paris.lutece.plugins.dansmarue.business.exceptions.InvalidStateActionException;
import fr.paris.lutece.plugins.dansmarue.business.exceptions.NonExistentFollowItem;
import fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties;
import fr.paris.lutece.plugins.dansmarue.service.dto.DashboardSignalementDTO;
import fr.paris.lutece.plugins.dansmarue.service.dto.SignalementExportCSVDTO;
import fr.paris.lutece.plugins.sira.dto.DossierSignalementDTO;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;


/**
 * The Interface ISignalementService.
 */
public interface ISignalementService
{
    /**
     * Get the case with the given id.
     * 
     * @param lIdSignalement The case id
     * @return The case with the given id
     */
    Signalement getSignalement( long lIdSignalement );

    /**
     * Get the case with the given id with photo content
     * 
     * @param lIdSignalement The case id
     * @return The case with the given id
     */
    Signalement getSignalementWithFullPhoto( long lIdSignalement );

    /**
     * Finds all categories which match the given filter.
     * 
     * @param filter The filter
     * @param paginationProperties the pagination properties
     * @param bLoadSignaleurs True to load the signaleurs of signalement found, false to ignore
     *            them
     * @return A list of categories which match the given filter
     */
    List<Signalement> findByFilter( SignalementFilter filter, PaginationProperties paginationProperties,
            boolean bLoadSignaleurs );

    /**
     * Insert.
     * 
     * @param signalement the signalement
     * @return the long
     */
    Long insert( Signalement signalement );

    /**
     * Insert from front. Does the same thing than insert but insert also
     * peripheral components like adresses, photos and so.
     * 
     * @param signalement the signalement
     * @return id of created signalement
     */
    Long insertWithPeripheralDatas( Signalement signalement );

    /**
     * Load a signalement by its id.
     * 
     * @param nId the n id
     * @return the signalement
     */
    Signalement loadById( long nId );

    /**
     * Update a signalement.
     * 
     * @param signalement the signalement to update
     */
    void update( Signalement signalement );

    /**
     * Load all SignalementExportCSVDTO with columns : Priorité, Type,
     * Direction, Adresse,
     * Date de création, Etat.
     * 
     * @param filter the filter
     * @param paginationProperties the pagination properties
     * @return the signalement for export by filter
     */
    List<SignalementExportCSVDTO> getSignalementForExportByFilter( SignalementFilter filter,
            PaginationProperties paginationProperties );

    /**
     * Load signalement with columns : Priorité, Type, Direction, Adresse, Date
     * de création, Etat.
     * 
     * @param signalementIds ids of signalement to get
     * @return list of SignalementExportCSVDTO with columns : Priorité, Type,
     *         Direction, Adresse, Date
     *         de création, Etat
     */
    List<SignalementExportCSVDTO> getSignalementForExport( int[] signalementIds );
    
    /**
     * Set the given id signalement as duplicate.
     * 
     * @param lIdSignalement the id signalement
     */
    void setDoublon( long lIdSignalement );

    /**
     * Gets the signalement by status id.
     * 
     * @param statusId the status id
     * @return the signalement listSS
     */
    List<Signalement> getSignalementByStatusId( Integer statusId );

    /**
     * Load all id signalement.
     * 
     * @return the list of all id signalement
     */
    List<Integer> getAllIdSignalement( );
    
    /**
     * Load all info of all the signalements.
     * 
     * @return the list of the all the signalements with info
     */
    List<Signalement> getInfosForAllSignalements( );

    /**
     * Add one to a signalement's suivi
     * 
     * @param nIdSignalement the signalement id
     */
    void incrementeSuiviByIdSignalement( Integer nIdSignalement );

    /**
     * Select all the signalement for a given point and a radius.
     * 
     * @param lat the lat
     * @param lng the lng
     * @param radius the radius
     * @return a list of all the signalement in the given parameter
     */
    List<Signalement> findAllSignalementInPerimeterWithInfo( Double lat, Double lng, Integer radius );

    /**
     * Select all the signalement for a given point and a radius.
     * 
     * @param lat the lat
     * @param lng the lng
     * @param radius the radius
     * @return a list of all the id signalement in the given parameter
     */
    List<DossierSignalementDTO> findAllSignalementInPerimeterWithDTO( Double lat, Double lng, Integer radius );

    /**
     * Get the distance between 2 signalements in meters.
     * 
     * @param lat1 the lat1
     * @param lng1 the lng1
     * @param lat2 the lat2
     * @param lng2 the lng2
     * @return the distance in meter (Integer)
     */
    Integer getDistanceBetweenSignalement( double lat1, double lng1, double lat2, double lng2 );


    /**
     * Initialize signalement workflow.
     * 
     * @param signalement the signalement
     */
    void initializeSignalementWorkflow( Signalement signalement );

    List<Signalement> findSignalementsByIdTelephone( String idTelephone );
    
    /**
     * TODO a documenter
     * @param idTypeSignalement the signalement id to load
     * @param lng
     * @param lat
     * @return
     */
    Unit getMajorUnit( Integer idTypeSignalement, Double lng, Double lat );

    boolean hasSignalementForRoadMap( List<Integer> listSectorId );

    /**
     * Get the informations of a signalement by its token
     * @param token
     * @return signalement
     */
    Signalement getSignalementByToken( String token );

    Double[] getGeomFromLambertToWgs84( Double dLatLambert, Double dLngLambert );

    /**
     * Count the number of signalement by filter
     * @param filter the filter
     * @param plugin the plugin
     * @return number of signalement
     */
    Integer countIdSignalementByFilter( SignalementFilter filter, Plugin plugin );

    /**
     * Delete a signalement and all its information
     * @param lIdSignalement the signalement id
     */
    void doDeleteSignalement( long lIdSignalement );
    
    /**
     * Delete a arrays of signalement and all its information
     * @param lIdSignalement the signalement id
     */
    void doDeleteSignalement( int []lIdSignalement );

    /**
     * Insert a message creation signalement (linked with notification user task
     * workflow)
     * @param messageCreation the default message send to user when creating a
     *            signalement
     */
    void insertMessageCreationSignalement( String messageCreation );

    /**
     * Update the message creation signalement (linked with notification user
     * task
     * workflow)
     * @param messageCreation the default message send to user when creating a
     *            signalement
     */
    void updateMessageCreationSignalement( String messageCreation );

    /**
     * Load the message creation signalement (linked with notification user
     * task workflow)
     * @return the message creation signalement
     */
    String loadMessageCreationSignalement( );

    /**
     * Remove the message creation signalement (linked with notification user
     * task workflow)
     */
    void removeMessageCreationSignalement( );

    /**
     * Replace the current state to a more explicit one for front office
     * @param stateOfSignalement
     * @return the new state
     */
    String changeToGoodStateForSuivi( State stateOfSignalement );

    /**
     * Affect unique token to the signalement, don't throw erro in case of charset exception
     * @param signalement the signalement to affect token
     */
    void affectToken( Signalement signalement );
    

	/**
	 * Set the given date de passage
	 * 
	 * @param dateDePassage
	 *            the string value of date de passage
	 * @param heureDePassage
	 *            the string value of heure de passage
	 * @param lIdSignalement
	 *            the id of signalement
	 */
	void setDateDePassage(String dateDePassage, String heureDePassage, Long lIdSignalement);

	/**
	 * Gets the message to notify the user with, for a signalement creation
	 * @return the message to notify
	 */
	String getMessageCreationSignalement();
	
	/**
	 * Returns the link for tracking signalement
	 * @param signalement
	 * @param request
	 * @return
	 */
	String getLienConsultation(Signalement signalement, HttpServletRequest request );

	/**
	 * Increases the congratulations count of a signalement
	 * @param idSignalement
	 * 		  the id of the signalement which the congratulations count must be increased
	 */
	void incrementFelicitationsByIdSignalement(int idSignalement);

	/**
	 * Decrements the number of people following a signalement
	 * @param nIdSignalement
	 * 		  the id of the signalement, which the following number must be decreased
	 */
	void decrementSuiviByIdSignalement(Integer nIdSignalement);
	
	/**
	 * Finds all signalements which are followed by an user
	 * @param guid the user guid
	 * @return
	 * 		  List of all signalements followed by this user
	 */
	List<Signalement> getSignalementsByGuid(String guid);
	
	/**
	 * Adds a follower to a signalement
	 * @param signalementId
	 * 		  The signalement id to follow
	 * @param guid
	 * 		  The follower guid
	 * @param strUDID
	 * 		  The follower udid
	 * @param email
	 * 		  The user email
	 * @param device
	 * 		  The user device
	 * @param userToken
	 * 		  The user token
	 * @param createUser
	 * 		  Defines if the user must be created
	 * @throws AlreadyFollowedException exception if the signalement is already followed by the user
	 * @throws InvalidStateActionException exception if the signalement is not in a state which allows following
	 */
	void addFollower(Long signalementId, String guid, String strUDID, String email, String device,
			String userToken, boolean createUser) throws AlreadyFollowedException, InvalidStateActionException;
	
	/**
	 * Removes a signalement follower
	 * @param signalementId
	 * 		  the signalement id which must be unfollowed
	 * @param guid
	 * 		  user guid
	 * @throws NonExistentFollowItem exception thrown when we try to remove a following item when the user is not following the signalement
	 */
	void removeFollower(Long signalementId, String guid) throws NonExistentFollowItem ;
	
	/**
	 * Filters signalement by the criterias defined in dashboard
	 * @param filter
	 * @return
	 */
	List<DashboardSignalementDTO> findByDashboardFilter(SignalementDashboardFilter filter);

	/**
	 * Finds signalements by their Ids, and display a restricted list
	 * @param signalementIds
	 * @param nbMaxItems
	 * @param offset
	 * @return
	 */
	List<Signalement> getByIds(List<Integer> signalementIds, Integer nbMaxItems, Integer offset);

	/**
     * Build the query to get the signalement  ids matching the filter
     * @param filter the signalementfilter
     * @return the query
     */
	List<Integer> getIdsSignalementByFilter(SignalementFilter filter);

	/**
	 * Get all possible actions for a resource (signalement) and a given user.
	 * @param nIdSignalement the resource we want to have the possible actions
	 * @param workflowId workflow type (ramen 1 or signalement 2)
	 * @param user user to check
	 * @return
	 */
	Collection<Action> getListActionsByIdSignalementAndUser(int nIdSignalement, int workflowId, AdminUser user);

	/**
	 * Check if a signalement is followable, based on its status
	 * @param nIdSignalement
	 * 		 The id of signalement to check
	 * @return
	 * 		  True if the signalement is in a followable state
	 *  	  False otherwise
	 */
	boolean isSignalementFollowable(int nIdSignalement);

	/**
	 * Checks if the user has already followed this signalement
	 * @param nIdSignalement
	 * 		Id of the signalement to check
	 * @param userGuid
	 * 		User to check
	 * @return
	 * 		True if the signalement has already been followed by the user
	 * 		False otherwise
	 */
	boolean isSignalementFollowedByUser(int nIdSignalement, String userGuid);
	
	/**
	 * Checks if the user has already followed this signalement
	 * @param reference
	 * 		Id of the signalement reference
	 * @return
	 * 		the signalement id reference
	 */
	String getSignalementReference(String reference, Signalement signalement);
	
	/**
	 * Add date dateMiseEnSurveillance for Signalement
	 * @param nIdSignalement
	 *        Id of the signalement reference
	 * @param dateMiseEnSurveillance
	 */
	void doMettreSousSurveillance(int nIdSignalement, String dateMiseEnSurveillance);
}
