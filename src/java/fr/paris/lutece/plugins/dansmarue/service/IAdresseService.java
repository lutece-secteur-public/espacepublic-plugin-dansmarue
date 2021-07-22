/*
 * Copyright (c) 2002-2021, City of Paris
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

import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.business.sector.Sector;

/**
 * The Interface IAdresseService.
 */
public interface IAdresseService
{

    /**
     * Create a new address.
     *
     * @param adresse
     *            the address object
     * @return the address id
     */
    Long insert( Adresse adresse );

    /**
     * Remove an address.
     *
     * @param lId
     *            the address id
     */
    void remove( long lId );

    /**
     * Load an address by its id.
     *
     * @param lId
     *            the address id
     * @return an address by its id
     */
    Adresse load( long lId );

    /**
     * Insert or update an address.
     *
     * @param adresse
     *            the address object
     */
    void store( Adresse adresse );

    /**
     * Load an address by its report id.
     *
     * @param lId
     *            the report id
     * @return the address
     */
    Adresse loadByIdSignalement( long lId );

    /**
     * Update an address.
     *
     * @param adresse
     *            the address object
     */
    void update( Adresse adresse );

    /**
     * Update adresse.
     *
     * @param adresse
     *            the adresse
     */
    void updateAdresse( Adresse adresse );

    /**
     * Gets the district by geom.
     *
     * @param lng
     *            the lng
     * @param lat
     *            the lat
     * @return a district by geom.
     */
    Arrondissement getArrondissementByGeom( Double lng, Double lat );

    /**
     * Gets the sector by geom and report type.
     *
     * @param lng
     *            the lng
     * @param lat
     *            the lat
     * @param id
     *            the id
     * @return the sector by geom and report type
     */
    Sector getSecteurByGeomAndTypeSignalement( Double lng, Double lat, Integer id );

    /**
     * Gets the sector by geom and report type.
     *
     * @param lng
     *            the lng
     * @param lat
     *            the lat
     * @param idUnitParent
     *            the unit id of the parent
     * @return the sector by geom and report type
     */
    Sector getSectorByGeomAndIdUnitParent( Double lng, Double lat, Integer idUnitParent );

    /**
     * Find wrong adresses (empty or without CP).
     *
     * @return the list
     */
    public List<Adresse> findWrongAdresses( );

    /**
     * Sets the coordonate to WSG84.
     *
     * @param adresse
     *            the adresse
     * @return the adresse
     */
    public Adresse setCoordonateLambert93ToWSG84( Adresse adresse );

    /**
     * Gets the adresse by position.
     *
     * @param adresse
     *            the adresse
     * @return the adresse by position
     */
    public String getAdresseByPosition( Adresse adresse );

    /**
     * Fix adresses.
     */
    public void fixAdresses( );
}
