package fr.paris.lutece.plugins.dansmarue.service.impl;

import fr.paris.lutece.plugins.dansmarue.business.dao.IObservationRejetDAO;
import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.service.IObservationRejetService;
import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.List;

import javax.inject.Inject;


public class ObservationRejetService implements IObservationRejetService
{
    @Inject
    private IObservationRejetDAO _observationRejetDAO;
    @Inject
    private ISignalementDAO _signalementDAO;
    
    
    //MESSAGES
    private static final String MESSAGE_ERROR_OBSERVATION_REJET_NAME_MUST_BE_UNIQUE = "dansmarue.message.observationRejet.error.alreadyExists";
    private static final String MESSAGE_ERROR_OBSERVATION_REJET_ORDER_INVALID = "dansmarue.message.observationRejet.error.order.invalid";

    /**
     * Return all ObservationRejet
     * @return a list of ObservationRejet
     */
    public List<ObservationRejet> getAllObservationRejet( )
    {
        Plugin pluginSignalement = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
        return _observationRejetDAO.getAllObservationRejet( pluginSignalement );
    }

    /**
     * Delete a nature objet
     */

    public void doDeleteObservationRejet( int nIdObservationRejet )
    {
        //check if the observation can be removed
        if ( !(_observationRejetDAO.countByIdObservationRejet( nIdObservationRejet ) > 0) )
        {
        
        //Update order
        _observationRejetDAO.decreaseOrdreOfAllNext(nIdObservationRejet);
        
        // Remove the nature objet
        _observationRejetDAO.remove( nIdObservationRejet );
        }
    }

    /**
     * Save a new observationRejet
     */

    public void doSaveObservationRejet( ObservationRejet observationRejet )
    {

        if ( _observationRejetDAO.existsObservationRejet( observationRejet) )
        {
            throw new BusinessException( observationRejet, MESSAGE_ERROR_OBSERVATION_REJET_NAME_MUST_BE_UNIQUE );
        }
        else
        {
            if ( observationRejet.getId( ) != null && observationRejet.getId( ) > 0 )
            {
                _observationRejetDAO.store( observationRejet );
            }
            else
            {
            	validateRejetOrder(observationRejet);
            	if(observationRejet.getOrdre() == null){
            		int ordre = _observationRejetDAO.getObservationRejetCount() + 1;
            		observationRejet.setOrdre(ordre);
            	}
                int idObservationRejet = _observationRejetDAO.insert( observationRejet );
                _observationRejetDAO.increaseOrdreOfAllNext(idObservationRejet);
            }
        }

    }
    
    
    public ObservationRejet getById( Integer nIdObservationRejet )
    {

        return _observationRejetDAO.load( nIdObservationRejet );

    }

    /**
     * Return all ObservationRejet actifs
     * @return a list of ObservationRejet actifs
     */
    public List<ObservationRejet> getAllObservationRejetActif( )
    {
        return _observationRejetDAO.getAllObservationRejetActif( );
    }
    
    /**
     * Updates an observation rejet order, by its id
     * @param observationRejet object containing id
     */
    public void updateObservationRejetOrdre(ObservationRejet observationRejet){
    	_observationRejetDAO.updateObservationRejetOrdre(observationRejet);
    }
    
    /**
     * Increase the ordre of the reject
     * @param observationRejet
     */
    public void increaseOrdreOfRejet(ObservationRejet observationRejet){
    	int observationRejetCount = _observationRejetDAO.getObservationRejetCount();
    	if(observationRejet != null && observationRejet.getOrdre() < observationRejetCount){
	    	_observationRejetDAO.decreaseOrdreOfNextRejet(observationRejet);
	    	observationRejet.setOrdre(observationRejet.getOrdre()+1);
	    	_observationRejetDAO.updateObservationRejetOrdre(observationRejet);
    	}
    }
    
    /**
     * Decrease the ordre of the rejet
     * @param observationRejet
     */
    public void decreaseOrdreOfRejet(ObservationRejet observationRejet){
    	if(observationRejet != null && observationRejet.getOrdre() > 1){
    		_observationRejetDAO.increaseOrdreOfPreviousRejet(observationRejet);
    		observationRejet.setOrdre(observationRejet.getOrdre()-1);
    		_observationRejetDAO.updateObservationRejetOrdre(observationRejet);
    	}
    }
    
    /**
     * Increases all the next orders
     * @param nIdObservationRejet id of the observationrejet which following orders must be increased
     */
    public void increaseOrdreOfAllNext(int nIdObservationRejet){
    	_observationRejetDAO.increaseOrdreOfAllNext(nIdObservationRejet);
    }
    
    /**
     * Decreases all the next orders
     * @param nIdObservationRejet id of the observationrejet which following orders must be decreased
     */
    public void decreaseOrdreOfAllNext(int nIdObservationRejet){
    	_observationRejetDAO.decreaseOrdreOfAllNext(nIdObservationRejet);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int countByIdObservationRejet(int nIdObservationRejet){
    	return _observationRejetDAO.countByIdObservationRejet(nIdObservationRejet);
    }
    
    /**
     * Checks if the order is valid
     * @param observationRejet
     * @return true if test pass
     * 		   false otherwise
     */
    private boolean validateRejetOrder(ObservationRejet observationRejet) throws BusinessException{
    	if(observationRejet.getOrdre() == null){
    		throw new BusinessException(observationRejet,MESSAGE_ERROR_OBSERVATION_REJET_ORDER_INVALID);
    	}
    	int count = getObservationRejetCount();
    	if(observationRejet.getOrdre() < 1 || observationRejet.getOrdre() > count+1){
    		throw new BusinessException(observationRejet,MESSAGE_ERROR_OBSERVATION_REJET_ORDER_INVALID);
    	}
    	return true;
    }
    
    public int getObservationRejetCount(){
    	return _observationRejetDAO.getObservationRejetCount();
    }
    
}