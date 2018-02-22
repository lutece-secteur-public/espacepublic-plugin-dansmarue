package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.IAdresseDAO;
import fr.paris.lutece.plugins.dansmarue.business.dao.IConseilQuartierDao;
import fr.paris.lutece.plugins.dansmarue.business.dao.IObservationRejetDAO;
import fr.paris.lutece.plugins.dansmarue.business.dao.IPhotoDAO;
import fr.paris.lutece.plugins.dansmarue.business.dao.IPrioriteDAO;
import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementDAO;
import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementSuiviDAO;
import fr.paris.lutece.plugins.dansmarue.business.dao.ISignaleurDAO;
import fr.paris.lutece.plugins.dansmarue.business.dao.ITaskNotificationConfigDAO;
import fr.paris.lutece.plugins.dansmarue.business.dao.ITypeSignalementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.ConseilQuartier;
import fr.paris.lutece.plugins.dansmarue.business.entities.DashboardPeriod;
import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Priorite;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementDashboardFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementSuivi;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.business.entities.SiraUser;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.business.exceptions.AlreadyFollowedException;
import fr.paris.lutece.plugins.dansmarue.business.exceptions.InvalidStateActionException;
import fr.paris.lutece.plugins.dansmarue.business.exceptions.NonExistentFollowItem;
import fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.service.IArrondissementService;
import fr.paris.lutece.plugins.dansmarue.service.IDashboardPeriodService;
import fr.paris.lutece.plugins.dansmarue.service.IFileMessageCreationService;
import fr.paris.lutece.plugins.dansmarue.service.INumeroSignalementService;
import fr.paris.lutece.plugins.dansmarue.service.IObservationRejetSignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementSuiviService;
import fr.paris.lutece.plugins.dansmarue.service.ISiraUserService;
import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;
import fr.paris.lutece.plugins.dansmarue.service.dto.DashboardSignalementDTO;
import fr.paris.lutece.plugins.dansmarue.service.dto.DossierSignalementDTO;
import fr.paris.lutece.plugins.dansmarue.service.dto.SignalementExportCSVDTO;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.plugins.dansmarue.utils.ImgUtils;
import fr.paris.lutece.plugins.unittree.business.unit.IUnitDAO;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.modules.sira.business.sector.ISectorDAO;
import fr.paris.lutece.plugins.unittree.modules.sira.business.sector.Sector;
import fr.paris.lutece.plugins.unittree.modules.sira.service.sector.ISectorService;
import fr.paris.lutece.plugins.unittree.modules.sira.service.unit.IUnitSiraService;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.util.image.ImageUtil;
import fr.paris.lutece.util.url.UrlItem;

/**
 * The Class SignalementService.
 */
public class SignalementService implements ISignalementService
{

    // PROPERTIES
    private static final String                 PROPERTY_HIDE_RULE               = "signalement.rulesToHideSignalement";
    private static final String                 PROPERTY_STATUS_PUBLIC           = "signalement.status.public";
    private static final String                 PROPERTY_STATUS_NOT_FOLLOWABLE   = "signalement.status.not.followable";
    private static final String                 PROPERTY_FILE_FOLDER_PATH        = "signalement.pathForFileMessageCreation";
    private static final String                 PROPERTY_ACTIONS_NON_AFFICHABLES = "signalement.workflow.actions.nonAffichables";
    private static final String                 PROPERTY_URL_PICTURE             = "signalement-rest.url_picture";
    private static final String                 ID_STATE_NOUVEAU                 = "signalement.idStateNouveau";
    private static final String                 ID_STATE_A_REQUALIFIER           = "signalement.idStateARequalifier";
    private static final String                 ID_STATE_ETAT_INITIAL            = "signalement.idStateEtatInitial";
    private static final String                 ID_STATE_A_TRAITER               = "signalement.idStateATraiter";
    private static final String                 ID_STATE_A_FAIRE_TERRAIN         = "signalement.idStateAFaireTerrain";
    private static final String                 ID_STATE_A_FAIRE_BUREAU          = "signalement.idStateAFaireBureau";
    private static final String                 ID_STATE_PROGRAMME               = "signalement.idStateProgramme";
    private static final String                 ID_STATE_SERVICE_FAIT            = "signalement.idStateServiceFait";
    private static final String                 ID_STATE_ARCHIVE                 = "signalement.idStateArchive";
    private static final String                 ID_STATE_REJETE                  = "signalement.idStateRejete";
    private static final String                 PROPERTY_TASK_NOTIFICATION_USER  = "signalement.taskType.signalementUserNotification";
    private static final String                 PROPERTY_ID_WORKFLOW_SIGNALEMENT = "signalement.idWorkflow";
    private static final String                 PROPERTY_BASE_URL                = "lutece.prod.url";

    // JSP
    private static final String                 JSP_PORTAL                       = "jsp/site/Portal.jsp";

    // CONSTANTS
    private static final String                 STRING_RECU                      = "Re&ccedil;u";
    private static final String                 STRING_OUVERT                    = "Ouvert";
    private static final String                 STRING_EN_COURS                  = "En cours";
    private static final String                 STRING_MESSAGE_CLOS              = "Message clos";
    private static final String                 STRING_TRAITEMENT_IMPOSSIBLE     = "Traitement impossible";

    private static final String                 CHARSET_UTF_8                    = "UTF-8";
    private static final int                    TOKEN_NB_RANDOM_CHAR             = 100;

    // MESSAGES
    private static final String                 MESSAGE_ERROR_NO_SECTOR          = "dansmarue.message.error.aucunSecteur";

    // PARAMETERS
    private static final String                 PARAMETER_PAGE                   = "page";
    private static final String                 PARAMETER_SUIVI                  = "suivi";
    private static final String                 PARAMETER_TOKEN                  = "token";

    /** The _fileMessageCreation service. */
    @Inject
    @Named( "fileMessageCreationService" )
    private IFileMessageCreationService         _fileMessageCreationService;
    /** The _signalement dao. */
    @Inject
    @Named( "signalementDAO" )
    private ISignalementDAO                     _signalementDAO;

    /** The _adresse dao. */
    @Inject
    @Named( "signalementAdresseDAO" )
    private IAdresseDAO                         _adresseDAO;
    
    /** The _conseilQuartier dao. */
    @Inject
    @Named( "signalement.conseilQuartierDAO" )
    private IConseilQuartierDao                 _conseilQuartierDAO;

    /** The _priorite dao. */
    @Inject
    @Named( "prioriteDAO" )
    private IPrioriteDAO                        _prioriteDAO;

    /** The _photo dao. */
    @Inject
    @Named( "photoDAO" )
    private IPhotoDAO                           _photoDAO;

