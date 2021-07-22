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
package fr.paris.lutece.plugins.dansmarue.commons.dao;

/**
 * The Class PaginationPropertiesImpl.
 */
public class PaginationPropertiesImpl implements PaginationProperties
{

    /** The n first result. */
    private int _nFirstResult;

    /** The n page size. */
    private int _nPageSize;

    /** The n items per page. */
    private int _nItemsPerPage;

    /** The n page index. */
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
        _nFirstResult = firstResult;
        _nPageSize = pageSize;
        _nItemsPerPage = itemsPerPage;
        _nPageIndex = pageIndex;
    }

    /**
     * Gets the first result.
     *
     * @return the first result
     */
    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties#getFirstResult()
     */
    @Override
    public int getFirstResult( )
    {
        return _nFirstResult;
    }

    /**
     * Gets the page size.
     *
     * @return the page size
     */
    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties#getPageSize()
     */
    @Override
    public int getPageSize( )
    {
        return _nPageSize;
    }

    /**
     * Gets the items per page.
     *
     * @return the items per page
     */
    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties#getItemsPerPage()
     */
    @Override
    public int getItemsPerPage( )
    {
        return _nItemsPerPage;
    }

    /**
     * Gets the page index.
     *
     * @return the page index
     */
    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties#getPageIndex()
     */
    @Override
    public int getPageIndex( )
    {
        return _nPageIndex;
    }
}
