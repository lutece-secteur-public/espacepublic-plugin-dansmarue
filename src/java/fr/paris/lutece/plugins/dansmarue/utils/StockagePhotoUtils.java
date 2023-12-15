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
package fr.paris.lutece.plugins.dansmarue.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

/**
 *Communication with S3 NetApp server.
 *
 *
 */
public class StockagePhotoUtils
{

    private static final int    NB_RANDOM_CHAR         = 100;

    private static final String MIME_TYPE_DEFAULT      = "image/jpeg";
    private static final String LOG_ERROR_START        = "Signalement id ";

    private static MinioClient  _s3Client;

    // Properties
    private static final String PROPERTIES_S3_URL      = "signalement.s3.url";
    private static final String PROPERTIES_S3_BUCKET   = "signalement.s3.bucket";
    private static final String PROPERTIES_S3_KEY      = "signalement.s3.key";
    @SuppressWarnings( "squid:S2068" )

    private static final String PROPERTIES_S3_PASSWORD = "signalement.s3.password";
    private static final String PROPERTIES_S3_PATH     = "signalement.s3.path";

    private StockagePhotoUtils( )
    {

    }

    /**
     * Get S3 client to interact with NetApp server.
     * @return _s3Client
     */
    private MinioClient getS3Client( )
    {

        if ( _s3Client == null )
        {

            _s3Client = MinioClient.builder( ).endpoint( AppPropertiesService.getProperty( PROPERTIES_S3_URL ) )
                    .credentials( AppPropertiesService.getProperty( PROPERTIES_S3_KEY ), AppPropertiesService.getProperty( PROPERTIES_S3_PASSWORD ) ).build( );
        }

        return _s3Client;
    }

    /**
     * Save a photo on  NetApp Serveur.
     * @param photoDMR
     *         photo to save
     * @return fasle if error
     */
    public boolean savePhotoOnNetAppServeur( PhotoDMR photoDMR )
    {

        if ( ( photoDMR == null ) || ( photoDMR.getImage( ) == null ) || ( photoDMR.getImage( ).getImage( ) == null ) )
        {
            return false;
        }

        String mimeType = photoDMR.getImage( ).getMimeType( ) != null ? photoDMR.getImage( ).getMimeType( ) : MIME_TYPE_DEFAULT;

        try
        {
            String completePathToFile = proceedSavePhoto( photoDMR.getImage( ).getImage( ), mimeType, photoDMR.getSignalement( ).getId( ) );
            photoDMR.setCheminPhoto( completePathToFile );
            AppLogService.info( "Sauvegarde de la photo : " + completePathToFile + " pour le signalement " + photoDMR.getSignalement( ).getId( ) );

            if ( ( photoDMR.getImageThumbnail( ) != null )  && ( photoDMR.getImageThumbnail( ).getImage( ) != null ))
            {
                String completePathToFileThumbnail = proceedSavePhoto( photoDMR.getImageThumbnail( ).getImage( ), mimeType, photoDMR.getSignalement( ).getId( ) );
                photoDMR.setCheminPhotoMiniature( completePathToFileThumbnail );
                AppLogService.info( "Sauvegarde de la photo miniature: " + completePathToFileThumbnail + " pour le signalement " + photoDMR.getSignalement( ).getId( ) );
            }

            return true;
        } catch ( IOException | InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException | NoSuchAlgorithmException | ServerException
                | XmlParserException | IllegalArgumentException e )
        {
            AppLogService.error( LOG_ERROR_START + photoDMR.getSignalement( ).getId( ) + " erreur à la sauvegarde de la photo sur NetApp" );
            AppLogService.error( e.getMessage( ) );

            return false;
        }

    }

