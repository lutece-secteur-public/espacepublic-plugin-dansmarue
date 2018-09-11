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

public class DossierSignalementDTO
{
    private Long    _id;
    private String  _strAdresse;
    private String  _strDateCreation;
    private String  _strCommentaire;
    private String  _strHeureCreation;
    private String  _strType;
    private String  _strImgUrl;
    private double  _lat;
    private double  _lng;
    private Integer _nDistance;
    private String  _strPrefix;
    private Integer _nIdCategory;

    public Long getId( )
    {
        return _id;
    }

    public void setId( Long id )
    {
        this._id = id;
    }

    public String getAdresse( )
    {
        return _strAdresse;
    }

    public void setAdresse( String adresse )
    {
        this._strAdresse = adresse;
    }

    public String getDateCreation( )
    {
        return _strDateCreation;
    }

    public void setDateCreation( String dateCreation )
    {
        this._strDateCreation = dateCreation;
    }

    public String getHeureCreation( )
    {
        return _strHeureCreation;
    }

    public void setHeureCreation( String heureCreation )
    {
        this._strHeureCreation = heureCreation;
    }

    public String getType( )
    {
        return _strType;
    }

    public void setType( String type )
    {
        this._strType = type;
    }

    public String getImgUrl( )
    {
        return _strImgUrl;
    }

    public void setImgUrl( String imgUrl )
    {
        this._strImgUrl = imgUrl;
    }

    public Integer getDistance( )
    {
        return _nDistance;
    }

    public void setDistance( Integer distance )
    {
        this._nDistance = distance;
    }

    public double getLat( )
    {
        return _lat;
    }

    public void setLat( double lat )
    {
        this._lat = lat;
    }

    public double getLng( )
    {
        return _lng;
    }

    public void setLng( double lng )
    {
        this._lng = lng;
    }

    public String getCommentaire( )
    {
        return _strCommentaire;
    }

    public void setCommentaire( String commentaire )
    {
        this._strCommentaire = commentaire;
    }

    public String getPrefix( )
    {
        return _strPrefix;
    }

    public void setPrefix( String strPrefix )
    {
        _strPrefix = strPrefix;
    }

    public Integer getIdCategory( )
    {
        return _nIdCategory;
    }

    public void setIdCategory( Integer idCategory )
    {
        this._nIdCategory = idCategory;
    }
}
