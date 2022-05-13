/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.dao.IViewRoleDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * DAO to access roles restrictions over signalements.
 */
public class ViewRoleDAO implements IViewRoleDAO
{

    /** The Constant SQL_QUERY_CHECK_USER_RESTRICTIONS. */
    private static final String SQL_QUERY_CHECK_USER_RESTRICTIONS = " SELECT COUNT( carr.resource_id ) FROM core_admin_user cau INNER JOIN core_user_role cur ON cau.id_user = cur.id_user INNER JOIN core_admin_role_resource carr ON cur.role_key = carr.role_key WHERE cau.id_user = ? AND carr.resource_type IN ( '"
            + Arrondissement.RESOURCE_TYPE + "','" + TypeSignalement.RESOURCE_TYPE + "') AND carr.resource_id != '" + RBAC.WILDCARD_RESOURCES_ID + "' ";

    /** The Constant SQL_QUERY_FIND_ARRONDISSEMENT_RESTRICTION_LIST. */
    private static final String SQL_QUERY_FIND_ARRONDISSEMENT_RESTRICTION_LIST = " SELECT DISTINCT carr.resource_id FROM core_admin_user cau INNER JOIN core_user_role cur ON cau.id_user = cur.id_user INNER JOIN core_admin_role_resource carr ON cur.role_key = carr.role_key WHERE cau.id_user = ? AND carr.resource_type = '"
            + Arrondissement.RESOURCE_TYPE + "' AND carr.resource_id != '" + RBAC.WILDCARD_RESOURCES_ID + "' ";

    /** The Constant SQL_QUERY_FIND_TYPE_SIGNALEMENT_RESTRICTION_LIST. */
    private static final String SQL_QUERY_FIND_TYPE_SIGNALEMENT_RESTRICTION_LIST = " SELECT DISTINCT carr.resource_id FROM core_admin_user cau INNER JOIN core_user_role cur ON cau.id_user = cur.id_user INNER JOIN core_admin_role_resource carr ON cur.role_key = carr.role_key WHERE cau.id_user = ? AND carr.resource_type = '"
            + TypeSignalement.RESOURCE_TYPE + "' AND carr.resource_id != '" + RBAC.WILDCARD_RESOURCES_ID + "' ";

    /** The Constant SQL_QUERY_FIND_CATEGORY_SIGNALEMENT_RESTRICTION_LIST. */
    private static final String SQL_QUERY_FIND_CATEGORY_SIGNALEMENT_RESTRICTION_LIST = "SELECT DISTINCT vstsawpl.id_parent AS id_category FROM core_user_role cur"
            + " INNER JOIN core_admin_role_resource carr ON carr.role_key = cur.role_key"
            + " INNER JOIN v_signalement_type_signalement_with_parents_links vstsawpl ON vstsawpl.id_type_signalement = CAST(carr.resource_id AS BIGINT)"
            + " WHERE cur.id_user = ? AND carr.resource_type = '" + TypeSignalement.RESOURCE_TYPE + "'" + " AND carr.resource_id != '"
            + RBAC.WILDCARD_RESOURCES_ID + "'" + " AND vstsawpl.actif = 1";

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
        daoUtil.close( );
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
        List<Integer> listArondissements = new ArrayList<>( );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            listArondissements.add( daoUtil.getInt( 1 ) );
        }
        if ( !listArondissements.isEmpty( ) )
        {
            daoUtil.close( );
            return listArondissements;
        }
        daoUtil.close( );
        return Collections.emptyList( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getUserRestrictedTypeSignalementList( int nIdUser )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_TYPE_SIGNALEMENT_RESTRICTION_LIST );
        daoUtil.setInt( 1, nIdUser );
        List<Integer> listTypeSignalements = new ArrayList<>( );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            listTypeSignalements.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.close( );
        if ( !listTypeSignalements.isEmpty( ) )
        {
            return listTypeSignalements;
        }
        return Collections.emptyList( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getUserRestrictedCategorySignalementList( int nIdUser )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_CATEGORY_SIGNALEMENT_RESTRICTION_LIST );
        daoUtil.setInt( 1, nIdUser );
        List<Integer> listCategorySignalementId = new ArrayList<>( );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            listCategorySignalementId.add( daoUtil.getInt( 1 ) );
        }
        daoUtil.close( );
        if ( !listCategorySignalementId.isEmpty( ) )
        {
            return listCategorySignalementId;
        }
        return Collections.emptyList( );
    }

}
