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
package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.IPhotoDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.utils.IDateUtils;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * The Class PhotoDAO.
 */
public class PhotoDAO implements IPhotoDAO
{

    /** The date utils */
    @Inject
    @Named( "signalement.dateUtils" )
    private transient IDateUtils _dateUtils;

    /** The Constant SQL_QUERY_NEW_PK. */
    private static final String SQL_QUERY_NEW_PK = "SELECT nextval('seq_signalement_photo_id_photo')";

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_photo(id_photo, fk_id_signalement, date_photo, vue_photo, chemin_photo, chemin_photo_miniature) VALUES (?, ?, ?, ?, ?, ?)";

    /** The Constant SQL_QUERY_DELETE. */
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_photo WHERE id_photo=?";

    private static final String SQL_QUERY_DELETE_PHOTO_FROM_DATABASE = "update signalement_photo set image_thumbnail = null, image_content= null, image_mime_type = null,  chemin_photo=?, chemin_photo_miniature=? where id_photo=?";

    /** The Constant SQL_QUERY_SELECT. */
    private static final String SQL_QUERY_SELECT_PATH_PHOTO = "SELECT chemin_photo, chemin_photo_miniature FROM signalement_photo WHERE id_photo =?";

    /** The Constant SQL_QUERY_UPDATE_PHOTO. */
    private static final String SQL_QUERY_UPDATE_PHOTO                        = "UPDATE signalement_photo SET date_photo=?, chemin_photo=?, chemin_photo_miniature=? where id_photo=?";
    /** The Constant SQL_QUERY_FIND_IMAGE_BY_PRIMARY_KEY. */
    private static final String SQL_QUERY_FIND_IMAGE_BY_PRIMARY_KEY           = "SELECT image_content, image_mime_type FROM signalement_photo WHERE id_photo=?";
    /** The Constant SQL_QUERY_FIND_IMAGE_THUMBNAIL_BY_PRIMARY_KEY. */
    private static final String SQL_QUERY_FIND_IMAGE_THUMBNAIL_BY_PRIMARY_KEY = "SELECT image_thumbnail, image_mime_type FROM signalement_photo WHERE id_photo=?";
    /** The Constant SQL_QUERY_SELECT_LIST_PHOTOS. */
    private static final String SQL_QUERY_SELECT_LIST_PHOTOS                  = "SELECT id_photo, fk_id_signalement, vue_photo, date_photo, is_anonymized, chemin_photo, chemin_photo_miniature FROM signalement_photo WHERE fk_id_signalement=?";
    /** The Constant SQL_QUERY_SELECT_LIST_PHOTOS_WITH_FULL. */
    private static final String SQL_QUERY_SELECT_LIST_PHOTOS_WITH_FULL        = "SELECT id_photo, image_content, image_thumbnail, image_mime_type, fk_id_signalement, vue_photo, date_photo, is_anonymized, chemin_photo, chemin_photo_miniature FROM signalement_photo WHERE fk_id_signalement=?";

    /** The Constant SQL_QUERY_SELECT_ID_FOR_SUPPRESSION_PHOTOS_DAEMON. */
    private static final String SQL_QUERY_SELECT_ID_FOR_SUPPRESSION_PHOTOS_DAEMON = "SELECT photo.id_photo, chemin_photo, chemin_photo_miniature FROM signalement_signalement signalement INNER JOIN signalement_type_signalement typeSignalement ON signalement.fk_id_type_signalement = typeSignalement.id_type_signalement INNER JOIN signalement_photo photo on photo.fk_id_signalement = signalement.id_signalement inner join workflow_resource_workflow workflow on workflow.id_resource = signalement.id_signalement WHERE typeSignalement.id_type_signalement IN ({0}) AND now() - ''{1} hours''::interval > date_creation and workflow.id_state IN ({2}) AND photo.date_photo is not null ORDER BY photo.id_photo LIMIT {3}  ;";

    /** The Constant EMPTY_STRING. */
    private static final String EMPTY_STRING = "";

    private static final String SQL_QUERY_SELECT_LIST_PHOTOS_TO_MIGRATE = "SELECT id_photo, image_content, image_thumbnail, image_mime_type, fk_id_signalement, vue_photo, date_photo, is_anonymized, chemin_photo, chemin_photo_miniature FROM signalement_photo where chemin_photo is null and chemin_photo_miniature is null order by id_photo asc limit ?";

