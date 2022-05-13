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
package fr.paris.lutece.plugins.dansmarue.service;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementFilter;
import fr.paris.lutece.plugins.dansmarue.commons.Order;
import fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties;
import fr.paris.lutece.plugins.dansmarue.service.dto.SignalementExportCSVDTO;

/**
 * The Interface ISignalementExportService.
 */
public interface ISignalementExportService
{

    /**
     * Returns a list of reports formatted for export according to the search parameters contained in the filter.
     *
     * @param ids
     *            the ids
     * @return a list of reports formatted for export
     */
    List<SignalementExportCSVDTO> findByIds( int [ ] ids );

    /**
     * Returns a list of reports formatted for export according to the search parameters contained in the filter.
     *
     * @param filter
     *            the request based filter
     * @return a list of reports formatted for export
     */
    List<SignalementExportCSVDTO> findByFilter( SignalementFilter filter );

    /**
     * Returns number of reports return by the search query.
     *
     * @param filter
     *            the request based filter
     * @return a list of reports formatted for export
     */
    int countSearchResult( SignalementFilter filter );

    /**
     * Returns a list of reports formatted for search screen according to the search parameters contained in the filter.
     *
     * @param filter
     *            the request based filter
     * @param paginationProperties
     *            pagination properties
     * @return a list of reports formatted for export
     */
    List<Signalement> findByFilterSearch( SignalementFilter filter, PaginationProperties paginationProperties );

    /**
     * Find by ids with photo.
     *
     * @param ids
     *            the ids
     * @return the list
     */
    List<SignalementExportCSVDTO> findByIdsWithPhoto( int [ ] ids );

    /**
     * Find by ids with photo with order.
     *
     * @param ids
     *            the ids
     * @param order
     *            the order
     * @return the list
     */
    List<SignalementExportCSVDTO> findByIdsWithPhotoWithOrder( int [ ] ids, Order order );

    /**
     * Find by filter search for FDT.
     *
     * @param filter
     *            the filter
     * @param paginationProperties
     *            the pagination properties
     * @return the list
     */
    List<Signalement> findByFilterSearchForFDT( SignalementFilter filter, PaginationProperties paginationProperties );
}
