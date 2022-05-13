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
package fr.paris.lutece.plugins.dansmarue.service.dto;

/**
 * The Class DossierSignalementDTO.
 */
public class DossierSignalementDTO
{

    /** The id. */
    private Long _id;

    /** The str adresse. */
    private String _strAdresse;

    /** The str date creation. */
    private String _strDateCreation;

    /** The str commentaire. */
    private String _strCommentaire;

    /** The str heure creation. */
    private String _strHeureCreation;

    /** The str type. */
    private String _strType;

    /** The str img url. */
    private String _strImgUrl;

    /** The lat. */
    private double _lat;

    /** The lng. */
    private double _lng;

    /** The n distance. */
    private Integer _nDistance;

    /** The str prefix. */
    private String _strPrefix;

    /** The n id category. */
    private Integer _nIdCategory;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId( )
    {
        return _id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId( Long id )
    {
        _id = id;
    }

    /**
     * Gets the adresse.
     *
     * @return the adresse
     */
    public String getAdresse( )
    {
        return _strAdresse;
    }

    /**
     * Sets the adresse.
     *
     * @param adresse
     *            the new adresse
     */
    public void setAdresse( String adresse )
    {
        _strAdresse = adresse;
    }

    /**
     * Gets the date creation.
     *
     * @return the date creation
     */
    public String getDateCreation( )
    {
        return _strDateCreation;
    }

    /**
     * Sets the date creation.
     *
     * @param dateCreation
     *            the new date creation
     */
    public void setDateCreation( String dateCreation )
    {
        _strDateCreation = dateCreation;
    }

    /**
     * Gets the heure creation.
     *
     * @return the heure creation
     */
    public String getHeureCreation( )
    {
        return _strHeureCreation;
    }

    /**
     * Sets the heure creation.
     *
     * @param heureCreation
     *            the new heure creation
     */
    public void setHeureCreation( String heureCreation )
    {
        _strHeureCreation = heureCreation;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType( )
    {
        return _strType;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType( String type )
    {
        _strType = type;
    }

    /**
     * Gets the img url.
     *
     * @return the img url
     */
    public String getImgUrl( )
    {
        return _strImgUrl;
    }

    /**
     * Sets the img url.
     *
     * @param imgUrl
     *            the new img url
     */
    public void setImgUrl( String imgUrl )
    {
        _strImgUrl = imgUrl;
    }

    /**
     * Gets the distance.
     *
     * @return the distance
     */
    public Integer getDistance( )
    {
        return _nDistance;
    }

    /**
     * Sets the distance.
     *
     * @param distance
     *            the new distance
     */
    public void setDistance( Integer distance )
    {
        _nDistance = distance;
    }

    /**
     * Gets the lat.
     *
     * @return the lat
     */
    public double getLat( )
    {
        return _lat;
    }

    /**
     * Sets the lat.
     *
     * @param lat
     *            the new lat
     */
    public void setLat( double lat )
    {
        _lat = lat;
    }

    /**
     * Gets the lng.
     *
     * @return the lng
     */
    public double getLng( )
    {
        return _lng;
    }

    /**
     * Sets the lng.
     *
     * @param lng
     *            the new lng
     */
    public void setLng( double lng )
    {
        _lng = lng;
    }

    /**
     * Gets the commentaire.
     *
     * @return the commentaire
     */
    public String getCommentaire( )
    {
        return _strCommentaire;
    }

    /**
     * Sets the commentaire.
     *
     * @param commentaire
     *            the new commentaire
     */
    public void setCommentaire( String commentaire )
    {
        _strCommentaire = commentaire;
    }

    /**
     * Gets the prefix.
     *
     * @return the prefix
     */
    public String getPrefix( )
    {
        return _strPrefix;
    }

    /**
     * Sets the prefix.
     *
     * @param strPrefix
     *            the new prefix
     */
    public void setPrefix( String strPrefix )
    {
        _strPrefix = strPrefix;
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
     * @param idCategory
     *            the new id category
     */
    public void setIdCategory( Integer idCategory )
    {
        _nIdCategory = idCategory;
    }
}
