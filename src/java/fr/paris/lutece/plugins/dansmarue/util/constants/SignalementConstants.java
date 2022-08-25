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
package fr.paris.lutece.plugins.dansmarue.util.constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * SignalementConstants.
 */
public final class SignalementConstants
{

    /** The Constant ROLE_OBSERVATEUR_MAIRIES. */
    // ROLES
    public static final String ROLE_OBSERVATEUR_MAIRIES = "OBSERVATEUR_MAIRIES";

    /** The Constant VALUE_PRIORITE_PEU_GENANT_ID. */
    // VALUES
    public static final Integer VALUE_PRIORITE_PEU_GENANT_ID = 3;

    /** The Constant WORKFLOW_ACTION_NAME_REQUALIFIER. */
    public static final String WORKFLOW_ACTION_NAME_REQUALIFIER = "Requalifier";

    /** The Constant FIELD_LAT. */
    // FIELDS
    public static final String FIELD_LAT = "lat";

    /** The Constant FIELD_LNG. */
    public static final String FIELD_LNG = "lng";

    /** The Constant SIGNALEMENT_WORKFLOW_ID. */
    public static final Integer SIGNALEMENT_WORKFLOW_ID = 2;

    /** The Constant MARK_JSP_BACK. */
    // MARKS
    public static final String MARK_JSP_BACK = "jsp_back";

    /** The Constant MARK_NB_ITEMS_PER_PAGE. */
    public static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";

    /** The Constant MARK_PAGINATOR. */
    public static final String MARK_PAGINATOR = "paginator";

    /** The Constant MARK_FILTER. */
    public static final String MARK_FILTER = "filter";

    /** The Constant MARK_LOCALE. */
    public static final String MARK_LOCALE = "locale";

    /**
     * The Constant PROPERTY_DEFAULT_ITEM_PER_PAGE.
     */
    // Properties
    public static final String PROPERTY_DEFAULT_ITEM_PER_PAGE = "signalement.itemsPerPage";

    /**
     * The Constant PROPERTY_SERVICE_FAIT_VALUE.
     */
    public static final String PROPERTY_SERVICE_FAIT_VALUE = "signalement.idStateServiceFait";

    public static final String PROPERTY_SERVICE_FAIT_LIBELLE = "signalement.libelleStateServiceFait";

    /**
     * The Constant PARAMETER_BUTTON_CANCEL.
     */
    // Parameters
    public static final String PARAMETER_BUTTON_CANCEL = "cancel";

    /**
     * The Constant PARAMETER_ERROR.
     */
    public static final String PARAMETER_ERROR = "error";

    /**
     * The Constant PARAMETER_VALIDATE_NEXT.
     */
    public static final String PARAMETER_VALIDATE_NEXT = "validate_next";

    /**
     * The Constant PARAMETER_BUTTON_SEARCH.
     */
    public static final String PARAMETER_BUTTON_SEARCH = "search";

    /**
     * The Constant PARAMETER_PRIORITE.
     */
    public static final String PARAMETER_PRIORITE = "priorite";

    /**
     * The Constant MESSAGE_ERROR_OCCUR.
     */
    // Messages
    public static final String MESSAGE_ERROR_OCCUR = "dansmarue.message.error.erroroccur";

    public static final String MESSAGE_ERROR_PRIORITE = "dansmarue.message.error.priorite";

    public static final String MESSAGE_ERROR_TYPE_SIGNALEMENT = "dansmarue.message.error.typeSignalement";

    /**
     * The Constant MESSAGE_SIGNALEMENT_NOT_FOUND.
     */
    public static final String MESSAGE_SIGNALEMENT_NOT_FOUND = "dansmarue.task_annulation_signalement.signalementNotFound";

    /**
     * The Constant MESSAGE_OBSERVATION_NOT_FOUND.
     */
    public static final String MESSAGE_OBSERVATION_NOT_FOUND = "task_annulation_signalement.observationNotFound";

    /**
     * The Constant ATTRIBUTE_HAS_NEXT.
     */
    // Attribute
    public static final String ATTRIBUTE_HAS_NEXT = "has_next";

    /**
     * The Constant ATTRIBUTE_SESSION_DERNIERE_SAISIE_ACTION.
     */
    // Attribute session
    public static final String ATTRIBUTE_SESSION_DERNIERE_SAISIE_ACTION = "derniere_saisie_action";

