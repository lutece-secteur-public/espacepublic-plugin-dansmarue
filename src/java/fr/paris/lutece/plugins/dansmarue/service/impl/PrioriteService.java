package fr.paris.lutece.plugins.dansmarue.service.impl;

import fr.paris.lutece.plugins.dansmarue.business.dao.IPrioriteDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Priorite;
import fr.paris.lutece.plugins.dansmarue.service.IPrioriteService;

import java.util.List;

import javax.inject.Inject;


public class PrioriteService implements IPrioriteService
{

    @Inject
    private IPrioriteDAO _prioriteDAO;

    public Long insert( Priorite priorite )
    {
        return _prioriteDAO.insert( priorite );
    }


    public void remove( long lId )
    {
        _prioriteDAO.remove( lId );

    }


    public Priorite load( long lId )
    {
        return _prioriteDAO.load( lId );
    }


    public void store( Priorite priorite )
    {
        _prioriteDAO.store( priorite );

    }

    public List<Priorite> getAllPriorite( )
    {
        return _prioriteDAO.getAllPriorite( );
    }

}
