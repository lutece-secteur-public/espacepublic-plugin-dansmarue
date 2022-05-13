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
package fr.paris.lutece.plugins.dansmarue.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.web.depot.IDepotComponent;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitUserAttributeService;
import fr.paris.lutece.plugins.unittree.web.unit.IUnitAttributeComponent;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * UnitAttributeManager.
 */
public final class DepotManager
{

    /**
     * Private constructor.
     */
    private DepotManager( )
    {
    }

    /**
     * Get the list of unit user depot components.
     *
     * @return a list of {@link IUnitAttributeComponent}
     */
    public static List<IDepotComponent> getListUnitAttributeComponents( )
    {
        return SpringContextService.getBeansOfType( IDepotComponent.class );
    }

    /**
     * Fill the model for the unit user attribute component.
     *
     * @param request
     *            the HTTP request
     * @param signalement
     *            the report
     * @param model
     *            the model
     * @param strMark
     *            the marker
     */
    public static void fillModel( HttpServletRequest request, Signalement signalement, Map<String, Object> model, String strMark )
    {
        for ( IDepotComponent component : getListUnitAttributeComponents( ) )
        {
            component.fillModel( request, signalement, model );
        }

        model.put( strMark, getListUnitAttributeComponents( ) );
    }

    /**
     * Get the list of unit user attribute services.
     *
     * @return a list of {@link IUnitUserAttributeService}
     */
    public static List<IDepotService> getListUnitAttributeService( )
    {
        return SpringContextService.getBeansOfType( IDepotService.class );
    }

    /**
     * Do create the additional attributes of the given unit.
     *
     * @param request
     *            the HTTP request
     * @param resourceId
     *            the original resource id
     */
    public static void doCreate( HttpServletRequest request, int resourceId )
    {
        for ( IDepotService service : getListUnitAttributeService( ) )
        {
            service.doCreate( request, resourceId );
        }
    }

    /**
     * Do create the additional attributes of the given unit.
     *
     * @param request
     *            the HTTP request
     */
    public static void doValidate( HttpServletRequest request )
    {
        for ( IDepotService service : getListUnitAttributeService( ) )
        {
            service.doValidate( request );
        }
    }
}
