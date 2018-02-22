/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
 * This class provides Data Access methods for Quartier objects
 */

public final class ConseilQuartierDAO implements IConseilQuartierDao
{

    // Constants

    private static final String SQL_QUERY_NEW_PK    = "SELECT max( id_consqrt ) FROM signalement_conseil_quartier";
    private static final String SQL_QUERY_SELECT    = "SELECT id_consqrt, numero_consqrt, surface, nom_consqrt, numero_arrondissement FROM signalement_conseil_quartier WHERE id_consqrt = ?";
    private static final String SQL_QUERY_INSERT    = "INSERT INTO signalement_conseil_quartier ( id_consqrt, numero_consqrt, surface, nom_consqrt, numero_arrondissement ) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE    = "DELETE FROM signalement_conseil_quartier WHERE id_consqrt = ? ";
    private static final String SQL_QUERY_UPDATE    = "UPDATE signalement_conseil_quartier SET id_consqrt = ?, numero_consqrt = ?, surface = ?, nom_consqrt = ?, numero_arrondissement = ? WHERE id_consqrt = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_consqrt, numero_consqrt, surface, nom_consqrt, numero_arrondissement FROM signalement_conseil_quartier order by numero_arrondissement,nom_consqrt asc";
    private static final String SQL_QUERY_SELECT_BY_ADRESSE = "SELECT id_consqrt, numero_consqrt, surface, nom_consqrt, numero_arrondissement from signalement_conseil_quartier where ST_Intersects( ( select ST_Union(geom) from signalement_adresse where id_adresse = ? )::geometry, signalement_conseil_quartier.geom::geometry )";
    
    /**
     * Generates a new primary key
     *
     * @return The new primary key
     */

    public int newPrimaryKey( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery( );

        int nKey;

        if ( !daoUtil.next( ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free( );

        return nKey;
    }

    /**
     * Insert a new record in the table.
     *
     * @param quartier
     *            instance of the Quartier object to insert
     */

    public void insert( ConseilQuartier quartier )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        quartier.setIdConsqrt( newPrimaryKey( ) );

        daoUtil.setInt( 1, quartier.getIdConsqrt( ) );
        daoUtil.setString( 2, quartier.getNumeroConsqrt( ) );
        daoUtil.setBigDecimal( 3, quartier.getSurface( ) );
        daoUtil.setString( 4, quartier.getNomConsqrt( ) );
        daoUtil.setBigDecimal( 5, quartier.getNumeroArrondissement( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Load the data of the quartier from the table
     *
     * @param nId
     *            The identifier of the quartier
     * @return the instance of the Quartier
     */

    public ConseilQuartier load( int nId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery( );

        ConseilQuartier quartier = null;

        if ( daoUtil.next( ) )
        {
            quartier = new ConseilQuartier( );

            quartier.setIdConsqrt( daoUtil.getInt( 1 ) );
            quartier.setNumeroConsqrt( daoUtil.getString( 2 ) );
            quartier.setSurface( daoUtil.getBigDecimal( 3 ) );
            quartier.setNomConsqrt( daoUtil.getString( 4 ) );
            quartier.setNumeroArrondissement( daoUtil.getBigDecimal( 5 ) );
        }

        daoUtil.free( );
        return quartier;
    }
    
    @Override
    public ConseilQuartier selectQuartierByAdresse( int nIdAdresse )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ADRESSE );
        daoUtil.setInt( 1, nIdAdresse );
        daoUtil.executeQuery( );

        ConseilQuartier quartier = null;

        if ( daoUtil.next( ) )
        {
            quartier = new ConseilQuartier( );

            quartier.setIdConsqrt( daoUtil.getInt( 1 ) );
            quartier.setNumeroConsqrt( daoUtil.getString( 2 ) );
            quartier.setSurface( daoUtil.getBigDecimal( 3 ) );
            quartier.setNomConsqrt( daoUtil.getString( 4 ) );
            quartier.setNumeroArrondissement( daoUtil.getBigDecimal( 5 ) );
        }

        daoUtil.free( );
        return quartier;
    }

    /**
     * Delete a record from the table
     *
     * @param nQuartierId
     *            The identifier of the quartier
     */

    public void delete( int nQuartierId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nQuartierId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Update the record in the table
     *
     * @param quartier
     *            The reference of the quartier
     */

    public void store( ConseilQuartier quartier )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        daoUtil.setInt( 1, quartier.getIdConsqrt( ) );
        daoUtil.setString( 2, quartier.getNumeroConsqrt( ) );
        daoUtil.setBigDecimal( 3, quartier.getSurface( ) );
        daoUtil.setString( 4, quartier.getNomConsqrt( ) );
        daoUtil.setBigDecimal( 5, quartier.getNumeroArrondissement( ) );
        daoUtil.setInt( 6, quartier.getIdConsqrt( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Load the data of all the quartiers and returns them as a list
     *
     * @return The list which contains the data of all the quartiers
     */

    @Override
    public List<ConseilQuartier> selectQuartiersList( )
    {
        List<ConseilQuartier> quartierList = new ArrayList<ConseilQuartier>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            ConseilQuartier quartier = new ConseilQuartier( );

            quartier.setIdConsqrt( daoUtil.getInt( 1 ) );
            quartier.setNumeroConsqrt( daoUtil.getString( 2 ) );
            quartier.setSurface( daoUtil.getBigDecimal( 3 ) );
            quartier.setNomConsqrt( daoUtil.getString( 4 ) );
            quartier.setNumeroArrondissement( daoUtil.getBigDecimal( 5 ) );

            quartierList.add( quartier );
        }

        daoUtil.free( );
        return quartierList;
    }

}
