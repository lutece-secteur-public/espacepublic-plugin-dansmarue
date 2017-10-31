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
     * Get all arrondissement
     * @return the list of all arrondissement
     */
    public List<Arrondissement> getAllArrondissement( )
    {
        return _arrondissementDAO.getAllArrondissement( );
    }

    /**
     * Get the correct arrondissement for a given geometry
     * @return the list of all arrondissement
     */
    public Arrondissement getArrondissementByGeom( double lng, double lat )
    {
        return this._arrondissementDAO.getArrondissementByGeom( lng, lat );
    }

    public List<Integer> getArrondissementsInSector( List<Integer> sectors )
    {
        return _arrondissementDAO.getArrondissementsInSector( sectors );
    }

    @Override
    public Arrondissement getByIdArrondissement( int nIdArrondissement )
    {
        return _arrondissementDAO.getByIdArrondissement( nIdArrondissement );
    }

}
