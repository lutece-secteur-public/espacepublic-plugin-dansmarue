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
package fr.paris.lutece.plugins.dansmarue.web;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.EtatSignalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.FeuilleDeTournee;
import fr.paris.lutece.plugins.dansmarue.business.entities.FeuilleDeTourneeFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.Priorite;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.commons.Order;
import fr.paris.lutece.plugins.dansmarue.commons.ResultList;
import fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties;
import fr.paris.lutece.plugins.dansmarue.service.IArrondissementService;
import fr.paris.lutece.plugins.dansmarue.service.IConseilQuartierService;
import fr.paris.lutece.plugins.dansmarue.service.IFeuilleDeTourneeService;
import fr.paris.lutece.plugins.dansmarue.service.IPrioriteService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementExportService;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.service.dto.SignalementExportCSVDTO;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.plugins.dansmarue.utils.ListUtils;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.business.sector.Sector;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.service.sector.ISectorService;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.util.LocalizedDelegatePaginator;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.beanvalidation.ValidationError;
import fr.paris.lutece.util.html.HtmlTemplate;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONBuilder;

@Controller( controllerJsp = "ManageFeuilleDeTournee.jsp", controllerPath = "jsp/admin/plugins/signalement/", right = "FEUILLE_DE_TOURNEE" )
public class ManageFeuilleDeTourneeJspBean extends AbstractJspBean
{
    private static final long serialVersionUID = -5616670500035044759L;

    // Template html
    private static final String TEMPLATE_MANAGE = "/admin/plugins/signalement/manage_feuille_tournee.html";
    private static final String TEMPLATE_CREATE = "/admin/plugins/signalement/create_feuille_tournee.html";
    private static final String TEMPLATE_EDIT   = "/admin/plugins/signalement/edit_feuille_tournee.html";
    private static final String TEMPLATE_LOAD   = "/admin/plugins/signalement/load_feuille_tournee.html";
    private static final String TEMPLATE_SELECT = "/admin/plugins/signalement/select_feuille_tournee.html";

    // View
    private static final String VIEW_MANAGE = "manage";
    private static final String VIEW_CREATE = "create";
    private static final String VIEW_EDIT   = "edit";
    private static final String VIEW_LOAD   = "load";

    // Action
    private static final String ACTION_INIT_SEARCH_ANO = "doInitSearchAno";
    private static final String ACTION_SEARCH_FDT      = "doSearchFDT";
    private static final String ACTION_SEARCH_ANO      = "doSearchAno";
    private static final String ACTION_SAVE_FDT        = "dosaveFDT";
    private static final String ACTION_UPDATE_FDT      = "doUpdateFDT";
    private static final String ACTION_SAVE_FILTER     = "doSaveFilter";
    private static final String ACTION_UPDATE_FILTER   = "doUpdateFilter";
    private static final String ACTION_DELETE_FILTER   = "doDeleteFilter";
    private static final String ACTION_LOAD_FILTER     = "doLoadFilter";
    private static final String ACTION_GET_DELETE_FDT  = "getDeleteFDT";

    // Marker

    private static final String MARK_USER_ENTITE             = "user_entite";
    private static final String MARK_ID_FILTER_SELECT        = "id_filter_select";
    private static final String MARK_FILTER_LIST             = "filter_list";
    private static final String MARK_TYPE_LIST               = "type_list";
    private static final String MARK_ETAT_LIST               = "etat_list";
    private static final String MARK_FILTER                  = "filter";
    private static final String MARK_PRIORITE_LIST           = "priorite_list";
    private static final String MARK_ARRONDISSEMENT_LIST     = "arrondissement_list";
    private static final String MARK_CONSEIL_QUARTIER_LIST   = "conseilQuartier_list";
    private static final String MARK_SECTEUR_LIST            = "secteur_list";
    private static final String MARK_ENTITY_LIST             = "entity_list";
    private static final String MARK_ID_ETATS_DEFAULT        = "etats_default";
    private static final String MARK_SEARCH_FILTER_CREATOR   = "search_filter_creator";
    public static final  String MARK_NOM_TEMPLATE            = "nom_template";
    private static final String MARK_SIGNALEMENT_LIST        = "signalement_list";
    private static final String MARK_TOTAL_SIGNALEMENT       = "total_signalement";
    private static final String MARK_LIST_FDT                = "fdt_list";
    private static final String MARK_FILTER_FDT              = "filter_fdt";
    private static final String MARK_SIGNALEMENTS_MAP_LIST   = "signalements_map_list";
    private static final String MARK_SIGNALEMENTS_SELECT_IDS = "signalementSelectIds";
    private static final String MARK_USER_LOGIN              = "user_login";
    private static final String MARK_FEUILLE_DE_TOURNEE      = "feuille_de_tournee";
    private static final String MARK_UPDATED_PART_FDT_NAME   = "updated_part_name";
    private static final String MARK_FDT_COMMENT             = "fdt_comment";
    private static final String MARK_ONGLET_ACTIF            = "onglet_actif";
    private static final String MARK_MARKER_SELECT           = "marker_select";
    private static final String MARK_DIRECTION_LIST          = "direction_list";
    private static final String MARK_ENTITE_LIST             = "entite_list";

    // Parameter
    private static final String PARAMETER_SEARCH_FILTER_CREATOR      = "param_search_filter_creator";
    private static final String PARAMETER_SEARCH_FILTER_NAME         = "param_search_filter_name";
    private static final String PARAMETER_SEARCH_FILTER_COMMENT      = "param_search_filter_comment";
    private static final String PARAMETER_SEARCH_FILTER_SELECT       = "param_filter";
    private static final String PARAMETER_FILTER_LOAD                = "param_filter_load";
    private static final String PARAMETER_NAME                       = "param_fdt_name";
    private static final String PARAMETER_ETAT                       = "etat";
    private static final String PARAMETER_PRIORITE                   = "priorite";
    private static final String PARAMETER_TYPOLOGIE                  = "typologieSelect";
    private static final String PARAMETER_ARRONDISEMENT              = "arrondissement";
    private static final String PARAMETER_QUARTIER                   = "listIdQuartierParam";
    private static final String PARAMETER_DATE_BEGIN                 = "dateBegin";
    private static final String PARAMETER_DATE_END                   = "dateEnd";
    private static final String PARAMETER_DATE_REQUALIFICATION_BEGIN = "dateRequalificationBegin";
    private static final String PARAMETER_DATE_REQUALIFICATION_END   = "dateRequalificationEnd";
    private static final String PARAMETER_DATE_PROGRAMMATION_BEGIN   = "dateProgrammationBegin";
    private static final String PARAMETER_DATE_PROGRAMMATION_END     = "dateProgrammationEnd";
    private static final String PARAMETER_COMMENTAIRE_AGENT_TERRAIN  = "commentaireAgentTerrain";
    private static final String PARAMETER_COMMENTAIRE_USAGER         = "commentaire";
    private static final String PARAMETER_ADRESSE                    = "adresse";
    private static final String PARAMETER_MAIL_USAGER                = "mail";
    private static final String PARAMETER_ENTITE                     = "idSector";
    private static final String PARAMETER_DESTINAIRES_EXPORT         = "destinataires";
    private static final String PARAMETER_FEUILLE_DE_TOURNEE_ID      = "idFeuilleDeTournee";
    private static final String PARAMETER_SIGNALEMENTS_SELECT_IDS    = "signalementSelectIds";
    private static final String PARAMETER_SAVE_NAME_PREFIX           = "param_save_fdt_name_prefix";
    private static final String PARAMETER_SAVE_NAME                  = "param_save_fdt_name";
    private static final String PARAMETER_SAVE_IS_UPDATE             = "param_save_fdt_is_update";
    private static final String PARAMETER_CARTE_BASE64 = "param_carte_base_64";
    private static final String PARAMETER_SAVE_COMMENT = "param_save_fdt_comment";
    private static final String PARAMETER_ONGLET_ACTIF = "param_onglet_actif";
    private static final String PARAMETER_TYPE_EXPORT = "param_type_export";
    private static final String PARAMETER_MARKER_SELECT = "marker_select";
    private static final String PARAMETER_DIRECTION_ID = "direction_id";
    private static final String PARAMETER_ENTITE_ID = "entite_id";

