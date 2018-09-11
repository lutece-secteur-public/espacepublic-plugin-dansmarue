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
package fr.paris.lutece.plugins.dansmarue.business.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class Adresse
{
    private Double      _lat;
    private Double      _lng;
    private Double      _latAddress;
    private Double      _lngAddress;
    private Long        _nId;
    private Signalement _signalement;
    private String      _strAdresse;
    private String      _strPrecisionLocalisation;

    public String getAdresse( )
    {
        return _strAdresse;
    }

    public Long getId( )
    {
        return _nId;
    }

    public Double getLat( )
    {
        return _lat;
    }

    public Double getLng( )
    {
        return _lng;
    }

    public Double getLatAddress( )
    {
        return _latAddress;
    }

    public Double getLngAddress( )
    {
        return _lngAddress;
    }

    public String getPrecisionLocalisation( )
    {
        return _strPrecisionLocalisation;
    }

    public Signalement getSignalement( )
    {
        return _signalement;
    }

    public void setAdresse( String adresse )
    {
        _strAdresse = adresse;
    }

    public void setId( Long id )
    {
        _nId = id;
    }

    public void setLat( Double lat )
    {
        _lat = lat;
    }

    public void setLng( Double lng )
    {
        _lng = lng;
    }

    public void setLatAddress( Double latAddress )
    {
        _latAddress = latAddress;
    }

    public void setLngAddress( Double lngAddress )
    {
        _lngAddress = lngAddress;
    }

    public void setPrecisionLocalisation( String precisionLocalisation )
    {
        _strPrecisionLocalisation = precisionLocalisation;
    }

    public void setSignalement( Signalement signalement )
    {
        _signalement = signalement;
    }

    public String getGoogleLink( )
    {
        if ( ( getLat( ) == null ) || ( getLng( ) == null ) || ( getAdresse( ) == null ) )
        {
            return getAdresse( );
        } else
        {
            String strLat = Double.toString( getLat( ) ).replaceAll( ",", "\\." );
            String strLng = Double.toString( getLng( ) ).replaceAll( ",", "\\." );
            String strAdr = getAdresse( ).replaceAll( "'", " " ).replaceAll( " ", "\\+" );
            return "https://maps.google.fr/?t=h&z=18&q=" + strLat + "," + strLng + "+(" + strAdr + ")";
        }
    }

}
