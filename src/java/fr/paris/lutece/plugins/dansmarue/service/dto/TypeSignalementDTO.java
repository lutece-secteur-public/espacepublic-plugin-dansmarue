/*
 * Copyright (c) 2002-2021, City of Paris
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
package fr.paris.lutece.plugins.dansmarue.service.dto;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.portal.service.image.ImageResource;

/**
 * The Class TypeSignalementDTO.
 */
public class TypeSignalementDTO
{

    /** The _actif. */
    private boolean _bActif;

    /** The id. */
    private Integer _nId;

    /** The parent report type id. */
    private Integer _nIdTypeSignalementParent;

    /** The _is selected. */
    private boolean _bIsSelected;

    /** The libelle. */
    private String _strLibelle;

    /** The imageUrl. */
    private String _strImageUrl;

    /** The imageResource. */
    private ImageResource _image;

    /** The list child. */
    private List<TypeSignalementDTO> _listChild = new ArrayList<TypeSignalementDTO>( );

    /** The _minus. */
    private boolean _bMinus;

    /** The parent report type. */
    private TypeSignalement _typeSignalementParent;

    /** The unit. */
    private Unit _unit;

    /** The str alias. */
    private String _strAlias;

    /** The str alias mobile. */
    private String _strAliasMobile;

    /** The b is agent. */
    private boolean _bIsAgent;

    /** The b hors DMR. */
    private boolean _bHorsDMR;

    /** The str message hors DMR. */
    private String _strMessageHorsDMR;

    /** Le nombre de sous niveau. */
    private Integer nbSousNiveau;

    /** Le niveau du type de signalement dans sa hierachie. */
    private Integer niveau;

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
     * Gets the parent report type id.
     *
     * @return the parent report type id
     */
    public Integer getIdTypeSignalementParent( )
    {
        return _nIdTypeSignalementParent;
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
     * Gets the imageUrl.
     *
     * @return the imageUrl
     */
    public String getImageUrl( )
    {
        return _strImageUrl;
    }

    /**
     * Gets the list child.
     *
     * @return the list child
     */
    public List<TypeSignalementDTO> getListChild( )
    {
        return _listChild;
    }

    /**
     * Gets the parent report type.
     *
     * @return the parent report type
     */
    public TypeSignalement getTypeSignalementParent( )
    {
        return _typeSignalementParent;
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
     * Checks if is active.
     *
     * @return true, if is active
     */
    public boolean isActif( )
    {
        return _bActif;
    }

    /**
     * Checks if is minus.
     *
     * @return true, if is minus
     */
    public boolean isMinus( )
    {
        return _bMinus;
    }

    /**
     * Checks if is selected.
     *
     * @return true, if is selected
     */
    protected boolean isSelected( )
    {
        return _bIsSelected;
    }

    /**
     * Sets the active.
     *
     * @param actif
     *            the new active
     */
    public void setActif( boolean actif )
    {
        _bActif = actif;
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
     * Sets the parent report type id.
     *
     * @param idTypeSignalementParent
     *            the new parent report type id
     */
    public void setIdTypeSignalementParent( Integer idTypeSignalementParent )
    {
        _nIdTypeSignalementParent = idTypeSignalementParent;
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
     * Sets the Image url.
     *
     * @param imageUrl
     *            the new image url
     */
    public void setImageUrl( String imageUrl )
    {
        _strImageUrl = imageUrl;
    }

    /**
     * Sets the list child.
     *
     * @param listChild
     *            the new list child
     */
    public void setListChild( List<TypeSignalementDTO> listChild )
    {
        _listChild = listChild;
    }

    /**
     * Sets the minus.
     *
     * @param minus
     *            the new minus
     */
    public void setMinus( boolean minus )
    {
        _bMinus = minus;
    }

    /**
     * Sets the selected.
     *
     * @param isSelected
     *            the new selected
     */
    protected void setSelected( boolean isSelected )
    {
        _bIsSelected = isSelected;
    }

    /**
     * Sets the parent report type id.
     *
     * @param typeSignalementParent
     *            the new parent report type id
     */
    public void setTypeSignalementParent( TypeSignalement typeSignalementParent )
    {
        _typeSignalementParent = typeSignalementParent;
    }

    /**
     * Sets the unit.
     *
     * @param unit
     *            the new unit
     */
    public void setUnit( Unit unit )
    {
        _unit = unit;
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
     * Gets the alias.
     *
     * @return the alias
     */
    public String getAlias( )
    {
        return _strAlias;
    }

    /**
     * Sets the alias.
     *
     * @param _strAlias
     *            the new alias
     */
    public void setAlias( String _strAlias )
    {
        this._strAlias = _strAlias;
    }

    /**
     * Gets the alias mobile.
     *
     * @return the alias mobile
     */
    public String getAliasMobile( )
    {
        return _strAliasMobile;
    }

    /**
     * Sets the alias mobile.
     *
     * @param _strAliasMobile
     *            the new alias mobile
     */
    public void setAliasMobile( String _strAliasMobile )
    {
        this._strAliasMobile = _strAliasMobile;
    }

    /**
     * Gets the nb sous niveau.
     *
     * @return the nb sous niveau
     */
    public Integer getNbSousNiveau( )
    {
        return nbSousNiveau;
    }

    /**
     * Sets the nb sous niveau.
     *
     * @param nbSousNiveau
     *            the new nb sous niveau
     */
    public void setNbSousNiveau( Integer nbSousNiveau )
    {
        this.nbSousNiveau = nbSousNiveau;
    }

    /**
     * Gets the niveau.
     *
     * @return the niveau
     */
    public Integer getNiveau( )
    {
        return niveau;
    }

    /**
     * Sets the level.
     *
     * @param niveau
     *            the new level
     */
    public void setLevel( Integer niveau )
    {
        this.niveau = niveau;
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
