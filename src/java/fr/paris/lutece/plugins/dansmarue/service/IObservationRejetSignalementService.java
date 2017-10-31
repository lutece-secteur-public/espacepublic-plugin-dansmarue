package fr.paris.lutece.plugins.dansmarue.service;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;

public interface IObservationRejetSignalementService {
	/**
	 * Inserts an entry of a signalement rejet reason
	 * @param idSignalement
	 * 		  The signalement id
	 * @param idRaisonRejet
	 * 		  The rejet id
	 */
	void insert(int idSignalement, Integer idRaisonRejet, String observationRejetComment);
	
	/**
	 * Removes an entry of a signalement rejet reason
	 * @param idSignalement
	 * 		  The signalement id
	 * @param idRaisonRejet
	 * 		  The rejet id
	 */
	void delete(int idSignalement, Integer idRaisonRejet);
	
	/**
	 * Finds alls reject reasons of a signalement
	 * @param idSignalement
	 * 		  The signalement Id
	 * @return
	 * 		  List of observation rejet matching the signalement id
	 */
	List<ObservationRejet> findByIdSignalement(int idSignalement);
	
}
