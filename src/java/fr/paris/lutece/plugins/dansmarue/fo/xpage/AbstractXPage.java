/*
*
*  * Copyright (c) 2002-2017, Mairie de Paris
*  * All rights reserved.
*  *
*  * Redistribution and use in source and binary forms, with or without
*  * modification, are permitted provided that the following conditions
*  * are met:
*  *
*  *  1. Redistributions of source code must retain the above copyright notice
*  *     and the following disclaimer.
*  *
*  *  2. Redistributions in binary form must reproduce the above copyright notice
*  *     and the following disclaimer in the documentation and/or other materials
*  *     provided with the distribution.
*  *
*  *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
*  *     contributors may be used to endorse or promote products derived from
*  *     this software without specific prior written permission.
*  *
*  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
*  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
*  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
*  * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
*  * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
*  * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
*  * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
*  * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
*  * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
*  * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
*  * POSSIBILITY OF SUCH DAMAGE.
*  *
*  * License 1.0
*
*/
package fr.paris.lutece.plugins.dansmarue.fo.xpage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.IntegerConverter;

import fr.paris.lutece.plugins.dansmarue.service.impl.PrioriteService;
import fr.paris.lutece.plugins.dansmarue.service.impl.SignalementService;
import fr.paris.lutece.plugins.dansmarue.service.impl.SignalementSuiviService;
import fr.paris.lutece.plugins.dansmarue.service.impl.TypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.upload.handler.DansMaRueUploadHandler;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.util.mvc.utils.MVCMessage;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.ErrorMessage;

public class AbstractXPage extends MVCApplication
{

    private static final long                      serialVersionUID        = 1723850575195580846L;
    private static final String                    PARAMETER_TOKEN         = "token";
    // PAGES
    protected static final String                  PAGE_PORTAL             = "Portal.jsp?page=";
    protected static final String                  PAGE_PORTAL_FULL        = "jsp/site/Portal.jsp?page=";

    // XPAGE NAMES
    protected static final String                  XPAGE_DANSMARUE         = "dansmarue";
    protected static final String                  XPAGE_DOUBLONS          = "doublons";
    public static final String                     XPAGE_SUIVI             = "suivi";

    // URLS
    protected static final String                  URL_ACCUEIL             = "jsp/site/Portal.jsp?page=accueil";

    // MARKS
    protected static final String                  MARK_BASE_URL           = "base_url";
    protected static final String                  MARK_WRAPPER_WARNINGS   = "wrapper_warnings";
    protected static final String                  MARK_MAP_ERRORS         = "map_errors";
    protected static final String                  MARK_ERRORS             = "errors";

    protected static final SignalementService      signalementService      = SpringContextService.getBean( "signalementService" );
    protected static final TypeSignalementService  typeSignalementService  = SpringContextService.getBean( "typeSignalementService" );
    protected static final PrioriteService         prioriteService         = SpringContextService.getBean( "prioriteService" );
    protected static final DansMaRueUploadHandler  dansmarueUploadHandler  = SpringContextService.getBean( "dansmarueUploadHandler" );
    protected static final SignalementSuiviService signalementSuiviService = SpringContextService.getBean( "signalementSuiviService" );

    private Map<String, List<ErrorMessage>>        mapErrors               = new HashMap<>( );

    /**
     * Default constructor
     */
    public AbstractXPage( )
    {
        super( );

        // Converter Registration
        ConvertUtilsBean convertUtilsBean = BeanUtilsBean.getInstance( ).getConvertUtils( );

        // Re-definition of the Integer converter to have null instead of 0 if the value does not exist
        convertUtilsBean.register( new IntegerConverter( null ), Integer.class );
    }

    /**
     * Redirect home
     *
     * @param request
     *            {@link HttpServletRequest}
     * @return {@link XPage}
     */
    public XPage redirectHome( final HttpServletRequest request )
    {
        return redirect( request, PAGE_PORTAL + XPAGE_DANSMARUE );
    }

    /**
     * Return localized message.
     *
     * @param key
     *            i18n key
     * @param request
     *            the request
     * @return localized message
     */
    protected String getMessage( String key, HttpServletRequest request )
    {
        return I18nService.getLocalizedString( key, request.getLocale( ) );
    }

    /**
     * Return localized message.
     *
     * @param key
     *            i18n key
     * @param request
     *            the request
     * @param arg
     *            {@link Object} Arguments
     * @return localized message
     */
    protected String getMessageWithParam( String key, HttpServletRequest request, Object[] arg )
    {
        return I18nService.getLocalizedString( key, arg, request.getLocale( ) );
    }

    /**
     * @param request
     *            http request
     * @param token
     *            {@link String} token
     */
    protected void setToken( final HttpServletRequest request, final String token )
    {
        request.getSession( ).setAttribute( PARAMETER_TOKEN, token );
    }

    /**
     * @param request
     *            http request
     * @return {@link String} the token
     */
    protected String getToken( final HttpServletRequest request )
    {
        return ( String ) request.getSession( ).getAttribute( PARAMETER_TOKEN );
    }

    /**
     * @param request
     *            http request
     */
    protected void removeToken( final HttpServletRequest request )
    {
        request.getSession( ).removeAttribute( PARAMETER_TOKEN );
    }

    /**
     * Return authified user throw exception if requestAuthent=true null otherwise.
     *
     * @param request
     *            http request
     * @return user lutece
     * @throws UserNotSignedException
     *             {@link UserNotSignedException}
     */
    protected LuteceUser getUser( HttpServletRequest request ) throws UserNotSignedException
    {
        return getUser( request, true );
    }

    /**
     * Return authified user throw exception if requestAuthent=true null otherwise.
     *
     * @param request
     *            http request
     * @param requestAuthent
     *            {@link Boolean}
     * @return user lutece
     * @throws UserNotSignedException
     *             if the user is not logged in
     */
    protected LuteceUser getUser( HttpServletRequest request, boolean requestAuthent ) throws UserNotSignedException
    {
        LuteceUser user = null;
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            user = SecurityService.getInstance( ).getRemoteUser( request );
            if ( requestAuthent && ( user == null ) )
            {
                throw new UserNotSignedException( );
            }
        }
        return user;

    }

    /**
     * Add an error message. The error message must NOT be an I18n key.
     *
     * @param strField
     *            The field for which there is an error
     * @param strMessage
     *            The message
     */
    protected void addError( String strField, String strMessage )
    {
        MVCMessage mvcMessage = new MVCMessage( strMessage );
        List<ErrorMessage> errorMessages;

        if ( mapErrors.containsKey( strField ) )
        {
            errorMessages = mapErrors.get( strField );
        } else
        {
            errorMessages = new ArrayList<>( );
            mapErrors.put( strField, errorMessages );
        }

        errorMessages.add( mvcMessage );
        super.addError( strMessage );
    }

    /**
     * Add an error message. The error message must be an I18n key.
     *
     * @param strField
     *            The field for which there is an error
     * @param strMessageKey
     *            The message
     * @param locale
     *            The locale to display the message in
     */
    protected void addError( String strField, String strMessageKey, Locale locale )
    {
        addError( strField, I18nService.getLocalizedString( strMessageKey, locale ) );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void fillCommons( Map<String, Object> model )
    {
        super.fillCommons( model );
        Map<String, List<ErrorMessage>> mapErrorFieldsCopy = new HashMap<>( mapErrors );
        model.put( MARK_MAP_ERRORS, mapErrorFieldsCopy );
        mapErrors.clear( );

    }
}
