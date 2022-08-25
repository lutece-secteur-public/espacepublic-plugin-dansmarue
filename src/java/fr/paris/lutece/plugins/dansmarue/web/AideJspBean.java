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

package fr.paris.lutece.plugins.dansmarue.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.Aide;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.FunctionnalException;
import fr.paris.lutece.plugins.dansmarue.service.IAideService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.beanvalidation.ValidationError;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the user interface to manage Aide features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageAides.jsp", controllerPath = "jsp/admin/plugins/signalement/", right = "REFERENTIEL_MANAGEMENT_SIGNALEMENT" )
public class AideJspBean extends AbstractJspBean
{
    private static final long serialVersionUID = 7574940562233843780L;

    // Templates
    private static final String TEMPLATE_MANAGE_AIDES = "/admin/plugins/signalement/manage_aides.html";
    private static final String TEMPLATE_CREATE_AIDE = "/admin/plugins/signalement/create_aide.html";
    private static final String TEMPLATE_MODIFY_AIDE = "/admin/plugins/signalement/modify_aide.html";

    // Parameters
    private static final String PARAMETER_ID_AIDE = "aide_id";
    /** The Constant PARAMETER_PHOTO. */
    public static final String PARAMETER_PHOTO = "photo";


    // Views
    private static final String VIEW_MANAGE_AIDES = "manageAides";
    private static final String VIEW_CREATE_AIDE = "createAide";
    private static final String VIEW_MODIFY_AIDE = "modifyAide";

    // Actions
    private static final String ACTION_CREATE_AIDE = "createAide";
    private static final String ACTION_MODIFY_AIDE = "modifyAide";
    private static final String ACTION_REMOVE_AIDE = "removeAide";
    private static final String ACTION_CONFIRM_REMOVE_AIDE = "confirmRemoveAide";
    private static final String ACTION_INCREASE_ORDER_AIDE = "increaseOrdreAide";
    private static final String ACTION_DECREASE_ORDER_AIDE = "decreaseOrdreAide";

    // Markers
    private static final String MARK_AIDE_LIST = "aide_list";
    private static final String MARK_AIDE = "aide";
    /** The Constant MARK_WEBAPP_URL. */
    private static final String MARK_WEBAPP_URL = "webapp_url";

    private static final String JSP_MANAGE_AIDES = "jsp/admin/plugins/signalement/ManageAides.jsp";

    /** The Constant JSP_DELETE_ACTUALITE. */
    private static final String JSP_DELETE_AIDE = "jsp/admin/plugins/signalement/DoDeleteAide.jsp";

    /** The Constant JSP_SAVE_ACTUALITE. */
    private static final String JSP_SAVE_AIDE = "SaveAide.jsp";

    /** The Constant JSP_MODIFY_ACTUALITE. */
    private static final String JSP_MODIFY_AIDE = "ModifyAide.jsp";

    /** The Constant ERROR_OCCUR. */
    // CONSTANTS
    public static final String ERROR_OCCUR = "error";

    // Properties
    private static final String MESSAGE_TITLE_DELETE_AIDE = "dansmarue.messagetitle.aide.delete.confirmation";

    private static final String MESSAGE_CONFIRM_REMOVE_AIDE = "dansmarue.message.confirmRemoveAide";

    private transient IAideService _aideService = SpringContextService.getBean( "aideService" );

    /**
     * Get the manage Aide page.
     *
     * @param request
     *            the request
     * @return the page with Aide list
     */
    @View( value = VIEW_MANAGE_AIDES, defaultView = true )
    public String getManageAides( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );

        List<Aide> listAides = _aideService.getAllAide( );

