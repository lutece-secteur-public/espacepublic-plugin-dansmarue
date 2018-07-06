package fr.paris.lutece.plugins.dansmarue.business.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * The Class SignalementRequalification.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class SignalementRequalification
{
    private Long _nIdSignalement;
    private Integer _nIdTypeSignalement;
    private String _strAdresse;
    private Integer _nIdSector;
    private String _dateRequalification;
    /**
     * @return the nIdSignalement
     */
    public Long getIdSignalement( )
    {
        return _nIdSignalement;
    }
    /**
     * @param nIdSignalement the nIdSignalement to set
     */
    public void setIdSignalement( Long nIdSignalement )
    {
        this._nIdSignalement = nIdSignalement;
    }
    /**
     * @return the _nIdTypeSignalement
     */
    public Integer getIdTypeSignalement( )
    {
        return _nIdTypeSignalement;
    }
    /**
     * @param _nIdTypeSignalement the _nIdTypeSignalement to set
     */
    public void setIdTypeSignalement( Integer nIdTypeSignalement )
    {
        this._nIdTypeSignalement = nIdTypeSignalement;
    }
    /**
     * @return the _strAdresse
     */
    public String getAdresse( )
    {
        return _strAdresse;
    }
    /**
     * @param _strAdresse the _strAdresse to set
     */
    public void setAdresse( String strAdresse )
    {
        this._strAdresse = strAdresse;
    }
    /**
     * @return the _nIdSector
     */
    public Integer getIdSector( )
    {
        return _nIdSector;
    }
    /**
     * @param _nIdSector the _nIdSector to set
     */
    public void setIdSector( Integer nIdSector )
    {
        this._nIdSector = nIdSector;
    }
    /**
     * @return the _dateRequalification
     */
    public String getDateRequalification( )
    {
        return _dateRequalification;
    }
    /**
     * @param _dateRequalification the _dateRequalification to set
     */
    public void setDateRequalification( String dateRequalification )
    {
        this._dateRequalification = dateRequalification;
    }
    
    
   
}
