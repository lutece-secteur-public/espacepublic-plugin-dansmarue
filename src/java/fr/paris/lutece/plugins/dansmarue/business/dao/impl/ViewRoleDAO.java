package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.dao.IViewRoleDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * DAO to access roles restrictions over signalements
 */
public class ViewRoleDAO implements IViewRoleDAO
{
    private static final String SQL_QUERY_CHECK_USER_RESTRICTIONS = " SELECT COUNT( carr.resource_id ) FROM core_admin_user cau INNER JOIN core_user_role cur ON cau.id_user = cur.id_user INNER JOIN core_admin_role_resource carr ON cur.role_key = carr.role_key WHERE cau.id_user = ? AND carr.resource_type IN ( '"
            + Arrondissement.RESOURCE_TYPE
            + "','"
            + TypeSignalement.RESOURCE_TYPE
            + "') AND carr.resource_id != '"
            + RBAC.WILDCARD_RESOURCES_ID + "' ";

    private static final String SQL_QUERY_FIND_ARRONDISSEMENT_RESTRICTION_LIST = " SELECT DISTINCT carr.resource_id FROM core_admin_user cau INNER JOIN core_user_role cur ON cau.id_user = cur.id_user INNER JOIN core_admin_role_resource carr ON cur.role_key = carr.role_key WHERE cau.id_user = ? AND carr.resource_type = '"
            + Arrondissement.RESOURCE_TYPE + "' AND carr.resource_id != '" + RBAC.WILDCARD_RESOURCES_ID + "' ";
    private static final String SQL_QUERY_FIND_TYPE_SIGNALEMENT_RESTRICTION_LIST = " SELECT DISTINCT carr.resource_id FROM core_admin_user cau INNER JOIN core_user_role cur ON cau.id_user = cur.id_user INNER JOIN core_admin_role_resource carr ON cur.role_key = carr.role_key WHERE cau.id_user = ? AND carr.resource_type = '"
            + TypeSignalement.RESOURCE_TYPE + "' AND carr.resource_id != '" + RBAC.WILDCARD_RESOURCES_ID + "' ";
    private static final String SQL_QUERY_FIND_CATEGORY_SIGNALEMENT_RESTRICTION_LIST = "SELECT DISTINCT vstsawpl.id_parent AS id_category FROM core_user_role cur"
				+ " INNER JOIN core_admin_role_resource carr ON carr.role_key = cur.role_key"
				+ " INNER JOIN v_signalement_type_signalement_with_parents_links vstsawpl ON vstsawpl.id_type_signalement = CAST(carr.resource_id AS BIGINT)"
				+ " WHERE cur.id_user = ? AND carr.resource_type = '" + TypeSignalement.RESOURCE_TYPE + "'"
				+ " AND carr.resource_id != '" + RBAC.WILDCARD_RESOURCES_ID + "'"
				+ " AND vstsawpl.actif = 1";
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean findUserIsRestricted( int nIdUser )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK_USER_RESTRICTIONS );
        daoUtil.setInt( 1, nIdUser );
        boolean bResult = false;
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            bResult = daoUtil.getInt( 1 ) > 0;
        }
        daoUtil.free( );
        return bResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getUserRestrictedArrondissementList( int nIdUser )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ARRONDISSEMENT_RESTRICTION_LIST );
        daoUtil.setInt( 1, nIdUser );
        List<Integer> listArondissements = new ArrayList<Integer>( );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            listArondissements.add( daoUtil.getInt( 1 ) );
        }
        if ( listArondissements.size( ) >= 0 )
        {
        	daoUtil.free();
            return listArondissements;
        }
        daoUtil.free( );
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getUserRestrictedTypeSignalementList( int nIdUser )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_TYPE_SIGNALEMENT_RESTRICTION_LIST );
        daoUtil.setInt( 1, nIdUser );
        List<Integer> listTypeSignalements = new ArrayList<Integer>( );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            listTypeSignalements.add( daoUtil.getInt( 1 ) );
        }

    	daoUtil.free();
        if ( listTypeSignalements.size( ) >= 0 )
        {
            return listTypeSignalements;
        }
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getUserRestrictedCategorySignalementList( int nIdUser){
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_CATEGORY_SIGNALEMENT_RESTRICTION_LIST );
        daoUtil.setInt( 1, nIdUser );
        List<Integer> listCategorySignalementId = new ArrayList<Integer>( );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
        	listCategorySignalementId.add( daoUtil.getInt( 1 ) );
        }
    	daoUtil.free();
        if ( listCategorySignalementId.size( ) >= 0 )
        {
            return listCategorySignalementId;
        }
        return null;
    }

}
