/*
 * Copyright (c) 2002-2022, City of Paris
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

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.IFeuilleDeTourneeDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.FeuilleDeTournee;
import fr.paris.lutece.plugins.dansmarue.business.entities.FeuilleDeTourneeFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.FeuilleDeTourneeFilterSearch;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementBean;
import fr.paris.lutece.plugins.dansmarue.commons.Order;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * The Class FeuilleDeTourneeDAO.
 */
public class FeuilleDeTourneeDAO implements IFeuilleDeTourneeDAO
{

    /** The Constant SQL_QUERY_SELECT_ALL_ARRONDISSEMENT. */
    private static final String SQL_QUERY_SELECT_BY_ID = "SELECT id, createur, fk_id_unit, date_creation, date_modification, commentaire, id_filtre_init, signalement_ids, nom, carte, uu.\"label\", uu2.\"label\", id_direction, id_entite FROM signalement_feuille_de_tournee fdt left join unittree_unit uu on uu.id_unit = fdt.id_direction left join unittree_unit uu2 on uu2.id_unit = fdt.id_entite  where id=?";

    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_feuille_de_tournee (id, createur, fk_id_unit, date_creation, commentaire, id_filtre_init, signalement_ids, nom, carte, id_direction, id_entite) VALUES(?, ?, ?, ?, ?, ?, ''{0}'', ?, ?, ?, ?)";

    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_feuille_de_tournee SET date_modification=?, nom = ?, commentaire = ?, id_direction = ?, id_entite = ?, signalement_ids=''{0}'' WHERE id=?";

    private static final String SQL_QUERY_UPDATE_MAP = "UPDATE signalement_feuille_de_tournee SET carte = ? WHERE id=?";

    private static final String SQL_QUERY_UPDATE_BY_NAME = "UPDATE signalement_feuille_de_tournee SET date_modification=?, commentaire = ?, signalement_ids=''{0}'' WHERE nom=? RETURNING id";

    private static final String SQL_QUERY_NEW_PK = "SELECT max( id ) FROM signalement_feuille_de_tournee";

    private static final String SQL_QUERY_NEW_PK_FILTER = "SELECT max( id ) FROM signalement_feuille_de_tournee_filter";

    private static final String SQL_QUERY_INSERT_FILTER = "INSERT INTO signalement_feuille_de_tournee_filter (id, nom, createur, id_entite, commentaire, date, valeur) VALUES(?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_QUERY_UPDATE_FILTER = "UPDATE signalement_feuille_de_tournee_filter set createur = ?, commentaire= ?, date = ?, valeur = ? WHERE id_entite = ? and nom = ?";

    private static final String SQL_QUERY_DELETE_FILTER = "DELETE FROM signalement_feuille_de_tournee_filter WHERE id=?";

    private static final String SQL_QUERY_LOAD_FILTER_BY_ID_UNIT = "SELECT id, nom FROM signalement_feuille_de_tournee_filter WHERE id_entite = ?";

    private static final String SQL_QUERY_FIND_FILTER_BY_NAME = "SELECT id, nom, createur, id_entite, commentaire, date FROM signalement_feuille_de_tournee_filter WHERE nom = ?";

    private static final String SQL_QUERY_LOAD_FILTER_BY_ID = "SELECT valeur FROM signalement_feuille_de_tournee_filter WHERE id = ?";

    private static final String SQL_QUERY_LOAD_SIGNALEMENTS_BEAN_BY_ID = "select id_signalement, commentaire_usager, commentaire_agent_terrain, date_creation, date_prevu_traitement, adresse, numero, priorite, etat, type_signalement from signalement_export inner join (select unnest((select signalement_ids from signalement_feuille_de_tournee where id=?)::BIGINT[]) as sub) as n2 on n2.sub = id_signalement order by  CASE WHEN arrondissement = 'Paris Centre' and SUBSTR(SUBSTRING(adresse, '(75[0-9][0-9][0-9])'),4,2) ~ '^[0-9\\.]+$'"
            + "THEN SUBSTR(SUBSTRING(adresse, '(75[0-9][0-9][0-9])'),4,2)"
            + "END, id_arrondissement  asc, lower(regexp_replace(adresse,'[[:digit:]]','','g')) asc, nullif(regexp_replace(split_part(adresse,' ',1), '\\D', '', 'g'), '')::int asc";

