package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.dao.IArrondissementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.util.sql.DAOUtil;

public class ArrondissementDAO implements IArrondissementDAO
{

    private static final String SQL_QUERY_SELECT_ALL_ARRONDISSEMENT  = "SELECT id_arrondissement, numero_arrondissement, actif, geom FROM signalement_arrondissement ORDER BY id_arrondissement";
    private static final String SQL_QUERY_SELECT_BY_IDARRONDISSEMENT = "SELECT id_arrondissement, numero_arrondissement, actif, geom FROM signalement_arrondissement WHERE id_arrondissement=?";

    @Override
    public Arrondissement getArrondissementByGeom( Double lng, Double lat )
    {
        Arrondissement ret;
        if ( ( lng != null ) && ( lat != null ) )
        {
            DAOUtil daoUtil = new DAOUtil(
                    "SELECT id_arrondissement, numero_arrondissement, actif, geom FROM signalement_arrondissement WHERE ST_Contains(signalement_arrondissement.geom, ST_GeomFromText('POINT("
                            + lng + " " + lat + ")', 4326))" );

            // daoUtil.setDouble( 1, lng );
            // daoUtil.setDouble( 2, lat );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                ret = convertDaoUtilToEntity( daoUtil );
            } else
            {
                ret = null;
            }
            daoUtil.free( );
        } else
        {
            ret = null;
        }
        return ret;
    }

    /**
     * expected column order is id_arrondissement, arrondissement, actif
     * 
     * @param daoUtil
     *            daoUtil
     * @return An arrondissement entity filled with query's data.
     */
    private Arrondissement convertDaoUtilToEntity( DAOUtil daoUtil )
    {
        int nIndex = 1;
        long nIdArrondissement = daoUtil.getLong( nIndex++ );
        Arrondissement arrondissement = new Arrondissement( );
        arrondissement.setId( nIdArrondissement );
        arrondissement.setNumero( daoUtil.getString( nIndex++ ) );
        if ( daoUtil.getInt( nIndex++ ) == 1 )
        {
            arrondissement.setActif( true );
        } else
        {
            arrondissement.setActif( false );
        }

        return arrondissement;
    }

    @Override
    public List<Arrondissement> getAllArrondissement( )
    {
        List<Arrondissement> listResult = new ArrayList<Arrondissement>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_ARRONDISSEMENT );
        daoUtil.executeQuery( );
        int nIndex;
        Arrondissement arrondissement;
        while ( daoUtil.next( ) )
        {
            nIndex = 1;
            arrondissement = new Arrondissement( );
            arrondissement.setId( daoUtil.getLong( nIndex++ ) );
            arrondissement.setNumero( daoUtil.getString( nIndex++ ) );
            arrondissement.setActif( daoUtil.getBoolean( nIndex++ ) );

            listResult.add( arrondissement );
        }

        daoUtil.free( );

        return listResult;
    }

    /**
     * Return an arrondissement by his Id
     * 
     * @return an arrondissement
     */

    @Override
    public Arrondissement getByIdArrondissement( int nIdArrondissement )
    {

        Arrondissement arrondissement = new Arrondissement( );
        Integer nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_IDARRONDISSEMENT );
        daoUtil.setInt( 1, nIdArrondissement );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {

            arrondissement.setId( daoUtil.getLong( nIndex++ ) );
            arrondissement.setNumero( daoUtil.getString( nIndex++ ) );
            arrondissement.setActif( daoUtil.getBoolean( nIndex++ ) );
        }

        daoUtil.free( );

        return arrondissement;
    }

    @Override
    public List<Integer> getArrondissementsInSector( List<Integer> sectors )
    {
        List<Integer> allIdArrondissements = new ArrayList<Integer>( );
        String query = "SELECT DISTINCT signalement_arrondissement.id_arrondissement FROM signalement_arrondissement, unittree_sector WHERE ST_contains( signalement_arrondissement.geom, ST_centroid(unittree_sector.geom)) "
                + " AND id_sector IN (";
        boolean first = true;
        for ( Integer idSector : sectors )
        {
            if ( !first )
            {
                query += ",";
            }
            query += idSector;
            first = false;
        }
        query += ") ORDER BY id_arrondissement ";

        DAOUtil daoUtil = new DAOUtil( query );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            allIdArrondissements.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return allIdArrondissements;

    }

}
