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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementExportDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.EtatSignalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Priorite;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.commons.Order;
import fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties;
import fr.paris.lutece.plugins.dansmarue.service.dto.SignalementExportCSVDTO;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.icon.Icon;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * The Class SignalementExportDAO.
 */
public class SignalementExportDAO implements ISignalementExportDAO
{

    /** The Constant SQL_QUERY_SELECTALL. */
    // SQL QUERIES
    private static final String SQL_QUERY_SELECTALL = "SELECT numero, priorite, type_signalement, alias, alias_mobile, direction, quartier, adresse, coord_x, coord_y, arrondissement, secteur, date_creation, heure_creation, etat, mail_usager, commentaire_usager, nb_photos, raisons_rejet, nb_suivis, nb_felicitations, date_cloture, is_photo_service_fait, mail_destinataire_courriel, courriel_expediteur, date_envoi_courriel, id_mail_service_fait, executeur_service_fait, date_derniere_action, date_prevu_traitement, commentaire_agent_terrain, executeur_rejet, executeur_mise_surveillance, nb_requalifications, id_signalement "
            + "FROM  signalement_export";

    /** The Constant SQL_QUERY_SEARCH_ID. */
    private static final String SQL_QUERY_SEARCH_ID = "SELECT numero FROM  signalement_export";

    private static final String SQL_QUERY_SELECTALL_SEARCH = "SELECT se.id_signalement , se.numero, se.priorite, se.type_signalement, se.direction, se.adresse, se.coord_x, se.coord_y, se.date_creation, se.etat, se.mail_usager, se.commentaire_usager,"
            + " se.nb_suivis, se.date_prevu_traitement, se.commentaire_agent_terrain, ss.is_send_ws, ws.id_state, sp.vue_photo , sp.image_thumbnail, sp.id_photo, wa.id_action, wa.name, wa.id_icon"
            + " FROM  signalement_export se" + " LEFT OUTER JOIN signalement_photo sp on sp.fk_id_signalement = se.id_signalement"
            + " inner join signalement_signalement ss on ss.id_signalement = se.id_signalement" + " inner join workflow_state ws on ws.name = se.etat"
            + " LEFT OUTER JOIN workflow_action wa on wa.id_state_before = ws.id_state";

    /** The Constant SQL_QUERY_COUNT_SEARCH. */
    private static final String SQL_QUERY_COUNT_SEARCH = "SELECT count(*) FROM  signalement_export";

    /** The Constant SQL_QUERY_WHERE_NUMERO_IN. */
    private static final String SQL_QUERY_WHERE_NUMERO_IN = " WHERE se.numero IN ({0})";

    /** The Constant SQL_QUERY_WHERE_ID_IN. */
    private static final String SQL_QUERY_WHERE_ID_IN = " WHERE id_signalement IN ({0})";

    /** The Constant SQL_QUERY_ADD_FILTER_PRIORITE. */
    private static final String SQL_QUERY_ADD_FILTER_PRIORITE = " priorite in (select sp.libelle from signalement_priorite sp where sp.id_priorite in ({0}))";

    /** The Constant SQL_QUERY_ADD_FILTER_LIST_TYPE_SIGNALEMENT. */
    // Filter by report type list
    private static final String SQL_QUERY_ADD_FILTER_LIST_TYPE_SIGNALEMENT = " id_type_signalement IN ({0}) ";

    /** The Constant SQL_QUERY_ADD_FILTER_TYPE_SIGNALEMENT. */
    // Filter by report type
    private static final String SQL_QUERY_ADD_FILTER_TYPE_SIGNALEMENT = " id_type_signalement IN (SELECT id_type_signalement FROM v_signalement_type_signalement_with_parents_links WHERE id_parent = {0} )";

    /** The Constant SQL_QUERY_ADD_FILTER_CATEGORIE. */
    // Filter by categorie
    private static final String SQL_QUERY_ADD_FILTER_CATEGORIE = " id_type_signalement IN (SELECT id_type_signalement FROM v_signalement_type_signalement_with_parents_links WHERE id_parent in ({0}) )";

    private static final String SQL_QUERY_ADD_FILTER_FDT = " id_signalement in (select unnest(signalement_ids) from signalement_feuille_de_tournee where id=?) ";

    /** The Constant SQL_QUERY_ADD_FILTER_NUMERO. */
    // Filter by numero
    private static final String SQL_QUERY_ADD_FILTER_NUMERO = " numero = ";

    /** The Constant SQL_QUERY_ADD_FILTER_DIRECTION. */
    // Filter by id unit
    private static final String SQL_QUERY_ADD_FILTER_DIRECTION = " id_direction= ";

