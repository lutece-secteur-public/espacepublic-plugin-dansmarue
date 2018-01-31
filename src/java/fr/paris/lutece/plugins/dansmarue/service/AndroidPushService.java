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
package fr.paris.lutece.plugins.dansmarue.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import net.sf.json.JSONObject;

public class AndroidPushService
{
    private static final Logger LOGGER               = Logger.getLogger( AndroidPushService.class );

    // PROPERTIES
    private static final String PROPERTY_GCM_API_KEY = "signalement.gcm.api.key";
    private static final String PROPERTY_FCM_URL     = "signalement.fcm.url";

    // MARKS
    private static final String MARK_TO              = "to";
    private static final String MARK_BODY            = "body";
    private static final String MARK_TITLE           = "title";
    private static final String MARK_DATA            = "data";
    private static final String MARK_NOTIFICATION    = "notification";
    private static final String MARK_AUTHORIZATION   = "Authorization";

    public static void sendPush( String userToken, String title, String content, Map<String, String> payload )
    {
        String response = null;
        String gcmApiKey = AppPropertiesService.getProperty( PROPERTY_GCM_API_KEY );
        String fcmUrl = AppPropertiesService.getProperty( PROPERTY_FCM_URL );
        try
        {
            HttpAccess httpAccess = new HttpAccess( );

            JSONObject message = new JSONObject( );
            message.accumulate( MARK_TO, userToken );

            JSONObject notification = new JSONObject( );
            notification.accumulate( MARK_BODY, content );
            notification.accumulate( MARK_TITLE, title );

            message.accumulate( MARK_NOTIFICATION, notification );

            if ( null != payload )
            {
                JSONObject data = new JSONObject( );
                for ( Entry<String, String> payloadItem : payload.entrySet( ) )
                {
                    data.accumulate( payloadItem.getKey( ), payloadItem.getValue( ) );
                }
                message.accumulate( MARK_DATA, data );
            }

            Map<String, String> headersRequest = new HashMap<>( );
            headersRequest.put( MARK_AUTHORIZATION, "key=" + gcmApiKey );

            response = httpAccess.doPostJSON( fcmUrl, message.toString( ), headersRequest, null );

        } catch ( Exception ex )
        {
            LOGGER.error( "Erreur lors de l'appel au WS de push android", ex );
        }
    }

}
