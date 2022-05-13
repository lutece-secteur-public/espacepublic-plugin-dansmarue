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

import fr.paris.lutece.plugins.dansmarue.business.entities.ConseilQuartier;

/**
 * The Interface IConseilQuartierDao.
 */
public interface IConseilQuartierDao
{

    /**
     * Load the data of all the neighborhoods and returns them as a list.
     *
     * @return The list which contains the data of all the neighborhoods
     */
    List<ConseilQuartier> selectQuartiersList( );

    /**
     * Load the data of the neighborhood from the table.
     *
     * @param nId
     *            The identifier of the neighborhood
     * @return the instance of the neighborhood
     */
    ConseilQuartier load( int nId );

    /**
     * Returns a neighborhood from an address.
     *
     * @param nIdAdresse
     *            the address id
     * @return the instance of the neighborhood
     */
    ConseilQuartier selectQuartierByAdresse( int nIdAdresse );

    /**
     * Insert a new record in the table.
     *
     * @param quartier
     *            instance of the neighborhood object to insert
     */
    void insert( ConseilQuartier quartier );

    /**
     * Delete a record from the table.
     *
     * @param nQuartierId
     *            The identifier of the neighborhood
     */
    void delete( int nQuartierId );

    /**
     * Update the record in the table.
     *
     * @param quartier
     *            The reference of the neighborhood
     */
    void store( ConseilQuartier quartier );
}
