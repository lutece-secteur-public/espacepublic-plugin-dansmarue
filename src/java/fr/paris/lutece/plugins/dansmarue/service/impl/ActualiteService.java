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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fr.paris.lutece.plugins.dansmarue.business.dao.IActualiteDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Actualite;
import fr.paris.lutece.plugins.dansmarue.service.IActualiteService;
import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

/**
 * The Class ActualiteService.
 */
public class ActualiteService implements IActualiteService
{

    /** The actualite DAO. */
    @Inject
    private IActualiteDAO _actualiteDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Actualite> getAllActualite( )
    {
        Plugin pluginSignalement = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
        return _actualiteDAO.getAllActualite( pluginSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doDeleteActualite( long lId )
    {
        _actualiteDAO.decreaseOrdreOfAllNext( lId );
        _actualiteDAO.remove( lId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSaveActualite( Actualite actualite )
    {
        if ( ( actualite.getId( ) != null ) && ( actualite.getId( ) > 0 ) )
        {
            if ( ( actualite.getImage( ) != null ) && ( actualite.getImage( ).getImage( ) != null ) )
            {
                _actualiteDAO.store( actualite );
            }
            else
            {
                _actualiteDAO.storeWithoutImage( actualite );
            }

        }
        else
        {
            int idActualite = _actualiteDAO.insert( actualite );
            _actualiteDAO.increaseOrdreOfAllNext( idActualite );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Actualite getById( Integer nIdActualite )
    {
        return _actualiteDAO.load( nIdActualite );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Actualite> getAllActualiteActif( )
    {
        return _actualiteDAO.getAllActualiteActif( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseOrdreOfActualite( Actualite actualite )
    {
        int actualiteCount = _actualiteDAO.getActualiteCount( );
        if ( ( actualite != null ) && ( actualite.getOrdre( ) < actualiteCount ) )
        {
            _actualiteDAO.decreaseOrdreOfNextActualite( actualite );
            actualite.setOrdre( actualite.getOrdre( ) + 1 );
            _actualiteDAO.updateActualiteOrdre( actualite );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreaseOrdreOfActualite( Actualite actualite )
    {
        if ( ( actualite != null ) && ( actualite.getOrdre( ) > 1 ) )
        {
            _actualiteDAO.increaseOrdreOfPreviousActualite( actualite );
            actualite.setOrdre( actualite.getOrdre( ) - 1 );
            _actualiteDAO.updateActualiteOrdre( actualite );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseOrdreOfAllNext( int nIdActualite )
    {
        _actualiteDAO.increaseOrdreOfAllNext( nIdActualite );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreaseOrdreOfAllNext( int nIdActualite )
    {
        _actualiteDAO.decreaseOrdreOfAllNext( nIdActualite );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getActualiteCount( )
    {
        return _actualiteDAO.getActualiteCount( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateActualiteOrdre( Actualite actualite )
    {
        _actualiteDAO.updateActualiteOrdre( actualite );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageResource getImageResource( int nIdActualite )
    {
        return _actualiteDAO.getImageResource( nIdActualite );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVersionActualite( )
    {
        return _actualiteDAO.getVersionActualite( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Actualite> getActualiteWithVersion( int mobileVersionActualite, int currentversion )
    {
        List<Actualite> lstActualite = new ArrayList<>( );

        if ( mobileVersionActualite != currentversion )
        {
            return _actualiteDAO.getAllActualiteActif( );
        }

        return lstActualite;
    }
}
