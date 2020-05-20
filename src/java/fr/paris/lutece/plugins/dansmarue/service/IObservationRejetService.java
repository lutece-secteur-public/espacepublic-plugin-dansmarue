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
package fr.paris.lutece.plugins.dansmarue.service;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;

/**
 * The Interface IObservationRejetService.
 */
public interface IObservationRejetService
{

    /**
     * Return all reject observation.
     *
     * @return a list of ObservationRejet
     */
    List<ObservationRejet> getAllObservationRejet( );

    /**
     * Delete a reject observation.
     *
     * @param nIdObservationRejet            the reject observation id
     */
    void doDeleteObservationRejet( int nIdObservationRejet );

    /**
     * Save a new reject observation.
     *
     * @param observationRejet            the reject observation
     */
    void doSaveObservationRejet( ObservationRejet observationRejet );

    /**
     * Load a reject observation.
     *
     * @param nIdObservationRejet            the observationRejet id
     * @return a reject observation
     */
    ObservationRejet getById( Integer nIdObservationRejet );

    /**
     * Return all active ObservationRejet.
     *
     * @return a list of active reject observation
     */
    List<ObservationRejet> getAllObservationRejetActif( );

    /**
     * Decreases the order of the next reject.
     *
     * @param observationRejet            the observationRejet object
     */
    void increaseOrdreOfRejet( ObservationRejet observationRejet );

    /**
     * Increases the order of the previous reject.
     *
     * @param observationRejet            the observationRejet object
     */
    void decreaseOrdreOfRejet( ObservationRejet observationRejet );

    /**
     * Increases all the next orders.
     *
     * @param nIdObservationRejet            the reject observation id
     */
    void increaseOrdreOfAllNext( int nIdObservationRejet );

    /**
     * Decreases all the next orders.
     *
     * @param nIdObservationRejet            the reject observation id
     */
    void decreaseOrdreOfAllNext( int nIdObservationRejet );

    /**
     * Counts the number of time the reject observation has been used.
     *
     * @param nIdObservationRejet            the id of reject observation to get the use count
     * @return the number of reject observation uses
     */
    int countByIdObservationRejet( int nIdObservationRejet );

    /**
     * Returns the number of reject observation existing.
     *
     * @return the number of reject observation
     */
    int getObservationRejetCount( );

    /**
     * Updates a reject observation order, by its id.
     *
     * @param observationRejet            the observationRejet object
     */
    void updateObservationRejetOrdre( ObservationRejet observationRejet );
}
