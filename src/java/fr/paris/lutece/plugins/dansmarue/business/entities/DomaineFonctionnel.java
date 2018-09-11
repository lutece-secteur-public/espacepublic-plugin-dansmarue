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

import java.util.List;

import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotBlank;

import fr.paris.lutece.portal.service.rbac.RBACResource;
import fr.paris.lutece.util.ReferenceList;

public class DomaineFonctionnel implements RBACResource
{

    /** The Constant RESOURCE_TYPE. */
    public static final String RESOURCE_TYPE = "SIGNALEMENT_DOMAINE_FONCTIONNEL";

    /** The id. */
    private Integer            _nId;

    /** The libelle. */
    @NotBlank
    private String             _strLibelle;

    /** The active boolean. */
    private boolean            _bActif;

    /** The districts ids. */
    private List<Integer>      _listArrondissementsIds;

    /** The neighborhoods ids. */
    private List<Integer>      _listQuartiersIds;

    /** The report types ids. */
    private List<Integer>      _listTypesSignalementIds;

    /** The boards ref list. */
    @Transient
    private ReferenceList      _directionsRefList;

    /** The districts ref list. */
    @Transient
    private ReferenceList      _arrondissementsRefList;

    /** The neighborhoods ref list. */
    @Transient
    private ReferenceList      _quartierRefList;

    /** The sectors ref list. */
    @Transient
    private ReferenceList      _secteursRefList;

    /** The categories ref list. */
    @Transient
    private ReferenceList      _categoriesRefList;

    /** The anomaly types ref list. */
    @Transient
    private ReferenceList      _typesAnomalieRefList;

    /** ENTITIES from unittree. */
    private List<Integer>      _unitIds;

    /**
     * Getter for the id.
     *
     * @return the id
     */
    public Integer getId( )
    {
        return _nId;
    }

    /**
     * Setter for the id.
     *
     * @param id
     *            the id to set
     */
    public void setId( Integer id )
    {
        this._nId = id;
    }

    /**
     * Getter for the libelle.
     *
     * @return the libelle
     */
    public String getLibelle( )
    {
        return _strLibelle;
    }

    /**
     * Setter for the libelle.
     *
     * @param libelle
     *            the libelle to set
     */
    public void setLibelle( String libelle )
    {
        this._strLibelle = libelle;
    }

    /**
     * Getter for the actif.
     *
     * @return the actif
     */
    public boolean isActif( )
    {
        return _bActif;
    }

    /**
     * Setter for the actif.
     *
     * @param actif
     *            the actif to set
     */
    public void setActif( boolean actif )
    {
        this._bActif = actif;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.portal.service.rbac.RBACResource#getResourceTypeCode()
     */
    @Override
    public String getResourceTypeCode( )
    {
        return RESOURCE_TYPE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.portal.service.rbac.RBACResource#getResourceId()
     */
    @Override
    public String getResourceId( )
    {
        return Long.toString( getId( ) );
    }

    /**
     * Getter for the arrondissementsIds.
     *
     * @return the arrondissementsIds
     */
    public List<Integer> getArrondissementsIds( )
    {
        return _listArrondissementsIds;
    }

    /**
     * Setter for the arrondissementsIds.
     *
     * @param arrondissementsIds
     *            the arrondissementsIds to set
     */
    public void setArrondissementsIds( List<Integer> arrondissementsIds )
    {
        this._listArrondissementsIds = arrondissementsIds;
    }

    /**
     * Getter for the typesSignalementIds.
     *
     * @return the typesSignalementIds
     */
    public List<Integer> getTypesSignalementIds( )
    {
        return _listTypesSignalementIds;
    }

    /**
     * Setter for the typesSignalementIds.
     *
     * @param typesSignalementIds
     *            the typesSignalementIds to set
     */
    public void setTypesSignalementIds( List<Integer> typesSignalementIds )
    {
        this._listTypesSignalementIds = typesSignalementIds;
    }

    /**
     * Getter for the unitIds.
     *
     * @return the unitIds
     */
    public List<Integer> getUnitIds( )
    {
        return _unitIds;
    }

    /**
     * Setter for the unitIds.
     *
     * @param unitIds
     *            the unitIds to set
     */
    public void setUnitIds( List<Integer> unitIds )
    {
        this._unitIds = unitIds;
    }

    /**
     * Getter for the directionsRefList.
     *
     * @return the directionsRefList
     */
    public ReferenceList getDirectionsRefList( )
    {
        return _directionsRefList;
    }

    /**
     * Setter for the directionsRefList.
     *
     * @param directionsRefList
     *            the directionsRefList to set
     */
    public void setDirectionsRefList( ReferenceList directionsRefList )
    {
        this._directionsRefList = directionsRefList;
    }

    /**
     * Getter for the arrondissementsRefList.
     *
     * @return the arrondissementsRefList
     */
    public ReferenceList getArrondissementsRefList( )
    {
        return _arrondissementsRefList;
    }

    /**
     * Setter for the arrondissementsRefList.
     *
     * @param arrondissementsRefList
     *            the arrondissementsRefList to set
     */
    public void setArrondissementsRefList( ReferenceList arrondissementsRefList )
    {
        this._arrondissementsRefList = arrondissementsRefList;
    }

    /**
     * Getter for the secteursRefList.
     *
     * @return the secteursRefList
     */
    public ReferenceList getSecteursRefList( )
    {
        return _secteursRefList;
    }

    /**
     * Setter for the secteursRefList.
     *
     * @param secteursRefList
     *            the secteursRefList to set
     */
    public void setSecteursRefList( ReferenceList secteursRefList )
    {
        this._secteursRefList = secteursRefList;
    }

    /**
     * Getter for the categoriesRefList.
     *
     * @return the categoriesRefList
     */
    public ReferenceList getCategoriesRefList( )
    {
        return _categoriesRefList;
    }

    /**
     * Setter for the categoriesRefList.
     *
     * @param categoriesRefList
     *            the categoriesRefList to set
     */
    public void setCategoriesRefList( ReferenceList categoriesRefList )
    {
        this._categoriesRefList = categoriesRefList;
    }

    /**
     * Getter for the typesAnomalieRefList.
     *
     * @return the typesAnomalieRefList
     */
    public ReferenceList getTypesAnomalieRefList( )
    {
        return _typesAnomalieRefList;
    }

    /**
     * Setter for the typesAnomalieRefList.
     *
     * @param typesAnomalieRefList
     *            the typesAnomalieRefList to set
     */
    public void setTypesAnomalieRefList( ReferenceList typesAnomalieRefList )
    {
        this._typesAnomalieRefList = typesAnomalieRefList;
    }

    /**
     * Gets the quartiers ids.
     *
     * @return the quartiers ids
     */
    public List<Integer> getQuartiersIds( )
    {
        return _listQuartiersIds;
    }

    /**
     * Sets the quartiers ids.
     *
     * @param quartiersIds
     *            the new quartiers ids
     */
    public void setQuartiersIds( List<Integer> quartiersIds )
    {
        this._listQuartiersIds = quartiersIds;
    }

    /**
     * Gets the quartier ref list.
     *
     * @return the quartier ref list
     */
    public ReferenceList getQuartierRefList( )
    {
        return _quartierRefList;
    }

    /**
     * Sets the quartier ref list.
     *
     * @param quartierRefList
     *            the new quartier ref list
     */
    public void setQuartierRefList( ReferenceList quartierRefList )
    {
        this._quartierRefList = quartierRefList;
    }

}
