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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.commons.Order;

/**
 * The Class SignalementFilter.
 */
public class SignalementFilter implements Serializable
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The list id type signalements. */
    private List<Integer> _listIdTypeSignalements;

    /** The n id domaine. */
    private int _nIdDomaine;

    /** The n id type signalement. */
    private int _nIdTypeSignalement;

    /** The str numero. */
    private String _strNumero;

    /** The n id direction. */
    private List<Integer> _listIdDirection;

    /** The list id arrondissements. */
    private List<Integer> _listIdArrondissements;

    /** The n id arrondissement. */
    private int _nIdArrondissement;

    /** The n id sector. */
    private int _nIdSector;

    /** The str adresse. */
    private String _strAdresse;

    /** The str mail. */
    private String _strMail;

    /** The str commentaire. */
    private String _strCommentaire;

    /** The str commentaire agent terrain. */
    private String _strCommentaireAgentTerrain;

    /** The str date begin. */
    private String _strDateBegin;

    /** The str date end. */
    private String _strDateEnd;

    /** The str date service done begin. */
    private String _strDateDoneBegin;

    /** The str date service done end. */
    private String _strDateDoneEnd;

    /** The str date requalification begin. */
    private String _strDateRequalificationBegin;

    /** The str date requalification end. */
    private String _strDateRequalificationEnd;

    /** The str date programmation begin. */
    private String _strDateProgrammingBegin;

    /** The str date programmation end. */
    private String _strDateProgrammingEnd;

    /** The n id etat. */
    private int _nIdEtat;

    /** The list etats. */
    private List<EtatSignalement> _listEtats;

    /** The list priorites. */
    private List<Integer> _listIdPriorites;

    /** The list id secteur autorises. */
    private List<Integer> _listIdSecteurAutorises;

    /** The list id unit. */
    private List<Integer> _listIdUnit;

    /** The list id categories. */
    private List<Integer> _listIdCategories;

    /** The list id quartier. */
    private List<Integer> _listIdQuartier;

    /** The photo id. */
    private int _nIdVuePhoto;

    /**
     * Mail dernier interveant sur l'anomalie.
     */
    private String _strMailDernierIntervenant;

    /** The b order asc. */
    private boolean _bOrderAsc;

    /** The list orders. */
    private List<Order> _listOrders = new ArrayList<>( );

    private int _nIdFdt;

    private String _strOnglet;

    /** The b filter by photo service done only */
    private boolean _bPhotoDoneOnly;

    /**
     * Init empty list for report filter to avoid Null Pointer Exception.
     */
    public void initEmptyList( )
    {
        _listIdTypeSignalements = Collections.emptyList( );
        _listIdArrondissements = Collections.emptyList( );
        _listEtats = Collections.emptyList( );
        _listIdSecteurAutorises = Collections.emptyList( );
        _listIdUnit = Collections.emptyList( );
        _listIdCategories = Collections.emptyList( );
        _listIdQuartier = Collections.emptyList( );
        _listOrders = Collections.emptyList( );
        _listIdPriorites = Collections.emptyList( );
    }

    /**
     * Gets the etats.
     *
     * @return the etats
     */
    public List<EtatSignalement> getEtats( )
    {
        return _listEtats;
    }

    /**
     * Sets the etats.
     *
     * @param etats
     *            the new etats
     */
    public void setEtats( List<EtatSignalement> etats )
    {
        _listEtats = etats;
    }

    /**
     * Gets priorites.
     *
     * @return the priorite
     */
    public List<Integer> getPriorites( )
    {
        return _listIdPriorites;
    }

    /**
     * Sets the priorites.
     *
     * @param priorites
     *            the priorites
     */
    public void setPriorites( List<Integer> priorites )
    {
        _listIdPriorites = priorites;
    }

    /**
     * Gets the list id secteur autorises.
     *
     * @return the list id secteur autorises
     */
    public List<Integer> getListIdSecteurAutorises( )
    {
        return _listIdSecteurAutorises;
    }

    /**
     * Sets the list id secteur autorises.
     *
     * @param listIdSecteurAutorises
     *            the new list id secteur autorises
     */
    public void setListIdSecteurAutorises( List<Integer> listIdSecteurAutorises )
    {
        _listIdSecteurAutorises = listIdSecteurAutorises;
    }

    /**
     * get Set the order to ascending or descending.
     *
     * @param bOrderAsc
     *            the order (true if ascending)
     */
    public void setOrderAsc( boolean bOrderAsc )
    {
        _bOrderAsc = bOrderAsc;
    }

    /**
     * Get the order column name.
     *
     * @return the column name
     */
    public List<Order> getOrders( )
    {
        return _listOrders;
    }

    /**
     * Set the column to order.
     *
     * @param orders
     *            the column name
     */
    public void setOrders( List<Order> orders )
    {
        _listOrders = orders;
    }

    /**
     * Gets the id type signalement.
     *
     * @return the id type signalement
     */
    public int getIdTypeSignalement( )
    {
        return _nIdTypeSignalement;
    }

    /**
     * Sets the id type signalement.
     *
     * @param nIdTypeSignalement
     *            the new id type signalement
     */
    public void setIdTypeSignalement( int nIdTypeSignalement )
    {
        _nIdTypeSignalement = nIdTypeSignalement;
    }

    /**
     * Gets the list id type signalements.
     *
     * @return the idTypeSignalement
     */
    public List<Integer> getListIdTypeSignalements( )
    {
        return _listIdTypeSignalements;
    }

    /**
     * Sets the list id type signalements.
     *
     * @param listIdTypeSignalements
     *            the list of reporting type ids to set
     */
    public void setListIdTypeSignalements( List<Integer> listIdTypeSignalements )
    {
        _listIdTypeSignalements = listIdTypeSignalements;
    }

    /**
     * Gets the id direction.
     *
     * @return the idDirection
     */
    public List<Integer> getListIdDirection( )
    {
        return _listIdDirection;
    }

    /**
     * Sets the id direction.
     *
     * @param listIdDirection
     *            the idDirection to set
     */
    public void setListIdDirection( List<Integer> listIdDirection )
    {
        _listIdDirection = listIdDirection;
    }

    /**
     * Gets the adresse.
     *
     * @return the address
     */
    public String getAdresse( )
    {
        return _strAdresse;
    }

    /**
     * Sets the adresse.
     *
     * @param pAdresse
     *            the address to set
     */
    public void setAdresse( String pAdresse )
    {
        _strAdresse = pAdresse;
    }

    /**
     * Gets the mail.
     *
     * @return the mail
     */
    public String getMail( )
    {
        return _strMail;
    }

    /**
     * Sets the mail.
     *
     * @param pMail
     *            the mail to set
     */
    public void setMail( String pMail )
    {
        _strMail = pMail;
    }

    /**
     * Gets the commentaire.
     *
     * @return the commentary
     */
    public String getCommentaire( )
    {
        return _strCommentaire;
    }

    /**
     * Sets the commentaire.
     *
     * @param pCommentaire
     *            the commentary to set
     */
    public void setCommentaire( String pCommentaire )
    {
        _strCommentaire = pCommentaire;
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
     * @param pCommentaireAgentTerrain
     *            the new commentaire agent terrain
     */
    public void setCommentaireAgentTerrain( String pCommentaireAgentTerrain )
    {
        _strCommentaireAgentTerrain = pCommentaireAgentTerrain;
    }

    /**
     * Gets the date begin.
     *
     * @return the DateBegin
     */
    public String getDateBegin( )
    {
        return _strDateBegin;
    }

    /**
     * Sets the date begin.
     *
     * @param pDateBegin
     *            the DateBegin to set
     */
    public void setDateBegin( String pDateBegin )
    {
        _strDateBegin = pDateBegin;
    }

    /**
     * Gets the date done begin.
     *
     * @return the DateDoneBegin
     */
    public String getDateDoneBegin( )
    {
        return _strDateDoneBegin;
    }

    /**
     * Sets the date done begin.
     *
     * @param pDateDoneBegin
     *            the DateDoneBegin to set
     */
    public void setDateDoneBegin( String pDateDoneBegin )
    {
        _strDateDoneBegin = pDateDoneBegin;
    }

    /**
     * Gets the date requalification begin.
     *
     * @return the DateRequalificationBegin
     */
    public String getDateRequalificationBegin( )
    {
        return _strDateRequalificationBegin;
    }

    /**
     * Sets the date requalification begin.
     *
     * @param pDateRequalificationBegin
     *            the DateDoneBegin to set
     */
    public void setDateRequalificationBegin( String pDateRequalificationBegin )
    {
        _strDateRequalificationBegin = pDateRequalificationBegin;
    }

    /**
     * Gets the date programmation begin.
     *
     * @return the DateRequalificationBegin
     */
    public String getDateProgrammationBegin( )
    {
        return _strDateProgrammingBegin;
    }

    /**
     * Sets the date programmation begin.
     *
     * @param pDateProgrammationBegin
     *            the pDateProgrammationBegin to set
     */
    public void setDateProgrammationBegin( String pDateProgrammationBegin )
    {
        _strDateProgrammingBegin = pDateProgrammationBegin;
    }

    /**
     * Gets the date end.
     *
     * @return the DateEnd
     */
    public String getDateEnd( )
    {
        return _strDateEnd;
    }

    /**
     * Sets the date end.
     *
     * @param pDateEnd
     *            the DateEnd to set
     */
    public void setDateEnd( String pDateEnd )
    {
        _strDateEnd = pDateEnd;
    }

    /**
     * Gets the date service done end.
     *
     * @return the DateDoneEnd
     */
    public String getDateDoneEnd( )
    {
        return _strDateDoneEnd;
    }

    /**
     * Sets the date done end.
     *
     * @param pDateDoneEnd
     *            the DateDoneEnd to set
     */
    public void setDateDoneEnd( String pDateDoneEnd )
    {
        _strDateDoneEnd = pDateDoneEnd;
    }

    /**
     * Gets the date requalification end.
     *
     * @return the DateRequalificationEnd
     */
    public String getDateRequalificationEnd( )
    {
        return _strDateRequalificationEnd;
    }

    /**
     * Sets the date requalification end.
     *
     * @param pDateRequalificationEnd
     *            the pDateRequalificationEnd to set
     */
    public void setDateRequalificationEnd( String pDateRequalificationEnd )
    {
        _strDateRequalificationEnd = pDateRequalificationEnd;
    }

    /**
     * Gets the date programmation end.
     *
     * @return the DateProgrammationEnd
     */
    public String getDateProgrammationEnd( )
    {
        return _strDateProgrammingEnd;
    }

    /**
     * Sets the date programmation end.
     *
     * @param pDateProgrammationEnd
     *            the pDateProgrammationEnd to set
     */
    public void setDateProgrammationEnd( String pDateProgrammationEnd )
    {
        _strDateProgrammingEnd = pDateProgrammationEnd;
    }

    /**
     * Checks if is order asc.
     *
     * @return the _bOrderAsc
     */
    public boolean isOrderAsc( )
    {
        return _bOrderAsc;
    }

    /**
     * Gets the numero.
     *
     * @return the number
     */
    public String getNumero( )
    {
        return _strNumero;
    }

    /**
     * Sets the numero.
     *
     * @param pNumero
     *            the number to set
     */
    public void setNumero( String pNumero )
    {
        _strNumero = pNumero;
    }

    /**
     * Gets the id etat.
     *
     * @return the idEtat
     */
    public int getIdEtat( )
    {
        return _nIdEtat;
    }

    /**
     * Sets the id etat.
     *
     * @param pIdEtat
     *            the idEtat to set
     */
    public void setIdEtat( int pIdEtat )
    {
        _nIdEtat = pIdEtat;
    }

    /**
     * Gets the id arrondissement.
     *
     * @return the id arrondissement
     */
    public int getIdArrondissement( )
    {
        return _nIdArrondissement;
    }

    /**
     * Sets the id arrondissement.
     *
     * @param nIdArrondissement
     *            the new id arrondissement
     */
    public void setIdArrondissement( int nIdArrondissement )
    {
        _nIdArrondissement = nIdArrondissement;
    }

    /**
     * Gets the list id arrondissements.
     *
     * @return the list id arrondissements
     */
    public List<Integer> getListIdArrondissements( )
    {
        return _listIdArrondissements;
    }

    /**
     * Sets the list id arrondissements.
     *
     * @param listIdArrondissements
     *            the new list id arrondissements
     */
    public void setListIdArrondissements( List<Integer> listIdArrondissements )
    {
        _listIdArrondissements = listIdArrondissements;
    }

    /**
     * Gets the id sector.
     *
     * @return the id sector
     */
    public int getIdSector( )
    {
        return _nIdSector;
    }

    /**
     * Sets the id sector.
     *
     * @param idSector
     *            the new id sector
     */
    public void setIdSector( int idSector )
    {
        _nIdSector = idSector;
    }

    /**
     * Gets the list id categories.
     *
     * @return the list id categories
     */
    public List<Integer> getListIdCategories( )
    {
        return _listIdCategories;
    }

    /**
     * Sets the list id categories.
     *
     * @param listIdCategories
     *            the new list id categories
     */
    public void setListIdCategories( List<Integer> listIdCategories )
    {
        _listIdCategories = listIdCategories;
    }

    /**
     * Sets the id domaine.
     *
     * @param nIdDomaine
     *            the new id domaine
     */
    public void setIdDomaine( int nIdDomaine )
    {
        _nIdDomaine = nIdDomaine;
    }

    /**
     * Gets the id domaine.
     *
     * @return the id domaine
     */
    public int getIdDomaine( )
    {
        return _nIdDomaine;
    }

    /**
     * Gets the list id quartier.
     *
     * @return the list id quartier
     */
    public List<Integer> getListIdQuartier( )
    {
        return _listIdQuartier;
    }

    /**
     * Sets the list id quartier.
     *
     * @param listIdQuartier
     *            the new list id quartier
     */
    public void setListIdQuartier( List<Integer> listIdQuartier )
    {
        _listIdQuartier = listIdQuartier;
    }

    /**
     * Gets the id photo.
     *
     * @return the id photo
     */
    public int getIdVuePhoto( )
    {
        return _nIdVuePhoto;
    }

    /**
     * Sets the id photo.
     *
     * @param idVuePhoto
     *            the new id vue photo
     */
    public void setIdVuePhoto( int idVuePhoto )
    {
        _nIdVuePhoto = idVuePhoto;
    }

    /**
     * Gets the list id unit.
     *
     * @return the list id unit
     */
    public List<Integer> getListIdUnit( )
    {
        return _listIdUnit;
    }

    /**
     * Sets the list id unit.
     *
     * @param listIdUnit
     *            the new list id unit
     */
    public void setListIdUnit( List<Integer> listIdUnit )
    {
        _listIdUnit = listIdUnit;
    }

    /**
     * Gets the mail dernier intervenant.
     *
     * @return mail dernier intervenant
     */
    public String getMailDernierIntervenant( )
    {
        return _strMailDernierIntervenant;
    }

    /**
     * Set mail dernier intervenant.
     *
     * @param mailDernierIntervenant
     *            the new mail dernier intervenant
     */
    public void setMailDernierIntervenant( String mailDernierIntervenant )
    {
        _strMailDernierIntervenant = mailDernierIntervenant;
    }

    public int getIdFdt( )
    {
        return _nIdFdt;
    }

    public void setIdFdt( int nIdFdt )
    {
        _nIdFdt = nIdFdt;
    }

    public String getOnglet( )
    {
        return _strOnglet;
    }

    public void setOnglet( String onglet )
    {
        _strOnglet = onglet;
    }

    public boolean isPhotoDoneOnly( )
    {
        return _bPhotoDoneOnly;
    }

    public void setPhotoDoneOnly( boolean photoDoneOnly )
    {
        _bPhotoDoneOnly = photoDoneOnly;
    }

}
