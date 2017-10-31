package fr.paris.lutece.plugins.dansmarue.business.entities;

public class Signaleur
{
    private Long _lId;
    private String _strMail;
    private Signalement _signalement;
    private String _strIdTelephone;
    private String _strGuid;

    public Long getId( )
    {
        return _lId;
    }

    public void setId( Long id )
    {
        this._lId = id;
    }

    public String getMail( )
    {
        return _strMail;
    }

    public void setMail( String mail )
    {
        this._strMail = mail;
    }

    public Signalement getSignalement( )
    {
        return _signalement;
    }

    public void setSignalement( Signalement signalement )
    {
        this._signalement = signalement;
    }

    public String getIdTelephone( )
    {
        return _strIdTelephone;
    }

    public void setIdTelephone( String idTelephone )
    {
        this._strIdTelephone = idTelephone;
    }
    
    public void setGuid( String guid){
    	this._strGuid = guid;
    }
    
    public String getGuid (){
    	return this._strGuid;
    }
}
