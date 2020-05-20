/*
 * Copyright (c) 2002-2020, City of Paris
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
import javax.inject.Named;

import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementSuiviDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementSuivi;
import fr.paris.lutece.plugins.dansmarue.business.entities.SiraUser;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementSuiviService;

/**
 * The Class SignalementSuiviService.
 */
public class SignalementSuiviService implements ISignalementSuiviService
{

    /** The signalement suivi DAO. */
    @Inject
    @Named( "signalementSuiviDAO" )
    private ISignalementSuiviDAO signalementSuiviDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long insert( SignalementSuivi signalementSuivi )
    {
        return signalementSuiviDAO.insert( signalementSuivi );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( long lId )
    {
        signalementSuiviDAO.remove( lId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SignalementSuivi load( long lId )
    {
        return signalementSuiviDAO.load( lId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update( SignalementSuivi signalementSuivi )
    {
        signalementSuiviDAO.update( signalementSuivi );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SiraUser> findUsersMobilesByIdSignalement( long idSignalement )
    {
        return signalementSuiviDAO.findUsersMobileByIdSignalement( idSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SiraUser> findFollowersMobilesByIdSignalement( long idSignalement )
    {
        return signalementSuiviDAO.findFollowersMobilesByIdSignalement( idSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SiraUser> findCreatorMobilesByIdSignalement( long idSignalement )
    {
        return signalementSuiviDAO.findCreatorMobilesByIdSignalement( idSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( SignalementSuivi signalementSuivi )
    {
        long idSuivi = signalementSuiviDAO.findByIdSignalementAndGuid( signalementSuivi.getIdSignalement( ), signalementSuivi.getUserGuid( ) );
        if ( idSuivi != -1 )
        {
            remove( idSuivi );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeByIdSignalement( long idSignalement )
    {
        signalementSuiviDAO.removeByIdSignalement( idSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> findUsersMailByIdSignalement( long idSignalement )
    {
        return signalementSuiviDAO.findUsersMailByIdSignalement( idSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getNbFollowersByIdSignalement( int idSignalement )
    {
        return signalementSuiviDAO.getNbFollowersByIdSignalement( idSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long findByIdSignalementAndGuid( long idSignalement, String guid )
    {
        return signalementSuiviDAO.findByIdSignalementAndGuid( idSignalement, guid );
    }

}
