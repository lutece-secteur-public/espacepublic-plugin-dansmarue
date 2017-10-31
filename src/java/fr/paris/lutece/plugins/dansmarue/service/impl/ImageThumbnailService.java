package fr.paris.lutece.plugins.dansmarue.service.impl;

import fr.paris.lutece.plugins.dansmarue.service.IPhotoService;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.image.ImageResourceProvider;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * 
 * This class provide services for ImageServiceTypeObjet
 * 
 */
public class ImageThumbnailService implements ImageResourceProvider
{
    private static final String IMAGE_RESOURCE_TYPE_ID = "photo_signalement_thumbnail";

    @Autowired
    private IPhotoService _photoService;


    /**
     * Get the resource for image
     * @param nIdPhoto The identifier of nIdPhoto object
     * @return The ImageResource
     */
    @Override
    public ImageResource getImageResource( int nIdPhoto )
    {
        return _photoService.getImageThumbnailResource( nIdPhoto );
    }

    /**
     * Get the type of resource
     * @return The type of resource
     */
    @Override
    public String getResourceTypeId( )
    {
        return IMAGE_RESOURCE_TYPE_ID;
    }
}
