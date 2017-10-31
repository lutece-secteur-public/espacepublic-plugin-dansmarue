package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import fr.paris.lutece.plugins.dansmarue.service.IDmrRoadMapService;
import fr.paris.lutece.plugins.sira.business.dao.IRoadMapDAO;
import fr.paris.lutece.plugins.sira.business.entities.RoadMapFilter;
import fr.paris.lutece.plugins.sira.business.entities.SignalementDossier;
import fr.paris.lutece.plugins.sira.business.entities.TypeSignalement;
import fr.paris.lutece.portal.service.plugin.Plugin;

public class DmrRoadMapService implements IDmrRoadMapService
{

    @Inject
    @Named( "signalement.roadMapDAO" )
    private IRoadMapDAO _roadMapDAO;

    @Override
    public List<SignalementDossier> getListSignalementDossierByParam( boolean paramList, RoadMapFilter filter, Plugin plugin,
            List<TypeSignalement> typeSignalementId )
    {
        List<SignalementDossier> lstSignalementDossier;

        if ( paramList )
        {
            lstSignalementDossier = _roadMapDAO.getListSignalementDossierByListIdSector( filter, plugin, typeSignalementId );
        } else
        {
            lstSignalementDossier = _roadMapDAO.getListSignalementDossierByIdSector( filter, plugin, typeSignalementId );
        }

        return lstSignalementDossier;
    }

}
