package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.ITaskNotificationConfigDAO;
import fr.paris.lutece.util.sql.DAOUtil;

public class TaskNotificationConfigDAO implements ITaskNotificationConfigDAO{
	 private static final String SQL_QUERY_MESSAGE_BY_TASK_TYPE_STATE_WORKFLOW = "SELECT message FROM signalement_workflow_notification_user_config notif_user_config "
				+" INNER JOIN workflow_task task ON notif_user_config.id_task = task.id_task "
				+" INNER JOIN workflow_action action ON task.id_action = action.id_action "
				+" WHERE task_type_key= ? "
				+" AND action.id_workflow=? "
				+" AND action.id_state_before=? "
				+" AND is_automatic=1";
	 
	 
	 /**
	  * Gets the message of a signalement notification user task
	  * by its, task type, idworkflow and state before for automatic actions
	  * @param taskType the type of the notification task
	  * @param idWorkflow the workflow id
	  * @param idStateBefore the id of the state before executing action
	  * @return
	  */
	 public String getMessageByTaskTypeStateWorkflow(String taskType, int idWorkflow, int idStateBefore){
    	DAOUtil daoUtil = new DAOUtil(SQL_QUERY_MESSAGE_BY_TASK_TYPE_STATE_WORKFLOW);
         
         daoUtil.setString( 1, taskType );
         daoUtil.setInt( 2, idWorkflow );
         daoUtil.setInt( 3, idStateBefore );
         
         daoUtil.executeQuery( );

         int nPos = 0;

         String message = StringUtils.EMPTY;
         
         if ( daoUtil.next( ) )
         {
        	 message  = daoUtil.getString( ++nPos );
         }
         
         daoUtil.free( );
         return message;
    	
    }
}
