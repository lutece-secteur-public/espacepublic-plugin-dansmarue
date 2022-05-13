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
package fr.paris.lutece.plugins.dansmarue.business.dao;

import java.util.List;
import java.util.Map;

import fr.paris.lutece.plugins.dansmarue.business.entities.NotificationSignalementUserMultiContents;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * ISignalementWorklowDAO.
 */
public interface IWorkflowDAO
{

    /**
     * Update DansMaRue workflow id.
     *
     * @param nId
     *            DansMaRue workflow id
     * @param plugin
     *            le plugin
     */
    void updateWorkflowId( Integer nId, Plugin plugin );

    /**
     * Returns DansMaRue workflow id.
     *
     * @param plugin
     *            the plugin
     * @return DansMaRue workflow id
     */
    Integer selectWorkflowId( Plugin plugin );

    /**
     * Returns the action id according to the start and finish states.
     *
     * @param idStateBefore
     *            the state before processing
     * @param idStateAfter
     *            the state after processing
     * @param plugin
     *            the plugin
     * @return The id of the action -1 if not found
     */
    int selectIdActionByStates( int idStateBefore, int idStateAfter, Plugin plugin );

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
     * Returns an action by its id.
     *
     * @param id
     *            the aciton id
     * @return an action
     */
    Action findByPrimaryKey( int id );

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
    List<ResourceHistory> selectByResource( int nIdResource, String strResourceType, int nIdWorkflow );

    /**
     * Returns the notification messages associated with a task.
     *
     * @param listTaskPrestaServiceFait
     *            a list of service done tasks
     * @return a map of tasks id and messages associated with the task
     */
    Map<Integer, List<NotificationSignalementUserMultiContents>> selectMessageServiceFaitPresta( List<String> listTaskPrestaServiceFait );

    /**
     * Returns the message sended to the user from the task.
     *
     * @param nIdMessage
     *            the message id
     * @param idTask
     *            the workflow task id
     * @param plugin
     *            the plugin
     * @return a notification message for the user
     */
    NotificationSignalementUserMultiContents getMessageByIdMessage( Long nIdMessage, Integer idTask, Plugin plugin );

    /**
     * Returns list message service done for a list of task.
     *
     * @param tasks
     *            list of tasks
     *
     * @return list of message
     */
    List<NotificationSignalementUserMultiContents> getMessageByIdMessage( List<ITask> tasks );

    /**
     * Update DansMaRue workflow id.
     *
     * @param userAccessCode
     *            User Access Code
     * @param idResourceHistory
     *            id resource history
     */
    void updateUserAccessCodeResouceHistory( String userAccessCode, int idResourceHistory );

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