    private static final String SQL_QUERY_LOAD_FDT_BY_FILTER = "SELECT id, createur, fk_id_unit, date_creation, date_modification, commentaire, id_filtre_init, signalement_ids, nom, uu.\"label\" direction, uu2.\"label\" entite, id_direction, id_entite FROM signalement_feuille_de_tournee fdt left join unittree_unit uu on uu.id_unit = fdt.id_direction left join unittree_unit uu2 on uu2.id_unit = fdt.id_entite ";

    private static final String SQL_QUERY_LOAD_FDT_BY_FILTER_ADD_DATE_DEBUT = " date_trunc('days',date_creation)>=? ";

    private static final String SQL_QUERY_LOAD_FDT_BY_FILTER_ADD_DATE_FIN = " date_trunc('days',date_creation)<=? ";

    private static final String SQL_QUERY_LOAD_FDT_BY_FILTER_ADD_NOM = " lower_unaccent(nom) LIKE lower_unaccent(?) ";

    private static final String SQL_QUERY_LOAD_FDT_BY_FILTER_ADD_CREATEUR = " lower_unaccent(createur) LIKE lower_unaccent(?) ";

    private static final String SQL_QUERY_LOAD_NAMES = "select nom from signalement_feuille_de_tournee";

    private static final String SQL_QUERY_LOAD_BY_NAME = "SELECT id, createur, fk_id_unit, date_creation, date_modification, commentaire, id_filtre_init, signalement_ids, nom, uu.\"label\", uu2.\"label\", id_direction, id_entite from signalement_feuille_de_tournee fdt left join unittree_unit uu on uu.id_unit = fdt.id_direction left join unittree_unit uu2 on uu2.id_unit = fdt.id_entite where nom = ?";

    private static final String SQL_QUERY_DELETE = "DELETE FROM  public.signalement_feuille_de_tournee WHERE id=?";

    private static final String SQL_QUERY_DELETE_OLD = "delete from signalement_feuille_de_tournee where date_creation::date < now()::date  - INTERVAL ''{0} DAY''";

    private static final String SQL_QUERY_SELECT_ALL = "SELECT id, createur, fk_id_unit, date_creation, date_modification, commentaire, id_filtre_init, signalement_ids, nom, uu.\"label\", uu2.\"label\", id_direction, id_entite FROM signalement_feuille_de_tournee fdt left join unittree_unit uu on uu.id_unit = fdt.id_direction left join unittree_unit uu2 on uu2.id_unit = fdt.id_entite order by id desc";

    /**
     * Generates a new primary key.
     *
     * @param querry
     *            querry to execute.
     * @return The new primary key
     */
    private int newPrimaryKey( String querry )
    {
        DAOUtil daoUtil = new DAOUtil( querry );
        daoUtil.executeQuery( );

        int nKey;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }
        else
        {
            nKey = 1;
        }

        daoUtil.close( );

        return nKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeuilleDeTournee load( Integer nId )
    {
        FeuilleDeTournee feuilleDeTournee = new FeuilleDeTournee( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ID );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            feuilleDeTournee = fillFeuilleDeTournee( daoUtil, true );
        }
        daoUtil.close( );

