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
package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.IWorkflowDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.NotificationSignalementUserMultiContents;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.icon.Icon;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.business.workflow.Workflow;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * WorkflowDAO.
 */
public class WorkflowDAO implements IWorkflowDAO
{

    /** The Constant SQL_QUERY_DELETE. */
    // QUERY
    private static final String SQL_QUERY_DELETE                                     = "DELETE FROM signalement_workflow";

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT                                     = "INSERT INTO signalement_workflow (id_workflow) VALUES (?)";

    /** The Constant SQL_QUERY_SELECT. */
    private static final String SQL_QUERY_SELECT                                     = "SELECT id_workflow FROM signalement_workflow";

    /** The Constant SQL_QUERY_SELECT_ACTION_BY_STATES. */
    private static final String SQL_QUERY_SELECT_ACTION_BY_STATES                    = "SELECT id_action FROM workflow_action WHERE id_state_before=? AND id_state_after=?";

    /** The Constant SQL_QUERY_FIND_ACTION_BY_NAME. */
    private static final String SQL_QUERY_FIND_ACTION_BY_NAME                        = "SELECT id_action, name, id_state_before, id_state_after, id_icon FROM workflow_action WHERE name=?";

    /** The Constant SQL_QUERY_SELECT_MULTI_CONTENTS_MESSAGE_NOTIFICATION. */
    private static final String SQL_QUERY_SELECT_MULTI_CONTENTS_MESSAGE_NOTIFICATION = "select notification_value from signalement_workflow_notifuser_multi_contents_value where id_history = ? ";

    /** The Constant SQL_QUERY_SELECT_MESSAGE_NOTIFICATION. */
    private static final String SQL_QUERY_SELECT_MESSAGE_NOTIFICATION                = "select notification_value from signalement_workflow_notification_user_value where id_history = ? ";

    /** The Constant SQL_QUERY_SELECT_USER_SERVICE_FAIT. */
    private static final String SQL_QUERY_SELECT_USER_SERVICE_FAIT                   = "select user_access_code from workflow_resource_history where id_resource = ? and id_action in (62,70,22,18,49,53,41)";

    /** The Constant SQL_QUERY_SELECT_HISTORY_BY_RESOURCES. */
    private static final String SQL_QUERY_SELECT_HISTORY_BY_RESOURCES                = "select * from workflow_resource_history where id_resource = ? and resource_type = ? and id_workflow = ? ";

    /** The Constant SQL_QUERY_SELECT_ACTION_BY_HISTORY. */
    private static final String SQL_QUERY_SELECT_ACTION_BY_HISTORY                   = "select * from workflow_action where id_action = ?";

    /** The Constant SQL_QUERY_SELECT_ALL_MESSAGE_TASK. */
    private static final String SQL_QUERY_SELECT_ALL_MESSAGE_TASK                    = "SELECT id_task, id_message from signalement_workflow_notifuser_multi_contents_task WHERE id_task in (";

    /** The Constant SQL_QUERY_FIND_BY_PRIMARY_KEY. */
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY                        = "SELECT signalement_workflow_notifuser_multi_contents_config.id_message,subject,sender,title,message"
            + " FROM signalement_workflow_notifuser_multi_contents_config INNER JOIN signalement_workflow_notifuser_multi_contents_task on signalement_workflow_notifuser_multi_contents_config.id_message = signalement_workflow_notifuser_multi_contents_task.id_message WHERE signalement_workflow_notifuser_multi_contents_config.id_message=? AND signalement_workflow_notifuser_multi_contents_task.id_task=?";

    /** The Constant SQL_QUERY_FIND_MESSAGE_ID_TITLE_BY_TASK. */
    private static final String SQL_QUERY_FIND_MESSAGE_ID_TITLE_BY_TASK              = "SELECT cc.id_message, cc.title FROM signalement_workflow_notifuser_multi_contents_task ct, signalement_workflow_notifuser_multi_contents_config cc"
            + " WHERE ct.id_message = cc.id_message and ct.id_task in ({0}) order by cc.id_message";

    /** The Constant SQL_QUERY_UPDATE_USER_ACCESS_CODE_WORKFLOW_HISTORY. */
    private static final String SQL_QUERY_UPDATE_USER_ACCESS_CODE_WORKFLOW_HISTORY   = "UPDATE workflow_resource_history set user_access_code = ? WHERE id_history = ? ";

