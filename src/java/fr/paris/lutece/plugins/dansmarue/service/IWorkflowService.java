package fr.paris.lutece.plugins.dansmarue.service;



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
}
