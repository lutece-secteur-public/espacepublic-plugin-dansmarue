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
package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import fr.paris.lutece.plugins.dansmarue.business.dao.IDashboardPeriodDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.DashboardPeriod;
import fr.paris.lutece.plugins.dansmarue.service.IDashboardPeriodService;
import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;

/**
 * The Class DashboardPeriodService.
 */
public class DashboardPeriodService implements IDashboardPeriodService
{

    /** The dashboard period DAO. */
    @Inject
    @Named( "signalement.dashboardPeriodDAO" )
    private IDashboardPeriodDAO _dashboardPeriodDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long insert( DashboardPeriod dashboardPeriod )
    {
        return _dashboardPeriodDAO.insert( dashboardPeriod, SignalementPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( long lId )
    {
        _dashboardPeriodDAO.remove( lId, SignalementPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DashboardPeriod load( long lId )
    {
        return _dashboardPeriodDAO.load( lId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update( DashboardPeriod dashboardPeriod )
    {
        _dashboardPeriodDAO.update( dashboardPeriod, SignalementPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DashboardPeriod> getDashboardPeriodCriterias( )
    {
        return _dashboardPeriodDAO.getDashboardPeriodCriterias( );
    }

    /**
     * {@inheritDoc}}
     */
    @Override
    public List<DashboardPeriod> getAllDashboardPeriods( )
    {
        return _dashboardPeriodDAO.getAllDashboardPeriods( );
    }

}