    // MESSAGES
    private static final String MESSAGE_ERROR_DATE = "dansmarue.message.fdt.date.error";
    private static final String MESSAGE_ACCESS_DENIED = "user does not have the right to do this";
    private static final String MESSAGE_CONFIRMATION_DELETE_FDT = "dansmarue.tournee.delete.confirmation";
    private static final String MESSAGE_TITLE_DELETE_FDT = "dansmarue.tournee.delete.title";
    private static final String MESSAGE_ERREUR_SELECTION = "dansmarue.tournee.erreur.liste.vide";

    /** The Constant ID_JARDIN. */
    public static final Integer ID_JARDIN = 260;
    /** The Constant ID_SECTOR. */
    private static final String ID_SECTOR = "idSector";
    /** The Constant ID_ETATS_DEFAULT. */
    private static final int [ ] ID_ETATS_DEFAULT = {
            7, 8, 16, 17, 18, 21
    };

    public static final Integer DEFAULT_ID = -1;

    private static final String PROPERTY_SEARCH_INITIAL_NB_DAYS = "signalement.reportingList.init.datebegin";

    private static final String URL_JSP_MANAGE_FEUILLE_TOURNEE = "jsp/admin/plugins/signalement/ManageFeuilleDeTournee.jsp";
    private static final String URL_JSP_DELETE_FEUILLE_TOURNEE = "jsp/admin/plugins/signalement/DoDeleteFeuilleDeTournee.jsp";

    private static final String TYPE_PDF = "application/pdf";

    private static final String NOM_TEMPLATE               = "feuilleDeTournee";
    private static final String NOM_TEMPLATE_CARTE         = "feuilleDeTourneeCarte";
    private static final String EXTENSION_APPLICATION_JSON = "application/json";
    private static final String JSON_KEY_ID                = "id";
    public static final  String PAGE_INDEX                 = "page_index";
    public static final  String ID_UNIT                    = "idUnit";
    public static final  String LABEL                      = "label";

    // Service
    private final transient ITypeSignalementService   _typeSignalementService   = SpringContextService.getBean( "typeSignalementService" );
    private final transient IPrioriteService          _prioriteService          = SpringContextService.getBean( "prioriteService" );
    private final transient IArrondissementService    _arrondissementService    = SpringContextService.getBean( "signalement.arrondissementService" );
    private final transient IConseilQuartierService   _conseilQuartier          = SpringContextService.getBean( "signalement.conseilQuartierService" );
    private final transient IUnitService              _unitService              = SpringContextService.getBean( "unittree.unitService" );
    private final transient ISectorService            _sectorService            = SpringContextService.getBean( "unittree-dansmarue.sectorService" );
    private final transient IFeuilleDeTourneeService  _feuilleTourneeService    = SpringContextService.getBean( "feuilleDeTourneeService" );
    private final transient ISignalementExportService _signalementExportService = SpringContextService.getBean( "signalementExportService" );

    private static final String STATE_NOT_DISPLAY = "signalement.state.not.display";

    private SignalementFilter      _signalementFilter;
    private FeuilleDeTourneeFilter _feuilleDeTourneeFilter;

    private static final String ONGLET_ACTIF_CARTE   = "carte";
    private static final String ONGLET_ACTIF_TABLEAU = "tableau";

    /**
     * Page de gestion des feuilles de tournée.
     *
     * @param request
     *            the request
     * @return the manage
     */
    @View( value = VIEW_MANAGE, defaultView = true )
    public String getManage( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );

        Locale locale = getLocale( );
        model.put( SignalementConstants.MARK_LOCALE, request.getLocale( ) );
        model.put( MARK_NOM_TEMPLATE, NOM_TEMPLATE );

        _feuilleDeTourneeFilter = _feuilleTourneeService.initFdtFilter( _feuilleDeTourneeFilter );

        List<FeuilleDeTournee> feuilleDeTourneeList;

        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        if ( strSortedAttributeName != null )
        {
            String strAscSort = request.getParameter( Parameters.SORTED_ASC );
            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );
            Order order = new Order( strSortedAttributeName, bIsAscSort );

