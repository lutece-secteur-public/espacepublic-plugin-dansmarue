package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.DashboardPeriod;
import fr.paris.lutece.plugins.dansmarue.business.entities.EtatSignalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Priorite;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementDashboardFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.commons.Order;
import fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties;
import fr.paris.lutece.plugins.dansmarue.service.dto.DashboardSignalementDTO;
import fr.paris.lutece.plugins.dansmarue.service.dto.DossierSignalementDTO;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.plugins.unittree.modules.sira.business.sector.Sector;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * The Class SignalementDAO.
 */
public class SignalementDAO implements ISignalementDAO
{

    /** The Constant SQL_QUERY_NEW_PK. */
    private static final String SQL_QUERY_NEW_PK                                   = "SELECT nextval('seq_signalement_signalement_id_signalement')";

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT                                   = "INSERT INTO signalement_signalement (id_signalement, suivi, date_creation, commentaire, annee, mois, numero, PREFIX, fk_id_priorite, fk_id_type_signalement, fk_id_arrondissement, fk_id_sector, heure_creation, is_doublon, token) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /** The Constant SQL_QUERY_DELETE. */
    private static final String SQL_QUERY_DELETE                                   = "DELETE FROM signalement_signalement WHERE id_signalement = ?";

    /** The Constant SQL_QUERY_SELECT. */
    private static final String SQL_QUERY_SELECT                                   = "SELECT id_signalement, suivi, date_creation, date_prevue_traitement, commentaire, annee, mois, numero, prefix, fk_id_priorite, fk_id_arrondissement, fk_id_type_signalement, fk_id_sector, heure_creation, is_doublon, token, service_fait_date_passage, felicitations FROM signalement_signalement WHERE id_signalement = ?";

    /** The Constant SQL_QUERY_SELECT_BY_STATUS. */
    private static final String SQL_QUERY_SELECT_BY_STATUS                         = "SELECT signalement.id_signalement, signalement.suivi, signalement.date_creation, signalement.date_prevue_traitement, signalement.commentaire, signalement.annee,  signalement.mois, signalement.numero, signalement.prefix, signalement.fk_id_priorite,   signalement.fk_id_arrondissement,  signalement.fk_id_type_signalement,  signalement.fk_id_sector,   signalement.is_doublon, signalement.service_fait_date_passage FROM signalement_signalement AS signalement  INNER JOIN workflow_resource_workflow AS resource_workflow ON resource_workflow.id_resource = signalement.id_signalement  INNER JOIN workflow_resource_history AS resource_history ON resource_history.id_resource = signalement.id_signalement INNER JOIN workflow_action AS action ON action.id_action = resource_history.id_action WHERE resource_workflow.resource_type = ''SIGNALEMENT_SIGNALEMENT''  AND resource_history.resource_type = ''SIGNALEMENT_SIGNALEMENT''  AND resource_workflow.id_state = ? AND action.id_state_after = ? AND resource_history.creation_date + ''{0} days''::interval < now();";
    /** The Constant SQL_QUERY_UPDATE. */
    private static final String SQL_QUERY_UPDATE                                   = "UPDATE signalement_signalement SET id_signalement=?, suivi=?, date_creation=?, date_prevue_traitement=?, commentaire=? , fk_id_priorite=?, fk_id_type_signalement=?, fk_id_arrondissement = ?, fk_id_sector = ?, is_doublon = ?, service_fait_date_passage = ? WHERE id_signalement=?";

    /** The Constant SQL_QUERY_SELECT_ALL. */
    private static final String SQL_QUERY_SELECT_ALL                               = "SELECT id_signalement, suivi, date_creation, date_prevue_traitement, commentaire, annee, mois, numero, prefix, fk_id_priorite, fk_id_arrondissement, fk_id_type_signalement, fk_id_sector, is_doublon FROM signalement_signalement";

    /** The Constant SQL_QUERY_ADD_FILTER_SECTOR_ALLOWED. */
    private static final String SQL_QUERY_ADD_FILTER_SECTOR_ALLOWED                = " fk_id_sector IN (SELECT s.id_sector FROM unittree_sector s INNER JOIN unittree_unit_sector u ON s.id_sector = u.id_sector WHERE u.id_unit in ({0}))";

    /** The Constant SQL_QUERY_SELECT_SIGNALEMENT_BY_TOKEN. */
    private static final String SQL_QUERY_SELECT_SIGNALEMENT_BY_TOKEN              = "SELECT id_signalement, token FROM signalement_signalement WHERE token = ?";
    
    /** SQL QUERY FOR WebServicePartnerDaemon */
    private static final String SQL_QUERY_SELECT_ID_SIGNALEMENT_FOR_PARTNER_DEAMON = "SELECT id_signalement FROM signalement_signalement, workflow_resource_workflow  WHERE id_signalement = id_resource AND id_state =? ORDER BY id_signalement DESC";

    // FOR THE FILTERS
    /** The Constant SQL_QUERY_PART_SELECT. */
    private static final String SQL_QUERY_PART_SELECT                              = " signalement.id_signalement, priorite.libelle, signalement.fk_id_type_signalement ";

    /** The Constant SQL_QUERY_FROM_ALL. */
    private static final String SQL_QUERY_FROM_ALL                                 = " FROM signalement_signalement AS signalement INNER JOIN signalement_priorite AS priorite ON priorite.id_priorite = signalement.fk_id_priorite INNER JOIN signalement_adresse AS adr ON signalement.id_signalement = adr.fk_id_signalement INNER JOIN workflow_resource_workflow AS workflow ON workflow.id_resource = signalement.id_signalement INNER JOIN unittree_unit_sector unit_sector ON unit_sector.id_sector = signalement.fk_id_sector INNER JOIN unittree_unit direction_unit ON direction_unit.id_unit = unit_sector.id_unit AND direction_unit.id_parent = 0";

    private static final String SQL_QUERY_FROM_SIGNALEMENT_TYPE                    = " INNER JOIN signalement_type_signalement AS type ON type.id_type_signalement = signalement.fk_id_type_signalement ";

    private static final String SQL_QUERY_FROM_VIEW_TYPE_SIGNALEMENT_W_PARENT_LINK = " INNER JOIN v_signalement_type_signalement_with_parents_links AS vstswpl ON vstswpl.id_type_signalement= signalement.fk_id_type_signalement ";

    private static final String SQL_QUERY_FROM_SIGNALEMENT_CATEGORY                = " INNER JOIN v_signalement_type_signalement_with_parents_links AS vstswp ON vstswp.id_type_signalement = signalement.fk_id_type_signalement AND vstswp.is_parent_a_category=1";

    private static final String SQL_QUERY_FROM_UNITTREE                            = " INNER JOIN unittree_unit_sector uus ON signalement.fk_id_sector = uus.id_sector ";

    private static final String SQL_QUERY_FROM_SIGNALEUR                           = " LEFT OUTER JOIN signalement_signaleur AS signaleur ON signalement.id_signalement = signaleur.fk_id_signalement ";

    /** The Constant SQL_QUERY_ADD_FILTER_LIST_TYPE_SIGNALEMENT. */
    private static final String SQL_QUERY_ADD_FILTER_LIST_TYPE_SIGNALEMENT         = " signalement.fk_id_type_signalement IN ({0}) ";

    /** The Constant SQL_QUERY_ADD_FILTER_TYPE_SIGNALEMENT. */
    private static final String SQL_QUERY_ADD_FILTER_TYPE_SIGNALEMENT              = " vstswpl.id_parent = ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_NUMERO. */
    private static final String SQL_QUERY_ADD_FILTER_NUMERO                        = " signalement.prefix || signalement.annee || signalement.mois || signalement.numero LIKE ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_DIRECTION. */
    private static final String SQL_QUERY_ADD_FILTER_DIRECTION                     = " uus.id_unit = ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_LIST_ARRONDISSEMENT. */
    private static final String SQL_QUERY_ADD_FILTER_LIST_ARRONDISSEMENT           = " signalement.fk_id_arrondissement IN ({0}) ";

    /** The Constant SQL_QUERY_ADD_FILTER_LIST_QUARTIER. */
    private static final String SQL_QUERY_ADD_FILTER_LIST_QUARTIER                 = " ST_Intersects(adr.geom::geometry, (select ST_Union(geom) from signalement_conseil_quartier  where id_consqrt in ({0}))::geometry) ";

    /** The Constant SQL_QUERY_ADD_FILTER_ARRONDISSEMENT. */
    private static final String SQL_QUERY_ADD_FILTER_ARRONDISSEMENT                = " signalement.fk_id_arrondissement = ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_SECTOR. */
    private static final String SQL_QUERY_ADD_FILTER_SECTOR                        = " signalement.fk_id_sector = ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_ETAT. */
    private static final String SQL_QUERY_ADD_FILTER_ETAT                          = " workflow.id_state = ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_ADRESSE. */
    private static final String SQL_QUERY_ADD_FILTER_ADRESSE                       = "lower_unaccent(adr.adresse) LIKE lower_unaccent(?) ";

