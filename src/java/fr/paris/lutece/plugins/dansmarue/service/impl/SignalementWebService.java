package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementWebService;
import fr.paris.lutece.plugins.dansmarue.utils.ws.IWebServiceCaller;
import fr.paris.lutece.util.signrequest.RequestAuthenticator;


public class SignalementWebService implements ISignalementWebService
{

    //JSON TAG
    public static final String REQUEST_METHOD_NAME = "addAnomalie";

    public static final String JSON_TAG_ANOMALIE = "anomalie";
    public static final String JSON_TAG_UDID = "udid";
    public static final String JSON_TAG_EMAIL = "email";
    public static final String JSON_TAG_PHOTOS = "photos";

    @Inject
    private IWebServiceCaller _wsCaller;

    @Inject
    @Named( "rest.requestAuthenticator" )
    private RequestAuthenticator _authenticator;
    
    public JSONObject getJSONResponse( Signalement signalement, String url )
    {
        JSONObject response = null;

        try
        {
            String strResp = sendByWS( signalement, url );

            JSONArray array = JSONArray.fromObject( strResp );
            response = array.getJSONObject( 0 );
        }
        catch ( BusinessException e )
        {
            response = new JSONObject( );
            response.accumulate( "request", REQUEST_METHOD_NAME );
            JSONObject error = new JSONObject( );
            error.accumulate( "error", "erreur lors du contact avec " + url );
            response.accumulate( "answer", error );
        }
        catch ( UnsupportedEncodingException e )
        {
            response = new JSONObject( );
            response.accumulate( "request", REQUEST_METHOD_NAME );
            JSONObject error = new JSONObject( );
            error.accumulate( "error", "erreur d'encoding" );
            response.accumulate( "answer", error );
        }

        return response;
    }

    @Override
    public String sendByWS( Signalement signalement, String url) throws BusinessException,
            UnsupportedEncodingException
    {
        String result = null;

        if ( signalement == null )
            throw new BusinessException( signalement, "dansmarue.ws.error.url.empty" );

        JSONObject json = createJSON( signalement );

        JSONObject jsonSrc = new JSONObject( );
        jsonSrc.accumulate( JSON_TAG_ANOMALIE, json );

        //name of the method in REST api
        jsonSrc.accumulate( "request", REQUEST_METHOD_NAME );

        Map<String, List<String>> params = new HashMap<String, List<String>>( );
        List<String> values = new ArrayList<String>( );
        String jsonFormated = "[" + jsonSrc.toString( ) + "]";
        values.add( jsonFormated );
        params.put( "jsonStream", values );

        try
        {
            result = _wsCaller.callWebService( url, params, _authenticator, values );
        }
        catch ( Exception e )
        {
            throw new BusinessException( signalement, "dansmarue.ws.error.url.connexion" );
        }

        return result;
    }

    public JSONObject createJSON( Signalement signalement ) throws UnsupportedEncodingException
    {
        // content of the json stream must be encode, because using application/x-www-form-urlencoded

        JSONObject jsonAnomalie = new JSONObject( );
        jsonAnomalie.accumulate( "id", signalement.getId( ) );
        jsonAnomalie.accumulate( "reference", signalement.getNumeroSignalement() );
        jsonAnomalie.accumulate( "date_creation", signalement.getDateCreation( ) );
        jsonAnomalie.accumulate( "commentaire", encode( signalement.getCommentaire( ) ) );
        jsonAnomalie.accumulate( "type", encode( signalement.getTypeSignalement( ).getLibelle( ) ) );
        jsonAnomalie.accumulate( "secteur", encode( signalement.getSecteur( ).getName( ) ) );
        jsonAnomalie.accumulate( "priorite", encode( signalement.getPriorite( ).getLibelle( ) ) );
        jsonAnomalie.accumulate( "arrondissement", signalement.getArrondissement( ).getNumero( ) );
        jsonAnomalie.accumulate( "adresse", encode( signalement.getAdresses( ).get( 0 ).getAdresse( ) ) );
        jsonAnomalie.accumulate( "lat", signalement.getAdresses( ).get( 0 ).getLat( ) );
        jsonAnomalie.accumulate( "lng", signalement.getAdresses( ).get( 0 ).getLng( ) );
        jsonAnomalie.accumulate( "token", signalement.getToken( ) );
        

        List<PhotoDMR> photos = signalement.getPhotos( );

        JSONArray array = new JSONArray( );
        if ( photos != null && !photos.isEmpty( ) )
        {
            for ( PhotoDMR p : photos )
            {
                array.add( p.getId( ) );
            }
            jsonAnomalie.accumulate( JSON_TAG_PHOTOS, array );
        }

        return jsonAnomalie;
    }

    /**
     * Encode signalement data before create JSON
     * @param data the signalement data
     * @return the encoded data
     * @throws UnsupportedEncodingException when charset cannot be use
     */
    private String encode( String data ) throws UnsupportedEncodingException
    {
        return StringUtils.isNotBlank( data ) ? URLEncoder.encode( data, CharEncoding.UTF_8 ) : StringUtils.EMPTY;
    }
}
