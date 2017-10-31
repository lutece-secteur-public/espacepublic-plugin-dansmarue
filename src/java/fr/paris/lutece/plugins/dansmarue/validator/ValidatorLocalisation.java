package fr.paris.lutece.plugins.dansmarue.validator;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.service.IArrondissementService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class ValidatorLocalisation {
	
	public boolean validate( Adresse adresse )
    {
		

        Double dLat;
        Double dLng;
        try
        {
            dLat = adresse.getLat();
            dLng = adresse.getLng();
        }
        catch ( Exception e )
        {
            // en cas d'erreur de cast des champs lat/lng
            dLat = null;
            dLng = null;
        }
        return validate(dLat,dLng);
    }
	
	
	
	/**
	 * Retrieves from the request, the coordinates, then check if inside Paris
	 * @param request
	 * @return
	 */
	public boolean validate( HttpServletRequest request )
    {
        Double dLat;
        Double dLng;
        try
        {
            dLat = Double.parseDouble( request.getParameter( SignalementConstants.FIELD_LAT ) );
            dLng = Double.parseDouble( request.getParameter( SignalementConstants.FIELD_LNG ) );
        }
        catch ( Exception e )
        {
            // en cas d'erreur de cast des champs lat/lng
            dLat = null;
            dLng = null;
        }
        return validate(dLat,dLng);
    }
	
	/**
	 * Checks if coordinates are inside Paris
	 * @param dLat
	 * @param dLng
	 * @return
	 */
	public boolean validate(Double dLat, Double dLng){
		boolean dansParis = ( ( dLat != null && dLng != null ) && estDansParis( dLat, dLng ) );
        boolean resultOK = dansParis;

        return resultOK;
	}
	
	  /**
     * Est dans paris.
     * 
     * @param errorList the error list
     * @param dLat the lat
     * @param dLng the lng
     * @return true, if successful, coordinate are within paris.
     */
    private boolean estDansParis(  Double dLat, Double dLng )
    {
        IArrondissementService arrondissementService = (IArrondissementService) SpringContextService
                .getBean( "signalement.arrondissementService" );
        boolean dansParis = false;
        if ( dLat == null || dLng == null )
        {
            dansParis = false;
        }
        else
        {
            Arrondissement arrondissementByGeom = arrondissementService.getArrondissementByGeom( dLng, dLat );
            dansParis = arrondissementByGeom != null;
        }


        return dansParis;
    }
}
