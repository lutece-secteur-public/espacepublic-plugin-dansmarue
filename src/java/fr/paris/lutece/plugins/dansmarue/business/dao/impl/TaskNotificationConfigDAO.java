/*
 * Copyright (c) 2002-2018, Mairie de Paris
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

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.ITaskNotificationConfigDAO;
import fr.paris.lutece.util.sql.DAOUtil;

public class TaskNotificationConfigDAO implements ITaskNotificationConfigDAO
{
    private static final String SQL_QUERY_MESSAGE_BY_TASK_TYPE_STATE_WORKFLOW = "SELECT message FROM signalement_workflow_notification_user_config notif_user_config "
            + " INNER JOIN workflow_task task ON notif_user_config.id_task = task.id_task " + " INNER JOIN workflow_action action ON task.id_action = action.id_action " + " WHERE task_type_key= ? "
            + " AND action.id_workflow=? " + " AND action.id_state_before=? " + " AND is_automatic=1";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessageByTaskTypeStateWorkflow( String taskType, int idWorkflow, int idStateBefore )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_MESSAGE_BY_TASK_TYPE_STATE_WORKFLOW );

        int nIndex = 1;

        daoUtil.setString( nIndex++, taskType );
        daoUtil.setInt( nIndex++, idWorkflow );
        daoUtil.setInt( nIndex, idStateBefore );

        daoUtil.executeQuery( );

        int nPos = 0;

        String message = StringUtils.EMPTY;

        if ( daoUtil.next( ) )
        {
            message = daoUtil.getString( ++nPos );
        }

        daoUtil.close( );
        return message;

    }
}