    /**
     * Delete photo on NetApp server.
     * @param photoDMR
     *         photo to save
     * @return fasle if error
     */
    public boolean deletePhotoOnNetAppServeur( PhotoDMR photoDMR )
    {

        if ( photoDMR == null )
        {
            return false;
        }

        boolean result = true;

        if ( StringUtils.isNotBlank( photoDMR.getCheminPhoto( ) ) )
        {
            try
            {
                getS3Client( ).removeObject( RemoveObjectArgs.builder( ).bucket( AppPropertiesService.getProperty( PROPERTIES_S3_BUCKET ) ).object( photoDMR.getCheminPhoto( ) ).build( ) );
            } catch ( InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException | NoSuchAlgorithmException | ServerException
                    | XmlParserException | IllegalArgumentException | IOException e )
            {
                result = false;
                AppLogService.error( LOG_ERROR_START + photoDMR.getSignalement( ).getId( ) + " erreur à la supression de la photo " + photoDMR.getCheminPhoto( ) + " sur NetApp" );
                AppLogService.error( e.getMessage( ) );
            }
        }

        if ( StringUtils.isNotBlank( photoDMR.getCheminPhotoMiniature( ) ) )
        {
            try
            {
                getS3Client( ).removeObject( RemoveObjectArgs.builder( ).bucket( AppPropertiesService.getProperty( PROPERTIES_S3_BUCKET ) ).object( photoDMR.getCheminPhotoMiniature( ) ).build( ) );
            } catch ( InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException | NoSuchAlgorithmException | ServerException
                    | XmlParserException | IllegalArgumentException | IOException e )
            {
                result = false;
                AppLogService.error( LOG_ERROR_START + photoDMR.getSignalement( ).getId( ) + " erreur à la supression de la photo " + photoDMR.getCheminPhotoMiniature( ) + " sur NetApp" );
                AppLogService.error( e.getMessage( ) );
            }
        }

        return result;
    }

    /**
     * Load photo on NetApp server
     * @param pathToPhoto
     *          path to find photo
     * @return ImageResource find
     */
    public ImageResource loadPhotoOnNetAppServeur( String pathToPhoto )
    {

        ImageResource image = new ImageResource( );
        image.setMimeType( StringUtils.substringAfter( pathToPhoto, "." ) );

        try ( InputStream is = getS3Client( ).getObject( GetObjectArgs.builder( ).bucket( AppPropertiesService.getProperty( PROPERTIES_S3_BUCKET ) ).object( pathToPhoto ).build( ) ) )
        {
            image.setImage( IOUtils.toByteArray( is ) );
        } catch ( InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException | NoSuchAlgorithmException | ServerException
                | XmlParserException | IllegalArgumentException | IOException e )
        {

            AppLogService.error( "Erreur chargment de la photo " + pathToPhoto );
            AppLogService.error( e.getMessage( ) );
        }

        return image;
    }

    /**
     * Proceed save photo.
     *
     * @param photoToSave
     * @param mimeType
     * @param idSignalement
     *
     * @return path to the photo on NetApp serveur
     *
     * @throws InvalidKeyException
     * @throws ErrorResponseException
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws InvalidResponseException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws XmlParserException
     * @throws IllegalArgumentException
     * @throws IOException
     */
    private  String proceedSavePhoto( byte[] photoToSave, String mimeType, long idSignalement ) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException,
    InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException, IOException
    {

        String fileName = generateUniqueName( mimeType, idSignalement );
        String completePathToFile = AppPropertiesService.getProperty( PROPERTIES_S3_PATH ) + fileName;
        if(completePathToFile.startsWith( "/" )) {
            //remove first char if is /
            completePathToFile = completePathToFile.substring( 1 );
        }
        getS3Client( ).putObject( PutObjectArgs.builder( ).bucket( AppPropertiesService.getProperty( PROPERTIES_S3_BUCKET ) ).object( completePathToFile )
                .stream( new ByteArrayInputStream( photoToSave ), photoToSave.length, -1 ).build( ) );

        return completePathToFile;
    }

    /**
     * Generate a unique name for the photo.
     * @param mimeType
     *          photo mime
     * @param idSignalement
     *          id signalement
     * @return the unique name
     */
    private  String generateUniqueName( String mimeType, Long idSignalement )
    {

        mimeType = mimeType.replace( SignalementConstants.MIME_TYPE_START, "" );

        StringBuilder randomAplaNum = new StringBuilder( RandomStringUtils.randomAlphanumeric( NB_RANDOM_CHAR ) );
        Timestamp timestamp = new Timestamp( System.currentTimeMillis( ) );
        randomAplaNum.append( timestamp.getTime( ) );
        String uniqueName = DigestUtils.md5DigestAsHex( randomAplaNum.toString( ).getBytes( ) );

        return uniqueName.concat( "_" ).concat( idSignalement.toString( ) ).concat( "." ).concat( mimeType );

    }

}
