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
package fr.paris.lutece.plugins.dansmarue.service.actions;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.pluginaction.AbstractPluginAction;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;

/**
 * The Class MassDeleteAction.
 */
public class MassDeleteAction extends AbstractPluginAction<SignalementFields> implements ISignalementAction
{

    /** The Constant ACTION_NAME. */
    // ACTIONS
    private static final String ACTION_NAME = "Mass Delete Actions";

    /** The Constant JSP_ACTION_MASS_DELETE. */
    // JSP
    private static final String JSP_ACTION_MASS_DELETE = "jsp/admin/plugins/signalement/MassDeleteSignalement.jsp";

    // PARAMETERS
    /** the button is an image so the name is .x or .y */
    private static final String PARAMETER_BUTTON_MASS_DELETE_ACTION_X = "mass_delete";

    /**
     * Fill the model.
     *
     * @param request
     *            the HttpServletRequest
     * @param adminUser
     *            the user
     * @param model
     *            the model
     */
    @Override
    public void fillModel( HttpServletRequest request, AdminUser adminUser, Map<String, Object> model )
    {
        // nothing
    }

    /**
     * Returns if the action is triggered.
     *
     * @param request
     *            the HttpServletRequest
     * @return if the button was clicked
     */
    @Override
    public boolean isInvoked( HttpServletRequest request )
    {
        return request.getParameter( PARAMETER_BUTTON_MASS_DELETE_ACTION_X ) != null;
    }

    /**
     * Process the mass delete action.
     *
     * @param request
     *            the HttpServletRequest
     * @param response
     *            the HttpServletResponse
     * @param adminUser
     *            the user
     * @param sessionFields
     *            the report session fields
     * @return The action result
     * @throws AccessDeniedException
     *             Throws an accessDeniedException
     */
    @Override
    public IPluginActionResult process( HttpServletRequest request, HttpServletResponse response, AdminUser adminUser, SignalementFields sessionFields )
            throws AccessDeniedException
    {
        IPluginActionResult actionResult = new DefaultPluginActionResult( );
        actionResult.setRedirect( AppPathService.getBaseUrl( request ) + JSP_ACTION_MASS_DELETE );
        return actionResult;
    }

    /**
     * Get the mass delete button template.
     *
     * @return null
     */
    @Override
    public String getButtonTemplate( )
    {
        return null;
    }

    /**
     * Get the action name.
     *
     * @return action name
     */
    @Override
    public String getName( )
    {
        return ACTION_NAME;
    }
}