    /** The Constant SQL_QUERY_ADD_FILTER_ARRONDISSEMENT. */
    // Filter by id Arrondissement
    private static final String SQL_QUERY_ADD_FILTER_ARRONDISSEMENT = " id_arrondissement = ";

    /** The Constant SQL_QUERY_ADD_FILTER_LIST_ARRONDISSEMENT. */
    // Filter by Arrondissements list
    private static final String SQL_QUERY_ADD_FILTER_LIST_ARRONDISSEMENT = " id_arrondissement IN ({0}) ";

    /** The Constant SQL_QUERY_ADD_FILTER_LIST_QUARTIER. */
    // Filter by neighborhood list
    private static final String SQL_QUERY_ADD_FILTER_LIST_QUARTIER = " id_quartier IN ({0}) ";

    /** The Constant SQL_QUERY_ADD_FILTER_SECTOR. */
    // Filter by sector id
    private static final String SQL_QUERY_ADD_FILTER_SECTOR = " id_sector =  ";

    /** The Constant SQL_QUERY_ADD_FILTER_SECTOR_ALLOWED. */
    // Filter by unit list
    private static final String SQL_QUERY_ADD_FILTER_SECTOR_ALLOWED = " id_sector IN (SELECT s.id_sector FROM unittree_sector s INNER JOIN unittree_unit_sector u ON s.id_sector = u.id_sector WHERE u.id_unit in ({0}))";

    /** The Constant SQL_QUERY_ADD_FILTER_ADRESSE. */
    // Filter by address
    private static final String SQL_QUERY_ADD_FILTER_ADRESSE = " lower_unaccent(adresse) LIKE lower_unaccent(?) ";

    /** The Constant SQL_QUERY_ADD_FILTER_MAIL. */
    // Filter by mail
    private static final String SQL_QUERY_ADD_FILTER_MAIL = " lower_unaccent(mail_usager) LIKE lower_unaccent(?) ";

    /** The Constant SQL_QUERY_ADD_FILTER_COMMENTAIRE. */
    // Filter by description
    private static final String SQL_QUERY_ADD_FILTER_COMMENTAIRE = " lower_unaccent(commentaire_usager) LIKE lower_unaccent(?) ";

    /** The Constant SQL_QUERY_ADD_FILTER_COMMENTAIRE_AGENT_TERRAIN. */
    // Filter by agent comment
    private static final String SQL_QUERY_ADD_FILTER_COMMENTAIRE_AGENT_TERRAIN = " lower_unaccent(commentaire_agent_terrain) LIKE lower_unaccent(?) ";

    /** The Constant SQL_QUERY_ADD_FILTER_DATE_BEGIN. */
    // Filter by begin date
    private static final String SQL_QUERY_ADD_FILTER_DATE_BEGIN = " to_date(date_creation,'DD/MM/YYYY') >= ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_DATE_END. */
    // Filter by end date
    private static final String SQL_QUERY_ADD_FILTER_DATE_END = " to_date(date_creation,'DD/MM/YYYY') <= ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_DATE_DONE_BEGIN. */
    // Filter by begin date
    private static final String SQL_QUERY_ADD_FILTER_DATE_DONE_BEGIN = " to_date(date_derniere_action,'DD/MM/YYYY') >= ? AND id_wkf_state = 10";

    /** The Constant SQL_QUERY_ADD_FILTER_DATE_DONE_END. */
    // Filter by end date
    private static final String SQL_QUERY_ADD_FILTER_DATE_DONE_END = " to_date(date_derniere_action,'DD/MM/YYYY') <= ? AND id_wkf_state = 10";

    /** The Constant SQL_QUERY_ADD_FILTER_MAIL. */
    // Filter by mail
    private static final String SQL_QUERY_ADD_FILTER_MAIL_DERNIER_INTERVENANT = " (lower_unaccent(executeur_service_fait) LIKE lower_unaccent(?) OR lower_unaccent(executeur_rejet) LIKE lower_unaccent(?))";

    /** The Constant SQL_QUERY_ADD_FILTER_LIST_STATE. */
    // Filter by report state list
    private static final String SQL_QUERY_ADD_FILTER_LIST_STATE = " id_wkf_state IN ({0}) ";

    /** The Constant SQL_QUERY_NO_STATE_FILTER. */
    private static final String SQL_QUERY_NO_STATE_FILTER = " etat is not null ";

    private static final String SQL_QUERY_DEFAULT_ORDER_FDT = " id_arrondissement  asc, lower(regexp_replace(adresse,'[[:digit:]]','','g')) asc, "
            + "nullif(regexp_replace(split_part(adresse,' ',1), '\\D', '', 'g'), '')::int asc ";

