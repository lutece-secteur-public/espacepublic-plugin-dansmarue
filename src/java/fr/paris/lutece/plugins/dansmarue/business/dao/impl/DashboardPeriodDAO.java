/*
 * Copyright (c) 2002-2020, City of Paris
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
package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.dao.IDashboardPeriodDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.DashboardPeriod;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * The Class DashboardPeriodDAO.
 */
public class DashboardPeriodDAO implements IDashboardPeriodDAO
{

    /** The Constant SQL_QUERY_NEW_PK. */
    private static final String SQL_QUERY_NEW_PK                = "SELECT nextval('seq_signalement_dashboard_period_id')";

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT                = "INSERT INTO signalement_dashboard_period(id_period, libelle, lower_bound, higher_bound, unit, category, ordre) VALUES (?,?,?,?,?,?,?)";

    /** The Constant SQL_QUERY_DELETE. */
    private static final String SQL_QUERY_DELETE                = "DELETE FROM signalement_dashboard_period WHERE id_period = ?";

    /** The Constant SQL_QUERY_SELECT. */
    private static final String SQL_QUERY_SELECT                = "SELECT id_period, libelle, lower_bound, higher_bound, unit, category, ordre FROM signalement_dashboard_period WHERE id_period = ?";

    /** The Constant SQL_QUERY_UPDATE. */
    private static final String SQL_QUERY_UPDATE                = "UPDATE signalement_dashboard_period SET libelle=?, lower_bound=?, higher_bound=?, unit=?, category=?, ordre=? WHERE id_period=?";

    /** The Constant SQL_QUERY_DASHBOARD_CRITERIAS. */
    private static final String SQL_QUERY_DASHBOARD_CRITERIAS   = "SELECT id_period, libelle, lower_bound, higher_bound, unit, category, ordre FROM signalement_dashboard_period WHERE category is null"
            + " ORDER BY ordre";

    /** The Constant SQL_QUERY_ALL_DASHBOARD_PERIODS. */
    private static final String SQL_QUERY_ALL_DASHBOARD_PERIODS = "SELECT id_period, libelle, lower_bound, higher_bound, unit, category, ordre FROM signalement_dashboard_period"
            + " ORDER BY category, ordre";

    /**
     * {@inheritDoc}
     */
    @Override
    public Long newPrimaryKey( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery( );
        Long nKey = null;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getLong( 1 );
        }
        daoUtil.close( );
        return nKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long insert( DashboardPeriod dashboardPeriod, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        if ( ( dashboardPeriod.getId( ) == null ) || ( dashboardPeriod.getId( ) == 0 ) )
        {
            dashboardPeriod.setId( newPrimaryKey( ) );
        }
        int nIndex = 1;

        daoUtil.setLong( nIndex++, dashboardPeriod.getId( ) );
        daoUtil.setString( nIndex++, dashboardPeriod.getLibelle( ) );
        daoUtil.setInt( nIndex++, dashboardPeriod.getLowerBound( ) );
        daoUtil.setInt( nIndex++, dashboardPeriod.getHigherBound( ) );
        daoUtil.setString( nIndex++, dashboardPeriod.getUnit( ) );
        daoUtil.setString( nIndex++, dashboardPeriod.getCategory( ) );
        daoUtil.setInt( nIndex, dashboardPeriod.getOrdre( ) );

        daoUtil.executeUpdate( );

        daoUtil.close( );

        return dashboardPeriod.getId( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( long lId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setLong( 1, lId );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DashboardPeriod load( long lId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );

        int nIndex = 1;

        daoUtil.setLong( nIndex, lId );

        daoUtil.executeQuery( );

        DashboardPeriod dashboardPeriod = null;

        if ( daoUtil.next( ) )
        {
            nIndex = 1;
            dashboardPeriod = new DashboardPeriod( );
            dashboardPeriod.setId( daoUtil.getLong( nIndex++ ) );
            dashboardPeriod.setLibelle( daoUtil.getString( nIndex++ ) );
            dashboardPeriod.setLowerBound( ( Integer ) daoUtil.getObject( nIndex++ ) );
            dashboardPeriod.setHigherBound( ( Integer ) daoUtil.getObject( nIndex++ ) );
            dashboardPeriod.setUnit( daoUtil.getString( nIndex++ ) );
            dashboardPeriod.setCategory( daoUtil.getString( nIndex++ ) );
            dashboardPeriod.setOrdre( daoUtil.getInt( nIndex ) );
        }

        daoUtil.close( );
        return dashboardPeriod;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update( DashboardPeriod dashboardPeriod, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        int nIndex = 1;

        daoUtil.setString( nIndex++, dashboardPeriod.getLibelle( ) );
        daoUtil.setInt( nIndex++, dashboardPeriod.getLowerBound( ) );
        daoUtil.setInt( nIndex++, dashboardPeriod.getHigherBound( ) );
        daoUtil.setString( nIndex++, dashboardPeriod.getUnit( ) );
        daoUtil.setString( nIndex++, dashboardPeriod.getCategory( ) );

        // WHERE
        daoUtil.setLong( nIndex, dashboardPeriod.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DashboardPeriod> getDashboardPeriodCriterias( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DASHBOARD_CRITERIAS );
        int nIndex = 1;
        daoUtil.executeQuery( );

        List<DashboardPeriod> dashboardPeriodCriteriaList = new ArrayList<>( );
        while ( daoUtil.next( ) )
        {
            nIndex = 1;
            DashboardPeriod dashboardPeriod = new DashboardPeriod( );
            dashboardPeriod.setId( daoUtil.getLong( nIndex++ ) );
            dashboardPeriod.setLibelle( daoUtil.getString( nIndex++ ) );
            dashboardPeriod.setLowerBound( ( Integer ) daoUtil.getObject( nIndex++ ) );
            dashboardPeriod.setHigherBound( ( Integer ) daoUtil.getObject( nIndex++ ) );
            dashboardPeriod.setUnit( daoUtil.getString( nIndex++ ) );
            dashboardPeriod.setCategory( daoUtil.getString( nIndex++ ) );
            dashboardPeriod.setOrdre( daoUtil.getInt( nIndex ) );

            dashboardPeriodCriteriaList.add( dashboardPeriod );
        }
        daoUtil.close( );
        return dashboardPeriodCriteriaList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DashboardPeriod> getAllDashboardPeriods( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ALL_DASHBOARD_PERIODS );
        int nIndex = 1;
        daoUtil.executeQuery( );

        List<DashboardPeriod> dashboardPeriodCriteriaList = new ArrayList<>( );
        while ( daoUtil.next( ) )
        {
            nIndex = 1;

            DashboardPeriod dashboardPeriod = new DashboardPeriod( );
            dashboardPeriod.setId( daoUtil.getLong( nIndex++ ) );
            dashboardPeriod.setLibelle( daoUtil.getString( nIndex++ ) );
            dashboardPeriod.setLowerBound( ( Integer ) daoUtil.getObject( nIndex++ ) );
            dashboardPeriod.setHigherBound( ( Integer ) daoUtil.getObject( nIndex++ ) );
            dashboardPeriod.setUnit( daoUtil.getString( nIndex++ ) );
            dashboardPeriod.setCategory( daoUtil.getString( nIndex++ ) );
            dashboardPeriod.setOrdre( daoUtil.getInt( nIndex ) );

            dashboardPeriodCriteriaList.add( dashboardPeriod );
        }
        daoUtil.close( );
        return dashboardPeriodCriteriaList;
    }

}
