/*
 * Copyright (c) 2002-2022, City of Paris
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

/**
 * The Class TypeSignalement.
 */
public class TypeSignalement implements RBACResource
{

    /** The Constant RESOURCE_TYPE. */
    public static final String RESOURCE_TYPE = "TYPE_SIGNALEMENT";

    /** The n id. */
    private Integer _nId;

    /** The str libelle. */
    @NotBlank
    private String _strLibelle;

    /** The b actif. */
    private boolean _bActif;

    /** The b is agent. */
    private boolean _bIsAgent;

    /** The type signalement parent. */
    @JsonBackReference
    private TypeSignalement _typeSignalementParent = null;

    /** The unit. */
    private Unit _unit = new Unit( );

    /** The n id type signalement parent. */
    private Integer _nIdTypeSignalementParent;

    /** The image. */
    private ImageResource _image = null;

    /** The n ordre. */
    @NotNull
    @Min( 1 )
    private int _nOrdre;

    /** The str image url. */
    private String _strImageUrl = "";

    /** The n id category. */
    @Transient
    private Integer _nIdCategory;

    /** The str alias. */
    private String _strAlias;

    /** The str alias mobile. */
    private String _strAliasMobile;

    /** The n nb messages. */
    private Integer _nNbMessages;

    /** The b hors DMR. */
    private boolean _bHorsDMR;

    /** The str message hors DMR. */
    private String _strMessageHorsDMR;

    /**
     * Gets the ordre.
     *
     * @return the ordre
     */
    public int getOrdre( )
    {
        return _nOrdre;
    }

    /**
     * Sets the ordre.
     *
     * @param ordre
     *            the new ordre
     */
    public void setOrdre( int ordre )
    {
        _nOrdre = ordre;
    }

    /**
     * Gets the id type signalement parent.
     *
     * @return the id type signalement parent
     */
    public Integer getIdTypeSignalementParent( )
    {
        return _nIdTypeSignalementParent;
    }