    /** The Constant SQL_QUERY_ADD_FILTER_MAIL. */
    private static final String SQL_QUERY_ADD_FILTER_MAIL                          = "lower_unaccent(signaleur.mail) LIKE lower_unaccent(?) ";

    /** The Constant SQL_QUERY_ADD_FILTER_COMMENTAIRE. */
    private static final String SQL_QUERY_ADD_FILTER_COMMENTAIRE                   = " signalement.commentaire LIKE ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_DATE_BEGIN. */
    private static final String SQL_QUERY_ADD_FILTER_DATE_BEGIN                    = "signalement.date_creation >= ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_DATE_END. */
    private static final String SQL_QUERY_ADD_FILTER_DATE_END                      = "signalement.date_creation <= ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_CATEGORY. */
    private static final String SQL_QUERY_ADD_FILTER_CATEGORY                      = " vstswp.id_parent IN ({0}) ";

    /** The Constant SQL_QUERY_SELECT_ALL_ID_SIGNALEMENT. */
    private static final String SQL_QUERY_SELECT_ALL_ID_SIGNALEMENT                = "select id_signalement from signalement_signalement";

    /** The Constant SQL_UPDATE_SUIVI_PLUS_ONE. */
    private static final String SQL_UPDATE_SUIVI_PLUS_ONE                          = "UPDATE signalement_signalement SET suivi=suivi+1 WHERE id_signalement=?";

    /** The Constant SQL_UPDATE_SUIVI_PLUS_ONE. */
    private static final String SQL_UPDATE_SUIVI_MINUS_ONE                         = "UPDATE signalement_signalement SET suivi=suivi-1 WHERE id_signalement=?";

    /** The Constant SQL_QUERY_FELICITATIONS_INCREMENT **/
    private static final String SQL_QUERY_FELICITATIONS_INCREMENT                  = "UPDATE signalement_signalement SET felicitations=felicitations+1 WHERE id_signalement=?";

    /** The Constant SQL_SELECT_BY_ID_TELEPHONE. */
    private static final String SQL_QUERY_SELECT_BY_ID_TELEPHONE                   = "SELECT fk_id_signalement FROM signalement_signaleur WHERE id_telephone=?";

    /** The Constant SQL_QUERY_HAS_SIGNALEMENT_FOR_ROAD_MAP. */
    private static final String SQL_QUERY_HAS_SIGNALEMENT_FOR_ROAD_MAP             = "SELECT DISTINCT signalement.numero AS numero FROM signalement_signalement AS signalement INNER JOIN unittree_sector AS sector ON sector.id_sector = signalement.fk_id_sector INNER JOIN workflow_resource_workflow AS workflow ON workflow.id_resource=signalement.id_signalement INNER JOIN unittree_unit_sector AS unit_sector ON sector.id_sector=unit_sector.id_sector WHERE (workflow.id_state = 8 OR workflow.id_state=9) AND signalement.fk_id_type_signalement IN  ('1000','1001','1002','1003','1004','1005','1007','1009','1010') AND unit_sector.id_unit = 108 AND signalement.fk_id_sector IN ( ";

    /** The Constant SQL_QUERY_INSERT_MESSAGE_CREATION. */
    private static final String SQL_QUERY_INSERT_MESSAGE_CREATION                  = "INSERT INTO signalement_message_creation VALUES (1,?)";

    /** The Constant SQL_QUERY_UPDATE_MESSAGE_CREATION. */
    private static final String SQL_QUERY_UPDATE_MESSAGE_CREATION                  = "UPDATE signalement_message_creation SET contenu=? WHERE id_message=1";

    /** The Constant SQL_QUERY_SELECT_MESSAGE_CREATION. */
    private static final String SQL_QUERY_SELECT_MESSAGE_CREATION                  = "SELECT contenu FROM signalement_message_creation WHERE id_message=1";

    /** The Constant SQL_QUERY_DELETE_MESSAGE_CREATION. */
    private static final String SQL_QUERY_DELETE_MESSAGE_CREATION                  = "DELETE FROM signalement_message_creation WHERE id_message=1";
    
    /** The Constant SQL_QUERY_UPDATE_DATE_MISE_SURVEILLANCE. */
    private static final String SQL_QUERY_UPDATE_DATE_MISE_SURVEILLANCE            = "UPDATE signalement_signalement SET date_mise_surveillance =? WHERE id_signalement=?";

    /** The Constant SQL_WHERE. */
    private static final String SQL_WHERE                                          = " WHERE ";

    /** The Constant SQL_AND. */
    private static final String SQL_AND                                            = " AND ";

    /** The Constant SQL_OR. */
    private static final String SQL_OR                                             = " OR ";

    /** The Constant SQL_GROUP_BY. */
    private static final String SQL_GROUP_BY                                       = " GROUP BY ";

    /** The Constant SQL_SELECT. */
    private static final String SQL_SELECT                                         = "SELECT ";

    private static final String SQL_SELECT_COUNT_ID_SIGNALEMENT                    = "SELECT COUNT (DISTINCT signalement.id_signalement) ";

    private static final String SQL_SELECT_ID_SIGNALEMENT                          = "SELECT DISTINCT signalement.id_signalement";

    /** The Constant COMA_SEPARATOR. */
    private static final String COMA_SEPARATOR                                     = ", ";

    /** The Constant CLOSED_BRACKET. */
    private static final String CLOSED_BRACKET                                     = " ) ";

    /** The Constant DISTINCT */
    private static final String SQL_DISTINCT                                       = " DISTINCT ";

    /**
     * Makes references between client sort keyword and actual sql joins / columns names.
     */
    private Map<String, String> _ordersMap;

