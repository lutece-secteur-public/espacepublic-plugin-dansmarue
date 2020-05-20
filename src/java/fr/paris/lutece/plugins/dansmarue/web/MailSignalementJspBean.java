/*
 * Copyright (c) 2002-2020, City of Paris
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.FunctionnalException;
import fr.paris.lutece.plugins.dansmarue.service.IPhotoService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.mail.MailItem;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.mail.FileAttachment;
import fr.paris.lutece.util.url.UrlItem;

/**
 * The Class MailSignalementJspBean.
 */
public class MailSignalementJspBean extends AbstractJspBean
{

    /** The Constant serialVersionUID. */
    private static final long   serialVersionUID                  = -5382654434035169932L;

    /** The Constant LINE_SEPARATOR. */
    // CONSTANTS
    private static final String LINE_SEPARATOR                    = "<br />";

    /** The Constant RECIPIENT_SEPARATOR. */
    private static final String RECIPIENT_SEPARATOR               = ";";

    /** The Constant TEMPLATE_MANAGE_MAIL. */
    // TEMPLATES
    private static final String TEMPLATE_MANAGE_MAIL              = "admin/plugins/signalement/manage_mail.html";

    // PARAMETERS
    /** The Constant PARAMETER_SIGNALEMENT_ID. */
    public static final String  PARAMETER_SIGNALEMENT_ID          = "signalement_id";
    /** The Constant PARAMETER_RECIPIENT. */
    public static final String  PARAMETER_RECIPIENT               = "recipientMail";
    /** The Constant PARAMETER_SENDER. */
    public static final String  PARAMETER_SENDER                  = "senderMail";
    /** The Constant PARAMETER_OBJECT. */
    public static final String  PARAMETER_OBJECT                  = "objectMail";

    /** The Constant PARAMETER_SUBJECT. */
    public static final String  PARAMETER_SUBJECT                 = "DMR – Anomalie sur un espace public";
    /** The Constant PARAMETER_CONTENT. */
    public static final String  PARAMETER_CONTENT                 = "contentMail";

    /** The Constant PARAMETER_WANTS_PHOTO_IN_MAIL. */
    public static final String  PARAMETER_WANTS_PHOTO_IN_MAIL     = "photos";

    /** The Constant PARAMETER_ON. */
    public static final String  PARAMETER_ON                      = "on";

    /** The Constant MESSAGE_ERROR_RECIPIENT_MANDATORY. */
    // MESSAGES
    private static final String MESSAGE_ERROR_RECIPIENT_MANDATORY = "dansmarue.message.recipient.mandatory";

    /** The Constant MESSAGE_ERROR_RECIPIENT_FORMAT. */
    private static final String MESSAGE_ERROR_RECIPIENT_FORMAT    = "dansmarue.message.recipient.format";

    /** The Constant MESSAGE_ERROR_SENDER_MANDATORY. */
    private static final String MESSAGE_ERROR_SENDER_MANDATORY    = "dansmarue.message.sender.mandatory";

    /** The Constant MESSAGE_ERROR_OBJECT_MANDATORY. */
    private static final String MESSAGE_ERROR_OBJECT_MANDATORY    = "dansmarue.message.object.mandatory";

    /** The Constant MESSAGE_ERROR_CONTENT_MANDATORY. */
    private static final String MESSAGE_ERROR_CONTENT_MANDATORY   = "dansmarue.message.content.mandatory";

    /** The Constant MESSAGE_ERROR_EXPEDITEUR_FORMAT. */
    private static final String MESSAGE_ERROR_EXPEDITEUR_FORMAT   = "dansmarue.message.exp.format";

    /** The Constant MESSAGE_MAIL_NUMBER_SIGNALEMENT. */
    private static final String MESSAGE_MAIL_NUMBER_SIGNALEMENT   = "Num\u00e9ro du message";

    /** The Constant MESSAGE_MAIL_DATE_CREATION. */
    private static final String MESSAGE_MAIL_DATE_CREATION        = "Date de cr\u00e9ation de l'anomalie";

    /** The Constant MESSAGE_MAIL_COMMENTAIRE. */
    private static final String MESSAGE_MAIL_COMMENTAIRE          = "Commentaire";

    /** The Constant MESSAGE_MAIL_MAIL_SIGNALEUR. */
    private static final String MESSAGE_MAIL_MAIL_SIGNALEUR       = "Email du signaleur";

    /** The Constant MESSAGE_MAIL_TYPE_SIGNALEMENT. */
    private static final String MESSAGE_MAIL_TYPE_SIGNALEMENT     = "Type d'incident";

    /** The Constant MESSAGE_MAIL_PRIORITE. */
    private static final String MESSAGE_MAIL_PRIORITE             = "Priorit\u00E9";

