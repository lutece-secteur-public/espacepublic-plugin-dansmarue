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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementSuiviDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Priorite;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementSuivi;
import fr.paris.lutece.plugins.dansmarue.business.entities.SiraUser;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.business.sector.Sector;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * The Class SignalementSuiviDAO.
 */
public class SignalementSuiviDAO implements ISignalementSuiviDAO
{

    /** The Constant SQL_QUERY_NEW_PK. */
    private static final String SQL_QUERY_NEW_PK = "SELECT nextval('seq_signalement_suivi_id')";

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_suivi(id_suivi, fk_id_signalement, fk_user_guid) VALUES (?,?,?)";

    /** The Constant SQL_QUERY_DELETE. */
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_suivi WHERE id_suivi = ?";

    /** The Constant SQL_QUERY_SELECT. */
    private static final String SQL_QUERY_SELECT = "SELECT id_suivi, fk_id_signalement, fk_user_guid FROM signalement_suivi WHERE id_suivi = ?";

    /** The Constant SQL_QUERY_UPDATE. */
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_suivi SET fk_id_signalement=?, fk_user_guid=? WHERE id_suivi=?";

    /** The Constant SQL_QUERY_SELECT_USERS_MOBILES_BY_ID_SIGNALEMENT. */
    private static final String SQL_QUERY_SELECT_USERS_MOBILES_BY_ID_SIGNALEMENT = "SELECT id_sira_user, user_guid, user_udid, user_device, user_email, user_token FROM sira_user INNER JOIN signalement_suivi ON fk_user_guid = user_guid WHERE fk_id_signalement = ?";

    /** The Constant SQL_QUERY_SELECT_FOLLOWERS_MOBILES_BY_ID_SIGNALEMENT. */
    private static final String SQL_QUERY_SELECT_FOLLOWERS_MOBILES_BY_ID_SIGNALEMENT = "SELECT id_sira_user, user_guid, user_udid, user_device, user_email, user_token FROM sira_user INNER JOIN signalement_suivi ON fk_user_guid = user_guid WHERE fk_id_signalement = ? and (select guid from signalement_signaleur where fk_id_signalement=?) != user_guid";

    /** The Constant SQL_QUERY_SELECT_CREATOR_MOBILES_BY_ID_SIGNALEMENT. */
    private static final String SQL_QUERY_SELECT_CREATOR_MOBILES_BY_ID_SIGNALEMENT = "SELECT id_sira_user, user_guid, user_udid, user_device, user_email, user_token FROM sira_user INNER JOIN signalement_suivi ON fk_user_guid = user_guid WHERE fk_id_signalement = ? and (select guid from signalement_signaleur where fk_id_signalement=?) = user_guid";

    /** The Constant SQL_QUERY_SELECT_BY_ID_SIGNALEMENT_USER_GUID. */
    private static final String SQL_QUERY_SELECT_BY_ID_SIGNALEMENT_USER_GUID = "SELECT id_suivi FROM signalement_suivi WHERE fk_id_signalement=? AND fk_user_guid=?";

    /** The Constant SQL_QUERY_SELECT_SIGNALEMENTS_RESOLVED_BY_GUID. */
    private static final String SQL_QUERY_SELECT_SIGNALEMENTS_RESOLVED_BY_GUID = "select id_signalement, suivi, date_creation, date_prevue_traitement, commentaire, annee,mois, numero, prefix, fk_id_priorite,fk_id_arrondissement,fk_id_type_signalement,fk_id_sector,is_doublon,token,service_fait_date_passage,felicitations,date_mise_surveillance,date_rejet,courriel_destinataire,courriel_expediteur,courriel_date,is_send_ws,commentaire_agent_terrain,id_adresse, adresse, ST_X(geom), ST_Y(geom), precision_localisation "
            + "from signalement_suivi join workflow_resource_workflow w on w.id_resource = signalement_suivi.fk_id_signalement join signalement_signalement signalement on signalement.id_signalement =  signalement_suivi.fk_id_signalement join signalement_adresse adresse on adresse.fk_id_signalement = signalement_suivi.fk_id_signalement WHERE fk_user_guid = ? and id_state in (10, 11, 12, 22) order by id_suivi desc limit ?";

