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
package fr.paris.lutece.plugins.dansmarue.service.role;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.business.dao.IViewRoleDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;

/**
 * Service to manage view roles. Allow to modify view roles, and to check if a user is authorized to view a report
 */
public class SignalementViewRoleService
{

    /** The view role DAO. */
    @Inject
    @Named( "signalement.viewRoleDAO" )
    IViewRoleDAO _viewRoleDAO;

    /**
     * Check if a user is restricted by a view role.
     *
     * @param nIdUser
     *            The is of the user to check
     * @param request
     *            The request to get the session of.
     * @return True if the user is restricted by view role, false otherwise
     */
    public boolean checkIsUserRestrictedByViewRoles( int nIdUser, HttpServletRequest request )
    {
        if ( request == null )
        {
            return isUserRestrictedByViewRoles( nIdUser );
        }
        Object obj = request.getSession( ).getAttribute( SignalementConstants.ATTRIBUTE_SESSION_IS_USER_RECTRICTED );
        if ( obj == null )
        {
            boolean bIsRestricted = isUserRestrictedByViewRoles( nIdUser );
            request.getSession( ).setAttribute( SignalementConstants.ATTRIBUTE_SESSION_IS_USER_RECTRICTED, bIsRestricted );
            return bIsRestricted;
        }
        return (Boolean) obj;
    }

    /**
     * Check if a user has role restriction over reports.
     *
     * @param nIdUser
     *            The id of the user to check
     * @return True if the user is restricted, false otherwise
     */
    public boolean isUserRestrictedByViewRoles( int nIdUser )
    {
        return _viewRoleDAO.findUserIsRestricted( nIdUser );
    }

    /**
     * Get the list of districts ids the user is allowed to view report of. The list is loaded from the session, or from the database is no one exists in
     * session.
     *
     * @param nIdUser
     *            the id of the user
     * @param request
     *            The request to get the session of.
     * @return The list of districts ids, or null if the user has no restriction over districts
     */
    @SuppressWarnings( "unchecked" )
    public List<Integer> getUserRestrictedArrondissementList( int nIdUser, HttpServletRequest request )
    {
        List<Integer> listArrondissements = null;
        if ( request != null )
        {
            Object obj = request.getSession( ).getAttribute( SignalementConstants.ATTRIBUTE_SESSION_LIST_RESTRICTED_ARRONDISSEMENTS );
            if ( obj != null )
            {
                listArrondissements = (List<Integer>) obj;
            }
        }
        if ( listArrondissements == null )
        {
            listArrondissements = getUserRestrictedArrondissementList( nIdUser );
            if ( request != null )
            {
                request.getSession( ).setAttribute( SignalementConstants.ATTRIBUTE_SESSION_LIST_RESTRICTED_ARRONDISSEMENTS, listArrondissements );
            }
        }
        return listArrondissements;
    }

    /**
     * Get the list of districts ids the user is allowed to view report of.
     *
     * @param nIdUser
     *            the id of the user
     * @return The list of districts ids, or null if the user has no restriction over districts
     */
    public List<Integer> getUserRestrictedArrondissementList( int nIdUser )
    {
        return _viewRoleDAO.getUserRestrictedArrondissementList( nIdUser );
    }

    /**
     * Get the list of ids of report types the user is allowed to view report of. The list is loaded from the session, or from the database is no one exists in
     * session.
     *
     * @param nIdUser
     *            the id of the user
     * @param request
     *            The request to get the session of.
     * @return The list of ids of report type, or null if the user has no restriction over report types.
     */
    @SuppressWarnings( "unchecked" )
    public List<Integer> getUserRestrictedTypeSignalementList( int nIdUser, HttpServletRequest request )
    {
        List<Integer> listTypeSignalement = null;
        if ( request != null )
        {
            Object obj = request.getSession( ).getAttribute( SignalementConstants.ATTRIBUTE_SESSION_LIST_RESTRICTED_TYPE_SIGNALEMENT );
            if ( obj != null )
            {
                listTypeSignalement = (List<Integer>) obj;
            }
        }
        if ( listTypeSignalement == null )
        {
            listTypeSignalement = getUserRestrictedTypeSignalementList( nIdUser );
            if ( request != null )
            {
                request.getSession( ).setAttribute( SignalementConstants.ATTRIBUTE_SESSION_LIST_RESTRICTED_TYPE_SIGNALEMENT, listTypeSignalement );
            }
        }
        return listTypeSignalement;
    }

