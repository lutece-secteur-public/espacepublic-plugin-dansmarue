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
package fr.paris.lutece.plugins.dansmarue.service;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.Aide;
import fr.paris.lutece.portal.service.image.ImageResource;

/**
 * The Interface IAideService.
 */
public interface IAideService
{

    /**
     * Return all aide.
     *
     * @return a list of aide
     */
    List<Aide> getAllAide( );

    /**
     * Delete a aide.
     *
     * @param nIdAide
     *            the aide id
     */
    void doDeleteAide( long nIdAide );

    /**
     * Save a new aide.
     *
     * @param Aide
     *            the aide
     */
    void doSaveAide( Aide aide );

    /**
     * Load a aide.
     *
     * @param nIdAide
     *            the aide id
     * @return a aide
     */
    Aide getById( Integer nIdAide );

    /**
     * Return all active Aide.
     *
     * @return a list of active aide
     */
    List<Aide> getAllAideActif( );

    /**
     * Decreases the order of the next reject.
     *
     * @param aide
     *            the aide object
     */
    void increaseOrdreOfAide( Aide aide );

    /**
     * Increases the order of the previous reject.
     *
     * @param aide
     *            the aide object
     */
    void decreaseOrdreOfAide( Aide aide );

    /**
     * Increases all the next orders.
     *
     * @param nIdAide
     *            the aide id
     */
    void increaseOrdreOfAllNext( int nIdAide );

    /**
     * Decreases all the next orders.
     *
     * @param nIdAide
     *            the aide id
     */
    void decreaseOrdreOfAllNext( int nIdAide );

    /**
     * Returns the number of aide existing.
     *
     * @return the number of aide
     */
    int getAideCount( );

    /**
     * Updates a aide order, by its id.
     *
     * @param aide
     *            the Aide object
     */
    void updateAideOrdre( Aide aide );

    List<Aide> getAideWithVersion( int versionAide );

    int getVersionAide( );

    /**
     * Gets the image resource.
     *
     * @param nIdAide
     *            the n id aide
     * @return the image resource
     */
    ImageResource getImageResource( int nIdAide );
}