        return feuilleDeTournee;
    }

    /**
     * Fill feuille de tournee.
     *
     * @param daoUtil
     *            the dao util
     * @param loadMap
     *            laod FeuilleDeTournee map
     * @return the feuille de tournee
     */
    private FeuilleDeTournee fillFeuilleDeTournee( DAOUtil daoUtil, boolean loadMap )
    {
        int nIndex = 0;
        FeuilleDeTournee feuilleDeTournee = new FeuilleDeTournee( );

        feuilleDeTournee.setId( daoUtil.getInt( ++nIndex ) );
        feuilleDeTournee.setCreateur( daoUtil.getString( ++nIndex ) );
        feuilleDeTournee.setUnitId( daoUtil.getInt( ++nIndex ) );
        feuilleDeTournee.setDateCreation( DateUtils.getDateFr( daoUtil.getDate( ++nIndex ) ) );
        feuilleDeTournee.setDateModification( DateUtils.getDateFr( daoUtil.getDate( ++nIndex ) ) );
        feuilleDeTournee.setCommentaire( daoUtil.getString( ++nIndex ) );
        feuilleDeTournee.setFiltreFdtId( daoUtil.getInt( ++nIndex ) );

        String ids = daoUtil.getString( ++nIndex );
        List<String> idsString = Arrays.asList( ids.replace( "{", "" ).replace( "}", "" ).split( "," ) );
        feuilleDeTournee.setListSignalementIds( idsString.stream( ).map( Integer::valueOf ).collect( Collectors.toList( ) ) );

        feuilleDeTournee.setNom( daoUtil.getString( ++nIndex ) );
        if ( loadMap )
        {
            feuilleDeTournee.setCarteBase64( daoUtil.getString( ++nIndex ) );
        }
        feuilleDeTournee.setDirectionLibelle( daoUtil.getString( ++nIndex ) );
        feuilleDeTournee.setEntiteLibelle( daoUtil.getString( ++nIndex ) );
        feuilleDeTournee.setIdDirection( daoUtil.getInt( ++nIndex ) );
        feuilleDeTournee.setIdEntite( daoUtil.getInt( ++nIndex ) );

        return feuilleDeTournee;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer insert( FeuilleDeTournee feuilleDeTournee )
    {
        List<String> newList = feuilleDeTournee.getListSignalementIds( ).stream( ).map( String::valueOf ).collect( Collectors.toList( ) );
        int idFeuilleDeTournee;

        try ( DAOUtil daoUtil = new DAOUtil( MessageFormat.format( SQL_QUERY_INSERT, "{" + StringUtils.join( newList.toArray( ), "," ) + "}" ) ) ; )
        {
            int nIndex = 0;
            idFeuilleDeTournee = newPrimaryKey( SQL_QUERY_NEW_PK );

            daoUtil.setInt( ++nIndex, idFeuilleDeTournee );
            daoUtil.setString( ++nIndex, feuilleDeTournee.getCreateur( ) );
            daoUtil.setInt( ++nIndex, feuilleDeTournee.getUnitId( ) );
            daoUtil.setTimestamp( ++nIndex, new Timestamp( System.currentTimeMillis( ) ) );
            daoUtil.setString( ++nIndex, feuilleDeTournee.getCommentaire( ) );
            daoUtil.setInt( ++nIndex, feuilleDeTournee.getFiltreFdtId( ) );
            daoUtil.setString( ++nIndex, feuilleDeTournee.getNom( ) );
            daoUtil.setString( ++nIndex, feuilleDeTournee.getCarteBase64( ) );
            daoUtil.setInt( ++nIndex, feuilleDeTournee.getIdDirection( ) );
            daoUtil.setInt( ++nIndex, feuilleDeTournee.getIdEntite( ) != null ? feuilleDeTournee.getIdEntite( ) : -1 );

            daoUtil.executeUpdate( );
        }

        return idFeuilleDeTournee;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update( FeuilleDeTournee feuilleDeTournee )
    {
        List<String> newList = feuilleDeTournee.getListSignalementIds( ).stream( ).map( String::valueOf ).collect( Collectors.toList( ) );

        try ( DAOUtil daoUtil = new DAOUtil( MessageFormat.format( SQL_QUERY_UPDATE, "{" + StringUtils.join( newList.toArray( ), "," ) + "}" ) ) ; )
        {
            int nIndex = 0;

            daoUtil.setTimestamp( ++nIndex, new Timestamp( System.currentTimeMillis( ) ) );
            daoUtil.setString( ++nIndex, feuilleDeTournee.getNom( ) );
            daoUtil.setString( ++nIndex, feuilleDeTournee.getCommentaire( ) );
            daoUtil.setInt( ++nIndex, feuilleDeTournee.getIdDirection( ) );
            daoUtil.setInt( ++nIndex, feuilleDeTournee.getIdEntite( ) );
            daoUtil.setInt( ++nIndex, feuilleDeTournee.getId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insertSearchFilter( String creator, String name, String comment, String jsonFiltertoSave, int idUnit )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_FILTER ) )
        {
            int nIndex = 1;
            daoUtil.setLong( nIndex++, newPrimaryKey( SQL_QUERY_NEW_PK_FILTER ) );
            daoUtil.setString( nIndex++, name );
            daoUtil.setString( nIndex++, creator );
            daoUtil.setInt( nIndex++, idUnit );
            daoUtil.setString( nIndex++, comment );
            daoUtil.setTimestamp( nIndex++, new Timestamp( System.currentTimeMillis( ) ) );
            daoUtil.setString( nIndex, jsonFiltertoSave );

            daoUtil.executeUpdate( );

        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void updateSearchFilter( String creator, String name, String comment, String jsonFiltertoSave, int idUnit )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_FILTER ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, creator );
            daoUtil.setString( nIndex++, comment );
            daoUtil.setTimestamp( nIndex++, new Timestamp( System.currentTimeMillis( ) ) );
            daoUtil.setString( nIndex++, jsonFiltertoSave );
            daoUtil.setInt( nIndex++, idUnit );
            daoUtil.setString( nIndex, name );

            daoUtil.executeUpdate( );

        }

    }

    @Override
    public synchronized void deleteSearchFilter( int idFilter )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_FILTER ) )
        {
            daoUtil.setInt( 1, idFilter );
            daoUtil.executeUpdate( );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, String> loadSearchFilter( int idUnit )
    {
        Map<Integer, String> filters = new HashedMap( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_LOAD_FILTER_BY_ID_UNIT ) )
        {
            daoUtil.setInt( 1, idUnit );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                int nIndex = 1;
                filters.put( daoUtil.getInt( nIndex++ ), daoUtil.getString( nIndex ) );
            }
        }

        return filters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeuilleDeTourneeFilterSearch findSearchFilter( String filterName )
    {

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_FILTER_BY_NAME ) )
        {
            daoUtil.setString( 1, filterName );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                int nIndex = 1;
                FeuilleDeTourneeFilterSearch filterSearch = new FeuilleDeTourneeFilterSearch( );
                filterSearch.setId( daoUtil.getInt( nIndex++ ) );
                filterSearch.setNom( daoUtil.getString( nIndex++ ) );
                filterSearch.setCreateur( daoUtil.getString( nIndex++ ) );
                filterSearch.setIdEntite( daoUtil.getInt( nIndex++ ) );
                filterSearch.setCommentaire( daoUtil.getString( nIndex++ ) );
                Timestamp timeStamp = daoUtil.getTimestamp( nIndex );
                filterSearch.setDate( DateUtils.getDate( timeStamp, "dd/MM/yyyy HH:mm" ) );

                return filterSearch;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String loadSearchFilterById( int idToLoad )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_LOAD_FILTER_BY_ID ) )
        {
            daoUtil.setInt( 1, idToLoad );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                int nIndex = 1;
                return daoUtil.getString( nIndex );
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SignalementBean> loadSignalementBeanList( int idFeuilleDeTournee )
    {
        List<SignalementBean> listSignalementsBean = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_LOAD_SIGNALEMENTS_BEAN_BY_ID ) )
        {
            daoUtil.setInt( 1, idFeuilleDeTournee );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                int nIndex = 0;
                SignalementBean signalementBean = new SignalementBean( );
                signalementBean.setId( daoUtil.getLong( ++nIndex ) );
                signalementBean.setCommentaire( daoUtil.getString( ++nIndex ) );
                signalementBean.setCommentaireAgentTerrain( daoUtil.getString( ++nIndex ) );
                signalementBean.setDateCreation( daoUtil.getString( ++nIndex ) );
                signalementBean.setDatePrevueTraitement( daoUtil.getString( ++nIndex ) );
                signalementBean.setAdresse( daoUtil.getString( ++nIndex ) );
                signalementBean.setNumeroComplet( daoUtil.getString( ++nIndex ) );
                signalementBean.setPriorite( daoUtil.getString( ++nIndex ) );
                signalementBean.setStatut( daoUtil.getString( ++nIndex ) );
                signalementBean.setTypeSignalementComplet( daoUtil.getString( ++nIndex ) );

                listSignalementsBean.add( signalementBean );
            }
        }

        return listSignalementsBean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FeuilleDeTournee> loadFdtByFilter( FeuilleDeTourneeFilter feuilleDeTourneeFilter )
    {
        List<FeuilleDeTournee> feuilleDeTourneeList = new ArrayList<>( );

        String query = SQL_QUERY_LOAD_FDT_BY_FILTER;
        query += getConditionsQueryLoadFdtByFilter( feuilleDeTourneeFilter );
        query += " order by date_creation desc";

        try ( DAOUtil daoUtil = new DAOUtil( query ) )
        {
            setConditionsQueryLoadFdtByFilter( feuilleDeTourneeFilter, daoUtil );

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                FeuilleDeTournee feuilleDeTournee = fillFeuilleDeTournee( daoUtil, false );
                feuilleDeTourneeList.add( feuilleDeTournee );
            }
        }

        return feuilleDeTourneeList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FeuilleDeTournee> loadFdtByFilterWithOrder( FeuilleDeTourneeFilter feuilleDeTourneeFilter, Order order )
    {
        List<FeuilleDeTournee> feuilleDeTourneeList = new ArrayList<>( );

        String query = SQL_QUERY_LOAD_FDT_BY_FILTER;
        query += getConditionsQueryLoadFdtByFilter( feuilleDeTourneeFilter );
        query += "ORDER BY " + order.getName( ) + " " + order.getOrder( );

        try ( DAOUtil daoUtil = new DAOUtil( query ) )
        {
            setConditionsQueryLoadFdtByFilter( feuilleDeTourneeFilter, daoUtil );

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                FeuilleDeTournee feuilleDeTournee = fillFeuilleDeTournee( daoUtil, false );
                feuilleDeTourneeList.add( feuilleDeTournee );
            }
        }

        return feuilleDeTourneeList;
    }

    private String getConditionsQueryLoadFdtByFilter( FeuilleDeTourneeFilter feuilleDeTourneeFilter )
    {
        boolean isFilterCreateur = ( feuilleDeTourneeFilter.getCreateur( ) != null ) && !feuilleDeTourneeFilter.getCreateur( ).isEmpty( );
        boolean isFilterNom = ( feuilleDeTourneeFilter.getNom( ) != null ) && !feuilleDeTourneeFilter.getNom( ).isEmpty( );
        boolean isFilterByDateCreationDebut = ( feuilleDeTourneeFilter.getDateCreationDebut( ) != null )
                && !feuilleDeTourneeFilter.getDateCreationDebut( ).isEmpty( );
        boolean isFilterByDateCreationFin = ( feuilleDeTourneeFilter.getDateCreationFin( ) != null )
                && !feuilleDeTourneeFilter.getDateCreationFin( ).isEmpty( );

        boolean isFilter = isFilterCreateur || isFilterNom || isFilterByDateCreationDebut || isFilterByDateCreationFin;

        String conditions = "";

        List<String> conditionsList = new ArrayList<>( );

        if ( isFilter )
        {
            conditions += " WHERE ";

            if ( isFilterCreateur )
            {
                conditionsList.add( SQL_QUERY_LOAD_FDT_BY_FILTER_ADD_CREATEUR );
            }

            if ( isFilterNom )
            {
                conditionsList.add( SQL_QUERY_LOAD_FDT_BY_FILTER_ADD_NOM );
            }

            if ( isFilterByDateCreationDebut )
            {
                conditionsList.add( SQL_QUERY_LOAD_FDT_BY_FILTER_ADD_DATE_DEBUT );
            }

            if ( isFilterByDateCreationFin )
            {
                conditionsList.add( SQL_QUERY_LOAD_FDT_BY_FILTER_ADD_DATE_FIN );
            }
            conditions += StringUtils.join( conditionsList, " and " );
        }

        return conditions;
    }

    private void setConditionsQueryLoadFdtByFilter( FeuilleDeTourneeFilter feuilleDeTourneeFilter, DAOUtil daoUtil )
    {
        boolean isFilterCreateur = ( feuilleDeTourneeFilter.getCreateur( ) != null ) && !feuilleDeTourneeFilter.getCreateur( ).isEmpty( );
        boolean isFilterNom = ( feuilleDeTourneeFilter.getNom( ) != null ) && !feuilleDeTourneeFilter.getNom( ).isEmpty( );
        boolean isFilterByDateCreationDebut = ( feuilleDeTourneeFilter.getDateCreationDebut( ) != null )
                && !feuilleDeTourneeFilter.getDateCreationDebut( ).isEmpty( );
        boolean isFilterByDateCreationFin = ( feuilleDeTourneeFilter.getDateCreationFin( ) != null )
                && !feuilleDeTourneeFilter.getDateCreationFin( ).isEmpty( );

        boolean isFilter = isFilterCreateur || isFilterNom || isFilterByDateCreationDebut || isFilterByDateCreationFin;

        if ( isFilter )
        {
            int nIndex = 0;

            if ( isFilterCreateur )
            {
                daoUtil.setString( ++nIndex, "%" + feuilleDeTourneeFilter.getCreateur( ) + "%" );
            }

            if ( isFilterNom )
            {
                daoUtil.setString( ++nIndex, "%" + feuilleDeTourneeFilter.getNom( ) + "%" );
            }

            if ( isFilterByDateCreationDebut )
            {
                daoUtil.setDate( ++nIndex, DateUtil.formatDateSql( feuilleDeTourneeFilter.getDateCreationDebut( ), Locale.FRENCH ) );
            }

            if ( isFilterByDateCreationFin )
            {
                daoUtil.setDate( ++nIndex, DateUtil.formatDateSql( feuilleDeTourneeFilter.getDateCreationFin( ), Locale.FRENCH ) );
            }
        }
    }

    /**
     * Gets the noms existant.
     *
     * @return the noms existant
     */
    public List<String> getNomsExistant( )
    {
        List<String> nomsExistant = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_LOAD_NAMES ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                nomsExistant.add( daoUtil.getString( 1 ) );
            }
        }

        return nomsExistant;
    }

    public FeuilleDeTournee findFDTByName( String nomFDT )
    {
        FeuilleDeTournee feuilleDeTournee = new FeuilleDeTournee( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_LOAD_BY_NAME );
        daoUtil.setString( 1, nomFDT );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            feuilleDeTournee = fillFeuilleDeTournee( daoUtil, false );
        }
        daoUtil.close( );

        return feuilleDeTournee;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer updateByName( FeuilleDeTournee feuilleDeTournee )
    {
        List<String> newList = feuilleDeTournee.getListSignalementIds( ).stream( ).map( String::valueOf ).collect( Collectors.toList( ) );
        int idFeuilleDeTournee = -1;

        try ( DAOUtil daoUtil = new DAOUtil( MessageFormat.format( SQL_QUERY_UPDATE_BY_NAME, "{" + StringUtils.join( newList.toArray( ), "," ) + "}" ) ) ; )
        {
            int nIndex = 0;

            daoUtil.setTimestamp( ++nIndex, new Timestamp( System.currentTimeMillis( ) ) );
            daoUtil.setString( ++nIndex, feuilleDeTournee.getCommentaire( ) );
            daoUtil.setString( ++nIndex, feuilleDeTournee.getNom( ) );

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                idFeuilleDeTournee = daoUtil.getInt( 1 );
            }
        }
        return idFeuilleDeTournee;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( int nFeuilleDeTourneeId )
    {

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nFeuilleDeTourneeId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteOldFeuilleDeTournee( int jourDelaiSuppression )
    {
        try ( DAOUtil daoUtil = new DAOUtil( MessageFormat.format( SQL_QUERY_DELETE_OLD, jourDelaiSuppression ) ) )
        {
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FeuilleDeTournee> findAll( )
    {
        List<FeuilleDeTournee> feuilleDeTourneeList = new ArrayList<>( );

        String query = SQL_QUERY_SELECT_ALL;

        try ( DAOUtil daoUtil = new DAOUtil( query ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                FeuilleDeTournee feuilleDeTournee = fillFeuilleDeTournee( daoUtil, false );
                feuilleDeTourneeList.add( feuilleDeTournee );
            }
        }

        return feuilleDeTourneeList;
    }

    @Override
    public void updateMap( int idFeuilleDeTournee, String base64map )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_MAP ) )
        {

            daoUtil.setString( 1, base64map );
            daoUtil.setInt( 2, idFeuilleDeTournee );

            daoUtil.executeUpdate( );
        }

    }
}
