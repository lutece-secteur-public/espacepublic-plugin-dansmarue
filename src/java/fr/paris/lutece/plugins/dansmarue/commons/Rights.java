/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.plugins.dansmarue.commons;

import fr.paris.lutece.plugins.dansmarue.service.impl.MappingJspPermission;
import fr.paris.lutece.plugins.dansmarue.service.impl.PermissionRessourceType;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.service.sector.ISectorService;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Rights
{

    public static final String       PERMISSIONS                 = "PERMISSIONS";
    public static final String       PERMISSIONS_SECTEURS        = "PERMISSIONS_SECTEURS";

    private static final String      PERMISSION_JOKER            = "*";

    public static final String       RIGHT_SIGNALEMENT_DASHBOARD = "SIGNALEMENT_DASHBOARD";

    /** The unit service. */
    private IUnitService             _unitService;

    /** The sector service. */
    private ISectorService           _sectorService;

    /**
     * HttpRequest
     */
    private HttpServletRequest       _request;
    /**
     * List of user permissions retrieved in session
     */
    private HashMap<String, Boolean> _permissionsCache;
    /**
     * List of permissions on user's sectors
     */
    private HashMap<String, Boolean> _permissionsSecteurCache;

    /**
     * Initialize the rights: retrieve the permission lists in the session
     * 
     * @param request
     *            HttpRequest
     */
    @SuppressWarnings( "unchecked" )
    public void init( HttpServletRequest request )
    {
        this._request = request;
        this._unitService = ( IUnitService ) SpringContextService.getBean( "unittree.unitService" );
        this._sectorService = ( ISectorService ) SpringContextService.getBean( "unittree-dansmarue.sectorService" );
        HttpSession session = request.getSession( );
        if ( session.getAttribute( PERMISSIONS ) != null )
        {
            _permissionsCache = ( HashMap<String, Boolean> ) session.getAttribute( PERMISSIONS );
        } else
        {
            _permissionsCache = new HashMap<String, Boolean>( );
            session.setAttribute( PERMISSIONS, _permissionsCache );
            _permissionsCache.put( "**", true );
        }
        if ( session.getAttribute( PERMISSIONS_SECTEURS ) != null )
        {
            _permissionsSecteurCache = ( HashMap<String, Boolean> ) session.getAttribute( PERMISSIONS_SECTEURS );
        } else
        {
            _permissionsSecteurCache = new HashMap<String, Boolean>( );
            session.setAttribute( PERMISSIONS_SECTEURS, _permissionsSecteurCache );
            _permissionsSecteurCache.put( "**", true );
        }
    }

    /**
     * Returns if the user has the permission passed in parameter
     * 
     * @param ressourceType
     *            the resource type
     * @param permission
     *            permission key
     * @param secteur
     *            sector which needs a permission
     * @return allow or not
     */
    public boolean estAutorise( String ressourceType, String permission, String secteur )
    {
        HttpSession session = _request.getSession( );
        @SuppressWarnings( "unchecked" )
        Map<String, Boolean> permissionsCache = ( Map<String, Boolean> ) session.getAttribute( PERMISSIONS );
        String clePermission = ressourceType + permission;
        Boolean autorise = estAutoriseSecteur( secteur );
        if ( autorise )
        {
            Boolean autorisePermission = permissionsCache.get( clePermission );
            if ( autorisePermission == null )
            {
                autorisePermission = RBACService.isAuthorized( ressourceType, RBAC.WILDCARD_RESOURCES_ID, permission, AdminUserService.getAdminUser( _request ) );
                permissionsCache.put( clePermission, autorisePermission );
            }
            autorise &= autorisePermission;
        }
        return autorise;
    }

    /**
     * Returns whether the user is allowed to access the jsp passed as a parameter. Uses jsp mapping: permission to determine authorization.
     * 
     * @param jsp
     *            jsp url
     * @return allow or not
     */
    public boolean estAutoriseJsp( String jsp )
    {
        final int LONG_PREFIX_CHEMIN = 30;
        Boolean autorise = false;
        if ( "#".equals( jsp ) || PERMISSION_JOKER.equals( jsp ) )
        {
            autorise = true;
        } else
        {
            if ( jsp.length( ) > LONG_PREFIX_CHEMIN )
            {
                jsp = jsp.substring( LONG_PREFIX_CHEMIN );
            }
            PermissionRessourceType permissionRessource = MappingJspPermission.MAPPING_JSP_PERMISSIONS.get( jsp );
            if ( permissionRessource != null )
            {
                autorise = estAutorise( permissionRessource.getRessourceType( ), permissionRessource.getPermission( ), PERMISSION_JOKER );
            } else
            {
                autorise = false;
            }
        }
        return autorise;
    }

    /**
     * Check that the user is authorized on the sector passed in parameter
     * 
     * @param secteur
     *            the sector
     * @return allow or not
     */
    public boolean estAutoriseSecteur( String secteur )
    {
        Boolean autorise = false;
        if ( PERMISSION_JOKER.equals( secteur ) )
        {
            autorise = true;
        } else
        {
            HttpSession session = _request.getSession( );
            @SuppressWarnings( "unchecked" )
            Map<String, Boolean> permissionsSecteursCache = ( Map<String, Boolean> ) session.getAttribute( PERMISSIONS_SECTEURS );

            // get the courant user
            AdminUser adminUser = AdminUserService.getAdminUser( this._request );

            Boolean autorisePermission = permissionsSecteursCache.get( secteur );
            if ( autorisePermission == null )
            // Pas en cache
            {
                // get the unit for the courant user
                List<Unit> listUnits = _unitService.getUnitsByIdUser( adminUser.getUserId( ), false );

                if ( listUnits == null || listUnits.isEmpty( ) )
                {
                    autorise = false;
                } else
                {
                    Integer nCourantSector = 0;
                    try
                    {
                        nCourantSector = Integer.parseInt( secteur );
                    } catch ( NumberFormatException e )
                    {
                        return AdminMessageService.getMessageUrl( _request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP ) != null;
                    }

                    for ( Unit userUnit : listUnits )
                    {
                        // get the sectors for this unit
                        List<Integer> listSectorsOfUnit = _sectorService.getIdsSectorByIdUnit( userUnit.getIdUnit( ) );

                        for ( Integer nIdSector : listSectorsOfUnit )
                        {
                            if ( nIdSector.equals( nCourantSector ) )
                            {
                                autorise = true;
                                permissionsSecteursCache.put( nIdSector.toString( ), autorise );
                                return autorise;
                            }
                        }
                    }
                }
            } else
            {
                autorise = autorisePermission;
            }
        }
        return autorise;
    }

    /**
     * Checks if the user has at least one of the permissions passed in parameter
     * 
     * @param ressourceType
     *            the resource type
     * @param permissions
     *            the permission
     * @param secteur
     *            the sector
     * @return allow or not
     */
    public boolean hasAutorisation( String ressourceType, List<String> permissions, String secteur )
    {
        boolean estAutorise = false;
        for ( String permission : permissions )
        {
            estAutorise = estAutorise( ressourceType, permission, secteur );
            if ( estAutorise )
            {
                return estAutorise;
            }
        }
        return estAutorise;
    }

    /**
     * Checks if the user has the right passed in parameter
     * 
     * @param right
     *            the right to check
     * @return true if has right, otherwise false
     */
    public boolean hasRight( String right )
    {
        AdminUser adminUser = AdminUserService.getAdminUser( this._request );
        Map<String, Right> rights = adminUser.getRights( );
        return rights.containsKey( right );
    }

}
