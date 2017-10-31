package fr.paris.lutece.plugins.dansmarue.utils;

import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.image.ImageUtil;

public class ImgUtils
{

    /**
     * Instantiates a new list ImgUtils.
     */
    private ImgUtils( )
    {
        // does nothing
    }

    /**
     * Vérifie que la qualité de l'image ne depasse pas la taille max en paramètre (par défaut 300ko).
     *
     * @param blobImage the blob image
     * @return the byte[]
     */
    public static byte[] checkQuality( byte[] blobImage )
    {
        String width = AppPropertiesService.getProperty(SignalementConstants.IMAGE_RESIZE_WIDTH);
        String height = AppPropertiesService.getProperty(SignalementConstants.IMAGE_RESIZE_HEIGHT);
        
        // Redimenssionnement et compression de l'image 
        byte[] resizeImage = ImageUtil.resizeImage( blobImage, width, height, 0.85f);
        
        return resizeImage;
    }
    
}
