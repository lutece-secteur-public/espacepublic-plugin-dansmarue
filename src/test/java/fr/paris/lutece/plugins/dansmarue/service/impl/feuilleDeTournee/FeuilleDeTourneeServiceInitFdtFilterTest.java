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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import fr.paris.lutece.plugins.dansmarue.business.entities.FeuilleDeTourneeFilter;
import fr.paris.lutece.plugins.dansmarue.service.impl.FeuilleDeTourneeService;

/**
 * The Class FeuilleDeTourneeServiceTest.
 */
public class FeuilleDeTourneeServiceInitFdtFilterTest
{

    /** The formatter. */
    private DateTimeFormatter _formatter = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );

    /**
     * Return not nul filter when input is null.
     */
    @Test
    public void return_not_nul_when_input_is_null( )
    {
        // Given
        FeuilleDeTourneeFilter feuilleDeTourneeFilter = null;
        FeuilleDeTourneeService feuilleDeTourneeService = new FeuilleDeTourneeService( );

        // When
        feuilleDeTourneeFilter = feuilleDeTourneeService.initFdtFilter( feuilleDeTourneeFilter );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( feuilleDeTourneeFilter ).isNotNull( );
        softly.assertAll( );
    }

    /**
     * Return date debut 01 01 2021 when input is 01 01 2021.
     */
    @Test
    public void return_date_debut_01_01_2021_when_input_is_01_01_2021( )
    {
        // Given
        FeuilleDeTourneeFilter feuilleDeTourneeFilter = new FeuilleDeTourneeFilter( );
        FeuilleDeTourneeService feuilleDeTourneeService = new FeuilleDeTourneeService( );
        feuilleDeTourneeFilter.setDateCreationDebut( "01/01/2021" );

        // When
        feuilleDeTourneeFilter = feuilleDeTourneeService.initFdtFilter( feuilleDeTourneeFilter );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( feuilleDeTourneeFilter.getDateCreationDebut( ) ).isEqualTo( "01/01/2021" );
        softly.assertAll( );
    }

    /**
     * Return date debut actual date minus 30 days when input is null.
     */
    @Test
    public void return_date_debut_actual_date_minus_30_days_when_input_is_null( )
    {
        // Given
        FeuilleDeTourneeFilter feuilleDeTourneeFilter = null;
        FeuilleDeTourneeService feuilleDeTourneeService = new FeuilleDeTourneeService( );

        // When
        feuilleDeTourneeFilter = feuilleDeTourneeService.initFdtFilter( feuilleDeTourneeFilter );

        // Then
        LocalDate now = LocalDate.now( ).minusDays( 30 );

        String formattedString = now.format( _formatter );

        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( feuilleDeTourneeFilter.getDateCreationDebut( ) ).isEqualTo( formattedString );
        softly.assertAll( );
    }

    /**
     * Return date fin actual date when input is null.
     */
    @Test
    public void return_date_fin_actual_date_when_input_is_null( )
    {
        // Given
        FeuilleDeTourneeFilter feuilleDeTourneeFilter = null;
        FeuilleDeTourneeService feuilleDeTourneeService = new FeuilleDeTourneeService( );

        // When
        feuilleDeTourneeFilter = feuilleDeTourneeService.initFdtFilter( feuilleDeTourneeFilter );

        // Then
        LocalDate now = LocalDate.now( );

        String formattedString = now.format( _formatter );

        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( feuilleDeTourneeFilter.getDateCreationFin( ) ).isEqualTo( formattedString );
        softly.assertAll( );
    }

    /**
     * Return date fin 05 20 2021 when input is 05 20 2021.
     */
    @Test
    public void return_date_fin_05_20_2021_when_input_is_05_20_2021( )
    {
        // Given
        FeuilleDeTourneeFilter feuilleDeTourneeFilter = new FeuilleDeTourneeFilter( );
        FeuilleDeTourneeService feuilleDeTourneeService = new FeuilleDeTourneeService( );
        feuilleDeTourneeFilter.setDateCreationFin( "05/20/2021" );

        // When
        feuilleDeTourneeFilter = feuilleDeTourneeService.initFdtFilter( feuilleDeTourneeFilter );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( feuilleDeTourneeFilter.getDateCreationFin( ) ).isEqualTo( "05/20/2021" );
        softly.assertAll( );
    }

}
