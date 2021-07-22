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
package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.dao.IDashboardUserPreferencesDAO;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * The Class DashboardUserPreferencesDAO.
 */
public class DashboardUserPreferencesDAO implements IDashboardUserPreferencesDAO
{

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_dashboard_user_preferences(fk_id_user, fk_id_state) VALUES (?,?)";

    /** The Constant SQL_QUERY_DELETE. */
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_dashboard_user_preferences WHERE fk_id_user = ? AND fk_id_state = ?";

    /** The Constant SQL_QUERY_SELECT_BY_USER_ID. */
    private static final String SQL_QUERY_SELECT_BY_USER_ID = "SELECT fk_id_state FROM signalement_dashboard_user_preferences WHERE fk_id_user = ?";

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( Integer idUser, Integer idState )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, idUser );
        daoUtil.setInt( nIndex, idState );

        daoUtil.executeUpdate( );

        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( Integer idUser, Integer idState )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, idUser );
        daoUtil.setInt( nIndex, idState );

        daoUtil.executeUpdate( );

        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> findUserDashboardStates( Integer idUser )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_USER_ID );
        int nIndex = 1;
        daoUtil.setInt( nIndex, idUser );

        daoUtil.executeQuery( );

        List<Integer> userPreferedStates = new ArrayList<>( );
        while ( daoUtil.next( ) )
        {
            nIndex = 1;
            userPreferedStates.add( daoUtil.getInt( nIndex ) );
        }

        daoUtil.close( );

        return userPreferedStates;
    }

}
