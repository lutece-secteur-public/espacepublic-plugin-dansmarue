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

import fr.paris.lutece.plugins.dansmarue.business.entities.SiraUser;

/**
 * The Interface ISiraUserDAO.
 */
public interface ISiraUserDAO
{

    /**
     * Inserts into the database a new sira user.
     *
     * @param siraUser
     *            the sira user to insert
     * @return the sira user id
     */
    public Long insert( SiraUser siraUser );

    /**
     * Removes from the data base a sira user.
     *
     * @param lId
     *            the id of the sira user to remove
     */
    public void remove( long lId );

    /**
     * Loads a sira user from its id.
     *
     * @param lId
     *            the id of the sira user to load
     * @return The sira user matching the id null otherwise
     */
    public SiraUser load( long lId );

    /**
     * Updates a SiraUser.
     *
     * @param siraUser
     *            the sira user to update
     */
    void update( SiraUser siraUser );

    /**
     * Finds a sira user from its guid and udid.
     *
     * @param guid
     *            the mon compte user id
     * @param token
     *            the user token
     * @return The user matching those ids null otherwise
     */
    SiraUser findByGuidAndToken( String guid, String token );

    /**
     * Finds a sira user from its guid.
     *
     * @param guid
     *            the mon compte user id
     * @return The user matching those ids null otherwise
     */
    SiraUser findByGuid( String guid );

    /**
     * Checks if is user is a prestataire.
     *
     * @param unitId
     *            the unit id
     * @return true, if is user prestataire
     */
    boolean isUserPrestataire( Integer unitId );

    /**
     * Gets the entite max from entite (the first parent).
     *
     * @param unitId
     *            the unit id
     * @return the entite max from entite
     */
    Integer getEntiteMaxFromEntite( Integer unitId );
}
