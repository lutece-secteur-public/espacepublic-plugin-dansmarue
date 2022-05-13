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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.IWorkflowDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.NotificationSignalementUserMultiContents;
import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.plugins.workflowcore.service.task.ITaskService;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * The Class WorkflowService.
 */
public class WorkflowService extends AbstractCacheableService implements IWorkflowService
{

    /** The Constant SIGNALEMENT_WORKFLOW_KEY. */
    private static final String SIGNALEMENT_WORKFLOW_KEY = "workflow_signalement";

    /** The Constant SERVICE_NAME. */
    private static final String SERVICE_NAME = "Signalement workflow service";

    /** The workflow DAO. */
    // DAO
    @Inject
    private IWorkflowDAO _workflowDAO;

    /** The task service. */
    @Inject
    private ITaskService _taskService;

    /**
     * Gets the name.
     *
     * @return the name
     */
    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.portal.service.util.LuteceService#getName()
     */
    @Override
    /**
     * {@inheritDoc}
     */
    public String getName( )
    {
        return SERVICE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getSignalementWorkflowId( )
    {
        Integer nIdWorkflow = (Integer) getFromCache( SIGNALEMENT_WORKFLOW_KEY );
        if ( nIdWorkflow == null )
        {
            nIdWorkflow = _workflowDAO.selectWorkflowId( null );
            putInCache( SIGNALEMENT_WORKFLOW_KEY, nIdWorkflow );
        }

        return nIdWorkflow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSignalementWorkflowId( Integer nIdWorkflow )
    {
        _workflowDAO.updateWorkflowId( nIdWorkflow, null );
        getCache( ).remove( SIGNALEMENT_WORKFLOW_KEY );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String selectMessageNotification( Integer idHistory )
    {
        return _workflowDAO.selectMessageNotification( idHistory );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String selectMultiContentsMessageNotification( Integer idHistory )
    {
        return _workflowDAO.selectMultiContentsMessageNotification( idHistory );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResourceHistory> getAllHistoryByResource( int nIdResource, String strResourceType, int nIdWorkflow )
    {
        List<ResourceHistory> listResourceHistory = _workflowDAO.selectByResource( nIdResource, strResourceType, nIdWorkflow );

        for ( ResourceHistory resourceHistory : listResourceHistory )
        {
            resourceHistory.setAction( _workflowDAO.findByPrimaryKey( resourceHistory.getAction( ).getId( ) ) );
        }

        return listResourceHistory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceHistory getLastHistoryResource( int nIdResource, String strResourceType, int nIdWorkflow )
    {
        List<ResourceHistory> listResourceHistory = _workflowDAO.selectByResource( nIdResource, strResourceType, nIdWorkflow );

        for ( ResourceHistory resourceHistory : listResourceHistory )
        {
            resourceHistory.setAction( _workflowDAO.findByPrimaryKey( resourceHistory.getAction( ).getId( ) ) );
        }

        return ( !listResourceHistory.isEmpty( ) ) ? listResourceHistory.get( 0 ) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String selectUserServiceFait( Integer idResource )
    {
        return _workflowDAO.selectUserServiceFait( idResource );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int selectIdActionByStates( int idStateBefore, int idStateAfter )
    {
        return _workflowDAO.selectIdActionByStates( idStateBefore, idStateAfter, null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, List<NotificationSignalementUserMultiContents>> selectMessageServiceFaitPresta( String strIdAction )
    {
        String strListTaskPrestaServiceFait = AppPropertiesService.getProperty( "signalement.task.presta.message.service.fait" );
        List<String> listTaskPrestaServiceFait = Arrays.asList( strListTaskPrestaServiceFait.split( "," ) );
        List<String> listTaskPrestaServiceFaitToReturn = new ArrayList<>( );

        if ( StringUtils.EMPTY.equals( strIdAction ) )
        {
            List<ITask> listActionTasks = _taskService.getListTaskByIdAction( Integer.parseInt( strIdAction ), Locale.FRENCH );
            for ( ITask task : listActionTasks )
            {
                String idTask = Integer.toString( task.getId( ) );
                if ( listTaskPrestaServiceFait.contains( idTask ) )
                {
                    listTaskPrestaServiceFaitToReturn.add( idTask );
                }
            }
        }
        else
        {
            listTaskPrestaServiceFaitToReturn = listTaskPrestaServiceFait;
        }

        return _workflowDAO.selectMessageServiceFaitPresta( listTaskPrestaServiceFaitToReturn );
    }

    /**
     * Gets the messages service fait.
     *
     * @param tasks
     *            the tasks
     * @return the messages service fait
     */
    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.dansmarue.service.IWorkflowService#getMessagesServiceFait(java.util.List)
     */
    @Override
    public List<NotificationSignalementUserMultiContents> getMessagesServiceFait( List<ITask> tasks )
    {

        List<ITask> filterList = tasks.stream( )
                .filter( task -> SignalementConstants.TASK_KEY_NOTIFICATION_USER_MULTICONTENTS.equals( task.getTaskType( ).getKey( ) ) )
                .collect( Collectors.toList( ) );

        return _workflowDAO.getMessageByIdMessage( filterList );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserAccessCodeHistoryResource( String userAccessCode, int idResourceHistory )
    {
        _workflowDAO.updateUserAccessCodeResouceHistory( userAccessCode, idResourceHistory );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> findIdTaskByTaskKey( String strTakKeyName )
    {
        return _workflowDAO.findIdTaskByTaskKey( strTakKeyName );
    }

    /**
     * Find action by name.
     *
     * @param strActionName
     *            the str action name
     * @return the list
     */
    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.dansmarue.service.IWorkflowService#findActionByName(java.lang.String)
     */
    @Override
    public List<Action> findActionByName( String strActionName )
    {
        return _workflowDAO.findActionByName( strActionName );
    }

}
