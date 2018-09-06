/*
 * Copyright (c) 2002-2018, Mairie de Paris
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.business.sector.Sector;

/**
 * The Class Signalement.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Signalement
{

    /** Workflow resource type. */
    public static final String     WORKFLOW_RESOURCE_TYPE       = "SIGNALEMENT_SIGNALEMENT";

    /** The addresses. */
    private List<Adresse>          _listAdresses                = new ArrayList<>( );

    /** The districts. */
    private Arrondissement         _arrondissement;

    /** The creation date. */
    private String                 _strDateCreation;

    /** The creation hour. */
    private Date                   _heureCreation;

    /** The monitoring date. */
    private String                 _strDateMiseEnSurveillance;

    /** The reject date. */
    private String                 _strDateRejet;

    /** The id. */
    private Long                   _id;

    /** The year. */
    private int                    _nAnnee;

    /** The number. */
    private int                    _nNumero;

    /** The follow. */
    private int                    _nSuivi;

    /** The congratulation */
    private int                    _nFelicitations;

    /** The photos. */
    private List<PhotoDMR>         _listPhotos                  = new ArrayList<>( );

    /** The priorities. */
    private Priorite               _priorite;

    /** The sector. */
    private Sector                 _secteur;

    /** The reporters list. */
    private List<Signaleur>        _listSignaleurs              = new ArrayList<>( );

    /** The commentary. */
    private String                 _strCommentaire;

    /** The scheduling commentary. */
    private String                 _strCommentaireProgrammation = "";

    /** The scheduled treatment date. */
    private String                 _strDatePrevueTraitement;

    /** The month. */
    private String                 _strMois;

    /** The prefix. */
    private String                 _strPrefix;

    /** The reporting type. */
    private TypeSignalement        _typeSignalement;

    /** The unit. */
    private Unit                   _unit                        = new Unit( );

    /** board */
    private Unit                   _direction;

    /** token */
    private String                 _strToken;

    /**
     * the passage date
     */
    private String                 _strDateServiceFaitTraitement;

    /**
     * the passage hour
     */
    private String                 _strHeureServiceFaitTraitement;

    private List<ObservationRejet> _listObservationsRejet;

    /** The attribute to check if the report is a duplicate */
    private boolean                _bIsDoublon;

    private String                 _strCourrielDestinataire;

    private String                 _strCourrielExpediteur;

    private Timestamp              _courrielDate;

    private boolean                _bIsSendWS;

    public void setDirection( Unit direction )
    {
        _unit = direction;
    }

    public void setDirectionSector( Unit direction )
    {
        _direction = direction;
    }

    /**
     * Generate google map link
     *
     * @param result
     *            the result
     * @param adresse
     *            the address
     */
    private void genererLienGoogleMap( StringBuilder result, Adresse adresse )
    {
        result.append( "<a class=\"map\" href=\"https://maps.google.fr/?t=h&z=18&q=" );
        result.append( adresse.getLat( ) );
        result.append( "," );
        result.append( adresse.getLng( ) );
        result.append( "+(" );
        result.append( adresse.getAdresse( ) );
        result.append( ")" );
        result.append( "\" target=\"map\">" );
        result.append( adresse.getAdresse( ) );
        result.append( "</a><br /> " );
    }

    /**
     * Gets the addresses.
     *
     * @return the addresses
     */
    public List<Adresse> getAdresses( )
    {
        return _listAdresses;
    }

    /**
     * Gets the year.
     *
     * @return the year
     */
    public int getAnnee( )
    {
        return _nAnnee;
    }

    /**
     * Gets the district.
     *
     * @return the district
     */
    public Arrondissement getArrondissement( )
    {
        return _arrondissement;
    }

    /**
     * Gets the commentary.
     *
     * @return the commentary
     */
    public String getCommentaire( )
    {
        return _strCommentaire;
    }

    /**
     * Gets the scheduling commentary.
     *
     * @return the scheduling commentary
     */
    public String getCommentaireProgrammation( )
    {
        return _strCommentaireProgrammation;
    }

    /**
     * Gets the creation date.
     *
     * @return the creation date
     */
    public String getDateCreation( )
    {
        return _strDateCreation;
    }

    /**
     * Gets the scheduled treatement date.
     *
     * @return the scheduled treatment date
     */
    public String getDatePrevueTraitement( )
    {
        return _strDatePrevueTraitement;
    }

    public Unit getDirectionSector( )
    {
        return _direction;
    }

    /**
     * Gets the creation hour.
     *
     * @return the creation hour
     */
    public Date getHeureCreation( )
    {
        return _heureCreation;
    }

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
     * Gets the addresses list.
     *
     * @return the addresses list
     */
    public String getListAdresses( )
    {
        StringBuilder result = new StringBuilder( "" );
        for ( Adresse adresse : _listAdresses )
        {

            genererLienGoogleMap( result, adresse );
        }
        return result.toString( );
    }

    /**
     * Gets the month.
     *
     * @return the month
     */
    public String getMois( )
    {
        return _strMois;
    }

    /**
     * Gets the number.
     *
     * @return the number
     */
    public int getNumero( )
    {
        return _nNumero;
    }

    /**
     * Gets the report number.
     *
     * @return the report number
     */
    public String getNumeroSignalement( )
    {
        return getPrefix( ) + getAnnee( ) + getMois( ) + getNumero( );
    }

    /**
     * Gets the photos.
     *
     * @return the photos
     */
    public List<PhotoDMR> getPhotos( )
    {
        return _listPhotos;
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
     * Gets the priorities.
     *
     * @return the priorities
     */
    public Priorite getPriorite( )
    {
        return _priorite;
    }

    /**
     * Gets the priorities name.
     *
     * @return the priorities name
     */
    public String getPrioriteName( )
    {
        return _priorite.getLibelle( );
    }

    /**
     * Gets the sector.
     *
     * @return the sector
     */
    public Sector getSecteur( )
    {
        return _secteur;
    }

    /**
     * Gets the reporters.
     *
     * @return the reporters
     */
    public List<Signaleur> getSignaleurs( )
    {
        return _listSignaleurs;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType( )
    {
        String formatTypeSignalement = null;
        if ( _typeSignalement != null )
        {
            formatTypeSignalement = _typeSignalement.getFormatTypeSignalement( );
        }
        return formatTypeSignalement;
    }

    /**
     * Gets the reporting type.
     *
     * @return the reporting type
     */
    public TypeSignalement getTypeSignalement( )
    {
        return _typeSignalement;
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
     * Gets the number of followers.
     *
     * @return the number of followers
     */
    public int getSuivi( )
    {
        return _nSuivi;
    }

    /**
     * Sets the addresses.
     *
     * @param pAdresses
     *            the addresses to set
     */
    public void setAdresses( List<Adresse> pAdresses )
    {
        _listAdresses = pAdresses;
    }

    /**
     * Sets the year.
     *
     * @param annee
     *            the new year
     */
    public void setAnnee( int annee )
    {
        _nAnnee = annee;
    }

    /**
     * Sets the district.
     *
     * @param arrondissement
     *            the new district
     */
    public void setArrondissement( Arrondissement arrondissement )
    {
        _arrondissement = arrondissement;
    }

    /**
     * Sets the commentary.
     *
     * @param commentaire
     *            the new commentary
     */
    public void setCommentaire( String commentaire )
    {
        _strCommentaire = commentaire;
    }

    /**
     * Sets the scheduling commentary.
     *
     * @param commentaireProgrammation
     *            the new scheduling commentary
     */
    public void setCommentaireProgrammation( String commentaireProgrammation )
    {
        _strCommentaireProgrammation = commentaireProgrammation;
    }

    /**
     * Sets the creation date.
     *
     * @param dateCreation
     *            the new creation date
     */
    public void setDateCreation( String dateCreation )
    {
        _strDateCreation = dateCreation;
    }

    /**
     * Sets the scheduled treatment date.
     *
     * @param datePrevueTraitement
     *            the new scheduled treatment date
     */
    public void setDatePrevueTraitement( String datePrevueTraitement )
    {
        _strDatePrevueTraitement = datePrevueTraitement;
    }

    /**
     * Sets the creation hour.
     *
     * @param heureCreation
     *            the creation hour
     */
    public void setHeureCreation( Date heureCreation )
    {
        _heureCreation = heureCreation;
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
     * Sets the month.
     *
     * @param mois
     *            the new month
     */
    public void setMois( String mois )
    {
        _strMois = mois;
    }

    /**
     * Sets the number.
     *
     * @param numero
     *            the new number
     */
    public void setNumero( int numero )
    {
        _nNumero = numero;
    }

    /**
     * Sets the photos.
     *
     * @param pPhotos
     *            the photos to set
     */
    public void setPhotos( List<PhotoDMR> pPhotos )
    {
        _listPhotos = pPhotos;
    }

    /**
     * Sets the prefix.
     *
     * @param prefix
     *            the new prefix
     */
    public void setPrefix( String prefix )
    {
        _strPrefix = prefix;
    }

    /**
     * Sets the priorities.
     *
     * @param priorite
     *            the new priorities
     */
    public void setPriorite( Priorite priorite )
    {
        _priorite = priorite;
    }

    /**
     * Sets the sector.
     *
     * @param secteur
     *            the new sector
     */
    public void setSecteur( Sector secteur )
    {
        _secteur = secteur;
    }

    /**
     * Sets the reporters.
     *
     * @param pSignaleurs
     *            the reporters to set
     */
    public void setSignaleurs( List<Signaleur> pSignaleurs )
    {
        _listSignaleurs = pSignaleurs;
    }

    /**
     * Sets the reporting type.
     *
     * @param typeSignalement
     *            the new reporting type
     */
    public void setTypeSignalement( TypeSignalement typeSignalement )
    {
        _typeSignalement = typeSignalement;
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
     * Sets the number of followers.
     *
     * @param suivi
     *            the new number of followers
     */
    public void setSuivi( int suivi )
    {
        _nSuivi = suivi;
    }

    /**
     * Sets is duplicate
     *
     * @param bIsDoublon
     *            true if the report is duplicate
     */
    public void setIsDoublon( boolean bIsDoublon )
    {
        _bIsDoublon = bIsDoublon;
    }

    /**
     * Check if the report is duplicated.
     *
     * @return true, if is duplicate
     */
    public boolean isDoublon( )
    {
        return _bIsDoublon;
    }

    public String getToken( )
    {
        return _strToken;
    }

    public void setToken( String token )
    {
        _strToken = token;
    }

    /**
     * Sets the addresses form.
     *
     * @param index
     *            index
     * @param adresse
     *            the addresses
     */
    public void setAdressesForm( int index, Adresse adresse )
    {
        while ( index >= _listAdresses.size( ) )
        {
            _listAdresses.add( new Adresse( ) );
        }

        _listAdresses.add( index, adresse );
    }

    /**
     * Gets the addresses form.
     *
     * @param index
     *            index
     * @return addresses
     */
    public Adresse getAdressesForm( int index )
    {
        while ( index >= _listAdresses.size( ) )
        {
            _listAdresses.add( new Adresse( ) );
        }

        return _listAdresses.get( index );
    }

    /**
     * @return the _strDateServiceFaitTraitement
     */
    public String getDateServiceFaitTraitement( )
    {
        return _strDateServiceFaitTraitement;
    }

    /**
     * @param _strDateServiceFaitTraitement
     *            the _strDateServiceFaitTraitement to set
     */
    public void setDateServiceFaitTraitement( String strDateServiceFaitTraitement )
    {
        this._strDateServiceFaitTraitement = strDateServiceFaitTraitement;
    }

    /**
     * @return the _strHeureServiceFaitTraitement
     */
    public String getHeureServiceFaitTraitement( )
    {
        return _strHeureServiceFaitTraitement;
    }

    /**
     * @param _strHeureServiceFaitTraitement
     *            the _strHeureServiceFaitTraitement to set
     */
    public void setHeureServiceFaitTraitement( String strHeureServiceFaitTraitement )
    {
        this._strHeureServiceFaitTraitement = strHeureServiceFaitTraitement;
    }

    /**
     * Getter for the _nFelicitation
     *
     * @return the _nFelicitation
     */
    public int getFelicitations( )
    {
        return _nFelicitations;
    }

    /**
     * Setter for the _nFelicitation
     *
     * @param nFelicitations
     *            the _nFelicitation to set
     */
    public void setFelicitations( int nFelicitations )
    {
        _nFelicitations = nFelicitations;
    }

    /**
     * Getter for the observationRejet List
     *
     * @return List of reject observation
     */
    public List<ObservationRejet> getObservationsRejet( )
    {
        return _listObservationsRejet;
    }

    /**
     * Setter for the observationRejet list
     *
     * @param observationsRejet
     *            The reject observation for this report
     */
    public void setObservationsRejet( List<ObservationRejet> observationsRejet )
    {
        this._listObservationsRejet = observationsRejet;
    }

    /**
     * Getter for the reference id for web service
     *
     * @return the reference id for web service
     */
    public String getSignalementReference( )
    {
        String reference = StringUtils.EMPTY;
        String prefix = getPrefix( );
        String annee = StringUtils.EMPTY + getAnnee( ) + StringUtils.EMPTY;
        String mois = getMois( );
        String numero = StringUtils.EMPTY + getNumero( ) + StringUtils.EMPTY;
        reference = prefix + annee + mois + numero;

        return reference;
    }

    /**
     * @return the _dateMiseEnSurveillance
     */
    public String getDateMiseEnSurveillance( )
    {
        return _strDateMiseEnSurveillance;
    }

    /**
     * @param _strDateMiseEnSurveillance
     *            the _dateMiseEnSurveillance to set
     */
    public void setDateMiseEnSurveillance( String dateMiseEnSurveillance )
    {
        this._strDateMiseEnSurveillance = dateMiseEnSurveillance;
    }

    /**
     * @return the _dateRejet
     */
    public String getDateRejet( )
    {
        return _strDateRejet;
    }

    /**
     * @param _strDateRejet
     *            the reject date
     */
    public void setDateRejet( String dateRejet )
    {
        this._strDateRejet = dateRejet;
    }

    public String getCourrielDestinataire( )
    {
        return _strCourrielDestinataire;
    }

    public void setCourrielDestinataire( String courrielDestinataire )
    {
        this._strCourrielDestinataire = courrielDestinataire;
    }

    public String getCourrielExpediteur( )
    {
        return _strCourrielExpediteur;
    }

    public void setCourrielExpediteur( String courrielExpediteur )
    {
        this._strCourrielExpediteur = courrielExpediteur;
    }

    public Timestamp getCourrielDate( )
    {
        return _courrielDate;
    }

    public void setCourrielDate( Timestamp courrielDate )
    {
        this._courrielDate = courrielDate;
    }

    /**
     * @return the isSendWS
     */
    public boolean getIsSendWS( )
    {
        return _bIsSendWS;
    }

    /**
     * @param isSendWS
     *            the isSendWS to set
     */
    public void setSendWs( boolean isSendWS )
    {
        this._bIsSendWS = isSendWS;
    }

}
