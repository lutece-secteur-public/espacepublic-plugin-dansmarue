package fr.paris.lutece.plugins.dansmarue.service;

import java.util.List;

import fr.paris.lutece.plugins.sira.business.entities.RoadMapFilter;
import fr.paris.lutece.plugins.sira.business.entities.SignalementDossier;
import fr.paris.lutece.plugins.sira.business.entities.TypeSignalement;
import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * The Interface IDmrRoadMapService.
 */
public interface IDmrRoadMapService
{

    List<SignalementDossier> getListSignalementDossierByParam( boolean paramList, RoadMapFilter filter, Plugin plugin,
            List<TypeSignalement> typeSignalementId );

}
