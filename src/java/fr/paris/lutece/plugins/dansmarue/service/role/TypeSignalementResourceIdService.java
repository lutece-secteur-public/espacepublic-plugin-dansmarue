package fr.paris.lutece.plugins.dansmarue.service.role;

import fr.paris.lutece.plugins.sira.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
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
 * Type signalement resource id service
 */
public class TypeSignalementResourceIdService extends ResourceIdService
{
    /**
     * Permission view type signalement
     */
    public static final String PERMISSION_VIEW_TYPE_SIGNALEMENT = "VIEW_TYPE_SIGNALEMENT";

    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "dansmarue.rbac.typeSignalement.resourceType.label";
    private static final String PROPERTY_LABEL_VIEW_TYPE_SIGNALEMENT = "dansmarue.rbac.typeSignalement.permission.viewTypeSignalement";

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
            this._typeSignalementService = (ITypeSignalementService) SpringContextService
                    .getBean( "typeSignalementService" );
        }
        return ListUtils.toReferenceList( _typeSignalementService.getAllTypeSignalement( ), "id",
                "formatTypeSignalement", null, false );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( String strId, Locale locale )
    {
        if ( _typeSignalementService == null )
        {
            this._typeSignalementService = (ITypeSignalementService) SpringContextService
                    .getBean( "typeSignalementService" );
        }
        TypeSignalement typeSignalement = _typeSignalementService.findByIdTypeSignalement( Integer.parseInt( strId ) );
        if ( typeSignalement == null )
        {
            return StringUtils.EMPTY;
        }
        return typeSignalement.getLibelle( );
    }

}
