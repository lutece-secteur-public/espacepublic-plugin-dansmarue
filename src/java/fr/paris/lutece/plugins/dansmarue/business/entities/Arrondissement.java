package fr.paris.lutece.plugins.dansmarue.business.entities;

import fr.paris.lutece.portal.service.rbac.RBACResource;


public class Arrondissement implements Comparable<Arrondissement>, RBACResource
{
    public static final String RESOURCE_TYPE = "SIGNALEMENT_ARRONDISSEMENT";
    private Long _nId;
    private String _strNumero;
    private boolean _bActif;

    public Long getId( )
    {
        return _nId;
    }

    public void setId( Long id )
    {
        this._nId = id;
    }

    public String getNumero( )
    {
        return _strNumero;
    }

    public void setNumero( String numero )
    {
        this._strNumero = numero;
    }

    public boolean getActif( )
    {
        return _bActif;
    }

    public void setActif( boolean actif )
    {
        this._bActif = actif;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceTypeCode( )
    {
        return RESOURCE_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceId( )
    {
        return Long.toString( getId( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo( Arrondissement o )
    {
        if ( o == null || this.getId( ) < o.getId( ) )
        {
            return -1;
        }
        if ( this.getId( ) > o.getId( ) )
        {
            return 1;
        }
        return 0;
    }
}
