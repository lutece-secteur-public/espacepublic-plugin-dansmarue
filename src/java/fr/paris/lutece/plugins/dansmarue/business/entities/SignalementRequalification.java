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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import fr.paris.lutece.plugins.unittree.modules.dansmarue.business.sector.Sector;

/**
 * The Class SignalementRequalification.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class SignalementRequalification
{

    /** The n id signalement. */
    private Long _nIdSignalement;

    /** The n id type signalement. */
    private Integer _nIdTypeSignalement;

    /** The str adresse. */
    private String _strAdresse;

    /** The n id sector. */
    private Integer _nIdSector;

    /** The date requalification. */
    private String _dateRequalification;

    /** The type signalement. */
    private TypeSignalement _typeSignalement;

    /** The sector. */
    private Sector _sector;

    /** The n id task. */
    private Integer _nIdTask;

    /** The str commentaire agent terrain. */
    private String _strCommentaireAgentTerrain;

    /**
     * Gets the id signalement.
     *
     * @return the nIdSignalement
     */
    public Long getIdSignalement( )
    {
        return _nIdSignalement;
    }

    /**
     * Sets the id signalement.
     *
     * @param nIdSignalement
     *            the nIdSignalement to set
     */
    public void setIdSignalement( Long nIdSignalement )
    {
        _nIdSignalement = nIdSignalement;
    }

    /**
     * Gets the id type signalement.
     *
     * @return the _nIdTypeSignalement
     */
    public Integer getIdTypeSignalement( )
    {
        return _nIdTypeSignalement;
    }

    /**
     * Sets the id type signalement.
     *
     * @param nIdTypeSignalement
     *            the _nIdTypeSignalement to set
     */
    public void setIdTypeSignalement( Integer nIdTypeSignalement )
    {
        _nIdTypeSignalement = nIdTypeSignalement;
    }

    /**
     * Gets the adresse.
     *
     * @return the _strAdresse
     */
    public String getAdresse( )
    {
        return _strAdresse;
    }

    /**
     * Sets the adresse.
     *
     * @param strAdresse
     *            the _strAdresse to set
     */
    public void setAdresse( String strAdresse )
    {
        _strAdresse = strAdresse;
    }

    /**
     * Gets the id sector.
     *
     * @return the _nIdSector
     */
    public Integer getIdSector( )
    {
        return _nIdSector;
    }

    /**
     * Sets the id sector.
     *
     * @param nIdSector
     *            the _nIdSector to set
     */
    public void setIdSector( Integer nIdSector )
    {
        _nIdSector = nIdSector;
    }

    /**
     * Gets the date requalification.
     *
     * @return the _dateRequalification
     */
    public String getDateRequalification( )
    {
        return _dateRequalification;
    }

    /**
     * Sets the date requalification.
     *
     * @param dateRequalification
     *            the _dateRequalification to set
     */
    public void setDateRequalification( String dateRequalification )
    {
        _dateRequalification = dateRequalification;
    }

    /**
     * Gets the type signalement.
     *
     * @return the _typeSignalement
     */
    public TypeSignalement getTypeSignalement( )
    {
        return _typeSignalement;
    }

    /**
     * Sets the type signalement.
     *
     * @param typeSignalement
     *            the _typeSignalement to set
     */
    public void setTypeSignalement( TypeSignalement typeSignalement )
    {
        _typeSignalement = typeSignalement;
    }

    /**
     * Gets the sector.
     *
     * @return the _sector
     */
    public Sector getSector( )
    {
        return _sector;
    }

    /**
     * Sets the sector.
     *
     * @param sector
     *            the _sector to set
     */
    public void setSector( Sector sector )
    {
        _sector = sector;
    }

    /**
     * Gets the id task.
     *
     * @return the _nIdTask
     */
    public Integer getIdTask( )
    {
        return _nIdTask;
    }

    /**
     * Sets the id task.
     *
     * @param nIdTask
     *            the _nIdTask to set
     */
    public void setIdTask( Integer nIdTask )
    {
        _nIdTask = nIdTask;
    }

    /**
     * Gets the commentaire agent terrain.
     *
     * @return the commentaire agent terrain
     */
    public String getCommentaireAgentTerrain( )
    {
        return _strCommentaireAgentTerrain;
    }

    /**
     * Sets the commentaire agent terrain.
     *
     * @param strCommentaireAgentTerrain
     *            the new commentaire agent terrain
     */
    public void setCommentaireAgentTerrain( String strCommentaireAgentTerrain )
    {
        _strCommentaireAgentTerrain = strCommentaireAgentTerrain;
    }

}
