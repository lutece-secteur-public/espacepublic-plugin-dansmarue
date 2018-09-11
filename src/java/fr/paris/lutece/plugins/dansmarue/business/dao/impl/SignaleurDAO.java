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

import fr.paris.lutece.plugins.dansmarue.business.dao.ISignaleurDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


public class SignaleurDAO implements ISignaleurDAO
{

    private static final String SQL_QUERY_NEW_PK                = "SELECT nextval('seq_signalement_signaleur_id_signaleur')";
    private static final String SQL_QUERY_INSERT                = "INSERT INTO signalement_signaleur(id_signaleur, mail, id_telephone, fk_id_signalement, guid) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_QUERY_DELETE                = "DELETE FROM signalement_signaleur WHERE id_signaleur = ?";
    private static final String SQL_QUERY_SELECT                = "SELECT id_signaleur, mail, fk_id_signalement, guid, id_telephone FROM signalement_signaleur WHERE id_signaleur = ?";
    private static final String SQL_QUERY_SELECT_BY_SIGNALEMENT = "SELECT id_signaleur, mail, fk_id_signalement, guid, id_telephone FROM signalement_signaleur WHERE fk_id_signalement = ?";
    private static final String SQL_QUERY_UPDATE                = "UPDATE signalement_signaleur SET id_signaleur=?, mail = ?, id_telephone=?, fk_id_signalement=?, guid=? WHERE id_signaleur=?";

    /**
     * Generates a new primary key
     * 
     * @param plugin
     *            the plugin
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
     * Upadate a signaleur
     * 
     * @param signaleur
     *            the signaleur to update
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public void update( Signaleur signaleur )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, signaleur.getId( ) );
        daoUtil.setString( nIndex++, signaleur.getMail( ) );
        daoUtil.setString( nIndex++, signaleur.getIdTelephone( ) );

        daoUtil.setLong( nIndex++, signaleur.getSignalement( ).getId( ) );
        daoUtil.setString( nIndex++, signaleur.getGuid( ) );

        daoUtil.setLong( nIndex++, signaleur.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long insert( Signaleur signaleur )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        if ( signaleur.getId( ) == null || signaleur.getId( ) == 0 )
        {
            signaleur.setId( newPrimaryKey( ) );
        }
        int nIndex = 1;
        daoUtil.setLong( nIndex++, signaleur.getId( ) );
        if ( signaleur.getMail( ) == null )
        {
            daoUtil.setString( nIndex++, "" );
        } else
        {
            daoUtil.setString( nIndex++, signaleur.getMail( ) );
        }
        daoUtil.setString( nIndex++, signaleur.getIdTelephone( ) );
        daoUtil.setLong( nIndex++, signaleur.getSignalement( ).getId( ) );
        daoUtil.setString( nIndex++, signaleur.getGuid( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );

        return signaleur.getId( );
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
    public Signaleur load( long lId )
    {
        Signaleur signaleur = new Signaleur( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setLong( 1, lId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            signaleur.setId( daoUtil.getLong( nIndex++ ) );
            signaleur.setMail( daoUtil.getString( nIndex++ ) );
            Signalement signalement = new Signalement( );
            signalement.setId( daoUtil.getLong( nIndex++ ) );
            signaleur.setGuid( daoUtil.getString( nIndex++ ) );
            signaleur.setIdTelephone( daoUtil.getString( nIndex++ ) );
            signaleur.setSignalement( signalement );
        }

        daoUtil.close( );

        return signaleur;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Signaleur loadByIdSignalement( long lId )
    {
        Signaleur signaleur = new Signaleur( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_SIGNALEMENT );
        daoUtil.setLong( 1, lId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            signaleur.setId( daoUtil.getLong( nIndex++ ) );
            signaleur.setMail( daoUtil.getString( nIndex++ ) );

            Signalement signalement = new Signalement( );

            signalement.setId( daoUtil.getLong( nIndex++ ) );

            signaleur.setSignalement( signalement );

            signaleur.setGuid( daoUtil.getString( nIndex++ ) );
            signaleur.setIdTelephone( daoUtil.getString( nIndex++ ) );

        }

        daoUtil.close( );

        return signaleur;
    }

    /**
     * Store a reporter
     * 
     * @param signaleur
     *            the signaleur object
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public void store( Signaleur signaleur )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, signaleur.getId( ) );
        daoUtil.setString( nIndex++, signaleur.getMail( ) );
        daoUtil.setString( nIndex++, signaleur.getIdTelephone( ) );
        daoUtil.setLong( nIndex++, signaleur.getSignalement( ).getId( ) );
        daoUtil.setString( nIndex++, signaleur.getGuid( ) );
        // WHERE
        daoUtil.setLong( nIndex++, signaleur.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Signaleur> findBySignalementId( long lIdSignalement )
    {
        List<Signaleur> result = new ArrayList<Signaleur>( );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_SIGNALEMENT );
        daoUtil.setLong( 1, lIdSignalement );

        daoUtil.executeQuery( );

        // For each result returned
        while ( daoUtil.next( ) )
        {
            Signaleur signaleur = new Signaleur( );
            int nIndex = 1;
            signaleur.setId( daoUtil.getLong( nIndex++ ) );
            signaleur.setMail( daoUtil.getString( nIndex++ ) );
            Signalement signalement = new Signalement( );
            signalement.setId( daoUtil.getLong( nIndex++ ) );
            signaleur.setSignalement( signalement );
            signaleur.setGuid( daoUtil.getString( nIndex++ ) );
            signaleur.setIdTelephone( daoUtil.getString( nIndex++ ) );
            result.add( signaleur );
        }

        daoUtil.close( );

        return result;
    }
}
