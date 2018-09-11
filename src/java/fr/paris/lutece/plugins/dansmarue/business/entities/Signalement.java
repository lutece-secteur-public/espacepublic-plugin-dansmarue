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

    public List<Adresse> getAdresses( )
    {
        return _listAdresses;
    }

    public int getAnnee( )
    {
        return _nAnnee;
    }

    public Arrondissement getArrondissement( )
    {
        return _arrondissement;
    }

    public String getCommentaire( )
    {
        return _strCommentaire;
    }

    public String getCommentaireProgrammation( )
    {
        return _strCommentaireProgrammation;
    }

    public String getDateCreation( )
    {
        return _strDateCreation;
    }

    public String getDatePrevueTraitement( )
    {
        return _strDatePrevueTraitement;
    }

    public Unit getDirectionSector( )
    {
        return _direction;
    }

    public Date getHeureCreation( )
    {
        return _heureCreation;
    }

    public Long getId( )
    {
        return _id;
    }

    public String getListAdresses( )
    {
        StringBuilder result = new StringBuilder( "" );
        for ( Adresse adresse : _listAdresses )
        {

            genererLienGoogleMap( result, adresse );
        }
        return result.toString( );
    }

    public String getMois( )
    {
        return _strMois;
    }

    public int getNumero( )
    {
        return _nNumero;
    }

    public String getNumeroSignalement( )
    {
        return getPrefix( ) + getAnnee( ) + getMois( ) + getNumero( );
    }

    public List<PhotoDMR> getPhotos( )
    {
        return _listPhotos;
    }

    public String getPrefix( )
    {
        return _strPrefix;
    }

    public Priorite getPriorite( )
    {
        return _priorite;
    }

    public String getPrioriteName( )
    {
        return _priorite.getLibelle( );
    }

    public Sector getSecteur( )
    {
        return _secteur;
    }

    public List<Signaleur> getSignaleurs( )
    {
        return _listSignaleurs;
    }

    public String getType( )
    {
        String formatTypeSignalement = null;
        if ( _typeSignalement != null )
        {
            formatTypeSignalement = _typeSignalement.getFormatTypeSignalement( );
        }
        return formatTypeSignalement;
    }

    public TypeSignalement getTypeSignalement( )
    {
        return _typeSignalement;
    }

    public Unit getUnit( )
    {
        return _unit;
    }

    public int getSuivi( )
    {
        return _nSuivi;
    }

    public void setAdresses( List<Adresse> pAdresses )
    {
        _listAdresses = pAdresses;
    }

    public void setAnnee( int annee )
    {
        _nAnnee = annee;
    }

    public void setArrondissement( Arrondissement arrondissement )
    {
        _arrondissement = arrondissement;
    }

    public void setCommentaire( String commentaire )
    {
        _strCommentaire = commentaire;
    }

    public void setCommentaireProgrammation( String commentaireProgrammation )
    {
        _strCommentaireProgrammation = commentaireProgrammation;
    }

    public void setDateCreation( String dateCreation )
    {
        _strDateCreation = dateCreation;
    }

    public void setDatePrevueTraitement( String datePrevueTraitement )
    {
        _strDatePrevueTraitement = datePrevueTraitement;
    }

    public void setHeureCreation( Date heureCreation )
    {
        _heureCreation = heureCreation;
    }

    public void setId( Long id )
    {
        _id = id;
    }

    public void setMois( String mois )
    {
        _strMois = mois;
    }

    public void setNumero( int numero )
    {
        _nNumero = numero;
    }

    public void setPhotos( List<PhotoDMR> pPhotos )
    {
        _listPhotos = pPhotos;
    }

    public void setPrefix( String prefix )
    {
        _strPrefix = prefix;
    }

    public void setPriorite( Priorite priorite )
    {
        _priorite = priorite;
    }

    public void setSecteur( Sector secteur )
    {
        _secteur = secteur;
    }

    public void setSignaleurs( List<Signaleur> pSignaleurs )
    {
        _listSignaleurs = pSignaleurs;
    }

    public void setTypeSignalement( TypeSignalement typeSignalement )
    {
        _typeSignalement = typeSignalement;
    }

    public void setUnit( Unit unit )
    {
        _unit = unit;
    }

    public void setSuivi( int suivi )
    {
        _nSuivi = suivi;
    }

    public void setIsDoublon( boolean bIsDoublon )
    {
        _bIsDoublon = bIsDoublon;
    }

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

    public void setAdressesForm( int index, Adresse adresse )
    {
        while ( index >= _listAdresses.size( ) )
        {
            _listAdresses.add( new Adresse( ) );
        }

        _listAdresses.add( index, adresse );
    }

    public Adresse getAdressesForm( int index )
    {
        while ( index >= _listAdresses.size( ) )
        {
            _listAdresses.add( new Adresse( ) );
        }

        return _listAdresses.get( index );
    }

    public String getDateServiceFaitTraitement( )
    {
        return _strDateServiceFaitTraitement;
    }

    public void setDateServiceFaitTraitement( String strDateServiceFaitTraitement )
    {
        this._strDateServiceFaitTraitement = strDateServiceFaitTraitement;
    }

    public String getHeureServiceFaitTraitement( )
    {
        return _strHeureServiceFaitTraitement;
    }

    public void setHeureServiceFaitTraitement( String strHeureServiceFaitTraitement )
    {
        this._strHeureServiceFaitTraitement = strHeureServiceFaitTraitement;
    }

    public int getFelicitations( )
    {
        return _nFelicitations;
    }

    public void setFelicitations( int nFelicitations )
    {
        _nFelicitations = nFelicitations;
    }

    public List<ObservationRejet> getObservationsRejet( )
    {
        return _listObservationsRejet;
    }

    public void setObservationsRejet( List<ObservationRejet> observationsRejet )
    {
        this._listObservationsRejet = observationsRejet;
    }

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

    public String getDateMiseEnSurveillance( )
    {
        return _strDateMiseEnSurveillance;
    }

    public void setDateMiseEnSurveillance( String dateMiseEnSurveillance )
    {
        this._strDateMiseEnSurveillance = dateMiseEnSurveillance;
    }

    public String getDateRejet( )
    {
        return _strDateRejet;
    }

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

    public boolean getIsSendWS( )
    {
        return _bIsSendWS;
    }

    public void setSendWs( boolean isSendWS )
    {
        this._bIsSendWS = isSendWS;
    }

}
