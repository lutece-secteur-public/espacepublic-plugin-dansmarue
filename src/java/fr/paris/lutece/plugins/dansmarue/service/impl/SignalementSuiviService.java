package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementSuiviDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementSuivi;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementSuiviService;
import fr.paris.lutece.plugins.sira.business.entities.SiraUser;

public class SignalementSuiviService implements ISignalementSuiviService {

	@Inject
	@Named("signalementSuiviDAO")
	private ISignalementSuiviDAO signalementSuiviDAO;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long insert(SignalementSuivi signalementSuivi) {
		return signalementSuiviDAO.insert(signalementSuivi);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(long lId) {
		signalementSuiviDAO.remove(lId);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SignalementSuivi load(long lId) {
		return signalementSuiviDAO.load(lId);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(SignalementSuivi signalementSuivi) {
		signalementSuiviDAO.update(signalementSuivi);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SiraUser> findUsersMobilesByIdSignalement(long idSignalement) {
		return signalementSuiviDAO.findUsersMobileByIdSignalement(idSignalement);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(SignalementSuivi signalementSuivi){
		long idSuivi = signalementSuiviDAO.findByIdSignalementAndGuid(signalementSuivi.getIdSignalement(), signalementSuivi.getUserGuid());
		if(idSuivi != -1){
			remove(idSuivi);
		}
	}
	
	@Override
	public void removeByIdSignalement(long idSignalement){
		signalementSuiviDAO.removeByIdSignalement(idSignalement);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> findUsersMailByIdSignalement(long idSignalement){
		return signalementSuiviDAO.findUsersMailByIdSignalement(idSignalement);
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getNbFollowersByIdSignalement(int idSignalement){
    	return signalementSuiviDAO.getNbFollowersByIdSignalement(idSignalement);
    }
    
    /**
     * {@inheritDoc}
     */
	@Override
	public long findByIdSignalementAndGuid(long idSignalement, String guid) {
		return signalementSuiviDAO.findByIdSignalementAndGuid(idSignalement, guid);
	}
    

}
