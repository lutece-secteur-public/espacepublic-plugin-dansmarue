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
package fr.paris.lutece.plugins.dansmarue.commons.dao;

public class PaginationPropertiesImpl implements PaginationProperties
{
    private int _nFirstResult;

    private int _nPageSize;

    private int _nItemsPerPage;

    private int _nPageIndex;

    /**
     * Creates a new PaginationPropertiesImpl.java object.
     */
    public PaginationPropertiesImpl( )
    {
        super( );
    }

    /**
     * Creates a new PaginationPropertiesImpl.java object.
     * 
     * @param firstResult
     *            the first result
     * @param pageSize
     *            the page size
     * @param itemsPerPage
     *            the number of item per page
     * @param pageIndex
     *            the page index
     */
    public PaginationPropertiesImpl( int firstResult, int pageSize, int itemsPerPage, int pageIndex )
    {
        super( );
        this._nFirstResult = firstResult;
        this._nPageSize = pageSize;
        this._nItemsPerPage = itemsPerPage;
        this._nPageIndex = pageIndex;
    }

    public int getFirstResult( )
    {
        return this._nFirstResult;
    }

    public int getPageSize( )
    {
        return this._nPageSize;
    }

    public int getItemsPerPage( )
    {
        return this._nItemsPerPage;
    }

    public int getPageIndex( )
    {
        return this._nPageIndex;
    }
}
