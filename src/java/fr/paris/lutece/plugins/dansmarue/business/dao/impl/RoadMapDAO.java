/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.dansmarue.business.dao.IRoadMapDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.RoadMapFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementDossier;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.commons.Order;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.sql.DAOUtil;

public class RoadMapDAO implements IRoadMapDAO
{
    private static final String SQL_QUERY_NOT_TODAY_FILTER                                             = " AND (signalement.date_creation::date < ?::date OR (signalement.date_creation::date = ?::date AND signalement.heure_creation::time < ?::time) ) ";
    private static final String SQL_QUERY_SELECT_DOSSIERSIGNALEMENT_BY_SECTOR_DATE_SERVICE_SIGNALEMENT = "SELECT DISTINCT signalement.id_signalement AS id, signalement.prefix AS TYPE, to_number('0' || SUBSTRING(adresse,0, POSITION(' ' IN adresse)), '99999999999.000')::integer as numberadresse, case when to_number('0' || SUBSTRING(adresse,0, POSITION(' ' IN adresse)), '99999999999.000')::integer = 0 AND POSITION(',' IN adresse) = 0 then SUBSTRING(adresse, 0) when to_number('0' || SUBSTRING(adresse,0, POSITION(' ' IN adresse)), '99999999999.000')::integer = 0 AND POSITION(',' IN adresse) != 0 then SUBSTRING(adresse, 0, POSITION(',' IN adresse)) when to_number('0' || SUBSTRING(adresse,0, POSITION(' ' IN adresse)), '99999999999.000')::integer != 0 AND POSITION(',' IN adresse) = 0 then SUBSTRING(SUBSTRING(adresse, POSITION(' ' IN adresse) + 1), 0) else SUBSTRING(SUBSTRING(adresse, POSITION(' ' IN adresse) + 1), 0, POSITION(',' IN SUBSTRING(adresse, POSITION(' ' IN adresse) + 1))) end as libelleadresse, adresse.adresse AS adresse, signalement.annee AS annee, signalement.mois AS mois, signalement.numero AS numero FROM signalement_signalement AS signalement INNER JOIN unittree_sector AS sector ON sector.id_sector = signalement.fk_id_sector INNER JOIN signalement_adresse AS adresse ON adresse.fk_id_signalement = signalement.id_signalement INNER JOIN workflow_resource_workflow AS workflow ON workflow.id_resource=signalement.id_signalement WHERE (workflow.id_state = 8 OR workflow.id_state=9 ) AND signalement.fk_id_sector = ? ";
    private static final String SQL_QUERY_AND_TYPE_SIGNALEMENT_IND                                     = " AND signalement.fk_id_type_signalement IN ";

    // Querys for list of sector
    private static final String SQL_QUERY_SELECT_SIGNALEMENT_BY_LIST_SECTOR_DATE_SERVICE               = " SELECT DISTINCT signalement.id_signalement AS id, signalement.prefix AS TYPE, to_number('0' || SUBSTRING(adresse,0, POSITION(' ' IN adresse)), '99999999999.000')::integer as numberadresse, case when to_number('0' || SUBSTRING(adresse,0, POSITION(' ' IN adresse)), '99999999999.000')::integer = 0 AND POSITION(',' IN adresse) = 0 then SUBSTRING(adresse, 0) when to_number('0' || SUBSTRING(adresse,0, POSITION(' ' IN adresse)), '99999999999.000')::integer = 0 AND POSITION(',' IN adresse) != 0 then SUBSTRING(adresse, 0, POSITION(',' IN adresse)) when to_number('0' || SUBSTRING(adresse,0, POSITION(' ' IN adresse)), '99999999999.000')::integer != 0 AND POSITION(',' IN adresse) = 0 then SUBSTRING(SUBSTRING(adresse, POSITION(' ' IN adresse) + 1), 0) else SUBSTRING(SUBSTRING(adresse, POSITION(' ' IN adresse) + 1), 0, POSITION(',' IN SUBSTRING(adresse, POSITION(' ' IN adresse) + 1))) end as libelleadresse, adresse.adresse AS adresse, signalement.annee AS annee, signalement.mois AS mois, signalement.numero AS numero FROM signalement_signalement AS signalement INNER JOIN unittree_sector AS sector ON sector.id_sector = signalement.fk_id_sector INNER JOIN signalement_adresse AS adresse ON adresse.fk_id_signalement = signalement.id_signalement INNER JOIN workflow_resource_workflow AS workflow ON workflow.id_resource=signalement.id_signalement WHERE (workflow.id_state = 8 OR workflow.id_state=9 ) AND signalement.fk_id_sector IN ";

    // CONSTANTS
    private static final String OPEN_BRACKET                                                           = " ( ";
    private static final String CLOSED_BRACKET                                                         = " ) ";
    private static final String COMMA                                                                  = " , ";

    @Override
    public List<SignalementDossier> getListSignalementDossierByIdSector( RoadMapFilter filter, Plugin plugin, List<TypeSignalement> typeSignalementId )
    {
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_DOSSIERSIGNALEMENT_BY_SECTOR_DATE_SERVICE_SIGNALEMENT );

        if ( !filter.getAfficherSignalementsDuJour( ) )
        {
            sbSQL.append( SQL_QUERY_NOT_TODAY_FILTER );
        }

