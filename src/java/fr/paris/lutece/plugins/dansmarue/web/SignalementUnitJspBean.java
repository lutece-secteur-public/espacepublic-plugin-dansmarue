package fr.paris.lutece.plugins.dansmarue.web;

import fr.paris.lutece.plugins.dansmarue.business.entities.UnitNode;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementUnitService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.ListUtils;
import fr.paris.lutece.plugins.dansmarue.utils.UnitUtils;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;


/**
 * This class provides the user interface to manage form features ( manage,
 * create, modify, remove)
 */

public class SignalementUnitJspBean extends AbstractJspBean
{
    
    // PARAMETERS
    private static final String PARAMETER_VISIBLE_UNITS = "visible_units";
	
    //JSP
    private static final String JSP_MANAGE_SIGNALEMENT_UNIT = "jsp/admin/plugins/signalement/ManageSignalementUnit.jsp";

    //RIGHT
    public static final String RIGHT_MANAGE_DOMAINE_FONCTIONNEL = "REFERENTIEL_MANAGEMENT_SIGNALEMENT";

    //CONSTANTS
    public static final String ERROR_OCCUR = "error";

    //Markers
    private static final String MARK_UNIT_TREE = "unit_tree";
    private static final String MARK_VISIBLE_UNITS_LIST = "visible_units_list";

    //TEMPLATES
    private static final String TEMPLATE_MANAGE_SIGNALEMENT_UNIT = "admin/plugins/signalement/manage_signalement_unit.html";

    // SERVICES
    private ISignalementUnitService _signalementUnitService;
    private IUnitService _unitService = SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );

    @Override
    public void init( HttpServletRequest request, String strRight ) throws AccessDeniedException
    {
        super.init( request, strRight );
        _signalementUnitService = (ISignalementUnitService) SpringContextService.getBean( "signalementUnitService" );
    }

    public String getManageSignalementUnit( HttpServletRequest request )
    {

    	Map<String,Object> model = new HashMap<String,Object>();
        UnitNode root = new UnitNode(_unitService.getRootUnit(false));
        UnitUtils.buildTree(root);
        model.put(MARK_UNIT_TREE, root);
        
        List<Integer> visibleUnitsIds = _signalementUnitService.getVisibleUnitsIds();
        model.put(MARK_VISIBLE_UNITS_LIST, visibleUnitsIds);
        
        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MANAGE_SIGNALEMENT_UNIT, getLocale( ), model );
        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Save all SignalementUnit
     * @param request The HTTP request
     * @return redirection url
     */

    public String doSaveSignalementUnit( HttpServletRequest request )
    {
    	
    	if ( StringUtils.isNotBlank( request.getParameter( "cancel" ) ) )
        {
            return doGoBack( request );
        }
    	
    	String[] visibleUnitsIds = request.getParameterValues(PARAMETER_VISIBLE_UNITS);
    	List<Integer> visibleInitsIdsList = new ArrayList<Integer>();
    	if(!ArrayUtils.isEmpty(visibleUnitsIds)){
    		visibleInitsIdsList = ListUtils.getListOfIntFromStrArray(visibleUnitsIds);
    	}
    	_signalementUnitService.saveVisibleUnits(visibleInitsIdsList);
    	return doGoBack( request );
    }


    /**
     * Return the url of the JSP which called the last action
     * @param request The Http request
     * @return The url of the last JSP
     */
    private String doGoBack( HttpServletRequest request )
    {
        String strJspBack = request.getParameter( SignalementConstants.MARK_JSP_BACK );

        return StringUtils.isNotBlank( strJspBack ) ? ( AppPathService.getBaseUrl( request ) + strJspBack )
                : AppPathService.getBaseUrl( request ) + JSP_MANAGE_SIGNALEMENT_UNIT;

    }

}
