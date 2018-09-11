/*
 * Copyright (c) 2002-2018, Mairie de Paris
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

import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementUnitDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementUnit;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

public class SignalementUnitDAO implements ISignalementUnitDAO
{

    private static final String SQL_QUERY_INSERT                   = "INSERT INTO signalement_unit(fk_id_unit, visible) VALUES (?, ?)";
    private static final String SQL_QUERY_DELETE                   = "DELETE FROM signalement_unit WHERE fk_id_unit=?";
    private static final String SQL_QUERY_SELECT                   = "SELECT id_unit, id_parent, label, description, visible FROM " + " unittree_unit unit"
            + " LEFT JOIN signalement_unit su ON su.fk_id_unit = unit.id_unit WHERE id_unit = ?";
    private static final String SQL_QUERY_UPDATE                   = "UPDATE signalement_unit SET visible=? WHERE fk_id_unit = ?";
    private static final String SQL_QUERY_DELETE_ALL               = "DELETE FROM signalement_unit";
    private static final String SQL_QUERY_SELECT_ALL_VISIBLE_UNITS = "SELECT fk_id_unit FROM signalement_unit WHERE visible = 1";
    private static final String SQL_QUERY_SELECT_UNITS_IDS         = "SELECT id_unit FROM unittree_unit";

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer insert( SignalementUnit signalementUnit, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, signalementUnit.getIdUnit( ) );
        daoUtil.setBoolean( nIndex++, signalementUnit.isVisible( ) );
        daoUtil.executeUpdate( );
        daoUtil.close( );

        return signalementUnit.getIdUnit( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( long lId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setLong( 1, lId );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAll( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ALL );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SignalementUnit load( Integer lId )
    {
        SignalementUnit signalementUnit = new SignalementUnit( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setLong( 1, lId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            signalementUnit.setIdUnit( daoUtil.getInt( nIndex++ ) );
            signalementUnit.setIdParent( daoUtil.getInt( nIndex++ ) );
            signalementUnit.setLabel( daoUtil.getString( nIndex++ ) );
            signalementUnit.setDescription( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.close( );

        return signalementUnit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( SignalementUnit signalementUnit )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nIndex = 1;
        daoUtil.setBoolean( nIndex++, signalementUnit.isVisible( ) );

        // WHERE
        daoUtil.setLong( nIndex++, signalementUnit.getIdUnit( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getVisibleUnitsIds( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_VISIBLE_UNITS );
        List<Integer> visibleUnitsIds = new ArrayList<Integer>( );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            int nIndex = 1;
            visibleUnitsIds.add( daoUtil.getInt( nIndex++ ) );
        }
        daoUtil.close( );
        return visibleUnitsIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getAllUnitsIds( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_UNITS_IDS );
        List<Integer> visibleUnitsIds = new ArrayList<Integer>( );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            int nIndex = 1;
            visibleUnitsIds.add( daoUtil.getInt( nIndex++ ) );
        }
        daoUtil.close( );
        return visibleUnitsIds;
    }

}
