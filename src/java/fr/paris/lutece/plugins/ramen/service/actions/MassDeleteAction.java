package fr.paris.lutece.plugins.ramen.service.actions;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.pluginaction.AbstractPluginAction;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DeleteAction
 */
public class MassDeleteAction extends AbstractPluginAction<DossierFields> implements IRamenAction
{
    // ACTIONS
    private static final String ACTION_NAME = "Mass Delete Actions";

    // JSP
    private static final String JSP_ACTION_MASS_DELETE = "jsp/admin/plugins/ramen/MassDeleteDossier.jsp";

    // PARAMETERS
    /** the button is an image so the name is .x or .y */
    private static final String PARAMETER_BUTTON_MASS_DELETE_ACTION_X = "mass_delete";

    public void fillModel( HttpServletRequest request, AdminUser adminUser, Map<String, Object> model )
    {
        // nothing
    }

    public boolean isInvoked( HttpServletRequest request )
    {
        return request.getParameter( PARAMETER_BUTTON_MASS_DELETE_ACTION_X ) != null;
    }

    public IPluginActionResult process( HttpServletRequest request, HttpServletResponse response, AdminUser adminUser, DossierFields sessionFields )
            throws AccessDeniedException
    {
        IPluginActionResult actionResult = new DefaultPluginActionResult(  );
        actionResult.setRedirect( AppPathService.getBaseUrl( request ) + JSP_ACTION_MASS_DELETE );
        return actionResult;
    }

    public String getButtonTemplate( )
    {
        return null;
    }

    public String getName( )
    {
        return ACTION_NAME;
    }
}
