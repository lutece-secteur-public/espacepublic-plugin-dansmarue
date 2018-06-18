package fr.paris.lutece.plugins.dansmarue.business.dao;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.NotificationSignalementUser3Contents;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.plugin.Plugin;


/**
 * 
 * ISignalementWorklowDAO
 * 
 */
public interface IWorkflowDAO
{
    /**
     * identifiant du workflow pour signalement
     * @param nId identifiant du workflow pour signalement
     * @param plugin le plugin
     */
    void updateWorkflowId( Integer nId, Plugin plugin );

    /**
     * identifiant du workflow pour signalement
     * @param plugin le plugin
     * @return identifiant du workflow pour signalement
     */
    Integer selectWorkflowId( Plugin plugin );
    
    /**
     * 
     * @param idStateBefore the state before processing his 
     * @param idStateAfter
     * @param plugin
     * @return The id of the action
     * 		   -1 if not found
     */
    int selectIdActionByStates( int idStateBefore, int idStateAfter, Plugin plugin );

    String selectMessageNotification( Integer idHistory );
    
    String selectUserServiceFait( Integer idResource );

    String select3ContentsMessageNotification( Integer idHistory );

    Action findByPrimaryKey( int id );

    List<ResourceHistory> selectByResource( int nIdResource,
            String strResourceType, int nIdWorkflow );

    List<NotificationSignalementUser3Contents> selectMessageServiceFaitPresta(
            List<String> listTaskPrestaServiceFait );

}
