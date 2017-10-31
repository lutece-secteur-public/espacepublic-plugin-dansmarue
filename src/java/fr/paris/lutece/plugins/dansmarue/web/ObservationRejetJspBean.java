package fr.paris.lutece.plugins.dansmarue.web;

import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.FunctionnalException;
import fr.paris.lutece.plugins.dansmarue.service.IObservationRejetService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.beanvalidation.ValidationError;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.quartz.xml.ValidationException;


/**
 * This class provides the user interface to manage form features ( manage,
 * create, modify, remove)
 */

public class ObservationRejetJspBean extends AbstractJspBean
{

    //MESSAGES
    private static final String MESSAGE_CONFIRMATION_DELETE_OBSERVATION_REJET = "dansmarue.message.observationRejet.delete.confirmation";
    private static final String MESSAGE_TITLE_DELETE_OBSERVATION_REJET = "dansmarue.messagetitle.observationRejet.delete.confirmation";
    private static final String MESSAGE_ERROR_OBSERVATION_CANT_BE_REMOVED = "dansmarue.message.observationRejet.error.used";
    // PARAMETERS
    public static final String PARAMETER_OBSERVATION_REJET_ID = "observation_rejet_id";

    //JSP
    private static final String JSP_MANAGE_OBSERVATION_REJET = "jsp/admin/plugins/signalement/ManageObservationRejet.jsp";
    private static final String JSP_DELETE_OBSERVATION_REJET = "jsp/admin/plugins/signalement/DoDeleteObservationRejet.jsp";
    private static final String JSP_SAVE_OBSERVATION_REJET = "SaveObservationRejet.jsp";
    private static final String JSP_MODIFY_OBSERVATION_REJET = "ModifyObservationRejet.jsp";

    //RIGHT
    public static final String RIGHT_MANAGE_OBSERVATION_REJET = "REFERENTIEL_MANAGEMENT_SIGNALEMENT";

    //CONSTANTS
    public static final String ERROR_OCCUR = "error";

    //Markers
    private static final String MARK_OBSERVATION_REJET_LIST = "observation_rejet_list";
    private static final String MARK_OBSERVATION_REJET = "observation_rejet";

    //TEMPLATES
    private static final String TEMPLATE_MANAGE_OBSERVATION_REJET = "admin/plugins/signalement/manage_observation_rejet.html";
    private static final String TEMPLATE_SAVE_OBSERVATION_REJET = "admin/plugins/signalement/save_observation_rejet.html";
    private static final String TEMPLATE_MODIFY_OBSERVATION_REJET = "admin/plugins/signalement/modify_observation_rejet.html";

    // SERVICES
    private IObservationRejetService _observationRejetService;// = (IObservationRejetService) SpringContextService.getBean( "observationRejetService" );
    private ISignalementService _signalementService;

    @Override
    public void init( HttpServletRequest request, String strRight ) throws AccessDeniedException
    {
        super.init( request, strRight );
        _observationRejetService = (IObservationRejetService) SpringContextService.getBean( "observationRejetService" );
        _signalementService = (ISignalementService) SpringContextService.getBean( "signalementService" );
    }

    /**
     * Get the manage ObservationRejet page
     * 
     * @param request
     *            the request
     * @return the page with ObservationRejet list
     */
    public String getManageObservationRejet( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        List<ObservationRejet> listeNatureObjet = _observationRejetService.getAllObservationRejet( );

        model.put( MARK_OBSERVATION_REJET_LIST, listeNatureObjet );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MANAGE_OBSERVATION_REJET, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Delete a ObservationRejet
     * @param request The HTTP request
     * @return redirection url
     */
    public String doDeleteObservationRejet( HttpServletRequest request )
    {

        String strObservationRejetId = request.getParameter( PARAMETER_OBSERVATION_REJET_ID );

        int nIdObservationRejet = 0;

        try
        {
            nIdObservationRejet = Integer.parseInt( strObservationRejetId );
        }
        catch ( NumberFormatException e )
        {

            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR,
                    AdminMessage.TYPE_STOP );
        }

        _observationRejetService.doDeleteObservationRejet( nIdObservationRejet );

        return doGoBack( request );

    }

    /**
     * Returns the confirmation message to delete a ObservationRejet
     * 
     * @param request The Http request
     * @return the html code message
     */
    public String getDeleteObservationRejet( HttpServletRequest request )
    {

        String strJspBack = JSP_MANAGE_OBSERVATION_REJET;

        String strIdObservationRejet = request.getParameter( PARAMETER_OBSERVATION_REJET_ID );

        int nIdObservationRejet = 0;

        try
        {
            nIdObservationRejet = Integer.parseInt( strIdObservationRejet );
        }
        catch ( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR,
                    AdminMessage.TYPE_STOP );
        }

