package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.util.List;

import javax.inject.Inject;

import fr.paris.lutece.plugins.dansmarue.business.dao.IDomaineFonctionnelDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.DomaineFonctionnel;
import fr.paris.lutece.plugins.dansmarue.service.IDomaineFonctionnelService;
import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

public class DomaineFonctionnelService implements IDomaineFonctionnelService {

	@Inject
	private IDomaineFonctionnelDAO _domaineFonctionnelDAO;
	
	@Override
	public Integer insert(DomaineFonctionnel domaineFonctionnel) {
		Plugin pluginSignalement = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
		return _domaineFonctionnelDAO.insert(domaineFonctionnel,pluginSignalement);
	}

	@Override
	public void remove(long lId) {
		_domaineFonctionnelDAO.remove(lId);
	}

	@Override
	public DomaineFonctionnel getById(Integer lId) {
		DomaineFonctionnel domaineFonctionnel = _domaineFonctionnelDAO.getById(lId);
		if(null != domaineFonctionnel){
			List<Integer> arrondissementIds = _domaineFonctionnelDAO.getArrondissementsIdsByDomaineId(lId);
			domaineFonctionnel.setArrondissementsIds(arrondissementIds);
			domaineFonctionnel.setQuartiersIds( _domaineFonctionnelDAO.getQuartiersIdsByDomaineId( domaineFonctionnel.getId() ) );
			List<Integer> typesSignalementIds = _domaineFonctionnelDAO.getTypesSignalementIdsByDomaineId(lId);
			domaineFonctionnel.setTypesSignalementIds(typesSignalementIds);
			List<Integer> unitsIds = _domaineFonctionnelDAO.getUnitsIdsByDomaineId(lId);
			domaineFonctionnel.setUnitIds(unitsIds);
		}
		return domaineFonctionnel;
	}

	@Override
	public void store(DomaineFonctionnel domaineFonctionnel) {
		_domaineFonctionnelDAO.store(domaineFonctionnel);

	}

	@Override
	public List<DomaineFonctionnel> getAllDomainesFonctionnel() {
		List<DomaineFonctionnel> domainesFonctionnelList = _domaineFonctionnelDAO.getAllDomainesFonctionnel();
		for(DomaineFonctionnel domaineFonctionnel:domainesFonctionnelList){
			List<Integer> arrondissementIds = _domaineFonctionnelDAO.getArrondissementsIdsByDomaineId(domaineFonctionnel.getId());
			domaineFonctionnel.setArrondissementsIds(arrondissementIds);
			domaineFonctionnel.setQuartiersIds( _domaineFonctionnelDAO.getQuartiersIdsByDomaineId( domaineFonctionnel.getId() ) );
			List<Integer> typesSignalementIds = _domaineFonctionnelDAO.getTypesSignalementIdsByDomaineId(domaineFonctionnel.getId());
			domaineFonctionnel.setTypesSignalementIds(typesSignalementIds);
			List<Integer> unitsIds = _domaineFonctionnelDAO.getUnitsIdsByDomaineId(domaineFonctionnel.getId());
			domaineFonctionnel.setUnitIds(unitsIds);
		}
		return domainesFonctionnelList;
	}

	@Override
	public List<Integer> getArrondissementsIdsByDomaineId(int idDomaine) {
		return _domaineFonctionnelDAO.getArrondissementsIdsByDomaineId(idDomaine);
	}

	@Override
	public List<Integer> getTypesSignalementIdsByDomaineId(int idDomaine) {
		return _domaineFonctionnelDAO.getTypesSignalementIdsByDomaineId(idDomaine);
	}

	@Override
	public List<DomaineFonctionnel> getAllDomainesFonctionnelWithData() {
		List<DomaineFonctionnel> domainesFonctionnelList = _domaineFonctionnelDAO.getAllDomainesFonctionnel();
		for(DomaineFonctionnel domaineFonctionnel:domainesFonctionnelList){
			List<Integer> arrondissementIds = _domaineFonctionnelDAO.getArrondissementsIdsByDomaineId(domaineFonctionnel.getId());
			domaineFonctionnel.setArrondissementsIds(arrondissementIds);
			domaineFonctionnel.setQuartiersIds( _domaineFonctionnelDAO.getQuartiersIdsByDomaineId( domaineFonctionnel.getId() ) );
			List<Integer> typesSignalementIds = _domaineFonctionnelDAO.getTypesSignalementIdsByDomaineId(domaineFonctionnel.getId());
			domaineFonctionnel.setTypesSignalementIds(typesSignalementIds);
			List<Integer> unitsIds = _domaineFonctionnelDAO.getUnitsIdsByDomaineId(domaineFonctionnel.getId());
			domaineFonctionnel.setUnitIds(unitsIds);
		}
		return domainesFonctionnelList;
	}
	
	@Override
	public List<DomaineFonctionnel> getAllDomainesFonctionnelActifs(){
		List<DomaineFonctionnel> domainesFonctionnelList = _domaineFonctionnelDAO.getAllDomainesFonctionnelActifs();
		for(DomaineFonctionnel domaineFonctionnel:domainesFonctionnelList){
			List<Integer> arrondissementIds = _domaineFonctionnelDAO.getArrondissementsIdsByDomaineId(domaineFonctionnel.getId());
			domaineFonctionnel.setArrondissementsIds(arrondissementIds);
			domaineFonctionnel.setQuartiersIds( _domaineFonctionnelDAO.getQuartiersIdsByDomaineId( domaineFonctionnel.getId() ) );
			List<Integer> typesSignalementIds = _domaineFonctionnelDAO.getTypesSignalementIdsByDomaineId(domaineFonctionnel.getId());
			domaineFonctionnel.setTypesSignalementIds(typesSignalementIds);
			List<Integer> unitsIds = _domaineFonctionnelDAO.getUnitsIdsByDomaineId(domaineFonctionnel.getId());
			domaineFonctionnel.setUnitIds(unitsIds);
		}
		return domainesFonctionnelList;
	}
	
}
