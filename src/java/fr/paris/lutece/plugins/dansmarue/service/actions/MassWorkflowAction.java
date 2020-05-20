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
package fr.paris.lutece.plugins.dansmarue.service.actions;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.web.pluginaction.AbstractPluginAction;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;

/**
 * The Class MassWorkflowAction.
 */
public class MassWorkflowAction extends AbstractPluginAction<SignalementFields> implements ISignalementAction
{

    /* (non-Javadoc)
     * @see fr.paris.lutece.portal.web.pluginaction.IPluginAction#isInvoked(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public boolean isInvoked( HttpServletRequest request )
    {
        return false;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.portal.web.pluginaction.IPluginAction#process(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, fr.paris.lutece.portal.business.user.AdminUser, java.lang.Object)
     */
    @Override
    public IPluginActionResult process( HttpServletRequest request, HttpServletResponse response, AdminUser adminUser, SignalementFields sessionFields ) throws AccessDeniedException
    {
        return null;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.portal.web.pluginaction.IPluginAction#fillModel(javax.servlet.http.HttpServletRequest, fr.paris.lutece.portal.business.user.AdminUser, java.util.Map)
     */
    @Override
    public void fillModel( HttpServletRequest request, AdminUser adminUser, Map<String, Object> model )
    {
        // nothing
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.portal.web.pluginaction.IPluginAction#getButtonTemplate()
     */
    @Override
    public String getButtonTemplate( )
    {
        return null;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.portal.web.pluginaction.IPluginAction#getName()
     */
    @Override
    public String getName( )
    {
        return null;
    }

}
