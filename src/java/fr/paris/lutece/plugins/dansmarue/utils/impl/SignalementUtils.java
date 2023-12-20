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
package fr.paris.lutece.plugins.dansmarue.utils.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.utils.ISignalementUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

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

/**
 * SignalementUtils.
 */
public final class SignalementUtils implements ISignalementUtils
{

    /** The Constant OS_NAME. */
    private final String OS_NAME = "os.name";

    /**
     * {@inheritDoc}
     */
    @Override
    public Plugin getPlugin( )
    {
        return PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    @Override
    public <T> void populate( T bean, HttpServletRequest request )
    {
        try
        {
            BeanUtils.populate( bean, request.getParameterMap( ) );
        }
        catch( IllegalAccessException e )
        {
            throw new AppException( "Unable to populate bean " + e.getMessage( ), e );
        }
        catch( InvocationTargetException e )
        {
            throw new AppException( "Unable to populate bean " + e.getMessage( ), e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int [ ] getIntArray( String [ ] stringArray )
    {
        if ( stringArray == null )
        {
            return null;
        }
        int [ ] intArray = new int [ stringArray.length];

        for ( int nIndex = 0; nIndex < stringArray.length; nIndex++ )
        {
            intArray [nIndex] = Integer.parseInt( stringArray [nIndex] );
        }

        return intArray;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPluginActionResult buildRedirectResult( String strUrl )
    {
        IPluginActionResult result = new DefaultPluginActionResult( );

        result.setRedirect( strUrl );

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWindows( )
    {
        String os = System.getProperty( OS_NAME ).toLowerCase( );
        return ( os.indexOf( "win" ) >= 0 );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMac( )
    {
        String os = System.getProperty( OS_NAME ).toLowerCase( );
        return ( os.indexOf( "mac" ) >= 0 );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSolaris( )
    {
        String os = System.getProperty( OS_NAME ).toLowerCase( );
        return ( os.indexOf( "sunos" ) >= 0 );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeUnitDEVEIntoSEJ( ReferenceList listeUnits )
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
     * {@inheritDoc}
     */
    @Override
    public boolean isTypeSignalement( String prefix )
    {
        return SignalementConstants.SIGNALEMENT_PREFIXES.contains( prefix );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getProperties( String prefix )
    {
        List<String> propertiesKeys = AppPropertiesService.getKeys( prefix );
        List<String> properties = new ArrayList<String>( );
        for ( String propertyKey : propertiesKeys )
        {
            String property = AppPropertiesService.getProperty( propertyKey );
            if ( StringUtils.isNotBlank( property ) )
            {
                properties.add( property );
            }
        }
        return properties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValidAddress( String address )
    {

        if ( ( address == null ) || StringUtils.isEmpty( address.trim( ) ) )
        {
            return false;
        }
        else
            if ( !StringUtils.containsIgnoreCase( address, "PARIS" ) )
            {
                return false;
            }
            else
                if ( !address.matches( ".*75[0-9]{3}.*" ) )
                {
                    return false;
                }
                else
                {
                    return true;
                }
    }
}
