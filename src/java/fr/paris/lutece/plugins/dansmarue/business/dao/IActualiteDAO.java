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
package fr.paris.lutece.plugins.dansmarue.business.dao;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.Actualite;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * The Interface IActualiteDAO.
 */
public interface IActualiteDAO
{

    /**
     * Save a new actualite.
     *
     * @param actualite
     *            the actualite
     * @return actualite id
     */
    public Integer insert( Actualite actualite );

    /**
     * Delete a actualite.
     *
     * @param lId
     *            the actualite id
     */
    public void remove( long lId );

    /**
     * Load a actualite.
     *
     * @param lId
     *            the actualite id
     * @return a actualite
     */
    public Actualite load( Integer lId );

    /**
     * Store a actualite.
     *
     * @param actualite
     *            the actualite object
     */
    public void store( Actualite actualite );

    /**
     * Return all actualite.
     *
     * @param plugin
     *            the plugin
     * @return a list of Actualite
     */
    public List<Actualite> getAllActualite( Plugin plugin );

    /**
     * Check if the actualite already exists.
     *
     * @param actualite
     *            the actualite object
     * @return boolean
     */
    public boolean existsActualite( Actualite actualite );

    /**
     * Return all active Actualite.
     *
     * @return a list of active actualite
     */
    List<Actualite> getAllActualiteActif( );

    /**
     * Updates a actualite order, by its id.
     *
     * @param actualite
     *            the actualite object
     */
    void updateActualiteOrdre( Actualite actualite );

    /**
     * Decreases the order of the next reject.
     *
     * @param actualite
     *            the actualite object
     */
    void decreaseOrdreOfNextActualite( Actualite actualite );

    /**
     * Increases the order of the previous reject.
     *
     * @param actualite
     *            the actualite object
     */
    void increaseOrdreOfPreviousActualite( Actualite actualite );

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
     * Store without image.
     *
     * @param actualite
     *            the actualite
     */
    public void storeWithoutImage( Actualite actualite );

    /**
     * Gets the image resource.
     *
     * @param nIdActualite
     *            the n id actualite
     * @return the image resource
     */
    public ImageResource getImageResource( int nIdActualite );

    int getVersionActualite( );

}
