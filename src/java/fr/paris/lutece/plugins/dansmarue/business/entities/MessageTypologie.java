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

import org.hibernate.validator.constraints.NotBlank;

/**
 * The Class MessageTypologie.
 */
public class MessageTypologie
{

    /** The n id. */
    private Integer _nId;

    /** The n ordre. */
    private Integer _nOrdre;

    /** The str type message. */
    @NotBlank( message = "Le type de message est obligatoire." )
    private String _strTypeMessage;

    /** The str contenu message. */
    @NotBlank( message = "Le contenu du message est obligatoire." )
    private String _strContenuMessage;

    /** The n fk id type signalement. */
    private Integer _nFkIdTypeSignalement;

    /** The b actif. */
    private boolean _bActif;

    /**
     * Gets the ordre.
     *
     * @return the ordre
     */
    public Integer getOrdre( )
    {
        return _nOrdre;
    }

    /**
     * Sets the ordre.
     *
     * @param ordre
     *            the new ordre
     */
    public void setOrdre( Integer ordre )
    {
        _nOrdre = ordre;
    }

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
     * @param _nId
     *            the new id
     */
    public void setId( Integer _nId )
    {
        this._nId = _nId;
    }

    /**
     * Gets the type message.
     *
     * @return the type message
     */
    public String getTypeMessage( )
    {
        return _strTypeMessage;
    }

    /**
     * Sets the type message.
     *
     * @param _strTypeMessage
     *            the new type message
     */
    public void setTypeMessage( String _strTypeMessage )
    {
        this._strTypeMessage = _strTypeMessage;
    }

    /**
     * Gets the contenu message.
     *
     * @return the contenu message
     */
    public String getContenuMessage( )
    {
        return _strContenuMessage;
    }

    /**
     * Sets the contenu message.
     *
     * @param _strContenuMessage
     *            the new contenu message
     */
    public void setContenuMessage( String _strContenuMessage )
    {
        this._strContenuMessage = _strContenuMessage;
    }

    /**
     * Gets the fk id type signalement.
     *
     * @return the fk id type signalement
     */
    public Integer getFkIdTypeSignalement( )
    {
        return _nFkIdTypeSignalement;
    }

    /**
     * Sets the fk id type signalement.
     *
     * @param fkIdTypeSignalement
     *            the new fk id type signalement
     */
    public void setFkIdTypeSignalement( Integer fkIdTypeSignalement )
    {
        _nFkIdTypeSignalement = fkIdTypeSignalement;
    }

    /**
     * Gets the actif.
     *
     * @return the actif
     */
    public boolean getActif( )
    {
        return _bActif;
    }

    /**
     * Sets the actif.
     *
     * @param actif
     *            the new actif
     */
    public void setActif( boolean actif )
    {
        _bActif = actif;
    }

}
