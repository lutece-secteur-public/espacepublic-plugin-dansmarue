package fr.paris.lutece.plugins.dansmarue.business.entities;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;

public class UnitNode {
	private Unit unit;
	private List<UnitNode> subUnits;
	
	public UnitNode(Unit unit){
		this.unit = unit;
		this.subUnits = new ArrayList<UnitNode>();
	}
	
	public UnitNode(Unit unit,List<UnitNode> subUnits){
		this.unit = unit;
		this.subUnits = subUnits;
	}
	
	/**
	 * @return the unit
	 */
	public Unit getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	/**
	 * @return the subUnits
	 */
	public List<UnitNode> getSubUnits() {
		return subUnits;
	}
	/**
	 * @param subUnits the subUnits to set
	 */
	public void setSubUnits(List<UnitNode> subUnits) {
		this.subUnits = subUnits;
	}
}
