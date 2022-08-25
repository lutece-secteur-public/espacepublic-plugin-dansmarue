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

import fr.paris.lutece.plugins.dansmarue.business.entities.Actualite;
import fr.paris.lutece.portal.service.image.ImageResource;

/**
 * The Interface IActualiteService.
 */
public interface IActualiteService
{

    /**
     * Return all actualite.
     *
     * @return a list of actualite
     */
    List<Actualite> getAllActualite( );

    /**
     * Delete a actualite.
     *
     * @param nIdActualite
     *            the actualite id
     */
    void doDeleteActualite( long nIdActualite );

    /**
     * Save a new actualite.
     *
     * @param Actualite
     *            the actualite
     */
    void doSaveActualite( Actualite actualite );

    /**
     * Load a actualite.
     *
     * @param nIdActualite
     *            the actualite id
     * @return a actualite
     */
    Actualite getById( Integer nIdActualite );

    /**
     * Return all active Actualite.
     *
     * @return a list of active actualite
     */
    List<Actualite> getAllActualiteActif( );

    /**
     * Decreases the order of the next reject.
     *
     * @param actualite
     *            the actualite object
     */
    void increaseOrdreOfActualite( Actualite actualite );

    /**
     * Increases the order of the previous reject.
     *
     * @param actualite
     *            the actualite object
     */
    void decreaseOrdreOfActualite( Actualite actualite );

    /**
     * Increases all the next orders.
     *
     * @param nIdActualite
     *            the actualite id
     */
    void increaseOrdreOfAllNext( int nIdActualite );

    /**
     * Decreases all the next orders.
     *
     * @param nIdActualite
     *            the actualite id
     */
    void decreaseOrdreOfAllNext( int nIdActualite );

    /**
     * Returns the number of actualite existing.
     *
     * @return the number of actualite
     */
    int getActualiteCount( );

    /**
     * Updates a actualite order, by its id.
     *
     * @param Actualite
     *            the Actualite object
     */
    void updateActualiteOrdre( Actualite actualite );

    /**
     * Gets the image resource.
     *
     * @param nIdActualite
     *            the n id actualite
     * @return the image resource
     */
    ImageResource getImageResource( int nIdActualite );

    /**
     * Get the current version of news list.
     *
     * @return the current verion
     */
    int getVersionActualite( );

    /**
     * Get news list if currentVersion is newer than mobileVersionActualite. empty list other
     *
     * @param mobileVersionActualite
     *            new list version in mobile application
     * @param currentVersion
     *            current version of the news list
     * @return NewsList.
     */
    List<Actualite> getActualiteWithVersion( int mobileVersionActualite, int currentVersion );

}
