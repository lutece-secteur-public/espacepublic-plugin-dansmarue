package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.util.List;

import javax.inject.Inject;

import fr.paris.lutece.plugins.dansmarue.business.dao.IDashboardUserPreferencesDAO;
import fr.paris.lutece.plugins.dansmarue.service.IDashboardUserPreferencesService;

public class DashboardUserPreferencesService implements IDashboardUserPreferencesService{

	
	@Inject
	IDashboardUserPreferencesDAO dashboardUserPreferencesDAO;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insert(Integer idUser, Integer idState) {
		dashboardUserPreferencesDAO.insert(idUser, idState);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(Integer idUser, Integer idState) {
		dashboardUserPreferencesDAO.remove(idUser, idState);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Integer> findUserDashboardStates(Integer idUser) {
		return dashboardUserPreferencesDAO.findUserDashboardStates(idUser);
	}

}
