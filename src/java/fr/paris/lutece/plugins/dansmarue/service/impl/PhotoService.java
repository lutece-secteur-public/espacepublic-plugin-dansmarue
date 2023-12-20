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

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.IPhotoDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.service.IPhotoService;
import fr.paris.lutece.plugins.dansmarue.utils.StockagePhotoUtils;
import fr.paris.lutece.portal.service.image.ImageResource;

/**
 * The Class PhotoService.
 */
public class PhotoService implements IPhotoService
{


    /** The photo DAO. */
    @Inject
    private IPhotoDAO _photoDAO;

    @Inject
    private StockagePhotoUtils _stockagePhotoUtils;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long insert( PhotoDMR photo )
    {
        boolean result = _stockagePhotoUtils.savePhotoOnNetAppServeur( photo );
        if ( result )
        {
            return _photoDAO.insert( photo );
        } else
        {
            return ( long ) -1;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( long lId )
    {
        _photoDAO.remove( lId );
        _stockagePhotoUtils.deletePhotoOnNetAppServeur( load( lId ) );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhotoDMR load( long lId )
    {
        return _photoDAO.load( lId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageResource getImageResource( int nKey )
    {
        return _photoDAO.loadPhoto( nKey );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageResource getImageResource( int nKey, String strToken )
    {
        PhotoDMR photoDMR = load( nKey );
        if(StringUtils.isBlank( photoDMR.getCheminPhoto() )) {
            return new ImageResource();
        }
        String storedToken = photoDMR.getCheminPhoto().split( "_" )[0];
        if( !storedToken.equals( strToken ) ) {
            return new ImageResource();
        }
        return _stockagePhotoUtils.loadPhotoOnNetAppServeur( photoDMR.getCheminPhoto( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageResource getImageThumbnailResource( int nIdPhoto )
    {
        return _photoDAO.loadPhotoThumbnail( nIdPhoto );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageResource getImageThumbnailResource( int nIdPhoto, String strToken )
    {
        PhotoDMR photoDMR = load( nIdPhoto );
        if(StringUtils.isBlank( photoDMR.getCheminPhotoMiniature() )) {
            return new ImageResource();
        }
        String storedToken = photoDMR.getCheminPhotoMiniature().split( "_" )[0];
        if( !storedToken.equals( strToken ) )
        {
            return new ImageResource();
        }
        return _stockagePhotoUtils.loadPhotoOnNetAppServeur( photoDMR.getCheminPhotoMiniature( ) );
    }

    @Override
    public List<PhotoDMR> findPhotosToMigrate( int maxPhoto)
    {
        return _photoDAO.findPhotosToMigrate( maxPhoto );
    }

    @Override
    public void removePhotoFromDatabase( PhotoDMR photoDMR )
    {
        _photoDAO.removePhotoFromDatabase( photoDMR );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PhotoDMR> findBySignalementId( long lIdSignalement )
    {
        return _photoDAO.findBySignalementId( lIdSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PhotoDMR> findWithFullPhotoBySignalementId( long lIdSignalement )
    {
        List<PhotoDMR> photos = _photoDAO.findWithFullPhotoBySignalementId( lIdSignalement );
        //Load Photo on S3 server
        for(PhotoDMR photo : photos) {
            if (( photo.getImage( ) == null ) || ( photo.getImage( ).getImage( ) == null ) ) {
                ImageResource imageRessource = _stockagePhotoUtils.loadPhotoOnNetAppServeur( photo.getCheminPhoto( ) );
                photo.setImage( imageRessource );
            }

            if (( photo.getImageThumbnail( ) == null ) || ( photo.getImageThumbnail( ).getImage( ) == null ) ) {
                ImageResource imageRessource = _stockagePhotoUtils.loadPhotoOnNetAppServeur( photo.getCheminPhotoMiniature( ) );
                photo.setImageThumbnail( imageRessource );
            }
        }
        return photos;
    }

    @Override
    public void update( PhotoDMR photoDMR )
    {
        _photoDAO.updatePhoto( photoDMR );

    }

}
