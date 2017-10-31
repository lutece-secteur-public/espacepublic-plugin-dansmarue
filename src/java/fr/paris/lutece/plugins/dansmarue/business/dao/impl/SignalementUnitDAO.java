package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementUnitDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementUnit;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

public class SignalementUnitDAO implements ISignalementUnitDAO{

	private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_unit(fk_id_unit, visible) VALUES (?, ?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_unit WHERE fk_id_unit=?";
    private static final String SQL_QUERY_SELECT = "SELECT id_unit, id_parent, label, description, visible FROM "
    		+ " unittree_unit unit"
    		+ " LEFT JOIN signalement_unit su ON su.fk_id_unit = unit.id_unit WHERE id_unit = ?";
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_unit SET visible=? WHERE fk_id_unit = ?";
    private static final String SQL_QUERY_DELETE_ALL = "DELETE FROM signalement_unit";
    private static final String SQL_QUERY_SELECT_ALL_VISIBLE_UNITS = "SELECT fk_id_unit FROM signalement_unit WHERE visible = 1";
    private static final String SQL_QUERY_SELECT_UNITS_IDS = "SELECT id_unit FROM unittree_unit";
    
    /**
     * Save a new signalementUnit
     * 
     */
    @Override
    public Integer insert( SignalementUnit signalementUnit, Plugin plugin)
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT,plugin );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, signalementUnit.getIdUnit( ) );
        daoUtil.setBoolean( nIndex++, signalementUnit.isVisible() );
        daoUtil.executeUpdate( );
        daoUtil.free( );

        return signalementUnit.getIdUnit( );
    }


    /**
     * Delete an signalementUnit
     * 
     * @param lId the signalementUnit id
     */
    @Override
    public void remove( long lId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setLong( 1, lId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Deletes all from the table
     */
    @Override
    public void removeAll(){
    	DAOUtil daoUtil = new DAOUtil(SQL_QUERY_DELETE_ALL);
    	daoUtil.executeUpdate();
    	daoUtil.free();
    }
    
    /**
     * Load a signalementUnit
     * 
     * @param lId the signalementUnit id
     */
    @Override
    public SignalementUnit load( Integer lId )
    {
    	SignalementUnit signalementUnit = new SignalementUnit( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setLong( 1, lId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            signalementUnit.setIdUnit(daoUtil.getInt(nIndex++));
            signalementUnit.setIdParent(daoUtil.getInt(nIndex++));
            signalementUnit.setLabel(daoUtil.getString(nIndex++));
            signalementUnit.setDescription(daoUtil.getString(nIndex++));
        }

        daoUtil.free( );

        return signalementUnit;
    }

    /**
     * Store a signalementUnit
     * 
     * @param signalementUnit the signalementUnit object
     */
    @Override
    public void store( SignalementUnit signalementUnit)
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nIndex = 1;
        daoUtil.setBoolean( nIndex++, signalementUnit.isVisible( ) );
        
        //WHERE
        daoUtil.setLong( nIndex++, signalementUnit.getIdUnit());

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Gets all units ids which are visible 
     * @return list containing the id of visible units
     */
    @Override
    public List<Integer> getVisibleUnitsIds(){
    	 DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_ALL_VISIBLE_UNITS);
    	 List<Integer> visibleUnitsIds = new ArrayList<Integer>();
    	 daoUtil.executeQuery( );
         while ( daoUtil.next( ) )
         {
        	int nIndex = 1;
        	visibleUnitsIds.add(daoUtil.getInt(nIndex++));
         }
 		 daoUtil.free();
         return visibleUnitsIds;
    }
    
    /**
     * Gets all units ids
     * @return list containing the id of units
     */
    @Override
    public List<Integer> getAllUnitsIds(){
    	DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_UNITS_IDS);
    	List<Integer> visibleUnitsIds = new ArrayList<Integer>();
    	daoUtil.executeQuery( );
        while( daoUtil.next( ) )
        {
	       	int nIndex = 1;
	       	visibleUnitsIds.add(daoUtil.getInt(nIndex++));
        }
		daoUtil.free();
        return visibleUnitsIds;
    }
    
}
