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

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * This is the business class for the object Aide
 */
public class Aide
{

    // Variables declarations
    private Integer       _idAide;

    @NotEmpty( message = "#i18n{signalement.validation.aide.Libelle.notEmpty}" )
    @Size( max = 255, message = "#i18n{signalement.validation.aide.Libelle.size}" )
    private String        _strLibelle;

    /** The str hypetexte url. */
    @NotBlank( message = "Le champ url est obligatoire" )
    private String        _strHypertexteUrl;

    /** The image. */
    @NotNull( message = "L'image est obligatoire" )
    private ImageResource _image = null;

    private boolean       _bActif;

    private Integer       _nOrdre;

    /**
     *
     */
    public Aide( )
    {
    }

    /**
     * @param _idAide
     * @param _strLibelle
     * @param _strTexte
     * @param _bActif
     * @param _nOrdre
     */
    public Aide( Integer idAide, String strLibelle, String strHypertexteUrl, ImageResource image, boolean bActif, Integer nOrdre )
    {
        _idAide = idAide;
        _strLibelle = strLibelle;
        _strHypertexteUrl = strHypertexteUrl;
        _image = image;
        _bActif = bActif;
        _nOrdre = nOrdre;
    }

    /**
     * Returns the Id
     *
     * @return The Id
     */
    public Integer getId( )
    {
        return _idAide;
    }

    /**
     * Sets the Id
     *
     * @param nId
     *            The Id
     */
    public void setId( Integer id )
    {
        _idAide = id;
    }

    /**
     * Returns the Libelle
     *
     * @return The Libelle
     */
    public String getLibelle( )
    {
        return _strLibelle;
    }

    /**
     * Sets the Libelle
     *
     * @param strLibelle
     *            The Libelle
     */
    public void setLibelle( String strLibelle )
    {
        _strLibelle = strLibelle;
    }

    public void setHypertexteUrl( String hypertexteUrl )
    {
        _strHypertexteUrl = hypertexteUrl;
    }

    /**
     * Gets the hypertexte url.
     *
     * @return the hypertexte url
     */
    public String getHypertexteUrl( )
    {
        return _strHypertexteUrl;

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
     * Sets the image content.
     *
     * @param imageContent
     *            the new image content
     */
    public void setImageContent( byte[] imageContent )
    {
        _image.setImage( imageContent );
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
     * Returns the Actif
     *
     * @return The Actif
     */
    public boolean getActif( )
    {
        return _bActif;
    }

    /**
     * Sets the Actif
     *
     * @param bActif
     *            The Actif
     */
    public void setActif( boolean actif )
    {
        _bActif = actif;
    }

    /**
     * Returns the Ordre
     *
     * @return The Ordre
     */
    public Integer getOrdre( )
    {
        return _nOrdre;
    }

    /**
     * Sets the Ordre
     *
     * @param nOrdre
     *            The Ordre
     */
    public void setOrdre( Integer ordre )
    {
        _nOrdre = ordre;
    }

    public String getImageUrl( )
    {
        return AppPropertiesService.getProperty( SignalementConstants.PROPERTY_URL_HELP_PICTURE ) + getId( );
    }

    @AssertTrue( message = "Le champ image est obligatoire" )
    private boolean isImageOk( )
    {
        return ( _idAide != null ) || ( _image.getImage( ) != null );
    }
}