        model.put( MARK_AIDE_LIST, listAides );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MANAGE_AIDES, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );

    }

    /**
     * Returns the form to create a aide
     *
     * @param request
     *            The Http request
     * @return the html code of the aide form
     */
    @View( value = VIEW_CREATE_AIDE )
    public String getCreateAide( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );

        Aide aide = new Aide( );
        // value of "actif" by default
        aide.setActif( true );

        // Manage validation errors
        FunctionnalException ve = getErrorOnce( request );

        if ( ve != null )
        {
            aide = (Aide) ve.getBean( );
            model.put( ERROR_OCCUR, getHtmlError( ve ) );
        }

        model.put( MARK_AIDE, aide );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_CREATE_AIDE, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Process the data capture form of a new aide
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */

    @Action( ACTION_CREATE_AIDE )
    public String doCreateAide( HttpServletRequest request )
    {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Aide aide = new Aide( );
        if ( StringUtils.isNotBlank( request.getParameter( "cancel" ) ) )
        {
            return redirectView( request, VIEW_MANAGE_AIDES );
        }

        populate( aide, request );

        FileItem imageSource = multipartRequest.getFile( PARAMETER_PHOTO );

        byte [ ] baImageSource = imageSource.get( );
        ImageResource image = new ImageResource( );

        image.setImage( baImageSource );
        image.setMimeType( imageSource.getContentType( ) );
        aide.setImage( image );

        // Controls mandatory fields
        List<ValidationError> errors = validate( aide, "" );
        if ( errors.isEmpty( ) )
        {
            try
            {
                _aideService.doSaveAide( aide );
            }
            catch( BusinessException e )
            {
                UrlItem url = new UrlItem( JSP_SAVE_AIDE );
                return manageFunctionnalException( request, e, url.getUrl( ) );
            }
        }
        else
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, errors ) );
        }

        return redirectView( request, VIEW_MANAGE_AIDES );
    }

    /**
     * Manages the removal form of a aide whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_AIDE )
    public String getRemoveAide( HttpServletRequest request )
    {
        String strJspBack = JSP_MANAGE_AIDES;

        String strIdAide = request.getParameter( PARAMETER_ID_AIDE );

        int nIdAide = 0;

        try
        {
            nIdAide = Integer.parseInt( strIdAide );
        }
        catch( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        Map<String, Object> urlParam = new HashMap<>( );
        urlParam.put( PARAMETER_ID_AIDE, nIdAide );

        return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_DELETE_AIDE, null, MESSAGE_CONFIRM_REMOVE_AIDE, JSP_DELETE_AIDE,
                "_self", AdminMessage.TYPE_CONFIRMATION, urlParam, strJspBack ) );
    }

    /**
     * Handles the removal form of a aide
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage aides
     */
    @Action( ACTION_REMOVE_AIDE )
    public String doRemoveAide( HttpServletRequest request )
    {
        String strIdAide = request.getParameter( PARAMETER_ID_AIDE );

        int nIdAide = 0;

        try
        {
            nIdAide = Integer.parseInt( strIdAide );
        }
        catch( NumberFormatException e )
        {

            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        _aideService.doDeleteAide( nIdAide );

        return redirectView( request, VIEW_MANAGE_AIDES );
    }

    /**
     * Returns the form to update info about a aide
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */

    @View( value = VIEW_MODIFY_AIDE )
    public String getModifyAide( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );

        Aide aide;

        String strIdAide = request.getParameter( PARAMETER_ID_AIDE );

        int nIdAide = 0;

        try
        {
            nIdAide = Integer.parseInt( strIdAide );
        }
        catch( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        aide = _aideService.getById( nIdAide );

        // Manage validation errors
        FunctionnalException ve = getErrorOnce( request );
        if ( ve != null )
        {
            aide = (Aide) ve.getBean( );
            model.put( ERROR_OCCUR, getHtmlError( ve ) );
        }

        model.put( MARK_AIDE, aide );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MODIFY_AIDE, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Process the change form of a aide
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_AIDE )
    public String doModifyAide( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( "cancel" ) ) )
        {
            return redirectView( request, VIEW_MANAGE_AIDES );
        }

        Aide aide = new Aide( );
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        populate( aide, request );

        FileItem imageSource = multipartRequest.getFile( PARAMETER_PHOTO );

        byte [ ] baImageSource = imageSource.get( );
        ImageResource image = new ImageResource( );

        image.setImage( baImageSource );
        aide.setImage( image );

        // Controls mandatory fields
        List<ValidationError> errors = validate( aide, "" );
        if ( errors.isEmpty( ) )
        {
            try
            {
                _aideService.doSaveAide( aide );
            }
            catch( BusinessException e )
            {
                UrlItem url = new UrlItem( JSP_MODIFY_AIDE );
                url.addParameter( PARAMETER_ID_AIDE, aide.getId( ) );
                return manageFunctionnalException( request, e, url.getUrl( ) );
            }
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, errors );
        }

        return redirectView( request, VIEW_MANAGE_AIDES );
    }

    /**
     * Increase the aide order.
     *
     * @param request
     *            the HttpServletRequest
     * @return The url of the last JSP
     */
    @Action( ACTION_INCREASE_ORDER_AIDE )
    public String doIncreaseAideOrder( HttpServletRequest request )
    {
        Aide aide = new Aide( );
        populate( aide, request );
        _aideService.increaseOrdreOfAide( aide );
        return redirectView( request, JSP_MANAGE_AIDES );
    }

    /**
     * Decrease the aide order.
     *
     * @param request
     *            the HttpServletRequest
     * @return The url of the last JSP
     */
    @Action( ACTION_DECREASE_ORDER_AIDE )
    public String doDecreaseAideOrder( HttpServletRequest request )
    {
        Aide aide = new Aide( );
        populate( aide, request );
        _aideService.decreaseOrdreOfAide( aide );
        return redirectView( request, JSP_MANAGE_AIDES );

    }
}
