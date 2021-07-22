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

import fr.paris.lutece.plugins.dansmarue.business.dao.IObservationRejetDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * The Class ObservationRejetDAO.
 */
public class ObservationRejetDAO implements IObservationRejetDAO
{

    /** The Constant SQL_QUERY_NEW_PK. */
    private static final String SQL_QUERY_NEW_PK = "SELECT nextval('seq_signalement_observation_rejet_id_observation_rejet')";

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_observation_rejet(id_observation_rejet, libelle, actif, ordre) VALUES (?, ?, ?, ?)";

    /** The Constant SQL_QUERY_DELETE. */
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_observation_rejet WHERE id_observation_rejet=?";

    /** The Constant SQL_QUERY_SELECT. */
    private static final String SQL_QUERY_SELECT = "SELECT id_observation_rejet, libelle, actif, ordre FROM signalement_observation_rejet WHERE id_observation_rejet = ?";

    /** The Constant SQL_QUERY_UPDATE. */
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_observation_rejet SET id_observation_rejet=?, libelle=?, actif=? WHERE id_observation_rejet = ?";

    /** The Constant SQL_QUERY_SELECT_ALL_OBSERVATION_REJET. */
    private static final String SQL_QUERY_SELECT_ALL_OBSERVATION_REJET = "SELECT id_observation_rejet, libelle, actif, ordre FROM signalement_observation_rejet ORDER BY ordre";

    /** The Constant SQL_QUERY_SELECT_ALL_OBSERVATION_REJET_ACTIF. */
    private static final String SQL_QUERY_SELECT_ALL_OBSERVATION_REJET_ACTIF = "SELECT id_observation_rejet, libelle, actif, ordre FROM signalement_observation_rejet WHERE actif=1 ORDER BY ordre";

    /** The Constant SQL_QUERY_EXISTS_OBSERVATION_OBJET. */
    private static final String SQL_QUERY_EXISTS_OBSERVATION_OBJET = "SELECT id_observation_rejet FROM signalement_observation_rejet WHERE libelle=?";

    /** The Constant SQL_QUERY_EXISTS_OBSERVATION_OBJET_WITH_ID. */
    private static final String SQL_QUERY_EXISTS_OBSERVATION_OBJET_WITH_ID = "SELECT id_observation_rejet FROM signalement_observation_rejet WHERE libelle=? AND NOT id_observation_rejet=? ";

    /** The Constant SQL_QUERY_UPDATE_OBSERVATION_REJET_ORDRE. */
    private static final String SQL_QUERY_UPDATE_OBSERVATION_REJET_ORDRE = "UPDATE signalement_observation_rejet SET ordre = ? WHERE id_observation_rejet = ?";

    /** The Constant SQL_QUERY_DECREASE_ORDER_OF_NEXT. */
    private static final String SQL_QUERY_DECREASE_ORDER_OF_NEXT = "UPDATE signalement_observation_rejet SET ordre = ordre-1 WHERE ordre= (? + 1)";

    /** The Constant SQL_QUERY_INCREASE_ORDER_OF_PREVIOUS. */
    private static final String SQL_QUERY_INCREASE_ORDER_OF_PREVIOUS = "UPDATE signalement_observation_rejet SET ordre = ordre+1 WHERE ordre= (? - 1)";

    /** The Constant SQL_QUERY_INCREASE_ORDER_OF_ALL_NEXT. */
    private static final String SQL_QUERY_INCREASE_ORDER_OF_ALL_NEXT = "UPDATE signalement_observation_rejet SET ordre = ordre+1 WHERE ordre >= "
            + "(SELECT ordre FROM signalement_observation_rejet WHERE id_observation_rejet=?) AND id_observation_rejet != ?";

    /** The Constant SQL_QUERY_DECREASE_ORDER_OF_ALL_NEXT. */
    private static final String SQL_QUERY_DECREASE_ORDER_OF_ALL_NEXT = "UPDATE signalement_observation_rejet SET ordre = ordre-1 WHERE ordre >= "
            + "(SELECT ordre FROM signalement_observation_rejet WHERE id_observation_rejet=?)";

    /** The Constant SQL_QUERY_GET_OBSERVATION_REJET_COUNT. */
    private static final String SQL_QUERY_GET_OBSERVATION_REJET_COUNT = "SELECT count(id_observation_rejet) FROM signalement_observation_rejet";

    /** The Constant SQL_COUNT_BY_ID_OBSERVATION_REJET. */
    private static final String SQL_COUNT_BY_ID_OBSERVATION_REJET = "SELECT count(*) FROM signalement_observation_rejet_signalement WHERE fk_id_observation_rejet = ?";

