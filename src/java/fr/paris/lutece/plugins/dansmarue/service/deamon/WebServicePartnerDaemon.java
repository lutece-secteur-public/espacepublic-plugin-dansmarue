package fr.paris.lutece.plugins.dansmarue.service.deamon;

import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;

public class WebServicePartnerDaemon extends Daemon
{
    // Properties
    private static final String ID_STATE_ECHEC_WS           = "signalement.idStateEchecEnvoiWS";
    private static final String ID_STATE_SERVICE_FAIT       = "signalement.idStateServiceFait";
    // max anomalies traited by passage of daemon
    private static final String MAX_ANOMALIES_TRAITED       = "signalement.daemon.maxAnomalies.traited";

    // service
    private IWorkflowService    _signalementWorkflowService = SpringContextService.getBean( "signalement.workflowService" );

    // dao
    private ISignalementDAO     _signalementDAO             = SpringContextService.getBean( "signalementDAO" );

    @Override
    public void run( )
    {

        WorkflowService workflowService = WorkflowService.getInstance( );

        // 1) find the anomalies with the failure state
        int stateToFind = Integer.valueOf( AppPropertiesService.getProperty( ID_STATE_ECHEC_WS ) );
        List<Integer> lstIdsSignalementFound = _signalementDAO.findIdsSingalementForWSPartnerDeamon( stateToFind );

        AppLogService.info( lstIdsSignalementFound.size( ) + " anomalie(s) found with the failure send by ws" );

        // 2) call workflow to send anomalies to partner by WS.
        int max = Integer.valueOf( AppPropertiesService.getProperty( MAX_ANOMALIES_TRAITED ) );
        int idActionTransfert = Integer.valueOf( AppPropertiesService.getProperty( SignalementConstants.ID_ACTION_TRANSFERT_PARTNER ) );
        int idActionDone = Integer.valueOf( AppPropertiesService.getProperty( SignalementConstants.ID_ACTION_DAEMON_SIGNALEMENT_DONE ) );
        int stateServiceFait = Integer.valueOf( AppPropertiesService.getProperty( ID_STATE_SERVICE_FAIT ) );

        lstIdsSignalementFound.stream( ).limit( max ).forEach( idSignalement ->
        {
            ResourceHistory history = _signalementWorkflowService.getLastHistoryResource( idSignalement, Signalement.WORKFLOW_RESOURCE_TYPE, SignalementConstants.SIGNALEMENT_WORKFLOW_ID );

            if ( history.getAction( ).getStateAfter( ).getId( ) == stateServiceFait )
            {
                // if state after of the last anomalies is id 10 "service fait" call workflow action id 86 Demon Service fait
                AppLogService.info( "call action id 86 Demon Service fait for anomalie " + idSignalement );
                workflowService.doProcessAction( idSignalement, Signalement.WORKFLOW_RESOURCE_TYPE, idActionDone, null, null, Locale.FRANCE, true );

            } else
            {
                // else call action id 85 Transferer prestataire
                AppLogService.info( "call action id 85 Transferer prestataire for anomalie " + idSignalement );
                workflowService.doProcessAction( idSignalement, Signalement.WORKFLOW_RESOURCE_TYPE, idActionTransfert, null, null, Locale.FRANCE, true );
            }

        }

        );

    }

}
