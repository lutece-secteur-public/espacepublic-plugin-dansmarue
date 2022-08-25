/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.dansmarue.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.business.entities.EtatSignalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.ServiceFaitMasseFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementDashboardFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementRequalification;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.business.entities.TableauDeBordFilter;
import fr.paris.lutece.plugins.dansmarue.business.exceptions.AlreadyFollowedException;
import fr.paris.lutece.plugins.dansmarue.business.exceptions.InvalidStateActionException;
import fr.paris.lutece.plugins.dansmarue.business.exceptions.NonExistentFollowItem;
import fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties;
import fr.paris.lutece.plugins.dansmarue.service.dto.DashboardSignalementDTO;
import fr.paris.lutece.plugins.dansmarue.service.dto.DossierSignalementDTO;
import fr.paris.lutece.plugins.dansmarue.service.dto.SignalementExportCSVDTO;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import net.sf.json.JSONObject;

/**
 * The Interface ISignalementService.
 */
public interface ISignalementService
{
    /**
     * Get the report with the given id.
     *
     * @param lIdSignalement
     *            The report id
     * @return The report with the given id
     */
    Signalement getSignalement( long lIdSignalement );

    /**
     * Get the report with the given id with photo content.
     *
     * @param lIdSignalement
     *            The report id
     * @return The report with the given id
     */
    Signalement getSignalementWithFullPhoto( long lIdSignalement );

    /**
     * Finds all categories which match the given filter.
     *
     * @param filter
     *            The filter
     * @param paginationProperties
     *            the pagination properties
     * @param bLoadSignaleurs
     *            True to load the reporters of report found, false to ignore them
     * @return A list of categories which match the given filter
     */
    List<Signalement> findByFilter( SignalementFilter filter, PaginationProperties paginationProperties, boolean bLoadSignaleurs );

    /**
     * Inserts a report.
     *
     * @param signalement
     *            the report
     * @return the generated report id
     */
    Long insert( Signalement signalement );

    /**
     * Insert from front. Does the same thing than insert but insert also peripheral components like addresses, photos and so.
     *
     * @param signalement
     *            the report
     * @return id of created report
     */
    Long insertWithPeripheralDatas( Signalement signalement );

    /**
     * Load a report by its id.
     *
     * @param nId
     *            the n id
     * @return the report
     */
    Signalement loadById( long nId );

    /**
     * Update a report.
     *
     * @param signalement
     *            the report to update
     */
    void update( Signalement signalement );

    /**
     * Load all SignalementExportCSVDTO with columns : Priorities, Type, Board, Address, Creation date, State.
     *
     * @param filter
     *            the filter
     * @param paginationProperties
     *            the pagination properties
     * @return the report for export by filter
     */
    List<SignalementExportCSVDTO> getSignalementForExportByFilter( SignalementFilter filter, PaginationProperties paginationProperties );

    /**
     * Load report with columns : Priorities, Type, Board, Address, Creation date, State.
     *
     * @param signalementIds
     *            ids of report to get
     * @return list of SignalementExportCSVDTO with columns : Priorities, Type, Board, Address, Creation date, State.
     */
    List<SignalementExportCSVDTO> getSignalementForExport( int [ ] signalementIds );

    /**
     * Set the given report id as duplicate.
     *
     * @param lIdSignalement
     *            the id report
     */
    void setDoublon( long lIdSignalement );

    /**
     * Gets the report by status id.
     *
     * @param statusId
     *            the status id
     * @return the report listSS
     */
    List<Signalement> getSignalementByStatusId( Integer statusId );

    /**
     * Load all report ids.
     *
     * @return the list of all report ids
     */
    List<Integer> getAllIdSignalement( );

    /**
     * Load all info of all the reports.
     *
     * @return the list of the all the reports with info
     */
    List<Signalement> getInfosForAllSignalements( );

    /**
     * Add one to a report's follow.
     *
     * @param nIdSignalement
     *            the report id
     */
    void incrementeSuiviByIdSignalement( Integer nIdSignalement );

    /**
     * Select all the report for a given point and a radius.
     *
     * @param lat
     *            the lat
     * @param lng
     *            the lng
     * @param radius
     *            the radius
     * @param specificSignalementNumberSearch
     *            specific Signalement to find (function SearchByNumber mobile application)
     * @return a list of all the reports in the given parameter
     */
    List<Signalement> findAllSignalementInPerimeterWithInfo( Double lat, Double lng, Integer radius, String specificSignalementNumberSearch );

