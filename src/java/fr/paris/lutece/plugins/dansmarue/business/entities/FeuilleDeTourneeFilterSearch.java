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
package fr.paris.lutece.plugins.dansmarue.business.entities;

import java.io.Serializable;

/**
 * The Class FeuilleDeTourneeFilterSearch.
 */
public class FeuilleDeTourneeFilterSearch implements Serializable
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6647285175541120803L;

    /** The n id. */
    private Integer _nId;

    /** The str nom. */
    private String _strNom;

    /** The str createur. */
    private String _strCreateur;

    /** The n idEntite. */
    private Integer _nIdEntite;

    /** The str commentaire. */
    private String _strCommentaire;

    /** The str date creation. */
    private String _strDate;

    /** The str valeur. */
    private String _strValeur;

    public Integer getId( )
    {
        return _nId;
    }

    public String getNom( )
    {
        return _strNom;
    }

    public String getCreateur( )
    {
        return _strCreateur;
    }

    public Integer getIdEntite( )
    {
        return _nIdEntite;
    }

    public String getCommentaire( )
    {
        return _strCommentaire;
    }

    public String getDate( )
    {
        return _strDate;
    }

    public String getValeur( )
    {
        return _strValeur;
    }

    public void setId( Integer id )
    {
        _nId = id;
    }

    public void setNom( String nom )
    {
        _strNom = nom;
    }

    public void setCreateur( String createur )
    {
        _strCreateur = createur;
    }

    public void setIdEntite( Integer idEntite )
    {
        _nIdEntite = idEntite;
    }

    public void setCommentaire( String commentaire )
    {
        _strCommentaire = commentaire;
    }

    public void setDate( String date )
    {
        _strDate = date;
    }

    public void setValeur( String valeur )
    {
        _strValeur = valeur;
    }

}
