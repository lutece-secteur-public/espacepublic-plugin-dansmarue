package fr.paris.lutece.plugins.dansmarue.service.dto;

public class SignalementMapMarkerDTO {
	
	private static final String LINE_BREAK = "<br></br>";
	private Long idSignalement;
	private String tooltip;
	private Double lng;
	private Double lat;
	private String iconName;
//	private String adresse;
//	private String priorite;
//	private String dateCreation;
	
	/**
	 * @return the idSignalement
	 */
	public Long getIdSignalement() {
		return idSignalement;
	}
	/**
	 * @param idSignalement the idSignalement to set
	 */
	public void setIdSignalement(Long idSignalement) {
		this.idSignalement = idSignalement;
	}
	public String getTooltip() {
		return tooltip;
	}
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	
	public void addTooltipText(String title, String text){
		if(tooltip == null){
			tooltip = title + text;
		}else{
			tooltip += LINE_BREAK + title +  text;
		}
	}
	/**
	 * @return the lng
	 */
	public Double getLng() {
		return lng;
	}
	/**
	 * @param lng the lng to set
	 */
	public void setLng(Double lng) {
		this.lng = lng;
	}
	/**
	 * @return the lat
	 */
	public Double getLat() {
		return lat;
	}
	/**
	 * @param lat the lat to set
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}
	
	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	
}
