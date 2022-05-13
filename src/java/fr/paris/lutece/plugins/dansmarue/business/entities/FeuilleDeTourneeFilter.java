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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The Class FeuilleDeTourneeFilter.
 */
public class FeuilleDeTourneeFilter implements Serializable
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5973557041839509840L;

    /** The nom. */
    private String _nom;

    /** The str createur. */
    private String _strCreateur;

    /** The str date creation debut. */
    private String _strDateCreationDebut;

    /** The str date creation fin. */
    private String _strDateCreationFin;

    private DateTimeFormatter _formatter = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );

    /**
     * Gets the nom.
     *
     * @return the nom
     */
    public String getNom( )
    {
        return _nom;
    }

    /**
     * Sets the nom.
     *
     * @param nom
     *            the new nom
     */
    public void setNom( String nom )
    {
        _nom = nom;
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
     * Gets the date creation.
     *
     * @return the date creation
     */
    public String getDateCreationDebut( )
    {
        return _strDateCreationDebut;
    }

    /**
     * Sets the date creation.
     *
     * @param dateCreationDebut
     *            the new date creation debut
     */
    public void setDateCreationDebut( String dateCreationDebut )
    {
        _strDateCreationDebut = dateCreationDebut;
    }

    /**
     * Gets the date creation fin.
     *
     * @return the date creation fin
     */
    public String getDateCreationFin( )
    {
        return _strDateCreationFin;
    }

    /**
     * Sets the date creation fin.
     *
     * @param dateCreationFin
     *            the new date creation fin
     */
    public void setDateCreationFin( String dateCreationFin )
    {
        _strDateCreationFin = dateCreationFin;
    }

    /**
     * Checks if is dates valid.
     *
     * @return true, if is dates valid
     */
    public boolean isDatesValid( )
    {
        if ( ( _strDateCreationDebut == null ) || _strDateCreationDebut.isEmpty( ) || ( _strDateCreationFin == null ) || ( _strDateCreationFin.isEmpty( ) ) )
        {
            return true;
        }

        LocalDate dateCreationDebut = LocalDate.parse( _strDateCreationDebut, _formatter );
        LocalDate dateCreationFin = LocalDate.parse( _strDateCreationFin, _formatter );

        return dateCreationFin.isAfter( dateCreationDebut ) || dateCreationFin.isEqual( dateCreationDebut );
    }

}
