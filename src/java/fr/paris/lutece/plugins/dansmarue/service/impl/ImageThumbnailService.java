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
package fr.paris.lutece.plugins.dansmarue.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import fr.paris.lutece.plugins.dansmarue.service.IPhotoService;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.image.ImageResourceProvider;

/**
 * This class provide services for ImageServiceTypeObjet.
 */
public class ImageThumbnailService implements ImageResourceProvider
{

    /** The Constant IMAGE_RESOURCE_TYPE_ID. */
    private static final String IMAGE_RESOURCE_TYPE_ID = "photo_signalement_thumbnail";

    /** The photo service. */
    @Autowired
    private IPhotoService _photoService;

    /**
     * Get the resource for image.
     *
     * @param nIdPhoto
     *            The identifier of nIdPhoto object
     * @return The ImageResource
     */
    @Override
    public ImageResource getImageResource( int nIdPhoto )
    {
        return _photoService.getImageThumbnailResource( nIdPhoto );
    }

    /**
     * Get the resource for image only if the token match the stored token.
     *
     * @param nIdPhoto
     *            The identifier of nIdPhoto object
     * @param strToken
     *            The token to verify
     * @return The ImageResource
     */
    public ImageResource getImageResource( int nIdPhoto, String strToken )
    {
        return _photoService.getImageThumbnailResource( nIdPhoto, strToken );
    }

    /**
     * Get the type of resource.
     *
     * @return The type of resource
     */
    @Override
    public String getResourceTypeId( )
    {
        return IMAGE_RESOURCE_TYPE_ID;
    }
}
