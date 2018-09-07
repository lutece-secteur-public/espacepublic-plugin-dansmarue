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
package fr.paris.lutece.plugins.dansmarue.service;

import java.io.UnsupportedEncodingException;

import net.sf.json.JSONObject;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;

public interface ISignalementWebService
{

    /**
     * Send signalement to the correct recipient, with Web Service
     * 
     * @param signalement
     *            the signalement to send
     * @param url
     *            the ws url
     * @return the return of the call, fail or success
     * @throws UnsupportedEncodingException
     * @throws BusinessException
     */
    String sendByWS( Signalement signalement, String url ) throws UnsupportedEncodingException;

    /**
     * Get the JSON reponse
     * 
     * @param signalement
     *            the report object
     * @param url
     *            the url to call
     * @return the response JSON object
     */
    JSONObject getJSONResponse( Signalement signalement, String url );

    /**
     * convert signalement into json formated data
     * 
     * @param signalement
     *            the signalement
     * @return the json
     * @throws UnsupportedEncodingException
     *             the exception
     */
    JSONObject createJSON( Signalement signalement ) throws UnsupportedEncodingException;

    /**
     * Send notification service done to the correct recipient, with Web Service
     * 
     * @param signalement
     *            the signalement to send
     * @param urlPartner
     *            the ws url
     * @return the return of the call, fail or success
     */
    JSONObject callWSPartnerServiceDone( Signalement signalement, String urlPartner );
}
