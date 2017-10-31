package fr.paris.lutece.plugins.dansmarue.service;

import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.portal.service.image.ImageResource;

import java.util.List;


public interface IPhotoService
{
    Long insert( PhotoDMR photo );

    void remove( long lId );

    PhotoDMR load( long lId );

    void store( PhotoDMR photo );

    ImageResource getImageResource( int nKey );

    /**
     * Load a photo with the id of the signalement
     * 
     * @param lId the signalement id
     */
    PhotoDMR loadByIdSignalement( long lId );
    
    List<PhotoDMR> findBySignalementId( long lIdSignalement );

    List<PhotoDMR> findWithFullPhotoBySignalementId( long lIdSignalement );

    ImageResource loadPhoto( int nIdPhoto );

    ImageResource getImageThumbnailResource( int nIdPhoto );
}
    
