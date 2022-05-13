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

import fr.paris.lutece.plugins.dansmarue.business.entities.DomaineFonctionnel;
import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * The Interface IDomaineFonctionnelDAO.
 */
public interface IDomaineFonctionnelDAO
{

    /**
     * Inserts into the database, the functional domain.
     *
     * @param domaineFonctionnel
     *            the functional domain
     * @param plugin
     *            the plugin
     * @return the functional domain id
     */
    Integer insert( DomaineFonctionnel domaineFonctionnel, Plugin plugin );

    /**
     * Gets the next id.
     *
     * @param plugin
     *            the plugin
     * @return The new primary key
     */
    Integer newPrimaryKey( Plugin plugin );

    /**
     * Removes a functional domain.
     *
     * @param lId
     *            the domain id
     */
    void remove( long lId );

    /**
     * Loads a functional domain.
     *
     * @param lId
     *            the id of the domain to load
     * @return a functional domain
     */
    DomaineFonctionnel getById( Integer lId );

    /**
     * Inserts or Updates a functional domain.
     *
     * @param domaineFonctionnel
     *            the functional domain object
     */
    void store( DomaineFonctionnel domaineFonctionnel );

    /**
     * Gets all functional domains.
     *
     * @return list of functional domains
     */
    List<DomaineFonctionnel> getAllDomainesFonctionnel( );

    /**
     * Gets all districts ids linked to this domain.
     *
     * @param idDomaine
     *            the domain id
     * @return list of districts ids linked to this domain
     */
    List<Integer> getArrondissementsIdsByDomaineId( int idDomaine );

    /**
     * Gets the neighborhoods ids by domain id.
     *
     * @param idDomaine
     *            the domain id
     * @return the neighborhoods ids by domain id
     */
    List<Integer> getQuartiersIdsByDomaineId( int idDomaine );

    /**
     * Gets all reporting types ids linked to this domain.
     *
     * @param idDomaine
     *            the domain id
     * @return list of reporting types id linked to this domain
     */
    List<Integer> getTypesSignalementIdsByDomaineId( int idDomaine );

    /**
     * Gets all units linked to this domain.
     *
     * @param idDomaine
     *            the domain id
     * @return list of units id linked to this domain
     */
    List<Integer> getUnitsIdsByDomaineId( int idDomaine );

    /**
     * Gets all active domains.
     *
     * @return list of active domains
     */
    List<DomaineFonctionnel> getAllDomainesFonctionnelActifs( );
}