    /**
     * Generates a new primary key.
     *
     * @return The new primary key
     */
    private Long newPrimaryKey( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery( );
        Long nKey = null;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getLong( 1 );
        }
        daoUtil.close( );
        return nKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long insert( PhotoDMR photo )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) )
        {
            if ( ( photo.getId( ) == null ) || ( photo.getId( ) == 0 ) )
            {
                photo.setId( newPrimaryKey( ) );
            }

            int nIndex = 1;
            daoUtil.setLong( nIndex++, photo.getId( ) );

            daoUtil.setLong( nIndex++, photo.getSignalement( ).getId( ) );

            if ( ( photo.getDate( ) != null ) && !( photo.getDate( ).equals( EMPTY_STRING ) ) )
            {
                daoUtil.setDate( nIndex++, DateUtil.formatDateSql( photo.getDate( ), Locale.FRENCH ) );
            }
            else
            {
                daoUtil.setDate( nIndex++, new java.sql.Date( Calendar.getInstance( ).getTime( ).getTime( ) ) );
            }

            if ( photo.getVue( ) != null )
            {
                daoUtil.setInt( nIndex++, photo.getVue( ) );
            }

            daoUtil.setString( nIndex++, photo.getCheminPhoto( ) );
            daoUtil.setString( nIndex, photo.getCheminPhotoMiniature( ) );

            daoUtil.executeUpdate( );

            return photo.getId( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( long lId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setLong( 1, lId );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhotoDMR load( long lId )
    {
        PhotoDMR photo = new PhotoDMR( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PATH_PHOTO );
        daoUtil.setLong( 1, lId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            photo.setCheminPhoto( daoUtil.getString( nIndex++ ) );
            photo.setCheminPhotoMiniature( daoUtil.getString( nIndex ) );
        }

        daoUtil.close( );

        return photo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePhoto( PhotoDMR photo )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_PHOTO );
        int nIndex = 1;

        if ( photo.getDate( ) == null )
        {
            daoUtil.setDate( nIndex++, null );
        }
        else
        {
            daoUtil.setDate( nIndex++, DateUtil.formatDateSql( photo.getDate( ), Locale.FRENCH ) );
        }
        daoUtil.setString( nIndex++, photo.getCheminPhoto( ) );
        daoUtil.setString( nIndex++, photo.getCheminPhotoMiniature( ) );

        // WHERE
        daoUtil.setLong( nIndex, photo.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageResource loadPhoto( int nIdPhoto )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_IMAGE_BY_PRIMARY_KEY );
        daoUtil.setInt( 1, nIdPhoto );
        daoUtil.executeQuery( );

        ImageResource image = new ImageResource( );

        if ( daoUtil.next( ) )
        {
            image.setImage( daoUtil.getBytes( 1 ) );
            image.setMimeType( daoUtil.getString( 2 ) );
        }

        daoUtil.close( );

        return image;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageResource loadPhotoThumbnail( int nIdPhoto )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_IMAGE_THUMBNAIL_BY_PRIMARY_KEY );
        daoUtil.setInt( 1, nIdPhoto );
        daoUtil.executeQuery( );

        ImageResource image = new ImageResource( );

        if ( daoUtil.next( ) )
        {
            image.setImage( daoUtil.getBytes( 1 ) );
            image.setMimeType( daoUtil.getString( 2 ) );
        }

        daoUtil.close( );

        return image;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PhotoDMR> findBySignalementId( long lIdSignalement )
    {
        List<PhotoDMR> result = new ArrayList<>( );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LIST_PHOTOS );
        daoUtil.setLong( 1, lIdSignalement );

        daoUtil.executeQuery( );

        // For each result returned
        while ( daoUtil.next( ) )
        {
            PhotoDMR photo = new PhotoDMR( );
            int nIndex = 1;
            photo.setId( daoUtil.getLong( nIndex++ ) );
            Signalement signalement = new Signalement( );
            signalement.setId( daoUtil.getLong( nIndex++ ) );
            photo.setVue( daoUtil.getInt( nIndex++ ) );
            photo.setDate( _dateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );
            photo.setAnonymized( daoUtil.getBoolean( nIndex++ ) );
            photo.setCheminPhoto( daoUtil.getString( nIndex++ ) );
            photo.setCheminPhotoMiniature( daoUtil.getString( nIndex ) );

            photo.setSignalement( signalement );
            result.add( photo );
        }

        daoUtil.close( );

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PhotoDMR> findWithFullPhotoBySignalementId( long lIdSignalement )
    {
        List<PhotoDMR> result = new ArrayList<>( );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LIST_PHOTOS_WITH_FULL );
        daoUtil.setLong( 1, lIdSignalement );

        daoUtil.executeQuery( );

        // For each result returned
        while ( daoUtil.next( ) )
        {
            PhotoDMR photo = new PhotoDMR( );
            int nIndex = 1;
            photo.setId( daoUtil.getLong( nIndex++ ) );
            Object oImageContent = daoUtil.getBytes( nIndex++ );
            photo.setImage( new ImageResource( ) );
            photo.setImageContent( ( byte[] ) oImageContent );
            Object oImageContentThumbnail = daoUtil.getBytes( nIndex++ );
            photo.setImageThumbnailWithBytes( ( byte[] ) oImageContentThumbnail );
            photo.setMimeType( daoUtil.getString( nIndex++ ) );
            Signalement signalement = new Signalement( );
            signalement.setId( daoUtil.getLong( nIndex++ ) );
            photo.setVue( daoUtil.getInt( nIndex++ ) );
            photo.setDate( _dateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );
            photo.setAnonymized( daoUtil.getBoolean( nIndex++ ) );
            photo.setCheminPhoto( daoUtil.getString( nIndex++ ) );
            photo.setCheminPhotoMiniature( daoUtil.getString( nIndex ) );

            photo.setSignalement( signalement );
            result.add( photo );
        }

        daoUtil.close( );

        return result;
    }

    /**
     * Find photos for suppr photos daemon.
     *
     * @param anomaliesCible
     *            the anomalies cible
     * @param tempsConservation
     *            the temps conservation
     * @param etatsCible
     *            the etats cible
     * @param limitRequest
     *            the limit request
     * @return the list
     */
    /*
     * (non-Javadoc)
     *
     * @see fr.paris.lutece.plugins.dansmarue.business.dao.IPhotoDAO#findPhotosForSupprPhotosDaemon(java.util.List, java.lang.Integer, java.util.List,
     * java.lang.Integer)
     */
    @Override
    public List<PhotoDMR> findPhotosForSupprPhotosDaemon( List<String> anomaliesCible, Integer tempsConservation, List<String> etatsCible, Integer limitRequest )
    {
        String intAnomaliesCiblesValues = StringUtils.join( anomaliesCible.stream( ).toArray( ), "," );
        String intEtatsCiblesValues = StringUtils.join( etatsCible.stream( ).toArray( ), "," );

        DAOUtil daoUtil = new DAOUtil( MessageFormat.format( SQL_QUERY_SELECT_ID_FOR_SUPPRESSION_PHOTOS_DAEMON, intAnomaliesCiblesValues, tempsConservation, intEtatsCiblesValues, limitRequest ) );

        daoUtil.executeQuery( );

        List<PhotoDMR> photos = new ArrayList<>( );

        while ( daoUtil.next( ) )
        {
            PhotoDMR photo = new PhotoDMR( );
            int nIndex = 1;
            photo.setId( daoUtil.getLong( nIndex++ ) );
            photo.setCheminPhoto( daoUtil.getString( nIndex++ ) );
            photo.setCheminPhotoMiniature( daoUtil.getString( nIndex ) );

            photos.add( photo );
        }

        daoUtil.close( );

        return photos;
    }

    @Override
    public List<PhotoDMR> findPhotosToMigrate( int nNbPhotoToMigrate )
    {
        List<PhotoDMR> listPhotosToMigrate = new ArrayList<>( );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LIST_PHOTOS_TO_MIGRATE );
        daoUtil.setLong( 1, nNbPhotoToMigrate );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            PhotoDMR photo = new PhotoDMR( );
            int nIndex = 1;
            photo.setId( daoUtil.getLong( nIndex++ ) );
            Object oImageContent = daoUtil.getBytes( nIndex++ );
            photo.setImage( new ImageResource( ) );
            photo.setImageContent( ( byte[] ) oImageContent );
            Object oImageContentThumbnail = daoUtil.getBytes( nIndex++ );
            photo.setImageThumbnailWithBytes( ( byte[] ) oImageContentThumbnail );
            photo.setMimeType( daoUtil.getString( nIndex++ ) );
            Signalement signalement = new Signalement( );
            signalement.setId( daoUtil.getLong( nIndex++ ) );
            photo.setVue( daoUtil.getInt( nIndex++ ) );
            photo.setDate( _dateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );
            photo.setAnonymized( daoUtil.getBoolean( nIndex++ ) );
            photo.setCheminPhoto( daoUtil.getString( nIndex++ ) );
            photo.setCheminPhotoMiniature( daoUtil.getString( nIndex ) );

            photo.setSignalement( signalement );

            listPhotosToMigrate.add( photo );
        }

        daoUtil.close( );

        return listPhotosToMigrate;
    }

    @Override
    public void removePhotoFromDatabase( PhotoDMR photoDMR )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_PHOTO_FROM_DATABASE ) )
        {
            daoUtil.setString( 1, photoDMR.getCheminPhoto( ) );
            daoUtil.setString( 2, photoDMR.getCheminPhotoMiniature( ) );
            daoUtil.setLong( 3, photoDMR.getId( ) );
            daoUtil.executeUpdate( );
        }
    }

}
