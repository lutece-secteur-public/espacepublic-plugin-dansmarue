package fr.paris.lutece.plugins.dansmarue.business.entities;

public class SignalementGeoLoc implements Comparable<SignalementGeoLoc>
{
    private Signalement _signalement;
    private Integer _nDistance;

    public Integer getDistance( )
    {
        return _nDistance;
    }

    public void setDistance( Integer distance )
    {
        this._nDistance = distance;
    }

    public Signalement getSignalement( )
    {
        return _signalement;
    }

    public void setSignalement( Signalement signalement )
    {
        this._signalement = signalement;
    }

	@Override
	public int compareTo( SignalementGeoLoc signalementGeoloc ) 
	{
	    if( signalementGeoloc.getDistance(  ) > this.getDistance(  ) ) return -1;
	    else if ( signalementGeoloc.getDistance(  ) == this.getDistance(  ) ) return 0;
	    else return 1;
	}

}
