/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.plugins.dansmarue.business.entities;

import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.image.ImageResourceProvider;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.url.UrlItem;

public class Photo
{
    public static final Integer OVERVIEW        = 1;
    public static final Integer DETAILED_VIEW   = 0;
    private Long                _id;
    private ImageResource       _image          = null;
    private ImageResource       _imageThumbnail = new ImageResource( );
    private String              _strDate;
    private Integer             _nVue;
    private String              _strImageUrl;
    private String              _strImageThumbnailUrl;

    public Photo( )
    {

    }

    public Photo( Long id, ImageResource image, ImageResource imageThumbnail, String date, Integer vue )
    {
        super( );
        this._id = id;
        this._image = image;
        this._imageThumbnail = imageThumbnail;
        this._strDate = date;
        this._nVue = vue;
    }

    public Long getId( )
    {
        return _id;
    }

    public void setId( Long id )
    {
        _id = id;
    }

    public ImageResource getImage( )
    {
        return _image;
    }

    public void setImage( ImageResource image )
    {
        _image = image;
    }

    public void setImageUrl( String imageUrl )
    {
        _strImageUrl = imageUrl;
    }

    public String getImageUrl( )
    {
        if ( _strImageUrl == null )
        {
            String strResourceType = ( ( ImageResourceProvider ) SpringContextService.getBean( "signalement.imageService" ) ).getResourceTypeId( );
            UrlItem url = new UrlItem( Parameters.IMAGE_SERVLET );
            url.addParameter( Parameters.RESOURCE_TYPE, strResourceType );
            url.addParameter( Parameters.RESOURCE_ID, Long.toString( _id ) );
            return url.getUrlWithEntity( );
        } else
        {
            return _strImageUrl;
        }
    }

    public void setImageThumbnailUrl( String imageThumbnailUrl )
    {
        _strImageThumbnailUrl = imageThumbnailUrl;
    }

    public String getImageThumbnailUrl( )
    {
        if ( _strImageThumbnailUrl == null )
        {
            String strResourceType = ( ( ImageResourceProvider ) SpringContextService.getBean( "signalement.imageThumbnailService" ) ).getResourceTypeId( );
            UrlItem url = new UrlItem( Parameters.IMAGE_SERVLET );
            url.addParameter( Parameters.RESOURCE_TYPE, strResourceType );
            url.addParameter( Parameters.RESOURCE_ID, Long.toString( _id ) );
            return url.getUrlWithEntity( );
        } else
        {
            return _strImageThumbnailUrl;
        }

    }

    public void setMimeType( String strMimeType )
    {
        _image.setMimeType( strMimeType );
    }

    public void setImageContent( byte[] imageContent )
    {
        _image.setImage( imageContent );
    }

    /**
     * Sets the thumbnailed image.
     *
     * @param imageContent
     *            the new image thumbnailed image
     */
    public void setImageThumbnail( ImageResource imageResource )
    {
        _imageThumbnail.setImage( imageResource.getImage( ) );
    }

    /**
     * @return the thumbnailed image
     */
    public ImageResource getImageThumbnail( )
    {
        return _imageThumbnail;
    }

    public String getDate( )
    {
        return _strDate;
    }

    public void setDate( String date )
    {
        _strDate = date;
    }

    public Integer getVue( )
    {
        return _nVue;
    }

    public void setVue( Integer vue )
    {
        _nVue = vue;
    }
}