            feuilleDeTourneeList = _feuilleTourneeService.loadFdtByFilterWithOrder( _feuilleDeTourneeFilter, order );
        }
        else
        {
            feuilleDeTourneeList = _feuilleTourneeService.loadFdtByFilter( _feuilleDeTourneeFilter );
        }

        List<FeuilleDeTournee> feuilleDeTourneeListAutorise = _feuilleTourneeService.getFeuilleDeTourneeAutorise( feuilleDeTourneeList,
                AdminUserService.getAdminUser( request ) );

        model.put( MARK_LIST_FDT, feuilleDeTourneeListAutorise );
        model.put( MARK_FILTER_FDT, _feuilleDeTourneeFilter );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE, locale, model );

        return getAdminPage( templateList.getHtml( ) );
    }

    @Action( ACTION_SEARCH_FDT )
    public String doSearchFdt( HttpServletRequest request )
    {
        if ( _feuilleDeTourneeFilter == null )
        {
            _feuilleDeTourneeFilter = new FeuilleDeTourneeFilter( );
        }
        populate( _feuilleDeTourneeFilter, request );

        if ( !_feuilleDeTourneeFilter.isDatesValid( ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DATE, AdminMessage.TYPE_STOP ) );
        }

        return getManage( request );
    }

    /**
     * Page de création d'une feuille de tournée.
     *
     * @param request
     *            the request
     * @return the creates the
     */
    @View( value = VIEW_CREATE )
    public String getCreate( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );

        Locale locale = getLocale( );

        AdminUser connectedUser = AdminUserService.getAdminUser( request );
        List<Unit> listUnit = _unitService.getUnitsByIdUser( connectedUser.getUserId( ), false );
        Unit unit = listUnit.isEmpty( ) ? null : listUnit.get( 0 );

        model.put( MARK_FILTER_LIST, _feuilleTourneeService.loadSearchFilter( unit != null ? unit.getIdUnit( ) : DEFAULT_ID ) );

        List<TypeSignalement> types = _typeSignalementService.getAllTypeSignalementActif( );
        model.put( MARK_TYPE_LIST, types );
        List<State> states = getListeEtats( );
        model.put( MARK_ETAT_LIST, states );

        // get all the priorities
        List<Priorite> priorites = _prioriteService.getAllPriorite( );
        model.put( MARK_PRIORITE_LIST, priorites );

        // get all arrondissements
        List<Arrondissement> arrondissements = _arrondissementService.getAllArrondissement( );
        model.put( MARK_ARRONDISSEMENT_LIST, arrondissements );

        // quartier
        model.put( MARK_CONSEIL_QUARTIER_LIST, _conseilQuartier.selectQuartiersList( ) );

        // Entite
        List<Sector> listSectorsOfUnits = getListSectorAllowed( AdminUserService.getAdminUser( request ) );
        ReferenceList listeSecteur = ListUtils.toReferenceList( listSectorsOfUnits, ID_SECTOR, "name", StringUtils.EMPTY, true );
        model.put( MARK_SECTEUR_LIST, listeSecteur );

        if ( ( request.getParameter( PARAMETER_FILTER_LOAD ) != null ) && ( Integer.parseInt( request.getParameter( PARAMETER_FILTER_LOAD ) ) > 0 ) )
        {
            int idFilter = Integer.parseInt( request.getParameter( PARAMETER_FILTER_LOAD ) );

            model.put( MARK_FILTER, initFilterWithSelectedFilter( states, idFilter ) );
            model.put( MARK_ID_FILTER_SELECT, idFilter );
        }
        else
        {
            model.put( MARK_FILTER, initFilter( states, priorites, arrondissements ) );
            model.put( MARK_ID_FILTER_SELECT, DEFAULT_ID );
        }

        model.put( MARK_ID_ETATS_DEFAULT, ID_ETATS_DEFAULT );

        // Filtre de recherche

        model.put( MARK_SEARCH_FILTER_CREATOR, connectedUser.getEmail( ) );

        model.put( MARK_USER_ENTITE, unit != null ? ( unit.getLabel( ) + "_" ) : "" );

        model.put( SignalementConstants.MARK_LOCALE, request.getLocale( ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_CREATE, locale, model );

        return getAdminPage( templateList.getHtml( ) );
    }

    /**
     * Init signalement Filter.
     *
     * @param states
     *            All display state
     * @param priorites
     *            all priorites
     * @param arrondissements
     *            all arrondisements
     * @return SignalementFilter
     */
    private SignalementFilter initFilter( List<State> states, List<Priorite> priorites, List<Arrondissement> arrondissements )
    {
        _signalementFilter = new SignalementFilter( );

        _signalementFilter.setListIdTypeSignalements( new ArrayList<Integer>( ) );

        List<EtatSignalement> etats = new ArrayList<>( );
        for ( State state : states )
        {
            /*
             * initialization of default values a New, To be processed, To be done in the field and To be done Office
             */
            if ( ( state != null ) && ( ( state.getId( ) == SignalementConstants.ID_STATE_NOUVEAU )
                    || ( state.getId( ) == SignalementConstants.ID_STATE_A_TRAITER ) || ( state.getId( ) == SignalementConstants.ID_STATE_A_FAIRE_TERRAIN )
                    || ( state.getId( ) == SignalementConstants.ID_STATE_A_FAIRE_BUREAU )
                    || ( state.getId( ) == SignalementConstants.ID_STATE_TRANSFERE_PRESTATAIRE )
                    || ( state.getId( ) == SignalementConstants.ID_STATE_PROGRAMME_PRESTATAIRE ) ) )
            {

                EtatSignalement etatSignalement = new EtatSignalement( );

                etatSignalement.setCoche( true );
                etatSignalement.setId( (long) state.getId( ) );
                etatSignalement.setLibelle( state.getName( ) );
                etats.add( etatSignalement );
            }
        }
        _signalementFilter.setEtats( etats );
        _signalementFilter.setPriorites( priorites.stream( ).map( priorite -> priorite.getId( ).intValue( ) ).collect( Collectors.toList( ) ) );
        _signalementFilter.setListIdArrondissements( arrondissements.stream( ).map( arr -> arr.getId( ).intValue( ) ).collect( Collectors.toList( ) ) );
        _signalementFilter.setListIdQuartier( new ArrayList<Integer>( ) );

        int nbdays = AppPropertiesService.getPropertyInt( PROPERTY_SEARCH_INITIAL_NB_DAYS, -1 );
        if ( nbdays > 0 )
        {
            _signalementFilter.setDateBegin( LocalDate.now( ).minusDays( nbdays ).format( DateTimeFormatter.ofPattern( DateUtils.DATE_FR ) ) );

        }

        // Date de fin à la date actuelle
        _signalementFilter.setDateEnd( LocalDate.now( ).format( DateTimeFormatter.ofPattern( DateUtils.DATE_FR ) ) );

        return _signalementFilter;
    }

    /**
     * Init signalement Filter with filter load.
     *
     * @param states       All display state
     * @param idFilterLoad id filter select
     * @return SignalementFilter
     */
    private SignalementFilter initFilterWithSelectedFilter( List<State> states, int idFilterLoad )
    {

        String jsonFilterValue = _feuilleTourneeService.loadSearchFilterById( idFilterLoad );
        JSONObject jsonObjectFilterValue = JSONObject.fromObject( jsonFilterValue );

        return fillSignalementFilterFromJsonObject( jsonObjectFilterValue, states );
    }

    /**
     * add Default Order search.
     *
     * @param request
     *            the request
     */
    private void addFilterOrder( HttpServletRequest request )
    {

        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        if ( strSortedAttributeName != null )
        {
            // during a ASC/DESC sorting and action
            _signalementFilter.setOrders( new ArrayList<Order>( ) );

            String strAscSort = request.getParameter( Parameters.SORTED_ASC );
            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );
            _signalementFilter.setOrderAsc( bIsAscSort );
            Order order = new Order( strSortedAttributeName, bIsAscSort );

            _signalementFilter.getOrders( ).add( order );
        }
        else
        {
            strSortedAttributeName = "fdt.default";
            boolean bIsAscSort = true;
            _signalementFilter.setOrderAsc( bIsAscSort );
            Order order = new Order( strSortedAttributeName, bIsAscSort );
            _signalementFilter.getOrders( ).add( order );

        }

    }

    /**
     * Fill Signalement Filter object.
     *
     * @param jsonObjectFilterValue
     *            filter value
     * @param states
     *            All display state
     * @return SignalementFilter
     */
    private SignalementFilter fillSignalementFilterFromJsonObject( JSONObject jsonObjectFilterValue, List<State> states )
    {

        // State
        List<EtatSignalement> etats = new ArrayList<>( );
        for ( State state : states )
        {
            JSONArray arrayIdStateSelect = jsonObjectFilterValue.getJSONArray( PARAMETER_ETAT );
            for ( int i = 0; i < arrayIdStateSelect.size( ); i++ )
            {
                if ( arrayIdStateSelect.getInt( i ) == state.getId( ) )
                {
                    EtatSignalement etatSignalement = new EtatSignalement( );

                    etatSignalement.setCoche( true );
                    etatSignalement.setId( (long) state.getId( ) );
                    etatSignalement.setLibelle( state.getName( ) );
                    etats.add( etatSignalement );
                }
            }
        }

        _signalementFilter = new SignalementFilter( );

        _signalementFilter
        .setListIdTypeSignalements( new ArrayList<Integer>( JSONArray.toCollection( jsonObjectFilterValue.getJSONArray( PARAMETER_TYPOLOGIE ) ) ) );
        _signalementFilter.setEtats( etats );
        _signalementFilter.setPriorites( new ArrayList<Integer>( JSONArray.toCollection( jsonObjectFilterValue.getJSONArray( PARAMETER_PRIORITE ) ) ) );
        _signalementFilter
        .setListIdArrondissements( new ArrayList<Integer>( JSONArray.toCollection( jsonObjectFilterValue.getJSONArray( PARAMETER_ARRONDISEMENT ) ) ) );
        _signalementFilter.setListIdQuartier( new ArrayList<Integer>( JSONArray.toCollection( jsonObjectFilterValue.getJSONArray( PARAMETER_QUARTIER ) ) ) );

        _signalementFilter.setDateBegin( jsonObjectFilterValue.getString( PARAMETER_DATE_BEGIN ) );
        _signalementFilter.setDateEnd( jsonObjectFilterValue.getString( PARAMETER_DATE_END ) );

        _signalementFilter.setDateRequalificationBegin( jsonObjectFilterValue.getString( PARAMETER_DATE_REQUALIFICATION_BEGIN ) );
        _signalementFilter.setDateRequalificationEnd( jsonObjectFilterValue.getString( PARAMETER_DATE_REQUALIFICATION_END ) );

        _signalementFilter.setDateProgrammationBegin( jsonObjectFilterValue.getString( PARAMETER_DATE_PROGRAMMATION_BEGIN ) );
        _signalementFilter.setDateProgrammationEnd( jsonObjectFilterValue.getString( PARAMETER_DATE_PROGRAMMATION_END ) );

        _signalementFilter.setCommentaireAgentTerrain( jsonObjectFilterValue.getString( PARAMETER_COMMENTAIRE_AGENT_TERRAIN ) );
        _signalementFilter.setCommentaire( jsonObjectFilterValue.getString( PARAMETER_COMMENTAIRE_USAGER ) );
        _signalementFilter.setAdresse( jsonObjectFilterValue.getString( PARAMETER_ADRESSE ) );
        _signalementFilter.setIdSector( jsonObjectFilterValue.getInt( PARAMETER_ENTITE ) );
        _signalementFilter.setMail( jsonObjectFilterValue.getString( PARAMETER_MAIL_USAGER ) );

        return _signalementFilter;

    }

    /**
     * Get List sector alloed for user.
     *
     * @param adminUser
     *            Connected user
     * @return List sector
     */
    private List<Sector> getListSectorAllowed( AdminUser adminUser )
    {
        // get the user's allowed sectors
        List<Sector> listSectorsOfUnits = new ArrayList<>( );
        List<Unit> listUnits = _unitService.getUnitsByIdUser( adminUser.getUserId( ), false );
        for ( Unit userUnit : listUnits )
        {
            for ( Sector secteurUnit : _sectorService.loadByIdUnitWithoutChosenId( userUnit.getIdUnit( ), ID_JARDIN ) )
            {
                boolean bFound = false;
                for ( Sector secteur : listSectorsOfUnits )
                {
                    if ( secteur.getIdSector( ) == secteurUnit.getIdSector( ) )
                    {
                        bFound = true;
                        break;
                    }
                }
                if ( !bFound )
                {
                    listSectorsOfUnits.add( secteurUnit );
                }
            }
        }

        return listSectorsOfUnits;
    }

    /**
     * List of states available for filtering.
     *
     * @return a state list
     */
    private List<State> getListeEtats( )
    {
        List<State> listeEtat = (List<State>) WorkflowService.getInstance( ).getAllStateByWorkflow( SignalementConstants.SIGNALEMENT_WORKFLOW_ID, getUser( ) );
        List<State> listeTemp = new ArrayList<>( );
        String [ ] idStateNotDisplay = AppPropertiesService.getProperty( STATE_NOT_DISPLAY ).split( "," );

        if ( idStateNotDisplay.length > 0 )
        {
            List<Integer> listTmp = Arrays.asList( idStateNotDisplay ).stream( ).map( Integer::parseInt ).collect( Collectors.toList( ) );
            listTmp.stream( ).forEach( ( Integer idState ) -> {
                for ( State state : listeEtat )
                {
                    if ( state.getId( ) == idState.intValue( ) )
                    {
                        listeTemp.add( state );
                    }
                }
            } );
        }
        listeEtat.removeAll( listeTemp );

        listeEtat.sort( ( o1, o2 ) -> Integer.valueOf( o1.getOrder( ) ).compareTo( Integer.valueOf( o2.getOrder( ) ) ) );

        return listeEtat;
    }

    /**
     * Page de création d'une feuille de tournée.
     *
     * @param request
     *            the request
     * @return the creates the
     * @throws AccessDeniedException
     *             the access denied exception
     */
    @View( value = VIEW_EDIT )
    public String getEdit( HttpServletRequest request ) throws AccessDeniedException
    {
        Map<String, Object> model = getModel( );
        AdminUser connectedUser = AdminUserService.getAdminUser( request );

        int feuilledeDeTourneId = -1;

        if ( request.getParameter( PARAMETER_FEUILLE_DE_TOURNEE_ID ) != null )
        {
            feuilledeDeTourneId = Integer.parseInt( request.getParameter( PARAMETER_FEUILLE_DE_TOURNEE_ID ) );
        }

        FeuilleDeTournee feuilleDeTournee = _feuilleTourneeService.load( feuilledeDeTourneId );

        if ( feuilleDeTournee.getId( ) != null )
        {
            int idEntite = feuilleDeTournee.getIdEntite() > -1 ? feuilleDeTournee.getIdEntite() : feuilleDeTournee.getIdDirection();
            if ( !_feuilleTourneeService.isUnitAuthorizedToUser( connectedUser, _unitService.getUnit( idEntite, false ) ) )
            {
                throw new AccessDeniedException( MESSAGE_ACCESS_DENIED );
            }

            String ongletActif = request.getParameter( PARAMETER_ONGLET_ACTIF );
            if ( ( ongletActif == null ) && ( request.getParameter( PAGE_INDEX ) == null ) )
            {
                ongletActif = ONGLET_ACTIF_CARTE;
            }
            else if ( ongletActif == null )
            {
                ongletActif = ONGLET_ACTIF_TABLEAU;
            }
            model.put( MARK_ONGLET_ACTIF, ongletActif );


            List<SignalementExportCSVDTO> signalements;

            String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
            if ( strSortedAttributeName != null )
            {
                String strAscSort = request.getParameter( Parameters.SORTED_ASC );
                boolean bIsAscSort = Boolean.parseBoolean( strAscSort );
                Order order = new Order( strSortedAttributeName, bIsAscSort );

                signalements = _signalementExportService
                        .findByIdsWithPhotoWithOrder( feuilleDeTournee.getListSignalementIds( ).stream( ).mapToInt( i -> i ).toArray( ), order );
            }
            else
            {
                signalements = _signalementExportService
                        .findByIdsWithPhoto( feuilleDeTournee.getListSignalementIds( ).stream( ).mapToInt( i -> i ).toArray( ) );
            }

            if ( request.getParameterValues( PARAMETER_SIGNALEMENTS_SELECT_IDS ) != null )
            {
                model.put( MARK_SIGNALEMENTS_SELECT_IDS, Arrays.asList( request.getParameter( PARAMETER_SIGNALEMENTS_SELECT_IDS ).split(",") ).
                        stream( ).map( str -> Integer.parseInt( str ) ).collect( Collectors.toList( ) ) );
            }
            else
            {
                model.put( MARK_SIGNALEMENTS_SELECT_IDS, feuilleDeTournee.getListSignalementIds( ) );
            }



            ResultList<SignalementExportCSVDTO> resultListSignalement = managePaginationForViewEditAndLoad(request,signalements);

            LocalizedDelegatePaginator<SignalementExportCSVDTO> paginator = this.getPaginator( request, resultListSignalement,
                    URL_JSP_MANAGE_FEUILLE_TOURNEE + "?view=edit&idFeuilleDeTournee=" + feuilledeDeTourneId, signalements.size( ) );

            model.put( SignalementConstants.MARK_NB_ITEMS_PER_PAGE, StringUtils.EMPTY + _nItemsPerPage );
            model.put( MARK_FEUILLE_DE_TOURNEE, feuilleDeTournee );
            model.put( MARK_UPDATED_PART_FDT_NAME,
                    feuilleDeTournee.getNom( ).substring( StringUtils.ordinalIndexOf( feuilleDeTournee.getNom( ), "_", 4 ) + 1 ) );
            model.put( MARK_FDT_COMMENT, feuilleDeTournee.getCommentaire( ) );
            model.put( SignalementConstants.MARK_PAGINATOR, paginator );
            model.put( MARK_SIGNALEMENT_LIST, paginator.getPageItems( ) );
            model.put( MARK_NOM_TEMPLATE, NOM_TEMPLATE );
            model.put( MARK_SIGNALEMENTS_MAP_LIST, _feuilleTourneeService.getSignalementsMapMarkerDTOFromSignalementsEdition( signalements, request.getLocale( ) ) );

            List<Unit> listUnit = _unitService.getUnitsByIdUser( connectedUser.getUserId( ), false );
            Unit unit = listUnit.isEmpty( ) ? null : listUnit.get( 0 );
            model.put( MARK_USER_ENTITE, unit != null ? unit.getLabel( ) : "" );

            model.put( MARK_USER_LOGIN, connectedUser.getAccessCode( ) );
            model.put( MARK_MARKER_SELECT, request.getParameter( PARAMETER_MARKER_SELECT ) );
            model.put( MARK_DIRECTION_LIST, ListUtils.toReferenceList( _unitService.getUnitsFirstLevel( false ), ID_UNIT, LABEL, StringUtils.EMPTY ) );
            model.put( MARK_ENTITE_LIST, ListUtils.toReferenceList( getEntityListByIdDirection( feuilleDeTournee.getIdDirection( ) ), ID_UNIT, LABEL, null ) );
        }
        else
        {
            throw new AccessDeniedException( MESSAGE_ACCESS_DENIED );
        }

        Locale locale = getLocale( );
        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_EDIT, locale, model );

        return getAdminPage( templateList.getHtml( ) );
    }

    /**
     * Page de création d'une feuille de tournée.
     *
     * @param request
     *            the request
     * @return the creates the
     */
    @View( value = VIEW_LOAD )
    public String getLoad( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );
        int feuilledeDeTourneId = -1;

        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );

        if ( request.getAttribute( PARAMETER_FEUILLE_DE_TOURNEE_ID ) != null )
        {
            feuilledeDeTourneId = (int) request.getAttribute( PARAMETER_FEUILLE_DE_TOURNEE_ID );
            request.setAttribute( PARAMETER_FEUILLE_DE_TOURNEE_ID, null );
        }
        else
            if ( request.getParameter( PARAMETER_FEUILLE_DE_TOURNEE_ID ) != null )
            {
                feuilledeDeTourneId = Integer.parseInt( request.getParameter( PARAMETER_FEUILLE_DE_TOURNEE_ID ) );
            }

        String ongletActif = request.getParameter( PARAMETER_ONGLET_ACTIF );
        if ( ( ongletActif == null ) && ( request.getParameter( PAGE_INDEX ) == null ) )
        {
            ongletActif = ONGLET_ACTIF_CARTE;
        }
        else if ( ongletActif == null )
        {
            ongletActif = ONGLET_ACTIF_TABLEAU;
        }

        FeuilleDeTournee feuilleDeTournee = _feuilleTourneeService.load( feuilledeDeTourneId );

        if ( feuilleDeTournee != null )
        {

            int [ ] idsSignalement = feuilleDeTournee.getListSignalementIds( ).stream( ).mapToInt( i -> i ).toArray( );

            List<SignalementExportCSVDTO> signalements;

            if ( strSortedAttributeName != null )
            {
                String strAscSort = request.getParameter( Parameters.SORTED_ASC );
                boolean bIsAscSort = Boolean.parseBoolean( strAscSort );
                Order order = new Order( strSortedAttributeName, bIsAscSort );

                signalements = _signalementExportService.findByIdsWithPhotoWithOrder( idsSignalement, order );
            }
            else
            {
                signalements = _signalementExportService.findByIdsWithPhoto( idsSignalement );
            }


            ResultList<SignalementExportCSVDTO> resultListSignalement = managePaginationForViewEditAndLoad(request,signalements);

            LocalizedDelegatePaginator<SignalementExportCSVDTO> paginator = this.getPaginator( request, resultListSignalement,
                    URL_JSP_MANAGE_FEUILLE_TOURNEE + "?view=load&idFeuilleDeTournee=" + feuilledeDeTourneId, signalements.size( ) );

            model.put( SignalementConstants.MARK_NB_ITEMS_PER_PAGE, StringUtils.EMPTY + _nItemsPerPage );
            model.put( MARK_FEUILLE_DE_TOURNEE, feuilleDeTournee );
            model.put( SignalementConstants.MARK_PAGINATOR, paginator );
            model.put( MARK_SIGNALEMENT_LIST, paginator.getPageItems( ) );
            model.put( MARK_NOM_TEMPLATE, NOM_TEMPLATE );
            model.put( MARK_SIGNALEMENTS_MAP_LIST,
                    _feuilleTourneeService.getSignalementsMapMarkerDTOFromSignalementsConsultation( signalements, request.getLocale( ) ) );
            model.put( MARK_ONGLET_ACTIF, ongletActif );
            model.put( MARK_MARKER_SELECT, request.getParameter( PARAMETER_MARKER_SELECT ) );
        }

        Locale locale = getLocale( );
        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_LOAD, locale, model );

        return getAdminPage( templateList.getHtml( ) );
    }


    private ResultList<SignalementExportCSVDTO> managePaginationForViewEditAndLoad(HttpServletRequest request, List<SignalementExportCSVDTO> signalements ) {

        ResultList<SignalementExportCSVDTO> resultListSignalement = new ResultList<>( );


        PaginationProperties paginationProerties = getPaginationProperties( request, signalements.size( ) );
        int minIndex = paginationProerties.getFirstResult( );
        int maxIndex = ( paginationProerties.getPageIndex( ) * paginationProerties.getItemsPerPage( ) ) < signalements.size( )
                ? paginationProerties.getPageIndex( ) * paginationProerties.getItemsPerPage( )
                        : signalements.size( );
        List<SignalementExportCSVDTO> signalementToDisplay = signalements.subList( minIndex, maxIndex );

        resultListSignalement.addAll( signalementToDisplay );

        return resultListSignalement;
    }

    /**
     * Do search ano.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( ACTION_INIT_SEARCH_ANO )
    public String doInitSearchAno( HttpServletRequest request )
    {

        JSONObject jsonFilterValue = JSONObject.fromObject( buildJsonSearchFilter( request ) );
        _signalementFilter = fillSignalementFilterFromJsonObject( jsonFilterValue, getListeEtats( ) );

        return doSearchAno( request );
    }

    /**
     * Do search ano.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( ACTION_SEARCH_ANO )
    public String doSearchAno( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );

        String ongletActif = request.getParameter( PARAMETER_ONGLET_ACTIF );
        if ( ( ongletActif == null ) && ( request.getParameter( PAGE_INDEX ) == null ) )
        {
            ongletActif = ONGLET_ACTIF_CARTE;
        }
        else if ( ongletActif == null )
        {
            ongletActif = ONGLET_ACTIF_TABLEAU;
        }

        if ( ( _signalementFilter.getListIdUnit( ) == null ) || _signalementFilter.getListIdUnit( ).isEmpty( ) )
        {
            AdminUser connectedUser = AdminUserService.getAdminUser( request );
            List<Unit> listUnits = _unitService.getUnitsByIdUser( connectedUser.getUserId( ), false );
            _signalementFilter.setListIdUnit( listUnits.stream( ).map( Unit::getIdUnit ).collect( Collectors.toList( ) ) );
        }

        addFilterOrder( request );

        int totalAnomalies = _signalementExportService.countSearchResult( _signalementFilter );

        if ( !_feuilleTourneeService.isNbResultOk( totalAnomalies ) )
        {
            String [ ] args = new String [ 1];
            args [0] = DatastoreService.getDataValue( "sitelabels.site_property.feuille.de.tournee.limite.signalement", "0" );

            return redirect( request, AdminMessageService.getMessageUrl( request, "dansmarue.map.max.results.execeeded", args, AdminMessage.TYPE_STOP ) );
        }

        if ( ongletActif.equals( ONGLET_ACTIF_CARTE ) )
        {
            List<Signalement> listSignalementsMap = _signalementExportService.findByFilterSearch( _signalementFilter, null );
            model.put( MARK_SIGNALEMENTS_MAP_LIST, _feuilleTourneeService.getSignalementsMapMarkerDTO( listSignalementsMap, request.getLocale( ) ) );
        }
        else
        {
            List<Signalement> listSignalements = _signalementExportService.findByFilterSearch( _signalementFilter,
                    getPaginationProperties( request, totalAnomalies ) );

            ResultList<Signalement> resultListSignalement = new ResultList<>( );

            resultListSignalement.addAll( listSignalements );

            resultListSignalement.setTotalResult( listSignalements.size( ) );
            LocalizedDelegatePaginator<Signalement> paginator = this.getPaginator( request, resultListSignalement,
                    URL_JSP_MANAGE_FEUILLE_TOURNEE + "?action=doSearchAno", totalAnomalies );

            // the paginator
            model.put( SignalementConstants.MARK_NB_ITEMS_PER_PAGE, StringUtils.EMPTY + _nItemsPerPage );
            model.put( SignalementConstants.MARK_PAGINATOR, paginator );
            model.put( MARK_SIGNALEMENT_LIST, paginator.getPageItems( ) );

            model.put( MARK_TOTAL_SIGNALEMENT, listSignalements.size( ) );
        }

        model.put( MARK_ONGLET_ACTIF, ongletActif );
        model.put( MARK_MARKER_SELECT, request.getParameter( PARAMETER_MARKER_SELECT ) );

        if ( request.getParameterValues( PARAMETER_SIGNALEMENTS_SELECT_IDS ) != null )
        {
            model.put( MARK_SIGNALEMENTS_SELECT_IDS, Arrays.asList( request.getParameterValues( PARAMETER_SIGNALEMENTS_SELECT_IDS ) ) );
        }

        AdminUser connectedUser = AdminUserService.getAdminUser( request );
        List<Unit> listUnit = _unitService.getUnitsByIdUser( connectedUser.getUserId( ), false );
        Unit unit = listUnit.isEmpty( ) ? null : listUnit.get( 0 );
        model.put( MARK_USER_ENTITE, unit != null ? unit.getLabel( ) : "" );

        model.put( MARK_USER_LOGIN, connectedUser.getAccessCode( ) );

        ReferenceList directionList = new ReferenceList( );
        directionList.addItem( "", "" );
        directionList.addAll( ListUtils.toReferenceList( _unitService.getUnitsFirstLevel( false ), ID_UNIT, LABEL, null ) );
        model.put( MARK_DIRECTION_LIST, directionList );

        Locale locale = getLocale( );
        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_SELECT, locale, model );

        return getAdminPage( templateList.getHtml( ) );
    }

    /**
     * Gets the delete FDT.
     *
     * @param request
     *            the request
     * @return the delete FDT
     * @throws AccessDeniedException
     *             the access denied exception
     */
    @Action( ACTION_GET_DELETE_FDT )
    public String getDeleteFDT( HttpServletRequest request ) throws AccessDeniedException
    {
        String strFeuilleDeTourneeId = request.getParameter( PARAMETER_FEUILLE_DE_TOURNEE_ID );

        int nFeuilleDeTourneeId = 0;

        try
        {
            nFeuilleDeTourneeId = Integer.parseInt( strFeuilleDeTourneeId );
        }
        catch( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        Map<String, Object> urlParam = new HashMap<>( );
        urlParam.put( PARAMETER_FEUILLE_DE_TOURNEE_ID, nFeuilleDeTourneeId );

        AdminUser connectedUser = AdminUserService.getAdminUser( request );

        FeuilleDeTournee feuilleDeTournee = _feuilleTourneeService.load( nFeuilleDeTourneeId );

        if ( feuilleDeTournee == null )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        if ( !_feuilleTourneeService.isUnitAuthorizedToUser( connectedUser, _unitService.getUnit( feuilleDeTournee.getUnitId( ), false ) ) )
        {
            throw new AccessDeniedException( MESSAGE_ACCESS_DENIED );
        }

        return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRMATION_DELETE_FDT, null, MESSAGE_TITLE_DELETE_FDT,
                URL_JSP_DELETE_FEUILLE_TOURNEE, "_self", AdminMessage.TYPE_CONFIRMATION, urlParam, URL_JSP_MANAGE_FEUILLE_TOURNEE ) );
    }

    /**
     * Do delete feuille de tournee.
     *
     * @param request
     *            the request
     * @return the string
     */
    public String doDeleteFeuilleDeTournee( HttpServletRequest request )
    {
        String strFeuilleDeTourneeId = request.getParameter( PARAMETER_FEUILLE_DE_TOURNEE_ID );

        int nFeuilleDeTourneeId = 0;

        try
        {
            nFeuilleDeTourneeId = Integer.parseInt( strFeuilleDeTourneeId );
        }
        catch( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        _feuilleTourneeService.delete( nFeuilleDeTourneeId );
        return doGoBack( request );
    }

    /**
     * Return the url of the JSP which called the last action.
     *
     * @param request
     *            The Http request
     * @return The url of the last JSP
     */
    private String doGoBack( HttpServletRequest request )
    {
        String strJspBack = request.getParameter( SignalementConstants.MARK_JSP_BACK );

        return StringUtils.isNotBlank( strJspBack ) ? ( AppPathService.getBaseUrl( request ) + strJspBack )
                : ( AppPathService.getBaseUrl( request ) + URL_JSP_MANAGE_FEUILLE_TOURNEE );
    }

    @Action( ACTION_SAVE_FDT )
    public String doSaveFDT( HttpServletRequest request )
    {
        if ( ( request.getParameterValues( PARAMETER_SIGNALEMENTS_SELECT_IDS ) != null )
                && !request.getParameterValues( PARAMETER_SIGNALEMENTS_SELECT_IDS ) [0].isEmpty( ) )
        {
            String name = request.getParameter( PARAMETER_SAVE_NAME_PREFIX ) + request.getParameter( PARAMETER_SAVE_NAME );
            List<String> idsSelectStr = Arrays.asList( request.getParameterValues( PARAMETER_SIGNALEMENTS_SELECT_IDS ) [0].split( "," ) );
            List<Integer> idsSelect = idsSelectStr.stream( ).map( Integer::parseInt ).collect( Collectors.toList( ) );

            String comment = request.getParameter( PARAMETER_SAVE_COMMENT );
            Integer idEntite = request.getParameter( PARAMETER_ENTITE_ID ) != null ? Integer.parseInt( request.getParameter( PARAMETER_ENTITE_ID ) ) : null;
            Integer idDirection = request.getParameter( PARAMETER_DIRECTION_ID ) != null ? Integer.parseInt( request.getParameter( PARAMETER_DIRECTION_ID ) )
                    : null;

            AdminUser connectedUser = AdminUserService.getAdminUser( request );
            List<Unit> listUnit = _unitService.getUnitsByIdUser( connectedUser.getUserId( ), false );
            Unit unit = listUnit.isEmpty( ) ? null : listUnit.get( 0 );
            int idUnit = unit == null ? DEFAULT_ID : unit.getIdUnit( );

            Boolean isUpdate = request.getParameter( PARAMETER_SAVE_IS_UPDATE ) != null;

            FeuilleDeTournee feuilleDeTournee = new FeuilleDeTournee( AdminUserService.getAdminUser( request ).getAccessCode( ), idUnit, comment, 1, idsSelect,
                    name, idDirection, idEntite );

            List<ValidationError> errors = validate( feuilleDeTournee, "" );
            if ( !errors.isEmpty( ) )
            {
                return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, errors ) );
            }

            if ( request.getParameter( PARAMETER_CARTE_BASE64 ) != null )
            {
                feuilleDeTournee.setCarteBase64( request.getParameter( PARAMETER_CARTE_BASE64 ) );
            }

            int feuilleDeTourneeId;

            if ( Boolean.FALSE.equals( isUpdate ) )
            {
                feuilleDeTourneeId = _feuilleTourneeService.insert( feuilleDeTournee );
            }
            else
            {
                feuilleDeTourneeId = _feuilleTourneeService.updateByName( feuilleDeTournee );
            }
            request.setAttribute( PARAMETER_FEUILLE_DE_TOURNEE_ID, feuilleDeTourneeId );

            return getLoad( request );
        }
        else
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_ERREUR_SELECTION, AdminMessage.TYPE_ERROR ) );
        }
    }

    @Action( ACTION_UPDATE_FDT )
    public String doUpdateFDT( HttpServletRequest request )
    {
        String strFeuilleDeTourneeId = request.getParameter( PARAMETER_FEUILLE_DE_TOURNEE_ID );

        if ( ( request.getParameterValues( PARAMETER_SIGNALEMENTS_SELECT_IDS ) != null )
                && !request.getParameterValues( PARAMETER_SIGNALEMENTS_SELECT_IDS ) [0].isEmpty( ) && ( strFeuilleDeTourneeId != null ) )
        {
            FeuilleDeTournee feuilleDeTournee = populateFeuilleDeTournee( request );

            String nom = request.getParameter( PARAMETER_SAVE_NAME_PREFIX ) + request.getParameter( PARAMETER_SAVE_NAME );
            String originalName = feuilleDeTournee.getNom( ).substring( StringUtils.ordinalIndexOf( feuilleDeTournee.getNom( ), "_", 4 ) + 1 );
            String newName = nom.substring( StringUtils.ordinalIndexOf( nom, "_", 4 ) + 1 );

            if ( originalName.equals( newName ) )
            {
                _feuilleTourneeService.update( feuilleDeTournee );
                request.setAttribute( PARAMETER_FEUILLE_DE_TOURNEE_ID, feuilleDeTournee.getId( ) );
            }
            else
            {
                AdminUser connectedUser = AdminUserService.getAdminUser( request );
                String userAccessCode = connectedUser.getAccessCode( );
                if ( !StringUtils.isEmpty( userAccessCode ) )
                {
                    feuilleDeTournee.setCreateur( userAccessCode );
                }

                feuilleDeTournee.setNom( nom );
                Integer newId = _feuilleTourneeService.insert( feuilleDeTournee );
                request.setAttribute( PARAMETER_FEUILLE_DE_TOURNEE_ID, newId );
            }

            return getLoad( request );
        }
        else
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_ERREUR_SELECTION, AdminMessage.TYPE_ERROR ) );
        }
    }

    private FeuilleDeTournee populateFeuilleDeTournee( HttpServletRequest request )
    {
        Integer feuilleDeTourneeId = Integer.parseInt( request.getParameter( PARAMETER_FEUILLE_DE_TOURNEE_ID ) );
        List<String> idsSelectStr = Arrays.asList( request.getParameterValues( PARAMETER_SIGNALEMENTS_SELECT_IDS )[0].split( "," ) );
        List<Integer> idsSelect = idsSelectStr.stream( ).map( Integer::parseInt ).collect( Collectors.toList( ) );
        String comment = request.getParameter( PARAMETER_SAVE_COMMENT );

        Integer idEntite = request.getParameter( PARAMETER_ENTITE_ID ) != null ? Integer.parseInt( request.getParameter( PARAMETER_ENTITE_ID ) ) : -1;
        Integer idDirection = request.getParameter( PARAMETER_DIRECTION_ID ) != null ? Integer.parseInt( request.getParameter( PARAMETER_DIRECTION_ID ) ) : null;

        FeuilleDeTournee feuilleDeTournee = _feuilleTourneeService.load( feuilleDeTourneeId );
        feuilleDeTournee.setListSignalementIds( idsSelect );
        feuilleDeTournee.setCommentaire( comment );
        feuilleDeTournee.setIdDirection( idDirection );
        feuilleDeTournee.setIdEntite( idEntite );

        return feuilleDeTournee;
    }

    /**
     * Do save filter.
     *
     * @param request the request
     * @return the string
     */
    @Action( ACTION_SAVE_FILTER )
    public String doSaveFilter( HttpServletRequest request )
    {

        return processSaveOrUpdateFilter( request, false );
    }

    /**
     * Do update filter.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( ACTION_UPDATE_FILTER )
    public String doUpdateFilter( HttpServletRequest request )
    {

        return processSaveOrUpdateFilter( request, true );
    }

    /**
     * Do delete filter.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( ACTION_DELETE_FILTER )
    public String doDeleteFilter( HttpServletRequest request )
    {
        String idFilter = request.getParameter( PARAMETER_SEARCH_FILTER_SELECT );
        if ( idFilter != null )
        {
            _feuilleTourneeService.deleteFilterSearch( Integer.valueOf( idFilter ) );
        }

        return redirectView( request, VIEW_CREATE );
    }

    /**
     * processSaveOrUpdateFilter.
     *
     * @param request
     *            the request
     * @param isUpdate
     *            is an update ?
     * @return the string
     */
    private String processSaveOrUpdateFilter( HttpServletRequest request, boolean isUpdate )
    {

        String creator = request.getParameter( PARAMETER_SEARCH_FILTER_CREATOR );
        String name = request.getParameter( PARAMETER_SEARCH_FILTER_NAME );
        String comment = request.getParameter( PARAMETER_SEARCH_FILTER_COMMENT );
        String searchFilter = buildJsonSearchFilter( request );

        AdminUser connectedUser = AdminUserService.getAdminUser( request );
        List<Unit> listUnit = _unitService.getUnitsByIdUser( connectedUser.getUserId( ), false );
        Unit unit = listUnit.isEmpty( ) ? null : listUnit.get( 0 );
        int idUnit = unit == null ? DEFAULT_ID : unit.getIdUnit( );

        String feuilleTourneeName = unit != null ? ( unit.getLabel( ) + "_" + name ) : name;
        _feuilleTourneeService.saveFilterSearch( creator, feuilleTourneeName, comment, searchFilter, idUnit, isUpdate );

        return redirectView( request, VIEW_CREATE );
    }

    /**
     * Do load filter.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( ACTION_LOAD_FILTER )
    public String doLoadFilter( HttpServletRequest request )
    {
        String idFilter = request.getParameter( PARAMETER_SEARCH_FILTER_SELECT );

        if ( idFilter != null )
        {
            return redirect( request, VIEW_CREATE, PARAMETER_FILTER_LOAD, Integer.valueOf( idFilter ) );
        }

        return redirectView( request, VIEW_CREATE );
    }

    /**
     * Build Json string to save search filter property
     *
     * @param request
     *            the request
     * @return json string
     */
    private String buildJsonSearchFilter( HttpServletRequest request )
    {

        List<Integer> selectedState = new ArrayList<>( );
        List<Integer> selectedPriorite = new ArrayList<>( );
        List<Integer> selectedTypologie = new ArrayList<>( );
        List<Integer> selectedArrondissement = new ArrayList<>( );
        List<Integer> selectedQuartier = new ArrayList<>( );

        if ( request.getParameterValues( PARAMETER_TYPOLOGIE ) != null )
        {
            selectedTypologie = Arrays.asList( request.getParameterValues( PARAMETER_TYPOLOGIE ) ).stream( ).map( Integer::valueOf )
                    .collect( Collectors.toList( ) );
        }

        if ( request.getParameterValues( PARAMETER_QUARTIER ) != null )
        {
            selectedQuartier = Arrays.asList( request.getParameterValues( PARAMETER_QUARTIER ) ).stream( ).map( Integer::valueOf )
                    .collect( Collectors.toList( ) );
        }

        request.getParameterMap( ).forEach( ( k, v ) -> {
            if ( k.startsWith( PARAMETER_ETAT ) )
            {
                selectedState.add( Integer.valueOf( k.substring( PARAMETER_ETAT.length( ) ) ) );
            }

            if ( k.startsWith( PARAMETER_PRIORITE ) )
            {
                selectedPriorite.add( Integer.valueOf( k.substring( PARAMETER_PRIORITE.length( ) ) ) );
            }

            if ( k.startsWith( PARAMETER_ARRONDISEMENT ) )
            {
                selectedArrondissement.add( Integer.valueOf( k.substring( PARAMETER_ARRONDISEMENT.length( ) ) ) );
            }

        } );

        JSONObject jsonObject = new JSONObject( );
        jsonObject.accumulate( PARAMETER_TYPOLOGIE, selectedTypologie );
        jsonObject.accumulate( PARAMETER_ETAT, selectedState );
        jsonObject.accumulate( PARAMETER_PRIORITE, selectedPriorite );
        jsonObject.accumulate( PARAMETER_ARRONDISEMENT, selectedArrondissement );
        jsonObject.accumulate( PARAMETER_QUARTIER, selectedQuartier );
        jsonObject.accumulate( PARAMETER_DATE_BEGIN, request.getParameter( PARAMETER_DATE_BEGIN ) );
        jsonObject.accumulate( PARAMETER_DATE_END, request.getParameter( PARAMETER_DATE_END ) );
        jsonObject.accumulate( PARAMETER_DATE_REQUALIFICATION_BEGIN, request.getParameter( PARAMETER_DATE_REQUALIFICATION_BEGIN ) );
        jsonObject.accumulate( PARAMETER_DATE_REQUALIFICATION_END, request.getParameter( PARAMETER_DATE_REQUALIFICATION_END ) );
        jsonObject.accumulate( PARAMETER_DATE_PROGRAMMATION_BEGIN, request.getParameter( PARAMETER_DATE_PROGRAMMATION_BEGIN ) );
        jsonObject.accumulate( PARAMETER_DATE_PROGRAMMATION_END, request.getParameter( PARAMETER_DATE_PROGRAMMATION_END ) );
        jsonObject.accumulate( PARAMETER_COMMENTAIRE_AGENT_TERRAIN, request.getParameter( PARAMETER_COMMENTAIRE_AGENT_TERRAIN ) );
        jsonObject.accumulate( PARAMETER_COMMENTAIRE_USAGER, request.getParameter( PARAMETER_COMMENTAIRE_USAGER ) );
        jsonObject.accumulate( PARAMETER_ADRESSE, request.getParameter( PARAMETER_ADRESSE ) );
        jsonObject.accumulate( PARAMETER_ENTITE, request.getParameter( PARAMETER_ENTITE ) );
        jsonObject.accumulate( PARAMETER_MAIL_USAGER, request.getParameter( PARAMETER_MAIL_USAGER ) );

        return jsonObject.toString( );
    }

    public void doExportPDF( HttpServletRequest request, HttpServletResponse response )
    {
        String destinairesParam = request.getParameter( PARAMETER_DESTINAIRES_EXPORT );
        Integer feuilleeDeTourneId = request.getParameter( PARAMETER_FEUILLE_DE_TOURNEE_ID ) != null
                ? Integer.parseInt( request.getParameter( PARAMETER_FEUILLE_DE_TOURNEE_ID ) )
                        : 0;
        String base64Map = request.getParameter( PARAMETER_CARTE_BASE64 );

        String nomExport = _feuilleTourneeService.load( feuilleeDeTourneId ).getNom( ) + ".pdf";
        String typeExport = request.getParameter( PARAMETER_TYPE_EXPORT );

        byte [ ] bytesData = generateAndSendPdf( feuilleeDeTourneId, base64Map, destinairesParam, typeExport, request );

        response.setContentType( TYPE_PDF );

        response.setHeader( "Content-Disposition", "attachment;filename=" + nomExport );
        OutputStream out = null;
        try
        {
            out = response.getOutputStream( );

            out.write( bytesData );

        }
        catch( IOException e )
        {
            AppLogService.error( "[EditionUtils.getEdition]Erreur lors de la génération du pdf", e );
        }
        finally
        {
            if ( out != null )
            {
                try
                {
                    out.flush( );
                    out.close( );
                }
                catch( IOException e )
                {
                    AppLogService.error( "[EditionUtils.getEdition]Erreur lors de la génération du pdf", e );
                }
            }
        }

    }

    public byte [ ] generateAndSendPdf( Integer feuilleeDeTourneId, String base64Map, String destinairesParam, String typeExport, HttpServletRequest request )
    {

        List<String> destinairesList = _feuilleTourneeService.getDestinatairesFromParam( destinairesParam );

        if ( !destinairesList.isEmpty( ) && !_feuilleTourneeService.isFormatEmailOK( destinairesList ) )
        {
            return new byte[0];
        }
        else
        {
            boolean isExportWithMap = true;
            String nomTemplate = "";

            switch( typeExport )
            {
                case SignalementConstants.TYPE_EXPORT_FDT_CARTE:
                    nomTemplate = NOM_TEMPLATE_CARTE;
                    break;
                case SignalementConstants.TYPE_EXPORT_FDT_LISTE:
                    nomTemplate = NOM_TEMPLATE;
                    isExportWithMap = false;
                    break;
                case SignalementConstants.TYPE_EXPORT_FDT_FULL:
                    nomTemplate = NOM_TEMPLATE;
                    break;
                default:
                    break;
            }

            String pathToTemplate = request.getServletContext( ).getRealPath( "editions" ) + "/" + nomTemplate + ".jrxml";

            JRDataSource dataSource = _feuilleTourneeService.generateTemplateDataSource( feuilleeDeTourneId, isExportWithMap, base64Map );
            byte [ ] bytesData;
            try
            {
                bytesData = _feuilleTourneeService.generateTemplateBytesData( dataSource, pathToTemplate );

                String nomExport = _feuilleTourneeService.load( feuilleeDeTourneId ).getNom( );

                _feuilleTourneeService.sendFeuilleDeTourneeByMail( destinairesList, bytesData, nomExport );

                return bytesData;
            } catch ( JRException e )
            {
                AppLogService.error( "Erreur lors de la génération du pdf", e );
            }
        }
        return new byte[0];
    }

    public void checkNameFDT( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        String nomFDT = request.getParameter( PARAMETER_NAME );

        ObjectMapper mapper = new ObjectMapper( );
        String feuilleDeTournee = mapper.writeValueAsString( _feuilleTourneeService.findFDTByName( nomFDT ) );
        response.setContentType( EXTENSION_APPLICATION_JSON );

        try
        {
            response.getWriter( ).print( feuilleDeTournee );
        }
        catch( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
    }

    public void checkNameFilterSearch( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {

        String filterSearchName = request.getParameter( PARAMETER_SEARCH_FILTER_NAME );

        ObjectMapper mapper = new ObjectMapper( );
        String filterSearch = mapper.writeValueAsString( _feuilleTourneeService.findFilterSearch( filterSearchName ) );
        response.setContentType( EXTENSION_APPLICATION_JSON );

        try
        {
            response.getWriter( ).print( filterSearch );
        }
        catch( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }

    }

    /**
     * Gets the entity list by id direction.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     */
    public void getEntityListByIdDirection( HttpServletRequest request, HttpServletResponse response )
    {
        String strDirectionId = request.getParameter( PARAMETER_DIRECTION_ID );

        JSONBuilder jsonStringer;
        response.setContentType( EXTENSION_APPLICATION_JSON );
        try
        {
            jsonStringer = new JSONBuilder( response.getWriter( ) );
            try
            {
                Integer directionId = Integer.parseInt( strDirectionId );

                ReferenceList refListEntity = ListUtils.toReferenceList( getEntityListByIdDirection( directionId ), ID_UNIT, LABEL, "" );

                jsonStringer.object( ).key( MARK_ENTITY_LIST ).array( );
                for ( ReferenceItem sector : refListEntity )
                {
                    jsonStringer.object( ).key( JSON_KEY_ID ).value( sector.getCode( ) ).key( "value" ).value( sector.getName( ) ).endObject( );
                }
                jsonStringer.endArray( ).endObject( );
            }
            catch ( NumberFormatException e )
            {
                jsonStringer.object( ).key( "errors" ).array( ).value( e.getMessage( ) ).endArray( ).endObject( );
            }
        }
        catch ( IOException e1 )
        {
            AppLogService.error( e1.getMessage( ), e1 );
        }
    }

    private List<Unit> getEntityListByIdDirection( Integer directionId )
    {
        List<Unit> entityList = new ArrayList<>( );

        // Récupération des entités de niveau 2
        List<Unit> entityLvl2List = _unitService.getSubUnits( directionId, false );

        // Récupération des entités de niveau 3
        for ( Unit entityLvl2 : entityLvl2List )
        {
            entityList.add( entityLvl2 );
            if ( _unitService.hasSubUnits( entityLvl2.getIdUnit( ) ) )
            {
                List<Unit> entityLvl3List = _unitService.getSubUnits( entityLvl2.getIdUnit( ), false );
                for ( Unit entityLvl3 : entityLvl3List )
                {
                    entityLvl3.setLabel( entityLvl2.getLabel( ) + " / " + entityLvl3.getLabel( ) );
                    entityList.add( entityLvl3 );
                }
            }
        }

        return entityList;
    }
}
