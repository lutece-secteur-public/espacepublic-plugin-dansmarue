package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import fr.paris.lutece.plugins.dansmarue.business.dao.IDashboardPeriodDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.DashboardPeriod;
import fr.paris.lutece.plugins.dansmarue.service.IDashboardPeriodService;
import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;

public class DashboardPeriodService implements IDashboardPeriodService{

	@Inject
	@Named("signalement.dashboardPeriodDAO")
	private IDashboardPeriodDAO _dashboardPeriodDAO;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long insert(DashboardPeriod dashboardPeriod) {
		return _dashboardPeriodDAO.insert(dashboardPeriod,SignalementPlugin.getPlugin());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(long lId) {
		_dashboardPeriodDAO.remove(lId, SignalementPlugin.getPlugin());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DashboardPeriod load(long lId) {
		return _dashboardPeriodDAO.load(lId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(DashboardPeriod dashboardPeriod) {
		_dashboardPeriodDAO.update(dashboardPeriod, SignalementPlugin.getPlugin());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DashboardPeriod> getDashboardPeriodCriterias() {
		return _dashboardPeriodDAO.getDashboardPeriodCriterias();
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public List<DashboardPeriod> getAllDashboardPeriods() {
		return _dashboardPeriodDAO.getAllDashboardPeriods();
	}


}
