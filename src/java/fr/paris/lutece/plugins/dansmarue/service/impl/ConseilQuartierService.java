package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import fr.paris.lutece.plugins.dansmarue.business.dao.IConseilQuartierDao;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.ConseilQuartier;
import fr.paris.lutece.plugins.dansmarue.service.IConseilQuartierService;


public class ConseilQuartierService implements IConseilQuartierService
{
    @Inject
    @Named( "signalement.conseilQuartierDAO" )
    private IConseilQuartierDao _conseilQuartierDAO;
    
    @Override
    public List<ConseilQuartier> selectQuartiersList( )
    {
        return _conseilQuartierDAO.selectQuartiersList( );
    }

}
