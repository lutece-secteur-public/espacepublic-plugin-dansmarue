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

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

/**
 * The Class FeuilleDeTourneeValidationTest.
 */
public class FeuilleDeTourneeValidationTest
{

    /**
     * Return true when date debut is null.
     */
    @Test
    public void return_true_when_date_debut_is_null( )
    {
        // Given
        FeuilleDeTourneeFilter feuilleDeTourneeFilter = new FeuilleDeTourneeFilter( );
        feuilleDeTourneeFilter.setDateCreationFin( "10/11/2010" );

        // When
        boolean result = feuilleDeTourneeFilter.isDatesValid( );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isTrue( );
        softly.assertAll( );
    }

    /**
     * Return true when date debut is empty.
     */
    @Test
    public void return_true_when_date_debut_is_empty( )
    {
        // Given
        FeuilleDeTourneeFilter feuilleDeTourneeFilter = new FeuilleDeTourneeFilter( );
        feuilleDeTourneeFilter.setDateCreationDebut( "" );
        feuilleDeTourneeFilter.setDateCreationFin( "10/11/2010" );

        // When
        boolean result = feuilleDeTourneeFilter.isDatesValid( );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isTrue( );
        softly.assertAll( );
    }

    /**
     * Return true when date fin is null.
     */
    @Test
    public void return_true_when_date_fin_is_null( )
    {
        // Given
        FeuilleDeTourneeFilter feuilleDeTourneeFilter = new FeuilleDeTourneeFilter( );
        feuilleDeTourneeFilter.setDateCreationDebut( "10/11/2010" );

        // When
        boolean result = feuilleDeTourneeFilter.isDatesValid( );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isTrue( );
        softly.assertAll( );
    }

    /**
     * Return true when date fin is empty.
     */
    @Test
    public void return_true_when_date_fin_is_empty( )
    {
        // Given
        FeuilleDeTourneeFilter feuilleDeTourneeFilter = new FeuilleDeTourneeFilter( );
        feuilleDeTourneeFilter.setDateCreationFin( "" );
        feuilleDeTourneeFilter.setDateCreationDebut( "10/11/2010" );

        // When
        boolean result = feuilleDeTourneeFilter.isDatesValid( );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isTrue( );
        softly.assertAll( );
    }

    /**
     * Return true when date fin is after date debut.
     */
    @Test
    public void return_true_when_date_fin_is_after_date_debut( )
    {
        // Given
        FeuilleDeTourneeFilter feuilleDeTourneeFilter = new FeuilleDeTourneeFilter( );
        feuilleDeTourneeFilter.setDateCreationDebut( "10/11/2010" );
        feuilleDeTourneeFilter.setDateCreationFin( "11/11/2010" );

        // When
        boolean result = feuilleDeTourneeFilter.isDatesValid( );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isTrue( );
        softly.assertAll( );
    }

    /**
     * Return true when date fin is equal to date debut.
     */
    @Test
    public void return_true_when_date_fin_is_equal_to_date_debut( )
    {
        // Given
        FeuilleDeTourneeFilter feuilleDeTourneeFilter = new FeuilleDeTourneeFilter( );
        feuilleDeTourneeFilter.setDateCreationDebut( "11/11/2010" );
        feuilleDeTourneeFilter.setDateCreationFin( "11/11/2010" );

        // When
        boolean result = feuilleDeTourneeFilter.isDatesValid( );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isTrue( );
        softly.assertAll( );
    }

    /**
     * Return false when date fin is before date debut.
     */
    @Test
    public void return_false_when_date_fin_is_before_date_debut( )
    {
        // Given
        FeuilleDeTourneeFilter feuilleDeTourneeFilter = new FeuilleDeTourneeFilter( );
        feuilleDeTourneeFilter.setDateCreationDebut( "12/11/2010" );
        feuilleDeTourneeFilter.setDateCreationFin( "11/11/2010" );

        // When
        boolean result = feuilleDeTourneeFilter.isDatesValid( );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isFalse( );
        softly.assertAll( );
    }
}
