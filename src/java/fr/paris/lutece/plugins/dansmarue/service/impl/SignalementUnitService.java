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

import org.apache.commons.collections.CollectionUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementUnitDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementUnit;
import fr.paris.lutece.plugins.dansmarue.business.entities.UnitWithDepth;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementUnitService;
import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

/**
 * The Class SignalementUnitService.
 */
public class SignalementUnitService implements ISignalementUnitService
{

    /** The signalement unit DAO. */
    @Inject
    private ISignalementUnitDAO signalementUnitDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer insert( SignalementUnit unitSignalement )
    {
        return signalementUnitDAO.insert( unitSignalement, PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( long lId )
    {
        signalementUnitDAO.remove( lId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SignalementUnit load( Integer lId )
    {
        return signalementUnitDAO.load( lId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( SignalementUnit unitSignalement )
    {
        signalementUnitDAO.store( unitSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getVisibleUnitsIds( )
    {
        return signalementUnitDAO.getVisibleUnitsIds( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getAllUnitsIds( )
    {
        return signalementUnitDAO.getAllUnitsIds( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveVisibleUnits( List<Integer> visibleUnitsIds )
    {
        signalementUnitDAO.removeAll( );
        if ( CollectionUtils.isNotEmpty( visibleUnitsIds ) )
        {
            for ( Integer unitId : visibleUnitsIds )
            {
                SignalementUnit signalementUnit = new SignalementUnit( );
                signalementUnit.setIdUnit( unitId );
                signalementUnit.setVisible( true );
                signalementUnitDAO.insert( signalementUnit, PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME ) );
            }
            List<Integer> allUnitsIds = signalementUnitDAO.getAllUnitsIds( );
            allUnitsIds.removeAll( visibleUnitsIds );
            for ( Integer unitId : allUnitsIds )
            {
                SignalementUnit signalementUnit = new SignalementUnit( );
                signalementUnit.setIdUnit( unitId );
                signalementUnit.setVisible( false );
                signalementUnitDAO.insert( signalementUnit, PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME ) );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UnitWithDepth> getVisibleUnitsWithDepth( Integer idUnit )
    {
        return signalementUnitDAO.getVisibleUnitsWithDepth( idUnit );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getMaxDepthVisibleUnit( Integer idUnit )
    {
        return signalementUnitDAO.getMaxDepthVisibleUnit( idUnit );
    }

}
