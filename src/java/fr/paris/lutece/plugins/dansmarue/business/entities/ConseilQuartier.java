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
import java.math.BigDecimal;

/**
 * This is the business class for the object neighborhood.
 */
public class ConseilQuartier implements Serializable
{



    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1628830962955366624L;

    /** The n id consqrt. */
    // Variables declarations
    private int _nIdConsqrt;

    /** The str numero consqrt. */
    private String _strNumeroConsqrt;

    /** The surface. */
    private BigDecimal _surface;

    /** The str nom consqrt. */
    private String _strNomConsqrt;

    /** The numero arrondissement. */
    private BigDecimal _numeroArrondissement;

    /**
     * Returns the IdConsqrt.
     *
     * @return The IdConsqrt
     */
    public int getIdConsqrt( )
    {
        return _nIdConsqrt;
    }

    /**
     * Sets the IdConsqrt.
     *
     * @param nIdConsqrt
     *            The IdConsqrt
     */
    public void setIdConsqrt( int nIdConsqrt )
    {
        _nIdConsqrt = nIdConsqrt;
    }

    /**
     * Returns the NumeroConsqrt.
     *
     * @return The NumeroConsqrt
     */
    public String getNumeroConsqrt( )
    {
        return _strNumeroConsqrt;
    }

    /**
     * Sets the NumeroConsqrt.
     *
     * @param strNumeroConsqrt
     *            The NumeroConsqrt
     */
    public void setNumeroConsqrt( String strNumeroConsqrt )
    {
        _strNumeroConsqrt = strNumeroConsqrt;
    }

    /**
     * Returns the Surface.
     *
     * @return The Surface
     */
    public BigDecimal getSurface( )
    {
        return _surface;
    }

    /**
     * Sets the Surface.
     *
     * @param surface
     *            The Surface
     */
    public void setSurface( BigDecimal surface )
    {
        _surface = surface;
    }

    /**
     * Returns the NomConsqrt.
     *
     * @return The NomConsqrt
     */
    public String getNomConsqrt( )
    {
        return _strNomConsqrt;
    }

    /**
     * Sets the NomConsqrt.
     *
     * @param strNomConsqrt
     *            The NomConsqrt
     */
    public void setNomConsqrt( String strNomConsqrt )
    {
        _strNomConsqrt = strNomConsqrt;
    }

    /**
     * Returns the NumeroArrondissement.
     *
     * @return The NumeroArrondissement
     */
    public BigDecimal getNumeroArrondissement( )
    {
        return _numeroArrondissement;
    }

    /**
     * Sets the NumeroArrondissement.
     *
     * @param numeroArrondissement
     *            The NumeroArrondissement
     */
    public void setNumeroArrondissement( BigDecimal numeroArrondissement )
    {
        _numeroArrondissement = numeroArrondissement;
    }
}
