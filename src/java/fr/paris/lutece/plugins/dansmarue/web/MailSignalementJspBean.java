package fr.paris.lutece.plugins.dansmarue.web;

import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.FunctionnalException;
import fr.paris.lutece.plugins.dansmarue.service.IPhotoService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.impl.EmailValidator;


public class MailSignalementJspBean extends AbstractJspBean
{

    //TEMPLATES
    private static final String TEMPLATE_MANAGE_MAIL = "admin/plugins/signalement/manage_mail.html";

    //PARAMETERS
    /** The Constant PARAMETER_SIGNALEMENT_ID. */
    public static final String PARAMETER_SIGNALEMENT_ID = "signalement_id";
    /** The Constant PARAMETER_RECIPIENT. */
    public static final String PARAMETER_RECIPIENT = "recipientMail";
    /** The Constant PARAMETER_SENDER. */
    public static final String PARAMETER_SENDER = "senderMail";
    /** The Constant PARAMETER_OBJECT. */
    public static final String PARAMETER_OBJECT = "objectMail";
    /** The Constant PARAMETER_CONTENT. */
    public static final String PARAMETER_CONTENT = "contentMail";
    public static final String PARAMETER_WANTS_PHOTO_IN_MAIL = "photos";
    public static final String PARAMETER_ON = "on";

    //MESSAGES
    private static final String MESSAGE_ERROR_RECIPIENT_MANDATORY = "dansmarue.message.recipient.mandatory";
    private static final String MESSAGE_ERROR_RECIPIENT_FORMAT = "dansmarue.message.recipient.format";
    private static final String MESSAGE_ERROR_SENDER_MANDATORY = "dansmarue.message.sender.mandatory";
    private static final String MESSAGE_ERROR_OBJECT_MANDATORY = "dansmarue.message.object.mandatory";
    private static final String MESSAGE_ERROR_CONTENT_MANDATORY = "dansmarue.message.content.mandatory";
    private static final String MESSAGE_ERROR_EXPEDITEUR_FORMAT = "dansmarue.message.exp.format";
    private static final String MESSAGE_MAIL_NUMBER_SIGNALEMENT = "Num\u00e9ro du message";
    private static final String MESSAGE_MAIL_COMMENTAIRE = "Commentaire";
    private static final String MESSAGE_MAIL_MAIL_SIGNALEUR = "Email du signaleur";
    private static final String MESSAGE_MAIL_TYPE_SIGNALEMENT = "Type d'incident";
    private static final String MESSAGE_MAIL_PRIORITE = "Priorit\u00E9";
    private static final String MESSAGE_MAIL_ADRESSE_SIGNALEMENT = "Localisation";
    private static final String MESSAGE_MAIL_PRECISION_LOC = "Pr\u00e9cision de localisation";
    private static final String MESSAGE_MAIL_LINK = "Lien";
    private static final String MESSAGE_MAIL_BONJOUR = "Bonjour, ";

    //CONSTANTS
    private final static String LINE_SEPARATOR = "<br />";
    private final static String RECIPIENT_SEPARATOR = ";";

    // MEMBERS VARIABLES
    private ISignalementService _signalementService;
    private IPhotoService _photoService;

    //PROPERTIES
    private static final String PROPERTY_BASE_URL = "lutece.prod.url";

    //Markers
    /** The Constant MARK_MAIL_ITEM. */
    private static final String MARK_MAIL_ITEM = "mail_item";
    private static final String MARK_WEBAPP_URL = "webapp_url";

    //JSP
    private static final String JSP_MANAGE_MAIL = "DoCreateMailSignalement.jsp";
    private static final String JSP_MANAGE_SIGNALEMENTS = "ManageSignalement.jsp";
    private static final String JSP_PORTAL = "jsp/admin/plugins/signalement/ViewSignalement.jsp?signalement_id=";
    private static final String JSP_MANAGE_SIGNALEMENT = "jsp/admin/plugins/signalement/ManageSignalement.jsp";

    @Override
    public void init( HttpServletRequest request, String strRight, String keyResourceType, String permission )
            throws AccessDeniedException
    {
        super.init( request, strRight, keyResourceType, permission );
        initServices( );
    }

    /**
     * Initialize the signalemnet DAO
     */
    private void initServices( )
    {
        this._signalementService = (ISignalementService) SpringContextService.getBean( "signalementService" );
        this._photoService = (IPhotoService) SpringContextService.getBean( "photoService" );
    }

