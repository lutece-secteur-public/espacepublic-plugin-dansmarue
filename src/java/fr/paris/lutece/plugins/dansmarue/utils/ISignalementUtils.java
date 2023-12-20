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
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * ISignalementUtils.
 */
public interface ISignalementUtils
{

    /**
     * Gets the plugin.
     *
     * @return the plugin
     */
    Plugin getPlugin( );

    /**
     * Populate a bean, launch a {@link AppException} in case of error.
     *
     * @param <T>
     *            This is the type parameter
     * @param bean
     *            the bean
     * @param request
     *            the request
     */
    @SuppressWarnings( "unchecked" )
    <T> void populate( T bean, HttpServletRequest request );

    /**
     * getIntArray : does not catch {@link NumberFormatException}.
     *
     * @param stringArray
     *            string array
     * @return int array
     */
    int [ ] getIntArray( String [ ] stringArray );

    /**
     * buildRedirectResult.
     *
     * @param strUrl
     *            url to redirect
     * @return {@link IPluginActionResult}
     */
    IPluginActionResult buildRedirectResult( String strUrl );

    /**
     * Get the current operating system, here if it's windows.
     *
     * @return boolean windows or not
     */
    boolean isWindows( );

    /**
     * Get the current operating system, here if it's windows.
     *
     * @return boolean windows or not
     */
    boolean isMac( );

    /**
     * Get the current operating system, here if it's solaris.
     *
     * @return boolean solaris or not
     */
    boolean isSolaris( );

    /**
     * Replace "DEVE" unit id by the SEJ unit id.
     *
     * @param listeUnits
     *            the units list
     */
    void changeUnitDEVEIntoSEJ( ReferenceList listeUnits );

    /**
     * Checks wether the prefix belongs to reporting type.
     *
     * @param prefix
     *            the prefix
     * @return a boolean
     */
    boolean isTypeSignalement( String prefix );

    /**
     * Returns all the properties matching the prefix.
     *
     * @param prefix
     *            the prefix
     * @return a boolean
     */
    List<String> getProperties( String prefix );

    /**
     * Check address.
     *
     * @param address
     *            the address
     * @return is valid
     */
    boolean isValidAddress( String address );
}
