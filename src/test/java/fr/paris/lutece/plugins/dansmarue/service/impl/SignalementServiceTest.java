/*
 * Copyright (c) 2002-2021, City of Paris
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

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import fr.paris.lutece.plugins.dansmarue.business.entities.EtatSignalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementFilter;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * The Class SignalementServiceTest.
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( {
        AppPropertiesService.class
} )
public class SignalementServiceTest
{
    SignalementService _signalementService = new SignalementService( );
    EtatSignalement stateSF = new EtatSignalement( );

    @Before
    public void initStateSF( )
    {
        stateSF.setCoche( true );
        stateSF.setId( 10l );
        stateSF.setLibelle( "Service fait" );
    }

    @Test
    public void return_null_when_date_sf_debut_and_date_sf_fin_is_null( )
    {
        // Given
        SignalementFilter signalementFilter = new SignalementFilter( );

        // When
        EtatSignalement result = _signalementService.getStateIfDateIndicated( signalementFilter );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isNull( );
        softly.assertAll( );
    }

    @Test
    public void return_null_when_date_sf_debut_and_date_sf_fin_is_empty( )
    {
        // Given
        SignalementFilter signalementFilter = new SignalementFilter( );
        signalementFilter.setDateDoneBegin( "" );
        signalementFilter.setDateDoneEnd( "" );

        // When
        EtatSignalement result = _signalementService.getStateIfDateIndicated( signalementFilter );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isNull( );
        softly.assertAll( );
    }

    @Test
    public void return_stateSF_when_date_sf_debut_is_not_empty_and_date_sf_fin_is_empty( )
    {
        // Given
        SignalementFilter signalementFilter = new SignalementFilter( );
        signalementFilter.setDateDoneBegin( "01/10/2018" );
        signalementFilter.setDateDoneEnd( "" );

        PowerMockito.mockStatic( AppPropertiesService.class );
        Mockito.when( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_SERVICE_FAIT_VALUE ) ).thenReturn( "10" );
        Mockito.when( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_SERVICE_FAIT_LIBELLE ) ).thenReturn( "Service fait" );

        // When
        EtatSignalement result = _signalementService.getStateIfDateIndicated( signalementFilter );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isNotNull( );
        softly.assertThat( result ).isEqualTo( stateSF );
        softly.assertAll( );
    }

    @Test
    public void return_stateSF_when_date_sf_debut_is_empty_and_date_sf_fin_is_not_empty( )
    {
        // Given
        SignalementFilter signalementFilter = new SignalementFilter( );
        signalementFilter.setDateDoneBegin( "" );
        signalementFilter.setDateDoneEnd( "12/01/2016" );

        PowerMockito.mockStatic( AppPropertiesService.class );
        Mockito.when( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_SERVICE_FAIT_VALUE ) ).thenReturn( "10" );
        Mockito.when( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_SERVICE_FAIT_LIBELLE ) ).thenReturn( "Service fait" );

        // When
        EtatSignalement result = _signalementService.getStateIfDateIndicated( signalementFilter );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isNotNull( );
        softly.assertThat( result ).isEqualTo( stateSF );
        softly.assertAll( );
    }

    @Test
    public void return_stateSF_when_date_sf_debut_is_not_empty_and_date_sf_fin_is_not_empty( )
    {
        // Given
        SignalementFilter signalementFilter = new SignalementFilter( );
        signalementFilter.setDateDoneBegin( "24/11/2014" );
        signalementFilter.setDateDoneEnd( "12/01/2016" );

        PowerMockito.mockStatic( AppPropertiesService.class );
        Mockito.when( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_SERVICE_FAIT_VALUE ) ).thenReturn( "10" );
        Mockito.when( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_SERVICE_FAIT_LIBELLE ) ).thenReturn( "Service fait" );

        // When
        EtatSignalement result = _signalementService.getStateIfDateIndicated( signalementFilter );

        // Then
        SoftAssertions softly = new SoftAssertions( );
        softly.assertThat( result ).isNotNull( );
        softly.assertThat( result ).isEqualTo( stateSF );
        softly.assertAll( );
    }

}
