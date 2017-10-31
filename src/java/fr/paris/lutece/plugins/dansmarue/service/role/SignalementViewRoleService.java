package fr.paris.lutece.plugins.dansmarue.service.role;

import fr.paris.lutece.plugins.dansmarue.business.dao.IViewRoleDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;


/**
 * Service to manage view roles. Allow to modify view roles, and to check if a
 * user is authorized to view a signalement
 */
public class SignalementViewRoleService
{
    @Inject
    @Named( "signalement.viewRoleDAO" )
    IViewRoleDAO _viewRoleDAO;

    /**
     * Check if a user is restricted by a view role
     * @param request The request to get the session of.
     * @param nIdUser The is of the user to check
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
            request.getSession( ).setAttribute( SignalementConstants.ATTRIBUTE_SESSION_IS_USER_RECTRICTED,
                    bIsRestricted );
            return bIsRestricted;
        }
        return (Boolean) obj;
    }

    /**
     * Check if a user has role restriction over signalements
     * @param nIdUser The id of the user to check
     * @return True if the user is restricted, false otherwise
     */
    public boolean isUserRestrictedByViewRoles( int nIdUser )
    {
        return _viewRoleDAO.findUserIsRestricted( nIdUser );
    }

    /**
     * Get the list of arrondissement ids the user is allowed to view
     * signalement of. The list is loaded from the session, or from the database
     * is no one exists in session.
     * @param nIdUser the id of the user
     * @param request The request to get the session of.
     * @return The list of arrondissement ids, or null if the user has no
     *         restriction over arrondissements
     */
    public List<Integer> getUserRestrictedArrondissementList( int nIdUser, HttpServletRequest request )
    {
        List<Integer> listArrondissements = null;
        if ( request != null )
        {
            Object obj = request.getSession( ).getAttribute(
                    SignalementConstants.ATTRIBUTE_SESSION_LIST_RESTRICTED_ARRONDISSEMENTS );
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
                request.getSession( ).setAttribute(
                        SignalementConstants.ATTRIBUTE_SESSION_LIST_RESTRICTED_ARRONDISSEMENTS, listArrondissements );
            }
        }
        return listArrondissements;
    }

    /**
     * Get the list of arrondissement ids the user is allowed to view
     * signalement of.
     * @param nIdUser the id of the user
     * @return The list of arrondissement ids, or null if the user has no
     *         restriction over arrondissements
     */
    public List<Integer> getUserRestrictedArrondissementList( int nIdUser )
    {
        return _viewRoleDAO.getUserRestrictedArrondissementList( nIdUser );
    }

    /**
     * Get the list of ids of signalement type the user is allowed to view
     * signalement of. The list is loaded from the session, or from the database
     * is no one exists in session.
     * @param nIdUser the id of the user
     * @param request The request to get the session of.
     * @return The list of ids of signalement type, or null if the user has no
     *         restriction over signalement types.
     */
    public List<Integer> getUserRestrictedTypeSignalementList( int nIdUser, HttpServletRequest request )
    {
        List<Integer> listTypeSignalement = null;
        if ( request != null )
        {
            Object obj = request.getSession( ).getAttribute(
                    SignalementConstants.ATTRIBUTE_SESSION_LIST_RESTRICTED_TYPE_SIGNALEMENT );
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
                request.getSession( ).setAttribute(
                        SignalementConstants.ATTRIBUTE_SESSION_LIST_RESTRICTED_TYPE_SIGNALEMENT, listTypeSignalement );
            }
        }
        return listTypeSignalement;
    }

    /**
     * Get the list of ids of signalement type the user is allowed to view
     * signalement of.
     * @param nIdUser the id of the user
     * @return The list of ids of signalement type, or null if the user has no
     *         restriction over signalement types.
     */
    public List<Integer> getUserRestrictedTypeSignalementList( int nIdUser )
    {
        return _viewRoleDAO.getUserRestrictedTypeSignalementList( nIdUser );
    }

    /**
     * Check if a user is authorized to access a signalement according to view
     * roles
     * @param signalement The signalement
     * @param request The request
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
        if ( signalement.getArrondissement( ) != null && listArrondissement != null && listArrondissement.size( ) > 0 )
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

        boolean bAuthorizedTypeSignalement = false;
        List<Integer> listTypeSignalement = getUserRestrictedTypeSignalementList( adminUser.getUserId( ), request );
        if ( signalement.getTypeSignalement( ) != null && listTypeSignalement != null
                && listTypeSignalement.size( ) > 0 )
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

        return bAuthorizedArrondissement && bAuthorizedTypeSignalement;
    }
    
    
    /**
     * Get the list of ids of signalement categories the user is allowed to view
     * signalement of. The list is loaded from the session, or from the database
     * is no one exists in session.
     * @param nIdUser the id of the user
     * @param request The request to get the session of.
     * @return The list of ids of signalement categories, or null if the user has no
     *         restriction over signalement categories.
     */
    public List<Integer> getUserRestrictedCategorySignalementList( int nIdUser, HttpServletRequest request )
    {
        List<Integer> listCategoriesSignalement = null;
        if ( request != null )
        {
            Object obj = request.getSession( ).getAttribute(
                    SignalementConstants.ATTRIBUTE_SESSION_LIST_RESTRICTED_CATEGORY_SIGNALEMENT );
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
                request.getSession( ).setAttribute(
                        SignalementConstants.ATTRIBUTE_SESSION_LIST_RESTRICTED_CATEGORY_SIGNALEMENT, listCategoriesSignalement );
            }
        }
        return listCategoriesSignalement;
    }
    
    /**
     * Get the list of ids of signalement category the user is allowed to view
     * signalement of.
     * @param nIdUser the id of the user
     * @return The list of ids of signalement category, or null if the user has no
     *         restriction over signalement categories.
     */
    public List<Integer> getUserRestrictedCategorySignalementList( int nIdUser )
    {
        return _viewRoleDAO.getUserRestrictedCategorySignalementList( nIdUser );
    }
}
