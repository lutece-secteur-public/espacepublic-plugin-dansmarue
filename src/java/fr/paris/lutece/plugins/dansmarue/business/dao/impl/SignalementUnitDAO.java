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

import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementUnitDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementUnit;
import fr.paris.lutece.plugins.dansmarue.business.entities.UnitWithDepth;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * The Class SignalementUnitDAO.
 */
public class SignalementUnitDAO implements ISignalementUnitDAO
{

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_unit(fk_id_unit, visible) VALUES (?, ?)";

    /** The Constant SQL_QUERY_DELETE. */
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_unit WHERE fk_id_unit=?";

    /** The Constant SQL_QUERY_SELECT. */
    private static final String SQL_QUERY_SELECT = "SELECT id_unit, id_parent, label, description, visible FROM " + " unittree_unit unit"
            + " LEFT JOIN signalement_unit su ON su.fk_id_unit = unit.id_unit WHERE id_unit = ?";

    /** The Constant SQL_QUERY_UPDATE. */
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_unit SET visible=? WHERE fk_id_unit = ?";

    /** The Constant SQL_QUERY_DELETE_ALL. */
    private static final String SQL_QUERY_DELETE_ALL = "DELETE FROM signalement_unit";

    /** The Constant SQL_QUERY_SELECT_ALL_VISIBLE_UNITS. */
    private static final String SQL_QUERY_SELECT_ALL_VISIBLE_UNITS = "SELECT fk_id_unit FROM signalement_unit WHERE visible = 1";

    /** The Constant SQL_QUERY_SELECT_UNITS_IDS. */
    private static final String SQL_QUERY_SELECT_UNITS_IDS = "SELECT id_unit FROM unittree_unit";

    /** The Constant SQL_QUERY_GET_VISIBLE_UNITS_WITH_DEPTH. */
    private static final String SQL_QUERY_GET_VISIBLE_UNITS_WITH_DEPTH = "select id_unit, id_parent, label, description, depth from  "
            + "(WITH RECURSIVE descendants AS ( " + "    SELECT id_unit, id_parent, label, description, 0 depth " + "    FROM unittree_unit "
            + "    WHERE id_unit = ? " + "UNION " + "    SELECT p.id_unit, p.id_parent, p.label, p.description, d.depth+ 1 " + "    FROM unittree_unit p "
            + "    INNER JOIN descendants d " + "    ON p.id_parent = d.id_unit " + ") " + "SELECT p.id_unit, p.id_parent, p.label, p.description, d.depth "
            + "FROM descendants d " + "INNER JOIN descendants p ON d.id_unit = p.id_unit " + "order by d.depth, p.label asc) as tata "
            + "where id_unit not in (select fk_id_unit from signalement_unit where visible=0) " + "";

    /** The Constant SQL_QUERY_GET_MAX_DEPTH_VISIBLE_UNITS. */
    private static final String SQL_QUERY_GET_MAX_DEPTH_VISIBLE_UNITS = "select max(depth) from  " + "(WITH RECURSIVE descendants AS ( "
            + "    SELECT id_unit, id_parent, label, description, 0 depth " + "    FROM unittree_unit " + "    WHERE id_unit = ? " + "UNION "
            + "    SELECT p.id_unit, p.id_parent, p.label, p.description, d.depth+ 1 " + "    FROM unittree_unit p " + "    INNER JOIN descendants d "
            + "    ON p.id_parent = d.id_unit " + ") " + "SELECT p.id_unit, p.id_parent, p.label, p.description, d.depth " + "FROM descendants d "
            + "INNER JOIN descendants p ON d.id_unit = p.id_unit " + "order by d.depth, p.label asc) as tata "
            + "where id_unit not in (select fk_id_unit from signalement_unit where visible=0)";

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer insert( SignalementUnit signalementUnit, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, signalementUnit.getIdUnit( ) );
        daoUtil.setBoolean( nIndex, signalementUnit.isVisible( ) );
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
            signalementUnit.setDescription( daoUtil.getString( nIndex ) );
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
        daoUtil.setLong( nIndex, signalementUnit.getIdUnit( ) );

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
        List<Integer> visibleUnitsIds = new ArrayList<>( );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            int nIndex = 1;
            visibleUnitsIds.add( daoUtil.getInt( nIndex ) );
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
        List<Integer> visibleUnitsIds = new ArrayList<>( );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            int nIndex = 1;
            visibleUnitsIds.add( daoUtil.getInt( nIndex ) );
        }
        daoUtil.close( );
        return visibleUnitsIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UnitWithDepth> getVisibleUnitsWithDepth( Integer idUnit )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GET_VISIBLE_UNITS_WITH_DEPTH );
        List<UnitWithDepth> unitWithDepthList = new ArrayList<>( );
        daoUtil.setInt( 1, idUnit );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            UnitWithDepth unitWithDepth = new UnitWithDepth( );
            int nIndex = 1;

            unitWithDepth.setIdUnit( daoUtil.getInt( nIndex++ ) );
            unitWithDepth.setIdParent( daoUtil.getInt( nIndex++ ) );
            unitWithDepth.setLabel( daoUtil.getString( nIndex++ ) );
            unitWithDepth.setDescription( daoUtil.getString( nIndex++ ) );
            unitWithDepth.setDepth( daoUtil.getInt( nIndex ) );

            unitWithDepthList.add( unitWithDepth );
        }
        daoUtil.close( );

        return unitWithDepthList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getMaxDepthVisibleUnit( Integer idUnit )
    {
        Integer maxDepth = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GET_MAX_DEPTH_VISIBLE_UNITS );
        daoUtil.setInt( 1, idUnit );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            maxDepth = daoUtil.getInt( 1 );
        }
        daoUtil.close( );

        return maxDepth;
    }
}
