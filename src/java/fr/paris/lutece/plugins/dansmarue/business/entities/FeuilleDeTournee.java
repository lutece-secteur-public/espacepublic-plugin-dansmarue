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
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * The Class FeuilleDeTournee.
 */
public class FeuilleDeTournee implements Serializable
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2400491434593259964L;

    /** The n id. */
    private Integer _nId;

    /** The str createur. */
    private String _strCreateur;

    /** The n unit id. */
    private Integer _nUnitId;

    /** The str date creation. */
    private String _strDateCreation;

    /** The str date modification. */
    private String _strDateModification;

    /** The str commentaire. */
    private String _strCommentaire;

    /** The n filtre fdt id. */
    private Integer _nFiltreFdtId;

    /** The list signalement ids. */
    private List<Integer> _listSignalementIds;

    /** The list signalement. */
    private List<Signalement> _listSignalement;

    /** The list signalement bean. */
    private List<SignalementBean> _listSignalementBean;

    private String _strNom;

    /** base64 string represent map */
    private String _strCarteBase64;

    /** display map in jasper pdf */
    private String _strDisplayMap;

    @NotNull( message = "Le champ direction est obligatoire." )
    private Integer _nIdDirection;

    private Integer _nIdEntite;

    private String _strDirectionLibelle;

    private String _strEntiteLibelle;


    /** The str info avant tournee. */
    private String _strInfoAvantTournee;

    /** The str info apres tournee. */
    private String _strInfoApresTournee;

    /**
     * Instantiates a new feuille de tournee.
     */
    public FeuilleDeTournee( )
    {
        _nIdDirection = -1;
    }

    /**
     * Instantiates a new feuille de tournee.
     *
     * @param strCreateur
     *            the str createur
     * @param nUnitId
     *            the n unit id
     * @param strCommentaire
     *            the str commentaire
     * @param nFiltreFdtId
     *            the n filtre fdt id
     * @param listSignalementIds
     *            the list signalement ids
     * @param strNom
     *            the str nom
     * @param nIdDirection
     *            the n id direction
     * @param nIdEntite
     *            the n id entite
     */
    public FeuilleDeTournee( String strCreateur, Integer nUnitId, String strCommentaire, Integer nFiltreFdtId, List<Integer> listSignalementIds, String strNom,
            Integer nIdDirection, Integer nIdEntite, String strInfoAvantTournee )
    {
        super( );
        _strCreateur = strCreateur;
        _nUnitId = nUnitId;
        _strCommentaire = strCommentaire;
        _nFiltreFdtId = nFiltreFdtId;
        _listSignalementIds = listSignalementIds;
        _strNom = strNom;
        _nIdDirection = nIdDirection;
        _nIdEntite = nIdEntite;
        _strInfoAvantTournee = strInfoAvantTournee;
    }

    /**
     * Instantiates a new feuille de tournee.
     *
     * @param nId
     *            the n id
     * @param strCreateur
     *            the str createur
     * @param nUnitId
     *            the n unit id
     * @param strDateCreation
     *            the str date creation
     * @param strDateModification
     *            the str date modification
     * @param strCommentaire
     *            the str commentaire
     * @param nFiltreFdtId
     *            the n filtre fdt id
     * @param listSignalementIds
     *            the list signalement ids
     * @param listSignalement
     *            the list signalement
     * @param listSignalementBean
     *            the list signalement bean
     */
    public FeuilleDeTournee( Integer nId, String strCreateur, Integer nUnitId, String strDateCreation, String strDateModification, String strCommentaire,
            Integer nFiltreFdtId, List<Integer> listSignalementIds, List<Signalement> listSignalement, List<SignalementBean> listSignalementBean )
    {
        super( );
        _nId = nId;
        _strCreateur = strCreateur;
        _nUnitId = nUnitId;
        _strDateCreation = strDateCreation;
        _strDateModification = strDateModification;
        _strCommentaire = strCommentaire;
        _nFiltreFdtId = nFiltreFdtId;
        _listSignalementIds = listSignalementIds;
        _listSignalement = listSignalement;
        _listSignalementBean = listSignalementBean;
        _nIdDirection = -1;
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
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId( Integer id )
    {
        _nId = id;
    }

    /**
     * Gets the createur.
     *
     * @return the createur
     */
    public String getCreateur( )
    {
        return _strCreateur;
    }

    /**
     * Sets the createur.
     *
     * @param createur
     *            the new createur
     */
    public void setCreateur( String createur )
    {
        _strCreateur = createur;
    }

    /**
     * Gets the unit id.
     *
     * @return the unit id
     */
    public Integer getUnitId( )
    {
        return _nUnitId;
    }

    /**
     * Sets the unit id.
     *
     * @param unitId
     *            the new unit id
     */
    public void setUnitId( Integer unitId )
    {
        _nUnitId = unitId;
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
     * @param strDateCreation
     *            the new date creation
     */
    public void setDateCreation( String strDateCreation )
    {
        _strDateCreation = strDateCreation;
    }

    /**
     * Gets the date modification.
     *
     * @return the date modification
     */
    public String getDateModification( )
    {
        return _strDateModification;
    }

    /**
     * Sets the date modification.
     *
     * @param strDateModification
     *            the new date modification
     */
    public void setDateModification( String strDateModification )
    {
        _strDateModification = strDateModification;
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
     * @param strCommentaire
     *            the new commentaire
     */
    public void setCommentaire( String strCommentaire )
    {
        _strCommentaire = strCommentaire;
    }

    /**
     * Gets the filtre fdt id.
     *
     * @return the filtre fdt id
     */
    public Integer getFiltreFdtId( )
    {
        return _nFiltreFdtId;
    }

    /**
     * Sets the filtre fdt id.
     *
     * @param filtreFdtId
     *            the new filtre fdt id
     */
    public void setFiltreFdtId( Integer filtreFdtId )
    {
        _nFiltreFdtId = filtreFdtId;
    }

    /**
     * Gets the list signalement ids.
     *
     * @return the list signalement ids
     */
    public List<Integer> getListSignalementIds( )
    {
        return _listSignalementIds;
    }

    /**
     * Sets the list signalement ids.
     *
     * @param listSignalementIds
     *            the new list signalement ids
     */
    public void setListSignalementIds( List<Integer> listSignalementIds )
    {
        _listSignalementIds = listSignalementIds;
    }

    /**
     * Gets the list signalement.
     *
     * @return the list signalement
     */
    public List<Signalement> getListSignalement( )
    {
        return _listSignalement;
    }

    /**
     * Sets the list signalement.
     *
     * @param listSignalement
     *            the new list signalement
     */
    public void setListSignalement( List<Signalement> listSignalement )
    {
        _listSignalement = listSignalement;
    }

    /**
     * Gets the list signalement bean.
     *
     * @return the list signalement bean
     */
    public List<SignalementBean> getListSignalementBean( )
    {
        return _listSignalementBean;
    }

    /**
     * Sets the list signalement bean.
     *
     * @param listSignalementBean
     *            the new list signalement bean
     */
    public void setListSignalementBean( List<SignalementBean> listSignalementBean )
    {
        _listSignalementBean = listSignalementBean;
    }

    public String getNom( )
    {
        return _strNom;
    }

    public void setNom( String nom )
    {
        _strNom = nom;
    }

    public String getCarteBase64( )
    {
        return _strCarteBase64;
    }

    public void setCarteBase64( String carteBase64 )
    {
        _strCarteBase64 = carteBase64;
    }

    public String getDisplayMap( )
    {
        return _strDisplayMap;
    }

    public void setDisplayMap( String displayMap )
    {
        _strDisplayMap = displayMap;
    }

    public Integer getIdDirection( )
    {
        return _nIdDirection;
    }

    public void setIdDirection( Integer idDirection )
    {
        _nIdDirection = idDirection;
    }

    public Integer getIdEntite( )
    {
        return _nIdEntite;
    }

    public void setIdEntite( Integer idEntite )
    {
        _nIdEntite = idEntite;
    }

    public String getDirectionLibelle( )
    {
        return _strDirectionLibelle;
    }

    public void setDirectionLibelle( String directionLibelle )
    {
        _strDirectionLibelle = directionLibelle;
    }

    public String getEntiteLibelle( )
    {
        return _strEntiteLibelle;
    }

    public void setEntiteLibelle( String entiteLibelle )
    {
        _strEntiteLibelle = entiteLibelle;
    }

    public String getInfoAvantTournee( )
    {
        return _strInfoAvantTournee;
    }

    public void setInfoAvantTournee( String strInfoAvantTournee )
    {
        _strInfoAvantTournee = strInfoAvantTournee;
    }

    public String getInfoApresTournee( )
    {
        return _strInfoApresTournee;
    }

    public void setInfoApresTournee( String strInfoApresTournee )
    {
        _strInfoApresTournee = strInfoApresTournee;
    }

}
