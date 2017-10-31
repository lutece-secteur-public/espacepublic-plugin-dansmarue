package fr.paris.lutece.plugins.dansmarue.service.impl;

import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.image.ImageResourceManager;
import fr.paris.lutece.portal.service.image.ImageResourceProvider;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import javax.inject.Inject;


/**
 * 
 * This class provide services for ImageServiceTypeObjet
 * 
 */
public class ImageObjetService implements ImageResourceProvider
{
    private static final String IMAGE_RESOURCE_TYPE_ID = "image_type_signalement";

    @Inject
    private ITypeSignalementService _typeSignalementService;

    private static ImageObjetService _singleton = new ImageObjetService( );

    /**
     * Creates a new instance of CategoryService
     */
    ImageObjetService( )
    {
        _typeSignalementService = (ITypeSignalementService) SpringContextService.getBean( "typeSignalementService" );

        ImageResourceManager.registerProvider( this );
    }

    /**
     * Get the unique instance of the service
     * 
     * @return The unique instance
     */
    public static ImageObjetService getInstance( )
    {
        return _singleton;
    }

    /**
     * Init
     */
    public static void init( )
    {
        // implicitly initialize the singleton
    }

    /**
     * Get the resource for image
     * @param nIdTypeSignalement The identifier of nIdTypeSignalement object
     * @return The ImageResource
     */
    @Override
    public ImageResource getImageResource( int nIdTypeSignalement )
    {
        return _typeSignalementService.getImageResource( nIdTypeSignalement );
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
