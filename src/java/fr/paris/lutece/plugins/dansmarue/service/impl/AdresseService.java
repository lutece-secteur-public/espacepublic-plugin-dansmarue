package fr.paris.lutece.plugins.dansmarue.service.impl;

import fr.paris.lutece.plugins.dansmarue.business.dao.IAdresseDAO;
import fr.paris.lutece.plugins.dansmarue.business.dao.IArrondissementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.service.IAdresseService;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.business.sector.ISectorDAO;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.business.sector.Sector;

import javax.inject.Inject;
import javax.inject.Named;


public class AdresseService implements IAdresseService
{

    @Inject
    @Named( "signalementAdresseDAO" )
    private IAdresseDAO _adresseSignalementDAO;

    @Inject
    @Named( "signalement.arrondissementDAO" )
    private IArrondissementDAO _arrondissementDAO;

    @Inject
    @Named( "unittree-dansmarue.sectorDAO" )
    private ISectorDAO _sectorDAO;

    public Long insert( Adresse adresse )
    {
        return _adresseSignalementDAO.insert( adresse );
    }

    public void remove( long lId )
    {
        _adresseSignalementDAO.remove( lId );

    }

    public Adresse load( long lId )
    {
        return _adresseSignalementDAO.load( lId );
    }

    public void store( Adresse adresse )
    {
        _adresseSignalementDAO.store( adresse );

    }

    /**
     * Load an adresse by its Id signalement
     * 
     * @param lId the signalement id
     */
    public Adresse loadByIdSignalement( long lId )
    {
        return _adresseSignalementDAO.loadByIdSignalement( lId );
    }

    /**
     * Update an adresse
     * 
     */
    public void update( Adresse adresse )
    {
        _adresseSignalementDAO.update( adresse );

    }

    @Override
    public Arrondissement getArrondissementByGeom( Double lng, Double lat )
    {
        return this._arrondissementDAO.getArrondissementByGeom( lng, lat );
    }

    @Override
    public Sector getSecteurByGeomAndTypeSignalement( Double lng, Double lat, Integer idTypeSignalement )
    {
        return this._sectorDAO.getSectorByGeomAndTypeSignalement( lng, lat, idTypeSignalement );
    }

    @Override
    public Sector getSectorByGeomAndIdUnitParent( Double lng, Double lat, Integer idUnitParent )
    {
        return this._sectorDAO.getSectorByGeomAndIdUnitParent( lng, lat, idUnitParent );
    }

}
