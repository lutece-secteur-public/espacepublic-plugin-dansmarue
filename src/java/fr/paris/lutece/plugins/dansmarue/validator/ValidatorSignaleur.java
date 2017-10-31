package fr.paris.lutece.plugins.dansmarue.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


public class ValidatorSignaleur {
    
	
	private static final String ADRESSE_EMAIL_NULL = "#i18n{dansmarue.message.erreur.email.invalide}";
	private static final String PARAMETER_EMAIL ="email";
	public Map<String, String> validate(  HttpServletRequest request)
    {
		

		Map<String, String> errorList = new HashMap();
		if(request.getParameter(PARAMETER_EMAIL) == null || StringUtils.isEmpty(PARAMETER_EMAIL)){
			
        	Boolean formatCodePostalOK = false;
            Pattern pattern = Pattern.compile( "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$" );
            Matcher matcher = pattern.matcher( request.getParameter(PARAMETER_EMAIL ) );
            while ( matcher.find( ) )
            {
                formatCodePostalOK = true;
            }

            if ( !formatCodePostalOK ){
            	errorList.put( PARAMETER_EMAIL, ADRESSE_EMAIL_NULL );
            }
    	}
        return errorList;
    }
}
