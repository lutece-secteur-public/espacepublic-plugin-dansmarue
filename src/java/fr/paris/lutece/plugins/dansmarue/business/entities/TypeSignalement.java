/*
 * Copyright (c) 2002-2012, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.dansmarue.business.entities;

import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.rbac.RBACResource;

public class TypeSignalement implements RBACResource
{
    public static final String RESOURCE_TYPE          = "TYPE_SIGNALEMENT";

    private Integer            _nId;
    @NotBlank
    private String             _strLibelle;
    private boolean            _bActif;

    @JsonBackReference
    private TypeSignalement    _typeSignalementParent = null;
    private Unit               _unit                  = new Unit( );

    private Integer            _nIdTypeSignalementParent;
    private ImageResource      _image                 = null;
    @NotNull
    @Min( 1 )
    private int                _nOrdre;
    private String             _strImageUrl           = "";

    @Transient
    private Integer            _nIdCategory;

    private String             _strAlias;
    private String             _strAliasMobile;

    public int getOrdre( )
    {
        return _nOrdre;
    }

    public void setOrdre( int ordre )
    {
        _nOrdre = ordre;
    }

    public Integer getIdTypeSignalementParent( )
    {
        return _nIdTypeSignalementParent;
    }

    public void setIdTypeSignalementParent( Integer idTypeSignalementParent )
    {
        _nIdTypeSignalementParent = idTypeSignalementParent;
    }

    public Integer getId( )
    {
        return _nId;
    }

    public void setId( Integer id )
    {
        _nId = id;
    }

    public String getLibelle( )
    {
        return _strLibelle;
    }

    public void setLibelle( String libelle )
    {
        _strLibelle = libelle;
    }

    public boolean getActif( )
    {
        return _bActif;
    }

    public void setActif( boolean actif )
    {
        _bActif = actif;
    }

    public ImageResource getImage( )
    {
        return _image;
    }

    public void setImage( ImageResource image )
    {
        _image = image;
    }

    public String getImageUrl( )
    {
        return _strImageUrl;
    }

    public void setImageUrl( String imageUrl )
    {
        _strImageUrl = imageUrl;
    }

    public void setMimeType( String strMimeType )
    {
        _image.setMimeType( strMimeType );
    }

    public void setImageContent( byte[] imageContent )
    {
        _image.setImage( imageContent );
    }

    /**
     * To string representation
     *
     * @return formatted string
     */
    public String getFormatTypeSignalement( )
    {
        if ( getTypeSignalementParent( ) != null )
        {
            return getTypeSignalementParent( ).getFormatTypeSignalement( ) + " > " + _strLibelle;
        } else
        {
            return _strLibelle;
        }
    }

    /**
     * @return the unit
     */
    public Unit getUnit( )
    {
        return _unit;
    }

    /**
     * @param pUnit
     *            the unit to set
     */
    public void setUnit( Unit pUnit )
    {
        _unit = pUnit;
    }

    public void setTypeSignalementParent( TypeSignalement typeSignalement )
    {
        _typeSignalementParent = typeSignalement;
    }

    public TypeSignalement getTypeSignalementParent( )
    {
        return _typeSignalementParent;
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
        return Integer.toString( getId( ) );
    }

    public Integer getIdCategory( )
    {
        return _nIdCategory;
    }

    public void setIdCategory( Integer nIdCategory )
    {
        this._nIdCategory = nIdCategory;
    }

    /**
     * Retrieves the root (category) for the type
     *
     * @return
     */
    @JsonIgnore
    public TypeSignalement getRoot( )
    {
        if ( getTypeSignalementParent( ) != null )
        {
            return getTypeSignalementParent( ).getRoot( );
        } else
        {
            return this;
        }
    }

    /**
     * Getter for the alias
     *
     * @return the alias
     */
    public String getAlias( )
    {
        return _strAlias;
    }

    /**
     * Setter for the alias
     *
     * @param alias
     *            the alias to set
     */
    public void setAlias( String alias )
    {
        this._strAlias = alias;
    }

    /**
     * Returns the default Libelle if no alias is set
     *
     * @return
     */
    public String getAliasDefault( )
    {
        if ( StringUtils.isNotEmpty( _strAlias ) )
        {
            return _strAlias;
        }

        return getLibelle( );
    }

    /**
     * Getter for the alias mobile
     *
     * @return the alias mobile
     */
    public String getAliasMobile( )
    {
        return _strAliasMobile;
    }

    /**
     * Setter for the alias mobile
     *
     * @param aliasMobile
     *            the alias mobile to set
     */
    public void setAliasMobile( String aliasMobile )
    {
        this._strAliasMobile = aliasMobile;
    }

    /**
     * Returns the default Libelle if no alias mobile is set
     *
     * @return
     */
    public String getAliasMobileDefault( )
    {
        if ( StringUtils.isNotEmpty( _strAliasMobile ) )
        {
            return _strAliasMobile;
        }

        return getLibelle( );
    }

}
