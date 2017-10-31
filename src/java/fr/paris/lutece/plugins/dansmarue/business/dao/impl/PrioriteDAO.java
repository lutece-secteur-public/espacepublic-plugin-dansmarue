package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import fr.paris.lutece.plugins.dansmarue.business.dao.IPrioriteDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Priorite;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


public class PrioriteDAO implements IPrioriteDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT nextval('seq_signalement_priorite_id_priorite')";
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_priorite(id_priorite, libelle) VALUES (?, ?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_priorite WHERE id_priorite=?";
    private static final String SQL_QUERY_SELECT = "SELECT id_priorite, libelle FROM signalement_priorite WHERE id_priorite = ?";
    private static final String SQL_QUERY_SELECT_ALL = "SELECT id_priorite, libelle, ordre_priorite FROM signalement_priorite ORDER BY ordre_priorite";
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_priorite SET id_priorite=?, libelle=? WHERE id_priorite = ?";

   
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
     * Select all the priorites
     * 
     */
    
    public List<Priorite> getAllPriorite( )
    {
        List<Priorite> listResult = new ArrayList( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;
            Priorite priorite = new Priorite( );
            priorite.setId( daoUtil.getLong( nIndex++ ) );
            priorite.setLibelle( daoUtil.getString( nIndex++ ) );
            priorite.setOrdrePriorite(daoUtil.getInt(nIndex ++) );
            listResult.add( priorite );
        }

        daoUtil.free( );

        return listResult;

    }
    
    
    
    /**
     * Save a new priorite
     * 
     */
    public Long insert( Priorite priorite )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        if ( priorite.getId( ) == null || priorite.getId( ) == 0 )
        {
            priorite.setId( newPrimaryKey( ) );
        }
        int nIndex = 1;
        daoUtil.setLong( nIndex++, priorite.getId( ) );
        daoUtil.setString( nIndex++, priorite.getLibelle( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );

        return priorite.getId( );
    }

    /**
     * Delete an priorite
     * 
     * @param lId the priorite id
     */
    public void remove( long lId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setLong( 1, lId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Load a priorite
     * 
     * @param lId the priorite id
     */
    public Priorite load( long lId )
    {
        Priorite priorite = new Priorite( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setLong( 1, lId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            priorite.setId( daoUtil.getLong( nIndex++ ) );
            priorite.setLibelle( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free( );

        return priorite;
    }

    /**
     * Store a priorite
     * 
     * @param priorite the priorite object
     */
    public void store( Priorite priorite )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, priorite.getId( ) );
        daoUtil.setString( nIndex++, priorite.getLibelle( ) );
        //WHERE
        daoUtil.setLong( nIndex++, priorite.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }


}
