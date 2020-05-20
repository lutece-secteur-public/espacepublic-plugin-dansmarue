/*
 * Copyright (c) 2002-2020, City of Paris
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

import fr.paris.lutece.plugins.dansmarue.business.entities.Source;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalementWithDepth;
import fr.paris.lutece.plugins.dansmarue.service.dto.TypeSignalementDTO;
import fr.paris.lutece.portal.service.image.ImageResource;


/**
 * The Interface ITypeSignalementService.
 */
public interface ITypeSignalementService
{

    /**
     * Return all report types.
     *
     * @return a list of report types
     */
    List<TypeSignalement> getAllTypeSignalement( );

    /**
     * Return all active report types.
     *
     * @return a list of active report types
     */
    List<TypeSignalement> getAllTypeSignalementActif( );

    /**
     * Return all type signalement agent or non agent.
     *
     * @param isAgent the is agent
     * @return the all type signalement by is agent
     */
    List<TypeSignalement> getAllTypeSignalementByIsAgent( boolean isAgent );

    /**
     * Load the report type.
     *
     * @param nIdTypeSignalement
     *            the report type id
     * @return an instance of {@link TypeSignalement}
     */
    TypeSignalement getByIdTypeSignalement( Integer nIdTypeSignalement );

    /**
     * Gets the all report type without children.
     *
     * @return the all report type without children
     */
    List<TypeSignalement> getAllTypeSignalementWithoutChildren( );

    /**
     * all report types.
     *
     * @return all report types.
     */
    List<TypeSignalement> getAll( );

    /**
     * Gets the all report sub types.
     *
     * @param nIdTypeSignalement
     *            the report type id
     * @return all report sub types by id.
     */
    List<TypeSignalement> getAllSousTypeSignalement( Integer nIdTypeSignalement );

    /**
     * Gets the all report sub types which are active.
     *
     * @param nIdTypeSignalement
     *            the report type id
     * @return the all report sub types
     */
    List<TypeSignalement> getAllSousTypeSignalementActifs( Integer nIdTypeSignalement );

    /**
     * Gets the all report types without parent.
     *
     * @return all report types without parent.
     */
    List<TypeSignalement> getAllTypeSignalementWithoutParent( );

    /**
     * Gets the all active report types without parent.
     *
     * @return all active report types without parent.
     */
    List<TypeSignalement> getAllTypeSignalementActifWithoutParent( );

    /**
     * Gets the all type signalement actif with depth.
     *
     * @param unitId
     *            the unit id
     * @return the all type signalement actif with depth
     */
    List<TypeSignalementWithDepth> getAllTypeSignalementActifWithDepth( Integer unitId );

    /**
     * Insert a new report type.
     *
     * @param typeSignalement
     *            the report type
     * @return the new primary key
     */
    Integer insert( TypeSignalement typeSignalement );

    /**
     * Gets the direct parent of a report type.
     *
     * @param nIdTypeSignalement
     *            the report type id
     * @return the direct parent of a report type.
     */
    TypeSignalement getParent( Integer nIdTypeSignalement );

    /**
     * Returns a report sub type by id.
     *
     * @param nIdTypeSignalement            the report type id
     * @return the report type object
     */
    TypeSignalement getTypeSignalementByIdWithParents( Integer nIdTypeSignalement );

    /**
     * Updates a report type.
     *
     * @param typeSignalement            the report type
     */
    void update( TypeSignalement typeSignalement );

    /**
     * Check if a report type exists and adds it if not.
     *
     * @param typeSignalement
     *            the report type
     * @return true if report type already exists.
     */
    boolean existsSaveTypeSignalement( TypeSignalement typeSignalement );

    /**
     * Get a report types linked to a report.
     *
     * @param nIdTypeSignalement
     *            the report type id
     * @return the report types linked to a report.
     */
    TypeSignalement findByIdTypeSignalement( Integer nIdTypeSignalement );

    /**
     * Remove a unit.
     *
     * @param nIdTypeSignalement
     *            the report type id
     */
    void delete( Integer nIdTypeSignalement );

    /**
     * Find by report id.
     *
     * @param nIdSignalement
     *            the report id
     * @return true, if successful the report is linked to a type.
     */
    boolean findByIdSignalement( Integer nIdSignalement );

    /**
     * Gets a report type by its id.
     *
     * @param nId            report type id
     * @return the report type
     */
    TypeSignalement getTypeSignalement( Integer nId );

    /**
     * update a root report type.
     *
     * @param typeSignalement
     *            the report type
     */
    void updateWithoutParent( TypeSignalement typeSignalement );

    /**
     * insert a report type as a root.
     *
     * @param typeSignalement
     *            the report type
     * @return the report type id
     */
    Integer insertWithoutParent( TypeSignalement typeSignalement );

    /**
     * disable (put active=false) all children of the report type.
     *
     * @param typeSignalement
     *            the report type
     */
    void disableChildren( TypeSignalement typeSignalement );

    /**
     * Get all typeSignalement with no children, actif and linked to a unit.
     *
     * @return the list
     */
    List<TypeSignalement> getAllTypeSignalementActifLinkedToUnit( );

    /**
     * Get all report types with no children and linked to a unit.
     *
     * @return the list
     */
    List<TypeSignalement> getAllTypeSignalementLinkedToUnit( );

    /**
     * Gets the all sub types . Like getAllSousType returns also sub sub types and so on.
     *
     * @param typeSignalementId            the report types
     * @param listeTypeSignalement            the list of report types
     */
    void getAllSousTypeSignalementCascade( Integer typeSignalementId, List<TypeSignalement> listeTypeSignalement );

