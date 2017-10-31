package fr.paris.lutece.plugins.dansmarue.business.dao;

import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.portal.service.image.ImageResource;

import java.util.List;


public interface IPhotoDAO
{
    Long insert( PhotoDMR photo );

    void remove( long lId );

    PhotoDMR load( long lId );

    void store( PhotoDMR photo );

    /**
     * Load the data of the image from the table.
     * 
     * @param nIdPhoto The identifier of the formResponse
     * @return the image resource
     */
    ImageResource loadPhoto( int nIdPhoto );

    List<PhotoDMR> findBySignalementId( long lIdSignalement );

    List<PhotoDMR> findWithFullPhotoBySignalementId( long lIdSignalement );

    /**
     * Load a photo with the id of the signalement
     * 
     * @param lId the signalement id
     */
    PhotoDMR loadByIdSignalement( long lId );

    /**
     * Load a photo thumbnail.
     * 
     * @param nIdPhoto the photo id
     * @return the image resource
     */
    ImageResource loadPhotoThumbnail( int nIdPhoto );
}
