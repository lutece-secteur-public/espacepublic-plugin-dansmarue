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
package fr.paris.lutece.plugins.dansmarue.commons.exceptions;

import java.util.HashMap;
import java.util.Map;


/**
 * The Class FunctionnalException.
 */
public class FunctionnalException extends RuntimeException
{

    /** The Constant serialVersionUID. */
    private static final long   serialVersionUID = 7260516599116118558L;

    /** The bean. */
    private transient Object              _bean;

    /** The additional parameters. */
    private transient Map<String, Object> _additionalParameters;

    /**
     * Instantiates a new functionnal exception.
     *
     * @param bean the bean
     */
    public FunctionnalException( Object bean )
    {
        _bean = bean;
        setAdditionalParameters( new HashMap<String, Object>( ) );
    }

    /**
     * Instantiates a new functionnal exception.
     *
     * @param bean the bean
     * @param additionalParameters the additional parameters
     */
    public FunctionnalException( Object bean, Map<String, Object> additionalParameters )
    {
        _bean = bean;
        _additionalParameters = additionalParameters;
    }

    /**
     * Gets the bean.
     *
     * @return the bean
     */
    public Object getBean( )
    {
        return _bean;
    }

    /**
     * Sets the bean.
     *
     * @param bean            the bean to set
     */
    public void setBean( Object bean )
    {
        _bean = bean;
    }

    /**
     * Gets the additional parameters.
     *
     * @return the aditionnal parameters
     */
    public Map<String, Object> getAdditionalParameters( )
    {
        return _additionalParameters;
    }

    /**
     * Sets the additional parameters.
     *
     * @param additionnalParameters            the additional parameters to add
     */
    public void setAdditionalParameters( Map<String, Object> additionnalParameters )
    {
        _additionalParameters = additionnalParameters;
    }

}
