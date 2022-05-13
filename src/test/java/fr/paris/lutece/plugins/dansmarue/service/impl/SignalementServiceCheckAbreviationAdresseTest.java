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
package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * The Class SignalementServiceTest.
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( {
        AppPropertiesService.class
} )
public class SignalementServiceCheckAbreviationAdresseTest
{
    SignalementService _signalementService = new SignalementService( );

    @Test
    public void return_null_when_signalement_is_null( )
    {
        // Given
        Signalement signalement = null;

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement ).isNull( );
        softly.assertAll( );
    }

    @Test
    public void return_null_when_signalement_list_adress_is_null( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = null;

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ) ).isNull( );
        softly.assertAll( );
    }

    @Test
    public void return_null_when_signalement_adress_is_null( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        adresses.add( null );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ) ).isNull( );
        softly.assertAll( );
    }

    @Test
    public void return_null_when_signalement_first_adress_is_null( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( null );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isNull( );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_begin_with_Pl( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "Pl. de Clichy, 75017 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "place de Clichy, 75017 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Pl( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "30 Pl. de Clichy, 75017 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "30 place de Clichy, 75017 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Bd( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "94 Bd Haussmann, 75008 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "94 boulevard Haussmann, 75008 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Av( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "24 Av. des Champs-Elysées, 75016 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "24 avenue des Champs-Elysées, 75016 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Vla( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "3 Vla Léandre, 75018 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "3 villa Léandre, 75018 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Pass( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "57 Pass. Brady, 75010 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "57 passage Brady, 75010 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Imp( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "2 Imp. de la Chapelle, 75018 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "2 impasse de la Chapelle, 75018 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Sq( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "4 Sq. Rapp, 75007 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "4 square Rapp, 75007 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Cr( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "41 Cr Saint-Emilion, 75012 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "41 cours Saint-Emilion, 75012 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Ave( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "41 Ave Saint-Emilion, 75012 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "41 avenue Saint-Emilion, 75012 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Blvd( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "41 Blvd Saint-Emilion, 75012 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "41 boulevard Saint-Emilion, 75012 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Dr( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "41 Dr Saint-Emilion, 75012 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "41 docteur Saint-Emilion, 75012 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Espl( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "41 Espl. Saint-Emilion, 75012 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "41 esplanade Saint-Emilion, 75012 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Frme( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "41 Frme Saint-Emilion, 75012 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "41 Ferme Saint-Emilion, 75012 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Prom( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "41 Prom. Saint-Emilion, 75012 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "41 promenade Saint-Emilion, 75012 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Prte( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "41 Prte Saint-Emilion, 75012 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "41 porte Saint-Emilion, 75012 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_R( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "41 R. Saint-Emilion, 75012 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "41 rue Saint-Emilion, 75012 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Rdpt( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "41 Rdpt Saint-Emilion, 75012 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "41 rond-point Saint-Emilion, 75012 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Rte( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "41 Rte Saint-Emilion, 75012 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "41 route Saint-Emilion, 75012 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Sent( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "41 Sent. Saint-Emilion, 75012 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "41 sentier Saint-Emilion, 75012 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Ch( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "41 Ch. Saint-Emilion, 75012 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "41 Charles Saint-Emilion, 75012 PARIS" );
        softly.assertAll( );
    }

    @Test
    public void return_place_when_signalement_adress_contain_Chem( )
    {
        // Given
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( "41 Chem. Saint-Emilion, 75012 PARIS" );
        adresses.add( adresse );

        signalement.setAdresses( adresses );

        // When
        _signalementService.checkAbreviationAdresse( signalement );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( signalement.getAdresses( ).get( 0 ).getAdresse( ) ).isEqualTo( "41 Chemin Saint-Emilion, 75012 PARIS" );
        softly.assertAll( );
    }

}
