/*
 * Copyright (c) 2002-2008, Mairie de Paris
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


import fr.paris.lutece.plugins.dansmarue.commons.ResultList;
import fr.paris.lutece.plugins.dansmarue.commons.Rights;
import fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties;
import fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationPropertiesImpl;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.FunctionnalException;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.ValidationException;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.util.LocalizedDelegatePaginator;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;

import org.apache.commons.lang.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;


/**
 * Abstract class for jsp bean
 */
public class AbstractJspBean extends PluginAdminPageJspBean
{

    protected String _strCurrentPageIndex = "";
    protected int _nItemsPerPage;
    public final int N_DEFAULT_ITEMS_PER_PAGE = AppPropertiesService.getPropertyInt(
            SignalementConstants.PROPERTY_DEFAULT_ITEM_PER_PAGE, 20 );
    protected static final String MARK_FILTER = "filter";
    public static final String URL_JSP_MANAGE_SIGNALEMENTS = "jsp/admin/plugins/signalement/ManageSignalement.jsp";
    protected static final String MARK_PAGINATOR = "paginator";
    protected static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private HttpServletRequest _request;

    //PARAMETERS
    private static final String PARAMETER_BUTTON_ACTUALISER = "Actualiser";
    private static final String PARAMETER_PAGE_INDEX_ONE = "1";
    private static final int PARAMETER_INT_PAGE_INDEX_ONE = 1;

    @Override
    public void init( HttpServletRequest request, String strRight ) throws AccessDeniedException
    {
        //        LOG.debug( "Init " + this.getClass( ) + ", id " + System.identityHashCode( this ) );
        super.init( request, strRight );
        this._request = request;
    }

    /**
     * Return a paginator for the view using parameter in http request
     * @param request http request
     * @param list bean list to paginate
     * @return paginator
     */
    protected <T> LocalizedDelegatePaginator<T> getPaginator( HttpServletRequest request, ResultList<T> list, String jspUrl,
            int totalResult )
    {
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        int nCurrentPageIndex = PARAMETER_INT_PAGE_INDEX_ONE;

        if ( StringUtils.isNotEmpty( _strCurrentPageIndex ) )
        {
            nCurrentPageIndex = Integer.valueOf( _strCurrentPageIndex );
        }

        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                N_DEFAULT_ITEMS_PER_PAGE );

        while ( ( ( nCurrentPageIndex - 1 ) * _nItemsPerPage ) > totalResult )
        {
            _strCurrentPageIndex = String.valueOf( nCurrentPageIndex - 1 );
            nCurrentPageIndex = Integer.valueOf( _strCurrentPageIndex );
        }

        StringBuilder strBaseUrl = new StringBuilder( );

        strBaseUrl.append( AppPathService.getBaseUrl( request ).toString( ) );
        strBaseUrl.append( jspUrl );

        LocalizedDelegatePaginator<T> paginator = new LocalizedDelegatePaginator<T>( list, _nItemsPerPage,
                strBaseUrl.toString( ), Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex, totalResult, getLocale( ) );

