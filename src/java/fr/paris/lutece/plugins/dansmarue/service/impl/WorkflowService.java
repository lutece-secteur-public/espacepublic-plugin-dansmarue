package fr.paris.lutece.plugins.dansmarue.service.impl;

import fr.paris.lutece.plugins.dansmarue.business.dao.IWorkflowDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.NotificationSignalementUser3Contents;
import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.Arrays;
import java.util.List;

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
        _workflowDAO.updateWorkflowId( nIdWorkflow, null );
        getCache( ).remove( SIGNALEMENT_WORKFLOW_KEY );
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public String selectMessageNotification( Integer idHistory ){
        return _workflowDAO.selectMessageNotification( idHistory );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String select3ContentsMessageNotification( Integer idHistory ){
        return _workflowDAO.select3ContentsMessageNotification( idHistory );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResourceHistory> getAllHistoryByResource( int nIdResource, String strResourceType, int nIdWorkflow )
    {
        List<ResourceHistory> listResourceHistory = _workflowDAO.selectByResource( nIdResource, strResourceType,
                nIdWorkflow );

        for ( ResourceHistory resourceHistory : listResourceHistory )
        {
            resourceHistory.setAction( _workflowDAO.findByPrimaryKey( resourceHistory.getAction(  ).getId(  ) ) );
        }

        return listResourceHistory;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceHistory getLastHistoryResource( int nIdResource, String strResourceType, int nIdWorkflow )
    {
        List<ResourceHistory> listResourceHistory = _workflowDAO.selectByResource( nIdResource, strResourceType,
                nIdWorkflow );

        for ( ResourceHistory resourceHistory : listResourceHistory )
        {
            resourceHistory.setAction( _workflowDAO.findByPrimaryKey( resourceHistory.getAction(  ).getId(  ) ) );
        }

        return ( !listResourceHistory.isEmpty( ) ) ? listResourceHistory.get( 0 ) : null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String selectUserServiceFait( Integer idResource ){
        return _workflowDAO.selectUserServiceFait( idResource );
    }

    @Override
    public int selectIdActionByStates( int idStateBefore, int idStateAfter )
    {
        return _workflowDAO.selectIdActionByStates(idStateBefore, idStateAfter, null);
    }
    
    @Override
    public List<NotificationSignalementUser3Contents> selectMessageServiceFaitPresta( )
    {
        String strListTaskPrestaServiceFait = AppPropertiesService.getProperty( "signalement.task.presta.message.service.fait" );
        List<String> listTaskPrestaServiceFait = Arrays.asList( strListTaskPrestaServiceFait.split( "," ) );
        
        return _workflowDAO.selectMessageServiceFaitPresta( listTaskPrestaServiceFait );
    }

}
