package fr.paris.lutece.plugins.dansmarue.business.entities;

import java.io.Serializable;


public class EtatSignalement implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Long _lId;
    private String _strLibelle;
    private String _strCode;
    private Boolean _bCoche;

    public void setId( Long id )
    {
        this._lId = id;
    }

    public Long getId( )
    {
        return _lId;
    }

    /**
     * @return the libelle
     */
    public String getLibelle( )
    {
        return _strLibelle;
    }

    /**
     * @param libelle the libelle to set
     */
    public void setLibelle( String libelle )
    {
        this._strLibelle = libelle;
    }

    /**
     * @return the code
     */
    public String getCode( )
    {
        return _strCode;
    }

    /**
     * @param code the code to set
     */
    public void setCode( String code )
    {
        this._strCode = code;
    }

    /**
     * @return the checked
     */
    public Boolean getCoche( )
    {
        return this._bCoche;
    }

    /**
     * @param pChecked the checked to set
     */
    public void setCoche( Boolean pChecked )
    {
        this._bCoche = pChecked;
    }

}