    /**
     * Get the list of ids of report type the user is allowed to view report of.
     *
     * @param nIdUser
     *            the id of the user
     * @return The list of ids of report type, or null if the user has no restriction over report types.
     */
    public List<Integer> getUserRestrictedTypeSignalementList( int nIdUser )
    {
        return _viewRoleDAO.getUserRestrictedTypeSignalementList( nIdUser );
    }

    /**
     * Check if a user is authorized to access a report according to view roles.
     *
     * @param signalement
     *            The report
     * @param request
     *            The request
     * @return True if the user is authorized, false otherwise
     */
    public boolean isUserAuthorized( Signalement signalement, HttpServletRequest request )
    {
        AdminUser adminUser = AdminUserService.getAdminUser( request );
        // If the user is not restricted, we return true
        if ( !isUserRestrictedByViewRoles( adminUser.getUserId( ) ) )
        {
            return true;
        }
        List<Integer> listArrondissement = getUserRestrictedArrondissementList( adminUser.getUserId( ), request );

        boolean bAuthorizedArrondissement = false;
        if ( ( signalement.getArrondissement( ) != null ) && ( listArrondissement != null ) && !listArrondissement.isEmpty( ) )
        {
            for ( Integer nIdArrondissement : listArrondissement )
            {
                long nArrondissementId = nIdArrondissement;
                if ( nArrondissementId == signalement.getArrondissement( ).getId( ).longValue( ) )
                {
                    bAuthorizedArrondissement = true;
                    break;
                }
            }
        }
        else
        {
            bAuthorizedArrondissement = true;
        }

        return bAuthorizedArrondissement && isAuthorizedTypeSignalement( adminUser, signalement, request );
    }

    private boolean isAuthorizedTypeSignalement( AdminUser adminUser, Signalement signalement, HttpServletRequest request )
    {

        boolean bAuthorizedTypeSignalement = false;
        List<Integer> listTypeSignalement = getUserRestrictedTypeSignalementList( adminUser.getUserId( ), request );
        if ( ( signalement.getTypeSignalement( ) != null ) && ( listTypeSignalement != null ) && !listTypeSignalement.isEmpty( ) )
        {
            for ( Integer nIdTypeSignalement : listTypeSignalement )
            {
                int nTypeSignalementId = nIdTypeSignalement;
                if ( nTypeSignalementId == signalement.getTypeSignalement( ).getId( ).intValue( ) )
                {
                    bAuthorizedTypeSignalement = true;
                    break;
                }
            }
        }
        else
        {
            bAuthorizedTypeSignalement = true;
        }

        return bAuthorizedTypeSignalement;
    }

    /**
     * Get the list of ids of report categories the user is allowed to view report of. The list is loaded from the session, or from the database is no one
     * exists in session.
     *
     * @param nIdUser
     *            the id of the user
     * @param request
     *            The request to get the session of.
     * @return The list of ids of report categories, or null if the user has no restriction over report categories.
     */
    @SuppressWarnings( "unchecked" )
    public List<Integer> getUserRestrictedCategorySignalementList( int nIdUser, HttpServletRequest request )
    {
        List<Integer> listCategoriesSignalement = null;
        if ( request != null )
        {
            Object obj = request.getSession( ).getAttribute( SignalementConstants.ATTRIBUTE_SESSION_LIST_RESTRICTED_CATEGORY_SIGNALEMENT );
            if ( obj != null )
            {
                listCategoriesSignalement = (List<Integer>) obj;
            }
        }
        if ( listCategoriesSignalement == null )
        {
            listCategoriesSignalement = getUserRestrictedCategorySignalementList( nIdUser );
            if ( request != null )
            {
                request.getSession( ).setAttribute( SignalementConstants.ATTRIBUTE_SESSION_LIST_RESTRICTED_CATEGORY_SIGNALEMENT, listCategoriesSignalement );
            }
        }
        return listCategoriesSignalement;
    }

    /**
     * Get the list of ids of report category the user is allowed to view report of.
     *
     * @param nIdUser
     *            the id of the user
     * @return The list of ids of report category, or null if the user has no restriction over report categories.
     */
    public List<Integer> getUserRestrictedCategorySignalementList( int nIdUser )
    {
        return _viewRoleDAO.getUserRestrictedCategorySignalementList( nIdUser );
    }
}
