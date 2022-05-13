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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * The Class SignalementBean.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class SignalementBean implements Serializable
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2999837851233494577L;

    /** Workflow resource type. */
    public static final String WORKFLOW_RESOURCE_TYPE = "SIGNALEMENT_SIGNALEMENT";

    /** The adresse. */
    private String _adresse;

    /** The creation date. */
    private String _strDateCreation;

    /** The id. */
    private Long _id;

    /** Anomalie number. */
    private String _strNumeroComplet;

    /** The image ensemble content. */
    private String _strImageEnsembleContent;

    /** The image pres content. */
    private String _strImagePresContent;

    /** The priorities. */
    private String _priorite;

    /** The commentary. */
    private String _strCommentaire;

    /** The str commentaire agent terrain. */
    private String _strCommentaireAgentTerrain;

    /** The scheduled treatment date. */
    private String _strDatePrevueTraitement;

    /** The str type signalement complet. */
    private String _strTypeSignalementComplet;

    /** The str statut. */
    private String _strStatut;

    /**
     * Gets the adresse.
     *
     * @return the adresse
     */
    public String getAdresse( )
    {
        return _adresse;
    }

    /**
     * Sets the adresse.
     *
     * @param adresse
     *            the new adresses
     */
    public void setAdresse( String adresse )
    {
        _adresse = adresse;
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
     * Gets the id.
     *
     * @return the id
     */
    public Long getId( )
    {
        return _id;
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
     * Gets the numero complet.
     *
     * @return the numero complet
     */
    public String getNumeroComplet( )
    {
        return _strNumeroComplet;
    }

    /**
     * Sets the numero complet.
     *
     * @param strNumeroComplet
     *            the new numero complet
     */
    public void setNumeroComplet( String strNumeroComplet )
    {
        _strNumeroComplet = strNumeroComplet;
    }

    /**
     * Gets the image ensemble content.
     *
     * @return the image ensemble content
     */
    public String getImageEnsembleContent( )
    {
        return _strImageEnsembleContent;
    }

    /**
     * Sets the image ensemble content.
     *
     * @param imageEnsembleContent
     *            the new image ensemble content
     */
    public void setImageEnsembleContent( String imageEnsembleContent )
    {
        _strImageEnsembleContent = imageEnsembleContent;
    }

    /**
     * Gets the image pres content.
     *
     * @return the image pres content
     */
    public String getImagePresContent( )
    {
        return _strImagePresContent;
    }

    /**
     * Sets the image pres content.
     *
     * @param imagePresContent
     *            the new image pres content
     */
    public void setImagePresContent( String imagePresContent )
    {
        _strImagePresContent = imagePresContent;
    }

    /**
     * Gets the priorite.
     *
     * @return the priorite
     */
    public String getPriorite( )
    {
        return _priorite;
    }

    /**
     * Sets the priorite.
     *
     * @param priorite
     *            the new priorite
     */
    public void setPriorite( String priorite )
    {
        _priorite = priorite;
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
     * @param strCommentaireAgentTerrain
     *            the new commentaire agent terrain
     */
    public void setCommentaireAgentTerrain( String strCommentaireAgentTerrain )
    {
        _strCommentaireAgentTerrain = strCommentaireAgentTerrain;
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
     * Sets the date prevue traitement.
     *
     * @param strDatePrevueTraitement
     *            the new date prevue traitement
     */
    public void setDatePrevueTraitement( String strDatePrevueTraitement )
    {
        _strDatePrevueTraitement = strDatePrevueTraitement;
    }

    /**
     * Gets the type signalement complet.
     *
     * @return the type signalement complet
     */
    public String getTypeSignalementComplet( )
    {
        return _strTypeSignalementComplet;
    }

    /**
     * Sets the type signalement complet.
     *
     * @param strTypeSignalementComplet
     *            the new type signalement complet
     */
    public void setTypeSignalementComplet( String strTypeSignalementComplet )
    {
        _strTypeSignalementComplet = strTypeSignalementComplet;
    }

    /**
     * Gets the statut.
     *
     * @return the statut
     */
    public String getStatut( )
    {
        return _strStatut;
    }

    /**
     * Sets the statut.
     *
     * @param strStatut
     *            the new statut
     */
    public void setStatut( String strStatut )
    {
        _strStatut = strStatut;
    }
}
