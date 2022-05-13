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
import javax.inject.Named;

import fr.paris.lutece.plugins.dansmarue.business.dao.IPhotoDAO;
import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementExportDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementFilter;
import fr.paris.lutece.plugins.dansmarue.commons.Order;
import fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementExportService;
import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;
import fr.paris.lutece.plugins.dansmarue.service.dto.SignalementExportCSVDTO;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

/**
 * The Class SignalementExportService.
 */
public class SignalementExportService implements ISignalementExportService
{
    /** The _signalement dao. */
    @Inject
    @Named( "signalementExportDAO" )
    private ISignalementExportDAO _signalementExportDAO;

    @Inject
    @Named( "photoDAO" )
    private IPhotoDAO _photoDAO;

    /** The plugin signalement. */
    // Plugin
    private Plugin _pluginSignalement = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );

    /**
     * Find by ids.
     *
     * @param ids
     *            the ids
     * @return the list
     */
    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.dansmarue.service.ISignalementExportService#findByIds(int[])
     */
    @Override
    public List<SignalementExportCSVDTO> findByIds( int [ ] ids )
    {

        return _signalementExportDAO.findByIds( ids, _pluginSignalement );
    }

    /**
     * Find by filter.
     *
     * @param filter
     *            the filter
     * @return the list
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.paris.lutece.plugins.dansmarue.service.ISignalementExportService#findByFilter(fr.paris.lutece.plugins.dansmarue.business.entities.SignalementFilter)
     */
    @Override
    public List<SignalementExportCSVDTO> findByFilter( SignalementFilter filter )
    {

        return _signalementExportDAO.findByFilter( filter, _pluginSignalement );
    }

    /**
     * Count search result.
     *
     * @param filter
     *            the filter
     * @return the int
     */
    @Override
    public int countSearchResult( SignalementFilter filter )
    {

        return _signalementExportDAO.countSignalementSearch( filter, _pluginSignalement );
    }

    /**
     * Find by filter search.
     *
     * @param filter
     *            the filter
     * @param paginationProperties
     *            the pagination properties
     * @return the list
     */
    @Override
    public List<Signalement> findByFilterSearch( SignalementFilter filter, PaginationProperties paginationProperties )
    {
        List<Signalement> result = new ArrayList<>( );
        List<String> numeroFinds = _signalementExportDAO.searchNumeroByFilter( filter, paginationProperties, _pluginSignalement );
        if ( !numeroFinds.isEmpty( ) )
        {
            result = _signalementExportDAO.searchFindByFilter( filter, numeroFinds, _pluginSignalement );
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Signalement> findByFilterSearchForFDT( SignalementFilter filter, PaginationProperties paginationProperties )
    {
        List<Signalement> result = new ArrayList<>( );
        List<String> numeroFinds = _signalementExportDAO.searchNumeroByFilter( filter, paginationProperties, _pluginSignalement );
        if ( !numeroFinds.isEmpty( ) )
        {
            result = _signalementExportDAO.searchFindByFilterForFDT( filter, numeroFinds, _pluginSignalement );
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SignalementExportCSVDTO> findByIdsWithPhoto( int [ ] ids )
    {
        List<SignalementExportCSVDTO> signalementExportCSVDTOList = findByIds( ids );
        for ( SignalementExportCSVDTO signalementExportCSVDTO : signalementExportCSVDTOList )
        {
            signalementExportCSVDTO.setPhotos( _photoDAO.findBySignalementId( signalementExportCSVDTO.getIdSignalement( ) ) );
        }
        return signalementExportCSVDTOList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SignalementExportCSVDTO> findByIdsWithPhotoWithOrder( int [ ] ids, Order order )
    {
        List<SignalementExportCSVDTO> signalementExportCSVDTOList = _signalementExportDAO.findByIdsWithOrder( ids, order, _pluginSignalement );
        for ( SignalementExportCSVDTO signalementExportCSVDTO : signalementExportCSVDTOList )
        {
            signalementExportCSVDTO.setPhotos( _photoDAO.findBySignalementId( signalementExportCSVDTO.getIdSignalement( ) ) );
        }
        return signalementExportCSVDTOList;
    }

}
