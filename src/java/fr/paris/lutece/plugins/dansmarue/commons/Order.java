/*
 * Copyright (c) 2002-2022, City of Paris
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
package fr.paris.lutece.plugins.dansmarue.commons;

import java.io.Serializable;

/**
 * The Class Order.
 */
public class Order implements Serializable
{

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -887656883773949701L;

    /** The Constant DESC. */
    private static final String DESC = "DESC";

    /** The Constant ASC. */
    private static final String ASC = "ASC";

    /** The str name. */
    private String _strName;

    /** The str order. */
    // ASC or DESC
    private String _strOrder;

    /**
     * Instantiates a new order.
     */
    public Order( )
    {
        // Empty constructor for Json Mapping
    }

    /**
     * Instantiates a new order.
     *
     * @param name
     *            the name
     * @param order
     *            the order
     */
    public Order( String name, String order )
    {
        _strName = name;
        _strOrder = order;
    }

    /**
     * Instantiates a new order.
     *
     * @param name
     *            the name
     * @param bIsAsc
     *            the b is asc
     */
    public Order( String name, boolean bIsAsc )
    {
        _strName = name;
        if ( bIsAsc )
        {
            _strOrder = ASC;
        }
        else
        {
            _strOrder = DESC;
        }
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName( )
    {
        return _strName;
    }

    /**
     * Sets the name.
     *
     * @param pName
     *            the name to set
     */
    public void setName( String pName )
    {
        _strName = pName;
    }

    /**
     * Gets the order.
     *
     * @return the order
     */
    public String getOrder( )
    {
        return _strOrder;
    }

    /**
     * Sets the order.
     *
     * @param pOrder
     *            the order to set
     */
    public void setOrder( String pOrder )
    {
        _strOrder = pOrder;
    }

}