    /**
     * Gets the anomalie by number.
     *
     * @param number
     *            the number
     * @return the anomalie by number
     */
    Signalement getAnomalieByNumber( String number );

    /**
     * Select all the report for a given point and a radius.
     *
     * @param lat
     *            the lat
     * @param lng
     *            the lng
     * @param radius
     *            the radius
     * @return a list of all the reports in the given parameter
     */
    List<DossierSignalementDTO> findAllSignalementInPerimeterWithDTO( Double lat, Double lng, Integer radius );

    /**
     * Get the distance between 2 reports in meters.
     *
     * @param lat1
     *            the lat1
     * @param lng1
     *            the lng1
     * @param lat2
     *            the lat2
     * @param lng2
     *            the lng2
     * @return the distance in meter (Integer)
     */
    Integer getDistanceBetweenSignalement( double lat1, double lng1, double lat2, double lng2 );

    /**
     * Initialize report workflow.
     *
     * @param signalement
     *            the report
     */
    void initializeSignalementWorkflow( Signalement signalement );

    /**
     * Returns all the reports linked to a selected phone number.
     *
     * @param idTelephone
     *            the phone id
     * @return list of reports
     */
    List<Signalement> findSignalementsByIdTelephone( String idTelephone );

    /**
     * Get the Parent Unit.
     *
     * @param idTypeSignalement
     *            the report id to load
     * @param lng
     *            the longitude
     * @param lat
     *            the latitude
     * @return the parent unit
     */
    Unit getMajorUnit( Integer idTypeSignalement, Double lng, Double lat );

    /**
     * Get the informations of a report by its token.
     *
     * @param token
     *            the report token
     * @return report
     */
    Signalement getSignalementByToken( String token );

    /**
     * Returns a geom.
     *
     * @param dLatLambert
     *            the latitude
     * @param dLngLambert
     *            the longitude
     * @return a double array
     */
    Double [ ] getGeomFromLambertToWgs84( Double dLatLambert, Double dLngLambert );

    /**
     * Gets the geom from lambert 93 to wgs 84.
     *
     * @param dLatLambert
     *            the d lat lambert
     * @param dLngLambert
     *            the d lng lambert
     * @return the geom from lambert 93 to wgs 84
     */
    Double [ ] getGeomFromLambert93ToWgs84( Double dLatLambert, Double dLngLambert );

    /**
     * Count the number of report by filter.
     *
     * @param filter
     *            the filter
     * @param plugin
     *            the plugin
     * @return number of report
     */
    Integer countIdSignalementByFilter( SignalementFilter filter, Plugin plugin );

    /**
     * Delete a report and all its information.
     *
     * @param lIdSignalement
     *            the report id
     */
    void doDeleteSignalement( long lIdSignalement );

    /**
     * Delete a arrays of report and all its information.
     *
     * @param lIdSignalement
     *            the report id
     */
    void doDeleteSignalement( int [ ] lIdSignalement );

    /**
     * Insert a message of report creation (linked with notification user task workflow).
     *
     * @param messageCreation
     *            the default message send to user when creating a report
     */
    void insertMessageCreationSignalement( String messageCreation );

    /**
     * Update the message of report creation (linked with notification user task workflow).
     *
     * @param messageCreation
     *            the default message send to user when creating a report
     */
    void updateMessageCreationSignalement( String messageCreation );

    /**
     * Load the message of report creation (linked with notification user task workflow).
     *
     * @return the message of report creation
     */
    String loadMessageCreationSignalement( );

    /**
     * Remove the message of report creation (linked with notification user task workflow).
     */
    void removeMessageCreationSignalement( );

    /**
     * Replace the current state to a more explicit one for front office.
     *
     * @param stateOfSignalement
     *            the current state of the report
     * @return the new state
     */
    String changeToGoodStateForSuivi( State stateOfSignalement );

    /**
     * Affect unique token to the report, don't throw erro in case of charset exception.
     *
     * @param signalement
     *            the report to affect token
     */
    void affectToken( Signalement signalement );

    /**
     * Set the given passage date.
     *
     * @param dateDePassage
     *            the string value of passage date
     * @param heureDePassage
     *            the string value of passage hour
     * @param lIdSignalement
     *            the id of report
     */
    void setDateDePassage( String dateDePassage, String heureDePassage, Long lIdSignalement );

    /**
     * Gets the message to notify the user with, for a report creation.
     *
     * @return the message to notify
     */
    String getMessageCreationSignalement( );

