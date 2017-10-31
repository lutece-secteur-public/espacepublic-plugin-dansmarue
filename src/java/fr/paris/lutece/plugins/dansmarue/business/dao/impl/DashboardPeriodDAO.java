package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.dao.IDashboardPeriodDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.DashboardPeriod;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

public class DashboardPeriodDAO implements IDashboardPeriodDAO {

	private static final String SQL_QUERY_NEW_PK = "SELECT nextval('seq_signalement_dashboard_period_id')";
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_dashboard_period(id_period, libelle, lower_bound, higher_bound, unit, category, ordre) VALUES (?,?,?,?,?,?,?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_dashboard_period WHERE id_period = ?";
    private static final String SQL_QUERY_SELECT = "SELECT id_period, libelle, lower_bound, higher_bound, unit, category, ordre FROM signalement_dashboard_period WHERE id_period = ?";
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_dashboard_period SET libelle=?, lower_bound=?, higher_bound=?, unit=?, category=?, ordre=? WHERE id_period=?";
	private static final String SQL_QUERY_DASHBOARD_CRITERIAS = "SELECT id_period, libelle, lower_bound, higher_bound, unit, category, ordre FROM signalement_dashboard_period WHERE category is null"
			+ " ORDER BY ordre";
	private static final String SQL_QUERY_ALL_DASHBOARD_PERIODS = "SELECT id_period, libelle, lower_bound, higher_bound, unit, category, ordre FROM signalement_dashboard_period"
			+ " ORDER BY category, ordre";
    
    /**
     * Generates a new primary key
     * 
     * @param plugin the plugin
     * @return The new primary key
     */
    private Long newPrimaryKey( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery( );
        Long nKey = null;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getLong( 1 );
        }
        daoUtil.free( );
        return nKey;
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long insert(DashboardPeriod dashboardPeriod, Plugin plugin) {
		 DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
         if ( dashboardPeriod.getId( ) == null || dashboardPeriod.getId( ) == 0 )
         {
        	 dashboardPeriod.setId( newPrimaryKey( ) );
         }
         int nIndex = 1; 
         
         daoUtil.setLong(nIndex++,dashboardPeriod.getId());
         daoUtil.setString(nIndex++,dashboardPeriod.getLibelle());
         daoUtil.setInt(nIndex++,dashboardPeriod.getLowerBound());
         daoUtil.setInt(nIndex++,dashboardPeriod.getHigherBound());
         daoUtil.setString(nIndex++,dashboardPeriod.getUnit());
         daoUtil.setString(nIndex++,dashboardPeriod.getCategory());
         daoUtil.setInt(nIndex++, dashboardPeriod.getOrdre());
         
         daoUtil.executeUpdate();
         
         daoUtil.free();
         
         return dashboardPeriod.getId();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(long lId, Plugin plugin) {
		DAOUtil daoUtil = new DAOUtil (SQL_QUERY_DELETE, plugin);
		daoUtil.setLong(1, lId);
		daoUtil.executeUpdate();
        daoUtil.free();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DashboardPeriod load(long lId) {
		DAOUtil daoUtil = new DAOUtil (SQL_QUERY_SELECT);
		
		int nIndex = 1;
		
		daoUtil.setLong(nIndex++, lId);
		
		daoUtil.executeQuery();
		
		DashboardPeriod dashboardPeriod = null;
		
		if(daoUtil.next()){
			nIndex = 1;
			dashboardPeriod = new DashboardPeriod();
			dashboardPeriod.setId(daoUtil.getLong(nIndex++));
			dashboardPeriod.setLibelle(daoUtil.getString(nIndex++));
			dashboardPeriod.setLowerBound((Integer)daoUtil.getObject(nIndex++));
			dashboardPeriod.setHigherBound((Integer)daoUtil.getObject(nIndex++));
			dashboardPeriod.setUnit(daoUtil.getString(nIndex++));
			dashboardPeriod.setCategory(daoUtil.getString(nIndex++));
			dashboardPeriod.setOrdre(daoUtil.getInt(nIndex++));
		}

        daoUtil.free();
		return dashboardPeriod;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(DashboardPeriod dashboardPeriod, Plugin plugin) {
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_UPDATE, plugin);
        
		int nIndex = 1;
		
        daoUtil.setString(nIndex++,dashboardPeriod.getLibelle());
        daoUtil.setInt(nIndex++,dashboardPeriod.getLowerBound());
        daoUtil.setInt(nIndex++,dashboardPeriod.getHigherBound());
        daoUtil.setString(nIndex++,dashboardPeriod.getUnit());
        daoUtil.setString(nIndex++,dashboardPeriod.getCategory());
        
        //WHERE
        daoUtil.setLong(nIndex++,dashboardPeriod.getId());
        
        daoUtil.executeUpdate();
        daoUtil.free();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DashboardPeriod> getDashboardPeriodCriterias(){
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_DASHBOARD_CRITERIAS);
		int nIndex = 1;
		daoUtil.executeQuery();
		
		List<DashboardPeriod> dashboardPeriodCriteriaList = new ArrayList<>();
		while(daoUtil.next()){
			nIndex = 1;
			DashboardPeriod dashboardPeriod = new DashboardPeriod();
			dashboardPeriod = new DashboardPeriod();
			dashboardPeriod.setId(daoUtil.getLong(nIndex++));
			dashboardPeriod.setLibelle(daoUtil.getString(nIndex++));
			dashboardPeriod.setLowerBound((Integer)daoUtil.getObject(nIndex++));
			dashboardPeriod.setHigherBound((Integer)daoUtil.getObject(nIndex++));
			dashboardPeriod.setUnit(daoUtil.getString(nIndex++));
			dashboardPeriod.setCategory(daoUtil.getString(nIndex++));
			dashboardPeriod.setOrdre(daoUtil.getInt(nIndex++));
			
			dashboardPeriodCriteriaList.add(dashboardPeriod);
		}
		daoUtil.free();
		return dashboardPeriodCriteriaList;
	}

	@Override
	public List<DashboardPeriod> getAllDashboardPeriods() {
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_ALL_DASHBOARD_PERIODS);
		int nIndex = 1;
		daoUtil.executeQuery();
		
		List<DashboardPeriod> dashboardPeriodCriteriaList = new ArrayList<>();
		while(daoUtil.next()){
			nIndex = 1;
			DashboardPeriod dashboardPeriod = new DashboardPeriod();
			dashboardPeriod = new DashboardPeriod();
			dashboardPeriod.setId(daoUtil.getLong(nIndex++));
			dashboardPeriod.setLibelle(daoUtil.getString(nIndex++));
			dashboardPeriod.setLowerBound((Integer)daoUtil.getObject(nIndex++));
			dashboardPeriod.setHigherBound((Integer)daoUtil.getObject(nIndex++));
			dashboardPeriod.setUnit(daoUtil.getString(nIndex++));
			dashboardPeriod.setCategory(daoUtil.getString(nIndex++));
			dashboardPeriod.setOrdre(daoUtil.getInt(nIndex++));
			
			dashboardPeriodCriteriaList.add(dashboardPeriod);
		}
		daoUtil.free();
		return dashboardPeriodCriteriaList;
	}
	
	
	
}
