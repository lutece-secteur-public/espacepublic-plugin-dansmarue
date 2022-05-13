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

import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.service.IAdresseService;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * The Class RattrapageAdressesDaemon.
 */
public class RattrapageAdressesDaemon extends Daemon
{

    /** The adresse service. */
    // service
    private IAdresseService _adresseService = SpringContextService.getBean( "adresseSignalementService" );

    private static final String SITE_LABEL_NB_TOUR = "sitelabels.site_property.demon.rattrapage.adresse.nb.tour";

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
        int nbTours = Integer.parseInt( DatastoreService.getDataValue( SITE_LABEL_NB_TOUR, "1" ) );
        int compteur = 0;
        int nbAddressToProcess = 5;

        while ( ( compteur < nbTours ) && ( nbAddressToProcess == 5 ) )
        {
            // 1) find 5 anomalies with bad adresse
            List<Adresse> listWrongAdresses = _adresseService.findWrongAdresses( );
            nbAddressToProcess = listWrongAdresses.size( );

            // 2) call WS openstreetmap for reverse geocoding
            listWrongAdresses.forEach( adresse -> adresse.setAdresse( _adresseService.getAdresseByPosition( adresse ) ) );

            // 3) Update adresses
            listWrongAdresses.forEach( adresse -> _adresseService.updateAdresse( adresse ) );

            // 4) Rattrapages d'adresses avec des cas particuliers (DMR-1433) :
            // - adresses avec E-Arrondissement
            // - adresses en Parigi pour la ville
            // - adresses qui n'ont pas de virgule avant le code postale
            _adresseService.fixAdresses( );

            compteur++;
        }

    }

}
