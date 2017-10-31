package fr.paris.lutece.plugins.dansmarue.business.dao;

import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;

import java.util.List;


public interface IArrondissementDAO
{

    Arrondissement getArrondissementByGeom( Double lng, Double lat );

    List<Arrondissement> getAllArrondissement( );

    List<Integer> getArrondissementsInSector( List<Integer> sectors );
    
    Arrondissement getByIdArrondissement( int nIdArrondissement );

}
