package fr.paris.lutece.plugins.dansmarue.business.dao;

import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;

import java.util.List;


public interface IAdresseDAO
{
    public Long insert( Adresse adresse );

    public void remove( long lId );

    public Adresse load( long lId );

    public void store( Adresse adresse );

    List<Adresse> findBySignalementId( long lIdSignalement );

    /**
     * Load an adresse by its Id signalement
     * 
     * @param lId the signalement id
     */
    Adresse loadByIdSignalement( long lId );

    /**
     * Update an adresse
     * 
     */
    void update( Adresse adresse );
}