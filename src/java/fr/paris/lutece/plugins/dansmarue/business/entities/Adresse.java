package fr.paris.lutece.plugins.dansmarue.business.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class Adresse
{
    private Double      _lat;
    private Double      _lng;
    private Double      _latAddress;
    private Double      _lngAddress;
    private Long        _nId;
    private Signalement _signalement;
    private String      _strAdresse;
    private String      _strPrecisionLocalisation;

    public String getAdresse( )
    {
        return _strAdresse;
    }

    public Long getId( )
    {
        return _nId;
    }

    public Double getLat( )
    {
        return _lat;
    }

    public Double getLng( )
    {
        return _lng;
    }

    public Double getLatAddress( )
    {
        return _latAddress;
    }

    public Double getLngAddress( )
    {
        return _lngAddress;
    }

    public String getPrecisionLocalisation( )
    {
        return _strPrecisionLocalisation;
    }

    public Signalement getSignalement( )
    {
        return _signalement;
    }

    public void setAdresse( String adresse )
    {
        _strAdresse = adresse;
    }

    public void setId( Long id )
    {
        _nId = id;
    }

    public void setLat( Double lat )
    {
        _lat = lat;
    }

    public void setLng( Double lng )
    {
        _lng = lng;
    }

    public void setLatAddress( Double latAddress )
    {
        _latAddress = latAddress;
    }

    public void setLngAddress( Double lngAddress )
    {
        _lngAddress = lngAddress;
    }

    public void setPrecisionLocalisation( String precisionLocalisation )
    {
        _strPrecisionLocalisation = precisionLocalisation;
    }

    public void setSignalement( Signalement signalement )
    {
        _signalement = signalement;
    }

    public String getGoogleLink( )
    {
        if ( ( getLat( ) == null ) || ( getLng( ) == null ) || ( getAdresse( ) == null ) )
        {
            return getAdresse( );
        } else
        {
            String strLat = Double.toString( getLat( ) ).replaceAll( ",", "\\." );
            String strLng = Double.toString( getLng( ) ).replaceAll( ",", "\\." );
            String strAdr = getAdresse( ).replaceAll( "'", " " ).replaceAll( " ", "\\+" );
            return "https://maps.google.fr/?t=h&z=18&q=" + strLat + "," + strLng + "+(" + strAdr + ")";
        }
    }

}
