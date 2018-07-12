package fr.paris.lutece.plugins.dansmarue.business.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import fr.paris.lutece.plugins.unittree.modules.sira.business.sector.Sector;

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
    private TypeSignalement _typeSignalement;
    private Sector _sector;
    private Integer _nIdTask;
    
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
    /**
     * @return the _typeSignalement
     */
    public TypeSignalement getTypeSignalement( )
    {
        return _typeSignalement;
    }
    /**
     * @param _typeSignalement the _typeSignalement to set
     */
    public void setTypeSignalement( TypeSignalement typeSignalement )
    {
        this._typeSignalement = typeSignalement;
    }
    /**
     * @return the _sector
     */
    public Sector getSector( )
    {
        return _sector;
    }
    /**
     * @param _sector the _sector to set
     */
    public void setSector( Sector sector )
    {
        this._sector = sector;
    }
    /**
     * @return the _nIdTask
     */
    public Integer getIdTask( )
    {
        return _nIdTask;
    }
    /**
     * @param _nIdTask the _nIdTask to set
     */
    public void setIdTask( Integer _nIdTask )
    {
        this._nIdTask = _nIdTask;
    }
    
    
   
}
