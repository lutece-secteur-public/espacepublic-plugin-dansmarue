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

import fr.paris.lutece.plugins.dansmarue.business.dao.IPrioriteDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Priorite;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * The Class PrioriteDAO.
 */
public class PrioriteDAO implements IPrioriteDAO
{

    /** The Constant SQL_QUERY_NEW_PK. */
    private static final String SQL_QUERY_NEW_PK = "SELECT nextval('seq_signalement_priorite_id_priorite')";

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_priorite(id_priorite, libelle) VALUES (?, ?)";

    /** The Constant SQL_QUERY_DELETE. */
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_priorite WHERE id_priorite=?";

    /** The Constant SQL_QUERY_SELECT. */
    private static final String SQL_QUERY_SELECT = "SELECT id_priorite, libelle FROM signalement_priorite WHERE id_priorite = ?";

    /** The Constant SQL_QUERY_SELECT_ALL. */
    private static final String SQL_QUERY_SELECT_ALL = "SELECT id_priorite, libelle, ordre_priorite FROM signalement_priorite ORDER BY ordre_priorite";

    /** The Constant SQL_QUERY_UPDATE. */
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_priorite SET id_priorite=?, libelle=? WHERE id_priorite = ?";

    /**
     * Generates a new primary key.
     *
     * @return The new primary key
     */
    private Long newPrimaryKey( )
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
    public List<Priorite> getAllPriorite( )
    {
        List<Priorite> listResult = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;
            Priorite priorite = new Priorite( );
            priorite.setId( daoUtil.getLong( nIndex++ ) );
            priorite.setLibelle( daoUtil.getString( nIndex++ ) );
            priorite.setOrdrePriorite( daoUtil.getInt( nIndex ) );
            listResult.add( priorite );
        }

        daoUtil.close( );

        return listResult;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long insert( Priorite priorite )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) )
        {
            if ( ( priorite.getId( ) == null ) || ( priorite.getId( ) == 0 ) )
            {
                priorite.setId( newPrimaryKey( ) );
            }
            int nIndex = 1;
            daoUtil.setLong( nIndex++, priorite.getId( ) );
            daoUtil.setString( nIndex, priorite.getLibelle( ) );

            daoUtil.executeUpdate( );

            return priorite.getId( );
        }
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
    public Priorite load( long lId )
    {
        Priorite priorite = new Priorite( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setLong( 1, lId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            priorite.setId( daoUtil.getLong( nIndex++ ) );
            priorite.setLibelle( daoUtil.getString( nIndex ) );
        }

        daoUtil.close( );

        return priorite;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( Priorite priorite )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, priorite.getId( ) );
        daoUtil.setString( nIndex++, priorite.getLibelle( ) );
        // WHERE
        daoUtil.setLong( nIndex, priorite.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

}
