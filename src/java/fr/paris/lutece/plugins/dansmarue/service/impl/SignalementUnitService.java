package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementUnitDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementUnit;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementUnitService;
import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

public class SignalementUnitService implements ISignalementUnitService{

	@Inject
	private ISignalementUnitDAO signalementUnitDAO;
	
	@Override
	public Integer insert(SignalementUnit unitSignalement) {
		return signalementUnitDAO.insert(unitSignalement,PluginService.getPlugin(SignalementPlugin.PLUGIN_NAME));
	}

	@Override
	public void remove(long lId) {
		signalementUnitDAO.remove(lId);
	}

	@Override
	public SignalementUnit load(Integer lId) {
		return signalementUnitDAO.load(lId);
	}

	@Override
	public void store(SignalementUnit unitSignalement) {
		signalementUnitDAO.store(unitSignalement);
	}
	
	@Override
	public List<Integer> getVisibleUnitsIds(){
		return signalementUnitDAO.getVisibleUnitsIds();
	}
	
	@Override
	public List<Integer> getAllUnitsIds(){
		return signalementUnitDAO.getAllUnitsIds();
	}
	
	@Override
	public void saveVisibleUnits(List<Integer> visibleUnitsIds){
		signalementUnitDAO.removeAll();
		if(CollectionUtils.isNotEmpty(visibleUnitsIds)){
			for(Integer unitId:visibleUnitsIds){
				SignalementUnit signalementUnit = new SignalementUnit();
				signalementUnit.setIdUnit(unitId);
				signalementUnit.setVisible(true);
				signalementUnitDAO.insert(signalementUnit, PluginService.getPlugin(SignalementPlugin.PLUGIN_NAME));
			}
			List<Integer> allUnitsIds = signalementUnitDAO.getAllUnitsIds();
			allUnitsIds.removeAll(visibleUnitsIds);
			for(Integer unitId:allUnitsIds){
				SignalementUnit signalementUnit = new SignalementUnit();
				signalementUnit.setIdUnit(unitId);
				signalementUnit.setVisible(false);
				signalementUnitDAO.insert(signalementUnit, PluginService.getPlugin(SignalementPlugin.PLUGIN_NAME));
			}
		}
	}
	
}
