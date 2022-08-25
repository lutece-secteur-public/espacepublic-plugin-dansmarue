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

import java.io.*;

/**
 * The Class Priorite.
 */
public class Priorite implements Serializable
{

    private static final long serialVersionUID = -4659583432830516262L;
    
    /** The id. */
    private Long _id;

    /** The str libelle. */
    private String _strLibelle;

    /** The n ordre priorite. */
    private Integer _nOrdrePriorite;

    /**
     * Gets the ordre priorite.
     *
     * @return the ordre priorite
     */
    public Integer getOrdrePriorite( )
    {
        return _nOrdrePriorite;
    }

    /**
     * Sets the ordre priorite.
     *
     * @param ordrePriorite
     *            the ordre priorite
     * @return the integer
     */
    public Integer setOrdrePriorite( Integer ordrePriorite )
    {
        _nOrdrePriorite = ordrePriorite;
        return _nOrdrePriorite;
    }

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
     * @param id
     *            the new id
     */
    public void setId( long id )
    {
        _id = id;
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
     * @param libelle
     *            the new libelle
     */
    public void setLibelle( String libelle )
    {
        _strLibelle = libelle;
    }

}