    /** The Constant SQL_AND. */
    // SQL Constants
    private static final String SQL_AND = " AND ";

    /** The Constant SQL_WHERE. */
    private static final String SQL_WHERE = " WHERE ";

    /** The Constant SQL_OR. */
    private static final String SQL_OR = " OR ";

    /** The Constant SEJ_ID. */
    // Constants
    private static final int SEJ_ID = 94;

    /** The Constant COMMA_SEPARATOR. */
    private static final String COMMA_SEPARATOR = ", ";

    /** The Constant SIMPLE_QUOTE. */
    private static final String SIMPLE_QUOTE = "'";

    /** The Constant NUM_SIGNALEMENT. */
    private static final String NUM_SIGNALEMENT = "numSignalement";

    /** The Constant SIGNALEUR_MAIL. */
    private static final String SIGNALEUR_MAIL = "signaleur.mail";

    /** The Constant DEFAULT_ORDER_FDT. */
    private static final String DEFAULT_ORDER_FDT = "fdt.default";

    /** The orders map. */
    private Map<String, String> _ordersMap;

    /**
     * Instantiates a new report dao.
     */
    public SignalementExportDAO( )
    {
        super( );

        // Initializes the list of keys / value used for sorting columns
        // by concerns of non-regression the current keys are the direct paths to the attributes used previously.
        _ordersMap = new HashMap<>( );
        _ordersMap.put( "signalement.suivi", "nb_suivis" );
        _ordersMap.put( NUM_SIGNALEMENT, "numero" );
        _ordersMap.put( SIGNALEUR_MAIL, "mail_usager" );
        _ordersMap.put( "priorite.libelle", "priorite" );
        _ordersMap.put( "type.libelle", "type_signalement" );
        _ordersMap.put( "direction_unit.label", "id_direction" );
        _ordersMap.put( "adr.adresse", "adresse" );
        _ordersMap.put( "signalement.commentaire", "commentaire_usager" );
        _ordersMap.put( "signalement.date_creation", "id_signalement" );
        _ordersMap.put( "signalement.heure_creation", null );
        _ordersMap.put( "photo.id_photo", "nb_photos" );
    }

    /**
     * Find by ids.
     *
     * @param ids
     *            the ids
     * @param plugin
     *            the plugin
     * @return the list
     */
    /*
     * (non-Javadoc)
     *
     * @see fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementExportDAO#findByIds(int[], fr.paris.lutece.portal.service.plugin.Plugin)
     */
    @Override
    public List<SignalementExportCSVDTO> findByIds( int [ ] ids, Plugin plugin )
    {
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECTALL );

