package fr.paris.lutece.plugins.dansmarue.business.dao;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.DomaineFonctionnel;
import fr.paris.lutece.portal.service.plugin.Plugin;

public interface IDomaineFonctionnelDAO
{
	/**
	 * Inserts into the database, the domaineFonctionnel
	 * @param domaineFonctionnel
	 * @return
	 */
    Integer insert( DomaineFonctionnel domaineFonctionnel, Plugin plugin );
    
    /**
     * Gets the next id
     * @param plugin
     * @return
     */
    Integer newPrimaryKey( Plugin plugin );
    
    /**
     * Removes a domaine fonctionnel
     * @param lId
     */
    void remove( long lId );
    
    /**
     * Loads a domaine fonctionnel
     * @param lId the id of the domaine to load
     * @return
     */
    DomaineFonctionnel getById( Integer lId );
    
    /**
     * Updates a domaine fonctionnel
     * @param domaineFonctionnel
     */
    void store( DomaineFonctionnel domaineFonctionnel );

    /**
     * Gets all domaines fonctionnel
     * @return
     */
	List<DomaineFonctionnel> getAllDomainesFonctionnel();

	/**
	 * Gets all arrondissement ids linked to this domain
	 * @param idDomaine
	 * @return
	 */
	List<Integer> getArrondissementsIdsByDomaineId(int idDomaine);
	
	/**
	 * Gets the quartiers ids by domaine id.
	 *
	 * @param idDomaine the id domaine
	 * @return the quartiers ids by domaine id
	 */
	List<Integer> getQuartiersIdsByDomaineId(int idDomaine);
	
	/**
	 * Gets all types signalement ids linked to this domain
	 * @param idDomaine
	 * @return
	 */
	List<Integer> getTypesSignalementIdsByDomaineId(int idDomaine);
	
	/**
	 * Gets all units linked to this domain
	 * @param idDomaine
	 * @return
	 */
	List<Integer> getUnitsIdsByDomaineId(int idDomaine);

	/**
	 * Gets all active domains
	 * @return list of active domains
	 */
	List<DomaineFonctionnel> getAllDomainesFonctionnelActifs();
}
