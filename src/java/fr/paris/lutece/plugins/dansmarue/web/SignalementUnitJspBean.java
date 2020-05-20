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
package fr.paris.lutece.plugins.dansmarue.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.UnitNode;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementUnitService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.ListUtils;
import fr.paris.lutece.plugins.dansmarue.utils.UnitUtils;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class provides the user interface to manage form features ( manage, create, modify, remove).
 */

public class SignalementUnitJspBean extends AbstractJspBean
{

    /** The Constant serialVersionUID. */
    private static final long       serialVersionUID                 = 3660192401536428781L;

    /** The Constant PARAMETER_VISIBLE_UNITS. */
    // PARAMETERS
    private static final String     PARAMETER_VISIBLE_UNITS          = "visible_units";

    /** The Constant JSP_MANAGE_SIGNALEMENT_UNIT. */
    // JSP
    private static final String     JSP_MANAGE_SIGNALEMENT_UNIT      = "jsp/admin/plugins/signalement/ManageSignalementUnit.jsp";

    /** The Constant RIGHT_MANAGE_DOMAINE_FONCTIONNEL. */
    // RIGHT
    public static final String      RIGHT_MANAGE_DOMAINE_FONCTIONNEL = "REFERENTIEL_MANAGEMENT_SIGNALEMENT";

    /** The Constant ERROR_OCCUR. */
    // CONSTANTS
    public static final String      ERROR_OCCUR                      = "error";

    /** The Constant MARK_UNIT_TREE. */
    // Markers
    private static final String     MARK_UNIT_TREE                   = "unit_tree";

    /** The Constant MARK_VISIBLE_UNITS_LIST. */
    private static final String     MARK_VISIBLE_UNITS_LIST          = "visible_units_list";

    /** The Constant TEMPLATE_MANAGE_SIGNALEMENT_UNIT. */
    // TEMPLATES
    private static final String     TEMPLATE_MANAGE_SIGNALEMENT_UNIT = "admin/plugins/signalement/manage_signalement_unit.html";

    /** The signalement unit service. */
    // SERVICES
    private transient ISignalementUnitService _signalementUnitService;

    /** The unit service. */
    private transient IUnitService            _unitService                     = SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.dansmarue.web.AbstractJspBean#init(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    @Override
    public void init( HttpServletRequest request, String strRight ) throws AccessDeniedException
    {
        super.init( request, strRight );
        _signalementUnitService = ( ISignalementUnitService ) SpringContextService.getBean( "signalementUnitService" );
    }

    /**
     * Gets the manage signalement unit.
     *
     * @return the manage signalement unit
     */
    public String getManageSignalementUnit( )
    {

        Map<String, Object> model = new HashMap<String, Object>( );
        UnitNode root = new UnitNode( _unitService.getRootUnit( false ) );
        UnitUtils.buildTree( root );
        model.put( MARK_UNIT_TREE, root );

        List<Integer> visibleUnitsIds = _signalementUnitService.getVisibleUnitsIds( );
        model.put( MARK_VISIBLE_UNITS_LIST, visibleUnitsIds );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MANAGE_SIGNALEMENT_UNIT, getLocale( ), model );
        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Save all SignalementUnit.
     *
     * @param request            The HTTP request
     * @return redirection url
     */

    public String doSaveSignalementUnit( HttpServletRequest request )
    {

        if ( StringUtils.isNotBlank( request.getParameter( "cancel" ) ) )
        {
            return doGoBack( request );
        }

        String[] visibleUnitsIds = request.getParameterValues( PARAMETER_VISIBLE_UNITS );
        List<Integer> visibleInitsIdsList = new ArrayList<Integer>( );
        if ( !ArrayUtils.isEmpty( visibleUnitsIds ) )
        {
            visibleInitsIdsList = ListUtils.getListOfIntFromStrArray( visibleUnitsIds );
        }
        _signalementUnitService.saveVisibleUnits( visibleInitsIdsList );
        return doGoBack( request );
    }

    /**
     * Return the url of the JSP which called the last action.
     *
     * @param request            The Http request
     * @return The url of the last JSP
     */
    private String doGoBack( HttpServletRequest request )
    {
        String strJspBack = request.getParameter( SignalementConstants.MARK_JSP_BACK );

        return StringUtils.isNotBlank( strJspBack ) ? ( AppPathService.getBaseUrl( request ) + strJspBack ) : AppPathService.getBaseUrl( request ) + JSP_MANAGE_SIGNALEMENT_UNIT;

    }

}
