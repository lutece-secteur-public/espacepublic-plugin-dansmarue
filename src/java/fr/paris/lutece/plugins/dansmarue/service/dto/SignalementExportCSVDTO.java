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
    private String _strQuartier;
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
    private String _strDateCloture;
    private List<String> _strRaisonsRejet;
    private String _strNumeroSignalement;
    private String _strNbSuivis;
    private String _strNbFelicitations;
    private String[] _tabAllDatas;
    private boolean isPhotoServiceFait;
    private Integer idMailServiceFait;
    private String executeurServiceFait;
    private String dateDerniereAction;
    private String actionCourriel;


    public String[] getTabAllDatas( )
    {
        return new String[] { getNumeroSignalement( ), getPriorite( ), getTypeSignalement( ), getAlias(), getAliasMobile(), getDirection( ),
                getQuartier(), getAdresse( ), Double.toString(getCoordX()), Double.toString(getCoordY()), getArrondissement(), getSecteur(), 
                getDateCreation( ), getHeureCreation(), getEtat( ), getMailUsager(), getCommentaireUsager(), Integer.toString(getNbPhotos()),
                getDateCloture(), getFormatObservationRejet(), getNbSuivis(), getNbFelicitations(), isPhotoServiceFait( ) ? "Photo présente" : "Pas de photo", 
                getActionCourriel( ), getIdMailServiceFait( ) != null ? getIdMailServiceFait( ).toString( ) : "", getExecuteurServiceFait(),  getDateDerniereAction( ) };
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

    public void setNumeroSignalement( String strNumeroSignalement )
    {
        this._strNumeroSignalement = strNumeroSignalement;
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

    public void setSecteur( String strSecteur )
    {
        this._strSecteur = strSecteur;
    }

    public String getNbSuivis( )
    {
        return _strNbSuivis;
    }

    public void setNbSuivis( String strNbSuivis )
    {
        this._strNbSuivis = strNbSuivis;
    }

    public String getNbFelicitations( )
    {
        return _strNbFelicitations;
    }

    public void setNbFelicitations( String strNbFelicitations )
    {
        this._strNbFelicitations = strNbFelicitations;
    }

    public String getQuartier( )
    {
        return _strQuartier;
    }

    public void setQuartier( String strQuartier )
    {
        this._strQuartier = strQuartier;
    }

    /**
     * @return the _strDateCloture
     */
    public String getDateCloture( )
    {
        return _strDateCloture;
    }

    /**
     * @param _strDateCloture the _strDateCloture to set
     */
    public void setDateCloture( String strDateCloture )
    {
        this._strDateCloture = strDateCloture;
    }

    public boolean isPhotoServiceFait( )
    {
        return isPhotoServiceFait;
    }

    public void setPhotoServiceFait( boolean isPhotoServiceFait )
    {
        this.isPhotoServiceFait = isPhotoServiceFait;
    }

    public Integer getIdMailServiceFait( )
    {
        return idMailServiceFait;
    }

    public void setIdMailServiceFait( Integer idMailServiceFait )
    {
        this.idMailServiceFait = idMailServiceFait;
    }

    public String getExecuteurServiceFait( )
    {
        return executeurServiceFait;
    }

    public void setExecuteurServiceFait( String executeurServiceFait )
    {
        this.executeurServiceFait = executeurServiceFait;
    }

    public String getDateDerniereAction( )
    {
        return dateDerniereAction;
    }

    public void setDateDerniereAction( String dateDerniereAction )
    {
        this.dateDerniereAction = dateDerniereAction;
    }

    public String getActionCourriel( )
    {
        return actionCourriel;
    }

    public void setActionCourriel( String actionCourriel )
    {
        this.actionCourriel = actionCourriel;
    }
    
    
}