    /** The Constant MESSAGE_MAIL_ADRESSE_SIGNALEMENT. */
    private static final String MESSAGE_MAIL_ADRESSE_SIGNALEMENT  = "Localisation";

    /** The Constant MESSAGE_MAIL_PRECISION_LOC. */
    private static final String MESSAGE_MAIL_PRECISION_LOC        = "Pr\u00e9cision de localisation";

    /** The Constant MESSAGE_MAIL_LINK_WITH_ACCOUNT. */
    private static final String MESSAGE_MAIL_LINK_WITH_ACCOUNT    = "Lien back-office authentifié";

    /** The Constant MESSAGE_MAIL_LINK. */
    private static final String MESSAGE_MAIL_LINK                 = "Lien accessible pour concessionnaire";

    /** The Constant MESSAGE_MAIL_BONJOUR. */
    private static final String MESSAGE_MAIL_BONJOUR              = "Bonjour, ";

    /** The signalement service. */
    // MEMBERS VARIABLES
    private transient ISignalementService _signalementService;

    /** The photo service. */
    private transient IPhotoService       _photoService;

    /** The Constant PROPERTY_BASE_URL. */
    // PROPERTIES
    private static final String PROPERTY_BASE_URL                 = "lutece.prod.url";

    /** The Constant PROPERTY_BASE_TS_URL. */
    private static final String PROPERTY_BASE_TS_URL              = "lutece.ts.prod.url";

    // Markers
    /** The Constant MARK_MAIL_ITEM. */
    private static final String MARK_MAIL_ITEM                    = "mail_item";

    /** The Constant MARK_WEBAPP_URL. */
    private static final String MARK_WEBAPP_URL                   = "webapp_url";

    /** The Constant MARK_SENDER_MAIL. */
    private static final String MARK_SENDER_MAIL                  = "sender_mail";

    /** The Constant JSP_MANAGE_MAIL. */
    // JSP
    private static final String JSP_MANAGE_MAIL                   = "DoCreateMailSignalement.jsp";

    /** The Constant JSP_MANAGE_SIGNALEMENTS. */
    private static final String JSP_MANAGE_SIGNALEMENTS           = "ManageSignalement.jsp";

    /** The Constant JSP_PORTAL. */
    private static final String JSP_PORTAL                        = "jsp/admin/plugins/signalement/ViewSignalement.jsp?signalement_id=";

    /** The Constant JSP_PORTAL_USER. */
    private static final String JSP_PORTAL_USER                   = "jsp/site/Portal.jsp?instance=signalement&bo_link=true&page=suivi&token=";

    /** The Constant JSP_MANAGE_SIGNALEMENT. */
    private static final String JSP_MANAGE_SIGNALEMENT            = "jsp/admin/plugins/signalement/ManageSignalement.jsp";

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.dansmarue.web.AbstractJspBean#init(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void init( HttpServletRequest request, String strRight, String keyResourceType, String permission ) throws AccessDeniedException
    {
        super.init( request, strRight, keyResourceType, permission );
        initServices( );
    }

    /**
     * Initialize the report DAO.
     */
    private void initServices( )
    {
        _signalementService = ( ISignalementService ) SpringContextService.getBean( "signalementService" );
        _photoService = ( IPhotoService ) SpringContextService.getBean( "photoService" );
    }

