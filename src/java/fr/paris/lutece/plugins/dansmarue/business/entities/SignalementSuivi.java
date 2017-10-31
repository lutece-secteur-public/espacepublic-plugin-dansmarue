package fr.paris.lutece.plugins.dansmarue.business.entities;

public class SignalementSuivi {
	private Long id;
	private Long idSignalement;
	private String userGuid;
	
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
	 * Getter for the idSignalement
	 * @return the idSignalement
	 */
	public Long getIdSignalement() {
		return idSignalement;
	}
	/**
	 * Setter for the idSignalement
	 * @param idSignalement the idSignalement to set
	 */
	public void setIdSignalement(Long idSignalement) {
		this.idSignalement = idSignalement;
	}
	/**
	 * Getter for the userGuid
	 * @return the userGuid
	 */
	public String getUserGuid() {
		return userGuid;
	}
	/**
	 * Setter for the userGuid
	 * @param userGuid the userGuid to set
	 */
	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}
	
	
}