    /**
     * The Constant ATTRIBUTE_SESSION_IS_USER_RECTRICTED.
     */
    public static final String ATTRIBUTE_SESSION_IS_USER_RECTRICTED = "signalement_is_user_restricted";

    /**
     * The Constant ATTRIBUTE_SESSION_LIST_RESTRICTED_ARRONDISSEMENTS.
     */
    public static final String ATTRIBUTE_SESSION_LIST_RESTRICTED_ARRONDISSEMENTS = "signalement_list_restricted_arrondissements";

    /**
     * The Constant ATTRIBUTE_SESSION_LIST_RESTRICTED_TYPE_SIGNALEMENT.
     */
    public static final String ATTRIBUTE_SESSION_LIST_RESTRICTED_TYPE_SIGNALEMENT = "signalement_list_restricted_type_signalement";

    /**
     * The Constant ATTRIBUTE_SESSION_LIST_RESTRICTED_CATEGORY_SIGNALEMENT.
     */
    public static final String ATTRIBUTE_SESSION_LIST_RESTRICTED_CATEGORY_SIGNALEMENT = "signalement_list_restricted_category_signalement";

    /** The Constant REFERENCE_LIST_ID. */
    // for lists
    public static final String REFERENCE_LIST_ID = "id";

    /** The Constant REFERENCE_LIST_LIBELLE. */
    public static final String REFERENCE_LIST_LIBELLE = "libelle";

    /** The Constant GOOGLE_MAPS_API_KEY. */
    // google maps
    public static final String GOOGLE_MAPS_API_KEY = AppPropertiesService.getProperty( "signalement.maps.key" );

    /** The Constant ARCHIVE_LIMIT. */
    // archive
    public static final String ARCHIVE_LIMIT = "signalement.archive.limit";

    /** The Constant IMAGE_RESIZE_WIDTH. */
    // images resizing
    public static final String IMAGE_RESIZE_WIDTH = "image.resize.width";

    /** The Constant IMAGE_RESIZE_HEIGHT. */
    public static final String IMAGE_RESIZE_HEIGHT = "image.resize.height";

    /** The Constant IMAGE_THUMBNAIL_RESIZE_WIDTH. */
    public static final String IMAGE_THUMBNAIL_RESIZE_WIDTH = "imageThumbnail.resize.width";

    /** The Constant IMAGE_THUMBNAIL_RESIZE_HEIGHT. */
    public static final String IMAGE_THUMBNAIL_RESIZE_HEIGHT = "imageThumbnail.resize.height";

    /** The Constant UNIT_ATELIER_JARDINAGE. */
    // units property keys definitions
    public static final String UNIT_ATELIER_JARDINAGE = "signalement.unit.atelierJardinage";

    /** The Constant UNIT_JARDINAGE. */
    public static final String UNIT_JARDINAGE = "signalement.unit.jardinage";

    /** The Constant UNIT_SYLVICOLE. */
    public static final String UNIT_SYLVICOLE = "signalement.unit.sylvicole";

    /** The Constant UNIT_CIMETIERE. */
    public static final String UNIT_CIMETIERE = "signalement.unit.cimetiere";

    /** The Constant UNIT_DEVE. */
    public static final String UNIT_DEVE = AppPropertiesService.getProperty( "signalement.unit.deve" );

    /** The Constant UNIT_DPE. */
    public static final Integer UNIT_DPE = AppPropertiesService.getPropertyInt( "signalement.unit.dpe", -1 );

    /** The Constant UNIT_RAMEN. */
    public static final Integer UNIT_RAMEN = AppPropertiesService.getPropertyInt( "signalement.unit.ramen", -1 );

    /** The Constant TYPE_SIGNALEMENT_ENCOMBRANT. */
    // reporting types keys definitions
    public static final String TYPE_SIGNALEMENT_ENCOMBRANT = "signalement.typeSignalement.encombrant";

    /** The Constant INC_VERSION_TYPE_SIGNALEMENT. */
    // reporting types inc version
    public static final String INC_VERSION_TYPE_SIGNALEMENT = "signalement.inc.typeSignalement";

    /** The Constant SIGNALEMENT_PREFIX_IOS. */
    // Report prefixes
    public static final String SIGNALEMENT_PREFIX_IOS = AppPropertiesService.getProperty( "signalement.prefix.origin.ios" );

