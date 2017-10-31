package fr.paris.lutece.plugins.dansmarue.service;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.DomaineFonctionnel;
import fr.paris.lutece.portal.service.plugin.Plugin;

public interface IDomaineFonctionnelService {
	/**
	 * Inserts into the database, the domaineFonctionnel
	 * @param domaineFonctionnel
	 * @return
	 */
    Integer insert( DomaineFonctionnel domaineFonctionnel );
    
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
	 * Gets all types signalement ids linked to this domain
	 * @param idDomaine
	 * @return
	 */
	List<Integer> getTypesSignalementIdsByDomaineId(int idDomaine);

	/**
     * Gets all domaines fonctionnel, with Arrondissements, Entites, Category as objects
     * types anomalie
     * @return
     */
	List<DomaineFonctionnel> getAllDomainesFonctionnelWithData();

	/**
	 * Gets all domaines wich are active
	 * @return the list of domains which are active
	 */
	List<DomaineFonctionnel> getAllDomainesFonctionnelActifs();
	
}
