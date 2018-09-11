/*
 * Copyright (c) 2002-2018, Mairie de Paris
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

public class DashboardPeriod
{
    private Long    _id;
    private String  _strLibelle;
    private Integer _nLowerBound;
    private Integer _nHigherBound;
    private String  _strUnit;
    private String  _strCategory;
    private Integer _nOrdre;

    /**
     * Getter for the id
     * 
     * @return the id
     */
    public Long getId( )
    {
        return _id;
    }

    /**
     * Setter for the id
     * 
     * @param id
     *            the id to set
     */
    public void setId( Long id )
    {
        this._id = id;
    }

    /**
     * Getter for the libelle
     * 
     * @return the libelle
     */
    public String getLibelle( )
    {
        return _strLibelle;
    }

    /**
     * Setter for the libelle
     * 
     * @param libelle
     *            the libelle to set
     */
    public void setLibelle( String libelle )
    {
        this._strLibelle = libelle;
    }

    /**
     * Getter for the lowerBound
     * 
     * @return the lowerBound
     */
    public Integer getLowerBound( )
    {
        return _nLowerBound;
    }

    /**
     * Setter for the lowerBound
     * 
     * @param lowerBound
     *            the lowerBound to set
     */
    public void setLowerBound( Integer lowerBound )
    {
        this._nLowerBound = lowerBound;
    }

    /**
     * Getter for the higherBound
     * 
     * @return the higherBound
     */
    public Integer getHigherBound( )
    {
        return _nHigherBound;
    }

    /**
     * Setter for the higherBound
     * 
     * @param higherBound
     *            the higherBound to set
     */
    public void setHigherBound( Integer higherBound )
    {
        this._nHigherBound = higherBound;
    }

    /**
     * Getter for the unit
     * 
     * @return the unit
     */
    public String getUnit( )
    {
        return _strUnit;
    }

    /**
     * Setter for the unit
     * 
     * @param unit
     *            the unit to set
     */
    public void setUnit( String unit )
    {
        this._strUnit = unit;
    }

    /**
     * Getter for the category
     * 
     * @return the category
     */
    public String getCategory( )
    {
        return _strCategory;
    }

    /**
     * Setter for the category
     * 
     * @param category
     *            the category to set
     */
    public void setCategory( String category )
    {
        this._strCategory = category;
    }

    /**
     * Getter for the order
     * 
     * @return the order
     */
    public Integer getOrdre( )
    {
        return _nOrdre;
    }

    /**
     * Setter for the order
     * 
     * @param ordre
     *            the order to set
     */
    public void setOrdre( Integer ordre )
    {
        this._nOrdre = ordre;
    }

}
