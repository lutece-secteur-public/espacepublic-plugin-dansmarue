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

import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementSuivi;
import fr.paris.lutece.plugins.dansmarue.business.entities.SiraUser;

/**
 * The Interface ISignalementSuiviService.
 */
public interface ISignalementSuiviService
{

    /**
     * Inserts into the database a new report follow.
     *
     * @param signalementSuivi
     *            the report follow to insert
     * @return the report id
     */
    public Long insert( SignalementSuivi signalementSuivi );

    /**
     * Removes from the data base a report follow.
     *
     * @param lId
     *            the id of the report follow to remove
     */
    public void remove( long lId );

    /**
     * Loads a report follow from its id.
     *
     * @param lId
     *            the id of the report follow to load
     * @return The report follow matching the id null otherwise
     */
    public SignalementSuivi load( long lId );

    /**
     * Updates a report follow.
     *
     * @param signalementSuivi
     *            the report follow to update
     */
    void update( SignalementSuivi signalementSuivi );

    /**
     * Search all users which subscribed to this anomaly.
     *
     * @param idSignalement
     *            the report id
     * @return list of users
     */
    List<SiraUser> findUsersMobilesByIdSignalement( long idSignalement );

    /**
     * Search all users which subscribed to this anomaly - Except the creator of the anomaly.
     *
     * @param idSignalement
     *            the report id
     * @return list of users
     */
    List<SiraUser> findFollowersMobilesByIdSignalement( long idSignalement );

    /**
     * Search all users from the GUID who created the anomaly.
     *
     * @param idSignalement
     *            the report id
     * @return list of users
     */
    List<SiraUser> findCreatorMobilesByIdSignalement( long idSignalement );

    /**
     * Removes from the data base a report follow.
     *
     * @param signalementSuivi
     *            the report follow object
     */
    void remove( SignalementSuivi signalementSuivi );

    /**
     * Finds all followers mails of a report.
     *
     * @param idSignalement
     *            The report from which the followers must be found
     * @return List of mail of all report followers
     */
    List<String> findUsersMailByIdSignalement( long idSignalement );

    /**
     * Counts the number of followers of the report.
     *
     * @param idSignalement
     *            The report id from which we want to get the followers count
     * @return The number of followers, for this report
     */
    Integer getNbFollowersByIdSignalement( int idSignalement );

    /**
     * Removes all reports following.
     *
     * @param idSignalement
     *            The report id from which the following must be removed
     */
    void removeByIdSignalement( long idSignalement );

    /**
     * Finds a report follow, by an report id and a user guid.
     *
     * @param idSignalement
     *            the report id
     * @param guid
     *            the user guid
     * @return the id of the report follow item -1 otherwise
     */
    long findByIdSignalementAndGuid( long idSignalement, String guid );

}
