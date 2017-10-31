package fr.paris.lutece.plugins.dansmarue.utils;

import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 
 * SignalementUtils
 *
 */
public final class SignalementUtils 
{
	/**
	 * Utility class
	 */
	private SignalementUtils()
	{
		// nothing
	}
	
	/**
	 * Gets the plugin
	 * @return
	 */
	public static Plugin getPlugin()
	{
		return PluginService.getPlugin(SignalementPlugin.PLUGIN_NAME );
	}
	
	/**
	 * Peuple un bean, lance une {@link AppException} en cas d'erreur
	 * @param bean le bean
	 * @param request la request
	 */
	public static <T> void populate( T bean, HttpServletRequest request )
	{
		try 
		{
			BeanUtils.populate( bean, request.getParameterMap(  ) );
		} 
		catch ( IllegalAccessException e ) 
		{
			throw new AppException( "Unable to populate bean " + e.getMessage(  ), e );
		} 
		catch ( InvocationTargetException e ) 
		{
			throw new AppException( "Unable to populate bean " + e.getMessage(  ), e );
		}
	}
	
	/**
	 * 
	 * getIntArray : does not catch {@link NumberFormatException}
	 * @param stringArray string array
	 * @return int array
	 */
	public static int[] getIntArray( String[] stringArray )
	{
	    if ( stringArray == null )
	    {
	        return null;
	    }
	    int[] intArray = new int[stringArray.length];
	    
	    for ( int nIndex = 0; nIndex < stringArray.length; nIndex++ )
	    {
	         intArray[nIndex] = Integer.parseInt( stringArray[nIndex] );
	    }
	    
	    return intArray;
	}
	
	/**
	 * 
	 * buildRedirectResult
	 * @param strUrl url to redirect
	 * @return {@link IPluginActionResult}
	 */
	public static IPluginActionResult buildRedirectResult( String strUrl )
	{
	    IPluginActionResult result = new DefaultPluginActionResult( );
	    
	    result.setRedirect( strUrl );
	    
	    return result;
	}

    /**
     * Get the current operating system, here if it's windows
     * @return boolean windows or not
     */
    public static boolean isWindows( )
    {
        String os = System.getProperty( "os.name" ).toLowerCase( );
        return ( os.indexOf( "win" ) >= 0 );

    }

    /**
     * Get the current operating system, here if it's windows
     * @return boolean windows or not
     */
    public static boolean isMac( )
    {
        String os = System.getProperty( "os.name" ).toLowerCase( );
        return ( os.indexOf( "mac" ) >= 0 );

    }

    /**
     * Get the current operating system, here if it's unix
     * @return boolean unix or not
     */
    public static boolean isUnix( )
    {
        String os = System.getProperty( "os.name" ).toLowerCase( );
        return ( os.indexOf( "nix" ) >= 0 || os.indexOf( "nux" ) >= 0 || os.indexOf( "aix" ) > 0 );

    }

    /**
     * Get the current operating system, here if it's solaris
     * @return boolean solaris or not
     */
    public static boolean isSolaris( )
    {
        String os = System.getProperty( "os.name" ).toLowerCase( );
        return ( os.indexOf( "sunos" ) >= 0 );

    }

    /**
     * Replace "DEVE" unit id by the SEJ unit id
     * @param listeUnits the liste of units
     */
    public static void changeUnitDEVEIntoSEJ( ReferenceList listeUnits )
    {
        for ( ReferenceItem item : listeUnits )
        {
            if ( item.getCode( ).equals( SignalementConstants.UNIT_DEVE ) )
            {
                item.setCode( AppPropertiesService.getProperty( SignalementConstants.UNIT_ATELIER_JARDINAGE ) );
            }
        }
    }
    
    /**
     * Checks wether the prefix belongs to signalement type
     * @param prefixe
     * @return
     */
    public static boolean isTypeSignalement(String prefixe){
    	return SignalementConstants.SIGNALEMENT_PREFIXES.contains(prefixe);
    }

}
