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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.business.entities.Source;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;

/**
 * The Class ManageTypeSignalementBySourceJspBean.
 */
@Controller( controllerJsp = "ManageTypeSignalementBySource.jsp", controllerPath = "jsp/admin/plugins/signalement/", right = "REFERENTIEL_MANAGEMENT_SIGNALEMENT" )
public class ManageTypeSignalementBySourceJspBean extends AbstractJspBean
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant RIGHT_MANAGE_TYPE_SIGNALEMENT. */
    // RIGHT
    public static final String RIGHT_MANAGE_TYPE_SIGNALEMENT = "REFERENTIEL_MANAGEMENT_SIGNALEMENT";

    /** The Constant MARK_LIST_TYPE_SIGNALEMENT. */
    private static final String MARK_LIST_TYPE_SIGNALEMENT = "list_type";

    /** The Constant MARK_LIST_TYPE_SIGNALEMENT_SOURCE. */
    private static final String MARK_LIST_TYPE_SIGNALEMENT_SOURCE = "list_type_source";

    /** The Constant MARK_LIST_SOURCE. */
    private static final String MARK_LIST_SOURCE = "list_source";

    /** The Constant MARK_SOURCE. */
    private static final String MARK_SOURCE = "source";

    /** The Constant TEMPLATE_MANAGE_TYPE_SIGNALEMENT_BY_SOURCE. */
    // TEMPLATES
    private static final String TEMPLATE_MANAGE_TYPE_SIGNALEMENT_BY_SOURCE = "admin/plugins/signalement/manage_typesignalement_by_source.html";

    /** The Constant TEMPLATE_MANAGE_SOURCE. */
    private static final String TEMPLATE_MANAGE_SOURCE = "admin/plugins/signalement/manage_source.html";

    /** The Constant TEMPLATE_ADD_SOURCE. */
    private static final String TEMPLATE_ADD_SOURCE = "admin/plugins/signalement/add_source.html";

    /** The Constant PARAMETER_MARK_TYPE_SIGNALEMENT_ID. */
    // PARAMETERS
    private static final String PARAMETER_MARK_TYPE_SIGNALEMENT_ID = "typesignalement_id";

    /** The Constant PARAMETER_MARK_SOURCE_ID. */
    private static final String PARAMETER_MARK_SOURCE_ID = "source_id";

    /** The type signalement service. */
    // SERVICES
    private transient ITypeSignalementService _typeSignalementService = SpringContextService.getBean( "typeSignalementService" );

    /** The Constant VIEW_MANAGE_MESSAGES. */
    // Views
    private static final String VIEW_MANAGE_MESSAGES = "manageMessages";

    /** The Constant VIEW_MANAGE_MESSAGES_BY_SOURCE. */
    private static final String VIEW_MANAGE_MESSAGES_BY_SOURCE = "manageMessagesBySource";

    /** The Constant VIEW_ADD_SOURCE. */
    private static final String VIEW_ADD_SOURCE = "addSource";

    /** The Constant ACTION_ADD_SOURCE. */
    // Actions
    private static final String ACTION_ADD_SOURCE = "doAddSource";

    /** The Constant ACTION_REMOVE_SOURCE. */
    private static final String ACTION_REMOVE_SOURCE = "doRemoveSource";

    /** The Constant ACTION_ADD_TYPE_TO_SOURCE. */
    private static final String ACTION_ADD_TYPE_TO_SOURCE = "doAddTypeToSource";

    /** The Constant ACTION_REMOVE_TYPE_FROM_SOURCE. */
    private static final String ACTION_REMOVE_TYPE_FROM_SOURCE = "doRemoveTypeFromSource";

    /** The Constant ACTION_MODIFY_SOURCE. */
    private static final String ACTION_MODIFY_SOURCE = "doModifySource";

    /** The n id source. */
    private Integer _nIdSource;

    /**
     * Gets the manage message type signalement.
     *
     * @param request
     *            the request
     * @return the manage message type signalement
     */
    @View( value = VIEW_MANAGE_MESSAGES, defaultView = true )
    public String getManageMessageTypeSignalement( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_LIST_SOURCE, _typeSignalementService.getListSource( ) );

        return getAdminPage( AppTemplateService.getTemplate( TEMPLATE_MANAGE_SOURCE, getLocale( ), model ).getHtml( ) );
    }

    /**
     * Get the manage reporting type page.
     *
     * @param request
     *            the request
     * @return the page with reporting type list
     */
    @View( value = VIEW_MANAGE_MESSAGES_BY_SOURCE )
    public String getManageMessageTypeSignalementBySource( HttpServletRequest request )
    {
        int nIdSource;

        if ( request.getParameter( PARAMETER_MARK_SOURCE_ID ) != null )
        {
            // Accès à la page via une redirection
            nIdSource = Integer.parseInt( request.getParameter( PARAMETER_MARK_SOURCE_ID ) );
        }
        else
        {
            // Accès à la page via une action (add/remove)
            nIdSource = _nIdSource;
        }

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_LIST_TYPE_SIGNALEMENT, _typeSignalementService.getAllTypeLastlevelNotInSource( nIdSource ) );
        model.put( MARK_LIST_TYPE_SIGNALEMENT_SOURCE, _typeSignalementService.getAllTypeSignalementBySource( nIdSource ) );
        model.put( MARK_SOURCE, _typeSignalementService.getSourceById( nIdSource ) );

        return getAdminPage( AppTemplateService.getTemplate( TEMPLATE_MANAGE_TYPE_SIGNALEMENT_BY_SOURCE, getLocale( ), model ).getHtml( ) );
    }

    /**
     * Do add type to source.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( ACTION_ADD_TYPE_TO_SOURCE )
    public String doAddTypeToSource( HttpServletRequest request )
    {
        int nIdTypeSignalement = 0;
        int nIdSource = 0;

        try
        {
            nIdTypeSignalement = Integer.parseInt( request.getParameter( PARAMETER_MARK_TYPE_SIGNALEMENT_ID ) );
            nIdSource = Integer.parseInt( request.getParameter( PARAMETER_MARK_SOURCE_ID ) );
            _typeSignalementService.insertTypeSignalementSource( nIdTypeSignalement, nIdSource );
            _nIdSource = nIdSource;
        }
        catch( Exception e )
        {
            return AdminMessageService.getMessageUrl( request, "dansmarue.manageTypeSignalement.source.erreur", AdminMessage.TYPE_STOP );
        }

        return redirectView( request, VIEW_MANAGE_MESSAGES_BY_SOURCE );
    }

    /**
     * Do remove type to source.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( ACTION_REMOVE_TYPE_FROM_SOURCE )
    public String doRemoveTypeFromSource( HttpServletRequest request )
    {
        int nIdTypeSignalement = 0;
        int nIdSource = 0;

        try
        {
            nIdTypeSignalement = Integer.parseInt( request.getParameter( PARAMETER_MARK_TYPE_SIGNALEMENT_ID ) );
            nIdSource = Integer.parseInt( request.getParameter( PARAMETER_MARK_SOURCE_ID ) );
            _typeSignalementService.removeTypeSignalementSource( nIdTypeSignalement, nIdSource );
            _nIdSource = nIdSource;
        }
        catch( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        return redirectView( request, VIEW_MANAGE_MESSAGES_BY_SOURCE );
    }

    /**
     * Gets the adds the cource.
     *
     * @param request
     *            the request
     * @return the adds the cource
     */
    @View( value = VIEW_ADD_SOURCE )
    public String getAddCource( HttpServletRequest request )
    {
        return getAdminPage( AppTemplateService.getTemplate( TEMPLATE_ADD_SOURCE, getLocale( ) ).getHtml( ) );
    }

    /**
     * Do add cource.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( ACTION_ADD_SOURCE )
    public String doAddCource( HttpServletRequest request )
    {
        Source source = new Source( );
        populate( source, request, request.getLocale( ) );
        _typeSignalementService.addSource( source );

        return redirectView( request, VIEW_MANAGE_MESSAGES );
    }

    /**
     * Do remove cource.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( ACTION_REMOVE_SOURCE )
    public String doRemoveSource( HttpServletRequest request )
    {
        int nIdSource = 0;

        try
        {
            nIdSource = Integer.parseInt( request.getParameter( PARAMETER_MARK_SOURCE_ID ) );
            _typeSignalementService.removeSource( nIdSource );
        }
        catch( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        return redirectView( request, VIEW_MANAGE_MESSAGES );
    }

    /**
     * Do modify source.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( ACTION_MODIFY_SOURCE )
    public String doModifySource( HttpServletRequest request )
    {
        Source source = new Source( );
        populate( source, request, request.getLocale( ) );
        _typeSignalementService.updateSource( source );

        _nIdSource = source.getId( );

        return redirectView( request, VIEW_MANAGE_MESSAGES_BY_SOURCE );
    }

}
