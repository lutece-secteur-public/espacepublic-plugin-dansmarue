/*
 * Copyright (c) 2002-2021, City of Paris
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

import fr.paris.lutece.plugins.dansmarue.business.dao.IPhotoDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.service.IPhotoService;
import fr.paris.lutece.portal.service.image.ImageResource;

/**
 * The Class PhotoService.
 */
public class PhotoService implements IPhotoService
{

    /** The photo DAO. */
    @Inject
    private IPhotoDAO _photoDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long insert( PhotoDMR photo )
    {
        return _photoDAO.insert( photo );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( long lId )
    {
        _photoDAO.remove( lId );

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
    public void store( PhotoDMR photo )
    {
        _photoDAO.store( photo );

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
    public ImageResource getImageThumbnailResource( int nIdPhoto )
    {
        return _photoDAO.loadPhotoThumbnail( nIdPhoto );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhotoDMR loadByIdSignalement( long lId )
    {
        return _photoDAO.loadByIdSignalement( lId );
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
        return _photoDAO.findWithFullPhotoBySignalementId( lIdSignalement );
    }
}