    /** The Constant SIGNALEMENT_PREFIX_ANDROID. */
    public static final String SIGNALEMENT_PREFIX_ANDROID = AppPropertiesService.getProperty( "signalement.prefix.origin.android" );

    /** The Constant SIGNALEMENT_PREFIX_TELESERVICE. */
    public static final String SIGNALEMENT_PREFIX_TELESERVICE = AppPropertiesService.getProperty( "signalement.prefix.origin.teleservice" );

    /** The Constant SIGNALEMENT_PREFIX_BACKOFFICE. */
    public static final String SIGNALEMENT_PREFIX_BACKOFFICE = AppPropertiesService.getProperty( "signalement.prefix.origin.backoffice" );

    /** The Constant SIGNALEMENT_PREFIX_KEY. */
    public static final String SIGNALEMENT_PREFIX_KEY = "signalement.prefix.origin";

    /** The Constant SIGNALEMENT_PREFIXES. */
    public static final List<String> SIGNALEMENT_PREFIXES = SignalementUtils.getProperties( SIGNALEMENT_PREFIX_KEY );

    /** The Constant START_SIGNALEMENT_NUMERO. */
    public static final int START_SIGNALEMENT_NUMERO = 1;

    /** The Constant ID_ACTION_TRANSFERT_PARTNER. */
    // workflow action
    public static final String ID_ACTION_TRANSFERT_PARTNER = "signalement.daemon.idAction.transfert.partner";

    /** The Constant ID_ACTION_DAEMON_SIGNALEMENT_DONE. */
    public static final String ID_ACTION_DAEMON_SIGNALEMENT_DONE = "signalement.daemon.idAction.done";

    /** The Constant ID_ACTION_NOTIFICATION_ABONNE_SERVICE_PROGRAMME. */
    public static final String ID_ACTION_NOTIFICATION_ABONNE_SERVICE_PROGRAMME = "signalement.daemon.idAction.notif.service.prog";

    /** The Constant ID_ACTION_NOTIFICATION_ABONNE_SERVICE_PROGRAMME_TIERS. */
    public static final String ID_ACTION_NOTIFICATION_ABONNE_SERVICE_PROGRAMME_TIERS = "signalement.daemon.idAction.notif.service.prog.tiers";

    /** The Constant ID_STATE_NOUVEAU. */
    // State id
    public static final Integer ID_STATE_NOUVEAU = AppPropertiesService.getPropertyInt( "signalement.idStateNouveau", -1 );

    /** The Constant ID_STATE_A_TRAITER. */
    public static final Integer ID_STATE_A_TRAITER = AppPropertiesService.getPropertyInt( "signalement.idStateATraiter", -1 );

    /** The Constant ID_STATE_A_REQUALIFIER. */
    public static final Integer ID_STATE_A_REQUALIFIER = AppPropertiesService.getPropertyInt( "signalement.idStateARequalifier", -1 );

    /** The Constant ID_STATE_PROGRAMME. */
    public static final Integer ID_STATE_PROGRAMME = AppPropertiesService.getPropertyInt( "signalement.idStateProgramme", -1 );

    /** The Constant ID_STATE_A_FAIRE_TERRAIN. */
    public static final Integer ID_STATE_A_FAIRE_TERRAIN = AppPropertiesService.getPropertyInt( "signalement.idStateAFaireTerrain", -1 );

    /** The Constant ID_STATE_A_FAIRE_BUREAU. */
    public static final Integer ID_STATE_A_FAIRE_BUREAU = AppPropertiesService.getPropertyInt( "signalement.idStateAFaireBureau", -1 );

    /** The Constant ID_STATE_A_TRANSFERE_PRESTATAIRE. */
    public static final Integer ID_STATE_A_TRANSFERE_PRESTATAIRE = AppPropertiesService.getPropertyInt( "signalement.idStateATransferePrestataire", -1 );

    /** The Constant ID_STATE_TRANSFERE_PRESTATAIRE. */
    public static final Integer ID_STATE_TRANSFERE_PRESTATAIRE = AppPropertiesService.getPropertyInt( "signalement.idStateTransferePrestataire", -1 );