    /** The Constant SQL_QUERY_SELECT_SIGNALEMENTS_NOT_RESOLVED_BY_GUID. */
    private static final String SQL_QUERY_SELECT_SIGNALEMENTS_NOT_RESOLVED_BY_GUID = "select id_signalement, suivi, date_creation, date_prevue_traitement, commentaire, annee,mois, numero, prefix, fk_id_priorite,fk_id_arrondissement,fk_id_type_signalement,fk_id_sector,is_doublon,token,service_fait_date_passage,felicitations,date_mise_surveillance,date_rejet,courriel_destinataire,courriel_expediteur,courriel_date,is_send_ws,commentaire_agent_terrain,id_adresse, adresse, ST_X(geom), ST_Y(geom), precision_localisation "
            + "from signalement_suivi join workflow_resource_workflow w on w.id_resource = signalement_suivi.fk_id_signalement join signalement_signalement signalement on signalement.id_signalement =  signalement_suivi.fk_id_signalement join signalement_adresse adresse on adresse.fk_id_signalement = signalement_suivi.fk_id_signalement WHERE fk_user_guid = ? and id_state not in (10, 11, 12, 22) order by id_suivi desc limit ?";

    /** The Constant SQL_QUERY_SELECT_USERS_MAIL_BY_ID_SIGNALEMENT. */
    private static final String SQL_QUERY_SELECT_USERS_MAIL_BY_ID_SIGNALEMENT = "SELECT DISTINCT user_email FROM sira_user INNER JOIN signalement_suivi ON fk_user_guid = user_guid WHERE fk_id_signalement = ?";

    /** The Constant SQL_QUERY_SELECT_FOLLOWERS_COUNT_BY_ID_SIGNALEMENT. */
    private static final String SQL_QUERY_SELECT_FOLLOWERS_COUNT_BY_ID_SIGNALEMENT = "SELECT COUNT(*) FROM signalement_suivi WHERE fk_id_signalement=?";

    /** The Constant SQL_QUERY_SELECT_FOLLOWERS_GUID_BY_ID_RESOURCE. */
    private static final String SQL_QUERY_SELECT_FOLLOWERS_GUID_BY_ID_RESOURCE = "SELECT fk_user_guid FROM signalement_suivi WHERE fk_id_signalement = ?";

