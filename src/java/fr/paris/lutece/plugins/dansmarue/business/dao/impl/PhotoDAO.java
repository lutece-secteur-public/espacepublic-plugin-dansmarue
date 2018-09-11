/*
 * Copyright (c) 2002-2018, Mairie de Paris
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.dansmarue.business.dao.IPhotoDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.sql.DAOUtil;

public class PhotoDAO implements IPhotoDAO
{
    private static final String SQL_QUERY_NEW_PK                              = "SELECT nextval('seq_signalement_photo_id_photo')";
    private static final String SQL_QUERY_INSERT                              = "INSERT INTO signalement_photo(id_photo, image_content, image_mime_type, image_thumbnail, fk_id_signalement, date_photo, vue_photo) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_QUERY_DELETE                              = "DELETE FROM signalement_photo WHERE id_photo=?";
    private static final String SQL_QUERY_SELECT                              = "SELECT id_photo, image_content, image_mime_type, vue_photo, date_photo, fk_id_signalement FROM signalement_photo WHERE id_photo =?";
    private static final String SQL_QUERY_SELECT_BY_SIGNALEMENT               = "SELECT id_photo, image_content, image_mime_type, vue_photo, date_photo, fk_id_signalement FROM signalement_photo WHERE fk_id_signalement =?";
    private static final String SQL_QUERY_UPDATE                              = "UPDATE signalement_photo SET id_photo=?, image_content=?, image_mime_type=?, fk_id_signalement=? WHERE id_photo=?";
    private static final String SQL_QUERY_FIND_IMAGE_BY_PRIMARY_KEY           = "SELECT image_content, image_mime_type, vue_photo,date_photo FROM signalement_photo WHERE id_photo=? ";
    private static final String SQL_QUERY_FIND_IMAGE_THUMBNAIL_BY_PRIMARY_KEY = "SELECT image_thumbnail, image_mime_type FROM signalement_photo WHERE id_photo=?";
    private static final String SQL_QUERY_SELECT_LIST_PHOTOS                  = "SELECT id_photo, fk_id_signalement, vue_photo, date_photo FROM signalement_photo WHERE fk_id_signalement=?";
    private static final String SQL_QUERY_SELECT_LIST_PHOTOS_WITH_FULL        = "SELECT id_photo, image_content, image_thumbnail, image_mime_type, fk_id_signalement, vue_photo, date_photo FROM signalement_photo WHERE fk_id_signalement=?";
    private static final String EMPTY_STRING                                  = "";

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
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        if ( ( photo.getId( ) == null ) || ( photo.getId( ) == 0 ) )
        {
            photo.setId( newPrimaryKey( ) );
        }

        int nIndex = 1;
        daoUtil.setLong( nIndex++, photo.getId( ) );

        if ( photo.getImage( ) != null )
        {
            daoUtil.setBytes( nIndex++, photo.getImage( ).getImage( ) );
            daoUtil.setString( nIndex++, photo.getImage( ).getMimeType( ) );
        } else
        {
            byte[] baImageNull = null;
            daoUtil.setBytes( nIndex++, baImageNull );
            daoUtil.setString( nIndex++, "" );
        }
        if ( photo.getImageThumbnail( ) != null )
        {
            daoUtil.setBytes( nIndex++, photo.getImageThumbnail( ).getImage( ) );
        } else
        {
            byte[] baImageNull = null;
            daoUtil.setBytes( nIndex++, baImageNull );
        }

        daoUtil.setLong( nIndex++, photo.getSignalement( ).getId( ) );

        if ( ( photo.getDate( ) != null ) && !( photo.getDate( ).equals( EMPTY_STRING ) ) )
        {
            daoUtil.setDate( nIndex++, DateUtil.formatDateSql( photo.getDate( ), Locale.FRENCH ) );
        } else
        {
            daoUtil.setDate( nIndex++, new java.sql.Date( Calendar.getInstance( ).getTime( ).getTime( ) ) );
        }

        if ( photo.getVue( ) != null )
        {
            daoUtil.setInt( nIndex++, photo.getVue( ) );
        }

        daoUtil.executeUpdate( );
        daoUtil.close( );

        return photo.getId( );
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
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setLong( 1, lId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            photo.setId( daoUtil.getLong( nIndex++ ) );
            Object oImageContent = daoUtil.getBytes( nIndex++ );
            photo.setImage( new ImageResource( ) );
            photo.setImageContent( ( byte[] ) oImageContent );
            photo.setMimeType( daoUtil.getString( nIndex++ ) );
            photo.setVue( daoUtil.getInt( nIndex++ ) );
            photo.setDate( DateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );

            photo.getSignalement( ).setId( daoUtil.getLong( nIndex++ ) );
        }

        daoUtil.close( );

        return photo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhotoDMR loadByIdSignalement( long signalementId )
    {
        PhotoDMR photo = new PhotoDMR( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_SIGNALEMENT );
        daoUtil.setLong( 1, signalementId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            photo.setId( daoUtil.getLong( nIndex++ ) );
            Object oImageContent = daoUtil.getBytes( nIndex++ );
            photo.setImage( new ImageResource( ) );
            photo.setImageContent( ( byte[] ) oImageContent );
            Object oImageThumbnailContent = daoUtil.getBytes( nIndex++ );
            photo.setImageThumbnailWithBytes( ( byte[] ) oImageThumbnailContent );
            photo.setMimeType( daoUtil.getString( nIndex++ ) );
            photo.setVue( daoUtil.getInt( nIndex++ ) );
            photo.setDate( DateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );

            Signalement signalement = new Signalement( );
            signalement.setId( daoUtil.getLong( nIndex++ ) );
            photo.setSignalement( signalement );
        }

        daoUtil.close( );

        return photo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( PhotoDMR photo )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nIndex = 1;

        daoUtil.setLong( nIndex++, photo.getId( ) );

        daoUtil.setBytes( nIndex++, photo.getImage( ).getImage( ) );
        daoUtil.setString( nIndex++, photo.getImage( ).getMimeType( ) );
        daoUtil.setLong( nIndex++, photo.getSignalement( ).getId( ) );

        // WHERE
        daoUtil.setLong( nIndex++, photo.getId( ) );

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
        List<PhotoDMR> result = new ArrayList<PhotoDMR>( );

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
            photo.setDate( DateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );

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
        List<PhotoDMR> result = new ArrayList<PhotoDMR>( );

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
            photo.setDate( DateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );

            photo.setSignalement( signalement );
            result.add( photo );
        }

        daoUtil.close( );

        return result;
    }
}