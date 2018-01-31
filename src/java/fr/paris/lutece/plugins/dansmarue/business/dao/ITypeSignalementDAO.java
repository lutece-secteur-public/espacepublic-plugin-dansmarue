package fr.paris.lutece.plugins.dansmarue.business.dao;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.plugin.Plugin;


/**
 * The Interface ITypeSignalementDAO.
 */
public interface ITypeSignalementDAO
{

    /**
     * Get new primary key
     * @param plugin the plugin
     * @return a new primary key
     */
    Integer newPrimaryKey( Plugin plugin );

    /**
     * Load the type Signalement.
     * 
     * @param lIdTypeSignalement the type signalement id
     * @param plugin the plugin
     * @return an instance of {@link TypeSignalement}
     */
    TypeSignalement load( Integer lIdTypeSignalement, Plugin plugin );

    /**
     * Return all type signalement
     * 
     * @return a list of type signalement
     */
    List<TypeSignalement> getAllTypeSignalement( );

    /**
     * Insert a new TypeSignalement.
     * 
     * @param typeSignalement the type signalement
     * @param plugin the plugin
     * @return the new primary key
     */
    Integer insert( TypeSignalement typeSignalement, Plugin plugin );

    /**
     * Remove a unit.
     * 
     * @param nIdTypeSignalement the type signalement id
     * @param plugin the plugin
     */
    void remove( Integer nIdTypeSignalement, Plugin plugin );

    /**
     * Gets the all type signalement without children.
     * 
     * @return the all type signalement without children
     */
    List<TypeSignalement> getAllTypeSignalementWithoutChildren( );

    /**
     * all signalement types.
     * 
     * @return all signalement types.
     */
    List<TypeSignalement> getAll( );

    /**
     * Gets the all signalement sub types.
     * 
     * @param lIdTypeSignalement the type signalement id
     * @return all signalement sub types by id.
     */
    List<TypeSignalement> getAllSousTypeSignalement( Integer lIdTypeSignalement );
    
    /**
     * Gets the all sous type signalement which are active.
     * 
     * @param nIdTypeSignalement the n id type signalement
     * @return the all sous type signalement
     */
    List<TypeSignalement> getAllSousTypeSignalementActifs( Integer nIdTypeSignalement );

    /**
     * Gets the all signalement types without parent.
     * 
     * @return all signalement types without parent.
     */
    List<TypeSignalement> getAllTypeSignalementWithoutParent( );

    /**
     * Gets the all signalement types actif without parent.
     * 
     * @return all signalement types actif without parent.
     */
    List<TypeSignalement> getAllTypeSignalementActifWithoutParent( );
    
    /**
     * Get a signalement types linked to a signalement.
     * 
     * @param nIdTypeSignalement the type signalement id
     * @return the signalement types linked to a signalement.
     */
    TypeSignalement findByIdTypeSignalement( Integer nIdTypeSignalement );

    /**
     * Gets the direct parent of a type signalement.
     * 
     * @param nIdTypeSignalement the type signalement id
     * @param plugin the plugin
     * @return the direct parent of a type signalement.
     */
    TypeSignalement getParent( Integer nIdTypeSignalement, Plugin plugin );

    /**
     * Adds a type signalement
     * 
     * @param typeSignalement the type signalement
     * @param plugin the plugin
     */
    void store( TypeSignalement typeSignalement, Plugin plugin );

    /**
     * Check if a signalement type exists and adds it if not.
     * 
     * @param typeSignalement the type signalement
     * @return true if signalement type already exists.
     */
    boolean existsSaveTypeSignalement( TypeSignalement typeSignalement );

    /**
     * Find by id signalement.
     * 
     * @param nIdSignalement the signalement id
     * @return true, if successful the signalement is linked to a type.
     */
    boolean findByIdSignalement( Integer nIdSignalement );

    /**
     * Gets a type signalement by its id
     * 
     * @param typeSignalementId type signalement id
     * @return the type signalement
     */
    TypeSignalement getTypeSignalement( Integer typeSignalementId );

    /**
     * Get a type signalement with every parents without their photos.
     * @param nId The id of the type signalement to get
     * @return The type signalement.
     */
    TypeSignalement getTypeSignalementWithoutPhoto( Integer nId );

    /**
     * update a root type signalement.
     * 
     * @param typeSignalement the type signalement
     * @param plugin the plugin
     */
    void updateWithoutParent( TypeSignalement typeSignalement, Plugin plugin );

    /**
     * insert a type signalement as a root.
     * 
     * @param typeSignalement the type signalement
     * @param plugin the plugin
     * @return the integer
     */
    Integer insertWithoutParent( TypeSignalement typeSignalement, Plugin plugin );

    TypeSignalement getTypeSignalementByIdWithParents( Integer nIdTypeSignalement );
    
    int getIdTypeSignalementOrdreInferieur( TypeSignalement typeSignalement );
    
    int getIdTypeSignalementOrdreSuperieur( TypeSignalement typeSignalement );

    void updateOrdre( TypeSignalement typeSignalement );

    boolean existsOrdre( int ordre, TypeSignalement typeSignalementParent );

    void updateOrderGreaterThan( Integer ordre, TypeSignalement typeSignalementParent );

    Integer getMaximumOrderInHierarchyTypeSignalement( TypeSignalement typeSignalementParent );

    Integer getMinimumOrderAfterGivenOrder( Integer order, TypeSignalement typeSignalementParent );
    
    void updateOrderSuperiorAfterDeletingTypeSignalement( Integer order, TypeSignalement typeSignalementParent );

    ImageResource loadImage( int nKey );
    
    /**
     * Method to get the last type signalement version 
     * @return last type signalement version
     */
    double findLastVersionTypeSignalement(  );

    /**
     * Save a new TypeSignalement Alias
     * 
     */
	Integer insertAlias(TypeSignalement typeSignalement, Plugin plugin);

	/**
     * Updates an TypeSignalement Alias
     * 
     */
	void updateAlias(TypeSignalement typeSignalement, Plugin plugin);

	 /**
     * Delete an TypeSignalement Alias
     * 
     */
	void deleteAlias(Integer idTypeSignalement, Plugin plugin);

	 /**
     * Refresh the view table, which links a type to all its parents including intermediates
     */
	void refreshViewTypesWithParentsLinks();

	/**
     * Retrieves a category id, of a type signalement id
     * @param idTypeSignalement
     * @return
     * 		The id of the category of the type signalement
     * 		-1 if not found
     */
	int getCategoryFromTypeId(Integer idTypeSignalement);

	/**
	 * Finds all signalement even without unit
	 * @return
	 * 		List of type signalement even without unit
	 */
	List<TypeSignalement> getAllTypeSignalementEvenWithoutUnit();

}
