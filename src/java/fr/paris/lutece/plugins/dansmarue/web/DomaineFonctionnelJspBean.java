package fr.paris.lutece.plugins.dansmarue.web;

import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.DomaineFonctionnel;
import fr.paris.lutece.plugins.sira.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.UnitNode;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.FunctionnalException;
import fr.paris.lutece.plugins.dansmarue.service.IArrondissementService;
import fr.paris.lutece.plugins.dansmarue.service.IConseilQuartierService;
import fr.paris.lutece.plugins.dansmarue.service.IDomaineFonctionnelService;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.ListUtils;
import fr.paris.lutece.plugins.dansmarue.utils.UnitUtils;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.unittree.service.unit.UnitService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.beanvalidation.ValidationError;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.xml.ValidationException;


/**
 * This class provides the user interface to manage form features ( manage,
 * create, modify, remove)
 */

public class DomaineFonctionnelJspBean extends AbstractJspBean
{

    //MESSAGES
    private static final String MESSAGE_CONFIRMATION_DELETE_DOMAINE_FONCTIONNEL = "dansmarue.message.domainefonctionnel.delete.confirmation";
    private static final String MESSAGE_TITLE_DELETE_DOMAINE_FONCTIONNEL = "dansmarue.messagetitle.domainefonctionnel.delete.confirmation";
//    private static final String MESSAGE_ERROR_OBSERVATION_CANT_BE_REMOVED = "dansmarue.message.domaineFonctionnel.error.used";
    
    // PARAMETERS
    private static final String PARAMETER_DOMAINE_FONCTIONNEL_ID = "domaine_fonctionnel_id";
    private static final String PARAMETER_ARRONDISSEMENTS = "arrondissements";
    private static final String PARAMETER_QUARTIERS = "conseilQuartier";
    private static final String PARAMETER_CATEGORIES = "categories";
    private static final String PARAMETER_UNITS = "units";
    
    //JSP
    private static final String JSP_MANAGE_DOMAINE_FONCTIONNEL = "jsp/admin/plugins/signalement/ManageDomaineFonctionnel.jsp";
    private static final String JSP_DELETE_DOMAINE_FONCTIONNEL = "jsp/admin/plugins/signalement/DoDeleteDomaineFonctionnel.jsp";
    private static final String JSP_SAVE_DOMAINE_FONCTIONNEL = "SaveDomaineFonctionnel.jsp";
    private static final String JSP_MODIFY_DOMAINE_FONCTIONNEL = "ModifyDomaineFonctionnel.jsp";

    //RIGHT
    public static final String RIGHT_MANAGE_DOMAINE_FONCTIONNEL = "REFERENTIEL_MANAGEMENT_SIGNALEMENT";

    //CONSTANTS
    public static final String ERROR_OCCUR = "error";

    //Markers
    private static final String MARK_DOMAINE_FONCTIONNEL_LIST = "domaine_fonctionnel_list";
    private static final String MARK_DOMAINE_FONCTIONNEL = "domaine_fonctionnel";
    private static final String MARK_ARRONDISSEMENT_LIST = "arrondissement_list";
    private static final String MARK_QUARTIER_LIST = "quartier_list";
    private static final String MARK_ARRONDISSEMENT_LIST_SIZE = "arrondissement_list_size";
    private static final String MARK_CATEGORY_LIST = "category_list";
    private static final String MARK_CATEGORY_LIST_SIZE = "category_list_size";
    private static final String MARK_UNIT_TREE = "unit_tree";
    private static final String MARK_UNIT_LIST_SIZE = "unit_list_size";
    

    //TEMPLATES
    private static final String TEMPLATE_MANAGE_DOMAINE_FONCTIONNEL = "admin/plugins/signalement/manage_domaine_fonctionnel.html";
    private static final String TEMPLATE_SAVE_DOMAINE_FONCTIONNEL = "admin/plugins/signalement/save_domaine_fonctionnel.html";
    private static final String TEMPLATE_MODIFY_DOMAINE_FONCTIONNEL = "admin/plugins/signalement/modify_domaine_fonctionnel.html";

