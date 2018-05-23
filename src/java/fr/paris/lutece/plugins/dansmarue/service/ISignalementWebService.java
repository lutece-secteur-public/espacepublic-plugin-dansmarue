package fr.paris.lutece.plugins.dansmarue.service;

import java.io.UnsupportedEncodingException;

import net.sf.json.JSONObject;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;


public interface ISignalementWebService
{

    /**
     * Send signalement to the correct recipient, with Web Service
     * @param signalement the signalement to send
     * @param url the ws url
     * @return the return of the call, fail or success
     * @throws UnsupportedEncodingException
     * @throws BusinessException
     */
    String sendByWS( Signalement signalement, String url ) throws BusinessException, UnsupportedEncodingException;

    JSONObject getJSONResponse( Signalement signalement, String url );
    
    /**
     * convert signalement into json formated data
     * @param signalement the signalement
     * @return the json
     * @throws UnsupportedEncodingException the exception
     */
    JSONObject createJSON( Signalement signalement ) throws UnsupportedEncodingException;
    
    
    /**
     * Send notification service done to the correct recipient, with Web Service
     * @param signalement the signalement to send
     * @param urlPartner  the ws url
     * @return the return of the call, fail or success
     */
    JSONObject callWSPartnerServiceDone (Signalement signalement, String urlPartner );
}
