package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.dao.IDashboardUserPreferencesDAO;
import fr.paris.lutece.util.sql.DAOUtil;

public class DashboardUserPreferencesDAO implements IDashboardUserPreferencesDAO {

	private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_dashboard_user_preferences(fk_id_user, fk_id_state) VALUES (?,?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_dashboard_user_preferences WHERE fk_id_user = ? AND fk_id_state = ?";
    private static final String SQL_QUERY_SELECT_BY_USER_ID = "SELECT fk_id_state FROM signalement_dashboard_user_preferences WHERE fk_id_user = ?";
	
    /**
     * {@inheritDoc}
     */
    @Override
	public void insert(Integer idUser, Integer idState) {
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_INSERT);
		int nIndex = 1;
		daoUtil.setInt(nIndex++, idUser);
		daoUtil.setInt(nIndex++, idState);
		
		daoUtil.executeUpdate();
		
		daoUtil.free();
	}
    
    /**
     * {@inheritDoc}
     */
	@Override
	public void remove(Integer idUser, Integer idState) {
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_DELETE);
		int nIndex = 1;
		daoUtil.setInt(nIndex++, idUser);
		daoUtil.setInt(nIndex++, idState);
		
		daoUtil.executeUpdate();
		
		daoUtil.free();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Integer> findUserDashboardStates(Integer idUser) {
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_BY_USER_ID);
		int nIndex = 1;
		daoUtil.setInt(nIndex++, idUser);
		
		daoUtil.executeQuery();
		
		List<Integer> userPreferedStates = new ArrayList<>();
		while(daoUtil.next()){
			nIndex=1;
			userPreferedStates.add(daoUtil.getInt(nIndex++));
		}
		
		daoUtil.free();
		
		return userPreferedStates;
	}

	
}
