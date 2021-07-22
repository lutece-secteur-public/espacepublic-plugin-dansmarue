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

import fr.paris.lutece.plugins.dansmarue.business.entities.DashboardPeriod;

/**
 * The Interface IDashboardPeriodService.
 */
public interface IDashboardPeriodService
{

    /**
     * Inserts into the database a new dashboard period.
     *
     * @param dashboardPeriod
     *            the dashboard period to insert
     * @return the dashboard period id
     */
    public Long insert( DashboardPeriod dashboardPeriod );

    /**
     * Removes from the data base a dashboard period.
     *
     * @param lId
     *            the id of the dashboard period to remove
     */
    public void remove( long lId );

    /**
     * Loads a dashboard period from its id.
     *
     * @param lId
     *            the id of the dashboard period to load
     * @return The dashboard period matching the id null otherwise
     */
    public DashboardPeriod load( long lId );

    /**
     * Updates a SignalementDashboardPeriod.
     *
     * @param dashboardPeriod
     *            the dashboard period element to update
     */
    void update( DashboardPeriod dashboardPeriod );

    /**
     * Gets all dashboard search period criterias.
     *
     * @return List of dashboard perido criterias
     */
    List<DashboardPeriod> getDashboardPeriodCriterias( );

    /**
     * Get all dashboard periods.
     *
     * @return List of all dashboard periods
     */
    List<DashboardPeriod> getAllDashboardPeriods( );
}