    /** The Constant SQL_QUERY_DELETE_BY_ID_SIGNALEMENT. */
    private static final String SQL_QUERY_DELETE_BY_ID_SIGNALEMENT = "DELETE FROM signalement_suivi WHERE fk_id_signalement = ?";

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
    public Long insert( SignalementSuivi signalementSuivi )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) )
        {
            if ( ( signalementSuivi.getId( ) == null ) || ( signalementSuivi.getId( ) == 0 ) )
            {
                signalementSuivi.setId( newPrimaryKey( ) );
            }
            int nIndex = 1;

            daoUtil.setLong( nIndex++, signalementSuivi.getId( ) );
            daoUtil.setLong( nIndex++, signalementSuivi.getIdSignalement( ) );
            daoUtil.setString( nIndex, signalementSuivi.getUserGuid( ) );

            daoUtil.executeUpdate( );

            return signalementSuivi.getId( );
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
    public SignalementSuivi load( long lId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );

        int nIndex = 1;

        daoUtil.setLong( nIndex, lId );

        daoUtil.executeQuery( );

        SignalementSuivi signalementSuivi = null;

        if ( daoUtil.next( ) )
        {
            nIndex = 1;
            signalementSuivi = new SignalementSuivi( );
            signalementSuivi.setId( daoUtil.getLong( nIndex++ ) );
            signalementSuivi.setIdSignalement( daoUtil.getLong( nIndex++ ) );
            signalementSuivi.setUserGuid( daoUtil.getString( nIndex ) );
        }

        daoUtil.close( );
        return signalementSuivi;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update( SignalementSuivi signalementSuivi )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        int nIndex = 1;

        daoUtil.setLong( nIndex++, signalementSuivi.getIdSignalement( ) );
        daoUtil.setString( nIndex++, signalementSuivi.getUserGuid( ) );

        // WHERE
        daoUtil.setLong( nIndex, signalementSuivi.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SiraUser> findUsersMobileByIdSignalement( long idSignalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USERS_MOBILES_BY_ID_SIGNALEMENT );
        int nIndex = 1;

        daoUtil.setLong( nIndex, idSignalement );

        daoUtil.executeQuery( );

        List<SiraUser> siraUserList = new ArrayList<>( );
        while ( daoUtil.next( ) )
        {
            nIndex = 1;
            SiraUser siraUser = new SiraUser( );
            siraUser.setId( daoUtil.getLong( nIndex++ ) );
            siraUser.setGuid( daoUtil.getString( nIndex++ ) );
            siraUser.setUdid( daoUtil.getString( nIndex++ ) );
            siraUser.setDevice( daoUtil.getString( nIndex++ ) );
            siraUser.setMail( daoUtil.getString( nIndex++ ) );
            siraUser.setToken( daoUtil.getString( nIndex ) );

            siraUserList.add( siraUser );
        }
        daoUtil.close( );
        return siraUserList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SiraUser> findFollowersMobilesByIdSignalement( long idSignalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_FOLLOWERS_MOBILES_BY_ID_SIGNALEMENT );
        int nIndex = 1;

        daoUtil.setLong( nIndex++, idSignalement );
        daoUtil.setLong( nIndex, idSignalement );

        daoUtil.executeQuery( );

        List<SiraUser> siraUserList = new ArrayList<>( );
        while ( daoUtil.next( ) )
        {
            nIndex = 1;
            SiraUser siraUser = new SiraUser( );
            siraUser.setId( daoUtil.getLong( nIndex++ ) );
            siraUser.setGuid( daoUtil.getString( nIndex++ ) );
            siraUser.setUdid( daoUtil.getString( nIndex++ ) );
            siraUser.setDevice( daoUtil.getString( nIndex++ ) );
            siraUser.setMail( daoUtil.getString( nIndex++ ) );
            siraUser.setToken( daoUtil.getString( nIndex ) );

            siraUserList.add( siraUser );
        }
        daoUtil.close( );
        return siraUserList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SiraUser> findCreatorMobilesByIdSignalement( long idSignalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CREATOR_MOBILES_BY_ID_SIGNALEMENT );
        int nIndex = 1;

        daoUtil.setLong( nIndex++, idSignalement );
        daoUtil.setLong( nIndex, idSignalement );

        daoUtil.executeQuery( );

        List<SiraUser> siraUserList = new ArrayList<>( );
        while ( daoUtil.next( ) )
        {
            nIndex = 1;
            SiraUser siraUser = new SiraUser( );
            siraUser.setId( daoUtil.getLong( nIndex++ ) );
            siraUser.setGuid( daoUtil.getString( nIndex++ ) );
            siraUser.setUdid( daoUtil.getString( nIndex++ ) );
            siraUser.setDevice( daoUtil.getString( nIndex++ ) );
            siraUser.setMail( daoUtil.getString( nIndex++ ) );
            siraUser.setToken( daoUtil.getString( nIndex ) );

            siraUserList.add( siraUser );
        }
        daoUtil.close( );
        return siraUserList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> findUsersMailByIdSignalement( long idSignalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USERS_MAIL_BY_ID_SIGNALEMENT );
        int nIndex = 1;

        daoUtil.setLong( nIndex, idSignalement );

        daoUtil.executeQuery( );

        List<String> followersMails = new ArrayList<>( );
        while ( daoUtil.next( ) )
        {
            nIndex = 1;
            String mail = daoUtil.getString( nIndex );
            if ( !StringUtils.isBlank( mail ) )
            {
                followersMails.add( mail );
            }
        }
        daoUtil.close( );
        return followersMails;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long findByIdSignalementAndGuid( long idSignalement, String guid )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ID_SIGNALEMENT_USER_GUID );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, idSignalement );
        daoUtil.setString( nIndex, guid );

        daoUtil.executeQuery( );

        long idSuivi = -1;

        if ( daoUtil.next( ) )
        {
            nIndex = 1;
            idSuivi = daoUtil.getLong( nIndex );
        }

        daoUtil.close( );

        return idSuivi;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Signalement> findSignalementsByGuid( String guid, boolean isResolved )
    {
        DAOUtil daoUtil;
        List<Signalement> signalements = new ArrayList<>( );

        if ( isResolved )
        {
            daoUtil = new DAOUtil( SQL_QUERY_SELECT_SIGNALEMENTS_RESOLVED_BY_GUID );
        }
        else
        {
            daoUtil = new DAOUtil( SQL_QUERY_SELECT_SIGNALEMENTS_NOT_RESOLVED_BY_GUID );
        }
        int nIndex = 0;
        daoUtil.setString( ++nIndex, guid );

        daoUtil.setInt( ++nIndex, Integer.parseInt( DatastoreService.getDataValue( "sitelabels.site_property.mobile.limitationNbAnoMonEspace", "100" ) ) );

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            signalements.add( fillSignalement( daoUtil ) );
        }
        daoUtil.close( );
        return signalements;
    }

    /**
     * Fill Signalement entite.
     *
     * @param daoUtil
     *            DaoUtil
     * @return the signalement
     */
    private Signalement fillSignalement( DAOUtil daoUtil )
    {

        int nIndex = 0;
        Signalement signalement = new Signalement( );

        // Fill signalement
        signalement.setId( daoUtil.getLong( ++nIndex ) );
        signalement.setSuivi( daoUtil.getInt( ++nIndex ) );
        signalement.setDateCreation( DateUtils.getDateFr( daoUtil.getDate( ++nIndex ) ) );
        signalement.setHeureCreation( daoUtil.getTimestamp( nIndex ) );
        signalement.setDatePrevueTraitement( DateUtils.getDateFr( daoUtil.getDate( ++nIndex ) ) );
        signalement.setCommentaire( daoUtil.getString( ++nIndex ) );
        signalement.setAnnee( daoUtil.getInt( ++nIndex ) );
        signalement.setMois( daoUtil.getString( ++nIndex ) );
        signalement.setNumero( daoUtil.getInt( ++nIndex ) );
        signalement.setPrefix( daoUtil.getString( ++nIndex ) );

        if ( daoUtil.getLong( ++nIndex ) > 0 )
        {
            Priorite priorite = new Priorite( );
            priorite.setId( daoUtil.getLong( nIndex ) );
            signalement.setPriorite( priorite );
        }
        if ( daoUtil.getLong( ++nIndex ) > 0 )
        {
            Arrondissement arrondissement = new Arrondissement( );
            arrondissement.setId( daoUtil.getLong( nIndex ) );
            signalement.setArrondissement( arrondissement );
        }
        if ( daoUtil.getLong( ++nIndex ) > 0 )
        {
            TypeSignalement typeSignalement = new TypeSignalement( );
            typeSignalement.setId( daoUtil.getInt( nIndex ) );
            signalement.setTypeSignalement( typeSignalement );
        }
        if ( daoUtil.getInt( ++nIndex ) > 0 )
        {
            Sector sector = new Sector( );
            sector.setIdSector( daoUtil.getInt( nIndex ) );
            signalement.setSecteur( sector );
        }

        signalement.setIsDoublon( daoUtil.getBoolean( ++nIndex ) );
        signalement.setToken( daoUtil.getString( ++nIndex ) );
        Date serviceFaitTraitement = daoUtil.getTimestamp( ++nIndex );

        if ( serviceFaitTraitement != null )
        {
            signalement.setDateServiceFaitTraitement( DateUtils.getDateFr( serviceFaitTraitement ) );
            signalement.setHeureServiceFaitTraitement( DateUtils.getHourFrSansColonne( serviceFaitTraitement ) );
        }
        signalement.setFelicitations( daoUtil.getInt( ++nIndex ) );
        Date miseEnSurveillance = daoUtil.getTimestamp( ++nIndex );

        if ( miseEnSurveillance != null )
        {
            signalement.setDateMiseEnSurveillance( DateUtils.getDateFr( miseEnSurveillance ) );
        }
        Date rejet = daoUtil.getTimestamp( ++nIndex );

        if ( rejet != null )
        {
            signalement.setDateRejet( DateUtils.getDateFr( rejet ) );
        }
        signalement.setCourrielDestinataire( daoUtil.getString( ++nIndex ) );
        signalement.setCourrielExpediteur( daoUtil.getString( ++nIndex ) );
        signalement.setCourrielDate( daoUtil.getTimestamp( ++nIndex ) );
        signalement.setSendWs( daoUtil.getBoolean( ++nIndex ) );
        signalement.setCommentaireAgentTerrain( daoUtil.getString( ++nIndex ) );

        // Fill adresse
        Adresse adresse = new Adresse( );

        adresse.setId( daoUtil.getLong( ++nIndex ) );
        adresse.setAdresse( daoUtil.getString( ++nIndex ) );
        adresse.setLng( daoUtil.getDouble( ++nIndex ) );
        adresse.setLat( daoUtil.getDouble( ++nIndex ) );
        adresse.setPrecisionLocalisation( daoUtil.getString( ++nIndex ) );
        List<Adresse> adresses = new ArrayList<>( );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        return signalement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getNbFollowersByIdSignalement( long idSignalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_FOLLOWERS_COUNT_BY_ID_SIGNALEMENT );
        int nIndex = 1;

        daoUtil.setLong( nIndex, idSignalement );

        int count = 0;

        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            nIndex = 1;
            count = daoUtil.getInt( nIndex );
        }

        daoUtil.close( );

        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeByIdSignalement( long idSignalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_ID_SIGNALEMENT );
        int nIndex = 1;
        daoUtil.setLong( nIndex, idSignalement );

        daoUtil.executeUpdate( );

        daoUtil.close( );
    }

    /**
     * Gets the all suiveurs guid.
     *
     * @param idRessource
     *            the id ressource
     * @return the all suiveurs guid
     */
    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementSuiviDAO#getAllSuiveursGuid(java.lang.Integer)
     */
    @Override
    public List<String> getAllSuiveursGuid( Integer idRessource )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_FOLLOWERS_GUID_BY_ID_RESOURCE );

        daoUtil.setInt( 1, idRessource );

        daoUtil.executeQuery( );

        int nIndex;
        List<String> followersGuid = new ArrayList<>( );

        while ( daoUtil.next( ) )
        {
            nIndex = 1;
            followersGuid.add( daoUtil.getString( nIndex ) );
        }

        daoUtil.close( );

        return followersGuid;
    }

}
