package fr.paris.lutece.plugins.dansmarue.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dansmarue.business.entities.RequalificationMasseFilter;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;

/**
 * The Class ManageRequalificationMasseJspBean.
 */
@Controller( controllerJsp = "ManageRequalificationMasse.jsp", controllerPath = "jsp/admin/plugins/signalement/", right = "REFERENTIEL_MANAGEMENT_SIGNALEMENT" )
public class ManageRequalificationMasseJspBean extends AbstractJspBean
{
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3445779133060187135L;

    /** The Constant RIGHT_MANAGE_REQUALIFICATION_MASSE. */
    // RIGHT
    public static final String RIGHT_MANAGE_REQUALIFICATION_MASSE = "REFERENTIEL_MANAGEMENT_SIGNALEMENT";

    /** The Constant MARK_LIST_TYPE_SIGNALEMENT. */
    private static final String MARK_LIST_TYPE_SIGNALEMENT = "list_type";

    /** The Constant MARK_REPARTITION. */
    private static final String MARK_REPARTITION = "repartition";

    /** The Constant MARK_FILTER. */
    private static final String MARK_FILTER = "filter";

    /** The Constant MARK_IS_EXECUTION_OK. */
    private static final String MARK_IS_EXECUTION_OK = "isExecutionOk";

    /** The Constant MARK_ERROR_MESSAGE. */
    private static final String MARK_ERROR_MESSAGE= "errorMessage";

    /** The Constant MARK_TYPE_CIBLE. */
    private static final String MARK_TYPE_CIBLE= "typeCible";

    /** The Constant TEMPLATE_MANAGE_SERVICE_FAIT_MASSE. */
    // TEMPLATES
    private static final String TEMPLATE_MANAGE_REQUALIFICATION_MASSE = "admin/plugins/signalement/manage_requalification_masse.html";

    /** The Constant TEMPLATE_RAPPORT_EXECUTION_SERVICE_FAIT_MASSE. */
    private static final String TEMPLATE_RAPPORT_EXECUTION_REQUALIFICATION_MASSE = "admin/plugins/signalement/rapport_execution_requalification_masse.html";

    /** The type signalement service. */
    // SERVICES
    private transient ITypeSignalementService _typeSignalementService = SpringContextService.getBean( "typeSignalementService" );

    /** The signalement service. */
    private transient ISignalementService _signalementService = SpringContextService.getBean( "signalementService" );

    /** The Constant VIEW_MANAGE_REQUALIFICATION_MASSE. */
    // Views
    private static final String VIEW_MANAGE_REQUALIFICATION_MASSE = "manageRequalificationMasse";

    /** The Constant ACTION_SEARCH. */
    // Actions
    private static final String ACTION_SEARCH = "search";

    /** The Constant ACTION_EXECUTE_REQUALIFICATION. */
    private static final String ACTION_EXECUTE_REQUALIFICATION = "executeRequalification";

    /** The requalification masse filter. */
    private RequalificationMasseFilter _requalificationMasseFilter = new RequalificationMasseFilter( );

    /** The repartition. */
    private Map<String, Integer> _repartition = new HashMap<>( );

    /**
     * Get the manage requalification masse page.
     *
     * @param request
     *            the request
     * @return the page for requalification masse
     */
    @View( value = VIEW_MANAGE_REQUALIFICATION_MASSE, defaultView = true )
    public String getManageMessageTypeSignalement( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_LIST_TYPE_SIGNALEMENT, _typeSignalementService.getListTypeSignalementActifLastLevel( ) );
        model.put( SignalementConstants.MARK_LOCALE, request.getLocale( ) );
        model.put( MARK_FILTER, _requalificationMasseFilter );

        return getAdminPage( AppTemplateService.getTemplate( TEMPLATE_MANAGE_REQUALIFICATION_MASSE, getLocale( ), model ).getHtml( ) );
    }

    /**
     * Do search.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( ACTION_SEARCH )
    public String doSearch( HttpServletRequest request )
    {
        // Remplissage du filtre
        _requalificationMasseFilter = new RequalificationMasseFilter( );
        populate( _requalificationMasseFilter, request, request.getLocale( ) );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_LIST_TYPE_SIGNALEMENT, _typeSignalementService.getListTypeSignalementActifLastLevel( ) );
        model.put( SignalementConstants.MARK_LOCALE, request.getLocale( ) );
        model.put( MARK_FILTER, _requalificationMasseFilter );

        _repartition = _signalementService.getRepartitionRequalificationMasse( _requalificationMasseFilter );

        model.put( MARK_REPARTITION, _repartition );

        return getAdminPage( AppTemplateService.getTemplate( TEMPLATE_MANAGE_REQUALIFICATION_MASSE, getLocale( ), model ).getHtml( ) );
    }

    /**
     * Do execute service fait.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( ACTION_EXECUTE_REQUALIFICATION )
    public String doExecuteRequalification( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );

        // Récupération de l'utilisateur connecté à ajouter dans l'historique
        AdminUser adminUser = AdminUserService.getAdminUser( request );
        _requalificationMasseFilter.setAdminUserAccessCode( adminUser != null ? adminUser.getAccessCode( ) : "admin" );

        // Récupération du commentaire à ajouter dans l'historique
        _requalificationMasseFilter.setCommentaire( request.getParameter( "commentaire" ) );

        // Récupération du type cible pour la requalification
        _requalificationMasseFilter.setIdTypeCible( Integer.parseInt( request.getParameter( "idTypeCible" ) ) );

        String error = _signalementService.executeRequalificationMasse( _requalificationMasseFilter );

        model.put( MARK_FILTER, _requalificationMasseFilter );
        model.put( MARK_REPARTITION, _repartition );
        model.put( MARK_TYPE_CIBLE, _typeSignalementService.getTypeSignalement( _requalificationMasseFilter.getIdTypeCible( ) ) );
        model.put( MARK_IS_EXECUTION_OK, error == null );
        model.put( MARK_ERROR_MESSAGE, error );

        return getAdminPage( AppTemplateService.getTemplate( TEMPLATE_RAPPORT_EXECUTION_REQUALIFICATION_MASSE, getLocale( ), model ).getHtml( ) );
    }
}
