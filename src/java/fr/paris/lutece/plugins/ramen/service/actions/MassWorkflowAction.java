package fr.paris.lutece.plugins.ramen.service.actions;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.web.pluginaction.AbstractPluginAction;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MassWorkflowAction extends AbstractPluginAction<DossierFields> implements IRamenAction
{

	public boolean isInvoked(HttpServletRequest request) 
	{
		return false;
	}

	public IPluginActionResult process(HttpServletRequest request,
			HttpServletResponse response, AdminUser adminUser,
			DossierFields sessionFields) throws AccessDeniedException {
		return null;
	}

	public void fillModel(HttpServletRequest request, AdminUser adminUser,
			Map<String, Object> model) {
	}

	public String getButtonTemplate() {
		return null;
	}

	public String getName() {
		return null;
	}

}
