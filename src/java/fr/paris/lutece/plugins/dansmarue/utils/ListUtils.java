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
package fr.paris.lutece.plugins.dansmarue.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

public final class ListUtils
{

    private static final String ERROR_LIST_CREATION     = "Erreur lors de la création d'une liste pour combo : ";
    private static final Logger LOGGER                  = Logger.getLogger( ListUtils.class );
    private static final String PROPERTY_LIST_SEPARATOR = ";";
    private static final String PROPERTY_LIST_COMA      = ",";

    /**
     * Instantiates a new list utils.
     */
    private ListUtils( )
    {
        // does nothing
    }

    /**
     * Return.
     * 
     * @param propertyKey
     *            the property key
     * @return the property list
     */
    public static List<String> getPropertyList( String propertyKey )
    {
        String property = AppPropertiesService.getProperty( propertyKey );
        if ( property != null )
        {
            String[] items = property.split( PROPERTY_LIST_SEPARATOR );
            if ( items != null )
            {
                return Arrays.asList( items );
            }
        }
        return null;
    }

    /**
     * Conversion of a list of type {@link List} to a {@link ReferenceList}
     * 
     * @param list
     *            the list to be converted
     * @param key
     *            the value of the bean property used as a key in the {@link ReferenceList}
     * @param value
     *            the value of the bean property used as a value in the {@link ReferenceList}
     * @param firstItem
     *            value of the first line in the {@link ReferenceList} (example, to display the ReferenceList in a drop-down list:" -- Select a value --").
     * @return The {@link ReferenceList} populated with data from the List
     */
    public static ReferenceList toReferenceList( List<?> list, String key, String value, String firstItem )
    {
        return ListUtils.toReferenceList( list, key, value, firstItem, false );
    }

    /**
     * Conversion of a list of type {@link List} to a {@link ReferenceList}
     * 
     * @param list
     *            the list to be converted
     * @param key
     *            the value of the bean property used as a key in the {@link ReferenceList}
     * @param value
     *            the value of the bean property used as a value in the {@link ReferenceList}
     * @param firstItem
     *            value of the first line in the {@link ReferenceList} (example, to display the ReferenceList in a drop-down list:" -- Select a value --").
     * @param sort
     *            if the list has to be sorted
     * @return The {@link ReferenceList} populated with data from the List
     */
    public static ReferenceList toReferenceList( List<?> list, String key, String value, String firstItem, boolean sort )
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
                Comparator<ReferenceItem> fct = new Comparator<ReferenceItem>( )
                {

                    @Override
                    public int compare( ReferenceItem o1, ReferenceItem o2 )
                    {

                        return o1.getName( ).compareTo( o2.getName( ) );
                    }
                };
                Collections.sort( referenceList, fct );
            }
        } catch ( IllegalAccessException e )
        {
            LOGGER.warn( ERROR_LIST_CREATION + e.getMessage( ), e );
        } catch ( InvocationTargetException e )
        {
            LOGGER.warn( ERROR_LIST_CREATION + e.getMessage( ), e );
        } catch ( NoSuchMethodException e )
        {
            LOGGER.warn( ERROR_LIST_CREATION + e.getMessage( ), e );
        } catch ( Exception e )
        {
            LOGGER.warn( ERROR_LIST_CREATION + e.getMessage( ), e );
        }

        return referenceList;
    }

    /**
     * Remove every elements from a reference list that are NOT contained in a given list.
     * 
     * @param refList
     *            The list ro filter
     * @param listId
     *            The list of ids to retain in the reference list
     * @param bKeepFirstItem
     *            True to keep the first item, false to keep it only ifs code match any item of the id list.
     * @return a new list with only the items of the referenceList which ids are in the parameter list. Note that a new list is created, but items are NOT duplicated !
     */
    public static ReferenceList retainReferenceList( ReferenceList refList, List<Integer> listId, boolean bKeepFirstItem )
    {
        ReferenceList filterList = new ReferenceList( );
        if ( listId == null || listId.isEmpty( ) )
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
     * Converts an array of string, to a list of int
     * 
     * @param array
     *            an array of string
     * @return an integer list
     */
    public static List<Integer> getListOfIntFromStrArray( String[] array )
    {
        if ( null == array )
        {
            return null;
        }
        List<Integer> intList = new ArrayList<Integer>( );
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
     * Return.
     * 
     * @param propertyKey
     *            the property key
     * @return the property list
     */
    public static List<Integer> getPropertyListInt( String propertyKey )
    {
        String property = AppPropertiesService.getProperty( propertyKey );
        if ( property != null )
        {
            String[] items = property.split( PROPERTY_LIST_COMA );
            List<String> itemsAsList = Arrays.asList( items );
            if ( items != null )
            {
                return itemsAsList.stream( ).map( Integer::valueOf ).collect( Collectors.toList( ) );
            }
        }
        return null;
    }

}
