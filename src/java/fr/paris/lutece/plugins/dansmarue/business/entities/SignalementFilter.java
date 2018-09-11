/*
 * Copyright (c) 2002-2011, Mairie de Paris
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
import java.util.Collections;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.commons.Order;

public class SignalementFilter implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private List<Integer>         _listIdTypeSignalements;
    private int                   _nIdDomaine;
    private int                   _nIdTypeSignalement;
    private String                _strNumero;
    private int                   _nIdDirection;
    private List<Integer>         _listIdArrondissements;
    private int                   _nIdArrondissement;
    private int                   _nIdSector;
    private String                _strAdresse;
    private String                _strMail;
    private String                _strCommentaire;
    private String                _strDateBegin;
    private String                _strDateEnd;
    private int                   _nIdEtat;
    private List<EtatSignalement> _listEtats;
    private List<Integer>         _listIdSecteurAutorises;
    private List<Integer>         _listIdUnit;
    private List<Integer>         _listIdCategories;
    private List<Integer>         _listIdQuartier;

    private boolean               _bOrderAsc;
    private List<Order>           _listOrders      = new ArrayList<>( );

    /**
     * Init empty list for report filter to avoid Null Pointer Exception.
     */
    public void initEmptyList( )
    {
        _listIdTypeSignalements = Collections.emptyList( );
        _listIdArrondissements = Collections.emptyList( );
        _listEtats = Collections.emptyList( );
        _listIdSecteurAutorises = Collections.emptyList( );
        _listIdUnit = Collections.emptyList( );
        _listIdCategories = Collections.emptyList( );
        _listIdQuartier = Collections.emptyList( );
        _listOrders = Collections.emptyList( );
    }

    public List<EtatSignalement> getEtats( )
    {
        return _listEtats;
    }

    public void setEtats( List<EtatSignalement> etats )
    {
        _listEtats = etats;
    }

    public List<Integer> getListIdSecteurAutorises( )
    {
        return _listIdSecteurAutorises;
    }

    public void setListIdSecteurAutorises( List<Integer> listIdSecteurAutorises )
    {
        _listIdSecteurAutorises = listIdSecteurAutorises;
    }

    /**
     * get Set the order to ascending or descending
     * 
     * @param bOrderAsc
     *            the order (true if ascending)
     */
    public void setOrderAsc( boolean bOrderAsc )
    {
        _bOrderAsc = bOrderAsc;
    }

    /**
     * Get the order column name
     * 
     * @return the column name
     */
    public List<Order> getOrders( )
    {
        return _listOrders;
    }

    /**
     * Set the column to order
     * 
     * @param orders
     *            the column name
     */
    public void setOrders( List<Order> orders )
    {
        _listOrders = orders;
    }

    public int getIdTypeSignalement( )
    {
        return _nIdTypeSignalement;
    }

    public void setIdTypeSignalement( int nIdTypeSignalement )
    {
        _nIdTypeSignalement = nIdTypeSignalement;
    }

    /**
     * @return the idTypeSignalement
     */
    public List<Integer> getListIdTypeSignalements( )
    {
        return _listIdTypeSignalements;
    }

    /**
     * @param listIdTypeSignalements
     *            the list of reporting type ids to set
     */
    public void setListIdTypeSignalements( List<Integer> listIdTypeSignalements )
    {
        _listIdTypeSignalements = listIdTypeSignalements;
    }

    /**
     * @return the idDirection
     */
    public int getIdDirection( )
    {
        return _nIdDirection;
    }

    /**
     * @param pIdDirection
     *            the idDirection to set
     */
    public void setIdDirection( int pIdDirection )
    {
        _nIdDirection = pIdDirection;
    }

    /**
     * @return the address
     */
    public String getAdresse( )
    {
        return _strAdresse;
    }

    /**
     * @param pAdresse
     *            the address to set
     */
    public void setAdresse( String pAdresse )
    {
        _strAdresse = pAdresse;
    }

    /**
     * @return the mail
     */
    public String getMail( )
    {
        return _strMail;
    }

    /**
     * @param pMail
     *            the mail to set
     */
    public void setMail( String pMail )
    {
        _strMail = pMail;
    }

    /**
     * @return the commentary
     */
    public String getCommentaire( )
    {
        return _strCommentaire;
    }

    /**
     * @param pCommentaire
     *            the commentary to set
     */
    public void setCommentaire( String pCommentaire )
    {
        _strCommentaire = pCommentaire;
    }

    /**
     * @return the DateBegin
     */
    public String getDateBegin( )
    {
        return _strDateBegin;
    }

    /**
     * @param pDateBegin
     *            the DateBegin to set
     */
    public void setDateBegin( String pDateBegin )
    {
        _strDateBegin = pDateBegin;
    }

    /**
     * @return the DateEnd
     */
    public String getDateEnd( )
    {
        return _strDateEnd;
    }

    /**
     * @param pDateEnd
     *            the DateEnd to set
     */
    public void setDateEnd( String pDateEnd )
    {
        _strDateEnd = pDateEnd;
    }

    /**
     * @return the _bOrderAsc
     */
    public boolean isOrderAsc( )
    {
        return _bOrderAsc;
    }

    /**
     * @return the number
     */
    public String getNumero( )
    {
        return _strNumero;
    }

    /**
     * @param pNumero
     *            the number to set
     */
    public void setNumero( String pNumero )
    {
        _strNumero = pNumero;
    }

    /**
     * @return the idEtat
     */
    public int getIdEtat( )
    {
        return _nIdEtat;
    }

    /**
     * @param pIdEtat
     *            the idEtat to set
     */
    public void setIdEtat( int pIdEtat )
    {
        _nIdEtat = pIdEtat;
    }

    public int getIdArrondissement( )
    {
        return _nIdArrondissement;
    }

    public void setIdArrondissement( int nIdArrondissement )
    {
        _nIdArrondissement = nIdArrondissement;
    }

    public List<Integer> getListIdArrondissements( )
    {
        return _listIdArrondissements;
    }

    public void setListIdArrondissements( List<Integer> listIdArrondissements )
    {
        _listIdArrondissements = listIdArrondissements;
    }

    public int getIdSector( )
    {
        return _nIdSector;
    }

    public void setIdSector( int idSector )
    {
        _nIdSector = idSector;
    }

    public List<Integer> getListIdCategories( )
    {
        return _listIdCategories;
    }

    public void setListIdCategories( List<Integer> listIdCategories )
    {
        this._listIdCategories = listIdCategories;
    }

    public void setIdDomaine( int nIdDomaine )
    {
        _nIdDomaine = nIdDomaine;
    }

    public int getIdDomaine( )
    {
        return _nIdDomaine;
    }

    public List<Integer> getListIdQuartier( )
    {
        return _listIdQuartier;
    }

    public void setListIdQuartier( List<Integer> listIdQuartier )
    {
        this._listIdQuartier = listIdQuartier;
    }

    public List<Integer> getListIdUnit( )
    {
        return _listIdUnit;
    }

    public void setListIdUnit( List<Integer> listIdUnit )
    {
        this._listIdUnit = listIdUnit;
    }

}