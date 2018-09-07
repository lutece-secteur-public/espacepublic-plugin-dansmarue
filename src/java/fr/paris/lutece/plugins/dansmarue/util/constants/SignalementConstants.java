/*
 * Copyright (c) 2002-2011, Mairie de Paris
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

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * SignalementConstants
 */
public final class SignalementConstants
{
    // ROLES
    public static final String       ROLE_OBSERVATEUR_MAIRIES                               = "OBSERVATEUR_MAIRIES";

    // VALUES
    public static final Integer      VALUE_PRIORITE_PEU_GENANT_ID                           = 3;

    // FIELDS
    public static final String       FIELD_LAT                                              = "lat";
    public static final String       FIELD_LNG                                              = "lng";

    public static final Integer      SIGNALEMENT_WORKFLOW_ID                                = 2;

    // MARKS
    public static final String       MARK_JSP_BACK                                          = "jsp_back";
    public static final String       MARK_NB_ITEMS_PER_PAGE                                 = "nb_items_per_page";
    public static final String       MARK_PAGINATOR                                         = "paginator";
    public static final String       MARK_FILTER                                            = "filter";
    public static final String       MARK_LOCALE                                            = "locale";

    // Properties
    public static final String       PROPERTY_DEFAULT_ITEM_PER_PAGE                         = "signalement.itemsPerPage";
    public static final String       PROPERTY_SERVICE_FAIT_VALUE                            = "signalement.idStateServiceFait";

    // Parameters
    public static final String       PARAMETER_BUTTON_CANCEL                                = "cancel";
    public static final String       PARAMETER_ERROR                                        = "error";
    public static final String       PARAMETER_VALIDATE_NEXT                                = "validate_next";
    public static final String       PARAMETER_BUTTON_SEARCH                                = "search";
    public static final String       PARAMETER_PRIORITE                                     = "priorite";

    // Messages
    public static final String       MESSAGE_ERROR_OCCUR                                    = "dansmarue.message.error.erroroccur";
    public static final String       MESSAGE_SIGNALEMENT_NOT_FOUND                          = "dansmarue.task_annulation_signalement.signalementNotFound";
    public static final String       MESSAGE_OBSERVATION_NOT_FOUND                          = "task_annulation_signalement.observationNotFound";

    // Attribute
    public static final String       ATTRIBUTE_HAS_NEXT                                     = "has_next";

    // Attribute session
    public static final String       ATTRIBUTE_SESSION_DERNIERE_SAISIE_ACTION               = "derniere_saisie_action";
    public static final String       ATTRIBUTE_SESSION_IS_USER_RECTRICTED                   = "signalement_is_user_restricted";
    public static final String       ATTRIBUTE_SESSION_LIST_RESTRICTED_ARRONDISSEMENTS      = "signalement_list_restricted_arrondissements";
    public static final String       ATTRIBUTE_SESSION_LIST_RESTRICTED_TYPE_SIGNALEMENT     = "signalement_list_restricted_type_signalement";
    public static final String       ATTRIBUTE_SESSION_LIST_RESTRICTED_CATEGORY_SIGNALEMENT = "signalement_list_restricted_category_signalement";

    // for lists
    public static final String       REFERENCE_LIST_ID                                      = "id";
    public static final String       REFERENCE_LIST_LIBELLE                                 = "libelle";

    // google maps
    public static final String       GOOGLE_MAPS_API_KEY                                    = AppPropertiesService.getProperty( "signalement.maps.key" );

    // archive
    public static final String       ARCHIVE_LIMIT                                          = "signalement.archive.limit";

    // images resizing
    public static final String       IMAGE_RESIZE_WIDTH                                     = "image.resize.width";
    public static final String       IMAGE_RESIZE_HEIGHT                                    = "image.resize.height";

    public static final String       IMAGE_THUMBNAIL_RESIZE_WIDTH                           = "imageThumbnail.resize.width";
    public static final String       IMAGE_THUMBNAIL_RESIZE_HEIGHT                          = "imageThumbnail.resize.height";

