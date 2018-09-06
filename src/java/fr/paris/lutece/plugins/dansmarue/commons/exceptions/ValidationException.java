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
package fr.paris.lutece.plugins.dansmarue.commons.exceptions;

import fr.paris.lutece.portal.service.i18n.I18nService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.ConstraintViolation;

public class ValidationException extends FunctionnalException
{

    private static final long            serialVersionUID = 1L;

    public static final String           VALIDATION_ERROR = I18nService.getLocalizedString( "validation.erreur", Locale.getDefault( ) );
    private List<ConstraintViolation<?>> _constraintViolationList;

    public ValidationException( Object bean, ConstraintViolation<?> constraintViolation )
    {
        super( bean );
        _constraintViolationList = new ArrayList<ConstraintViolation<?>>( );
        _constraintViolationList.add( constraintViolation );
    }

    public ValidationException( Object bean )
    {
        super( bean );
        _constraintViolationList = new ArrayList<ConstraintViolation<?>>( );
    }

    /**
     * @return the constraintViolationList
     */
    public List<ConstraintViolation<?>> getConstraintViolationList( )
    {
        return _constraintViolationList;
    }

    /**
     * @param constraintViolationList
     *            the constraintViolationList to set
     */
    public void setConstraintViolationList( List<ConstraintViolation<?>> constraintViolationList )
    {
        this._constraintViolationList = constraintViolationList;
    }

    public void addConstraintViolation( ConstraintViolation<?> constraintViolation )
    {
        this._constraintViolationList.add( constraintViolation );
    }

    public String getMessage( )
    {
        StringBuilder sbMessage = new StringBuilder( );
        sbMessage.append( VALIDATION_ERROR );
        if ( _constraintViolationList != null )
        {
            for ( ConstraintViolation<?> constraintViolation : _constraintViolationList )
            {
                sbMessage.append( System.getProperty( "line.separator" ) ).append( "Valeur '" ).append( constraintViolation.getInvalidValue( ) ).append( "' incorrecte pour '" )
                        .append( constraintViolation.getPropertyPath( ) ).append( "' : " ).append( constraintViolation.getMessage( ) );
            }
        }
        return sbMessage.toString( );
    }

}
