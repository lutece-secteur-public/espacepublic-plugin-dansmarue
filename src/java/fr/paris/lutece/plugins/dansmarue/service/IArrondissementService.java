package fr.paris.lutece.plugins.dansmarue.service;

import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;

import java.util.List;


public interface IArrondissementService
{
    /**
     * Get all arrondissement
     * @return the list of all arrondissement
     */
    List<Arrondissement> getAllArrondissement( );

    /**
     * Get the correct arrondissement for a given geometry
     * @return the list of all arrondissement
     */
    Arrondissement getArrondissementByGeom( double lng, double lat );

    List<Integer> getArrondissementsInSector( List<Integer> sectors );
    
    Arrondissement getByIdArrondissement( int nIdArrondissement );

}
