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
package fr.paris.lutece.plugins.dansmarue.validator;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.service.IArrondissementService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

public class ValidatorLocalisation
{

    public boolean validate( Adresse adresse )
    {

        Double dLat;
        Double dLng;
        try
        {
            dLat = adresse.getLat( );
            dLng = adresse.getLng( );
        } catch ( Exception e )
        {
            // in case of cast error of lat/lng fields
            dLat = null;
            dLng = null;
            AppLogService.error( e.getMessage( ), e );
        }
        return validate( dLat, dLng );
    }

    /**
     * Retrieves from the request, the coordinates, then check if inside Paris
     * 
     * @param request
     *            the HTTPServletRequest
     * @return true if inside Paris
     */
    public boolean validate( HttpServletRequest request )
    {
        Double dLat;
        Double dLng;
        try
        {
            dLat = Double.parseDouble( request.getParameter( SignalementConstants.FIELD_LAT ) );
            dLng = Double.parseDouble( request.getParameter( SignalementConstants.FIELD_LNG ) );
        } catch ( Exception e )
        {
            // in case of cast error of lat/lng fields
            dLat = null;
            dLng = null;
            AppLogService.error( e.getMessage( ), e );
        }
        return validate( dLat, dLng );
    }

    /**
     * Checks if coordinates are inside Paris
     * 
     * @param dLat
     *            the latitude
     * @param dLng
     *            the longitude
     * @return true if in Paris
     */
    public boolean validate( Double dLat, Double dLng )
    {
        return ( ( dLat != null && dLng != null ) && estDansParis( dLat, dLng ) );
    }

    /**
     * Is in Paris.
     * 
     * @param errorList
     *            the error list
     * @param dLat
     *            the lat
     * @param dLng
     *            the lng
     * @return true, if successful, coordinate are within paris.
     */
    private boolean estDansParis( Double dLat, Double dLng )
    {
        IArrondissementService arrondissementService = ( IArrondissementService ) SpringContextService.getBean( "signalement.arrondissementService" );
        boolean dansParis = false;
        if ( dLat == null || dLng == null )
        {
            dansParis = false;
        } else
        {
            Arrondissement arrondissementByGeom = arrondissementService.getArrondissementByGeom( dLng, dLat );
            dansParis = arrondissementByGeom != null;
        }

        return dansParis;
    }
}