    /**
     * Gets the report types tree.
     *
     * @param actif
     *            if true only actives report types are returned, if not all of them are returned
     * @return a tree of report types
     */
    List<TypeSignalementDTO> getTypeSignalementTree( boolean actif );

    /**
     * Gets the type signalement tree from source.
     *
     * @param nIdSource
     *            the n id source
     * @return the type signalement tree from source
     */
    List<TypeSignalementDTO> getTypeSignalementTreeFromSource( Integer nIdSource );

    /**
     * Returns a report id with the minimal order.
     *
     * @param typeSignalement            the report type object
     * @return the report type with the minimal order
     */
    int getIdTypeSignalementOrdreInferieur( TypeSignalement typeSignalement );

    /**
     * Returns a report id with the maximal order.
     *
     * @param typeSignalement            the report type object
     * @return the report type with the maximal order
     */
    int getIdTypeSignalementOrdreSuperieur( TypeSignalement typeSignalement );

    /**
     * Change the order of a report type.
     *
     * @param typeSignalement            the report type object
     */
    void updateOrdre( TypeSignalement typeSignalement );

    /**
     * Check if an order exists.
     *
     * @param ordre            the order
     * @param typeSignalementParent            the parent report type object
     * @return a boolean
     */
    boolean existsOrdre( int ordre, TypeSignalement typeSignalementParent );

    /**
     * Update the order.
     *
     * @param ordre            the order
     * @param typeSignalementParent            the parent report type object
     */
    void updateOrderGreaterThan( Integer ordre, TypeSignalement typeSignalementParent );

    /**
     * Returns the minimum order in hierarchy of a report type family.
     *
     * @param order            the order
     * @param typeSignalementParent            the parent report type
     * @return the minimum order
     */
    Integer getMinimumOrderAfterGivenOrder( Integer order, TypeSignalement typeSignalementParent );

    /**
     * Returns the maximum order in hierarchy of a report type family.
     *
     * @param typeSignalementParent            the parent report type
     * @return the maximum order
     */
    Integer getMaximumOrderInHierarchyTypeSignalement( TypeSignalement typeSignalementParent );

    /**
     * Update the order of the report types if one is delete in the tree.
     *
     * @param order            the order
     * @param typeSignalementParent            the parent report type
     */
    void updateOrderSuperiorAfterDeletingTypeSignalement( Integer order, TypeSignalement typeSignalementParent );

    /**
     * Load the report type image.
     *
     * @param nIdTypeSignalement            the id of the report type
     * @return the image object
     */
    ImageResource getImageResource( int nIdTypeSignalement );

    /**
     * Method to get the last report type version.
     *
     * @return last report type version
     */
    double findLastVersionTypeSignalement( );

    /**
     * Retrieves a category id, of a report type id.
     *
     * @param idTypeSignalement            the report type id
     * @return The id of the category of the report type -1 if not found
     */
    int getCategoryFromTypeId( Integer idTypeSignalement );

    /**
     * Finds report type with parent even without unit.
     *
     * @param id            the report type
     * @return List of report type even without unit
     */
    TypeSignalement getTypeSignalementByIdWithParentsWithoutUnit( Integer id );

    /**
     * Update only the parent.
     *
     * @param nIdTypeSignalement the n id type signalement
     * @param nIdParent the n id parent
     *
     */
    void updateParent( Integer nIdTypeSignalement, Integer nIdParent );

    /**
     * Get the last lvl of typesignalement with parents in the libelle.
     *
     * @return List of report type even without unit
     */
    List<TypeSignalement> getListTypeSignalementLastLevelWithMessage( );

    /**
     * Get the last lvl of typesignalement with parents in the libelle.
     *
     * @return List of report type even without unit
     */
    List<TypeSignalement> getListTypeSignalementLastLevelWithoutMessage( );

    /**
     * Gets the list ids children.
     *
     * @param categoryIds
     *            the category ids
     * @return the list ids children
     */
    List<Integer> getListIdsChildren( List<Integer> categoryIds );

    /**
     * Insert type signalement source.
     *
     * @param idType
     *            the id type
     * @param idSource
     *            the id source
     */
    void insertTypeSignalementSource( Integer idType, Integer idSource );

    /**
     * Removes the type signalement source.
     *
     * @param idType
     *            the id type
     * @param idSource
     *            the id source
     */
    void removeTypeSignalementSource( Integer idType, Integer idSource );

    /**
     * Gets the all type signalement by source.
     *
     * @param idSource
     *            the id source
     * @return the all type signalement by source
     */
    List<TypeSignalement> getAllTypeSignalementBySource( Integer idSource );

    /**
     * Gets the all type lastlevel not in source.
     *
     * @param idSource
     *            the id source
     * @return the all type lastlevel not in source
     */
    List<TypeSignalement> getAllTypeLastlevelNotInSource( Integer idSource );

    /**
     * Gets the all type signalement DTO by source.
     *
     * @param idSource
     *            the id source
     * @return the all type signalement DTO by source
     */
    List<TypeSignalementDTO> getAllTypeSignalementDTOBySource( Integer idSource );

    /**
     * Gets the list source.
     *
     * @return the list source
     */
    List<Source> getListSource( );

    /**
     * Adds the source.
     *
     * @param source
     *            the source
     */
    void addSource( Source source );

    /**
     * Removes the source.
     *
     * @param idSource
     *            the id source
     */
    void removeSource( Integer idSource );

    /**
     * Gets the source by id.
     *
     * @param idSource
     *            the id source
     * @return the source by id
     */
    Source getSourceById( Integer idSource );

    /**
     * Update source.
     *
     * @param source
     *            the source
     */
    void updateSource( Source source );
}