    /** The Constant SQL_QUERY_FIND_TASK_ID_BY_TASK_TYPE_KEY. */
    private static final String SQL_QUERY_FIND_TASK_ID_BY_TASK_TYPE_KEY              = "SELECT id_task FROM workflow_task WHERE task_type_key=?";

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateWorkflowId( Integer nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.executeQuery( );
        daoUtil.close( );

        daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        if ( nId == null )
        {
            daoUtil.setIntNull( 1 );
        } else
        {
            daoUtil.setInt( 1, nId );
        }

        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer selectWorkflowId( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.executeQuery( );

        Integer workflowId;
        if ( daoUtil.next( ) )
        {
            workflowId = daoUtil.getInt( 1 );
        } else
        {
            workflowId = null;
        }

        daoUtil.close( );

        return workflowId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String selectMultiContentsMessageNotification( Integer idHistory )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_MULTI_CONTENTS_MESSAGE_NOTIFICATION );

        int nIndex = 1;
        daoUtil.setInt( nIndex, idHistory );

        daoUtil.executeQuery( );

        String message = null;
        if ( daoUtil.next( ) )
        {
            message = daoUtil.getString( 1 );
        }

        daoUtil.close( );

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String selectMessageNotification( Integer idHistory )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_MESSAGE_NOTIFICATION );

        int nIndex = 1;
        daoUtil.setInt( nIndex, idHistory );

        daoUtil.executeQuery( );

        String message = null;
        if ( daoUtil.next( ) )
        {
            message = daoUtil.getString( 1 );
        }

