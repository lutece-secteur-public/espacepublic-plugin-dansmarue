package fr.paris.lutece.plugins.dansmarue.service.dto;

import java.time.LocalDate;

public class DashboardSignalementDTO {
	private Integer idSignalement;
	private LocalDate creationDate;
	private Integer idStatus;
	private LocalDate datePrevueTraitement;
	private LocalDate dateMiseEnSurveillance;
	
	public Integer getIdSignalement() {
		return idSignalement;
	}
	public void setIdSignalement(Integer idSignalement) {
		this.idSignalement = idSignalement;
	}
	public LocalDate getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}
	public Integer getIdStatus() {
		return idStatus;
	}
	public void setIdStatus(Integer idStatus) {
		this.idStatus = idStatus;
	}
	public LocalDate getDatePrevueTraitement() {
		return datePrevueTraitement;
	}
	public void setDatePrevueTraitement(LocalDate datePrevueTraitement) {
		this.datePrevueTraitement = datePrevueTraitement;
	}
	
    public LocalDate getDateMiseEnSurveillance( )
    {
        return dateMiseEnSurveillance;
    }
    public void setDateMiseEnSurveillance( LocalDate dateMiseEnSurveillance )
    {
        this.dateMiseEnSurveillance = dateMiseEnSurveillance;
    }
	
	
}