    // units property keys definitions
    public static final String       UNIT_ATELIER_JARDINAGE                                 = "signalement.unit.atelierJardinage";
    public static final String       UNIT_JARDINAGE                                         = "signalement.unit.jardinage";
    public static final String       UNIT_SYLVICOLE                                         = "signalement.unit.sylvicole";
    public static final String       UNIT_CIMETIERE                                         = "signalement.unit.cimetiere";
    public static final String       UNIT_DEVE                                              = AppPropertiesService.getProperty( "signalement.unit.deve" );
    public static final Integer      UNIT_DPE                                               = AppPropertiesService.getPropertyInt( "signalement.unit.dpe", -1 );
    public static final Integer      UNIT_RAMEN                                             = AppPropertiesService.getPropertyInt( "signalement.unit.ramen", -1 );

    // reporting types keys definitions
    public static final String       TYPE_SIGNALEMENT_ENCOMBRANT                            = "signalement.typeSignalement.encombrant";

    // reporting types inc version
    public static final String       INC_VERSION_TYPE_SIGNALEMENT                           = "signalement.inc.typeSignalement";

    // Report prefixes
    public static final String       SIGNALEMENT_PREFIX_IOS                                 = AppPropertiesService.getProperty( "signalement.prefix.origin.ios" );
    public static final String       SIGNALEMENT_PREFIX_ANDROID                             = AppPropertiesService.getProperty( "signalement.prefix.origin.android" );
    public static final String       SIGNALEMENT_PREFIX_TELESERVICE                         = AppPropertiesService.getProperty( "signalement.prefix.origin.teleservice" );
    public static final String       SIGNALEMENT_PREFIX_BACKOFFICE                          = AppPropertiesService.getProperty( "signalement.prefix.origin.backoffice" );
    public static final String       SIGNALEMENT_PREFIX_KEY                                 = "signalement.prefix.origin";
    public static final List<String> SIGNALEMENT_PREFIXES                                   = SignalementUtils.getProperties( SIGNALEMENT_PREFIX_KEY );

    public static final int          START_SIGNALEMENT_NUMERO                               = 1;

    // workflow action
    public static final String       ID_ACTION_TRANSFERT_PARTNER                            = "signalement.daemon.idAction.transfert.partner";
    public static final String       ID_ACTION_DAEMON_SIGNALEMENT_DONE                      = "signalement.daemon.idAction.done";

    // State id
    public static final Integer      ID_STATE_NOUVEAU                                       = AppPropertiesService.getPropertyInt( "signalement.idStateNouveau", -1 );
    public static final Integer      ID_STATE_A_TRAITER                                     = AppPropertiesService.getPropertyInt( "signalement.idStateATraiter", -1 );
    public static final Integer      ID_STATE_PROGRAMME                                     = AppPropertiesService.getPropertyInt( "signalement.idStateProgramme", -1 );
    public static final Integer      ID_STATE_A_FAIRE_TERRAIN                               = AppPropertiesService.getPropertyInt( "signalement.idStateAFaireTerrain", -1 );
    public static final Integer      ID_STATE_A_FAIRE_BUREAU                                = AppPropertiesService.getPropertyInt( "signalement.idStateAFaireBureau", -1 );
    public static final Integer      ID_STATE_TRANSFERE_PRESTATAIRE                         = AppPropertiesService.getPropertyInt( "signalement.idStateTransferePrestataire", -1 );
    public static final Integer      ID_STATE_PROGRAMME_PRESTATAIRE                         = AppPropertiesService.getPropertyInt( "signalement.idStateServiceProgrammePrestataire", -1 );

    // Photo view
    public static final Integer      OVERVIEW                                               = 1;
    public static final Integer      DETAILED_VIEW                                          = 0;
    public static final Integer      SERVICE_DONE_VIEW                                      = 2;

    /**
     * Utility class - empty constructor
     */
    private SignalementConstants( )
    {
        // nothing
    }

}
