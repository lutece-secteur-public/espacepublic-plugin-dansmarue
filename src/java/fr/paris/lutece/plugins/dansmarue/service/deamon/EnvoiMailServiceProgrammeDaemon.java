/*
 * Copyright (c) 2002-2021, City of Paris
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

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;

/**
 * The Class EnvoiMailServiceProgrammeDaemon.
 */
public class EnvoiMailServiceProgrammeDaemon extends Daemon
{

    /** The signalement service. */
    // service
    private ISignalementService _signalementService = SpringContextService.getBean( "signalementService" );

    /** The workflow service. */
    WorkflowService _workflowService = WorkflowService.getInstance( );

    /** The id action notif abonne service programme. */
    private int _idActionNotifAbonneServiceProgramme = Integer
            .parseInt( AppPropertiesService.getProperty( SignalementConstants.ID_ACTION_NOTIFICATION_ABONNE_SERVICE_PROGRAMME, "93" ) );

    /** The id action notif abonne service programme tiers. */
    private int _idActionNotifAbonneServiceProgrammeTiers = Integer
            .parseInt( AppPropertiesService.getProperty( SignalementConstants.ID_ACTION_NOTIFICATION_ABONNE_SERVICE_PROGRAMME_TIERS, "94" ) );

    /**
     * Deamon permettant d'envoyer un mail aux abonnées lorsque la date de service programmée est celle du jour.
     */
    @Override
    public void run( )
    {
        /* Service programmé */
        // Récupération des signalements ayant une date de programmation de service fait aujourd'hui et etant au statut Service programmé
        List<Integer> signalementProgrammeIds = _signalementService.getSignalementsServiceProgrammeIds( );

        // Pour chaque signalement, envoi d'un mails aux abonnés
        if ( !signalementProgrammeIds.isEmpty( ) )
        {
            signalementProgrammeIds.forEach( id -> _workflowService.doProcessAction( id, Signalement.WORKFLOW_RESOURCE_TYPE,
                    _idActionNotifAbonneServiceProgramme, null, null, Locale.FRANCE, true ) );
        }

        /* Service programmé tiers */
        // Récupération des signalements ayant une date de programmation de service fait aujourd'hui et etant au statut Service programmé tiers
        List<Integer> signalementProgrammeTiersIds = _signalementService.getSignalementsServiceProgrammeTierIds( );

        // Pour chaque signalement, envoi d'un mails aux abonnés
        if ( !signalementProgrammeTiersIds.isEmpty( ) )
        {
            signalementProgrammeTiersIds.forEach( id -> _workflowService.doProcessAction( id, Signalement.WORKFLOW_RESOURCE_TYPE,
                    _idActionNotifAbonneServiceProgrammeTiers, null, null, Locale.FRANCE, true ) );
        }

    }

}