    /**
     * Sets the id type signalement parent.
     *
     * @param idTypeSignalementParent
     *            the new id type signalement parent
     */
    public void setIdTypeSignalementParent( Integer idTypeSignalementParent )
    {
        _nIdTypeSignalementParent = idTypeSignalementParent;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId( )
    {
        return _nId;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId( Integer id )
    {
        _nId = id;
    }

    /**
     * Gets the libelle.
     *
     * @return the libelle
     */
    public String getLibelle( )
    {
        return _strLibelle;
    }

    /**
     * Sets the libelle.
     *
     * @param libelle
     *            the new libelle
     */
    public void setLibelle( String libelle )
    {
        _strLibelle = libelle;
    }

    /**
     * Gets the actif.
     *
     * @return the actif
     */
    public boolean getActif( )
    {
        return _bActif;
    }

    /**
     * Sets the actif.
     *
     * @param actif
     *            the new actif
     */
    public void setActif( boolean actif )
    {
        _bActif = actif;
    }

    /**
     * Gets the checks if is agent.
     *
     * @return the checks if is agent
     */
    public boolean getIsAgent( )
    {
        return _bIsAgent;
    }

    /**
     * Sets the checks if is agent.
     *
     * @param bIsAgent
     *            the new checks if is agent
     */
    public void setIsAgent( boolean bIsAgent )
    {
        _bIsAgent = bIsAgent;
    }

    /**
     * Gets the image.
     *
     * @return the image
     */
    public ImageResource getImage( )
    {
        return _image;
    }

    /**
     * Sets the image.
     *
     * @param image
     *            the new image
     */
    public void setImage( ImageResource image )
    {
        _image = image;
    }

    /**
     * Gets the image url.
     *
     * @return the image url
     */
    public String getImageUrl( )
    {
        return _strImageUrl;
    }

    /**
     * Sets the image url.
     *
     * @param imageUrl
     *            the new image url
     */
    public void setImageUrl( String imageUrl )
    {
        _strImageUrl = imageUrl;
    }

    /**
     * Sets the mime type.
     *
     * @param strMimeType
     *            the new mime type
     */
    public void setMimeType( String strMimeType )
    {
        _image.setMimeType( strMimeType );
    }

    /**
     * Sets the image content.
     *
     * @param imageContent
     *            the new image content
     */
    public void setImageContent( byte [ ] imageContent )
    {
        _image.setImage( imageContent );
    }

    /**
     * To string representation.
     *
     * @return formatted string
     */
    public String getFormatTypeSignalement( )
    {
        if ( getTypeSignalementParent( ) != null )
        {
            return getTypeSignalementParent( ).getFormatTypeSignalement( ) + " > " + _strLibelle;
        }
        else
        {
            return _strLibelle;
        }
    }

    /**
     * Gets the unit.
     *
     * @return the unit
     */
    public Unit getUnit( )
    {
        return _unit;
    }

    /**
     * Sets the unit.
     *
     * @param pUnit
     *            the unit to set
     */
    public void setUnit( Unit pUnit )
    {
        _unit = pUnit;
    }

    /**
     * Sets the type signalement parent.
     *
     * @param typeSignalement
     *            the new type signalement parent
     */
    public void setTypeSignalementParent( TypeSignalement typeSignalement )
    {
        _typeSignalementParent = typeSignalement;
    }

    /**
     * Gets the type signalement parent.
     *
     * @return the type signalement parent
     */
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

    /**
     * Gets the id category.
     *
     * @return the id category
     */
    public Integer getIdCategory( )
    {
        return _nIdCategory;
    }

    /**
     * Sets the id category.
     *
     * @param nIdCategory
     *            the new id category
     */
    public void setIdCategory( Integer nIdCategory )
    {
        _nIdCategory = nIdCategory;
    }

    /**
     * Gets the root.
     *
     * @return the root
     */
    @JsonIgnore
    public TypeSignalement getRoot( )
    {
        if ( getTypeSignalementParent( ) != null )
        {
            return getTypeSignalementParent( ).getRoot( );
        }
        else
        {
            return this;
        }
    }

    /**
     * Getter for the alias.
     *
     * @return the alias
     */
    public String getAlias( )
    {
        return _strAlias;
    }

    /**
     * Setter for the alias.
     *
     * @param alias
     *            the alias to set
     */
    public void setAlias( String alias )
    {
        _strAlias = alias;
    }

    /**
     * Gets the alias default.
     *
     * @return the alias default
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
     * Getter for the alias mobile.
     *
     * @return the alias mobile
     */
    public String getAliasMobile( )
    {
        return _strAliasMobile;
    }

    /**
     * Setter for the alias mobile.
     *
     * @param aliasMobile
     *            the alias mobile to set
     */
    public void setAliasMobile( String aliasMobile )
    {
        _strAliasMobile = aliasMobile;
    }

    /**
     * Gets the alias mobile default.
     *
     * @return the alias mobile default
     */
    public String getAliasMobileDefault( )
    {
        if ( StringUtils.isNotEmpty( _strAliasMobile ) )
        {
            return _strAliasMobile;
        }

        return getLibelle( );
    }

    /**
     * Gets the nb messages.
     *
     * @return the nb messages
     */
    public Integer getNbMessages( )
    {
        return _nNbMessages;
    }

    /**
     * Sets the nb messages.
     *
     * @param _nNbMessages
     *            the new nb messages
     */
    public void setNbMessages( Integer _nNbMessages )
    {
        this._nNbMessages = _nNbMessages;
    }

    /**
     * Checks if is hors DMR.
     *
     * @return true, if is hors DMR
     */
    public boolean isHorsDMR( )
    {
        return _bHorsDMR;
    }

    /**
     * Sets the hors DMR.
     *
     * @param horsDMR
     *            the new hors DMR
     */
    public void setHorsDMR( boolean horsDMR )
    {
        _bHorsDMR = horsDMR;
    }

    /**
     * Gets the message hors DMR.
     *
     * @return the message hors DMR
     */
    public String getMessageHorsDMR( )
    {
        return _strMessageHorsDMR;
    }

    /**
     * Sets the message hors DMR.
     *
     * @param messageHorsDMR
     *            the new message hors DMR
     */
    public void setMessageHorsDMR( String messageHorsDMR )
    {
        _strMessageHorsDMR = messageHorsDMR;
    }

}
