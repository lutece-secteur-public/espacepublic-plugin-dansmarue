package fr.paris.lutece.plugins.dansmarue.web;

import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class ReferentielJspBean extends AbstractJspBean
{
    //RIGHT
    public static final String RIGHT_MANAGE_REFERENTIEL = "REFERENTIEL_MANAGEMENT_SIGNALEMENT";

    //TEMPLATES
    private static final String TEMPLATE_MANAGE_REFERENTIEL = "admin/plugins/signalement/manage_referentiel.html";

    public String getManageReferentiel( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MANAGE_REFERENTIEL, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }
}