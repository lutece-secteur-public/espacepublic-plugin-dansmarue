package fr.paris.lutece.plugins.dansmarue.business.exceptions;

/**
 * Exception which is raised when an user tries to unfollow a signalement he has not followed
 *
 */
public class NonExistentFollowItem extends Exception{
	
	public NonExistentFollowItem(String message){
		super(message);
	}
}
