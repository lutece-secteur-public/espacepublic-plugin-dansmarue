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
package fr.paris.lutece.plugins.dansmarue.service;

import java.util.List;
import java.util.Map;

import fr.paris.lutece.plugins.dansmarue.business.entities.NotificationSignalementUserMultiContents;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;

/**
 * IWorkflowService.
 */
public interface IWorkflowService
{

    /**
     * Returns DansMaRue workflow id.
     *
     * @return DansMaRue workflow id
     */
    Integer getSignalementWorkflowId( );

    /**
     * Update DansMaRue workflow id.
     *
     * @param nIdWorkflow
     *            DansMaRue workflow id
     */
    void setSignalementWorkflowId( Integer nIdWorkflow );

    /**
     * Returns the action id according to the start and finish states.
     *
     * @param idStateBefore
     *            the state before processing
     * @param idStateAfter
     *            the state after processing
     * @return The id of the action -1 if not found
     */
    int selectIdActionByStates( int idStateBefore, int idStateAfter );

    /**
     * Returns the notification message linked to a workflow history.
     *
     * @param idHistory
     *            the workflow history id
     * @return the notification message
     */
    String selectMessageNotification( Integer idHistory );

    /**
     * Returns the user access code from the id of the report.
     *
     * @param idResource
     *            the workflow resource id
     * @return the user access code
     */
    String selectUserServiceFait( Integer idResource );

    /**
     * Returns the notification multicontent message linked to a workflow history.
     *
     * @param idHistory
     *            the workflow history id
     * @return the notification message
     */
    String selectMultiContentsMessageNotification( Integer idHistory );

    /**
     * Returns a list of workflow resource history.
     *
     * @param nIdResource
     *            the workflow resource id
     * @param strResourceType
     *            the workflow resource type
     * @param nIdWorkflow
     *            the workflow history id
     * @return a list of workflow resource history
     */
    List<ResourceHistory> getAllHistoryByResource( int nIdResource, String strResourceType, int nIdWorkflow );

    /**
     * Returns the last workflow history resource.
     *
     * @param nIdResource
     *            the workflow resource id
     * @param strResourceType
     *            the workflow resource type
     * @param nIdWorkflow
     *            the workflow history id
     * @return a workflow resource history
     */
    ResourceHistory getLastHistoryResource( int nIdResource, String strResourceType, int nIdWorkflow );

    /**
     * Returns the notification messages associated with a task.
     *
     * @param listTaskPrestaServiceFait
     *            a list of service done tasks
     * @return a map of tasks id and messages associated with the task
     */
    Map<Integer, List<NotificationSignalementUserMultiContents>> selectMessageServiceFaitPresta( String listTaskPrestaServiceFait );

    /**
     * Returns List Message Service Fait.
     *
     * @param tasks
     *            a list of task linked to message service fait
     * @return the messages service fait
     */
    List<NotificationSignalementUserMultiContents> getMessagesServiceFait( List<ITask> tasks );

    /**
     * Update user access code for history resource.
     *
     * @param userAccessCode
     *            User Access Code
     * @param idResourceHistory
     *            id resource history
     *
     */
    void setUserAccessCodeHistoryResource( String userAccessCode, int idResourceHistory );

    /**
     * Find list of id task corresponding with the task key name.
     *
     * @param strTakKeyName
     *            task key name
     * @return list ids task
     */
    List<Long> findIdTaskByTaskKey( String strTakKeyName );

    /**
     * Find list of action with the action name.
     *
     * @param strActionName
     *            action name
     * @return list action
     */
    List<Action> findActionByName( String strActionName );
}
