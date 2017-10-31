package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.dao.IObservationRejetSignalementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;
import fr.paris.lutece.util.sql.DAOUtil;

public class ObservationRejetSignalementDAO implements IObservationRejetSignalementDAO{

	private static final String SQL_QUERY_NEW_PK = "SELECT nextval('seq_observation_rejet_signalement')";
	private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_observation_rejet_signalement (id_observation_rejet_signalement, fk_id_signalement, fk_id_observation_rejet, observation_rejet_comment) "
			+ "VALUES (?,?,?,?)";
	
	private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_observation_rejet_signalement WHERE id_signalement=? AND id_observation_rejet=?";
	private static final String SQL_QUERY_FIND_BY_ID_SIGNALEMENT = "SELECT id_observation_rejet, libelle, actif, ordre, observation_rejet_comment FROM signalement_observation_rejet sor " 
				+ " RIGHT JOIN signalement_observation_rejet_signalement sors "
				+ " ON sors.fk_id_observation_rejet = sor.id_observation_rejet WHERE fk_id_signalement=?";
	
	
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
	
	
	@Override
	public void insert(int idSignalement, Integer idRaisonRejet, String observationRejetComment) {
		 DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
		 Long idOservationRejetSignalement = newPrimaryKey( );
         int nIndex = 1; 
         
         daoUtil.setLong(nIndex++,idOservationRejetSignalement);
         daoUtil.setLong(nIndex++,idSignalement);
         if(null != idRaisonRejet){
        	 daoUtil.setInt(nIndex++, idRaisonRejet);
         }else{
        	 daoUtil.setIntNull(nIndex++);
         }
         daoUtil.setString(nIndex++, observationRejetComment);
         
         daoUtil.executeUpdate();
         
         daoUtil.free();		
	}

	@Override
	public void delete(int idSignalement, Integer idRaisonRejet) {
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_DELETE);
		
		int nIndex = 1;
				

		daoUtil.setInt(nIndex++, idSignalement);
		
		if(null != idRaisonRejet){
       	 daoUtil.setInt(nIndex++, idRaisonRejet);
        }else{
       	 daoUtil.setIntNull(nIndex++);
        }
		
		daoUtil.executeUpdate();
		daoUtil.free();
		
	}

	@Override
	public List<ObservationRejet> findByIdSignalement(int idSignalement) {
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_FIND_BY_ID_SIGNALEMENT);
		int nIndex = 1;
		
		daoUtil.setInt(nIndex++, idSignalement);
		
		daoUtil.executeQuery();
		
		List<ObservationRejet> observationRejets = new ArrayList<>();
		
		while(daoUtil.next()){
			nIndex = 1;
			ObservationRejet observationRejet = new ObservationRejet();
			Long observationRejetId = ((Long)daoUtil.getObject(nIndex++));
			if(null != observationRejetId){
				observationRejet.setId(observationRejetId.intValue());
				observationRejet.setLibelle(daoUtil.getString(nIndex++));
				observationRejet.setActif(daoUtil.getBoolean(nIndex++));
				observationRejet.setOrdre(daoUtil.getInt(nIndex++));
			}else{
				nIndex++;
				nIndex++;
				nIndex++;
				observationRejet.setLibelle(daoUtil.getString(nIndex++));
			}
			observationRejets.add(observationRejet);
		}
		
		daoUtil.free();
		
		return observationRejets;
		
	}

}
