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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;

/**
 * The Class SignalementExportCSVDTO.
 */
public class SignalementExportCSVDTO
{

    /** The str priorite. */
    private String _strPriorite;

    /** The str type signalement. */
    private String _strTypeSignalement;

    /** The str alias. */
    private String _strAlias;

    /** The str alias mobile. */
    private String _strAliasMobile;

    /** The str direction. */
    private String _strDirection;

    /** The str quartier. */
    private String _strQuartier;

    /** The str adresse. */
    private String _strAdresse;

    /** The d coord X. */
    private Double _dCoordX;

    /** The d coord Y. */
    private Double _dCoordY;

    /** The str arrondissement. */
    private String _strArrondissement;

    /** The str secteur. */
    private String _strSecteur;

    /** The str date creation. */
    private String _strDateCreation;

    /** The str heure creation. */
    private String _strHeureCreation;

    /** The str etat. */
    private String _strEtat;

    /** The str mail usager. */
    private String _strMailUsager;

    /** The str commentaire usager. */
    private String _strCommentaireUsager;

    /** The str commentair agent terrain. */
    private String _strCommentairAgentTerrain;

    /** The n nb photos. */
    private Integer _nNbPhotos;

    /** The str date cloture. */
    private String _strDateCloture;

    /** The str date prevu traitement. */
    private String _strDatePrevuTraitement;

    /** The str raisons rejet. */
    private String _strRaisonsRejet;

    /** The str numero signalement. */
    private String _strNumeroSignalement;

    /** The n nb suivis. */
    private Integer _nNbSuivis;

    /** The n nb felicitations. */
    private Integer _nNbFelicitations;

    /** The b is photo service fait. */
    private boolean _bIsPhotoServiceFait;

    /** The n id mail service fait. */
    private Integer _nIdMailServiceFait;

    /** The str executeur service fait. */
    private String _strExecuteurServiceFait;

    /** The str date derniere action. */
    private String _strDateDerniereAction;

    /** The str mail destinataire courriel. */
    private String _strMailDestinataireCourriel;

    /** The str courriel expediteur. */
    private String _strCourrielExpediteur;

    /** The str date envoi courriel. */
    private String _strDateEnvoiCourriel;

    /** The str executeur rejet. */
    private String _strExecuteurRejet;

    /** The str executeur mise surveillance. */
    private String _strExecuteurMiseSurveillance;

    /** The n nb requalifications. */
    private Integer _nNbRequalifications;

    /** The str reg ex special caracter. */
    private String _strRegExSpecialCaracter = "[’]";

    private Integer _nIdSignalement;

    private Integer _nIdStatut;

    /** The str precision terrain */
    private String _strPrecisionTerrain;

    /** The photos. */
    private List<PhotoDMR> _listPhotos = new ArrayList<>( );

    /**
     * Gets the tab all datas.
     *
     * @return the tab all datas
     */
    public String [ ] getTabAllDatas( )
    {
        return new String [ ] {
                String.valueOf( getIdSignalement( ) ), getNumeroSignalement( ), getPriorite( ), getTypeSignalement( ), getAlias( ), getAliasMobile( ),
                getDirection( ), getQuartier( ), getAdresse( ), Double.toString( getCoordX( ) ), Double.toString( getCoordY( ) ), getArrondissement( ),
                getSecteur( ), getDateCreation( ), getHeureCreation( ), getEtat( ), getMailUsager( ), getCommentaireUsager( ),
                Integer.toString( getNbPhotos( ) ), getRaisonsRejet( ), Integer.toString( getNbSuivis( ) ), Integer.toString( getNbFelicitations( ) ),
                getDateCloture( ), isPhotoServiceFait( ) ? "Photo présente" : "Pas de photo", getMailDestinataireCourriel( ), getCourrielExpediteur( ),
                getDateEnvoiCourriel( ), getIdMailServiceFait( ) != 0 ? getIdMailServiceFait( ).toString( ) : "", getExecuteurServiceFait( ),
                getDateDerniereAction( ), getDatePrevuTraitement( ), getCommentairAgentTerrain( ), getExecuteurRejet( ), getExecuteurMiseSurveillance( ),
                getPrecisionTerrain(), getNbRequalifications( )
        };
    }

    /**
     * Gets the tab all datas with num pin.
     *
     * @param numPin
     *            the num pin
     * @return the tab all datas with num pin
     */
    public String [ ] getTabAllDatasWithNumPin( int numPin )
    {
        return (String [ ]) ArrayUtils.add( getTabAllDatas( ), 0, Integer.toString( numPin ) );
    }

    /**
     * Gets the priorite.
     *
     * @return the priorite
     */
    public String getPriorite( )
    {
        return _strPriorite;
    }

