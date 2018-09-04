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

    public static final String PERMISSIONS = "PERMISSIONS";
    public static final String PERMISSIONS_SECTEURS = "PERMISSIONS_SECTEURS";

    private static final String PERMISSION_JOKER = "*";
    
    public static final String RIGHT_SIGNALEMENT_DASHBOARD= "SIGNALEMENT_DASHBOARD";

    /** The unit service. */
    private IUnitService _unitService;

    /** The sector service. */
    private ISectorService _sectorService;

    /**
     * Requète http
     */
    private HttpServletRequest _request;
    /**
     * Liste des permissions de l'utilisateur récupéré en session
     */
    private HashMap<String, Boolean> _permissionsCache;
    /**
     * Liste des permissions sur les secteurs de l'utilisateur
     */
    private HashMap<String, Boolean> _permissionsSecteurCache;

    /**
     * Initialise les droits : récupère les listes de permissions dans la
     * session
     * @param request requète http
     */
    public void init( HttpServletRequest request )
    {
        this._request = request;
        this._unitService = (IUnitService) SpringContextService.getBean( "unittree.unitService" );
        this._sectorService = (ISectorService) SpringContextService.getBean( "unittree-dansmarue.sectorService" );
        HttpSession session = request.getSession( );
        if ( session.getAttribute( PERMISSIONS ) != null )
        {
            _permissionsCache = (HashMap<String, Boolean>) session.getAttribute( PERMISSIONS );
        }
        else
        {
            _permissionsCache = new HashMap<String, Boolean>( );
            session.setAttribute( PERMISSIONS, _permissionsCache );
            _permissionsCache.put( "**", true );
        }
        if ( session.getAttribute( PERMISSIONS_SECTEURS ) != null )
        {
            _permissionsSecteurCache = (HashMap<String, Boolean>) session.getAttribute( PERMISSIONS_SECTEURS );
        }
        else
        {
            _permissionsSecteurCache = new HashMap<String, Boolean>( );
            session.setAttribute( PERMISSIONS_SECTEURS, _permissionsSecteurCache );
            _permissionsSecteurCache.put( "**", true );
        }
    }

    /**
     * Renvoie si l'utilisateur possède la permission passée en paramètre
     * @param ressourceType type de ressource (use case)
     * @param permission clé de la permission
     * @param secteur secteur dont il faut vérifier la
     *            permission
     * @return autorisé ou pas
     */
    public boolean estAutorise( String ressourceType, String permission, String secteur )
    {
        HttpSession session = _request.getSession( );
        Map<String, Boolean> permissionsCache = (Map<String, Boolean>) session.getAttribute( PERMISSIONS );
        String clePermission = ressourceType + permission;
        Boolean autorise = estAutoriseSecteur( secteur );
        if ( autorise )
        {
            Boolean autorisePermission = permissionsCache.get( clePermission );
            if ( autorisePermission == null )
            //Pas en cache
            {
                autorisePermission = RBACService.isAuthorized( ressourceType, RBAC.WILDCARD_RESOURCES_ID, permission,
                        AdminUserService.getAdminUser( _request ) );
                permissionsCache.put( clePermission, autorisePermission );
            }
            autorise &= autorisePermission;
        }
        return autorise;
    }

    /**
     * Renvoie si l'utilisateur est autorisé à accéder à la jsp passée en
     * paramètre.
     * Utilise le mapping jsp : permission pour déterminer l'autorisation.
     * @param jsp url de la jsp
     * @return autorisé ou non
     */
    public boolean estAutoriseJsp( String jsp )
    {
        final int LONG_PREFIX_CHEMIN = 30; //Longueur jsp/admin/plugins/signalement/
        Boolean autorise = false;
        if ( jsp.equals( "#" ) || jsp.equals( PERMISSION_JOKER ) )
        {
            autorise = true;
        }
        else
        {
            if ( jsp.length( ) > LONG_PREFIX_CHEMIN )
            {
                jsp = jsp.substring( LONG_PREFIX_CHEMIN );
            }
            PermissionRessourceType permissionRessource = MappingJspPermission.MAPPING_JSP_PERMISSIONS.get( jsp );
            if ( permissionRessource != null )
            {
                autorise = estAutorise( permissionRessource.getRessourceType( ), permissionRessource.getPermission( ),
                        PERMISSION_JOKER );
            }
            else
            {
                autorise = false;
            }
        }
        return autorise;
    }

    /**
     * Vérifie que l'utilisateur est autorisé sur le secteur passé en
     * paramètre
     * @param secteur le secteur
     * @return autorisé ou non
     */
    public boolean estAutoriseSecteur( String secteur )
    {
        Boolean autorise = false;
        if ( PERMISSION_JOKER.equals( secteur ) )
        {
            autorise = true;
        }
        else
        {
            HttpSession session = _request.getSession( );
            Map<String, Boolean> permissionsSecteursCache = (Map<String, Boolean>) session
                    .getAttribute( PERMISSIONS_SECTEURS );

            // get the courant user
            AdminUser adminUser = AdminUserService.getAdminUser( this._request );
            
            Boolean autorisePermission = permissionsSecteursCache.get( secteur );
            if ( autorisePermission == null )
            //Pas en cache
            {
                // get the unit for the courant user
                List<Unit> listUnits = _unitService.getUnitsByIdUser( adminUser.getUserId( ), false );

                if ( listUnits == null || listUnits.size( ) == 0 )
                {
                    autorise = false;
                }
                else
                {
                    Integer nCourantSector = 0;
                    try
                    {
                        nCourantSector = Integer.parseInt( secteur );
                    }
                    catch ( NumberFormatException e )
                    {
                        return AdminMessageService.getMessageUrl( _request, SignalementConstants.MESSAGE_ERROR_OCCUR,
                                AdminMessage.TYPE_STOP ) != null;
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
            }
            else
            {
                autorise = autorisePermission;
            }
        }
        return autorise;
    }
    
    /**
     * Vérifie si l'utilisateur possède au moins l'une des permissions passées en paramètre
     * @param ressourceType
     * @param permissions
     * @param secteur
     * @return
     */
    public boolean hasAutorisation(String ressourceType, List<String> permissions, String secteur){
    	boolean estAutorise = false;
    	for(String permission : permissions){
    		estAutorise = estAutorise(ressourceType,permission,secteur);
    		if(estAutorise){
    			return estAutorise;
    		}
    	}
    	return estAutorise;
    }
    
    /**
     * Vérifie si l'utilisateur possède le droit passé en paramètre
     * @param right
     * 		  Le droit à vérifier
     * @return
     * 		  true si le droit est présent
     * 		  false sinon
     */
    public boolean hasRight(String right){
    	AdminUser adminUser = AdminUserService.getAdminUser( this._request );
    	Map<String,Right> rights = adminUser.getRights();
    	return rights.containsKey(right);
    }
    
}
