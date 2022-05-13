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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Priorite;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.utils.ws.IWebServiceCaller;
import fr.paris.lutece.plugins.dansmarue.utils.ws.WebServiceCaller;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.business.sector.Sector;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.signrequest.RequestAuthenticator;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Class SignalementWebServiceTest.
 */
@RunWith( MockitoJUnitRunner.class )
public class SignalementWebServiceTest
{

    /** The ws caller. */
    @Mock
    private IWebServiceCaller _wsCaller;

    /** The authenticator. */
    @Mock( name = "signalement.outputws.requestAuthenticator" )
    private RequestAuthenticator _authenticator;

    /** The signalement web service. */
    @InjectMocks
    SignalementWebService _signalementWebService = new SignalementWebService( );

    /** The signalement. */
    private Signalement signalement;

    /**
     * Inits the signalement.
     *
     * @throws ParseException
     *             the parse exception
     */
    @Before
    public void initSignalement( ) throws ParseException
    {
        signalement = new Signalement( );
        signalement.setTypeSignalement( new TypeSignalement( ) );
        signalement.setSecteur( new Sector( ) );
        signalement.setPriorite( new Priorite( ) );
        signalement.setArrondissement( new Arrondissement( ) );
        signalement.setAdresses( new ArrayList<Adresse>( ) );
        signalement.getAdresses( ).add( new Adresse( ) );

        List<PhotoDMR> photos = new ArrayList<PhotoDMR>( );
        PhotoDMR p1 = new PhotoDMR( );
        p1.setId( 1l );
        PhotoDMR p2 = new PhotoDMR( );
        p2.setId( 42l );
        PhotoDMR p3 = new PhotoDMR( );
        p3.setId( 2l );
        photos.add( p1 );
        photos.add( p2 );
        photos.add( p3 );
        signalement.setPhotos( photos );
        signalement.setHeureCreation( new SimpleDateFormat( "HH:mm:ss" ).parse( "00:00:00" ) );
    }

    /**
     * Send by WS test.
     *
     * @throws HttpAccessException
     *             the http access exception
     * @throws BusinessException
     *             the business exception
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     * @throws ParseException
     *             the parse exception
     */
    @Test
    public void sendByWSTest( ) throws HttpAccessException, BusinessException, UnsupportedEncodingException, ParseException
    {
        String jsonStream = "{\"anomalie\":{\"id\":null,\"reference\":\"null0null0\",\"date_creation\":null,\"heure_creation\":\"00:00:00\",\"commentaire\":\"\",\"type\":\"\",\"priorite\":\"\",\"adresse\":\"\",\"lat\":null,\"lng\":null,\"token\":null,\"photos\":[]},\"request\":\"addAnomalie\"}";
        List<String> arrayList = new ArrayList<String>( );
        arrayList.add( jsonStream );

        Map<String, List<String>> hashMap = new HashMap<String, List<String>>( );
        hashMap.put( "jsonStream", arrayList );

        when( _wsCaller.callWebService( "", hashMap, _authenticator, arrayList ) ).thenReturn( "OK" );

        signalement.setHeureCreation( new SimpleDateFormat( "HH:mm:ss" ).parse( "00:00:00" ) );
        signalement.setPhotos( null );
        String result = _signalementWebService.sendByWS( signalement, "" );

        assertTrue( result != null );
        assertTrue( result.length( ) > 0 );
    }

    /**
     * Send by WS http exception test.
     *
     * @throws HttpAccessException
     *             the http access exception
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    @Test( expected = HttpAccessException.class )
    @Ignore( "Implementation partiel du test, il faudrait founir une implémentation partiel pour le ws caller" )
    public void sendByWSHttpExceptionTest( ) throws HttpAccessException, UnsupportedEncodingException
    {
        when( _wsCaller.callWebService( "", new HashMap<String, List<String>>( ), _authenticator, new ArrayList<String>( ) ) )
                .thenReturn( ( new WebServiceCaller( ) ).callWebService( "", new HashMap<String, List<String>>( ), _authenticator, new ArrayList<String>( ) ) );

        String result = _signalementWebService.sendByWS( signalement, "" );

        assertTrue( result != null );
        assertTrue( result.length( ) > 0 );
    }

    /**
     * Creates the JSON exception test.
     *
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    @Test( expected = NullPointerException.class )
    public void createJSONExceptionTest( ) throws UnsupportedEncodingException
    {
        JSONObject createJSON = _signalementWebService.createJSON( null );
        assertTrue( createJSON != null );
    }

    /**
     * Creates the JSON test.
     *
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    @Test
    public void createJSONTest( ) throws UnsupportedEncodingException
    {
        JSONObject anomalie = _signalementWebService.createJSON( signalement );
        assertTrue( anomalie != null );
        JSONArray photosIds = anomalie.getJSONArray( SignalementWebService.JSON_TAG_PHOTOS );

        JSONArray expectedIds = new JSONArray( );
        expectedIds.add( 1 );
        expectedIds.add( 42 );
        expectedIds.add( 2 );
        // assertTrue( expectedIds.equals( photosIds ) );
    }
}
