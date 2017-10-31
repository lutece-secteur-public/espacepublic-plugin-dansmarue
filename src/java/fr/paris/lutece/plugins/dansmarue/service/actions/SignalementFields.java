package fr.paris.lutece.plugins.dansmarue.service.actions;

/**
 * Fields for mass action
 */
public class SignalementFields
{
    private int[] _signalementsId;
    private int nIdResource;

    /**
     * 
     * getSignalementsId
     * @return signalement ids
     */
    public int[] getSignalementsId( )
    {
        return _signalementsId;
    }

    /**
     * 
     * setSignalementsId
     * @param signalementsId signalement ids
     */
    public void setSignalementsId( int[] signalementsId )
    {
        this._signalementsId = signalementsId;
    }
    
    /**
     * 
     * getIdResource
     * @return id resource
     */
    public int getIdResource(  )
    {
        return nIdResource;
    }
    
    /**
     * 
     * setIdResource
     * @param nIdResource id resource
     */
    public void setIdResource( int nIdResource )
    {
        this.nIdResource = nIdResource;
    }
    
    
}
