package fr.paris.lutece.plugins.dansmarue.business.dao;

/**
 * The interface of ITaskNotificationConfigDAO
 *
 */
public interface ITaskNotificationConfigDAO {
	
	/**
	  * Gets the message of a signalement notification user task
	  * by its, task type, idworkflow and state before for automatic actions
	  * @param taskType
	  * @param idWorkflow
	  * @param idStateBefore
	  * @return
	  */
	String getMessageByTaskTypeStateWorkflow(String taskType, int idWorkflow, int idStateBefore);
	
}
