package fr.paris.lutece.plugins.dansmarue.service;

import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.business.sector.Sector;


public interface IAdresseService
{
    Long insert( Adresse adresse );

    void remove( long lId );

    Adresse load( long lId );

    void store( Adresse adresse );

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

    /**
     * Gets the arrondissement by geom.
     * 
     * @param lng the lng
     * @param lat the lat
     * @return an arrondissement by geom.
     */
    Arrondissement getArrondissementByGeom( Double lng, Double lat );

    /**
     * Gets the secteur by geom and type signalement.
     * 
     * @param lng the lng
     * @param lat the lat
     * @param id the id
     * @return the secteur by geom and type signalement
     */
    Sector getSecteurByGeomAndTypeSignalement( Double lng, Double lat, Integer id );

    /**
     * Gets the sector by geom and type signalement.
     * 
     * @param lng the lng
     * @param lat the lat
     * @param idUnitParent the unit id of the parent
     * @return the sector by geom and type signalement
     */
    Sector getSectorByGeomAndIdUnitParent( Double lng, Double lat, Integer idUnitParent );

}
