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

/**
 * The Class Signaleur.
 */
public class Signaleur
{

    /** The id. */
    private Long        _id;

    /** The str mail. */
    private String      _strMail;

    /** The signalement. */
    private Signalement _signalement;

    /** The str id telephone. */
    private String      _strIdTelephone;

    /** The str guid. */
    private String      _strGuid;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId( )
    {
        return _id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId( Long id )
    {
        _id = id;
    }

    /**
     * Gets the mail.
     *
     * @return the mail
     */
    public String getMail( )
    {
        return _strMail;
    }

    /**
     * Sets the mail.
     *
     * @param mail the new mail
     */
    public void setMail( String mail )
    {
        _strMail = mail;
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
     * Sets the signalement.
     *
     * @param signalement the new signalement
     */
    public void setSignalement( Signalement signalement )
    {
        _signalement = signalement;
    }

    /**
     * Gets the id telephone.
     *
     * @return the id telephone
     */
    public String getIdTelephone( )
    {
        return _strIdTelephone;
    }

    /**
     * Sets the id telephone.
     *
     * @param idTelephone the new id telephone
     */
    public void setIdTelephone( String idTelephone )
    {
        _strIdTelephone = idTelephone;
    }

    /**
     * Sets the guid.
     *
     * @param guid the new guid
     */
    public void setGuid( String guid )
    {
        _strGuid = guid;
    }

    /**
     * Gets the guid.
     *
     * @return the guid
     */
    public String getGuid( )
    {
        return _strGuid;
    }
}