    /**
     * Generates a new primary key.
     *
     * @return The new primary key
     */
    private Integer newPrimaryKey( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery( );
        Integer nKey = null;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 );
        }
        else
        {
            nKey = 1;
        }
        daoUtil.close( );

        return nKey.intValue( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer insert( ObservationRejet observationRejet )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) )
        {
            if ( ( observationRejet.getId( ) == null ) || ( observationRejet.getId( ) == 0 ) )
            {
                observationRejet.setId( newPrimaryKey( ) );
            }
            int nIndex = 1;
            daoUtil.setLong( nIndex++, observationRejet.getId( ) );
            daoUtil.setString( nIndex++, observationRejet.getLibelle( ) );
            daoUtil.setBoolean( nIndex++, observationRejet.getActif( ) );
            daoUtil.setLong( nIndex, observationRejet.getOrdre( ) );
            daoUtil.executeUpdate( );

            return observationRejet.getId( );
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
    public ObservationRejet load( Integer lId )
    {
        ObservationRejet observationRejet = new ObservationRejet( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setLong( 1, lId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            observationRejet.setId( daoUtil.getInt( nIndex++ ) );
            observationRejet.setLibelle( daoUtil.getString( nIndex++ ) );
            observationRejet.setActif( daoUtil.getBoolean( nIndex++ ) );
            observationRejet.setOrdre( daoUtil.getInt( nIndex ) );
        }

        daoUtil.close( );

        return observationRejet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( ObservationRejet observationRejet )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, observationRejet.getId( ) );
        daoUtil.setString( nIndex++, observationRejet.getLibelle( ) );
        daoUtil.setBoolean( nIndex++, observationRejet.getActif( ) );

        // WHERE
        daoUtil.setLong( nIndex, observationRejet.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ObservationRejet> getAllObservationRejet( Plugin plugin )
    {

        List<ObservationRejet> listResult = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_OBSERVATION_REJET, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;

            ObservationRejet observationRejet = new ObservationRejet( );
            observationRejet.setId( daoUtil.getInt( nIndex++ ) );
            observationRejet.setLibelle( daoUtil.getString( nIndex++ ) );
            observationRejet.setActif( daoUtil.getBoolean( nIndex++ ) );
            observationRejet.setOrdre( daoUtil.getInt( nIndex ) );
            listResult.add( observationRejet );
        }

        daoUtil.close( );

        return listResult;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ObservationRejet> getAllObservationRejetActif( )
    {

        List<ObservationRejet> listResult = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_OBSERVATION_REJET_ACTIF );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;

            ObservationRejet observationRejet = new ObservationRejet( );
            observationRejet.setId( daoUtil.getInt( nIndex++ ) );
            observationRejet.setLibelle( daoUtil.getString( nIndex++ ) );
            observationRejet.setActif( daoUtil.getBoolean( nIndex++ ) );
            observationRejet.setOrdre( daoUtil.getInt( nIndex ) );
            listResult.add( observationRejet );
        }

        daoUtil.close( );

        return listResult;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsObservationRejet( ObservationRejet observationRejet )
    {
        boolean existsObservationRejet = false;

        DAOUtil daoUtil;

        if ( ( observationRejet.getId( ) != null ) && ( observationRejet.getId( ) > 0 ) )
        {
            int nIndex = 1;

            daoUtil = new DAOUtil( SQL_QUERY_EXISTS_OBSERVATION_OBJET_WITH_ID );
            daoUtil.setString( nIndex++, observationRejet.getLibelle( ) );
            daoUtil.setLong( nIndex, observationRejet.getId( ) );
        }
        else
        {
            daoUtil = new DAOUtil( SQL_QUERY_EXISTS_OBSERVATION_OBJET );
            daoUtil.setString( 1, observationRejet.getLibelle( ) );
        }

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            existsObservationRejet = true;
        }

        daoUtil.close( );

        return existsObservationRejet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreaseOrdreOfNextRejet( ObservationRejet observationRejet )
    {
        DAOUtil daoUtil;
        if ( observationRejet.getOrdre( ) != null )
        {
            int nIndex = 1;
            daoUtil = new DAOUtil( SQL_QUERY_DECREASE_ORDER_OF_NEXT );
            daoUtil.setInt( nIndex, observationRejet.getOrdre( ) );
            daoUtil.executeUpdate( );
            daoUtil.close( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseOrdreOfPreviousRejet( ObservationRejet observationRejet )
    {
        DAOUtil daoUtil;
        if ( observationRejet.getOrdre( ) != null )
        {
            int nIndex = 1;
            daoUtil = new DAOUtil( SQL_QUERY_INCREASE_ORDER_OF_PREVIOUS );
            daoUtil.setInt( nIndex, observationRejet.getOrdre( ) );
            daoUtil.executeUpdate( );
            daoUtil.close( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateObservationRejetOrdre( ObservationRejet observationRejet )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_OBSERVATION_REJET_ORDRE );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, observationRejet.getOrdre( ) );
        daoUtil.setInt( nIndex, observationRejet.getId( ) );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseOrdreOfAllNext( int nIdObservationRejet )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INCREASE_ORDER_OF_ALL_NEXT );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, nIdObservationRejet );
        daoUtil.setInt( nIndex, nIdObservationRejet );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreaseOrdreOfAllNext( int nIdObservationRejet )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DECREASE_ORDER_OF_ALL_NEXT );
        int nIndex = 1;
        daoUtil.setInt( nIndex, nIdObservationRejet );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getObservationRejetCount( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GET_OBSERVATION_REJET_COUNT );
        daoUtil.executeQuery( );
        int observationRejetCount = 0;
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            observationRejetCount = daoUtil.getInt( nIndex );

        }
        daoUtil.close( );
        return observationRejetCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countByIdObservationRejet( int idObservationRejet )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_COUNT_BY_ID_OBSERVATION_REJET );
        int nIndex = 1;
        daoUtil.setInt( nIndex, idObservationRejet );
        daoUtil.executeQuery( );
        int observationRejetCount = 0;
        if ( daoUtil.next( ) )
        {
            nIndex = 1;
            observationRejetCount = daoUtil.getInt( nIndex );

        }
        daoUtil.close( );
        return observationRejetCount;

    }

}
