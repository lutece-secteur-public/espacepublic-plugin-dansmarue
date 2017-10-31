package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import fr.paris.lutece.plugins.dansmarue.business.dao.IObservationRejetDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


public class ObservationRejetDAO implements IObservationRejetDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT nextval('seq_signalement_observation_rejet_id_observation_rejet')";
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_observation_rejet(id_observation_rejet, libelle, actif, ordre) VALUES (?, ?, ?, ?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_observation_rejet WHERE id_observation_rejet=?";
    private static final String SQL_QUERY_SELECT = "SELECT id_observation_rejet, libelle, actif, ordre FROM signalement_observation_rejet WHERE id_observation_rejet = ?";
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_observation_rejet SET id_observation_rejet=?, libelle=?, actif=? WHERE id_observation_rejet = ?";
    private static final String SQL_QUERY_SELECT_ALL_OBSERVATION_REJET = "SELECT id_observation_rejet, libelle, actif, ordre FROM signalement_observation_rejet ORDER BY ordre";
    private static final String SQL_QUERY_SELECT_ALL_OBSERVATION_REJET_ACTIF = "SELECT id_observation_rejet, libelle, actif, ordre FROM signalement_observation_rejet WHERE actif=1 ORDER BY ordre";
    private static final String SQL_QUERY_EXISTS_OBSERVATION_OBJET = "SELECT id_observation_rejet FROM signalement_observation_rejet WHERE libelle=?";
    private static final String SQL_QUERY_EXISTS_OBSERVATION_OBJET_WITH_ID = "SELECT id_observation_rejet FROM signalement_observation_rejet WHERE libelle=? AND NOT id_observation_rejet=? ";
    private static final String SQL_QUERY_UPDATE_OBSERVATION_REJET_ORDRE = "UPDATE signalement_observation_rejet SET ordre = ? WHERE id_observation_rejet = ?";
    private static final String SQL_QUERY_DECREASE_ORDER_OF_NEXT = "UPDATE signalement_observation_rejet SET ordre = ordre-1 WHERE ordre= (? + 1)";
    private static final String SQL_QUERY_INCREASE_ORDER_OF_PREVIOUS = "UPDATE signalement_observation_rejet SET ordre = ordre+1 WHERE ordre= (? - 1)";
    private static final String SQL_QUERY_INCREASE_ORDER_OF_ALL_NEXT = "UPDATE signalement_observation_rejet SET ordre = ordre+1 WHERE ordre >= "
    		+ "(SELECT ordre FROM signalement_observation_rejet WHERE id_observation_rejet=?) AND id_observation_rejet != ?";
    private static final String SQL_QUERY_DECREASE_ORDER_OF_ALL_NEXT = "UPDATE signalement_observation_rejet SET ordre = ordre-1 WHERE ordre >= "
    		+ "(SELECT ordre FROM signalement_observation_rejet WHERE id_observation_rejet=?)";
    private static final String SQL_QUERY_GET_OBSERVATION_REJET_COUNT = "SELECT count(id_observation_rejet) FROM signalement_observation_rejet";
    
    private static final String SQL_COUNT_BY_ID_OBSERVATION_REJET = "SELECT count(*) FROM signalement_observation_rejet_signalement WHERE fk_id_observation_rejet = ?";
    
    /**
     * Generates a new primary key
     * 
     * @param plugin the plugin
     * @return The new primary key
     */
    private Integer newPrimaryKey( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery( );
        Integer nKey = null;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 );
        }
        daoUtil.free( );

        return nKey.intValue( );
    }

    /**
     * Save a new observationRejet
     * 
     */
    public Integer insert( ObservationRejet observationRejet )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        if ( observationRejet.getId( ) == null || observationRejet.getId( ) == 0 )
        {
            observationRejet.setId( newPrimaryKey( ) );
        }
        int nIndex = 1;
        daoUtil.setLong( nIndex++, observationRejet.getId( ) );
        daoUtil.setString( nIndex++, observationRejet.getLibelle( ) );
        daoUtil.setBoolean( nIndex++, observationRejet.getActif( ) );
        daoUtil.setLong(nIndex, observationRejet.getOrdre());
        daoUtil.executeUpdate( );
        daoUtil.free( );

        return observationRejet.getId( );
    }


    /**
     * Delete an observationRejet
     * 
     * @param lId the observationRejet id
     */
    public void remove( long lId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setLong( 1, lId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Load a observationRejet
     * 
     * @param lId the observationRejet id
     */
    public ObservationRejet load( Integer lId )
    {
        ObservationRejet observationRejet = new ObservationRejet( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setLong( 1, lId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            observationRejet.setId( daoUtil.getInt( nIndex++ ) );
            observationRejet.setLibelle( daoUtil.getString( nIndex++ ) );
            observationRejet.setActif( daoUtil.getBoolean( nIndex++ ) );
            observationRejet.setOrdre(daoUtil.getInt( nIndex++ ));
        }

        daoUtil.free( );

        return observationRejet;
    }

    /**
     * Store a observationRejet
     * 
     * @param observationRejet the observationRejet object
     */
    public void store( ObservationRejet observationRejet )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, observationRejet.getId( ) );
        daoUtil.setString( nIndex++, observationRejet.getLibelle( ) );
        daoUtil.setBoolean( nIndex++, observationRejet.getActif( ) );
        
        //WHERE
        daoUtil.setLong( nIndex++, observationRejet.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Return all ObservationRejet
     * @return a list of ObservationRejet
     */
    public List<ObservationRejet> getAllObservationRejet( Plugin plugin )
    {

        List<ObservationRejet> listResult = new ArrayList<ObservationRejet>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_OBSERVATION_REJET, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;

            ObservationRejet observationRejet = new ObservationRejet( );
            observationRejet.setId( daoUtil.getInt( nIndex++ ) );
            observationRejet.setLibelle( daoUtil.getString( nIndex++ ) );
            observationRejet.setActif( daoUtil.getBoolean( nIndex++ ) );
            observationRejet.setOrdre( daoUtil.getInt( nIndex++ ));
            listResult.add( observationRejet );
        }

        daoUtil.free( );

        return listResult;

    }

    /**
     * Return all ObservationRejet actifs
     * @return a list of ObservationRejet actifs
     */
    public List<ObservationRejet> getAllObservationRejetActif( )
    {

        List<ObservationRejet> listResult = new ArrayList<ObservationRejet>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_OBSERVATION_REJET_ACTIF );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;

            ObservationRejet observationRejet = new ObservationRejet( );
            observationRejet.setId( daoUtil.getInt( nIndex++ ) );
            observationRejet.setLibelle( daoUtil.getString( nIndex++ ) );
            observationRejet.setActif( daoUtil.getBoolean( nIndex++ ) );
            observationRejet.setOrdre( daoUtil.getInt( nIndex++ ));
            listResult.add( observationRejet );
        }

        daoUtil.free( );

        return listResult;

    }

    /**
     * Check if the observationRejet already exists
     * @return boolean
     */
    public boolean existsObservationRejet( ObservationRejet observationRejet )
    {
        boolean existsObservationRejet = false;

        DAOUtil daoUtil;

        if ( observationRejet.getId( ) != null && observationRejet.getId( ) > 0 )
        {
            daoUtil = new DAOUtil( SQL_QUERY_EXISTS_OBSERVATION_OBJET_WITH_ID );
            daoUtil.setString( 1, observationRejet.getLibelle( ) );
            daoUtil.setLong( 2, observationRejet.getId( ) );
        }
        else
        {
            daoUtil = new DAOUtil( SQL_QUERY_EXISTS_OBSERVATION_OBJET );
            daoUtil.setString( 1, observationRejet.getLibelle( ) );
        }


        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            existsObservationRejet = true;
        }

        daoUtil.free( );

        return existsObservationRejet;
    }
    
    /**
     * Decreases the order of the next rejet
     * @param observationRejet
     */
    public void decreaseOrdreOfNextRejet(ObservationRejet observationRejet){
    	DAOUtil daoUtil;
    	if(observationRejet.getOrdre() != null){
    		int nIndex=1;
    		daoUtil = new DAOUtil(SQL_QUERY_DECREASE_ORDER_OF_NEXT);
    		daoUtil.setInt(nIndex++, observationRejet.getOrdre());
        	daoUtil.executeUpdate();
        	daoUtil.free();
    	}
    }
    
    /**
     * Increases the order of the previous rejet
     * @param observationRejet
     */
    public void increaseOrdreOfPreviousRejet(ObservationRejet observationRejet){
    	DAOUtil daoUtil;
    	if(observationRejet.getOrdre() != null){
    		int nIndex=1;
    		daoUtil = new DAOUtil(SQL_QUERY_INCREASE_ORDER_OF_PREVIOUS);
    		daoUtil.setInt(nIndex++, observationRejet.getOrdre());
        	daoUtil.executeUpdate();
        	daoUtil.free();
    	}
    }
    
    /**
     * Updates an observation rejet order, by its id
     * @param observationRejet containing the id
     */
    public void updateObservationRejetOrdre(ObservationRejet observationRejet){
    	DAOUtil daoUtil = new DAOUtil(SQL_QUERY_UPDATE_OBSERVATION_REJET_ORDRE);
    	int nIndex = 1;
    	daoUtil.setInt(nIndex++, observationRejet.getOrdre());
    	daoUtil.setInt(nIndex++, observationRejet.getId());
    	daoUtil.executeUpdate();
    }
    
    /**
     * Increases all the next orders
     * @param nIdObservationRejet
     */
    public void increaseOrdreOfAllNext(int nIdObservationRejet){
    	DAOUtil daoUtil = new DAOUtil(SQL_QUERY_INCREASE_ORDER_OF_ALL_NEXT);
		int nIndex=1;
		daoUtil.setInt(nIndex++, nIdObservationRejet);
		daoUtil.setInt(nIndex++, nIdObservationRejet);
    	daoUtil.executeUpdate();
    	daoUtil.free();
    }
    
    /**
     * Decreases all the next orders
     * @param nIdObservationRejet
     */
    public void decreaseOrdreOfAllNext(int nIdObservationRejet){
    	DAOUtil daoUtil = new DAOUtil(SQL_QUERY_DECREASE_ORDER_OF_ALL_NEXT);
		int nIndex=1;
		daoUtil.setInt(nIndex++, nIdObservationRejet);
    	daoUtil.executeUpdate();
    	daoUtil.free();
	}
    
    /**
     * Returns the number of observation rejet existing
     * @return
     */
    public int getObservationRejetCount(){
    	DAOUtil daoUtil = new DAOUtil(SQL_QUERY_GET_OBSERVATION_REJET_COUNT);
    	daoUtil.executeQuery();
    	int observationRejetCount = 0;
    	if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            observationRejetCount = daoUtil.getInt(nIndex++);
            
        }
    	daoUtil.free();
    	return observationRejetCount;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int countByIdObservationRejet(int idObservationRejet){
    	DAOUtil daoUtil = new DAOUtil(SQL_COUNT_BY_ID_OBSERVATION_REJET);
    	int nIndex = 1;
    	daoUtil.setInt(nIndex, idObservationRejet);
    	daoUtil.executeQuery();
    	int observationRejetCount = 0;
    	if ( daoUtil.next( ) )
        {
            nIndex = 1;
            observationRejetCount = daoUtil.getInt(nIndex++);
            
        }
    	daoUtil.free();
    	return observationRejetCount;
    	
    }
    
    
}
