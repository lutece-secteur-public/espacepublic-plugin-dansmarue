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
package fr.paris.lutece.plugins.dansmarue.service.impl.feuilleDeTournee;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import fr.paris.lutece.plugins.dansmarue.service.impl.FeuilleDeTourneeService;

/**
 * The Class FeuilleDeTourneeServiceTest.
 */
public class FeuilleDeTourneeServiceGetDestinaitaireTest
{
    @Test
    public void return_empty_when_input_is_null( )
    {
        // Given
        String input = null;

        FeuilleDeTourneeService feuilleDeTourneeService = new FeuilleDeTourneeService( );

        // When
        List<String> result = feuilleDeTourneeService.getDestinatairesFromParam( input );

        // Then

        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isEmpty( );
        softly.assertAll( );

    }

    @Test
    public void return_empty_when_input_is_empty( )
    {
        // Given
        String input = "";

        FeuilleDeTourneeService feuilleDeTourneeService = new FeuilleDeTourneeService( );

        // When
        List<String> result = feuilleDeTourneeService.getDestinatairesFromParam( input );

        // Then

        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isEmpty( );
        softly.assertAll( );

    }

    @Test
    public void return_toto_at_hotmailfr_when_input_is_toto_at_hotmailfr( )
    {
        // Given
        String input = "toto@hotmail.fr";

        List<String> expected = new ArrayList<>( );
        expected.add( "toto@hotmail.fr" );

        FeuilleDeTourneeService feuilleDeTourneeService = new FeuilleDeTourneeService( );

        // When
        List<String> result = feuilleDeTourneeService.getDestinatairesFromParam( input );

        // Then

        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isNotEmpty( );
        softly.assertThat( result ).isEqualTo( expected );
        softly.assertAll( );

    }

    @Test
    public void return_bob_at_hotmailfr_when_input_is_bob_at_hotmailfr( )
    {
        // Given
        String input = "bob@hotmail.fr";

        List<String> expected = new ArrayList<>( );
        expected.add( "bob@hotmail.fr" );

        FeuilleDeTourneeService feuilleDeTourneeService = new FeuilleDeTourneeService( );

        // When
        List<String> result = feuilleDeTourneeService.getDestinatairesFromParam( input );

        // Then

        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isNotEmpty( );
        softly.assertThat( result ).isEqualTo( expected );
        softly.assertAll( );

    }

    @Test
    public void return_charly_at_hotmailfr_and_yoyo_at_gmailcom_when_input_is_charly_at_hotmailfr_and_yoyo_at_gmailcom( )
    {
        // Given
        String input = "charly@hotmail.fr,yoyo@gmail.com";

        List<String> expected = new ArrayList<>( );
        expected.add( "charly@hotmail.fr" );
        expected.add( "yoyo@gmail.com" );

        FeuilleDeTourneeService feuilleDeTourneeService = new FeuilleDeTourneeService( );

        // When
        List<String> result = feuilleDeTourneeService.getDestinatairesFromParam( input );

        // Then

        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isNotEmpty( );
        softly.assertThat( result ).isEqualTo( expected );
        softly.assertAll( );

    }

    @Test
    public void return_mike_at_hotmailfr_and_jason_at_gmailcom_when_input_is_mike_at_hotmailfr_and_jason_at_gmailcom( )
    {
        // Given
        String input = "mike@hotmail.fr,jason@gmail.com";

        List<String> expected = new ArrayList<>( );
        expected.add( "mike@hotmail.fr" );
        expected.add( "jason@gmail.com" );

        FeuilleDeTourneeService feuilleDeTourneeService = new FeuilleDeTourneeService( );

        // When
        List<String> result = feuilleDeTourneeService.getDestinatairesFromParam( input );

        // Then

        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isNotEmpty( );
        softly.assertThat( result ).isEqualTo( expected );
        softly.assertAll( );

    }
}
