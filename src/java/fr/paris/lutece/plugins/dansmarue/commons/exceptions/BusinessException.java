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
package fr.paris.lutece.plugins.dansmarue.commons.exceptions;

import java.util.Locale;
import java.util.Map;

import fr.paris.lutece.portal.service.i18n.I18nService;

/**
 * The Class BusinessException.
 */
public class BusinessException extends FunctionnalException
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -615983331551016543L;

    /** The str code. */
    private String _strCode;

    /** The str arguments. */
    private String [ ] _strArguments;

    /**
     * Exception constructor.
     *
     * @param bean
     *            the bean object
     * @param code
     *            Exception code
     */
    public BusinessException( Object bean, String code )
    {
        super( bean );
        _strCode = code;
    }

    /**
     * Exception constructor.
     *
     * @param bean
     *            the bean object
     * @param additionalParameters
     *            Additional Exception Parameters
     * @param code
     *            Exception code
     */
    public BusinessException( Object bean, Map<String, Object> additionalParameters, String code )
    {
        super( bean, additionalParameters );
        _strCode = code;
    }

    /**
     * Exception constructor.
     *
     * @param bean
     *            the bean object
     * @param additionalParameters
     *            Additional Exception Parameters
     * @param code
     *            Exception code
     * @param arguments
     *            Exception arguments
     */
    public BusinessException( Object bean, Map<String, Object> additionalParameters, String code, String... arguments )
    {
        super( bean, additionalParameters );
        _strCode = code;
    }

    /**
     * Exception constructor.
     *
     * @param bean
     *            the bean object
     * @param code
     *            Exception code
     * @param arguments
     *            Exception arguments
     */
    public BusinessException( Object bean, String code, String... arguments )
    {
        super( bean );
        _strCode = code;
        _strArguments = arguments;
    }

    /**
     * Get the exception code.
     *
     * @return Exception code
     */
    public String getCode( )
    {
        return _strCode;
    }

    /**
     * Initialize the exception code.
     *
     * @param code
     *            Exception code
     */
    public void setCode( String code )
    {
        _strCode = code;
    }

    /**
     * Get the exception arguments.
     *
     * @return exception arguments
     */
    public String [ ] getArguments( )
    {
        return _strArguments;
    }

    /**
     * Initialize the exception arguments.
     *
     * @param arguments
     *            exception arguments
     */
    public void setArguments( String [ ] arguments )
    {
        _strArguments = arguments;
    }

    /**
     * Error message builder from a {@link BusinessException}.
     *
     * @return The {@link String} array containing the i18n keys of the error messages
     */
    @Override
    public String getMessage( )
    {
        String errorMessage = "";
        if ( ( getArguments( ) != null ) && ( getArguments( ).length > 0 ) )
        {
            errorMessage = I18nService.getLocalizedString( getCode( ), getArguments( ), Locale.getDefault( ) );
        }
        else
        {
            errorMessage = I18nService.getLocalizedString( getCode( ), Locale.getDefault( ) );
        }

        return errorMessage;
    }
}
