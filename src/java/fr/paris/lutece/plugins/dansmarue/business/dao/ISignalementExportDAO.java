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

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementFilter;
import fr.paris.lutece.plugins.dansmarue.commons.Order;
import fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties;
import fr.paris.lutece.plugins.dansmarue.service.dto.SignalementExportCSVDTO;
import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * The Interface ISignalementExportDAO.
 */
public interface ISignalementExportDAO
{

    /**
     * Returns a list of reports formatted for export according to the search parameters contained in the filter.
     *
     * @param ids
     *            the ids
     * @param plugin
     *            the plugin
     * @return a list of reports formatted for export
     */
    List<SignalementExportCSVDTO> findByIds( int [ ] ids, Plugin plugin );

    /**
     * Returns a list of reports formatted for export according to the search parameters contained in the filter.
     *
     * @param filter
     *            the request based filter
     * @param plugin
     *            the plugin
     * @return a list of reports formatted for export
     */
    List<SignalementExportCSVDTO> findByFilter( SignalementFilter filter, Plugin plugin );

    /**
     * Count signalement search.
     *
     * @param filter
     *            the request based filter
     * @param plugin
     *            the plugin
     * @return number of reports found.
     */
    int countSignalementSearch( SignalementFilter filter, Plugin plugin );

    /**
     * Return numero report find.
     *
     * @param filter
     *            the filter
     * @param paginationProperties
     *            the pagination properties
     * @param plugin
     *            the plugin
     * @return list of numero signalement
     */
    List<String> searchNumeroByFilter( SignalementFilter filter, PaginationProperties paginationProperties, Plugin plugin );

    /**
     * Return a list of reports for search screen according to the search parameters contained in the filter.
     *
     * @param filter
     *            the request based filter
     * @param listIdSignalement
     *            the list id signalement
     * @param plugin
     *            the plugin
     * @return a list of reports formatted for search
     */
    List<Signalement> searchFindByFilter( SignalementFilter filter, List<String> listIdSignalement, Plugin plugin );

    /**
     * Search find by filter for FDT.
     *
     * @param filter
     *            the filter
     * @param listIdSignalement
     *            the list id signalement
     * @param plugin
     *            the plugin
     * @return the list
     */
    List<Signalement> searchFindByFilterForFDT( SignalementFilter filter, List<String> listIdSignalement, Plugin plugin );

    /**
     * Find by ids with order.
     *
     * @param ids
     *            the ids
     * @param order
     *            the order
     * @param plugin
     *            the plugin
     * @return the list
     */
    List<SignalementExportCSVDTO> findByIdsWithOrder( int [ ] ids, Order order, Plugin plugin );

}
