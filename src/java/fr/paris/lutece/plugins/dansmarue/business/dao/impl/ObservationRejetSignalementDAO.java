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

import fr.paris.lutece.plugins.dansmarue.business.dao.IObservationRejetSignalementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;
import fr.paris.lutece.util.sql.DAOUtil;

public class ObservationRejetSignalementDAO implements IObservationRejetSignalementDAO
{

    private static final String SQL_QUERY_NEW_PK                 = "SELECT nextval('seq_observation_rejet_signalement')";
    private static final String SQL_QUERY_INSERT                 = "INSERT INTO signalement_observation_rejet_signalement (id_observation_rejet_signalement, fk_id_signalement, fk_id_observation_rejet, observation_rejet_comment) "
            + "VALUES (?,?,?,?)";

    private static final String SQL_QUERY_DELETE                 = "DELETE FROM signalement_observation_rejet_signalement WHERE id_signalement=? AND id_observation_rejet=?";
    private static final String SQL_QUERY_FIND_BY_ID_SIGNALEMENT = "SELECT id_observation_rejet, libelle, actif, ordre, observation_rejet_comment FROM signalement_observation_rejet sor "
            + " RIGHT JOIN signalement_observation_rejet_signalement sors " + " ON sors.fk_id_observation_rejet = sor.id_observation_rejet WHERE fk_id_signalement=?";

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
     * {@inheritDoc}
     */
    @Override
    public void insert( int idSignalement, Integer idRaisonRejet, String observationRejetComment )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        Long idOservationRejetSignalement = newPrimaryKey( );
        int nIndex = 1;

        daoUtil.setLong( nIndex++, idOservationRejetSignalement );
        daoUtil.setLong( nIndex++, idSignalement );
        if ( null != idRaisonRejet )
        {
            daoUtil.setInt( nIndex++, idRaisonRejet );
        } else
        {
            daoUtil.setIntNull( nIndex++ );
        }
        daoUtil.setString( nIndex++, observationRejetComment );

        daoUtil.executeUpdate( );

        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int idSignalement, Integer idRaisonRejet )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, idSignalement );

        if ( null != idRaisonRejet )
        {
            daoUtil.setInt( nIndex++, idRaisonRejet );
        } else
        {
            daoUtil.setIntNull( nIndex++ );
        }

        daoUtil.executeUpdate( );
        daoUtil.close( );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ObservationRejet> findByIdSignalement( int idSignalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID_SIGNALEMENT );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, idSignalement );

        daoUtil.executeQuery( );

        List<ObservationRejet> observationRejets = new ArrayList<>( );

        while ( daoUtil.next( ) )
        {
            nIndex = 1;
            ObservationRejet observationRejet = new ObservationRejet( );
            Long observationRejetId = ( ( Long ) daoUtil.getObject( nIndex++ ) );
            if ( null != observationRejetId )
            {
                observationRejet.setId( observationRejetId.intValue( ) );
                observationRejet.setLibelle( daoUtil.getString( nIndex++ ) );
                observationRejet.setActif( daoUtil.getBoolean( nIndex++ ) );
                observationRejet.setOrdre( daoUtil.getInt( nIndex++ ) );
            } else
            {
                nIndex++;
                nIndex++;
                nIndex++;
                observationRejet.setLibelle( daoUtil.getString( nIndex++ ) );
            }
            observationRejets.add( observationRejet );
        }

        daoUtil.close( );

        return observationRejets;

    }

}
