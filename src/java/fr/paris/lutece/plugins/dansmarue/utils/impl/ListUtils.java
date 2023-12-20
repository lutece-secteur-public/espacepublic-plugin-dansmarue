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
package fr.paris.lutece.plugins.dansmarue.utils.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import fr.paris.lutece.plugins.dansmarue.utils.IListUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

/**
 * ListUtils.
 */
public final class ListUtils implements IListUtils
{

    /** The Constant ERROR_LIST_CREATION. */
    private final String ERROR_LIST_CREATION = "Error when creating a combo list: ";

    /** The Constant PROPERTY_LIST_SEPARATOR. */
    private final String PROPERTY_LIST_SEPARATOR = ";";

    /** The Constant PROPERTY_LIST_COMA. */
    private final String PROPERTY_LIST_COMA = ",";

    /**
     * Instantiates a new list utils.
     */
    private ListUtils( )
    {
        // does nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getPropertyList( String propertyKey )
    {
        String property = AppPropertiesService.getProperty( propertyKey );
        if ( property != null )
        {
            String [ ] items = property.split( PROPERTY_LIST_SEPARATOR );
            if ( items != null )
            {
                return Arrays.asList( items );
            }
        }
        return Collections.emptyList( );
    }

    /**
     * {@inheritDoc}
     */
    public ReferenceList toReferenceList( List<?> list, String key, String value, String firstItem )
    {
        return toReferenceList( list, key, value, firstItem, false );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList toReferenceList( List<?> list, String key, String value, String firstItem, boolean sort )
    {
        ReferenceList referenceList = new ReferenceList( );
        String valeurKey;
        String valeurValue;

        try
        {
            if ( firstItem != null )
            {
                referenceList.addItem( "-1", firstItem );
            }

            for ( Object element : list )
            {
                valeurKey = BeanUtils.getSimpleProperty( element, key );
                valeurValue = BeanUtils.getSimpleProperty( element, value );
                referenceList.addItem( valeurKey, valeurValue );
            }
            if ( sort )
            {
                referenceList.sort( ( ReferenceItem o1, ReferenceItem o2 ) -> o1.getName( ).compareTo( o2.getName( ) ) );
            }
        }
        catch( IllegalAccessException e )
        {
            AppLogService.error( ERROR_LIST_CREATION + e.getMessage( ), e );
        }
        catch( InvocationTargetException e )
        {
            AppLogService.error( ERROR_LIST_CREATION + e.getMessage( ), e );
        }
        catch( NoSuchMethodException e )
        {
            AppLogService.error( ERROR_LIST_CREATION + e.getMessage( ), e );
        }
        catch( Exception e )
        {
            AppLogService.error( ERROR_LIST_CREATION + e.getMessage( ), e );
        }

        return referenceList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList retainReferenceList( ReferenceList refList, List<Integer> listId, boolean bKeepFirstItem )
    {
        ReferenceList filterList = new ReferenceList( );
        if ( ( listId == null ) || listId.isEmpty( ) )
        {
            return refList;
        }
        List<String> listCode = new ArrayList<>( );
        for ( Integer nId : listId )
        {
            listCode.add( Integer.toString( nId ) );
        }
        if ( bKeepFirstItem )
        {
            listCode.add( refList.get( 0 ).getCode( ) );
        }
        for ( ReferenceItem item : refList )
        {
            for ( String strCode : listCode )
            {
                if ( StringUtils.equals( strCode, item.getCode( ) ) )
                {
                    filterList.add( item );
                    listCode.remove( strCode );
                    break;
                }
            }
        }
        return filterList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getListOfIntFromStrArray( String [ ] array )
    {
        if ( null == array )
        {
            return Collections.emptyList( );
        }
        List<Integer> intList = new ArrayList<>( );
        for ( String value : array )
        {
            Integer intValue = NumberUtils.toInt( value );
            if ( intValue >= 0 )
            {
                intList.add( intValue );
            }
        }
        return intList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getListOfStringFromStrArray( String [ ] array )
    {
        if ( null == array )
        {
            return Collections.emptyList( );
        }
        List<String> strList = new ArrayList<>( );
        for ( String value : array )
        {
            if ( !StringUtils.EMPTY.equals( value ) )
            {
                strList.add( value );
            }
        }
        return strList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getPropertyListInt( String propertyKey )
    {
        String property = AppPropertiesService.getProperty( propertyKey );
        if ( property != null )
        {
            String [ ] items = property.split( PROPERTY_LIST_COMA );
            List<String> itemsAsList = Arrays.asList( items );
            if ( items != null )
            {
                return itemsAsList.stream( ).map( Integer::valueOf ).collect( Collectors.toList( ) );
            }
        }
        return Collections.emptyList( );
    }

}