    /** The _signaleur dao. */
    @Inject
    @Named( "signaleurDAO" )
    private ISignaleurDAO                       _signaleurDAO;

    /** The _type signalement dao. */
    @Inject
    @Named( "typeSignalementDAO" )
    private ITypeSignalementDAO                 _typeSignalementDAO;

    /** The _observation rejet dao. */
    @Inject
    @Named( "observationRejetDAO" )
    private IObservationRejetDAO                _observationRejetDAO;

    /** The _tasNotificationConfig dao */
    @Inject
    @Named( "taskNotificationConfigDAO" )
    private ITaskNotificationConfigDAO          _taskNotificationConfigDAO;
    
    @Inject
    @Named( "unittree-sira.sectorDAO" )
    private ISectorDAO                          _sectorDAO;

    @Inject
    @Named( "unittree.unitDAO" )
    private IUnitDAO                            _unitDAO; 
    
    @Inject
    @Named( "signalementSuiviDAO" )
    private ISignalementSuiviDAO                signalementSuiviDAO;

    @Inject
    @Named( "siraUserService" )
    private ISiraUserService                    _siraUserService;

    @Inject
    @Named( "signalement.dashboardPeriodService" )
    private IDashboardPeriodService             _dashboardPeriodService;
    
    @Inject
    @Named("dansmarue.numeroSignalementService")
    private INumeroSignalementService           _numeroSignalementService;

    // SERVICES
    /** The _signalement workflow service. */
    @Inject
    private IWorkflowService                    _signalementWorkflowService;

    @Inject
    private ISectorService                      _sectorService;

    @Inject
    private ISignalementSuiviService            _signalementSuiviService;

    @Inject
    private IObservationRejetSignalementService _observationRejetSignalementService;

    @Inject
    private IArrondissementService              _arrondissementService;

    @Override
    public Signalement getSignalement( long lIdSignalement )
    {
        Signalement signalement = getSignalementInfoWithoutPhoto( lIdSignalement );

        // get his photos without content
        if ( ( signalement != null ) && ( signalement.getPhotos( ) != null ) )
        {
            List<PhotoDMR> photos = _photoDAO.findBySignalementId( signalement.getId( ) );
            signalement.setPhotos( photos );
        }

        return signalement;
    }

    @Override
    public Signalement getSignalementWithFullPhoto( long lIdSignalement )
    {
        Signalement signalement = getSignalementInfoWithoutPhoto( lIdSignalement );

        // get his photos with content
        if ( ( signalement != null ) && ( signalement.getPhotos( ) != null ) )
        {
            List<PhotoDMR> photos = _photoDAO.findWithFullPhotoBySignalementId( signalement.getId( ) );
            signalement.setPhotos( photos );
        }

        return signalement;
    }

    /**
     * Return signalement with all info without photo
     * 
     * @param lIdSignalement
     *            signalement id
     * @return signalement the signalement
     */
    private Signalement getSignalementInfoWithoutPhoto( long lIdSignalement )
    {
        // get the signalement
        Signalement signalement = _signalementDAO.loadById( lIdSignalement );

        if ( signalement == null )
        {
            return null;
        }

        // get his priority
        signalement.setPriorite( _prioriteDAO.load( signalement.getPriorite( ).getId( ) ) );

        // get his type
        signalement.setTypeSignalement( _typeSignalementDAO.getTypeSignalement( signalement.getTypeSignalement( ).getId( ) ) );

        // get his adresse
        signalement.setAdresses( _adresseDAO.findBySignalementId( signalement.getId( ) ) );

        // get his signaleurs
        if ( signalement.getSignaleurs( ) != null )
        {
            List<Signaleur> listSignaleur = _signaleurDAO.findBySignalementId( signalement.getId( ) );
            signalement.setSignaleurs( listSignaleur );
        }

        return signalement;
    }

    @Override
    public List<Signalement> findByFilter( SignalementFilter filter, PaginationProperties paginationProperties, boolean bLoadSignaleurs )
    {
        Plugin pluginSignalement = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );

