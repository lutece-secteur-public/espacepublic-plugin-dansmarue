package fr.paris.lutece.plugins.dansmarue.business.entities;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;


public class ObservationRejet
{
    private Integer _lId;
    @NotBlank
    private String _strLibelle;
    private boolean _bActif;
    
    //Ordre d'affichage
    private Integer _iOrdre;

    public Integer getId( )
    {
        return _lId;
    }

    public void setId( Integer id )
    {
        this._lId = id;
    }

    public String getLibelle( )
    {
        return _strLibelle;
    }

    public void setLibelle( String strLibelle )
    {
        this._strLibelle = strLibelle;
    }

    public boolean getActif( )
    {
        return _bActif;
    }

    public void setActif( boolean actif )
    {
        this._bActif = actif;
    }

	public Integer getOrdre() {
		return _iOrdre;
	}

	public void setOrdre(Integer ordre) {
		this._iOrdre = ordre;
	}

}
