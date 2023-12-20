/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.dansmarue.service.deamon;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;

import fr.paris.lutece.plugins.dansmarue.business.dao.IPhotoDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.StockagePhotoUtils;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.image.ImageUtil;

/**
 * The Class SuppressionPhotosDaemon.
 */
public class SuppressionPhotosDaemon extends Daemon
{

    /** The Constant TYPE_ANOMALIE_CIBLE. */
    // Properties
    private static final String TYPE_ANOMALIE_CIBLE = "daemon.suppressionPhotos.anomalieCible";

    /** The Constant DUREE_EXISTENCE_ANOMALIE_CIBLE. */
    private static final String DUREE_EXISTENCE_ANOMALIE_CIBLE = "daemon.suppressionPhotos.dureeConservation";

    /** The Constant ETAT_ANOMALIE_CIBLE. */
    private static final String ETAT_ANOMALIE_CIBLE = "daemon.suppressionPhotos.etatsCible";

    /** The Constant NB_LINES_PER_REQUEST. */
    private static final String NB_LINES_PER_REQUEST = "daemon.suppressionPhotos.request.limit";

    /** The Constant PATH_PHOTO_DELETE. */
    private static final String PATH_PHOTO_DELETE = "../../images/photo_delete.jpg";

    /** The Constant PHOTO_MIME_TYPE. */
    private static final String PHOTO_MIME_TYPE = "image/jpeg";

    /** The photo DAO. */
    // dao
    private IPhotoDAO _photoDAO = SpringContextService.getBean( "photoDAO" );

    private StockagePhotoUtils _stockagePhotoUtils =  SpringContextService.getBean( "signalement.stockagePhotoUtils" );

    /**
     * Run.
     */
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run( )
    {
        Calendar dateMinusNbMonth = Calendar.getInstance( );
        AppLogService.info( "////////////////////// Lancement du daemon de suppression des photos au " + dateMinusNbMonth.getTime( ).toString( )
                + "//////////////////////" );
        // Type d'anomalie où la photo doit être supprimé
        List<String> anomaliesCible = Arrays.asList( AppPropertiesService.getProperty( TYPE_ANOMALIE_CIBLE ).split( "," ) );
        // Durée d'existence (en jours) à partir de laquelle la photo de l'anomalie doit être supprimé
        Integer dureeExistenceAnomalie = Integer.valueOf( AppPropertiesService.getProperty( DUREE_EXISTENCE_ANOMALIE_CIBLE ) );
        // Etat des anomalies ciblés par le daemon
        List<String> etatsCible = Arrays.asList( AppPropertiesService.getProperty( ETAT_ANOMALIE_CIBLE ).split( "," ) );

        Integer limitRequest = Integer.valueOf( AppPropertiesService.getProperty( NB_LINES_PER_REQUEST ) );

        AppLogService.info( " Récupération des photos des signalement " + anomaliesCible.toString( ) + " de plus de " + dureeExistenceAnomalie + " jours " );
        List<PhotoDMR> photos = _photoDAO.findPhotosForSupprPhotosDaemon( anomaliesCible, dureeExistenceAnomalie, etatsCible, limitRequest );

        ImageResource image = getPhotoDelete( );
        ImageResource imageResize = getResizeImage( image );

        for ( PhotoDMR photo : photos )
        {
            if ( _stockagePhotoUtils.deletePhotoOnNetAppServeur( photo ) )
            {
                photo.setImage( image );
                photo.setImageThumbnail( imageResize );
                photo.setDate( null );
                _stockagePhotoUtils.savePhotoOnNetAppServeur( photo );

                _photoDAO.updatePhoto( photo );
            }
        }
        AppLogService.info( photos.size( ) + " photos ont été supprimées" );
    }

    /**
     * Gets the photo delete.
     *
     * @return the photo delete
     */
    private ImageResource getPhotoDelete( )
    {
        byte [ ] imageInByte = null;
        try
        {
            BufferedImage image = ImageIO.read( new File( getClass( ).getClassLoader( ).getResource( PATH_PHOTO_DELETE ).toURI( ) ) );
            ByteArrayOutputStream baos = new ByteArrayOutputStream( );
            ImageIO.write( image, "jpg", baos );
            baos.flush( );
            imageInByte = baos.toByteArray( );
            baos.close( );
        }
        catch( Exception e )
        {
            AppLogService.error( "Erreur lors de la récupération de l'image de suppression" + e.getCause( ) );
        }
        ImageResource image = new ImageResource( );
        image.setImage( imageInByte );
        image.setMimeType( PHOTO_MIME_TYPE );

        return image;
    }

    /**
     * Gets the resize image.
     *
     * @param originalImage
     *            the original image
     * @return the resize image
     */
    private ImageResource getResizeImage( ImageResource originalImage )
    {
        String width = AppPropertiesService.getProperty( SignalementConstants.IMAGE_THUMBNAIL_RESIZE_WIDTH );
        String height = AppPropertiesService.getProperty( SignalementConstants.IMAGE_THUMBNAIL_RESIZE_HEIGHT );

        ImageResource imageResize = new ImageResource( );
        imageResize.setImage( ImageUtil.resizeImage( originalImage.getImage( ), width, height, 1 ) );
        imageResize.setMimeType( PHOTO_MIME_TYPE );

        return imageResize;
    }

}
