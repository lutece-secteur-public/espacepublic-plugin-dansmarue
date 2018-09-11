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
package fr.paris.lutece.plugins.dansmarue.service.dto;

public class SignalementMapMarkerDTO
{

    private static final String LINE_BREAK = "<br></br>";
    private Long                _idSignalement;
    private String              _strTooltip;
    private Double              _lng;
    private Double              _lat;
    private String              _strIconName;

    /**
     * @return the idSignalement
     */
    public Long getIdSignalement( )
    {
        return _idSignalement;
    }

    /**
     * @param idSignalement
     *            the idSignalement to set
     */
    public void setIdSignalement( Long idSignalement )
    {
        this._idSignalement = idSignalement;
    }

    public String getTooltip( )
    {
        return _strTooltip;
    }

    public void setTooltip( String tooltip )
    {
        this._strTooltip = tooltip;
    }

    public void addTooltipText( String title, String text )
    {
        if ( _strTooltip == null )
        {
            _strTooltip = title + text;
        } else
        {
            _strTooltip += LINE_BREAK + title + text;
        }
    }

    /**
     * @return the lng
     */
    public Double getLng( )
    {
        return _lng;
    }

    /**
     * @param lng
     *            the lng to set
     */
    public void setLng( Double lng )
    {
        this._lng = lng;
    }

    /**
     * @return the lat
     */
    public Double getLat( )
    {
        return _lat;
    }

    /**
     * @param lat
     *            the lat to set
     */
    public void setLat( Double lat )
    {
        this._lat = lat;
    }

    public String getIconName( )
    {
        return _strIconName;
    }

    public void setIconName( String iconName )
    {
        this._strIconName = iconName;
    }

}
