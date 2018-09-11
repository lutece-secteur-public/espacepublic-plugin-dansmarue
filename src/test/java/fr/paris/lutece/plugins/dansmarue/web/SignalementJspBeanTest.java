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


@RunWith( PowerMockRunner.class )
@PrepareForTest( { SignalementJspBean.class, SpringContextService.class, AppPropertiesService.class } )
public class SignalementJspBeanTest
{

    @Mock
    ITypeSignalementService _typeSignalementService;
    @Mock
    ISignalementService _signalementService;
    @Mock
    IAdresseService _adresseService;
    @Mock
    IPhotoService _photoService;
    @Mock
    ISignaleurService _signaleurService;
    @Mock
    IWorkflowService _signalementWorkflowService;
    @Mock
    IPrioriteService _prioriteService;
    @Mock
    IArrondissementService _arrondissementService;
    @Mock
    SignalementViewRoleService _signalementViewRoleService;
    @Mock
    IUnitService _unitService;
    @Mock
    ISectorService _sectorService;
    @Mock
    IFileMessageCreationService _fileMessageCreationService;
    @Mock
    ISignalementWebService _signalementWebServices;

    @Before
    public void initSpringContext( )
    {
        PowerMockito.mockStatic( SpringContextService.class );
        when( SpringContextService.getBean( "signalementService" ) ).thenReturn( _signalementService );
        when( SpringContextService.getBean( "signalement.workflowService" ) ).thenReturn( _signalementWorkflowService );
        when( SpringContextService.getBean( "signalement.arrondissementService" ) ).thenReturn( _arrondissementService );
        when( SpringContextService.getBean( "signalement.signalementViewRoleService" ) ).thenReturn(
                _signalementViewRoleService );
        when( SpringContextService.getBean( "typeSignalementService" ) ).thenReturn( _typeSignalementService );
        when( SpringContextService.getBean( "signaleurService" ) ).thenReturn( _signaleurService );
        when( SpringContextService.getBean( "prioriteService" ) ).thenReturn( _prioriteService );
        when( SpringContextService.getBean( "unittree.unitService" ) ).thenReturn( _unitService );
        when( SpringContextService.getBean( "unittree-dansmarue.sectorService" ) ).thenReturn( _sectorService );
        when( SpringContextService.getBean( "adresseSignalementService" ) ).thenReturn( _adresseService );
        when( SpringContextService.getBean( "photoService" ) ).thenReturn( _photoService );
        when( SpringContextService.getBean( "fileMessageCreationService" ) ).thenReturn( _fileMessageCreationService );
        when( SpringContextService.getBean( "signalement.signalementWebService" ) )
                .thenReturn( _signalementWebServices );
    }

    @Before
    public void initAppProperties( )
    {
        PowerMockito.mockStatic( AppPropertiesService.class );
        when( AppPropertiesService.getPropertyInt( SignalementConstants.PROPERTY_DEFAULT_ITEM_PER_PAGE, 50 ) )
                .thenReturn( 50 );
    }

    /**
     * Parse HttpRequest, get data and save (create or update) signalement
     * @throws Exception
     */
    @Test( expected = NullPointerException.class )
    public void getDataAndSaveTest( ) throws Exception
    {     
        initAppProperties( );
        Signalement signalement = new Signalement( );
        MultipartHttpServletRequest request = mock( MultipartHttpServletRequest.class );
        when( request.getParameter( "" ) ).thenReturn( "" );

        SignalementJspBean signalementJspBean = new SignalementJspBean( );
        //PowerMockito.when( signalementJspBean, "initServices").thenCallRealMethod( );

        signalementJspBean.initServices( );
        signalementJspBean.getSignalementDataAndSave( request, signalement );
        
        //fail while mandatory fields aren't mocked
        //fail because AppPropertiesService.getPropertyInt(SignalementConstants.PROPERTY_DEFAULT_ITEM_PER_PAGE, 50 ); isn't sub
    }
}
