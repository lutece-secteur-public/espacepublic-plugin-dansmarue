package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.util.List;

import javax.inject.Inject;

import fr.paris.lutece.plugins.dansmarue.business.dao.IObservationRejetSignalementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;
import fr.paris.lutece.plugins.dansmarue.service.IObservationRejetSignalementService;

public class ObservationRejetSignalementService implements IObservationRejetSignalementService {

	
	@Inject
    private IObservationRejetSignalementDAO _observationRejetSignalementDAO;
	
	@Override
	public void insert(int idSignalement, Integer idRaisonRejet, String observationRejetComment) {
		_observationRejetSignalementDAO.insert(idSignalement, idRaisonRejet, observationRejetComment);
	}

	@Override
	public void delete(int idSignalement, Integer idRaisonRejet) {
		_observationRejetSignalementDAO.delete(idSignalement, idRaisonRejet);
	}

	@Override
	public List<ObservationRejet> findByIdSignalement(int idSignalement) {
		return _observationRejetSignalementDAO.findByIdSignalement(idSignalement);
	}

}
