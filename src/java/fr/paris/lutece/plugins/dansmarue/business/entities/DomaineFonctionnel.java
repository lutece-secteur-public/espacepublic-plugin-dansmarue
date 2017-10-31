package fr.paris.lutece.plugins.dansmarue.business.entities;

import java.util.List;

import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotBlank;

import fr.paris.lutece.portal.service.rbac.RBACResource;
import fr.paris.lutece.util.ReferenceList;

// TODO: Auto-generated Javadoc
/**
 * The Class DomaineFonctionnel.
 */
public class DomaineFonctionnel implements RBACResource {

	/** The Constant RESOURCE_TYPE. */
	public static final String RESOURCE_TYPE = "SIGNALEMENT_DOMAINE_FONCTIONNEL";
	
	/** The id. */
	private Integer id;
	
	/** The libelle. */
	@NotBlank
	private String libelle;
	
	/** The actif. */
	private boolean actif;
	
	/** The arrondissements ids. */
	private List<Integer> arrondissementsIds;
	
	/** The quartiers ids. */
	private List<Integer> quartiersIds;
	
	/** The types signalement ids. */
	private List<Integer> typesSignalementIds;
	
	/** The directions ref list. */
	@Transient
	private ReferenceList directionsRefList;
	
	/** The arrondissements ref list. */
	@Transient
	private ReferenceList arrondissementsRefList;
	
	/** The quartier ref list. */
	@Transient
    private ReferenceList quartierRefList;
	
	/** The secteurs ref list. */
	@Transient
	private ReferenceList secteursRefList;
	
	/** The categories ref list. */
	@Transient
	private ReferenceList categoriesRefList;
	
	/** The types anomalie ref list. */
	@Transient
	private ReferenceList typesAnomalieRefList;
	
	/**  ENTITES du unittree*. */
	private List<Integer> unitIds;
	
	/**
	 * Getter for the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Setter for the id.
	 *
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Getter for the libelle.
	 *
	 * @return the libelle
	 */
	public String getLibelle() {
		return libelle;
	}

	/**
	 * Setter for the libelle.
	 *
	 * @param libelle the libelle to set
	 */
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	/**
	 * Getter for the actif.
	 *
	 * @return the actif
	 */
	public boolean isActif() {
		return actif;
	}

	/**
	 * Setter for the actif.
	 *
	 * @param actif the actif to set
	 */
	public void setActif(boolean actif) {
		this.actif = actif;
	}

	/* (non-Javadoc)
	 * @see fr.paris.lutece.portal.service.rbac.RBACResource#getResourceTypeCode()
	 */
	@Override
	public String getResourceTypeCode() {
		return RESOURCE_TYPE;
	}

	/* (non-Javadoc)
	 * @see fr.paris.lutece.portal.service.rbac.RBACResource#getResourceId()
	 */
	@Override
	public String getResourceId() {
		 return Long.toString( getId( ) );
	}

	/**
	 * Getter for the arrondissementsIds.
	 *
	 * @return the arrondissementsIds
	 */
	public List<Integer> getArrondissementsIds() {
		return arrondissementsIds;
	}

	/**
	 * Setter for the arrondissementsIds.
	 *
	 * @param arrondissementsIds the arrondissementsIds to set
	 */
	public void setArrondissementsIds(List<Integer> arrondissementsIds) {
		this.arrondissementsIds = arrondissementsIds;
	}

	/**
	 * Getter for the typesSignalementIds.
	 *
	 * @return the typesSignalementIds
	 */
	public List<Integer> getTypesSignalementIds() {
		return typesSignalementIds;
	}

	/**
	 * Setter for the typesSignalementIds.
	 *
	 * @param typesSignalementIds the typesSignalementIds to set
	 */
	public void setTypesSignalementIds(List<Integer> typesSignalementIds) {
		this.typesSignalementIds = typesSignalementIds;
	}

	/**
	 * Getter for the unitIds.
	 *
	 * @return the unitIds
	 */
	public List<Integer> getUnitIds() {
		return unitIds;
	}

	/**
	 * Setter for the unitIds.
	 *
	 * @param unitIds the unitIds to set
	 */
	public void setUnitIds(List<Integer> unitIds) {
		this.unitIds = unitIds;
	}

	/**
	 * Getter for the directionsRefList.
	 *
	 * @return the directionsRefList
	 */
	public ReferenceList getDirectionsRefList() {
		return directionsRefList;
	}

	/**
	 * Setter for the directionsRefList.
	 *
	 * @param directionsRefList the directionsRefList to set
	 */
	public void setDirectionsRefList(ReferenceList directionsRefList) {
		this.directionsRefList = directionsRefList;
	}

	/**
	 * Getter for the arrondissementsRefList.
	 *
	 * @return the arrondissementsRefList
	 */
	public ReferenceList getArrondissementsRefList() {
		return arrondissementsRefList;
	}

	/**
	 * Setter for the arrondissementsRefList.
	 *
	 * @param arrondissementsRefList the arrondissementsRefList to set
	 */
	public void setArrondissementsRefList(ReferenceList arrondissementsRefList) {
		this.arrondissementsRefList = arrondissementsRefList;
	}

	/**
	 * Getter for the secteursRefList.
	 *
	 * @return the secteursRefList
	 */
	public ReferenceList getSecteursRefList() {
		return secteursRefList;
	}

	/**
	 * Setter for the secteursRefList.
	 *
	 * @param secteursRefList the secteursRefList to set
	 */
	public void setSecteursRefList(ReferenceList secteursRefList) {
		this.secteursRefList = secteursRefList;
	}

	/**
	 * Getter for the categoriesRefList.
	 *
	 * @return the categoriesRefList
	 */
	public ReferenceList getCategoriesRefList() {
		return categoriesRefList;
	}

	/**
	 * Setter for the categoriesRefList.
	 *
	 * @param categoriesRefList the categoriesRefList to set
	 */
	public void setCategoriesRefList(ReferenceList categoriesRefList) {
		this.categoriesRefList = categoriesRefList;
	}

	/**
	 * Getter for the typesAnomalieRefList.
	 *
	 * @return the typesAnomalieRefList
	 */
	public ReferenceList getTypesAnomalieRefList() {
		return typesAnomalieRefList;
	}

	/**
	 * Setter for the typesAnomalieRefList.
	 *
	 * @param typesAnomalieRefList the typesAnomalieRefList to set
	 */
	public void setTypesAnomalieRefList(ReferenceList typesAnomalieRefList) {
		this.typesAnomalieRefList = typesAnomalieRefList;
	}

    /**
     * Gets the quartiers ids.
     *
     * @return the quartiers ids
     */
    public List<Integer> getQuartiersIds( )
    {
        return quartiersIds;
    }

    /**
     * Sets the quartiers ids.
     *
     * @param quartiersIds the new quartiers ids
     */
    public void setQuartiersIds( List<Integer> quartiersIds )
    {
        this.quartiersIds = quartiersIds;
    }

    /**
     * Gets the quartier ref list.
     *
     * @return the quartier ref list
     */
    public ReferenceList getQuartierRefList( )
    {
        return quartierRefList;
    }

    /**
     * Sets the quartier ref list.
     *
     * @param quartierRefList the new quartier ref list
     */
    public void setQuartierRefList( ReferenceList quartierRefList )
    {
        this.quartierRefList = quartierRefList;
    }

}
