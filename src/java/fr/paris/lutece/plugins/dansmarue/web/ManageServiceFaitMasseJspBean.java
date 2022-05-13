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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.business.entities.ServiceFaitMasseFilter;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;

/**
 * The Class ManageTypeSignalementBySourceJspBean.
 */
@Controller( controllerJsp = "ManageServiceFaitMasse.jsp", controllerPath = "jsp/admin/plugins/signalement/", right = "REFERENTIEL_MANAGEMENT_SIGNALEMENT" )
public class ManageServiceFaitMasseJspBean extends AbstractJspBean
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3821167568683063124L;

    /** The Constant RIGHT_MANAGE_TYPE_SIGNALEMENT. */
    // RIGHT
    public static final String RIGHT_MANAGE_TYPE_SIGNALEMENT = "REFERENTIEL_MANAGEMENT_SIGNALEMENT";

    /** The Constant MARK_LIST_TYPE_SIGNALEMENT. */
    private static final String MARK_LIST_TYPE_SIGNALEMENT = "list_type";

    /** The Constant MARK_LIST_STATE. */
    private static final String MARK_LIST_STATE = "list_state";

    /** The Constant MARK_REPARTITION. */
    private static final String MARK_REPARTITION = "repartition";

    /** The Constant MARK_FILTER. */
    private static final String MARK_FILTER = "filter";

    /** The Constant MARK_IS_EXECUTION_OK. */
    private static final String MARK_IS_EXECUTION_OK = "isExecutionOk";

    /** The Constant TEMPLATE_MANAGE_SERVICE_FAIT_MASSE. */
    // TEMPLATES
    private static final String TEMPLATE_MANAGE_SERVICE_FAIT_MASSE = "admin/plugins/signalement/manage_service_fait_masse.html";

    /** The Constant TEMPLATE_RAPPORT_EXECUTION_SERVICE_FAIT_MASSE. */
    private static final String TEMPLATE_RAPPORT_EXECUTION_SERVICE_FAIT_MASSE = "admin/plugins/signalement/rapport_execution_service_fait_masse.html";

    /** The type signalement service. */
    // SERVICES
    private transient ITypeSignalementService _typeSignalementService = SpringContextService.getBean( "typeSignalementService" );

    /** The signalement service. */
    private transient ISignalementService _signalementService = SpringContextService.getBean( "signalementService" );

    /** The Constant VIEW_MANAGE_SERVICE_FAIT_MASSE. */
    // Views
    private static final String VIEW_MANAGE_SERVICE_FAIT_MASSE = "manageServiceFaitMasse";

    /** The Constant PROPERTY_ID_WORKFLOW_SIGNALEMENT. */
    // Properties
    private static final String PROPERTY_ID_WORKFLOW_SIGNALEMENT = "signalement.idWorkflow";

    /** The Constant ACTION_SEARCH. */
    // Actions
    private static final String ACTION_SEARCH = "search";

    /** The Constant ACTION_EXECUTE_SERVICE_FAIT. */
    private static final String ACTION_EXECUTE_SERVICE_FAIT = "executeServiceFait";

    /** The service fait masse filter. */
    private ServiceFaitMasseFilter _serviceFaitMasseFilter = new ServiceFaitMasseFilter( );

    /** The repartition. */
    private Map<String, Integer> _repartition = new HashMap<>( );

    /**
     * Get the manage reporting type page.
     *
     * @param request
     *            the request
     * @return the page with reporting type list
     */
    @View( value = VIEW_MANAGE_SERVICE_FAIT_MASSE, defaultView = true )
    public String getManageMessageTypeSignalement( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_LIST_STATE, getEtatEnCours( ) );
        model.put( MARK_LIST_TYPE_SIGNALEMENT, _typeSignalementService.getAllTypeSignalementActif( ) );
        model.put( SignalementConstants.MARK_LOCALE, request.getLocale( ) );
        model.put( MARK_FILTER, _serviceFaitMasseFilter );

        return getAdminPage( AppTemplateService.getTemplate( TEMPLATE_MANAGE_SERVICE_FAIT_MASSE, getLocale( ), model ).getHtml( ) );
    }

    /**
     * Do search.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( ACTION_SEARCH )
    public String doSearch( HttpServletRequest request )
    {
        // Remplissage du filtre
        _serviceFaitMasseFilter = new ServiceFaitMasseFilter( );
        populate( _serviceFaitMasseFilter, request, request.getLocale( ) );

        // Si pas d'état renseigné, on selectionne les états en cours
        if ( _serviceFaitMasseFilter.getIdEtats( ) == null )
        {
            _serviceFaitMasseFilter.setIdEtats( getEtatEnCours( ).stream( ).map( State::getId ).toArray( Integer [ ]::new ) );
        }

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_LIST_STATE, getEtatEnCours( ) );
        model.put( MARK_LIST_TYPE_SIGNALEMENT, _typeSignalementService.getListTypeSignalementLastLevelWithoutMessage( ) );
        model.put( SignalementConstants.MARK_LOCALE, request.getLocale( ) );
        model.put( MARK_FILTER, _serviceFaitMasseFilter );

        _repartition = _signalementService.getRepartitionServiceFaitMasse( _serviceFaitMasseFilter );

        model.put( MARK_REPARTITION, _repartition );

        return getAdminPage( AppTemplateService.getTemplate( TEMPLATE_MANAGE_SERVICE_FAIT_MASSE, getLocale( ), model ).getHtml( ) );
    }

    /**
     * Do execute service fait.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( ACTION_EXECUTE_SERVICE_FAIT )
    public String doExecuteServiceFait( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );

        // Récupération du paramètre à ajouter dans l'historique
        _serviceFaitMasseFilter.setCommentaire( request.getParameter( "commentaire" ) );

        model.put( MARK_FILTER, _serviceFaitMasseFilter );
        model.put( MARK_REPARTITION, _repartition );
        model.put( MARK_LIST_STATE, _serviceFaitMasseFilter.getIdEtats( ) != null ? getEtatFiltre( _serviceFaitMasseFilter ) : null );
        model.put( MARK_IS_EXECUTION_OK, _signalementService.executeServiceFaitMasse( _serviceFaitMasseFilter ) );

        return getAdminPage( AppTemplateService.getTemplate( TEMPLATE_RAPPORT_EXECUTION_SERVICE_FAIT_MASSE, getLocale( ), model ).getHtml( ) );
    }

    /**
     * Gets the etat en cours. Etats en cours: tous les états sauf : « Service fait », « Sous surveillance », « Rejeté » et « Archivé - 10, 11, 12, 22 »
     *
     * @return the etat en cours
     */
    private List<State> getEtatEnCours( )
    {
        WorkflowService workflowService = WorkflowService.getInstance( );
        Collection<State> states = workflowService.getAllStateByWorkflow( AppPropertiesService.getPropertyInt( PROPERTY_ID_WORKFLOW_SIGNALEMENT, -1 ),
                getUser( ) );

        return states.stream( ).filter( state -> ( state.getId( ) < 10 ) || ( ( state.getId( ) > 12 ) && ( state.getId( ) != 22 ) ) )
                .collect( Collectors.toList( ) );
    }

    /**
     * Gets the etat filtre.
     *
     * @param filtre
     *            the filtre
     * @return the etat filtre
     */
    private List<State> getEtatFiltre( ServiceFaitMasseFilter filtre )
    {
        // Récupération de l'objet State pour les Ids de type du filtre
        WorkflowService workflowService = WorkflowService.getInstance( );
        Collection<State> states = workflowService.getAllStateByWorkflow( AppPropertiesService.getPropertyInt( PROPERTY_ID_WORKFLOW_SIGNALEMENT, -1 ),
                getUser( ) );

        List<Integer> stateIdsFiltreList = Arrays.asList( filtre.getIdEtats( ) );

        return states.stream( ).filter( state -> stateIdsFiltreList.contains( state.getId( ) ) ).collect( Collectors.toList( ) );
    }

}
