package fr.paris.lutece.plugins.dansmarue.business.dao;

import java.util.List;


public interface IViewRoleDAO
{
    /**
     * Check if a user has restriction over arrondissement or signalement types.
     * @param nIdUser The id of the user to check
     * @return True if the user has restriction, false otherwise
     */
    boolean findUserIsRestricted( int nIdUser );
    
    /**
     * Get the list of arrondissement ids the user is allowed to view
     * signalement of.
     * @param nIdUser the id of the user
     * @return The list of arrondissement ids, or null if the user has no
     *         restriction over arrondissements
     */
    List<Integer> getUserRestrictedArrondissementList( int nIdUser );

    /**
     * Get the list of ids of signalement type the user is allowed to view
     * signalement of.
     * @param nIdUser the id of the user
     * @return The list of ids of signalement type, or null if the user has no
     *         restriction over signalement types.
     */
    List<Integer> getUserRestrictedTypeSignalementList( int nIdUser );

    /**
     * Get the list of ids of signalement categories allowed for the user.
     * @param nIdUser
     * 		  The id of the user
     * @return
     * 		  List of ids of signalement categories that the user is allowed to view
     * 		  null otherwise
     */
	List<Integer> getUserRestrictedCategorySignalementList(int nIdUser);
}
