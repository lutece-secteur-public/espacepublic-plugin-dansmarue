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

import fr.paris.lutece.plugins.dansmarue.business.dao.IAideDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Aide;
import fr.paris.lutece.plugins.dansmarue.service.IAideService;
import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

public class AideService implements IAideService
{

    /** The aide DAO. */
    @Inject
    private IAideDAO _aideDAO;

    @Override
    public List<Aide> getAllAide( )
    {
        Plugin pluginSignalement = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
        return _aideDAO.selectAidesList( pluginSignalement );
    }

    @Override
    public void doDeleteAide( long nIdAide )
    {
        _aideDAO.decreaseOrdreOfAllNext( (int) nIdAide );
        _aideDAO.delete( nIdAide );

    }

    @Override
    public void doSaveAide( Aide aide )
    {
        if ( ( aide.getId( ) != null ) && ( aide.getId( ) > 0 ) )
        {
            if ( ( aide.getImage( ) != null ) && ( aide.getImage( ).getImage( ) != null ) )
            {
                _aideDAO.store( aide );
            }
            else
            {
                _aideDAO.storeWithoutImage( aide );
            }
        }
        else
        {
            int idAide = _aideDAO.insert( aide );
            _aideDAO.increaseOrdreOfAllNext( idAide );
        }

    }

    @Override
    public Aide getById( Integer nIdAide )
    {
        return _aideDAO.load( nIdAide );
    }

    @Override
    public List<Aide> getAllAideActif( )
    {
        return _aideDAO.getAllAideActif( );
    }

    @Override
    public void increaseOrdreOfAide( Aide aide )
    {
        int aideCount = _aideDAO.getAideCount( );
        if ( ( aide != null ) && ( aide.getOrdre( ) < aideCount ) )
        {
            _aideDAO.decreaseOrdreOfNextAide( aide );
            aide.setOrdre( aide.getOrdre( ) + 1 );
            _aideDAO.updateAideOrdre( aide );
        }

    }

    @Override
    public void decreaseOrdreOfAide( Aide aide )
    {
        if ( ( aide != null ) && ( aide.getOrdre( ) > 1 ) )
        {
            _aideDAO.increaseOrdreOfPreviousAide( aide );
            aide.setOrdre( aide.getOrdre( ) - 1 );
            _aideDAO.updateAideOrdre( aide );
        }

    }

    @Override
    public void increaseOrdreOfAllNext( int nIdAide )
    {
        _aideDAO.increaseOrdreOfAllNext( nIdAide );

    }

    @Override
    public void decreaseOrdreOfAllNext( int nIdAide )
    {
        _aideDAO.decreaseOrdreOfAllNext( nIdAide );

    }

    @Override
    public int getAideCount( )
    {
        return _aideDAO.getAideCount( );
    }

    @Override
    public void updateAideOrdre( Aide aide )
    {
        _aideDAO.updateAideOrdre( aide );

    }

    @Override
    public List<Aide> getAideWithVersion( int versionAide )
    {
        List<Aide> lstAide = new ArrayList<>( );

        if ( versionAide != _aideDAO.getVersionAide( ) )
        {
            return _aideDAO.getAllAideActif( );
        }

        return lstAide;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVersionAide( )
    {
        return _aideDAO.getVersionAide( );
    }

    @Override
    public ImageResource getImageResource( int nIdAide )
    {
        return _aideDAO.getImageResource( nIdAide );
    }

}
