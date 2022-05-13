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

import static fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants.PERIODE_TDB_30_DERNIERS_JOURS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.TableauDeBordFilter;
import fr.paris.lutece.plugins.dansmarue.service.IArrondissementService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementUnitService;
import fr.paris.lutece.plugins.dansmarue.service.ISiraUserService;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.service.role.SignalementResourceIdService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.ListUtils;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.service.sector.ISectorService;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflowcore.service.state.IStateService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * The Class SignalementDashboardJspBean.
 */
public class SignalementDashboardJspBean extends AbstractJspBean
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5000201974017218954L;

    // RIGHTS
    /** The Constant RIGHT_MANAGE_SIGNALEMENT. */
    public static final String RIGHT_MANAGE_SIGNALEMENT = "SIGNALEMENT_DASHBOARD";

    // TEMPLATES
    /** The Constants TEMPLATE_MANAGE_SIGNALEMENT_DASHBOARD. */
    private static final String TEMPLATE_MANAGE_SIGNALEMENT_DASHBOARD = "admin/plugins/signalement/manage_signalement_dashboard.html";

    // PARAMETERS
    /** The Constant PARAMETER_SIGNALEMENT_ID. */
    public static final String PARAMETER_SIGNALEMENT_ID = "signalement_id";

    /** The Constant PARAMETER_SELECTED_STATE. */
    public static final String PARAMETER_SELECTED_STATE = "selectedState";

    /** The Constant PARAMETER_SELECTED_PERIOD. */
    public static final String PARAMETER_SELECTED_PERIOD = "selectedPeriod";

    /** The Constant PARAMETER_SELECTED_TRANCHE. */
    public static final String PARAMETER_SELECTED_TRANCHE = "selectedTranche";

    /** The Constant SESSION_ATTRIBUTE_SIGNALEMENT. */
    // SESSION
    private static final String SESSION_ATTRIBUTE_SIGNALEMENT = "signalement";

    /** The Constant MARK_ARRONDISSEMENT_LIST. */
    // MARKERS
    private static final String MARK_ARRONDISSEMENT_LIST = "arrondissement_list";

    /** The Constant MARK_STATES1_LIST. */
    private static final String MARK_STATES1_LIST = "states_list_1";

    /** The Constant MARK_STATES2_LIST. */
    private static final String MARK_STATES2_LIST = "states_list_2";

    /** The Constant MARK_CATEGORY_LIST. */
    private static final String MARK_CATEGORY_LIST = "category_list";

    /** The Constant MARK_ENTITE_PARENT_USER. */
    private static final String MARK_ENTITE_PARENT_USER = "entiteParentUser";

    /** The Constant MARK_SIGNALEMENTS. */
    private static final String MARK_SIGNALEMENTS = "signalements_list";

    /** The Constant MARK_STATES_LIST. */
    private static final String MARK_STATES_LIST = "states_list";

    /** The Constant MARK_DASHBOARD_USER_UNIT_TREE. */
    private static final String MARK_DASHBOARD_USER_UNIT_TREE = "user_unit_tree";

    /** The Constant MARK_DASHBOARD_MAX_DEPTH. */
    private static final String MARK_DASHBOARD_MAX_DEPTH = "maxDepth";

    /** The Constant PROPERTY_ID_STATE_DIRECTION_TABLEAU1. */
    // PROPERTIES
    private static final String PROPERTY_ID_STATE_DIRECTION_TABLEAU1 = "signalement.dashboard.state.direction.tableauUn";

    /** The Constant PROPERTY_ID_STATE_DIRECTION_TABLEAU2. */
    private static final String PROPERTY_ID_STATE_DIRECTION_TABLEAU2 = "signalement.dashboard.state.direction.tableauDeux";

    /** The Constant PROPERTY_ID_STATE_PRESTA_TABLEAU1. */
    private static final String PROPERTY_ID_STATE_PRESTA_TABLEAU1 = "signalement.dashboard.state.prestataire.tableauUn";

    /** The Constant PROPERTY_ID_STATE_ADMIN_TABLEAU1. */
    private static final String PROPERTY_ID_STATE_ADMIN_TABLEAU1 = "signalement.dashboard.state.admin.tableauUn";

    /** The Constant PROPERTY_ID_STATE_ADMIN_TABLEAU2. */
    private static final String PROPERTY_ID_STATE_ADMIN_TABLEAU2 = "signalement.dashboard.state.admin.tableauDeux";

    /** The signalement service. */
    // SERVICES
    private transient ISignalementService _signalementService = SpringContextService.getBean( "signalementService" );

    /** The type signalement service. */
    private transient ITypeSignalementService _typeSignalementService = SpringContextService.getBean( "typeSignalementService" );

    /** The unit service. */
    private transient IUnitService _unitService = SpringContextService.getBean( "unittree.unitService" );

    /** The arrondissement service. */
    private transient IArrondissementService _arrondissementService = SpringContextService.getBean( "signalement.arrondissementService" );

    /** The state service. */
    private transient IStateService _stateService = SpringContextService.getBean( "workflow.stateService" );

    /** The sector service. */
    private transient ISectorService _sectorService = SpringContextService.getBean( "unittree-dansmarue.sectorService" );

    /** The signalement unit service. */
    private transient ISignalementUnitService _signalementUnitService = SpringContextService.getBean( "signalementUnitService" );

    /** The sira user service. */
    private transient ISiraUserService _siraUserService = SpringContextService.getBean( "siraUserService" );

    /** The tableau de bord filter. */
    // MEMBERS
    private TableauDeBordFilter tableauDeBordFilter = null;

    /**
     * View for the report management dashboard.
     *
     * @param request
     *            the HttpServletRequest
     * @return the dashboard view
     */
    public String doGetManageSignalementDashboard( HttpServletRequest request )
    {
        AdminUser adminUser = AdminUserService.getAdminUser( request );

        Map<String, Object> model = new HashMap<>( );

        // Récupération de l'entité de l'user connecté
        List<Unit> userUnitsList = _unitService.getUnitsByIdUser( adminUser.getUserId( ), false );

        // Vérification du type d'utilisateur connecté
        boolean isUserAdminRacine = userUnitsList.get( 0 ).getIdUnit( ) == 0;
        boolean isUserPrestataire = false;

        if ( !isUserAdminRacine )
        {
            isUserPrestataire = _siraUserService.isUserPrestataire( userUnitsList.get( 0 ).getIdUnit( ) );
        }

        // Récupération de la liste des status en fonction du type d'utilisateur
        setStatesList( isUserAdminRacine, isUserPrestataire, model );

        /**** Critères de recherche: Période, entité, arrondissement, type ano ****/

        // Récupération des arrondissements via les secteur de l'entité de l'user
        model.put( MARK_ARRONDISSEMENT_LIST, getListArrondissement( userUnitsList.get( 0 ) ) );

        // Récupération des types de signalement de niveau 1
        setCategoryList( model, userUnitsList.get( 0 ).getIdUnit( ) );

        // Récupération des entités
        setEntite( model, userUnitsList.get( 0 ) );

        // Application des critères de recherche
        tableauDeBordFilter = new TableauDeBordFilter( );
        populate( tableauDeBordFilter, request );
        setTableauDeBordFilter( userUnitsList.get( 0 ).getIdUnit( ), request );
        model.put( MARK_FILTER, tableauDeBordFilter );

        // Référentiel des statuts pour l'affichage
        model.put( MARK_STATES_LIST, _stateService.findStatesBetweenOrders( 0, 99, 2 ) );

        // Recherche des signalements: Les signalements sont dans une map par statut, puis dans une sous map par tranche de date de création
        model.put( MARK_SIGNALEMENTS, _signalementService.getSignalementsTDB( tableauDeBordFilter ) );

        return getAdminPage( getTemplate( TEMPLATE_MANAGE_SIGNALEMENT_DASHBOARD, model ) );
    }

    /**
     * Gets the list arrondissement.
     *
     * @param userUnit
     *            the user unit
     * @return the list arrondissement
     */
    private List<Arrondissement> getListArrondissement( Unit userUnit )
    {
        List<Integer> listIdAllowedSectors = _sectorService.getIdsSectorByIdUnit( userUnit.getIdUnit( ) );
        List<Arrondissement> listArrondissement = new ArrayList<>( );

        for ( Integer nArrId : _arrondissementService.getArrondissementsInSector( listIdAllowedSectors ) )
        {
            listArrondissement.add( _arrondissementService.getByIdArrondissement( nArrId ) );
        }
        return listArrondissement;
    }

    /**
     * Do search signalement dashboard.
     *
     * @param request
     *            the request
     * @return the string
     */
    public String doSearchSignalementDashboard( HttpServletRequest request )
    {
        populate( tableauDeBordFilter, request );
        return doGetManageSignalementDashboard( request );
    }

    /**
     * Sets the tableau de bord filter.
     *
     * @param unitId
     *            the new tableau de bord filter
     * @param request
     *            the request
     */
    private void setTableauDeBordFilter( Integer unitId, HttpServletRequest request )
    {
        // Valeurs par défaut :
        // -« Mois courant » pour le critère Période
        // -l’entité d’affectation de l’agent connecté pour le critère « Entité »
        if ( tableauDeBordFilter.getPeriodId( ) == null )
        {
            tableauDeBordFilter.setPeriodId( PERIODE_TDB_30_DERNIERS_JOURS );
        }

        if ( tableauDeBordFilter.getUnitId( ) == null )
        {
            tableauDeBordFilter.setUnitId( unitId );
        }

        if ( request.getParameter( "depth" ) == null )
        {
            tableauDeBordFilter.setDepth( 0 );
        }

        if ( request.getParameter( "depthCategory" ) == null )
        {
            tableauDeBordFilter.setDepthCategory( 0 );
        }

        if ( request.getParameter( "categoryId" ) == null )
        {
            tableauDeBordFilter.setCategoryIds( null );
        }
        else
        {
            // Niveau de la catégory sélectionnée
            String depthCategory = request.getParameter( "depthCategory" );

            // Catégorie sélectionnée
            String categorieId = request.getParameter( "categoryId" );

            if ( !StringUtils.isEmpty( categorieId ) && !StringUtils.isEmpty( depthCategory ) )
            {
                setTableauDeBordCategoryFilter( depthCategory, categorieId );
            }
        }

        if ( request.getParameter( "arrondissementIds" ) == null )
        {
            tableauDeBordFilter.setArrondissementIds( null );
        }
    }

    /**
     * Set TableauDeBordCategoryFilter.
     * 
     * @param depthCategory
     *            category level
     * @param categorieId
     *            id category
     */
    private void setTableauDeBordCategoryFilter( String depthCategory, String categorieId )
    {

        List<Integer> categoryIds = new ArrayList<>( );
        int ncategorieId = Integer.parseInt( categorieId );
        categoryIds.add( ncategorieId );

        // Récupération des catégories enfants en fonction du nivau de la catégorie sélectionnée
        switch( depthCategory )
        {
            case "0":
                // Niveau 0 -> 2 niveaux d'enfant

                // Récupération de la liste des enfants de niveau 1
                List<Integer> categoryIdsChildLvl1 = _typeSignalementService.getListIdsChildren( categoryIds );
                categoryIds.addAll( categoryIdsChildLvl1 );

                // Récupération de la liste des enfants de niveau 2
                if ( !categoryIdsChildLvl1.isEmpty( ) )
                {
                    List<Integer> categoryIdsChildLvl2 = _typeSignalementService.getListIdsChildren( categoryIdsChildLvl1 );
                    categoryIds.addAll( categoryIdsChildLvl2 );
                }

                break;
            case "1":
                // Niveau 1 -> 1 seul niveau d'enfant
                categoryIds.addAll( _typeSignalementService.getListIdsChildren( categoryIds ) );
                break;
            default:
                break;
        }

        tableauDeBordFilter.setCategoryIds( categoryIds.stream( ).map( Integer::new ).toArray( Integer [ ]::new ) );
        tableauDeBordFilter.setCategoryId( ncategorieId );
        tableauDeBordFilter.setCategoryParentId( _typeSignalementService.findByIdTypeSignalement( ncategorieId ).getIdTypeSignalementParent( ) );
        tableauDeBordFilter.setDepthCategory( Integer.parseInt( depthCategory ) );
    }

    /**
     * Sets the states list in the model.
     *
     * @param isUserAdminRacine
     *            the is user admin racine
     * @param isUserPrestataire
     *            the is user prestataire
     * @param model
     *            the model
     */
    private void setStatesList( boolean isUserAdminRacine, boolean isUserPrestataire, Map<String, Object> model )
    {
        // Récupération des statuts par utilisateur
        List<Integer> listIdStateTableau1;
        List<Integer> listIdStateTableau2 = new ArrayList<>( );

        if ( isUserAdminRacine )
        {
            listIdStateTableau1 = getListByProperty( PROPERTY_ID_STATE_ADMIN_TABLEAU1 );
            listIdStateTableau2 = getListByProperty( PROPERTY_ID_STATE_ADMIN_TABLEAU2 );
        }
        else
            if ( isUserPrestataire )
            {
                listIdStateTableau1 = getListByProperty( PROPERTY_ID_STATE_PRESTA_TABLEAU1 );
            }
            else
            {
                listIdStateTableau1 = getListByProperty( PROPERTY_ID_STATE_DIRECTION_TABLEAU1 );
                listIdStateTableau2 = getListByProperty( PROPERTY_ID_STATE_DIRECTION_TABLEAU2 );
            }

        model.put( MARK_STATES1_LIST, listIdStateTableau1 );
        model.put( MARK_STATES2_LIST, listIdStateTableau2 );
    }

    /**
     * Sets the category list in the model.
     *
     * @param model
     *            the model
     * @param unitId
     *            the unit id
     */
    private void setCategoryList( Map<String, Object> model, Integer unitId )
    {
        // Récupération de l'entinté parent max de l'utilisateur
        model.put( MARK_ENTITE_PARENT_USER, _siraUserService.getEntiteMaxFromEntite( unitId ) );
        // Récupération des catégories avec leur niveau
        model.put( MARK_CATEGORY_LIST, _typeSignalementService.getAllTypeSignalementActifWithDepth( unitId ) );
    }

    /**
     * Sets the entite in the model.
     *
     * @param model
     *            the model
     * @param unit
     *            the unit
     */
    private void setEntite( Map<String, Object> model, Unit unit )
    {
        model.put( MARK_DASHBOARD_USER_UNIT_TREE, _signalementUnitService.getVisibleUnitsWithDepth( unit.getIdUnit( ) ) );
        model.put( MARK_DASHBOARD_MAX_DEPTH, _signalementUnitService.getMaxDepthVisibleUnit( unit.getIdUnit( ) ) );
    }

    /**
     * Gets the list of Ids by property.
     *
     * @param property
     *            the property
     * @return the list by property
     */
    private List<Integer> getListByProperty( String property )
    {
        List<Integer> listInteger = new ArrayList<>( );

        String strIds = AppPropertiesService.getProperty( property );
        if ( StringUtils.isNotBlank( strIds ) )
        {
            String [ ] idArr = strIds.split( "," );
            listInteger = ListUtils.getListOfIntFromStrArray( idArr );
        }

        return listInteger;
    }

    /**
     * Redirects to manage reports page, with reports list.
     *
     * @param request
     *            the HttpServletRequest
     * @throws AccessDeniedException
     *             throws AccessDeniedException
     */
    public void redirectToManageSignalement( HttpServletRequest request ) throws AccessDeniedException
    {
        // Récupération des parametres
        Integer selectedState = Integer.parseInt( request.getParameter( PARAMETER_SELECTED_STATE ) );
        Integer selectedPeriod = tableauDeBordFilter.getPeriodId( );
        tableauDeBordFilter.getPeriodId( );

        // Initialisation des criterias pour la SignalementJspBean
        Map<String, List<String>> criterias = new LinkedHashMap<>( );

        // State
        List<String> state = new ArrayList<>( );
        state.add( _stateService.findByPrimaryKey( selectedState ).getName( ) );
        criterias.put( "#i18n{dansmarue.dashboard.criterias.status}", state );
        tableauDeBordFilter.setState( selectedState );

        // Period
        List<String> period = new ArrayList<>( );

        switch( selectedPeriod )
        {
            case 2:
                period.add( I18nService.getLocalizedString( "dansmarue.dashboard.criterias.period.30jours", request.getLocale( ) ) );
                break;
            case 1:
                period.add( I18nService.getLocalizedString( "dansmarue.dashboard.criterias.period.90jours", request.getLocale( ) ) );
                break;
            case 0:
                period.add( I18nService.getLocalizedString( "dansmarue.dashboard.criterias.period.180jours", request.getLocale( ) ) );
                break;
            default:
                break;
        }

        criterias.put( "#i18n{dansmarue.dashboard.criterias.period}", period );

        // Entite
        if ( tableauDeBordFilter.getUnitId( ) != null )
        {
            List<String> entite = new ArrayList<>( );
            entite.add( _unitService.getUnit( tableauDeBordFilter.getUnitId( ), false ).getLabel( ) );
            criterias.put( "#i18n{dansmarue.dashboard.criterias.unit}", entite );
        }

        // Catégorie
        if ( tableauDeBordFilter.getCategoryId( ) != null )
        {
            List<String> categories = new ArrayList<>( );
            categories.add( _typeSignalementService.findByIdTypeSignalement( tableauDeBordFilter.getCategoryId( ) ).getLibelle( ) );

            criterias.put( "#i18n{dansmarue.dashboard.criterias.categories}", categories );
        }

        // Arrondissements
        if ( !ArrayUtils.isEmpty( tableauDeBordFilter.getArrondissementIds( ) ) )
        {
            List<Arrondissement> arrondissements = _arrondissementService.getAllArrondissement( );
            List<String> arrondissementCriteria = new ArrayList<>( );
            Integer [ ] arrondissementIds = tableauDeBordFilter.getArrondissementIds( );
            for ( Integer arrondissementId : arrondissementIds )
            {
                for ( Arrondissement arrondissement : arrondissements )
                {
                    if ( arrondissementId == arrondissement.getId( ).intValue( ) )
                    {
                        arrondissementCriteria.add( arrondissement.getNumero( ) );
                    }
                }
            }
            criterias.put( "#i18n{dansmarue.dashboard.criterias.arrondissements}", arrondissementCriteria );
        }

        // Tranche - Si ce n'est pas un clique sur l'entete du tableau
        valueTrancheCriteria( request, criterias, selectedState );

        // Initialisation de la SignalementJspBean
        SignalementJspBean signalement = (SignalementJspBean) request.getSession( ).getAttribute( SESSION_ATTRIBUTE_SIGNALEMENT );
        if ( null == signalement )
        {
            signalement = new SignalementJspBean( );
            signalement.init( request, "SIGNALEMENT_MANAGEMENT", SignalementResourceIdService.KEY_ID_RESOURCE,
                    SignalementResourceIdService.PERMISSION_RECHERCHER_SIGNALEMENT );
            request.getSession( ).setAttribute( SESSION_ATTRIBUTE_SIGNALEMENT, signalement );
        }

        // Set des critères
        signalement.setDashboardCriterias( criterias );

        // Set des signalementsIds
        signalement.setDashboardSignalementList( _signalementService.getIdSignalementsTDB( tableauDeBordFilter ) );
    }

    /**
     * Value tranche crieria with selected tranche.
     * 
     * @param request
     *            http request
     * @param criterias
     *            criteria map
     * @param selectedState
     *            selected state
     */
    private void valueTrancheCriteria( HttpServletRequest request, Map<String, List<String>> criterias, Integer selectedState )
    {
        if ( request.getParameter( PARAMETER_SELECTED_TRANCHE ) != null )
        {
            List<String> tranche = new ArrayList<>( );

            switch( request.getParameter( PARAMETER_SELECTED_TRANCHE ) )
            {
                case "0":
                    tranche.add( valueLabelTrancheCriteria( request, "0", selectedState ) );
                    break;
                case "1":
                    tranche.add( valueLabelTrancheCriteria( request, "1", selectedState ) );
                    break;
                case "2":
                    tranche.add( valueLabelTrancheCriteria( request, "2", selectedState ) );
                    break;
                default:
                    break;
            }
            criterias.put( "#i18n{dansmarue.dashboard.criterias.repartition}", tranche );
            tableauDeBordFilter.setTrancheId( Integer.parseInt( request.getParameter( PARAMETER_SELECTED_TRANCHE ) ) );
        }
        else
        {
            tableauDeBordFilter.setTrancheId( null );
        }
    }

    /**
     * Value label for tranche criteria/.
     *
     * @param request
     *            http request
     * @param selectedTranche
     *            selected tranche
     * @param selectedState
     *            selected state
     * @return the string
     */
    private String valueLabelTrancheCriteria( HttpServletRequest request, String selectedTranche, Integer selectedState )
    {
        if ( ( SignalementConstants.ID_STATE_PROGRAMME.intValue( ) == selectedState )
                || ( SignalementConstants.ID_STATE_PROGRAMME_PRESTATAIRE.intValue( ) == selectedState ) )
        {
            return I18nService.getLocalizedString( "dansmarue.dashboard.service.programme.tranche" + selectedTranche, request.getLocale( ) );
        }
        else
        {
            return I18nService.getLocalizedString( "dansmarue.dashboard.tranche" + selectedTranche, request.getLocale( ) );
        }
    }
}
