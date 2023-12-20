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
package fr.paris.lutece.plugins.dansmarue.service;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.portal.service.image.ImageResource;

/**
 * The Interface IPhotoService.
 */
public interface IPhotoService
{
    /**
     * Save a new photo.
     *
     * @param photo
     *            the photo
     * @return the long
     */
    Long insert( PhotoDMR photo );

    /**
     * Delete an photo.
     *
     * @param lId
     *            the photo id
     */
    void remove( long lId );

    /**
     * Load a photo.
     *
     * @param lId
     *            the photo id
     * @return the photo
     */
    PhotoDMR load( long lId );

    /**
     * Update a photo.
     *
     * @param photoDMR the photo dmr
     */
    void update( PhotoDMR photoDMR);

    /**
     * Gets the image resource.
     *
     * @param nKey
     *            the n key
     * @return the image resource
     */
    ImageResource getImageResource( int nKey );

    /**
     * Gets the image resource.
     *
     * @param nKey
     *            the n key
     * @param strToken
     *            the token
     * @return the image resource
     */
    ImageResource getImageResource( int nKey, String strToken );

    /**
     * Find photos for a report id.
     *
     * @param lIdSignalement
     *            the report id
     * @return list of photos
     */
    List<PhotoDMR> findBySignalementId( long lIdSignalement );

    /**
     * Find photos for a report id with photo content.
     *
     * @param lIdSignalement
     *            the report id
     * @return list of photos
     */
    List<PhotoDMR> findWithFullPhotoBySignalementId( long lIdSignalement );

    /**
     * Load a thumbnailed photo.
     *
     * @param nIdPhoto
     *            the photo id
     * @return the image resource
     */
    ImageResource getImageThumbnailResource( int nIdPhoto );

    /**
     * Load a thumbnailed photo.
     *
     * @param nIdPhoto
     *            the photo id
     * @param strToken
     *            the token
     * @return the image resource
     */
    ImageResource getImageThumbnailResource( int nIdPhoto, String strToken );

    /**
     * Find photos to migrate list.
     * @param maxPhoto
     *         max photo load
     * @return the list
     */
    List<PhotoDMR> findPhotosToMigrate( int maxPhoto);

    /**
     * Remove photo from database.
     * @param photoDMR the photo dmr
     */
    void removePhotoFromDatabase( PhotoDMR photoDMR );
}
