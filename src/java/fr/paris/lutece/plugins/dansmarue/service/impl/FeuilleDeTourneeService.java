/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.IPhotoDAO;
import fr.paris.lutece.plugins.dansmarue.business.dao.impl.FeuilleDeTourneeDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.FeuilleDeTournee;
import fr.paris.lutece.plugins.dansmarue.business.entities.FeuilleDeTourneeFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.FeuilleDeTourneeFilterSearch;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementBean;
import fr.paris.lutece.plugins.dansmarue.commons.Order;
import fr.paris.lutece.plugins.dansmarue.service.IFeuilleDeTourneeService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementExportService;
import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.plugins.dansmarue.service.dto.SignalementExportCSVDTO;
import fr.paris.lutece.plugins.dansmarue.service.dto.SignalementMapMarkerDTO;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.StockagePhotoUtils;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.mail.FileAttachment;
import fr.paris.lutece.util.string.StringUtil;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * The Class FeuilleDeTourneeService.
 */
public class FeuilleDeTourneeService implements IFeuilleDeTourneeService
{

    /** The feuille de tournee dao. */
    @Inject
    @Named( "dansmarue.FeuilleDeTourneeDAO" )
    private FeuilleDeTourneeDAO _feuilleDeTourneeDao;

    @Inject
    private IPhotoDAO _photoDAO;

