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

import fr.paris.lutece.plugins.dansmarue.business.entities.Aide;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;

/**
 * IAideDAO Interface
 */
public interface IAideDAO
{
    /**
     * Insert a new record in the table.
     *
     * @param aide
     *            instance of the Aide object to insert
     */
    public Integer insert( Aide aide );

    /**
     * Update the record in the table
     *
     * @param aide
     *            the reference of the Aide
     */
    public void store( Aide aide );

    /**
     * Delete a record from the table
     *
     * @param nKey
     *            The identifier of the Aide to delete
     */
    public void delete( long nIdAide );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     *
     * @param nKey
     *            The identifier of the aide
     * @param plugin
     *            the Plugin
     * @return The instance of the aide
     */
    public Aide load( Integer nIdAide );

    /**
     * Load the data of all the aide objects and returns them as a list
     *
     * @param plugin
     *            the Plugin
     * @return The list which contains the data of all the aide objects
     */
    public List<Aide> selectAidesList( Plugin plugin );

    /**
     * Load the id of all the aide objects and returns them as a list
     *
     * @param plugin
     *            the Plugin
     * @return The list which contains the id of all the aide objects
     */
    public List<Integer> selectIdAidesList( Plugin plugin );

    /**
     * Load the data of all the aide objects and returns them as a referenceList
     *
     * @param plugin
     *            the Plugin
     * @return The referenceList which contains the data of all the aide objects
     */
    public ReferenceList selectAidesReferenceList( Plugin plugin );

    List<Aide> getAllAideActif( );

    /**
     * Updates a aide order, by its id.
     *
     * @param aide
     *            the aide object
     */
    void updateAideOrdre( Aide aide );

    /**
     * Decreases the order of the next reject.
     *
     * @param aide
     *            the aide object
     */
    void decreaseOrdreOfNextAide( Aide aide );

    /**
     * Increases the order of the previous reject.
     *
     * @param aide
     *            the aide object
     */
    void increaseOrdreOfPreviousAide( Aide aide );

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

    public boolean existsAide( Aide aide );

    int getVersionAide( );

    ImageResource getImageResource( int nIdAide );

    /**
     * Store without image.
     *
     * @param aide
     *            the aide
     */
    public void storeWithoutImage( Aide aide );

}
