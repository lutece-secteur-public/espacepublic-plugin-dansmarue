package fr.paris.lutece.plugins.dansmarue.service.role;

import fr.paris.lutece.plugins.dansmarue.business.entities.DomaineFonctionnel;
import fr.paris.lutece.plugins.dansmarue.service.IDomaineFonctionnelService;
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
 * Domaine fonctionnel resource id service
 */
public class DomaineFonctionnelSignalementResourceIdService extends ResourceIdService
{
    /**
     * Permission view arrondissement
     */
    public static final String PERMISSION_CONSULT_SIGNALEMENT = "PERMISSION_VIEW_SIGNALEMENT";

    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "dansmarue.rbac.domainefonctionnel.resourceType.label";
    private static final String PROPERTY_LABEL_CONSULT_SIGNALEMENT = "dansmarue.rbac.domainefonctionnel.permission.consultsignalement";

    private IDomaineFonctionnelService _domaineFonctionnelService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void register( )
    {
        ResourceType rt = new ResourceType( );
        rt.setResourceIdServiceClass( DomaineFonctionnelSignalementResourceIdService.class.getName( ) );
        rt.setPluginName( SignalementPlugin.PLUGIN_NAME );
        rt.setResourceTypeKey( DomaineFonctionnel.RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = null;

        p = new Permission( );
        p.setPermissionKey( PERMISSION_CONSULT_SIGNALEMENT );
        p.setPermissionTitleKey( PROPERTY_LABEL_CONSULT_SIGNALEMENT );
        rt.registerPermission( p );

        ResourceTypeManager.registerResourceType( rt );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getResourceIdList( Locale locale )
    {
        if ( _domaineFonctionnelService == null )
        {
            this._domaineFonctionnelService = (IDomaineFonctionnelService) SpringContextService
                    .getBean( "domaineFonctionnelService" );
        }
        return ListUtils.toReferenceList( _domaineFonctionnelService.getAllDomainesFonctionnel( ), "id", "libelle", null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( String strId, Locale locale )
    {
        if ( _domaineFonctionnelService == null )
        {
            this._domaineFonctionnelService = (IDomaineFonctionnelService) SpringContextService
                    .getBean( "domaineFonctionnelService" );
        }
        
        DomaineFonctionnel domaineFonctionnel = _domaineFonctionnelService.getById( Integer.parseInt( strId ) );
        if ( domaineFonctionnel == null )
        {
            return StringUtils.EMPTY;
        }
        return domaineFonctionnel.getLibelle();
    }

}
