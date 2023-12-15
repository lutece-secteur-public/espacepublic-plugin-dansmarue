package fr.paris.lutece.plugins.dansmarue.service;

import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.web.depot.IDepotComponent;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitUserAttributeService;
import fr.paris.lutece.plugins.unittree.web.unit.IUnitAttributeComponent;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/** IDepotManager */
public interface IDepotManager
{
    /**
     * Get the list of unit user depot components.
     *
     * @return a list of {@link IUnitAttributeComponent}
     */
    List<IDepotComponent> getListUnitAttributeComponents( );

    /**
     * Fill the model for the unit user attribute component.
     *
     * @param request
     *            the HTTP request
     * @param signalement
     *            the report
     * @param model
     *            the model
     * @param strMark
     *            the marker
     */
    void fillModel( HttpServletRequest request, Signalement signalement, Map<String, Object> model, String strMark );

    /**
     * Get the list of unit user attribute services.
     *
     * @return a list of {@link IUnitUserAttributeService}
     */
    List<IDepotService> getListUnitAttributeService( );

    /**
     * Do create the additional attributes of the given unit.
     *
     * @param request
     *            the HTTP request
     * @param resourceId
     *            the original resource id
     */
    void doCreate( HttpServletRequest request, int resourceId );

    /**
     * Do create the additional attributes of the given unit.
     *
     * @param request
     *            the HTTP request
     */
    void doValidate( HttpServletRequest request );
}
