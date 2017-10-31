package fr.paris.lutece.plugins.dansmarue.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.UnitNode;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class UnitUtils {
	

    private static IUnitService _unitService = SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );
	
	public static void buildTree(UnitNode startNode){
		List<Unit> units = _unitService.getSubUnits(startNode.getUnit().getIdUnit(), false);
		if(CollectionUtils.isNotEmpty(units)){
			List<UnitNode> unitNodes = new ArrayList<UnitNode>();
			for(Unit unit : units){
				UnitNode unitNode = new UnitNode(unit);
				buildTree(unitNode);
				unitNodes.add(unitNode);
			}
			startNode.setSubUnits(unitNodes);
		}
	}
	
	public static void buildTree(UnitNode startNode,int depth){
		List<Unit> units = _unitService.getSubUnits(startNode.getUnit().getIdUnit(), false);
		if(CollectionUtils.isNotEmpty(units) && depth > 0){
			List<UnitNode> unitNodes = new ArrayList<UnitNode>();
			for(Unit unit : units){
				UnitNode unitNode = new UnitNode(unit);
				buildTree(unitNode,depth-1);
				unitNodes.add(unitNode);
			}
			startNode.setSubUnits(unitNodes);
		}
	}
	
	public static void populateSubUnitsSet(Unit startNode,Set<Unit> units){
		List<Unit> subUnits = _unitService.getSubUnits(startNode.getIdUnit(),false);
		units.addAll(subUnits);
		for(Unit subUnit:subUnits ){
			populateSubUnitsSet(subUnit,units);
		}
	}
}
