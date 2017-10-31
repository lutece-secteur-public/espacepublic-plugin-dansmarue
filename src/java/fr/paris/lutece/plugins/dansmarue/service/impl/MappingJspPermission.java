/*
 * Copyright (c) 2002-2011, Mairie de Paris
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
package fr.paris.lutece.plugins.dansmarue.service.impl;

import fr.paris.lutece.plugins.dansmarue.service.role.SignalementResourceIdService;

import java.util.HashMap;
import java.util.Map;


/**
 * Classe permettant de faire le mapping entre les jsp et leur permission
 * associ�e.
 * 
 */
public class MappingJspPermission
{

    public static final Map<String, PermissionRessourceType> MAPPING_JSP_PERMISSIONS;
    static
    {
        MAPPING_JSP_PERMISSIONS = new HashMap<String, PermissionRessourceType>( );

        MAPPING_JSP_PERMISSIONS.put( "ManageReferentiel.jsp", new PermissionRessourceType(
                SignalementResourceIdService.KEY_ID_RESOURCE,
                SignalementResourceIdService.PERMISSION_GESTION_REFERENTIEL ) );

        MAPPING_JSP_PERMISSIONS.put( "ManageSignalement.jsp", new PermissionRessourceType(
                SignalementResourceIdService.KEY_ID_RESOURCE,
                SignalementResourceIdService.PERMISSION_RECHERCHER_SIGNALEMENT ) );

        MAPPING_JSP_PERMISSIONS.put( "SaveSignalement.jsp", new PermissionRessourceType(
 SignalementResourceIdService.KEY_ID_RESOURCE,
                SignalementResourceIdService.PERMISSION_CREATION_SIGNALEMENT ) );

        MAPPING_JSP_PERMISSIONS.put( "DoSaveSignalement.jsp", new PermissionRessourceType(
 SignalementResourceIdService.KEY_ID_RESOURCE,
                SignalementResourceIdService.PERMISSION_CREATION_SIGNALEMENT ) );

        MAPPING_JSP_PERMISSIONS.put( "ModifySignalement.jsp", new PermissionRessourceType(
                SignalementResourceIdService.KEY_ID_RESOURCE,
                SignalementResourceIdService.PERMISSION_MODIFICATION_SIGNALEMENT ) );

        MAPPING_JSP_PERMISSIONS.put( "DoModifySignalement.jsp", new PermissionRessourceType(
                SignalementResourceIdService.KEY_ID_RESOURCE,
                SignalementResourceIdService.PERMISSION_MODIFICATION_SIGNALEMENT ) );

        MAPPING_JSP_PERMISSIONS.put( "ViewSignalement.jsp", new PermissionRessourceType(
                SignalementResourceIdService.KEY_ID_RESOURCE,
                SignalementResourceIdService.PERMISSION_CONSULTATION_SIGNALEMENT ) );

        MAPPING_JSP_PERMISSIONS.put( "DoCreateMailSignalement.jsp", new PermissionRessourceType(
                SignalementResourceIdService.KEY_ID_RESOURCE,
                SignalementResourceIdService.PERMISSION_ENVOI_MAIL_SIGNALEMENT ) );

        MAPPING_JSP_PERMISSIONS.put( "DeleteSignalement.jsp", new PermissionRessourceType(
                SignalementResourceIdService.KEY_ID_RESOURCE,
                SignalementResourceIdService.PERMISSION_SUPPRIMER_SIGNALEMENT ) );

        MAPPING_JSP_PERMISSIONS.put( "DoDeleteSignalement.jsp", new PermissionRessourceType(
                SignalementResourceIdService.KEY_ID_RESOURCE,
                SignalementResourceIdService.PERMISSION_SUPPRIMER_SIGNALEMENT ) );
        

    }
}
