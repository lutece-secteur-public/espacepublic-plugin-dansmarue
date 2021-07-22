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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import com.rometools.utils.Strings;

import au.com.bytecode.opencsv.CSVWriter;
import fr.paris.lutece.plugins.dansmarue.business.entities.MessageTypologie;
import fr.paris.lutece.plugins.dansmarue.business.entities.MessageTypologieExport;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.service.IMessageTypologieService;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.beanvalidation.ValidationError;
import fr.paris.lutece.util.url.UrlItem;

/**
 * The Class MessageTypeSignalementJspBean.
 */
@Controller( controllerJsp = "ManageMessageTypeSignalement.jsp", controllerPath = "jsp/admin/plugins/signalement/", right = "REFERENTIEL_MANAGEMENT_SIGNALEMENT" )
public class MessageTypeSignalementJspBean extends AbstractJspBean
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant RIGHT_MANAGE_TYPE_SIGNALEMENT. */
    // RIGHT
    public static final String RIGHT_MANAGE_TYPE_SIGNALEMENT = "REFERENTIEL_MANAGEMENT_SIGNALEMENT";

    /** The Constant MARK_LIST_TYPE_SIGNALEMENT. */
    private static final String MARK_LIST_TYPE_SIGNALEMENT = "list_type";

    /** The Constant MARK_LIST_MESSAGE. */
    private static final String MARK_LIST_MESSAGE = "list_message";

    /** The Constant MARK_TYPE_SIGNALEMENT. */
    private static final String MARK_TYPE_SIGNALEMENT = "type_signalement";

    /** The Constant MARK_WEBAPP_URL. */
    private static final String MARK_WEBAPP_URL = "webapp_url";

    /** The Constant MARK_ERRORS. */
    protected static final String MARK_ERRORS = "errors";

    /** The Constant MARK_IS_FROM_VALIDATION. */
    protected static final String MARK_IS_FROM_VALIDATION = "is_from_validation";

    /** The Constant MESSAGE_CONFIRMATION_DELETE_MESSAGE. */
    // Messages
    private static final String MESSAGE_CONFIRMATION_DELETE_MESSAGE = "dansmarue.messagetitle.messageTypeSignalement.delete.confirmation";

    /** The Constant MESSAGE_TITLE_DELETE_MESSAGE. */
    private static final String MESSAGE_TITLE_DELETE_MESSAGE = "dansmarue.message.messageTypeSignalement.delete.confirmation";

    /** The Constant MESSAGE_CONFIRMATION_DELETE_ALL_MESSAGE. */
    private static final String MESSAGE_CONFIRMATION_DELETE_ALL_MESSAGE = "dansmarue.messagetitle.messageTypeSignalement.deleteall.confirmation";

    /** The Constant MESSAGE_TITLE_DELETE_ALL_MESSAGE. */
    private static final String MESSAGE_TITLE_DELETE_ALL_MESSAGE = "dansmarue.message.messageTypeSignalement.deleteall.confirmation";

    /** The Constant PARAMETER_TYPE_SIGNALEMENT. */
    private static final String PARAMETER_TYPE_SIGNALEMENT = "typesignalement_id";

    /** The Constant PARAMETER_ORDRE_INITIAL. */
    private static final String PARAMETER_ORDRE_INITIAL = "ordreInitial";

    /** The Constant TEMPLATE_MANAGE_MESSAGE_TYPE_SIGNALEMENT. */
    // TEMPLATES
    private static final String TEMPLATE_MANAGE_MESSAGE_TYPE_SIGNALEMENT = "admin/plugins/signalement/manage_message_typesignalement.html";

    /** The Constant TEMPLATE_MODIFY_MESSAGE_TYPE_SIGNALEMENT. */
    private static final String TEMPLATE_MODIFY_MESSAGE_TYPE_SIGNALEMENT = "admin/plugins/signalement/modify_message_typesignalement.html";

    /** The Constant TEMPLATE_SAVE_MESSAGE_TYPE_SIGNALEMENT. */
    private static final String TEMPLATE_SAVE_MESSAGE_TYPE_SIGNALEMENT = "admin/plugins/signalement/save_message_typesignalement.html";

    /** The Constant JSP_MODIFY. */
    // JSP
    private static final String JSP_MODIFY = "ModifyMessageTypeSignalement.jsp";

    /** The Constant JSP_MANAGE. */
    private static final String JSP_MANAGE = "ManageMessageTypeSignalement.jsp";

    /** The Constant JSP_DELETE_MESSAGE. */
    private static final String JSP_DELETE_MESSAGE = "jsp/admin/plugins/signalement/DoDeleteMessageTypeSignalement.jsp";

    /** The Constant JSP_DELETE_ALL_MESSAGES. */
    private static final String JSP_DELETE_ALL_MESSAGES = "jsp/admin/plugins/signalement/DoDeleteAllMessagesTypeSignalement.jsp";

    /** The Constant PARAMETER_MARK_TYPE_SIGNALEMENT_ID. */
    // PARAMETERS
    private static final String PARAMETER_MARK_TYPE_SIGNALEMENT_ID = "typesignalement_id";

    /** The Constant PARAMETER_MARK_MESSAGE_ID. */
    private static final String PARAMETER_MARK_MESSAGE_ID = "message_id";

    /** The message typologie service. */
    // SERVICES
    private transient IMessageTypologieService _messageTypologieService = SpringContextService.getBean( "messageTypologieService" );

    /** The type signalement service. */
    private transient ITypeSignalementService _typeSignalementService = SpringContextService.getBean( "typeSignalementService" );

    /** The Constant VIEW_MANAGE_MESSAGES. */
    // Views
    private static final String VIEW_MANAGE_MESSAGES = "manageMessages";

    /** The Constant VIEW_SAVE_MESSAGES. */
    private static final String VIEW_SAVE_MESSAGES = "saveMessages";

    /** The Constant VIEW_MODIFY_MESSAGES. */
    private static final String VIEW_MODIFY_MESSAGES = "modifyMessages";

    /** The Constant CSV_ISO. */
    private static final String CSV_ISO = "ISO-8859-1";

    /** The Constant CSV_SEPARATOR. */
    private static final char CSV_SEPARATOR = ';';

    /**
     * Get the manage reporting type page.
     *
     * @param request
     *            the request
     * @return the page with reporting type list
     */
    @View( value = VIEW_MANAGE_MESSAGES, defaultView = true )
    public String getManageMessageTypeSignalement( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_LIST_TYPE_SIGNALEMENT, _typeSignalementService.getListTypeSignalementLastLevelWithMessage( ) );

        return getAdminPage( AppTemplateService.getTemplate( TEMPLATE_MANAGE_MESSAGE_TYPE_SIGNALEMENT, getLocale( ), model ).getHtml( ) );
    }

    /**
     * Gets the modify message type signalement.
     *
     * @param request
     *            the request
     * @return the modify message type signalement
     */
    @View( value = VIEW_MODIFY_MESSAGES )
    public String getModifyMessageTypeSignalement( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );

        if ( !Strings.isBlank( request.getParameter( PARAMETER_TYPE_SIGNALEMENT ) ) )
        {
            Integer idTypeSignalement = Integer.parseInt( request.getParameter( PARAMETER_TYPE_SIGNALEMENT ) );

            model.put( MARK_LIST_MESSAGE, _messageTypologieService.loadAllByIdType( idTypeSignalement ) );
            model.put( MARK_TYPE_SIGNALEMENT, _typeSignalementService.getTypeSignalementByIdWithParentsWithoutUnit( idTypeSignalement ) );
            model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        }

        return getAdminPage( AppTemplateService.getTemplate( TEMPLATE_MODIFY_MESSAGE_TYPE_SIGNALEMENT, getLocale( ), model ).getHtml( ) );
    }

    /**
     * Do modify message type signalement.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( "doModifyMessageTypeSignalement" )
    public String doModifyMessageTypeSignalement( HttpServletRequest request )
    {
        MessageTypologie messageTypologie = new MessageTypologie( );
        populate( messageTypologie, request );

        List<ValidationError> errors = validate( messageTypologie, "" );

        if ( !errors.isEmpty( ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, errors );
        }

        // MAJ de l'ordre des messages
        String strOrdreInitial = request.getParameter( PARAMETER_ORDRE_INITIAL );
        if ( !Strings.isBlank( strOrdreInitial ) )
        {
            Integer ordreInitial = Integer.parseInt( strOrdreInitial );

            // L'ordre a monté, décrémentation de l'ordre des messages qui passent derrière
            if ( ordreInitial > messageTypologie.getOrdre( ) )
            {
                _messageTypologieService.decreaseOrdreMessageTypologie( ordreInitial, messageTypologie );
            }
            else
                if ( ordreInitial < messageTypologie.getOrdre( ) )
                {
                    // L'ordre a baissé, incrémentation de l'ordre des messages qui passent devant
                    _messageTypologieService.increaseOrdreMessageTypologie( ordreInitial, messageTypologie );
                }
        }

        _messageTypologieService.updateMessageTypologie( messageTypologie );

        UrlItem urlItem = new UrlItem( JSP_MODIFY );
        urlItem.addParameter( PARAMETER_MARK_TYPE_SIGNALEMENT_ID, Long.toString( messageTypologie.getFkIdTypeSignalement( ) ) );

        return urlItem.getUrl( );
    }

    /**
     * Gets the save message type signalement.
     *
     * @param request
     *            the request
     * @return the save message type signalement
     */
    @View( value = VIEW_SAVE_MESSAGES )
    public String getSaveMessageTypeSignalement( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );
        model.put( MARK_ERRORS, new ArrayList<>( ) );

        model.put( MARK_LIST_TYPE_SIGNALEMENT, _typeSignalementService.getListTypeSignalementLastLevelWithoutMessage( ) );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );

        return getAdminPage( AppTemplateService.getTemplate( TEMPLATE_SAVE_MESSAGE_TYPE_SIGNALEMENT, getLocale( ), model ).getHtml( ) );
    }

    /**
     * Do save message type signalement.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( "" )
    public String doSaveMessageTypeSignalement( HttpServletRequest request )
    {
        MessageTypologie messageTypologie = new MessageTypologie( );
        populate( messageTypologie, request );

        List<ValidationError> errors = validate( messageTypologie, "" );

        if ( !errors.isEmpty( ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, errors );
        }

        messageTypologie.setContenuMessage( StringEscapeUtils.unescapeHtml( messageTypologie.getContenuMessage( ) ).replaceAll( "<[^>]*>", "" ) );
        _messageTypologieService.createMessageTypologie( messageTypologie );

        UrlItem urlItem = new UrlItem( JSP_MODIFY );
        urlItem.addParameter( PARAMETER_MARK_TYPE_SIGNALEMENT_ID, Long.toString( messageTypologie.getFkIdTypeSignalement( ) ) );

        return urlItem.getUrl( );
    }

    /**
     * delete message type signalement.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( "deleteMessageTypeSignalement" )
    public String deleteMessageTypeSignalement( HttpServletRequest request )
    {
        MessageTypologie messageTypologie = _messageTypologieService
                .loadMessageTypologie( Integer.parseInt( request.getParameter( PARAMETER_MARK_MESSAGE_ID ) ) );

        Map<String, Object> urlParam = new HashMap<>( );
        urlParam.put( PARAMETER_MARK_MESSAGE_ID, messageTypologie.getId( ) );
        urlParam.put( PARAMETER_MARK_TYPE_SIGNALEMENT_ID, messageTypologie.getFkIdTypeSignalement( ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRMATION_DELETE_MESSAGE, null, MESSAGE_TITLE_DELETE_MESSAGE, JSP_DELETE_MESSAGE, "_self",
                AdminMessage.TYPE_CONFIRMATION, urlParam, JSP_MANAGE );
    }

    /**
     * Do delete message type signalement.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( "doDeleteMessageTypeSignalement" )
    public String doDeleteMessageTypeSignalement( HttpServletRequest request )
    {
        Integer messageId = Integer.parseInt( request.getParameter( PARAMETER_MARK_MESSAGE_ID ) );
        Integer typeSignalementId = Integer.parseInt( request.getParameter( PARAMETER_MARK_TYPE_SIGNALEMENT_ID ) );

        // MAJ de l'ordre des messages - Incrémentation des messages d'ordres inférieur au message supprimé
        MessageTypologie messageTypologie = _messageTypologieService.loadMessageTypologie( messageId );
        _messageTypologieService.increaseOrdreMessageTypologieUnder( messageTypologie );

        _messageTypologieService.removeMessageTypologie( messageId );

        UrlItem urlItem;

        // Si il y a encore des messages de configuré, redirection vers la page de moficiation
        if ( !_messageTypologieService.loadAllByIdType( typeSignalementId ).isEmpty( ) )
        {
            urlItem = new UrlItem( JSP_MODIFY );
            urlItem.addParameter( PARAMETER_MARK_TYPE_SIGNALEMENT_ID, Long.toString( typeSignalementId ) );
        }
        else
        {
            // Sinon vers la page de création
            urlItem = new UrlItem( JSP_MANAGE );
        }
        return urlItem.getUrl( );
    }

    /**
     * Delete all messages type signalement.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( "deleteAllMessagesTypeSignalement" )
    public String deleteAllMessagesTypeSignalement( HttpServletRequest request )
    {
        TypeSignalement typeSignalement = _typeSignalementService
                .getByIdTypeSignalement( Integer.parseInt( request.getParameter( PARAMETER_MARK_TYPE_SIGNALEMENT_ID ) ) );

        Map<String, Object> urlParam = new HashMap<>( );
        urlParam.put( PARAMETER_MARK_TYPE_SIGNALEMENT_ID, typeSignalement.getId( ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRMATION_DELETE_ALL_MESSAGE, null, MESSAGE_TITLE_DELETE_ALL_MESSAGE,
                JSP_DELETE_ALL_MESSAGES, "_self", AdminMessage.TYPE_CONFIRMATION, urlParam, JSP_MANAGE );
    }

    /**
     * Do delete all message type signalement.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( "doDeleteAllMessagesTypeSignalement" )
    public String doDeleteAllMessagesTypeSignalement( HttpServletRequest request )
    {
        _messageTypologieService.removeMessageTypologieByIdTypeSignalement( Integer.parseInt( request.getParameter( PARAMETER_MARK_TYPE_SIGNALEMENT_ID ) ) );

        UrlItem urlItem = new UrlItem( JSP_MANAGE );
        return urlItem.getUrl( );
    }

    /**
     * Export des messages de typologie.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     */
    public void exportMessageTypologie( HttpServletRequest request, HttpServletResponse response )
    {
        String [ ] datas;
        try
        {
            CSVWriter writer = null;
            response.setCharacterEncoding( CSV_ISO );
            writer = new CSVWriter( response.getWriter( ), CSV_SEPARATOR );

            writer.writeNext( new String [ ] {
                    "Type d'anomalie", "Type de message de service fait associé", "Contenu du message", "Nombre de message configuré", "Actif"
            } );

            List<MessageTypologieExport> messageTypologieExportList = _messageTypologieService.getExportMessages( );

            for ( MessageTypologieExport messageTypologieExport : messageTypologieExportList )
            {
                if ( messageTypologieExport.getFkIdTypeSignalement( ) != null )
                {
                    TypeSignalement typeSignalement = _typeSignalementService
                            .getTypeSignalementByIdWithParents( messageTypologieExport.getFkIdTypeSignalement( ) );
                    if ( typeSignalement != null )
                    {
                        messageTypologieExport.setLibelleTypeSignalement( typeSignalement.getFormatTypeSignalement( ) );
                    }
                }
                datas = messageTypologieExport.getTabAllDatas( );
                writer.writeNext( datas );
            }

            writer.close( );

        }
        catch( IOException e )
        {
            AppLogService.error( e );
        }
    }
}
