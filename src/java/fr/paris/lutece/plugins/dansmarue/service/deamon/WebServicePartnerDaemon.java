/*
 * Copyright (c) 2002-2018, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
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
    // maximum amount of anomalies treated by the daemon at once
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
