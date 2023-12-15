package fr.paris.lutece.plugins.dansmarue.utils;

import fr.paris.lutece.plugins.dansmarue.utils.impl.ListUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * IListUtils
 */
public interface IListUtils
{
    /**
     * Return.
     *
     * @param propertyKey
     *            the property key
     * @return the property list
     */
    List<String> getPropertyList( String propertyKey );

    /**
     * Conversion of a list of type {@link List} to a {@link ReferenceList}.
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
    ReferenceList toReferenceList( List<?> list, String key, String value, String firstItem );

    /**
     * Conversion of a list of type {@link List} to a {@link ReferenceList}.
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
    ReferenceList toReferenceList( List<?> list, String key, String value, String firstItem, boolean sort );

    /**
     * Remove every elements from a reference list that are NOT contained in a given list.
     *
     * @param refList
     *            The list ro filter
     * @param listId
     *            The list of ids to retain in the reference list
     * @param bKeepFirstItem
     *            True to keep the first item, false to keep it only ifs code match any item of the id list.
     * @return a new list with only the items of the referenceList which ids are in the parameter list. Note that a new list is created, but items are NOT
     *         duplicated !
     */
    ReferenceList retainReferenceList( ReferenceList refList, List<Integer> listId, boolean bKeepFirstItem );

    /**
     * Converts an array of string, to a list of int.
     *
     * @param array
     *            an array of string
     * @return an integer list
     */
    List<Integer> getListOfIntFromStrArray( String [ ] array );

    /**
     * Converts an array of string, to a list of int.
     *
     * @param array
     *            an array of string
     * @return an integer list
     */
    List<String> getListOfStringFromStrArray( String [ ] array );

    /**
     * Return.
     *
     * @param propertyKey
     *            the property key
     * @return the property list
     */
    List<Integer> getPropertyListInt( String propertyKey );
}
