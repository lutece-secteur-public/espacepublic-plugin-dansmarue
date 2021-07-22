/*
 * Copyright (c) 2002-2021, City of Paris
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

import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.util.ReferenceList;

/**
 * The Class SignalementResourceIdService.
 */
public class SignalementResourceIdService extends ResourceIdService
{

    /** The Constant KEY_ID_RESOURCE. */
    public static final String KEY_ID_RESOURCE = "GESTION_DES_SIGNALEMENTS";

    /** The Constant PERMISSION_GESTION_REFERENTIEL. */
    public static final String PERMISSION_GESTION_REFERENTIEL = "GESTION_REFERENTIEL";

    /** The Constant PERMISSION_RECHERCHER_SIGNALEMENT. */
    public static final String PERMISSION_RECHERCHER_SIGNALEMENT = "RECHERCHER_SIGNALEMENT";

    /** The Constant PERMISSION_CREATION_SIGNALEMENT. */
    public static final String PERMISSION_CREATION_SIGNALEMENT = "CREATION_SIGNALEMENT";

    /** The Constant PERMISSION_MODIFICATION_SIGNALEMENT. */
    public static final String PERMISSION_MODIFICATION_SIGNALEMENT = "MODIFICATION_SIGNALEMENT";

    /** The Constant PERMISSION_CONSULTATION_SIGNALEMENT. */
    public static final String PERMISSION_CONSULTATION_SIGNALEMENT = "CONSULTATION_SIGNALEMENT";

    /** The Constant PERMISSION_ENVOI_MAIL_SIGNALEMENT. */
    public static final String PERMISSION_ENVOI_MAIL_SIGNALEMENT = "ENVOI_MAIL_SIGNALEMENT";

    /** The Constant PERMISSION_SUPPRIMER_SIGNALEMENT. */
    public static final String PERMISSION_SUPPRIMER_SIGNALEMENT = "SUPPRESSION_SIGNALEMENT";

    /** The Constant EXPORTER_SIGNALEMENT. */
    public static final String EXPORTER_SIGNALEMENT = "EXPORTER_SIGNALEMENT";

    /** The Constant SUPPRIMER_SIGNALEMENT_MASSE. */
    public static final String SUPPRIMER_SIGNALEMENT_MASSE = "SUPPRIMER_SIGNALEMENT_MASSE";

    /** The Constant TRAITEMENT_MASSE. */
    public static final String TRAITEMENT_MASSE = "TRAITEMENT_MASSE";

    /** The Constant PROPERTY_LABEL_RESOURCE_TYPE. */
    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "dansmarue.rbac.resourceType.label.gestionDesSignalements";

    /**
     * Creates a new instance of ReportingResourceIdService.
     */
    public SignalementResourceIdService( )
    {
        setPluginName( "signalement" );
    }

    /**
     * Logs Lutece resources.
     */
    @Override
    public void register( )
    {
        ResourceType rt = new ResourceType( );
        rt.setResourceIdServiceClass( SignalementResourceIdService.class.getName( ) );
        rt.setPluginName( "signalement" );
        rt.setResourceTypeKey( KEY_ID_RESOURCE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = null;

        p = new Permission( );
        p.setPermissionKey( PERMISSION_GESTION_REFERENTIEL );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.gestionreferentiel" );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_RECHERCHER_SIGNALEMENT );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.recherchersignalement" );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_CREATION_SIGNALEMENT );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.creationsignalement" );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_MODIFICATION_SIGNALEMENT );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.modificationsignalement" );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_CONSULTATION_SIGNALEMENT );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.consultationsignalement" );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_SUPPRIMER_SIGNALEMENT );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.suppressionsignalement" );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_ENVOI_MAIL_SIGNALEMENT );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.envoimailsignalement" );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( EXPORTER_SIGNALEMENT );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.exportsignalement" );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( TRAITEMENT_MASSE );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.traitementmasse" );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( SUPPRIMER_SIGNALEMENT_MASSE );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.suppressionsignalementmasse" );
        rt.registerPermission( p );

        ResourceTypeManager.registerResourceType( rt );
    }

    /**
     * Returns a list of resource identifiers or null.
     *
     * @param locale
     *            The current locale
     * @return A list of resource ids
     */
    @Override
    public ReferenceList getResourceIdList( Locale locale )
    {
        return null;
    }

    /**
     * Returns the title of the resource specified by its ID or null.
     *
     * @param strId
     *            The resource id
     * @param locale
     *            The current locale
     * @return The title of the specified resource
     */
    @Override
    public String getTitle( String strId, Locale locale )
    {
        return null;
    }
}
