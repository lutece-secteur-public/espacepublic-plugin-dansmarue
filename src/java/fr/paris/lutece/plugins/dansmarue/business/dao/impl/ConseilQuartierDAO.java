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

import fr.paris.lutece.plugins.dansmarue.business.dao.IConseilQuartierDao;
import fr.paris.lutece.plugins.dansmarue.business.entities.ConseilQuartier;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for Neighborhood objects.
 */
public final class ConseilQuartierDAO implements IConseilQuartierDao
{

    /** The Constant SQL_QUERY_NEW_PK. */
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_consqrt ) FROM signalement_conseil_quartier";

    /** The Constant SQL_QUERY_SELECT. */
    private static final String SQL_QUERY_SELECT = "SELECT id_consqrt, numero_consqrt, surface, nom_consqrt, numero_arrondissement FROM signalement_conseil_quartier WHERE id_consqrt = ?";

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_conseil_quartier ( id_consqrt, numero_consqrt, surface, nom_consqrt, numero_arrondissement ) VALUES ( ?, ?, ?, ?, ? ) ";

    /** The Constant SQL_QUERY_DELETE. */
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_conseil_quartier WHERE id_consqrt = ? ";

    /** The Constant SQL_QUERY_UPDATE. */
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_conseil_quartier SET id_consqrt = ?, numero_consqrt = ?, surface = ?, nom_consqrt = ?, numero_arrondissement = ? WHERE id_consqrt = ?";

    /** The Constant SQL_QUERY_SELECTALL. */
    private static final String SQL_QUERY_SELECTALL = "SELECT id_consqrt, numero_consqrt, surface, nom_consqrt, numero_arrondissement FROM signalement_conseil_quartier order by numero_arrondissement,nom_consqrt asc";

    /** The Constant SQL_QUERY_SELECT_BY_ADRESSE. */
    private static final String SQL_QUERY_SELECT_BY_ADRESSE = "SELECT id_consqrt, numero_consqrt, surface, nom_consqrt, numero_arrondissement from signalement_conseil_quartier where ST_Intersects( ( select ST_Union(geom) from signalement_adresse where id_adresse = ? )::geometry, signalement_conseil_quartier.geom::geometry )";

    /**
     * Generates a new primary key.
     *
     * @return The new primary key
     */
    private int newPrimaryKey( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery( );

        int nKey;

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.close( );

        return nKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( ConseilQuartier quartier )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) )
        {

            int nIndex = 1;

            quartier.setIdConsqrt( newPrimaryKey( ) );

            daoUtil.setInt( nIndex++, quartier.getIdConsqrt( ) );
            daoUtil.setString( nIndex++, quartier.getNumeroConsqrt( ) );
            daoUtil.setBigDecimal( nIndex++, quartier.getSurface( ) );
            daoUtil.setString( nIndex++, quartier.getNomConsqrt( ) );
            daoUtil.setBigDecimal( nIndex, quartier.getNumeroArrondissement( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConseilQuartier load( int nId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery( );

        ConseilQuartier quartier = null;

        if ( daoUtil.next( ) )
        {
            int nIndex = 1;

            quartier = new ConseilQuartier( );

            quartier.setIdConsqrt( daoUtil.getInt( nIndex++ ) );
            quartier.setNumeroConsqrt( daoUtil.getString( nIndex++ ) );
            quartier.setSurface( daoUtil.getBigDecimal( nIndex++ ) );
            quartier.setNomConsqrt( daoUtil.getString( nIndex++ ) );
            quartier.setNumeroArrondissement( daoUtil.getBigDecimal( nIndex ) );
        }

        daoUtil.close( );

        return quartier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConseilQuartier selectQuartierByAdresse( int nIdAdresse )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ADRESSE );
        daoUtil.setInt( 1, nIdAdresse );
        daoUtil.executeQuery( );

        ConseilQuartier quartier = new ConseilQuartier( );

        if ( daoUtil.next( ) )
        {
            int nIndex = 1;

            quartier.setIdConsqrt( daoUtil.getInt( nIndex++ ) );
            quartier.setNumeroConsqrt( daoUtil.getString( nIndex++ ) );
            quartier.setSurface( daoUtil.getBigDecimal( nIndex++ ) );
            quartier.setNomConsqrt( daoUtil.getString( nIndex++ ) );
            quartier.setNumeroArrondissement( daoUtil.getBigDecimal( nIndex ) );
        }

        daoUtil.close( );

        return quartier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nQuartierId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nQuartierId );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( ConseilQuartier quartier )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, quartier.getIdConsqrt( ) );
        daoUtil.setString( nIndex++, quartier.getNumeroConsqrt( ) );
        daoUtil.setBigDecimal( nIndex++, quartier.getSurface( ) );
        daoUtil.setString( nIndex++, quartier.getNomConsqrt( ) );
        daoUtil.setBigDecimal( nIndex++, quartier.getNumeroArrondissement( ) );
        daoUtil.setInt( nIndex, quartier.getIdConsqrt( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ConseilQuartier> selectQuartiersList( )
    {
        List<ConseilQuartier> quartierList = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;
            ConseilQuartier quartier = new ConseilQuartier( );

            quartier.setIdConsqrt( daoUtil.getInt( nIndex++ ) );
            quartier.setNumeroConsqrt( daoUtil.getString( nIndex++ ) );
            quartier.setSurface( daoUtil.getBigDecimal( nIndex++ ) );
            quartier.setNomConsqrt( daoUtil.getString( nIndex++ ) );
            quartier.setNumeroArrondissement( daoUtil.getBigDecimal( nIndex ) );

            quartierList.add( quartier );
        }

        daoUtil.close( );

        return quartierList;
    }
}
