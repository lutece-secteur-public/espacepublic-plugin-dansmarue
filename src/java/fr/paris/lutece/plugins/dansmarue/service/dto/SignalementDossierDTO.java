/*
 * Copyright (c) 2002-2020, City of Paris
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

/**
 * The Class SignalementDossierDTO.
 */
public class SignalementDossierDTO
{

    /** The date creation. */
    private Date         _dateCreation;

    /** The date debut service. */
    private Date         _dateDebutService;

    /** The heure creation. */
    private Date         _heureCreation;

    /** The heure debut service. */
    private Date         _heureDebutService;

    /** The n id. */
    private Integer      _nId;

    /** The list actions. */
    private List<Action> _listActions;

    /** The photos. */
    private List<Photo>  _photos;

    /** The str adresse. */
    private String       _strAdresse;

    /** The str num ressource. */
    private String       _strNumRessource;

    /** The str type. */
    private String       _strType;

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
     * Gets the est bleue.
     *
     * @return the est bleue
     */
    public boolean getEstBleue( )
    {
        boolean datesNotNulls = ( _dateCreation != null ) && ( _dateDebutService != null );
        boolean heuresNotNulls = ( _heureCreation != null ) && ( _heureDebutService != null );

        return datesNotNulls && heuresNotNulls
                && ( _dateCreation.after( _dateDebutService ) || ( DateUtils.sameDate( _dateCreation, _dateDebutService ) && DateUtils.sameHourOrAfter( _heureCreation, _heureDebutService ) ) );
    }

    /**
     * Gets the heure creation.
     *
     * @return the heure creation
     */
    public Date getHeureCreation( )
    {
        return _heureCreation;
    }

    /**
     * Gets the heure debut service.
     *
     * @return the heure debut service
     */
    public Date getHeureDebutService( )
    {
        return _heureDebutService;
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
     * Gets the list actions.
     *
     * @return the list actions
     */
    public List<Action> getListActions( )
    {
        return _listActions;
    }

    /**
     * Gets the list photos.
     *
     * @return the list photos
     */
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

    /**
     * Gets the num ressource.
     *
     * @return the num ressource
     */
    public String getNumRessource( )
    {
        return _strNumRessource;
    }

    /**
     * Gets the photos.
     *
     * @return the photos
     */
    public List<Photo> getPhotos( )
    {
        return _photos;
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
     * Sets the adresse.
     *
     * @param adresse the new adresse
     */
    public void setAdresse( String adresse )
    {
        _strAdresse = adresse;
    }

    /**
     * Sets the date creation.
     *
     * @param strDate the new date creation
     */
    public void setDateCreation( String strDate )
    {
        _dateCreation = DateUtils.parseDate( strDate );
    }

    /**
     * Gets the date creation.
     *
     * @return the date creation
     */
    public Date getDateCreation( )
    {
        return _dateCreation;
    }

    /**
     * Sets the date debut service.
     *
     * @param strDateDebutService the new date debut service
     */
    public void setDateDebutService( String strDateDebutService )
    {
        _dateDebutService = DateUtils.parseDate( strDateDebutService );
    }

    /**
     * Sets the heure creation.
     *
     * @param heureCreation the new heure creation
     */
    public void setHeureCreation( Date heureCreation )
    {
        _heureCreation = heureCreation;
    }

    /**
     * Sets the heure debut service.
     *
     * @param heureDebutService the new heure debut service
     */
    public void setHeureDebutService( Date heureDebutService )
    {
        _heureDebutService = heureDebutService;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId( Integer id )
    {
        _nId = id;
    }

    /**
     * Sets the list actions.
     *
     * @param listActions the new list actions
     */
    public void setListActions( List<Action> listActions )
    {
        _listActions = listActions;
    }

    /**
     * Sets the num ressource.
     *
     * @param numRessource the new num ressource
     */
    public void setNumRessource( String numRessource )
    {
        _strNumRessource = numRessource;
    }

    /**
     * Sets the photos.
     *
     * @param photos the new photos
     */
    public void setPhotos( List<Photo> photos )
    {
        _photos = photos;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public void setType( String type )
    {
        _strType = type;
    }
}
