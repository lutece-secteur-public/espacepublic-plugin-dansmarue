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
 * The Class TypeSignalementWithDepth.
 */
public class TypeSignalementWithDepth
{

    /** The Constant RESOURCE_TYPE. */
    public static final String RESOURCE_TYPE = "TYPE_SIGNALEMENT";

    /** The n id. */
    private Integer            _nId;

    /** The n id parent. */
    private Integer            _nIdParent;

    /** The str libelle. */
    private String             _strLibelle;

    /** The n depth. */
    private Integer            _nDepth;

    /** The n id unit. */
    private Integer            _nIdUnit;

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
     * Gets the depth.
     *
     * @return the depth
     */
    public int getDepth( )
    {
        return _nDepth;
    }

    /**
     * Sets the depth.
     *
     * @param nDepth the new depth
     */
    public void setDepth( int nDepth )
    {
        _nDepth = nDepth;
    }

    /**
     * Gets the id parent.
     *
     * @return the id parent
     */
    public Integer getIdParent( )
    {
        return _nIdParent;
    }

    /**
     * Sets the id parent.
     *
     * @param nIdParent the new id parent
     */
    public void setIdParent( Integer nIdParent )
    {
        _nIdParent = nIdParent;
    }

    /**
     * Gets the id unit.
     *
     * @return the id unit
     */
    public Integer getIdUnit( )
    {
        return _nIdUnit;
    }

    /**
     * Sets the id unit.
     *
     * @param nIdUnit the new id unit
     */
    public void setIdUnit( Integer nIdUnit )
    {
        _nIdUnit = nIdUnit;
    }
}
