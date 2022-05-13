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

import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementUnit;
import fr.paris.lutece.plugins.dansmarue.business.entities.UnitWithDepth;

/**
 * The Interface ISignalementUnitService.
 */
public interface ISignalementUnitService
{

    /**
     * Inserts into the database, the report unit.
     *
     * @param unitSignalement
     *            the report unit
     * @return the report unit id
     */
    Integer insert( SignalementUnit unitSignalement );

    /**
     * Removes report unit properties.
     *
     * @param lId
     *            the report unit id
     */
    void remove( long lId );

    /**
     * Loads the report unit.
     *
     * @param lId
     *            the report unit id
     * @return the report unit
     */
    SignalementUnit load( Integer lId );

    /**
     * Updates a report unit.
     *
     * @param unitSignalement
     *            the report unit object
     */
    void store( SignalementUnit unitSignalement );

    /**
     * Gets all units ids which are visible.
     *
     * @return list containing the id of visible units
     */
    List<Integer> getVisibleUnitsIds( );

    /**
     * Gets all units ids.
     *
     * @return list containing the id of units
     */
    List<Integer> getAllUnitsIds( );

    /**
     * Save units defined as visible.
     *
     * @param visibleUnitsIds
     *            list of visible units ids
     */
    void saveVisibleUnits( List<Integer> visibleUnitsIds );

    /**
     * Gets the units with depth.
     *
     * @param idUnit
     *            the id unit
     * @return the units with depth
     */
    public List<UnitWithDepth> getVisibleUnitsWithDepth( Integer idUnit );

    /**
     * Gets the max depth visible unit.
     *
     * @param idUnit
     *            the id unit
     * @return the max depth visible unit
     */
    public Integer getMaxDepthVisibleUnit( Integer idUnit );
}
