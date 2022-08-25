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
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.Actualite;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.FunctionnalException;
import fr.paris.lutece.plugins.dansmarue.service.IActualiteService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.beanvalidation.ValidationError;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the user interface to manage form features ( manage, create, modify, remove).
 */
public class ActualiteJspBean extends AbstractJspBean
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1159722856896152359L;

    /** The Constant MESSAGE_CONFIRMATION_DELETE_ACTUALITE. */
    // MESSAGES
    private static final String MESSAGE_CONFIRMATION_DELETE_ACTUALITE = "dansmarue.message.actualite.delete.confirmation";

    /** The Constant MESSAGE_TITLE_DELETE_ACTUALITE. */
    private static final String MESSAGE_TITLE_DELETE_ACTUALITE = "dansmarue.messagetitle.actualite.delete.confirmation";

    /** The Constant PARAMETER_ACTUALITE_ID. */
    // PARAMETERS
    public static final String PARAMETER_ACTUALITE_ID = "actualite_id";

    /** The Constant JSP_MANAGE_ACTUALITE. */
    // JSP
    private static final String JSP_MANAGE_ACTUALITE = "jsp/admin/plugins/signalement/ManageActualite.jsp";

    /** The Constant JSP_DELETE_ACTUALITE. */
    private static final String JSP_DELETE_ACTUALITE = "jsp/admin/plugins/signalement/DoDeleteActualite.jsp";

    /** The Constant JSP_SAVE_ACTUALITE. */
    private static final String JSP_SAVE_ACTUALITE = "SaveActualite.jsp";

    /** The Constant JSP_MODIFY_ACTUALITE. */
    private static final String JSP_MODIFY_ACTUALITE = "ModifyActualite.jsp";

    /** The Constant RIGHT_MANAGE_ACTUALITE. */
    // RIGHT
    public static final String RIGHT_MANAGE_ACTUALITE = "REFERENTIEL_MANAGEMENT_SIGNALEMENT";

    /** The Constant ERROR_OCCUR. */
    // CONSTANTS
    public static final String ERROR_OCCUR = "error";

    /** The Constant MARK_ACTUALITE_LIST. */
    // Markers
    private static final String MARK_ACTUALITE_LIST = "actualite_list";

    /** The Constant MARK_ACTUALITE. */
    private static final String MARK_ACTUALITE = "actualite";

    private static final String MARK_WEBAPP_URL = "webapp_url";

    /** The Constant TEMPLATE_MANAGE_ACTUALITE. */
    // TEMPLATES
    private static final String TEMPLATE_MANAGE_ACTUALITE = "admin/plugins/signalement/manage_actualite.html";

    /** The Constant TEMPLATE_SAVE_ACTUALITE. */
    private static final String TEMPLATE_SAVE_ACTUALITE = "admin/plugins/signalement/save_actualite.html";

    /** The Constant TEMPLATE_MODIFY_ACTUALITE. */
    private static final String TEMPLATE_MODIFY_ACTUALITE = "admin/plugins/signalement/modify_actualite.html";

    /** The Constant PARAMETER_PHOTO. */
    public static final String PARAMETER_PHOTO = "photo";

    /** The actualite service. */
    // SERVICES
    private transient IActualiteService _actualiteService;

    /**
     * Inits the.
     *
     * @param request
     *            the request
     * @param strRight
     *            the str right
     * @throws AccessDeniedException
     *             the access denied exception
     */
    /*
     * (non-Javadoc)
     *
     * @see fr.paris.lutece.plugins.dansmarue.web.AbstractJspBean#init(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    @Override
    public void init( HttpServletRequest request, String strRight ) throws AccessDeniedException
    {
        super.init( request, strRight );
        _actualiteService = SpringContextService.getBean( "actualiteService" );
    }

    /**
     * Get the manage Actualite page.
     *
     * @param request
     *            the request
     * @return the page with Actualite list
     */
    public String getManageActualite( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );

        List<Actualite> listeNatureObjet = _actualiteService.getAllActualite( );

        model.put( MARK_ACTUALITE_LIST, listeNatureObjet );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MANAGE_ACTUALITE, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Delete a Actualite.
     *
     * @param request
     *            The HTTP request
     * @return redirection url
     */
    public String doDeleteActualite( HttpServletRequest request )
    {

        String strActualiteId = request.getParameter( PARAMETER_ACTUALITE_ID );

        int nIdActualite = 0;

        try
        {
            nIdActualite = Integer.parseInt( strActualiteId );
        }
        catch( NumberFormatException e )
        {

            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        _actualiteService.doDeleteActualite( nIdActualite );

        return doGoBack( request );

    }

    /**
     * Returns the confirmation message to delete a Actualite.
     *
     * @param request
     *            The Http request
     * @return the html code message
     */
    public String getDeleteActualite( HttpServletRequest request )
    {

        String strJspBack = JSP_MANAGE_ACTUALITE;

        String strIdActualite = request.getParameter( PARAMETER_ACTUALITE_ID );

        int nIdActualite = 0;

        try
        {
            nIdActualite = Integer.parseInt( strIdActualite );
        }
        catch( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        Map<String, Object> urlParam = new HashMap<>( );
        urlParam.put( PARAMETER_ACTUALITE_ID, nIdActualite );

        return AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_DELETE_ACTUALITE, null, MESSAGE_CONFIRMATION_DELETE_ACTUALITE, JSP_DELETE_ACTUALITE,
                "_self", AdminMessage.TYPE_CONFIRMATION, urlParam, strJspBack );

    }

    /**
     * Returns the form for Actualite creation.
     *
     * @param request
     *            The HTTP request
     * @return HTML Form
     */

    public String getSaveActualite( HttpServletRequest request )
    {

        Map<String, Object> model = new HashMap<>( );

        Actualite actualite = new Actualite( );
        // value of "actif" by default
        actualite.setActif( true );

        // Manage validation errors
        FunctionnalException ve = getErrorOnce( request );

        if ( ve != null )
        {
            actualite = (Actualite) ve.getBean( );
            model.put( ERROR_OCCUR, getHtmlError( ve ) );
        }

        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_ACTUALITE, actualite );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_SAVE_ACTUALITE, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Returns the form for Actualite modification.
     *
     * @param request
     *            The HTTP request
     * @return HTML Form
     */

    public String getModifyActualite( HttpServletRequest request )
    {

        Map<String, Object> model = new HashMap<>( );

        Actualite actualite;

        String strIdActualite = request.getParameter( PARAMETER_ACTUALITE_ID );

        int nIdActualite = 0;

        try
        {
            nIdActualite = Integer.parseInt( strIdActualite );
        }
        catch( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        actualite = _actualiteService.getById( nIdActualite );

        // Manage validation errors
        FunctionnalException ve = getErrorOnce( request );
        if ( ve != null )
        {
            actualite = (Actualite) ve.getBean( );
            model.put( ERROR_OCCUR, getHtmlError( ve ) );
        }

        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_ACTUALITE, actualite );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MODIFY_ACTUALITE, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Modify a Actualite.
     *
     * @param request
     *            The HTTP request
     * @return redirection url
     */

    public String doModifyActualite( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( "cancel" ) ) )
        {
            return doGoBack( request );
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Actualite actualite = new Actualite( );
        populate( actualite, request );

        FileItem imageSource = multipartRequest.getFile( PARAMETER_PHOTO );

        byte [ ] baImageSource = imageSource.get( );
        ImageResource image = new ImageResource( );

        image.setImage( baImageSource );
        actualite.setImage( image );

        // Controls mandatory fields
        List<ValidationError> errors = validate( actualite, "" );
        if ( errors.isEmpty( ) )
        {
            try
            {
                _actualiteService.doSaveActualite( actualite );
            }
            catch( BusinessException e )
            {
                UrlItem url = new UrlItem( JSP_MODIFY_ACTUALITE );
                url.addParameter( PARAMETER_ACTUALITE_ID, actualite.getId( ) );
                return manageFunctionnalException( request, e, url.getUrl( ) );
            }
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, errors );
        }

        return doGoBack( request );

    }

    /**
     * Save a Actualite.
     *
     * @param request
     *            The HTTP request
     * @return redirection url
     */
    public String doSaveActualite( HttpServletRequest request )
    {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Actualite actualite = new Actualite( );
        if ( StringUtils.isNotBlank( request.getParameter( "cancel" ) ) )
        {
            return doGoBack( request );
        }

        populate( actualite, request );

        FileItem imageSource = multipartRequest.getFile( PARAMETER_PHOTO );

        byte [ ] baImageSource = imageSource.get( );
        ImageResource image = new ImageResource( );

        image.setImage( baImageSource );
        image.setMimeType( imageSource.getContentType( ) );
        actualite.setImage( image );

        // Controls mandatory fields
        List<ValidationError> errors = validate( actualite, "" );
        if ( errors.isEmpty( ) )
        {
            try
            {
                _actualiteService.doSaveActualite( actualite );
            }
            catch( BusinessException e )
            {
                UrlItem url = new UrlItem( JSP_SAVE_ACTUALITE );
                return manageFunctionnalException( request, e, url.getUrl( ) );
            }
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, errors );
        }

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
                : ( AppPathService.getBaseUrl( request ) + JSP_MANAGE_ACTUALITE );

    }

    /**
     * Increase the actualite order.
     *
     * @param request
     *            the HttpServletRequest
     * @return The url of the last JSP
     */
    public String doIncreaseActualiteOrder( HttpServletRequest request )
    {
        Actualite actualite = new Actualite( );
        populate( actualite, request );
        _actualiteService.increaseOrdreOfActualite( actualite );
        return AppPathService.getBaseUrl( request ) + JSP_MANAGE_ACTUALITE;
    }

    /**
     * Decrease the actualite order.
     *
     * @param request
     *            the HttpServletRequest
     * @return The url of the last JSP
     */
    public String doDecreaseActualiteOrder( HttpServletRequest request )
    {
        Actualite actualite = new Actualite( );
        populate( actualite, request );
        _actualiteService.decreaseOrdreOfActualite( actualite );
        return AppPathService.getBaseUrl( request ) + JSP_MANAGE_ACTUALITE;

    }

}
