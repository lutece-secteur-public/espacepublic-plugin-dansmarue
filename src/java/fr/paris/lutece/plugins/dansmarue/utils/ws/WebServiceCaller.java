/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.plugins.dansmarue.utils.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.signrequest.NoSecurityAuthenticator;
import fr.paris.lutece.util.signrequest.RequestAuthenticator;
import fr.paris.lutece.util.signrequest.RequestHashAuthenticator;


/**
 * WebServiceCaller
 */
public class WebServiceCaller implements IWebServiceCaller
{
    private static final String MARK_JSON_STREAM = "jsonStream";
    private static final String PARAMETER_TYPE_FORM = "application/x-www-form-urlencoded";
    private static final String MARK_CONTENT_TYPE = "Content-Type";
    private static final String PARAMETER_METHOD = "POST";
    private static final String PARAMETER_LINE_SEPARATOR = System.getProperty( "line.separator" );
    private static final String PROPERTY_TRACE_ENABLED = "signalement.trace.webservices.calls.enabled";
    private static final String PROPERTY_AUTH_SECRET = "signalement.trace.webservices.secret";

    private static Logger LOGGER = Logger.getLogger( WebServiceCaller.class );

    @Override
    public String postJSON( String strUrl, JSONObject json ) throws MalformedURLException, IOException
    {
        LOGGER.debug( "Send to " + strUrl + " : " + json.toString( ) );
        URL url = new URL( strUrl );
        HttpURLConnection conn = (HttpURLConnection) url.openConnection( );
        conn.setDoOutput( true );
        conn.setRequestMethod( PARAMETER_METHOD );
        conn.setRequestProperty( MARK_CONTENT_TYPE, PARAMETER_TYPE_FORM );

        OutputStreamWriter os = new OutputStreamWriter( conn.getOutputStream( ) );
        os.write( wrapJsonToPostData( json ) );
        os.flush( );

        if ( conn.getResponseCode( ) != HttpURLConnection.HTTP_OK )
        {
            LOGGER.error( "Connection response code : " + conn.getResponseCode( ) );
            throw new RuntimeException( "Failed : HTTP error code : " + conn.getResponseCode( ) );
        }

        BufferedReader br = new BufferedReader( new InputStreamReader( ( conn.getInputStream( ) ) ) );

        StringBuilder strBuilder = new StringBuilder( );
        String output;
        while ( ( output = br.readLine( ) ) != null )
        {
            strBuilder.append( output );
        }

        conn.disconnect( );

        return strBuilder.toString( );
    }

    /**
     * Enclose json data with param name use by REST api
     * @param json the json object to send
     * @return the post data
     */
    private String wrapJsonToPostData( JSONObject json )
    {
        return MARK_JSON_STREAM + "=[" + json.toString( ) + "]";
    }

    @Override
    public String callWebService( String strUrl, Map<String, List<String>> params, RequestAuthenticator authenticator,
            List<String> listElements ) throws HttpAccessException
    {
        String strResponse = StringUtils.EMPTY;

        HttpAccess httpAccess = new HttpAccess( );
        strResponse = httpAccess.doPostMultiValues( strUrl, params, authenticator, listElements );

        return strResponse;
    }

    /**
     * Ask the properties service to know if trace is enabled
     * @return boolean if enable, false otherwise
     */
    public boolean isTraceEnabled( )
    {
        return StringUtils.isNotBlank( AppPropertiesService.getProperty( PROPERTY_TRACE_ENABLED ) );
    }

    /**
     * Trace the web service call
     * @param strUrl The WS URI
     * @param mapParameters The parameters
     * @param authenticator The Request Authenticator
     * @param listElements The list of elements to use to build the signature
     * @return The trace
     */
    protected String trace( String strUrl, Map<String, List<String>> mapParameters, RequestAuthenticator authenticator,
            List<String> listElements )
    {
        StringBuilder sbTrace = new StringBuilder( );
        sbTrace.append( PARAMETER_LINE_SEPARATOR + " ---------------------- WebService Call -------------------" );
        sbTrace.append( PARAMETER_LINE_SEPARATOR + "WebService URL : " ).append( strUrl );
        sbTrace.append( PARAMETER_LINE_SEPARATOR + "Parameters : " );

        Iterator<Entry<String, List<String>>> i = mapParameters.entrySet( ).iterator( );

        while ( i.hasNext( ) )
        {
            Entry<String, List<String>> entry = i.next( );
            String strParameter = entry.getKey( );
            List<String> values = entry.getValue( );

            for ( String strValue : values )
            {
                sbTrace.append( PARAMETER_LINE_SEPARATOR + "   " ).append( strParameter ).append( ":" )
                        .append( strValue );
            }
        }

        sbTrace.append( PARAMETER_LINE_SEPARATOR + "Security : " );
        sbTrace.append( PARAMETER_LINE_SEPARATOR + "Values used for the request signature : " );

        for ( String strValue : listElements )
        {
            sbTrace.append( PARAMETER_LINE_SEPARATOR + "   " ).append( strValue );
        }

        if ( authenticator instanceof RequestHashAuthenticator )
        {
            RequestHashAuthenticator auth = (RequestHashAuthenticator) authenticator;
            String strTimestamp = "" + new Date( ).getTime( );
            String strSecret = AppPropertiesService.getProperty(PROPERTY_AUTH_SECRET);
            String strSignature = auth.buildSignature( listElements, strTimestamp, strSecret);
            sbTrace.append( PARAMETER_LINE_SEPARATOR + " Request Authenticator : RequestHashAuthenticator" );
            sbTrace.append( PARAMETER_LINE_SEPARATOR + " Timestamp sample : " ).append( strTimestamp );
            sbTrace.append( PARAMETER_LINE_SEPARATOR + " Signature for this timestamp : " ).append( strSignature );
        }
        else if ( authenticator instanceof NoSecurityAuthenticator )
        {
            sbTrace.append( PARAMETER_LINE_SEPARATOR + " No request authentification" );
        }
        else
        {
            sbTrace.append( PARAMETER_LINE_SEPARATOR + " Unknown Request authenticator" );
        }

        sbTrace.append( PARAMETER_LINE_SEPARATOR
                + " --------------------------------------------------------------------" );

        return sbTrace.toString( );
    }
}
