package fr.paris.lutece.plugins.dansmarue.service.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
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
import fr.paris.lutece.plugins.unittree.modules.sira.business.sector.Sector;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.signrequest.RequestAuthenticator;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RunWith( MockitoJUnitRunner.class )
public class SignalementWebServiceTest
{

    @Mock
    private IWebServiceCaller    _wsCaller;

    @Mock( name = "signalement.outputws.requestAuthenticator" )
    private RequestAuthenticator _authenticator;

    @InjectMocks
    SignalementWebService        _signalementWebService = new SignalementWebService( );

    private Signalement          signalement;

    @Before
    public void initSignalement( )
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
    }

    @Test
    public void sendByWSTest( ) throws HttpAccessException, BusinessException, UnsupportedEncodingException
    {
        String jsonStream = "[{\"anomalie\":{\"id\":null,\"date_creation\":null,\"commentaire\":\"\",\"type\":\"\",\"secteur\":\"\",\"priorite\":\"\",\"arrondissement\":null,\"adresse\":\"\",\"lat\":null,\"lng\":null,\"photos\":[1,42,2]},\"request\":\"addAnomalie\"}]";

        List<String> arrayList = new ArrayList<String>( );
        arrayList.add( jsonStream );

        Map<String, List<String>> hashMap = new HashMap<String, List<String>>( );
        hashMap.put( "jsonStream", arrayList );

        when( _wsCaller.callWebService( "", hashMap, _authenticator, arrayList ) ).thenReturn( "OK" );

        String result = _signalementWebService.sendByWS( signalement, "" );

        assertTrue( result != null );
        assertTrue( result.length( ) > 0 );
    }

    @Test( expected = HttpAccessException.class )
    @Ignore( "Implementation partiel du test, il faudrait founir une implémentation partiel pour le ws caller" )
    public void sendByWSHttpExceptionTest( ) throws HttpAccessException, UnsupportedEncodingException
    {
        when( _wsCaller.callWebService( "", new HashMap<String, List<String>>( ), _authenticator, new ArrayList<String>( ) ) ).thenReturn(
                ( new WebServiceCaller( ) ).callWebService( "", new HashMap<String, List<String>>( ), _authenticator, new ArrayList<String>( ) ) );

        String result = _signalementWebService.sendByWS( signalement, "" );

        assertTrue( result != null );
        assertTrue( result.length( ) > 0 );
    }

    @Test( expected = NullPointerException.class )
    public void createJSONExceptionTest( ) throws UnsupportedEncodingException
    {
        JSONObject createJSON = _signalementWebService.createJSON( null );
        assertTrue( createJSON != null );
    }

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
        assertTrue( expectedIds.equals( photosIds ) );
    }
}
