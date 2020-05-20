/*
 * Copyright (c) 2002-2020, City of Paris
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


/**
 * The Class Adresse.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Adresse
{

    /** The lat. */
    private Double      _lat;

    /** The lng. */
    private Double      _lng;

    /** The lat address. */
    private Double      _latAddress;

    /** The lng address. */
    private Double      _lngAddress;

    /** The n id. */
    private Long        _nId;

    /** The signalement. */
    private Signalement _signalement;

    /** The str adresse. */
    private String      _strAdresse;

    /** The str precision localisation. */
    private String      _strPrecisionLocalisation;

    /**
     * Gets the adresse.
     *
     * @return the adresse
     */
    public String getAdresse( )
    {
        return _strAdresse;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId( )
    {
        return _nId;
    }

    /**
     * Gets the lat.
     *
     * @return the lat
     */
    public Double getLat( )
    {
        return _lat;
    }

    /**
     * Gets the lng.
     *
     * @return the lng
     */
    public Double getLng( )
    {
        return _lng;
    }

    /**
     * Gets the lat address.
     *
     * @return the lat address
     */
    public Double getLatAddress( )
    {
        return _latAddress;
    }

    /**
     * Gets the lng address.
     *
     * @return the lng address
     */
    public Double getLngAddress( )
    {
        return _lngAddress;
    }

    /**
     * Gets the precision localisation.
     *
     * @return the precision localisation
     */
    public String getPrecisionLocalisation( )
    {
        return _strPrecisionLocalisation;
    }

    /**
     * Gets the signalement.
     *
     * @return the signalement
     */
    public Signalement getSignalement( )
    {
        return _signalement;
    }

    /**
     * Sets the adresse.
     *
     * @param adresse the new adresse
     */
    public void setAdresse( String adresse )
    {
        _strAdresse = adresse;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId( Long id )
    {
        _nId = id;
    }

    /**
     * Sets the lat.
     *
     * @param lat the new lat
     */
    public void setLat( Double lat )
    {
        _lat = lat;
    }

    /**
     * Sets the lng.
     *
     * @param lng the new lng
     */
    public void setLng( Double lng )
    {
        _lng = lng;
    }

    /**
     * Sets the lat address.
     *
     * @param latAddress the new lat address
     */
    public void setLatAddress( Double latAddress )
    {
        _latAddress = latAddress;
    }

    /**
     * Sets the lng address.
     *
     * @param lngAddress the new lng address
     */
    public void setLngAddress( Double lngAddress )
    {
        _lngAddress = lngAddress;
    }

    /**
     * Sets the precision localisation.
     *
     * @param precisionLocalisation the new precision localisation
     */
    public void setPrecisionLocalisation( String precisionLocalisation )
    {
        _strPrecisionLocalisation = precisionLocalisation;
    }

    /**
     * Sets the signalement.
     *
     * @param signalement the new signalement
     */
    public void setSignalement( Signalement signalement )
    {
        _signalement = signalement;
    }

    /**
     * Gets the google link.
     *
     * @return the google link
     */
    public String getGoogleLink( )
    {
        if ( ( getLat( ) == null ) || ( getLng( ) == null ) || ( getAdresse( ) == null ) )
        {
            return getAdresse( );
        }
        else
        {
            String strLat = Double.toString( getLat( ) ).replace( ",", "\\." );
            String strLng = Double.toString( getLng( ) ).replace( ",", "\\." );
            String strAdr = getAdresse( ).replace( "'", " " ).replace( " ", "\\+" );
            return "https://maps.google.fr/?t=h&z=18&q=" + strLat + "," + strLng + "+(" + strAdr + ")";
        }
    }

}
