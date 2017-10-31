package fr.paris.lutece.plugins.dansmarue.service.role;

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

import java.util.Locale;

import org.apache.commons.lang.StringUtils;


/**
 * Arrondissement resource id service
 */
public class ArrondissementSignalementResourceIdService extends ResourceIdService
{
    /**
     * Permission view arrondissement
     */
    public static final String PERMISSION_VIEW_ARRONDISSEMENT = "VIEW_ARRONDISSEMENT_SIGNALEMENT";

    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "dansmarue.rbac.arrondissement.resourceType.label";
    private static final String PROPERTY_LABEL_VIEW_ARRONDISSEMENT = "dansmarue.rbac.arrondissement.permission.viewArrondissement";

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
            this._arrondissementService = (IArrondissementService) SpringContextService
                    .getBean( "signalement.arrondissementService" );
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
            this._arrondissementService = (IArrondissementService) SpringContextService
                    .getBean( "signalement.arrondissementService" );
        }
        Arrondissement arrondissement = _arrondissementService.getByIdArrondissement( Integer.parseInt( strId ) );
        if ( arrondissement == null )
        {
            return StringUtils.EMPTY;
        }
        return arrondissement.getNumero( );
    }

}
