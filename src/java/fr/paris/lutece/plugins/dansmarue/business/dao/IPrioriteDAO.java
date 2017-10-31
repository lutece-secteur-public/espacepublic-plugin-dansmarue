package fr.paris.lutece.plugins.dansmarue.business.dao;

import fr.paris.lutece.plugins.dansmarue.business.entities.Priorite;

import java.util.List;


public interface IPrioriteDAO
{
    public Long insert( Priorite priorite );

    public void remove( long lId );

    public Priorite load( long lId );

    public void store( Priorite priorite );
    
    /**
     * Select all the priorites
     * 
     */
    
    List<Priorite> getAllPriorite( );

}