    /**
     * Return the manage mail page.
     *
     * @param request
     *            the request
     * @return html of the manage mail page
     */
    public String getManageMail( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        String strCaseId = request.getParameter( PARAMETER_SIGNALEMENT_ID );

        MailItem mailItem = new MailItem( );

        int nIdCase = 0;

        try
        {
            nIdCase = Integer.parseInt( strCaseId );
        }
        catch ( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        // Manage validation errors
        FunctionnalException ve = getErrorOnce( request );
        if ( ve != null )
        {
            mailItem = ( MailItem ) ve.getBean( );
            model.put( "error", getHtmlError( ve ) );
        }
        else
        {
            // If there is no precedent errors
            if ( StringUtils.isNotBlank( strCaseId ) )
            {
                Signalement signalement = _signalementService.getSignalement( Long.valueOf( nIdCase ) );

                List<Signaleur> signaleurs = signalement.getSignaleurs( );

                StringBuilder strBuff = new StringBuilder( );

                // Bonjour
                strBuff.append( MESSAGE_MAIL_BONJOUR + LINE_SEPARATOR );

                // case number
                strBuff.append( LINE_SEPARATOR + MESSAGE_MAIL_NUMBER_SIGNALEMENT + " : " + signalement.getNumeroSignalement( ) );

                // date_creation
                strBuff.append( LINE_SEPARATOR + MESSAGE_MAIL_DATE_CREATION + " : Le " + signalement.getDateCreation( ) + " à " + DateUtils.getHourFr( signalement.getHeureCreation( ) ) );

                // mail
                if ( !signaleurs.isEmpty( ) && !signaleurs.get( 0 ).getMail( ).isEmpty( ) )
                {
                    strBuff.append( LINE_SEPARATOR + MESSAGE_MAIL_MAIL_SIGNALEUR + " : " + signaleurs.get( 0 ).getMail( ) );
                }

                // signalement type
                strBuff.append( LINE_SEPARATOR + MESSAGE_MAIL_TYPE_SIGNALEMENT + " : " + signalement.getType( ) );

                // priority
                strBuff.append( LINE_SEPARATOR + MESSAGE_MAIL_PRIORITE + " : " + signalement.getPrioriteName( ) );

                // address
                strBuff.append( LINE_SEPARATOR + MESSAGE_MAIL_ADRESSE_SIGNALEMENT + " : " );
                for ( Adresse adresse : signalement.getAdresses( ) )
                {
                    strBuff.append( "<a href=\"" + adresse.getGoogleLink( ) + "\">" + adresse.getAdresse( ) + "</a>" + LINE_SEPARATOR );
                }

                for ( Adresse adresse : signalement.getAdresses( ) )
                {
                    if ( StringUtils.isNotBlank( adresse.getPrecisionLocalisation( ) ) )
                    {
                        strBuff.append( MESSAGE_MAIL_PRECISION_LOC + " : " + adresse.getPrecisionLocalisation( ) + LINE_SEPARATOR );
                    }
                }

                // comment
                if ( StringUtils.isNotBlank( signalement.getCommentaire( ) ) )
                {
                    strBuff.append( MESSAGE_MAIL_COMMENTAIRE + " : " + signalement.getCommentaire( ) );
                }

                // Link to the consultation page with BO account
                strBuff.append( LINE_SEPARATOR + LINE_SEPARATOR + MESSAGE_MAIL_LINK_WITH_ACCOUNT + " : <a href=\"" + getLinkConsultationWithAcc( ) + signalement.getId( ) + "\">"
                        + getLinkConsultationWithAcc( ) + signalement.getId( ) + "</a>" );

                // Link to the consultation page user
                strBuff.append( LINE_SEPARATOR + LINE_SEPARATOR + MESSAGE_MAIL_LINK + " : <a href=\"" + getLinkConsultation( ) + signalement.getToken( ) + "\">" + getLinkConsultation( )
                + signalement.getToken( ) + "</a>" );

                mailItem.setMessage( strBuff.toString( ) );
                mailItem.setSubject( PARAMETER_SUBJECT );
            }

        }

        AdminUser user = getUser( );

        model.put( MARK_SENDER_MAIL, user.getEmail( ) );
        model.put( PARAMETER_SIGNALEMENT_ID, nIdCase );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( "locale", getLocale( ) );

        // Add jsp back for cancel button
        model.put( SignalementConstants.MARK_JSP_BACK, JSP_MANAGE_SIGNALEMENT );
        model.put( MARK_MAIL_ITEM, mailItem );
        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MANAGE_MAIL, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );

    }

