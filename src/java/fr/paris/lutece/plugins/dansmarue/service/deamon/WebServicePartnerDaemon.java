package fr.paris.lutece.plugins.dansmarue.service.deamon;

import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;

public class WebServicePartnerDaemon extends Daemon
{
    // Properties
    private static final String ID_STATE_ECHEC_WS = "signalement.idStateEchecEnvoiWS";
    //max anomalies traited by passage of daemon
    private static final String MAX_ANOMALIES_TRAITED = "signalement.daemon.maxAnomalies.traited";
    
    // dao
    private ISignalementDAO     _signalementDAO   = SpringContextService.getBean( "signalementDAO" );

    @Override
    public void run( )
    {

        WorkflowService workflowService = WorkflowService.getInstance( );
        
        // 1) find the anomalies with the failure state
        int stateToFind = Integer.valueOf( AppPropertiesService.getProperty( ID_STATE_ECHEC_WS ) );
        List<Integer> lstIdsSignalementFound = _signalementDAO.findIdsSingalementForWSPartnerDeamon( stateToFind );

        AppLogService.info( lstIdsSignalementFound.size( ) + " anomalie(s) found with the failure send by ws" );

        // 2) call workflow to send anomalies to partner by WS.
        int max =  Integer.valueOf( AppPropertiesService.getProperty( MAX_ANOMALIES_TRAITED ) );
        int idAction = Integer.valueOf( AppPropertiesService.getProperty( SignalementConstants.ID_ACTION_TRANSFERT_PARTNER ) );
        
        lstIdsSignalementFound.stream( ).limit( max ).forEach( idSignalement ->
          {
            workflowService.doProcessAction( idSignalement, Signalement.WORKFLOW_RESOURCE_TYPE, 
                    idAction, null, null, Locale.FRANCE, true );
          }

        );

    }

}
