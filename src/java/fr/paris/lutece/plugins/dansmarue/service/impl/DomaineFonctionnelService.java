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

import fr.paris.lutece.plugins.dansmarue.business.dao.IDomaineFonctionnelDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.DomaineFonctionnel;
import fr.paris.lutece.plugins.dansmarue.service.IDomaineFonctionnelService;
import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

/**
 * The Class DomaineFonctionnelService.
 */
public class DomaineFonctionnelService implements IDomaineFonctionnelService
{

    /** The domaine fonctionnel DAO. */
    @Inject
    private IDomaineFonctionnelDAO _domaineFonctionnelDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer insert( DomaineFonctionnel domaineFonctionnel )
    {
        Plugin pluginSignalement = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
        return _domaineFonctionnelDAO.insert( domaineFonctionnel, pluginSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( long lId )
    {
        _domaineFonctionnelDAO.remove( lId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomaineFonctionnel getById( Integer lId )
    {
        DomaineFonctionnel domaineFonctionnel = _domaineFonctionnelDAO.getById( lId );
        if ( null != domaineFonctionnel )
        {
            List<Integer> arrondissementIds = _domaineFonctionnelDAO.getArrondissementsIdsByDomaineId( lId );
            domaineFonctionnel.setArrondissementsIds( arrondissementIds );
            domaineFonctionnel.setQuartiersIds( _domaineFonctionnelDAO.getQuartiersIdsByDomaineId( domaineFonctionnel.getId( ) ) );
            List<Integer> typesSignalementIds = _domaineFonctionnelDAO.getTypesSignalementIdsByDomaineId( lId );
            domaineFonctionnel.setTypesSignalementIds( typesSignalementIds );
            List<Integer> unitsIds = _domaineFonctionnelDAO.getUnitsIdsByDomaineId( lId );
            domaineFonctionnel.setUnitIds( unitsIds );
        }
        return domaineFonctionnel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( DomaineFonctionnel domaineFonctionnel )
    {
        _domaineFonctionnelDAO.store( domaineFonctionnel );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DomaineFonctionnel> getAllDomainesFonctionnel( )
    {
        List<DomaineFonctionnel> domainesFonctionnelList = _domaineFonctionnelDAO.getAllDomainesFonctionnel( );
        for ( DomaineFonctionnel domaineFonctionnel : domainesFonctionnelList )
        {
            List<Integer> arrondissementIds = _domaineFonctionnelDAO.getArrondissementsIdsByDomaineId( domaineFonctionnel.getId( ) );
            domaineFonctionnel.setArrondissementsIds( arrondissementIds );
            domaineFonctionnel.setQuartiersIds( _domaineFonctionnelDAO.getQuartiersIdsByDomaineId( domaineFonctionnel.getId( ) ) );
            List<Integer> typesSignalementIds = _domaineFonctionnelDAO.getTypesSignalementIdsByDomaineId( domaineFonctionnel.getId( ) );
            domaineFonctionnel.setTypesSignalementIds( typesSignalementIds );
            List<Integer> unitsIds = _domaineFonctionnelDAO.getUnitsIdsByDomaineId( domaineFonctionnel.getId( ) );
            domaineFonctionnel.setUnitIds( unitsIds );
        }
        return domainesFonctionnelList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getArrondissementsIdsByDomaineId( int idDomaine )
    {
        return _domaineFonctionnelDAO.getArrondissementsIdsByDomaineId( idDomaine );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getTypesSignalementIdsByDomaineId( int idDomaine )
    {
        return _domaineFonctionnelDAO.getTypesSignalementIdsByDomaineId( idDomaine );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DomaineFonctionnel> getAllDomainesFonctionnelActifs( )
    {
        List<DomaineFonctionnel> domainesFonctionnelList = _domaineFonctionnelDAO.getAllDomainesFonctionnelActifs( );
        for ( DomaineFonctionnel domaineFonctionnel : domainesFonctionnelList )
        {
            List<Integer> arrondissementIds = _domaineFonctionnelDAO.getArrondissementsIdsByDomaineId( domaineFonctionnel.getId( ) );
            domaineFonctionnel.setArrondissementsIds( arrondissementIds );
            domaineFonctionnel.setQuartiersIds( _domaineFonctionnelDAO.getQuartiersIdsByDomaineId( domaineFonctionnel.getId( ) ) );
            List<Integer> typesSignalementIds = _domaineFonctionnelDAO.getTypesSignalementIdsByDomaineId( domaineFonctionnel.getId( ) );
            domaineFonctionnel.setTypesSignalementIds( typesSignalementIds );
            List<Integer> unitsIds = _domaineFonctionnelDAO.getUnitsIdsByDomaineId( domaineFonctionnel.getId( ) );
            domaineFonctionnel.setUnitIds( unitsIds );
        }
        return domainesFonctionnelList;
    }

}
