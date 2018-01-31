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
package fr.paris.lutece.plugins.dansmarue.service.dto;

import java.util.List;

/**
 *
 * This DTO is used in the Signalement Front Office Step 2 This library allows module-formengine-signalement to have access to plugin-ramen methods
 *
 */

public class DossierSignalementDTO
{
    private Long                id;
    private String              adresse;
    private String              dateCreation;
    private String              commentaire;
    private String              heureCreation;
    private List<EncombrantDTO> encombrants;
    private String              type;
    private String              imgUrl;
    private double              lat;
    private double              lng;
    private Integer             distance;
    private String              prefix;
    private Integer             idCategory;

    public Long getId( )
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getAdresse( )
    {
        return adresse;
    }

    public void setAdresse( String adresse )
    {
        this.adresse = adresse;
    }

    public String getDateCreation( )
    {
        return dateCreation;
    }

    public void setDateCreation( String dateCreation )
    {
        this.dateCreation = dateCreation;
    }

    public String getHeureCreation( )
    {
        return heureCreation;
    }

    public void setHeureCreation( String heureCreation )
    {
        this.heureCreation = heureCreation;
    }

    public List<EncombrantDTO> getEncombrants( )
    {
        return encombrants;
    }

    public void setEncombrants( List<EncombrantDTO> encombrants )
    {
        this.encombrants = encombrants;
    }

    public String getType( )
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getImgUrl( )
    {
        return imgUrl;
    }

    public void setImgUrl( String imgUrl )
    {
        this.imgUrl = imgUrl;
    }

    public Integer getDistance( )
    {
        return distance;
    }

    public void setDistance( Integer distance )
    {
        this.distance = distance;
    }

    public double getLat( )
    {
        return lat;
    }

    public void setLat( double lat )
    {
        this.lat = lat;
    }

    public double getLng( )
    {
        return lng;
    }

    public void setLng( double lng )
    {
        this.lng = lng;
    }

    public String getCommentaire( )
    {
        return commentaire;
    }

    public void setCommentaire( String commentaire )
    {
        this.commentaire = commentaire;
    }

    /**
     * @return the prefix
     */
    public String getPrefix( )
    {
        return prefix;
    }

    /**
     * @param strPrefix
     *            the prefix to set
     */
    public void setPrefix( String strPrefix )
    {
        prefix = strPrefix;
    }

    public Integer getIdCategory( )
    {
        return idCategory;
    }

    public void setIdCategory( Integer idCategory )
    {
        this.idCategory = idCategory;
    }
}
