/*
 * Copyright (c) 2002-2020, City of Paris
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
package fr.paris.lutece.plugins.dansmarue.service.role;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.service.IArrondissementService;
import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;
import fr.paris.lutece.plugins.dansmarue.utils.ListUtils;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

/**
 * District resource id service.
 */
public class ArrondissementSignalementResourceIdService extends ResourceIdService
{

    /** Permission view District. */
    public static final String     PERMISSION_VIEW_ARRONDISSEMENT     = "VIEW_ARRONDISSEMENT_SIGNALEMENT";

    /** The Constant PROPERTY_LABEL_RESOURCE_TYPE. */
    private static final String    PROPERTY_LABEL_RESOURCE_TYPE       = "dansmarue.rbac.arrondissement.resourceType.label";

    /** The Constant PROPERTY_LABEL_VIEW_ARRONDISSEMENT. */
    private static final String    PROPERTY_LABEL_VIEW_ARRONDISSEMENT = "dansmarue.rbac.arrondissement.permission.viewArrondissement";

    /** The arrondissement service. */
    private IArrondissementService _arrondissementService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void register( )
    {
        ResourceType rt = new ResourceType( );
        rt.setResourceIdServiceClass( ArrondissementSignalementResourceIdService.class.getName( ) );
        rt.setPluginName( SignalementPlugin.PLUGIN_NAME );
        rt.setResourceTypeKey( Arrondissement.RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = null;

        p = new Permission( );
        p.setPermissionKey( PERMISSION_VIEW_ARRONDISSEMENT );
        p.setPermissionTitleKey( PROPERTY_LABEL_VIEW_ARRONDISSEMENT );
        rt.registerPermission( p );

        ResourceTypeManager.registerResourceType( rt );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getResourceIdList( Locale locale )
    {
        if ( _arrondissementService == null )
        {
            _arrondissementService = ( IArrondissementService ) SpringContextService.getBean( "signalement.arrondissementService" );
        }
        return ListUtils.toReferenceList( _arrondissementService.getAllArrondissement( ), "id", "numero", null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( String strId, Locale locale )
    {
        if ( _arrondissementService == null )
        {
            _arrondissementService = ( IArrondissementService ) SpringContextService.getBean( "signalement.arrondissementService" );
        }
        Arrondissement arrondissement = _arrondissementService.getByIdArrondissement( Integer.parseInt( strId ) );
        if ( arrondissement == null )
        {
            return StringUtils.EMPTY;
        }
        return arrondissement.getNumero( );
    }

}
