package fr.paris.lutece.plugins.dansmarue.fo.xpage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.paris.lutece.plugins.dansmarue.service.output.SignalementOutputPrcessor;
import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Priorite;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.service.dto.TypeSignalementDTO;
import fr.paris.lutece.plugins.dansmarue.utils.ListUtils;
import fr.paris.lutece.plugins.dansmarue.validator.ValidatorFinalisation;
import fr.paris.lutece.plugins.dansmarue.validator.ValidatorLocalisation;
import fr.paris.lutece.plugins.dansmarue.validator.ValidatorSignaleur;
import fr.paris.lutece.plugins.leaflet.modules.sira.entities.Address;
import fr.paris.lutece.plugins.leaflet.modules.sira.service.IAddressSuggestPOIService;
import fr.paris.lutece.plugins.sira.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.sira.dto.DossierSignalementDTO;
import fr.paris.lutece.plugins.sira.utils.SiraUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

@Controller( xpageName = AbstractXPage.XPAGE_DANSMARUE, pageTitleI18nKey = "dansmarue.xpage.dansmarue.pageTitle", pagePathI18nKey = "dansmarue.xpage.dansmarue.pagePathLabel" )
public class XPageDansMaRue extends AbstractXPage
{

    private static final long                      serialVersionUID                  = 7782392238698476108L;

    // TEMPLATES
    private static final String                    TEMPLATE_XPAGE_ACCUEIL            = "/skin/plugins/dansmarue/accueil.html";
    private static final String                    TEMPLATE_XPAGE_DOUBLONS           = "/skin/plugins/dansmarue/doublons.html";
    private static final String                    TEMPLATE_XPAGE_CATEGORIE          = "/skin/plugins/dansmarue/type_signalement.html";
    private static final String                    TEMPLATE_XPAGE_FINALISATION       = "/skin/plugins/dansmarue/finalisation.html";
    private static final String                    TEMPLATE_XPAGE_SUIVI              = "/skin/plugins/dansmarue/suivi_signalement.html";
    private static final String                    TEMPLATE_XPAGE_CONFIRMATION       = "/skin/plugins/dansmarue/confirmation.html";

    // VIEW
    private static final String                    VIEW_DOUBLONS                     = "doublons";
    private static final String                    VIEW_CATEGORIE                    = "categorie";
    private static final String                    VIEW_TYPE_SIGNALEMENT             = "type_signalement";
    private static final String                    VIEW_FINALISATION                 = "finalisation";
    private static final String                    VIEW_SUIVI_SIGNALEMENT            = "suivi_signalement";
    private static final String                    VIEW_CONFIRMATION                 = "confirmation";
    private static final String                    VIEW_ADRESSE_ANOMALIE             = "adresse_anomalie";

    // ACTIONS
    private static final String                    ACTION_NEXT                       = "valider_next";
    private static final String                    ACTION_SEARCH_ADDRESS             = "search_address";
    private static final String                    ACTION_VALIDATE_ADDRESS           = "validate_address";
    private static final String                    ACTION_VALIDATE_DOUBLONS          = "validate_doublons";
    private static final String                    ACTION_VALIDATE_CATEGORIE         = "validate_categorie";
    private static final String                    ACTION_VALIDATE_FINALISATION      = "validate_finalisation";
    private static final String                    ACTION_VALIDATE_SUIVI_SIGNALEMENT = "validate_suivi_signalement";
    private static final String                    ACTION_VALIDATE_SIGNALEMENT       = "validate_signalement";
    private static final String                    ACTION_DOUBLONS                   = "doublons_next";
    private static final String                    ACTION_CATEGORIE                  = "categorie_next";
    private static final String                    ACTION_TYPE_SIGNALEMENT           = "type_signalement_next";
    private static final String                    ACTION_FINALISATION               = "finalisation_next";
    private static final String                    ACTION_SUIVI_SIGNALEMENT          = "suivi_signalement_next";
    private static final String                    ACTION_DOWNLOAD                   = "download";
    private static final String                    ACTION_RETOUR_ACCUEIL             = "retour_accueil";