        if ( _observationRejetService.countByIdObservationRejet( nIdObservationRejet ) > 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_OBSERVATION_CANT_BE_REMOVED,
                    AdminMessage.TYPE_STOP );
        }

        Map<String, Object> urlParam = new HashMap<String, Object>( );
        urlParam.put( PARAMETER_OBSERVATION_REJET_ID, nIdObservationRejet );

        return AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_DELETE_OBSERVATION_REJET, null,
                MESSAGE_CONFIRMATION_DELETE_OBSERVATION_REJET, JSP_DELETE_OBSERVATION_REJET, "_self",
                AdminMessage.TYPE_CONFIRMATION, urlParam, strJspBack );

    }

    /**
     * Returns the form for ObservationRejet creation
     * @param request The HTTP request
     * @return HTML Form
     */

    public String getSaveObservationRejet( HttpServletRequest request )
    {

        Map<String, Object> model = new HashMap<String, Object>( );

        ObservationRejet observationRejet = new ObservationRejet( );
        // value of "actif" by default
        observationRejet.setActif( true );

        // Manage validation errors
        FunctionnalException ve = getErrorOnce( request );

        if ( ve != null )
        {
            observationRejet = (ObservationRejet) ve.getBean( );
            model.put( ERROR_OCCUR, getHtmlError( ve ) );
        }

        model.put( MARK_OBSERVATION_REJET, observationRejet );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_SAVE_OBSERVATION_REJET, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Returns the form for ObservationRejet modification
     * @param request The HTTP request
     * @return HTML Form
     */

    public String getModifyObservationRejet( HttpServletRequest request )
    {

        Map<String, Object> model = new HashMap<String, Object>( );

        ObservationRejet observationRejet = new ObservationRejet( );

        String strIdObservationRejet = request.getParameter( PARAMETER_OBSERVATION_REJET_ID );

        int nIdObservationRejet = 0;

        try
        {
            nIdObservationRejet = Integer.parseInt( strIdObservationRejet );
        }
        catch ( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR,
                    AdminMessage.TYPE_STOP );
        }

        observationRejet = _observationRejetService.getById( nIdObservationRejet );

        // Manage validation errors
        FunctionnalException ve = getErrorOnce( request );
        if ( ve != null )
        {
            observationRejet = (ObservationRejet) ve.getBean( );
            model.put( "error", getHtmlError( ve ) );
        }

        model.put( MARK_OBSERVATION_REJET, observationRejet );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MODIFY_OBSERVATION_REJET, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Modify a ObservationRejet
     * @param request The HTTP request
     * @return redirection url
     */

    public String doModifyObservationRejet( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( "cancel" ) ) )
        {
            return doGoBack( request );
        }

        String strIdObservationRejet = request.getParameter( PARAMETER_OBSERVATION_REJET_ID );
        int nIdObservationRejet = 0;

        try
        {
            nIdObservationRejet = Integer.parseInt( strIdObservationRejet );
        }
        catch ( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR,
                    AdminMessage.TYPE_STOP );
        }

        ObservationRejet observationRejet = new ObservationRejet( );
        populate( observationRejet, request );

        // Controls mandatory fields
        List<ValidationError> errors = validate( observationRejet, "" );
        if ( errors.isEmpty( ) )
        {
            try
            {
                _observationRejetService.doSaveObservationRejet( observationRejet );
            }
            catch ( BusinessException e )
            {
                UrlItem url = new UrlItem( JSP_MODIFY_OBSERVATION_REJET );
                url.addParameter( PARAMETER_OBSERVATION_REJET_ID, observationRejet.getId( ) );
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
     * Save a ObservationRejet
     * @param request The HTTP request
     * @return redirection url
     * @throws ValidationException
     */
    public String doSaveObservationRejet( HttpServletRequest request ) throws ValidationException
    {

        FunctionnalException ve = getErrorOnce( request );

        if ( StringUtils.isNotBlank( request.getParameter( "cancel" ) ) )
        {
            return doGoBack( request );
        }

        ObservationRejet observationRejet = new ObservationRejet( );
        populate( observationRejet, request );

        // Controls mandatory fields
        List<ValidationError> errors = validate( observationRejet, "" );
        if ( errors.isEmpty( ) )
        {
            try
            {
                _observationRejetService.doSaveObservationRejet( observationRejet );
            }
            catch ( BusinessException e )
            {
                UrlItem url = new UrlItem( JSP_SAVE_OBSERVATION_REJET );
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
     * Return the url of the JSP which called the last action
     * @param request The Http request
     * @return The url of the last JSP
     */
    private String doGoBack( HttpServletRequest request )
    {
        String strJspBack = request.getParameter( SignalementConstants.MARK_JSP_BACK );

        return StringUtils.isNotBlank( strJspBack ) ? ( AppPathService.getBaseUrl( request ) + strJspBack )
                : AppPathService.getBaseUrl( request ) + JSP_MANAGE_OBSERVATION_REJET;

    }
    
    /**
     * Increase the observation rejet ordre
     * @param request
     * @return
     */
    public String doIncreaseObservationRejetOrder(HttpServletRequest request){
    	ObservationRejet observationRejet = new ObservationRejet();
    	populate(observationRejet,request);
    	_observationRejetService.increaseOrdreOfRejet(observationRejet);
    	return AppPathService.getBaseUrl( request ) + JSP_MANAGE_OBSERVATION_REJET;
    }
    
    /**
     * Decrease the observation rejet ordre
     * @param request
     * @return
     */
    public String doDecreaseObservationRejetOrder(HttpServletRequest request){
    	ObservationRejet observationRejet = new ObservationRejet();
    	populate(observationRejet,request);
    	_observationRejetService.decreaseOrdreOfRejet(observationRejet);
    	return AppPathService.getBaseUrl( request ) + JSP_MANAGE_OBSERVATION_REJET;
    	
    }

}
