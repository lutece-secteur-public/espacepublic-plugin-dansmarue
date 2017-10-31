package fr.paris.lutece.plugins.dansmarue.service;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;


public interface ISignaleurService
{
    Long insert( Signaleur signaleur );

    void remove( long lId );

    Signaleur load( long lId );

    void store( Signaleur signaleur );

    /**
     * Load a signaleur by the signalement id
     * 
     * @param lId the signaleur id
     */
    Signaleur loadByIdSignalement( long lId );
    
    /**
     * Upadate a signaleur
     * 
     * @param signaleur the signaleur to update
     */
    public void update( Signaleur signaleur );

}