        return paginator;
    }

    /**
     * 
     * 
     * @param request
     * @param strRight
     * @param keyResourceType
     * @param permission
     * @throws AccessDeniedException
     */
    public void init( HttpServletRequest request, String strRight, String keyResourceType, String permission )
            throws AccessDeniedException
    {
        this.init( request, strRight );
        if ( !RBACService.isAuthorized( keyResourceType, RBAC.WILDCARD_RESOURCES_ID, permission, getUser( ) ) )
        {
            throw new AccessDeniedException( MessageFormat.format( "Acces denied for {0} / {1}.", keyResourceType,
                    permission ) );
        }
    }

    /**
     * Return a bean for pagination in service/dao using parameter in http
     * request
     * @param request http request
     * @param totalResult the total result
     * @return paginator
     */
    protected PaginationProperties getPaginationProperties( HttpServletRequest request, Integer totalResult )
    {
        String strPageIndex = "";

        strPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX,
                _strCurrentPageIndex );

        int nCurrentPageIndex = PARAMETER_INT_PAGE_INDEX_ONE;

        if ( StringUtils.isNotEmpty( strPageIndex ) )
        {
            nCurrentPageIndex = Integer.valueOf( strPageIndex );
        }

        int nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                N_DEFAULT_ITEMS_PER_PAGE );

        while ( ( ( nCurrentPageIndex - 1 ) * nItemsPerPage ) > totalResult )
        {
            nCurrentPageIndex = nCurrentPageIndex - 1;
            _strCurrentPageIndex = Integer.toString( nCurrentPageIndex );
        }

        return new PaginationPropertiesImpl( ( nCurrentPageIndex - 1 ) * nItemsPerPage, nItemsPerPage, nItemsPerPage,
                nCurrentPageIndex );
    }

    /**
     * Return localized message
     * @param key i18n key
     * @return localized message
     */
    protected String getMessage( String key )
    {
        return I18nService.getLocalizedString( key, this.getLocale( ) );
    }

    /**
     * Return localized message with args
     * @param key i18n key
     * @param args args
     * @return localized message
     */
    protected String getMessage( String key, String... args )
    {
        return I18nService.getLocalizedString( key, args, this.getLocale( ) );
    }

    /**
     * Get validation error from session and remove from it
     * @param request http request
     * @return validation exception
     */
    protected FunctionnalException getErrorOnce( HttpServletRequest request )
    {
        FunctionnalException fe = (FunctionnalException) request.getSession( ).getAttribute(
                SignalementConstants.PARAMETER_ERROR );
        if ( fe != null )
        {
            request.getSession( ).removeAttribute( SignalementConstants.PARAMETER_ERROR );
        }
        return fe;
    }
    /**
     * Return html code for error message
     * @param ve validation exception
     * @return html
     */
    protected String getHtmlError( FunctionnalException e )
    {
        Map<String, Object> model = new HashMap<String, Object>( );
        List<String> messageList = new ArrayList<String>( );
        try
        {
            throw e;
        }
        // Validation error
        catch ( ValidationException ve )
        {
            String typeName = ve.getBean( ).getClass( ).getSimpleName( );

            // Add a validation error message using value, field name and
            // provided
            // message
            for ( ConstraintViolation<?> constraintViolation : ve.getConstraintViolationList( ) )
            {
                String fieldName = getMessage( "dansmarue.field." + typeName + "."
                        + constraintViolation.getPropertyPath( ) );
                messageList.add( getMessage( "dansmarue.validation.error",
                        String.valueOf( constraintViolation.getInvalidValue( ) ), fieldName,
                        constraintViolation.getMessage( ) ) );
            }
        }
        // Business error
        catch ( BusinessException be )
        {
            messageList.add( getMessage( be.getCode( ), be.getArguments( ) ) );
        }
        
        model.put( "messageList", messageList );

        HtmlTemplate template = AppTemplateService.getTemplate( "admin/plugins/signalement/error.html",
                getLocale( ), model );
        return template.getHtml( );
    }

    protected String manageFunctionnalException( HttpServletRequest request, FunctionnalException e, String targetUrl )
    {

        request.getSession( ).setAttribute( SignalementConstants.PARAMETER_ERROR, e );
        return targetUrl;
    }

    /**
     * Generates an html page.
     * @param strTemplate template file path.
     * @param model Model map.
     * @return html page.
     */
    protected String getTemplate( String strTemplate, Map<String, Object> model )
    {
        model.put( "locale", Locale.FRANCE );
        Rights rights = new Rights( );
        rights.init( _request );
        model.put( "rights", rights );
        return AppTemplateService.getTemplate( strTemplate, getLocale( ), model ).getHtml( );
    }

    /**
     * 
     * @param idSecteur
     */
    protected boolean estAutoriseSecteur( HttpServletRequest request, String idSecteur )
    {
        // ici faire un appel à right 
        Rights right = new Rights( );
        right.init( request );
        Boolean isAuthorized = false;

        if ( right.estAutoriseSecteur( idSecteur ) )
        {
            isAuthorized = true;
        }

        return isAuthorized;
    }

}
