package fr.paris.lutece.plugins.dansmarue.business.dao;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;

import java.util.List;


public interface ISignaleurDAO
{
    public Long insert( Signaleur signaleur );

    public void remove( long lId );

    public Signaleur load( long lId );

    public void store( Signaleur signaleur );

    List<Signaleur> findBySignalementId( long lIdSignalement );

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
    void update( Signaleur signaleur );
}
