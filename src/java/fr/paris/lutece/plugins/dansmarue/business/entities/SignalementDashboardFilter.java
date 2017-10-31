package fr.paris.lutece.plugins.dansmarue.business.entities;

public class SignalementDashboardFilter {
	
	private Integer periodId;
	private Integer unitId;
	private Integer[] categoryIds;
	private Integer[] arrondissementIds;
	private DashboardPeriod dashboardPeriod;
	private Integer depth;
	
	public SignalementDashboardFilter(){
	}
	
	/**
	 * Getter for the periodId
	 * @return the periodId
	 */
	public Integer getPeriodId() {
		return periodId;
	}
	/**
	 * Setter for the periodId
	 * @param periodId the periodId to set
	 */
	public void setPeriodId(Integer periodId) {
		this.periodId = periodId;
	}
	/**
	 * Getter for the unitId
	 * @return the unitId
	 */
	public Integer getUnitId() {
		return unitId;
	}
	/**
	 * Setter for the unitId
	 * @param unitId the unitId to set
	 */
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	/**
	 * Getter for the categoryIds
	 * @return the categoryIds
	 */
	public Integer[] getCategoryIds() {
		return categoryIds;
	}
	/**
	 * Setter for the categoryIds
	 * @param categoryIds the categoryIds to set
	 */
	public void setCategoryIds(Integer[] categoryIds) {
		this.categoryIds = categoryIds;
	}
	/**
	 * Getter for the arrondissementIds
	 * @return the arrondissementIds
	 */
	public Integer[] getArrondissementIds() {
		return arrondissementIds;
	}
	/**
	 * Setter for the arrondissementIds
	 * @param arrondissementIds the arrondissementIds to set
	 */
	public void setArrondissementIds(Integer[] arrondissementIds) {
		this.arrondissementIds = arrondissementIds;
	}

	public DashboardPeriod getDashboardPeriod() {
		return dashboardPeriod;
	}

	public void setDashboardPeriod(DashboardPeriod dashboardPeriod) {
		this.dashboardPeriod = dashboardPeriod;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	
}