    /**
     * Returns the link for tracking report.
     *
     * @param signalement
     *            the report object
     * @param request
     *            HttpServletRequest
     * @return the tracking link
     */
    String getLienConsultation( Signalement signalement, HttpServletRequest request );

    /**
     * Increases the congratulations count of a report.
     *
     * @param idSignalement
     *            the id of the report which the congratulations count must be increased
     */
    void incrementFelicitationsByIdSignalement( int idSignalement );

    /**
     * Decrements the number of people following a report.
     *
     * @param nIdSignalement
     *            the id of the report, which the following number must be decreased
     */
    void decrementSuiviByIdSignalement( Integer nIdSignalement );

    /**
     * Finds all reports which are followed by an user.
     *
     * @param guid
     *            the user guid
     * @param isResolved
     *            the is resolved
     * @return List of all reports followed by this user
     */
    List<Signalement> getSignalementsByGuid( String guid, boolean isResolved );

    /**
     * Adds a follower to a report.
     *
     * @param signalementId
     *            The report id to follow
     * @param guid
     *            The follower guid
     * @param strUDID
     *            The follower udid
     * @param email
     *            The user email
     * @param device
     *            The user device
     * @param userToken
     *            The user token
     * @param createUser
     *            Defines if the user must be created
     * @throws AlreadyFollowedException
     *             exception if the report is already followed by the user
     * @throws InvalidStateActionException
     *             exception if the report is not in a state which allows following
     */
    void addFollower( Long signalementId, String guid, String strUDID, String email, String device, String userToken, boolean createUser );

    /**
     * Removes a report follower.
     *
     * @param signalementId
     *            the report id which must be unfollowed
     * @param guid
     *            user guid
     * @throws NonExistentFollowItem
     *             exception thrown when we try to remove a following item when the user is not following the report
     */
    void removeFollower( Long signalementId, String guid ) throws NonExistentFollowItem;

    /**
     * Filters report by the criterias defined in dashboard.
     *
     * @param filter
     *            the report filter
     * @return a list of report for dashboard
     */
    List<DashboardSignalementDTO> findByDashboardFilter( SignalementDashboardFilter filter );

    /**
     * Finds reports by their Ids, and display a restricted list.
     *
     * @param signalementIds
     *            the reports ids
     * @param nbMaxItems
     *            the maximum number of items to return
     * @param offset
     *            the offset
     * @return a list of reports
     */
    List<Signalement> getByIds( List<Integer> signalementIds, Integer nbMaxItems, Integer offset );

    /**
     * Build the query to get the report ids matching the filter.
     *
     * @param filter
     *            the report filter
     * @return the query
     */
    List<Integer> getIdsSignalementByFilter( SignalementFilter filter );

    /**
     * Get all possible actions for a resource (report) and a given user.
     *
     * @param nIdSignalement
     *            the resource we want to have the possible actions
     * @param workflowId
     *            workflow type
     * @param user
     *            user to check
     * @return a list with all possible actions
     */
    Collection<Action> getListActionsByIdSignalementAndUser( int nIdSignalement, int workflowId, AdminUser user );

    /**
     * Check if a report is followable, based on its status.
     *
     * @param nIdSignalement
     *            The id of report to check
     * @return True if the report is in a followable state False otherwise
     */
    boolean isSignalementFollowable( int nIdSignalement );

    /**
     * Checks if the user has already followed this report.
     *
     * @param nIdSignalement
     *            Id of the report to check
     * @param userGuid
     *            User to check
     * @return True if the report has already been followed by the user False otherwise
     */
    boolean isSignalementFollowedByUser( int nIdSignalement, String userGuid );

    /**
     * Checks if the user has already followed this report.
     *
     * @param reference
     *            Id of the report reference
     * @param signalement
     *            the report
     * @return the report id reference
     */
    String getSignalementReference( String reference, Signalement signalement );

    /**
     * Add date dateMiseEnSurveillance for report.
     *
     * @param nIdSignalement
     *            Id of the report reference
     * @param dateMiseEnSurveillance
     *            the monitoring date
     */
    void doMettreSousSurveillance( int nIdSignalement, String dateMiseEnSurveillance );

    /**
     * Return the report history.
     *
     * @param idSignalement
     *            the report id
     * @param request
     *            HttpServletRequest
     * @return a json object containing the report history
     */
    JSONObject getHistorySignalement( Integer idSignalement, HttpServletRequest request );

    /**
     * Add a rejection date to the report.
     *
     * @param lIdSignalement
     *            the report id
     * @param dateRejet
     *            the rejection date
     */
    void setDateRejet( Integer lIdSignalement, String dateRejet );

