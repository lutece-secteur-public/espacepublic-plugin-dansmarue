package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.IWorkflowDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.NotificationSignalementUser3Contents;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.icon.Icon;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.business.workflow.Workflow;
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
    private static final String SQL_QUERY_SELECT_MESSAGES_PRESTA = 
            "SELECT id_task,subject,sender,message1,message2,subject_ramen,message1_ramen,message2_ramen,title1, title2 "
            + " FROM signalement_workflow_notifuser_3contents_config WHERE id_task in(";
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_workflow";
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_workflow (id_workflow) VALUES (?)";
    private static final String SQL_QUERY_SELECT = "SELECT id_workflow FROM signalement_workflow";
    private static final String SQL_QUERY_SELECT_HISTORY_BY_SIGNALEMENT = "SELECT workflow_action.name, workflow_resource_history.creation_date, workflow_resource_history.user_access_code FROM workflow_action, workflow_resource_history WHERE workflow_action.id_action = workflow_resource_history.id_action AND resource_type = ? AND id_resource = ?";
    private static final String SQL_QUERY_SELECT_ACTION_BY_STATES = "SELECT id_action FROM workflow_action WHERE id_state_before=? AND id_state_after=?";
    private static final String SQL_QUERY_SELECT_3CONTENTS_MESSAGE_NOTIFICATION = "select notification_value from signalement_workflow_notifuser_3contents_value where id_history = ? "; 
    private static final String SQL_QUERY_SELECT_MESSAGE_NOTIFICATION = "select notification_value from signalement_workflow_notification_user_value where id_history = ? "; 
    private static final String SQL_QUERY_SELECT_USER_SERVICE_FAIT = "select user_access_code from workflow_resource_history where id_resource = ? and id_action in (62,70,22,18,49,53,41)"; 
    private static final String SQL_QUERY_SELECT_HISTORY_BY_RESOURCES = "select * from workflow_resource_history where id_resource = ? and resource_type = ? and id_workflow = ? "; 
    private static final String SQL_QUERY_SELECT_ACTION_BY_HISTORY = "select * from workflow_action where id_action = ?";

    
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
    public String select3ContentsMessageNotification( Integer idHistory )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_3CONTENTS_MESSAGE_NOTIFICATION );
        
        int nIndex = 1;
        daoUtil.setInt(nIndex++, idHistory);
        
        daoUtil.executeQuery( );

        String message = null;
        if ( daoUtil.next( ) )
        {
            message = daoUtil.getString( 1 );
        }

        daoUtil.free( );

        return message;
    }
    
    /**
     * {@inheritDoc}
     */
    public String selectMessageNotification( Integer idHistory )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_MESSAGE_NOTIFICATION );
        
        int nIndex = 1;
        daoUtil.setInt(nIndex++, idHistory);
        
        daoUtil.executeQuery( );

        String message = null;
        if ( daoUtil.next( ) )
        {
            message = daoUtil.getString( 1 );
        }

        daoUtil.free( );

        return message;
    }
    
    /**
     * {@inheritDoc}
     */
    public String selectUserServiceFait( Integer idResource )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USER_SERVICE_FAIT );
        
        int nIndex = 1;
        daoUtil.setInt(nIndex++, idResource);
        
        daoUtil.executeQuery( );

        String user = null;
        if ( daoUtil.next( ) )
        {
            user = daoUtil.getString( 1 );
        }

        daoUtil.free( );

        return user;
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
    
    @Override
    public List<ResourceHistory> selectByResource( int nIdResource,
            String strResourceType, int nIdWorkflow )
    {
        ResourceHistory rh = new ResourceHistory();
        
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_HISTORY_BY_RESOURCES );
        int nIndex = 1;
        daoUtil.setInt(nIndex++, nIdResource);
        daoUtil.setString(nIndex++, strResourceType);
        daoUtil.setInt(nIndex++, nIdWorkflow);
        
        List<ResourceHistory> response = new ArrayList<ResourceHistory>();
        
        daoUtil.executeQuery();
                
        if(daoUtil.next()){
            nIndex = 1;
            rh.setId( daoUtil.getInt( nIndex++ ) );
            rh.setIdResource( daoUtil.getInt( nIndex++ ) );
            rh.setResourceType( daoUtil.getString( nIndex++ ) );
            
            Workflow wf = new Workflow();
            wf.setId( daoUtil.getInt( nIndex++ ) );
            rh.setWorkFlow( wf );
            
            Action action = new Action();
            action.setId( daoUtil.getInt( nIndex++ ) );            
            rh.setAction( action );
            
            rh.setCreationDate( daoUtil.getTimestamp( nIndex++ ) );
            rh.setUserAccessCode( daoUtil.getString( nIndex++ ) );
            
            response.add( rh );
        }
        
        daoUtil.free();
        
        return response;
    }

    @Override
    public Action findByPrimaryKey( int id )
    {
        Action action = new Action( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ACTION_BY_HISTORY );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, id );
        
        daoUtil.executeQuery();
                
        if(daoUtil.next()){
            nIndex = 1;
            
            action.setId( daoUtil.getInt( nIndex++ ) );
            action.setName( daoUtil.getString( nIndex++ ) );
            action.setDescription( daoUtil.getString( nIndex++ ) );
            
            Workflow wf = new Workflow();
            wf.setId( daoUtil.getInt( nIndex++ ) );
            action.setWorkflow( wf );
            
            State bf = new State();
            bf.setId( daoUtil.getInt( nIndex++ ) );
            action.setStateBefore( bf );
            
            State af = new State();
            bf.setId( daoUtil.getInt( nIndex++ ) );
            action.setStateAfter( bf );
            
            Icon ic = new Icon();
            ic.setId( daoUtil.getInt( nIndex++ ) );
            action.setIcon( ic );
            
            action.setAutomaticState( daoUtil.getBoolean( nIndex++ ) );
            action.setMassAction( daoUtil.getBoolean( nIndex++ ) ); 
            action.setOrder( daoUtil.getInt( nIndex++ ) );            
            action.setAutomaticReflexiveAction( daoUtil.getBoolean( nIndex++ ) );
            
        }
        
        daoUtil.free();
        
        return action;
    }

    @Override
    public List<NotificationSignalementUser3Contents> selectMessageServiceFaitPresta( List<String> listTaskPrestaServiceFait )
    {
        List<NotificationSignalementUser3Contents> messagesServiceFait = new ArrayList<>( );
        
        String[] tab = new String[listTaskPrestaServiceFait.size( )];
        for ( int i = 0; i < tab.length; i++ )
        {
            tab[i] = "?";
        }
        String listewhere = StringUtils.join( tab, "," );
        
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_MESSAGES_PRESTA + listewhere + ")" );
        
        int nIndex = 1;
        for ( String idTask : listTaskPrestaServiceFait )
        {
            daoUtil.setInt( nIndex++, Integer.parseInt( idTask ) );
        }

        daoUtil.executeQuery( );
        
        while ( daoUtil.next( ) )
        {
            nIndex = 1;
            NotificationSignalementUser3Contents message = new NotificationSignalementUser3Contents( );
            
            message.setIdTask( daoUtil.getInt( nIndex++ ) );
            message.setSubject( daoUtil.getString( nIndex++ ) );
            message.setSender( daoUtil.getString( nIndex++ ) );
            message.setMessage1( daoUtil.getString( nIndex++ ) );
            message.setMessage2( daoUtil.getString( nIndex++ ) );
            message.setSubjectRamen( daoUtil.getString( nIndex++ ) );
            message.setMessage1Ramen( daoUtil.getString( nIndex++ ) );
            message.setMessage2Ramen( daoUtil.getString( nIndex++ ) );
            message.setTitle1( daoUtil.getString( nIndex++) );
            message.setTitle2( daoUtil.getString( nIndex++ ) );
            
            messagesServiceFait.add( message );
        }

        daoUtil.free( );
        
        return messagesServiceFait;
    }
    
}