        if ( ( typeSignalementId != null ) && !typeSignalementId.isEmpty( ) )
        {
            sbSQL.append( SQL_QUERY_AND_TYPE_SIGNALEMENT_IND );
            sbSQL.append( "(" );

            int typeSignalementSize = typeSignalementId.size( );
            Object[] listQuestionMark = new Object[typeSignalementSize];

            for ( int i = 0; i < typeSignalementSize; i++ )
            {
                listQuestionMark[i] = '?';
            }

            sbSQL.append( org.apache.commons.lang.StringUtils.join( listQuestionMark, ',' ) );
            sbSQL.append( ")" );
        }

        // ADD ORDERS
        Order order = filter.getOrder( );

        if ( order != null )
        {
            sbSQL.append( " ORDER BY " );

            if ( order.getName( ).equals( "numRessource" ) )
            {
                sbSQL.append( " type " + order.getOrder( ) + ", annee " + order.getOrder( ) + ", mois " + order.getOrder( ) + ", numero " + order.getOrder( ) + " " );
            } else if ( order.getName( ).equals( "adresse" ) )
            {
                sbSQL.append( " libelleadresse " + order.getOrder( ) + ", numberadresse " + order.getOrder( ) + " " );
            } else
            {
                sbSQL.append( order.getName( ) + " " + order.getOrder( ) + " " );
            }
        }

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );
        int index = 1;

        Date dateSql = DateUtil.formatDateSql( filter.getDate( ), Locale.FRENCH );
        daoUtil.setInt( index++, filter.getIdSector( ) );

        if ( !filter.getAfficherSignalementsDuJour( ) )
        {
            daoUtil.setDate( index++, dateSql );
            daoUtil.setDate( index++, dateSql );
            daoUtil.setTimestamp( index++, new Timestamp( filter.getHeureDebutService( ).getTime( ) ) );
        }

        if ( ( typeSignalementId != null ) && !typeSignalementId.isEmpty( ) )
        {
            for ( TypeSignalement typeSignalement : typeSignalementId )
            {
                daoUtil.setInt( index++, typeSignalement.getId( ) );
            }
        }

        daoUtil.executeQuery( );

        List<SignalementDossier> listeResult = new ArrayList<>( );
        SignalementDossier sd;

        while ( daoUtil.next( ) )
        {
            sd = new SignalementDossier( );
            sd.setId( daoUtil.getInt( 1 ) );
            sd.setType( daoUtil.getString( 2 ) );
            listeResult.add( sd );
        }

        daoUtil.free( );

        return listeResult;
    }

    @Override
    public List<SignalementDossier> getListSignalementDossierByListIdSector( RoadMapFilter filter, Plugin plugin, List<TypeSignalement> typeSignalementId )
    {
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_SIGNALEMENT_BY_LIST_SECTOR_DATE_SERVICE ).append( OPEN_BRACKET );
        StringBuilder sbListSector = new StringBuilder( );

        // Build a list of id sector in sql format
        for ( Integer nIdSector : filter.getListIdSector( ) )
        {
            if ( sbListSector.length( ) != 0 )
            {
                sbListSector.append( COMMA );
            }

            sbListSector.append( nIdSector );
        }

        sbSQL.append( sbListSector ).append( CLOSED_BRACKET );

        if ( !filter.getAfficherSignalementsDuJour( ) )
        {
            sbSQL.append( SQL_QUERY_NOT_TODAY_FILTER );
        }

        if ( ( typeSignalementId != null ) && !typeSignalementId.isEmpty( ) )
        {
            sbSQL.append( SQL_QUERY_AND_TYPE_SIGNALEMENT_IND );
            sbSQL.append( "(" );

            int typeSignalementSize = typeSignalementId.size( );
            Object[] listQuestionMark = new Object[typeSignalementSize];

            for ( int i = 0; i < typeSignalementSize; i++ )
            {
                listQuestionMark[i] = '?';
            }

            sbSQL.append( org.apache.commons.lang.StringUtils.join( listQuestionMark, ',' ) );
            sbSQL.append( ")" );
        }

        // ADD ORDERS
        Order order = filter.getOrder( );

        if ( order != null )
        {
            sbSQL.append( " ORDER BY " );

            if ( order.getName( ).equals( "numRessource" ) )
            {
                sbSQL.append( " type " + order.getOrder( ) + ", annee " + order.getOrder( ) + ", mois " + order.getOrder( ) + ", numero " + order.getOrder( ) + " " );
            } else if ( order.getName( ).equals( "adresse" ) )
            {
                sbSQL.append( " libelleadresse " + order.getOrder( ) + ", numberadresse " + order.getOrder( ) + " " );
            } else
            {
                sbSQL.append( order.getName( ) + " " + order.getOrder( ) + " " );
            }
        }

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );
        int index = 1;
        Date dateSql = DateUtil.formatDateSql( filter.getDate( ), Locale.FRENCH );

        if ( !filter.getAfficherSignalementsDuJour( ) )
        {
            daoUtil.setDate( index++, dateSql );
            daoUtil.setDate( index++, dateSql );
            daoUtil.setTimestamp( index++, new Timestamp( filter.getHeureDebutService( ).getTime( ) ) );
        }

        if ( ( typeSignalementId != null ) && !typeSignalementId.isEmpty( ) )
        {
            for ( TypeSignalement typeSignalement : typeSignalementId )
            {
                daoUtil.setInt( index++, typeSignalement.getId( ) );
            }
        }

        daoUtil.executeQuery( );

        List<SignalementDossier> listeResult = new ArrayList<SignalementDossier>( );
        SignalementDossier sd;

        while ( daoUtil.next( ) )
        {
            sd = new SignalementDossier( );
            sd.setId( daoUtil.getInt( 1 ) );
            sd.setType( daoUtil.getString( 2 ) );
            listeResult.add( sd );
        }

        daoUtil.free( );

        return listeResult;
    }

}
