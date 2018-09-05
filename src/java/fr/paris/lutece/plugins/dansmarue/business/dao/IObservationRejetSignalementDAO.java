package fr.paris.lutece.plugins.dansmarue.business.dao;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;

public interface IObservationRejetSignalementDAO
{

    /**
     * Inserts an entry of a report reject reason
     * 
     * @param idSignalement
     *            The report id
     * @param idRaisonRejet
     *            The reject id
     * @param observationRejetComment
     *            The comment of the reason "Autre"
     */
    void insert( int idSignalement, Integer idRaisonRejet, String observationRejetComment );

    /**
     * Removes an entry of a report reject reason
     * 
     * @param idSignalement
     *            The reporting id
     * @param idRaisonRejet
     *            The reject id
     */
    void delete( int idSignalement, Integer idRaisonRejet );

    /**
     * Finds alls reject reasons of a report
     * 
     * @param idSignalement
     *            The report Id
     * @return List of reject observation matching the report id
     */
    List<ObservationRejet> findByIdSignalement( int idSignalement );

}
