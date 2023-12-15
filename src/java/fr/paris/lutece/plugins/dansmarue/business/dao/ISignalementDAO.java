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
package fr.paris.lutece.plugins.dansmarue.business.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import fr.paris.lutece.plugins.dansmarue.business.entities.RequalificationMasseFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.ServiceFaitMasseFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementDashboardFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementRequalification;
import fr.paris.lutece.plugins.dansmarue.business.entities.TableauDeBordFilter;
import fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties;
import fr.paris.lutece.plugins.dansmarue.service.dto.DashboardSignalementDTO;
import fr.paris.lutece.plugins.dansmarue.service.dto.DossierSignalementDTO;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * The Interface ISignalementDAO.
 */
public interface ISignalementDAO
{

    /**
     * Inserts a report.
     *
     * @param signalement
     *            the report
     * @return the generated report id
     */
    Long insert( Signalement signalement );

    /**
     * Removes a report.
     *
     * @param lId
     *            the id
     */
    void remove( long lId );

    /**
     * Load a report by its id.
     *
     * @param lId
     *            the report id
     * @return the report
     */
    Signalement load( long lId );

    /**
     * Returns a list of reports according to the search parameters contained in the filter.
     *
     * @param filter
     *            the request based filter
     * @param paginationProperties
     *            the pagination
     * @param plugin
     *            the plugin
     * @return a list of reports
     */
    List<Signalement> findByFilter( SignalementFilter filter, PaginationProperties paginationProperties, Plugin plugin );

    /**
     * Load a report by its id.
     *
     * @param nId
     *            the report id
     * @return the report
     */
    Signalement loadById( long nId );

    /**
     * Gets the anomalie by number.
     *
     * @param number
     *            the number
     * @return the anomalie by number
     */
    Signalement getAnomalieByNumber( String number );

    /**
     * Update a report.
     *
     * @param signalement
     *            the report to update
     */
    void update( Signalement signalement );

    /**
     * Load all report with columns : Priorité, Type, Direction, Adresse, Date de création, Etat.
     *
     * @return all the reports
     */
    List<Signalement> getAllSignalement( );

    /**
     * Gets the reports by status id.
     *
     * @param statusId
     *            the status id
     * @param limit
     *            time limit before archiving
     * @return the reports list
     */
    List<Signalement> getSignalementsArchivableByStatusId( Integer statusId, Integer limit );

    /**
     * Load all reports ids.
     *
     * @return the list of all reports ids
     */
    List<Integer> getAllIdSignalement( );

    /**
     * Add one to a report's follow.
     *
     * @param nIdSignalement
     *            the report id
     */
    void incrementeSuiviByIdSignalement( Integer nIdSignalement );

    /**
     * Retrieves one to a report's follow.
     *
     * @param nIdSignalement
     *            the report id
     */
    void decrementSuiviByIdSignalement( Integer nIdSignalement );

    /**
     * Select all the reports for a given point and a radius.
     *
     * @param lat
     *            the lat
     * @param lng
     *            the lng
     * @param radius
     *            the radius
     * @param listStatus
     *            the list status
     * @return a list of all the reports id in the given parameter
     */
    List<Integer> findAllSignalementInPerimeter( Double lat, Double lng, Integer radius, List<Integer> listStatus );

    /**
     * Select all the reports for a given point and a radius.
     *
     * @param lat
     *            the lat
     * @param lng
     *            the lng
     * @param radius
     *            the radius
     * @param listStatus
     *            the list status
     * @return a list of all the reports ids in the given parameter
     */
    List<DossierSignalementDTO> findAllSignalementInPerimeterWithDTO( Double lat, Double lng, Integer radius, List<Integer> listStatus );

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
     * Returns all the reports linked to a selected phone number.
     *
     * @param idTelephone
     *            the phone id
     * @return list of reports
     */
    List<Integer> findByIdTelephone( String idTelephone );

    /**
     * Get a reports by a token.
     *
     * @param token
     *            the token
     * @param plugin
     *            the plugin
     * @return the report if exists or null
     */
    Signalement getSignalementByToken( String token, Plugin plugin );

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
     * Count the number of results for a given query.
     *
     * @param filter
     *            the signalementfilter
     * @param plugin
     *            the plugin
     * @return the query
     */
    Integer countIdSignalementByFilter( SignalementFilter filter, Plugin plugin );

    /**
     * Build the query to count the number of results for a given query.
     *
     * @param filter
     *            the signalementfilter
     * @return the query
     */
    String buildSQLQueryForCount( SignalementFilter filter );

    /**
     * Insert a message creation report (linked with notification user task workflow).
     *
     * @param messageCreation
     *            the default message send to user when creating a report
     */
    void insertMessageCreationSignalement( String messageCreation );

    /**
     * Update the message creation report (linked with notification user task workflow).
     *
     * @param messageCreation
     *            the default message send to user when creating a report
     */
    void updateMessageCreationSignalement( String messageCreation );

    /**
     * Load the message creation report (linked with notification user task workflow).
     *
     * @return the message creation report
     */
    String loadMessageCreationSignalement( );

    /**
     * Remove the message creation report (linked with notification user task workflow).
     */
    void removeMessageCreationSignalement( );

    /**
     * Increments the number of congratulations for this report.
     *
     * @param idSignalement
     *            the report id, to increment
     */
    void incrementFelicitationsByIdSignalement( int idSignalement );