        daoUtil.close( );

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String selectUserServiceFait( Integer idResource )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USER_SERVICE_FAIT );

        int nIndex = 1;
        daoUtil.setInt( nIndex, idResource );

        daoUtil.executeQuery( );

        String user = null;
        if ( daoUtil.next( ) )
        {
            user = daoUtil.getString( 1 );
        }

        daoUtil.close( );

        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int selectIdActionByStates( int idStateBefore, int idStateAfter, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ACTION_BY_STATES, plugin );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, idStateBefore );
        daoUtil.setInt( nIndex, idStateAfter );

        daoUtil.executeQuery( );

        int idAction = -1;

        if ( daoUtil.next( ) )
        {
            idAction = daoUtil.getInt( 1 );
        }

        daoUtil.close( );

        return idAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResourceHistory> selectByResource( int nIdResource, String strResourceType, int nIdWorkflow )
    {
        ResourceHistory rh = new ResourceHistory( );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_HISTORY_BY_RESOURCES );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, nIdResource );
        daoUtil.setString( nIndex++, strResourceType );
        daoUtil.setInt( nIndex, nIdWorkflow );

        List<ResourceHistory> response = new ArrayList<>( );

        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            nIndex = 1;
            rh.setId( daoUtil.getInt( nIndex++ ) );
            rh.setIdResource( daoUtil.getInt( nIndex++ ) );
            rh.setResourceType( daoUtil.getString( nIndex++ ) );

            Workflow wf = new Workflow( );
            wf.setId( daoUtil.getInt( nIndex++ ) );
            rh.setWorkFlow( wf );

            Action action = new Action( );
            action.setId( daoUtil.getInt( nIndex++ ) );
            rh.setAction( action );

            rh.setCreationDate( daoUtil.getTimestamp( nIndex++ ) );
            rh.setUserAccessCode( daoUtil.getString( nIndex ) );

            response.add( rh );
        }

        daoUtil.close( );

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Action findByPrimaryKey( int id )
    {
        Action action = new Action( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ACTION_BY_HISTORY );
        int nIndex = 1;
        daoUtil.setInt( nIndex, id );

        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            nIndex = 1;

            action.setId( daoUtil.getInt( nIndex++ ) );
            action.setName( daoUtil.getString( nIndex++ ) );
            action.setDescription( daoUtil.getString( nIndex++ ) );

            Workflow wf = new Workflow( );
            wf.setId( daoUtil.getInt( nIndex++ ) );
            action.setWorkflow( wf );

            State bf = new State( );
            bf.setId( daoUtil.getInt( nIndex++ ) );
            action.setStateBefore( bf );

            State af = new State( );
            af.setId( daoUtil.getInt( nIndex++ ) );
            action.setStateAfter( af );

            Icon ic = new Icon( );
            ic.setId( daoUtil.getInt( nIndex++ ) );
            action.setIcon( ic );

            action.setAutomaticState( daoUtil.getBoolean( nIndex++ ) );
            action.setMassAction( daoUtil.getBoolean( nIndex++ ) );
            action.setOrder( daoUtil.getInt( nIndex++ ) );
            action.setAutomaticReflexiveAction( daoUtil.getBoolean( nIndex ) );

        }

        daoUtil.close( );

        return action;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, List<NotificationSignalementUserMultiContents>> selectMessageServiceFaitPresta( List<String> listTaskPrestaServiceFait )
    {
        Map<Integer, List<NotificationSignalementUserMultiContents>> taskMessagesServiceFait = new HashMap<>( );

        String[] tab = new String[listTaskPrestaServiceFait.size( )];
        for ( int i = 0; i < tab.length; i++ )
        {
            tab[i] = "?";
        }
        String listewhere = StringUtils.join( tab, "," );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_MESSAGE_TASK + listewhere + ") ORDER BY id_task, id_message" );

        int index = 1;
        for ( String idTask : listTaskPrestaServiceFait )
        {
            daoUtil.setInt( index++, Integer.parseInt( idTask ) );
        }

        daoUtil.executeQuery( );

        List<Long> listIdMessage = new ArrayList<>( );
        List<Integer> listIdTask = new ArrayList<>( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;
            listIdTask.add( daoUtil.getInt( nIndex++ ) );
            listIdMessage.add( daoUtil.getLong( nIndex ) );
        }

        daoUtil.close( );

        Set<Integer> set = new HashSet<>( );
        set.addAll( listIdTask );
        List<Integer> distinctListIdTask = new ArrayList<>( set );

        for ( Integer idTask : distinctListIdTask )
        {
            List<NotificationSignalementUserMultiContents> messagesServiceFait = new ArrayList<>( );

            for ( Long idMessage : listIdMessage )
            {
                NotificationSignalementUserMultiContents message = getMessageByIdMessage( idMessage, idTask, null );
                if ( message.getIdMessage( ) != null )
                {
                    message.setIdTask( idTask );
                    messagesServiceFait.add( message );
                }
            }
            taskMessagesServiceFait.put( idTask, messagesServiceFait );
        }

        return taskMessagesServiceFait;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationSignalementUserMultiContents getMessageByIdMessage( Long nIdMessage, Integer idTask, Plugin plugin )
    {
        NotificationSignalementUserMultiContents userTaskConfig = new NotificationSignalementUserMultiContents( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );

        int nIndex = 1;

        daoUtil.setLong( nIndex++, nIdMessage );
        daoUtil.setInt( nIndex, idTask );

        daoUtil.executeQuery( );

        int nPos = 0;

        if ( daoUtil.next( ) )
        {
            userTaskConfig.setIdMessage( daoUtil.getLong( ++nPos ) );
            userTaskConfig.setSubject( daoUtil.getString( ++nPos ) );
            userTaskConfig.setSender( daoUtil.getString( ++nPos ) );
            userTaskConfig.setTitle( daoUtil.getString( ++nPos ) );
            userTaskConfig.setMessage( daoUtil.getString( ++nPos ) );
        }

        daoUtil.close( );

        return userTaskConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NotificationSignalementUserMultiContents> getMessageByIdMessage( List<ITask> tasks )
    {

        List<NotificationSignalementUserMultiContents> listMessages = new ArrayList<>( );
        if ( tasks.isEmpty( ) )
        {
            return listMessages;
        }

        String inClauseValue = StringUtils.join( tasks.stream( ).map( ITask::getId ).toArray( ), "," );
        String sqlRequest = MessageFormat.format( SQL_QUERY_FIND_MESSAGE_ID_TITLE_BY_TASK, inClauseValue );
        DAOUtil daoUtil = new DAOUtil( sqlRequest );

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            NotificationSignalementUserMultiContents message = new NotificationSignalementUserMultiContents( );
            message.setIdMessage( daoUtil.getLong( 1 ) );
            message.setTitle( daoUtil.getString( 2 ) );
            listMessages.add( message );
        }

        daoUtil.close( );

        return listMessages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUserAccessCodeResouceHistory( String userAccessCode, int idResourceHistory )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_USER_ACCESS_CODE_WORKFLOW_HISTORY );
        int nIndex = 1;
        daoUtil.setString( nIndex++, userAccessCode );
        daoUtil.setInt( nIndex, idResourceHistory );
        daoUtil.executeUpdate( );

        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> findIdTaskByTaskKey( String strTakKeyName )
    {

        List<Long> listIdsTask = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_TASK_ID_BY_TASK_TYPE_KEY ); )
        {
            daoUtil.setString( 1, strTakKeyName );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                listIdsTask.add( daoUtil.getLong( 1 ) );
            }
        }

        return listIdsTask;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Action> findActionByName( String strActionName )
    {

        List<Action> listAction = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ACTION_BY_NAME ); )
        {
            daoUtil.setString( 1, strActionName );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                int nIndex = 1;
                Action action = new Action( );
                action.setId( daoUtil.getInt( nIndex++ ) );
                action.setName( daoUtil.getString( nIndex++ ) );
                State sateBefore = new State( );
                sateBefore.setId( daoUtil.getInt( nIndex++ ) );
                action.setStateBefore( sateBefore );
                State sateAfter = new State( );
                sateAfter.setId( daoUtil.getInt( nIndex++ ) );
                action.setStateAfter( sateAfter );
                Icon icon = new Icon( );
                icon.setId( daoUtil.getInt( nIndex ) );
                action.setIcon( icon );
                listAction.add(action );
            }
        }

        return listAction;
    }

}