    // SERVICES
    private IDomaineFonctionnelService _domaineFonctionnelService;
    private IArrondissementService _arrondissementService;
    private IConseilQuartierService _quartierService;
    private ITypeSignalementService _typeSignalementService;
    private IUnitService _unitService = SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );

    //PROPERTIES
    private static final String PROPERTY_DOMAINE_ARRONDISSEMENT_SELECT_SIZE = AppPropertiesService.getProperty("signalement.domaine.select.arrondissement.list.size");
    private static final String PROPERTY_DOMAINE_UNIT_SELECT_SIZE = AppPropertiesService.getProperty("signalement.domaine.select.unit.list.size");
    private static final String PROPERTY_DOMAINE_CATEGORIE_SELECT_SIZE = AppPropertiesService.getProperty("signalement.domaine.select.categorie.list.size");
    private static final Integer PROPERTY_DOMAINE_UNIT_DEPTH = AppPropertiesService.getPropertyInt("signalement.domaine.unit.depth", 0);
    
    @Override
    public void init( HttpServletRequest request, String strRight ) throws AccessDeniedException
    {
        super.init( request, strRight );
        _domaineFonctionnelService = (IDomaineFonctionnelService) SpringContextService.getBean( "domaineFonctionnelService" );
        _arrondissementService = (IArrondissementService) SpringContextService.getBean("signalement.arrondissementService");
        _quartierService = (IConseilQuartierService) SpringContextService.getBean("signalement.conseilQuartierService");
        _typeSignalementService = (ITypeSignalementService) SpringContextService.getBean("typeSignalementService");
    }

    /**
     * Get the manage DomaineFonctionnel page
     * 
     * @param request
     *            the request
     * @return the page with DomaineFonctionnel list
     */
    public String getManageDomaineFonctionnel( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        List<DomaineFonctionnel> listeNatureObjet = _domaineFonctionnelService.getAllDomainesFonctionnel( );

        model.put( MARK_DOMAINE_FONCTIONNEL_LIST, listeNatureObjet );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MANAGE_DOMAINE_FONCTIONNEL, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Delete a DomaineFonctionnel
     * @param request The HTTP request
     * @return redirection url
     */
    public String doDeleteDomaineFonctionnel( HttpServletRequest request )
    {

        String strDomaineFonctionnelId = request.getParameter( PARAMETER_DOMAINE_FONCTIONNEL_ID );

        int nIdDomaineFonctionnel = 0;

        try
        {
            nIdDomaineFonctionnel = Integer.parseInt( strDomaineFonctionnelId );
        }
        catch ( NumberFormatException e )
        {

            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR,
                    AdminMessage.TYPE_STOP );
        }

        _domaineFonctionnelService.remove( nIdDomaineFonctionnel );

        return doGoBack( request );

    }

    /**
     * Returns the confirmation message to delete a DomaineFonctionnel
     * 
     * @param request The Http request
     * @return the html code message
     */
    public String getDeleteDomaineFonctionnel( HttpServletRequest request )
    {

        String strJspBack = JSP_MANAGE_DOMAINE_FONCTIONNEL;

        String strIdDomaineFonctionnel = request.getParameter( PARAMETER_DOMAINE_FONCTIONNEL_ID );

        int nIdDomaineFonctionnel = 0;

        try
        {
            nIdDomaineFonctionnel = Integer.parseInt( strIdDomaineFonctionnel );
        }
        catch ( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR,
                    AdminMessage.TYPE_STOP );
        }

        Map<String, Object> urlParam = new HashMap<String, Object>( );
        urlParam.put( PARAMETER_DOMAINE_FONCTIONNEL_ID, nIdDomaineFonctionnel );

        return AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_DELETE_DOMAINE_FONCTIONNEL, null,
                MESSAGE_CONFIRMATION_DELETE_DOMAINE_FONCTIONNEL, JSP_DELETE_DOMAINE_FONCTIONNEL, "_self",
                AdminMessage.TYPE_CONFIRMATION, urlParam, strJspBack );

    }

    /**
     * Returns the form for DomaineFonctionnel creation
     * @param request The HTTP request
     * @return HTML Form
     */

    public String getSaveDomaineFonctionnel( HttpServletRequest request )
    {

        Map<String, Object> model = new HashMap<String, Object>( );

        DomaineFonctionnel domaineFonctionnel = new DomaineFonctionnel( );
        // value of "actif" by default
        domaineFonctionnel.setActif( true );

        // Manage validation errors
        FunctionnalException ve = getErrorOnce( request );

        if ( ve != null )
        {
            domaineFonctionnel = (DomaineFonctionnel) ve.getBean( );
            model.put( ERROR_OCCUR, getHtmlError( ve ) );
        }

        List<Arrondissement> arrondissementList = _arrondissementService.getAllArrondissement();
        List<TypeSignalement> categoryList = _typeSignalementService.getAllTypeSignalementWithoutParent();
        
        UnitNode root = new UnitNode(_unitService.getRootUnit(false));
        UnitUtils.buildTree(root,PROPERTY_DOMAINE_UNIT_DEPTH);
        
        model.put(MARK_QUARTIER_LIST, _quartierService.selectQuartiersList( ));
        model.put(MARK_UNIT_TREE, root);
        model.put(MARK_UNIT_LIST_SIZE, PROPERTY_DOMAINE_UNIT_SELECT_SIZE);
        model.put(MARK_ARRONDISSEMENT_LIST, arrondissementList);
        model.put(MARK_ARRONDISSEMENT_LIST_SIZE, PROPERTY_DOMAINE_ARRONDISSEMENT_SELECT_SIZE);
        model.put(MARK_CATEGORY_LIST, categoryList);
        model.put(MARK_CATEGORY_LIST_SIZE, PROPERTY_DOMAINE_CATEGORIE_SELECT_SIZE);
        
        model.put( MARK_DOMAINE_FONCTIONNEL, domaineFonctionnel );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_SAVE_DOMAINE_FONCTIONNEL, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Returns the form for DomaineFonctionnel modification
     * @param request The HTTP request
     * @return HTML Form
     */

    public String getModifyDomaineFonctionnel( HttpServletRequest request )
    {

        Map<String, Object> model = new HashMap<String, Object>( );

        DomaineFonctionnel domaineFonctionnel = new DomaineFonctionnel( );

        String strIdDomaineFonctionnel = request.getParameter( PARAMETER_DOMAINE_FONCTIONNEL_ID );

        int nIdDomaineFonctionnel = 0;

        try
        {
            nIdDomaineFonctionnel = Integer.parseInt( strIdDomaineFonctionnel );
        }
        catch ( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR,
                    AdminMessage.TYPE_STOP );
        }

        domaineFonctionnel = _domaineFonctionnelService.getById( nIdDomaineFonctionnel );

        // Manage validation errors
        FunctionnalException ve = getErrorOnce( request );
        if ( ve != null )
        {
            domaineFonctionnel = (DomaineFonctionnel) ve.getBean( );
            model.put( "error", getHtmlError( ve ) );
        }

        List<Arrondissement> arrondissementList = _arrondissementService.getAllArrondissement();
        List<TypeSignalement> categoryList = _typeSignalementService.getAllTypeSignalementWithoutParent();
       
        UnitNode root = new UnitNode(_unitService.getRootUnit(false));
        UnitUtils.buildTree(root,PROPERTY_DOMAINE_UNIT_DEPTH);
        
        model.put(MARK_UNIT_TREE, root);
        model.put(MARK_UNIT_LIST_SIZE, PROPERTY_DOMAINE_UNIT_SELECT_SIZE);
        model.put(MARK_ARRONDISSEMENT_LIST, arrondissementList);
        model.put(MARK_QUARTIER_LIST, _quartierService.selectQuartiersList( ));
        model.put(MARK_ARRONDISSEMENT_LIST_SIZE, PROPERTY_DOMAINE_ARRONDISSEMENT_SELECT_SIZE);
        model.put(MARK_CATEGORY_LIST, categoryList);
        model.put(MARK_CATEGORY_LIST_SIZE, PROPERTY_DOMAINE_CATEGORIE_SELECT_SIZE);
        
        model.put( MARK_DOMAINE_FONCTIONNEL, domaineFonctionnel );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MODIFY_DOMAINE_FONCTIONNEL, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Modify a DomaineFonctionnel
     * @param request The HTTP request
     * @return redirection url
     */

    public String doModifyDomaineFonctionnel( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( "cancel" ) ) )
        {
            return doGoBack( request );
        }

        String strIdDomaineFonctionnel = request.getParameter( PARAMETER_DOMAINE_FONCTIONNEL_ID );
        int nIdDomaineFonctionnel = 0;

        try
        {
            nIdDomaineFonctionnel = Integer.parseInt( strIdDomaineFonctionnel );
        }
        catch ( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR,
                    AdminMessage.TYPE_STOP );
        }

        DomaineFonctionnel domaineFonctionnel = new DomaineFonctionnel( );
        populate( domaineFonctionnel, request );

        String[] arrondissements = request.getParameterValues(PARAMETER_ARRONDISSEMENTS);
        String[] categories = request.getParameterValues(PARAMETER_CATEGORIES);
        String[] units = request.getParameterValues(PARAMETER_UNITS);
        String[] quartiers = request.getParameterValues(PARAMETER_QUARTIERS);
        
        if(!ArrayUtils.isEmpty(arrondissements)){
        	List<Integer> arrondissementList = ListUtils.getListOfIntFromStrArray(arrondissements);
        	domaineFonctionnel.setArrondissementsIds(arrondissementList);
        }
        if(!ArrayUtils.isEmpty(quartiers)){
            domaineFonctionnel.setQuartiersIds(ListUtils.getListOfIntFromStrArray(quartiers));
        }
        if(!ArrayUtils.isEmpty(categories)){
        	List<Integer> categoryList = ListUtils.getListOfIntFromStrArray(categories);
        	domaineFonctionnel.setTypesSignalementIds(categoryList);
        }
        if(!ArrayUtils.isEmpty(units)){
        	List<Integer> unidsIds = ListUtils.getListOfIntFromStrArray(units);
        	domaineFonctionnel.setUnitIds(unidsIds);
        }
        
        // Controls mandatory fields
        List<ValidationError> errors = validate( domaineFonctionnel, "" );
        if ( errors.isEmpty( ) )
        {
            try
            {
                _domaineFonctionnelService.store( domaineFonctionnel );
            }
            catch ( BusinessException e )
            {
                UrlItem url = new UrlItem( JSP_MODIFY_DOMAINE_FONCTIONNEL );
                url.addParameter( PARAMETER_DOMAINE_FONCTIONNEL_ID, domaineFonctionnel.getId( ) );
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
     * Save a DomaineFonctionnel
     * @param request The HTTP request
     * @return redirection url
     * @throws ValidationException
     */
    public String doSaveDomaineFonctionnel( HttpServletRequest request ) throws ValidationException
    {

        FunctionnalException ve = getErrorOnce( request );

        if ( StringUtils.isNotBlank( request.getParameter( "cancel" ) ) )
        {
            return doGoBack( request );
        }

        DomaineFonctionnel domaineFonctionnel = new DomaineFonctionnel( );
        populate( domaineFonctionnel, request );

        String[] arrondissements = request.getParameterValues(PARAMETER_ARRONDISSEMENTS);
        String[] categories = request.getParameterValues(PARAMETER_CATEGORIES);
        String[] units = request.getParameterValues(PARAMETER_UNITS);
        String[] quartiers = request.getParameterValues(PARAMETER_QUARTIERS);
        
        if(!ArrayUtils.isEmpty(quartiers)){
            domaineFonctionnel.setQuartiersIds(ListUtils.getListOfIntFromStrArray(quartiers));
        }
        if(!ArrayUtils.isEmpty(arrondissements)){
        	List<Integer> arrondissementList = ListUtils.getListOfIntFromStrArray(arrondissements);
        	domaineFonctionnel.setArrondissementsIds(arrondissementList);
        }
        if(!ArrayUtils.isEmpty(categories)){
        	List<Integer> categoryList = ListUtils.getListOfIntFromStrArray(categories);
        	domaineFonctionnel.setTypesSignalementIds(categoryList);
        }
        if(!ArrayUtils.isEmpty(units)){
        	List<Integer> unitsIds = ListUtils.getListOfIntFromStrArray(units);
        	domaineFonctionnel.setUnitIds(unitsIds);
        }
        
        // Controls mandatory fields
        List<ValidationError> errors = validate( domaineFonctionnel, "" );
        if ( errors.isEmpty( ) )
        {
            try
            {
                _domaineFonctionnelService.insert( domaineFonctionnel );
            }
            catch ( BusinessException e )
            {
                UrlItem url = new UrlItem( JSP_SAVE_DOMAINE_FONCTIONNEL );
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
                : AppPathService.getBaseUrl( request ) + JSP_MANAGE_DOMAINE_FONCTIONNEL;

    }

}