    // PARAMETERS
    private static final String                    PARAMETER_ADDRESS                 = "adresse";
    private static final String                    PARAMETER_WITHOUT_JS              = "withoutJS";
    private static final String                    PARAMETER_VALID_ADDRESS           = "validAddress";
    private static final String                    PARAMETER_TYPE_SIGNALEMENT_ID     = "typeSignalementId";
    private static final String                    PARAMETER_TYPE_SIGNALEMENT        = "typeSignalement";
    private static final String                    PARAMETER_PRIORITE                = "priorite";
    private static final String                    PARAMETER_COMMENTAIRE             = "commentaire";
    private static final String                    PARAMETER_SIGNALEMENT_ID          = "signalementId";
    private static final String                    PARAMETER_PHOTO_DETAILLEE         = "photo_detaillee";
    private static final String                    PARAMETER_PHOTO_DENSEMBLE         = "photo_ensemble";
    private static final String                    PARAMETER_EMAIL                   = "email";

    // MARKERS
    public static final String                     MARK_PROPOSED_ADDRESSES           = "proposedAddresses";
    public static final String                     MARK_NO_VALID_ADDRESSES           = "noValidAddresses";
    private static final String                    MARK_SIGNALEMENT                  = "signalementFOBean";
    private static final String                    MARK_ADRESSE                      = "adresse";
    private static final String                    MARK_LIST_DOUBLONS                = "list_doublons";
    private static final String                    MARK_TYPE_SIGNALEMENT             = "typeSignalement";
    private static final String                    MARK_TYPE_SIGNALEMENT_ID          = "typeSignalementid";
    private static final String                    MARK_LIST_PHOTO_DETAILLEE         = "listupload_photo_detaillee";
    private static final String                    MARK_LIST_PHOTO_ENSEMBLE          = "listupload_photo_ensemble";
    private static final String                    MARK_PHOTO_ENSEMBLE               = "photo_ensemble";
    private static final String                    MARK_PHOTO_DETAILLEE              = "photo_detaillee";
    private static final String                    MARK_EMAIL                        = "email";
    private static final String                    MARK_NOT_SIGNED_IN                = "not_signed_in";

    // SERVICES
    private static final IAddressSuggestPOIService addressSuggestPOIService          = SpringContextService.getBean( "addressSuggestPOIService" );

    // MESSAGES
    private static final String                    MESSAGE_ERROR_LOCALISATION        = "dansmarue.etape.localisation.error.horsparis";

    private static final String                    MON_ANCRE                         = "#monancre";

    private LuteceUser                             _luteceUser;
    private Signalement                            _signalement;
    private TypeSignalement                        _typeSignalement;
    private Adresse                                _adresse;
    private Signaleur                              _signaleur;
    private List<PhotoDMR>                         _photos                           = new ArrayList<PhotoDMR>( );

    /*
     * Constantes pour le download
     */
    public static final String                     PARAMETER_FIELD_NAME              = "fieldName";

