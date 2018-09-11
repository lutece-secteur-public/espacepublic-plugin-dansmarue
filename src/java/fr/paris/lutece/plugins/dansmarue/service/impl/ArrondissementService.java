package fr.paris.lutece.plugins.dansmarue.service.impl;

import fr.paris.lutece.plugins.dansmarue.business.dao.IArrondissementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.service.IArrondissementService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class ArrondissementService implements IArrondissementService
{
    @Inject
    @Named( "signalement.arrondissementDAO" )
    private IArrondissementDAO _arrondissementDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Arrondissement> getAllArrondissement( )
    {
        return _arrondissementDAO.getAllArrondissement( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Arrondissement getArrondissementByGeom( double lng, double lat )
    {
        return this._arrondissementDAO.getArrondissementByGeom( lng, lat );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getArrondissementsInSector( List<Integer> sectors )
    {
        return _arrondissementDAO.getArrondissementsInSector( sectors );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Arrondissement getByIdArrondissement( int nIdArrondissement )
    {
        return _arrondissementDAO.getByIdArrondissement( nIdArrondissement );
    }

}
