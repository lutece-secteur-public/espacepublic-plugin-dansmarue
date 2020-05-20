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
package fr.paris.lutece.plugins.dansmarue.service.dto;

import java.time.LocalDate;

/**
 * The Class DashboardSignalementDTO.
 */
public class DashboardSignalementDTO
{

    /** The n id signalement. */
    private Integer   _nIdSignalement;

    /** The creation date. */
    private LocalDate _creationDate;

    /** The n id status. */
    private Integer   _nIdStatus;

    /** The date prevue traitement. */
    private LocalDate _datePrevueTraitement;

    /** The date mise en surveillance. */
    private LocalDate _dateMiseEnSurveillance;

    /**
     * Gets the id signalement.
     *
     * @return the id signalement
     */
    public Integer getIdSignalement( )
    {
        return _nIdSignalement;
    }

    /**
     * Sets the id signalement.
     *
     * @param idSignalement the new id signalement
     */
    public void setIdSignalement( Integer idSignalement )
    {
        _nIdSignalement = idSignalement;
    }

    /**
     * Gets the creation date.
     *
     * @return the creation date
     */
    public LocalDate getCreationDate( )
    {
        return _creationDate;
    }

    /**
     * Sets the creation date.
     *
     * @param creationDate the new creation date
     */
    public void setCreationDate( LocalDate creationDate )
    {
        _creationDate = creationDate;
    }

    /**
     * Gets the id status.
     *
     * @return the id status
     */
    public Integer getIdStatus( )
    {
        return _nIdStatus;
    }

    /**
     * Sets the id status.
     *
     * @param idStatus the new id status
     */
    public void setIdStatus( Integer idStatus )
    {
        _nIdStatus = idStatus;
    }

    /**
     * Gets the date prevue traitement.
     *
     * @return the date prevue traitement
     */
    public LocalDate getDatePrevueTraitement( )
    {
        return _datePrevueTraitement;
    }

    /**
     * Sets the date prevue traitement.
     *
     * @param datePrevueTraitement the new date prevue traitement
     */
    public void setDatePrevueTraitement( LocalDate datePrevueTraitement )
    {
        _datePrevueTraitement = datePrevueTraitement;
    }

    /**
     * Gets the date mise en surveillance.
     *
     * @return the date mise en surveillance
     */
    public LocalDate getDateMiseEnSurveillance( )
    {
        return _dateMiseEnSurveillance;
    }

    /**
     * Sets the date mise en surveillance.
     *
     * @param dateMiseEnSurveillance the new date mise en surveillance
     */
    public void setDateMiseEnSurveillance( LocalDate dateMiseEnSurveillance )
    {
        _dateMiseEnSurveillance = dateMiseEnSurveillance;
    }

}
