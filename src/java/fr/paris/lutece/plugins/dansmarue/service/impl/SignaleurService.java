package fr.paris.lutece.plugins.dansmarue.service.impl;

import fr.paris.lutece.plugins.dansmarue.business.dao.ISignaleurDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.service.ISignaleurService;

import javax.inject.Inject;


public class SignaleurService implements ISignaleurService
{
    @Inject
    private ISignaleurDAO _signaleurDAO;

    public Long insert( Signaleur signaleur )
    {
        return _signaleurDAO.insert( signaleur );
    }

    public void remove( long lId )
    {
        _signaleurDAO.remove( lId );

    }

    public Signaleur load( long lId )
    {
        return _signaleurDAO.load( lId );
    }

    public void store( Signaleur signaleur )
    {
        _signaleurDAO.store( signaleur );

    }

    /**
     * Load a signaleur by the signalement id
     * 
     * @param lId the signaleur id
     */
    public Signaleur loadByIdSignalement( long lId )
    {
        return _signaleurDAO.loadByIdSignalement( lId );
    }

    /**
     * Upadate a signaleur
     * 
     * @param signaleur the signaleur to update
     */
    public void update( Signaleur signaleur )
    {
        _signaleurDAO.update( signaleur );
    }

}
