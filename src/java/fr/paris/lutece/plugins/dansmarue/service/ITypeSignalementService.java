package fr.paris.lutece.plugins.dansmarue.service;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.service.dto.TypeSignalementDTO;
import fr.paris.lutece.portal.service.image.ImageResource;

/**
 * The Interface ITypeSignalementService.
 */
public interface ITypeSignalementService
{
    /**
     * Return all type signalement
     *
     * @return a list of type signalement
     */
    List<TypeSignalement> getAllTypeSignalement( );

    /**
     * Return all type signalement which are in active state
     *
     * @return a list of type signalement which are in active state
     */
    List<TypeSignalement> getAllTypeSignalementActif( );

    /**
     * Get a type signalement by his Id.
     *
     * @param nIdTypeSignalement
     *            the type signalement id
     * @return a type signalement
     */
    TypeSignalement getByIdTypeSignalement( Integer nIdTypeSignalement );

    List<TypeSignalement> getAllTypeSignalementWithoutChildren( );

    List<TypeSignalement> getAll( );

    /**
     * Gets the all sous type signalement.
     *
     * @param nIdTypeSignalement
     *            the n id type signalement
     * @return the all sous type signalement
     */
    List<TypeSignalement> getAllSousTypeSignalement( Integer nIdTypeSignalement );

    /**
     * Gets the all sous type signalement which are active.
     *
     * @param nIdTypeSignalement
     *            the n id type signalement
     * @return the all sous type signalement
     */
    List<TypeSignalement> getAllSousTypeSignalementActifs( Integer nIdTypeSignalement );

    List<TypeSignalement> getAllTypeSignalementWithoutParent( );

    /**
     * Gets all the type signalement root (level 0, without parent) which are actif
     * 
     * @return
     */
    List<TypeSignalement> getAllTypeSignalementActifWithoutParent( );

    Integer insert( TypeSignalement typeSignalement );

    TypeSignalement getParent( Integer nIdTypeSignalement );

    /**
     * Gets the type signalement by id with parents.
     *
     * @param nIdTypeSignalement
     *            the id type signalement
     * @return the type signalement by id with parents
     */

    TypeSignalement getTypeSignalementByIdWithParents( Integer nIdTypeSignalement );

    void update( TypeSignalement typeSignalement );

    boolean existsSaveTypeSignalement( TypeSignalement typeSignalement );

    /**
     * Get a typesignalement linked to a signalement.
     *
     * @param lIdTypeSignalement
     *            the type signalement id
     * @return the type signalement
     */
    TypeSignalement findByIdTypeSignalement( Integer lIdTypeSignalement );

    void delete( Integer nIdTypeSignalement );

    /**
     * Verify if the type signalement is used in a signalement before delete.
     *
     * @param nIdSignalement
     *            the signalement id
     * @return true if found.
     */
    boolean findByIdSignalement( Integer nIdSignalement );

    TypeSignalement getTypeSignalement( Integer nId );

    /**
     * update a root type signalement.
     *
     * @param typeSignalement
     *            the type signalement
     */
    void updateWithoutParent( TypeSignalement typeSignalement );

    /**
     * insert a type signalement as a root.
     *
     * @param typeSignalement
     *            the type signalement
     * @return the insert object id
     */
    Integer insertWithoutParent( TypeSignalement typeSignalement );

    /**
     * disable (put active=false) all children of typeSignalement.
     *
     * @param typeSignalement
     *            the type signalement
     */
    void disableChildren( TypeSignalement typeSignalement );

    /**
     * Get all typeSignalement with no children, actif and linked to a unit
     * 
     * @return the list
     */
    List<TypeSignalement> getAllTypeSignalementActifLinkedToUnit( );

    /**
     * Get all typeSignalement with no children and linked to a unit
     * 
     * @return the list
     */
    List<TypeSignalement> getAllTypeSignalementLinkedToUnit( );

    /**
     * Gets the all sub types . Like getAllSousType returns also sub sub types and so on.
     *
     * @param typeSignalementId
     *            the type signalement id
     * @param listeTypeSignalement
     *            the liste type signalement
     */
    void getAllSousTypeSignalementCascade( Integer typeSignalementId, List<TypeSignalement> listeTypeSignalement );

    /**
     * Gets the type signalement tree.
     *
     * @param actif
     *            if true only actives signalement are returned, if not all of them are returned
     * @return a tree of type signalement
     */
    List<TypeSignalementDTO> getTypeSignalementTree( boolean actif );

    int getIdTypeSignalementOrdreInferieur( TypeSignalement typeSignalement );

    int getIdTypeSignalementOrdreSuperieur( TypeSignalement typeSignalement );

    void updateOrdre( TypeSignalement typeSignalement );

    boolean existsOrdre( int ordre, TypeSignalement typeSignalementParent );

    void updateOrderGreaterThan( Integer ordre, TypeSignalement typeSignalementParent );

    Integer getMinimumOrderAfterGivenOrder( Integer order, TypeSignalement typeSignalementParent );

    Integer getMaximumOrderInHierarchyTypeSignalement( TypeSignalement typeSignalementParent );

    void updateOrderSuperiorAfterDeletingTypeSignalement( Integer order, TypeSignalement typeSignalementParent );

    ImageResource getImageResource( int nIdTypeSignalement );

    /**
     * Method to get the last type signalement version
     * 
     * @return last type signalement version
     */
    double findLastVersionTypeSignalement( );

    /**
     * Retrieves a category id, of a type signalement id
     * 
     * @param idTypeSignalement
     * @return The id of the category of the type signalement -1 if not found
     */
    int getCategoryFromTypeId( Integer idTypeSignalement );

}