    /**
     * Find the report to display in the dashboard.
     *
     * @param filter
     *            the report filter
     * @param pluginSignalement
     *            the plugin
     * @return the dashboard report list
     */
    List<DashboardSignalementDTO> findByDashboardFilter( SignalementDashboardFilter filter, Plugin pluginSignalement );

    /**
     * Build the query to get the reports ids matching the filter.
     *
     * @param filter
     *            the signalementfilter
     * @param plugin
     *            the plugin
     * @return the query
     */
    List<Integer> getIdsSignalementByFilter( SignalementFilter filter, Plugin plugin );

    /**
     * Execute Query to add a monitoring date for this report.
     *
     * @param idSignalement
     *            the report id
     * @param dateMiseEnSurveillance
     *            the monitoring date
     */
    void addMiseEnSurveillanceDate( int idSignalement, String dateMiseEnSurveillance );

    /**
     * Find reports ids canditate for WebServicePartnerDeamon.
     *
     * @param signalementState
     *            the report state
     * @param nbDays
     *            number of days between current date and signalement creation date
     * @return list of reports ids.
     */
    List<Integer> findIdsSingalementForWSPartnerDeamon( int signalementState, int nbDays );

    /**
     * Add a rejection date to the report.
     *
     * @param idSignalement
     *            the report id
     * @param dateRejet
     *            the rejection date
     */
    void setDateRejet( int idSignalement, String dateRejet );

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
     * @param idHistory
     *            the workflow history id
     * @param idTask
     *            the workflow task id
     * @param commentaireAgentTerrain
     *            commentaire Agent Terrain
     */
    void saveRequalification( long lIdSignalement, Integer idTypeSignalement, String adresse, Integer idSector, Integer idTask,
            Integer idHistory,String commentaireAgentTerrain );

    /**
     * Returns a list of requalification entries for a report, if it has been requalified.
     *
     * @param lIdSignalement
     *            the report id
     * @return a list of requalification entries
     */
    List<SignalementRequalification> getRequalification( long lIdSignalement );

    /**
     * Returns the mail id of the service done.
     *
     * @param idSignalement
     *            the report id
     * @return the service done mail id
     */
    Integer getIdMailServiceFait( Long idSignalement );

    /**
     * Returns a requalification object by history and task ids.
     *
     * @param idHistory
     *            the workflow history id
     * @param idTask
     *            the workflow task id
     * @return the requalification object
     */
    SignalementRequalification getSignalementRequalificationByTaskHistory( int idHistory, int idTask );

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
     * Update commentaitre agent terrain.
     *
     * @param commentaireAgent
     *            new commentaire agent terrain
     * @param lIdSignalement
     *            the report id
     */
    void updateCommentaireAgent( String commentaireAgent, long lIdSignalement );

    /**
     * Find ano without state.
     *
     * @param delay
     *            delay in minutes since anomaly creation before delete.
     * @return the list
     */
    List<Long> findAnoWithoutState( int delay );

    /**
     * Gets the repartition service fait masse.
     *
     * @param serviceFaitMasseFilter
     *            the service fait masse filter
     * @return the repartition service fait masse
     */
    Map<String, Integer> getRepartitionServiceFaitMasse( ServiceFaitMasseFilter serviceFaitMasseFilter );

    /**
     * Adds the service fait histo masse.
     *
     * @param serviceFaitMasseFilter
     *            the service fait masse filter
     */
    void addServiceFaitHistoMasse( ServiceFaitMasseFilter serviceFaitMasseFilter );

    /**
     * Update state service fait masse.
     *
     * @param serviceFaitMasseFilter
     *            the service fait masse filter
     */
    void updateStateServiceFaitMasse( ServiceFaitMasseFilter serviceFaitMasseFilter );

    /**
     * Update date passage service fait masse.
     *
     * @param serviceFaitMasseFilter
     *            the service fait masse filter
     */
    void updateDatePassageServiceFaitMasse( ServiceFaitMasseFilter serviceFaitMasseFilter );

    /**
     * Gets the repartition requalification masse.
     *
     * @param requalificationMasseFilter
     *            the requalification masse filter
     * @return the repartition requalification masse
     */
    Map<String, Integer> getRepartitionRequalificationMasse( RequalificationMasseFilter requalificationMasseFilter );

    /**
     * Adds the requalification histo masse.
     *
     * @param requalificationMasseFilter
     *            the requalification masse filter
     */
    void addRequalificationHistoMasse( RequalificationMasseFilter requalificationMasseFilter );

    /**
     * Update type requalification masse.
     *
     * @param requalificationMasseFilter
     *            the requalification masse filter
     */
    void updateTypeRequalificationMasse( RequalificationMasseFilter requalificationMasseFilter );

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
     * Find signalement prestataire.
     *
     * @param idSignalement
     *            idSignalement
     * @return label prestataire.
     */
    String findLabelPrestataireSignalement( int idSignalement );

    /**
     * Gets action for state.
     *
     * @param stateId
     *            the state id
     * @param workflowId
     *            the workflow id
     * @return the action for state
     */
    Collection<Action> getActionForState( int stateId, Integer workflowId );

    /**
     * Search signalement with id list. Method use by mobil application
     *
     * @param idsSignalement
     *            List of id signalement
     * @return List Signalement
     */
    List<Signalement> getSignalementForMobilByListId( List<Integer> idsSignalement );

    void addHistoriqueCommentaireAgentTerrain( Signalement signalement, int nIdResourceHistory );

    String getHistoriqueCommentaireAgentTerrain( int nIdResourceHistory );
}
