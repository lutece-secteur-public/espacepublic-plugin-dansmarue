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
package fr.paris.lutece.plugins.dansmarue.business.dao;

import java.util.List;
import java.util.Map;

import fr.paris.lutece.plugins.dansmarue.business.entities.FeuilleDeTournee;
import fr.paris.lutece.plugins.dansmarue.business.entities.FeuilleDeTourneeFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.FeuilleDeTourneeFilterSearch;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementBean;
import fr.paris.lutece.plugins.dansmarue.commons.Order;

/**
 * The Interface IFeuilleDeTourneeDAO.
 */
public interface IFeuilleDeTourneeDAO
{

    /**
     * Load.
     *
     * @param id
     *            the id
     * @return the feuille de tournee
     */
    FeuilleDeTournee load( Integer id );

    /**
     * Insert.
     *
     * @param feuilleDeTournee
     *            the feuille de tournee
     * @param prefixFDTNom
     *            prefix du nom de la feuille de tournee enregistre dans core_datastore
     * @param isCopy
     *            feuilleDeTournee is a copy
     * @return the integer
     */
    Integer insert( FeuilleDeTournee feuilleDeTournee, String prefixFDTNom, boolean isCopy );

    /**
     * Insert search filter.
     *
     * @param creator
     *            the creator
     * @param name
     *            the name
     * @param comment
     *            the comment
     * @param jsonFiltertoSave
     *            the json filterto save
     * @param idUnit
     *            the id unit
     */
    void insertSearchFilter( String creator, String name, String comment, String jsonFiltertoSave, int idUnit );

    /**
     * Update search filter.
     *
     * @param creator
     *            the creator
     * @param name
     *            the name
     * @param comment
     *            the comment
     * @param jsonFiltertoSave
     *            the json filterto save
     * @param idUnit
     *            the id unit
     */
    void updateSearchFilter( String creator, String name, String comment, String jsonFiltertoSave, int idUnit );

    /**
     * Load search filter.
     *
     * @param idUnit
     *            id unit to search
     * @return search filter found
     */
    Map<Integer, String> loadSearchFilter( int idUnit );

    /**
     * Find a SearchFilter.
     *
     * @param filterName
     *            filter name to find
     * @return search filter found
     */
    FeuilleDeTourneeFilterSearch findSearchFilter( String filterName );

    /**
     * Delete Search filter.
     *
     * @param idFilter
     *            idFilter
     */
    void deleteSearchFilter( int idFilter );

    /**
     * Load search filter.
     *
     * @param idToLoad
     *            id to load
     * @return filter value
     */
    String loadSearchFilterById( int idToLoad );

    /**
     * Load signalement bean list.
     *
     * @param idFeuilleDeTournee
     *            the id feuille de tournee
     * @return the list
     */
    List<SignalementBean> loadSignalementBeanList( int idFeuilleDeTournee );

    /**
     * Update.
     *
     * @param feuilleDeTournee
     *            the feuille de tournee
     */
    void update( FeuilleDeTournee feuilleDeTournee );

    /**
     * Update map feuille de tournee.
     *
     * @param idFeuilleDeTournee
     *            the id feuille de tournee
     * @param base64map
     *            map
     */
    void updateMap( int idFeuilleDeTournee, String base64map );

    /**
     * Update by name.
     *
     * @param feuilleDeTournee
     *            the feuille de tournee
     * @return the integer
     */
    Integer updateByName( FeuilleDeTournee feuilleDeTournee );

    /**
     * Removes the.
     *
     * @param nFeuilleDeTourneeId
     *            the n feuille de tournee id
     */
    void remove( int nFeuilleDeTourneeId );

    /**
     * Delete old feuille de tournee.
     *
     * @param jourDelaiSuppression
     *            the jour delai suppression
     */
    void deleteOldFeuilleDeTournee( int jourDelaiSuppression );

    /**
     * Find all.
     *
     * @return the list
     */
    List<FeuilleDeTournee> findAll( );

    /**
     * Load fdt by filter.
     *
     * @param feuilleDeTourneeFilter
     *            the feuille de tournee filter
     * @return the list
     */
    List<FeuilleDeTournee> loadFdtByFilter( FeuilleDeTourneeFilter feuilleDeTourneeFilter );

    /**
     * Load fdt by filter with order.
     *
     * @param feuilleDeTourneeFilter
     *            the feuille de tournee filter
     * @param order
     *            the order
     * @return the list
     */
    List<FeuilleDeTournee> loadFdtByFilterWithOrder( FeuilleDeTourneeFilter feuilleDeTourneeFilter, Order order );
}
