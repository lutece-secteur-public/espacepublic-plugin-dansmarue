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

import fr.paris.lutece.plugins.dansmarue.business.dao.IArrondissementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * The Class ArrondissementDAO.
 */
public class ArrondissementDAO implements IArrondissementDAO
{

    /** The Constant SQL_QUERY_SELECT_ALL_ARRONDISSEMENT. */
    private static final String SQL_QUERY_SELECT_ALL_ARRONDISSEMENT  = "SELECT id_arrondissement, numero_arrondissement, actif, geom FROM signalement_arrondissement ORDER BY id_arrondissement";

    /** The Constant SQL_QUERY_SELECT_BY_IDARRONDISSEMENT. */
    private static final String SQL_QUERY_SELECT_BY_IDARRONDISSEMENT = "SELECT id_arrondissement, numero_arrondissement, actif, geom FROM signalement_arrondissement WHERE id_arrondissement=?";

    /**
     * {@inheritDoc}
     */
    @Override
    public Arrondissement getArrondissementByGeom( Double lng, Double lat )
    {
        Arrondissement ret;

        if ( ( lng != null ) && ( lat != null ) )
        {
            try(  DAOUtil daoUtil = new DAOUtil(
                    "SELECT id_arrondissement, numero_arrondissement, actif, geom FROM signalement_arrondissement WHERE ST_Contains(signalement_arrondissement.geom, ST_GeomFromText('POINT(" + lng
                    + " " + lat + ")', 4326))" )){

                daoUtil.executeQuery( );

                if ( daoUtil.next( ) )
                {
                    ret = convertDaoUtilToEntity( daoUtil );
                }
                else
                {
                    ret = null;
                }

            }
        }
        else
        {
            ret = null;
        }

        return ret;
    }

    /**
     * expected column order is id_arrondissement, arrondissement, active.
     *
     * @param daoUtil            daoUtil
     * @return A district entity filled with query's data.
     */
    public Arrondissement convertDaoUtilToEntity( DAOUtil daoUtil )
    {
        int nIndex = 1;
        long nIdArrondissement = daoUtil.getLong( nIndex++ );
        Arrondissement arrondissement = new Arrondissement( );
        arrondissement.setId( nIdArrondissement );
        arrondissement.setNumero( daoUtil.getString( nIndex++ ) );
        boolean isActif = daoUtil.getInt( nIndex ) == 1;
        arrondissement.setActif( isActif );

        return arrondissement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Arrondissement> getAllArrondissement( )
    {
        List<Arrondissement> listResult = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_ARRONDISSEMENT );
        daoUtil.executeQuery( );

        int nIndex;
        Arrondissement arrondissement;

        while ( daoUtil.next( ) )
        {
            nIndex = 1;
            arrondissement = new Arrondissement( );
            arrondissement.setId( daoUtil.getLong( nIndex++ ) );
            arrondissement.setNumero( daoUtil.getString( nIndex++ ) );
            arrondissement.setActif( daoUtil.getBoolean( nIndex ) );

            listResult.add( arrondissement );
        }

        daoUtil.close( );

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Arrondissement getByIdArrondissement( int nIdArrondissement )
    {
        Arrondissement arrondissement = new Arrondissement( );
        Integer nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_IDARRONDISSEMENT );
        daoUtil.setInt( 1, nIdArrondissement );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            arrondissement.setId( daoUtil.getLong( nIndex++ ) );
            arrondissement.setNumero( daoUtil.getString( nIndex++ ) );
            arrondissement.setActif( daoUtil.getBoolean( nIndex++ ) );
        }

        daoUtil.close( );

        return arrondissement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getArrondissementsInSector( List<Integer> sectors )
    {
        List<Integer> allIdArrondissements = new ArrayList<>( );
        // Si le secteur au minimum à 95% dans l'arrondissmeent, alors il est considéré comme lié à l'arrondissement
        String query = "SELECT DISTINCT signalement_arrondissement.id_arrondissement FROM signalement_arrondissement, unittree_sector WHERE ST_Intersects("
                + " signalement_arrondissement.geom, unittree_sector.geom) and "
                + " (st_area(st_intersection(signalement_arrondissement.geom, unittree_sector.geom))/st_area(signalement_arrondissement.geom)) > .39 "
                + " and ST_isValid(unittree_sector.geom) AND id_sector IN (";
        boolean first = true;

        StringBuilder buf = new StringBuilder( );
        for ( Integer idSector : sectors )
        {
            if ( !first )
            {
                buf.append( "," );
            }

            buf.append( idSector );
            first = false;
        }
        query += buf.toString( );
        query += ") ORDER BY id_arrondissement ";

        DAOUtil daoUtil = new DAOUtil( query );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            allIdArrondissements.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.close( );

        return allIdArrondissements;
    }
}
