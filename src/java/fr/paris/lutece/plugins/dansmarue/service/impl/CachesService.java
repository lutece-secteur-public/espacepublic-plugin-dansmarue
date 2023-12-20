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
import javax.inject.Named;

import fr.paris.lutece.plugins.dansmarue.business.dao.IArrondissementDAO;
import fr.paris.lutece.plugins.dansmarue.business.dao.IConseilQuartierDao;
import fr.paris.lutece.plugins.dansmarue.business.dao.ITypeSignalementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.ConseilQuartier;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;

/**
 * The Class CacheService.
 */
public class CachesService extends AbstractCacheableService
{
    private static final String SERVICE_NAME = "Caches DMR service";
    private static final String ARRONDISSEMENTS = "arrondissements";
    private static final String CONSEIL_QUARTIER = "conseilQuartier";
    private static final String WORKFLOW_STATE = "workflowState";
    private static final String TYPE_SIGNALEMENT = "typeSignalement";

    @Inject
    @Named( "signalement.arrondissementDAO" )
    private IArrondissementDAO _arrondissementDAO;

    @Inject
    @Named( "signalement.conseilQuartierDAO" )
    private IConseilQuartierDao _conseilQuartierDAO;

    @Inject
    @Named( "typeSignalementDAO" )
    private ITypeSignalementDAO _typesignalementDAO;

    /**
     * Instantiates a new Cache service.
     */
    public CachesService( )
    {
        initCache( );
    }

    /**
     * Gets all arrondissement.
     *
     * @return the all arrondissement
     */
    public List<Arrondissement> getAllArrondissement( )
    {
        List<Arrondissement> arrondissements = (List<Arrondissement>) getFromCache( ARRONDISSEMENTS );
        if ( arrondissements == null )
        {
            arrondissements = _arrondissementDAO.getAllArrondissement( );
            putInCache( ARRONDISSEMENTS, arrondissements );
        }
        return arrondissements;
    }

    @Override
    public String getName( )
    {
        return SERVICE_NAME;
    }

    /**
     * Select quartiers list list.
     *
     * @return the list
     */
    public List<ConseilQuartier> selectQuartiersList( )
    {
        List<ConseilQuartier> conseilQuartierList = (List<ConseilQuartier>) getFromCache( CONSEIL_QUARTIER );
        if ( conseilQuartierList == null )
        {
            conseilQuartierList = _conseilQuartierDAO.selectQuartiersList( );
            putInCache( CONSEIL_QUARTIER, conseilQuartierList );
        }
        return conseilQuartierList;
    }

    /**
     * Gets all state by workflow.
     *
     * @param user
     *            the user
     * @return the all state by workflow
     */
    public List<State> getAllStateByWorkflow( AdminUser user )
    {
        List<State> stateList = (List<State>) getFromCache( WORKFLOW_STATE );
        if ( stateList == null )
        {
            stateList = (List<State>) WorkflowService.getInstance( ).getAllStateByWorkflow( SignalementConstants.SIGNALEMENT_WORKFLOW_ID, user );
            putInCache( WORKFLOW_STATE, stateList );
        }
        return stateList;
    }

    public List<TypeSignalement> getAllTypeSignalement( )
    {

        List<TypeSignalement> listTypeSignalement = (List<TypeSignalement>) getFromCache( TYPE_SIGNALEMENT );
        if ( listTypeSignalement == null )
        {
            listTypeSignalement = _typesignalementDAO.getAllTypeSignalement( );
            putInCache( TYPE_SIGNALEMENT, listTypeSignalement );
        }

        return listTypeSignalement;
    }
}
