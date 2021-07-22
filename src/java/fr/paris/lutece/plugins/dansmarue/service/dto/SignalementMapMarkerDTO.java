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
package fr.paris.lutece.plugins.dansmarue.service.dto;

/**
 * The Class SignalementMapMarkerDTO.
 */
public class SignalementMapMarkerDTO
{

    /** The Constant LINE_BREAK. */
    private static final String LINE_BREAK = "<br></br>";

    /** The id signalement. */
    private Long _idSignalement;

    /** The str tooltip. */
    private String _strTooltip;

    /** The lng. */
    private Double _lng;

    /** The lat. */
    private Double _lat;

    /** The str icon name. */
    private String _strIconName;

    /** The number color. **/
    private String _strColorNumber;

    /**
     * Gets the id signalement.
     *
     * @return the idSignalement
     */
    public Long getIdSignalement( )
    {
        return _idSignalement;
    }

    /**
     * Sets the id signalement.
     *
     * @param idSignalement
     *            the idSignalement to set
     */
    public void setIdSignalement( Long idSignalement )
    {
        _idSignalement = idSignalement;
    }

    /**
     * Gets the tooltip.
     *
     * @return the tooltip
     */
    public String getTooltip( )
    {
        return _strTooltip;
    }

    /**
     * Sets the tooltip.
     *
     * @param tooltip
     *            the new tooltip
     */
    public void setTooltip( String tooltip )
    {
        _strTooltip = tooltip;
    }

    /**
     * Adds the tooltip text.
     *
     * @param title
     *            the title
     * @param text
     *            the text
     */
    public void addTooltipText( String title, String text )
    {
        if ( _strTooltip == null )
        {
            _strTooltip = title + text;
        }
        else
        {
            _strTooltip += LINE_BREAK + title + text;
        }
    }

    public void addTooltipTextWithClass( String title, String text )
    {
        if ( _strTooltip == null )
        {
            _strTooltip = "<span class='tooltipMapTitle'>" + title + "</span>" + text;
        }
        else
        {
            _strTooltip += LINE_BREAK + "<span class='tooltipMapTitle'>" + title + "</span>" + text;
        }
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
     * Sets the lng.
     *
     * @param lng
     *            the lng to set
     */
    public void setLng( Double lng )
    {
        _lng = lng;
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
     * Sets the lat.
     *
     * @param lat
     *            the lat to set
     */
    public void setLat( Double lat )
    {
        _lat = lat;
    }

    /**
     * Gets the icon name.
     *
     * @return the icon name
     */
    public String getIconName( )
    {
        return _strIconName;
    }

    /**
     * Sets the icon name.
     *
     * @param iconName
     *            the new icon name
     */
    public void setIconName( String iconName )
    {
        _strIconName = iconName;
    }

    public String getColorNumber( )
    {
        return _strColorNumber;
    }

    public void setColorNumber( String colorNumber )
    {
        _strColorNumber = colorNumber;
    }

}
