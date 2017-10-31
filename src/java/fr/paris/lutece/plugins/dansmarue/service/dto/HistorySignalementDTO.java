package fr.paris.lutece.plugins.dansmarue.service.dto;

public class HistorySignalementDTO
{
    String _strDate;
    String _strState;
    String _strMessage;

    /**
     * Get the date
     * @return the date (string)
     */
    public String getDate( )
    {
        return _strDate;
    }

    /**
     * Set the date
     * @param date the new date (string)
     */
    public void setDate( String date )
    {
        this._strDate = date;
    }

    /**
     * Get the state
     * @return the state
     */
    public String getState( )
    {
        return _strState;
    }

    /**
     * Set the state
     * @param state the new state (string)
     */
    public void setState( String state )
    {
        this._strState = state;
    }

    /**
     * Get the message
     * @return the message
     */
    public String getMessage( )
    {
        return _strMessage;
    }

    /**
     * Set the message
     * @param message the new message (string)
     */
    public void setMessage( String message )
    {
        this._strMessage = message;
    }

}