    protected boolean isUserConnected( HttpServletRequest request ) throws UserNotSignedException
    {
        // Vérifie que l'utilisateur est bien connecté
        boolean connected = false;
        // AppLogService.debug( "Mode bouchon :" + modeBouchon );
        // if ( modeBouchon )
        // {
        // MokeLuteceAuthentication auth = new MokeLuteceAuthentication( );
        // _luteceUser = new MokeLuteceUser( AppPropertiesService.getProperty( OPEM_AM_GUID ), auth );
        // _luteceUser.setUserInfo( LuteceUser.NAME_FAMILY, AppPropertiesService.getProperty( OPEN_AM_LAST_NAME ) );
        // _luteceUser.setUserInfo( LuteceUser.NAME_GIVEN, AppPropertiesService.getProperty( OPEM_AM_NAME ) );
        // _luteceUser.setUserInfo( LuteceUser.BUSINESS_INFO_ONLINE_EMAIL, AppPropertiesService.getProperty( OPEN_AM_EMAIL ) );
        // connected = true;
        // } else
        // {
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            _luteceUser = SecurityService.getInstance( ).getRemoteUser( request );
            connected = ( _luteceUser != null );
        }
        // }
        return connected;
    }

    /**
     * Returns the content of the page accueil.
     *
     * @param request
     *            The HTTP request
     * @return The view
     * @throws UserNotSignedException
     *             {@link UserNotSignedException}
     */
    @View( value = AbstractXPage.XPAGE_DANSMARUE, defaultView = true )
    public XPage viewAccueil( HttpServletRequest request ) throws UserNotSignedException
    {
        Map<String, Object> model = getModel( );
        _luteceUser = getUser( request, false );
        model.put( MARK_ADRESSE, _adresse );
        model.put( MARK_MAP_ERRORS, request.getSession( ).getAttribute( MARK_MAP_ERRORS ) );
        return getXPage( TEMPLATE_XPAGE_ACCUEIL, request.getLocale( ), model );
    }

    @View( VIEW_ADRESSE_ANOMALIE )
    public XPage viewAddress( HttpServletRequest request )
    {

        Map<String, Object> model = getModel( );
        return getXPage( TEMPLATE_XPAGE_ACCUEIL, request.getLocale( ), model );
    }

    /**
     * Displayed in accessibility mode, fills the proposed address list
     * 
     * @param request
     * @return
     */
    @Action( ACTION_SEARCH_ADDRESS )
    public XPage searchAddress( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );
        String address = request.getParameter( PARAMETER_ADDRESS );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( address );
        List<Address> addressList = addressSuggestPOIService.getAddressItem( address );
        model.put( MARK_ADRESSE, adresse );
        model.put( MARK_PROPOSED_ADDRESSES, addressList );
        return getXPage( TEMPLATE_XPAGE_ACCUEIL, request.getLocale( ), model );
    }

    @Action( ACTION_VALIDATE_ADDRESS )
    public XPage validateAddress( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );

        Adresse adresse = new Adresse( );
        if ( StringUtils.isNotEmpty( request.getParameter( PARAMETER_WITHOUT_JS ) ) && ( request.getParameter( PARAMETER_VALID_ADDRESS ) != null ) )
        {

            String allParameter = request.getParameter( PARAMETER_VALID_ADDRESS );
            String delimiter = "/";

            String[] temp = allParameter.split( delimiter );

            // get the address label
            String labelAddress = temp[0].toLowerCase( );

            // get the lat/lng in lambert 27561

            String strLng = temp[1];
            String strLat = temp[2];

            Double dLat = Double.parseDouble( strLat );
            Double dLng = Double.parseDouble( strLng );

            // transform the lambert coordinates to WGS84 for the
            Double[] geom = signalementService.getGeomFromLambertToWgs84( dLng, dLat );

            adresse.setAdresse( labelAddress );
            adresse.setLng( geom[0] );
            adresse.setLat( geom[1] );
        } else
        {
            populate( adresse, request );
        }

        ValidatorLocalisation validateLocalisation = new ValidatorLocalisation( );
        boolean isLocalisationValide = validateLocalisation.validate( adresse );

        Map<String, String> errors = new HashMap<String, String>( );

        if ( !isLocalisationValide )
        {
            _adresse = adresse;
            errors.put( PARAMETER_ADDRESS, I18nService.getLocalizedString( MESSAGE_ERROR_LOCALISATION, request.getLocale( ) ) );
            request.getSession( ).setAttribute( MARK_MAP_ERRORS, errors );
            return redirectView( request, PAGE_PORTAL + XPAGE_DANSMARUE + MON_ANCRE );
        }

        errors = ( Map<String, String> ) request.getSession( ).getAttribute( MARK_MAP_ERRORS );
        if ( null != errors )
        {
            errors.remove( PARAMETER_ADDRESS );
        }

        // Signalement signalement = new Signalement();
        if ( _signalement == null )
        {
            _signalement = new Signalement( );
        }
        _signalement.setAdressesForm( 0, adresse );
        _adresse = adresse;

        // request.getSession().setAttribute(MARK_ADRESSE, adresse);
        // request.getSession().setAttribute(MARK_SIGNALEMENT, signalement);

        return redirectView( request, VIEW_DOUBLONS );
    }

    @View( VIEW_DOUBLONS )
    public XPage viewDoublons( HttpServletRequest request ) throws UserNotSignedException
    {
        Map<String, Object> model = getModel( );

        LuteceUser user = getUser( request, false );

        Adresse adresse = _signalement.getAdresses( ).get( 0 );
        // Adresse adresse = (Adresse) request.getSession().getAttribute(MARK_ADRESSE);
        // Signalement demandeSignalement = new Signalement();
        // demandeSignalement.setAdressesForm(0, adresse);
        // DemandeSignalement demandeSignalement = (DemandeSignalement) getParentForm( ).getFormDocument( request );
        // String strAdresse = demandeSignalement.getAdresse( );
        double lat = adresse.getLat( );
        double lng = adresse.getLng( );

        TypeSignalement typeSignalement;
        // get all the dossiers and signalements in perimeter
        List<DossierSignalementDTO> listDoublonsSignalement = signalementService.findAllSignalementInPerimeterWithDTO( lat, lng, 50 );
        // Ajout des dossier Ramen dans la liste des signalement

        // TODO appeler WS
        // List<DossierSignalementDTO> listDossierRamen = dossierRamenService.getDossiersCourrantsByGeomWithLimit(50, lng , lat);
        // Iterator<DossierSignalementDTO> dosIterator = listDossierRamen.iterator();
        // while(dosIterator.hasNext()){
        // DossierSignalementDTO dossierSignalementDTO = dosIterator.next();
        // dossierSignalementDTO.setDistance( signalementService.getDistanceBetweenSignalement( lat, lng,
        // dossierSignalementDTO.getLat( ), dossierSignalementDTO.getLng( ) ) );
        // typeSignalement = typeSignalementService.findByIdTypeSignalement(1000);
        // if(StringUtils.isEmpty(dossierSignalementDTO.getImgUrl())){
        // dossierSignalementDTO.setImgUrl(typeSignalement.getImageUrl());
        // }
        // }
        // set la distance + le type de signalement pour chaque DossierSignalementDTO
        Iterator<DossierSignalementDTO> sigIterator = listDoublonsSignalement.iterator( );
        while ( sigIterator.hasNext( ) )
        {
            DossierSignalementDTO dossierSignalementDTO = sigIterator.next( );
            // Vérification si statut permettant le suivi
            boolean followableState = signalementService.isSignalementFollowable( dossierSignalementDTO.getId( ).intValue( ) );
            boolean alreadyFollowed = ( null != user ) && signalementService.isSignalementFollowedByUser( dossierSignalementDTO.getId( ).intValue( ), user.getName( ) );
            if ( !followableState || alreadyFollowed )
            {
                sigIterator.remove( );
                continue;
            }
            dossierSignalementDTO.setDistance( signalementService.getDistanceBetweenSignalement( lat, lng, dossierSignalementDTO.getLat( ), dossierSignalementDTO.getLng( ) ) );
            typeSignalement = typeSignalementService.getTypeSignalement( Integer.parseInt( dossierSignalementDTO.getType( ) ) );
            dossierSignalementDTO.setType( typeSignalement.getFormatTypeSignalement( ) );
            // on récupère l'image par defaut s'il n'y a pas d'image enregistrer
            if ( ( dossierSignalementDTO.getImgUrl( ) == null ) || StringUtils.isEmpty( dossierSignalementDTO.getImgUrl( ) ) )
            {
                dossierSignalementDTO.setImgUrl( typeSignalement.getRoot( ).getImageUrl( ) );
            }
        }
        List<DossierSignalementDTO> listDoublons = listDoublonsSignalement;
        // TODO decommenter après appel WS
        // listDoublons.addAll(listDossierRamen);

        // order by distance
        SiraUtils.tri_bulles( listDoublons );

        model.put( MARK_LIST_DOUBLONS, listDoublons );
        // model.put( MARK_LIST_SIGNALEMENT_PREFIX, SignalementConstants.SIGNALEMENT_PREFIXES );

        model.put( MARK_ADRESSE, adresse );
        model.put( MARK_NOT_SIGNED_IN, user == null );
        model.put( MARK_BASE_URL, AppPathService.getBaseUrl( request ) );

        return getXPage( TEMPLATE_XPAGE_DOUBLONS, request.getLocale( ), model );
    }

    // Valider l'etape Localisation
    @Action( ACTION_VALIDATE_DOUBLONS )
    public XPage setLocalisation( HttpServletRequest request )
    {
        // Map<String, String> errors = new HashMap<String, String>();
        //
        // ValidatorLocalisation validateLocalisation = new ValidatorLocalisation();
        // boolean isLocalisationValide = validateLocalisation.validate(request);
        //
        // if(!isLocalisationValide){
        // errors.put(PARAMETER_ADDRESS, "L'adresse entrée est inexistante ou hors Paris.");
        // request.getSession().setAttribute(MARK_MAP_ERRORS, errors);
        // return redirectView( request , PAGE_PORTAL + XPAGE_DANSMARUE );
        // }
        // errors.remove(PARAMETER_ADDRESS);
        // Adresse adresse= new Adresse();
        // Signalement signalement = new Signalement();
        // signalement.setAdressesForm(0, adresse);
        //
        // populate(adresse, request);
        // request.getSession().setAttribute(MARK_ADRESSE, adresse);
        // request.getSession().setAttribute(MARK_SIGNALEMENT, signalement);

        return redirectView( request, VIEW_CATEGORIE );
    }

    @View( VIEW_CATEGORIE )
    public XPage viewTypeSignalement( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );

        List<TypeSignalementDTO> typeSignalementTree = typeSignalementService.getTypeSignalementTree( true );
        // List<TypeSignalement> listAutocompleteTypeSignalement = typeSignalementService.getAllTypeSignalementActif();
        Adresse adresse = _signalement.getAdresses( ).get( 0 );
        // set l'id du type de signalement a 0 - si on récuèpre un id on set l'id pour l'autoséléctionnner
        String id = "0";
        if ( ( _signalement.getTypeSignalement( ) != null ) && ( _signalement.getTypeSignalement( ).getId( ) != null ) )
        {
            id = _signalement.getTypeSignalement( ).getId( ).toString( );
        }

        // model.put("listAutoComplete", listAutocompleteTypeSignalement);
        model.put( "categorie", id );
        model.put( MARK_ADRESSE, adresse );
        model.put( MARK_TYPE_SIGNALEMENT, typeSignalementTree );
        // model.put( "typeSignalementParamName", MARK_TYPE_SIGNALEMENT_ID );

        return getXPage( TEMPLATE_XPAGE_CATEGORIE, request.getLocale( ), model );
    }

    @Action( ACTION_VALIDATE_CATEGORIE )
    public XPage setTypeSignalment( HttpServletRequest request )
    {
        boolean hasError = false;
        Map<String, String> errors = new HashMap<String, String>( );
        TypeSignalement typeSignalement = new TypeSignalement( );
        if ( StringUtils.isNotEmpty( request.getParameter( PARAMETER_WITHOUT_JS ) ) && ( request.getParameter( "radiobtn-grp-acc" ) != null ) )
        {
            typeSignalement = typeSignalementService.getTypeSignalement( Integer.parseInt( request.getParameter( "radiobtn-grp-acc" ) ) );
            _signalement.setTypeSignalement( typeSignalement );
        } else if ( ( request.getParameter( PARAMETER_TYPE_SIGNALEMENT_ID ) != null ) && StringUtils.isNotEmpty( request.getParameter( PARAMETER_TYPE_SIGNALEMENT_ID ) )
                && !request.getParameter( PARAMETER_TYPE_SIGNALEMENT_ID ).equals( "0" ) )
        {
            typeSignalement = typeSignalementService.getTypeSignalement( Integer.parseInt( request.getParameter( PARAMETER_TYPE_SIGNALEMENT_ID ) ) );

            _signalement.setTypeSignalement( typeSignalement );
        } else
        {
            errors.put( PARAMETER_TYPE_SIGNALEMENT, "Veuillez choisir une categorie." );
            // request.getSession().setAttribute(MARK_MAP_ERRORS, errors);
            return redirectView( request, VIEW_CATEGORIE );
        }

        return redirectView( request, VIEW_FINALISATION );
    }

    @View( VIEW_FINALISATION )
    public XPage viewFinalisation( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );
        // Adresse adresse =
        // Signalement demandeSignalement = (Signalement) request.getSession().getAttribute(MARK_SIGNALEMENT);
        List<FileItem> listPhotosDetaillee = new ArrayList<FileItem>( );
        List<FileItem> listPhotosEnsemble = new ArrayList<FileItem>( );

        FileItem photoDetaillee = dansmarueUploadHandler.getFile( request, MARK_PHOTO_DETAILLEE );
        if ( photoDetaillee != null )
        {

            listPhotosDetaillee.add( photoDetaillee );
        }
        FileItem photoEnsemble = dansmarueUploadHandler.getFile( request, MARK_PHOTO_ENSEMBLE );
        if ( photoEnsemble != null )
        {

            listPhotosEnsemble.add( photoEnsemble );
        }

        model.put( MARK_LIST_PHOTO_DETAILLEE, listPhotosDetaillee );
        model.put( MARK_LIST_PHOTO_ENSEMBLE, listPhotosEnsemble );
        model.put( MARK_ADRESSE, _signalement.getAdresses( ).get( 0 ) );
        model.put( MARK_TYPE_SIGNALEMENT, _signalement.getTypeSignalement( ).getFormatTypeSignalement( ) );
        model.put( dansmarueUploadHandler.getHandlerName( ), dansmarueUploadHandler );

        model.put( MARK_SIGNALEMENT, _signalement );

        List<Priorite> priorites = prioriteService.getAllPriorite( );

        ReferenceList listePriorite = ListUtils.toReferenceList( priorites, "id", "libelle", null, false );

        if ( _signalement.getPriorite( ) != null )
        {
            for ( ReferenceItem r : listePriorite )
            {

                if ( StringUtils.equals( r.getName( ), _signalement.getPrioriteName( ) ) )
                {
                    r.setChecked( true );
                }
            }
        } else
        {
            listePriorite.get( 0 ).setChecked( true );
        }

        model.put( "priorite_list", listePriorite );

        // Adresse adresse = (Adresse) request.getSession().getAttribute(MARK_ADRESSE);
        // model.put( MARK_ADRESSE, adresse );
        return getXPage( TEMPLATE_XPAGE_FINALISATION, request.getLocale( ), model );
    }

    @Action( ACTION_VALIDATE_SUIVI_SIGNALEMENT )
    public XPage setSuiviSignalement( HttpServletRequest request ) throws UserNotSignedException
    {

        if ( isUserConnected( request ) && StringUtils.isNotEmpty( request.getParameter( PARAMETER_SIGNALEMENT_ID ) ) && ( request.getParameter( PARAMETER_SIGNALEMENT_ID ) != null ) )
        {
            LuteceUser user = getUser( request, true );
            long idSignalement = Integer.parseInt( request.getParameter( PARAMETER_SIGNALEMENT_ID ) );
            signalementService.addFollower( idSignalement, user.getName( ), "", user.getUserInfo( LuteceUser.BUSINESS_INFO_ONLINE_EMAIL ), "", "", true );
        }

        return redirectView( request, VIEW_DOUBLONS );

    }

    @Action( ACTION_VALIDATE_FINALISATION )
    public XPage setFinalisation( HttpServletRequest request )
    {
        boolean hasError = false;
        Map<String, String> errors = new HashMap<String, String>( );
        // Signalement signalement = (Signalement) request.getSession().getAttribute(MARK_SIGNALEMENT);

        // Récupèration de la priorite saisi par l'utilisateur
        ValidatorFinalisation vFinalisation = new ValidatorFinalisation( );
        boolean isFinalisationValide = vFinalisation.validate( request );
        if ( ( request.getParameter( PARAMETER_PRIORITE ) != null ) && ( !StringUtils.isEmpty( request.getParameter( PARAMETER_PRIORITE ) ) && isFinalisationValide ) )
        {
            Priorite priorite = prioriteService.load( NumberUtils.toInt( request.getParameter( PARAMETER_PRIORITE ) ) );
            _signalement.setPriorite( priorite );
        } else
        {

            errors.put( PARAMETER_PRIORITE, "L'adresse entrée est inexistante ou hors Paris." );
            hasError = true;
        }
        // Récupèration du commentaire
        _signalement.setCommentaire( request.getParameter( PARAMETER_COMMENTAIRE ) );
        ;

        // supprime les photos s'il existe pour mettre les nouvelles
        if ( !_photos.isEmpty( ) )
        {
            _photos.clear( );

        }
        // Récupèration du fichier
        List<PhotoDMR> Photos = new ArrayList<PhotoDMR>( );
        if ( dansmarueUploadHandler.hasFile( request, PARAMETER_PHOTO_DETAILLEE ) )
        {
            Date date = new Date( );
            DateFormat dateFormat = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );

            FileItem fileItem = dansmarueUploadHandler.getFile( request, PARAMETER_PHOTO_DETAILLEE );

            PhotoDMR photoDetaillee = new PhotoDMR( );
            photoDetaillee.setVue( PhotoDMR.VUE_DETAILLE );
            photoDetaillee.setDate( dateFormat.format( date ) );
            photoDetaillee.getImageThumbnail( ).setImage( fileItem.get( ) );
            photoDetaillee.getImageThumbnail( ).setMimeType( fileItem.getContentType( ) );
            ImageResource image = new ImageResource( );
            image.setImage( fileItem.get( ) );
            image.setMimeType( fileItem.getContentType( ) );
            photoDetaillee.setImage( image );
            photoDetaillee.setSignalement( _signalement );
            _photos.add( photoDetaillee );

        }
        // Récupèration du fichier
        if ( dansmarueUploadHandler.hasFile( request, PARAMETER_PHOTO_DENSEMBLE ) )
        {
            Date date = new Date( );
            DateFormat dateFormat = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );

            FileItem fileItem = dansmarueUploadHandler.getFile( request, PARAMETER_PHOTO_DENSEMBLE );
            PhotoDMR photoDEnsemble = new PhotoDMR( );
            photoDEnsemble.setVue( PhotoDMR.VUE_D_ENSEMBLE );
            photoDEnsemble.setDate( dateFormat.format( date ) );
            photoDEnsemble.getImageThumbnail( ).setImage( fileItem.get( ) );
            photoDEnsemble.getImageThumbnail( ).setMimeType( fileItem.getContentType( ) );
            ImageResource image = new ImageResource( );
            image.setImage( fileItem.get( ) );
            image.setMimeType( fileItem.getContentType( ) );
            photoDEnsemble.setImage( image );
            photoDEnsemble.setSignalement( _signalement );
            _photos.add( photoDEnsemble );
        }
        _signalement.setPhotos( _photos );

        if ( hasError )
        {
            return redirectView( request, VIEW_FINALISATION );
        }

        return redirectView( request, VIEW_SUIVI_SIGNALEMENT );
    }

    @View( VIEW_SUIVI_SIGNALEMENT )
    public XPage viewSuiviSignalement( HttpServletRequest request ) throws UserNotSignedException
    {
        Map<String, Object> model = getModel( );

        if ( _signaleur == null )
        {
            _signaleur = new Signaleur( );
        }
        FileItem photoDetaillee = dansmarueUploadHandler.getFile( request, MARK_PHOTO_DETAILLEE );
        FileItem photoEnsemble = dansmarueUploadHandler.getFile( request, MARK_PHOTO_ENSEMBLE );

        model.put( MARK_PHOTO_DETAILLEE, photoDetaillee );
        model.put( MARK_PHOTO_ENSEMBLE, photoEnsemble );

        model.put( MARK_ADRESSE, _signalement.getAdresses( ).get( 0 ) );
        model.put( MARK_TYPE_SIGNALEMENT, _signalement.getTypeSignalement( ).getFormatTypeSignalement( ) );
        model.put( MARK_SIGNALEMENT, _signalement );
        model.put( dansmarueUploadHandler.getHandlerName( ), dansmarueUploadHandler );

        LuteceUser user = getUser( request, false );
        if ( isUserConnected( request ) && ( _signaleur.getMail( ) == null ) )
        {
            model.put( MARK_EMAIL, user.getUserInfo( LuteceUser.BUSINESS_INFO_ONLINE_EMAIL ) );
        } else if ( _signaleur.getMail( ) != null )
        {
            model.put( MARK_EMAIL, _signaleur.getMail( ) );
        }

        model.put( MARK_NOT_SIGNED_IN, user == null );

        return getXPage( TEMPLATE_XPAGE_SUIVI, request.getLocale( ), model );
    }

    @View( VIEW_CONFIRMATION )
    public XPage viewConfirmation( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );

        return getXPage( TEMPLATE_XPAGE_CONFIRMATION, request.getLocale( ), model );
    }

    @Action( ACTION_VALIDATE_SIGNALEMENT )
    public XPage setSignalement( HttpServletRequest request ) throws UserNotSignedException
    {

        if ( ( request.getParameter( PARAMETER_EMAIL ) != null ) && StringUtils.isNotEmpty( PARAMETER_EMAIL ) )
        {

            Signaleur signaleur = new Signaleur( );
            ValidatorSignaleur vsignaleur = new ValidatorSignaleur( );
            Map<String, String> errors = vsignaleur.validate( request );
            if ( !errors.isEmpty( ) )
            {

                return redirectView( request, VIEW_SUIVI_SIGNALEMENT );
            }
            signaleur.setMail( request.getParameter( PARAMETER_EMAIL ) );

            List<Signaleur> signaleurs = new ArrayList<Signaleur>( );
            signaleurs.add( signaleur );
            _signalement.setSignaleurs( signaleurs );

        }

        SignalementOutputPrcessor signalementProcessor = new SignalementOutputPrcessor( );
        if ( !signalementProcessor.sauvegarderSignalement( _signalement, getUser( request, false ) ) )
        {
            return redirectView( request, VIEW_SUIVI_SIGNALEMENT );
        }

        return redirectView( request, VIEW_CONFIRMATION );

    }

    @Action( ACTION_RETOUR_ACCUEIL )
    public XPage retour( HttpServletRequest request )
    {

        _signalement = new Signalement( );
        _adresse = new Adresse( );
        _typeSignalement = new TypeSignalement( );
        _signaleur = new Signaleur( );
        ;
        _photos = new ArrayList<PhotoDMR>( );
        // Enumeration<String> e = request.getSession().getAttributeNames();
        // while(e.hasMoreElements()){
        // request.getSession().removeAttribute(e.);
        // e.nextElement().;
        // }
        //
        return redirectView( request, AbstractXPage.XPAGE_DANSMARUE );
    }

    @Action( ACTION_DOWNLOAD )
    public XPage download( HttpServletRequest request )
    {
        FileItem fileDemandeItem = dansmarueUploadHandler.getFile( request, request.getParameter( PARAMETER_FIELD_NAME ) );
        if ( fileDemandeItem != null )
        {
            return download( fileDemandeItem.get( ), fileDemandeItem.getName( ), fileDemandeItem.getContentType( ) );
        }
        return null;
    }
}
