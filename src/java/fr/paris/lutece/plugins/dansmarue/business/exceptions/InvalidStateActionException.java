package fr.paris.lutece.plugins.dansmarue.business.exceptions;

/**
 * Exception raised when the signalement state does not allow the action
 */
public class InvalidStateActionException extends RuntimeException {

	public InvalidStateActionException(String message){
		super(message);
	}
}