        List<Signalement> listeSignalements = _signalementDAO.findByFilter( filter, paginationProperties, pluginSignalement );
        if ( listeSignalements != null )
        {

            for ( Signalement signalement : listeSignalements )
            {

                fillSignalementWithData( signalement, bLoadSignaleurs );

            }
        }
        return listeSignalements;
    }

    /**
     * Fill signalement with data.
     *
     * @param signalement
     *            the signalement
     * @param bLoadSignaleurs
     *            True to load signaleurs, false to ignore them
     */
    private void fillSignalementWithData( Signalement signalement, boolean bLoadSignaleurs )
    {
        signalement.setAdresses( _adresseDAO.findBySignalementId( signalement.getId( ) ) );
        if ( bLoadSignaleurs )
        {
            signalement.setSignaleurs( _signaleurDAO.findBySignalementId( signalement.getId( ) ) );
        }
        signalement.setPhotos( _photoDAO.findBySignalementId( signalement.getId( ) ) );
        signalement.setTypeSignalement( _typeSignalementDAO.getTypeSignalementWithoutPhoto( signalement.getTypeSignalement( ).getId( ) ) );
        signalement.setDirection( signalement.getUnit( ) );
    }

    @Override
    public Long insert( Signalement signalement )
    {
        updateSignalementUnit( signalement );
        
        Long numeroSignalement = _numeroSignalementService.findByMonthYear( signalement.getMois( ), signalement.getAnnee( ) );
        signalement.setNumero( numeroSignalement.intValue( ) );
        
        return _signalementDAO.insert( signalement );
    }

    @Override
    public Long insertWithPeripheralDatas( Signalement signalement )
    {
        // determines unit of a signalement by coordinates and selected type.
        try
        {
            updateSignalementUnit( signalement );
        } catch ( BusinessException e )
        {
            throw new BusinessException( signalement, MESSAGE_ERROR_NO_SECTOR );
        }

        Long numeroSignalement = _numeroSignalementService.findByMonthYear( signalement.getMois( ), signalement.getAnnee( ) );
        signalement.setNumero( numeroSignalement.intValue( ) );

        Long signalementId = _signalementDAO.insert( signalement );
        signalement.setId( signalementId );

        // we assume their is always exactly ONE adresse in a created signalement
        Adresse adresse = signalement.getAdresses( ).get( 0 );
        adresse.setSignalement( signalement );
        _adresseDAO.insert( adresse );

        // we assume their is always exactly ONE signaleur in a created signalement
        List<Signaleur> signaleurs = signalement.getSignaleurs( );
        if ( ( signaleurs != null ) && !signaleurs.isEmpty( ) )
        {
            Signaleur signaleur = signaleurs.get( 0 );
            signaleur.setSignalement( signalement );
            _signaleurDAO.insert( signaleur );
        }

        List<PhotoDMR> photos = signalement.getPhotos( );
        if ( photos != null )
        {
            String width = AppPropertiesService.getProperty( SignalementConstants.IMAGE_THUMBNAIL_RESIZE_WIDTH );
            String height = AppPropertiesService.getProperty( SignalementConstants.IMAGE_THUMBNAIL_RESIZE_HEIGHT );
            for ( PhotoDMR photo : photos )
            {
                photo.setSignalement( signalement );
                byte[] resizeImage = ImageUtil.resizeImage( photo.getImage( ).getImage( ), width, height, 1 );
                photo.setImageContent( ImgUtils.checkQuality( photo.getImage( ).getImage( ) ) );
                photo.setImageThumbnailWithBytes( resizeImage );
                _photoDAO.insert( photo );
            }
        }

        String strFileName = "messagecreation_" + signalementId.toString( );
        String strFilePath = AppPathService.getAbsolutePathFromRelativePath( AppPropertiesService.getProperty( PROPERTY_FILE_FOLDER_PATH ) );
        String strMessageCreation = getMessageCreationSignalement( );

        try
        {
            _fileMessageCreationService.createFile( strFilePath, strFileName, strMessageCreation, CHARSET_UTF_8 );
        } catch ( IOException e )
        {
            AppLogService.error( e );
        }

        initializeSignalementWorkflow( signalement );
        return signalementId;
    }

    /**
     * Load a signalement by its id.
     *
     * @param nId
     *            the n id
     * @return the signalement
     */
    @Override
    public Signalement loadById( long nId )
    {
        return _signalementDAO.loadById( nId );
    }

    /**
     * Gets the letter by month.
     *
     * @param month
     *            the month
     * @return the letter by month
     */
    public static String getLetterByMonth( int month )
    {
        String letter = Character.toString( Character.toChars( 65 + month )[0] );
        return month < 12 ? letter : null;
    }

    @Override
    public void update( Signalement signalement )
    {
        // this.updateSignalementUnit( signalement );
        _signalementDAO.update( signalement );

    }

    @Override
    public List<SignalementExportCSVDTO> getSignalementForExport( int[] signalementIds )
    {

        Plugin plugin = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );

        List<Arrondissement> arrondissements = _arrondissementService.getAllArrondissement( );

        List<Signalement> listeSignalement = new ArrayList<Signalement>( );
        Signalement sign;
        for ( int nIdDossier : signalementIds )
        {
            sign = getSignalement( nIdDossier );
            listeSignalement.add( sign );
        }

        List<SignalementExportCSVDTO> listeSignalementExportCSVDTO = new ArrayList<SignalementExportCSVDTO>( );

        SignalementExportCSVDTO dto;
        for ( Signalement signalement : listeSignalement )
        {
            dto = new SignalementExportCSVDTO( );

            // libelle priorite
            Priorite priorite = _prioriteDAO.load( signalement.getPriorite( ).getId( ) );
            dto.setPriorite( priorite.getLibelle( ) );

            // libelle type signalement
            // TypeSignalement type = this._typeSignalementDAO.load( signalement.getTypeSignalement( ).getId( ), plugin );
            TypeSignalement type = _typeSignalementDAO.getTypeSignalementByIdWithParents( signalement.getTypeSignalement( ).getId( ) );

            dto.setTypeSignalement( type.getFormatTypeSignalement( ) );

            // Alias type signalement
            dto.setAlias( StringUtils.defaultString( signalement.getTypeSignalement( ).getAlias( ) ) );

            // Alias mobile type signalement
            dto.setAliasMobile( StringUtils.defaultString( signalement.getTypeSignalement( ).getAliasMobile( ) ) );

            // label unit
            int idUnit = _sectorDAO.getDirectionUnitIdBySectorId(  signalement.getSecteur( ).getIdSector( ) );
            Unit unit = _unitDAO.load( idUnit, plugin );
            dto.setDirection( unit.getLabel( ) );
            
            // Quartier
            ConseilQuartier quartier = _conseilQuartierDAO.selectQuartierByAdresse( signalement.getAdresses( ).get( 0 ).getId( ).intValue( ) ) ;
            dto.setQuartier( quartier.getNomConsqrt( ) );
            
            // adresse
            Adresse adresse = _adresseDAO.loadByIdSignalement( signalement.getId( ) );
            dto.setAdresse( adresse.getAdresse( ) );

            // Coord X
            dto.setCoordX( adresse.getLng( ) );

            // Coord Y
            dto.setCoordY( adresse.getLat( ) );

            // Arrondissement
            for ( Arrondissement arrondissement : arrondissements )
            {
                if ( arrondissement.getId( ) == signalement.getArrondissement( ).getId( ) )
                {
                    dto.setArrondissement( arrondissement.getNumero( ) );
                }
            }
            
            // Secteur d'affectation
            Sector sector = _sectorDAO.load( signalement.getSecteur( ).getIdSector( ), plugin );
            dto.setSecteur( sector.getName( ) );

            // date creation
            dto.setDateCreation( signalement.getDateCreation( ) );

            // numero signalement
            dto.setNumeroSignalement( signalement.getNumeroSignalement( ) );

            // etat
            State state = WorkflowService.getInstance( ).getState( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, _signalementWorkflowService.getSignalementWorkflowId( ),
                    null );
            dto.setEtat( state.getName( ) );

            listeSignalementExportCSVDTO.add( dto );

            // Heure de création
            dto.setHeureCreation( DateUtils.getHourFr( signalement.getHeureCreation( ) ) );

            // Mail usager
            if ( CollectionUtils.isNotEmpty( signalement.getSignaleurs( ) ) )
            {
                dto.setMailusager( StringUtils.defaultString( signalement.getSignaleurs( ).get( 0 ).getMail( ) ) );
            }
            // Commentaire usager
            dto.setCommentaireUsager( StringUtils.defaultString( signalement.getCommentaire( ) ) );

            // Nombre de photos
            Integer nbPhotos = signalement.getPhotos( ).size( );
            dto.setNbPhotos( nbPhotos );

            // Raisons rejet
            List<ObservationRejet> observationsRejets = _observationRejetSignalementService.findByIdSignalement( signalement.getId( ).intValue( ) );

            List<String> strRejets = new ArrayList<>( );
            for ( ObservationRejet observationRejet : observationsRejets )
            {
                strRejets.add( observationRejet.getLibelle( ) );
            }
            dto.setRaisonsRejet( strRejets );
            
            // Nombre de suivis
            dto.setNbSuivis( String.valueOf( signalement.getSuivi( ) ) );
            
            // Nombre de félicitations
            dto.setNbFelicitations( String.valueOf( signalement.getFelicitations( ) ) );
        }
        return listeSignalementExportCSVDTO;

    }

    @Override
    public List<SignalementExportCSVDTO> getSignalementForExportByFilter( SignalementFilter filter, PaginationProperties paginationProperties )
    {
        Plugin plugin = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
        
        List<Signalement> listeSignalement = findByFilter( filter, paginationProperties, true );

        List<SignalementExportCSVDTO> listeSignalementExportCSVDTO = new ArrayList<SignalementExportCSVDTO>( );

        List<Arrondissement> arrondissements = _arrondissementService.getAllArrondissement( );

        SignalementExportCSVDTO dto;
        for ( Signalement signalement : listeSignalement )
        {
            dto = new SignalementExportCSVDTO( );

            // libelle priorite
            dto.setPriorite( signalement.getPriorite( ).getLibelle( ) );

            // libelle type signalement
            dto.setTypeSignalement( signalement.getTypeSignalement( ).getFormatTypeSignalement( ) );

            // Alias type signalement
            dto.setAlias( StringUtils.defaultString( signalement.getTypeSignalement( ).getAlias( ) ) );

            // Alias mobile type signalement
            dto.setAliasMobile( StringUtils.defaultString( signalement.getTypeSignalement( ).getAliasMobile( ) ) );

            // label unit
            dto.setDirection( signalement.getTypeSignalement( ).getUnit( ).getLabel( ) );
            
            // Quartier
            ConseilQuartier quartier = _conseilQuartierDAO.selectQuartierByAdresse( signalement.getAdresses( ).get( 0 ).getId( ).intValue( ) ) ;
            dto.setQuartier( quartier.getNomConsqrt( ) );

            // adresse
            Adresse adresse = _adresseDAO.loadByIdSignalement( signalement.getId( ) );
            dto.setAdresse( adresse.getAdresse( ) );

            // Coord X
            dto.setCoordX( adresse.getLng( ) );

            // Coord Y
            dto.setCoordY( adresse.getLat( ) );

            // Arrondissement
            for ( Arrondissement arrondissement : arrondissements )
            {
                if ( arrondissement.getId( ) == signalement.getArrondissement( ).getId( ) )
                {
                    dto.setArrondissement( arrondissement.getNumero( ) );
                }
            }
            
            // Secteur d'affectation
            Sector sector = _sectorDAO.load( signalement.getSecteur( ).getIdSector( ), plugin );
            dto.setSecteur( sector.getName( ) );
            
            // date creation
            dto.setDateCreation( signalement.getDateCreation( ) );

            // numero signalement
            dto.setNumeroSignalement( signalement.getNumeroSignalement( ) );

            // etat
            State state = WorkflowService.getInstance( ).getState( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, _signalementWorkflowService.getSignalementWorkflowId( ),
                    null );
            dto.setEtat( state.getName( ) );

            // Heure de création
            dto.setHeureCreation( DateUtils.getHourFr( signalement.getHeureCreation( ) ) );

            // Mail usager
            if ( CollectionUtils.isNotEmpty( signalement.getSignaleurs( ) ) )
            {
                dto.setMailusager( StringUtils.defaultString( signalement.getSignaleurs( ).get( 0 ).getMail( ) ) );
            }

            // Commentaire usager
            dto.setCommentaireUsager( StringUtils.defaultString( signalement.getCommentaire( ) ) );

            // Nombre de photos
            Integer nbPhotos = signalement.getPhotos( ).size( );
            dto.setNbPhotos( nbPhotos );

            // Raisons rejet
            List<ObservationRejet> observationsRejets = _observationRejetSignalementService.findByIdSignalement( signalement.getId( ).intValue( ) );

            List<String> strRejets = new ArrayList<>( );
            for ( ObservationRejet observationRejet : observationsRejets )
            {
                strRejets.add( observationRejet.getLibelle( ) );
            }
            dto.setRaisonsRejet( strRejets );
            
            // Nombre de suivis
            dto.setNbSuivis( String.valueOf( signalement.getSuivi( ) ) );
            
            // Nombre de félicitations
            dto.setNbFelicitations( String.valueOf( signalement.getFelicitations( ) ) );

            listeSignalementExportCSVDTO.add( dto );
        }
        return listeSignalementExportCSVDTO;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDoublon( long lIdSignalement )
    {
        Signalement signalement = getSignalement( lIdSignalement );
        if ( signalement != null )
        {
            signalement.setIsDoublon( true );
            update( signalement );
        }
    }

    @Override
    public List<Signalement> getSignalementByStatusId( Integer statusId )
    {
        Integer limit = AppPropertiesService.getPropertyInt( SignalementConstants.ARCHIVE_LIMIT, 30 );
        return _signalementDAO.getSignalementsArchivableByStatusId( statusId, limit );
    }

    /**
     * Update signalement unit.
     *
     *
     * @param signalement
     *            the signalement
     */
    private void updateSignalementUnit( Signalement signalement )
    {
        Adresse adresse = signalement.getAdresses( ).get( 0 );
        Double lng = adresse.getLng( );
        Double lat = adresse.getLat( );
        Unit majorUnit = this.getMajorUnit( signalement.getTypeSignalement( ).getId( ), lng, lat );

        if ( majorUnit == null )
        {
            throw new BusinessException( signalement, MESSAGE_ERROR_NO_SECTOR );
        }

        Sector secteur = _sectorService.getSectorByGeomAndUnit( lng, lat, majorUnit.getIdUnit( ) );
        signalement.setSecteur( secteur );
    }

    @Override
    public Unit getMajorUnit( Integer idTypeSignalement, Double lng, Double lat )
    {
        IUnitSiraService unitSiraService = SpringContextService.getBean( "unittree-sira.unitSiraService" );
        List<Unit> listeUnit = unitSiraService.getUnitsLeafsByGeom( lng, lat );
        Unit majorUnit = this.getMajorUnit( listeUnit );
        if ( majorUnit == null )
        {
            // = signalement.getTypeSignalement( ).getId( );
            majorUnit = unitSiraService.findUnitByGeomAndTypeSignalement( lng, lat, idTypeSignalement );
        } else
        {
            int jardinage = AppPropertiesService.getPropertyInt( SignalementConstants.UNIT_JARDINAGE, -1 );
            if ( majorUnit.getIdParent( ) == jardinage )
            {
                majorUnit = findUnitAtelierJardinage( listeUnit );
            }
        }
        return majorUnit;
    }

    /**
     * Find unit atelier jardinage.
     *
     * @param listeUnit
     *            the liste unit
     * @return the unit
     */
    private Unit findUnitAtelierJardinage( List<Unit> listeUnit )
    {
        Unit selectedUnit = null;
        Iterator<Unit> iterator = listeUnit.iterator( );
        int atelierJardinage = AppPropertiesService.getPropertyInt( SignalementConstants.UNIT_ATELIER_JARDINAGE, -1 );
        while ( ( selectedUnit == null ) && iterator.hasNext( ) )
        {
            Unit unit = iterator.next( );
            int idParent = unit.getIdParent( );
            if ( idParent == atelierJardinage )
            {
                selectedUnit = unit;
            }

        }
        return selectedUnit;

    }

    /**
     * Gets the major unit, e.g if multiples units overlays each others, it looks for the most specific of them.
     *
     * @param listeUnit
     *            the list of units match specific coordinates
     * @return the major unit of the coordinate
     */
    private Unit getMajorUnit( List<Unit> listeUnit )
    {
        int sylvicole = AppPropertiesService.getPropertyInt( SignalementConstants.UNIT_SYLVICOLE, -1 );
        int cimetiere = AppPropertiesService.getPropertyInt( SignalementConstants.UNIT_CIMETIERE, -1 );
        int jardinage = AppPropertiesService.getPropertyInt( SignalementConstants.UNIT_JARDINAGE, -1 );
        Unit selected = null;
        Iterator<Unit> iterator = listeUnit.iterator( );
        Unit next;

        while ( ( selected == null ) && iterator.hasNext( ) )
        {
            next = iterator.next( );
            int idParent = next.getIdParent( );
            if ( idParent != sylvicole )
            {
                if ( idParent != cimetiere )
                {
                    if ( idParent == jardinage )
                    {
                        selected = next;
                    }
                } else
                {

                    selected = next;
                }
            } else
            {
                selected = next;
            }

        }
        return selected;
    }

    @Override
    public List<Integer> getAllIdSignalement( )
    {
        return _signalementDAO.getAllIdSignalement( );
    }

    @Override
    public List<Signalement> getInfosForAllSignalements( )
    {
        List<Integer> listIdSignalement = _signalementDAO.getAllIdSignalement( );
        List<Signalement> listAllSignalement = new ArrayList<Signalement>( );

        for ( Integer nIdSignalement : listIdSignalement )
        {
            Signalement signalement = _signalementDAO.loadById( nIdSignalement );

            List<PhotoDMR> listPhoto = _photoDAO.findBySignalementId( nIdSignalement );
            List<Signaleur> listSignaleur = _signaleurDAO.findBySignalementId( nIdSignalement );
            List<Adresse> listAdresse = _adresseDAO.findBySignalementId( nIdSignalement );

            signalement.setPhotos( listPhoto );
            signalement.setSignaleurs( listSignaleur );
            signalement.setAdresses( listAdresse );

            listAllSignalement.add( signalement );
        }

        return listAllSignalement;
    }

    @Override
    public List<Signalement> findSignalementsByIdTelephone( String idTelephone )
    {
        List<Integer> listIdSignalement = _signalementDAO.findByIdTelephone( idTelephone );
        List<Signalement> listAllSignalement = new ArrayList<Signalement>( );

        for ( Integer nIdSignalement : listIdSignalement )
        {
            Signalement signalement = _signalementDAO.loadById( nIdSignalement );

            List<PhotoDMR> listPhoto = _photoDAO.findBySignalementId( nIdSignalement );
            List<Signaleur> listSignaleur = _signaleurDAO.findBySignalementId( nIdSignalement );
            List<Adresse> listAdresse = _adresseDAO.findBySignalementId( nIdSignalement );

            signalement.setPhotos( listPhoto );
            signalement.setSignaleurs( listSignaleur );
            signalement.setAdresses( listAdresse );

            listAllSignalement.add( signalement );
        }

        return listAllSignalement;
    }

    @Override
    public void incrementeSuiviByIdSignalement( Integer nIdSignalement )
    {
        _signalementDAO.incrementeSuiviByIdSignalement( nIdSignalement );
    }

    @Override
    public void decrementSuiviByIdSignalement( Integer nIdSignalement )
    {
        _signalementDAO.decrementSuiviByIdSignalement( nIdSignalement );
    }

    @Override
    public List<Signalement> findAllSignalementInPerimeterWithInfo( Double lat, Double lng, Integer radius )
    {
        List<Integer> listStatus = getStatusToShowToPublic( );

        List<Integer> listIdSignalementInParameter = _signalementDAO.findAllSignalementInPerimeter( lat, lng, radius, listStatus );
        List<Signalement> listAllSignalement = new ArrayList<Signalement>( );

        for ( Integer nIdSignalement : listIdSignalementInParameter )
        {

            Signalement signalement = _signalementDAO.loadById( nIdSignalement );
            TypeSignalement typeSignalement = _typeSignalementDAO.getTypeSignalement( signalement.getTypeSignalement( ).getId( ) );
            String type = typeSignalement.getIdCategory( ).toString( );
            signalement.setTypeSignalement( typeSignalement );
            if ( !isHiddenBySpecificRule( type, signalement.getDateCreation( ) ) )
            {
                List<PhotoDMR> listPhoto = _photoDAO.findBySignalementId( nIdSignalement );
                List<Signaleur> listSignaleur = _signaleurDAO.findBySignalementId( nIdSignalement );
                List<Adresse> listAdresse = _adresseDAO.findBySignalementId( nIdSignalement );

                signalement.setPhotos( listPhoto );
                signalement.setSignaleurs( listSignaleur );
                signalement.setAdresses( listAdresse );

                listAllSignalement.add( signalement );
            }
        }

        return listAllSignalement;

    }

    /**
     * Certains signalements peuvent ne pas apparaitre.
     * 
     * @param strIdCategory
     *            id de la catégorie
     * @param strDate
     * @return
     */
    private boolean isHiddenBySpecificRule( String strIdCategory, String strDate )
    {
        String rule = AppPropertiesService.getProperty( PROPERTY_HIDE_RULE );
        if ( ( rule != null ) && ( rule.length( ) > 0 ) )
        {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern( DateUtils.DATE_FR );
            try
            {
                for ( String strRule : rule.split( "," ) )
                {
                    String strRuleId = strRule.split( "-" )[0];
                    int nbreDays = Integer.parseInt( strRule.split( "-" )[1] );
                    if ( Integer.parseInt( strIdCategory ) == Integer.parseInt( strRuleId ) )
                    {
                        LocalDate creationDate = LocalDate.parse( strDate, formatter );
                        LocalDate now = LocalDate.now( );

                        long ellapsedDays = ChronoUnit.DAYS.between( creationDate, now );

                        if ( ellapsedDays > nbreDays )
                        {
                            return true;
                        }
                    }
                }
            } catch ( DateTimeParseException e )
            {
                AppLogService.error( e );
                return false;
            }
        }
        return false;
    }

    /**
     * we search only with the following states : nouveau, à traiter, service programmé
     * 
     * @return list of status that public can see
     */
    private List<Integer> getStatusToShowToPublic( )
    {
        String property = AppPropertiesService.getProperty( PROPERTY_STATUS_PUBLIC );
        List<Integer> listStatus = new ArrayList<Integer>( );
        if ( StringUtils.isNotBlank( property ) )
        {
            String[] idStatusArr = null;
            if ( property.contains( "," ) )
            {
                idStatusArr = property.split( "," );
            } else
            {
                idStatusArr = new String[1];
                idStatusArr[0] = property;
            }
            for ( String idStatus : idStatusArr )
            {
                listStatus.add( Integer.parseInt( idStatus ) );
            }

        }
        return listStatus;
    }

    @Override
    public Integer getDistanceBetweenSignalement( double lat1, double lng1, double lat2, double lng2 )
    {
        return _signalementDAO.getDistanceBetweenSignalement( lat1, lng1, lat2, lng2 );
    }

    @Override
    public List<DossierSignalementDTO> findAllSignalementInPerimeterWithDTO( Double lat, Double lng, Integer radius )
    {
        List<Integer> listStatus = getStatusToShowToPublic( );

        // get the signalements in the parameter
        List<DossierSignalementDTO> listDTO = _signalementDAO.findAllSignalementInPerimeterWithDTO( lat, lng, radius, listStatus );

        for ( Iterator<DossierSignalementDTO> itr = listDTO.iterator( ); itr.hasNext( ); )
        {
            DossierSignalementDTO dossierSignalementDTO = itr.next( );
            String strIdSignalementCategory = Integer.toString( dossierSignalementDTO.getIdCategory( ) );
            if ( isHiddenBySpecificRule( strIdSignalementCategory, dossierSignalementDTO.getDateCreation( ) ) )
            {
                itr.remove( );
            }
        }

        // add the photo of all the signalements in the perimeter
        for ( DossierSignalementDTO dossierSignalementDTO : listDTO )
        {
            List<PhotoDMR> listPhoto = _photoDAO.findBySignalementId( dossierSignalementDTO.getId( ) );
            if ( listPhoto.size( ) > 0 )
            {
                dossierSignalementDTO.setImgUrl( AppPropertiesService.getProperty( 
                        PROPERTY_URL_PICTURE ) + listPhoto.get( 0 ).getId( ) ) ;
            }
        }

        return listDTO;
    }

    @Override
    public void initializeSignalementWorkflow( Signalement signalement )
    {
        // set the state of the signalement with the workflow
        WorkflowService workflowService = WorkflowService.getInstance( );
        if ( workflowService.isAvailable( ) )
        {
            // récupération de l'identifiant du workflow
            Integer workflowId = _signalementWorkflowService.getSignalementWorkflowId( );
            if ( workflowId != null )
            {
                State state = workflowService.getState( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, workflowId, null );

                // FIXME : gestion des longs dans le plugin-workflow - LUTECE-1467
                // création de l'état initial et exécution des tâches automatiques
                workflowService.executeActionAutomatic( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, workflowId, null );
            } else
            {
                AppLogService.error( "Signalement : No workflow selected" );
            }
        } else
        {
            AppLogService.error( "Signalement : Workflow not available" );
        }
    }

    @Override
    public boolean hasSignalementForRoadMap( List<Integer> listSectorId )
    {
        return _signalementDAO.hasSignalementForRoadMap( listSectorId );
    }

    /**
     * Get the informations of a signalement by its token
     * 
     * @param token
     * @return signalement
     */
    @Override
    public Signalement getSignalementByToken( String token )
    {
        Plugin pluginSignalement = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
        Signalement signalement = _signalementDAO.getSignalementByToken( token, pluginSignalement );
        if ( signalement != null )
        {
            signalement = getSignalement( signalement.getId( ) );
        } else
        {
            return null;
        }
        return signalement;
    }

    @Override
    public Double[] getGeomFromLambertToWgs84( Double dLatLambert, Double dLngLambert )
    {
        return _signalementDAO.getGeomFromLambertToWgs84( dLatLambert, dLngLambert );
    }

    @Override
    public Integer countIdSignalementByFilter( SignalementFilter filter, Plugin plugin )
    {
        return _signalementDAO.countIdSignalementByFilter( filter, plugin );
    }

    @Override
    public void doDeleteSignalement( long lIdSignalement )
    {
        // delete the photos of the signalement
        List<PhotoDMR> photos = _photoDAO.findBySignalementId( lIdSignalement );
        for ( PhotoDMR photo : photos )
        {
            _photoDAO.remove( photo.getId( ) );
        }

        // delete the adresse of the signalement
        List<Adresse> adresses = _adresseDAO.findBySignalementId( lIdSignalement );
        for ( Adresse adresse : adresses )
        {
            _adresseDAO.remove( adresse.getId( ) );
        }

        // delete the signaleur of the signalement
        Signaleur signaleur = _signaleurDAO.loadByIdSignalement( lIdSignalement );
        if ( ( signaleur.getId( ) != null ) && ( signaleur.getId( ) > 0 ) )
        {
            _signaleurDAO.remove( signaleur.getId( ) );
        }

        // Delete the following data of the signalement
        _signalementSuiviService.removeByIdSignalement( lIdSignalement );

        // delete the signalement
        _signalementDAO.remove( lIdSignalement );

        // delete from workflow
        WorkflowService.getInstance( ).doRemoveWorkFlowResource( ( int ) lIdSignalement, Signalement.WORKFLOW_RESOURCE_TYPE );
    }

    @Override
    public void doDeleteSignalement( int[] lIdSignalement )
    {

        // TODO Auto-generated method stub

        if ( ( lIdSignalement != null ) && ( lIdSignalement.length > 0 ) )
        {

            for ( long idSignalement : lIdSignalement )
            {
                doDeleteSignalement( idSignalement );
            }
        }

    }

    @Override
    public void insertMessageCreationSignalement( String messageCreation )
    {
        _signalementDAO.insertMessageCreationSignalement( messageCreation );

    }

    @Override
    public void updateMessageCreationSignalement( String messageCreation )
    {
        _signalementDAO.updateMessageCreationSignalement( messageCreation );

    }

    @Override
    public String loadMessageCreationSignalement( )
    {
        return _signalementDAO.loadMessageCreationSignalement( );
    }

    @Override
    public void removeMessageCreationSignalement( )
    {
        _signalementDAO.removeMessageCreationSignalement( );

    }

    /**
     * Replace the current state to a more explicit one for front office
     * 
     * @param stateOfSignalement
     * @return the new state
     */
    @Override
    public String changeToGoodStateForSuivi( State stateOfSignalement )
    {
        String strState = stateOfSignalement.getName( );
        String strId = String.valueOf( stateOfSignalement.getId( ) );

        if ( strId.equals( AppPropertiesService.getProperty( ID_STATE_NOUVEAU ) ) || strId.equals( AppPropertiesService.getProperty( ID_STATE_A_REQUALIFIER ) )
                || strId.equals( AppPropertiesService.getProperty( ID_STATE_ETAT_INITIAL ) ) )
        {
            strState = STRING_RECU;
        } else if ( strId.equals( AppPropertiesService.getProperty( ID_STATE_A_TRAITER ) ) || strId.equals( AppPropertiesService.getProperty( ID_STATE_A_FAIRE_TERRAIN ) )
                || strId.equals( AppPropertiesService.getProperty( ID_STATE_A_FAIRE_BUREAU ) ) )
        {
            strState = STRING_OUVERT;
        } else if ( strId.equals( AppPropertiesService.getProperty( ID_STATE_PROGRAMME ) ) )
        {
            strState = STRING_EN_COURS;
        } else if ( strId.equals( AppPropertiesService.getProperty( ID_STATE_SERVICE_FAIT ) ) || strId.equals( AppPropertiesService.getProperty( ID_STATE_ARCHIVE ) ) )
        {
            strState = STRING_MESSAGE_CLOS;
        } else if ( strId.equals( AppPropertiesService.getProperty( ID_STATE_REJETE ) ) )
        {
            strState = STRING_TRAITEMENT_IMPOSSIBLE;
        }

        return strState;
    }

    @Override
    public void affectToken( Signalement signalement )
    {
        try
        {
            StringBuilder token = new StringBuilder( RandomStringUtils.randomAlphanumeric( TOKEN_NB_RANDOM_CHAR ) );
            token.append( new Date( ).getTime( ) );
            signalement.setToken( new String( DigestUtils.md5DigestAsHex( token.toString( ).getBytes( CHARSET_UTF_8 ) ) ) );
        } catch ( UnsupportedEncodingException e )
        {
            AppLogService.error( e );
        }
    }

    @Override
    public void setDateDePassage( String dateDePassage, String heureDePassage, Long lIdSignalement )
    {

        Signalement signalement = getSignalement( lIdSignalement );
        if ( signalement != null )
        {
            signalement.setDateServiceFaitTraitement( dateDePassage );
            signalement.setHeureServiceFaitTraitement( heureDePassage );
            update( signalement );
        }

    }

    /**
     * Gets the message to notify the user with, for a signalement creation
     * 
     * @return the message to notify
     */
    @Override
    public String getMessageCreationSignalement( )
    {
        int idStateBefore = AppPropertiesService.getPropertyInt( ID_STATE_ETAT_INITIAL, -1 );
        String taskType = AppPropertiesService.getProperty( PROPERTY_TASK_NOTIFICATION_USER );
        int idWorkflow = AppPropertiesService.getPropertyInt( PROPERTY_ID_WORKFLOW_SIGNALEMENT, -1 );
        return _taskNotificationConfigDAO.getMessageByTaskTypeStateWorkflow( taskType, idWorkflow, idStateBefore );
    }

    /**
     * Get the link of the "consultation page" (front office signalement)
     * 
     * @param signalement
     *            the signalement
     * @param request
     *            the request
     * @return the url of the
     */
    @Override
    public String getLienConsultation( Signalement signalement, HttpServletRequest request )
    {
        UrlItem urlItem;

        urlItem = new UrlItem( AppPropertiesService.getProperty( PROPERTY_BASE_URL ) + JSP_PORTAL );

        urlItem.addParameter( PARAMETER_PAGE, PARAMETER_SUIVI );
        urlItem.addParameter( PARAMETER_TOKEN, signalement.getToken( ) );

        return urlItem.getUrl( );
    }

    /**
     * Increments the number of congratulations for this signalement
     * 
     * @param idSignalement
     *            the signalement id, to increment
     */
    @Override
    public void incrementFelicitationsByIdSignalement( int idSignalement )
    {
        _signalementDAO.incrementFelicitationsByIdSignalement( idSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Signalement> getSignalementsByGuid( String guid )
    {
        List<Long> signalementIdsList = signalementSuiviDAO.findSignalementsByGuid( guid );
        List<Signalement> signalements = new ArrayList<>( );
        for ( Long idSignalement : signalementIdsList )
        {

            Signalement signalement = _signalementDAO.loadById( idSignalement );
            TypeSignalement typeSignalement = _typeSignalementDAO.getTypeSignalement( signalement.getTypeSignalement( ).getId( ) );
            signalement.setTypeSignalement( typeSignalement );
            List<PhotoDMR> listPhoto = _photoDAO.findBySignalementId( idSignalement );
            List<Signaleur> listSignaleur = _signaleurDAO.findBySignalementId( idSignalement );
            List<Adresse> listAdresse = _adresseDAO.findBySignalementId( idSignalement );
            signalement.setPhotos( listPhoto );
            signalement.setSignaleurs( listSignaleur );
            signalement.setAdresses( listAdresse );
            signalements.add( signalement );
        }
        return signalements;
    }

    @Override
    public void addFollower( Long signalementId, String guid, String strUDID, String email, String device, String userToken, boolean createUser )
            throws InvalidStateActionException, AlreadyFollowedException
    {

        State signalementState = WorkflowService.getInstance( ).getState( signalementId.intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, _signalementWorkflowService.getSignalementWorkflowId( ),
                null );
        Integer stateId = signalementState.getId( );

        List<Integer> notFollowableStatusList = getNotFollowableStatus( );
        List<Integer> statusPublic = getStatusToShowToPublic( );

        if ( !statusPublic.contains( stateId ) || notFollowableStatusList.contains( stateId ) )
        {
            throw new InvalidStateActionException( "Le statut de l'anomalie ne permet pas cette action" );
        }

        if ( createUser )
        {
            SiraUser siraUser = new SiraUser( );
            siraUser.setGuid( guid );
            siraUser.setUdid( strUDID );
            siraUser.setDevice( device );
            siraUser.setMail( email );
            siraUser.setToken( userToken );

            // Creates the user if does not exists

            SiraUser siraUserDB = _siraUserService.findByGuidAndToken( siraUser.getGuid( ), siraUser.getToken( ) );
            if ( null == siraUserDB )
            {
                _siraUserService.insert( siraUser );
            }
        }
        // Follow the signalement
        SignalementSuivi signalementSuivi = new SignalementSuivi( );
        signalementSuivi.setIdSignalement( signalementId );
        signalementSuivi.setUserGuid( guid );

        long idSuivi = _signalementSuiviService.findByIdSignalementAndGuid( signalementSuivi.getIdSignalement( ), signalementSuivi.getUserGuid( ) );
        if ( idSuivi != -1 )
        {
            throw new AlreadyFollowedException( "L'anomalie est déjà suivie par l'utilisateur" );
        }

        if ( idSuivi == -1 )
        {
            _signalementSuiviService.insert( signalementSuivi );
            _signalementDAO.incrementeSuiviByIdSignalement( signalementId.intValue( ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFollower( Long signalementId, String guid ) throws NonExistentFollowItem
    {
        // Unfollow the signalement
        SignalementSuivi signalementSuivi = new SignalementSuivi( );
        signalementSuivi.setIdSignalement( signalementId );
        signalementSuivi.setUserGuid( guid );

        long idSuivi = _signalementSuiviService.findByIdSignalementAndGuid( signalementId, guid );
        if ( idSuivi == -1 )
        {
            throw new NonExistentFollowItem( "L'anomalie n'est pas actuellement suivie par l'usager" );
        }
        _signalementSuiviService.remove( idSuivi );
        decrementSuiviByIdSignalement( signalementId.intValue( ) );
    }

    /**
     * Get status which are in the public list, and are not followable
     * 
     * @return List of state ids in which the signalement is not followable but is in public list
     */
    private List<Integer> getNotFollowableStatus( )
    {
        String property = AppPropertiesService.getProperty( PROPERTY_STATUS_NOT_FOLLOWABLE );
        List<Integer> listStatus = new ArrayList<Integer>( );
        if ( StringUtils.isNotBlank( property ) )
        {
            String[] idStatusArr = null;
            if ( property.contains( "," ) )
            {
                idStatusArr = property.split( "," );
            } else
            {
                idStatusArr = new String[1];
                idStatusArr[0] = property;
            }
            for ( String idStatus : idStatusArr )
            {
                listStatus.add( Integer.parseInt( idStatus ) );
            }

        }
        return listStatus;
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public List<DashboardSignalementDTO> findByDashboardFilter( SignalementDashboardFilter filter )
    {
        if ( filter.getPeriodId( ) != null )
        {
            DashboardPeriod dashboardPeriod = _dashboardPeriodService.load( filter.getPeriodId( ) );
            filter.setDashboardPeriod( dashboardPeriod );
        }
        Plugin pluginSignalement = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
        List<DashboardSignalementDTO> listeSignalements = _signalementDAO.findByDashboardFilter( filter, pluginSignalement );
        return listeSignalements;
    }

    @Override
    public List<Signalement> getByIds( List<Integer> signalementIds, Integer from, Integer to )
    {
        List<Signalement> signalements = new ArrayList<>( );
        List<Integer> signalementsSubList = new ArrayList<>( signalementIds );

        int supBound = signalementIds.size( );
        if ( ( from != null ) && ( to < signalementIds.size( ) ) )
        {
            supBound = to;
        }

        int infBound = 0;
        if ( ( from != null ) && ( from < signalementIds.size( ) ) )
        {
            infBound = from;
        }

        signalementsSubList = signalementIds.subList( infBound, supBound );
        for ( Integer signalementId : signalementsSubList )
        {
            Signalement signalement = loadById( signalementId );
            signalement.setPriorite( _prioriteDAO.load( signalement.getPriorite( ).getId( ) ) );
            fillSignalementWithData( signalement, false );
            signalements.add( signalement );
        }
        return signalements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getIdsSignalementByFilter( SignalementFilter filter )
    {
        ;
        return _signalementDAO.getIdsSignalementByFilter( filter, PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Action> getListActionsByIdSignalementAndUser( int nIdSignalement, int workflowId, AdminUser user )
    {
        // actions du workflow
        Collection<Action> listActions = new ArrayList<Action>( );

        Collection<Action> listActionsPossibles = WorkflowService.getInstance( ).getActions( nIdSignalement, Signalement.WORKFLOW_RESOURCE_TYPE, workflowId, user );

        String strListActionsNonAffichables = AppPropertiesService.getProperty( PROPERTY_ACTIONS_NON_AFFICHABLES );
        List<String> listActionsNonAffichables = Arrays.asList( strListActionsNonAffichables.split( "," ) );

        for ( Action action : listActionsPossibles )
        {
            if ( !listActionsNonAffichables.contains( String.valueOf( action.getId( ) ) ) )
            {
                listActions.add( action );
            }
        }

        return listActions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSignalementFollowable( int nIdSignalement )
    {
        State signalementState = WorkflowService.getInstance( ).getState( nIdSignalement, Signalement.WORKFLOW_RESOURCE_TYPE, _signalementWorkflowService.getSignalementWorkflowId( ), null );
        Integer stateId = signalementState.getId( );

        List<Integer> notFollowableStatusList = getNotFollowableStatus( );
        List<Integer> statusPublic = getStatusToShowToPublic( );

        if ( !statusPublic.contains( stateId ) || notFollowableStatusList.contains( stateId ) )
        {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSignalementFollowedByUser( int nIdSignalement, String userGuid )
    {
        long idSuivi = _signalementSuiviService.findByIdSignalementAndGuid( nIdSignalement, userGuid );
        if ( idSuivi != -1 )
        {
            return true;
        }
        return false;
    }

    /**
     * Checks if the user has already followed this signalement
     * 
     * @param reference
     *            Id of the signalement reference
     * @return the signalement id reference
     */
    @Override
    public String getSignalementReference( String reference, Signalement signalement )
    {
        String prefix = signalement.getPrefix( );
        String annee = StringUtils.EMPTY + signalement.getAnnee( ) + StringUtils.EMPTY;
        String mois = signalement.getMois( );
        String numero = StringUtils.EMPTY + signalement.getNumero( ) + StringUtils.EMPTY;
        reference = prefix + annee + mois + numero;
        return reference;
    }

    @Override
    public void doMettreSousSurveillance( int nIdSignalement, String dateMiseEnSurveillance )
    {
        _signalementDAO.addMiseEnSurveillanceDate( nIdSignalement, dateMiseEnSurveillance );
        
    }

}