    /**
     * Sets the priorite.
     *
     * @param priorite
     *            the new priorite
     */
    public void setPriorite( String priorite )
    {
        _strPriorite = priorite;
    }

    /**
     * Gets the type signalement.
     *
     * @return the type signalement
     */
    public String getTypeSignalement( )
    {
        return _strTypeSignalement;
    }

    /**
     * Sets the type signalement.
     *
     * @param typeSignalement
     *            the new type signalement
     */
    public void setTypeSignalement( String typeSignalement )
    {
        _strTypeSignalement = typeSignalement;
    }

    /**
     * Gets the direction.
     *
     * @return the direction
     */
    public String getDirection( )
    {
        return _strDirection;
    }

    /**
     * Sets the direction.
     *
     * @param direction
     *            the new direction
     */
    public void setDirection( String direction )
    {
        _strDirection = direction;
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
     * Gets the etat.
     *
     * @return the etat
     */
    public String getEtat( )
    {
        return _strEtat;
    }

    /**
     * Sets the etat.
     *
     * @param etat
     *            the new etat
     */
    public void setEtat( String etat )
    {
        _strEtat = etat;
    }

    /**
     * Gets the numero signalement.
     *
     * @return the numero signalement
     */
    public String getNumeroSignalement( )
    {
        return _strNumeroSignalement;
    }

    /**
     * Sets the numero signalement.
     *
     * @param strNumeroSignalement
     *            the new numero signalement
     */
    public void setNumeroSignalement( String strNumeroSignalement )
    {
        _strNumeroSignalement = strNumeroSignalement;
    }

    /**
     * Gets the alias.
     *
     * @return the alias
     */
    public String getAlias( )
    {
        return _strAlias;
    }

    /**
     * Sets the alias.
     *
     * @param strAlias
     *            the new alias
     */
    public void setAlias( String strAlias )
    {
        _strAlias = strAlias;
    }

    /**
     * Gets the alias mobile.
     *
     * @return the alias mobile
     */
    public String getAliasMobile( )
    {
        return _strAliasMobile;
    }

    /**
     * Sets the alias mobile.
     *
     * @param strAliasMobile
     *            the new alias mobile
     */
    public void setAliasMobile( String strAliasMobile )
    {
        _strAliasMobile = strAliasMobile;
    }

    /**
     * Gets the coord X.
     *
     * @return the coord X
     */
    public Double getCoordX( )
    {
        return _dCoordX;
    }

    /**
     * Sets the coord X.
     *
     * @param dCoordX
     *            the new coord X
     */
    public void setCoordX( Double dCoordX )
    {
        _dCoordX = dCoordX;
    }

    /**
     * Gets the coord Y.
     *
     * @return the coord Y
     */
    public Double getCoordY( )
    {
        return _dCoordY;
    }

    /**
     * Sets the coord Y.
     *
     * @param dCoordY
     *            the new coord Y
     */
    public void setCoordY( Double dCoordY )
    {
        _dCoordY = dCoordY;
    }

    /**
     * Gets the arrondissement.
     *
     * @return the arrondissement
     */
    public String getArrondissement( )
    {
        return _strArrondissement;
    }

    /**
     * Sets the arrondissement.
     *
     * @param strArrondissement
     *            the new arrondissement
     */
    public void setArrondissement( String strArrondissement )
    {
        _strArrondissement = strArrondissement;
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
     * @param strHeureCreation
     *            the new heure creation
     */
    public void setHeureCreation( String strHeureCreation )
    {
        _strHeureCreation = strHeureCreation;
    }

    /**
     * Gets the mail usager.
     *
     * @return the mail usager
     */
    public String getMailUsager( )
    {
        return _strMailUsager;
    }

    /**
     * Sets the mailusager.
     *
     * @param strMailUsager
     *            the new mailusager
     */
    public void setMailusager( String strMailUsager )
    {
        _strMailUsager = strMailUsager;
    }

    /**
     * Gets the commentaire usager.
     *
     * @return the commentaire usager
     */
    public String getCommentaireUsager( )
    {
        return _strCommentaireUsager;
    }

    /**
     * Sets the commentaire usager.
     *
     * @param strCommentaireUsager
     *            the new commentaire usager
     */
    public void setCommentaireUsager( String strCommentaireUsager )
    {
        _strCommentaireUsager = strCommentaireUsager;
    }

    /**
     * Gets the commentair agent terrain.
     *
     * @return the commentair agent terrain
     */
    public String getCommentairAgentTerrain( )
    {
        return _strCommentairAgentTerrain;
    }

    /**
     * Sets the commentair agent terrain.
     *
     * @param strCommentairAgentTerrain
     *            the new commentair agent terrain
     */
    public void setCommentairAgentTerrain( String strCommentairAgentTerrain )
    {
        _strCommentairAgentTerrain = strCommentairAgentTerrain;
    }

    /**
     * Gets the nb photos.
     *
     * @return the nb photos
     */
    public Integer getNbPhotos( )
    {
        return _nNbPhotos;
    }

    /**
     * Sets the nb photos.
     *
     * @param nNbPhotos
     *            the new nb photos
     */
    public void setNbPhotos( Integer nNbPhotos )
    {
        _nNbPhotos = nNbPhotos;
    }

    /**
     * Gets the raisons rejet.
     *
     * @return the raisons rejet
     */
    public final String getRaisonsRejet( )
    {
        return _strRaisonsRejet.replaceAll( _strRegExSpecialCaracter, "'" );
    }

    /**
     * Sets the raisons rejet.
     *
     * @param raisonsRejet
     *            the new raisons rejet
     */
    public void setRaisonsRejet( String raisonsRejet )
    {
        _strRaisonsRejet = raisonsRejet;
    }

    /**
     * Gets the secteur.
     *
     * @return the secteur
     */
    public String getSecteur( )
    {
        return _strSecteur;
    }

    /**
     * Sets the secteur.
     *
     * @param strSecteur
     *            the new secteur
     */
    public void setSecteur( String strSecteur )
    {
        _strSecteur = strSecteur;
    }

    /**
     * Gets the nb suivis.
     *
     * @return the nb suivis
     */
    public Integer getNbSuivis( )
    {
        return _nNbSuivis;
    }

    /**
     * Sets the nb suivis.
     *
     * @param nNbSuivis
     *            the new nb suivis
     */
    public void setNbSuivis( Integer nNbSuivis )
    {
        _nNbSuivis = nNbSuivis;
    }

    /**
     * Gets the nb felicitations.
     *
     * @return the nb felicitations
     */
    public Integer getNbFelicitations( )
    {
        return _nNbFelicitations;
    }

    /**
     * Sets the nb felicitations.
     *
     * @param nNbFelicitations
     *            the new nb felicitations
     */
    public void setNbFelicitations( Integer nNbFelicitations )
    {
        _nNbFelicitations = nNbFelicitations;
    }

    /**
     * Gets the quartier.
     *
     * @return the quartier
     */
    public String getQuartier( )
    {
        return _strQuartier;
    }

    /**
     * Sets the quartier.
     *
     * @param strQuartier
     *            the new quartier
     */
    public void setQuartier( String strQuartier )
    {
        _strQuartier = strQuartier;
    }

    /**
     * Gets the date cloture.
     *
     * @return the date cloture
     */
    public String getDateCloture( )
    {
        return _strDateCloture;
    }

    /**
     * Sets the date cloture.
     *
     * @param strDateCloture
     *            the new date cloture
     */
    public void setDateCloture( String strDateCloture )
    {
        _strDateCloture = strDateCloture;
    }

    /**
     * Gets the date prevu traitement.
     *
     * @return the date prevu traitement
     */
    public String getDatePrevuTraitement( )
    {
        return _strDatePrevuTraitement;
    }

    /**
     * Sets the date prevu traitement.
     *
     * @param strDatePrevuTraitement
     *            the new date prevu traitement
     */
    public void setDatePrevuTraitement( String strDatePrevuTraitement )
    {
        _strDatePrevuTraitement = strDatePrevuTraitement;
    }

    /**
     * Checks if is photo service fait.
     *
     * @return true, if is photo service fait
     */
    public boolean isPhotoServiceFait( )
    {
        return _bIsPhotoServiceFait;
    }

    /**
     * Sets the photo service fait.
     *
     * @param isPhotoServiceFait
     *            the new photo service fait
     */
    public void setPhotoServiceFait( boolean isPhotoServiceFait )
    {
        _bIsPhotoServiceFait = isPhotoServiceFait;
    }

    /**
     * Gets the id mail service fait.
     *
     * @return the id mail service fait
     */
    public Integer getIdMailServiceFait( )
    {
        return _nIdMailServiceFait;
    }

    /**
     * Sets the id mail service fait.
     *
     * @param idMailServiceFait
     *            the new id mail service fait
     */
    public void setIdMailServiceFait( Integer idMailServiceFait )
    {
        _nIdMailServiceFait = idMailServiceFait;
    }

    /**
     * Gets the executeur service fait.
     *
     * @return the executeur service fait
     */
    public String getExecuteurServiceFait( )
    {
        return _strExecuteurServiceFait;
    }

    /**
     * Sets the executeur service fait.
     *
     * @param executeurServiceFait
     *            the new executeur service fait
     */
    public void setExecuteurServiceFait( String executeurServiceFait )
    {
        _strExecuteurServiceFait = executeurServiceFait;
    }

    /**
     * Gets the date derniere action.
     *
     * @return the date derniere action
     */
    public String getDateDerniereAction( )
    {
        return _strDateDerniereAction;
    }

    /**
     * Sets the date derniere action.
     *
     * @param dateDerniereAction
     *            the new date derniere action
     */
    public void setDateDerniereAction( String dateDerniereAction )
    {
        _strDateDerniereAction = dateDerniereAction;
    }

    /**
     * Gets the mail destinataire courriel.
     *
     * @return the _strMailDestinataireCourriel
     */
    public String getMailDestinataireCourriel( )
    {
        return _strMailDestinataireCourriel;
    }

    /**
     * Sets the mail destinataire courriel.
     *
     * @param strMailDestinataireCourriel
     *            the new mail destinataire courriel
     */
    public void setMailDestinataireCourriel( String strMailDestinataireCourriel )
    {
        _strMailDestinataireCourriel = strMailDestinataireCourriel;
    }

    /**
     * Gets the courriel expediteur.
     *
     * @return the _strCourrielExpediteur
     */
    public String getCourrielExpediteur( )
    {
        return _strCourrielExpediteur;
    }

    /**
     * Sets the courriel expediteur.
     *
     * @param strCourrielExpediteur
     *            the new courriel expediteur
     */
    public void setCourrielExpediteur( String strCourrielExpediteur )
    {
        _strCourrielExpediteur = strCourrielExpediteur;
    }

    /**
     * Gets the date envoi courriel.
     *
     * @return the _strDateEnvoiCourriel
     */
    public String getDateEnvoiCourriel( )
    {
        return _strDateEnvoiCourriel;
    }

    /**
     * Sets the date envoi courriel.
     *
     * @param strDateEnvoiCourriel
     *            the new date envoi courriel
     */
    public void setDateEnvoiCourriel( String strDateEnvoiCourriel )
    {
        _strDateEnvoiCourriel = strDateEnvoiCourriel;
    }

    /**
     * Gets the executeur rejet.
     *
     * @return the executeur rejet
     */
    public String getExecuteurRejet( )
    {
        return _strExecuteurRejet;
    }

    /**
     * Sets the executeur rejet.
     *
     * @param executeurRejet
     *            the new executeur rejet
     */
    public void setExecuteurRejet( String executeurRejet )
    {
        _strExecuteurRejet = executeurRejet;
    }

    /**
     * Gets the executeur mise surveillance.
     *
     * @return the executeur mise surveillance
     */
    public String getExecuteurMiseSurveillance( )
    {
        return _strExecuteurMiseSurveillance;
    }

    /**
     * Sets the executeur mise surveillance.
     *
     * @param executeurMiseSurveillance
     *            the new executeur mise surveillance
     */
    public void setExecuteurMiseSurveillance( String executeurMiseSurveillance )
    {
        _strExecuteurMiseSurveillance = executeurMiseSurveillance;
    }

    /**
     * Get number requalification.
     * 
     * @return String value nbRequalification
     */
    public String getNbRequalifications( )
    {
        return _nNbRequalifications != null ? Integer.toString( _nNbRequalifications ) : StringUtils.EMPTY;
    }

    /**
     * Set requalification number.
     * 
     * @param nbRequalifications
     *            nombre de requalification
     */
    public void setNbRequalifications( Integer nbRequalifications )
    {
        _nNbRequalifications = nbRequalifications;
    }

    /**
     * Gets the id signalement.
     *
     * @return the id signalement
     */
    public Integer getIdSignalement( )
    {
        return _nIdSignalement;
    }

    /**
     * Sets the id signalement.
     *
     * @param nIdSignalement
     *            the new id signalement
     */
    public void setIdSignalement( Integer nIdSignalement )
    {
        _nIdSignalement = nIdSignalement;
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
     * Sets the photos.
     *
     * @param pPhotos
     *            the new photos
     */
    public void setPhotos( List<PhotoDMR> pPhotos )
    {
        _listPhotos = pPhotos;
    }

    public Integer getIdStatut( )
    {
        return _nIdStatut;
    }

    public void setIdStatut( Integer nIdStatut )
    {
        this._nIdStatut = nIdStatut;
    }

    /**
     * Gets the precision terrain.
     *
     * @return the precision terrain
     */
    public String getPrecisionTerrain( )
    {
        return _strPrecisionTerrain;
    }

    /**
     * Sets the precision terrain.
     *
     * @param precision terrain
     *            the new precision terrain
     */
    public void setPrecisionTerrain( String precisionTerrain )
    {
        _strPrecisionTerrain = precisionTerrain;
    }

}