    private int _searchFdtInitSearchCreationDateNbDays = 30;
    private DateTimeFormatter _formatter = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );

    @Inject
    private ISignalementExportService _signalementExportService;
    @Inject
    private IWorkflowService _signalementWorkflowService;
    @Inject
    private IUnitService _unitService;

    private static final String COLOR_NUMBER_GREEN = "green";
    private static final String COLOR_NUMBER_RED = "red";
    private static final String KEY_PREFIX_FDT_NAME = "sitelabels.site_property.feuilledetournee.nom.prefix";

    /**
     * Load.
     *
     * @param id
     *            the id
     * @return the feuille de tournee
     */
    @Override
    public FeuilleDeTournee load( Integer id )
    {
        return _feuilleDeTourneeDao.load( id );
    }

    /**
     * Insert.
     *
     * @param feuilleDeTournee
     *            the feuille de tournee
     * @return the integer
     */
    @Override
    public Integer insert( FeuilleDeTournee feuilleDeTournee, boolean isCopy )
    {
        String prefixFDTNom = DatastoreService.getDataValue( KEY_PREFIX_FDT_NAME, StringUtils.EMPTY );
        return _feuilleDeTourneeDao.insert( feuilleDeTournee, prefixFDTNom, isCopy );
    }

    /**
     * Save filter search.
     *
     * @param creator
     *            the creator
     * @param name
     *            the name
     * @param comment
     *            the comment
     * @param jsonFiltertoSave
     *            the json filterto save
     * @param idUnit
     *            the id unit
     * @param isUpdate
     *            the is update
     */
    @Override
    public void saveFilterSearch( String creator, String name, String comment, String jsonFiltertoSave, int idUnit, boolean isUpdate )
    {
        if ( isUpdate )
        {
            _feuilleDeTourneeDao.updateSearchFilter( creator, name, comment, jsonFiltertoSave, idUnit );
        }
        else
        {
            _feuilleDeTourneeDao.insertSearchFilter( creator, name, comment, jsonFiltertoSave, idUnit );
        }

    }

    /**
     * Load search filter.
     *
     * @param idUnit
     *            the id unit
     * @return the map
     */
    @Override
    public Map<Integer, String> loadSearchFilter( int idUnit )
    {
        return _feuilleDeTourneeDao.loadSearchFilter( idUnit );
    }

    @Override
    public FeuilleDeTourneeFilterSearch findFilterSearch( String nameToSearch )
    {
        return _feuilleDeTourneeDao.findSearchFilter( nameToSearch );

    }

    @Override
    public void deleteFilterSearch( int idFilterToDelete )
    {
        _feuilleDeTourneeDao.deleteSearchFilter( idFilterToDelete );

    }

    /**
     * Load search filter by id.
     *
     * @param idToLoad
     *            the id to load
     * @return the string
     */
    @Override
    public String loadSearchFilterById( int idToLoad )
    {
        return _feuilleDeTourneeDao.loadSearchFilterById( idToLoad );
    }

    /**
     * Load signalement bean list.
     *
     * @param idFeuilleDeTournee
     *            the id feuille de tournee
     * @return the list
     */
    @Override
    public List<SignalementBean> loadSignalementBeanList( int idFeuilleDeTournee )
    {
        List<SignalementBean> listSignalementsBean = _feuilleDeTourneeDao.loadSignalementBeanList( idFeuilleDeTournee );

        // Récupération des photos
        for ( SignalementBean signalementBean : listSignalementsBean )
        {
            List<PhotoDMR> photos = _photoDAO.findWithFullPhotoBySignalementId( signalementBean.getId( ) );

            loadPhotoOnS3Server(photos);

            for ( PhotoDMR photo : photos )
            {
                if ( ( photo.getImage( ) != null ) && ( photo.getImage( ).getImage( ) != null ) )
                {
                    if ( photo.getVue( ) == 0 )
                    {
                        signalementBean.setImagePresContent( Base64.getEncoder( ).encodeToString( photo.getImage( ).getImage( ) ) );
                    }
                    else
                        if ( photo.getVue( ) == 1 )
                        {
                            signalementBean.setImageEnsembleContent( Base64.getEncoder( ).encodeToString( photo.getImage( ).getImage( ) ) );
                        }
                }
            }

        }

        return listSignalementsBean;
    }

    private void loadPhotoOnS3Server(List<PhotoDMR> photos) {

        StockagePhotoUtils stockagePhotoUtils =  SpringContextService.getBean( "signalement.stockagePhotoUtils" );

        //Load Photo on S3 server
        for(PhotoDMR photo : photos) {
            if (( photo.getImage( ) == null ) || ( photo.getImage( ).getImage( ) == null ) ) {
                ImageResource imageRessource = stockagePhotoUtils.loadPhotoOnNetAppServeur( photo.getCheminPhoto( ) );
                photo.setImage( imageRessource );
            }

            if (( photo.getImageThumbnail( ) == null ) || ( photo.getImageThumbnail( ).getImage( ) == null ) ) {
                ImageResource imageRessource = stockagePhotoUtils.loadPhotoOnNetAppServeur( photo.getCheminPhotoMiniature( ) );
                photo.setImage( imageRessource );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getDestinatairesFromParam( String destinataireString )
    {
        List<String> destinairesList = new ArrayList<>( );

        if ( ( destinataireString == null ) || "".equals( destinataireString ) )
        {
            return destinairesList;
        }
        else
            if ( !destinataireString.contains( "," ) )
            {
                destinairesList.add( destinataireString );
            }
            else
            {
                destinairesList = Stream.of( destinataireString.split( "," ) ).collect( Collectors.toList( ) );
            }

        return destinairesList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JRDataSource generateTemplateDataSource( Integer feuilleDeTourneeId, boolean exportWithMap, String base64map )
    {
        List<FeuilleDeTournee> listFeuilleDeTournee = new ArrayList<>( );

        FeuilleDeTournee feuilleDeTournee = load( feuilleDeTourneeId );
        feuilleDeTournee.setDisplayMap( String.valueOf( exportWithMap ) );
        feuilleDeTournee.setListSignalementBean( loadSignalementBeanList( feuilleDeTournee.getId( ) ) );

        if ( exportWithMap && ( base64map != null ) )
        {
            feuilleDeTournee.setCarteBase64( base64map );
            _feuilleDeTourneeDao.updateMap( feuilleDeTourneeId, base64map );
        }

        listFeuilleDeTournee.add( feuilleDeTournee );

        return new JRBeanCollectionDataSource( listFeuilleDeTournee, false );
    }

    /**
     * Generate template bytes data.
     *
     * @param dataSource
     *            the data source
     * @param pathToTemplate
     *            the path to template
     * @return the byte[]
     * @throws JRException
     *             the JR exception
     */
    @Override
    public byte [ ] generateTemplateBytesData( JRDataSource dataSource, String pathToTemplate ) throws JRException
    {
        // Pour tester le rapport dans jasperstudio -> Classe TestFactory
        JasperReport report = JasperCompileManager.compileReport( pathToTemplate );

        JasperPrint print = JasperFillManager.fillReport( report, null, dataSource );
        return JasperExportManager.exportReportToPdf( print );
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void sendFeuilleDeTourneeByMail( List<String> destinairesList, byte [ ] bytesData, String exportName )
    {
        if ( !destinairesList.isEmpty( ) )
        {
            String objet_prefix = DatastoreService.getDataValue( "sitelabels.site_property.feuilledetournee.mail.objet", "" );
            String objet = objet_prefix + " [" + exportName + "]";
            String message = DatastoreService.getDataValue( "sitelabels.site_property.feuilledetournee.mail.message", "" );

            List<FileAttachment> filesAttachement = new ArrayList<>( );
            FileAttachment fileAttachment = new FileAttachment( exportName + ".pdf", bytesData, "application/pdf" );
            filesAttachement.add( fileAttachment );

            for ( String destinaire : destinairesList )
            {
                MailService.sendMailMultipartHtml( destinaire, null, null, "Mairie de Paris", "noreply-dansmarue@paris.fr", objet, message, null,
                        filesAttachement );
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFormatEmailOK( List<String> destinairesList )
    {
        if ( destinairesList == null )
        {
            return false;
        }
        else
        {
            return destinairesList.stream( ).allMatch( StringUtil::checkEmail );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeuilleDeTourneeFilter initFdtFilter( FeuilleDeTourneeFilter feuilleDeTourneeFilter )
    {
        if ( feuilleDeTourneeFilter == null )
        {
            feuilleDeTourneeFilter = new FeuilleDeTourneeFilter( );

            LocalDate nowMinusDays = LocalDate.now( ).minusDays( _searchFdtInitSearchCreationDateNbDays );
            LocalDate now = LocalDate.now( );

            if ( ( feuilleDeTourneeFilter.getDateCreationDebut( ) == null ) || feuilleDeTourneeFilter.getDateCreationDebut( ).isEmpty( ) )
            {
                feuilleDeTourneeFilter.setDateCreationDebut( nowMinusDays.format( _formatter ) );
            }

            if ( ( feuilleDeTourneeFilter.getDateCreationFin( ) == null ) || feuilleDeTourneeFilter.getDateCreationFin( ).isEmpty( ) )
            {
                feuilleDeTourneeFilter.setDateCreationFin( now.format( _formatter ) );
            }
        }

        return feuilleDeTourneeFilter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FeuilleDeTournee> loadFdtByFilter( FeuilleDeTourneeFilter feuilleDeTourneeFilter )
    {
        return _feuilleDeTourneeDao.loadFdtByFilter( feuilleDeTourneeFilter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FeuilleDeTournee> loadFdtByFilterWithOrder( FeuilleDeTourneeFilter feuilleDeTourneeFilter, Order order )
    {
        return _feuilleDeTourneeDao.loadFdtByFilterWithOrder( feuilleDeTourneeFilter, order );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SignalementMapMarkerDTO> getSignalementsMapMarkerDTO( List<Signalement> signalementList, Locale locale )
    {
        List<SignalementMapMarkerDTO> signalementMarkers = new ArrayList<>( );

        for ( Signalement signalement : signalementList )
        {
            SignalementMapMarkerDTO sigMarker = new SignalementMapMarkerDTO( );
            sigMarker.setIdSignalement( signalement.getId( ) );
            sigMarker.setLng( signalement.getAdresses( ).get( 0 ).getLng( ) );
            sigMarker.setLat( signalement.getAdresses( ).get( 0 ).getLat( ) );
            setColorNumberMarker( signalement.getIdState( ), sigMarker );
            fillPopup( sigMarker, signalement, locale );

            signalementMarkers.add( sigMarker );
        }

        return signalementMarkers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SignalementMapMarkerDTO> getSignalementsMapMarkerDTOFromSignalementsConsultation( List<SignalementExportCSVDTO> signalements, Locale locale )
    {
        List<SignalementMapMarkerDTO> signalementMarkers = new ArrayList<>( );

        for ( SignalementExportCSVDTO signalement : signalements )
        {
            SignalementMapMarkerDTO sigMarker = new SignalementMapMarkerDTO( );
            sigMarker.setIdSignalement( Long.valueOf( signalement.getIdSignalement( ) ) );
            sigMarker.setLng( signalement.getCoordY( ) );
            sigMarker.setLat( signalement.getCoordX( ) );

            setColorNumberMarker( signalement.getIdStatut( ), sigMarker );
            fillPopupWithDTO( sigMarker, signalement, locale, false );

            signalementMarkers.add( sigMarker );
        }

        return signalementMarkers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SignalementMapMarkerDTO> getSignalementsMapMarkerDTOFromSignalementsEdition( List<SignalementExportCSVDTO> signalements, Locale locale )
    {
        List<SignalementMapMarkerDTO> signalementMarkers = new ArrayList<>( );

        for ( SignalementExportCSVDTO signalement : signalements )
        {
            SignalementMapMarkerDTO sigMarker = new SignalementMapMarkerDTO( );
            sigMarker.setIdSignalement( Long.valueOf( signalement.getIdSignalement( ) ) );
            sigMarker.setLng( signalement.getCoordY( ) );
            sigMarker.setLat( signalement.getCoordX( ) );
            setColorNumberMarker( signalement.getIdStatut( ), sigMarker );
            fillPopupWithDTO( sigMarker, signalement, locale, true );

            signalementMarkers.add( sigMarker );
        }

        return signalementMarkers;
    }

    /**
     * Set the color number in marker according to signalement state.
     *
     * @param idState
     *            signalement state.
     */
    private void setColorNumberMarker( int idState, SignalementMapMarkerDTO sigMarker )
    {

        if ( Arrays.stream( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_MARKER_STATES_GREEN ).split( "," ) )
                .anyMatch( state -> String.valueOf( idState ).equals( state ) ) )
        {
            sigMarker.setColorNumber( COLOR_NUMBER_GREEN );
        }
        else
        {
            sigMarker.setColorNumber( COLOR_NUMBER_RED );
        }
    }

    /**
     * Fill popup.
     *
     * @param sigMarker
     *            the sig marker
     * @param signalement
     *            the signalement
     * @param stateSignalement
     *            state signalement
     * @param locale
     *            the locale
     */
    private void fillPopup( SignalementMapMarkerDTO sigMarker, Signalement signalement, Locale locale )
    {
        // Numero
        String numeroTitle = I18nService.getLocalizedString( "dansmarue.map.tooltips.numero", locale );
        sigMarker.addTooltipTextWithClass( numeroTitle, signalement.getNumeroSignalement( ) );

        // Etat
        String etatTitle = I18nService.getLocalizedString( "dansmarue.map.tooltips.etat", locale );
        sigMarker.addTooltipTextWithClass( etatTitle, signalement.getStateName( ) );

        // Type
        String typeTitle = I18nService.getLocalizedString( "dansmarue.map.tooltips.type", locale );
        sigMarker.addTooltipTextWithClass( typeTitle, signalement.getType( ) );

        // Adresse
        String adresse = signalement.getAdresses( ).get( 0 ).getAdresse( );
        String adresseTitle = I18nService.getLocalizedString( "dansmarue.map.tooltips.adresse", locale );
        sigMarker.addTooltipTextWithClass( adresseTitle, adresse );

        // Lien vers l'anomalie
        String url = "<a class='tooltipMapLink' target='_blank' title='#i18n{dansmarue.map.tooltips.detail}' href='jsp/admin/plugins/signalement/ViewSignalement.jsp?signalement_id="
                + signalement.getId( ) + "'>#i18n{dansmarue.map.tooltips.detail}</a>";
        sigMarker.addTooltipText( "", url );

        // Photo
        if ( !signalement.getPhotos( ).isEmpty( ) && ( signalement.getPhotos( ).get( 0 ) != null )
                && !StringUtils.isEmpty( signalement.getPhotos( ).get( 0 ).getImageThumbnailUrl( ) ) )
        {
            String img = "<img class='tooltipMapPhoto' src='" + signalement.getPhotos( ).get( 0 ).getImageUrl( ) + "' />";
            sigMarker.addTooltipText( "", img );
        }

        // Btn action
        String actionAjout = "<span class='btnActionTooltip' id='actionAjout_" + signalement.getId( ) + "' onclick='addToSelection(" + signalement.getId( )
        + ")'><i style='color: #1DAF26;' class='fa fa-plus-circle'></i> #i18n{dansmarue.map.tooltips.ajouter}</span>";
        String actionSuppression = "<span class='btnActionTooltip' id='actionSuppression_" + signalement.getId( ) + "' onclick='removeFromSelection("
                + signalement.getId( ) + ")'><i style='color: #FF0707;' class='fa fa-minus-circle'></i> #i18n{dansmarue.map.tooltips.retirer}</span>";
        sigMarker.addTooltipText( "", actionAjout + actionSuppression );
    }

    /**
     * Fill popup with DTO.
     *
     * @param sigMarker
     *            the sig marker
     * @param signalement
     *            the signalement
     * @param stateSignalement
     *            state signalement
     * @param locale
     *            the locale
     */
    private void fillPopupWithDTO( SignalementMapMarkerDTO sigMarker, SignalementExportCSVDTO signalement, Locale locale, boolean isEdition )
    {
        // Numero
        String numeroTitle = I18nService.getLocalizedString( "dansmarue.map.tooltips.numero", locale );
        sigMarker.addTooltipTextWithClass( numeroTitle, signalement.getNumeroSignalement( ) );

        // Etat
        String etatTitle = I18nService.getLocalizedString( "dansmarue.map.tooltips.etat", locale );
        sigMarker.addTooltipTextWithClass( etatTitle, signalement.getEtat( ) );

        // Type
        String typeTitle = I18nService.getLocalizedString( "dansmarue.map.tooltips.type", locale );
        sigMarker.addTooltipTextWithClass( typeTitle, signalement.getTypeSignalement( ) );

        // Adresse
        String adresse = signalement.getAdresse( );
        String adresseTitle = I18nService.getLocalizedString( "dansmarue.map.tooltips.adresse", locale );
        sigMarker.addTooltipTextWithClass( adresseTitle, adresse );

        // Lien vers l'anomalie
        String url = "<a class='tooltipMapLink' target='_blank' title='#i18n{dansmarue.map.tooltips.detail}' href='jsp/admin/plugins/signalement/ViewSignalement.jsp?signalement_id="
                + signalement.getIdSignalement( ) + "'>#i18n{dansmarue.map.tooltips.detail}</a>";
        sigMarker.addTooltipText( "", url );

        // Photo
        if ( !signalement.getPhotos( ).isEmpty( ) && ( signalement.getPhotos( ).get( 0 ) != null )
                && !StringUtils.isEmpty( signalement.getPhotos( ).get( 0 ).getImageThumbnailUrl( ) ) )
        {
            String img = "<img class='tooltipMapPhoto' src='" + signalement.getPhotos( ).get( 0 ).getImageUrl( ) + "' />";
            sigMarker.addTooltipText( "", img );
        }

        if ( isEdition )
        {
            // Btn action
            String actionAjout = "<span class='btnActionTooltip' id='actionAjout_" + signalement.getIdSignalement( ) + "' onclick='addToSelection("
                    + signalement.getIdSignalement( )
                    + ")'><i style='color: #1DAF26;' class='fa fa-plus-circle'></i> #i18n{dansmarue.map.tooltips.ajouter}</span>";
            String actionSuppression = "<span class='btnActionTooltip' id='actionSuppression_" + signalement.getIdSignalement( )
            + "' onclick='removeFromSelection(" + signalement.getIdSignalement( )
            + ")'><i style='color: #FF0707;' class='fa fa-minus-circle'></i> #i18n{dansmarue.map.tooltips.retirer}</span>";
            sigMarker.addTooltipText( "", actionAjout + actionSuppression );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getNomsExistant( )
    {
        return _feuilleDeTourneeDao.getNomsExistant( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeuilleDeTournee findFDTByName( String nomFDT )
    {
        return _feuilleDeTourneeDao.findFDTByName( nomFDT );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update( FeuilleDeTournee feuilleDeTournee )
    {
        _feuilleDeTourneeDao.update( feuilleDeTournee );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer updateByName( FeuilleDeTournee feuilleDeTournee )
    {
        return _feuilleDeTourneeDao.updateByName( feuilleDeTournee );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nFeuilleDeTourneeId )
    {
        _feuilleDeTourneeDao.remove( nFeuilleDeTourneeId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FeuilleDeTournee> getFeuilleDeTourneeAutorise( List<FeuilleDeTournee> feuilleDeTourneeList, AdminUser connectedUser )
    {
        List<FeuilleDeTournee> feuilleDeTourneeListAutorise = new ArrayList<>( );

        for ( FeuilleDeTournee feuilleDeTournee : feuilleDeTourneeList )
        {
            int idEntite = feuilleDeTournee.getIdEntite( ) > -1 ? feuilleDeTournee.getIdEntite( ) : feuilleDeTournee.getIdDirection( );
            if ( isUnitAuthorizedToUserWithoutAdminControl( connectedUser, _unitService.getUnit( idEntite, false ) ) )
            {
                feuilleDeTourneeListAutorise.add( feuilleDeTournee );
            }
        }
        return feuilleDeTourneeListAutorise;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUnitAuthorizedToUserWithoutAdminControl( AdminUser user, Unit unit )
    {
        List<Unit> listUnitsUser = _unitService.getUnitsByIdUser( user.getUserId( ), false );

        if ( unit != null )
        {
            for ( Unit unitUser : listUnitsUser )
            {
                if ( ( unitUser.getIdUnit( ) == unit.getIdUnit( ) ) || _unitService.isParent( unitUser, unit ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUnitAuthorizedToUser( AdminUser user, Unit unit )
    {
        if ( user.isAdmin( ) )
        {
            return true;
        }

        List<Unit> listUnitsUser = _unitService.getUnitsByIdUser( user.getUserId( ), false );

        if ( unit != null )
        {
            for ( Unit unitUser : listUnitsUser )
            {
                if ( ( unitUser.getIdUnit( ) == unit.getIdUnit( ) ) || _unitService.isParent( unitUser, unit ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteOldFeuilleDeTournee( int jourDelaiSuppression )
    {
        _feuilleDeTourneeDao.deleteOldFeuilleDeTournee( jourDelaiSuppression );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FeuilleDeTournee> getAllFeuilleDeTourneeAutorise( AdminUser connectedUser )
    {
        List<FeuilleDeTournee> feuilleDeTourneeListAutorise = new ArrayList<>( );
        List<FeuilleDeTournee> feuilleDeTourneeList = _feuilleDeTourneeDao.findAll( );

        for ( FeuilleDeTournee feuilleDeTournee : feuilleDeTourneeList )
        {
            int idEntite = feuilleDeTournee.getIdEntite( ) > -1 ? feuilleDeTournee.getIdEntite( ) : feuilleDeTournee.getIdDirection( );
            if ( isUnitAuthorizedToUserWithoutAdminControl( connectedUser, _unitService.getUnit( idEntite, false ) ) )
            {
                feuilleDeTourneeListAutorise.add( feuilleDeTournee );
            }
        }
        return feuilleDeTourneeListAutorise;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNbResultOk( int totalAnomalies )
    {
        if ( !StringUtils.equals( "0", DatastoreService.getDataValue( "sitelabels.site_property.feuille.de.tournee.limite.signalement", "0" ) ) )
        {
            int nbMaxResult = Integer.parseInt( DatastoreService.getDataValue( "sitelabels.site_property.feuille.de.tournee.limite.signalement", "0" ) );
            return totalAnomalies < nbMaxResult;
        }

        return true;
    }

}
