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

import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;
import fr.paris.lutece.plugins.dansmarue.utils.ListUtils;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

/**
 * The Class TypeSignalementResourceIdService.
 */
public class TypeSignalementResourceIdService extends ResourceIdService
{

    /** Permission view report type. */
    public static final String      PERMISSION_VIEW_TYPE_SIGNALEMENT     = "VIEW_TYPE_SIGNALEMENT";

    /** The Constant PROPERTY_LABEL_RESOURCE_TYPE. */
    private static final String     PROPERTY_LABEL_RESOURCE_TYPE         = "dansmarue.rbac.typeSignalement.resourceType.label";

    /** The Constant PROPERTY_LABEL_VIEW_TYPE_SIGNALEMENT. */
    private static final String     PROPERTY_LABEL_VIEW_TYPE_SIGNALEMENT = "dansmarue.rbac.typeSignalement.permission.viewTypeSignalement";

    /** The type signalement service. */
    private ITypeSignalementService _typeSignalementService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void register( )
    {
        ResourceType rt = new ResourceType( );
        rt.setResourceIdServiceClass( TypeSignalementResourceIdService.class.getName( ) );
        rt.setPluginName( SignalementPlugin.PLUGIN_NAME );
        rt.setResourceTypeKey( TypeSignalement.RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = null;

        p = new Permission( );
        p.setPermissionKey( PERMISSION_VIEW_TYPE_SIGNALEMENT );
        p.setPermissionTitleKey( PROPERTY_LABEL_VIEW_TYPE_SIGNALEMENT );
        rt.registerPermission( p );

        ResourceTypeManager.registerResourceType( rt );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getResourceIdList( Locale locale )
    {
        if ( _typeSignalementService == null )
        {
            _typeSignalementService = ( ITypeSignalementService ) SpringContextService.getBean( "typeSignalementService" );
        }
        return ListUtils.toReferenceList( _typeSignalementService.getAllTypeSignalement( ), "id", "formatTypeSignalement", null, false );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( String strId, Locale locale )
    {
        if ( _typeSignalementService == null )
        {
            _typeSignalementService = ( ITypeSignalementService ) SpringContextService.getBean( "typeSignalementService" );
        }
        TypeSignalement typeSignalement = _typeSignalementService.findByIdTypeSignalement( Integer.parseInt( strId ) );
        if ( typeSignalement == null )
        {
            return StringUtils.EMPTY;
        }
        return typeSignalement.getLibelle( );
    }

}
