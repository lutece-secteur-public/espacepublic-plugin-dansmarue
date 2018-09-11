/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementWebService;
import fr.paris.lutece.plugins.dansmarue.utils.ws.IWebServiceCaller;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.signrequest.RequestAuthenticator;

public class SignalementWebService implements ISignalementWebService
{

    // JSON TAG
    public static final String   REQUEST_METHOD_ADD  = "addAnomalie";
    public static final String   REQUEST_METHOD_DONE = "serviceDoneAnomalie";

    public static final String   JSON_TAG_ANOMALIE   = "anomalie";
    public static final String   JSON_TAG_UDID       = "udid";
    public static final String   JSON_TAG_EMAIL      = "email";
    public static final String   JSON_TAG_PHOTOS     = "photos";

    private static final String  TAG_REQUEST         = "request";

    @Inject
    private IWebServiceCaller    _wsCaller;

    @Inject
    @Named( "rest.requestAuthenticator" )
    private RequestAuthenticator _authenticator;

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject getJSONResponse( Signalement signalement, String url )
    {
        JSONObject response = null;

        try
        {
            String strResp = sendByWS( signalement, url );

            JSONArray array = JSONArray.fromObject( strResp );
            response = array.getJSONObject( 0 );
        } catch ( BusinessException e )
        {
            AppLogService.error( e.getMessage( ), e );
            response = new JSONObject( );
            response.accumulate( TAG_REQUEST, REQUEST_METHOD_ADD );
            JSONObject error = new JSONObject( );
            error.accumulate( "error", "Error when contacting " + url );
            response.accumulate( "answer", error );
        } catch ( UnsupportedEncodingException e )
        {
            AppLogService.error( e.getMessage( ), e );
            response = new JSONObject( );
            response.accumulate( TAG_REQUEST, REQUEST_METHOD_ADD );
            JSONObject error = new JSONObject( );
            error.accumulate( "error", "Encoding error" );
            response.accumulate( "answer", error );
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String sendByWS( Signalement signalement, String url ) throws UnsupportedEncodingException
    {
        String result = null;

        if ( signalement == null )
        {
            throw new BusinessException( signalement, "dansmarue.ws.error.url.empty" );
        }

        JSONObject json = createJSON( signalement );

        JSONObject jsonSrc = new JSONObject( );
        jsonSrc.accumulate( JSON_TAG_ANOMALIE, json );

        // name of the method in REST api
        jsonSrc.accumulate( TAG_REQUEST, REQUEST_METHOD_ADD );

        Map<String, List<String>> params = new HashMap<String, List<String>>( );
        List<String> values = new ArrayList<String>( );

        String jsonFormated = jsonSrc.toString( );
        values.add( jsonFormated );
        params.put( "jsonStream", values );

        try
        {
            AppLogService.info( "Call web service " + url + " for id anomalie : " + signalement.getId( ) );
            AppLogService.info( "Flux Json : " + jsonFormated );
            result = _wsCaller.callWebService( url, params, _authenticator, values );
        } catch ( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
            throw new BusinessException( signalement, "dansmarue.ws.error.url.connexion" );
        }

        AppLogService.info( "Web service response for id anomalie : " + signalement.getId( ) + " is : " + result );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject createJSON( Signalement signalement ) throws UnsupportedEncodingException
    {
        // content of the json stream must be encode, because using application/x-www-form-urlencoded

        JSONObject jsonAnomalie = new JSONObject( );
        jsonAnomalie.accumulate( "id", signalement.getId( ) );
        jsonAnomalie.accumulate( "reference", signalement.getNumeroSignalement( ) );
        jsonAnomalie.accumulate( "date_creation", signalement.getDateCreation( ) );
        jsonAnomalie.accumulate( "commentaire", encode( signalement.getCommentaire( ) ) );
        jsonAnomalie.accumulate( "type", encode( signalement.getTypeSignalement( ).getLibelle( ) ) );
        jsonAnomalie.accumulate( "priorite", encode( signalement.getPriorite( ).getLibelle( ) ) );
        jsonAnomalie.accumulate( "adresse", encode( signalement.getAdresses( ).get( 0 ).getAdresse( ) ) );
        jsonAnomalie.accumulate( "lat", signalement.getAdresses( ).get( 0 ).getLat( ) );
        jsonAnomalie.accumulate( "lng", signalement.getAdresses( ).get( 0 ).getLng( ) );
        jsonAnomalie.accumulate( "token", signalement.getToken( ) );

        List<PhotoDMR> photos = signalement.getPhotos( );

        JSONArray array = new JSONArray( );
        if ( photos != null && !photos.isEmpty( ) )
        {
            for ( PhotoDMR p : photos )
            {
                JSONObject photoJson = new JSONObject( );
                photoJson.accumulate( "id_photo", p.getId( ) );
                photoJson.accumulate( "vue_photo", p.getVue( ) );
                photoJson.accumulate( "photo", getImageBase64( p.getImage( ) ) );
                array.add( photoJson );
            }
            jsonAnomalie.accumulate( JSON_TAG_PHOTOS, array );
        } else
        {
            jsonAnomalie.accumulate( JSON_TAG_PHOTOS, array );
        }

        return jsonAnomalie;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject callWSPartnerServiceDone( Signalement signalement, String urlPartner )
    {

        JSONObject response = null;
        String result = null;

        Map<String, List<String>> params = new HashMap<String, List<String>>( );
        List<String> values = new ArrayList<String>( );

        JSONObject jsonSrc = new JSONObject( );
        jsonSrc.accumulate( TAG_REQUEST, REQUEST_METHOD_DONE );
        jsonSrc.accumulate( "id", signalement.getId( ) );
        jsonSrc.accumulate( "reference", signalement.getNumeroSignalement( ) );
        jsonSrc.accumulate( "token", signalement.getToken( ) );
        jsonSrc.accumulate( "date_creation", signalement.getDateCreation( ) );
        jsonSrc.accumulate( "date_service_fait", signalement.getDateServiceFaitTraitement( ) );

        String jsonFormated = jsonSrc.toString( );
        values.add( jsonFormated );
        params.put( "jsonStream", values );

        try
        {
            AppLogService.info( "Call web service PartnerServiceDone " + urlPartner + " for id anomalie : " + signalement.getId( ) );
            result = _wsCaller.callWebService( urlPartner, params, _authenticator, values );

            JSONArray array = JSONArray.fromObject( result );
            response = array.getJSONObject( 0 );

        } catch ( HttpAccessException e )
        {
            AppLogService.error( e.getMessage( ), e );
            throw new BusinessException( signalement.getId( ), "dansmarue.ws.error.url.connexion" );
        }

        AppLogService.info( "Web service PartnerServiceDone response for id anomalie : " + signalement.getId( ) + " is : " + result );
        return response;
    }

    /**
     * Encode report data before create JSON
     * 
     * @param data
     *            the signalement data
     * @return the encoded data
     * 
     * @throws UnsupportedEncodingException
     *             when charset cannot be use
     */
    private String encode( String data ) throws UnsupportedEncodingException
    {
        return StringUtils.isNotBlank( data ) ? URLEncoder.encode( data, CharEncoding.UTF_8 ) : StringUtils.EMPTY;
    }

    /**
     * Encode Image for WebService transport.
     * 
     * @param image
     *            the image object
     * @return Encode Image
     */
    private String getImageBase64( ImageResource image )
    {
        String dataImg = "";
        if ( image != null && image.getImage( ) != null )
        {
            Base64 codec = new Base64( );
            String data = new String( codec.encode( image.getImage( ) ) );
            String mimeType = ( image.getMimeType( ) == null ) ? "data:image/jpg;base64," : "data:" + image.getMimeType( ) + ";base64,";
            dataImg = mimeType + data;
        }

        return dataImg;
    }
}