    /**
     * Returns a requalification object by history and task ids.
     *
     * @param nIdHistory
     *            the workflow history id
     * @param idTask
     *            the workflow task id
     * @return the requalification object
     */
    SignalementRequalification getSignalementRequalificationByTaskHistory( int nIdHistory, int idTask );

    /**
     * Update the requalification.
     *
     * @param lIdSignalement
     *            the report id
     * @param nIdHistory
     *            the workflow history id
     * @param idTask
     *            the workflow task id
     */
    void setRequalificationIdHistory( Long lIdSignalement, int nIdHistory, int idTask );

    /**
     * Save the requalification of a report.
     *
     * @param lIdSignalement
     *            the report id
     * @param idTypeSignalement
     *            the report type id
     * @param adresse
     *            the report address
     * @param idSector
     *            the report sector
     * @param idTask
     *            the workflow task id
     * @param commentaireAgentTerrain
     *            commentaire Agent Terrain
     */
    void saveRequalification( long lIdSignalement, Integer idTypeSignalement, String adresse, Integer idSector, Integer idTask,
            String commentaireAgentTerrain );

    /**
     * Update the requalification history task.
     *
     * @param lIdSignalement
     *            the report id
     * @param idHistory
     *            the workflow history id
     * @param idTask
     *            the workflow task id
     */
    void setRequalificationIdHistoryAndIdTask( Long lIdSignalement, int idHistory, int idTask );

    /**
     * Gets the letter by month.
     *
     * @param month
     *            the month
     * @return the letter by month
     */
    String getLetterByMonth( int month );

    /**
     * Get the followers mails.
     *
     * @param idRessource
     *            the report id
     * @return a list of mails
     */
    List<String> getAllSuiveursMail( Integer idRessource );

    /**
     * Gets the signalements for the TDB.
     *
     * @param tableauDeBordFilter
     *            the tableau de bord filter
     * @return the signalements TDB
     */
    public Map<Integer, Map<Integer, Integer>> getSignalementsTDB( TableauDeBordFilter tableauDeBordFilter );

    /**
     * Gets the id signalements TDB.
     *
     * @param tableauDeBordFilter
     *            the tableau de bord filter
     * @return the id signalements TDB
     */
    public List<Integer> getIdSignalementsTDB( TableauDeBordFilter tableauDeBordFilter );

    /**
     * Update commentaire agent terrain.
     *
     * @param commentaireAgent
     *            commentaire agent terrain
     * @param lIdSignalement
     *            the report id
     */
    void updateCommentaireAgent( String commentaireAgent, Long lIdSignalement );

    /**
     * Find ano without state.
     * @param delay
     *         delay in minutes since anomaly creation before delete.
     *
     * @return the list
     */
    List<Long> findAnoWithoutState( int delay );

    /**
     * Find user for service done.
     *
     * @param userAccessCode
     *            user acess code
     * @param signaleur
     *            report creator
     * @return user mail
     */
    String findUserServiceFait( String userAccessCode, Signaleur signaleur );

    /**
     * Gets the repartition service fait masse.
     *
     * @param serviceFaitMasseFilter
     *            the service fait masse filter
     * @return the repartition service fait masse
     */
    Map<String, Integer> getRepartitionServiceFaitMasse( ServiceFaitMasseFilter serviceFaitMasseFilter );

    /**
     * Execute service fait masse.
     *
     * @param serviceFaitMasseFilter
     *            the service fait masse filter
     * @return true, if successful
     */
    boolean executeServiceFaitMasse( ServiceFaitMasseFilter serviceFaitMasseFilter );

    /**
     * Gets the signalements service programme ids.
     *
     * @return the signalements service programme ids
     */
    List<Integer> getSignalementsServiceProgrammeIds( );

    /**
     * Gets the signalements service programme tier ids.
     *
     * @return the signalements service programme tier ids
     */
    List<Integer> getSignalementsServiceProgrammeTierIds( );

    /**
     * Find Signalement prestataire.
     *
     * @param idSignalement
     *            id Signalement.
     * @return prestataire label.
     */
    String findPrestataireSignalement( int idSignalement );

    /**
     * Gets the state if date indicated.
     *
     * @param signalementFilter
     *            the signalement filter
     * @return the state if date indicated
     */
    EtatSignalement getStateIfDateIndicated( SignalementFilter signalementFilter );

    Collection<Action> getListActionsBySignalementAndUser( Signalement signalement, Integer signalementWorkflowId, AdminUser user, State state );
}
