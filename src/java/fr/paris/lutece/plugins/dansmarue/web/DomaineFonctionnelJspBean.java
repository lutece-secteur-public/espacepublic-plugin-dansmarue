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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.DomaineFonctionnel;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
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
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
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

/**
 * This class provides the user interface to manage form features ( manage, create, modify, remove).
 */

public class DomaineFonctionnelJspBean extends AbstractJspBean
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6689540999532217301L;

    /** The Constant MESSAGE_CONFIRMATION_DELETE_DOMAINE_FONCTIONNEL. */
    // MESSAGES
    private static final String MESSAGE_CONFIRMATION_DELETE_DOMAINE_FONCTIONNEL = "dansmarue.message.domainefonctionnel.delete.confirmation";

    /** The Constant MESSAGE_TITLE_DELETE_DOMAINE_FONCTIONNEL. */
    private static final String MESSAGE_TITLE_DELETE_DOMAINE_FONCTIONNEL = "dansmarue.messagetitle.domainefonctionnel.delete.confirmation";

    /** The Constant PARAMETER_DOMAINE_FONCTIONNEL_ID. */
    // PARAMETERS
    private static final String PARAMETER_DOMAINE_FONCTIONNEL_ID = "domaine_fonctionnel_id";

    /** The Constant PARAMETER_ARRONDISSEMENTS. */
    private static final String PARAMETER_ARRONDISSEMENTS = "arrondissements";

    /** The Constant PARAMETER_QUARTIERS. */
    private static final String PARAMETER_QUARTIERS = "conseilQuartier";

    /** The Constant PARAMETER_CATEGORIES. */
    private static final String PARAMETER_CATEGORIES = "categories";

    /** The Constant PARAMETER_UNITS. */
    private static final String PARAMETER_UNITS = "units";

    /** The Constant JSP_MANAGE_DOMAINE_FONCTIONNEL. */
    // JSP
    private static final String JSP_MANAGE_DOMAINE_FONCTIONNEL = "jsp/admin/plugins/signalement/ManageDomaineFonctionnel.jsp";

    /** The Constant JSP_DELETE_DOMAINE_FONCTIONNEL. */
    private static final String JSP_DELETE_DOMAINE_FONCTIONNEL = "jsp/admin/plugins/signalement/DoDeleteDomaineFonctionnel.jsp";

    /** The Constant JSP_SAVE_DOMAINE_FONCTIONNEL. */
    private static final String JSP_SAVE_DOMAINE_FONCTIONNEL = "SaveDomaineFonctionnel.jsp";

    /** The Constant JSP_MODIFY_DOMAINE_FONCTIONNEL. */
    private static final String JSP_MODIFY_DOMAINE_FONCTIONNEL = "ModifyDomaineFonctionnel.jsp";

    /** The Constant RIGHT_MANAGE_DOMAINE_FONCTIONNEL. */
    // RIGHT
    public static final String RIGHT_MANAGE_DOMAINE_FONCTIONNEL = "REFERENTIEL_MANAGEMENT_SIGNALEMENT";

    /** The Constant ERROR_OCCUR. */
    // CONSTANTS
    public static final String ERROR_OCCUR = "error";

    /** The Constant MARK_DOMAINE_FONCTIONNEL_LIST. */
    // Markers
    private static final String MARK_DOMAINE_FONCTIONNEL_LIST = "domaine_fonctionnel_list";

    /** The Constant MARK_DOMAINE_FONCTIONNEL. */
    private static final String MARK_DOMAINE_FONCTIONNEL = "domaine_fonctionnel";

    /** The Constant MARK_ARRONDISSEMENT_LIST. */
    private static final String MARK_ARRONDISSEMENT_LIST = "arrondissement_list";

    /** The Constant MARK_QUARTIER_LIST. */
    private static final String MARK_QUARTIER_LIST = "quartier_list";

    /** The Constant MARK_ARRONDISSEMENT_LIST_SIZE. */
    private static final String MARK_ARRONDISSEMENT_LIST_SIZE = "arrondissement_list_size";

    /** The Constant MARK_CATEGORY_LIST. */
    private static final String MARK_CATEGORY_LIST = "category_list";

    /** The Constant MARK_CATEGORY_LIST_SIZE. */
    private static final String MARK_CATEGORY_LIST_SIZE = "category_list_size";

    /** The Constant MARK_UNIT_TREE. */
    private static final String MARK_UNIT_TREE = "unit_tree";

    /** The Constant MARK_UNIT_LIST_SIZE. */
    private static final String MARK_UNIT_LIST_SIZE = "unit_list_size";

    /** The Constant TEMPLATE_MANAGE_DOMAINE_FONCTIONNEL. */
    // TEMPLATES
    private static final String TEMPLATE_MANAGE_DOMAINE_FONCTIONNEL = "admin/plugins/signalement/manage_domaine_fonctionnel.html";

    /** The Constant TEMPLATE_SAVE_DOMAINE_FONCTIONNEL. */
    private static final String TEMPLATE_SAVE_DOMAINE_FONCTIONNEL = "admin/plugins/signalement/save_domaine_fonctionnel.html";

    /** The Constant TEMPLATE_MODIFY_DOMAINE_FONCTIONNEL. */
    private static final String TEMPLATE_MODIFY_DOMAINE_FONCTIONNEL = "admin/plugins/signalement/modify_domaine_fonctionnel.html";

    /** The domaine fonctionnel service. */
    // SERVICES
    private transient IDomaineFonctionnelService _domaineFonctionnelService;

    /** The arrondissement service. */
    private transient IArrondissementService _arrondissementService;

    /** The quartier service. */
    private transient IConseilQuartierService _quartierService;

    /** The type signalement service. */
    private transient ITypeSignalementService _typeSignalementService;

    /** The unit service. */
    private transient IUnitService _unitService = SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );

    /** The Constant PROPERTY_DOMAINE_ARRONDISSEMENT_SELECT_SIZE. */
    // PROPERTIES
    private static final String PROPERTY_DOMAINE_ARRONDISSEMENT_SELECT_SIZE = AppPropertiesService
            .getProperty( "signalement.domaine.select.arrondissement.list.size" );

    /** The Constant PROPERTY_DOMAINE_UNIT_SELECT_SIZE. */
    private static final String PROPERTY_DOMAINE_UNIT_SELECT_SIZE = AppPropertiesService.getProperty( "signalement.domaine.select.unit.list.size" );

    /** The Constant PROPERTY_DOMAINE_CATEGORIE_SELECT_SIZE. */
    private static final String PROPERTY_DOMAINE_CATEGORIE_SELECT_SIZE = AppPropertiesService.getProperty( "signalement.domaine.select.categorie.list.size" );

    /** The Constant PROPERTY_DOMAINE_UNIT_DEPTH. */
    private static final Integer PROPERTY_DOMAINE_UNIT_DEPTH = AppPropertiesService.getPropertyInt( "signalement.domaine.unit.depth", 0 );

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
        _domaineFonctionnelService = (IDomaineFonctionnelService) SpringContextService.getBean( "domaineFonctionnelService" );
        _arrondissementService = (IArrondissementService) SpringContextService.getBean( "signalement.arrondissementService" );
        _quartierService = (IConseilQuartierService) SpringContextService.getBean( "signalement.conseilQuartierService" );
        _typeSignalementService = (ITypeSignalementService) SpringContextService.getBean( "typeSignalementService" );
    }

    /**
     * Get the manage DomaineFonctionnel page.
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
     * Delete a DomaineFonctionnel.
     *
     * @param request
     *            The HTTP request
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
        catch( NumberFormatException e )
        {

            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        _domaineFonctionnelService.remove( nIdDomaineFonctionnel );

        return doGoBack( request );

    }

    /**
     * Returns the confirmation message to delete a DomaineFonctionnel.
     *
     * @param request
     *            The Http request
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
        catch( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        Map<String, Object> urlParam = new HashMap<String, Object>( );
        urlParam.put( PARAMETER_DOMAINE_FONCTIONNEL_ID, nIdDomaineFonctionnel );

        return AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_DELETE_DOMAINE_FONCTIONNEL, null, MESSAGE_CONFIRMATION_DELETE_DOMAINE_FONCTIONNEL,
                JSP_DELETE_DOMAINE_FONCTIONNEL, "_self", AdminMessage.TYPE_CONFIRMATION, urlParam, strJspBack );

    }

    /**
     * Returns the form for DomaineFonctionnel creation.
     *
     * @param request
     *            The HTTP request
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

        List<Arrondissement> arrondissementList = _arrondissementService.getAllArrondissement( );
        List<TypeSignalement> categoryList = _typeSignalementService.getAllTypeSignalementWithoutParent( );

        UnitNode root = new UnitNode( _unitService.getRootUnit( false ) );
        UnitUtils.buildTree( root, PROPERTY_DOMAINE_UNIT_DEPTH );

        model.put( MARK_QUARTIER_LIST, _quartierService.selectQuartiersList( ) );
        model.put( MARK_UNIT_TREE, root );
        model.put( MARK_UNIT_LIST_SIZE, PROPERTY_DOMAINE_UNIT_SELECT_SIZE );
        model.put( MARK_ARRONDISSEMENT_LIST, arrondissementList );
        model.put( MARK_ARRONDISSEMENT_LIST_SIZE, PROPERTY_DOMAINE_ARRONDISSEMENT_SELECT_SIZE );
        model.put( MARK_CATEGORY_LIST, categoryList );
        model.put( MARK_CATEGORY_LIST_SIZE, PROPERTY_DOMAINE_CATEGORIE_SELECT_SIZE );

        model.put( MARK_DOMAINE_FONCTIONNEL, domaineFonctionnel );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_SAVE_DOMAINE_FONCTIONNEL, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Returns the form for DomaineFonctionnel modification.
     *
     * @param request
     *            The HTTP request
     * @return HTML Form
     */

    public String getModifyDomaineFonctionnel( HttpServletRequest request )
    {

        Map<String, Object> model = new HashMap<String, Object>( );

        DomaineFonctionnel domaineFonctionnel;

        String strIdDomaineFonctionnel = request.getParameter( PARAMETER_DOMAINE_FONCTIONNEL_ID );

        int nIdDomaineFonctionnel = 0;

        try
        {
            nIdDomaineFonctionnel = Integer.parseInt( strIdDomaineFonctionnel );
        }
        catch( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        domaineFonctionnel = _domaineFonctionnelService.getById( nIdDomaineFonctionnel );

        // Manage validation errors
        FunctionnalException ve = getErrorOnce( request );
        if ( ve != null )
        {
            domaineFonctionnel = (DomaineFonctionnel) ve.getBean( );
            model.put( ERROR_OCCUR, getHtmlError( ve ) );
        }

        List<Arrondissement> arrondissementList = _arrondissementService.getAllArrondissement( );
        List<TypeSignalement> categoryList = _typeSignalementService.getAllTypeSignalementWithoutParent( );

        UnitNode root = new UnitNode( _unitService.getRootUnit( false ) );
        UnitUtils.buildTree( root, PROPERTY_DOMAINE_UNIT_DEPTH );

        model.put( MARK_UNIT_TREE, root );
        model.put( MARK_UNIT_LIST_SIZE, PROPERTY_DOMAINE_UNIT_SELECT_SIZE );
        model.put( MARK_ARRONDISSEMENT_LIST, arrondissementList );
        model.put( MARK_QUARTIER_LIST, _quartierService.selectQuartiersList( ) );
        model.put( MARK_ARRONDISSEMENT_LIST_SIZE, PROPERTY_DOMAINE_ARRONDISSEMENT_SELECT_SIZE );
        model.put( MARK_CATEGORY_LIST, categoryList );
        model.put( MARK_CATEGORY_LIST_SIZE, PROPERTY_DOMAINE_CATEGORIE_SELECT_SIZE );

        model.put( MARK_DOMAINE_FONCTIONNEL, domaineFonctionnel );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MODIFY_DOMAINE_FONCTIONNEL, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Modify a DomaineFonctionnel.
     *
     * @param request
     *            The HTTP request
     * @return redirection url
     */

    public String doModifyDomaineFonctionnel( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( "cancel" ) ) )
        {
            return doGoBack( request );
        }
        DomaineFonctionnel domaineFonctionnel = new DomaineFonctionnel( );
        populate( domaineFonctionnel, request );

        String [ ] arrondissements = request.getParameterValues( PARAMETER_ARRONDISSEMENTS );
        String [ ] categories = request.getParameterValues( PARAMETER_CATEGORIES );
        String [ ] units = request.getParameterValues( PARAMETER_UNITS );
        String [ ] quartiers = request.getParameterValues( PARAMETER_QUARTIERS );

        if ( !ArrayUtils.isEmpty( arrondissements ) )
        {
            List<Integer> arrondissementList = ListUtils.getListOfIntFromStrArray( arrondissements );
            domaineFonctionnel.setArrondissementsIds( arrondissementList );
        }
        if ( !ArrayUtils.isEmpty( quartiers ) )
        {
            domaineFonctionnel.setQuartiersIds( ListUtils.getListOfIntFromStrArray( quartiers ) );
        }
        if ( !ArrayUtils.isEmpty( categories ) )
        {
            List<Integer> categoryList = ListUtils.getListOfIntFromStrArray( categories );
            domaineFonctionnel.setTypesSignalementIds( categoryList );
        }
        if ( !ArrayUtils.isEmpty( units ) )
        {
            List<Integer> unidsIds = ListUtils.getListOfIntFromStrArray( units );
            domaineFonctionnel.setUnitIds( unidsIds );
        }

        // Controls mandatory fields
        List<ValidationError> errors = validate( domaineFonctionnel, "" );
        if ( errors.isEmpty( ) )
        {
            try
            {
                _domaineFonctionnelService.store( domaineFonctionnel );
            }
            catch( BusinessException e )
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
     * Save a DomaineFonctionnel.
     *
     * @param request
     *            The HTTP request
     * @return redirection url
     */
    public String doSaveDomaineFonctionnel( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( "cancel" ) ) )
        {
            return doGoBack( request );
        }

        DomaineFonctionnel domaineFonctionnel = new DomaineFonctionnel( );
        populate( domaineFonctionnel, request );

        String [ ] arrondissements = request.getParameterValues( PARAMETER_ARRONDISSEMENTS );
        String [ ] categories = request.getParameterValues( PARAMETER_CATEGORIES );
        String [ ] units = request.getParameterValues( PARAMETER_UNITS );
        String [ ] quartiers = request.getParameterValues( PARAMETER_QUARTIERS );

        if ( !ArrayUtils.isEmpty( quartiers ) )
        {
            domaineFonctionnel.setQuartiersIds( ListUtils.getListOfIntFromStrArray( quartiers ) );
        }
        if ( !ArrayUtils.isEmpty( arrondissements ) )
        {
            List<Integer> arrondissementList = ListUtils.getListOfIntFromStrArray( arrondissements );
            domaineFonctionnel.setArrondissementsIds( arrondissementList );
        }
        if ( !ArrayUtils.isEmpty( categories ) )
        {
            List<Integer> categoryList = ListUtils.getListOfIntFromStrArray( categories );
            domaineFonctionnel.setTypesSignalementIds( categoryList );
        }
        if ( !ArrayUtils.isEmpty( units ) )
        {
            List<Integer> unitsIds = ListUtils.getListOfIntFromStrArray( units );
            domaineFonctionnel.setUnitIds( unitsIds );
        }

        // Controls mandatory fields
        List<ValidationError> errors = validate( domaineFonctionnel, "" );
        if ( errors.isEmpty( ) )
        {
            try
            {
                _domaineFonctionnelService.insert( domaineFonctionnel );
            }
            catch( BusinessException e )
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
                : ( AppPathService.getBaseUrl( request ) + JSP_MANAGE_DOMAINE_FONCTIONNEL );

    }

}
