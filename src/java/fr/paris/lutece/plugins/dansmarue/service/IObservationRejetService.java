package fr.paris.lutece.plugins.dansmarue.service;


import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;

import java.util.List;


public interface IObservationRejetService
{
    /**
     * Return all ObservationRejet
     * @return a list of ObservationRejet
     */
    List<ObservationRejet> getAllObservationRejet( );

    /**
     * Delete a ObservationRejet
     */
    void doDeleteObservationRejet( int nIdObservationRejet );

    void doSaveObservationRejet( ObservationRejet observationRejet );

    ObservationRejet getById( Integer nIdObservationRejet );
    
    /**
     * Return all ObservationRejet actifs
     * @return a list of ObservationRejet actifs
     */
    List<ObservationRejet> getAllObservationRejetActif( );

    void increaseOrdreOfRejet(ObservationRejet observationRejet);
    
    void decreaseOrdreOfRejet(ObservationRejet observationRejet);
    
    void increaseOrdreOfAllNext(int nIdObservationRejet);
    
    void decreaseOrdreOfAllNext(int nIdObservationRejet);

    /**
     * Counts the number of uses of an observationRejet
     * @param nIdObservationRejet
     * 			The id of the observation rejet which must be count
     * @return
     * 		  The number of times this observation rejet has been used
     */
	int countByIdObservationRejet(int nIdObservationRejet);
}
