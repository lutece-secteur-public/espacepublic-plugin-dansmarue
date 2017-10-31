package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import fr.paris.lutece.plugins.dansmarue.business.dao.IWorkflowDAO;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * 
 * WorkflowDAO
 * 
 */
public class WorkflowDAO implements IWorkflowDAO
{
    //CONSTANTS
    private static final String SIGNALEMENT_RESOURCE = "SIGNALEMENT_SIGNALEMENT";

    //QUERY
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_workflow";
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_workflow (id_workflow) VALUES (?)";
    private static final String SQL_QUERY_SELECT = "SELECT id_workflow FROM signalement_workflow";
    private static final String SQL_QUERY_SELECT_HISTORY_BY_SIGNALEMENT = "SELECT workflow_action.name, workflow_resource_history.creation_date, workflow_resource_history.user_access_code FROM workflow_action, workflow_resource_history WHERE workflow_action.id_action = workflow_resource_history.id_action AND resource_type = ? AND id_resource = ?";
    private static final String SQL_QUERY_SELECT_ACTION_BY_STATES = "SELECT id_action FROM workflow_action WHERE id_state_before=? AND id_state_after=?";
    
    /**
     * {@inheritDoc}
     */
    public void updateWorkflowId( Integer nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.executeQuery( );

        daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        if ( nId == null )
        {
            daoUtil.setIntNull( 1 );
        }
        else
        {
            daoUtil.setInt( 1, nId );
        }

        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    public Integer selectWorkflowId( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.executeQuery( );

        Integer workflowId;
        if ( daoUtil.next( ) )
        {
            workflowId = daoUtil.getInt( 1 );
        }
        else
        {
            workflowId = null;
        }

        daoUtil.free( );

        return workflowId;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int selectIdActionByStates(int idStateBefore, int idStateAfter, Plugin plugin){
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ACTION_BY_STATES, plugin );
    	int nIndex = 1;
    	daoUtil.setInt(nIndex++, idStateBefore);
    	daoUtil.setInt(nIndex++, idStateAfter);
    	
    	daoUtil.executeQuery();
    	
    	int idAction = -1;
    	
    	if(daoUtil.next()){
    		idAction = daoUtil.getInt(1);
    	}
    	
    	daoUtil.free();
    	
    	return idAction;
    }
    
}
