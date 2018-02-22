package fr.paris.lutece.plugins.dansmarue.business.dao;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.ConseilQuartier;

public interface IConseilQuartierDao
{
    List<ConseilQuartier> selectQuartiersList();
    
    ConseilQuartier load( int nId );
    
    ConseilQuartier selectQuartierByAdresse( int nIdAdresse );
}
