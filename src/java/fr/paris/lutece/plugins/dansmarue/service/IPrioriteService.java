package fr.paris.lutece.plugins.dansmarue.service;

import fr.paris.lutece.plugins.dansmarue.business.entities.Priorite;

import java.util.List;


public interface IPrioriteService
{
    Long insert( Priorite priorite );

    void remove( long lId );

    Priorite load( long lId );

    void store( Priorite priorite );
    
    /**
     * Select all the priorites
     * 
     */
    
    List<Priorite> getAllPriorite( );

}
