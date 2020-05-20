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

import org.hibernate.validator.constraints.NotBlank;

/**
 * The Class Source.
 */
public class Source
{

    /** The Constant RESOURCE_TYPE. */
    public static final String RESOURCE_TYPE = "TYPE_SIGNALEMENT";

    /** The n id. */
    private Integer            _nId;

    /** The str libelle. */
    @NotBlank
    private String             _strLibelle;

    /** The str description. */
    private String             _strDescription;

    /** The str commentaire. */
    private String             _strCommentaire;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId( )
    {
        return _nId;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId( Integer id )
    {
        _nId = id;
    }

    /**
     * Gets the libelle.
     *
     * @return the libelle
     */
    public String getLibelle( )
    {
        return _strLibelle;
    }

    /**
     * Sets the libelle.
     *
     * @param libelle the new libelle
     */
    public void setLibelle( String libelle )
    {
        _strLibelle = libelle;
    }

    /**
     * Gets the commentaire.
     *
     * @return the commentaire
     */
    public String getCommentaire( )
    {
        return _strCommentaire;
    }

    /**
     * Sets the commentaire.
     *
     * @param commentaire the new commentaire
     */
    public void setCommentaire( String commentaire )
    {
        _strCommentaire = commentaire;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription( )
    {
        return _strDescription;
    }

    /**
     * Sets the description.
     *
     * @param description the new description
     */
    public void setDescription( String description )
    {
        _strDescription = description;
    }

}
