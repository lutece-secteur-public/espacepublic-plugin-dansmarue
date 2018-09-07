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

import java.util.Date;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.Photo;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;

public class SignalementDossierDTO
{
    private Date         _dateCreation;
    private Date         _dateDebutService;
    private Date         _heureCreation;
    private Date         _heureDebutService;
    private Integer      _nId;
    private List<Action> _listActions;
    private List<Photo>  _photos;
    private String       _strAdresse;
    private String       _strNumRessource;
    private String       _strType;

    public String getAdresse( )
    {
        return _strAdresse;
    }

    public boolean getEstBleue( )
    {
        boolean datesNotNulls = ( _dateCreation != null ) && ( _dateDebutService != null );
        boolean heuresNotNulls = ( _heureCreation != null ) && ( _heureDebutService != null );

        return datesNotNulls && heuresNotNulls
                && ( _dateCreation.after( _dateDebutService ) || ( DateUtils.sameDate( _dateCreation, _dateDebutService ) && DateUtils.sameHourOrAfter( _heureCreation, _heureDebutService ) ) );
    }

    public Date getHeureCreation( )
    {
        return _heureCreation;
    }

    public Date getHeureDebutService( )
    {
        return _heureDebutService;
    }

    public Integer getId( )
    {
        return _nId;
    }

    public List<Action> getListActions( )
    {
        return _listActions;
    }

    public String getListPhotos( )
    {
        StringBuilder listURLPhotos = new StringBuilder( "" );

        if ( getPhotos( ) != null )
        {
            for ( Photo photo : getPhotos( ) )
            {
                if ( !"".equals( photo.getImageUrl( ) ) )
                {
                    listURLPhotos.append( "<img width=\"100\" src='" + photo.getImageUrl( ) + "' /> &nbsp;" );
                }
            }
        }

        return listURLPhotos.toString( );
    }

    public String getNumRessource( )
    {
        return _strNumRessource;
    }

    public List<Photo> getPhotos( )
    {
        return _photos;
    }

    public String getType( )
    {
        return _strType;
    }

    public void setAdresse( String adresse )
    {
        _strAdresse = adresse;
    }

    public void setDateCreation( String strDate )
    {
        _dateCreation = DateUtils.parseDate( strDate );
    }

    public Date getDateCreation( )
    {
        return _dateCreation;
    }

    public void setDateDebutService( String strDateDebutService )
    {
        _dateDebutService = DateUtils.parseDate( strDateDebutService );
    }

    public void setHeureCreation( Date heureCreation )
    {
        _heureCreation = heureCreation;
    }

    public void setHeureDebutService( Date heureDebutService )
    {
        _heureDebutService = heureDebutService;
    }

    public void setId( Integer id )
    {
        _nId = id;
    }

    public void setListActions( List<Action> listActions )
    {
        _listActions = listActions;
    }

    public void setNumRessource( String numRessource )
    {
        _strNumRessource = numRessource;
    }

    public void setPhotos( List<Photo> photos )
    {
        _photos = photos;
    }

    public void setType( String type )
    {
        _strType = type;
    }
}
