package fr.paris.lutece.plugins.dansmarue.service;

import java.util.List;
import java.util.Map;

import fr.paris.lutece.plugins.dansmarue.business.entities.NotificationSignalementUserMultiContents;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;

/**
 * 
 * IWorkflowService
 * 
 */
public interface IWorkflowService
{
    /**
     * L'identifiant du workflow utilisé par Signalement
     * @return l'identifiant si un workflow est associé, <code>null</code> sinon
     */
    Integer getSignalementWorkflowId( );

    /**
     * Identifiant du workflow
     * @param nIdWorkflow l'identifiant du workflow
     */
    void setSignalementWorkflowId( Integer nIdWorkflow );

    /**
     * 
     * @param idStateBefore the state before processing his 
     * @param idStateAfter
     * @return The id of the action
     * 		   -1 if not found
     */
	int selectIdActionByStates(int idStateBefore, int idStateAfter);

    String selectMessageNotification( Integer idHistory );

    String selectUserServiceFait( Integer idResource );

    String selectMultiContentsMessageNotification( Integer idHistory );

    List<ResourceHistory> getAllHistoryByResource( int nIdResource,
            String strResourceType, int nIdWorkflow );

    ResourceHistory getLastHistoryResource( int nIdResource,
            String strResourceType, int nIdWorkflow );

    Map<Integer, List<NotificationSignalementUserMultiContents>> selectMessageServiceFaitPresta( String strIdAction );
}
