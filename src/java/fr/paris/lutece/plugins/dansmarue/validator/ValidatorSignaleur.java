/*
 * Copyright (c) 2002-2020, City of Paris
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
package fr.paris.lutece.plugins.dansmarue.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * The Class ValidatorSignaleur.
 */
public class ValidatorSignaleur
{

    /** The Constant ADRESSE_EMAIL_NULL. */
    private static final String ADRESSE_EMAIL_NULL = "#i18n{dansmarue.message.erreur.email.invalide}";

    /** The Constant PARAMETER_EMAIL. */
    private static final String PARAMETER_EMAIL    = "email";

    /**
     * Validate.
     *
     * @param request the request
     * @return the map
     */
    public Map<String, String> validate( HttpServletRequest request )
    {

        Map<String, String> errorList = new HashMap<>( );
        if ( ( request.getParameter( PARAMETER_EMAIL ) == null ) || StringUtils.isEmpty( PARAMETER_EMAIL ) )
        {

            boolean formatCodePostalOK = false;
            Pattern pattern = Pattern.compile( "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$" );
            Matcher matcher = pattern.matcher( request.getParameter( PARAMETER_EMAIL ) );
            while ( matcher.find( ) )
            {
                formatCodePostalOK = true;
            }

            if ( !formatCodePostalOK )
            {
                errorList.put( PARAMETER_EMAIL, ADRESSE_EMAIL_NULL );
            }
        }
        return errorList;
    }
}
