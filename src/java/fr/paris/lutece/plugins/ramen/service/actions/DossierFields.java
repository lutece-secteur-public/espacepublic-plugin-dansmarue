package fr.paris.lutece.plugins.ramen.service.actions;

/**
 * Fields for mass action
 */
public class DossierFields
{
    private int[] _dossiersId;
    private int nIdResource;

    /**
     * 
     * getDossiersId
     * @return dossier ids
     */
    public int[] getDossiersId( )
    {
        return _dossiersId;
    }

    /**
     * 
     * setDossiersId
     * @param dossiersId dossier ids
     */
    public void setDossiersId( int[] dossiersId )
    {
        this._dossiersId = dossiersId;
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
