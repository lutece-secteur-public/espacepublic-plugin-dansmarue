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

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.anonymizationphoto.business.IAnonymizationPhotoDAO;
import fr.paris.lutece.plugins.anonymizationphoto.business.PhotoToAnonymized;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * The Class AnonymizationPhotoDAO.
 */
public class AnonymizationPhotoDAO implements IAnonymizationPhotoDAO
{

    /** The Constant PROPERTY_MAX_PHOTO_LOAD. */
    // PROPERTIES
    private static final int PROPERTY_MAX_PHOTO_LOAD = AppPropertiesService.getPropertyInt( "signalement.anonymization.photo.limit", 10 );

    /** The Constant SQL_QUERY_SELECT_LAST_ID_PHOTO_ANONYMIZED. */
    // Request
    private static final String SQL_QUERY_SELECT_LAST_ID_PHOTO_ANONYMIZED = "select max(id_photo) from signalement_photo where is_anonymized = 1;";

    /** The Constant SQL_QUERY_SELECT_PHOTO_TO_ANONYMIZED. */
    private static final String SQL_QUERY_SELECT_PHOTO_TO_ANONYMIZED = "select id_photo , image_content, image_mime_type from signalement_photo where is_anonymized = 0 and image_content is not null and id_photo > ? order by id_photo asc limit ?;";

    /** The Constant SQL_QUERY_UPDATE_PHOTO. */
    private static final String SQL_QUERY_UPDATE_PHOTO = "update signalement_photo set image_content = ?, image_thumbnail = ?, is_anonymized = 1 where id_photo = ?;";

    /** The Constant SQL_QUERY_SELECT_ALL_PHOTO_SIGNALEMENT. */
    private static final String SQL_QUERY_SELECT_ALL_PHOTO_SIGNALEMENT = "select id_photo, fk_id_signalement, vue_photo, sp.chemin_photo, sp.chemin_photo_miniature, is_anonymized from signalement_photo sp, workflow_resource_workflow wrw "
            + "where sp.fk_id_signalement = wrw.id_resource and sp.fk_id_signalement in "
            + "( select fk_id_signalement from signalement_photo sp2 where sp2.id_photo = ?) and wrw.id_state  = ?;";

    /**
     * Gets the last id photo anonymized.
     *
     * @return the last id photo anonymized
     */
    public int getLastIdPhotoAnonymized( )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LAST_ID_PHOTO_ANONYMIZED ) ; )
        {
            daoUtil.executeQuery( );
            daoUtil.next( );
            return daoUtil.getInt( 1 );
        }
    }

    /**
     * Load photos.
     *
     * @return the list
     */
    @Override
    public List<PhotoToAnonymized> loadPhotos( )
    {
        int idLastPhotoAnonymized = getLastIdPhotoAnonymized( );

        List<PhotoToAnonymized> listphotoToAnonymized = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PHOTO_TO_ANONYMIZED ) ; )
        {
            daoUtil.setInt( 1, idLastPhotoAnonymized );
            daoUtil.setInt( 2, PROPERTY_MAX_PHOTO_LOAD );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                PhotoToAnonymized photoToAnonymized = new PhotoToAnonymized( );
                photoToAnonymized.setIdResourceAnonymized( daoUtil.getInt( 1 ) );
                photoToAnonymized.setPhotoContent( daoUtil.getBytes( 2 ) );
                photoToAnonymized.setMimeType( daoUtil.getString( 3 ) );

                listphotoToAnonymized.add( photoToAnonymized );
            }
        }
        return listphotoToAnonymized;
    }

    /**
     * Insert anonymized photo.
     *
     * @param photoAnonymized
     *            the photo anonymized
     * @param thumbnailPhotoAnonymized
     *            the thumbnail photo anonymized
     * @param idPhoto
     *            the id photo
     */
    public void insertAnonymizedPhoto( byte [ ] photoAnonymized, byte [ ] thumbnailPhotoAnonymized, int idPhoto )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_PHOTO ) ; )
        {

            daoUtil.setBytes( 1, photoAnonymized );
            daoUtil.setBytes( 2, thumbnailPhotoAnonymized );
            daoUtil.setInt( 3, idPhoto );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * Find photos signalement.
     *
     * @param idPhoto
     *            the id photo
     * @return the list
     */
    public List<PhotoDMR> findPhotosSignalement( int idPhoto )
    {
        List<PhotoDMR> listPhotos = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_PHOTO_SIGNALEMENT ) ; )
        {
            daoUtil.setInt( 1, idPhoto );
            daoUtil.setInt( 2, SignalementConstants.ID_STATE_A_TRANSFERE_PRESTATAIRE );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                PhotoDMR photo = new PhotoDMR( );
                photo.setId( daoUtil.getLong( 1 ) );
                Signalement signalement = new Signalement( );
                signalement.setId( daoUtil.getLong( 2 ) );
                photo.setSignalement( signalement );
                photo.setVue( daoUtil.getInt( 3 ) );
                photo.setCheminPhoto( daoUtil.getString( 4 ) );
                photo.setCheminPhotoMiniature( daoUtil.getString( 5 ) );
                photo.setAnonymized( daoUtil.getBoolean( 6 ) );

                listPhotos.add( photo );
            }
        }
        return listPhotos;
    }

}
