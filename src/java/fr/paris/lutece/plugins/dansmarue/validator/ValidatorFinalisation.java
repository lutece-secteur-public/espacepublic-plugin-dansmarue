package fr.paris.lutece.plugins.dansmarue.validator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;

import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;



public class ValidatorFinalisation {
	
    public boolean validate(  HttpServletRequest request)
    {
        int typeSignalementId = NumberUtils.toInt( request.getParameter( SignalementConstants.PARAMETER_PRIORITE ) );
        boolean ok = typeSignalementId > 0;
        return ok;
    }
}
