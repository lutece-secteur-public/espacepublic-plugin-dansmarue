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

/*
 * Copyright (c) 2002-2010, Mairie de Paris
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

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;


/**
 * Utilitaire servant à la manipulation des listes
 * 
 */
public final class ListUtils
{

    private static final Logger LOGGER = Logger.getLogger( ListUtils.class );
    private static final String PROPERTY_LIST_SEPARATOR = ";";
    private static final String PROPERTY_LIST_COMA = ",";

    /**
     * Return.
     * 
     * @param propertyKey the property key
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
     * Conversion d'une liste de type {@link List} vers une
     * {@link ReferenceList}
     * 
     * @param list
     *            la liste à convertir
     * @param key
     *            la valeur de la propriété du bean servant de clé dans la
     *            {@link ReferenceList}
     * @param value
     *            la valeur de la propriété du bean servant de valeur dans la
     *            {@link ReferenceList}
     * @param firstItem
     *            valeur de la première ligne dans la {@link ReferenceList}
     *            (exemple, en vue d'afficher la ReferenceList dans une liste
     *            déroulante : " -- Sélectionnez une valeur --").
     * @return La {@link ReferenceList} peuplée avec les données de la Liste
     */
    public static ReferenceList toReferenceList( List<?> list, String key, String value, String firstItem )
    {
        return ListUtils.toReferenceList( list, key, value, firstItem, false );
    }

    /**
     * Conversion d'une liste de type {@link List} vers une.
     * 
     * @param list la liste à convertir
     * @param key la valeur de la propriété du bean servant de clé dans la
     * @param value la valeur de la propriété du bean servant de valeur dans la
     * @param firstItem valeur de la première ligne dans la
     *            {@link ReferenceList} (exemple, en vue d'afficher la
     *            ReferenceList dans une liste
     *            déroulante : " -- Sélectionnez une valeur --").
     * @param sort if true reference list twill be sorted
     * @return La {@link ReferenceList} peuplée avec les données de la Liste
     *         {@link ReferenceList} {@link ReferenceList} {@link ReferenceList}
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
        }
        catch ( IllegalAccessException e )
        {
            LOGGER.warn( "Erreur lors de la création d'une liste pour combo : " + e.getMessage( ), e );
        }
        catch ( InvocationTargetException e )
        {
            LOGGER.warn( "Erreur lors de la création d'une liste pour combo : " + e.getMessage( ), e );
        }
        catch ( NoSuchMethodException e )
        {
            LOGGER.warn( "Erreur lors de la création d'une liste pour combo : " + e.getMessage( ), e );
        }
        catch ( Exception e )
        {
            LOGGER.warn( "Erreur lors de la création d'une liste pour combo : " + e.getMessage( ), e );
        }

        return referenceList;
    }

    /**
     * Remove every elements from a reference list that are NOT contained in a
     * given list.
     * @param refList The list ro filter
     * @param listId The list of ids to retain in the reference list
     * @param bKeepFirstItem True to keep the first item, false to keep it only
     *            ifs code match any item of the id list.
     * @return a new list with only the items of the referenceList which ids are
     *         in the parameter list. Note that a new list is created, but items
     *         are NOT duplicated !
     */
    public static ReferenceList retainReferenceList( ReferenceList refList, List<Integer> listId, boolean bKeepFirstItem )
    {
        ReferenceList filterList = new ReferenceList( );
        if ( listId == null || listId.size( ) == 0 )
        {
            return refList;
        }
        List<String> listCode = new ArrayList<String>( );
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
     * Instantiates a new list utils.
     */
    private ListUtils( )
    {
        // does nothing
    }
    
    /**
     * Converts an array of string, to a list of int
     * @param array
     * @return
     */
    public static List<Integer> getListOfIntFromStrArray(String[] array){
    	if(null == array){
    		return null;
    	}
    	List<Integer> intList = new ArrayList<Integer>();
    	for(String value:array){
    		Integer intValue = NumberUtils.toInt(value);
    		if(intValue >= 0){
    			intList.add(intValue);
    		}
    	}
    	return intList;
    }
    
    /**
     * Return.
     * 
     * @param propertyKey the property key
     * @return the property list
     */
    public static List<Integer> getPropertyListInt( String propertyKey )
    {
        String property = AppPropertiesService.getProperty( propertyKey );
        if ( property != null )
        {
            String[] items = property.split( PROPERTY_LIST_COMA );
            List<String> itemsAsList = Arrays.asList(items);
            if ( items != null )
            {
                return itemsAsList.stream().map(Integer::valueOf).collect(Collectors.toList());
            }
        }
        return null;
    }

    
}