        sbSQL.append( MessageFormat.format( SQL_QUERY_WHERE_ID_IN, StringUtils.remove( StringUtils.remove( Arrays.toString( ids ), '[' ), ']' ) ) );

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );
        daoUtil.executeQuery( );

        List<SignalementExportCSVDTO> exportList = new ArrayList<>( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;

            SignalementExportCSVDTO exportReport = new SignalementExportCSVDTO( );

            exportReport.setNumeroSignalement( daoUtil.getString( nIndex++ ) );
            exportReport.setPriorite( daoUtil.getString( nIndex++ ) );
            exportReport.setTypeSignalement( daoUtil.getString( nIndex++ ) );
            exportReport.setAlias( daoUtil.getString( nIndex++ ) );
            exportReport.setAliasMobile( daoUtil.getString( nIndex++ ) );
            exportReport.setDirection( daoUtil.getString( nIndex++ ) );
            exportReport.setQuartier( daoUtil.getString( nIndex++ ) );
            exportReport.setAdresse( daoUtil.getString( nIndex++ ) );
            exportReport.setCoordX( daoUtil.getDouble( nIndex++ ) );
            exportReport.setCoordY( daoUtil.getDouble( nIndex++ ) );
            exportReport.setArrondissement( daoUtil.getString( nIndex++ ) );
            exportReport.setSecteur( daoUtil.getString( nIndex++ ) );
            exportReport.setDateCreation( daoUtil.getString( nIndex++ ) );
            exportReport.setHeureCreation( daoUtil.getString( nIndex++ ) );
            exportReport.setEtat( daoUtil.getString( nIndex++ ) );
            exportReport.setMailusager( daoUtil.getString( nIndex++ ) );
            exportReport.setCommentaireUsager( daoUtil.getString( nIndex++ ) );
            exportReport.setNbPhotos( daoUtil.getInt( nIndex++ ) );
            exportReport.setRaisonsRejet( daoUtil.getString( nIndex++ ) );
            exportReport.setNbSuivis( daoUtil.getInt( nIndex++ ) );
            exportReport.setNbFelicitations( daoUtil.getInt( nIndex++ ) );
            exportReport.setDateCloture( daoUtil.getString( nIndex++ ) );
            exportReport.setPhotoServiceFait( daoUtil.getBoolean( nIndex++ ) );
            exportReport.setMailDestinataireCourriel( daoUtil.getString( nIndex++ ) );
            exportReport.setCourrielExpediteur( daoUtil.getString( nIndex++ ) );
            exportReport.setDateEnvoiCourriel( daoUtil.getString( nIndex++ ) );
            exportReport.setIdMailServiceFait( daoUtil.getInt( nIndex++ ) );
            exportReport.setExecuteurServiceFait( daoUtil.getString( nIndex++ ) );
            exportReport.setDateDerniereAction( daoUtil.getString( nIndex++ ) );
            exportReport.setDatePrevuTraitement( daoUtil.getString( nIndex++ ) );
            exportReport.setCommentairAgentTerrain( daoUtil.getString( nIndex++ ) );
            exportReport.setExecuteurRejet( daoUtil.getString( nIndex++ ) );
            exportReport.setExecuteurMiseSurveillance( daoUtil.getString( nIndex++ ) );
            exportReport.setNbRequalifications( daoUtil.getInt( nIndex++ ) );
            exportReport.setIdSignalement( daoUtil.getInt( nIndex ) );

            exportList.add( exportReport );

        }

        daoUtil.close( );

        return exportList;
    }

    /**
     * Find by filter.
     *
     * @param filter
     *            the filter
     * @param plugin
     *            the plugin
     * @return the list
     */
    /*
     * (non-Javadoc)
     *
     * @see
     * fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementExportDAO#findByFilter(fr.paris.lutece.plugins.dansmarue.business.entities.SignalementFilter,
     * fr.paris.lutece.portal.service.plugin.Plugin)
     */
    @Override
    public List<SignalementExportCSVDTO> findByFilter( SignalementFilter filter, Plugin plugin )
    {
        List<SignalementExportCSVDTO> exportList = new ArrayList<>( );

        // return empty list if user is not allowed to see any report
        if ( CollectionUtils.isEmpty( filter.getListIdUnit( ) ) )
        {
            return exportList;
        }

        DAOUtil daoUtil = new DAOUtil( buildSQLQuery( filter, SQL_QUERY_SELECTALL ), plugin );

        fillWhereClause( filter, daoUtil );

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;

            SignalementExportCSVDTO exportReport = new SignalementExportCSVDTO( );

            exportReport.setNumeroSignalement( daoUtil.getString( nIndex++ ) );
            exportReport.setPriorite( daoUtil.getString( nIndex++ ) );
            exportReport.setTypeSignalement( daoUtil.getString( nIndex++ ) );
            exportReport.setAlias( daoUtil.getString( nIndex++ ) );
            exportReport.setAliasMobile( daoUtil.getString( nIndex++ ) );
            exportReport.setDirection( daoUtil.getString( nIndex++ ) );
            exportReport.setQuartier( daoUtil.getString( nIndex++ ) );
            exportReport.setAdresse( daoUtil.getString( nIndex++ ) );
            exportReport.setCoordX( daoUtil.getDouble( nIndex++ ) );
            exportReport.setCoordY( daoUtil.getDouble( nIndex++ ) );
            exportReport.setArrondissement( daoUtil.getString( nIndex++ ) );
            exportReport.setSecteur( daoUtil.getString( nIndex++ ) );
            exportReport.setDateCreation( daoUtil.getString( nIndex++ ) );
            exportReport.setHeureCreation( daoUtil.getString( nIndex++ ) );
            exportReport.setEtat( daoUtil.getString( nIndex++ ) );
            exportReport.setMailusager( daoUtil.getString( nIndex++ ) );
            exportReport.setCommentaireUsager( daoUtil.getString( nIndex++ ) );
            exportReport.setNbPhotos( daoUtil.getInt( nIndex++ ) );
            exportReport.setRaisonsRejet( daoUtil.getString( nIndex++ ) );
            exportReport.setNbSuivis( daoUtil.getInt( nIndex++ ) );
            exportReport.setNbFelicitations( daoUtil.getInt( nIndex++ ) );
            exportReport.setDateCloture( daoUtil.getString( nIndex++ ) );
            exportReport.setPhotoServiceFait( daoUtil.getBoolean( nIndex++ ) );
            exportReport.setMailDestinataireCourriel( daoUtil.getString( nIndex++ ) );
            exportReport.setCourrielExpediteur( daoUtil.getString( nIndex++ ) );
            exportReport.setDateEnvoiCourriel( daoUtil.getString( nIndex++ ) );
            exportReport.setIdMailServiceFait( daoUtil.getInt( nIndex++ ) );
            exportReport.setExecuteurServiceFait( daoUtil.getString( nIndex++ ) );
            exportReport.setDateDerniereAction( daoUtil.getString( nIndex++ ) );
            exportReport.setDatePrevuTraitement( daoUtil.getString( nIndex++ ) );
            exportReport.setCommentairAgentTerrain( daoUtil.getString( nIndex++ ) );
            exportReport.setExecuteurRejet( daoUtil.getString( nIndex++ ) );
            exportReport.setExecuteurMiseSurveillance( daoUtil.getString( nIndex++ ) );
            exportReport.setNbRequalifications( daoUtil.getInt( nIndex ) );

            exportList.add( exportReport );

        }

        daoUtil.close( );

        return exportList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> searchNumeroByFilter( SignalementFilter filter, PaginationProperties paginationProperties, Plugin plugin )
    {

        List<String> listIdFind = new ArrayList<>( );

        // return empty list if user is not allowed to see any report
        if ( CollectionUtils.isEmpty( filter.getListIdUnit( ) ) )
        {
            return listIdFind;
        }

        StringBuilder sbSQL = new StringBuilder( buildSQLQuery( filter, SQL_QUERY_SEARCH_ID ) );
        if ( paginationProperties != null )
        {
            sbSQL.append( "LIMIT " + paginationProperties.getItemsPerPage( ) );
            sbSQL.append( " OFFSET " + ( ( paginationProperties.getPageIndex( ) - 1 ) * paginationProperties.getItemsPerPage( ) ) );
        }

        try ( DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin ) ; )
        {

            fillWhereClause( filter, daoUtil );

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                listIdFind.add( "'" + daoUtil.getString( 1 ) + "'" );
            }

        }

        return listIdFind;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Signalement> searchFindByFilter( SignalementFilter filter, List<String> listIdSignalement, Plugin plugin )
    {

        List<Signalement> listSignalementFind = new ArrayList<>( );

        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECTALL_SEARCH );
        sbSQL.append( MessageFormat.format( SQL_QUERY_WHERE_NUMERO_IN, String.join( ",", listIdSignalement ) ) );
        // ADD ORDERS
        // ADD ORDERS
        if ( ( filter.getOrders( ).get( 0 ) != null ) && DEFAULT_ORDER_FDT.equals( filter.getOrders( ).get( 0 ).getName( ) ) )
        {
            addDefaultOrderFDTSearch( sbSQL );
        }
        else
        {
            List<Order> listeOrders = convertOrderToClause( filter.getOrders( ) );
            addOrderClause( sbSQL, listeOrders );
        }

        try ( DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin ) ; )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                int nIndex = 1;

                if ( listSignalementFind.stream( ).anyMatch( signalement -> signalement.getId( ) == daoUtil.getInt( 1 ) ) )
                {
                    listSignalementFind.stream( ).filter( signalement -> signalement.getId( ) == daoUtil.getInt( 1 ) ).findFirst( )
                            .ifPresent( ( Signalement signalement ) -> {
                                signalement.setPhotos( addPhotosToSignalement( daoUtil, signalement.getPhotos( ) ) );
                                signalement.setListActionAvailable( addActionToSignalement( daoUtil, signalement.getListActionAvailable( ) ) );
                            } );

                }
                else
                {

                    Signalement exportReport = new Signalement( );

                    exportReport.setId( daoUtil.getLong( nIndex++ ) );
                    exportReport.setNumeroComplet( daoUtil.getString( nIndex++ ) );
                    Priorite priorite = new Priorite( );
                    priorite.setLibelle( daoUtil.getString( nIndex++ ) );
                    exportReport.setPriorite( priorite );
                    exportReport.setTypeSignalementComplet( daoUtil.getString( nIndex++ ) );
                    Unit direction = new Unit( );
                    direction.setLabel( daoUtil.getString( nIndex++ ) );
                    exportReport.setDirectionSector( direction );
                    List<Adresse> listAdresse = new ArrayList<>( );
                    Adresse adresse = new Adresse( );
                    adresse.setAdresse( daoUtil.getString( nIndex++ ) );
                    adresse.setLat( daoUtil.getDouble( nIndex++ ) );
                    adresse.setLng( daoUtil.getDouble( nIndex++ ) );
                    listAdresse.add( adresse );
                    exportReport.setAdresses( listAdresse );
                    exportReport.setDateCreation( daoUtil.getString( nIndex++ ) );
                    exportReport.setStateName( daoUtil.getString( nIndex++ ) );

                    List<Signaleur> listSignaleur = new ArrayList<>( );
                    Signaleur signaleur = new Signaleur( );
                    signaleur.setMail( daoUtil.getString( nIndex++ ) );
                    listSignaleur.add( signaleur );
                    exportReport.setSignaleurs( listSignaleur );
                    exportReport.setCommentaire( daoUtil.getString( nIndex++ ) );
                    exportReport.setSuivi( daoUtil.getInt( nIndex++ ) );
                    exportReport.setDatePrevueTraitement( daoUtil.getString( nIndex++ ) );
                    exportReport.setCommentaireAgentTerrain( daoUtil.getString( nIndex++ ) );
                    exportReport.setSendWs( daoUtil.getBoolean( nIndex++ ) );
                    exportReport.setIdState( daoUtil.getInt( nIndex ) );

                    List<PhotoDMR> listPhotos = new ArrayList<>( );
                    exportReport.setPhotos( addPhotosToSignalement( daoUtil, listPhotos ) );

                    List<Action> listActions = new ArrayList<>( );
                    exportReport.setListActionAvailable( addActionToSignalement( daoUtil, listActions ) );

                    listSignalementFind.add( exportReport );
                }
            }

        }

        return listSignalementFind;
    }

    /**
     * Add photo to signalement if not exist.
     *
     * @param daoUtil
     *            the dao util
     * @param listPhotos
     *            the list photos
     * @return listPhotos
     */
    private List<PhotoDMR> addPhotosToSignalement( DAOUtil daoUtil, List<PhotoDMR> listPhotos )
    {

        int indexVuePhoto = 18;
        int indexPhotoMiniature = 19;
        int indexIdPhoto = 20;

        long idPhoto = daoUtil.getLong( indexIdPhoto );

        if ( listPhotos.stream( ).filter( photo -> photo.getId( ).longValue( ) == idPhoto ).count( ) > 0 )
        {
            return listPhotos;
        }

        Object oImageContentThumbnail = daoUtil.getBytes( indexPhotoMiniature );
        if ( oImageContentThumbnail != null )
        {
            PhotoDMR photo = new PhotoDMR( );
            ImageResource image = new ImageResource( );
            image.setImage( (byte [ ]) oImageContentThumbnail );
            photo.setImageThumbnail( image );
            photo.setVue( indexVuePhoto );
            photo.setId( idPhoto );
            listPhotos.add( photo );

        }

        return listPhotos;
    }

    /**
     * Add action worflow on signalement.
     *
     * @param daoUtil
     *            the dao util
     * @param listAction
     *            the list action
     * @return listAction
     */
    private List<Action> addActionToSignalement( DAOUtil daoUtil, List<Action> listAction )
    {
        int indexIdAction = 21;
        int indexActionName = 22;
        int indexActionIconId = 23;

        int idAction = daoUtil.getInt( indexIdAction );
        if ( idAction > 0 )
        {
            Action action = new Action( );
            action.setId( idAction );
            action.setName( daoUtil.getString( indexActionName ) );
            Icon icon = new Icon( );
            icon.setId( daoUtil.getInt( indexActionIconId ) );
            action.setIcon( icon );

            if ( listAction.stream( ).noneMatch( a -> a.getId( ) == idAction ) )
            {
                listAction.add( action );
            }
        }

        return listAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countSignalementSearch( SignalementFilter filter, Plugin plugin )
    {

        int count = 0;

        // return empty list if user is not allowed to see any report
        if ( CollectionUtils.isEmpty( filter.getListIdUnit( ) ) )
        {
            return count;
        }

        try ( DAOUtil daoUtil = new DAOUtil( buildSQLQuery( filter, SQL_QUERY_COUNT_SEARCH ), plugin ) ; )
        {
            fillWhereClause( filter, daoUtil );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                count = daoUtil.getInt( 1 );
            }
        }

        return count;
    }

    /**
     * Builds the SQL query.
     *
     * @param filter
     *            the filter
     * @param query
     *            Query to execute
     * @return the string
     */
    private String buildSQLQuery( SignalementFilter filter, String query )
    {
        StringBuilder sbSQL = new StringBuilder( query );

        boolean bHasFilterSignalementType = filter.getIdTypeSignalement( ) > 0;
        boolean bHasFilterUnit = filter.getIdDirection( ) > 0;
        boolean bHasFilterCategory = !CollectionUtils.isEmpty( filter.getListIdCategories( ) );

        int nIndex = 1;

        // Type
        if ( bHasFilterSignalementType )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_TYPE_SIGNALEMENT, Integer.toString( filter.getIdTypeSignalement( ) ) ) );
        }

        // Type list
        if ( CollectionUtils.isNotEmpty( filter.getListIdTypeSignalements( ) ) )
        {

            String unionQuery = StringUtils.join( filter.getListIdTypeSignalements( ), COMMA_SEPARATOR );

            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_LIST_TYPE_SIGNALEMENT, unionQuery ) );
        }

        // Report number
        if ( StringUtils.isNotBlank( filter.getNumero( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_NUMERO );
            sbSQL.append( SIMPLE_QUOTE + filter.getNumero( ) + SIMPLE_QUOTE );
        }

        // Board
        if ( bHasFilterUnit )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_DIRECTION );
            // Special case Specificity for DEVE entity, change the id from SEJ to DEVE
            if ( filter.getIdDirection( ) == SEJ_ID )
            {
                filter.setIdDirection( 1 );
            }
            sbSQL.append( filter.getIdDirection( ) );
        }

        // district
        if ( filter.getIdArrondissement( ) > 0 )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_ARRONDISSEMENT );
            sbSQL.append( filter.getIdArrondissement( ) );
        }
        if ( ( filter.getListIdArrondissements( ) != null ) && !filter.getListIdArrondissements( ).isEmpty( ) )
        {
            String unionQuery = StringUtils.join( filter.getListIdArrondissements( ), COMMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_LIST_ARRONDISSEMENT, unionQuery ) );
        }

        // neighborhood
        if ( ( filter.getListIdQuartier( ) != null ) && !filter.getListIdQuartier( ).isEmpty( ) )
        {
            String unionQuery = StringUtils.join( filter.getListIdQuartier( ), COMMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_LIST_QUARTIER, unionQuery ) );
        }

        // SECTOR
        if ( filter.getIdSector( ) > 0 )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_SECTOR );
            sbSQL.append( filter.getIdSector( ) );
        }

        // Allowed sectors

        if ( !filter.getListIdUnit( ).isEmpty( ) )
        {

            String unionQuery = StringUtils.join( filter.getListIdUnit( ), COMMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_SECTOR_ALLOWED, unionQuery ) );

        }

        // Priorite
        if ( ( filter.getPriorites( ) != null ) && !filter.getPriorites( ).isEmpty( ) )
        {

            String unionQuery = StringUtils.join( filter.getPriorites( ), COMMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_PRIORITE, unionQuery ) );
        }

        // Address
        if ( StringUtils.isNotBlank( filter.getAdresse( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_ADRESSE );
        }

        // Mail
        if ( StringUtils.isNotBlank( filter.getMail( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_MAIL );
        }

        // Commentary
        if ( StringUtils.isNotBlank( filter.getCommentaire( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_COMMENTAIRE );
        }

        if ( StringUtils.isNotBlank( filter.getCommentaireAgentTerrain( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_COMMENTAIRE_AGENT_TERRAIN );
        }

        // Creation date
        boolean dateBeginNotEmpty = StringUtils.isNotBlank( filter.getDateBegin( ) );
        boolean dateEndNotEmpty = StringUtils.isNotBlank( filter.getDateEnd( ) );

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

        // Service Fait date
        if ( StringUtils.isNotBlank( filter.getDateDoneBegin( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_DATE_DONE_BEGIN );
        }

        if ( StringUtils.isNotBlank( filter.getDateDoneEnd( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_DATE_DONE_END );
        }

        // Mail dernier intervenant
        if ( StringUtils.isNotBlank( filter.getMailDernierIntervenant( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_MAIL_DERNIER_INTERVENANT );
        }

        // State
        if ( CollectionUtils.isNotEmpty( filter.getEtats( ) ) )
        {
            // Get selected states ids to list
            List<Long> stateList = filter.getEtats( ).stream( ).filter( EtatSignalement::getCoche ).map( EtatSignalement::getId )
                    .collect( Collectors.toList( ) );

            String unionQuery = StringUtils.join( stateList, COMMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_LIST_STATE, unionQuery ) );
        }

        // ADDING CATEGORY FILTER
        if ( bHasFilterCategory )
        {
            String unionQuery = StringUtils.join( filter.getListIdCategories( ), COMMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_CATEGORIE, unionQuery ) );
        }

        // Feuille de tournee filter
        if ( filter.getIdFdt( ) > 0 )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_FDT );
        }

        // Exclude Report with no state
        addSQLWhereOr( false, sbSQL, nIndex );
        sbSQL.append( SQL_QUERY_NO_STATE_FILTER );

        if ( SQL_QUERY_SELECTALL.equals( query ) || SQL_QUERY_SEARCH_ID.equals( query ) )
        {
            // ADD ORDERS
            if ( ( filter.getOrders( ).get( 0 ) != null ) && DEFAULT_ORDER_FDT.equals( filter.getOrders( ).get( 0 ).getName( ) ) )
            {
                addDefaultOrderFDTSearch( sbSQL );
            }
            else
            {
                List<Order> listeOrders = convertOrderToClause( filter.getOrders( ) );
                if ( ( listeOrders != null ) && !listeOrders.isEmpty( ) )
                {
                    addOrderClause( sbSQL, listeOrders );
                }
            }
        }

        return sbSQL.toString( );
    }

    /**
     * Add order clause to query.
     *
     * @param sbSQL
     *            string builder query
     * @param listeOrders
     *            order to apply
     */
    private void addOrderClause( StringBuilder sbSQL, List<Order> listeOrders )
    {

        sbSQL.append( " ORDER BY " );
        int index = 1;

        for ( Order order : listeOrders )
        {
            if ( SIGNALEUR_MAIL.equals( order.getName( ) ) )
            {
                if ( index == listeOrders.size( ) )
                {
                    sbSQL.append( order.getName( ) + " " + order.getOrder( ) + " NULLS LAST " );
                }
                else
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
                }
                else
                {
                    sbSQL.append( order.getName( ) + " " + order.getOrder( ) + ", " );
                    index++;
                }
            }
        }

    }

    /**
     * Add default order clause to query
     * 
     * @param sbSQL
     *            string builder query
     */
    private void addDefaultOrderFDTSearch( StringBuilder sbSQL )
    {
        sbSQL.append( " ORDER BY " );
        sbSQL.append( SQL_QUERY_DEFAULT_ORDER_FDT );
    }

    /**
     * Fill query where clause.
     *
     * @param filter
     *            the filter
     * @param daoUtil
     *            the dao util
     */
    private void fillWhereClause( SignalementFilter filter, DAOUtil daoUtil )
    {

        int nIdx = 1;

        // Fill where clause
        if ( StringUtils.isNotBlank( filter.getAdresse( ) ) )
        {
            daoUtil.setString( nIdx++, addPercent( filter.getAdresse( ) ) );
        }

        if ( StringUtils.isNotBlank( filter.getMail( ) ) )
        {
            daoUtil.setString( nIdx++, addPercent( filter.getMail( ) ) );
        }

        if ( StringUtils.isNotBlank( filter.getCommentaire( ) ) )
        {
            daoUtil.setString( nIdx++, addPercent( filter.getCommentaire( ) ) );
        }

        if ( StringUtils.isNotBlank( filter.getCommentaireAgentTerrain( ) ) )
        {
            daoUtil.setString( nIdx++, addPercent( filter.getCommentaireAgentTerrain( ) ) );
        }

        if ( StringUtils.isNotBlank( filter.getDateBegin( ) ) )
        {
            daoUtil.setDate( nIdx++, DateUtil.formatDateSql( filter.getDateBegin( ), Locale.FRENCH ) );
        }
        if ( StringUtils.isNotBlank( filter.getDateEnd( ) ) )
        {
            daoUtil.setDate( nIdx++, DateUtil.formatDateSql( filter.getDateEnd( ), Locale.FRENCH ) );
        }

        if ( StringUtils.isNotBlank( filter.getDateDoneBegin( ) ) )
        {
            daoUtil.setDate( nIdx++, DateUtil.formatDateSql( filter.getDateDoneBegin( ), Locale.FRENCH ) );
        }
        if ( StringUtils.isNotBlank( filter.getDateDoneEnd( ) ) )
        {
            daoUtil.setDate( nIdx++, DateUtil.formatDateSql( filter.getDateDoneEnd( ), Locale.FRENCH ) );
        }

        if ( StringUtils.isNotBlank( filter.getMailDernierIntervenant( ) ) )
        {
            daoUtil.setString( nIdx++, addPercent( filter.getMailDernierIntervenant( ) ) );
            daoUtil.setString( nIdx++, addPercent( filter.getMailDernierIntervenant( ) ) );
        }

        if ( filter.getIdFdt( ) > 0 )
        {
            daoUtil.setInt( nIdx, filter.getIdFdt( ) );
        }
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
        }
        else
        {
            sbSQL.append( bIsWideSearch ? SQL_OR : SQL_AND );
        }

        return nIndex + 1;
    }

    /**
     * replaces sort keys with sql paths to columns to sort.
     *
     * @param orders
     *            the orders
     * @return the list
     */
    private List<Order> convertOrderToClause( List<Order> orders )
    {
        List<Order> ret = new ArrayList<>( );
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
}