    /**
     * Instantiates a new signalement dao.
     */
    public SignalementDAO( )
    {
        super( );

        // Initialise la liste des clé / valeur utilisé pour le tri des colonnes
        // par soucis de non régression les clé actuelles sont les les chemins directs vers les attributs utilisés précédement.
        _ordersMap = new HashMap<String, String>( );
        _ordersMap.put( "signalement.suivi", "signalement.suivi" );
        _ordersMap.put( "numSignalement", "numSignalement" );
        _ordersMap.put( "signaleur.mail", "signaleur.mail" );
        _ordersMap.put( "priorite.libelle", "priorite.libelle" );
        _ordersMap.put( "type.libelle", "type.libelle" );
        _ordersMap.put( "direction_unit.label", "direction_unit.label" );
        _ordersMap.put( "adr.adresse", "adr.adresse" );
        _ordersMap.put( "signalement.commentaire", "signalement.commentaire" );
        _ordersMap.put( "signalement.date_creation", "signalement.date_creation" );
        _ordersMap.put( "signalement.heure_creation", "signalement.heure_creation" );
    }

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
        daoUtil.free( );
        return nKey;
    }

    /**
     * Save a new signalement.
     *
     * @param signalement
     *            the signalement object
     * @return new object id
     */
    @Override
    public Long insert( Signalement signalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        if ( ( signalement.getId( ) == null ) || signalement.getId( ).equals( new Long( 0 ) ) )
        {
            signalement.setId( newPrimaryKey( ) );
        }
        int nIndex = 1;
        daoUtil.setLong( nIndex++, signalement.getId( ) );
        daoUtil.setInt( nIndex++, signalement.getSuivi( ) );
        daoUtil.setDate( nIndex++, DateUtil.formatDateSql( signalement.getDateCreation( ), Locale.FRENCH ) );
        daoUtil.setString( nIndex++, signalement.getCommentaire( ) );
        daoUtil.setInt( nIndex++, signalement.getAnnee( ) );
        daoUtil.setString( nIndex++, signalement.getMois( ) );
        daoUtil.setInt( nIndex++, signalement.getNumero( ) );
        daoUtil.setString( nIndex++, signalement.getPrefix( ) );
        daoUtil.setLong( nIndex++, signalement.getPriorite( ).getId( ) );
        daoUtil.setLong( nIndex++, signalement.getTypeSignalement( ).getId( ) );

        Arrondissement arrondissement = signalement.getArrondissement( );
        if ( arrondissement != null )
        {
            daoUtil.setLong( nIndex++, arrondissement.getId( ) );
        } else
        {

            daoUtil.setInt( nIndex++, 0 );

        }
        Sector secteur = signalement.getSecteur( );
        Integer idSector;
        if ( secteur != null )
        {
            idSector = secteur.getIdSector( );
        } else
        {
            idSector = 0;
        }
        daoUtil.setInt( nIndex++, idSector );

        daoUtil.setTimestamp( nIndex++, DateUtils.getCurrentHourAndMinute( ) );
        daoUtil.setBoolean( nIndex++, signalement.isDoublon( ) );
        daoUtil.setString( nIndex++, signalement.getToken( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );

        return signalement.getId( );
    }

    /**
     * Delete a signalement.
     *
     * @param lId
     *            the signalement id
     */
    @Override
    public void remove( long lId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setLong( 1, lId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Load a signalement by id.
     *
     * @param nId
     *            the signalement id
     * @return signalement entity
     */
    @Override
    public Signalement load( long nId )
    {
        Signalement signalement = new Signalement( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setLong( 1, nId );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {

            int nIndex = 1;
            signalement.setId( daoUtil.getLong( nIndex++ ) );
            signalement.setSuivi( daoUtil.getInt( nIndex++ ) );
            signalement.setDateCreation( DateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );
            signalement.setDatePrevueTraitement( DateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );
            signalement.setCommentaire( daoUtil.getString( nIndex++ ) );
            signalement.setAnnee( daoUtil.getInt( nIndex++ ) );
            signalement.setMois( daoUtil.getString( nIndex++ ) );
            signalement.setNumero( daoUtil.getInt( nIndex++ ) );
            signalement.setPrefix( daoUtil.getString( nIndex++ ) );

            if ( daoUtil.getLong( nIndex++ ) > 0 )
            {
                Priorite priorite = new Priorite( );
                priorite.setId( daoUtil.getLong( nIndex - 1 ) );
                signalement.setPriorite( priorite );
            }
            if ( daoUtil.getLong( nIndex++ ) > 0 )
            {
                Arrondissement arrondissement = new Arrondissement( );
                arrondissement.setId( daoUtil.getLong( nIndex - 1 ) );
                signalement.setArrondissement( arrondissement );
            }
            if ( daoUtil.getLong( nIndex++ ) > 0 )
            {
                TypeSignalement typeSignalement = new TypeSignalement( );
                typeSignalement.setId( daoUtil.getInt( nIndex - 1 ) );
                signalement.setTypeSignalement( typeSignalement );
            }
            if ( daoUtil.getInt( nIndex++ ) > 0 )
            {
                Sector sector = new Sector( );
                sector.setIdSector( daoUtil.getInt( nIndex - 1 ) );
                signalement.setSecteur( sector );
            }

            signalement.setHeureCreation( daoUtil.getTimestamp( nIndex++ ) );
            signalement.setIsDoublon( daoUtil.getBoolean( nIndex++ ) );
            signalement.setToken( daoUtil.getString( nIndex++ ) );
            Date serviceFaitTraitement = daoUtil.getTimestamp( nIndex++ );
            if ( serviceFaitTraitement != null )
            {
                signalement.setDateServiceFaitTraitement( DateUtils.getDateFr( serviceFaitTraitement ) );
                signalement.setHeureServiceFaitTraitement( DateUtils.getHourFrSansColonne( serviceFaitTraitement ) );
            }
            signalement.setFelicitations( daoUtil.getInt( nIndex++ ) );

        }

        daoUtil.free( );

        return signalement;
    }

    /**
     * Upadate a signalement.
     *
     * @param signalement
     *            the signalement to update
     */
    @Override
    public void update( Signalement signalement )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, signalement.getId( ) );
        daoUtil.setLong( nIndex++, signalement.getSuivi( ) );
        daoUtil.setDate( nIndex++, DateUtil.formatDateSql( signalement.getDateCreation( ), Locale.FRENCH ) );
        daoUtil.setDate( nIndex++, DateUtil.formatDateSql( signalement.getDatePrevueTraitement( ), Locale.FRENCH ) );
        daoUtil.setString( nIndex++, signalement.getCommentaire( ) );

        daoUtil.setLong( nIndex++, signalement.getPriorite( ).getId( ) );
        daoUtil.setLong( nIndex++, signalement.getTypeSignalement( ).getId( ) );

        daoUtil.setLong( nIndex++, signalement.getArrondissement( ).getId( ) );
        Sector secteur = signalement.getSecteur( );
        Integer idSector;
        if ( secteur != null )
        {
            idSector = secteur.getIdSector( );
        } else
        {
            idSector = 0;
        }
        daoUtil.setInt( nIndex++, idSector );

        daoUtil.setBoolean( nIndex++, signalement.isDoublon( ) );
        if ( StringUtils.isNotBlank( signalement.getDateServiceFaitTraitement( ) )
                && StringUtils.isNotBlank( signalement.getHeureServiceFaitTraitement( ) ) )
        {
            String dateTraitementString = signalement.getDateServiceFaitTraitement( ) + signalement.getHeureServiceFaitTraitement( );
            try
            {
                daoUtil.setTimestamp( nIndex++, DateUtils.formatDateSqlWithTime( dateTraitementString ) );
            } catch ( ParseException e )
            {
                e.printStackTrace( );
            }
        } else
        {
            daoUtil.setDate( nIndex++, null );
        }
        daoUtil.setLong( nIndex++, signalement.getId( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Load a signalement by its id.
     *
     * @param nId
     *            the signalement id
     * @return the signalement
     */
    @Override
    public Signalement loadById( long nId )
    {
        Signalement signalement = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setLong( 1, nId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            signalement = new Signalement( );
            int nIndex = 1;
            signalement.setId( daoUtil.getLong( nIndex++ ) );
            signalement.setSuivi( daoUtil.getInt( nIndex++ ) );
            signalement.setDateCreation( DateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );
            signalement.setDatePrevueTraitement( DateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );
            signalement.setCommentaire( daoUtil.getString( nIndex++ ) );

            signalement.setAnnee( daoUtil.getInt( nIndex++ ) );
            signalement.setMois( daoUtil.getString( nIndex++ ) );
            signalement.setNumero( daoUtil.getInt( nIndex++ ) );
            signalement.setPrefix( daoUtil.getString( nIndex++ ) );

            if ( daoUtil.getLong( nIndex++ ) > 0 )
            {
                Priorite priorite = new Priorite( );
                priorite.setId( daoUtil.getLong( nIndex - 1 ) );
                signalement.setPriorite( priorite );
            }
            if ( daoUtil.getLong( nIndex++ ) > 0 )
            {
                Arrondissement arrondissement = new Arrondissement( );
                arrondissement.setId( daoUtil.getLong( nIndex - 1 ) );
                signalement.setArrondissement( arrondissement );
            }
            if ( daoUtil.getLong( nIndex++ ) > 0 )
            {
                TypeSignalement typeSignalement = new TypeSignalement( );
                typeSignalement.setId( daoUtil.getInt( nIndex - 1 ) );
                signalement.setTypeSignalement( typeSignalement );
            }
            if ( daoUtil.getInt( nIndex++ ) > 0 )
            {
                Sector sector = new Sector( );
                sector.setIdSector( daoUtil.getInt( nIndex - 1 ) );
                signalement.setSecteur( sector );
            }

            signalement.setHeureCreation( daoUtil.getTimestamp( nIndex++ ) );
            signalement.setIsDoublon( daoUtil.getBoolean( nIndex++ ) );
            signalement.setToken( daoUtil.getString( nIndex++ ) );
            Date serviceFaitTraitement = daoUtil.getTimestamp( nIndex++ );
            if ( serviceFaitTraitement != null )
            {
                signalement.setDateServiceFaitTraitement( DateUtils.getDateFr( serviceFaitTraitement ) );
                signalement.setHeureServiceFaitTraitement( DateUtils.getHourFrSansColonne( serviceFaitTraitement ) );
            }
            signalement.setFelicitations( daoUtil.getInt( nIndex++ ) );

        }

        daoUtil.free( );

        return signalement;
    }

    /**
     * Update select part.
     *
     * @param filter
     *            the signalement filter
     * @return the string builder
     */
    private StringBuilder updateSelectPart( SignalementFilter filter )
    {
        StringBuilder sbSQL = new StringBuilder( );
        List<Order> listeOrders = convertOrderToClause( filter.getOrders( ) );
        if ( ( listeOrders != null ) && !listeOrders.isEmpty( ) )
        {
            sbSQL.append( " , " );
            int index = 1;
            for ( Order order : listeOrders )
            {
                if ( index == listeOrders.size( ) )
                {
                    if ( order.getName( ).equals( "numSignalement" ) )
                    {
                        sbSQL.append( "signalement.prefix, signalement.annee, signalement.mois, signalement.numero " );
                    } else
                    {
                        sbSQL.append( order.getName( ) + " " );
                    }
                } else
                {
                    if ( order.getName( ).equals( "numSignalement" ) )
                    {
                        sbSQL.append( "signalement.prefix, signalement.annee, signalement.mois, signalement.numero, " );
                    } else
                    {
                        sbSQL.append( order.getName( ) + ", " );
                    }
                    index++;
                }
            }
        }
        return sbSQL;

    }

    /**
     * remplace les clé de tri par des chemins sql vers des colonnes à trier.
     *
     * @param orders
     *            the orders
     * @return the list
     */
    private List<Order> convertOrderToClause( List<Order> orders )
    {
        List<Order> ret = new ArrayList<Order>( );
        for ( Order order : orders )
        {
            String string = _ordersMap.get( order.getName( ) );
            if ( string != null )
            {
                ret.add( new Order( string, order.getOrder( ) ) );
            }
        }
        return ret;
    }

    /**
     * Build the SQL query with filter.
     *
     * @param filter
     *            the filter
     * @return a SQL query
     */
    private String buildSQLQuery( SignalementFilter filter )
    {
        StringBuilder sbSQL = new StringBuilder( SQL_SELECT );
        sbSQL.append( SQL_DISTINCT );
        sbSQL.append( SQL_QUERY_PART_SELECT );
        sbSQL.append( updateSelectPart( filter ) );
        sbSQL.append( SQL_QUERY_FROM_ALL );

        boolean bHasOrderUnit = false;
        boolean bHasOrderSignalementType = false;
        boolean bHasOrderSignaleur = false;
        for ( Order order : filter.getOrders( ) )
        {
            if ( order.getName( ) != null )
            {
                if ( order.getName( ).startsWith( "unit." ) )
                {
                    bHasOrderUnit = true;
                } else if ( order.getName( ).startsWith( "type." ) )
                {
                    bHasOrderSignalementType = true;
                } else if ( order.getName( ).startsWith( "signaleur." ) )
                {
                    bHasOrderSignaleur = true;
                }
            }
        }

        boolean bHasFilterSignalementType = filter.getIdTypeSignalement( ) > 0;
        boolean bHasFilterUnit = filter.getIdDirection( ) > 0;
        boolean bHasFilerMail = ( filter.getMail( ) != null ) && !StringUtils.isBlank( filter.getMail( ) );
        boolean bHasFilterCategory = !CollectionUtils.isEmpty( filter.getListIdCategories( ) );

        // Units requires type
        if ( bHasFilterSignalementType || bHasOrderSignalementType || bHasFilterUnit || bHasOrderUnit )
        {
            sbSQL.append( SQL_QUERY_FROM_SIGNALEMENT_TYPE );
        }

        if ( bHasFilterSignalementType )
        {
            sbSQL.append( SQL_QUERY_FROM_VIEW_TYPE_SIGNALEMENT_W_PARENT_LINK );
        }

        if ( bHasFilterUnit || bHasOrderUnit )
        {
            sbSQL.append( SQL_QUERY_FROM_UNITTREE );
        }

        if ( bHasFilerMail || bHasOrderSignaleur )
        {
            sbSQL.append( SQL_QUERY_FROM_SIGNALEUR );
        }

        if ( bHasFilterCategory )
        {
            sbSQL.append( SQL_QUERY_FROM_SIGNALEMENT_CATEGORY );
        }

        int nIndex = 1;

        // Type
        if ( bHasFilterSignalementType )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_TYPE_SIGNALEMENT );
        }

        if ( ( filter.getListIdTypeSignalements( ) != null ) && !filter.getListIdTypeSignalements( ).isEmpty( ) )
        {
            int listeLength = filter.getListIdTypeSignalements( ).size( );
            Character[] array = new Character[listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array[i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_LIST_TYPE_SIGNALEMENT, unionQuery ) );
        }

        // Numéro de signalement
        if ( ( filter.getNumero( ) != null ) && !StringUtils.isBlank( filter.getNumero( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_NUMERO );
        }

        // Direction
        if ( bHasFilterUnit )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_DIRECTION );
        }

        // ARRONDISSEMENT
        if ( filter.getIdArrondissement( ) > 0 )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_ARRONDISSEMENT );
        }
        if ( ( filter.getListIdArrondissements( ) != null ) && !filter.getListIdArrondissements( ).isEmpty( ) )
        {
            int listeLength = filter.getListIdArrondissements( ).size( );
            Character[] array = new Character[listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array[i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_LIST_ARRONDISSEMENT, unionQuery ) );
        }

        // QUARTIER
        if ( ( filter.getListIdQuartier( ) != null ) && !filter.getListIdQuartier( ).isEmpty( ) )
        {
            int listeLength = filter.getListIdQuartier( ).size( );
            Character[] array = new Character[listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array[i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_LIST_QUARTIER, unionQuery ) );
        }

        // SECTOR
        if ( filter.getIdSector( ) > 0 )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_SECTOR );
        }

        // Allowed sectors

        if ( !filter.getListIdUnit( ).isEmpty( ) )
        {
            int listeLength = filter.getListIdUnit( ).size( );
            Character[] array = new Character[listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array[i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_SECTOR_ALLOWED, unionQuery ) );
        }

        else
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( "1 = 2" );
        }

        // Adresse
        if ( ( filter.getAdresse( ) != null ) && !StringUtils.isBlank( filter.getAdresse( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_ADRESSE );
        }

        // Mail
        if ( bHasFilerMail )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_MAIL );
        }

        // Commentaire
        if ( ( filter.getCommentaire( ) != null ) && !StringUtils.isBlank( filter.getCommentaire( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_COMMENTAIRE );
        }

        // Date de création
        boolean dateBeginNotEmpty = ( filter.getDateBegin( ) != null ) && !StringUtils.isBlank( filter.getDateBegin( ) );
        boolean dateEndNotEmpty = ( filter.getDateEnd( ) != null ) && !StringUtils.isBlank( filter.getDateEnd( ) );

        if ( dateBeginNotEmpty )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_DATE_BEGIN );
        }

        if ( dateEndNotEmpty )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_DATE_END );
        }

        // ETAT
        // if ( filter.getIdEtat( ) > 0 )
        // {
        // nIndex = this.addSQLWhereOr( false, sbSQL, nIndex );
        // sbSQL.append( SQL_QUERY_ADD_FILTER_ETAT );
        // }

        // ETAT
        if ( filter.getEtats( ) != null )
        {
            int i = filter.getEtats( ).size( );

            for ( EtatSignalement etatSign : filter.getEtats( ) )
            {
                if ( etatSign.getCoche( ) )
                {

                    if ( nIndex == 1 )
                    {
                        sbSQL.append( SQL_WHERE );
                    } else
                    {
                        if ( i == filter.getEtats( ).size( ) )
                        {
                            sbSQL.append( SQL_AND );
                        } else
                        {
                            sbSQL.append( SQL_OR );
                        }

                    }
                    nIndex++;

                    if ( i == filter.getEtats( ).size( ) )
                    {
                        sbSQL.append( " ( " );
                    }
                    sbSQL.append( SQL_QUERY_ADD_FILTER_ETAT );
                    i--;
                    if ( i < 1 )
                    {

                        sbSQL.append( " ) " );
                    }

                }

            }
        }

        // ADDING CATEGORY FILTER
        if ( bHasFilterCategory )
        {
            int listeLength = filter.getListIdCategories( ).size( );
            Character[] array = new Character[listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array[i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_CATEGORY, unionQuery ) );
        }

        // ADD THE GROUP BY
        if ( bHasFilerMail )
        {
            sbSQL.append( SQL_GROUP_BY );
            sbSQL.append( SQL_QUERY_PART_SELECT );
            sbSQL.append( updateSelectPart( filter ) );
        }
        // ADD ORDERS
        List<Order> listeOrders = convertOrderToClause( filter.getOrders( ) );
        if ( ( listeOrders != null ) && !listeOrders.isEmpty( ) )
        {
            sbSQL.append( " ORDER BY " );
            int index = 1;
            for ( Order order : listeOrders )
            {
                if ( "numSignalement".equals( order.getName( ) ) )
                {
                    if ( index == listeOrders.size( ) )
                    {
                        sbSQL.append( " signalement.prefix " + order.getOrder( ) + ", signalement.annee " + order.getOrder( ) + ", signalement.mois "
                                + order.getOrder( ) + ", signalement.numero " + order.getOrder( ) + " " );
                    } else
                    {
                        sbSQL.append( " signalement.prefix " + order.getOrder( ) + ", signalement.annee " + order.getOrder( ) + ", signalement.mois "
                                + order.getOrder( ) + ", signalement.numero " + order.getOrder( ) + ", " );
                        index++;
                    }
                }
                else if ( "signaleur.mail".equals( order.getName( ) ) )
                {
                    if ( index == listeOrders.size( ) )
                    {
                        sbSQL.append( order.getName( ) + " " + order.getOrder( ) + " NULLS LAST " );
                    } else
                    {
                        sbSQL.append( order.getName( ) + " " + order.getOrder( ) + " NULLS LAST, " );
                        index++;
                    }
                }                
                else
                {
                    if ( index == listeOrders.size( ) )
                    {
                        sbSQL.append( order.getName( ) + " " + order.getOrder( ) + " " );
                    } else
                    {
                        sbSQL.append( order.getName( ) + " " + order.getOrder( ) + ", " );
                        index++;
                    }
                }
            }
        }

        return sbSQL.toString( );
    }

    /**
     * Set the filter values on the DAOUtil.
     *
     * @param filter
     *            the filter
     * @param daoUtil
     *            the DAOUtil
     */
    private void setFilterValues( SignalementFilter filter, DAOUtil daoUtil )
    {
        int nIndex = 1;

        // Type
        if ( filter.getIdTypeSignalement( ) > 0 )
        {
            daoUtil.setLong( nIndex++, filter.getIdTypeSignalement( ) );
        }
        if ( ( filter.getListIdTypeSignalements( ) != null ) && !filter.getListIdTypeSignalements( ).isEmpty( ) )
        {
            for ( Integer nIdTypeSignalement : filter.getListIdTypeSignalements( ) )
            {
                daoUtil.setLong( nIndex++, nIdTypeSignalement );
            }
        }

        // Numéro de signalement
        if ( ( filter.getNumero( ) != null ) && !StringUtils.isBlank( filter.getNumero( ) ) )
        {
            daoUtil.setString( nIndex++, addPercent( filter.getNumero( ) ) );
        }

        // Direction
        if ( filter.getIdDirection( ) > 0 )
        {
            daoUtil.setLong( nIndex++, filter.getIdDirection( ) );
        }

        // Arrondissement
        if ( filter.getIdArrondissement( ) > 0 )
        {
            daoUtil.setLong( nIndex++, filter.getIdArrondissement( ) );
        }
        if ( ( filter.getListIdArrondissements( ) != null ) && !filter.getListIdArrondissements( ).isEmpty( ) )
        {
            for ( Integer nIdArrondissement : filter.getListIdArrondissements( ) )
            {
                daoUtil.setLong( nIndex++, nIdArrondissement );
            }
        }

        // Quartier
        if ( ( filter.getListIdQuartier( ) != null ) && !filter.getListIdQuartier( ).isEmpty( ) )
        {
            for ( Integer nIdQuartier : filter.getListIdQuartier( ) )
            {
                daoUtil.setLong( nIndex++, nIdQuartier );
            }
        }

        // Sector
        if ( filter.getIdSector( ) > 0 )
        {
            daoUtil.setLong( nIndex++, filter.getIdSector( ) );
        }

        // daoUtil.setLong() autant de fois qu'il y a d'elems dans la liste des secteurs autorisés.
        if ( ( filter.getListIdUnit( ) != null ) && !filter.getListIdUnit( ).isEmpty( ) )
        {
            for ( Integer nIdSecteur : filter.getListIdUnit( ) )
            {
                daoUtil.setLong( nIndex++, nIdSecteur );
            }

        }

        // Adresse
        if ( ( filter.getAdresse( ) != null ) && !StringUtils.isBlank( filter.getAdresse( ) ) )
        {
            daoUtil.setString( nIndex++, addPercent( filter.getAdresse( ) ) );
        }

        // Mail
        if ( ( filter.getMail( ) != null ) && !StringUtils.isBlank( filter.getMail( ) ) )
        {
            daoUtil.setString( nIndex++, addPercent( filter.getMail( ) ) );
        }

        // Commentaire
        if ( ( filter.getCommentaire( ) != null ) && !StringUtils.isBlank( filter.getCommentaire( ) ) )
        {
            daoUtil.setString( nIndex++, addPercent( filter.getCommentaire( ) ) );
        }

        // Date de création
        boolean dateBeginNotEmpty = ( filter.getDateBegin( ) != null ) && !StringUtils.isBlank( filter.getDateBegin( ) );
        boolean dateEndNotEmpty = ( filter.getDateEnd( ) != null ) && !StringUtils.isBlank( filter.getDateEnd( ) );

        if ( dateBeginNotEmpty )
        {
            daoUtil.setTimestamp( nIndex++, DateUtil.formatTimestamp( filter.getDateBegin( ), Locale.FRENCH ) );
        }

        if ( dateEndNotEmpty )
        {
            daoUtil.setTimestamp( nIndex++, DateUtil.formatTimestamp( filter.getDateEnd( ), Locale.FRENCH ) );
        }

        // //ETAT
        // if ( filter.getIdEtat( ) > 0 )
        // {
        // daoUtil.setLong( nIndex++, filter.getIdEtat( ) );
        // }

        // ETAT
        if ( ( filter.getEtats( ) != null ) && !filter.getEtats( ).isEmpty( ) )
        {
            for ( EtatSignalement etatSign : filter.getEtats( ) )
            {
                if ( etatSign.getCoche( ) )
                {
                    daoUtil.setLong( nIndex++, etatSign.getId( ) );
                }
            }
        }

        // CATEGORY
        if ( !CollectionUtils.isEmpty( filter.getListIdCategories( ) ) )
        {
            for ( Integer nIdCategory : filter.getListIdCategories( ) )
            {
                daoUtil.setLong( nIndex++, nIdCategory );
            }

        }

    }

    /**
     * Add % before and after the string.
     *
     * @param name
     *            the string
     * @return the string with %
     */
    private String addPercent( String name )
    {
        return "%" + name + "%";
    }

    /**
     * Add a <b>WHERE</b> or a <b>OR</b> depending of the index. <br/>
     * <ul>
     * <li>if <code>nIndex</code> == 1, then we add a <b>WHERE</b></li>
     * <li>if <code>nIndex</code> != 1, then we add a <b>OR</b> or a <b>AND</b> depending of the wide search characteristic</li>
     * </ul>
     *
     * @param bIsWideSearch
     *            true if it is a wide search, false otherwise
     * @param sbSQL
     *            the SQL query
     * @param nIndex
     *            the index
     * @return the new index
     */
    private int addSQLWhereOr( boolean bIsWideSearch, StringBuilder sbSQL, int nIndex )
    {
        if ( nIndex == 1 )
        {
            sbSQL.append( SQL_WHERE );
        } else
        {
            sbSQL.append( bIsWideSearch ? SQL_OR : SQL_AND );
        }

        return nIndex + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Signalement> findByFilter( SignalementFilter filter, PaginationProperties paginationProperties, Plugin plugin )
    {

        List<Signalement> listSignalements = new ArrayList<Signalement>( );
        StringBuilder sbSQL = new StringBuilder( buildSQLQuery( filter ) );

        if ( paginationProperties != null )
        {
            sbSQL.append( "LIMIT " + paginationProperties.getItemsPerPage( ) );
            sbSQL.append( " OFFSET " + ( ( paginationProperties.getPageIndex( ) - 1 ) * paginationProperties.getItemsPerPage( ) ) );
        }

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );
        
        //Special case Specificity for DEVE entity, change the id from SEJ to DEVE
        if ( filter.getIdDirection( ) == 94 )
        {
            filter.setIdDirection( 1 );
        }
        
        setFilterValues( filter, daoUtil );

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;

            Signalement signalement = load( daoUtil.getLong( nIndex++ ) );

            // Priorite
            Priorite priorite = new Priorite( );
            priorite.setLibelle( daoUtil.getString( nIndex++ ) );

            // Type Signalement
            TypeSignalement typeSignalement = new TypeSignalement( );
            // typeSignalement.setLibelle( daoUtil.getString( nIndex++ ) );
            typeSignalement.setId( daoUtil.getInt( nIndex++ ) );

            // Unit
            // Unit unit = new Unit( );
            // unit.setLabel( daoUtil.getString( nIndex++ ) );

            signalement.setPriorite( priorite );
            // typeSignalement.setUnit( unit );
            signalement.setTypeSignalement( typeSignalement );

            listSignalements.add( signalement );
        }

        daoUtil.free( );

        return listSignalements;
    }

    /**
     * Load all signalement.
     *
     * @return the all signalement
     */
    @Override
    public List<Signalement> getAllSignalement( )
    {
        List<Signalement> listResult = new ArrayList<Signalement>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL );
        daoUtil.executeQuery( );
        // int nIndex;
        while ( daoUtil.next( ) )
        {
            Signalement signalement = getSignalementFromDaoUtil( daoUtil );

            listResult.add( signalement );
        }

        daoUtil.free( );

        return listResult;
    }

    /**
     * Gets the signalement from dao util.
     *
     * @param daoUtil
     *            the dao util
     * @return the signalement from dao util
     */
    private Signalement getSignalementFromDaoUtil( DAOUtil daoUtil )
    {
        int nIndex;
        nIndex = 1;
        Signalement signalement = new Signalement( );

        signalement.setId( daoUtil.getLong( nIndex++ ) );
        signalement.setSuivi( daoUtil.getInt( nIndex++ ) );
        signalement.setDateCreation( DateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );
        signalement.setDatePrevueTraitement( DateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );
        signalement.setCommentaire( daoUtil.getString( nIndex++ ) );
        signalement.setAnnee( daoUtil.getInt( nIndex++ ) );
        signalement.setMois( daoUtil.getString( nIndex++ ) );
        signalement.setNumero( daoUtil.getInt( nIndex++ ) );
        signalement.setPrefix( daoUtil.getString( nIndex++ ) );

        if ( daoUtil.getLong( nIndex++ ) > 0 )
        {
            Priorite priorite = new Priorite( );
            priorite.setId( daoUtil.getLong( nIndex - 1 ) );
            signalement.setPriorite( priorite );
        }
        if ( daoUtil.getLong( nIndex++ ) > 0 )
        {
            Arrondissement arrondissement = new Arrondissement( );
            arrondissement.setId( daoUtil.getLong( nIndex - 1 ) );
            signalement.setArrondissement( arrondissement );
        }
        if ( daoUtil.getLong( nIndex++ ) > 0 )
        {
            TypeSignalement typeSignalement = new TypeSignalement( );
            typeSignalement.setId( daoUtil.getInt( nIndex - 1 ) );
            signalement.setTypeSignalement( typeSignalement );
        }
        if ( daoUtil.getInt( nIndex++ ) > 0 )
        {
            Sector sector = new Sector( );
            sector.setIdSector( daoUtil.getInt( nIndex - 1 ) );
            signalement.setSecteur( sector );
        }
        signalement.setIsDoublon( daoUtil.getBoolean( nIndex++ ) );
        return signalement;
    }

    @Override
    public List<Signalement> getSignalementsArchivableByStatusId( Integer statusId, Integer limit )
    {
        DAOUtil daoUtil = new DAOUtil( MessageFormat.format( SQL_QUERY_SELECT_BY_STATUS, limit ) );
        daoUtil.setInt( 1, statusId );
        daoUtil.setInt( 2, statusId );
        daoUtil.executeQuery( );
        List<Signalement> listResult = new ArrayList<Signalement>( );
        while ( daoUtil.next( ) )
        {
            Signalement signalement = getSignalementFromDaoUtil( daoUtil );

            listResult.add( signalement );
        }

        daoUtil.free( );

        return listResult;
    }

    @Override
    public List<Integer> getAllIdSignalement( )
    {
        List<Integer> listResult = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_ID_SIGNALEMENT );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            listResult.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );

        return listResult;
    }

    @Override
    public synchronized void incrementeSuiviByIdSignalement( Integer nIdSignalement )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_UPDATE_SUIVI_PLUS_ONE );
        daoUtil.setInt( 1, nIdSignalement );
        daoUtil.executeUpdate( );

        daoUtil.free( );

    }

    @Override
    public synchronized void decrementSuiviByIdSignalement( Integer nIdSignalement )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_UPDATE_SUIVI_MINUS_ONE );
        daoUtil.setInt( 1, nIdSignalement );
        daoUtil.executeUpdate( );

        daoUtil.free( );

    }

    @Override
    public List<Integer> findAllSignalementInPerimeter( Double lat, Double lng, Integer radius, List<Integer> listStatus )
    {

        StringBuilder stringBuilder = new StringBuilder( );
        stringBuilder.append( "SELECT id_signalement FROM signalement_adresse " );
        stringBuilder
                .append( "INNER JOIN signalement_signalement AS signalement ON signalement_adresse.fk_id_signalement = signalement.id_signalement " );
        stringBuilder.append(
                "INNER JOIN workflow_resource_workflow AS resource_workflow ON resource_workflow.id_resource = signalement.id_signalement " );
        stringBuilder.append( "WHERE ST_Within(ST_Transform( signalement_adresse.geom,3857), ST_Buffer(ST_Transform(ST_SetSRID(ST_MakePoint(" );
        stringBuilder.append( lng );
        stringBuilder.append( ", " );
        stringBuilder.append( lat );
        stringBuilder.append( "),4326),3857)," );
        stringBuilder.append( radius );
        stringBuilder.append( ")) = true " );
        stringBuilder.append( "AND resource_workflow.id_workflow = 2 " );
        stringBuilder.append( "AND resource_workflow.resource_type = 'SIGNALEMENT_SIGNALEMENT' " );

        if ( ( listStatus != null ) && !listStatus.isEmpty( ) )
        {
            Object[] qMarks = new Object[listStatus.size( )];
            for ( int i = 0; i < listStatus.size( ); i++ )
            {
                qMarks[i] = '?';
            }
            String joinQuestionMarks = StringUtils.join( qMarks, ", " );
            stringBuilder.append( "AND resource_workflow.id_state IN (" + joinQuestionMarks + ") " );
        }

        DAOUtil daoUtil = new DAOUtil( stringBuilder.toString( ) );

        List<Integer> listResult = new ArrayList<Integer>( );
        int index = 1;
        if ( ( listStatus != null ) && !listStatus.isEmpty( ) )
        {
            for ( Integer idStatus : listStatus )
            {
                daoUtil.setInt( index++, idStatus );
            }
        }

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            listResult.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return listResult;
    }

    @Override
    public List<DossierSignalementDTO> findAllSignalementInPerimeterWithDTO( Double lat, Double lng, Integer radius, List<Integer> listStatus )
    {

        StringBuilder stringBuilder = new StringBuilder( );
        stringBuilder.append(
                "SELECT id_signalement, adresse, date_creation, commentaire, heure_creation, ST_X(geom), ST_Y(geom), signalement.fk_id_type_signalement, signalement.prefix, vstswp.id_parent FROM signalement_adresse " );
        stringBuilder
                .append( "INNER JOIN signalement_signalement AS signalement ON signalement_adresse.fk_id_signalement = signalement.id_signalement " );
        stringBuilder.append( "INNER JOIN signalement_type_signalement AS type ON type.id_type_signalement = signalement.fk_id_type_signalement " );
        stringBuilder.append(
                "INNER JOIN v_signalement_type_signalement_with_parents_links AS vstswp ON vstswp.id_type_signalement = signalement.fk_id_type_signalement AND vstswp.is_parent_a_category=1 " );
        stringBuilder.append(
                "INNER JOIN workflow_resource_workflow AS resource_workflow ON resource_workflow.id_resource = signalement.id_signalement " );
        stringBuilder.append( "WHERE ST_Within(ST_Transform( signalement_adresse.geom,3857), ST_Buffer(ST_Transform(ST_SetSRID(ST_MakePoint(" );
        stringBuilder.append( lng );
        stringBuilder.append( ", " );
        stringBuilder.append( lat );
        stringBuilder.append( "),4326),3857)," );
        stringBuilder.append( radius );
        stringBuilder.append( ")) = true " );
        stringBuilder.append( "AND resource_workflow.id_workflow = 2 " );
        stringBuilder.append( "AND resource_workflow.resource_type = 'SIGNALEMENT_SIGNALEMENT' " );

        if ( ( listStatus != null ) && !listStatus.isEmpty( ) )
        {
            Object[] qMarks = new Object[listStatus.size( )];
            for ( int i = 0; i < listStatus.size( ); i++ )
            {
                qMarks[i] = '?';
            }
            String joinQuestionMarks = StringUtils.join( qMarks, ", " );
            stringBuilder.append( "AND resource_workflow.id_state IN (" + joinQuestionMarks + ") " );
        }

        DAOUtil daoUtil = new DAOUtil( stringBuilder.toString( ) );

        int index = 1;
        if ( ( listStatus != null ) && !listStatus.isEmpty( ) )
        {
            for ( Integer idStatus : listStatus )
            {
                daoUtil.setInt( index++, idStatus );
            }
        }

        daoUtil.executeQuery( );

        List<DossierSignalementDTO> listResult = new ArrayList<DossierSignalementDTO>( );

        while ( daoUtil.next( ) )
        {
            DossierSignalementDTO dossierSignalementDTO = new DossierSignalementDTO( );
            dossierSignalementDTO.setId( ( long ) daoUtil.getInt( 1 ) );
            dossierSignalementDTO.setAdresse( daoUtil.getString( 2 ) );
            dossierSignalementDTO.setDateCreation( DateUtils.getDateFr( daoUtil.getDate( 3 ) ) );
            dossierSignalementDTO.setCommentaire( daoUtil.getString( 4 ) );
            dossierSignalementDTO.setHeureCreation( DateUtils.getHourFr( daoUtil.getTimestamp( 5 ) ) );
            dossierSignalementDTO.setLng( daoUtil.getDouble( 6 ) );
            dossierSignalementDTO.setLat( daoUtil.getDouble( 7 ) );
            dossierSignalementDTO.setType( daoUtil.getString( 8 ) );
            dossierSignalementDTO.setPrefix( daoUtil.getString( 9 ) );
            dossierSignalementDTO.setIdCategory( daoUtil.getInt( 10 ) );
            listResult.add( dossierSignalementDTO );
        }

        daoUtil.free( );
        return listResult;
    }

    @Override
    public Integer getDistanceBetweenSignalement( double lat1, double lng1, double lat2, double lng2 )
    {
        String query = "SELECT ST_Distance(ST_GeographyFromText('POINT(" + lng1 + "" + lat1 + ")'), " + "ST_GeographyFromText('POINT(" + lng2 + ""
                + lat2 + ")')) ";

        Integer distance = 0;

        DAOUtil daoUtil = new DAOUtil( query );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            distance = daoUtil.getInt( 1 );
        }

        daoUtil.free( );
        return distance;

    }

    @Override
    public List<Integer> findByIdTelephone( String idTelephone )
    {

        List<Integer> listResult = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ID_TELEPHONE );
        daoUtil.setString( 1, idTelephone );

        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            listResult.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );

        return listResult;

    }

    /**
     * Know if there's results (signalements) before printing a road map
     *
     * @param listSectorId
     *            user allowed sectors
     * @return boolean
     */
    @Override
    public boolean hasSignalementForRoadMap( List<Integer> listSectorId )
    {
        StringBuilder sbQueryHasSignalementForRoadMap = new StringBuilder( SQL_QUERY_HAS_SIGNALEMENT_FOR_ROAD_MAP );
        boolean hasSignalement = false;

        // Build a list of id sector in sql format
        StringBuilder sbListSector = new StringBuilder( );
        for ( Integer nIdSector : listSectorId )
        {
            if ( sbListSector.length( ) != 0 )
            {
                sbListSector.append( COMA_SEPARATOR );
            }

            sbListSector.append( nIdSector );
        }
        sbQueryHasSignalementForRoadMap.append( sbListSector ).append( CLOSED_BRACKET );

        DAOUtil daoUtil = new DAOUtil( sbQueryHasSignalementForRoadMap.toString( ) );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            hasSignalement = true;
        }

        daoUtil.free( );
        return hasSignalement;
    }

    @Override
    public Signalement getSignalementByToken( String token, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_SIGNALEMENT_BY_TOKEN );
        daoUtil.setString( 1, token );
        daoUtil.executeQuery( );
        Signalement signalement = null;
        while ( daoUtil.next( ) )
        {
            signalement = new Signalement( );
            signalement.setId( daoUtil.getLong( 1 ) );
            signalement.setToken( daoUtil.getString( 2 ) );
        }

        daoUtil.free( );
        return signalement;

    }

    @Override
    public Double[] getGeomFromLambertToWgs84( Double dLatLambert, Double dLngLambert )
    {

        String query = "SELECT ST_X(ST_Transform(ST_GeomFromText('POINT(" + dLatLambert + " " + dLngLambert
                + ")',27561),4326)), ST_Y(ST_Transform(ST_GeomFromText('POINT(" + dLatLambert + " " + dLngLambert + ")',27561),4326))";

        Double geom[] = new Double[2];
        DAOUtil daoUtil = new DAOUtil( query );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            geom[0] = daoUtil.getDouble( 1 );
            geom[1] = daoUtil.getDouble( 2 );
        }

        daoUtil.free( );
        return geom;
    }

    /**
     * Count the number of results for a given query
     *
     * @param filter
     *            the signalementfilter
     * @param plugin
     *            the plugin
     * @return the query
     */
    @Override
    public Integer countIdSignalementByFilter( SignalementFilter filter, Plugin plugin )
    {
        StringBuilder sbSQL = new StringBuilder( buildSQLQueryForCount( filter ) );
        Integer nNbIdSignalement = 0;

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );
        setFilterValues( filter, daoUtil );

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            nNbIdSignalement = daoUtil.getInt( 1 );
        }

        daoUtil.free( );

        return nNbIdSignalement;
    }

    /**
     * Build the query to count the number of results for a given query
     *
     * @param filter
     *            the signalementfilter
     * @return the query
     */
    @Override
    public String buildSQLQueryForCount( SignalementFilter filter )
    {
        StringBuilder sbSQL = new StringBuilder( SQL_SELECT_COUNT_ID_SIGNALEMENT );
        addFilterCriterias( filter, sbSQL );

        return sbSQL.toString( );
    }

    /**
     * Adds filter criterias to query
     *
     * @param filter
     *            the signalement filter
     * @param sbSQL
     *            the quey
     */
    private void addFilterCriterias( SignalementFilter filter, StringBuilder sbSQL )
    {
        sbSQL.append( SQL_QUERY_FROM_ALL );

        boolean bHasFilerMail = ( filter.getMail( ) != null ) && !StringUtils.isBlank( filter.getMail( ) );
        boolean bHasFilterSignalementType = filter.getIdTypeSignalement( ) > 0;
        boolean bHasFilterUnit = filter.getIdDirection( ) > 0;
        boolean bHasFilterCategory = !CollectionUtils.isEmpty( filter.getListIdCategories( ) );

        // Units requires type
        if ( bHasFilterSignalementType || bHasFilterUnit )
        {
            sbSQL.append( SQL_QUERY_FROM_SIGNALEMENT_TYPE );
        }

        if ( bHasFilterSignalementType )
        {
            sbSQL.append( SQL_QUERY_FROM_VIEW_TYPE_SIGNALEMENT_W_PARENT_LINK );
        }

        if ( bHasFilterUnit )
        {
            sbSQL.append( SQL_QUERY_FROM_UNITTREE );
        }

        if ( bHasFilerMail )
        {
            sbSQL.append( SQL_QUERY_FROM_SIGNALEUR );
        }

        if ( bHasFilterCategory )
        {
            sbSQL.append( SQL_QUERY_FROM_SIGNALEMENT_CATEGORY );
        }

        int nIndex = 1;

        // Type
        if ( filter.getIdTypeSignalement( ) > 0 )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_TYPE_SIGNALEMENT );
        }
        if ( ( filter.getListIdTypeSignalements( ) != null ) && !filter.getListIdTypeSignalements( ).isEmpty( ) )
        {
            int listeLength = filter.getListIdTypeSignalements( ).size( );
            Character[] array = new Character[listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array[i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_LIST_TYPE_SIGNALEMENT, unionQuery ) );
        }

        // Numéro de signalement
        if ( ( filter.getNumero( ) != null ) && !StringUtils.isBlank( filter.getNumero( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_NUMERO );
        }

        // Direction
        if ( filter.getIdDirection( ) > 0 )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_DIRECTION );
        }

        // ARRONDISSEMENT
        if ( filter.getIdArrondissement( ) > 0 )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_ARRONDISSEMENT );
        }
        if ( ( filter.getListIdArrondissements( ) != null ) && !filter.getListIdArrondissements( ).isEmpty( ) )
        {
            int listeLength = filter.getListIdArrondissements( ).size( );
            Character[] array = new Character[listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array[i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_LIST_ARRONDISSEMENT, unionQuery ) );
        }

        // QUARTIER
        if ( ( filter.getListIdQuartier( ) != null ) && !filter.getListIdQuartier( ).isEmpty( ) )
        {
            int listeLength = filter.getListIdQuartier( ).size( );
            Character[] array = new Character[listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array[i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_LIST_QUARTIER, unionQuery ) );
        }

        // SECTOR
        if ( filter.getIdSector( ) > 0 )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_SECTOR );
        }

        // Allowed sectors

        if ( !filter.getListIdUnit( ).isEmpty( ) )
        {
            int listeLength = filter.getListIdUnit( ).size( );
            Character[] array = new Character[listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array[i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_SECTOR_ALLOWED, unionQuery ) );
        }

        else
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( "1 = 2" );
        }

        // Adresse
        if ( ( filter.getAdresse( ) != null ) && !StringUtils.isBlank( filter.getAdresse( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_ADRESSE );
        }

        // Mail
        if ( bHasFilerMail )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_MAIL );
        }

        // Commentaire
        if ( ( filter.getCommentaire( ) != null ) && !StringUtils.isBlank( filter.getCommentaire( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_COMMENTAIRE );
        }

        // Date de création
        boolean dateBeginNotEmpty = ( filter.getDateBegin( ) != null ) && !StringUtils.isBlank( filter.getDateBegin( ) );
        boolean dateEndNotEmpty = ( filter.getDateEnd( ) != null ) && !StringUtils.isBlank( filter.getDateEnd( ) );

        if ( dateBeginNotEmpty )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_DATE_BEGIN );
        }

        if ( dateEndNotEmpty )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_DATE_END );
        }

        // ETAT
        if ( filter.getEtats( ) != null )
        {
            int i = filter.getEtats( ).size( );

            for ( EtatSignalement etatSign : filter.getEtats( ) )
            {
                if ( etatSign.getCoche( ) )
                {

                    if ( nIndex == 1 )
                    {
                        sbSQL.append( SQL_WHERE );
                    } else
                    {
                        if ( i == filter.getEtats( ).size( ) )
                        {
                            sbSQL.append( SQL_AND );
                        } else
                        {
                            sbSQL.append( SQL_OR );
                        }

                    }
                    nIndex++;

                    if ( i == filter.getEtats( ).size( ) )
                    {
                        sbSQL.append( " ( " );
                    }
                    sbSQL.append( SQL_QUERY_ADD_FILTER_ETAT );
                    i--;
                    if ( i < 1 )
                    {

                        sbSQL.append( " ) " );
                    }

                }

            }
        }

        // ADDING CATEGORY FILTER
        if ( bHasFilterCategory )
        {
            int listeLength = filter.getListIdCategories( ).size( );
            Character[] array = new Character[listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array[i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_CATEGORY, unionQuery ) );
        }
    }

    @Override
    public void insertMessageCreationSignalement( String messageCreation )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_MESSAGE_CREATION );
        daoUtil.setString( 1, messageCreation );
        daoUtil.executeQuery( );
        daoUtil.free( );

    }

    @Override
    public void updateMessageCreationSignalement( String messageCreation )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_MESSAGE_CREATION );
        daoUtil.setString( 1, messageCreation );
        daoUtil.executeUpdate( );
        daoUtil.free( );

    }

    @Override
    public String loadMessageCreationSignalement( )
    {
        String strMessageCreation = StringUtils.EMPTY;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_MESSAGE_CREATION );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            strMessageCreation = daoUtil.getString( 1 );
        }
        daoUtil.free( );

        return strMessageCreation;

    }

    @Override
    public void removeMessageCreationSignalement( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_MESSAGE_CREATION );
        daoUtil.executeUpdate( );
        daoUtil.free( );

    }

    @Override
    public synchronized void incrementFelicitationsByIdSignalement( int idSignalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FELICITATIONS_INCREMENT );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, idSignalement );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    @Override
    public List<DashboardSignalementDTO> findByDashboardFilter( SignalementDashboardFilter filter, Plugin pluginSignalement )
    {
        StringBuilder query = new StringBuilder( );
        query.append( SQL_SELECT );
        query.append( "s.id_signalement" ).append( COMA_SEPARATOR );
        query.append( "s.date_creation" ).append( COMA_SEPARATOR );
        query.append( "id_state" ).append( COMA_SEPARATOR );
        query.append( "date_prevue_traitement" ).append( COMA_SEPARATOR );
        query.append( "date_mise_surveillance" );

        /// FROM
        query.append( " FROM " );
        query.append( "signalement_signalement s" );

        // JOIN
        query.append( " INNER JOIN " );
        query.append( " workflow_resource_workflow wrw " );
        query.append( " ON " );
        query.append( " wrw.id_resource = s.id_signalement " );

        query.append( " INNER JOIN " );
        query.append( "v_signalement_type_signalement_with_parents_links vstsawpl" );
        query.append( " ON " );
        query.append( " s.fk_id_type_signalement = vstsawpl.id_type_signalement " );
        query.append( " AND " );
        query.append( " vstsawpl.is_parent_a_category = 1 " );

        query.append( " INNER JOIN " );
        query.append( " unittree_unit_sector uus" );
        query.append( " ON " );
        query.append( " s.fk_id_sector = uus.id_sector" );

        int nIndex = 1;

        nIndex = addSQLWhereOr( false, query, nIndex );
        query.append( " wrw.resource_type = ? " );
        nIndex = addSQLWhereOr( false, query, nIndex );
        query.append( " wrw.id_workflow = ? " );

        // FILTER BY PERIOD
        DashboardPeriod period = filter.getDashboardPeriod( );
        Integer higherBound = null;
        Integer lowerBound = null;

        if ( period != null )
        {
            higherBound = period.getHigherBound( );
            lowerBound = period.getLowerBound( );

            if ( higherBound != null )
            {
                LocalDate higherBoundDate = LocalDate.now( );
                higherBoundDate.plus( new Long( higherBound ), ChronoUnit.MONTHS );
                // < à higherBound
                nIndex = addSQLWhereOr( false, query, nIndex );
                query.append( " s.date_creation < ? " );
            }

            if ( lowerBound != null )
            {
                LocalDate lowerBoundDate = LocalDate.now( );
                lowerBoundDate.plus( new Long( lowerBound ), ChronoUnit.MONTHS );
                // > à lowerBound
                nIndex = addSQLWhereOr( false, query, nIndex );
                query.append( " s.date_creation >= ? " );
            }
        }

        // FILTER BY UNIT (mandatory)
        nIndex = addSQLWhereOr( false, query, nIndex );
        query.append( " uus.id_unit = ? " );

        // FILTER BY CATEGORY
        if ( !ArrayUtils.isEmpty( filter.getCategoryIds( ) ) )
        {
            int listeLength = filter.getCategoryIds( ).length;
            Character[] array = new Character[listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array[i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, query, nIndex );
            query.append( MessageFormat.format( " vstsawpl.id_parent IN ({0}) ", unionQuery ) );
        }

        // FILTER BY ARRONDISSEMENT
        if ( !ArrayUtils.isEmpty( filter.getArrondissementIds( ) ) )
        {
            int listeLength = filter.getArrondissementIds( ).length;
            Character[] array = new Character[listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array[i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, query, nIndex );
            query.append( MessageFormat.format( " fk_id_arrondissement IN ({0}) ", unionQuery ) );
        }

        query.append( " ORDER BY id_state ASC, date_creation DESC " );

        DAOUtil daoUtil = new DAOUtil( query.toString( ) );

        nIndex = 1;

        daoUtil.setString( nIndex++, Signalement.WORKFLOW_RESOURCE_TYPE );
        daoUtil.setInt( nIndex++, SignalementConstants.SIGNALEMENT_WORKFLOW_ID );

        // Ajout des arguments de recherche
        if ( period != null )
        {
            if ( higherBound != null )
            {
                LocalDate higherBoundDate = LocalDate.now( );
                higherBoundDate = higherBoundDate.plus( new Long( higherBound ), ChronoUnit.MONTHS );
                daoUtil.setDate( nIndex++, java.sql.Date.valueOf( higherBoundDate ) );
            }

            if ( lowerBound != null )
            {
                // Gestion mois courant
                if ( lowerBound == -1 )
                {
                    lowerBound = lowerBound + 1;
                }
                LocalDate lowerBoundDate = LocalDate.now( );
                lowerBoundDate = lowerBoundDate.plus( new Long( lowerBound ), ChronoUnit.MONTHS );
                // Mois courant = 1er jour du mois
                if ( lowerBound == 0 )
                {
                    lowerBoundDate = lowerBoundDate.withDayOfMonth( 1 );
                }
                daoUtil.setDate( nIndex++, java.sql.Date.valueOf( lowerBoundDate ) );
            }
        }

        // Entité
        daoUtil.setInt( nIndex++, filter.getUnitId( ) );

        // Catégories
        if ( !ArrayUtils.isEmpty( filter.getCategoryIds( ) ) )
        {
            for ( Integer categoryId : filter.getCategoryIds( ) )
            {
                daoUtil.setInt( nIndex++, categoryId );
            }
        }

        // Arrondissements
        if ( !ArrayUtils.isEmpty( filter.getArrondissementIds( ) ) )
        {
            for ( Integer arrondissementId : filter.getArrondissementIds( ) )
            {
                daoUtil.setInt( nIndex++, arrondissementId );
            }
        }

        daoUtil.executeQuery( );

        List<DashboardSignalementDTO> dashboardSigList = new ArrayList<>( );
        while ( daoUtil.next( ) )
        {
            nIndex = 1;
            DashboardSignalementDTO dashboardSigDTO = new DashboardSignalementDTO( );
            dashboardSigDTO.setIdSignalement( daoUtil.getInt( nIndex++ ) );
            java.sql.Date creationDate = daoUtil.getDate( nIndex++ );
            if ( null != creationDate )
            {
                LocalDate localDate = creationDate.toLocalDate( );
                dashboardSigDTO.setCreationDate( localDate );
            }
            dashboardSigDTO.setIdStatus( daoUtil.getInt( nIndex++ ) );
            java.sql.Date datePrevueTraitement = daoUtil.getDate( nIndex++ );
            if ( null != datePrevueTraitement )
            {
                LocalDate localDate = datePrevueTraitement.toLocalDate( );
                dashboardSigDTO.setDatePrevueTraitement( localDate );
            }
            java.sql.Date dateMiseSurveillance = daoUtil.getDate( nIndex++ );
            if ( null != dateMiseSurveillance )
            {
                LocalDate localDate = dateMiseSurveillance.toLocalDate( );
                dashboardSigDTO.setDateMiseEnSurveillance( localDate );
            }
            dashboardSigList.add( dashboardSigDTO );
        }

        daoUtil.free( );
        return dashboardSigList;
    }

    /**
     * Finds the matching signalement ids for the filter
     *
     * @param filter
     *            the signalementfilter
     * @param plugin
     *            the plugin
     * @return the query
     */
    @Override
    public List<Integer> getIdsSignalementByFilter( SignalementFilter filter, Plugin plugin )
    {
        StringBuilder sbSQL = new StringBuilder( buildSQLQueryForIds( filter ) );

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );
        setFilterValues( filter, daoUtil );

        daoUtil.executeQuery( );

        List<Integer> signalementsIds = new ArrayList<>( );

        while ( daoUtil.next( ) )
        {
            signalementsIds.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );

        return signalementsIds;
    }

    /**
     * Build the query to get the signalement ids matching the filter
     *
     * @param filter
     *            the signalementfilter
     * @return the query
     */
    private String buildSQLQueryForIds( SignalementFilter filter )
    {
        StringBuilder sbSQL = new StringBuilder( SQL_SELECT_ID_SIGNALEMENT );
        addFilterCriterias( filter, sbSQL );
        return sbSQL.toString( );
    }

    @Override
    public void addMiseEnSurveillanceDate( int idSignalement, String dateMiseEnSurveillance )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_DATE_MISE_SURVEILLANCE );
        int nIndex = 1;
        daoUtil.setDate( nIndex++, DateUtil.formatDateSql( dateMiseEnSurveillance, Locale.FRENCH ) );
        daoUtil.setInt( nIndex++, idSignalement );
        daoUtil.executeUpdate( );
        daoUtil.free( );
        
        
    }
    
    /**
     * {@inheritDoc} 
     */
    public List<Integer> findIdsSingalementForWSPartnerDeamon(int signalementState) {
        
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ID_SIGNALEMENT_FOR_PARTNER_DEAMON );
        daoUtil.setInt( 1, signalementState );
        daoUtil.executeQuery( );
        
        List<Integer> signalementsIds = new ArrayList<>( );

        while ( daoUtil.next( ) )
        {
            signalementsIds.add( daoUtil.getInt( 1 ) );
        }
        
        daoUtil.free( );
      
        return signalementsIds;   
    }
}
