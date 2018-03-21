package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import fr.paris.lutece.plugins.dansmarue.business.dao.IAdresseDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


public class AdresseDAO implements IAdresseDAO
{
    private static final String SQL_QUERY_NEW_PK = " SELECT nextval('seq_signalement_adresse_id_adresse')";
    //    private static final String SQL_QUERY_INSERT_WITHOUT_GEOM = " INSERT INTO signalement_adresse(id_adresse, adresse, precision_localisation, fk_id_signalement, geom) VALUES (?, ?, ?, ?, null)";
    private static final String SQL_QUERY_INSERT_WITH_GEOM = " INSERT INTO signalement_adresse(id_adresse, adresse, precision_localisation, fk_id_signalement, geom) VALUES (?, ?, ?, ?, ST_SetSRID(ST_MakePoint(?, ?), 4326))";
    private static final String SQL_QUERY_DELETE = " DELETE FROM signalement_adresse WHERE id_adresse = ? ";
    private static final String SQL_QUERY_SELECT = " SELECT id_adresse, adresse, ST_X(geom), ST_Y(geom), precision_localisation, fk_id_signalement FROM signalement_adresse WHERE id_adresse = ? ";
    private static final String SQL_QUERY_SELECT_BY_SIGNALEMENT = " SELECT id_adresse, adresse, ST_X(geom), ST_Y(geom), precision_localisation, fk_id_signalement FROM signalement_adresse WHERE fk_id_signalement = ? ";
    private static final String SQL_QUERY_UPDATE = " UPDATE signalement_adresse SET id_adresse=?, adresse=?, precision_localisation=?, geom=ST_SetSRID(ST_MakePoint(?, ?), 4326), fk_id_signalement=? WHERE id_adresse = ? ";
    private static final String SQL_QUERY_SELECT_LIST_SIGNALEURS = " SELECT id_adresse, adresse, ST_X(geom), ST_Y(geom), precision_localisation, fk_id_signalement FROM signalement_adresse WHERE fk_id_signalement = ? ";
    private static final String SQL_QUERY_SELECT_GEOMETRY = " SELECT ST_X(geom), ST_Y(geom) FROM signalement_adresse WHERE id_adresse = ? ";

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
     * Update an adresse
     * 
     */
    public void update( Adresse adresse )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, adresse.getId( ) );
        daoUtil.setString( nIndex++, adresse.getAdresse( ) );
        daoUtil.setString( nIndex++, adresse.getPrecisionLocalisation( ) );
        daoUtil.setDouble( nIndex++, adresse.getLng( ) );
        daoUtil.setDouble( nIndex++, adresse.getLat( ) );
        daoUtil.setLong( nIndex++, adresse.getSignalement( ).getId( ) );

        daoUtil.setLong( nIndex++, adresse.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Save a new adresse
     * 
     */
    public synchronized Long insert( Adresse adresse )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_WITH_GEOM );
        if ( adresse.getId( ) == null || adresse.getId( ) == 0 )
        {
            adresse.setId( newPrimaryKey( ) );
            
            int nIndex = 1;
            daoUtil.setLong( nIndex++, adresse.getId( ) );
            daoUtil.setString( nIndex++, adresse.getAdresse( ) );
            daoUtil.setString( nIndex++, adresse.getPrecisionLocalisation( ) );
            daoUtil.setLong( nIndex++, adresse.getSignalement( ).getId( ) );
            daoUtil.setDouble( nIndex++, adresse.getLng( ) );
            daoUtil.setDouble( nIndex++, adresse.getLat( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
       

        return adresse.getId( );
    }

    /**
     * Delete an adresse
     * 
     * @param lId the adresse id
     */
    public void remove( long lId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setLong( 1, lId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Load a adresses
     * 
     * @param lId the adresses id
     */
    public Adresse load( long lId )
    {
        Adresse adresse = new Adresse( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setLong( 1, lId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            adresse.setId( daoUtil.getLong( nIndex++ ) );
            adresse.setAdresse( daoUtil.getString( nIndex++ ) );

            adresse.setLng( daoUtil.getDouble( nIndex++ ) );
            adresse.setLat( daoUtil.getDouble( nIndex++ ) );

            adresse.setPrecisionLocalisation( daoUtil.getString( nIndex++ ) );
            Signalement signalement = new Signalement( );
            signalement.setId( daoUtil.getLong( nIndex++ ) );
            adresse.setSignalement( signalement );
        }

        daoUtil.free( );

        return adresse;
    }

    /**
     * Load an adresse by its Id signalement
     * 
     * @param lId the signalement id
     */
    public Adresse loadByIdSignalement( long lId )
    {
        Adresse adresse = new Adresse( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_SIGNALEMENT );
        daoUtil.setLong( 1, lId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            adresse.setId( daoUtil.getLong( nIndex++ ) );
            adresse.setAdresse( daoUtil.getString( nIndex++ ) );

            adresse.setLng( daoUtil.getDouble( nIndex++ ) );
            adresse.setLat( daoUtil.getDouble( nIndex++ ) );

            adresse.setPrecisionLocalisation( daoUtil.getString( nIndex++ ) );
            Signalement signalement = new Signalement( );
            signalement.setId( daoUtil.getLong( nIndex++ ) );

            adresse.setSignalement( signalement );

        }

        daoUtil.free( );

        return adresse;
    }

    /**
     * Store a adresse
     * 
     * @param adresse the adresse object
     */
    public void store( Adresse adresse )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, adresse.getId( ) );
        daoUtil.setString( nIndex++, adresse.getAdresse( ) );
        daoUtil.setString( nIndex++, adresse.getPrecisionLocalisation( ) );
        daoUtil.setLong( nIndex++, adresse.getSignalement( ).getId( ) );
        //WHERE
        daoUtil.setLong( nIndex++, adresse.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Find adresses for a Signalement id
     * @param lIdSignalement the signalement id
     * @return list of adresses
     * 
     */
    public List<Adresse> findBySignalementId( long lIdSignalement )
    {
        List<Adresse> result = new ArrayList<Adresse>( );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LIST_SIGNALEURS );
        daoUtil.setLong( 1, lIdSignalement );

        daoUtil.executeQuery( );

        // Pour chaque resultat retourne
        while ( daoUtil.next( ) )
        {
            Adresse adresse = new Adresse( );
            int nIndex = 1;
            adresse.setId( daoUtil.getLong( nIndex++ ) );
            adresse.setAdresse( daoUtil.getString( nIndex++ ) );

            adresse.setLng( daoUtil.getDouble( nIndex++ ) );
            adresse.setLat( daoUtil.getDouble( nIndex++ ) );

            adresse.setPrecisionLocalisation( daoUtil.getString( nIndex++ ) );
            Signalement signalement = new Signalement( );
            signalement.setId( daoUtil.getLong( nIndex++ ) );
            adresse.setSignalement( signalement );
            result.add( adresse );
        }

        daoUtil.free( );

        return result;
    }

}