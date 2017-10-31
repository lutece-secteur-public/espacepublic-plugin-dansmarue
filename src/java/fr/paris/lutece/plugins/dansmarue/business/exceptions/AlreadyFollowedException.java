package fr.paris.lutece.plugins.dansmarue.business.exceptions;

/**
 * Exception raised when trying to follow a signalement which is already followed by the user
 */
public class AlreadyFollowedException extends RuntimeException{
	
	public AlreadyFollowedException(String message){
		super(message);
	}
	
}
