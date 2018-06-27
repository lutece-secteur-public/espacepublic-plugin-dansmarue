/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.plugins.dansmarue.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import au.com.bytecode.opencsv.CSVWriter;
import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.ConseilQuartier;
import fr.paris.lutece.plugins.dansmarue.business.entities.DomaineFonctionnel;
import fr.paris.lutece.plugins.dansmarue.business.entities.EtatSignalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Priorite;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.commons.Order;
import fr.paris.lutece.plugins.dansmarue.commons.ResultList;
import fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.FunctionnalException;
import fr.paris.lutece.plugins.dansmarue.service.IAdresseService;
import fr.paris.lutece.plugins.dansmarue.service.IArrondissementService;
import fr.paris.lutece.plugins.dansmarue.service.IConseilQuartierService;
import fr.paris.lutece.plugins.dansmarue.service.IDomaineFonctionnelService;
import fr.paris.lutece.plugins.dansmarue.service.IFileMessageCreationService;
import fr.paris.lutece.plugins.dansmarue.service.IPhotoService;
import fr.paris.lutece.plugins.dansmarue.service.IPrioriteService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ISignaleurService;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.plugins.dansmarue.service.actions.ISignalementAction;
import fr.paris.lutece.plugins.dansmarue.service.actions.SignalementFields;
import fr.paris.lutece.plugins.dansmarue.service.dto.SignalementExportCSVDTO;
import fr.paris.lutece.plugins.dansmarue.service.dto.SignalementMapMarkerDTO;
import fr.paris.lutece.plugins.dansmarue.service.impl.SignalementService;
import fr.paris.lutece.plugins.dansmarue.service.role.DomaineFonctionnelSignalementResourceIdService;
import fr.paris.lutece.plugins.dansmarue.service.role.SignalementViewRoleService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.plugins.dansmarue.utils.DirectionComparator;
import fr.paris.lutece.plugins.dansmarue.utils.ImgUtils;
import fr.paris.lutece.plugins.dansmarue.utils.ListUtils;
import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.plugins.dansmarue.utils.TypeSignalementItem;
import fr.paris.lutece.plugins.leaflet.modules.dansmarue.consts.LeafletDansMaRueConstants;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.modules.sira.business.sector.Sector;
import fr.paris.lutece.plugins.unittree.modules.sira.service.sector.ISectorService;
import fr.paris.lutece.plugins.unittree.modules.sira.service.unit.IUnitSiraService;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.fileupload.FileUploadService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.PluginActionManager;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.portal.web.util.LocalizedDelegatePaginator;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.image.ImageUtil;
import fr.paris.lutece.util.url.UrlItem;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONBuilder;

/**
 * The Class SignalementJspBean.
 */
public class SignalementJspBean extends AbstractJspBean
{

    // RIGHTS
    /** The Constant RIGHT_MANAGE_SIGNALEMENT. */
    public static final String            RIGHT_MANAGE_SIGNALEMENT                      = "SIGNALEMENT_MANAGEMENT";

    // TEMPLATES
    /** The Constant TEMPLATE_MANAGE_SIGNALEMENT. */
    private static final String           TEMPLATE_MANAGE_SIGNALEMENT                   = "admin/plugins/signalement/manage_signalement.html";

    /** The Constant TEMPLATE_DISPLAY_SIGNALEMENT */
    private static final String           TEMPLATE_DISPLAY_SIGNALEMENT                  = "admin/plugins/signalement/display_signalement.html";

    /** The Constant TEMPLATE_SAVE_SIGNALEMENT. */
    private static final String           TEMPLATE_SAVE_SIGNALEMENT                     = "admin/plugins/signalement/save_signalement.html";

    /** The Constant TEMPLATE_VIEW_SIGNALEMENT. */
    private static final String           TEMPLATE_VIEW_SIGNALEMENT                     = "admin/plugins/signalement/view_signalement.html";

    /** The Constant TEMPLATE_TASK_WORKFLOW. */
    private static final String           TEMPLATE_TASK_WORKFLOW                        = "admin/plugins/signalement/workflow/signalement_form.html";

    /** The Constant TEMPLATE_SIGNALEMENT_HISTORY. */
    private static final String           TEMPLATE_SIGNALEMENT_HISTORY                  = "admin/plugins/signalement/view_history_signalement.html";

    /** The Constant TEMPLATE_SAVE_PHOTO_TO_SIGNALEMENT. */
    private static final String           TEMPLATE_SAVE_PHOTO_TO_SIGNALEMENT            = "admin/plugins/signalement/add_photo_signalement.html";

    // JSP
    /** The Constant JSP_MANAGE_SIGNALEMENT. */
    private static final String           JSP_MANAGE_SIGNALEMENT                        = "jsp/admin/plugins/signalement/ManageSignalement.jsp";

    /** The Constant JSP_MANAGE_SIGNALEMENT. */
    private static final String           JSP_DELETE_SIGNALEMENT                        = "jsp/admin/plugins/signalement/DoDeleteSignalement.jsp";

    /** The Constant JSP_VIEW_SIGNALEMENT. */
    private static final String           JSP_VIEW_SIGNALEMENT                          = "ViewSignalement.jsp";

    /** The Constant JSP_MODIFY_SIGNALEMENT. */
    private static final String           JSP_MODIFY_SIGNALEMENT                        = "ModifySignalement.jsp";

    /** The Constant JSP_WORKFLOW_ACTION. */
    private static final String           JSP_WORKFLOW_ACTION                           = "jsp/admin/plugins/signalement/WorkflowAction.jsp";

    /** The Constant JSP_WORKFLOW__ACTION. */
    private static final String           JSP_WORKFLOW__ACTION                          = "jsp/admin/plugins/signalement/ProcessAction.jsp";

    /** The Constant JSP_DO_PROCESS_ACTION. */
    private static final String           JSP_DO_PROCESS_ACTION                         = "jsp/admin/plugins/signalement/DoProcessActionWorkflow.jsp";

    /** The Constant JSP_DO_PROCESS_MASS_ACTION. */
    private static final String           JSP_DO_PROCESS_MASS_ACTION                    = "jsp/admin/plugins/signalement/DoMassProcessActionWorkflow.jsp";

    /** The Constant JSP_DO_PROCESS_MASS_ACTION. */
    private static final String           JSP_DELETE_PHOTO_SIGNALEMENT                  = "jsp/admin/plugins/signalement/DoDeletePhotoSignalement.jsp";

    /** The Constant JSP_DO_PROCESS_MASS_ACTION. */
    private static final String           JSP_MASS_DELETE_SIGNALEMENT                   = "jsp/admin/plugins/signalement/DoMassDeleteSignalement.jsp";

    /** The Constant JSP_ADD_PHOTO_TO_SIGNALEMENT. */
    private static final String           JSP_ADD_PHOTO_TO_SIGNALEMENT                  = "AddPhotoToSignalement.jsp";

    /** The Constant URL_JSP_DISPLAY_SIGNALEMENTS **/
    private static final String           URL_JSP_DISPLAY_SIGNALEMENTS                  = "jsp/admin/plugins/signalement/DisplaySignalement.jsp";

    /** The Constant URL_JSP_GET_ROAD_MAP **/
    private static final String           URL_JSP_GET_ROAD_MAP                          = "jsp/admin/plugins/ramen/GetRoadMap.jsp";

    /** The Constant CSV_ISO. */
    private static final String           CSV_ISO                                       = "ISO-8859-1";

    // PARAMETERS
    /** The Constant PARAMETER_SIGNALEMENT_ID. */
    public static final String            PARAMETER_SIGNALEMENT_ID                      = "signalement_id";

    /** The Constant PARAMETER_PRIORITE. */
    public static final String            PARAMETER_PRIORITE                            = "priorite";

    /** The Constant PARAMETER_TYPE_SIGNALEMENT. */
    public static final String            PARAMETER_TYPE_SIGNALEMENT                    = "typeSignalement";

    /** The Constant PARAMETER_ADRESSE. */
    public static final String            PARAMETER_ADRESSE                             = "adresse";

    /** The Constant PARAMETER_MAIL. */
    public static final String            PARAMETER_MAIL                                = "mail";

    /** The Constant PARAMETER_PRECISION_LOCALISATION. */
    public static final String            PARAMETER_PRECISION_LOCALISATION              = "precisionLocalisation";

    /** The Constant PARAMETER_COMMENTAIRE. */
    public static final String            PARAMETER_COMMENTAIRE                         = "commentaire";

    /** The Constant PARAMETER_PHOTO. */
    public static final String            PARAMETER_PHOTO                               = "photo";

    /** The Constant PARAMETER_VUE_PHOTO. */
    public static final String            PARAMETER_VUE_PHOTO                           = "vuePhoto";

    /** The Constant PARAMETER_PHOTO_ENSEMBLE. */
    public static final String            PARAMETER_PHOTO_ENSEMBLE                      = "photoEnsemble";

    /** The Constant PARAMETER_PHOTO_PRES. */
    public static final String            PARAMETER_PHOTO_PRES                          = "photoPres";

    /** The Constant PARAMETER_PHOTO_MODIFY. */
    public static final String            PARAMETER_PHOTO_MODIFY                        = "photoModify";

    /** The Constant PARAMETER_ADRESSE_ID. */
    public static final String            PARAMETER_ADRESSE_ID                          = "adresse_id";

    /** The Constant PARAMETER_PHOTO_ID. */
    public static final String            PARAMETER_PHOTO_ID                            = "photo_id";

    /** The Constant PARAMETER_SIGNALEUR_ID. */
    public static final String            PARAMETER_SIGNALEUR_ID                        = "signaleur_id";

    /** The Constant PARAMETER_TELEPHONE_ID. */
    public static final String            PARAMETER_TELEPHONE_ID                        = "telephone_id";

    /** The Constant PARAMETER_TYPE_SIGNALEMENT_ID. */
    public static final String            PARAMETER_TYPE_SIGNALEMENT_ID                 = "typesignalement_id";

    /** The Constant PARAMETER_COMMENTAIRE_PROG. */
    public static final String            PARAMETER_COMMENTAIRE_PROG                    = "commentaireProgrammation";

    /** The Constant PARAMETER_ACTION_ID. */
    public static final String            PARAMETER_ACTION_ID                           = "action_id";

    /** The Constant PARAMETER_ACTION_TYPE. */
    public static final String            PARAMETER_ACTION_TYPE                         = "action_type";

    /** The Constant PARAMETER_NB_PHOTOS. */
    public static final String            PARAMETER_NB_PHOTOS                           = "nbPhotos";

    /** The Constant PARAMETER_LNG. */
    private static final String           PARAMETER_LNG                                 = "lng";

    /** The Constant PARAMETER_LAT. */
    private static final String           PARAMETER_LAT                                 = "lat";

    /** The Constant PARAMETER_ACTION_NEXT. */
    private static final String           PARAMETER_ACTION_NEXT                         = "next";

    /** The Constant PARAMETER_IS_ROAD_MAP. */
    private static final String           PARAMETER_IS_ROAD_MAP                         = "isRoadMap";

    /** The Constant PARAMETER_DATE_SERVICE. */
    private static final String           PARAMETER_DATE_SERVICE                        = "dateService";

    /** The Constant PARAMETER_SERVICE_ID. */
    private static final String           PARAMETER_SERVICE_ID                          = "serviceId";

    /** The Constant PARAMETER_SERVICE__ID. */
    private static final String           PARAMETER_SERVICE__ID                          = "service_id";

    /** The Constant PARAMETER_DIRECTION_ID. */
    private static final String           PARAMETER_DIRECTION_ID                        = "direction_id";

    /** The Constant PARAMETER_FROM_PAGE **/
    private static final String           PARAMETER_FROM_PAGE                           = "from_page";

    /** The Constant PARAMETER_VALUE_DISPLAY_PAGE **/
    private static final String           PARAMETER_VALUE_DISPLAY_PAGE                  = "display_page";
    
    private static final String           PARAMETER_NEXT_URL                            = "next";
    
    private static final String           PARAMETER_WEBAPP_RAMEN                        = "ramen";
    
    private static final String           PARAMETER_SECTOR__ID                          = "sector_id";
    
    private static final String           PARAMETER_UNIT__ID                            = "unit_id";

    /** The Constant JSON_KEY_ID. */
    private static final String           JSON_KEY_ID                                   = "id";

    /** The Constant ZERO_VOTE. */
    private static final Integer          ZERO_VOTE                                     = 0;

    // MARKERS
    /** The Constant MARK_PRIORITE_LIST. */
    private static final String           MARK_PRIORITE_LIST                            = "priorite_list";

    /** The Constant MARK_TYPE_LIST. */
    private static final String           MARK_TYPE_LIST                                = "type_list";

    /** The Constant MARK_SIGNALEMENT. */
    private static final String           MARK_SIGNALEMENT                              = "signalement";

    /** The Constant MARK_SIGNALEMENT_ID. */
    private static final String           MARK_SIGNALEMENT_ID                           = "signalement_id";

    /** The Constant MARK_TITLE. */
    public static final String            MARK_TITLE                                    = "title";

    /** The Constant MARK_TYPE. */
    public static final String            MARK_TYPE                                     = "type";

    /** The Constant MARK_PHOTOS. */
    public static final String            MARK_PHOTOS                                   = "photos";

    /** The Constant MARK_ADRESSE. */
    public static final String            MARK_ADRESSE                                  = "adresse";

    /** The Constant MARK_SIGNALEUR. */
    public static final String            MARK_SIGNALEUR                                = "signaleur";

    /** The Constant MARK_DIRECTION_LIST. */
    private static final String           MARK_DIRECTION_LIST                           = "direction_list";

    /** The Constant BUTTON_UPDATE_SECTOR_WITH_DIRECTION. */
    private static final String           BUTTON_UPDATE_SECTOR_WITH_DIRECTION           = "updateSectorsWithDirection";

    /** The Constant MARK_ARRONDISSEMENT_LIST. */
    private static final String           MARK_ARRONDISSEMENT_LIST                      = "arrondissement_list";

    /** The Constant MARK_CONSEIL_QUARTIER_LIST. */
    private static final String           MARK_CONSEIL_QUARTIER_LIST                    = "conseilQuartier_list";

    /** The Constant MARK_ETATS_LIST. */
    private static final String           MARK_ETATS_LIST                               = "etat_list";

    /** The Constant MARK_ETATS. */
    private static final String           MARK_ETATS                                    = "map_etats";
    
    /** The Constant MARK_HAS_SIGNALEMENT_PRESTATAIRE. */
    private static final String           MARK_HAS_SIGNALEMENT_PRESTATAIRE              = "hasSignalementPrestataire";

    /** The Constant MARK_ACTIONS. */
    private static final String           MARK_ACTIONS                                  = "map_actions";

    /** The Constant MARK_POSSIBLE_ACTIONS */
    private static final String           MARK_POSSIBLE_ACTIONS                         = "possible_actions";

    /** The Constant MARK_SIGNALEMENT_LIST. */
    private static final String           MARK_SIGNALEMENT_LIST                         = "signalement_list";

    /** The Constant MARK_TASK_FORM. */
    private static final String           MARK_TASK_FORM                                = "task_form";

    /** The Constant MARK_ACTION_ID. */
    private static final String           MARK_ACTION_ID                                = "action_id";

    /** The Constant MARK_HAS_NEXT. */
    private static final String           MARK_HAS_NEXT                                 = "has_next";

    /** The Constant MARK_STATE_SIGNALEMENT. */
    private static final String           MARK_STATE_SIGNALEMENT                        = "stateSignalement";
    
    /** The Constant MARL_STATE_SERVICE_FAIT. */
    private static final String           MARK_STATE_SERVICE_FAIT                       = "serviceFaitValue";    
    
    /** The Constant MARL_USER_SERVICE_FAIT. */
    private static final String           MARK_USER_SERVICE_FAIT                        = "serviceFaitUser"; 

    /** The Constant MARK_NO_VALID_ADDRESSES. */
    public static final String            MARK_NO_VALID_ADDRESSES                       = "noValidAddresses";

    /** The Constant MARK_PROPOSED_ADDRESSES. */
    public static final String            MARK_PROPOSED_ADDRESSES                       = "proposedAddresses";

    /** The Constant MARK_HISTORIQUE_LIST. */
    private static final String           MARK_HISTORIQUE_LIST                          = "historiqueHtml";

    /** The Constant MARK_SECTEUR_LIST. */
    private static final String           MARK_SECTEUR_LIST                             = "secteur_list";

    /** The Constant MARK_MESSAGE_FOR_USER. */
    private static final String           MARK_MESSAGE_FOR_USER                         = "messageForUser";

    /** The Constant MARK_KEY_MAPS. */
    private static final String           MARK_KEY_MAPS                                 = "key_maps";

    /** The Constant MARK_ID_ETATS_DEFAULT. */
    private static final String           MARK_ID_ETATS_DEFAULT                         = "etats_default";

    /** The constant MARK_BACK_URL */
    private static final String           MARK_BACK_URL                                 = "back_url";

    private static final String           MARK_ADVANCED_SEARCH_STATES                   = "advanced_searched_states";
    private static final String           MARK_DASHBOARD_CRITERIAS                      = "dashboard_criterias";
    private static final String           MARK_MAP_MAX_RESULTS                          = "map_max_results";
    private static final String           MARK_HAS_ADVANCED_CRITERIAS                   = "has_advanced_criterias";

    /** The Constant VUE_ENSEMBLE */
    public static final Integer           VUE_ENSEMBLE                                  = 1;

    /** The Constant VUE_PRES */
    public static final Integer           VUE_PRES                                      = 0;

    /** The Constant ID_JARDIN */
    public static final Integer           ID_JARDIN                                     = 260;

    /** The Constant ID_RACINE */
    public static final Integer           ID_RACINE                                     = 0;

    // I18N
    /** The Constant PAGE_TITLE_MANAGE_SIGNALEMENT. */
    private static final String           PAGE_TITLE_MANAGE_SIGNALEMENT                 = "dansmarue.page.signalement.manage.title";

    // CONSTANTS
    /** The Constant EMPTY_STRING. */
    private static final String           EMPTY_STRING                                  = "";

    /** The Constant MINUS_ONE. */
    private static final String           MINUS_ONE                                     = "-1";

    /** The Constant NO_RESOURCE_FOUND. */
    private static final int              NO_RESOURCE_FOUND                             = -1;

    /** The Constant ACTION_MESSAGE_TASK. */
    private static final int[]            ACTION_MESSAGE_TASK                           = { 13, 18, 22 };

    /** The Constant ID_ACTIONS_SERVICE_FAIT. */
    private static final int[]            ID_ACTIONS_SERVICE_FAIT                       = { 18, 22 };

    /** The Constant ID_ACTIONS_ACCEPTER. */
    private static final int[]            ID_ACTIONS_ACCEPTER                           = { 13 };

    /** The Constant ID_ETATS_DEFAULT. */
    private static final int[]            ID_ETATS_DEFAULT                              = { 7, 8, 16, 17, 18, 21 };
    
    private static final String           ID_STATE_SERVICE_PROGRAMME_PRESTATAIRE  = "signalement.idStateServiceProgrammePrestataire";
    
    private static final String           ID_STATE_TRANSFERE_PRESTATAIRE          = "signalement.idStateTransferePrestataire";

    /** The Constant CSV_SEPARATOR. */
    private static final char             CSV_SEPARATOR                                 = ';';

    /** The Constant STATE_INIT. */
    private static final String           STATE_INIT                                    = "signalement.state.inital";

    // MESSAGES
    /** The Constant MESSAGE_ERROR_EMPTY_FIELD. */
    private static final String           MESSAGE_ERROR_EMPTY_FIELD                     = "dansmarue.message.error.champObligatoireNull";

    /** The Constant MESSAGE_ERREUR_HORS_PARIS. */
    private static final String           MESSAGE_ERREUR_HORS_PARIS                     = "dansmarue.message.error.horsParis";

    /** The Constant MESSAGE_ERREUR_SECTEUR_NULL. */
    private static final String           MESSAGE_ERREUR_SECTEUR_NULL                   = "dansmarue.message.error.aucunSecteur";

    /** The Constant MESSAGE_ERROR_EMPTY_PHOTO_FIELD. */
    private static final String           MESSAGE_ERROR_EMPTY_PHOTO_FIELD               = "dansmarue.message.error.emptyPhoto";
    
    /** The Constant MESSAGE_ERROR_EXISTING_PHOTO. */
    private static final String           MESSAGE_ERROR_EXISTING_PHOTO                  = "dansmarue.message.error.photo.existante";

    /** The Constant MESSAGE_TITLE_MASS_ACTION_IMPOSSIBLE. */
    private static final String           MESSAGE_TITLE_MASS_ACTION_IMPOSSIBLE          = "dansmarue.messagetitle.massActionImpossible.message";

