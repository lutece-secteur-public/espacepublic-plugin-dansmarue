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
package fr.paris.lutece.plugins.dansmarue.business.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.commons.Order;

/**
 * The Class RoadMapFilter.
 */
public class RoadMapFilter implements Serializable
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The b afficher signalements du jour. */
    private Boolean _bAfficherSignalementsDuJour;

    /** The b order asc. */
    private boolean _bOrderAsc;

    /** The heure debut service. */
    private Date _heureDebutService;

    /** The n id sector. */
    private Integer _nIdSector;

    /** The n id service. */
    private Integer _nIdService;

    /** The order. */
    private Order _order;

    /** The str ddate. */
    private String _strDdate;

    /** The list id sector. */
    private List<Integer> _listIdSector;

    /**
     * Gets the afficher signalements du jour.
     *
     * @return the afficher signalements du jour
     */
    public Boolean getAfficherSignalementsDuJour( )
    {
        return _bAfficherSignalementsDuJour;
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public String getDate( )
    {
        return _strDdate;
    }

    /**
     * Gets the heure debut service.
     *
     * @return the heureDebutService
     */
    public Date getHeureDebutService( )
    {
        return _heureDebutService;
    }

    /**
     * Gets the id sector.
     *
     * @return the id sector
     */
    public Integer getIdSector( )
    {
        Integer ret;

        if ( _nIdSector != null )
        {
            ret = _nIdSector;
        }
        else
        {
            ret = -1;
        }

        return ret;
    }

    /**
     * Gets the id service.
     *
     * @return the id service
     */
    public Integer getIdService( )
    {
        Integer ret;

        if ( _nIdService != null )
        {
            ret = _nIdService;
        }
        else
        {
            ret = -1;
        }

        return ret;
    }

    /**
     * Gets the order.
     *
     * @return the order
     */
    public Order getOrder( )
    {
        return _order;
    }

    /**
     * Gets the id Unit.
     *
     * @return the id unit
     */
    public List<Integer> getListIdSector( )
    {
        List<Integer> ret;

        if ( _listIdSector != null )
        {
            ret = _listIdSector;
        }
        else
        {
            ret = new ArrayList<>( );
        }

        return ret;
    }

    /**
     * Checks if is order asc.
     *
     * @return true, if is order asc
     */
    public boolean isOrderAsc( )
    {
        return _bOrderAsc;
    }

    /**
     * Sets the afficher signalements du jour.
     *
     * @param afficherSignalementsDuJour
     *            the new afficher signalements du jour
     */
    public void setAfficherSignalementsDuJour( Boolean afficherSignalementsDuJour )
    {
        _bAfficherSignalementsDuJour = afficherSignalementsDuJour;
    }

    /**
     * Sets the date.
     *
     * @param date
     *            the new date
     */
    public void setDate( String date )
    {
        _strDdate = date;
    }

    /**
     * Sets the heure debut service.
     *
     * @param heureDebutService
     *            the heureDebutService to set
     */
    public void setHeureDebutService( Date heureDebutService )
    {
        _heureDebutService = heureDebutService;
    }

    /**
     * Sets the id sector.
     *
     * @param idRessource
     *            the new id sector
     */
    public void setIdSector( Integer idRessource )
    {
        _nIdSector = idRessource;
    }

    /**
     * Sets the id service.
     *
     * @param idService
     *            the new id service
     */
    public void setIdService( Integer idService )
    {
        _nIdService = idService;
    }

    /**
     * Sets the order.
     *
     * @param order
     *            the new order
     */
    public void setOrder( Order order )
    {
        _order = order;
    }

    /**
     * Sets the order asc.
     *
     * @param orderAsc
     *            the new order asc
     */
    public void setOrderAsc( boolean orderAsc )
    {
        _bOrderAsc = orderAsc;
    }

    /**
     * Sets the id unit.
     *
     * @param listIdSector
     *            list of sector ids to set
     */
    public void setListIdSector( List<Integer> listIdSector )
    {
        _listIdSector = listIdSector;
    }
}
