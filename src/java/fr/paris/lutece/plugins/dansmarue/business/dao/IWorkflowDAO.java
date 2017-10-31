package fr.paris.lutece.plugins.dansmarue.business.dao;

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

}