    /** The Constant MESSAGE_TITLE_DELETE_PHOTO_SIGNALEMENT. */
    private static final String           MESSAGE_TITLE_DELETE_PHOTO_SIGNALEMENT        = "dansmarue.messagetitle.deletePhotoSignalement";

    /** The Constant MESSAGE_TITLE_SERVICE_FAIT. */
    private static final String           MESSAGE_TITLE_SERVICE_FAIT                    = "dansmarue.messagetitle.servicefait.confirmation";

    /** The Constant MESSAGE_CONFIRMATION_SERVICE_FAIT. */
    private static final String           MESSAGE_CONFIRMATION_SERVICE_FAIT             = "dansmarue.message.servicefait.confirmation";

    /** The Constant MESSAGE_CONFIRMATION_DELETE_PHOTO_SIGNALEMENT. */
    private static final String           MESSAGE_CONFIRMATION_DELETE_PHOTO_SIGNALEMENT = "dansmarue.message.deletePhotoSignalement";

    /** The Constant MESSAGE_TITLE_MASS_SERVICE_FAIT. */
    private static final String           MESSAGE_TITLE_MASS_SERVICE_FAIT               = "dansmarue.messagetitle.massActionServiceFait.confirmation";

    /** The Constant MESSAGE_TITLE_MASS_DELETE_SIGNALEMENT. */
    private static final String           MESSAGE_CONFIRMATION_MASS_DELETE_SIGNALEMENT  = "dansmarue.message.massDeleteSignalement.confirmation";

    /** The Constant MESSAGE_CONFIRMATION_MASS_SERVICE_FAIT. */
    private static final String           MESSAGE_CONFIRMATION_MASS_SERVICE_FAIT        = "dansmarue.message.massActionServiceFait.confirmation";

    /** The Constant MESSAGE_TITLE_ACCEPTER. */
    private static final String           MESSAGE_TITLE_ACCEPTER                        = "dansmarue.messagetitle.Accepter.confirmation";

    /** The Constant MESSAGE_CONFIRMATION_ACCEPTER. */
    private static final String           MESSAGE_CONFIRMATION_ACCEPTER                 = "dansmarue.message.Accepter.confirmation";

    /** The Constant MESSAGE_TITLE_MASS_ACCEPTER. */
    private static final String           MESSAGE_TITLE_MASS_ACCEPTER                   = "dansmarue.messagetitle.massActionAccepter.confirmation";

    /** The Constant MESSAGE_CONFIRMATION_MASS_ACCEPTER. */
    private static final String           MESSAGE_CONFIRMATION_MASS_ACCEPTER            = "dansmarue.message.massActionAccepter.confirmation";

    /** The Constant MESSAGE_ACCESS_DENIED. */
    private static final String           MESSAGE_ACCESS_DENIED                         = "user does not have the right to do this";

    /** The Constant MESSAGE_TITLE_MASS_DELETE_SIGNALEMENT. */
    private static final String           MESSAGE_TITLE_MASS_DELETE_SIGNALEMENT         = "dansmarue.messagetitle.massDeleteSignalement.confirmation";

    /** The Constant MESSAGE_CONFIRMATION_DELETE_SIGNALEMENT. */
    private static final String           MESSAGE_CONFIRMATION_DELETE_SIGNALEMENT       = "dansmarue.messagetitle.deleteSignalement.confirmation";

    /** The Constant MESSAGE_TITLE_DELETE_SIGNALEMENT. */
    private static final String           MESSAGE_TITLE_DELETE_SIGNALEMENT              = "dansmarue.messagetitle.deleteSignalement.title";
    
    private static final String           MESSAGE_RAMEN_WS_ERROR                        = "dansmarue.ramen.webservice.erreur";
    
    /** The Constant USAGER_MOBILE */
    public static final String           USAGER_MOBILE                                  = "Usager mobile";

    // PROPERTIES
    private static final String           PROPERTY_FILE_FOLDER_PATH                     = "signalement.pathForFileMessageCreation";
    private static final String           PROPERTY_UNITS_RADIUS                         = "signalement.near.units.radius";
    private static final String           PROPERTY_MARKER_STATES_GREEN                  = "signalement.map.markers.states.green";
    private static final String           PROPERTY_MARKER_STATES_YELLOW                 = "signalement.map.markers.states.yellow";
    private static final String           PROPERTY_ADVANCED_SEARCH_STATES               = "signalement.reportingList.advancedsearch.states";
    private static final String           PROPERTY_MAP_MAX_RESULTS                      = "signalement.map.max.results";
    private static final String           PROPERTY_ARRONDISSEMENT_RESTRICT_UNIT         = "signalement.reportingList.arrondissement.restrict.units";

    // MEMBERS VARIABLES
    /** The _signalement filter. */
    private SignalementFilter             _signalementFilter;

    /** The _mass signalement ids. */
    private int[]                         _massSignalementIds;

    /** The _action type. */
    private String                        _actionType;

    private List<Arrondissement>          arrondissements;

    private List<TypeSignalement>         typesAnomalies;

    private List<Integer>                 dashboardSignalementList;
    private Map<String, List<String>>     dashboardCriterias;
    private Integer                       anomaliesCount;

    // SERVICES
    /** The _signalement service. */
    private ISignalementService           _signalementService;

    /** The _type signalement service. */
    private ITypeSignalementService       _typeSignalementService;

    /** The _priorite service. */
    private IPrioriteService              _prioriteService;

    /** The _signaleur service. */
    private ISignaleurService             _signaleurService;

    /** The _unit service. */
    private IUnitService                  _unitService;

    /** The _signalement workflow service. */
    private IWorkflowService              _signalementWorkflowService;

    /** The _adresse service. */
    private IAdresseService               _adresseService;

    /** The _photo service. */
    private IPhotoService                 _photoService;

    /** The _sector service. */
    private ISectorService                _sectorService;

    /** The _arrondissement service. */
    private IArrondissementService        _arrondissementService;

    /** The conseilQuartier service. */
    private IConseilQuartierService       _conseilQuartier;

    private IUnitSiraService              _unitSiraService;

    /** The _fileMessageCreation service. */
    private IFileMessageCreationService   _fileMessageCreationService;

    /** The _signalementViewRole service */
    private SignalementViewRoleService    _signalementViewRoleService;

    /** The _domaineFonctionnelService */
    private IDomaineFonctionnelService    _domaineFonctionnelService;

    // parameters
    private static final String           PARAMETER_SEARCH                              = "search";

    /** The Constant PARAMETER_ACTION_SERVICE_FAIT PM 2015. */
    private static final String           PARAMETER_ACTION_SERVICE_FAIT                 = "servicefait";

    /** The Constant PARAMETER_ACTION_SERVICE_FAIT. */
    private static final String           PARAMETER_ACTION_SERVICE_FAITS                = "servicefaits";

    /** The Constant PARAMETER_ACTION_PROGRAMMER. */
    private static final String           PARAMETER_ACTION_PROGRAMMER                   = "programmer";

    /** The Constant PARAMETER_ACTION_REPROGRAMMER. */
    private static final String           PARAMETER_ACTION_REPROGRAMMER                 = "reprogrammer";

    // Actions
    /** The Constant ACTION_SERVICE_FAIT. */
    private static final String           ACTION_SERVICE_FAIT                           = "Déclarer un service fait";

    /** The Constant ACTION_PROGRAMMER. */
    private static final String           ACTION_PROGRAMMER                             = "Programmer";

    /** The Constant ACTION_REPROGRAMMER. */
    private static final String           ACTION_REPROGRAMMER                           = "Reprogrammer";

    private static final int              MESSAGE_ERROR_EMPTY                           = 51;

    private static final SimpleDateFormat sdfDate                                       = new SimpleDateFormat( DateUtils.DATE_FR );

    private static List<Integer>          GREEN_MARKER_STATES;
    private static List<Integer>          YELLOW_MARKER_STATES;
    private static List<Integer>          ADVANCED_SEARCH_STATES;

    @Override
    public void init( HttpServletRequest request, String strRight ) throws AccessDeniedException
    {
        super.init( request, strRight );
        initServices( );
        initStaticMembers( );
    }

    @Override
    public void init( HttpServletRequest request, String strRight, String keyResourceType, String permission ) throws AccessDeniedException
    {
        super.init( request, strRight, keyResourceType, permission );
        initServices( );
        initStaticMembers( );

    }

    public void checkUserDomains( HttpServletRequest request ) throws AccessDeniedException
    {
        Collection<DomaineFonctionnel> domainesFonctionnels = _domaineFonctionnelService.getAllDomainesFonctionnelActifs( );
        domainesFonctionnels = RBACService.getAuthorizedCollection( domainesFonctionnels, DomaineFonctionnelSignalementResourceIdService.PERMISSION_CONSULT_SIGNALEMENT, getUser( ) );
        if ( CollectionUtils.isEmpty( domainesFonctionnels ) )
        {
            throw new AccessDeniedException( MessageFormat.format( "Acces denied for {0}.", DomaineFonctionnelSignalementResourceIdService.PERMISSION_CONSULT_SIGNALEMENT ) );
        }
    }

    public void initStaticMembers( )
    {
        // Init map markers
        if ( ( null == GREEN_MARKER_STATES ) || ( null == YELLOW_MARKER_STATES ) )
        {
            GREEN_MARKER_STATES = new ArrayList<>( );
            YELLOW_MARKER_STATES = new ArrayList<>( );

            String greenStates = AppPropertiesService.getProperty( PROPERTY_MARKER_STATES_GREEN );
            String yellowStates = AppPropertiesService.getProperty( PROPERTY_MARKER_STATES_YELLOW );

            String[] greenStatesArr = greenStates.split( "," );
            String[] yellowStatesArr = yellowStates.split( "," );

            for ( String greenState : greenStatesArr )
            {
                GREEN_MARKER_STATES.add( Integer.parseInt( greenState ) );
            }

            for ( String yellowState : yellowStatesArr )
            {
                YELLOW_MARKER_STATES.add( Integer.parseInt( yellowState ) );
            }
        }
        // Init advanced search states
        if ( ADVANCED_SEARCH_STATES == null )
        {
            String[] arrStates = AppPropertiesService.getProperty( PROPERTY_ADVANCED_SEARCH_STATES ).split( "," );
            List<String> statesList = Arrays.asList( arrStates );
            ADVANCED_SEARCH_STATES = statesList.stream( ).map( Integer::valueOf ).collect( Collectors.toList( ) );
        }

    }

    /**
     * Inits the services.
     */
    public void initServices( )
    {
        _signalementService = ( ISignalementService ) SpringContextService.getBean( "signalementService" );
        _typeSignalementService = ( ITypeSignalementService ) SpringContextService.getBean( "typeSignalementService" );
        _prioriteService = ( IPrioriteService ) SpringContextService.getBean( "prioriteService" );
        _signaleurService = ( ISignaleurService ) SpringContextService.getBean( "signaleurService" );
        _unitService = ( IUnitService ) SpringContextService.getBean( "unittree.unitService" );
        _signalementWorkflowService = ( IWorkflowService ) SpringContextService.getBean( "signalement.workflowService" );
        _adresseService = ( IAdresseService ) SpringContextService.getBean( "adresseSignalementService" );
        _photoService = ( IPhotoService ) SpringContextService.getBean( "photoService" );
        _sectorService = ( ISectorService ) SpringContextService.getBean( "unittree-sira.sectorService" );
        _arrondissementService = ( IArrondissementService ) SpringContextService.getBean( "signalement.arrondissementService" );
        _conseilQuartier = ( IConseilQuartierService ) SpringContextService.getBean( "signalement.conseilQuartierService" );
        _fileMessageCreationService = ( IFileMessageCreationService ) SpringContextService.getBean( "fileMessageCreationService" );
        _signalementViewRoleService = ( SignalementViewRoleService ) SpringContextService.getBean( "signalement.signalementViewRoleService" );
        _domaineFonctionnelService = ( IDomaineFonctionnelService ) SpringContextService.getBean( "domaineFonctionnelService" );
        _unitSiraService = ( IUnitSiraService ) SpringContextService.getBean( "unittree-sira.unitSiraService" );
    }

    /**
     * Instantiates a new signalement jsp bean.
     */
    public SignalementJspBean( )
    {
        super( );
    }

    public List<Integer> getDashboardSignalementList( )
    {
        return dashboardSignalementList;
    }

    public void setDashboardSignalementList( List<Integer> dashboardSignalementList )
    {
        this.dashboardSignalementList = dashboardSignalementList;
    }

    public Map<String, List<String>> getDashboardCriterias( )
    {
        return dashboardCriterias;
    }

    public void setDashboardCriterias( Map<String, List<String>> dashboardCriterias )
    {
        this.dashboardCriterias = dashboardCriterias;
    }

    /**
     * Get the signalement management page.
     *
     * @param request
     *            The HTTP request
     * @return redirection url
     */
    public String getManageSignalement( HttpServletRequest request )
    {
        // remove mass action properties
        clearMassAction( request );

        // Remove dashboard signalement if search performed
        String actionSearch = request.getParameter( PARAMETER_SEARCH );
        if ( null != actionSearch )
        {
            dashboardSignalementList = null;
        }

        SignalementFilter filter = getSignalementFilter( request );
        AdminUser adminUser = AdminUserService.getAdminUser( request );
        applyViewRoleRestrictionToFilter( request, adminUser );

        ///////////////////////////////////////////
        //// THE DIVISION LIST ALLOWED BY USER ////
        ///////////////////////////////////////////
        List<Unit> listUnits = _unitService.getUnitsByIdUser( adminUser.getUserId( ), false );
        
        ReferenceList listeDirections = new ReferenceList( );
        List<Sector> listSectorsOfUnits = new ArrayList<Sector>( );
        ReferenceList listeArrondissement = new ReferenceList( );
        boolean bHasAllArrondissements = false;
        List<Integer> listAllowedArrond = new ArrayList<Integer>( );

        boolean bIsUserRestrictedByViewRoles = _signalementViewRoleService.checkIsUserRestrictedByViewRoles( adminUser.getUserId( ), request );

        for ( Unit userUnit : listUnits )
        {
            ReferenceList newDirections;
            if ( userUnit.getIdParent( ) != -1 )
            {
                // rights on a specific unit
                List<Unit> direction = new ArrayList<Unit>( );
                // get the root unit (allowed) for the user
                Unit temp = userUnit;
                while ( temp.getIdUnit( ) != 0 )
                {
                    if ( temp.getIdParent( ) == 0 )
                    {
                        direction.add( temp );
                    }

                    temp = _unitService.getUnit( temp.getIdParent( ), true );
                }

                newDirections = ListUtils.toReferenceList( direction, "idUnit", "label", null );
            } else
            {
                // the direction list
                newDirections = ListUtils.toReferenceList( _unitService.getUnitsFirstLevel( false ), "idUnit", "label", null );
            }

            if ( ( newDirections != null ) && ( newDirections.size( ) > 0 ) )
            {
                for ( ReferenceItem refItem : listeDirections )
                {
                    for ( ReferenceItem refItemToAdd : newDirections )
                    {
                        // If the item already exists in the collection
                        if ( StringUtils.equals( refItemToAdd.getCode( ), refItem.getCode( ) ) )
                        {
                            newDirections.remove( refItemToAdd );
                            break;
                        }
                    }
                }
                listeDirections.addAll( newDirections );
            }

            // the sectors list
            // get the user's allowed sectors

            if ( filter.getIdDirection( ) <= 0 )
            {
                for ( Sector secteurUnit : _sectorService.loadByIdUnitWithoutChosenId( userUnit.getIdUnit( ), ID_JARDIN ) )
                {
                    boolean bFound = false;
                    for ( Sector secteur : listSectorsOfUnits )
                    {
                        if ( secteur.getIdSector( ) == secteurUnit.getIdSector( ) )
                        {
                            bFound = true;
                            break;
                        }
                    }
                    if ( !bFound )
                    {
                        listSectorsOfUnits.add( secteurUnit );
                    }
                }
            }

            ///////////////////////////////////////
            ///////////////////////////////////////

            List<Integer> unitsIds = ListUtils.getPropertyListInt( PROPERTY_ARRONDISSEMENT_RESTRICT_UNIT );

            if ( !bHasAllArrondissements )
            {
                boolean isChildOfRestrictedUnit = false;
                Unit temp = userUnit;
                while ( ( temp.getIdUnit( ) != 0 ) && !isChildOfRestrictedUnit )
                {
                    if ( unitsIds != null ) {                        
                        if( !unitsIds.isEmpty( ) || unitsIds.contains( temp.getIdParent( ) ) ){     
                            isChildOfRestrictedUnit = true;
                        }
                    } 
                    temp = _unitService.getUnit( temp.getIdParent( ), true );
                }

                ////////////////////////////////
                // the arrondissement list ///
                ////////////////////////////////

                if ( isChildOfRestrictedUnit )
                {
                    List<Integer> listIdAllowedSectors = _sectorService.getIdsSectorByIdUnit( userUnit.getIdUnit( ) );

                    for ( Integer nArrId : _arrondissementService.getArrondissementsInSector( listIdAllowedSectors ) )
                    {
                        if ( !listAllowedArrond.contains( nArrId ) )
                        {
                            listAllowedArrond.add( nArrId );
                        }
                    }
                } else
                {
                    listeArrondissement = ListUtils.toReferenceList( _arrondissementService.getAllArrondissement( ), "id", "numero", null );
                    bHasAllArrondissements = true;
                }
            }
        }

        // If we didn't added every arrondissement we add the selected ones
        if ( !bHasAllArrondissements )
        {
            List<Arrondissement> listAllowedArrondissements = new ArrayList<Arrondissement>( );
            for ( Integer nArrId : listAllowedArrond )
            {
                listAllowedArrondissements.add( _arrondissementService.getByIdArrondissement( nArrId ) );
            }
            Collections.sort( listAllowedArrondissements );
            listeArrondissement.addAll( ListUtils.toReferenceList( listAllowedArrondissements, "id", "numero", null ) );
        }

        if ( bIsUserRestrictedByViewRoles && ( filter.getListIdArrondissements( ) != null ) )
        {
            listeArrondissement = ListUtils.retainReferenceList( listeArrondissement, _signalementViewRoleService.getUserRestrictedArrondissementList( adminUser.getUserId( ) ), false );
        }

        // Fill the map
        Map<String, Object> model = new HashMap<String, Object>( );

        model.put( MARK_ARRONDISSEMENT_LIST, listeArrondissement );

        List<ConseilQuartier> listeConseilQuartier = _conseilQuartier.selectQuartiersList( );
        model.put( MARK_CONSEIL_QUARTIER_LIST, listeConseilQuartier );

        ReferenceItem refItem = new ReferenceItem( );
        refItem.setCode( Long.toString( -1 ) );
        refItem.setName( StringUtils.EMPTY );
        listeDirections.add( 0, refItem );
        SignalementUtils.changeUnitDEVEIntoSEJ( listeDirections );
        model.put( MARK_DIRECTION_LIST, listeDirections );

        if ( filter.getIdDirection( ) > 0 )
        {
            //Specificity for DEVE entity, change the id from SEJ to DEVE
            if ( filter.getIdDirection( ) == 94 )
            {
                filter.setIdDirection( 1 );
            }
            listSectorsOfUnits = _sectorService.loadByIdUnitWithoutChosenId( filter.getIdDirection( ), ID_JARDIN );
        }

        ReferenceList listeSecteur = ListUtils.toReferenceList( listSectorsOfUnits, "idSector", "name", StringUtils.EMPTY, true );
        model.put( MARK_SECTEUR_LIST, listeSecteur );

        // the TypeSignalement list
        // get all the type signalement without children
        List<TypeSignalement> types = _typeSignalementService.getAllTypeSignalementActif( );

        if ( bIsUserRestrictedByViewRoles && ( filter.getListIdTypeSignalements( ) != null ) && ( filter.getListIdTypeSignalements( ).size( ) > 0 ) )
        {
            List<Integer> filterIdTypes = filter.getListIdTypeSignalements( );
            if ( !CollectionUtils.isEmpty( filterIdTypes ) )
            {
                Iterator<TypeSignalement> iteratorType = types.iterator( );
                while ( iteratorType.hasNext( ) )
                {
                    TypeSignalement type = iteratorType.next( );
                    if ( !filterIdTypes.contains( type.getId( ) ) )
                    {
                        iteratorType.remove( );
                    }
                }
            }
        }
        model.put( MARK_TYPE_LIST, types );

        // Gets the category list (typeSignalement roots)
        // List<TypeSignalement> categories = this._typeSignalementService.getAllTypeSignalementActifWithoutParent();
        // ReferenceList listeCategories = ListUtils.toReferenceList(categories, "id", "libelle", null,
        // false);
        // if (bIsUserRestrictedByViewRoles && filter.getListIdTypeSignalements() != null
        // && filter.getListIdTypeSignalements().size() > 0) {
        // listeCategories = ListUtils.retainReferenceList(listeCategories, filter.getListIdTypeSignalements(),
        // true);
        // }
        // model.put(MARK_CATEGORY_LIST, listeCategories);

        // the etat list
        List<State> listeEtat = getListeEtats( );
        model.put( MARK_ETATS_LIST, listeEtat );

        Integer totalResult = _signalementService.countIdSignalementByFilter( filter, getPlugin( ) );

        List<Signalement> listSignalements = null;

        if ( dashboardSignalementList != null )
        {
            totalResult = dashboardSignalementList.size( );
            PaginationProperties paginationProperties = getPaginationProperties( request, totalResult );

            List<Signalement> dashboardSignalements = _signalementService.getByIds( dashboardSignalementList, paginationProperties.getItemsPerPage( ) * ( paginationProperties.getPageIndex( ) - 1 ),
                    paginationProperties.getItemsPerPage( ) * paginationProperties.getPageIndex( ) );

            listSignalements = dashboardSignalements;
        } else
        {
            listSignalements = _signalementService.findByFilter( filter, getPaginationProperties( request, totalResult ), true );
        }

        // get the first unit linked to the signalement sector
        // Map of idSector and Unit instance
        Map<String, Unit> mapUnits = new HashMap<>( );
        for ( Signalement signalement : listSignalements )
        {
            Sector sector = signalement.getSecteur( );
            if ( mapUnits.containsKey( String.valueOf( sector.getIdSector( ) ) ) )
            {
                signalement.setDirectionSector( mapUnits.get( String.valueOf( sector.getIdSector( ) ) ) );
            } else
            {
                List<Unit> listUnitsSector = _unitService.findBySectorId( sector.getIdSector( ) );
                for ( Unit unit : listUnitsSector )
                {
                    if ( unit.getIdParent( ) == 0 )
                    {
                        signalement.setDirectionSector( unit );
                        mapUnits.put( String.valueOf( sector.getIdSector( ) ), unit );
                    }
                }
            }
        }

        ResultList<Signalement> resultListSignalement = new ResultList<Signalement>( );

        resultListSignalement.addAll( listSignalements );

        resultListSignalement.setTotalResult( listSignalements.size( ) );
        LocalizedDelegatePaginator<Signalement> paginator = this.getPaginator( request, resultListSignalement, URL_JSP_MANAGE_SIGNALEMENTS, totalResult );

        anomaliesCount = totalResult;

        // the paginator
        model.put( SignalementConstants.MARK_NB_ITEMS_PER_PAGE, StringUtils.EMPTY + _nItemsPerPage );
        model.put( SignalementConstants.MARK_PAGINATOR, paginator );

        // workflow : recuperation des etats et des actions possibles pour les signalements de la page
        Map<String, List<Action>> mapActions = new HashMap<String, List<Action>>( );
        Map<String, String> mapStates = new HashMap<String, String>( );
        WorkflowService workflowService = WorkflowService.getInstance( );
        Integer signalementWorkflowId = _signalementWorkflowService.getSignalementWorkflowId( );
        boolean hasSignalementPrestataire = false;
        if ( workflowService.isAvailable( ) )
        {
            for ( Signalement signalement : paginator.getPageItems( ) )
            {
                // actions du workflow
                Collection<Action> listActions = _signalementService.getListActionsByIdSignalementAndUser( signalement.getId( ).intValue( ), signalementWorkflowId, getUser( ) );
                mapActions.put( signalement.getId( ).toString( ), new ArrayList<Action>( listActions ) );

                // etat
                State state = workflowService.getState( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, signalementWorkflowId, null );

                mapStates.put( signalement.getId( ).toString( ), state == null ? "Non défini" : state.getName( ) );
                
                if ( state.getId( ) == AppPropertiesService.getPropertyInt( ID_STATE_TRANSFERE_PRESTATAIRE, -1 ) || state.getId( ) == AppPropertiesService.getPropertyInt( ID_STATE_SERVICE_PROGRAMME_PRESTATAIRE, -1 ) ) {
                    hasSignalementPrestataire = true;
                }
            }
        }

        model.put( MARK_HAS_SIGNALEMENT_PRESTATAIRE, hasSignalementPrestataire );
        
        // Reaffichage des options avancees
        List<Integer> listArrondissementIds = listeArrondissement.stream( ).map( referenceItem -> Integer.valueOf( referenceItem.getCode( ) ) ).collect( Collectors.toList( ) );
        boolean hasCriteresAvances = hasCriteresAvances( filter, listArrondissementIds );

        model.put( MARK_HAS_ADVANCED_CRITERIAS, hasCriteresAvances );

        model.put( MARK_SIGNALEMENT_LIST, paginator.getPageItems( ) );
        model.put( MARK_ETATS, mapStates );
        model.put( MARK_ACTIONS, mapActions );

        model.put( MARK_TITLE, I18nService.getLocalizedString( PAGE_TITLE_MANAGE_SIGNALEMENT, getLocale( ) ) );

        // the filter
        if ( filter.getIdDirection( ) == 1 )
        {
            filter.setIdDirection( 94 );
        }
        model.put( SignalementConstants.MARK_FILTER, filter );
        model.put( SignalementConstants.MARK_LOCALE, request.getLocale( ) );

        model.put( MARK_ID_ETATS_DEFAULT, ID_ETATS_DEFAULT );
        model.put( MARK_ADVANCED_SEARCH_STATES, ADVANCED_SEARCH_STATES );
        model.put( MARK_MAP_MAX_RESULTS, AppPropertiesService.getPropertyInt( PROPERTY_MAP_MAX_RESULTS, 0 ) );

        if ( CollectionUtils.isNotEmpty( dashboardSignalementList ) )
        {
            model.put( MARK_DASHBOARD_CRITERIAS, dashboardCriterias );
        }

        return getAdminPage( getTemplate( TEMPLATE_MANAGE_SIGNALEMENT, model ) );

    }

