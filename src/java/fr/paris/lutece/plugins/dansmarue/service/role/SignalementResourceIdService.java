package fr.paris.lutece.plugins.dansmarue.service.role;

// Start of user code for imports

import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.util.ReferenceList;

import java.util.Locale;


// End of user code for imports

/**
 * SignalementResourceIdService
 */
public class SignalementResourceIdService extends ResourceIdService
{
    public static final String KEY_ID_RESOURCE = "GESTION_DES_SIGNALEMENTS";
    public static final String PERMISSION_GESTION_REFERENTIEL = "GESTION_REFERENTIEL";
    public static final String PERMISSION_RECHERCHER_SIGNALEMENT = "RECHERCHER_SIGNALEMENT";
    public static final String PERMISSION_CREATION_SIGNALEMENT = "CREATION_SIGNALEMENT";
    public static final String PERMISSION_MODIFICATION_SIGNALEMENT = "MODIFICATION_SIGNALEMENT";
    public static final String PERMISSION_CONSULTATION_SIGNALEMENT = "CONSULTATION_SIGNALEMENT";
    public static final String PERMISSION_ENVOI_MAIL_SIGNALEMENT = "ENVOI_MAIL_SIGNALEMENT";
    public static final String PERMISSION_SUPPRIMER_SIGNALEMENT = "SUPPRESSION_SIGNALEMENT";
    public static final String EXPORTER_SIGNALEMENT = "EXPORTER_SIGNALEMENT";
    public static final String SUPPRIMER_SIGNALEMENT_MASSE = "SUPPRIMER_SIGNALEMENT_MASSE";
    public static final String TRAITEMENT_MASSE = "TRAITEMENT_MASSE";

    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "dansmarue.rbac.resourceType.label.gestionDesSignalements";

    // Start of user code for specific constants

    // End of user code for specific constants
    /**
     * Cr�� une nouvelle instance de SignalementResourceIdService
     */
    public SignalementResourceIdService( )
    {
        setPluginName( "signalement" );
    }

    /**
     * Enregistre les ressources Lutece
     */
    public void register( )
    {
        ResourceType rt = new ResourceType( );
        rt.setResourceIdServiceClass( SignalementResourceIdService.class.getName( ) );
        rt.setPluginName( "signalement" );
        rt.setResourceTypeKey( KEY_ID_RESOURCE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = null;

        p = new Permission( );
        p.setPermissionKey( PERMISSION_GESTION_REFERENTIEL );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.gestionreferentiel" );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_RECHERCHER_SIGNALEMENT );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.recherchersignalement" );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_CREATION_SIGNALEMENT );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.creationsignalement" );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_MODIFICATION_SIGNALEMENT );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.modificationsignalement" );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_CONSULTATION_SIGNALEMENT );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.consultationsignalement" );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_SUPPRIMER_SIGNALEMENT );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.suppressionsignalement" );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_ENVOI_MAIL_SIGNALEMENT );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.envoimailsignalement" );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( EXPORTER_SIGNALEMENT );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.exportsignalement" );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( TRAITEMENT_MASSE );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.traitementmasse" );
        rt.registerPermission( p );
        
        p = new Permission( );
        p.setPermissionKey( SUPPRIMER_SIGNALEMENT_MASSE );
        p.setPermissionTitleKey( "dansmarue.rbac.permission.label.gestiondessignalements.suppressionsignalementmasse" );
        rt.registerPermission( p );
        
        ResourceTypeManager.registerResourceType( rt );
    }

    /**
     * Retourne une liste d'identifiants de resource ou null
     * @param locale La locale courante
     * @return Une liste d'id de resource
     */
    public ReferenceList getResourceIdList( Locale locale )
    {
        return null;
    }

    /**
     * retourne le titre de la resource sp�cifi�e par son ID ou null
     * @param strId L'id de la resource
     * @param locale La locale courante
     * @return Le titre de la resource sp�cifi�e
     */
    public String getTitle( String strId, Locale locale )
    {
        return null;
    }
}
