package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementSuiviDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementSuivi;
import fr.paris.lutece.plugins.dansmarue.business.entities.SiraUser;
import fr.paris.lutece.util.sql.DAOUtil;

public class SignalementSuiviDAO implements ISignalementSuiviDAO {

	private static final String SQL_QUERY_NEW_PK = "SELECT nextval('seq_signalement_suivi_id')";
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_suivi(id_suivi, fk_id_signalement, fk_user_guid) VALUES (?,?,?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_suivi WHERE id_suivi = ?";
    private static final String SQL_QUERY_SELECT = "SELECT id_suivi, fk_id_signalement, fk_user_guid FROM signalement_suivi WHERE id_suivi = ?";
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_suivi SET fk_id_signalement=?, fk_user_guid=? WHERE id_suivi=?";
	private static final String SQL_QUERY_SELECT_USERS_MOBILES_BY_ID_SIGNALEMENT = "SELECT id_sira_user, user_guid, user_udid, user_device, user_email, user_token FROM sira_user INNER JOIN signalement_suivi ON fk_user_guid = user_guid WHERE fk_id_signalement = ?";
	private static final String SQL_QUERY_SELECT_BY_ID_SIGNALEMENT_USER_GUID = "SELECT id_suivi FROM signalement_suivi WHERE fk_id_signalement=? AND fk_user_guid=?";
	private static final String SQL_QUERY_SELECT_SIGNALEMENTS_BY_GUID = "SELECT fk_id_signalement FROM signalement_suivi WHERE fk_user_guid = ?";
	private static final String SQL_QUERY_SELECT_USERS_MAIL_BY_ID_SIGNALEMENT = "SELECT DISTINCT user_email FROM sira_user INNER JOIN signalement_suivi ON fk_user_guid = user_guid WHERE fk_id_signalement = ?";
	private static final String SQL_QUERY_SELECT_FOLLOWERS_COUNT_BY_ID_SIGNALEMENT = "SELECT COUNT(*) FROM signalement_suivi WHERE fk_id_signalement=?";
	private static final String SQL_QUERY_DELETE_BY_ID_SIGNALEMENT = "DELETE FROM signalement_suivi WHERE fk_id_signalement = ?";
	
	
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
	public Long insert(SignalementSuivi signalementSuivi) {
		 DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
         if ( signalementSuivi.getId( ) == null || signalementSuivi.getId( ) == 0 )
         {
        	 signalementSuivi.setId( newPrimaryKey( ) );
         }
         int nIndex = 1; 
         
         daoUtil.setLong(nIndex++,signalementSuivi.getId());
         daoUtil.setLong(nIndex++,signalementSuivi.getIdSignalement());
         daoUtil.setString(nIndex++,signalementSuivi.getUserGuid());
         
         daoUtil.executeUpdate();
         
         daoUtil.free();
         
         return signalementSuivi.getId();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(long lId) {
		DAOUtil daoUtil = new DAOUtil (SQL_QUERY_DELETE);
		daoUtil.setLong(1, lId);
		daoUtil.executeUpdate();
        daoUtil.free();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SignalementSuivi load(long lId) {
		DAOUtil daoUtil = new DAOUtil (SQL_QUERY_SELECT);
		
		int nIndex = 1;
		
		daoUtil.setLong(nIndex++, lId);
		
		daoUtil.executeQuery();
		
		SignalementSuivi signalementSuivi = null;
		
		if(daoUtil.next()){
			nIndex = 1;
			signalementSuivi = new SignalementSuivi();
			signalementSuivi.setId(daoUtil.getLong(nIndex++));
			signalementSuivi.setIdSignalement(daoUtil.getLong(nIndex++));
			signalementSuivi.setUserGuid(daoUtil.getString(nIndex++));
		}

        daoUtil.free();
		return signalementSuivi;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(SignalementSuivi signalementSuivi) {
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_UPDATE);
        
		int nIndex = 1;
		
        daoUtil.setLong(nIndex++,signalementSuivi.getIdSignalement());
        daoUtil.setString(nIndex++,signalementSuivi.getUserGuid());
        
        //WHERE
        daoUtil.setLong(nIndex++,signalementSuivi.getId());
        
        daoUtil.executeUpdate();
        daoUtil.free();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SiraUser> findUsersMobileByIdSignalement(long idSignalement) {
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_USERS_MOBILES_BY_ID_SIGNALEMENT);
		int nIndex = 1;
		
		daoUtil.setLong(nIndex++, idSignalement);

		daoUtil.executeQuery();
		
		List<SiraUser> siraUserList = new ArrayList<>();
		while(daoUtil.next()){
			nIndex = 1;
			SiraUser siraUser = new SiraUser();
			siraUser.setId(daoUtil.getLong(nIndex++));
			siraUser.setGuid(daoUtil.getString(nIndex++));
			siraUser.setUdid(daoUtil.getString(nIndex++));
			siraUser.setDevice(daoUtil.getString(nIndex++));
			siraUser.setMail(daoUtil.getString(nIndex++));
			siraUser.setToken(daoUtil.getString(nIndex++));
			
			siraUserList.add(siraUser);
		}
		daoUtil.free();
		return siraUserList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> findUsersMailByIdSignalement(long idSignalement) {
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_USERS_MAIL_BY_ID_SIGNALEMENT);
		int nIndex = 1;
		
		daoUtil.setLong(nIndex++, idSignalement);

		daoUtil.executeQuery();
		
		List<String> followersMails = new ArrayList<>();
		while(daoUtil.next()){
			nIndex = 1;
			String mail = daoUtil.getString(nIndex++);
			
			followersMails.add(mail);
		}
		daoUtil.free();
		return followersMails;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long findByIdSignalementAndGuid(long idSignalement, String guid) {
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_BY_ID_SIGNALEMENT_USER_GUID);
		int nIndex = 1;
		daoUtil.setLong(nIndex++, idSignalement);
		daoUtil.setString(nIndex++, guid);
		
		daoUtil.executeQuery();
		
		long idSuivi = -1;
		
		if(daoUtil.next()){
			nIndex=1;
			idSuivi = daoUtil.getLong(nIndex++);
		}
		
		daoUtil.free();
		
		return idSuivi;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Long> findSignalementsByGuid(String guid){
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_SIGNALEMENTS_BY_GUID);
		int nIndex = 1;
		daoUtil.setString(nIndex++, guid);
		
		daoUtil.executeQuery();
		
		List<Long> signalementsIds = new ArrayList<>();
		while(daoUtil.next()){
			nIndex = 1;
			Long idSignalement = daoUtil.getLong(nIndex);
			signalementsIds.add(idSignalement);
		}
		daoUtil.free();
		return signalementsIds;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getNbFollowersByIdSignalement(long idSignalement){
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_FOLLOWERS_COUNT_BY_ID_SIGNALEMENT);
		int nIndex = 1;
		
		daoUtil.setLong(nIndex++, idSignalement);
		
		int count = 0;
		
		daoUtil.executeQuery();
		
		if(daoUtil.next()){
			nIndex = 1;
			count = daoUtil.getInt(nIndex++);
		}
		
		daoUtil.free();
		
		return count;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeByIdSignalement(long idSignalement){
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_DELETE_BY_ID_SIGNALEMENT);
		int nIndex = 1;
		daoUtil.setLong(nIndex, idSignalement);
		
		daoUtil.executeUpdate();
		
		daoUtil.free();
	}
	
}