    /**
     * Get the signalement history page.
     *
     * @param request
     *            The HTTP request
     * @return redirection url
     */
    public String getViewHistory( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>( );
        Signalement signalement = new Signalement( );
        List<PhotoDMR> photos = new ArrayList<>( );
        Signaleur signaleur = null;
        Adresse adresse = null;

        String strSignalementId = request.getParameter( PARAMETER_SIGNALEMENT_ID );
        Integer nIdSignalement = Integer.parseInt( strSignalementId );
        String strFromPage = request.getParameter( PARAMETER_FROM_PAGE );
        if ( StringUtils.equals( strFromPage, PARAMETER_VALUE_DISPLAY_PAGE ) )
        {
            model.put( MARK_BACK_URL, URL_JSP_DISPLAY_SIGNALEMENTS );
        } else
        {
            model.put( MARK_BACK_URL, URL_JSP_MANAGE_SIGNALEMENTS );
        }

        signalement = _signalementService.getSignalement( nIdSignalement );
        photos = _photoService.findBySignalementId( nIdSignalement );
        adresse = _adresseService.loadByIdSignalement( nIdSignalement );
        signaleur = _signaleurService.loadByIdSignalement( nIdSignalement );

        List<Unit> listUnitsSector = _unitService.findBySectorId( signalement.getSecteur( ).getIdSector( ) );
        Unit unitPrincipaleSector = new Unit( );

        for ( Unit unit : listUnitsSector )
        {
            if ( unit.getIdParent( ) == 0 )
            {
                unitPrincipaleSector = unit;
                signalement.setDirectionSector( unitPrincipaleSector );
            }
        }

        model.put( MARK_SIGNALEMENT, signalement );
        model.put( MARK_PHOTOS, photos );
        model.put( MARK_ADRESSE, adresse );
        model.put( MARK_SIGNALEUR, signaleur );

        // signalement's history html
        WorkflowService workflowService = WorkflowService.getInstance( );
        String historiqueList = workflowService.getDisplayDocumentHistory( nIdSignalement, Signalement.WORKFLOW_RESOURCE_TYPE, _signalementWorkflowService.getSignalementWorkflowId( ), request,
                getLocale( ) );

        model.put( MARK_HISTORIQUE_LIST, historiqueList );

        // get the signalement's state
        State stateSignalement = workflowService.getState( nIdSignalement, Signalement.WORKFLOW_RESOURCE_TYPE, _signalementWorkflowService.getSignalementWorkflowId( ), null );
        String serviceFaitValue = AppPropertiesService.getProperty( SignalementConstants.PROPERTY_SERVICE_FAIT_VALUE );
        
        model.put( MARK_STATE_SIGNALEMENT, stateSignalement );
        model.put( MARK_STATE_SERVICE_FAIT, Integer.parseInt( serviceFaitValue ) );
        
        String userAccessCode = "";
        if ( stateSignalement.getId( ) == Integer.parseInt( serviceFaitValue ) ) {
            userAccessCode = _signalementWorkflowService.selectUserServiceFait( signalement.getId( ).intValue( ) );    
        }
        if ( userAccessCode != StringUtils.EMPTY && userAccessCode != null ) {
            if( userAccessCode.equals( "auto" ) ) {
                if ( ( signaleur.getIdTelephone( ) != StringUtils.EMPTY && signaleur.getIdTelephone( ) != null && signaleur.getMail( ) == StringUtils.EMPTY ) ) {
                    model.put( MARK_USER_SERVICE_FAIT, USAGER_MOBILE );
                }               
                else {
                    model.put( MARK_USER_SERVICE_FAIT, signaleur.getMail( ) );
                }                
            }
            else {
                model.put( MARK_USER_SERVICE_FAIT, AdminUserHome.findUserByLogin( userAccessCode ).getEmail( ) );
            }
        }
        else {
            model.put( MARK_USER_SERVICE_FAIT, StringUtils.EMPTY ); 
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SIGNALEMENT_HISTORY, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Gets the signalement filter.
     *
     * @param request
     *            the request
     * @return the signalement filter
     */
    private SignalementFilter getSignalementFilter( HttpServletRequest request )
    {
        List<EtatSignalement> etats = new ArrayList<EtatSignalement>( );
        if ( _signalementFilter == null )
        {
            // passe lors du premier lancement
            _signalementFilter = new SignalementFilter( );
            String strSortedAttributeName = "signalement.date_creation";
            String strAscSort = request.getParameter( "DESC" );
            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );
            _signalementFilter.setOrderAsc( bIsAscSort );
            Order order = new Order( strSortedAttributeName, bIsAscSort );
            _signalementFilter.getOrders( ).add( order );

            strSortedAttributeName = "signalement.heure_creation";
            strAscSort = request.getParameter( "DESC" );
            bIsAscSort = Boolean.parseBoolean( strAscSort );
            _signalementFilter.setOrderAsc( bIsAscSort );
            order = new Order( strSortedAttributeName, bIsAscSort );
            _signalementFilter.getOrders( ).add( order );

            if ( dashboardSignalementList == null )
            {
                manageEtatList( etats );
            } else
            {
                _signalementFilter.setEtats( etats );
            }

        } else
        {
            // SORT
            String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );

            if ( dashboardSignalementList != null )
            {
                etats = new ArrayList<EtatSignalement>( );
                _signalementFilter.setEtats( etats );
            }

            if ( strSortedAttributeName != null )
            {
                // lors d'un tri ASC/DESC et action
                _signalementFilter.setOrders( new ArrayList<Order>( ) );

                String strAscSort = request.getParameter( Parameters.SORTED_ASC );
                boolean bIsAscSort = Boolean.parseBoolean( strAscSort );
                _signalementFilter.setOrderAsc( bIsAscSort );
                Order order = new Order( strSortedAttributeName, bIsAscSort );

                _signalementFilter.getOrders( ).add( order );
            }

            else if ( ( request.getParameter( SignalementConstants.PARAMETER_BUTTON_SEARCH ) != null ) || StringUtils.isNotBlank( request.getParameter( BUTTON_UPDATE_SECTOR_WITH_DIRECTION ) ) )
            {

                etats = new ArrayList<EtatSignalement>( );
                buildFilter( _signalementFilter, request );
                for ( State state : ( List<State> ) WorkflowService.getInstance( ).getAllStateByWorkflow( 2, getUser( ) ) )
                {
                    String strValue = request.getParameter( "etat" + state.getId( ) );
                    boolean bTypeDossChecked = Boolean.parseBoolean( strValue );
                    if ( bTypeDossChecked )
                    {
                        EtatSignalement etatSignalement = new EtatSignalement( );
                        etatSignalement.setCoche( bTypeDossChecked );
                        etatSignalement.setId( ( long ) state.getId( ) );
                        etatSignalement.setLibelle( state.getName( ) );
                        etats.add( etatSignalement );

                    }
                }
                _signalementFilter.setEtats( etats );

            }

        }

        return _signalementFilter;

    }

    public void manageEtatList( List<EtatSignalement> etats )
    {
        for ( State state : ( List<State> ) WorkflowService.getInstance( ).getAllStateByWorkflow( 2, getUser( ) ) )
        {
            /*
             * initialisation des valeurs par defaut a Nouveau, A traiter, A faire terrain et A faire Bureau
             */
            if ( ( state != null ) && ( ( state.getId( ) == 7 ) || ( state.getId( ) == 8 ) || ( state.getId( ) == 16 ) || ( state.getId( ) == 17 ) || ( state.getId( ) == 18 )
                    || ( state.getId( ) == 21 )) )
            {

                EtatSignalement etatSignalement = new EtatSignalement( );

                etatSignalement.setCoche( true );
                etatSignalement.setId( ( long ) state.getId( ) );
                etatSignalement.setLibelle( state.getName( ) );
                etats.add( etatSignalement );
            }
        }
        _signalementFilter.setEtats( etats );
    }

    private void applyViewRoleRestrictionToFilter( HttpServletRequest request, AdminUser adminUser )
    {
        if ( _signalementViewRoleService.checkIsUserRestrictedByViewRoles( adminUser.getUserId( ), request ) )
        {
            if ( _signalementFilter.getListIdArrondissements( ) == null )
            {
                _signalementFilter.setListIdArrondissements( _signalementViewRoleService.getUserRestrictedArrondissementList( adminUser.getUserId( ) ) );
            }
            if ( _signalementFilter.getListIdTypeSignalements( ) == null )
            {
                _signalementFilter.setListIdTypeSignalements( _signalementViewRoleService.getUserRestrictedTypeSignalementList( adminUser.getUserId( ) ) );
            }
        }

        // Allowed sectors list by user
        List<Unit> listUnits = _unitService.getUnitsByIdUser( adminUser.getUserId( ), false );

        List<Integer> listSectorsOfUnit = new ArrayList<Integer>( );
        List<Integer> listOfUnit = new ArrayList<Integer>( );
        for ( Unit userUnit : listUnits )
        {
            listOfUnit.add( userUnit.getIdUnit( ) );
            listSectorsOfUnit.addAll( _sectorService.getIdsSectorByIdUnit( userUnit.getIdUnit( ) ) );
        }

        _signalementFilter.setListIdUnit( listOfUnit );
        _signalementFilter.setListIdSecteurAutorises( listSectorsOfUnit );
    }

    /**
     * Cette methode return true si une ressource n apas l etat A traiter, Nouveau our Service Programmer
     *
     * @param idRessource
     * @return
     */

    public String IsMassServiceFait( int[] idRessource )
    {

        // si le tableau est vide ou nulle
        if ( ( idRessource == null ) || ( idRessource.length == 0 ) )
        {
            return "";
        }

        int index = 0;

        StringBuilder trouver = new StringBuilder( "" );

        /**
         * on parcour la liste des id a la recherche de ressource n ayant pas le bon etat.
         */
        while ( !trouver.equals( "" ) && ( index < idRessource.length ) )
        {

            // on recuper l etat de la ressource
            State etat = WorkflowService.getInstance( ).getState( idRessource[index], Signalement.WORKFLOW_RESOURCE_TYPE, _signalementWorkflowService.getSignalementWorkflowId( ), null );

            // si l etat est null
            if ( etat == null )
            {
                trouver.append( setNumeroSignalement( idRessource[index] ) );
                break;
            }

            // on recuper l id de l etat
            int etats = etat.getId( );

            // si l id de l etat est correspond a celui de l 'un des etat qu'on ne cherche pas
            if ( ( ( etats != 7 ) && ( etats != 8 ) && ( etats != 9 ) ) )
            {

                trouver.append( setNumeroSignalement( idRessource[index] ) );
                break;
            }

            // on incremente l index
            index++;

        }

        return trouver.toString( ).trim( );

    }

    /*
     * Cette methode retourne le numero d'un signalement.
     *
     * @param nIdSignalement id du signalement
     *
     * @return numero du signalement
     */
    public String setNumeroSignalement( int nIdSignalement )
    {

        // on declare les variables
        StringBuilder sbNumerosSignalement = new StringBuilder( );
        Signalement signalement;

        // on recupere le signalement
        signalement = _signalementService.getSignalement( nIdSignalement );

        // si le signalemen n existe pas
        if ( signalement == null )
        {
            return sbNumerosSignalement.append( "<br />" ).toString( );
        }

        /*
         * reconstitut le numero
         */
        sbNumerosSignalement.append( signalement.getPrefix( ) );
        sbNumerosSignalement.append( signalement.getAnnee( ) );
        sbNumerosSignalement.append( signalement.getMois( ) );
        sbNumerosSignalement.append( signalement.getNumero( ) );

        // on retourne le numero completer du saut de ligne
        return sbNumerosSignalement.append( "<br />" ).toString( );

    }

    /**
     * Builds the filter.
     *
     * @param filter
     *            the filter
     * @param request
     *            the request
     */
    protected void buildFilter( SignalementFilter filter, HttpServletRequest request )
    {
        // Récupération des arrondissements (c'est une liste)
        String[] arrondissements = request.getParameterValues( "listIdArrondissementsParam" );
        String[] quartiers = request.getParameterValues( "listIdQuartierParam" );
        if ( !ArrayUtils.isEmpty( arrondissements ) )
        {
            List<Integer> arrondissementIds = ListUtils.getListOfIntFromStrArray( arrondissements );
            filter.setListIdArrondissements( arrondissementIds );
        } else
        {
            filter.setListIdArrondissements( null );
        }

        if ( !ArrayUtils.isEmpty( quartiers ) )
        {
            List<Integer> quartiersIds = ListUtils.getListOfIntFromStrArray( quartiers );
            filter.setListIdQuartier( quartiersIds );
        } else
        {
            filter.setListIdQuartier( null );
        }

        populate( filter, request );
    }

