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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;

import org.apache.commons.lang.StringUtils;

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
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.web.util.LocalizedDelegatePaginator;
import fr.paris.lutece.util.html.AbstractPaginator;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * Abstract class for jsp bean.
 */
public class AbstractJspBean extends MVCAdminJspBean
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6753665775376332934L;

    /** The str current page index. */
    protected String _strCurrentPageIndex = "";

    /** The n items per page. */
    protected int _nItemsPerPage;

    /** The n default items per page. */
    public final int N_DEFAULT_ITEMS_PER_PAGE = AppPropertiesService.getPropertyInt( SignalementConstants.PROPERTY_DEFAULT_ITEM_PER_PAGE, 20 );

    /** The Constant MARK_FILTER. */
    protected static final String MARK_FILTER = "filter";

    /** The Constant URL_JSP_MANAGE_SIGNALEMENTS. */
    public static final String URL_JSP_MANAGE_SIGNALEMENTS = "jsp/admin/plugins/signalement/ManageSignalement.jsp";

    /** The Constant MARK_PAGINATOR. */
    protected static final String MARK_PAGINATOR = "paginator";

    /** The Constant MARK_NB_ITEMS_PER_PAGE. */
    protected static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";

    /** The request. */
    private transient HttpServletRequest _request;

    /** The Constant PARAMETER_INT_PAGE_INDEX_ONE. */
    // PARAMETERS
    private static final int PARAMETER_INT_PAGE_INDEX_ONE = 1;

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
     * @see fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean#init(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    @Override
    public void init( HttpServletRequest request, String strRight ) throws AccessDeniedException
    {
        super.init( request, strRight );
        _request = request;
    }

    /**
     * Return a paginator for the view using parameter in http request.
     *
     * @param <T>
     *            This is the type parameter
     * @param request
     *            http request
     * @param list
     *            bean list to paginate
     * @param jspUrl
     *            the jsp Url
     * @param totalResult
     *            the total result
     * @return paginator
     */
    protected <T> LocalizedDelegatePaginator<T> getPaginator( HttpServletRequest request, ResultList<T> list, String jspUrl, int totalResult )
    {
        _strCurrentPageIndex = AbstractPaginator.getPageIndex( request, AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        int nCurrentPageIndex = PARAMETER_INT_PAGE_INDEX_ONE;

        if ( StringUtils.isNotEmpty( _strCurrentPageIndex ) )
        {
            nCurrentPageIndex = Integer.valueOf( _strCurrentPageIndex );
        }

        _nItemsPerPage = AbstractPaginator.getItemsPerPage( request, AbstractPaginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, N_DEFAULT_ITEMS_PER_PAGE );

        while ( ( ( nCurrentPageIndex - 1 ) * _nItemsPerPage ) > totalResult )
        {
            _strCurrentPageIndex = String.valueOf( nCurrentPageIndex - 1 );
            nCurrentPageIndex = Integer.valueOf( _strCurrentPageIndex );
        }

        StringBuilder strBaseUrl = new StringBuilder( );

        strBaseUrl.append( AppPathService.getBaseUrl( request ).toString( ) );
        strBaseUrl.append( jspUrl );

        return new LocalizedDelegatePaginator<T>( list, _nItemsPerPage, strBaseUrl.toString( ), AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex,
                totalResult, getLocale( ) );

    }

    /**
     * Init the jspBean.
     *
     * @param request
     *            the HTTpServletRequest
     * @param strRight
     *            the right
     * @param keyResourceType
     *            the resource type
     * @param permission
     *            the permission
     * @throws AccessDeniedException
     *             Throws AccessDeniedException
     */
    public void init( HttpServletRequest request, String strRight, String keyResourceType, String permission ) throws AccessDeniedException
    {
        this.init( request, strRight );
        if ( !RBACService.isAuthorized( keyResourceType, RBAC.WILDCARD_RESOURCES_ID, permission, getUser( ) ) )
        {
            throw new AccessDeniedException( MessageFormat.format( "Acces denied for {0} / {1}.", keyResourceType, permission ) );
        }
    }

    /**
     * Returns a bean for pagination in service/dao using parameter in http request.
     *
     * @param request
     *            http request
     * @param totalResult
     *            the total result
     * @return paginator
     */
    protected PaginationProperties getPaginationProperties( HttpServletRequest request, Integer totalResult )
    {
        String strPageIndex = "";

        strPageIndex = AbstractPaginator.getPageIndex( request, AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        int nCurrentPageIndex = PARAMETER_INT_PAGE_INDEX_ONE;

        if ( StringUtils.isNotEmpty( strPageIndex ) )
        {
            nCurrentPageIndex = Integer.valueOf( strPageIndex );
        }

        int nItemsPerPage = AbstractPaginator.getItemsPerPage( request, AbstractPaginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, N_DEFAULT_ITEMS_PER_PAGE );

        while ( ( ( nCurrentPageIndex - 1 ) * nItemsPerPage ) > totalResult )
        {
            nCurrentPageIndex = nCurrentPageIndex - 1;
            _strCurrentPageIndex = Integer.toString( nCurrentPageIndex );
        }

        return new PaginationPropertiesImpl( ( nCurrentPageIndex - 1 ) * nItemsPerPage, nItemsPerPage, nItemsPerPage, nCurrentPageIndex );
    }

    /**
     * Return localized message.
     *
     * @param key
     *            i18n key
     * @return localized message
     */
    protected String getMessage( String key )
    {
        return I18nService.getLocalizedString( key, getLocale( ) );
    }

    /**
     * Return localized message with args.
     *
     * @param key
     *            i18n key
     * @param args
     *            args
     * @return localized message
     */
    protected String getMessage( String key, String... args )
    {
        return I18nService.getLocalizedString( key, args, getLocale( ) );
    }

    /**
     * Get validation error from session and remove from it.
     *
     * @param request
     *            http request
     * @return validation exception
     */
    protected FunctionnalException getErrorOnce( HttpServletRequest request )
    {
        FunctionnalException fe = (FunctionnalException) request.getSession( ).getAttribute( SignalementConstants.PARAMETER_ERROR );
        if ( fe != null )
        {
            request.getSession( ).removeAttribute( SignalementConstants.PARAMETER_ERROR );
        }
        return fe;
    }

    /**
     * Return html code for error message.
     *
     * @param e
     *            validation exception
     * @return html
     */
    protected String getHtmlError( FunctionnalException e )
    {
        Map<String, Object> model = new HashMap<>( );
        List<String> messageList = new ArrayList<>( );
        try
        {
            throw e;
        }
        catch( ValidationException ve )
        {
            String typeName = ve.getBean( ).getClass( ).getSimpleName( );

            // Add a validation error message using value, field name and provided message
            for ( ConstraintViolation<?> constraintViolation : ve.getConstraintViolationList( ) )
            {
                String fieldName = getMessage( "dansmarue.field." + typeName + "." + constraintViolation.getPropertyPath( ) );
                messageList.add( getMessage( "dansmarue.validation.error", String.valueOf( constraintViolation.getInvalidValue( ) ), fieldName,
                        constraintViolation.getMessage( ) ) );
            }

            AppLogService.error( ve.getMessage( ), ve );

        }
        catch( BusinessException be )
        {
            messageList.add( getMessage( be.getCode( ), be.getArguments( ) ) );
            AppLogService.error( be.getMessage( ), be );
        }

        model.put( "messageList", messageList );

        HtmlTemplate template = AppTemplateService.getTemplate( "admin/plugins/signalement/error.html", getLocale( ), model );
        return template.getHtml( );
    }

    /**
     * Manage functionnal exception.
     *
     * @param request
     *            the request
     * @param e
     *            the e
     * @param targetUrl
     *            the target url
     * @return the string
     */
    protected String manageFunctionnalException( HttpServletRequest request, FunctionnalException e, String targetUrl )
    {

        request.getSession( ).setAttribute( SignalementConstants.PARAMETER_ERROR, e );
        return targetUrl;
    }

    /**
     * Generates an html page.
     *
     * @param strTemplate
     *            template file path.
     * @param model
     *            Model map.
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
     * Check if the sector is allowed.
     *
     * @param request
     *            the HttpServletRequest
     * @param idSecteur
     *            the sector id
     * @return true if the sector is allowed
     */
    protected boolean estAutoriseSecteur( HttpServletRequest request, String idSecteur )
    {
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