    /**
     * Send a mail with informations of the form.
     *
     * @param request            the HttpServletRequest
     * @return url to go
     */
    public String doSendMail( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( SignalementConstants.PARAMETER_BUTTON_CANCEL ) ) )
        {
            String strJspBack = request.getParameter( SignalementConstants.MARK_JSP_BACK );

            return StringUtils.isNotBlank( strJspBack ) ? ( AppPathService.getBaseUrl( request ) + strJspBack ) : AppPathService.getBaseUrl( request ) + JSP_MANAGE_SIGNALEMENT;
        }

        String strIdSignalement = request.getParameter( PARAMETER_SIGNALEMENT_ID );
        String strWantPhotosInEmail = request.getParameter( PARAMETER_WANTS_PHOTO_IN_MAIL );

        Integer nIdSignalement = 0;
        try
        {
            nIdSignalement = Integer.parseInt( strIdSignalement );
        }
        catch ( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        MailItem mailItem = new MailItem( );

        populate( mailItem, request );


        if ( strWantPhotosInEmail.equals( PARAMETER_ON ) )
        {
            attachPhototoMail( mailItem, nIdSignalement );
        }

        // Try to validate fields of the form
        try
        {
            valideFormFields( mailItem );

            // Check format sender email
            EmailValidator emailValidator = new EmailValidator( );

            if ( !emailValidator.isValid( mailItem.getSenderEmail( ), null ) )
            {
                throw new BusinessException( mailItem, MESSAGE_ERROR_EXPEDITEUR_FORMAT );
            }
        }
        catch ( BusinessException e )
        {
            return manageFunctionnalException( request, e, JSP_MANAGE_MAIL + "?" + PARAMETER_SIGNALEMENT_ID + "=" + nIdSignalement );
        }

        // Send the mail
        MailService.sendMailMultipartHtml( mailItem.getRecipientsTo( ), null, null, "Mairie de Paris", mailItem.getSenderEmail( ), mailItem.getSubject( ), mailItem.getMessage( ), null,
                mailItem.getFilesAttachement( ) );

        // Recording of email information in the database
        Signalement signalement = _signalementService.getSignalement( nIdSignalement );
        Timestamp timestamp = new Timestamp( System.currentTimeMillis( ) );
        signalement.setCourrielDate( timestamp );
        signalement.setCourrielDestinataire( mailItem.getRecipientsTo( ) );
        signalement.setCourrielExpediteur( mailItem.getSenderEmail( ) );
        _signalementService.update( signalement );

        return JSP_MANAGE_SIGNALEMENTS;
    }

    /**
     * Check if the fields are completed.
     *
     * @param mailItem the mail item
     */
    private void valideFormFields( MailItem mailItem )
    {
        // Check empty fields
        if ( StringUtils.isBlank( mailItem.getRecipientsTo( ) ) )
        {
            throw new BusinessException( mailItem, MESSAGE_ERROR_RECIPIENT_MANDATORY );
        }
        else if ( StringUtils.isBlank( mailItem.getSenderEmail( ) ) )
        {
            throw new BusinessException( mailItem, MESSAGE_ERROR_SENDER_MANDATORY );
        }
        else if ( StringUtils.isBlank( mailItem.getSubject( ) ) )
        {
            throw new BusinessException( mailItem, MESSAGE_ERROR_OBJECT_MANDATORY );
        }
        else if ( StringUtils.isBlank( mailItem.getMessage( ) ) )
        {
            throw new BusinessException( mailItem, MESSAGE_ERROR_CONTENT_MANDATORY );
        }

        // Check format fields
        EmailValidator emailValidator = new EmailValidator( );
        String[] listRecipient = mailItem.getRecipientsTo( ).split( RECIPIENT_SEPARATOR );
        for ( String recipient : listRecipient )
        {
            if ( !emailValidator.isValid( recipient.trim( ), null ) )
            {
                throw new BusinessException( mailItem, MESSAGE_ERROR_RECIPIENT_FORMAT );
            }
        }
    }

    /**
     * Attach photo(s) to mail
     * @param mailItem
     *         mail object
     * @param nIdSignalement
     *         id signalement
     */
    private void attachPhototoMail(MailItem mailItem, Integer nIdSignalement) {

        List<PhotoDMR> photos = _photoService.findWithFullPhotoBySignalementId( nIdSignalement );

        List<FileAttachment> files = new ArrayList<>( );

        for ( PhotoDMR photo : photos )
        {
            if ( ( photo.getImage( ) != null ) && ( photo.getImage( ).getImage( ) != null ) )
            {
                String[] mime = photo.getImage( ).getMimeType( ).split( "/" );
                String imgExtention = mime[1];

                if ( photo.getVue( ) == 1 )
                {
                    files.add( new FileAttachment( SignalementConstants.NOM_PHOTO_ENSEMBLE_PJ + imgExtention, photo.getImage( ).getImage( ), photo.getImage( ).getMimeType( ) ) );
                }
                else if ( photo.getVue( ) == 0 )
                {
                    files.add( new FileAttachment( SignalementConstants.NOM_PHOTO_PRES_PJ + imgExtention, photo.getImage( ).getImage( ), photo.getImage( ).getMimeType( ) ) );
                }
                else
                {
                    files.add( new FileAttachment( SignalementConstants.NOM_PHOTO_SERVICE_FAIT_PJ + imgExtention, photo.getImage( ).getImage( ), photo.getImage( ).getMimeType( ) ) );
                }

            }
        }

        mailItem.setFilesAttachement( files );
    }

    /**
     * Gets the link consultation with acc.
     *
     * @return the link consultation with acc
     */
    private String getLinkConsultationWithAcc( )
    {
        UrlItem urlItem;

        urlItem = new UrlItem( AppPropertiesService.getProperty( PROPERTY_BASE_URL ) + JSP_PORTAL );

        return urlItem.getUrl( );
    }

    /**
     * Gets the link consultation.
     *
     * @return the link consultation
     */
    private String getLinkConsultation( )
    {
        UrlItem urlItem;

        urlItem = new UrlItem( AppPropertiesService.getProperty( PROPERTY_BASE_TS_URL ) + JSP_PORTAL_USER );

        return urlItem.getUrl( );
    }
}
