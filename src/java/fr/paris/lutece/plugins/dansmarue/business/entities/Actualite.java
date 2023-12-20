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
 * The Class Actualite.
 */
public class Actualite
{

    /** The id. */
    private Integer _idActualite;

    /** The str libelle. */
    @NotBlank( message = "Le champ libellé est obligatoire" )
    private String _strLibelle;

    /** The str texte. */
    @NotEmpty( message = "Le champ texte d\'actualité est obligatoire" )
    @Size( max = 700, message = "La taille du texte d'actualité est trop importante" )
    private String _strTexte;

    /** The image. */
    @NotNull( message = "L'image est obligatoire" )
    private ImageResource _image = null;

    /** The b actif. */
    private boolean _bActif;

    /** The n ordre. */
    private Integer _nOrdre;

    /**
     *
     */
    public Actualite( )
    {

    }

    /**
     * @param idActualite
     * @param strLibelle
     * @param image
     * @param strHypertexteUrl
     * @param bActif
     * @param nOrdre
     */
    public Actualite( Integer idActualite, String strLibelle, String strTexte, ImageResource image, boolean bActif, Integer nOrdre )
    {
        _idActualite = idActualite;
        _strLibelle = strLibelle;
        _strTexte = strTexte;
        _image = image;
        _bActif = bActif;
        _nOrdre = nOrdre;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId( )
    {
        return _idActualite;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId( Integer id )
    {
        _idActualite = id;
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
     * @param strLibelle
     *            the new libelle
     */
    public void setLibelle( String strLibelle )
    {
        _strLibelle = strLibelle;
    }

    /**
     * Sets the texte.
     *
     * @param hypertexteUrl
     *            the new hypertexte url
     */
    public void setTexte( String texte )
    {
        _strTexte = texte;
    }

    /**
     * Gets the texte.
     *
     * @return the texte
     */
    public String getTexte( )
    {
        return _strTexte;

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
    public void setImageContent( byte [ ] imageContent )
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
     * Gets the ordre.
     *
     * @return the ordre
     */
    public Integer getOrdre( )
    {
        return _nOrdre;
    }

    /**
     * Sets the ordre.
     *
     * @param ordre
     *            the new ordre
     */
    public void setOrdre( Integer ordre )
    {
        _nOrdre = ordre;
    }

    public String getImageUrl( )
    {
        return AppPropertiesService.getProperty( SignalementConstants.PROPERTY_URL_NEWS_PICTURE ) + getId( );
    }

    @AssertTrue( message = "Le champ image est obligatoire" )
    private boolean isImageOk( )
    {
        return ( _idActualite != null ) || ( _image.getImage( ) != null );
    }

}
