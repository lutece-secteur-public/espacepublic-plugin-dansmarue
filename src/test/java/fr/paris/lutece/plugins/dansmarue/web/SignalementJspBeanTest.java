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
package fr.paris.lutece.plugins.dansmarue.web;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.service.IAdresseService;
import fr.paris.lutece.plugins.dansmarue.service.IArrondissementService;
import fr.paris.lutece.plugins.dansmarue.service.IFileMessageCreationService;
import fr.paris.lutece.plugins.dansmarue.service.IPhotoService;
import fr.paris.lutece.plugins.dansmarue.service.IPrioriteService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementWebService;
import fr.paris.lutece.plugins.dansmarue.service.ISignaleurService;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.plugins.dansmarue.service.role.SignalementViewRoleService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.service.sector.ISectorService;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;

// TODO: Auto-generated Javadoc
/**
 * The Class SignalementJspBeanTest.
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( {
        SignalementJspBean.class, SpringContextService.class, AppPropertiesService.class
} )
public class SignalementJspBeanTest
{

    /** The type signalement service. */
    @Mock
    ITypeSignalementService _typeSignalementService;

    /** The signalement service. */
    @Mock
    ISignalementService _signalementService;

    /** The adresse service. */
    @Mock
    IAdresseService _adresseService;

    /** The photo service. */
    @Mock
    IPhotoService _photoService;

    /** The signaleur service. */
    @Mock
    ISignaleurService _signaleurService;

    /** The signalement workflow service. */
    @Mock
    IWorkflowService _signalementWorkflowService;

    /** The priorite service. */
    @Mock
    IPrioriteService _prioriteService;

    /** The arrondissement service. */
    @Mock
    IArrondissementService _arrondissementService;

    /** The signalement view role service. */
    @Mock
    SignalementViewRoleService _signalementViewRoleService;

    /** The unit service. */
    @Mock
    IUnitService _unitService;

    /** The sector service. */
    @Mock
    ISectorService _sectorService;

    /** The file message creation service. */
    @Mock
    IFileMessageCreationService _fileMessageCreationService;

    /** The signalement web services. */
    @Mock
    ISignalementWebService _signalementWebServices;

    /**
     * Inits the spring context.
     */
    @Before
    public void initSpringContext( )
    {
        PowerMockito.mockStatic( SpringContextService.class );
        when( SpringContextService.getBean( "signalementService" ) ).thenReturn( _signalementService );
        when( SpringContextService.getBean( "signalement.workflowService" ) ).thenReturn( _signalementWorkflowService );
        when( SpringContextService.getBean( "signalement.arrondissementService" ) ).thenReturn( _arrondissementService );
        when( SpringContextService.getBean( "signalement.signalementViewRoleService" ) ).thenReturn( _signalementViewRoleService );
        when( SpringContextService.getBean( "typeSignalementService" ) ).thenReturn( _typeSignalementService );
        when( SpringContextService.getBean( "signaleurService" ) ).thenReturn( _signaleurService );
        when( SpringContextService.getBean( "prioriteService" ) ).thenReturn( _prioriteService );
        when( SpringContextService.getBean( "unittree.unitService" ) ).thenReturn( _unitService );
        when( SpringContextService.getBean( "unittree-dansmarue.sectorService" ) ).thenReturn( _sectorService );
        when( SpringContextService.getBean( "adresseSignalementService" ) ).thenReturn( _adresseService );
        when( SpringContextService.getBean( "photoService" ) ).thenReturn( _photoService );
        when( SpringContextService.getBean( "fileMessageCreationService" ) ).thenReturn( _fileMessageCreationService );
        when( SpringContextService.getBean( "signalement.signalementWebService" ) ).thenReturn( _signalementWebServices );
    }

    /**
     * Inits the app properties.
     */
    @Before
    public void initAppProperties( )
    {
        PowerMockito.mockStatic( AppPropertiesService.class );
        when( AppPropertiesService.getPropertyInt( SignalementConstants.PROPERTY_DEFAULT_ITEM_PER_PAGE, 50 ) ).thenReturn( 50 );
    }

    /**
     * Parse HttpRequest, get data and save (create or update) signalement.
     *
     * @return the data and save test
     * @throws Exception
     *             the exception
     */
    @Test( expected = NullPointerException.class )
    public void getDataAndSaveTest( ) throws Exception
    {
        initAppProperties( );
        Signalement signalement = new Signalement( );
        MultipartHttpServletRequest request = mock( MultipartHttpServletRequest.class );
        when( request.getParameter( "" ) ).thenReturn( "" );

        SignalementJspBean signalementJspBean = new SignalementJspBean( );
        // PowerMockito.when( signalementJspBean, "initServices").thenCallRealMethod( );

        signalementJspBean.initServices( );
        signalementJspBean.getSignalementDataAndSave( request, signalement );

        // fail while mandatory fields aren't mocked
        // fail because AppPropertiesService.getPropertyInt(SignalementConstants.PROPERTY_DEFAULT_ITEM_PER_PAGE, 50 ); isn't sub
    }
}