    /**
     * Return the manage mail page.
     * 
     * @param request the request
     * @return html of the manage mail page
     * @throws AccessDeniedException the access denied exception
     */
    public String getManageMail( HttpServletRequest request ) throws AccessDeniedException
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
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR,
                    AdminMessage.TYPE_STOP );
        }

        // Manage validation errors
        FunctionnalException ve = getErrorOnce( request );
        if ( ve != null )
        {
            mailItem = (MailItem) ve.getBean( );
            model.put( "error", getHtmlError( ve ) );
        }
        else
        {
            //If there is no precedent errors
            if ( StringUtils.isNotBlank( strCaseId ) )
            {
                Signalement signalement = new Signalement( );
                signalement = _signalementService.getSignalement( Long.valueOf( nIdCase ) );

                List<Signaleur> signaleurs = signalement.getSignaleurs( );

                StringBuffer strBuff = new StringBuffer( );

                // Bonjour
                strBuff.append( MESSAGE_MAIL_BONJOUR + LINE_SEPARATOR );

                // case number
                strBuff.append( LINE_SEPARATOR + MESSAGE_MAIL_NUMBER_SIGNALEMENT + " : "
                        + signalement.getNumeroSignalement( ) );

                // mail 
                if ( !signaleurs.isEmpty( ) && !signaleurs.get( 0 ).getMail( ).isEmpty( ) )
                {
                    strBuff.append( LINE_SEPARATOR + MESSAGE_MAIL_MAIL_SIGNALEUR + " : "
                            + signaleurs.get( 0 ).getMail( ) );
                }

                // signalement type
                strBuff.append( LINE_SEPARATOR + MESSAGE_MAIL_TYPE_SIGNALEMENT + " : " + signalement.getType( ) );

                // priority 
                strBuff.append( LINE_SEPARATOR + MESSAGE_MAIL_PRIORITE + " : " + signalement.getPrioriteName( ) );
                
                // address
                strBuff.append( LINE_SEPARATOR + MESSAGE_MAIL_ADRESSE_SIGNALEMENT + " : " );
                for ( Adresse adresse : signalement.getAdresses( ) )
                {
                    strBuff.append( "<a href=\"" + adresse.getGoogleLink() + "\">" + adresse.getAdresse( ) + "</a>" + LINE_SEPARATOR );
                }

                for ( Adresse adresse : signalement.getAdresses( ) )
                {
                    if ( StringUtils.isNotBlank( adresse.getPrecisionLocalisation( ) ) )
                    {
                        strBuff.append( MESSAGE_MAIL_PRECISION_LOC + " : " + adresse.getPrecisionLocalisation( )
                                + LINE_SEPARATOR );
                    }
                }

                
                // comment
                if ( StringUtils.isNotBlank( signalement.getCommentaire( ) ) )
                {
                    strBuff.append( MESSAGE_MAIL_COMMENTAIRE + " : " + signalement.getCommentaire( ) );
                }

                // Link to the consultation page
                strBuff.append( LINE_SEPARATOR + LINE_SEPARATOR + MESSAGE_MAIL_LINK + " : "
                        + this.getLinkConsultation( signalement, request ) + signalement.getId( ) );

                mailItem.setMessage( strBuff.toString( ) );
            }

        }

        model.put( PARAMETER_SIGNALEMENT_ID, nIdCase );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( "locale", this.getLocale( ) );

        // Add jsp back for cancel button
        model.put( SignalementConstants.MARK_JSP_BACK, JSP_MANAGE_SIGNALEMENT );
        model.put( MARK_MAIL_ITEM, mailItem );
        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MANAGE_MAIL, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );

    }

    /**
     * Send a mail with informations of the form
     * @param request
     * @return url to go
     */
    public String doSendMail( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( SignalementConstants.PARAMETER_BUTTON_CANCEL ) ) )
        {
            String strJspBack = request.getParameter( SignalementConstants.MARK_JSP_BACK );

            return StringUtils.isNotBlank( strJspBack ) ? ( AppPathService.getBaseUrl( request ) + strJspBack )
                    : AppPathService.getBaseUrl( request ) + JSP_MANAGE_SIGNALEMENT;
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
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR,
                    AdminMessage.TYPE_STOP );
        }

        MailItem mailItem = new MailItem( );

        populate( mailItem, request );
        List<FileAttachment> files = new ArrayList<FileAttachment>( );

        if ( strWantPhotosInEmail != null )
        {
            if ( strWantPhotosInEmail.equals( PARAMETER_ON ) )
            {
                List<PhotoDMR> photos = _photoService.findWithFullPhotoBySignalementId( nIdSignalement );

                for ( PhotoDMR photo : photos )
                {
                    String[] mime = photo.getImage( ).getMimeType( ).split( "/" );
                    String imgExtention = mime[1];
                    if( imgExtention != null )
                    {
                    	imgExtention.replaceAll("pjpeg", "jpg");
                    }
                    
                    if ( photo.getVue( ) == 1 )
                    {
                        files.add( new FileAttachment( "VueEnsemble." + imgExtention, photo.getImage( )
                                        .getImage( ), photo.getImage( ).getMimeType( ) ) );

                    }
                    else
                    {
                        files.add( new FileAttachment( "VueDetaillee." + imgExtention, photo.getImage( )
                                        .getImage( ), photo.getImage( ).getMimeType( ) ) );
                    }
                }

                    mailItem.setFilesAttachement( files );

            }
        }

        // Try to validate fields of the form
        try
        {
            this.valideFormFields( request, mailItem );

            // Check format email expediteur 
            EmailValidator emailValidator = new EmailValidator( );

            if ( !emailValidator.isValid( mailItem.getSenderEmail( ), null ) )
            {
                throw new BusinessException( mailItem, MESSAGE_ERROR_EXPEDITEUR_FORMAT );
            }
        }
        catch ( BusinessException e )
        {
            return manageFunctionnalException( request, e, JSP_MANAGE_MAIL + "?" + PARAMETER_SIGNALEMENT_ID + "="
                    + nIdSignalement );
        }

        //Send the mail
        //        MailService.sendMailHtml( mailItem.getRecipientsTo( ), mailItem.getSenderEmail( ), "",
        //                mailItem.getSubject( ), mailItem.getMessage( ), );
        MailService.sendMailMultipartHtml( mailItem.getRecipientsTo( ), null, null, "Mairie de Paris",
                mailItem.getSenderEmail( ), mailItem.getSubject( ), mailItem.getMessage( ), null,
                mailItem.getFilesAttachement( ) );

        return JSP_MANAGE_SIGNALEMENTS;
    }

    /**
     * Check if the fields are completed
     * @param request the request
     */
    private void valideFormFields( HttpServletRequest request, MailItem mailItem )
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

    private String getLinkConsultation( Signalement signalement, HttpServletRequest request )
    {
        UrlItem urlItem;

        urlItem = new UrlItem( AppPropertiesService.getProperty( PROPERTY_BASE_URL ) + JSP_PORTAL );

        return urlItem.getUrl( );
    }
}