    /** The Constant ID_STATE_PROGRAMME_PRESTATAIRE. */
    public static final Integer ID_STATE_PROGRAMME_PRESTATAIRE = AppPropertiesService.getPropertyInt( "signalement.idStateServiceProgrammePrestataire", -1 );

    /** The Constant OVERVIEW. */
    // Photo view
    public static final Integer OVERVIEW = 1;

    /** The Constant DETAILED_VIEW. */
    public static final Integer DETAILED_VIEW = 0;

    /** The Constant SERVICE_DONE_VIEW. */
    public static final Integer SERVICE_DONE_VIEW = 2;

    /** The Constant TASK_KEY_NOTIFICATION_USER_MULTICONTENTS. */
    public static final String TASK_KEY_NOTIFICATION_USER_MULTICONTENTS = "taskSignalementUserNotificationMultiContents";

    /** The Constant PERIODE_TDB_30_DERNIERS_JOURS. */
    public static final Integer PERIODE_TDB_30_DERNIERS_JOURS = 2;

    /** The Constant PERIODE_TDB_90_DERNIERS_JOURS. */
    public static final Integer PERIODE_TDB_90_DERNIERS_JOURS = 1;

    /** The Constant PERIODE_TDB_180_DERNIERS_JOURS. */
    public static final Integer PERIODE_TDB_180_DERNIERS_JOURS = 0;

    /** The Constant PERIODE_MAP. */
    public static final Map<Integer, String> PERIODE_MAP = new HashMap<>( );
    static
    {
        PERIODE_MAP.put( 0, "180 DAY" );
        PERIODE_MAP.put( 1, "90 DAY" );
        PERIODE_MAP.put( 2, "30 DAY" );
    }

    /** The Constant ID_ERREUR_SIGNALEMENT. */
    public static final Long ID_ERREUR_SIGNALEMENT = -1L;

    /** The Constant RETOUR_CREATION_SIGNALEMENT. */
    public static final String RETOUR_CREATION_SIGNALEMENT = "isSaveSignalementOk";

    /** The Constant CODE_ERREUR_CREATION_SIGNALEMENT. */
    public static final String CODE_ERREUR_CREATION_SIGNALEMENT = "codeErreurCreation";

    /** The Constant ERREUR_SAUVEGARDE_PHOTO. */
    public static final Integer ERREUR_SAUVEGARDE_PHOTO = 1;

    /** The Constant NOM_PHOTO_ENSEMBLE_PJ. */
    // Nom Photo PJ Notification
    public static final String NOM_PHOTO_ENSEMBLE_PJ = AppPropertiesService.getProperty( "signalement.photo.ensemble" );

    /** The Constant NOM_PHOTO_PRES_PJ. */
    public static final String NOM_PHOTO_PRES_PJ = AppPropertiesService.getProperty( "signalement.photo.pres" );

    /** The Constant NOM_PHOTO_SERVICE_FAIT_PJ. */
    public static final String NOM_PHOTO_SERVICE_FAIT_PJ = AppPropertiesService.getProperty( "signalement.photo.fait" );

    /** The Constant PHOTO_SERVICE_FAIT_NOM. */
    public static final String PHOTO_SERVICE_FAIT_NOM = "Photo Service fait";

    /** The Constant PHOTO_INITIALE_NOM. */
    public static final String PHOTO_INITIALE_NOM = "Photo initiale";

    /** The Constant PROPERTY_MARKER_STATES_GREEN. */
    public static final String PROPERTY_MARKER_STATES_GREEN = "signalement.map.markers.states.green";

    /** The Constant PROPERTY_MARKER_STATES_YELLOW. */
    public static final String PROPERTY_MARKER_STATES_YELLOW = "signalement.map.markers.states.yellow";

    public static final String TYPE_EXPORT_FDT_CARTE = "1";

    public static final String TYPE_EXPORT_FDT_LISTE = "2";

    public static final String TYPE_EXPORT_FDT_FULL = "3";

    /** The Constant PROPERTY_URL_PICTURE. */
    public static final String PROPERTY_URL_NEWS_PICTURE = "signalement-rest.url_news_picture";

    /** The Constant PROPERTY_URL_PICTURE. */
    public static final String PROPERTY_URL_HELP_PICTURE = "signalement-rest.url_help_picture";

    /**
     * Utility class - empty constructor.
     */
    private SignalementConstants( )
    {
        // nothing
    }

}
