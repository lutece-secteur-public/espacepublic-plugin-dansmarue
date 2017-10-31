package fr.paris.lutece.plugins.dansmarue.service.impl;

import fr.paris.lutece.plugins.dansmarue.business.dao.IWorkflowDAO;
import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import javax.inject.Inject;

public class WorkflowService extends AbstractCacheableService implements IWorkflowService
{
    private static final String SIGNALEMENT_WORKFLOW_KEY = "workflow_signalement";
    private static final String SERVICE_NAME = "Signalement workflow service";


    //DAO
    @Inject
    private IWorkflowDAO _workflowDAO;

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
            // TODO need Plugin ?
            nIdWorkflow = _workflowDAO.selectWorkflowId( null );
            putInCache( SIGNALEMENT_WORKFLOW_KEY, nIdWorkflow );
        }

        return nIdWorkflow;
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void setSignalementWorkflowId( Integer nIdWorkflow )
    {
        // TODO need Plugin ?
        _workflowDAO.updateWorkflowId( nIdWorkflow, null );
        getCache( ).remove( SIGNALEMENT_WORKFLOW_KEY );
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public int selectIdActionByStates(int idStateBefore, int idStateAfter){
    	return _workflowDAO.selectIdActionByStates(idStateBefore, idStateAfter, null);
    }

}
