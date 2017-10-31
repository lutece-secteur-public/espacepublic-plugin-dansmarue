package fr.paris.lutece.plugins.dansmarue.business.entities;

public class Priorite
{
    private Long _lId;
    private String _strLibelle;
    private Integer _ordrePriorite;

    public Integer getOrdrePriorite() {
		return _ordrePriorite;
	}

	public Integer setOrdrePriorite(Integer ordrePriorite) {
		return this._ordrePriorite = ordrePriorite;
	}

	public Long getId( )
    {
        return _lId;
    }

    public void setId( long id )
    {
        this._lId = id;
    }

    public String getLibelle( )
    {
        return _strLibelle;
    }

    public void setLibelle( String libelle )
    {
        this._strLibelle = libelle;
    }


}
