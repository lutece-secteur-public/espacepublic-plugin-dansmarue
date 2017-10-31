package fr.paris.lutece.plugins.dansmarue.business.entities;

public class DashboardPeriod {
	private Long id;
	private String libelle;
	private Integer lowerBound;
	private Integer higherBound;
	private String unit;
	private String category;
	private Integer ordre;
	
	/**
	 * Getter for the id
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * Setter for the id
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * Getter for the libelle
	 * @return the libelle
	 */
	public String getLibelle() {
		return libelle;
	}
	/**
	 * Setter for the libelle
	 * @param libelle the libelle to set
	 */
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	/**
	 * Getter for the lowerBound
	 * @return the lowerBound
	 */
	public Integer getLowerBound() {
		return lowerBound;
	}
	/**
	 * Setter for the lowerBound
	 * @param lowerBound the lowerBound to set
	 */
	public void setLowerBound(Integer lowerBound) {
		this.lowerBound = lowerBound;
	}
	/**
	 * Getter for the higherBound
	 * @return the higherBound
	 */
	public Integer getHigherBound() {
		return higherBound;
	}
	/**
	 * Setter for the higherBound
	 * @param higherBound the higherBound to set
	 */
	public void setHigherBound(Integer higherBound) {
		this.higherBound = higherBound;
	}
	/**
	 * Getter for the unit
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * Setter for the unit
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * Getter for the category
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * Setter for the category
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * Getter for the ordre
	 * @return the ordre
	 */
	public Integer getOrdre() {
		return ordre;
	}
	/**
	 * Setter for the ordre
	 * @param ordre the ordre to set
	 */
	public void setOrdre(Integer ordre) {
		this.ordre = ordre;
	}
	
}
