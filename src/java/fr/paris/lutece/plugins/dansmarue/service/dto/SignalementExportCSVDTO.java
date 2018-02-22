package fr.paris.lutece.plugins.dansmarue.service.dto;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class SignalementExportCSVDTO
{
    private String _strPriorite;
    private String _strTypeSignalement;
    private String _strAlias;
    private String _strAliasMobile;
    private String _strDirection;
    private String _strAdresse;
    private Double _dCoordX;
    private Double _dCoordY;
    private String _strArrondissement;
    private String _strSecteur;
    private String _strDateCreation;
    private String _strHeureCreation;
    private String _strEtat;
    private String _strMailUsager;
    private String _strCommentaireUsager;
    private Integer _nNbPhotos;
    private List<String> _strRaisonsRejet;
    private String _strNumeroSignalement;
    private String _strNbSuivis;
    private String _strNbFelicitations;
    private String[] _tabAllDatas;


    public String[] getTabAllDatas( )
    {
        return new String[] { getNumeroSignalement( ), getPriorite( ), getTypeSignalement( ), getAlias(), getAliasMobile(), getDirection( ),
                getAdresse( ), Double.toString(getCoordX()), Double.toString(getCoordY()), getArrondissement(), getSecteur(), getDateCreation( ),
                getHeureCreation(), getEtat( ), getMailUsager(), getCommentaireUsager(), Integer.toString(getNbPhotos()),
                getFormatObservationRejet(), getNbSuivis(), getNbFelicitations()};
    }

    public String getPriorite( )
    {
        return _strPriorite;
    }

    public void setPriorite( String priorite )
    {
        this._strPriorite = priorite;
    }

    public String getTypeSignalement( )
    {
        return _strTypeSignalement;
    }

    public void setTypeSignalement( String typeSignalement )
    {
        this._strTypeSignalement = typeSignalement;
    }

    public String getDirection( )
    {
        return _strDirection;
    }

    public void setDirection( String direction )
    {
        this._strDirection = direction;
    }

    public String getAdresse( )
    {
        return _strAdresse;
    }

    public void setAdresse( String adresse )
    {
        this._strAdresse = adresse;
    }

    public String getDateCreation( )
    {
        return _strDateCreation;
    }

    public void setDateCreation( String dateCreation )
    {
        this._strDateCreation = dateCreation;
    }

    public String getEtat( )
    {
        return _strEtat;
    }

    public void setEtat( String etat )
    {
        this._strEtat = etat;
    }

    public String getNumeroSignalement( )
    {
        return _strNumeroSignalement;
    }

    public void setNumeroSignalement( String _strNumeroSignalement )
    {
        this._strNumeroSignalement = _strNumeroSignalement;
    }

    public String getAlias(){
    	return this._strAlias;
    }
    
    public void setAlias(String strAlias){
    	this._strAlias = strAlias;
    }
    
    public String getAliasMobile(){
    	return this._strAliasMobile;
    }
    
    public void setAliasMobile(String strAliasMobile){
    	this._strAliasMobile = strAliasMobile;
    }
    
    public Double getCoordX(){
    	return this._dCoordX;
    }
    
    public void setCoordX(Double dCoordX){
    	this._dCoordX = dCoordX;
    }
    
    public Double getCoordY(){
    	return this._dCoordY;
    }
    
    public void setCoordY(Double dCoordY){
    	this._dCoordY = dCoordY;
    }
    
    public String getArrondissement(){
    	return this._strArrondissement;
    }
    
    public void setArrondissement(String strArrondissement){
    	this._strArrondissement = strArrondissement;
    }
    
    public String getHeureCreation(){
    	return this._strHeureCreation;
    }
    
    public void setHeureCreation(String strHeureCreation){
    	this._strHeureCreation = strHeureCreation;
    }
    
    public String getMailUsager(){
    	return this._strMailUsager;
    }
    
    public void setMailusager(String strMailUsager){
    	this._strMailUsager = strMailUsager;
    }
    
    public String getCommentaireUsager(){
    	return this._strCommentaireUsager;
    }
    
    public void setCommentaireUsager(String strCommentaireUsager){
    	this._strCommentaireUsager = strCommentaireUsager;
    }
    
    public Integer getNbPhotos(){
    	return this._nNbPhotos;
    }
    
    public void setNbPhotos(Integer nNbPhotos){
    	this._nNbPhotos = nNbPhotos;
    }
    
    public final List<String> getRaisonsRejet(){
    	return this._strRaisonsRejet;
    }
    
    public void setRaisonsRejet(final List<String> raisonsRejet){
    	this._strRaisonsRejet = raisonsRejet;
    }
    
    public String getFormatObservationRejet(){
    	return StringUtils.join(_strRaisonsRejet,",");
    }

    public String getSecteur( )
    {
        return _strSecteur;
    }

    public void setSecteur( String _strSecteur )
    {
        this._strSecteur = _strSecteur;
    }

    public String getNbSuivis( )
    {
        return _strNbSuivis;
    }

    public void setNbSuivis( String _strNbSuivis )
    {
        this._strNbSuivis = _strNbSuivis;
    }

    public String getNbFelicitations( )
    {
        return _strNbFelicitations;
    }

    public void setNbFelicitations( String _strNbFelicitations )
    {
        this._strNbFelicitations = _strNbFelicitations;
    }
    
}
