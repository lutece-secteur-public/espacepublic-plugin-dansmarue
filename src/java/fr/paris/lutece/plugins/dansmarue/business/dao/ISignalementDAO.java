package fr.paris.lutece.plugins.dansmarue.business.dao;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementDashboardFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementFilter;
import fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties;
import fr.paris.lutece.plugins.dansmarue.service.dto.DashboardSignalementDTO;
import fr.paris.lutece.plugins.sira.dto.DossierSignalementDTO;
import fr.paris.lutece.portal.service.plugin.Plugin;


public interface ISignalementDAO
{

    /**
     * Inserts a signalement.
     * 
     * @param signalement the signalement
     * @return the generated signalement id
     */
    Long insert( Signalement signalement );

    /**
     * Removes a signalement.
     * 
     * @param lId the id
     */
    void remove( long lId );

    /**
     * Load a signalement by its id.
     * 
     * @param lId the "signalement" id
     * @return the signalement
     */
    Signalement load( long lId );

    List<Signalement> findByFilter( SignalementFilter filter, PaginationProperties paginationProperties, Plugin plugin );

    /**
     * Load a signalement by its id.
     * 
     * @param nId the signalement id
     * @return the signalement
     */
    Signalement loadById( long nId );

    /**
     * Update a signalement
     * 
     * @param signalement the signalement to update
     */
    void update( Signalement signalement );

    /**
     * Load all signalement with columns : Priorité, Type, Direction, Adresse,
     * Date de création, Etat.
     * 
     * @return all the signalements
     */
    List<Signalement> getAllSignalement( );
    
    /**
     * Gets the signalements by status id.
     * 
     * @param statusId the status id
     * @param limit time limit before archiving
     * @return the signalements list
     */
    List<Signalement> getSignalementsArchivableByStatusId( Integer statusId, Integer limit );

    /**
     * Load all id signalement.
     * 
     * @return the list of all id signalement
     */
    List<Integer> getAllIdSignalement( );

    /**
     * Add one to a signalement's suivi
     * 
     * @param nIdSignalement the signalement id
     */
    void incrementeSuiviByIdSignalement( Integer nIdSignalement );
    
    /**
     * Retrieves one to a signalement's suivi
     * 
     * @param nIdSignalement the signalement id
     */
    void decrementSuiviByIdSignalement( Integer nIdSignalement );

    /**
     * Select all the signalement for a given point and a radius.
     * 
     * @param lat the lat
     * @param lng the lng
     * @param radius the radius
     * @param listStatus the list status
     * @return a list of all the id signalement in the given parameter
     */
    List<Integer> findAllSignalementInPerimeter( Double lat, Double lng, Integer radius, List<Integer> listStatus );

    /**
     * Select all the signalement for a given point and a radius.
     * 
     * @param lat the lat
     * @param lng the lng
     * @param radius the radius
     * @param listStatus the list status
     * @return a list of all the id signalement in the given parameter
     */
    List<DossierSignalementDTO> findAllSignalementInPerimeterWithDTO( Double lat, Double lng, Integer radius,
            List<Integer> listStatus );

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

    List<Integer> findByIdTelephone( String idTelephone );

    boolean hasSignalementForRoadMap( List<Integer> listSectorId );

    /**
     * Get a signalement by a token
     * @param token the token
     * @param plugin the plugin
     * @return the signalement if exists or null
     */
    Signalement getSignalementByToken( String token, Plugin plugin );
    
    Double[] getGeomFromLambertToWgs84( Double dLatLambert, Double dLngLambert );

    /**
     * Count the number of results for a given query
     * @param filter the signalementfilter
     * @return the query
     */
    Integer countIdSignalementByFilter( SignalementFilter filter, Plugin plugin );

    /**
     * Build the query to count the number of results for a given query
     * @param filter the signalementfilter
     * @return the query
     */
    String buildSQLQueryForCount( SignalementFilter filter );

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
     * Increments the number of congratulations for this signalement
     * @param idSignalement the signalement id, to increment
     */
	void incrementFelicitationsByIdSignalement(int idSignalement);
	
	/**
	 * Filters signalement by dashboard criterias
	 * @return 
	 */
	List<DashboardSignalementDTO> findByDashboardFilter( SignalementDashboardFilter filter, Plugin pluginSignalement );

	/**
     * Build the query to get the signalement  ids matching the filter
     * @param filter the signalementfilter
     * @return the query
     */
	List<Integer> getIdsSignalementByFilter(SignalementFilter filter, Plugin plugin);
	
	/**
	 * Execute Query to add dateMiseEnSurveillance for this signalement
	 * @param idSignalement the signalement id
	 * @param dateMiseEnSurveillance the date
	 */
	void addMiseEnSurveillanceDate(int idSignalement, String dateMiseEnSurveillance);
}