    /**
     * Get the signalement creation form.
     *
     * @param request
     *            The HTTP request
     * @return redirection url
     * @throws AccessDeniedException
     *             the access denied exception
     */
    public String getSaveSignalement( HttpServletRequest request ) throws AccessDeniedException
    {
        Signalement signalement = null;
        Map<String, Object> model = new HashMap<String, Object>( );

        model.put( MARK_KEY_MAPS, SignalementConstants.GOOGLE_MAPS_API_KEY );

        // GESTION DES ERREURS1
        FunctionnalException fe = getErrorOnce( request );
        if ( fe != null )
        {
            signalement = ( Signalement ) fe.getBean( );
            model.put( "error", getHtmlError( fe ) );

            if ( fe.getAdditionalParameters( ) != null )
            {
                model.putAll( fe.getAdditionalParameters( ) );
            }

            if ( getHtmlError( fe ).length( ) == MESSAGE_ERROR_EMPTY )
            {
                model.put( "noErrorToDisplay", "noErrorToDisplay" );
            }

            String strSignalementId = request.getParameter( PARAMETER_SIGNALEMENT_ID );
            if ( strSignalementId == null )
            {

                // get all the priorites
                List<Priorite> priorites = _prioriteService.getAllPriorite( );
                ReferenceList listePriorite = ListUtils.toReferenceList( priorites, "id", "libelle", "" );
                model.put( MARK_PRIORITE_LIST, listePriorite );

                // get all the type signalement
                List<TypeSignalement> types = _typeSignalementService.getAllTypeSignalementActifLinkedToUnit( );
                ReferenceList listeTypes = ListUtils.toReferenceList( types, "id", "formatTypeSignalement", "", false );
                model.put( MARK_TYPE_LIST, listeTypes );

            }

        } else
        {
            // MODIFICATION
            String strSignalementId = request.getParameter( PARAMETER_SIGNALEMENT_ID );
            if ( strSignalementId != null )
            {
                List<PhotoDMR> photos = new ArrayList<>( );
                Signaleur signaleur = null;
                Adresse adresse = null;
                TypeSignalement typeSignalement = null;

                int nIdSignalement = Integer.parseInt( strSignalementId );
                long lIdSignalement = Long.parseLong( strSignalementId );

                // get the signalement infos
                signalement = _signalementService.getSignalement( nIdSignalement );

                // we check the right again (if the users tries with the address in the url)
                Boolean authorized = estAutoriseSecteur( request, String.valueOf( signalement.getSecteur( ).getIdSector( ) ) );
                authorized = authorized && _signalementViewRoleService.isUserAuthorized( signalement, request );
                if ( !authorized )
                {

                    throw new AccessDeniedException( MESSAGE_ACCESS_DENIED );
                }

                adresse = _adresseService.loadByIdSignalement( lIdSignalement );
                signaleur = _signaleurService.loadByIdSignalement( lIdSignalement );
                photos = _photoService.findBySignalementId( lIdSignalement );

                model.put( MARK_PHOTOS, photos );
                model.put( MARK_ADRESSE, adresse );
                model.put( MARK_SIGNALEUR, signaleur );

                // get the type signalement
                typeSignalement = _typeSignalementService.getTypeSignalement( signalement.getTypeSignalement( ).getId( ) );

                model.put( MARK_TYPE, typeSignalement );

                // get all the priorites
                List<Priorite> priorites = _prioriteService.getAllPriorite( );
                ReferenceList listePriorite = ListUtils.toReferenceList( priorites, "id", "libelle", "", true );
                model.put( MARK_PRIORITE_LIST, listePriorite );

                // get the signalement's state
                WorkflowService workflowService = WorkflowService.getInstance( );
                State stateSignalement = workflowService.getState( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, _signalementWorkflowService.getSignalementWorkflowId( ),
                        null );

                model.put( MARK_STATE_SIGNALEMENT, stateSignalement );

                model.put( SignalementConstants.MARK_LOCALE, request.getLocale( ) );

            } else
            {
                // CREATION

                signalement = new Signalement( );

                // get all the priorites
                List<Priorite> priorites = _prioriteService.getAllPriorite( );
                ReferenceList listePriorite = ListUtils.toReferenceList( priorites, "id", "libelle", "" );
                model.put( MARK_PRIORITE_LIST, listePriorite );

                // get all the type signalement
                List<TypeSignalement> types = _typeSignalementService.getAllTypeSignalementActifLinkedToUnit( );
                ReferenceList listeTypes = ListUtils.toReferenceList( types, "id", "formatTypeSignalement", "", false );
                model.put( MARK_TYPE_LIST, listeTypes );

                model.put( SignalementConstants.MARK_LOCALE, request.getLocale( ) );
                String strMessageCreation = StringUtils.EMPTY;

                // get the default message creation (linked to the workflow)
                if ( SignalementUtils.isWindows( ) )
                {
                    strMessageCreation = _signalementService.getMessageCreationSignalement( ).replaceAll( "</p>|<br/>|<br>|<br />", "\r" ).replaceAll( "<[^>]*>", "" );

                } else
                {
                    strMessageCreation = _signalementService.getMessageCreationSignalement( ).replaceAll( "<br/>|<br>|<br />", System.getProperty( "line.separator" ) ).replaceAll( "<[^>]*>", "" );

                }

                model.put( MARK_MESSAGE_FOR_USER, strMessageCreation );

            }
        }

        model.put( MARK_SIGNALEMENT, signalement );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SAVE_SIGNALEMENT, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );

    }

    /**
     * Returns the form to view a signalement.
     *
     * @param request
     *            The Http request
     * @return the html code of the folder form
     */
    public String getViewSignalement( HttpServletRequest request )
    {
        Signalement signalement = null;
        List<PhotoDMR> photos = new ArrayList<>( );
        Signaleur signaleur = null;
        Adresse adresse = null;

        Map<String, Object> model = new HashMap<>( );

        String strFromPage = request.getParameter( PARAMETER_FROM_PAGE );
        if ( StringUtils.equals( strFromPage, PARAMETER_VALUE_DISPLAY_PAGE ) )
        {
            model.put( MARK_BACK_URL, URL_JSP_DISPLAY_SIGNALEMENTS );
        } else
        {
            model.put( MARK_BACK_URL, URL_JSP_MANAGE_SIGNALEMENTS );
        }

        String strSignalementId = request.getParameter( PARAMETER_SIGNALEMENT_ID );
        if ( strSignalementId != null )
        {

            Long lIdSignalement = Long.parseLong( strSignalementId );
            signalement = _signalementService.getSignalement( lIdSignalement );
            photos = _photoService.findBySignalementId( lIdSignalement );
            adresse = _adresseService.loadByIdSignalement( lIdSignalement );
            signaleur = _signaleurService.loadByIdSignalement( lIdSignalement );

            Sector sector = signalement.getSecteur( );
            List<Unit> listUnitsSector = _unitService.findBySectorId( sector.getIdSector( ) );
            Unit unitPrincipaleSector = null;
            for ( Unit unit : listUnitsSector )
            {
                if ( unit.getIdParent( ) == 0 )
                {
                    unitPrincipaleSector = unit;
                    signalement.setDirectionSector( unitPrincipaleSector );
                }
            }
            signalement.setDirectionSector( unitPrincipaleSector );

            // get the signalement's state
            WorkflowService workflowService = WorkflowService.getInstance( );
            State stateSignalement = workflowService.getState( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, _signalementWorkflowService.getSignalementWorkflowId( ), null );

            // Get the signalement possible actions (based on user)
            Integer signalementWorkflowId = _signalementWorkflowService.getSignalementWorkflowId( );
            Collection<Action> possibleActions = _signalementService.getListActionsByIdSignalementAndUser( lIdSignalement.intValue( ), signalementWorkflowId, getUser( ) );

            model.put( MARK_POSSIBLE_ACTIONS, possibleActions );
            model.put( MARK_STATE_SIGNALEMENT, stateSignalement );
        }

        model.put( SignalementConstants.MARK_JSP_BACK, JSP_MANAGE_SIGNALEMENT );
        model.put( MARK_SIGNALEMENT, signalement );
        model.put( MARK_PHOTOS, photos );
        model.put( MARK_ADRESSE, adresse );
        model.put( MARK_SIGNALEUR, signaleur );

        model.put( "locale", Locale.FRANCE );

        String template = getTemplate( TEMPLATE_VIEW_SIGNALEMENT, model );

        return getAdminPage( template );
    }

    /**
     * Save/modify a signalement.
     *
     * @param request
     *            The HTTP request
     * @return redirection url
     */
    public String doSaveSignalement( HttpServletRequest request )
    {
        if ( null != request.getParameter( "cancel" ) )
        {
            return doGoBack( request );
        }

        MultipartHttpServletRequest multipartRequest = ( MultipartHttpServletRequest ) request;
        String strIdSignalement = multipartRequest.getParameter( PARAMETER_SIGNALEMENT_ID );
        Signalement signalement = new Signalement( );

        // if the user wants to add a new photo to the signalement
        if ( null != request.getParameter( "addPhotoModif" ) )
        {
            UrlItem urlItem = new UrlItem( JSP_ADD_PHOTO_TO_SIGNALEMENT );

            urlItem.addParameter( PARAMETER_SIGNALEMENT_ID, strIdSignalement );

            return urlItem.getUrl( );
        }

        // MODIFICATION
        if ( !strIdSignalement.equals( EMPTY_STRING ) )
        {
            long lIdSignalement = 0;
            signalement = new Signalement( );

            try
            {
                lIdSignalement = Long.parseLong( strIdSignalement );
            } catch ( NumberFormatException e )
            {
                throw new BusinessException( signalement, SignalementConstants.MESSAGE_ERROR_OCCUR );
            }

            // get the signalement by his id
            signalement = _signalementService.getSignalement( lIdSignalement );

            // get the address of the signalement by his id
            List<Adresse> adresses = new ArrayList<Adresse>( );
            Adresse adresseSignalement = new Adresse( );

            adresseSignalement = _adresseService.loadByIdSignalement( signalement.getId( ) );
            adresses.add( adresseSignalement );

            signalement.setAdresses( adresses );

        }

        String strError = getSignalementDataAndSave( multipartRequest, signalement );

        if ( strError != null )
        {
            return strError;
        }

        UrlItem urlItem = new UrlItem( JSP_VIEW_SIGNALEMENT );

        urlItem.addParameter( PARAMETER_SIGNALEMENT_ID, Long.toString( signalement.getId( ) ) );

        return urlItem.getUrl( );
    }

    /**
     * Clear mass action.
     *
     * @param request
     *            the request
     */
    private void clearMassAction( HttpServletRequest request )
    {
        // suppression de la dernière saisie
        request.getSession( ).setAttribute( SignalementConstants.ATTRIBUTE_SESSION_DERNIERE_SAISIE_ACTION, null );
        _massSignalementIds = null;
    }

    /**
     * Add a photo to a signalement (only in modification mode)
     *
     * @param request
     *            the request
     * @return The next url
     */
    public String getSavePhotoToSignalement( HttpServletRequest request )
    {

        Map<String, Object> model = new HashMap<String, Object>( );

        String strSignalementId = request.getParameter( PARAMETER_SIGNALEMENT_ID );

        model.put( MARK_SIGNALEMENT_ID, strSignalementId );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_SAVE_PHOTO_TO_SIGNALEMENT, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );

    }

    /**
     * Delete a photo linked to a signalement
     *
     * @param request
     *            the request
     * @return The next url
     */
    public String getDeletePhotoSignalement( HttpServletRequest request )
    {

        String strJspBack = JSP_MODIFY_SIGNALEMENT;

        String strPhotoId = request.getParameter( PARAMETER_PHOTO_ID );
        String strSignalementId = request.getParameter( PARAMETER_SIGNALEMENT_ID );

        Integer nIdPhoto = 0;
        Integer nIdSignalement = 0;

        nIdPhoto = Integer.parseInt( strPhotoId );
        nIdSignalement = Integer.parseInt( strSignalementId );

        Map<String, Object> urlParam = new HashMap<String, Object>( );
        urlParam.put( PARAMETER_PHOTO_ID, nIdPhoto );
        urlParam.put( PARAMETER_SIGNALEMENT_ID, nIdSignalement );

        return AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_DELETE_PHOTO_SIGNALEMENT, null, MESSAGE_CONFIRMATION_DELETE_PHOTO_SIGNALEMENT, JSP_DELETE_PHOTO_SIGNALEMENT, "_self",
                AdminMessage.TYPE_CONFIRMATION, urlParam, strJspBack );

    }

    /**
     * Delete a photo linked to a signalement
     *
     * @param request
     *            the request
     * @return The next url
     */
    public String doDeletePhotoSignalement( HttpServletRequest request )
    {
        String strPhotoId = request.getParameter( PARAMETER_PHOTO_ID );
        String strSignalementId = request.getParameter( PARAMETER_SIGNALEMENT_ID );

        int nIdPhoto = 0;

        nIdPhoto = Integer.parseInt( strPhotoId );

        _photoService.remove( nIdPhoto );

        UrlItem urlItem = new UrlItem( JSP_MODIFY_SIGNALEMENT );
        urlItem.addParameter( PARAMETER_SIGNALEMENT_ID, strSignalementId );
        return urlItem.getUrl( );

    }

    /**
     * Add a photo to a signalement (only in modification mode)
     *
     * @param request
     *            the request
     * @return The next url
     */
    public String doSavePhotoToSignalement( HttpServletRequest request )
    {

        MultipartHttpServletRequest multipartRequest = ( MultipartHttpServletRequest ) request;
        String strIdSignalement = multipartRequest.getParameter( PARAMETER_SIGNALEMENT_ID );
        String strVuePhoto = multipartRequest.getParameter( PARAMETER_VUE_PHOTO );
        Signalement signalement = new Signalement( );

        Long nIdSignalement = Long.parseLong( strIdSignalement );
        signalement.setId( nIdSignalement );

        if ( StringUtils.isNotBlank( request.getParameter( "cancelAddPhoto" ) ) )
        {
            UrlItem urlItem = new UrlItem( JSP_MODIFY_SIGNALEMENT );

            urlItem.addParameter( PARAMETER_SIGNALEMENT_ID, strIdSignalement );

            return urlItem.getUrl( );
        }
        
        List<PhotoDMR> photos = _signalementService.getSignalement( nIdSignalement ).getPhotos( );
        Integer nVuePhoto = Integer.parseInt( strVuePhoto );
        
        for (PhotoDMR photo : photos) {
            //Si photo déjà existante
            if(photo.getVue( ) == nVuePhoto) {
                return AdminMessageService.getMessageUrl( multipartRequest, MESSAGE_ERROR_EXISTING_PHOTO, AdminMessage.TYPE_STOP );
            }
        }
        

        FileItem imageSource = multipartRequest.getFile( PARAMETER_PHOTO );
        String strImageName = FileUploadService.getFileNameOnly( imageSource );

        byte[] baImageSource = imageSource.get( );
        ImageResource image = new ImageResource( );

        if ( ( strImageName != null ) && !strImageName.equals( EMPTY_STRING ) )
        {
            String width = AppPropertiesService.getProperty( SignalementConstants.IMAGE_THUMBNAIL_RESIZE_WIDTH );
            String height = AppPropertiesService.getProperty( SignalementConstants.IMAGE_THUMBNAIL_RESIZE_HEIGHT );
            byte[] resizeImage = ImageUtil.resizeImage( baImageSource, width, height, 1 );
            PhotoDMR photoSignalement = new PhotoDMR( );
            image.setImage( baImageSource );
            image.setMimeType( imageSource.getContentType( ) );
            photoSignalement.setImage( image );
            photoSignalement.setImageThumbnailWithBytes( resizeImage );
            photoSignalement.setSignalement( signalement );

            photoSignalement.setVue( nVuePhoto );

            SimpleDateFormat sdfDate = new SimpleDateFormat( DateUtils.DATE_FR );
            photoSignalement.setDate( sdfDate.format( Calendar.getInstance( ).getTime( ) ) );

            // creation of the image in the db linked to the signalement
            _photoService.insert( photoSignalement );

        } else
        {
            return AdminMessageService.getMessageUrl( multipartRequest, MESSAGE_ERROR_EMPTY_PHOTO_FIELD, AdminMessage.TYPE_STOP );
        }

        UrlItem urlItem = new UrlItem( JSP_MODIFY_SIGNALEMENT );

        urlItem.addParameter( PARAMETER_SIGNALEMENT_ID, strIdSignalement );

        return urlItem.getUrl( );

    }

    /**
     * Return the url of the JSP which called the last action.
     *
     * @param request
     *            The Http request
     * @return The url of the last JSP
     */
    private String doGoBack( HttpServletRequest request )
    {
        String strJspBack = request.getParameter( SignalementConstants.MARK_JSP_BACK );

        return StringUtils.isNotBlank( strJspBack ) ? AppPathService.getBaseUrl( request ) + strJspBack : AppPathService.getBaseUrl( request ) + JSP_MANAGE_SIGNALEMENT;
    }

    /**
     * Get the request data and if there is no error insert the data in the signalement object specified in parameter. return null if there is no error or else return the error page url
     *
     * @param multipartRequest
     *            the request
     * @param signalement
     *            the signalement
     * @return null if there is no error or else return the error page url
     */
    public String getSignalementDataAndSave( MultipartHttpServletRequest multipartRequest, Signalement signalement )
    {
        // Warning, before edit this method, another method can save signalement
        // SignalementRestService.saveIncident (in module-signalement-rest)
        try
        {
            setFromRequest( multipartRequest, signalement );
            // MODIFICATION
            String strIdSignaleur = multipartRequest.getParameter( PARAMETER_SIGNALEUR_ID );
            String strIdTelephone = multipartRequest.getParameter( PARAMETER_TELEPHONE_ID );
            String strIdSignalement = multipartRequest.getParameter( PARAMETER_SIGNALEMENT_ID );
            String strCommentaireProg = multipartRequest.getParameter( PARAMETER_COMMENTAIRE_PROG );

            // BOTH
            String strPriorite = multipartRequest.getParameter( PARAMETER_PRIORITE );
            String strTypeSignalement = multipartRequest.getParameter( PARAMETER_TYPE_SIGNALEMENT );
            String strMail = multipartRequest.getParameter( PARAMETER_MAIL );
            String strCommentaire = multipartRequest.getParameter( PARAMETER_COMMENTAIRE );
            FileItem imageSourceEnsemble = multipartRequest.getFile( PARAMETER_PHOTO_ENSEMBLE );
            FileItem imageSourcePres = multipartRequest.getFile( PARAMETER_PHOTO_PRES );

            String strMessageCreation = multipartRequest.getParameter( "message" );

            Map<String, Object> model = new HashMap<String, Object>( );
            model.put( MARK_MESSAGE_FOR_USER, strMessageCreation );

            Adresse adresse = new Adresse( );
            setFromRequest( multipartRequest, adresse, signalement );

            signalement.getAdresses( ).add( adresse );

            // set the signalement object attributes
            signalement.setCommentaire( strCommentaire );

            // CREATION
            if ( StringUtils.isBlank( strIdSignalement ) )
            {

                String dateOfTheDay = sdfDate.format( Calendar.getInstance( ).getTime( ) );
                signalement.setDateCreation( dateOfTheDay );
                Date dateDay = DateUtils.getDate( signalement.getDateCreation( ), false );
                String annee = DateUtils.getAnnee( Calendar.getInstance( ).getTime( ) );

                signalement.setAnnee( Integer.parseInt( annee ) );
                int moisSignalement = DateUtils.getMoisInt( dateDay );

                // associate the month with a letter (A for january, B for february...)
                signalement.setMois( SignalementService.getLetterByMonth( moisSignalement ) );

                // prefix for the signalement numero
                signalement.setPrefix( SignalementConstants.SIGNALEMENT_PREFIX_BACKOFFICE );

                // Creation of the unique token for the signalement
                _signalementService.affectToken( signalement );
            }

            // get the strings in int format
            int nPriorite = 0;
            Integer nTypeSignalement = 0;

            try
            {
                nPriorite = Integer.parseInt( strPriorite );
                if ( strIdSignalement.equals( EMPTY_STRING ) )
                {
                    nTypeSignalement = Integer.parseInt( strTypeSignalement );
                }
            } catch ( NumberFormatException e )
            {
                throw new BusinessException( signalement, model, SignalementConstants.MESSAGE_ERROR_OCCUR );
            }

            // add priorite to signalement
            signalement.setPriorite( _prioriteService.load( nPriorite ) );

            // add type_signalement to signalement
            if ( StringUtils.isBlank( strIdSignalement ) )
            {
                signalement.setTypeSignalement( _typeSignalementService.getByIdTypeSignalement( nTypeSignalement ) );
            }

            // save the signalement
            if ( strIdSignalement.equals( EMPTY_STRING ) )
            {

                // test if mandatory fields are not empty
                String urlError = checkMandatoryFields( multipartRequest, signalement );
                if ( StringUtils.isNotBlank( urlError ) )
                {
                    return urlError;
                }
                Arrondissement arrondissementByGeom = _adresseService.getArrondissementByGeom( adresse.getLng( ), adresse.getLat( ) );
                if ( arrondissementByGeom == null )
                {
                    return AdminMessageService.getMessageUrl( multipartRequest, MESSAGE_ERREUR_HORS_PARIS, AdminMessage.TYPE_STOP );
                }
                signalement.setArrondissement( arrondissementByGeom );

                Long lastInsertedSignalement;
                try
                {
                    lastInsertedSignalement = _signalementService.insert( signalement );

                    // create a file for the message creation
                    String strFileName = "messagecreation_" + lastInsertedSignalement.toString( );
                    String strFilePath = AppPathService.getAbsolutePathFromRelativePath( AppPropertiesService.getProperty( PROPERTY_FILE_FOLDER_PATH ) );

                    if ( strMessageCreation == null )
                    {
                        strMessageCreation = _signalementService.getMessageCreationSignalement( );
                    }

                    try
                    {
                        _fileMessageCreationService.createFile( strFilePath, strFileName, strMessageCreation, "UTF-8" );
                    } catch ( IOException e )
                    {
                        e.printStackTrace( );
                    }

                } catch ( BusinessException e )
                {
                    return AdminMessageService.getMessageUrl( multipartRequest, MESSAGE_ERREUR_SECTEUR_NULL, AdminMessage.TYPE_STOP );
                }

                signalement.setId( lastInsertedSignalement );

            } else
            {
                String urlErrors = checkUpdateMandatoryFields( multipartRequest, signalement );
                if ( StringUtils.isNotBlank( urlErrors ) )
                {
                    return urlErrors;
                }

                // modify the signalement
                signalement.setCommentaireProgrammation( strCommentaireProg );
                _signalementService.update( signalement );
            }

            // creation of the address
            adresse.setSignalement( signalement );
            if ( StringUtils.isBlank( strIdSignalement ) )
            {
                _adresseService.insert( adresse );
            } // else {
              // this._adresseService.update(adresse);
              // }

            Signaleur signaleur = new Signaleur( );

            // CREATION
            if ( StringUtils.isBlank( strIdSignalement ) )
            {
                // creation of the signaleur if not null
                if ( StringUtils.isNotBlank( strMail ) )
                {
                    signaleur.setMail( strMail );
                    signaleur.setIdTelephone( strIdTelephone );
                    signaleur.setSignalement( signalement );
                    _signaleurService.insert( signaleur );
                }
                else {
                    signaleur.setSignalement( signalement );
                    _signaleurService.insert( signaleur );
                }

                // image 1 ensemble treatement CREATION
                insertPhoto( signalement, imageSourceEnsemble, VUE_ENSEMBLE );

                // image 2 près treatement CREATION
                insertPhoto( signalement, imageSourcePres, VUE_PRES );
                _signalementService.initializeSignalementWorkflow( signalement );
            }
            // MODIFICATION
            else
            {
                try
                {
                    if ( StringUtils.isNotBlank( strIdSignaleur ) )
                    {
                        long lIdSignaleur = Long.parseLong( strIdSignaleur );
                        signaleur = _signaleurService.load( lIdSignaleur );
                    }
                    signaleur.setSignalement( signalement );
                    signaleur.setMail( strMail );
                    signaleur.setIdTelephone( strIdTelephone );

                    if ( StringUtils.isNotBlank( strIdSignaleur ) )
                    {
                        _signaleurService.update( signaleur );
                    } else
                    {
                        // CREATION
                        _signaleurService.insert( signaleur );
                    }

                } catch ( NumberFormatException e )
                {
                    throw new BusinessException( signalement, model, SignalementConstants.MESSAGE_ERROR_OCCUR );
                }
            }
            return null;
        } catch ( FunctionnalException e )
        {
            return manageFunctionnalException( multipartRequest, e, "SaveSignalement.jsp" );
        }
    }

    private void setFromRequest( MultipartHttpServletRequest multipartRequest, Signalement signalement )
    {

    }

    /**
     * Extract and set parameter for signalement adress
     *
     * @param multipartRequest
     *            the multipart request
     * @param adresse
     *            the adresse to set
     * @param signalement
     *            the signalement
     */
    private void setFromRequest( MultipartHttpServletRequest multipartRequest, Adresse adresse, Signalement signalement )
    {
        String strIdSignalement = multipartRequest.getParameter( PARAMETER_SIGNALEMENT_ID );
        String strIdAdresse = multipartRequest.getParameter( PARAMETER_ADRESSE_ID );
        String strAdresse = multipartRequest.getParameter( PARAMETER_ADRESSE );
        String strPrecisionLocalisation = multipartRequest.getParameter( PARAMETER_PRECISION_LOCALISATION );
        String strLng = multipartRequest.getParameter( PARAMETER_LNG );
        String strLat = multipartRequest.getParameter( PARAMETER_LAT );

        Double nLat = null;
        Double nLng = null;
        try
        {
            if ( !StringUtils.isEmpty( strLat ) )
            {
                nLat = Double.parseDouble( strLat );
            }
        } catch ( NumberFormatException e )
        {
            throw new BusinessException( signalement, SignalementConstants.MESSAGE_ERROR_OCCUR );
        }

        try
        {
            if ( !StringUtils.isEmpty( strLng ) )
            {
                nLng = Double.parseDouble( strLng );
            }
        } catch ( NumberFormatException e )
        {
            throw new BusinessException( signalement, SignalementConstants.MESSAGE_ERROR_OCCUR );
        }

        if ( StringUtils.isBlank( strIdSignalement ) )
        {
            adresse.setAdresse( strAdresse );
            adresse.setPrecisionLocalisation( strPrecisionLocalisation );
            adresse.setLat( nLat );
            adresse.setLng( nLng );
        } else
        {
            // modification of the adresse
            long lIdAdresse = 0;

            try
            {
                lIdAdresse = Long.parseLong( strIdAdresse );

            } catch ( NumberFormatException e )
            {
                throw new BusinessException( signalement, SignalementConstants.MESSAGE_ERROR_OCCUR );
            }

            adresse = _adresseService.load( lIdAdresse );
            adresse.setPrecisionLocalisation( strPrecisionLocalisation );
        }
    }

    /**
     * Check the mandatory fields before create signalement
     *
     * @param multipartRequest
     *            the request with signalement parameters
     * @param signalement
     *            the signalement to check
     * @return emptry string if no errors, link to the error otherwise
     */
    public String checkMandatoryFields( MultipartHttpServletRequest multipartRequest, Signalement signalement )
    {
        String url = StringUtils.EMPTY;

        String strIdSignalement = multipartRequest.getParameter( PARAMETER_SIGNALEMENT_ID );
        String strTypeSignalement = multipartRequest.getParameter( PARAMETER_TYPE_SIGNALEMENT );
        String strAdresse = multipartRequest.getParameter( PARAMETER_ADRESSE );
        String strPriorite = multipartRequest.getParameter( PARAMETER_PRIORITE );

        if ( ( strPriorite != null ) && MINUS_ONE.equals( strPriorite.trim( ) ) )
        {
            url = AdminMessageService.getMessageUrl( multipartRequest, MESSAGE_ERROR_EMPTY_FIELD, AdminMessage.TYPE_STOP );
        } else if ( StringUtils.isNotBlank( strIdSignalement ) )
        {
            if ( ( strTypeSignalement != null ) && ( MINUS_ONE.equals( strTypeSignalement.trim( ) ) || StringUtils.isNotBlank( strAdresse ) ) )
            {
                url = AdminMessageService.getMessageUrl( multipartRequest, MESSAGE_ERROR_EMPTY_FIELD, AdminMessage.TYPE_STOP );
            }
            signalement.setSuivi( ZERO_VOTE );
        }

        return url;
    }

    public String checkUpdateMandatoryFields( MultipartHttpServletRequest multipartRequest, Signalement signalement )
    {

        String url = StringUtils.EMPTY;

        String strPriorite = multipartRequest.getParameter( PARAMETER_PRIORITE );

        if ( ( strPriorite != null ) && MINUS_ONE.equals( strPriorite.trim( ) ) )
        {
            url = AdminMessageService.getMessageUrl( multipartRequest, MESSAGE_ERROR_EMPTY_FIELD, AdminMessage.TYPE_STOP );
        }

        return url;
    }

    private void insertPhoto( Signalement signalement, FileItem imageFile, Integer vuePhoto )
    {
        String strImageName = FileUploadService.getFileNameOnly( imageFile );
        if ( StringUtils.isNotBlank( strImageName ) )
        {
            ImageResource image = new ImageResource( );
            String width = AppPropertiesService.getProperty( SignalementConstants.IMAGE_THUMBNAIL_RESIZE_WIDTH );
            String height = AppPropertiesService.getProperty( SignalementConstants.IMAGE_THUMBNAIL_RESIZE_HEIGHT );
            byte[] resizeImage = ImageUtil.resizeImage( imageFile.get( ), width, height, 1 );
            PhotoDMR photoSignalement = new PhotoDMR( );
            image.setImage( imageFile.get( ) );
            String mimeType = imageFile.getContentType( ).replace( "pjpeg", "jpeg" );
            mimeType = mimeType.replace( "x-png", "png" );
            image.setMimeType( mimeType );
            photoSignalement.setImage( image );
            photoSignalement.setImageContent( ImgUtils.checkQuality( image.getImage( ) ) );
            photoSignalement.setImageThumbnailWithBytes( resizeImage );
            photoSignalement.setSignalement( signalement );
            photoSignalement.setVue( vuePhoto );
            photoSignalement.setDate( sdfDate.format( Calendar.getInstance( ).getTime( ) ) );

            // creation of the image in the db linked to the signalement
            _photoService.insert( photoSignalement );
        }
    }

    /**
     * Check the accessibility
     *
     * @param multipartRequest
     *            the request
     * @param signalement
     *            the signaement
     * @return empty if no errors, url otherwise
     */
    public String checkAccessibility( MultipartHttpServletRequest multipartRequest, Signalement signalement )
    {
        String errorUrl = StringUtils.EMPTY;
        String addressLoad = multipartRequest.getParameter( "adresseLoad" );
        String address = multipartRequest.getParameter( "adresse" );
        if ( ( multipartRequest.getParameter( "searchAddress" ) != null ) || ( StringUtils.isNotBlank( addressLoad ) && StringUtils.isNotBlank( address ) && !address.equals( addressLoad ) ) )
        {

            signalement.getAdresses( ).get( 0 ).setLat( null );
            signalement.getAdresses( ).get( 0 ).setLng( null );
            errorUrl = manageFunctionnalException( multipartRequest, new BusinessException( signalement, null ),
                    "SaveSignalement.jsp?searchAddress=" + multipartRequest.getParameter( "searchAddress" ) );
        } else if ( multipartRequest.getParameter( "validProposedAddress" ) != null )
        {
            // get the chosen address in the select list
            Adresse add = new Adresse( );

            String allParameter = multipartRequest.getParameter( "validAddress" );
            List<Adresse> addresses = getCoordinatesFromAccess( add, allParameter );
            signalement.setAdresses( addresses );

            errorUrl = manageFunctionnalException( multipartRequest, new BusinessException( signalement, null ), "SaveSignalement.jsp?validAddress=true" );
        }
        return errorUrl;
    }

    /**
     * Trouver le type d'action.
     *
     * @param request
     *            request
     * @return le nom de l'action
     */
    private String findActionType( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_ACTION_PROGRAMMER ) != null )
        {
            return ACTION_PROGRAMMER;
        }

        if ( request.getParameter( PARAMETER_ACTION_REPROGRAMMER ) != null )
        {
            return ACTION_REPROGRAMMER;
        }

        if ( ( request.getParameter( PARAMETER_ACTION_SERVICE_FAIT ) != null ) || ( request.getParameter( PARAMETER_ACTION_SERVICE_FAITS ) != null ) )
        {
            return ACTION_SERVICE_FAIT;
        }

        return null;
    }

    /**
     * Put action type.
     *
     * @param request
     *            the request
     * @param urlItem
     *            the url item
     */
    private void putActionType( HttpServletRequest request, UrlItem urlItem )
    {
        if ( request.getParameter( PARAMETER_ACTION_PROGRAMMER ) != null )
        {
            urlItem.addParameter( PARAMETER_ACTION_PROGRAMMER, request.getParameter( PARAMETER_ACTION_PROGRAMMER ) );
        }

        if ( request.getParameter( PARAMETER_ACTION_REPROGRAMMER ) != null )
        {
            urlItem.addParameter( PARAMETER_ACTION_REPROGRAMMER, request.getParameter( PARAMETER_ACTION_REPROGRAMMER ) );
        }

        if ( request.getParameter( PARAMETER_ACTION_SERVICE_FAIT ) != null )
        {
            urlItem.addParameter( PARAMETER_ACTION_SERVICE_FAIT, request.getParameter( PARAMETER_ACTION_SERVICE_FAIT ) );
        }
    }

    /**
     * Process action.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @return the i plugin action result
     * @throws AccessDeniedException
     *             the access denied exception
     */

    public IPluginActionResult processAction( HttpServletRequest request, HttpServletResponse response ) throws AccessDeniedException
    {

        setWorkflowReturnURI( request );
        String strIdResource = request.getParameter( PARAMETER_SIGNALEMENT_ID );
        _massSignalementIds = SignalementUtils.getIntArray( request.getParameterValues( PARAMETER_SIGNALEMENT_ID ) );
        String strUrl;
        int nIdResource;
        Number nIdAction = 0;
        try
        {
            nIdResource = Integer.parseInt( strIdResource );
        } catch ( NumberFormatException nfe )
        {
            return SignalementUtils.buildRedirectResult( getHomeUrl( request ) );
        }

        // actions "normales"
        ISignalementAction signalementAction = PluginActionManager.getPluginAction( request, ISignalementAction.class );
        if ( signalementAction != null )
        {
            SignalementFields signalementFields = new SignalementFields( );
            signalementFields.setSignalementsId( _massSignalementIds );
            signalementFields.setIdResource( nIdResource );
            return signalementAction.process( request, response, getUser( ), signalementFields );
        }

        // actions workflow
        String strIdAction = request.getParameter( PARAMETER_ACTION_ID );

        if ( strIdAction == null )
        {
            // mass action
            // mass actions
            _actionType = findActionType( request );

            if ( StringUtils.isNotBlank( _actionType ) )
            {

                // suppression des signalements sur lesquels l'action ne peut pas être réalisée
                int index = 0;
                boolean suppression = false;
                Signalement signalement;
                UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_WORKFLOW__ACTION );
                putActionType( request, urlItem );
                String[] messageArgs = new String[1];
                StringBuilder sbNumerosSignalement = new StringBuilder( );

                for ( int nIdSignalement : _massSignalementIds )
                {

                    if ( findNextAction( nIdSignalement, _actionType ) != null )
                    {
                        if ( !WorkflowService.getInstance( ).canProcessAction( nIdSignalement, Signalement.WORKFLOW_RESOURCE_TYPE, findNextAction( nIdSignalement, _actionType ).getId( ), null,
                                request, false ) )
                        {
                            suppression = true;
                            _massSignalementIds = ArrayUtils.remove( _massSignalementIds, index );

                            // construction numero signalement
                            sbNumerosSignalement.append( "<br />" );
                            signalement = _signalementService.getSignalement( nIdSignalement );
                            if ( signalement != null )
                            {
                                sbNumerosSignalement.append( signalement.getPrefix( ) );
                                sbNumerosSignalement.append( signalement.getAnnee( ) );
                                sbNumerosSignalement.append( signalement.getMois( ) );
                                sbNumerosSignalement.append( signalement.getNumero( ) );
                            }
                            index--;
                        } else
                        {
                            urlItem.addParameter( PARAMETER_SIGNALEMENT_ID, nIdSignalement );
                        }

                        index++;
                    } else
                    {
                        suppression = true;
                        _massSignalementIds = ArrayUtils.remove( _massSignalementIds, index );

                        // construction numero signalement
                        sbNumerosSignalement.append( "<br />" );
                        signalement = _signalementService.getSignalement( nIdSignalement );
                        if ( signalement != null )
                        {
                            sbNumerosSignalement.append( signalement.getPrefix( ) );
                            sbNumerosSignalement.append( signalement.getAnnee( ) );
                            sbNumerosSignalement.append( signalement.getMois( ) );
                            sbNumerosSignalement.append( signalement.getNumero( ) );
                        }
                    }
                }
                if ( suppression )
                {
                    messageArgs[0] = sbNumerosSignalement.toString( );

                    strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_MASS_ACTION_IMPOSSIBLE, messageArgs, urlItem.getUrl( ), AdminMessage.TYPE_WARNING );

                    return SignalementUtils.buildRedirectResult( strUrl );

                }

                nIdResource = findNextSignalementIdMassActions( );

                if ( nIdResource == NO_RESOURCE_FOUND )
                {
                    return SignalementUtils.buildRedirectResult( getHomeUrl( request ) );
                }

                Action action = findNextAction( nIdResource, _actionType );

                if ( action == null )
                {
                    return SignalementUtils.buildRedirectResult( getHomeUrl( request ) );
                }
                nIdAction = action.getId( );
            }
        } else if ( nIdAction != null )
        {

            try
            {
                nIdAction = Integer.parseInt( strIdAction );
            } catch ( NumberFormatException nfe )
            {
                throw new AppException( "Invalid action id " + nfe.getMessage( ), nfe );
            }
        }

        String strBaseUrl = AppPathService.getBaseUrl( request );
        if ( WorkflowService.getInstance( ).canProcessAction( nIdResource, Signalement.WORKFLOW_RESOURCE_TYPE, nIdAction.intValue( ), null, request, false ) )
        {

            UrlItem urlItem;
            if ( WorkflowService.getInstance( ).isDisplayTasksForm( nIdAction.intValue( ), getLocale( ) ) )
            {
                // jsp de formulaire
                urlItem = new UrlItem( strBaseUrl + JSP_WORKFLOW_ACTION );
            } else
            {
                if ( isMessageTask( nIdAction.intValue( ) ) )
                {
                    // jsp action avec message et sans interraction
                    boolean isMassAction = ( _massSignalementIds != null ) && ( _massSignalementIds.length > 1 );
                    String jspReturn = getMessageConfirmationSignalement( request, nIdAction.intValue( ), isMassAction );
                    urlItem = new UrlItem( jspReturn );

                } else
                {
                    // jsp action sans interraction
                    urlItem = new UrlItem( strBaseUrl + JSP_DO_PROCESS_ACTION );
                }

            }
            urlItem.addParameter( PARAMETER_ACTION_ID, nIdAction.intValue( ) );
            urlItem.addParameter( PARAMETER_SIGNALEMENT_ID, nIdResource );
            urlItem.addParameter( PARAMETER_IS_ROAD_MAP, Boolean.toString( StringUtils.isNotBlank( request.getParameter( PARAMETER_IS_ROAD_MAP ) ) ) );
            String strDateService = request.getParameter( PARAMETER_DATE_SERVICE );
            if ( StringUtils.isNotBlank( strDateService ) )
            {
                urlItem.addParameter( PARAMETER_DATE_SERVICE, strDateService );
            }
            String strIdService = request.getParameter( PARAMETER_SERVICE_ID );
            if ( StringUtils.isNotBlank( strIdService ) )
            {
                urlItem.addParameter( PARAMETER_SERVICE_ID, strIdService );
            }
            String strServiceId = request.getParameter( PARAMETER_SERVICE__ID );
            if ( StringUtils.isNotBlank( strServiceId ) )
            {
                urlItem.addParameter( PARAMETER_SERVICE__ID, strServiceId );
            }

            String next = request.getParameter( PARAMETER_NEXT_URL );

            if ( StringUtils.contains( next, URL_JSP_GET_ROAD_MAP ) ) {
                try {
                    // Gestion de l'URL de retour
                    URI uri = new URI(next);
                    String nextPath = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), null, uri.getFragment()).toString();
                    UrlItem nextUrl = new UrlItem(nextPath);
                    // date de service
                    if (StringUtils.isNotBlank(strDateService)) {
                        nextUrl.addParameter(PARAMETER_DATE_SERVICE, strDateService);
                    }
                    // service id
                    if (StringUtils.isNotBlank(strServiceId)) {
                        nextUrl.addParameter(PARAMETER_SERVICE__ID, strServiceId);
                    }
                    // unit id
                    String strUnitId = request.getParameter(PARAMETER_UNIT__ID);
                    if (StringUtils.isNotBlank(strUnitId)) {
                        nextUrl.addParameter(PARAMETER_UNIT__ID, strUnitId);
                    }
                    // sector id
                    String strSectorId = request.getParameter(PARAMETER_SECTOR__ID);
                    if (StringUtils.isNotBlank(strSectorId)) {
                        nextUrl.addParameter(PARAMETER_SECTOR__ID, strSectorId);
                    }
                    // set next url in the return url object
                    urlItem.addParameter(PARAMETER_NEXT_URL, encodeURIComponent( nextUrl.getUrl( ) ) );
                    // set next url in the session
                    request.getSession( ).setAttribute( PARAMETER_NEXT_URL, nextUrl.getUrl( ) );
                } catch (Exception e) {
                    AppLogService.error(e);
                }
            } else {
                urlItem.addParameter( PARAMETER_NEXT_URL, next );
            }

            strUrl = urlItem.getUrl( );
        } else
        {
            strUrl = AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
        }

        return SignalementUtils.buildRedirectResult( strUrl );
    }

    /**
     * Process service fait en masse pm 2015 action.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @return the i plugin action result
     * @throws AccessDeniedException
     *             the access denied exception
     */
    public IPluginActionResult processActionServicefait( HttpServletRequest request, HttpServletResponse response ) throws AccessDeniedException
    {

        setWorkflowReturnURI( request );
        String strIdResource = request.getParameter( PARAMETER_SIGNALEMENT_ID );
        _massSignalementIds = SignalementUtils.getIntArray( request.getParameterValues( PARAMETER_SIGNALEMENT_ID ) );
        String strUrl;
        int nIdResource;
        Number nIdAction = 0;
        try
        {
            nIdResource = Integer.parseInt( strIdResource );
        } catch ( NumberFormatException nfe )
        {
            return SignalementUtils.buildRedirectResult( getHomeUrl( request ) );
        }

        // actions workflow
        String strIdAction = request.getParameter( PARAMETER_ACTION_ID );

        if ( strIdAction == null )
        {
            // mass action
            // mass actions
            _actionType = findActionType( request );

            if ( StringUtils.isNotBlank( _actionType ) )
            {

                // suppression des signalements sur lesquels l'action ne peut pas être réalisée
                int index = 0;
                boolean suppression = false;
                String[] messageArgs = new String[1];
                UrlItem urlItem;
                StringBuilder sbNumerosSignalement = new StringBuilder( );
                Signalement signalement;

                urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_WORKFLOW__ACTION );

                putActionType( request, urlItem );

                // Controle des etats
                String trouve = IsMassServiceFait( _massSignalementIds );
                /*
                 * si au moins un des messages choisies n est pas a l etat nouveau ou a traiter ou service programme
                 */
                if ( !"".equals( trouve ) )
                {

                    messageArgs[0] = ( ( new StringBuilder( trouve ) ).append( "<br />" ) ).toString( );

                    strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_MASS_ACTION_IMPOSSIBLE, messageArgs, urlItem.getUrl( ), AdminMessage.TYPE_STOP );

                    return SignalementUtils.buildRedirectResult( strUrl );

                }

                else
                {

                    for ( int nIdSignalement : _massSignalementIds )
                    {

                        if ( findNextAction( nIdSignalement, _actionType ) != null )
                        {

                            if ( !WorkflowService.getInstance( ).canProcessAction( nIdSignalement, Signalement.WORKFLOW_RESOURCE_TYPE, findNextAction( nIdSignalement, _actionType ).getId( ), null,
                                    request, false ) )
                            {

                                suppression = true;
                                _massSignalementIds = ArrayUtils.remove( _massSignalementIds, index );

                                // construction numero signalement
                                sbNumerosSignalement.append( "<br />" );
                                signalement = _signalementService.getSignalement( nIdSignalement );
                                if ( signalement != null )
                                {
                                    sbNumerosSignalement.append( signalement.getPrefix( ) );
                                    sbNumerosSignalement.append( signalement.getAnnee( ) );
                                    sbNumerosSignalement.append( signalement.getMois( ) );
                                    sbNumerosSignalement.append( signalement.getNumero( ) );
                                }
                                index--;
                            } else
                            {
                                urlItem.addParameter( PARAMETER_SIGNALEMENT_ID, nIdSignalement );
                            }

                            index++;
                        } else
                        {
                            suppression = true;
                            _massSignalementIds = ArrayUtils.remove( _massSignalementIds, index );

                            // construction numero signalement
                            sbNumerosSignalement.append( "<br />" );
                            signalement = _signalementService.getSignalement( nIdSignalement );
                            if ( signalement != null )
                            {
                                sbNumerosSignalement.append( signalement.getPrefix( ) );
                                sbNumerosSignalement.append( signalement.getAnnee( ) );
                                sbNumerosSignalement.append( signalement.getMois( ) );
                                sbNumerosSignalement.append( signalement.getNumero( ) );
                            }
                        }
                    }

                }

                if ( suppression )
                {
                    messageArgs[0] = sbNumerosSignalement.toString( );

                    strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_MASS_ACTION_IMPOSSIBLE, messageArgs, urlItem.getUrl( ), AdminMessage.TYPE_WARNING );

                    return SignalementUtils.buildRedirectResult( strUrl );

                }

                nIdResource = findNextSignalementIdMassActions( );

                if ( nIdResource == NO_RESOURCE_FOUND )
                {
                    return SignalementUtils.buildRedirectResult( getHomeUrl( request ) );
                }

                Action action = findNextAction( nIdResource, _actionType );

                if ( action == null )
                {
                    return SignalementUtils.buildRedirectResult( getHomeUrl( request ) );
                }
                nIdAction = action.getId( );
            }
        } else if ( nIdAction != null )
        {

            try
            {
                nIdAction = Integer.parseInt( strIdAction );
            } catch ( NumberFormatException nfe )
            {
                throw new AppException( "Invalid action id " + nfe.getMessage( ), nfe );
            }
        }

        String strBaseUrl = AppPathService.getBaseUrl( request );
        if ( WorkflowService.getInstance( ).canProcessAction( nIdResource, Signalement.WORKFLOW_RESOURCE_TYPE, nIdAction.intValue( ), null, request, false ) )
        {

            UrlItem urlItem;
            if ( WorkflowService.getInstance( ).isDisplayTasksForm( nIdAction.intValue( ), getLocale( ) ) )
            {
                // jsp de formulaire
                urlItem = new UrlItem( strBaseUrl + JSP_WORKFLOW_ACTION );
            } else
            {
                if ( isMessageTask( nIdAction.intValue( ) ) )
                {
                    // jsp action avec message et sans interraction
                    boolean isMassAction = ( _massSignalementIds != null ) && ( _massSignalementIds.length > 1 );
                    String jspReturn = getMessageConfirmationSignalement( request, nIdAction.intValue( ), isMassAction );
                    urlItem = new UrlItem( jspReturn );

                } else
                {
                    // jsp action sans interraction
                    urlItem = new UrlItem( strBaseUrl + JSP_DO_PROCESS_ACTION );
                }

            }
            urlItem.addParameter( PARAMETER_ACTION_ID, nIdAction.intValue( ) );
            urlItem.addParameter( PARAMETER_SIGNALEMENT_ID, nIdResource );
            urlItem.addParameter( "next", request.getParameter( "next" ) );
            urlItem.addParameter( PARAMETER_IS_ROAD_MAP, Boolean.toString( StringUtils.isNotBlank( request.getParameter( PARAMETER_IS_ROAD_MAP ) ) ) );
            String strDateService = request.getParameter( PARAMETER_DATE_SERVICE );
            if ( StringUtils.isNotBlank( strDateService ) )
            {
                urlItem.addParameter( PARAMETER_DATE_SERVICE, strDateService );
            }
            String strIdService = request.getParameter( PARAMETER_SERVICE_ID );
            if ( StringUtils.isNotBlank( strIdService ) )
            {
                urlItem.addParameter( PARAMETER_SERVICE_ID, strIdService );
            }

            strUrl = urlItem.getUrl( );
        } else
        {
            strUrl = AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
        }

        return SignalementUtils.buildRedirectResult( strUrl );
    }

    /**
     * Checks if is message task.
     *
     * @param nIdAction
     *            the n id action
     * @return true, if is message task
     */
    private boolean isMessageTask( int nIdAction )
    {
        for ( int i : ACTION_MESSAGE_TASK )
        {
            if ( i == nIdAction )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Contient id action.
     *
     * @param listeIdActions
     *            the liste id actions
     * @param id
     *            the id
     * @return true, if successful
     */
    private boolean contientIdAction( int[] listeIdActions, int id )
    {
        for ( int i : listeIdActions )
        {
            if ( i == id )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the confirmation message to pass to state SERVICE FAIT a case.
     *
     * @param request
     *            The Http request
     * @param nIdAction
     *            the n id action
     * @param isMassAction
     *            the is mass action
     * @return the html code message
     */
    public String getMessageConfirmationSignalement( HttpServletRequest request, int nIdAction, boolean isMassAction )
    {

        String messageTitle = "";
        String messageConfirmation = "";

        if ( !contientIdAction( ACTION_MESSAGE_TASK, nIdAction ) )
        {
            return getHomeUrl( request );
        }

        Map<String, Object> urlParam = new HashMap<String, Object>( );
        urlParam.put( PARAMETER_ACTION_ID, nIdAction );
        if ( isMassAction )
        {
            if ( ArrayUtils.isEmpty( _massSignalementIds ) )
            {
                return getHomeUrl( request );
            }
            StringBuilder sbNumerosSignalement = new StringBuilder( );
            for ( int nIdSignalement : _massSignalementIds )
            {
                urlParam.put( PARAMETER_SIGNALEMENT_ID, nIdSignalement );
                // construction numero signalement
                sbNumerosSignalement.append( "<br />" );
                Signalement signalement = _signalementService.getSignalement( nIdSignalement );
                if ( signalement != null )
                {
                    sbNumerosSignalement.append( signalement.getPrefix( ) );
                    sbNumerosSignalement.append( signalement.getAnnee( ) );
                    sbNumerosSignalement.append( signalement.getMois( ) );
                    sbNumerosSignalement.append( signalement.getNumero( ) );
                }
            }

            String[] messageArgs = new String[1];
            messageArgs[0] = sbNumerosSignalement.toString( );

            if ( contientIdAction( ID_ACTIONS_SERVICE_FAIT, nIdAction ) )
            {
                messageConfirmation = MESSAGE_CONFIRMATION_MASS_SERVICE_FAIT;
                messageTitle = MESSAGE_TITLE_MASS_SERVICE_FAIT;
            }
            if ( contientIdAction( ID_ACTIONS_ACCEPTER, nIdAction ) )
            {
                messageConfirmation = MESSAGE_CONFIRMATION_MASS_ACCEPTER;
                messageTitle = MESSAGE_TITLE_MASS_ACCEPTER;
            }

            return AdminMessageService.getMessageUrl( request, messageTitle, messageArgs, messageConfirmation, JSP_DO_PROCESS_MASS_ACTION, "_self", AdminMessage.TYPE_CONFIRMATION, urlParam,
                    JSP_MANAGE_SIGNALEMENT );
        }

        if ( contientIdAction( ID_ACTIONS_SERVICE_FAIT, nIdAction ) )
        {
            messageConfirmation = MESSAGE_CONFIRMATION_SERVICE_FAIT;
            messageTitle = MESSAGE_TITLE_SERVICE_FAIT;
        }
        if ( contientIdAction( ID_ACTIONS_ACCEPTER, nIdAction ) )
        {
            messageConfirmation = MESSAGE_CONFIRMATION_ACCEPTER;
            messageTitle = MESSAGE_TITLE_ACCEPTER;
        }

        urlParam.put( PARAMETER_SIGNALEMENT_ID, _massSignalementIds[0] );
        return AdminMessageService.getMessageUrl( request, messageTitle, null, messageConfirmation, JSP_DO_PROCESS_ACTION, "_self", AdminMessage.TYPE_CONFIRMATION, urlParam, JSP_MANAGE_SIGNALEMENT );
    }

    /**
     * Do process action.
     *
     * @param request
     *            the request
     * @return the string
     */
    public String doProcessAction( HttpServletRequest request )
    {
        Boolean bHasNext = ( _massSignalementIds != null ) && ( _massSignalementIds.length > 1 );
        request.setAttribute( SignalementConstants.ATTRIBUTE_HAS_NEXT, bHasNext );

        String homeUrl = getWorkflowReturnURI( request );

        if ( request.getParameter( SignalementConstants.PARAMETER_BUTTON_CANCEL ) != null )
        {
            return homeUrl;
        }

        String strIdAction = request.getParameter( PARAMETER_ACTION_ID );
        String strIdResource = request.getParameter( PARAMETER_SIGNALEMENT_ID );

        int nIdAction;
        int nIdResource;
        try
        {
            nIdAction = Integer.parseInt( strIdAction );
            nIdResource = Integer.parseInt( strIdResource );
        } catch ( NumberFormatException nfe )
        {
            throw new AppException( "Invalid action id " + nfe.getMessage( ), nfe );
        }

        if ( WorkflowService.getInstance( ).canProcessAction( nIdResource, Signalement.WORKFLOW_RESOURCE_TYPE, nIdAction, null, request, false ) )
        {
            try {
                String strErrorUrl = WorkflowService.getInstance( ).doSaveTasksForm( nIdResource, Signalement.WORKFLOW_RESOURCE_TYPE, nIdAction, null, request, getLocale( ) );
                if ( strErrorUrl != null )
                {
                    return strErrorUrl;
                }
            } catch (Exception e) {
                AppLogService.error( e );
                return AdminMessageService.getMessageUrl(request, MESSAGE_RAMEN_WS_ERROR, AdminMessage.TYPE_STOP);
            }

            if ( request.getParameter( SignalementConstants.PARAMETER_VALIDATE_NEXT ) != null )
            {
                _massSignalementIds = ArrayUtils.remove( _massSignalementIds, 0 );
                nIdResource = findNextSignalementIdMassActions( );
                if ( nIdResource != NO_RESOURCE_FOUND )
                {
                    Action action = findNextAction( nIdResource, _actionType );
                    if ( action != null )
                    {
                        UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_WORKFLOW__ACTION );

                        for ( int nIdActionParameter : _massSignalementIds )
                        {
                            urlItem.addParameter( PARAMETER_SIGNALEMENT_ID, nIdActionParameter );
                        }

                        urlItem.addParameter( PARAMETER_ACTION_ID, action.getId( ) );

                        return urlItem.getUrl( );
                    }
                }
            }

            clearMassAction( request );
        } else
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
        }

        return homeUrl;
    }

    /**
     * Gets the workflow return adresse. getHomeUrl is a fallback if next is not defined.
     *
     * @param request
     *            the request
     * @return the workflow return adresse
     */
    private String getWorkflowReturnURI( HttpServletRequest request )
    {

        String homeUrl;
        try
        {
            String strNext = ( String ) request.getSession( ).getAttribute( "next" );
            if ( ( strNext != null ) || StringUtils.isEmpty( strNext ) )
            {
                homeUrl = strNext;
            } else
            {
                homeUrl = getHomeUrl( request );
            }

        } catch ( ClassCastException e )
        {
            homeUrl = getHomeUrl( request );
        }

        return homeUrl;
    }

    /**
     * Sets the workflow return uri from request param to session.
     *
     * @param request
     *            request objects.
     */
    private void setWorkflowReturnURI( HttpServletRequest request )
    {
        String homeUrl;
        String strNext = request.getParameter( PARAMETER_ACTION_NEXT );

        if ( ( strNext == null ) || StringUtils.isEmpty( strNext ) )
        {
            homeUrl = getHomeUrl( request );
        } else
        {
            homeUrl = strNext;
        }
        request.getSession( ).setAttribute( "next", homeUrl );
    }

    /**
     * Do process mass action.
     *
     * @param request
     *            the request
     * @return the string
     */
    public String doProcessMassAction( HttpServletRequest request )
    {

        String strIdAction = request.getParameter( PARAMETER_ACTION_ID );

        int nIdAction;
        try
        {
            nIdAction = Integer.parseInt( strIdAction );
        } catch ( NumberFormatException nfe )
        {
            throw new AppException( "Invalid action id " + nfe.getMessage( ), nfe );
        }
        for ( int nIdResource : _massSignalementIds )
        {
            if ( WorkflowService.getInstance( ).canProcessAction( nIdResource, Signalement.WORKFLOW_RESOURCE_TYPE, nIdAction, null, request, false ) )
            {
                String strErrorUrl = WorkflowService.getInstance( ).doSaveTasksForm( nIdResource, Signalement.WORKFLOW_RESOURCE_TYPE, nIdAction, null, request, getLocale( ) );
                if ( strErrorUrl != null )
                {
                    return strErrorUrl;
                }
            } else
            {
                return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
            }
        }
        clearMassAction( request );

        return getHomeUrl( request );
    }

    /**
     * Trouve l'action à exécuter.
     *
     * @param nId
     *            identifiant de la ressource
     * @param strActionName
     *            le nom de l'action
     * @return l'identifiant trouvé, 0 sinon.
     */
    private Action findNextAction( int nId, String strActionName )
    {
        Collection<Action> listActions = WorkflowService.getInstance( ).getActions( nId, Signalement.WORKFLOW_RESOURCE_TYPE, _signalementWorkflowService.getSignalementWorkflowId( ), getUser( ) );

        if ( ( listActions != null ) && !listActions.isEmpty( ) )
        {
            for ( Action action : listActions )
            {
                if ( StringUtils.equals( strActionName, action.getName( ) ) )
                {
                    return action;
                }
            }
        }

        // no action matching
        return null;
    }

    /**
     * Trouve l'identifiant de la prochaine action.
     *
     * @return l'identifiant trouvé, {@link #NO_RESOURCE_FOUND} sinon
     */
    private int findNextSignalementIdMassActions( )
    {
        while ( !ArrayUtils.isEmpty( _massSignalementIds ) )
        {
            int nIdResource = _massSignalementIds[0];
            Action action = findNextAction( nIdResource, _actionType );
            if ( action != null )
            {
                return nIdResource;
            }

            _massSignalementIds = ArrayUtils.remove( _massSignalementIds, 0 );
        }

        return NO_RESOURCE_FOUND;
    }

    /**
     * Process action.
     *
     * @param request
     *            request
     * @return url or form
     * @throws AccessDeniedException
     *             the access denied exception
     */
    public String getWorkflowActionForm( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdAction = request.getParameter( PARAMETER_ACTION_ID );
        String strIdResource = request.getParameter( PARAMETER_SIGNALEMENT_ID );

        Boolean bHasNext = ( _massSignalementIds != null ) && ( _massSignalementIds.length > 1 );

        request.setAttribute( SignalementConstants.ATTRIBUTE_HAS_NEXT, bHasNext );

        int nIdAction;
        int nIdResource;
        try
        {
            nIdAction = Integer.parseInt( strIdAction );
            nIdResource = Integer.parseInt( strIdResource );
        } catch ( NumberFormatException nfe )
        {
            throw new AppException( "Invalid action id " + nfe.getMessage( ), nfe );
        }

        /////////
        // we check the right again (if the users tries with the address in the url)
        /////////
        if ( nIdResource > 0 )
        {
            Signalement signalement = _signalementService.getSignalement( nIdResource );

            Boolean authorized = estAutoriseSecteur( request, String.valueOf( signalement.getSecteur( ).getIdSector( ) ) );
            authorized = authorized && _signalementViewRoleService.isUserAuthorized( signalement, request );
            if ( !authorized )
            {

                throw new AccessDeniedException( MESSAGE_ACCESS_DENIED );

            }
        }
        /////////////

        String strTaskForm;
        if ( WorkflowService.getInstance( ).canProcessAction( nIdResource, Signalement.WORKFLOW_RESOURCE_TYPE, nIdAction, null, request, false ) )
        {
            strTaskForm = WorkflowService.getInstance( ).getDisplayTasksForm( nIdResource, Signalement.WORKFLOW_RESOURCE_TYPE, nIdAction, request, getLocale( ) );
        } else
        {
            // problème de droits ?
            strTaskForm = StringUtils.EMPTY;
        }

        Map<String, Object> model = new HashMap<String, Object>( );       
        model.put( MARK_HAS_NEXT, bHasNext );
        model.put( MARK_TASK_FORM, strTaskForm );
        model.put( MARK_ACTION_ID, nIdAction );
        model.put( MARK_SIGNALEMENT, nIdResource );
        model.put( PARAMETER_SIGNALEMENT_ID, nIdResource );
        
        String nextURL =request.getParameter( PARAMETER_NEXT_URL );
        if(nextURL != null && nextURL.contains( PARAMETER_WEBAPP_RAMEN )) {
            //redirect on RAMEN Webapp
            UrlItem urlRedirect = new UrlItem( nextURL );
            
            String serviceID =request.getParameter( PARAMETER_SERVICE__ID );
            if(serviceID != null) {
                urlRedirect.addParameter( PARAMETER_SERVICE__ID, serviceID );
            }
            
            String sectorID =request.getParameter( PARAMETER_SECTOR__ID );
            if(sectorID != null) {
                urlRedirect.addParameter( PARAMETER_SECTOR__ID, sectorID );
            }
            
            String unitID =request.getParameter( PARAMETER_UNIT__ID );
            if(unitID != null) {
                urlRedirect.addParameter( PARAMETER_UNIT__ID, unitID );
            }
          
           model.put( MARK_BACK_URL, urlRedirect.getUrl( ) );
        } else {
           //stay on SIGNALEMENT Webapp
            model.put( MARK_BACK_URL,  JSP_MANAGE_SIGNALEMENT);
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_WORKFLOW, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Export in CSV all checked signalement or the result the research.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     */
    public void exportSignalement( HttpServletRequest request, HttpServletResponse response )
    {
        List<SignalementExportCSVDTO> listeSignalementExportCSVDTO = new ArrayList<SignalementExportCSVDTO>( );
        _massSignalementIds = SignalementUtils.getIntArray( request.getParameterValues( PARAMETER_SIGNALEMENT_ID ) );

        if ( _massSignalementIds != null )
        {
            // _massSignalementIds contient les id des cochés, ce sont ceux à exporter
            listeSignalementExportCSVDTO = _signalementService.getSignalementForExport( _massSignalementIds );
        } else if ( dashboardSignalementList != null )
        {
            int[] signalementIds = new int[dashboardSignalementList.size( )];
            for ( int i = 0; i < signalementIds.length; i++ )
            {
                signalementIds[i] = dashboardSignalementList.get( i );
            }
            listeSignalementExportCSVDTO = _signalementService.getSignalementForExport( signalementIds );
        } else
        {
            SignalementFilter filter = getSignalementFilter( request );

            listeSignalementExportCSVDTO = _signalementService.getSignalementForExportByFilter( filter, null );
        }

        String[] datas;
        try
        {
            CSVWriter writer = null;
            response.setCharacterEncoding( CSV_ISO );
            writer = new CSVWriter( response.getWriter( ), CSV_SEPARATOR );

            writer.writeNext( new String[] { "Numéro", "Priorité", "Type", "Alias", "Alias mobile", "Direction", "Quartier", "Adresse", "Coordonnée X", "Coordonnée Y", "Arrondissement", "Secteur d'affectation", "Date de création",
                    "Heure de création", "Etat", "Mail usager", "Commentaire usager", "Nombre de photos", "Date de clôture", "Raisons de rejet", "Nombre de suivis", "Nombre de félicitations" } );
            for ( SignalementExportCSVDTO signalementDTO : listeSignalementExportCSVDTO )
            {
                datas = signalementDTO.getTabAllDatas( );
                writer.writeNext( datas );
            }

            writer.close( );

        } catch ( IOException e )
        {
            AppLogService.error( e );
        }
    }

    /**
     * @param add
     * @param allParameter
     * @return
     */
    private List<Adresse> getCoordinatesFromAccess( Adresse add, String allParameter )
    {
        String delimiter = "/";

        String[] temp = allParameter.split( delimiter );

        // get the address libel
        String libelAddress = temp[0].toLowerCase( );

        // get the lat/lng in lambert 27561
        String tempLatLng = temp[1].substring( 8 );
        delimiter = ")";
        String tempLatLng2 = tempLatLng.substring( 0, tempLatLng.length( ) - 1 );
        String[] separatedLatLng = tempLatLng2.split( "\\s" );

        String strLat = separatedLatLng[0];
        String strLng = separatedLatLng[1];

        Double dLat = Double.parseDouble( strLat );
        Double dLng = Double.parseDouble( strLng );

        // transform the lambert coordinates to WGS84 for the database
        Double[] geom = _signalementService.getGeomFromLambertToWgs84( dLat, dLng );

        add.setAdresse( libelAddress );
        add.setLat( geom[1] );
        add.setLng( geom[0] );
        List<Adresse> addresses = new ArrayList<Adresse>( );
        addresses.add( add );
        return addresses;
    }

    /**
     * Returns the confirmation message to delete a signalement.
     *
     * @param request
     *            The Http request
     * @return the html code message
     * @throws AccessDeniedException
     *             If the current user is not authorized to access this feature
     */
    public String getDeleteSignalement( HttpServletRequest request ) throws AccessDeniedException
    {
        String strSignalementId = request.getParameter( PARAMETER_SIGNALEMENT_ID );

        int nIdSignalement = 0;

        try
        {
            nIdSignalement = Integer.parseInt( strSignalementId );
        } catch ( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        Map<String, Object> urlParam = new HashMap<String, Object>( );
        urlParam.put( PARAMETER_SIGNALEMENT_ID, nIdSignalement );

        Long lIdSignalement = Long.parseLong( strSignalementId );
        Signalement signalement = new Signalement( );
        signalement = _signalementService.getSignalement( lIdSignalement );

        // we check the right again (if the users tries with the address in the url)
        Boolean authorized = estAutoriseSecteur( request, String.valueOf( signalement.getSecteur( ).getIdSector( ) ) );
        authorized = authorized && _signalementViewRoleService.isUserAuthorized( signalement, request );
        if ( !authorized )
        {

            throw new AccessDeniedException( MESSAGE_ACCESS_DENIED );

        }

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRMATION_DELETE_SIGNALEMENT, null, MESSAGE_TITLE_DELETE_SIGNALEMENT, JSP_DELETE_SIGNALEMENT, "_self",
                AdminMessage.TYPE_CONFIRMATION, urlParam, JSP_MANAGE_SIGNALEMENT );
    }

    /**
     * Delete a signalement.
     *
     * @param request
     *            The Http request
     * @return url
     */
    public String doDeleteSignalement( HttpServletRequest request )
    {
        String strSignalementId = request.getParameter( PARAMETER_SIGNALEMENT_ID );

        int nIdSignalement = 0;

        try
        {
            nIdSignalement = Integer.parseInt( strSignalementId );
        } catch ( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        _signalementService.doDeleteSignalement( nIdSignalement );

        return doGoBack( request );
    }

    /**
     * Returns the confirmation message to delete a List of signalement.
     *
     * @param request
     *            The Http request
     * @return the html code message
     * @throws AccessDeniedException
     *             If the current user is not authorized to access this feature
     */
    public String getMassDeleteSignalement( HttpServletRequest request ) throws AccessDeniedException
    {

        Map<String, Object> urlParam = new HashMap<String, Object>( );
        if ( ArrayUtils.isEmpty( _massSignalementIds ) )
        {
            return getHomeUrl( request );
        }
        StringBuilder sbNumerosSignalement = new StringBuilder( );
        for ( int nIdSignalement : _massSignalementIds )
        {
            urlParam.put( PARAMETER_SIGNALEMENT_ID, nIdSignalement );

            // construction numero signalement
            sbNumerosSignalement.append( "<br />" );
            Signalement signalement = _signalementService.getSignalement( nIdSignalement );
            if ( signalement != null )
            {
                sbNumerosSignalement.append( signalement.getPrefix( ) );
                sbNumerosSignalement.append( signalement.getAnnee( ) );
                sbNumerosSignalement.append( signalement.getMois( ) );
                sbNumerosSignalement.append( signalement.getNumero( ) );
            }
        }

        String[] messageArgs = new String[1];
        // messageArgs[0] = sbNumerosSignalement.toString();

        String elementCount = Integer.toString( _massSignalementIds.length );
        messageArgs[0] = elementCount;

        return AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_MASS_DELETE_SIGNALEMENT, messageArgs, MESSAGE_CONFIRMATION_MASS_DELETE_SIGNALEMENT, JSP_MASS_DELETE_SIGNALEMENT, "_self",
                AdminMessage.TYPE_CONFIRMATION, urlParam, JSP_MANAGE_SIGNALEMENT );
    }

    /**
     * Delete a list of selected signalement.
     *
     * @param request
     *            The Http request
     * @return url
     */
    public String doMassDeleteSignalement( HttpServletRequest request )
    {

        if ( _massSignalementIds != null )
        {
            try
            {
                _signalementService.doDeleteSignalement( _massSignalementIds );
            } catch ( NumberFormatException e )
            {
                return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
            }

        }
        return getHomeUrl( request );
        // eturn doGoBack(request);
    }

    /**
     * Get the sector list attach to the selected unit
     *
     * @param request
     *            the request
     * @param response
     *            the response
     */
    public void getSectorListByIdDirection( HttpServletRequest request, HttpServletResponse response )
    {
        String strDirectionId = request.getParameter( PARAMETER_DIRECTION_ID );

        JSONBuilder jsonStringer;
        response.setContentType( "application/json" );
        try
        {
            jsonStringer = new JSONBuilder( response.getWriter( ) );
            try
            {
                Integer directionId = Integer.parseInt( strDirectionId );
                //Specificity for DEVE entity, change the id from SEJ to DEVE
                if ( directionId == 94 )
                {
                    directionId = 1;
                }
                Unit unitSelected = _unitService.getUnit( directionId, false );
                AdminUser adminUser = AdminUserService.getAdminUser( request );
                List<Unit> listUnits = _unitService.getUnitsByIdUser( adminUser.getUserId( ), false );
                List<Unit> listAuthorizedUnits = new ArrayList<>( );
                if ( unitSelected != null )
                {
                    for ( Unit userUnit : listUnits )
                    {
                        if ( ( userUnit.getIdUnit( ) == unitSelected.getIdUnit( ) ) || _unitService.isParent( unitSelected, userUnit ) )
                        {
                            listAuthorizedUnits.add( userUnit );
                        } else if ( _unitService.isParent( userUnit, unitSelected ) )
                        {
                            listAuthorizedUnits.add( unitSelected );
                        }
                    }
                } else
                {
                    listAuthorizedUnits = listUnits;
                }

                List<Sector> listSectors = new ArrayList<>( );
                for ( Unit userUnit : listAuthorizedUnits )
                {
                    // Trie les secteurs en retirant SC et Jardins pour la DEVE
                    List<Sector> listSectorsOfUnit = _sectorService.loadByIdUnitWithoutSpecificDeveUnits( userUnit.getIdUnit( ) );
                    for ( Sector sector : listSectors )
                    {
                        for ( Sector sectorUnit : listSectorsOfUnit )
                        {
                            if ( sector.getIdSector( ) == sectorUnit.getIdSector( ) )
                            {
                                listSectorsOfUnit.remove( sectorUnit );
                                break;
                            }
                        }
                    }
                    listSectors.addAll( listSectorsOfUnit );
                }
                ReferenceList refListSectorsOfUnit = ListUtils.toReferenceList( listSectors, "idSector", "name", StringUtils.EMPTY, true );

                jsonStringer.object( ).key( MARK_SECTEUR_LIST ).array( );
                for ( ReferenceItem sector : refListSectorsOfUnit )
                {
                    jsonStringer.object( ).key( JSON_KEY_ID ).value( sector.getCode( ) ).key( "value" ).value( sector.getName( ) ).endObject( );
                }
                jsonStringer.endArray( ).endObject( );
            } catch ( NumberFormatException e )
            {
                jsonStringer.object( ).key( "errors" ).array( ).value( e.getMessage( ) ).endArray( ).endObject( );
            }
        } catch ( IOException e1 )
        {
            AppLogService.error( e1.getMessage( ) );
        }

    }

    /**
     * Finds all signalement that the user can see, based on its domain
     *
     * @param request
     *            The HTTP request
     * @return redirection url
     */
    public String getDisplaySignalement( HttpServletRequest request )
    {
        SignalementFilter filter = getSignalementFilter( request );

        Map<String, Object> model = new HashMap<>( );

        List<State> listeEtat = getListeEtats( );
        model.put( MARK_ETATS_LIST, listeEtat );
        model.put( MARK_TITLE, I18nService.getLocalizedString( PAGE_TITLE_MANAGE_SIGNALEMENT, getLocale( ) ) );

        // the filter
        model.put( SignalementConstants.MARK_FILTER, filter );
        model.put( SignalementConstants.MARK_LOCALE, request.getLocale( ) );

        model.put( MARK_ID_ETATS_DEFAULT, ID_ETATS_DEFAULT );

        // Mise à jour du filtre en fonction des données du domaine
        int idDomaine = filter.getIdDomaine( );
        DomaineFonctionnel domaineFonc = _domaineFonctionnelService.getById( idDomaine );
        if ( null == domaineFonc )
        {
            AdminUser adminUser = AdminUserService.getAdminUser( request );
            // Récupération des domaines de l'utilisateur
            Collection<DomaineFonctionnel> domainesFonctionnels = _domaineFonctionnelService.getAllDomainesFonctionnelActifs( );
            domainesFonctionnels = RBACService.getAuthorizedCollection( domainesFonctionnels, DomaineFonctionnelSignalementResourceIdService.PERMISSION_CONSULT_SIGNALEMENT, adminUser );
            domaineFonc = domainesFonctionnels.iterator( ).next( );
        }

        // Arrondissements
        if ( CollectionUtils.isNotEmpty( domaineFonc.getArrondissementsIds( ) ) )
        {
            List<Integer> filterArrondissements = filter.getListIdArrondissements( );
            List<Integer> domainArrondissements = new ArrayList<>( domaineFonc.getArrondissementsIds( ) );
            if ( CollectionUtils.isNotEmpty( filterArrondissements ) )
            {
                domainArrondissements.retainAll( filterArrondissements );
                if ( CollectionUtils.isEmpty( domainArrondissements ) )
                {
                    domainArrondissements = new ArrayList<>( domaineFonc.getArrondissementsIds( ) );
                }
                filter.setListIdArrondissements( domainArrondissements );
            } else
            {
                filter.setListIdArrondissements( domainArrondissements );
            }
        } else
        {
            List<Integer> noRessource = new ArrayList<>( );
            noRessource.add( NO_RESOURCE_FOUND );
            filter.setListIdArrondissements( noRessource );
        }

        // Quartier
        boolean isAllQuartier = false;
        boolean filtreQuartier = false;
        if ( CollectionUtils.isNotEmpty( domaineFonc.getQuartiersIds( ) ) )
        {
            List<Integer> filterQuartiers = filter.getListIdQuartier( );
            List<Integer> domainQuartiers = new ArrayList<>( domaineFonc.getQuartiersIds( ) );
            if ( CollectionUtils.isNotEmpty( filterQuartiers ) )
            {
                filtreQuartier = true;
                domainQuartiers.retainAll( filterQuartiers );
                if ( CollectionUtils.isEmpty( domainQuartiers ) )
                {
                    domainQuartiers = new ArrayList<>( domaineFonc.getArrondissementsIds( ) );
                }
                filter.setListIdQuartier( domainQuartiers );
            } else if ( domainQuartiers.size( ) == 1 )
            {
                // Pas de filtre et 1 seul champ -> selectionné par défaut
                filtreQuartier = true;
                filter.setListIdQuartier( domainQuartiers );
            } else if ( domainQuartiers.size( ) > 1 )
            {
                // Pas de filtre et plusieurs champs -> Valeur selectionnée à vide mais recherche sur tous les quartiers
                filter.setListIdQuartier( domainQuartiers );
                isAllQuartier = true;
            }
        } else
        {
            List<Integer> noRessource = new ArrayList<>( );
            noRessource.add( NO_RESOURCE_FOUND );
            filter.setListIdQuartier( noRessource );
        }

        // Secteurs
        List<Integer> listUnitsIds = domaineFonc.getUnitIds( );
        if ( CollectionUtils.isNotEmpty( listUnitsIds ) )
        {
            filter.setListIdUnit( listUnitsIds );
        } else
        {
            List<Integer> noRessource = new ArrayList<>( );
            noRessource.add( NO_RESOURCE_FOUND );
            filter.setListIdSecteurAutorises( noRessource );
        }

        // Catégories
        if ( CollectionUtils.isNotEmpty( domaineFonc.getTypesSignalementIds( ) ) )
        {
            filter.setListIdCategories( new ArrayList<>( domaineFonc.getTypesSignalementIds( ) ) );
        } else
        {
            List<Integer> noRessource = new ArrayList<>( );
            noRessource.add( NO_RESOURCE_FOUND );
            filter.setListIdCategories( noRessource );
        }
        
        if ( filter.getIdDirection( ) == 94 )
        {
            filter.setIdDirection( 1 );
        }
        Integer totalResult = _signalementService.countIdSignalementByFilter( filter, getPlugin( ) );
        List<Signalement> signalementList = _signalementService.findByFilter( filter, getPaginationProperties( request, totalResult ), true );

        if ( isAllQuartier )
        {
            filter.setListIdQuartier( new ArrayList<>( ) );
        }

        Unit unitPrincipaleSector = null;

        // get the first unit linked to the signalement sector
        for ( Signalement signalement : signalementList )
        {
            Sector sector = signalement.getSecteur( );
            List<Unit> listUnitsSector = _unitService.findBySectorId( sector.getIdSector( ) );

            for ( Unit unit : listUnitsSector )
            {
                if ( unit.getIdParent( ) == 0 )
                {
                    unitPrincipaleSector = unit;
                    signalement.setDirectionSector( unitPrincipaleSector );
                }
            }
        }

        // sort list by first unit of each signalement
        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = request.getParameter( Parameters.SORTED_ASC );
        if ( StringUtils.isNotBlank( strSortedAttributeName ) && strSortedAttributeName.equals( "unit.label" ) )
        {
            DirectionComparator c = new DirectionComparator( );
            c.setSortOrder( strAscSort );
            Collections.sort( signalementList, c );
        }

        ResultList<Signalement> resultListSignalement = new ResultList<>( );

        resultListSignalement.addAll( signalementList );

        resultListSignalement.setTotalResult( signalementList.size( ) );
        LocalizedDelegatePaginator<Signalement> paginator = this.getPaginator( request, resultListSignalement, URL_JSP_DISPLAY_SIGNALEMENTS, totalResult );

        anomaliesCount = totalResult;

        // the paginator
        model.put( SignalementConstants.MARK_NB_ITEMS_PER_PAGE, StringUtils.EMPTY + _nItemsPerPage );
        model.put( SignalementConstants.MARK_PAGINATOR, paginator );

        // workflow : recuperation des etats et des actions possibles pour les signalements de la page
        Map<String, String> mapStates = new HashMap<>( );
        WorkflowService workflowService = WorkflowService.getInstance( );
        Integer signalementWorkflowId = _signalementWorkflowService.getSignalementWorkflowId( );
        if ( workflowService.isAvailable( ) )
        {
            for ( Signalement signalement : paginator.getPageItems( ) )
            {
                // etat
                State state = workflowService.getState( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, signalementWorkflowId, null );

                mapStates.put( signalement.getId( ).toString( ), state == null ? "Non défini" : state.getName( ) );
            }
        }

        // Reaffichage des options avancees
        boolean hasCriteresAvances = hasCriteresAvances( filter, domaineFonc.getArrondissementsIds( ) ) || filtreQuartier;

        model.put( MARK_HAS_ADVANCED_CRITERIAS, hasCriteresAvances );

        model.put( MARK_SIGNALEMENT_LIST, paginator.getPageItems( ) );
        model.put( MARK_ETATS, mapStates );
        model.put( MARK_ADVANCED_SEARCH_STATES, ADVANCED_SEARCH_STATES );
        model.put( MARK_MAP_MAX_RESULTS, AppPropertiesService.getPropertyInt( PROPERTY_MAP_MAX_RESULTS, 0 ) );

        if ( filter.getIdDirection( ) == 1 )
        {
            filter.setIdDirection( 94 );
        }
        model.put( MARK_FILTER, filter );
        _signalementFilter = filter;

        return getAdminPage( getTemplate( TEMPLATE_DISPLAY_SIGNALEMENT, model ) );

    }

    public void getDomain( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        int idDomaine = Integer.parseInt( request.getParameter( "idDomaine" ) );
        DomaineFonctionnel domFonc = _domaineFonctionnelService.getById( idDomaine );

        ReferenceList arrondissementRefList = ListUtils.toReferenceList( arrondissements, "id", "numero", null );
        ReferenceList quartierRefList = ListUtils.toReferenceList( _conseilQuartier.selectQuartiersList( ), "idConsqrt", "nomConsqrt", null );
        ReferenceItem defaultItem = new ReferenceItem( );
        defaultItem.setName( "" );
        defaultItem.setCode( "-1" );
        ReferenceList emptyList = new ReferenceList( );

        // Directions
        Set<Unit> directions = new TreeSet<>( new Comparator<Unit>( )
        {

            @Override
            public int compare( Unit o1, Unit o2 )
            {
                if ( o1 == null || o2 == null )
                    return 0;
                
                return o1.getLabel( ).compareTo( o2.getLabel( ) );
            }
            
        });
        if ( CollectionUtils.isNotEmpty( domFonc.getUnitIds( ) ) )
        {
            List<Unit> firstLevelUnits = _unitService.getUnitsFirstLevel( false );
            for ( Unit unit : firstLevelUnits )
            {
                List<Unit> children = _unitService.getSubUnits( unit.getIdUnit( ), false );
                if ( CollectionUtils.isNotEmpty( domFonc.getUnitIds( ) ) && domFonc.getUnitIds( ).contains( unit.getIdUnit( ) ) )
                {
                    directions.add( unit );
                }
                for ( Unit child : children )
                {
                    if ( CollectionUtils.isNotEmpty( domFonc.getUnitIds( ) ) && domFonc.getUnitIds( ).contains( child.getIdUnit( ) ) )
                    {
                        directions.add( unit );
                    }
                }
            }
            ReferenceList directionRefList = ReferenceList.convert( directions, "idUnit", "label", true );

            directionRefList.add( 0, defaultItem );
            domFonc.setDirectionsRefList( directionRefList );
        } else
        {
            domFonc.setDirectionsRefList( emptyList );
        }

        // Arrondissements
        if ( CollectionUtils.isEmpty( domFonc.getArrondissementsIds( ) ) )
        {
            domFonc.setArrondissementsRefList( emptyList );
        } else
        {
            domFonc.setArrondissementsRefList( ListUtils.retainReferenceList( arrondissementRefList, domFonc.getArrondissementsIds( ), false ) );
        }

        // Quartier
        if ( CollectionUtils.isEmpty( domFonc.getQuartiersIds( ) ) )
        {
            domFonc.setQuartierRefList( emptyList );
        } else
        {
            domFonc.setQuartierRefList( ListUtils.retainReferenceList( quartierRefList, domFonc.getQuartiersIds( ), false ) );
        }

        // Types anomalies
        if ( CollectionUtils.isEmpty( domFonc.getTypesSignalementIds( ) ) )
        {
            domFonc.setTypesAnomalieRefList( emptyList );
        } else
        {
            ReferenceList typesAnomaliesList = new ReferenceList( );
            for ( TypeSignalement typeSignalement : typesAnomalies )
            {
                if ( CollectionUtils.isNotEmpty( domFonc.getTypesSignalementIds( ) )
                        && ( domFonc.getTypesSignalementIds( ).contains( typeSignalement.getIdCategory( ) ) || domFonc.getTypesSignalementIds( ).contains( typeSignalement.getId( ) ) ) )
                {
                    TypeSignalementItem typeItem = new TypeSignalementItem( );
                    typeItem.setChecked( false );
                    typeItem.setCode( Integer.toString( typeSignalement.getId( ) ) );
                    typeItem.setName( typeSignalement.getFormatTypeSignalement( ) );
                    typeItem.setIdCategory( typeSignalement.getIdCategory( ) );
                    typesAnomaliesList.add( typeItem );
                }
            }
            domFonc.setTypesAnomalieRefList( typesAnomaliesList );
        }

        // Secteurs
        if ( CollectionUtils.isNotEmpty( directions ) )
        {
            List<Unit> directionList = new ArrayList<>( );
            directionList.addAll( directions );
            List<Sector> sectors = getSectorsByUnits( directionList );
            ReferenceList secteursRefList = ReferenceList.convert( sectors, "idSector", "name", true );
            secteursRefList.add( 0, defaultItem );
            domFonc.setSecteursRefList( secteursRefList );
        } else
        {
            domFonc.setSecteursRefList( emptyList );
        }

        ObjectMapper mapper = new ObjectMapper( );
        String domFoncJson = mapper.writeValueAsString( domFonc );
        response.setContentType( "application/json" );
        try
        {
            response.getWriter( ).print( domFoncJson );
        } catch ( IOException e )
        {
            AppLogService.error( e.getMessage( ) );
        }
    }

    /**
     * Returns a json, containing all domains, with their criterias
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    public void initDomains( HttpServletRequest request, HttpServletResponse response ) throws JsonGenerationException, JsonMappingException, IOException
    {
        AdminUser adminUser = AdminUserService.getAdminUser( request );

        int idDomaine = Integer.parseInt( request.getParameter( "idDomaine" ) );

        // Récupération des domaines de l'utilisateur
        Collection<DomaineFonctionnel> domainesFonctionnels = _domaineFonctionnelService.getAllDomainesFonctionnelActifs( );
        domainesFonctionnels = RBACService.getAuthorizedCollection( domainesFonctionnels, DomaineFonctionnelSignalementResourceIdService.PERMISSION_CONSULT_SIGNALEMENT, adminUser );

        // Arrondissements
        arrondissements = _arrondissementService.getAllArrondissement( );
        ReferenceList arrondissementRefList = ListUtils.toReferenceList( arrondissements, "id", "numero", null );

        // Quartier
        ReferenceList quartierRefList = ListUtils.toReferenceList( _conseilQuartier.selectQuartiersList( ), "idConsqrt", "nomConsqrt", null );

        // Types anomalie
        typesAnomalies = _typeSignalementService.getAllTypeSignalementActif( );

        ReferenceItem defaultItem = new ReferenceItem( );
        defaultItem.setName( "" );
        defaultItem.setCode( "-1" );

        ReferenceList emptyList = new ReferenceList( );

        // on ne récupère que les infos completes du 1er domaine ou du domaine selectionné
        boolean isDomaineCompletRenseigne = false;
        for ( DomaineFonctionnel domFonc : domainesFonctionnels )
        {
            if ( ( !isDomaineCompletRenseigne && ( idDomaine == 0 ) ) || ( idDomaine == domFonc.getId( ) ) )
            {
                // Directions
                Set<Unit> directions = new HashSet<>( );
                if ( CollectionUtils.isNotEmpty( domFonc.getUnitIds( ) ) )
                {
                    List<Unit> firstLevelUnits = _unitService.getUnitsFirstLevel( false );
                    for ( Unit unit : firstLevelUnits )
                    {
                        List<Unit> children = _unitService.getSubUnits( unit.getIdUnit( ), false );
                        if ( CollectionUtils.isNotEmpty( domFonc.getUnitIds( ) ) && domFonc.getUnitIds( ).contains( unit.getIdUnit( ) ) )
                        {
                            directions.add( unit );
                        }
                        for ( Unit child : children )
                        {
                            if ( CollectionUtils.isNotEmpty( domFonc.getUnitIds( ) ) && domFonc.getUnitIds( ).contains( child.getIdUnit( ) ) )
                            {
                                directions.add( unit );
                            }
                        }
                    }
                    ReferenceList directionRefList = ReferenceList.convert( directions, "idUnit", "label", true );

                    directionRefList.add( 0, defaultItem );
                    domFonc.setDirectionsRefList( directionRefList );
                } else
                {
                    domFonc.setDirectionsRefList( emptyList );
                }

                // Arrondissements
                if ( CollectionUtils.isEmpty( domFonc.getArrondissementsIds( ) ) )
                {
                    domFonc.setArrondissementsRefList( emptyList );
                } else
                {
                    domFonc.setArrondissementsRefList( ListUtils.retainReferenceList( arrondissementRefList, domFonc.getArrondissementsIds( ), false ) );
                }

                // Quartier
                if ( CollectionUtils.isEmpty( domFonc.getQuartiersIds( ) ) )
                {
                    domFonc.setQuartierRefList( emptyList );
                } else
                {
                    domFonc.setQuartierRefList( ListUtils.retainReferenceList( quartierRefList, domFonc.getQuartiersIds( ), false ) );
                }

                // Types anomalies
                if ( CollectionUtils.isEmpty( domFonc.getTypesSignalementIds( ) ) )
                {
                    domFonc.setTypesAnomalieRefList( emptyList );
                } else
                {
                    ReferenceList typesAnomaliesList = new ReferenceList( );
                    for ( TypeSignalement typeSignalement : typesAnomalies )
                    {
                        if ( CollectionUtils.isNotEmpty( domFonc.getTypesSignalementIds( ) )
                                && ( domFonc.getTypesSignalementIds( ).contains( typeSignalement.getIdCategory( ) ) || domFonc.getTypesSignalementIds( ).contains( typeSignalement.getId( ) ) ) )
                        {
                            TypeSignalementItem typeItem = new TypeSignalementItem( );
                            typeItem.setChecked( false );
                            typeItem.setCode( Integer.toString( typeSignalement.getId( ) ) );
                            typeItem.setName( typeSignalement.getFormatTypeSignalement( ) );
                            typeItem.setIdCategory( typeSignalement.getIdCategory( ) );
                            typesAnomaliesList.add( typeItem );
                        }
                    }
                    domFonc.setTypesAnomalieRefList( typesAnomaliesList );
                }

                // Secteurs
                if ( CollectionUtils.isNotEmpty( directions ) )
                {
                    List<Unit> directionList = new ArrayList<>( );
                    directionList.addAll( directions );
                    List<Sector> sectors = getSectorsByUnits( directionList );
                    ReferenceList secteursRefList = ReferenceList.convert( sectors, "idSector", "name", true );
                    secteursRefList.add( 0, defaultItem );
                    domFonc.setSecteursRefList( secteursRefList );
                } else
                {
                    domFonc.setSecteursRefList( emptyList );
                }
                isDomaineCompletRenseigne = true;
            }
        }

        ObjectMapper mapper = new ObjectMapper( );
        String domainesFoncJson = mapper.writeValueAsString( domainesFonctionnels );
        response.setContentType( "application/json" );
        try
        {
            response.getWriter( ).print( domainesFoncJson );
        } catch ( IOException e )
        {
            AppLogService.error( e.getMessage( ) );
        }
    }

    /**
     * Gets all Sectors based on Units
     *
     * @param listUnits
     * @return
     */
    public List<Sector> getSectorsByUnits( List<Unit> listUnits )
    {
        List<Sector> listSectors = new ArrayList<Sector>( );
        for ( Unit unit : listUnits )
        {
            List<Sector> listSectorsOfUnit = _sectorService.loadByIdUnitWithoutSpecificDeveUnits( unit.getIdUnit( ) );
            for ( Sector sector : listSectors )
            {
                for ( Sector sectorUnit : listSectorsOfUnit )
                {
                    if ( sector.getIdSector( ) == sectorUnit.getIdSector( ) )
                    {
                        listSectorsOfUnit.remove( sectorUnit );
                        break;
                    }
                }
            }
            listSectors.addAll( listSectorsOfUnit );
        }
        return listSectors;
    }

    /**
     * Get the sector list attach to the selected unit
     *
     * @param request
     *            the request
     * @param response
     *            the response
     */
    public void getSectorListByIdDirectionForDisplay( HttpServletRequest request, HttpServletResponse response )
    {
        int idDomaine = Integer.parseInt( request.getParameter( "idDomaine" ) );
        DomaineFonctionnel domFonc = _domaineFonctionnelService.getById( idDomaine );
        
        String strDirectionId = request.getParameter( PARAMETER_DIRECTION_ID );        
        
        JSONBuilder jsonStringer;
        response.setContentType( "application/json" );
        try
        {
            jsonStringer = new JSONBuilder( response.getWriter( ) );
            try
            {
                List<Unit> units = new ArrayList<>( );
                
                if ( CollectionUtils.isNotEmpty( domFonc.getUnitIds( ) ) ) {
                    
                    for ( Integer idUnit : domFonc.getUnitIds( ) ) {
                        Unit unitSelected = _unitService.getUnit( idUnit, false );
                        if( unitSelected.getIdParent( ) == Integer.parseInt( strDirectionId ) ) {
                            units.add( unitSelected );
                        }                        
                    }                   
                }                

                if ( CollectionUtils.isNotEmpty( units )  )
                {
                    List<Sector> listSectorsForSelectedUnit = getSectorsByUnits( units );                    

                    ReferenceList refListSectorsOfUnit = ListUtils.toReferenceList( listSectorsForSelectedUnit, "idSector", "name", StringUtils.EMPTY, true );

                    jsonStringer.object( ).key( MARK_SECTEUR_LIST ).array( );
                    for ( ReferenceItem sector : refListSectorsOfUnit )
                    {
                        jsonStringer.object( ).key( JSON_KEY_ID ).value( sector.getCode( ) ).key( "value" ).value( sector.getName( ) ).endObject( );
                    }
                    jsonStringer.endArray( ).endObject( );
                }
            } catch ( NumberFormatException e )
            {
                jsonStringer.object( ).key( "errors" ).array( ).value( e.getMessage( ) ).endArray( ).endObject( );
            }
        } catch ( IOException e1 )
        {
            AppLogService.error( e1 );
        }
    }

    /**
     * List of states available for filtering
     *
     * @return
     */
    private List<State> getListeEtats( )
    {
        List<State> listeEtat = ( List<State> ) WorkflowService.getInstance( ).getAllStateByWorkflow( 2, getUser( ) );
        List<State> listeTemp = new ArrayList<State>( );
        String strIdStateInit = AppPropertiesService.getProperty( STATE_INIT );

        // Delete the "Etat initial" state from the list
        if ( StringUtils.isNotBlank( strIdStateInit ) )
        {
            int nIdStateInit = Integer.parseInt( strIdStateInit );

            for ( State state : listeEtat )
            {
                if ( state.getId( ) == nIdStateInit )
                {
                    listeTemp.add( state );
                }
            }
            listeEtat.removeAll( listeTemp );
        }

        Collections.sort( listeEtat, new Comparator<State>( )
        {
            @Override
            public int compare( State o1, State o2 )
            {
                Integer o1Order = o1.getOrder( );
                Integer o2Order = o2.getOrder( );
                return o1Order.compareTo( o2Order );
            }
        } );
        return listeEtat;
    }

    /**
     * FInds all the units matchings the localization and within the specified radius
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    public void getSectorsByGeomAndUnits( HttpServletRequest request, HttpServletResponse response ) throws JsonGenerationException, JsonMappingException, IOException
    {
        String strLng = request.getParameter( PARAMETER_LNG );
        String strLat = request.getParameter( PARAMETER_LAT );
        String strTypeSignalementId = request.getParameter( PARAMETER_TYPE_SIGNALEMENT_ID );

        Integer nTypeSignalementId = null;
        Double nLat = null;
        Double nLng = null;
        try
        {
            if ( !StringUtils.isEmpty( strLat ) )
            {
                nLat = Double.parseDouble( strLat );
            }
        } catch ( NumberFormatException e )
        {
            throw new FunctionnalException( SignalementConstants.MESSAGE_ERROR_OCCUR );
        }

        try
        {
            if ( !StringUtils.isEmpty( strLng ) )
            {
                nLng = Double.parseDouble( strLng );
            }
        } catch ( NumberFormatException e )
        {
            throw new FunctionnalException( SignalementConstants.MESSAGE_ERROR_OCCUR );
        }

        try
        {
            if ( !StringUtils.isEmpty( strTypeSignalementId ) )
            {
                nTypeSignalementId = Integer.parseInt( strTypeSignalementId );
            }
        } catch ( NumberFormatException e )
        {
            throw new FunctionnalException( SignalementConstants.MESSAGE_ERROR_OCCUR );
        }

        Integer radius = AppPropertiesService.getPropertyInt( PROPERTY_UNITS_RADIUS, 0 );

        // Entite calculée par défaut de l'anomalie
        Unit defaultUnit = _unitSiraService.findUnitByGeomAndTypeSignalement( nLng, nLat, nTypeSignalementId );
        // Récupération des secteurs liés à cette direction et celle de DEVE
        List<Integer> idUnits = new ArrayList<>( );
        idUnits.add( defaultUnit.getIdUnit( ) );
        idUnits.add( Integer.parseInt( SignalementConstants.UNIT_DEVE ) );

        // Secteur calculé par défaut de l'anomalie
        Unit majorUnit = _signalementService.getMajorUnit( nTypeSignalementId, nLng, nLat );
        Sector computedSector = null;
        if ( null != majorUnit )
        {
            computedSector = _sectorService.getSectorByGeomAndUnit( nLng, nLat, majorUnit.getIdUnit( ) );
        }

        List<Sector> sectorList = _sectorService.findSectorsByDirectionsAndGeom( nLng, nLat, radius, idUnits );

        JSONObject result = new JSONObject( );
        result.accumulate( "computedSector", computedSector );
        result.accumulate( "sectors", sectorList );

        response.setContentType( "application/json" );
        try
        {
            response.getWriter( ).print( result.toString( ) );
        } catch ( IOException e )
        {
            AppLogService.error( e.getMessage( ) );
        }

    }

    public void reinitPaginatorCurrentPageIndex( )
    {
        _strCurrentPageIndex = "";
    }

    /**
     * Get as json all signalements in purpose to display them as marker on the map
     *
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    public void getSignalementsJsonForMap( HttpServletRequest request, HttpServletResponse response ) throws JsonGenerationException, JsonMappingException, IOException
    {
        // Executes only if anomalies count < max results
        int maxResults = AppPropertiesService.getPropertyInt( PROPERTY_MAP_MAX_RESULTS, 0 );
        if ( ( anomaliesCount != null ) && ( anomaliesCount > maxResults ) )
        {
            response.setContentType( "application/json" );
            response.getWriter( ).print( "" );
            return;
        }

        SignalementFilter filter = _signalementFilter;
        List<SignalementMapMarkerDTO> signalementMarkers = new ArrayList<>( );

        List<Signalement> signalements = new ArrayList<>( );

        if ( CollectionUtils.isNotEmpty( dashboardSignalementList ) )
        {
            signalements = _signalementService.getByIds( dashboardSignalementList, null, null );
        } else if ( filter != null )
        {
            List<Integer> signalementIds = _signalementService.getIdsSignalementByFilter( filter );
            signalements = _signalementService.getByIds( signalementIds, null, null );
        }

        for ( Signalement signalement : signalements )
        {
            SignalementMapMarkerDTO sigMarker = new SignalementMapMarkerDTO( );
            sigMarker.setIdSignalement( signalement.getId( ) );

            // Date creation
            String dateCreation = signalement.getDateCreation( ) + " " + DateUtils.getHourFr( signalement.getHeureCreation( ) );
            String dateCreationTitle = I18nService.getLocalizedString( "dansmarue.map.tooltips.datecreation", request.getLocale( ) );
            sigMarker.addTooltipText( dateCreationTitle, dateCreation );

            // Adresse
            String adresse = signalement.getAdresses( ).get( 0 ).getAdresse( );
            String adresseTitle = I18nService.getLocalizedString( "dansmarue.map.tooltips.adresse", request.getLocale( ) );
            sigMarker.addTooltipText( adresseTitle, adresse );

            // Priorite
            String priorite = signalement.getPrioriteName( );
            String prioriteTitle = I18nService.getLocalizedString( "dansmarue.map.tooltips.priorite", request.getLocale( ) );
            sigMarker.addTooltipText( prioriteTitle, priorite );
            
            // Type Signalement
            String typeSignalement = signalement.getTypeSignalement( ).getLibelle( );
            String typeSignalementTitle = I18nService.getLocalizedString( "dansmarue.map.tooltips.typeSignalement", request.getLocale( ) );
            sigMarker.addTooltipText( typeSignalementTitle, typeSignalement );

            // Lng
            sigMarker.setLng( signalement.getAdresses( ).get( 0 ).getLng( ) );

            // Lat
            sigMarker.setLat( signalement.getAdresses( ).get( 0 ).getLat( ) );

            // State
            State stateSignalement = WorkflowService.getInstance( ).getState( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE,
                    _signalementWorkflowService.getSignalementWorkflowId( ), null );
            if ( GREEN_MARKER_STATES.contains( stateSignalement.getId( ) ) )
            {
                sigMarker.setIconName( LeafletDansMaRueConstants.GREEN_ICON );
            } else if ( YELLOW_MARKER_STATES.contains( stateSignalement.getId( ) ) )
            {
                sigMarker.setIconName( LeafletDansMaRueConstants.YELLOW_ICON );
            } else
            {
                sigMarker.setIconName( LeafletDansMaRueConstants.RED_ICON );
            }

            signalementMarkers.add( sigMarker );
        }
        response.setContentType( "application/json" );
        ObjectMapper mapper = new ObjectMapper( );
        String sigMarkers = mapper.writeValueAsString( signalementMarkers );

        try
        {
            response.getWriter( ).print( sigMarkers );
        } catch ( IOException e )
        {
            AppLogService.error( e.getMessage( ) );
        }
    }

    private boolean hasCriteresAvances( SignalementFilter filter, List<Integer> availableArrondissementIds )
    {
        boolean hasCritereAdresse = StringUtils.isNotBlank( filter.getAdresse( ) );
        boolean hasCritereMail = StringUtils.isNotBlank( filter.getMail( ) );
        boolean hasCritereCommentaire = StringUtils.isNotBlank( filter.getCommentaire( ) );
        boolean hasCritereArrondissement = CollectionUtils.isNotEmpty( filter.getListIdArrondissements( ) ) && !( filter.getListIdArrondissements( ).containsAll( availableArrondissementIds ) );
        boolean hasCritereQuartier = CollectionUtils.isNotEmpty( filter.getListIdQuartier( ) );
        boolean hasCritereEtat = false;
        for ( EtatSignalement etat : filter.getEtats( ) )
        {
            if ( ADVANCED_SEARCH_STATES.contains( new Integer( etat.getId( ).intValue( ) ) ) )
            {
                hasCritereEtat = true;
            }
        }

        return hasCritereAdresse || hasCritereMail || hasCritereCommentaire || hasCritereArrondissement || hasCritereEtat || hasCritereQuartier;
    }

    private String encodeURIComponent( String component )
    {

        String result = null;

        try {
            result = URLEncoder.encode ( component, "UTF-8" )
                    .replaceAll( "\\%28", "(" )
                    .replaceAll( "\\%29", ")" )
                    .replaceAll( "\\+", "%20" )
                    .replaceAll( "\\%27", "'" )
                    .replaceAll( "\\%21", "!" )
                    .replaceAll( "\\%7E", "~" );
        } catch ( UnsupportedEncodingException e )
        {
            result = component;
        }

        return result;

    }

}
