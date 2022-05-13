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
 * The Class FeuilleDeTourneeServiceControlFormatEmailTest.
 */
public class FeuilleDeTourneeServiceControlFormatEmailTest
{
    @Test
    public void return_false_when_input_is_null( )
    {
        // Given
        List<String> input = null;
        FeuilleDeTourneeService feuilleDeTourneeService = new FeuilleDeTourneeService( );

        // When
        Boolean result = feuilleDeTourneeService.isFormatEmailOK( input );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isEqualTo( false );
        softly.assertAll( );

    }

    @Test
    public void return_false_when_input_is_one_not_valid_email( )
    {
        // Given
        List<String> input = new ArrayList<String>( );
        input.add( "a" );

        FeuilleDeTourneeService feuilleDeTourneeService = new FeuilleDeTourneeService( );

        // When
        Boolean result = feuilleDeTourneeService.isFormatEmailOK( input );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isEqualTo( false );
        softly.assertAll( );

    }

    @Test
    public void return_true_when_input_is_one_valid_email( )
    {
        // Given
        List<String> input = new ArrayList<String>( );
        input.add( "toto@hotmail.fr" );

        FeuilleDeTourneeService feuilleDeTourneeService = new FeuilleDeTourneeService( );

        // When
        Boolean result = feuilleDeTourneeService.isFormatEmailOK( input );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isEqualTo( true );
        softly.assertAll( );

    }

    @Test
    public void return_false_when_input_is_two_not_valid_email( )
    {
        // Given
        List<String> input = new ArrayList<String>( );
        input.add( "a" );
        input.add( "b" );

        FeuilleDeTourneeService feuilleDeTourneeService = new FeuilleDeTourneeService( );

        // When
        Boolean result = feuilleDeTourneeService.isFormatEmailOK( input );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isEqualTo( false );
        softly.assertAll( );

    }

    @Test
    public void return_true_when_input_is_two_valid_email( )
    {
        // Given
        List<String> input = new ArrayList<String>( );
        input.add( "toto@hotmail.fr" );
        input.add( "bob@gmail.fr" );

        FeuilleDeTourneeService feuilleDeTourneeService = new FeuilleDeTourneeService( );

        // When
        Boolean result = feuilleDeTourneeService.isFormatEmailOK( input );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isEqualTo( true );
        softly.assertAll( );

    }

    @Test
    public void return_false_when_input_is_one_not_valid_email_and_one_valid_email( )
    {
        // Given
        List<String> input = new ArrayList<String>( );
        input.add( "a" );
        input.add( "charly@gmail.fr" );

        FeuilleDeTourneeService feuilleDeTourneeService = new FeuilleDeTourneeService( );

        // When
        Boolean result = feuilleDeTourneeService.isFormatEmailOK( input );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isEqualTo( false );
        softly.assertAll( );

    }

}
