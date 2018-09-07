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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import fr.paris.lutece.plugins.unittree.modules.dansmarue.business.sector.Sector;

@JsonIgnoreProperties( ignoreUnknown = true )
public class SignalementRequalification
{
    private Long            _nIdSignalement;
    private Integer         _nIdTypeSignalement;
    private String          _strAdresse;
    private Integer         _nIdSector;
    private String          _dateRequalification;
    private TypeSignalement _typeSignalement;
    private Sector          _sector;
    private Integer         _nIdTask;

    /**
     * @return the nIdSignalement
     */
    public Long getIdSignalement( )
    {
        return _nIdSignalement;
    }

    /**
     * @param nIdSignalement
     *            the nIdSignalement to set
     */
    public void setIdSignalement( Long nIdSignalement )
    {
        this._nIdSignalement = nIdSignalement;
    }

    /**
     * @return the _nIdTypeSignalement
     */
    public Integer getIdTypeSignalement( )
    {
        return _nIdTypeSignalement;
    }

    /**
     * @param nIdTypeSignalement
     *            the _nIdTypeSignalement to set
     */
    public void setIdTypeSignalement( Integer nIdTypeSignalement )
    {
        this._nIdTypeSignalement = nIdTypeSignalement;
    }

    /**
     * @return the _strAdresse
     */
    public String getAdresse( )
    {
        return _strAdresse;
    }

    /**
     * @param strAdresse
     *            the _strAdresse to set
     */
    public void setAdresse( String strAdresse )
    {
        this._strAdresse = strAdresse;
    }

    /**
     * @return the _nIdSector
     */
    public Integer getIdSector( )
    {
        return _nIdSector;
    }

    /**
     * @param nIdSector
     *            the _nIdSector to set
     */
    public void setIdSector( Integer nIdSector )
    {
        this._nIdSector = nIdSector;
    }

    /**
     * @return the _dateRequalification
     */
    public String getDateRequalification( )
    {
        return _dateRequalification;
    }

    /**
     * @param dateRequalification
     *            the _dateRequalification to set
     */
    public void setDateRequalification( String dateRequalification )
    {
        this._dateRequalification = dateRequalification;
    }

    /**
     * @return the _typeSignalement
     */
    public TypeSignalement getTypeSignalement( )
    {
        return _typeSignalement;
    }

    /**
     * @param typeSignalement
     *            the _typeSignalement to set
     */
    public void setTypeSignalement( TypeSignalement typeSignalement )
    {
        this._typeSignalement = typeSignalement;
    }

    /**
     * @return the _sector
     */
    public Sector getSector( )
    {
        return _sector;
    }

    /**
     * @param sector
     *            the _sector to set
     */
    public void setSector( Sector sector )
    {
        this._sector = sector;
    }

    /**
     * @return the _nIdTask
     */
    public Integer getIdTask( )
    {
        return _nIdTask;
    }

    /**
     * @param nIdTask
     *            the _nIdTask to set
     */
    public void setIdTask( Integer nIdTask )
    {
        this._nIdTask = nIdTask;
    }

}
