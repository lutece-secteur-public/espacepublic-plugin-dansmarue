package fr.paris.lutece.plugins.dansmarue.business.dao;

import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;
import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;


public interface IObservationRejetDAO
{
    public Integer insert( ObservationRejet observationRejet );

    public void remove( long lId );

    public ObservationRejet load( Integer lId );

    public void store( ObservationRejet observationRejet );

    public List<ObservationRejet> getAllObservationRejet( Plugin plugin );

    public boolean existsObservationRejet( ObservationRejet observationRejet );
    
    /**
     * Return all ObservationRejet actifs
     * @return a list of ObservationRejet actifs
     */
    List<ObservationRejet> getAllObservationRejetActif( );
    
    void updateObservationRejetOrdre(ObservationRejet observationRejet);

    void decreaseOrdreOfNextRejet(ObservationRejet observationRejet);
    
    void increaseOrdreOfPreviousRejet(ObservationRejet observationRejet);
    
    void increaseOrdreOfAllNext(int nIdObservationRejet);
    
    void decreaseOrdreOfAllNext(int nIdObservationRejet);

    int getObservationRejetCount();
    
    /**
     * Counts the number of time the observation rejet has been used
    * @param idObservationRejet the id of observation rejet to get the use count
    * @return the number of observation rejet uses
    * */
	int countByIdObservationRejet(int idObservationRejet);
	
}
