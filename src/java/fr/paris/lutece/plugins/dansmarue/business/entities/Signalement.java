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

    /**  The congratulation. */
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

    /** The str commentaire agent terrain. */
    private String                 _strCommentaireAgentTerrain;

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

    /**  board. */
    private Unit                   _direction;

    /**  token. */
    private String                 _strToken;

    /** the passage date. */
    private String                 _strDateServiceFaitTraitement;

    /** the passage hour. */
    private String                 _strHeureServiceFaitTraitement;

    /** The list observations rejet. */
    private List<ObservationRejet> _listObservationsRejet;

    /**  The attribute to check if the report is a duplicate. */
    private boolean                _bIsDoublon;

    /** The str courriel destinataire. */
    private String                 _strCourrielDestinataire;

    /** The str courriel expediteur. */
    private String                 _strCourrielExpediteur;

    /** The courriel date. */
    private Timestamp              _courrielDate;

    /** The b is send WS. */
    private boolean                _bIsSendWS;

    /**
     * Sets the direction.
     *
     * @param direction the new direction
     */
    public void setDirection( Unit direction )
    {
        _unit = direction;
    }

    /**
     * Sets the direction sector.
     *
     * @param direction the new direction sector
     */
    public void setDirectionSector( Unit direction )
    {
        _direction = direction;
    }

    /**
     * Generer lien google map.
     *
     * @param result the result
     * @param adresse the adresse
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
     * Gets the adresses.
     *
     * @return the adresses
     */
    public List<Adresse> getAdresses( )
    {
        return _listAdresses;
    }

    /**
     * Gets the annee.
     *
     * @return the annee
     */
    public int getAnnee( )
    {
        return _nAnnee;
    }

    /**
     * Gets the arrondissement.
     *
     * @return the arrondissement
     */
    public Arrondissement getArrondissement( )
    {
        return _arrondissement;
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
     * Gets the commentaire programmation.
     *
     * @return the commentaire programmation
     */
    public String getCommentaireProgrammation( )
    {
        return _strCommentaireProgrammation;
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
     * Gets the date prevue traitement.
     *
     * @return the date prevue traitement
     */
    public String getDatePrevueTraitement( )
    {
        return _strDatePrevueTraitement;
    }

    /**
     * Gets the direction sector.
     *
     * @return the direction sector
     */
    public Unit getDirectionSector( )
    {
        return _direction;
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
     * Gets the id.
     *
     * @return the id
     */
    public Long getId( )
    {
        return _id;
    }

    /**
     * Gets the list adresses.
     *
     * @return the list adresses
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
     * Gets the mois.
     *
     * @return the mois
     */
    public String getMois( )
    {
        return _strMois;
    }

    /**
     * Gets the numero.
     *
     * @return the numero
     */
    public int getNumero( )
    {
        return _nNumero;
    }

    /**
     * Gets the numero signalement.
     *
     * @return the numero signalement
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
     * Gets the priorite.
     *
     * @return the priorite
     */
    public Priorite getPriorite( )
    {
        return _priorite;
    }

    /**
     * Gets the priorite name.
     *
     * @return the priorite name
     */
    public String getPrioriteName( )
    {
        return _priorite.getLibelle( );
    }

    /**
     * Gets the secteur.
     *
     * @return the secteur
     */
    public Sector getSecteur( )
    {
        return _secteur;
    }

    /**
     * Gets the signaleurs.
     *
     * @return the signaleurs
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
     * Gets the type signalement.
     *
     * @return the type signalement
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
     * Gets the suivi.
     *
     * @return the suivi
     */
    public int getSuivi( )
    {
        return _nSuivi;
    }

    /**
     * Sets the adresses.
     *
     * @param pAdresses the new adresses
     */
    public void setAdresses( List<Adresse> pAdresses )
    {
        _listAdresses = pAdresses;
    }

    /**
     * Sets the annee.
     *
     * @param annee the new annee
     */
    public void setAnnee( int annee )
    {
        _nAnnee = annee;
    }

    /**
     * Sets the arrondissement.
     *
     * @param arrondissement the new arrondissement
     */
    public void setArrondissement( Arrondissement arrondissement )
    {
        _arrondissement = arrondissement;
    }

    /**
     * Sets the commentaire.
     *
     * @param commentaire the new commentaire
     */
    public void setCommentaire( String commentaire )
    {
        _strCommentaire = commentaire;
    }

    /**
     * Sets the commentaire programmation.
     *
     * @param commentaireProgrammation the new commentaire programmation
     */
    public void setCommentaireProgrammation( String commentaireProgrammation )
    {
        _strCommentaireProgrammation = commentaireProgrammation;
    }

    /**
     * Sets the date creation.
     *
     * @param dateCreation the new date creation
     */
    public void setDateCreation( String dateCreation )
    {
        _strDateCreation = dateCreation;
    }

    /**
     * Sets the date prevue traitement.
     *
     * @param datePrevueTraitement the new date prevue traitement
     */
    public void setDatePrevueTraitement( String datePrevueTraitement )
    {
        _strDatePrevueTraitement = datePrevueTraitement;
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
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId( Long id )
    {
        _id = id;
    }

    /**
     * Sets the mois.
     *
     * @param mois the new mois
     */
    public void setMois( String mois )
    {
        _strMois = mois;
    }

    /**
     * Sets the numero.
     *
     * @param numero the new numero
     */
    public void setNumero( int numero )
    {
        _nNumero = numero;
    }

    /**
     * Sets the photos.
     *
     * @param pPhotos the new photos
     */
    public void setPhotos( List<PhotoDMR> pPhotos )
    {
        _listPhotos = pPhotos;
    }

    /**
     * Sets the prefix.
     *
     * @param prefix the new prefix
     */
    public void setPrefix( String prefix )
    {
        _strPrefix = prefix;
    }

    /**
     * Sets the priorite.
     *
     * @param priorite the new priorite
     */
    public void setPriorite( Priorite priorite )
    {
        _priorite = priorite;
    }

    /**
     * Sets the secteur.
     *
     * @param secteur the new secteur
     */
    public void setSecteur( Sector secteur )
    {
        _secteur = secteur;
    }

    /**
     * Sets the signaleurs.
     *
     * @param pSignaleurs the new signaleurs
     */
    public void setSignaleurs( List<Signaleur> pSignaleurs )
    {
        _listSignaleurs = pSignaleurs;
    }

    /**
     * Sets the type signalement.
     *
     * @param typeSignalement the new type signalement
     */
    public void setTypeSignalement( TypeSignalement typeSignalement )
    {
        _typeSignalement = typeSignalement;
    }

    /**
     * Sets the unit.
     *
     * @param unit the new unit
     */
    public void setUnit( Unit unit )
    {
        _unit = unit;
    }

    /**
     * Sets the suivi.
     *
     * @param suivi the new suivi
     */
    public void setSuivi( int suivi )
    {
        _nSuivi = suivi;
    }

    /**
     * Sets the checks if is doublon.
     *
     * @param bIsDoublon the new checks if is doublon
     */
    public void setIsDoublon( boolean bIsDoublon )
    {
        _bIsDoublon = bIsDoublon;
    }

    /**
     * Checks if is doublon.
     *
     * @return true, if is doublon
     */
    public boolean isDoublon( )
    {
        return _bIsDoublon;
    }

    /**
     * Gets the token.
     *
     * @return the token
     */
    public String getToken( )
    {
        return _strToken;
    }

    /**
     * Sets the token.
     *
     * @param token the new token
     */
    public void setToken( String token )
    {
        _strToken = token;
    }

    /**
     * Sets the adresses form.
     *
     * @param index the index
     * @param adresse the adresse
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
     * Gets the adresses form.
     *
     * @param index the index
     * @return the adresses form
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
     * Gets the date service fait traitement.
     *
     * @return the date service fait traitement
     */
    public String getDateServiceFaitTraitement( )
    {
        return _strDateServiceFaitTraitement;
    }

    /**
     * Sets the date service fait traitement.
     *
     * @param strDateServiceFaitTraitement the new date service fait traitement
     */
    public void setDateServiceFaitTraitement( String strDateServiceFaitTraitement )
    {
        _strDateServiceFaitTraitement = strDateServiceFaitTraitement;
    }

    /**
     * Gets the heure service fait traitement.
     *
     * @return the heure service fait traitement
     */
    public String getHeureServiceFaitTraitement( )
    {
        return _strHeureServiceFaitTraitement;
    }

    /**
     * Sets the heure service fait traitement.
     *
     * @param strHeureServiceFaitTraitement the new heure service fait traitement
     */
    public void setHeureServiceFaitTraitement( String strHeureServiceFaitTraitement )
    {
        _strHeureServiceFaitTraitement = strHeureServiceFaitTraitement;
    }

    /**
     * Gets the felicitations.
     *
     * @return the felicitations
     */
    public int getFelicitations( )
    {
        return _nFelicitations;
    }

    /**
     * Sets the felicitations.
     *
     * @param nFelicitations the new felicitations
     */
    public void setFelicitations( int nFelicitations )
    {
        _nFelicitations = nFelicitations;
    }

    /**
     * Gets the observations rejet.
     *
     * @return the observations rejet
     */
    public List<ObservationRejet> getObservationsRejet( )
    {
        return _listObservationsRejet;
    }

    /**
     * Sets the observations rejet.
     *
     * @param observationsRejet the new observations rejet
     */
    public void setObservationsRejet( List<ObservationRejet> observationsRejet )
    {
        _listObservationsRejet = observationsRejet;
    }

    /**
     * Gets the signalement reference.
     *
     * @return the signalement reference
     */
    public String getSignalementReference( )
    {

        String prefix = getPrefix( );
        String annee = StringUtils.EMPTY + getAnnee( ) + StringUtils.EMPTY;
        String mois = getMois( );
        String numero = StringUtils.EMPTY + getNumero( ) + StringUtils.EMPTY;
        return  prefix + annee + mois + numero;

    }

    /**
     * Gets the date mise en surveillance.
     *
     * @return the date mise en surveillance
     */
    public String getDateMiseEnSurveillance( )
    {
        return _strDateMiseEnSurveillance;
    }

    /**
     * Sets the date mise en surveillance.
     *
     * @param dateMiseEnSurveillance the new date mise en surveillance
     */
    public void setDateMiseEnSurveillance( String dateMiseEnSurveillance )
    {
        _strDateMiseEnSurveillance = dateMiseEnSurveillance;
    }

    /**
     * Gets the date rejet.
     *
     * @return the date rejet
     */
    public String getDateRejet( )
    {
        return _strDateRejet;
    }

    /**
     * Sets the date rejet.
     *
     * @param dateRejet the new date rejet
     */
    public void setDateRejet( String dateRejet )
    {
        _strDateRejet = dateRejet;
    }

    /**
     * Gets the courriel destinataire.
     *
     * @return the courriel destinataire
     */
    public String getCourrielDestinataire( )
    {
        return _strCourrielDestinataire;
    }

    /**
     * Sets the courriel destinataire.
     *
     * @param courrielDestinataire the new courriel destinataire
     */
    public void setCourrielDestinataire( String courrielDestinataire )
    {
        _strCourrielDestinataire = courrielDestinataire;
    }

    /**
     * Gets the courriel expediteur.
     *
     * @return the courriel expediteur
     */
    public String getCourrielExpediteur( )
    {
        return _strCourrielExpediteur;
    }

    /**
     * Sets the courriel expediteur.
     *
     * @param courrielExpediteur the new courriel expediteur
     */
    public void setCourrielExpediteur( String courrielExpediteur )
    {
        _strCourrielExpediteur = courrielExpediteur;
    }

    /**
     * Gets the courriel date.
     *
     * @return the courriel date
     */
    public Timestamp getCourrielDate( )
    {
        return _courrielDate;
    }

    /**
     * Sets the courriel date.
     *
     * @param courrielDate the new courriel date
     */
    public void setCourrielDate( Timestamp courrielDate )
    {
        _courrielDate = courrielDate;
    }

    /**
     * Gets the checks if is send WS.
     *
     * @return the checks if is send WS
     */
    public boolean getIsSendWS( )
    {
        return _bIsSendWS;
    }

    /**
     * Sets the send ws.
     *
     * @param isSendWS the new send ws
     */
    public void setSendWs( boolean isSendWS )
    {
        _bIsSendWS = isSendWS;
    }

    /**
     * Gets the commentaire agent terrain.
     *
     * @return the commentaire agent terrain
     */
    public String getCommentaireAgentTerrain( )
    {
        return _strCommentaireAgentTerrain;
    }

    /**
     * Sets the commentaire agent terrain.
     *
     * @param strCommentaireAgentTerrain the new commentaire agent terrain
     */
    public void setCommentaireAgentTerrain( String strCommentaireAgentTerrain )
    {
        _strCommentaireAgentTerrain = strCommentaireAgentTerrain;
    }

}
