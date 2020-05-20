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

import fr.paris.lutece.plugins.unittree.business.unit.Unit;

/**
 * The Class UnitWithDepth.
 */
public class UnitWithDepth
{

    /** The n id unit. */
    private int    _nIdUnit;

    /** The n id parent. */
    private int    _nIdParent;

    /** The str label. */
    private String _strLabel;

    /** The str description. */
    private String _strDescription;

    /** The n depth. */
    private int    _nDepth;

    /**
     * Get the id unit.
     *
     * @return the id unit
     */
    public int getIdUnit( )
    {
        return _nIdUnit;
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
     * @param _nDepth the new depth
     */
    public void setDepth( int _nDepth )
    {
        this._nDepth = _nDepth;
    }

    /**
     * Set the id unit.
     *
     * @param nIdUnit            the id unit
     */
    public void setIdUnit( int nIdUnit )
    {
        _nIdUnit = nIdUnit;
    }

    /**
     * Get the label.
     *
     * @return the label
     */
    public String getLabel( )
    {
        return _strLabel;
    }

    /**
     * Set the label.
     *
     * @param strLabel            the label
     */
    public void setLabel( String strLabel )
    {
        _strLabel = strLabel;
    }

    /**
     * Get the description.
     *
     * @return the description
     */
    public String getDescription( )
    {
        return _strDescription;
    }

    /**
     * Set the description.
     *
     * @param strDescription            the description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Set the id parent.
     *
     * @param nIdParent            the nIdParent to set
     */
    public void setIdParent( int nIdParent )
    {
        _nIdParent = nIdParent;
    }

    /**
     * Get the id parent.
     *
     * @return the nIdParent
     */
    public int getIdParent( )
    {
        return _nIdParent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode( )
    {
        return _nIdUnit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass( ) != obj.getClass( ) )
        {
            return false;
        }

        Unit unit = ( Unit ) obj;
        return _nIdUnit == unit.getIdUnit( );
    }
}
