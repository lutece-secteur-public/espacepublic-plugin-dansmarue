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

import javax.inject.Inject;

import fr.paris.lutece.plugins.dansmarue.business.dao.ISiraUserDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.SiraUser;
import fr.paris.lutece.plugins.dansmarue.service.ISiraUserService;

/**
 * The Class SiraUserService.
 */
public class SiraUserService implements ISiraUserService
{

    /** The sira user DAO. */
    @Inject
    private ISiraUserDAO _siraUserDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long insert( SiraUser siraUser )
    {
        return _siraUserDAO.insert( siraUser );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( long lId )
    {
        _siraUserDAO.remove( lId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SiraUser load( long lId )
    {
        return _siraUserDAO.load( lId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update( SiraUser siraUser )
    {
        _siraUserDAO.update( siraUser );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SiraUser findByGuidAndToken( String guid, String token )
    {
        return _siraUserDAO.findByGuidAndToken( guid, token );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SiraUser findByGuid( String guid )
    {
        return _siraUserDAO.findByGuid( guid );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createUser( SiraUser siraUser )
    {
        SiraUser siraUserFound = findByGuidAndToken( siraUser.getGuid( ), siraUser.getToken( ) );
        if ( null == siraUserFound )
        {
            insert( siraUser );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUserPrestataire( Integer unitId )
    {
        return _siraUserDAO.isUserPrestataire( unitId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getEntiteMaxFromEntite( Integer unitId )
    {
        return _siraUserDAO.getEntiteMaxFromEntite( unitId );
    }

}
