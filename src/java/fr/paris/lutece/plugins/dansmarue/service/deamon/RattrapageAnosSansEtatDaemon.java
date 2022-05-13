/*
 * Copyright (c) 2002-2022, City of Paris
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

import org.apache.commons.lang.time.StopWatch;

import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * The Class RattrapageAnosSansEtatDaemon.
 */
public class RattrapageAnosSansEtatDaemon extends Daemon
{

    /** The signalement service. */
    // service
    private ISignalementService _signalementService = SpringContextService.getBean( "signalementService" );

    /**
     * Run.
     */
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run( )
    {
        StopWatch watch = new StopWatch( );
        watch.start( );
        // Get the anomalies id without state - limit to 50
        List<Long> listAnomaliesId = _signalementService.findAnoWithoutState( );

        // Delete this anomalies
        if ( !listAnomaliesId.isEmpty( ) )
        {
            listAnomaliesId.forEach( idAnomalie -> _signalementService.doDeleteSignalement( idAnomalie ) );
        }

        watch.stop( );
        AppLogService.info( "Deamon rattrapageAnosSansEtatDaemon -  " + listAnomaliesId.size( ) + " anomalies supprimées: " );
        AppLogService.info( "Deamon rattrapageAnosSansEtatDaemon - Durée d'éxécution (ms): " + watch.getTime( ) );

        setLastRunLogs( "Nombre d'anomalies supprimées: " + listAnomaliesId.size( ) );
    }

}
