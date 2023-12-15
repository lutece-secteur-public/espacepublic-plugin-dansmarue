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
package fr.paris.lutece.plugins.dansmarue.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import au.com.bytecode.opencsv.CSVWriter;
import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.ConseilQuartier;
import fr.paris.lutece.plugins.dansmarue.business.entities.DomaineFonctionnel;
import fr.paris.lutece.plugins.dansmarue.business.entities.EtatSignalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.FeuilleDeTournee;
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
import fr.paris.lutece.plugins.dansmarue.service.IFeuilleDeTourneeService;
import fr.paris.lutece.plugins.dansmarue.service.IPhotoService;
import fr.paris.lutece.plugins.dansmarue.service.IPrioriteService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementExportService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ISignaleurService;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.plugins.dansmarue.service.actions.ISignalementAction;
import fr.paris.lutece.plugins.dansmarue.service.actions.SignalementFields;
import fr.paris.lutece.plugins.dansmarue.service.dto.SignalementExportCSVDTO;
import fr.paris.lutece.plugins.dansmarue.service.dto.SignalementMapMarkerDTO;
import fr.paris.lutece.plugins.dansmarue.service.impl.CachesService;
import fr.paris.lutece.plugins.dansmarue.service.role.DomaineFonctionnelSignalementResourceIdService;
import fr.paris.lutece.plugins.dansmarue.service.role.SignalementViewRoleService;
import fr.paris.lutece.plugins.dansmarue.util.constants.DateConstants;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.DirectionComparator;
import fr.paris.lutece.plugins.dansmarue.utils.IDateUtils;
import fr.paris.lutece.plugins.dansmarue.utils.IListUtils;
import fr.paris.lutece.plugins.dansmarue.utils.ISignalementUtils;
import fr.paris.lutece.plugins.dansmarue.utils.ImgUtils;
import fr.paris.lutece.plugins.dansmarue.utils.TypeSignalementItem;
import fr.paris.lutece.plugins.leaflet.modules.dansmarue.consts.LeafletDansMaRueConstants;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.business.sector.Sector;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.service.sector.ISectorService;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.service.unit.IUnitSiraService;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.service.task.ITaskService;
import fr.paris.lutece.portal.business.user.AdminUser;
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

    /** The Constant SELF. */
    private static final String SELF = "_self";

    /** The Constant NUMBER. */
    private static final String NUMBER = "numero";

    /** The Constant ID_DOMAINE. */
    private static final String ID_DOMAINE = "idDomaine";

    /** The Constant EXTENSION_APPLICATION_JSON. */
    private static final String EXTENSION_APPLICATION_JSON = "application/json";

    /** The Constant INVALID_ACTION_ID. */
    private static final String INVALID_ACTION_ID = "Invalid action id ";

    /** The Constant LIBELLE. */
    private static final String LIBELLE = "libelle";

    /** The Constant LINE_BREAK. */
    private static final String LINE_BREAK = "<br />";

    /** The Constant ID_SECTOR. */
    private static final String ID_SECTOR = "idSector";

    /** The Constant SEJ_ID. */
    private static final int SEJ_ID = 94;

    /** The Constant LABEL. */
    private static final String LABEL = "label";

    /** The Constant ID_UNIT. */
    private static final String ID_UNIT = "idUnit";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 9005837483124219369L;

    // RIGHTS
    /** The Constant RIGHT_MANAGE_SIGNALEMENT. */
    public static final String RIGHT_MANAGE_SIGNALEMENT = "SIGNALEMENT_MANAGEMENT";

    // TEMPLATES
    /** The Constant TEMPLATE_MANAGE_SIGNALEMENT. */
    private static final String TEMPLATE_MANAGE_SIGNALEMENT = "admin/plugins/signalement/manage_signalement.html";

    /** The Constant TEMPLATE_DISPLAY_SIGNALEMENT. */
    private static final String TEMPLATE_DISPLAY_SIGNALEMENT = "admin/plugins/signalement/display_signalement.html";

    /** The Constant TEMPLATE_SAVE_SIGNALEMENT. */
    private static final String TEMPLATE_SAVE_SIGNALEMENT = "admin/plugins/signalement/save_signalement.html";

    /** The Constant TEMPLATE_VIEW_SIGNALEMENT. */
    private static final String TEMPLATE_VIEW_SIGNALEMENT = "admin/plugins/signalement/view_signalement.html";

    /** The Constant TEMPLATE_TASK_WORKFLOW. */
    private static final String TEMPLATE_TASK_WORKFLOW = "admin/plugins/signalement/workflow/signalement_form.html";

    /** The Constant TEMPLATE_SIGNALEMENT_HISTORY. */
    private static final String TEMPLATE_SIGNALEMENT_HISTORY = "admin/plugins/signalement/view_history_signalement.html";

    /** The Constant TEMPLATE_SAVE_PHOTO_TO_SIGNALEMENT. */
    private static final String TEMPLATE_SAVE_PHOTO_TO_SIGNALEMENT = "admin/plugins/signalement/add_photo_signalement.html";

    // JSP
    /** The Constant JSP_MANAGE_SIGNALEMENT. */
    private static final String JSP_MANAGE_SIGNALEMENT = "jsp/admin/plugins/signalement/ManageSignalement.jsp";

    /** The Constant JSP_MANAGE_SIGNALEMENT. */
    private static final String JSP_DELETE_SIGNALEMENT = "jsp/admin/plugins/signalement/DoDeleteSignalement.jsp";

    /** The Constant JSP_VIEW_SIGNALEMENT. */
    private static final String JSP_VIEW_SIGNALEMENT = "ViewSignalement.jsp";

    /** The Constant JSP_MODIFY_SIGNALEMENT. */
    private static final String JSP_MODIFY_SIGNALEMENT = "ModifySignalement.jsp";

    /** The Constant JSP_WORKFLOW_ACTION. */
    private static final String JSP_WORKFLOW_ACTION = "jsp/admin/plugins/signalement/WorkflowAction.jsp";

    /** The Constant JSP_WORKFLOW__ACTION. */
    private static final String JSP_WORKFLOW_PROCESS_ACTION = "jsp/admin/plugins/signalement/ProcessAction.jsp";

    /** The Constant JSP_DO_PROCESS_ACTION. */
    private static final String JSP_DO_PROCESS_ACTION = "jsp/admin/plugins/signalement/DoProcessActionWorkflow.jsp";

    /** The Constant JSP_DO_PROCESS_MASS_ACTION. */
    private static final String JSP_DO_PROCESS_MASS_ACTION = "jsp/admin/plugins/signalement/DoMassProcessActionWorkflow.jsp";

    /** The Constant JSP_DO_PROCESS_MASS_ACTION. */
    private static final String JSP_DELETE_PHOTO_SIGNALEMENT = "jsp/admin/plugins/signalement/DoDeletePhotoSignalement.jsp";

    /** The Constant JSP_DO_PROCESS_MASS_ACTION. */
    private static final String JSP_MASS_DELETE_SIGNALEMENT = "jsp/admin/plugins/signalement/DoMassDeleteSignalement.jsp";

    /** The Constant JSP_ADD_PHOTO_TO_SIGNALEMENT. */
    private static final String JSP_ADD_PHOTO_TO_SIGNALEMENT = "AddPhotoToSignalement.jsp";

    /** The Constant URL_JSP_DISPLAY_SIGNALEMENTS *. */
    private static final String URL_JSP_DISPLAY_SIGNALEMENTS = "jsp/admin/plugins/signalement/DisplaySignalement.jsp";

    /** The Constant URL_JSP_GET_ROAD_MAP *. */
    private static final String URL_JSP_GET_ROAD_MAP = "jsp/admin/plugins/ramen/GetRoadMap.jsp";

    /** The Constant CSV_ISO. */
    private static final String CSV_ISO = "ISO-8859-1";

    // PARAMETERS
    /** The Constant PARAMETER_MARK_SIGNALEMENT_ID. */
    public static final String PARAMETER_MARK_SIGNALEMENT_ID = "signalement_id";

    /** The Constant PARAMETER_PRIORITE. */
    public static final String PARAMETER_PRIORITE = "priorite";

    /** The Constant PARAMETER_TYPE_SIGNALEMENT. */
    public static final String PARAMETER_TYPE_SIGNALEMENT = "typeSignalement";

    /** The Constant PARAMETER__MARK_ADRESSE. */
    public static final String PARAMETER_MARK_ADRESSE = "adresse";

    /** The Constant PARAMETER_MAIL. */
    public static final String PARAMETER_MAIL = "mail";

    /** The Constant PARAMETER_PRECISION_LOCALISATION. */
    public static final String PARAMETER_PRECISION_LOCALISATION = "precisionLocalisation";

    /** The Constant PARAMETER_COMMENTAIRE. */
    public static final String PARAMETER_COMMENTAIRE = "commentaire";

    /** The Constant PARAMETER_COMMENTAIRE_AGENT_TERRAIN. */
    public static final String PARAMETER_COMMENTAIRE_AGENT_TERRAIN = "commentaireAgentTerrain";

    /** The Constant PARAMETER_PHOTO. */
    public static final String PARAMETER_PHOTO = "photo";

    /** The Constant PARAMETER_VUE_PHOTO. */
    public static final String PARAMETER_VUE_PHOTO = "vuePhoto";

    /** The Constant PARAMETER_PHOTO_ENSEMBLE. */
    public static final String PARAMETER_PHOTO_ENSEMBLE = "photoEnsemble";

    /** The Constant PARAMETER_PHOTO_PRES. */
    public static final String PARAMETER_PHOTO_PRES = "photoPres";

    /** The Constant PARAMETER_ADRESSE_ID. */
    public static final String PARAMETER_ADRESSE_ID = "adresse_id";

    /** The Constant PARAMETER_PHOTO_ID. */
    public static final String PARAMETER_PHOTO_ID = "photo_id";

    /** The Constant PARAMETER_SIGNALEUR_ID. */
    public static final String PARAMETER_SIGNALEUR_ID = "signaleur_id";

    /** The Constant PARAMETER_TELEPHONE_ID. */
    public static final String PARAMETER_TELEPHONE_ID = "telephone_id";

    /** The Constant PARAMETER_TYPE_SIGNALEMENT_ID. */
    public static final String PARAMETER_TYPE_SIGNALEMENT_ID = "typesignalement_id";

    /** The Constant PARAMETER_COMMENTAIRE_PROG. */
    public static final String PARAMETER_COMMENTAIRE_PROG = "commentaireProgrammation";

    /** The Constant PARAMETER_COMMENTAIRE_PROG. */
    public static final String PARAMETER_DATE_PREVU_TRAITEMENT = "datePrevueTraitement";

    /** The Constant PARAMETER_MARK_ACTION_ID. */
    public static final String PARAMETER_MARK_ACTION_ID = "action_id";

    /** The Constant PARAMETER_LNG. */
    private static final String PARAMETER_LNG = "lng";

    /** The Constant PARAMETER_LAT. */
    private static final String PARAMETER_LAT = "lat";

    /** The Constant PARAMETER_ACTION_NEXT. */
    private static final String PARAMETER_ACTION_NEXT = "next";

    /** The Constant PARAMETER_IS_ROAD_MAP. */
    private static final String PARAMETER_IS_ROAD_MAP = "isRoadMap";

    /** The Constant PARAMETER_DATE_SERVICE. */
    private static final String PARAMETER_DATE_SERVICE = "dateService";

    /** The Constant PARAMETER_SERVICEID. */
    private static final String PARAMETER_SERVICEID = "serviceId";

    /** The Constant PARAMETER_SERVICE_ID. */
    private static final String PARAMETER_SERVICE_ID = "service_id";

    /** The Constant PARAMETER_DIRECTION_ID. */
    private static final String PARAMETER_DIRECTION_ID = "direction_id";

    private static final String PARAMETER_ONGLET = "onglet";

    /** The Constant PARAMETER_FROM_PAGE *. */
    private static final String PARAMETER_FROM_PAGE = "from_page";

    /** The Constant PARAMETER_VALUE_DISPLAY_PAGE *. */
    private static final String PARAMETER_VALUE_DISPLAY_PAGE = "display_page";

    /** The Constant PARAMETER_NEXT_URL. */
    private static final String PARAMETER_NEXT_URL = "next";

    /** The Constant PARAMETER_WEBAPP_RAMEN. */
    private static final String PARAMETER_WEBAPP_RAMEN = "ramen";

    /** The Constant PARAMETER_SECTOR_ID. */
    private static final String PARAMETER_SECTOR_ID = "sector_id";

    /** The Constant PARAMETER_UNIT_ID. */
    private static final String PARAMETER_UNIT_ID = "unit_id";

    /** The Constant JSON_KEY_ID. */
    private static final String JSON_KEY_ID = "id";

    /** The Constant ZERO_VOTE. */
    private static final Integer ZERO_VOTE = 0;

    // MARKERS
    /** The Constant MARK_PRIORITE_LIST. */
    private static final String MARK_PRIORITE_LIST = "priorite_list";

    /** The Constant MARK_TYPE_LIST. */
    private static final String MARK_TYPE_LIST = "type_list";

    /** The Constant MARK_SIGNALEMENT. */
    private static final String MARK_SIGNALEMENT = "signalement";

    /** The Constant MARK_TITLE. */
    public static final String MARK_TITLE = "title";

    /** The Constant MARK_TYPE. */
    public static final String MARK_TYPE = "type";

    /** The Constant MARK_PHOTOS. */
    public static final String MARK_PHOTOS = "photos";

    /** The Constant MARK_SIGNALEUR. */
    public static final String MARK_SIGNALEUR = "signaleur";

    /** The Constant MARK_DIRECTION_LIST. */
    private static final String MARK_DIRECTION_LIST = "direction_list";

    /** The Constant BUTTON_UPDATE_SECTOR_WITH_DIRECTION. */
    private static final String BUTTON_UPDATE_SECTOR_WITH_DIRECTION = "updateSectorsWithDirection";

    /** The Constant MARK_ARRONDISSEMENT_LIST. */
    private static final String MARK_ARRONDISSEMENT_LIST = "arrondissement_list";

    /** The Constant MARK_CONSEIL_QUARTIER_LIST. */
    private static final String MARK_CONSEIL_QUARTIER_LIST = "conseilQuartier_list";

    /** The Constant MARK_VUE_PHOTO_LIST. */
    private static final String MARK_VUE_PHOTO_LIST = "vuePhoto_list";

    /** The Constant MARK_ETATS_LIST. */
    private static final String MARK_ETATS_LIST = "etat_list";

    /** The Constant MARK_WEBAPP_URL. */
    private static final String MARK_WEBAPP_URL = "webapp_url";

    /** The Constant MARK_ETATS. */
    private static final String MARK_ETATS = "map_etats";

    /** The Constant MARK_PROGRAMMING_DATE. */
    private static final String MARK_PROGRAMMING_DATE = "map_programming_date";

    /** The Constant MARK_HAS_SIGNALEMENT_PRESTATAIRE. */
    private static final String MARK_HAS_SIGNALEMENT_PRESTATAIRE = "hasSignalementPrestataire";

    /** The Constant MARK_ACTIONS. */
    private static final String MARK_ACTIONS = "map_actions";

    /** The Constant MARK_POSSIBLE_ACTIONS. */
    private static final String MARK_POSSIBLE_ACTIONS = "possible_actions";

    /** The Constant MARK_SIGNALEMENT_LIST. */
    private static final String MARK_SIGNALEMENT_LIST = "signalement_list";

    /** The Constant MARK_TASK_FORM. */
    private static final String MARK_TASK_FORM = "task_form";

    /** The Constant MARK_HAS_NEXT. */
    private static final String MARK_HAS_NEXT = "has_next";

    /** The Constant MARK_STATE_SIGNALEMENT. */
    private static final String MARK_STATE_SIGNALEMENT = "stateSignalement";

    /** The Constant MARL_STATE_SERVICE_FAIT. */
    private static final String MARK_STATE_SERVICE_FAIT = "serviceFaitValue";

    /** The Constant MARL_USER_SERVICE_FAIT. */
    private static final String MARK_USER_SERVICE_FAIT = "serviceFaitUser";

    /** The Constant MARK_HISTORIQUE_LIST. */
    private static final String MARK_HISTORIQUE_LIST = "historiqueHtml";

    /** The Constant MARK_SECTEUR_LIST. */
    private static final String MARK_SECTEUR_LIST = "secteur_list";

    /** The Constant MARK_MESSAGE_FOR_USER. */
    private static final String MARK_MESSAGE_FOR_USER = "messageForUser";

    /** The Constant MARK_KEY_MAPS. */
    private static final String MARK_KEY_MAPS = "key_maps";

    /** The Constant MARK_ID_ETATS_DEFAULT. */
    private static final String MARK_ID_ETATS_DEFAULT = "etats_default";

    /** The constant MARK_BACK_URL. */
    private static final String MARK_BACK_URL = "back_url";

    /** The Constant MARK_DASHBOARD_CRITERIAS. */
    private static final String MARK_DASHBOARD_CRITERIAS = "dashboard_criterias";

    /** The Constant MARK_MAP_MAX_RESULTS. */
    private static final String MARK_MAP_MAX_RESULTS = "map_max_results";

    /** The Constant MARK_HAS_ADVANCED_CRITERIAS. */
    private static final String MARK_HAS_ADVANCED_CRITERIAS = "has_advanced_criterias";

    private static final String MARK_FEUILLE_DE_TOURNEE_LIST = "feuille_de_tournee_list";

    /** The Constant VUE_ENSEMBLE. */
    public static final Integer OVERVIEW = 1;

    /** The Constant VUE_PRES. */
    public static final Integer DETAILED_VIEW = 0;

    /** The Constant ID_JARDIN. */
    public static final Integer ID_JARDIN = 260;

    // I18N
    /** The Constant PAGE_TITLE_MANAGE_SIGNALEMENT. */
    private static final String PAGE_TITLE_MANAGE_SIGNALEMENT = "dansmarue.page.signalement.manage.title";

    // CONSTANTS
    /** The Constant EMPTY_STRING. */
    private static final String EMPTY_STRING = "";

    /** The Constant MINUS_ONE. */
    private static final String MINUS_ONE = "-1";

    /** The Constant NO_RESOURCE_FOUND. */
    private static final int NO_RESOURCE_FOUND = -1;

    /** The Constant ACTION_MESSAGE_TASK. */
    private static final int [ ] ACTION_MESSAGE_TASK = {
            13, 18, 22
    };

    /** The Constant ID_ACTIONS_SERVICE_FAIT. */
    private static final int [ ] ID_ACTIONS_SERVICE_FAIT = {
            18, 22
    };

    /** The Constant ID_ACTIONS_ACCEPTER. */
    private static final int [ ] ID_ACTIONS_ACCEPTER = {
            13
    };

    /** The Constant ID_ETATS_DEFAULT. */
    private static final int [ ] ID_ETATS_DEFAULT = {
            7, 8, 16, 17, 18, 21
    };

    /** The Constant ID_STATE_SERVICE_PROGRAMME_PRESTATAIRE. */
    private static final String ID_STATE_SERVICE_PROGRAMME_PRESTATAIRE = "signalement.idStateServiceProgrammePrestataire";

    /** The Constant ID_STATE_TRANSFERE_PRESTATAIRE. */
    private static final String ID_STATE_TRANSFERE_PRESTATAIRE = "signalement.idStateTransferePrestataire";

    /** The Constant CSV_SEPARATOR. */
    private static final char CSV_SEPARATOR = ';';

    /** The Constant STATE_INIT. */
    private static final String STATE_NOT_DISPLAY = "signalement.state.not.display";

    // MESSAGES
    /** The Constant MESSAGE_ERROR_EMPTY_FIELD. */
    private static final String MESSAGE_ERROR_EMPTY_FIELD = "dansmarue.message.error.champObligatoireNull";

    /** The Constant MESSAGE_ERREUR_HORS_PARIS. */
    private static final String MESSAGE_ERREUR_HORS_PARIS = "dansmarue.message.error.horsParis";

    /** The Constant MESSAGE_ERREUR_HORS_PARIS. */
    private static final String MESSAGE_ERREUR_INVALID_MAIL = "dansmarue.message.error.invalidMail";

    /** The Constant MESSAGE_ERREUR_SECTEUR_NULL. */
    private static final String MESSAGE_ERREUR_SECTEUR_NULL = "dansmarue.message.error.aucunSecteur";

    /** The Constant MESSAGE_ERROR_EMPTY_PHOTO_FIELD. */
    private static final String MESSAGE_ERROR_EMPTY_PHOTO_FIELD = "dansmarue.message.error.emptyPhoto";

    /** The Constant MESSAGE_ERROR_EXISTING_PHOTO. */
    private static final String MESSAGE_ERROR_EXISTING_PHOTO = "dansmarue.message.error.photo.existante";

    /** The Constant MESSAGE_TITLE_MASS_ACTION_IMPOSSIBLE. */
    private static final String MESSAGE_TITLE_MASS_ACTION_IMPOSSIBLE = "dansmarue.messagetitle.massActionImpossible.message";

    /** The Constant MESSAGE_TITLE_DELETE_PHOTO_SIGNALEMENT. */
    private static final String MESSAGE_TITLE_DELETE_PHOTO_SIGNALEMENT = "dansmarue.messagetitle.deletePhotoSignalement";

    /** The Constant MESSAGE_TITLE_SERVICE_FAIT. */
    private static final String MESSAGE_TITLE_SERVICE_FAIT = "dansmarue.messagetitle.servicefait.confirmation";

    /** The Constant MESSAGE_CONFIRMATION_SERVICE_FAIT. */
    private static final String MESSAGE_CONFIRMATION_SERVICE_FAIT = "dansmarue.message.servicefait.confirmation";

    /** The Constant MESSAGE_CONFIRMATION_DELETE_PHOTO_SIGNALEMENT. */
    private static final String MESSAGE_CONFIRMATION_DELETE_PHOTO_SIGNALEMENT = "dansmarue.message.deletePhotoSignalement";

    /** The Constant MESSAGE_TITLE_MASS_SERVICE_FAIT. */
    private static final String MESSAGE_TITLE_MASS_SERVICE_FAIT = "dansmarue.messagetitle.massActionServiceFait.confirmation";

    /** The Constant MESSAGE_TITLE_MASS_DELETE_SIGNALEMENT. */
    private static final String MESSAGE_CONFIRMATION_MASS_DELETE_SIGNALEMENT = "dansmarue.message.massDeleteSignalement.confirmation";

    /** The Constant MESSAGE_CONFIRMATION_MASS_SERVICE_FAIT. */
    private static final String MESSAGE_CONFIRMATION_MASS_SERVICE_FAIT = "dansmarue.message.massActionServiceFait.confirmation";

    /** The Constant MESSAGE_TITLE_ACCEPTER. */
    private static final String MESSAGE_TITLE_ACCEPTER = "dansmarue.messagetitle.Accepter.confirmation";

    /** The Constant MESSAGE_CONFIRMATION_ACCEPTER. */
    private static final String MESSAGE_CONFIRMATION_ACCEPTER = "dansmarue.message.Accepter.confirmation";

    /** The Constant MESSAGE_TITLE_MASS_ACCEPTER. */
    private static final String MESSAGE_TITLE_MASS_ACCEPTER = "dansmarue.messagetitle.massActionAccepter.confirmation";

    /** The Constant MESSAGE_CONFIRMATION_MASS_ACCEPTER. */
    private static final String MESSAGE_CONFIRMATION_MASS_ACCEPTER = "dansmarue.message.massActionAccepter.confirmation";

    /** The Constant MESSAGE_ACCESS_DENIED. */
    private static final String MESSAGE_ACCESS_DENIED = "user does not have the right to do this";

    /** The Constant MESSAGE_TITLE_MASS_DELETE_SIGNALEMENT. */
    private static final String MESSAGE_TITLE_MASS_DELETE_SIGNALEMENT = "dansmarue.messagetitle.massDeleteSignalement.confirmation";

    /** The Constant MESSAGE_CONFIRMATION_DELETE_SIGNALEMENT. */
    private static final String MESSAGE_CONFIRMATION_DELETE_SIGNALEMENT = "dansmarue.messagetitle.deleteSignalement.confirmation";

    /** The Constant MESSAGE_TITLE_DELETE_SIGNALEMENT. */
    private static final String MESSAGE_TITLE_DELETE_SIGNALEMENT = "dansmarue.messagetitle.deleteSignalement.title";

    /** The Constant MESSAGE_ERROR_ACTION. */
    private static final String MESSAGE_ERROR_ACTION = "dansmarue.erreur.action";

    /** The Constant PROPERTY_UNITS_RADIUS. */
    // PROPERTIES
    private static final String PROPERTY_UNITS_RADIUS = "signalement.near.units.radius";

    /** The Constant PROPERTY_SEARCH_INITIAL_NB_DAYS. */
    private static final String PROPERTY_SEARCH_INITIAL_NB_DAYS = "signalement.reportingList.init.datebegin";

    /** The Constant PROPERTY_MAP_MAX_RESULTS. */
    private static final String PROPERTY_MAP_MAX_RESULTS = "signalement.map.max.results";

    // MEMBERS VARIABLES
    /** The _signalement filter. */
    private SignalementFilter _signalementFilter;

    /** The _mass reports ids. */
    private int [ ] _massSignalementIds;

    /** The _action type. */
    private String _actionType;

    /** The arrondissements. */
    private List<Arrondissement> _arrondissements;

    /** The types anomalies. */
    private List<TypeSignalement> _typesAnomalies;

    /** The feuilles de tournee. */
    private List<FeuilleDeTournee> _feuillesDeTournee;

    /** The dashboard signalement list. */
    private List<Integer> _dashboardSignalementList;

    /** The dashboard criterias. */
    private Map<String, List<String>> _dashboardCriterias;

    /** The anomalies count. */
    private Integer _anomaliesCount;

    // SERVICES
    /** The _signalement service. */
    private transient ISignalementService _signalementService;

    /** The _type signalement service. */
    private transient ITypeSignalementService _typeSignalementService;

    /** The _priorite service. */
    private transient IPrioriteService _prioriteService;

    /** The _signaleur service. */
    private transient ISignaleurService _signaleurService;

    /** The _unit service. */
    private transient IUnitService _unitService;

    /** The _signalement workflow service. */
    private transient IWorkflowService _signalementWorkflowService;

    /** The _adresse service. */
    private transient IAdresseService _adresseService;

    /** The _photo service. */
    private transient IPhotoService _photoService;

    /** The _sector service. */
    private transient ISectorService _sectorService;

    /** The task service. */
    private transient ITaskService _taskService = SpringContextService.getBean( "workflow.taskService" );

    /** The _arrondissement service. */
    private transient IArrondissementService _arrondissementService;

    private transient CachesService _cachesService = SpringContextService.getBean( "signalement.cachesService" );

    /** The conseilQuartier service. */
    private IConseilQuartierService _conseilQuartier;

    /** The unit sira service. */
    private transient IUnitSiraService _unitSiraService;

    /** The _signalementViewRole service. */
    private transient SignalementViewRoleService _signalementViewRoleService;

    /** The _domaineFonctionnelService. */
    private transient IDomaineFonctionnelService _domaineFonctionnelService;

    /** The _signalement export service. */
    private transient ISignalementExportService _signalementExportService;

    private transient IFeuilleDeTourneeService _feuilleTourneeService = SpringContextService.getBean( "feuilleDeTourneeService" );

    /** The signalement utils */
    // UTILS
    private transient ISignalementUtils _signalementUtils = SpringContextService.getBean( "signalement.signalementUtils" );

    /** The date utils */
    private transient IDateUtils _dateUtils = SpringContextService.getBean( "signalement.dateUtils" );

    /** The list utils */
    private transient IListUtils _listUtils = SpringContextService.getBean( "signalement.listUtils" );

    /** The Constant _simple_date_format. */
    private final SimpleDateFormat _simple_date_format = new SimpleDateFormat( DateConstants.DATE_FR );

    /** The green marker states. */
    private List<Integer> _listGreenMarkerStates;

    /** The yellow marker states. */
    private List<Integer> _listYellowMarkerStates;

    /** The Constant PARAMETER_SEARCH. */
    // parameters
    private static final String PARAMETER_SEARCH = "search";

    /** The Constant PARAMETER_ACTION_SERVICE_FAIT PM 2015. */
    private static final String PARAMETER_ACTION_SERVICE_FAIT = "servicefait";

    /** The Constant PARAMETER_ACTION_SERVICE_FAIT. */
    private static final String PARAMETER_ACTION_SERVICE_FAITS = "servicefaits";

    /** The Constant PARAMETER_ACTION_PROGRAMMER. */
    private static final String PARAMETER_ACTION_PROGRAMMER = "programmer";

    /** The Constant PARAMETER_ACTION_REPROGRAMMER. */
    private static final String PARAMETER_ACTION_REPROGRAMMER = "reprogrammer";
    private static final String PARAMETER_ACTION_A_TRAITER= "aTraiter";
    private static final String PARAMETER_ACTION_A_FAIRE_BUREAU = "aFaireBureau";
    private static final String PARAMETER_ACTION_A_FAIRE_TERRAIN = "aFaireTerrain";

    // Actions
    /** The Constant ACTION_SERVICE_FAIT. */
    private static final String ACTION_SERVICE_FAIT = "Déclarer un service fait";

    /** The Constant ACTION_PROGRAMMER. */
    private static final String ACTION_PROGRAMMER = "Programmer";

    /** The Constant ACTION_REPROGRAMMER. */
    private static final String ACTION_REPROGRAMMER = "Reprogrammer";
    private static final String ACTION_A_TRAITER = "A traiter";
    private static final String ACTION_A_FAIRE_BUREAU = "A faire bureau";
    private static final String ACTION_A_FAIRE_TERRAIN = "A faire terrain";

    /** The Constant MESSAGE_ERROR_EMPTY. */
    private static final int MESSAGE_ERROR_EMPTY = 51;

    /** The Constant VALID_EMAIL_ADDRESS_REGEX. */
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE );

    private static final String PARAMETER_IS_REINIT_SEARCH = "isReinitSearch";

    /**
     * Instantiates a new signalement jsp bean.
     */
    public SignalementJspBean( )
    {
        super( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init( HttpServletRequest request, String strRight ) throws AccessDeniedException
    {
        super.init( request, strRight );
        initServices( );
        initStaticMembers( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init( HttpServletRequest request, String strRight, String keyResourceType, String permission ) throws AccessDeniedException
    {
        super.init( request, strRight, keyResourceType, permission );
        initServices( );
        initStaticMembers( );

    }

    /**
     * Check the user functional domains.
     *
     * @throws AccessDeniedException
     *             throws AccessDeniedException
     */
    public void checkUserDomains( ) throws AccessDeniedException
    {
        Collection<DomaineFonctionnel> domainesFonctionnels = _domaineFonctionnelService.getAllDomainesFonctionnelActifs( );
        domainesFonctionnels = RBACService.getAuthorizedCollection( domainesFonctionnels,
                DomaineFonctionnelSignalementResourceIdService.PERMISSION_CONSULT_SIGNALEMENT, getUser( ) );
        if ( CollectionUtils.isEmpty( domainesFonctionnels ) )
        {
            throw new AccessDeniedException(
                    MessageFormat.format( "Acces denied for {0}.", DomaineFonctionnelSignalementResourceIdService.PERMISSION_CONSULT_SIGNALEMENT ) );
        }
    }

    /**
     * Init the static members.
     */
    public void initStaticMembers( )
    {
        // Init map markers
        if ( ( null == _listGreenMarkerStates ) || ( null == _listYellowMarkerStates ) )
        {
            _listGreenMarkerStates = new ArrayList<>( );
            _listYellowMarkerStates = new ArrayList<>( );

            String greenStates = AppPropertiesService.getProperty( SignalementConstants.PROPERTY_MARKER_STATES_GREEN );
            String yellowStates = AppPropertiesService.getProperty( SignalementConstants.PROPERTY_MARKER_STATES_YELLOW );

            String [ ] greenStatesArr = greenStates.split( "," );
            String [ ] yellowStatesArr = yellowStates.split( "," );

            for ( String greenState : greenStatesArr )
            {
                _listGreenMarkerStates.add( Integer.parseInt( greenState ) );
            }

            for ( String yellowState : yellowStatesArr )
            {
                _listYellowMarkerStates.add( Integer.parseInt( yellowState ) );
            }
        }

    }

    /**
     * Inits the services.
     */
    public void initServices( )
    {
        _signalementService = (ISignalementService) SpringContextService.getBean( "signalementService" );
        _typeSignalementService = (ITypeSignalementService) SpringContextService.getBean( "typeSignalementService" );
        _prioriteService = (IPrioriteService) SpringContextService.getBean( "prioriteService" );
        _signaleurService = (ISignaleurService) SpringContextService.getBean( "signaleurService" );
        _unitService = (IUnitService) SpringContextService.getBean( "unittree.unitService" );
        _signalementWorkflowService = (IWorkflowService) SpringContextService.getBean( "signalement.workflowService" );
        _adresseService = (IAdresseService) SpringContextService.getBean( "adresseSignalementService" );
        _photoService = (IPhotoService) SpringContextService.getBean( "photoService" );
        _sectorService = (ISectorService) SpringContextService.getBean( "unittree-dansmarue.sectorService" );
        _arrondissementService = (IArrondissementService) SpringContextService.getBean( "signalement.arrondissementService" );
        _conseilQuartier = (IConseilQuartierService) SpringContextService.getBean( "signalement.conseilQuartierService" );
        _signalementViewRoleService = (SignalementViewRoleService) SpringContextService.getBean( "signalement.signalementViewRoleService" );
        _domaineFonctionnelService = (IDomaineFonctionnelService) SpringContextService.getBean( "domaineFonctionnelService" );
        _unitSiraService = (IUnitSiraService) SpringContextService.getBean( "unittree-dansmarue.unitSiraService" );
        _signalementExportService = (ISignalementExportService) SpringContextService.getBean( "signalementExportService" );
    }

    /**
     * Get the dashboard report list.
     *
     * @return list of ids
     */
    public List<Integer> getDashboardSignalementList( )
    {
        return _dashboardSignalementList;
    }

    /**
     * Set the dashboard report list.
     *
     * @param dashboardSignalementList
     *            dashboard report list
     */
    public void setDashboardSignalementList( List<Integer> dashboardSignalementList )
    {
        _dashboardSignalementList = dashboardSignalementList;
    }

    /**
     * Get the dashboard criterias.
     *
     * @return a map of criterias
     */
    public Map<String, List<String>> getDashboardCriterias( )
    {
        return _dashboardCriterias;
    }

    /**
     * Sets the dashboard criterias.
     *
     * @param dashboardCriterias
     *            the dashboard criterias
     */
    public void setDashboardCriterias( Map<String, List<String>> dashboardCriterias )
    {
        _dashboardCriterias = dashboardCriterias;
    }

    /**
     * Get the report management page.
     *
     * @param request
     *            The HTTP request
     * @return redirection url
     */
    public String getManageSignalement( HttpServletRequest request )
    {
        // remove mass action properties
        clearMassAction( request );

        // Remove dashboard report if search performed
        String actionSearch = request.getParameter( PARAMETER_SEARCH );
        if ( null != actionSearch )
        {
            _dashboardSignalementList = null;
        }

        if ( request.getParameter( PARAMETER_IS_REINIT_SEARCH ) != null )
        {
            Boolean isReinitSearch = StringUtils.equals( "1", request.getParameter( PARAMETER_IS_REINIT_SEARCH ) );
            if ( isReinitSearch )
            {
                _signalementFilter = null;
            }
        }

        SignalementFilter filter = getSignalementFilter( request );
        AdminUser adminUser = AdminUserService.getAdminUser( request );
        applyViewRoleRestrictionToFilter( request, adminUser );

        ///////////////////////////////////////////
        //// THE DIVISION LIST ALLOWED BY USER ////
        ///////////////////////////////////////////
        List<Unit> listUnits = _unitService.getUnitsByIdUser( adminUser.getUserId( ), false );

        ReferenceList listeDirections = new ReferenceList( );
        List<Sector> listSectorsOfUnits = new ArrayList<>( );
        ReferenceList listeArrondissement = new ReferenceList( );

        boolean bIsUserRestrictedByViewRoles = _signalementViewRoleService.checkIsUserRestrictedByViewRoles( adminUser.getUserId( ), request );

        for ( Unit userUnit : listUnits )
        {
            ReferenceList newDirections;
            if ( userUnit.getIdParent( ) != -1 )
            {
                // rights on a specific unit
                List<Unit> direction = new ArrayList<>( );
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

                newDirections = _listUtils.toReferenceList( direction, ID_UNIT, LABEL, null );
            }
            else
            {
                // the direction list
                newDirections = _listUtils.toReferenceList( _unitService.getUnitsFirstLevel( false ), ID_UNIT, LABEL, null );
            }

            if ( ( newDirections != null ) && ( !newDirections.isEmpty( ) ) )
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

            if ( CollectionUtils.isEmpty( filter.getListIdDirection( ) ) )
            {
                for ( Sector secteurUnit : _sectorService.loadByIdUnitWithoutChosenId( Collections.singletonList(userUnit.getIdUnit()), ID_JARDIN ) )
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
        }

        List<Arrondissement> listArrond = _cachesService.getAllArrondissement( );
        listeArrondissement.addAll( _listUtils.toReferenceList( listArrond, "id", NUMBER, null ) );

        if ( filter.getListIdArrondissements( ) == null )
        {
            List<Long> listIdLongArrond = listArrond.stream( ).map( Arrondissement::getId ).collect( Collectors.toList( ) );
            filter.setListIdArrondissements( listIdLongArrond.stream( ).map( Long::intValue ).collect( Collectors.toList( ) ) );
        }

        // Fill the map
        Map<String, Object> model = new HashMap<>( );

        model.put( MARK_ARRONDISSEMENT_LIST, listeArrondissement );

        List<ConseilQuartier> listeConseilQuartier = _cachesService.selectQuartiersList( );
        model.put( MARK_CONSEIL_QUARTIER_LIST, listeConseilQuartier );

        ReferenceList listVuePhotos = choiceTypePhoto( );
        model.put( MARK_VUE_PHOTO_LIST, listVuePhotos );

        ReferenceItem refItem = new ReferenceItem( );
        refItem.setCode( Long.toString( -1 ) );
        refItem.setName( StringUtils.EMPTY );
        listeDirections.add( 0, refItem );
        _signalementUtils.changeUnitDEVEIntoSEJ( listeDirections );
        model.put( MARK_DIRECTION_LIST, listeDirections );

        if ( CollectionUtils.isNotEmpty( filter.getListIdDirection( ) ) )
        {
            // Specificity for DEVE entity, change the id from SEJ to DEVE
            if ( filter.getListIdDirection( ).contains( SEJ_ID ) )
            {
                filter.getListIdDirection( ).removeIf( idDirection -> idDirection.equals( SEJ_ID ) );
                filter.getListIdDirection( ).add( 1 );
            }
            listSectorsOfUnits = _sectorService.loadByIdUnitWithoutChosenId( filter.getListIdDirection( ), ID_JARDIN );
        }

        ReferenceList listeSecteur = _listUtils.toReferenceList( listSectorsOfUnits, ID_SECTOR, "name", StringUtils.EMPTY, true );
        model.put( MARK_SECTEUR_LIST, listeSecteur );

        // the TypeSignalement list
        // get all the report type without children
        List<TypeSignalement> types = _typeSignalementService.getAllTypeSignalementActif( );

        if ( bIsUserRestrictedByViewRoles && ( filter.getListIdTypeSignalements( ) != null ) && ( !filter.getListIdTypeSignalements( ).isEmpty( ) ) )
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

        // the state list
        List<State> listeEtat = getListeEtats( );
        model.put( MARK_ETATS_LIST, listeEtat );

        Integer totalResult = 0;

        List<Signalement> listSignalements = null;

        if ( _dashboardSignalementList != null )
        {
            totalResult = _dashboardSignalementList.size( );
            PaginationProperties paginationProperties = getPaginationProperties( request, totalResult );

            List<Signalement> dashboardSignalements = _signalementService.getByIds( _dashboardSignalementList,
                    paginationProperties.getItemsPerPage( ) * ( paginationProperties.getPageIndex( ) - 1 ),
                    paginationProperties.getItemsPerPage( ) * paginationProperties.getPageIndex( ) );

            listSignalements = dashboardSignalements;
        }
        else
        {
            totalResult = _signalementExportService.countSearchResult( filter );
            listSignalements = _signalementExportService.findByFilterSearch( filter, getPaginationProperties( request, totalResult ) );

        }

        ResultList<Signalement> resultListSignalement = new ResultList<>( );

        resultListSignalement.addAll( listSignalements );

        resultListSignalement.setTotalResult( listSignalements.size( ) );
        LocalizedDelegatePaginator<Signalement> paginator = this.getPaginator( request, resultListSignalement, URL_JSP_MANAGE_SIGNALEMENTS, totalResult );

        _anomaliesCount = totalResult;

        // the paginator
        model.put( SignalementConstants.MARK_NB_ITEMS_PER_PAGE, StringUtils.EMPTY + _nItemsPerPage );
        model.put( SignalementConstants.MARK_PAGINATOR, paginator );

        // workflow : recovery of states and possible actions for page reports
        Map<String, List<Action>> mapActions = new HashMap<>( );
        Map<String, String> mapStates = new HashMap<>( );
        // Map pour afficher ou non la date de service programmée
        Map<String, Boolean> mapDisplayProgrammingDate = new HashMap<>( );
        WorkflowService workflowService = WorkflowService.getInstance( );
        Integer signalementWorkflowId = _signalementWorkflowService.getSignalementWorkflowId( );
        boolean hasSignalementPrestataire = false;
        if ( workflowService.isAvailable( ) )
        {
            for ( Signalement signalement : paginator.getPageItems( ) )
            {
                // state
                State state = null;
                if ( _dashboardSignalementList != null )
                {
                    state = workflowService.getState( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, signalementWorkflowId, null );
                }
                else
                {
                    state = new State( );
                    state.setId( signalement.getIdState( ) );
                    state.setName( signalement.getStateName( ) );
                }

                // workflow actions
                Collection<Action> listActions = null;

                listActions = _signalementService.getListActionsBySignalementAndUser( signalement, signalementWorkflowId, getUser( ), state );

                mapActions.put( signalement.getId( ).toString( ), new ArrayList<Action>( listActions ) );

                mapStates.put( signalement.getId( ).toString( ), state == null ? "Non défini" : state.getName( ) );

                if ( ( state != null )
                        && ( ( state.getId( ) == AppPropertiesService.getPropertyInt( ID_STATE_TRANSFERE_PRESTATAIRE, -1 ) )
                                || ( state.getId( ) == AppPropertiesService.getPropertyInt( ID_STATE_SERVICE_PROGRAMME_PRESTATAIRE, -1 ) ) )
                        && signalement.getIsSendWS( ) )
                {
                    hasSignalementPrestataire = true;
                }

                boolean displayProgrammingDate = ( state != null ) && ( ( state.getId( ) == SignalementConstants.ID_STATE_PROGRAMME_PRESTATAIRE )
                        || ( state.getId( ) == SignalementConstants.ID_STATE_PROGRAMME ) );
                mapDisplayProgrammingDate.put( signalement.getId( ).toString( ), displayProgrammingDate );
            }
        }

        model.put( MARK_HAS_SIGNALEMENT_PRESTATAIRE, hasSignalementPrestataire );

        // Display of advanced options again
        List<Integer> listArrondissementIds = listeArrondissement.stream( ).map( referenceItem -> Integer.valueOf( referenceItem.getCode( ) ) )
                .collect( Collectors.toList( ) );
        boolean hasCriteresAvances = hasCriteresAvances( filter, listArrondissementIds );

        model.put( MARK_HAS_ADVANCED_CRITERIAS, hasCriteresAvances );

        model.put( MARK_SIGNALEMENT_LIST, paginator.getPageItems( ) );
        model.put( MARK_ETATS, mapStates );
        model.put( MARK_PROGRAMMING_DATE, mapDisplayProgrammingDate );
        model.put( MARK_ACTIONS, mapActions );

        model.put( MARK_TITLE, I18nService.getLocalizedString( PAGE_TITLE_MANAGE_SIGNALEMENT, getLocale( ) ) );

        // the filter
        if ( CollectionUtils.isNotEmpty(filter.getListIdDirection()) && filter.getListIdDirection( ).contains( 1 ) )
        {
            filter.getListIdDirection( ).removeIf( idDirection -> idDirection.equals( 1 ) );
            filter.getListIdDirection( ).add( SEJ_ID );
        }

        model.put( SignalementConstants.MARK_LOCALE, request.getLocale( ) );

        model.put( MARK_ID_ETATS_DEFAULT, ID_ETATS_DEFAULT );
        model.put( MARK_MAP_MAX_RESULTS, AppPropertiesService.getPropertyInt( PROPERTY_MAP_MAX_RESULTS, 0 ) );

        if ( CollectionUtils.isNotEmpty( _dashboardSignalementList ) )
        {
            model.put( MARK_DASHBOARD_CRITERIAS, _dashboardCriterias );
            filter.setIdTypeSignalement( 0 );
            filter.setNumero( null );
            filter.setListIdDirection(Collections.singletonList(0));
            filter.setIdDomaine( 0 );
            filter.setIdSector( 0 );
        }

        model.put( SignalementConstants.MARK_FILTER, filter );

        model.put( MARK_FEUILLE_DE_TOURNEE_LIST, _feuilleTourneeService.getAllFeuilleDeTourneeAutorise( adminUser ) );

        return getAdminPage( getTemplate( TEMPLATE_MANAGE_SIGNALEMENT, model ) );

    }

    private ReferenceList choiceTypePhoto( )
    {

        ReferenceList listTypePhoto = new ReferenceList( );
        ReferenceItem refItemTypePhoto1 = new ReferenceItem( );
        refItemTypePhoto1.setCode( String.valueOf( 1 ) );
        refItemTypePhoto1.setName( SignalementConstants.PHOTO_INITIALE_NOM );
        listTypePhoto.add( refItemTypePhoto1 );
        ReferenceItem refItemTypePhoto2 = new ReferenceItem( );
        refItemTypePhoto2.setCode( String.valueOf( 2 ) );
        refItemTypePhoto2.setName( SignalementConstants.PHOTO_SERVICE_FAIT_NOM );
        listTypePhoto.add( refItemTypePhoto2 );

        return listTypePhoto;
    }

    /**
     * Get the report history page.
     *
     * @param request
     *            The HTTP request
     * @return redirection url
     */
    public String getViewHistory( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );
        Signalement signalement;
        List<PhotoDMR> photos;
        Signaleur signaleur = null;
        Adresse adresse = null;

        String strSignalementId = request.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );
        Integer nIdSignalement = Integer.parseInt( strSignalementId );
        String strFromPage = request.getParameter( PARAMETER_FROM_PAGE );
        if ( StringUtils.equals( strFromPage, PARAMETER_VALUE_DISPLAY_PAGE ) )
        {
            model.put( MARK_BACK_URL, URL_JSP_DISPLAY_SIGNALEMENTS );
        }
        else
        {
            model.put( MARK_BACK_URL, URL_JSP_MANAGE_SIGNALEMENTS );
        }

        signalement = _signalementService.getSignalement( nIdSignalement );
        photos = _photoService.findBySignalementId( nIdSignalement );
        adresse = _adresseService.loadByIdSignalement( nIdSignalement );
        signaleur = _signaleurService.loadByIdSignalement( nIdSignalement );

        List<Unit> listUnitsSector = _unitSiraService.findBySectorId( signalement.getSecteur( ).getIdSector( ) );
        Unit unitPrincipaleSector;

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
        model.put( PARAMETER_MARK_ADRESSE, adresse );
        model.put( MARK_SIGNALEUR, signaleur );

        // report's history html
        WorkflowService workflowService = WorkflowService.getInstance( );
        String historiqueList = workflowService.getDisplayDocumentHistory( nIdSignalement, Signalement.WORKFLOW_RESOURCE_TYPE,
                _signalementWorkflowService.getSignalementWorkflowId( ), request, getLocale( ) );

        model.put( MARK_HISTORIQUE_LIST, historiqueList );

        // get the report's state
        State stateSignalement = workflowService.getState( nIdSignalement, Signalement.WORKFLOW_RESOURCE_TYPE,
                _signalementWorkflowService.getSignalementWorkflowId( ), null );
        String serviceFaitValue = AppPropertiesService.getProperty( SignalementConstants.PROPERTY_SERVICE_FAIT_VALUE );

        model.put( MARK_STATE_SIGNALEMENT, stateSignalement );
        model.put( MARK_STATE_SERVICE_FAIT, Integer.parseInt( serviceFaitValue ) );

        String userAccessCode = "";
        if ( stateSignalement.getId( ) == Integer.parseInt( serviceFaitValue ) )
        {
            userAccessCode = _signalementWorkflowService.selectUserServiceFait( signalement.getId( ).intValue( ) );
        }

        model.put( MARK_USER_SERVICE_FAIT, _signalementService.findUserServiceFait( userAccessCode, signaleur ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SIGNALEMENT_HISTORY, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Gets the report filter.
     *
     * @param request
     *            the request
     * @return the report filter
     */
    private SignalementFilter getSignalementFilter( HttpServletRequest request )
    {
        List<EtatSignalement> etats = new ArrayList<>( );
        if ( _signalementFilter == null )
        {
            initFilterValue( request, etats );

        }
        else
        {
            // SORT
            String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );

            if ( _dashboardSignalementList != null )
            {
                etats = new ArrayList<>( );
                _signalementFilter.setEtats( etats );
            }

            if ( strSortedAttributeName != null )
            {
                // during a ASC/DESC sorting and action
                _signalementFilter.setOrders( new ArrayList<Order>( ) );

                String strAscSort = request.getParameter( Parameters.SORTED_ASC );
                boolean bIsAscSort = Boolean.parseBoolean( strAscSort );
                _signalementFilter.setOrderAsc( bIsAscSort );
                Order order = new Order( strSortedAttributeName, bIsAscSort );

                _signalementFilter.getOrders( ).add( order );
            }
            else
                if ( ( request.getParameter( SignalementConstants.PARAMETER_BUTTON_SEARCH ) != null )
                        || StringUtils.isNotBlank( request.getParameter( BUTTON_UPDATE_SECTOR_WITH_DIRECTION ) ) )
                {

                    etats = new ArrayList<>( );
                    buildFilter( _signalementFilter, request );
                    for ( State state : (List<State>) WorkflowService.getInstance( ).getAllStateByWorkflow( SignalementConstants.SIGNALEMENT_WORKFLOW_ID,
                            getUser( ) ) )
                    {
                        String strValue = request.getParameter( "etat" + state.getId( ) );
                        boolean bTypeDossChecked = Boolean.parseBoolean( strValue );
                        if ( bTypeDossChecked )
                        {
                            EtatSignalement etatSignalement = new EtatSignalement( );
                            etatSignalement.setCoche( bTypeDossChecked );
                            etatSignalement.setId( (long) state.getId( ) );
                            etatSignalement.setLibelle( state.getName( ) );
                            etats.add( etatSignalement );

                        }
                    }

                    EtatSignalement etatSignalementState = _signalementService.getStateIfDateIndicated( _signalementFilter );
                    if ( etatSignalementState != null )
                    {
                        etats.add( etatSignalementState );
                    }

                    _signalementFilter.setEtats( etats );

                }

        }

        return _signalementFilter;

    }

    /**
     * Init Filter for first search.
     *
     * @param request
     *            the request
     * @param etats
     *            default state list
     */
    private void initFilterValue( HttpServletRequest request, List<EtatSignalement> etats )
    {
        // passes at the first launch
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

        if ( _dashboardSignalementList == null )
        {
            manageEtatList( etats );
        }
        else
        {
            _signalementFilter.setEtats( etats );
        }

        int nbdays = AppPropertiesService.getPropertyInt( PROPERTY_SEARCH_INITIAL_NB_DAYS, -1 );
        if ( nbdays > 0 )
        {
            _signalementFilter.setDateBegin( LocalDate.now( ).minusDays( nbdays ).format( DateTimeFormatter.ofPattern( DateConstants.DATE_FR ) ) );
        }

        // Date de fin à la date actuelle
        _signalementFilter.setDateEnd( LocalDate.now( ).format( DateTimeFormatter.ofPattern( DateConstants.DATE_FR ) ) );
        _signalementFilter.setOnglet( "liste" );
        _signalementFilter.setPhotoDoneOnly( false );

        // Photo Initial par defaut
        _signalementFilter.setIdVuePhoto( 1 );
    }

    /**
     * Manage the states list.
     *
     * @param etats
     *            the states list
     */
    public void manageEtatList( List<EtatSignalement> etats )
    {
        for ( State state : (List<State>) WorkflowService.getInstance( ).getAllStateByWorkflow( SignalementConstants.SIGNALEMENT_WORKFLOW_ID, getUser( ) ) )
        {
            /*
             * initialization of default values a New, To be processed, To be done in the field and To be done Office
             */
            if ( ( state != null ) && ( ( state.getId( ) == SignalementConstants.ID_STATE_NOUVEAU )
                    || ( state.getId( ) == SignalementConstants.ID_STATE_A_TRAITER ) || ( state.getId( ) == SignalementConstants.ID_STATE_A_FAIRE_TERRAIN )
                    || ( state.getId( ) == SignalementConstants.ID_STATE_A_FAIRE_BUREAU )
                    || ( state.getId( ) == SignalementConstants.ID_STATE_TRANSFERE_PRESTATAIRE )
                    || ( state.getId( ) == SignalementConstants.ID_STATE_PROGRAMME_PRESTATAIRE ) ) )
            {

                EtatSignalement etatSignalement = new EtatSignalement( );

                etatSignalement.setCoche( true );
                etatSignalement.setId( (long) state.getId( ) );
                etatSignalement.setLibelle( state.getName( ) );
                etats.add( etatSignalement );
            }
        }
        _signalementFilter.setEtats( etats );
    }

    /**
     * Apply a restriction by role.
     *
     * @param request
     *            HttpRequest
     * @param adminUser
     *            the user
     */
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

        List<Integer> listSectorsOfUnit = new ArrayList<>( );
        List<Integer> listOfUnit = new ArrayList<>( );
        for ( Unit userUnit : listUnits )
        {
            listOfUnit.add( userUnit.getIdUnit( ) );
            listSectorsOfUnit.addAll( _sectorService.getIdsSectorByIdUnit( userUnit.getIdUnit( ) ) );
        }

        _signalementFilter.setListIdUnit( listOfUnit );
        _signalementFilter.setListIdSecteurAutorises( listSectorsOfUnit );
    }

    /**
     * This method return true if a resource does not have the state to be processed, New or Service Programmer.
     *
     * @param idRessource
     *            the resource id
     * @return true if the report is not in one of the 3 states above
     */
    public String isMassServiceFait( int [ ] idRessource )
    {

        // if the array is empty or null
        if ( ( idRessource == null ) || ( idRessource.length == 0 ) )
        {
            return "";
        }

        int index = 0;

        StringBuilder trouver = new StringBuilder( "" );

        /**
         * we go through the list of ids looking for resources that don't have the right state.
         */
        while ( index < idRessource.length )
        {

            // the state of the resource is recovered
            State etat = WorkflowService.getInstance( ).getState( idRessource [index], Signalement.WORKFLOW_RESOURCE_TYPE,
                    _signalementWorkflowService.getSignalementWorkflowId( ), null );

            // if the state is null
            if ( etat == null )
            {
                trouver.append( setNumeroSignalement( idRessource [index] ) );
                break;
            }

            // we get the state id
            int etats = etat.getId( );

            // if state id is the same as one of the states we are not looking for
            if ( ( etats != SignalementConstants.ID_STATE_NOUVEAU ) && ( etats != SignalementConstants.ID_STATE_A_TRAITER )
                    && ( etats != SignalementConstants.ID_STATE_PROGRAMME ) )
            {

                trouver.append( setNumeroSignalement( idRessource [index] ) );
                break;
            }

            // we increase the index
            index++;

        }

        return trouver.toString( ).trim( );

    }

    /**
     * Sets the numero signalement.
     *
     * @param nIdSignalement
     *            the n id signalement
     * @return the string
     */
    /*
     * This method returns the report number
     *
     * @param nIdSignalement the report id
     *
     * @return the rpeort number
     */
    public String setNumeroSignalement( int nIdSignalement )
    {

        // we declare the variables
        StringBuilder sbNumerosSignalement = new StringBuilder( );
        Signalement signalement;

        // we retrieve the report.
        signalement = _signalementService.getSignalement( nIdSignalement );

        // if the report does not exist
        if ( signalement == null )
        {
            return sbNumerosSignalement.append( LINE_BREAK ).toString( );
        }

        /*
         * reconstruct the number
         */
        sbNumerosSignalement.append( signalement.getPrefix( ) );
        sbNumerosSignalement.append( signalement.getAnnee( ) );
        sbNumerosSignalement.append( signalement.getMois( ) );
        sbNumerosSignalement.append( signalement.getNumero( ) );

        // return the number complete with a line break
        return sbNumerosSignalement.append( LINE_BREAK ).toString( );

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
        // Districts recovery (this is a list)
        String [ ] strArrondissements = request.getParameterValues( "listIdArrondissementsParam" );
        String [ ] strQuartiers = request.getParameterValues( "listIdQuartierParam" );
        String [ ] strDirections = request.getParameterValues( "listIdDirectionParam" );

        if ( !ArrayUtils.isEmpty( strArrondissements ) )
        {
            List<Integer> arrondissementIds = _listUtils.getListOfIntFromStrArray( strArrondissements );
            filter.setListIdArrondissements( arrondissementIds );
        }
        else
        {
            filter.setListIdArrondissements( null );
        }

        if ( !ArrayUtils.isEmpty( strQuartiers ) )
        {
            List<Integer> quartiersIds = _listUtils.getListOfIntFromStrArray( strQuartiers );
            filter.setListIdQuartier( quartiersIds );
        }
        else
        {
            filter.setListIdQuartier( null );
        }

        if ( !ArrayUtils.isEmpty( strDirections ) )
        {
            List<Integer> directionIds = _listUtils.getListOfIntFromStrArray( strDirections );
            filter.setListIdDirection( directionIds );
        }
        else
        {
            filter.setListIdDirection( null );
        }
        filter.getListIdDirection();

        populate( filter, request );
    }

    /**
     * Get the report creation form.
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
        Map<String, Object> model = new HashMap<>( );

        model.put( MARK_KEY_MAPS, SignalementConstants.GOOGLE_MAPS_API_KEY );
        model.put( SignalementConstants.MARK_LOCALE, request.getLocale( ) );

        // ERROR HANDLING
        FunctionnalException fe = getErrorOnce( request );
        if ( fe != null )
        {
            signalement = (Signalement) fe.getBean( );
            model.put( "error", getHtmlError( fe ) );

            if ( fe.getAdditionalParameters( ) != null )
            {
                model.putAll( fe.getAdditionalParameters( ) );
            }

            if ( getHtmlError( fe ).length( ) == MESSAGE_ERROR_EMPTY )
            {
                model.put( "noErrorToDisplay", "noErrorToDisplay" );
            }

            String strSignalementId = request.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );
            if ( strSignalementId == null )
            {

                // get all the priorities
                List<Priorite> priorites = _prioriteService.getAllPriorite( );
                ReferenceList listePriorite = _listUtils.toReferenceList( priorites, "id", LIBELLE, "" );
                model.put( MARK_PRIORITE_LIST, listePriorite );

                // get all the report type
                List<TypeSignalement> types = _typeSignalementService.getAllTypeSignalementActifLinkedToUnit( );
                ReferenceList listeTypes = _listUtils.toReferenceList( types, "id", "formatTypeSignalement", "", false );
                model.put( MARK_TYPE_LIST, listeTypes );

            }

        }
        else
        {
            // UPDATE
            String strSignalementId = request.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );
            if ( strSignalementId != null )
            {
                List<PhotoDMR> photos;
                Signaleur signaleur = null;
                Adresse adresse = null;
                TypeSignalement typeSignalement = null;

                int nIdSignalement = Integer.parseInt( strSignalementId );
                long lIdSignalement = Long.parseLong( strSignalementId );

                // get the report infos
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
                model.put( PARAMETER_MARK_ADRESSE, adresse );
                model.put( MARK_SIGNALEUR, signaleur );

                // get the report type
                typeSignalement = _typeSignalementService.getTypeSignalement( signalement.getTypeSignalement( ).getId( ) );

                model.put( MARK_TYPE, typeSignalement );

                // get all the priorities
                List<Priorite> priorites = _prioriteService.getAllPriorite( );
                ReferenceList listePriorite = _listUtils.toReferenceList( priorites, "id", LIBELLE, "", true );
                model.put( MARK_PRIORITE_LIST, listePriorite );

                // get the report's state
                WorkflowService workflowService = WorkflowService.getInstance( );
                State stateSignalement = workflowService.getState( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE,
                        _signalementWorkflowService.getSignalementWorkflowId( ), null );

                model.put( MARK_STATE_SIGNALEMENT, stateSignalement );
            }
            else
            {
                // CREATION

                signalement = new Signalement( );

                // get all the priorities
                List<Priorite> priorites = _prioriteService.getAllPriorite( );
                ReferenceList listePriorite = _listUtils.toReferenceList( priorites, "id", LIBELLE, "" );
                model.put( MARK_PRIORITE_LIST, listePriorite );

                // get all the report types
                List<TypeSignalement> types = _typeSignalementService.getAllTypeSignalementActifLinkedToUnit( );
                ReferenceList listeTypes = _listUtils.toReferenceList( types, "id", "formatTypeSignalement", "", false );
                model.put( MARK_TYPE_LIST, listeTypes );

                model.put( SignalementConstants.MARK_LOCALE, request.getLocale( ) );
                String strMessageCreation;

                // get the default message creation (linked to the workflow)
                if ( _signalementUtils.isWindows( ) )
                {
                    strMessageCreation = _signalementService.getMessageCreationSignalement( ).replaceAll( "</p>|<br/>|<br>|<br />", "\r" )
                            .replaceAll( "<[^>]*>", "" );

                }
                else
                {
                    strMessageCreation = _signalementService.getMessageCreationSignalement( )
                            .replaceAll( "<br/>|<br>|<br />", System.getProperty( "line.separator" ) ).replaceAll( "<[^>]*>", "" );

                }

                model.put( MARK_MESSAGE_FOR_USER, strMessageCreation );

            }
        }

        model.put( MARK_SIGNALEMENT, signalement );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SAVE_SIGNALEMENT, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );

    }

    /**
     * Returns the form to view a report.
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
        }
        else
        {
            model.put( MARK_BACK_URL, URL_JSP_MANAGE_SIGNALEMENTS );
        }

        String strSignalementId = request.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );
        if ( strSignalementId != null )
        {

            Long lIdSignalement = Long.parseLong( strSignalementId );
            signalement = _signalementService.getSignalement( lIdSignalement );
            photos = _photoService.findBySignalementId( lIdSignalement );
            adresse = _adresseService.loadByIdSignalement( lIdSignalement );
            signaleur = _signaleurService.loadByIdSignalement( lIdSignalement );

            Sector sector = signalement.getSecteur( );
            List<Unit> listUnitsSector = _unitSiraService.findBySectorId( sector.getIdSector( ) );
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

            // get the report's state
            WorkflowService workflowService = WorkflowService.getInstance( );
            State stateSignalement = workflowService.getState( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE,
                    _signalementWorkflowService.getSignalementWorkflowId( ), null );

            // Get the report possible actions (based on user)
            Integer signalementWorkflowId = _signalementWorkflowService.getSignalementWorkflowId( );
            Collection<Action> possibleActions = _signalementService.getListActionsByIdSignalementAndUser( lIdSignalement.intValue( ), signalementWorkflowId,
                    getUser( ) );

            model.put( MARK_POSSIBLE_ACTIONS, possibleActions );
            model.put( MARK_STATE_SIGNALEMENT, stateSignalement );
        }

        model.put( SignalementConstants.MARK_JSP_BACK, JSP_MANAGE_SIGNALEMENT );
        model.put( MARK_SIGNALEMENT, signalement );
        model.put( MARK_PHOTOS, photos );
        model.put( PARAMETER_MARK_ADRESSE, adresse );
        model.put( MARK_SIGNALEUR, signaleur );

        model.put( "locale", Locale.FRANCE );

        String template = getTemplate( TEMPLATE_VIEW_SIGNALEMENT, model );

        return getAdminPage( template );
    }

    /**
     * Save/modify a report.
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

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String strIdSignalement = multipartRequest.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );
        Signalement signalement = new Signalement( );

        // if the user wants to add a new photo to the report
        if ( null != request.getParameter( "addPhotoModif" ) )
        {
            UrlItem urlItem = new UrlItem( JSP_ADD_PHOTO_TO_SIGNALEMENT );

            urlItem.addParameter( PARAMETER_MARK_SIGNALEMENT_ID, strIdSignalement );

            return urlItem.getUrl( );
        }

        // UPDATE
        if ( !strIdSignalement.equals( EMPTY_STRING ) )
        {
            long lIdSignalement = 0;
            signalement = new Signalement( );

            try
            {
                lIdSignalement = Long.parseLong( strIdSignalement );
            }
            catch( NumberFormatException e )
            {
                throw new BusinessException( signalement, SignalementConstants.MESSAGE_ERROR_OCCUR );
            }

            // get the report by his id
            signalement = _signalementService.getSignalement( lIdSignalement );

            // get the address of the report by his id
            List<Adresse> adresses = new ArrayList<>( );
            Adresse adresseSignalement;

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

        urlItem.addParameter( PARAMETER_MARK_SIGNALEMENT_ID, Long.toString( signalement.getId( ) ) );

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
        // delete the last entry
        request.getSession( ).setAttribute( SignalementConstants.ATTRIBUTE_SESSION_DERNIERE_SAISIE_ACTION, null );
        _massSignalementIds = null;
    }

    /**
     * Add a photo to a report (only in modification mode).
     *
     * @param request
     *            the request
     * @return The next url
     */
    public String getSavePhotoToSignalement( HttpServletRequest request )
    {

        Map<String, Object> model = new HashMap<>( );

        String strSignalementId = request.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );

        model.put( PARAMETER_MARK_SIGNALEMENT_ID, strSignalementId );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_SAVE_PHOTO_TO_SIGNALEMENT, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );

    }

    /**
     * Delete a photo linked to a report.
     *
     * @param request
     *            the request
     * @return The next url
     */
    public String getDeletePhotoSignalement( HttpServletRequest request )
    {

        String strJspBack = JSP_MODIFY_SIGNALEMENT;

        String strPhotoId = request.getParameter( PARAMETER_PHOTO_ID );
        String strSignalementId = request.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );

        Integer nIdPhoto = 0;
        Integer nIdSignalement = 0;

        nIdPhoto = Integer.parseInt( strPhotoId );
        nIdSignalement = Integer.parseInt( strSignalementId );

        Map<String, Object> urlParam = new HashMap<>( );
        urlParam.put( PARAMETER_PHOTO_ID, nIdPhoto );
        urlParam.put( PARAMETER_MARK_SIGNALEMENT_ID, nIdSignalement );

        return AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_DELETE_PHOTO_SIGNALEMENT, null, MESSAGE_CONFIRMATION_DELETE_PHOTO_SIGNALEMENT,
                JSP_DELETE_PHOTO_SIGNALEMENT, SELF, AdminMessage.TYPE_CONFIRMATION, urlParam, strJspBack );

    }

    /**
     * Delete a photo linked to a report.
     *
     * @param request
     *            the request
     * @return The next url
     */
    public String doDeletePhotoSignalement( HttpServletRequest request )
    {
        String strPhotoId = request.getParameter( PARAMETER_PHOTO_ID );
        String strSignalementId = request.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );

        int nIdPhoto = 0;

        nIdPhoto = Integer.parseInt( strPhotoId );

        _photoService.remove( nIdPhoto );

        UrlItem urlItem = new UrlItem( JSP_MODIFY_SIGNALEMENT );
        urlItem.addParameter( PARAMETER_MARK_SIGNALEMENT_ID, strSignalementId );
        return urlItem.getUrl( );

    }

    /**
     * Add a photo to a report (only in modification mode).
     *
     * @param request
     *            the request
     * @return The next url
     */
    public String doSavePhotoToSignalement( HttpServletRequest request )
    {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String strIdSignalement = multipartRequest.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );
        String strVuePhoto = multipartRequest.getParameter( PARAMETER_VUE_PHOTO );
        Signalement signalement = new Signalement( );

        Long nIdSignalement = Long.parseLong( strIdSignalement );
        signalement.setId( nIdSignalement );

        if ( StringUtils.isNotBlank( request.getParameter( "cancelAddPhoto" ) ) )
        {
            UrlItem urlItem = new UrlItem( JSP_MODIFY_SIGNALEMENT );

            urlItem.addParameter( PARAMETER_MARK_SIGNALEMENT_ID, strIdSignalement );

            return urlItem.getUrl( );
        }

        List<PhotoDMR> photos = _signalementService.getSignalement( nIdSignalement ).getPhotos( );
        Integer nVuePhoto = Integer.parseInt( strVuePhoto );

        for ( PhotoDMR photo : photos )
        {
            // If a photo already exists
            if ( photo.getVue( ).equals( nVuePhoto ) )
            {
                return AdminMessageService.getMessageUrl( multipartRequest, MESSAGE_ERROR_EXISTING_PHOTO, AdminMessage.TYPE_STOP );
            }
        }

        FileItem imageSource = multipartRequest.getFile( PARAMETER_PHOTO );
        String strImageName = FileUploadService.getFileNameOnly( imageSource );

        byte [ ] baImageSource = imageSource.get( );
        ImageResource image = new ImageResource( );

        if ( ( strImageName != null ) && !strImageName.equals( EMPTY_STRING ) )
        {
            String width = AppPropertiesService.getProperty( SignalementConstants.IMAGE_THUMBNAIL_RESIZE_WIDTH );
            String height = AppPropertiesService.getProperty( SignalementConstants.IMAGE_THUMBNAIL_RESIZE_HEIGHT );
            byte [ ] resizeImage = ImageUtil.resizeImage( baImageSource, width, height, 1 );
            PhotoDMR photoSignalement = new PhotoDMR( );
            image.setImage( baImageSource );
            image.setMimeType( imageSource.getContentType( ) );
            photoSignalement.setImage( image );
            photoSignalement.setImageThumbnailWithBytes( resizeImage );
            photoSignalement.setSignalement( signalement );

            photoSignalement.setVue( nVuePhoto );

            photoSignalement.setDate( _simple_date_format.format( Calendar.getInstance( ).getTime( ) ) );

            // creation of the image in the db linked to the report
            _photoService.insert( photoSignalement );

        }
        else
        {
            return AdminMessageService.getMessageUrl( multipartRequest, MESSAGE_ERROR_EMPTY_PHOTO_FIELD, AdminMessage.TYPE_STOP );
        }

        UrlItem urlItem = new UrlItem( JSP_MODIFY_SIGNALEMENT );

        urlItem.addParameter( PARAMETER_MARK_SIGNALEMENT_ID, strIdSignalement );

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

        return StringUtils.isNotBlank( strJspBack ) ? ( AppPathService.getBaseUrl( request ) + strJspBack )
                : ( AppPathService.getBaseUrl( request ) + JSP_MANAGE_SIGNALEMENT );
    }

    /**
     * Get the request data and if there is no error insert the data in the report object specified in parameter. return null if there is no error or else
     * return the error page url
     *
     * @param multipartRequest
     *            the request
     * @param signalement
     *            the report
     * @return null if there is no error or else return the error page url
     */
    public String getSignalementDataAndSave( MultipartHttpServletRequest multipartRequest, Signalement signalement )
    {
        // Warning, before edit this method, another method can save report
        // SignalementRestService.saveIncident (in module-signalement-rest)
        try
        {
            // UPDATE
            String strIdSignaleur = multipartRequest.getParameter( PARAMETER_SIGNALEUR_ID );
            String strIdTelephone = multipartRequest.getParameter( PARAMETER_TELEPHONE_ID );
            String strIdSignalement = multipartRequest.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );
            String strCommentaireProg = multipartRequest.getParameter( PARAMETER_COMMENTAIRE_PROG );
            String strdatePrevuTraitement = multipartRequest.getParameter( PARAMETER_DATE_PREVU_TRAITEMENT );

            // BOTH
            String strPriorite = multipartRequest.getParameter( PARAMETER_PRIORITE );
            String strTypeSignalement = multipartRequest.getParameter( PARAMETER_TYPE_SIGNALEMENT );
            String strMail = multipartRequest.getParameter( PARAMETER_MAIL );
            String strCommentaire = multipartRequest.getParameter( PARAMETER_COMMENTAIRE );
            String strCommentaireAgentTerrain = multipartRequest.getParameter( PARAMETER_COMMENTAIRE_AGENT_TERRAIN );
            FileItem imageSourceEnsemble = multipartRequest.getFile( PARAMETER_PHOTO_ENSEMBLE );
            FileItem imageSourcePres = multipartRequest.getFile( PARAMETER_PHOTO_PRES );

            if ( !StringUtils.isEmpty( strMail ) && !VALID_EMAIL_ADDRESS_REGEX.matcher( strMail ).find( ) )
            {
                return AdminMessageService.getMessageUrl( multipartRequest, MESSAGE_ERREUR_INVALID_MAIL, AdminMessage.TYPE_STOP );
            }

            String strMessageCreation = multipartRequest.getParameter( "message" );

            Map<String, Object> model = new HashMap<>( );
            model.put( MARK_MESSAGE_FOR_USER, strMessageCreation );

            Adresse adresse = new Adresse( );
            setFromRequest( multipartRequest, adresse, signalement );

            signalement.getAdresses( ).add( adresse );

            // set the report object attributes
            signalement.setCommentaire( strCommentaire );
            signalement.setCommentaireAgentTerrain( strCommentaireAgentTerrain );

            // CREATION
            if ( StringUtils.isBlank( strIdSignalement ) )
            {

                String dateOfTheDay = _simple_date_format.format( Calendar.getInstance( ).getTime( ) );
                signalement.setDateCreation( dateOfTheDay );
                Date dateDay = _dateUtils.getDate( signalement.getDateCreation( ), false );
                String annee = _dateUtils.getAnnee( Calendar.getInstance( ).getTime( ) );

                signalement.setAnnee( Integer.parseInt( annee ) );
                int moisSignalement = _dateUtils.getMoisInt( dateDay );

                // associate the month with a letter (A for january, B for february...)
                signalement.setMois( _signalementService.getLetterByMonth( moisSignalement ) );

                // prefix for the report number
                signalement.setPrefix( SignalementConstants.SIGNALEMENT_PREFIX_BACKOFFICE );

                // Creation of the unique token for the report
                _signalementService.affectToken( signalement );
            }

            // get the strings in int format
            int nPriorite = 0;
            Integer nTypeSignalement = 0;

            try
            {
                nPriorite = Integer.parseInt( strPriorite );
                // add priorities to report
                signalement.setPriorite( _prioriteService.load( nPriorite ) );
            }
            catch( NumberFormatException e )
            {
                throw new BusinessException( signalement, model, SignalementConstants.MESSAGE_ERROR_PRIORITE );
            }

            if ( strIdSignalement.equals( EMPTY_STRING ) )
            {
                try
                {
                    nTypeSignalement = Integer.parseInt( strTypeSignalement );
                }
                catch( NumberFormatException e )
                {
                    throw new BusinessException( signalement, model, SignalementConstants.MESSAGE_ERROR_TYPE_SIGNALEMENT );
                }
            }

            signalement.setDatePrevueTraitement( StringUtils.defaultString( strdatePrevuTraitement ) );

            // add report type to report
            if ( StringUtils.isBlank( strIdSignalement ) )
            {
                signalement.setTypeSignalement( _typeSignalementService.getByIdTypeSignalement( nTypeSignalement ) );
            }

            // save the report
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

                }
                catch( BusinessException e )
                {
                    AppLogService.error( e.getMessage( ), e );
                    return AdminMessageService.getMessageUrl( multipartRequest, MESSAGE_ERREUR_SECTEUR_NULL, AdminMessage.TYPE_STOP );
                }

                signalement.setId( lastInsertedSignalement );

            }
            else
            {
                String urlErrors = checkUpdateMandatoryFields( multipartRequest );
                if ( StringUtils.isNotBlank( urlErrors ) )
                {
                    return urlErrors;
                }

                // modify the report
                signalement.setCommentaireProgrammation( strCommentaireProg );
                _signalementService.update( signalement );
            }

            // creation of the address
            adresse.setSignalement( signalement );
            if ( StringUtils.isBlank( strIdSignalement ) )
            {
                _adresseService.insert( adresse );
            }

            Signaleur signaleur = new Signaleur( );

            // CREATION
            if ( StringUtils.isBlank( strIdSignalement ) )
            {
                // creation of the reporter if not null
                if ( StringUtils.isNotBlank( strMail ) )
                {
                    signaleur.setMail( strMail );
                    signaleur.setIdTelephone( strIdTelephone );
                    signaleur.setSignalement( signalement );
                    _signaleurService.insert( signaleur );
                }
                else
                {
                    signaleur.setSignalement( signalement );
                    _signaleurService.insert( signaleur );
                }

                // image 1 overview treatement CREATION
                insertPhoto( signalement, imageSourceEnsemble, OVERVIEW );

                // image 2 detailed treatement CREATION
                insertPhoto( signalement, imageSourcePres, DETAILED_VIEW );
                _signalementService.initializeSignalementWorkflow( signalement );
            }
            else
            {
                // UPDATE
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
                    }
                    else
                    {
                        // CREATION
                        _signaleurService.insert( signaleur );
                    }

                }
                catch( NumberFormatException e )
                {
                    throw new BusinessException( signalement, model, SignalementConstants.MESSAGE_ERROR_OCCUR );
                }
            }
            return null;
        }
        catch( FunctionnalException e )
        {
            return manageFunctionnalException( multipartRequest, e, "SaveSignalement.jsp" );
        }
    }

    /**
     * Extract and set parameter for report address.
     *
     * @param multipartRequest
     *            the multipart request
     * @param adresse
     *            the address to set
     * @param signalement
     *            the report
     */
    private void setFromRequest( MultipartHttpServletRequest multipartRequest, Adresse adresse, Signalement signalement )
    {
        String strIdSignalement = multipartRequest.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );
        String strIdAdresse = multipartRequest.getParameter( PARAMETER_ADRESSE_ID );
        String strAdresse = multipartRequest.getParameter( PARAMETER_MARK_ADRESSE );
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
        }
        catch( NumberFormatException e )
        {
            throw new BusinessException( signalement, SignalementConstants.MESSAGE_ERROR_OCCUR );
        }

        try
        {
            if ( !StringUtils.isEmpty( strLng ) )
            {
                nLng = Double.parseDouble( strLng );
            }
        }
        catch( NumberFormatException e )
        {
            throw new BusinessException( signalement, SignalementConstants.MESSAGE_ERROR_OCCUR );
        }

        if ( StringUtils.isBlank( strIdSignalement ) )
        {
            adresse.setAdresse( strAdresse );
            adresse.setPrecisionLocalisation( strPrecisionLocalisation );
            adresse.setLat( nLat );
            adresse.setLng( nLng );
        }
        else
        {
            // modification of the address
            long lIdAdresse = 0;

            try
            {
                lIdAdresse = Long.parseLong( strIdAdresse );

            }
            catch( NumberFormatException e )
            {
                throw new BusinessException( signalement, SignalementConstants.MESSAGE_ERROR_OCCUR );
            }

            adresse = _adresseService.load( lIdAdresse );
            adresse.setPrecisionLocalisation( strPrecisionLocalisation );
        }
    }

    /**
     * Check the mandatory fields before create report.
     *
     * @param multipartRequest
     *            the request with report parameters
     * @param signalement
     *            the report to check
     * @return empty string if no errors, link to the error otherwise
     */
    public String checkMandatoryFields( MultipartHttpServletRequest multipartRequest, Signalement signalement )
    {
        String url = StringUtils.EMPTY;

        String strIdSignalement = multipartRequest.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );
        String strTypeSignalement = multipartRequest.getParameter( PARAMETER_TYPE_SIGNALEMENT );
        String strAdresse = multipartRequest.getParameter( PARAMETER_MARK_ADRESSE );
        String strPriorite = multipartRequest.getParameter( PARAMETER_PRIORITE );

        if ( ( strPriorite != null ) && MINUS_ONE.equals( strPriorite.trim( ) ) )
        {
            url = AdminMessageService.getMessageUrl( multipartRequest, MESSAGE_ERROR_EMPTY_FIELD, AdminMessage.TYPE_STOP );
        }
        else
            if ( StringUtils.isNotBlank( strIdSignalement ) )
            {
                if ( ( strTypeSignalement != null ) && ( MINUS_ONE.equals( strTypeSignalement.trim( ) ) || StringUtils.isNotBlank( strAdresse ) ) )
                {
                    url = AdminMessageService.getMessageUrl( multipartRequest, MESSAGE_ERROR_EMPTY_FIELD, AdminMessage.TYPE_STOP );
                }
                signalement.setSuivi( ZERO_VOTE );
            }

        return url;
    }

    /**
     * Check if the mandatory fields are not empty.
     *
     * @param multipartRequest
     *            multipartRequest
     * @return url
     */
    public String checkUpdateMandatoryFields( MultipartHttpServletRequest multipartRequest )
    {

        String url = StringUtils.EMPTY;

        String strPriorite = multipartRequest.getParameter( PARAMETER_PRIORITE );

        if ( ( strPriorite != null ) && MINUS_ONE.equals( strPriorite.trim( ) ) )
        {
            url = AdminMessageService.getMessageUrl( multipartRequest, MESSAGE_ERROR_EMPTY_FIELD, AdminMessage.TYPE_STOP );
        }

        return url;
    }

    /**
     * Insert the report photo.
     *
     * @param signalement
     *            the report
     * @param imageFile
     *            the image
     * @param vuePhoto
     *            the type of view (detailed or overview)
     */
    private void insertPhoto( Signalement signalement, FileItem imageFile, Integer vuePhoto )
    {
        String strImageName = FileUploadService.getFileNameOnly( imageFile );
        if ( StringUtils.isNotBlank( strImageName ) )
        {
            ImageResource image = new ImageResource( );
            String width = AppPropertiesService.getProperty( SignalementConstants.IMAGE_THUMBNAIL_RESIZE_WIDTH );
            String height = AppPropertiesService.getProperty( SignalementConstants.IMAGE_THUMBNAIL_RESIZE_HEIGHT );
            byte [ ] resizeImage = ImageUtil.resizeImage( imageFile.get( ), width, height, 1 );
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
            photoSignalement.setDate( _simple_date_format.format( Calendar.getInstance( ).getTime( ) ) );

            // creation of the image in the db linked to the report
            _photoService.insert( photoSignalement );
        }
    }

    /**
     * Check the accessibility.
     *
     * @param multipartRequest
     *            the request
     * @param signalement
     *            the report
     * @return empty if no errors, url otherwise
     */
    public String checkAccessibility( MultipartHttpServletRequest multipartRequest, Signalement signalement )
    {
        String errorUrl = StringUtils.EMPTY;
        String addressLoad = multipartRequest.getParameter( "adresseLoad" );
        String address = multipartRequest.getParameter( PARAMETER_MARK_ADRESSE );
        if ( ( multipartRequest.getParameter( "searchAddress" ) != null )
                || ( StringUtils.isNotBlank( addressLoad ) && StringUtils.isNotBlank( address ) && !address.equals( addressLoad ) ) )
        {

            signalement.getAdresses( ).get( 0 ).setLat( null );
            signalement.getAdresses( ).get( 0 ).setLng( null );
            errorUrl = manageFunctionnalException( multipartRequest, new BusinessException( signalement, null ),
                    "SaveSignalement.jsp?searchAddress=" + multipartRequest.getParameter( "searchAddress" ) );
        }
        else
            if ( multipartRequest.getParameter( "validProposedAddress" ) != null )
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
     * Find the type of action.
     *
     * @param request
     *            request
     * @return the action name
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

        if ( request.getParameter( PARAMETER_ACTION_A_TRAITER ) != null )
        {
            return ACTION_A_TRAITER;
        }

        if ( request.getParameter( PARAMETER_ACTION_A_FAIRE_BUREAU) != null )
        {
            return ACTION_A_FAIRE_BUREAU;
        }

        if ( request.getParameter( PARAMETER_ACTION_A_FAIRE_TERRAIN ) != null )
        {
            return ACTION_A_FAIRE_TERRAIN;
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
        String strIdResource = request.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );
        _massSignalementIds = _signalementUtils.getIntArray( request.getParameterValues( PARAMETER_MARK_SIGNALEMENT_ID ) );
        String strUrl;
        int nIdResource;
        Number nIdAction = 0;
        try
        {
            nIdResource = Integer.parseInt( strIdResource );
        }
        catch( NumberFormatException nfe )
        {
            return _signalementUtils.buildRedirectResult( getHomeUrl( request ) );
        }

        // "normal" actions
        ISignalementAction signalementAction = PluginActionManager.getPluginAction( request, ISignalementAction.class );
        if ( signalementAction != null )
        {
            SignalementFields signalementFields = new SignalementFields( );
            signalementFields.setSignalementsId( _massSignalementIds );
            signalementFields.setIdResource( nIdResource );
            return signalementAction.process( request, response, getUser( ), signalementFields );
        }

        // workflow's actions
        String strIdAction = request.getParameter( PARAMETER_MARK_ACTION_ID );

        if ( strIdAction == null )
        {
            // mass actions
            _actionType = findActionType( request );

            if ( StringUtils.isNotBlank( _actionType ) )
            {

                // deletion of reports on which the action cannot be carried out
                int index = 0;
                boolean suppression = false;
                Signalement signalement;
                UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_WORKFLOW_PROCESS_ACTION );
                putActionType( request, urlItem );
                String [ ] messageArgs = new String [ 1];
                StringBuilder sbNumerosSignalement = new StringBuilder( );

                for ( int nIdSignalement : _massSignalementIds )
                {

                    if ( findNextAction( nIdSignalement, _actionType ) != null )
                    {
                        if ( !WorkflowService.getInstance( ).canProcessAction( nIdSignalement, Signalement.WORKFLOW_RESOURCE_TYPE,
                                findNextAction( nIdSignalement, _actionType ).getId( ), null, request, false ) )
                        {
                            suppression = true;
                            _massSignalementIds = ArrayUtils.remove( _massSignalementIds, index );

                            // construct the report number
                            sbNumerosSignalement.append( LINE_BREAK );
                            signalement = _signalementService.getSignalement( nIdSignalement );
                            if ( signalement != null )
                            {
                                sbNumerosSignalement.append( signalement.getPrefix( ) );
                                sbNumerosSignalement.append( signalement.getAnnee( ) );
                                sbNumerosSignalement.append( signalement.getMois( ) );
                                sbNumerosSignalement.append( signalement.getNumero( ) );
                            }
                            index--;
                        }
                        else
                        {
                            urlItem.addParameter( PARAMETER_MARK_SIGNALEMENT_ID, nIdSignalement );
                        }

                        index++;
                    }
                    else
                    {
                        suppression = true;
                        _massSignalementIds = ArrayUtils.remove( _massSignalementIds, index );

                        // construct the report number
                        sbNumerosSignalement.append( LINE_BREAK );
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
                    messageArgs [0] = sbNumerosSignalement.toString( );

                    strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_MASS_ACTION_IMPOSSIBLE, messageArgs, urlItem.getUrl( ),
                            AdminMessage.TYPE_WARNING );

                    return _signalementUtils.buildRedirectResult( strUrl );

                }

                nIdResource = findNextSignalementIdMassActions( );

                if ( nIdResource == NO_RESOURCE_FOUND )
                {
                    return _signalementUtils.buildRedirectResult( getHomeUrl( request ) );
                }

                Action action = findNextAction( nIdResource, _actionType );

                if ( action == null )
                {
                    return _signalementUtils.buildRedirectResult( getHomeUrl( request ) );
                }
                nIdAction = action.getId( );
            }
        }
        else
            if ( nIdAction != null )
            {

                try
                {
                    nIdAction = Integer.parseInt( strIdAction );
                }
                catch( NumberFormatException nfe )
                {
                    throw new AppException( INVALID_ACTION_ID + nfe.getMessage( ), nfe );
                }
            }

        boolean isFromDisplaySignalement = PARAMETER_VALUE_DISPLAY_PAGE.equals( request.getParameter( PARAMETER_FROM_PAGE ) );
        String strBaseUrl = AppPathService.getBaseUrl( request );
        if ( isFromDisplaySignalement || WorkflowService.getInstance( ).canProcessAction( nIdResource, Signalement.WORKFLOW_RESOURCE_TYPE,
                nIdAction.intValue( ), null, request, false ) )
        {

            UrlItem urlItem;
            if ( WorkflowService.getInstance( ).isDisplayTasksForm( nIdAction.intValue( ), getLocale( ) ) )
            {
                // form jsp
                urlItem = new UrlItem( strBaseUrl + JSP_WORKFLOW_ACTION );
            }
            else
            {
                if ( isMessageTask( nIdAction.intValue( ) ) )
                {
                    // jsp action with message and without interaction
                    boolean isMassAction = ( _massSignalementIds != null ) && ( _massSignalementIds.length > 1 );
                    String jspReturn = getMessageConfirmationSignalement( request, nIdAction.intValue( ), isMassAction );
                    urlItem = new UrlItem( jspReturn );

                }
                else
                {
                    // jsp action without interaction
                    urlItem = new UrlItem( strBaseUrl + JSP_DO_PROCESS_ACTION );
                }

            }

            if ( isFromDisplaySignalement )
            {
                urlItem.addParameter( PARAMETER_FROM_PAGE, PARAMETER_VALUE_DISPLAY_PAGE );
            }
            urlItem.addParameter( PARAMETER_MARK_ACTION_ID, nIdAction.intValue( ) );
            urlItem.addParameter( PARAMETER_MARK_SIGNALEMENT_ID, nIdResource );
            urlItem.addParameter( PARAMETER_IS_ROAD_MAP, Boolean.toString( StringUtils.isNotBlank( request.getParameter( PARAMETER_IS_ROAD_MAP ) ) ) );
            String strDateService = request.getParameter( PARAMETER_DATE_SERVICE );
            if ( StringUtils.isNotBlank( strDateService ) )
            {
                urlItem.addParameter( PARAMETER_DATE_SERVICE, strDateService );
            }
            String strIdService = request.getParameter( PARAMETER_SERVICEID );
            if ( StringUtils.isNotBlank( strIdService ) )
            {
                urlItem.addParameter( PARAMETER_SERVICEID, strIdService );
            }
            String strServiceId = request.getParameter( PARAMETER_SERVICE_ID );
            if ( StringUtils.isNotBlank( strServiceId ) )
            {
                urlItem.addParameter( PARAMETER_SERVICE_ID, strServiceId );
            }

            String next = request.getParameter( PARAMETER_NEXT_URL );

            if ( StringUtils.contains( next, URL_JSP_GET_ROAD_MAP ) )
            {
                try
                {
                    // Return URL management
                    URI uri = new URI( next );
                    String nextPath = new URI( uri.getScheme( ), uri.getAuthority( ), uri.getPath( ), null, uri.getFragment( ) ).toString( );
                    UrlItem nextUrl = new UrlItem( nextPath );
                    // service date
                    if ( StringUtils.isNotBlank( strDateService ) )
                    {
                        nextUrl.addParameter( PARAMETER_DATE_SERVICE, strDateService );
                    }
                    // service id
                    if ( StringUtils.isNotBlank( strServiceId ) )
                    {
                        nextUrl.addParameter( PARAMETER_SERVICE_ID, strServiceId );
                    }
                    // unit id
                    String strUnitId = request.getParameter( PARAMETER_UNIT_ID );
                    if ( StringUtils.isNotBlank( strUnitId ) )
                    {
                        nextUrl.addParameter( PARAMETER_UNIT_ID, strUnitId );
                    }
                    // sector id
                    String strSectorId = request.getParameter( PARAMETER_SECTOR_ID );
                    if ( StringUtils.isNotBlank( strSectorId ) )
                    {
                        nextUrl.addParameter( PARAMETER_SECTOR_ID, strSectorId );
                    }
                    // set next url in the return url object
                    urlItem.addParameter( PARAMETER_NEXT_URL, encodeURIComponent( nextUrl.getUrl( ) ) );
                    // set next url in the session
                    request.getSession( ).setAttribute( PARAMETER_NEXT_URL, nextUrl.getUrl( ) );
                }
                catch( Exception e )
                {
                    AppLogService.error( e );
                }
            }
            else
            {
                urlItem.addParameter( PARAMETER_NEXT_URL, next );
            }

            strUrl = urlItem.getUrl( );
        }
        else
        {
            strUrl = AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
        }

        return _signalementUtils.buildRedirectResult( strUrl );
    }

    /**
     * Process mass service done.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @return the i plugin action result
     */
    public IPluginActionResult processActionServicefait( HttpServletRequest request, HttpServletResponse response )
    {

        setWorkflowReturnURI( request );
        String strIdResource = request.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );
        _massSignalementIds = _signalementUtils.getIntArray( request.getParameterValues( PARAMETER_MARK_SIGNALEMENT_ID ) );
        String strUrl;
        int nIdResource;
        Number nIdAction = 0;
        try
        {
            nIdResource = Integer.parseInt( strIdResource );
        }
        catch( NumberFormatException nfe )
        {
            return _signalementUtils.buildRedirectResult( getHomeUrl( request ) );
        }

        // actions workflow
        String strIdAction = request.getParameter( PARAMETER_MARK_ACTION_ID );

        if ( strIdAction == null )
        {
            // mass actions
            _actionType = findActionType( request );

            if ( StringUtils.isNotBlank( _actionType ) )
            {

                // deletion of reports on which the action cannot be carried out
                int index = 0;
                boolean suppression = false;
                String [ ] messageArgs = new String [ 1];
                UrlItem urlItem;
                StringBuilder sbNumerosSignalement = new StringBuilder( );
                Signalement signalement;

                urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_WORKFLOW_PROCESS_ACTION );

                putActionType( request, urlItem );

                // States control
                String trouve = isMassServiceFait( _massSignalementIds );
                /*
                 * if at least one of the selected messages is not in the new state or to be processed or program service
                 */
                if ( !"".equals( trouve ) )
                {

                    messageArgs [0] = ( ( new StringBuilder( trouve ) ).append( LINE_BREAK ) ).toString( );

                    strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_MASS_ACTION_IMPOSSIBLE, messageArgs, urlItem.getUrl( ),
                            AdminMessage.TYPE_STOP );

                    return _signalementUtils.buildRedirectResult( strUrl );

                }
                else
                {

                    for ( int nIdSignalement : _massSignalementIds )
                    {

                        if ( findNextAction( nIdSignalement, _actionType ) != null )
                        {
                            Action action = findNextAction( nIdSignalement, _actionType );
                            if ( ( action != null ) && !WorkflowService.getInstance( ).canProcessAction( nIdSignalement, Signalement.WORKFLOW_RESOURCE_TYPE,
                                    action.getId( ), null, request, false ) )
                            {

                                suppression = true;
                                _massSignalementIds = ArrayUtils.remove( _massSignalementIds, index );

                                // construct the report number
                                sbNumerosSignalement.append( LINE_BREAK );
                                signalement = _signalementService.getSignalement( nIdSignalement );
                                if ( signalement != null )
                                {
                                    sbNumerosSignalement.append( signalement.getPrefix( ) );
                                    sbNumerosSignalement.append( signalement.getAnnee( ) );
                                    sbNumerosSignalement.append( signalement.getMois( ) );
                                    sbNumerosSignalement.append( signalement.getNumero( ) );
                                }
                                index--;
                            }
                            else
                            {
                                urlItem.addParameter( PARAMETER_MARK_SIGNALEMENT_ID, nIdSignalement );
                            }

                            index++;
                        }
                        else
                        {
                            suppression = true;
                            _massSignalementIds = ArrayUtils.remove( _massSignalementIds, index );

                            // construct the report number
                            sbNumerosSignalement.append( LINE_BREAK );
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
                    messageArgs [0] = sbNumerosSignalement.toString( );

                    strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_MASS_ACTION_IMPOSSIBLE, messageArgs, urlItem.getUrl( ),
                            AdminMessage.TYPE_WARNING );

                    return _signalementUtils.buildRedirectResult( strUrl );

                }

                nIdResource = findNextSignalementIdMassActions( );

                if ( nIdResource == NO_RESOURCE_FOUND )
                {
                    return _signalementUtils.buildRedirectResult( getHomeUrl( request ) );
                }

                Action action = findNextAction( nIdResource, _actionType );

                if ( action == null )
                {
                    return _signalementUtils.buildRedirectResult( getHomeUrl( request ) );
                }
                nIdAction = action.getId( );
            }
        }
        else
            if ( nIdAction != null )
            {

                try
                {
                    nIdAction = Integer.parseInt( strIdAction );
                }
                catch( NumberFormatException nfe )
                {
                    throw new AppException( INVALID_ACTION_ID + nfe.getMessage( ), nfe );
                }
            }

        String strBaseUrl = AppPathService.getBaseUrl( request );
        if ( WorkflowService.getInstance( ).canProcessAction( nIdResource, Signalement.WORKFLOW_RESOURCE_TYPE, nIdAction.intValue( ), null, request, false ) )
        {

            UrlItem urlItem;
            if ( WorkflowService.getInstance( ).isDisplayTasksForm( nIdAction.intValue( ), getLocale( ) ) )
            {
                // jsp form
                urlItem = new UrlItem( strBaseUrl + JSP_WORKFLOW_ACTION );
            }
            else
            {
                if ( isMessageTask( nIdAction.intValue( ) ) )
                {
                    // jsp action with message and without interaction
                    boolean isMassAction = ( _massSignalementIds != null ) && ( _massSignalementIds.length > 1 );
                    String jspReturn = getMessageConfirmationSignalement( request, nIdAction.intValue( ), isMassAction );
                    urlItem = new UrlItem( jspReturn );

                }
                else
                {
                    // jsp action without interaction
                    urlItem = new UrlItem( strBaseUrl + JSP_DO_PROCESS_ACTION );
                }

            }
            urlItem.addParameter( PARAMETER_MARK_ACTION_ID, nIdAction.intValue( ) );
            urlItem.addParameter( PARAMETER_MARK_SIGNALEMENT_ID, nIdResource );
            urlItem.addParameter( "next", request.getParameter( "next" ) );
            urlItem.addParameter( PARAMETER_IS_ROAD_MAP, Boolean.toString( StringUtils.isNotBlank( request.getParameter( PARAMETER_IS_ROAD_MAP ) ) ) );
            String strDateService = request.getParameter( PARAMETER_DATE_SERVICE );
            if ( StringUtils.isNotBlank( strDateService ) )
            {
                urlItem.addParameter( PARAMETER_DATE_SERVICE, strDateService );
            }
            String strIdService = request.getParameter( PARAMETER_SERVICEID );
            if ( StringUtils.isNotBlank( strIdService ) )
            {
                urlItem.addParameter( PARAMETER_SERVICEID, strIdService );
            }

            strUrl = urlItem.getUrl( );
        }
        else
        {
            strUrl = AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
        }

        return _signalementUtils.buildRedirectResult( strUrl );
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
     * Contains id action.
     *
     * @param listeIdActions
     *            the action ids list
     * @param id
     *            the id
     * @return true, if successful
     */
    private boolean contientIdAction( int [ ] listeIdActions, int id )
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

        Map<String, Object> urlParam = new HashMap<>( );
        urlParam.put( PARAMETER_MARK_ACTION_ID, nIdAction );
        if ( isMassAction )
        {
            if ( ArrayUtils.isEmpty( _massSignalementIds ) )
            {
                return getHomeUrl( request );
            }
            StringBuilder sbNumerosSignalement = new StringBuilder( );
            for ( int nIdSignalement : _massSignalementIds )
            {
                urlParam.put( PARAMETER_MARK_SIGNALEMENT_ID, nIdSignalement );
                // construct the report number
                sbNumerosSignalement.append( LINE_BREAK );
                Signalement signalement = _signalementService.getSignalement( nIdSignalement );
                if ( signalement != null )
                {
                    sbNumerosSignalement.append( signalement.getPrefix( ) );
                    sbNumerosSignalement.append( signalement.getAnnee( ) );
                    sbNumerosSignalement.append( signalement.getMois( ) );
                    sbNumerosSignalement.append( signalement.getNumero( ) );
                }
            }

            String [ ] messageArgs = new String [ 1];
            messageArgs [0] = sbNumerosSignalement.toString( );

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

            return AdminMessageService.getMessageUrl( request, messageTitle, messageArgs, messageConfirmation, JSP_DO_PROCESS_MASS_ACTION, SELF,
                    AdminMessage.TYPE_CONFIRMATION, urlParam, JSP_MANAGE_SIGNALEMENT );
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

        urlParam.put( PARAMETER_MARK_SIGNALEMENT_ID, _massSignalementIds [0] );
        return AdminMessageService.getMessageUrl( request, messageTitle, null, messageConfirmation, JSP_DO_PROCESS_ACTION, SELF, AdminMessage.TYPE_CONFIRMATION,
                urlParam, JSP_MANAGE_SIGNALEMENT );
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

        String strIdSignalement = request.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );

        String homeUrl = getWorkflowReturnURI( request );

        boolean isFromDisplaySignalement = PARAMETER_VALUE_DISPLAY_PAGE.equals( request.getParameter( PARAMETER_FROM_PAGE ) );
        if ( isFromDisplaySignalement )
        {
            homeUrl = AppPathService.getBaseUrl( request ) + (String) request.getSession( ).getAttribute( "next" );
        }

        if ( request.getParameter( SignalementConstants.PARAMETER_BUTTON_CANCEL ) != null )
        {
            return homeUrl;
        }

        String strIdAction = request.getParameter( PARAMETER_MARK_ACTION_ID );
        String strIdResource = request.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );

        int nIdAction;
        int nIdResource;
        try
        {
            nIdAction = Integer.parseInt( strIdAction );
            nIdResource = Integer.parseInt( strIdResource );
        }
        catch( NumberFormatException nfe )
        {
            throw new AppException( INVALID_ACTION_ID + nfe.getMessage( ), nfe );
        }


        if ( isFromDisplaySignalement
                || WorkflowService.getInstance( ).canProcessAction( nIdResource, Signalement.WORKFLOW_RESOURCE_TYPE, nIdAction, null, request, false ) )
        {
            try
            {
                String strErrorUrl = WorkflowService.getInstance( ).doSaveTasksForm( nIdResource, Signalement.WORKFLOW_RESOURCE_TYPE, nIdAction, null, request,
                        getLocale( ) );
                if ( strErrorUrl != null )
                {
                    return strErrorUrl;
                }
            }
            catch( Exception e )
            {
                AppLogService.error( e );
                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_ACTION, AdminMessage.TYPE_STOP );
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
                        UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_WORKFLOW_PROCESS_ACTION );

                        for ( int nIdActionParameter : _massSignalementIds )
                        {
                            urlItem.addParameter( PARAMETER_MARK_SIGNALEMENT_ID, nIdActionParameter );
                        }

                        urlItem.addParameter( PARAMETER_MARK_ACTION_ID, action.getId( ) );

                        return urlItem.getUrl( );
                    }
                }
            }

            clearMassAction( request );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
        }

        return homeUrl;
    }

    /**
     * Gets the workflow return address. getHomeUrl is a fallback if next is not defined.
     *
     * @param request
     *            the request
     * @return the workflow return address
     */
    private String getWorkflowReturnURI( HttpServletRequest request )
    {

        String homeUrl;
        try
        {
            String strNext = (String) request.getSession( ).getAttribute( "next" );
            if ( StringUtils.isEmpty( strNext ) )
            {
                homeUrl = strNext;
            }
            else
            {
                homeUrl = getHomeUrl( request );
            }

        }
        catch( ClassCastException e )
        {
            AppLogService.error( e.getMessage( ), e );
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
        }
        else
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

        String strIdAction = request.getParameter( PARAMETER_MARK_ACTION_ID );

        int nIdAction;
        try
        {
            nIdAction = Integer.parseInt( strIdAction );
        }
        catch( NumberFormatException nfe )
        {
            throw new AppException( INVALID_ACTION_ID + nfe.getMessage( ), nfe );
        }
        for ( int nIdResource : _massSignalementIds )
        {
            if ( WorkflowService.getInstance( ).canProcessAction( nIdResource, Signalement.WORKFLOW_RESOURCE_TYPE, nIdAction, null, request, false ) )
            {
                String strErrorUrl = WorkflowService.getInstance( ).doSaveTasksForm( nIdResource, Signalement.WORKFLOW_RESOURCE_TYPE, nIdAction, null, request,
                        getLocale( ) );
                if ( strErrorUrl != null )
                {
                    return strErrorUrl;
                }
            }
            else
            {
                return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
            }
        }
        clearMassAction( request );

        return getHomeUrl( request );
    }

    /**
     * Find the action to be performed.
     *
     * @param nId
     *            resource id
     * @param strActionName
     *            action name
     * @return the id found, 0 otherwise.
     */
    private Action findNextAction( int nId, String strActionName )
    {
        Collection<Action> listActions = WorkflowService.getInstance( ).getActions( nId, Signalement.WORKFLOW_RESOURCE_TYPE,
                _signalementWorkflowService.getSignalementWorkflowId( ), getUser( ) );

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
     * Find the action to be performed..
     *
     * @return the id found, {@link #NO_RESOURCE_FOUND} otherwise
     */
    private int findNextSignalementIdMassActions( )
    {
        while ( !ArrayUtils.isEmpty( _massSignalementIds ) )
        {
            int nIdResource = _massSignalementIds [0];
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
        String strIdAction = request.getParameter( PARAMETER_MARK_ACTION_ID );
        String strIdResource = request.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );

        Boolean bHasNext = ( _massSignalementIds != null ) && ( _massSignalementIds.length > 1 );

        request.setAttribute( SignalementConstants.ATTRIBUTE_HAS_NEXT, bHasNext );

        int nIdAction;
        int nIdResource;
        try
        {
            nIdAction = Integer.parseInt( strIdAction );
            nIdResource = Integer.parseInt( strIdResource );
        }
        catch( NumberFormatException nfe )
        {
            throw new AppException( INVALID_ACTION_ID + nfe.getMessage( ), nfe );
        }

        /////////
        // we check the right again (if the users tries with the address in the url)
        /////////
        boolean isFromDisplaySignalement = PARAMETER_VALUE_DISPLAY_PAGE.equals( request.getParameter( PARAMETER_FROM_PAGE ) );
        if ( nIdResource > 0 )
        {
            Signalement signalement = _signalementService.getSignalement( nIdResource );

            Boolean authorized = isFromDisplaySignalement || estAutoriseSecteur( request, String.valueOf( signalement.getSecteur( ).getIdSector( ) ) );
            authorized = authorized && _signalementViewRoleService.isUserAuthorized( signalement, request );
            if ( !authorized )
            {

                throw new AccessDeniedException( MESSAGE_ACCESS_DENIED );

            }
        }
        /////////////

        String strTaskForm;
        if ( isFromDisplaySignalement
                || WorkflowService.getInstance( ).canProcessAction( nIdResource, Signalement.WORKFLOW_RESOURCE_TYPE, nIdAction, null, request, false ) )
        {
            strTaskForm = WorkflowService.getInstance( ).getDisplayTasksForm( nIdResource, Signalement.WORKFLOW_RESOURCE_TYPE, nIdAction, request,
                    getLocale( ) );
        }
        else
        {
            strTaskForm = StringUtils.EMPTY;
        }

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_HAS_NEXT, bHasNext );
        model.put( MARK_TASK_FORM, strTaskForm );
        model.put( PARAMETER_MARK_ACTION_ID, nIdAction );
        model.put( MARK_SIGNALEMENT, nIdResource );
        model.put( PARAMETER_MARK_SIGNALEMENT_ID, nIdResource );

        String nextURL = request.getParameter( PARAMETER_NEXT_URL );
        if ( isFromDisplaySignalement )
        {
            model.put( PARAMETER_FROM_PAGE, PARAMETER_VALUE_DISPLAY_PAGE );
            model.put( MARK_BACK_URL, URL_JSP_DISPLAY_SIGNALEMENTS );
            request.getSession( ).setAttribute( PARAMETER_NEXT_URL, URL_JSP_DISPLAY_SIGNALEMENTS );
        }
        else
            if ( ( nextURL != null ) && nextURL.contains( PARAMETER_WEBAPP_RAMEN ) )
            {
                // redirect on RAMEN Webapp
                UrlItem urlRedirect = new UrlItem( nextURL );

                String serviceID = request.getParameter( PARAMETER_SERVICE_ID );
                if ( serviceID != null )
                {
                    urlRedirect.addParameter( PARAMETER_SERVICE_ID, serviceID );
                }

                String sectorID = request.getParameter( PARAMETER_SECTOR_ID );
                if ( sectorID != null )
                {
                    urlRedirect.addParameter( PARAMETER_SECTOR_ID, sectorID );
                }

                String unitID = request.getParameter( PARAMETER_UNIT_ID );
                if ( unitID != null )
                {
                    urlRedirect.addParameter( PARAMETER_UNIT_ID, unitID );
                }

                model.put( MARK_BACK_URL, urlRedirect.getUrl( ) );
            }
            else
            {
                // stay on SIGNALEMENT Webapp
                model.put( MARK_BACK_URL, JSP_MANAGE_SIGNALEMENT );
            }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_WORKFLOW, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Export in CSV all checked report or the result the research.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     */
    public void exportSignalement( HttpServletRequest request, HttpServletResponse response )
    {
        List<SignalementExportCSVDTO> listeSignalementExportCSVDTO;
        _massSignalementIds = _signalementUtils.getIntArray( request.getParameterValues( PARAMETER_MARK_SIGNALEMENT_ID ) );

        if ( _massSignalementIds != null )
        {
            // _massSignalementIds contains the ids of the check markers, these are the ones to export
            listeSignalementExportCSVDTO = _signalementExportService.findByIds( _massSignalementIds );
        }
        else
            if ( _dashboardSignalementList != null )
            {
                int [ ] signalementIds = new int [ _dashboardSignalementList.size( )];
                for ( int i = 0; i < signalementIds.length; i++ )
                {
                    signalementIds [i] = _dashboardSignalementList.get( i );
                }
                listeSignalementExportCSVDTO = _signalementExportService.findByIds( signalementIds );
            }
            else
            {
                SignalementFilter filter = getSignalementFilter( request );

                listeSignalementExportCSVDTO = _signalementExportService.findByFilter( filter );
            }

        String [ ] datas;
        try
        {
            CSVWriter writer = null;
            response.setCharacterEncoding( CSV_ISO );
            writer = new CSVWriter( response.getWriter( ), CSV_SEPARATOR );

            writer.writeNext( new String [ ] {
                    "Identifiant technique", "Numéro", "Priorité", "Type", "Alias", "Alias mobile", "Direction", "Quartier", "Adresse", "Coordonnée X",
                    "Coordonnée Y", "Arrondissement", "Secteur d'affectation", "Date de création", "Heure de création", "Etat", "Mail usager",
                    "Commentaire usager", "Nombre de photos", "Raisons de rejet", "Nombre de suivis", "Nombre de félicitations", "Date de clôture",
                    "Présence de photos de service fait", "Mail du destinataire du courriel", "Expéditeur courriel", "Date envoi courriel",
                    "Identifiant type de message service fait", "Nom exécuteur service fait", "Date de dernière prise en compte", "Date de programmation",
                    "Commentaire agent terrain", "Rejeté par", "Sous Surveillance par", "Précisions terrain","Nombre de requalifications"
            } );
            for ( SignalementExportCSVDTO signalementDTO : listeSignalementExportCSVDTO )
            {
                datas = signalementDTO.getTabAllDatas( );
                writer.writeNext( datas );
            }

            writer.close( );

        }
        catch( IOException e )
        {
            AppLogService.error( e );
        }
    }

    /**
     * Return a list of address.
     *
     * @param add
     *            the add
     * @param allParameter
     *            the all parameter
     * @return an address list
     */
    private List<Adresse> getCoordinatesFromAccess( Adresse add, String allParameter )
    {
        String delimiter = "/";

        String [ ] temp = allParameter.split( delimiter );

        // get the address libel
        String libelAddress = temp [0].toLowerCase( );

        // get the lat/lng in lambert 27561
        String tempLatLng = temp [1].substring( 8 );

        String tempLatLng2 = tempLatLng.substring( 0, tempLatLng.length( ) - 1 );
        String [ ] separatedLatLng = tempLatLng2.split( "\\s" );

        String strLat = separatedLatLng [0];
        String strLng = separatedLatLng [1];

        Double dLat = Double.parseDouble( strLat );
        Double dLng = Double.parseDouble( strLng );

        // transform the lambert coordinates to WGS84 for the database
        Double [ ] geom = _signalementService.getGeomFromLambertToWgs84( dLat, dLng );

        add.setAdresse( libelAddress );
        add.setLat( geom [1] );
        add.setLng( geom [0] );
        List<Adresse> addresses = new ArrayList<>( );
        addresses.add( add );
        return addresses;
    }

    /**
     * Returns the confirmation message to delete a report.
     *
     * @param request
     *            The Http request
     * @return the html code message
     * @throws AccessDeniedException
     *             If the current user is not authorized to access this feature
     */
    public String getDeleteSignalement( HttpServletRequest request ) throws AccessDeniedException
    {
        String strSignalementId = request.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );

        int nIdSignalement = 0;

        try
        {
            nIdSignalement = Integer.parseInt( strSignalementId );
        }
        catch( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        Map<String, Object> urlParam = new HashMap<>( );
        urlParam.put( PARAMETER_MARK_SIGNALEMENT_ID, nIdSignalement );

        Long lIdSignalement = Long.parseLong( strSignalementId );
        Signalement signalement;
        signalement = _signalementService.getSignalement( lIdSignalement );

        // we check the right again (if the users tries with the address in the url)
        Boolean authorized = estAutoriseSecteur( request, String.valueOf( signalement.getSecteur( ).getIdSector( ) ) );
        authorized = authorized && _signalementViewRoleService.isUserAuthorized( signalement, request );
        if ( !authorized )
        {

            throw new AccessDeniedException( MESSAGE_ACCESS_DENIED );

        }

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRMATION_DELETE_SIGNALEMENT, null, MESSAGE_TITLE_DELETE_SIGNALEMENT,
                JSP_DELETE_SIGNALEMENT, SELF, AdminMessage.TYPE_CONFIRMATION, urlParam, JSP_MANAGE_SIGNALEMENT );
    }

    /**
     * Delete a report.
     *
     * @param request
     *            The Http request
     * @return url
     */
    public String doDeleteSignalement( HttpServletRequest request )
    {
        String strSignalementId = request.getParameter( PARAMETER_MARK_SIGNALEMENT_ID );

        int nIdSignalement = 0;

        try
        {
            nIdSignalement = Integer.parseInt( strSignalementId );
        }
        catch( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        _signalementService.doDeleteSignalement( nIdSignalement );

        return doGoBack( request );
    }

    /**
     * Returns the confirmation message to delete a report list.
     *
     * @param request
     *            The Http request
     * @return the html code message
     */
    public String getMassDeleteSignalement( HttpServletRequest request )
    {

        Map<String, Object> urlParam = new HashMap<>( );
        if ( ArrayUtils.isEmpty( _massSignalementIds ) )
        {
            return getHomeUrl( request );
        }
        StringBuilder sbNumerosSignalement = new StringBuilder( );
        for ( int nIdSignalement : _massSignalementIds )
        {
            urlParam.put( PARAMETER_MARK_SIGNALEMENT_ID, nIdSignalement );

            // construct the report number
            sbNumerosSignalement.append( LINE_BREAK );
            Signalement signalement = _signalementService.getSignalement( nIdSignalement );
            if ( signalement != null )
            {
                sbNumerosSignalement.append( signalement.getPrefix( ) );
                sbNumerosSignalement.append( signalement.getAnnee( ) );
                sbNumerosSignalement.append( signalement.getMois( ) );
                sbNumerosSignalement.append( signalement.getNumero( ) );
            }
        }

        String [ ] messageArgs = new String [ 1];

        String elementCount = Integer.toString( _massSignalementIds.length );
        messageArgs [0] = elementCount;

        return AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_MASS_DELETE_SIGNALEMENT, messageArgs, MESSAGE_CONFIRMATION_MASS_DELETE_SIGNALEMENT,
                JSP_MASS_DELETE_SIGNALEMENT, SELF, AdminMessage.TYPE_CONFIRMATION, urlParam, JSP_MANAGE_SIGNALEMENT );
    }

    /**
     * Delete a list of selected report.
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
            }
            catch( NumberFormatException e )
            {
                return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
            }

        }
        return getHomeUrl( request );
    }

    /**
     * Do update onglet actif.
     *
     * @param request
     *            the request
     */
    public void doUpdateOngletActif( HttpServletRequest request )
    {
        String strOnglet = request.getParameter( PARAMETER_ONGLET );
        if ( StringUtils.isNotEmpty( strOnglet ) )
        {
            _signalementFilter.setOnglet( strOnglet );
        }
    }

    /**
     * Get the sector list attach to the selected unit.
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
        response.setContentType( EXTENSION_APPLICATION_JSON );
        try
        {
            jsonStringer = new JSONBuilder( response.getWriter( ) );
            try
            {
                Integer directionId = Integer.parseInt( strDirectionId );
                // Specificity for DEVE entity, change the id from SEJ to DEVE
                if ( directionId == SEJ_ID )
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
                        }
                        else
                            if ( _unitService.isParent( userUnit, unitSelected ) )
                            {
                                listAuthorizedUnits.add( unitSelected );
                            }
                    }
                }
                else
                {
                    listAuthorizedUnits = listUnits;
                }

                List<Sector> listSectors = new ArrayList<>( );
                for ( Unit userUnit : listAuthorizedUnits )
                {
                    // Sort the sectors by removing SC and Gardens for the DEVE
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
                ReferenceList refListSectorsOfUnit = _listUtils.toReferenceList( listSectors, ID_SECTOR, "name", StringUtils.EMPTY, true );

                jsonStringer.object( ).key( MARK_SECTEUR_LIST ).array( );
                for ( ReferenceItem sector : refListSectorsOfUnit )
                {
                    jsonStringer.object( ).key( JSON_KEY_ID ).value( sector.getCode( ) ).key( "value" ).value( sector.getName( ) ).endObject( );
                }
                jsonStringer.endArray( ).endObject( );
            }
            catch( NumberFormatException e )
            {
                jsonStringer.object( ).key( "errors" ).array( ).value( e.getMessage( ) ).endArray( ).endObject( );
            }
        }
        catch( IOException e1 )
        {
            AppLogService.error( e1.getMessage( ), e1 );
        }

    }

    /**
     * Finds all report that the user can see, based on its domain.
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

        // Updating the filter according to domain data
        int idDomaine = filter.getIdDomaine( );
        DomaineFonctionnel domaineFonc = _domaineFonctionnelService.getById( idDomaine );
        if ( null == domaineFonc )
        {
            AdminUser adminUser = AdminUserService.getAdminUser( request );
            // Retrieving user domains
            Collection<DomaineFonctionnel> domainesFonctionnels = _domaineFonctionnelService.getAllDomainesFonctionnelActifs( );
            domainesFonctionnels = RBACService.getAuthorizedCollection( domainesFonctionnels,
                    DomaineFonctionnelSignalementResourceIdService.PERMISSION_CONSULT_SIGNALEMENT, adminUser );
            domaineFonc = domainesFonctionnels.iterator( ).next( );
        }

        // Districts
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
            }
            else
            {
                filter.setListIdArrondissements( domainArrondissements );
            }
        }
        else
        {
            List<Integer> noRessource = new ArrayList<>( );
            noRessource.add( NO_RESOURCE_FOUND );
            filter.setListIdArrondissements( noRessource );
        }

        // Neighborhoods
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
            }
            else
                if ( domainQuartiers.size( ) == 1 )
                {
                    // No filter and only 1 field -> selected by default
                    filtreQuartier = true;
                    filter.setListIdQuartier( domainQuartiers );
                }
                else
                    if ( domainQuartiers.size( ) > 1 )
                    {
                        // No filter and several fields -> Selected value empty but search on all neighborhoods
                        filter.setListIdQuartier( domainQuartiers );
                        isAllQuartier = true;
                    }
        }
        else
        {
            List<Integer> noRessource = new ArrayList<>( );
            noRessource.add( NO_RESOURCE_FOUND );
            filter.setListIdQuartier( noRessource );
        }

        // Sectors
        List<Integer> listUnitsIds = domaineFonc.getUnitIds( );
        if ( CollectionUtils.isNotEmpty( listUnitsIds ) )
        {
            filter.setListIdUnit( listUnitsIds );
        }
        else
        {
            List<Integer> noRessource = new ArrayList<>( );
            noRessource.add( NO_RESOURCE_FOUND );
            filter.setListIdSecteurAutorises( noRessource );
        }

        // Categories
        if ( CollectionUtils.isNotEmpty( domaineFonc.getTypesSignalementIds( ) ) )
        {
            filter.setListIdCategories( new ArrayList<>( domaineFonc.getTypesSignalementIds( ) ) );
        }
        else
        {
            List<Integer> noRessource = new ArrayList<>( );
            noRessource.add( NO_RESOURCE_FOUND );
            filter.setListIdCategories( noRessource );
        }

        List<Integer> listIdDirection = filter.getListIdDirection();
        if ( CollectionUtils.isNotEmpty(listIdDirection) && listIdDirection.contains( SEJ_ID ) )
        {

            filter.getListIdDirection( ).removeIf( idDirection -> idDirection.equals( SEJ_ID ) );
            filter.getListIdDirection( ).add( 1 );
        }

        Integer totalResult = _signalementExportService.countSearchResult( filter );
        List<Signalement> signalementList = _signalementExportService.findByFilterSearch( filter, getPaginationProperties( request, totalResult ) );

        if ( isAllQuartier )
        {
            filter.setListIdQuartier( new ArrayList<>( ) );
        }

        // sort list by first unit of each report
        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = request.getParameter( Parameters.SORTED_ASC );
        if ( StringUtils.isNotBlank( strSortedAttributeName ) && "unit.label".equals( strSortedAttributeName ) )
        {
            DirectionComparator c = new DirectionComparator( );
            c.setSortOrder( strAscSort );
            Collections.sort( signalementList, c );
        }

        ResultList<Signalement> resultListSignalement = new ResultList<>( );

        resultListSignalement.addAll( signalementList );

        resultListSignalement.setTotalResult( signalementList.size( ) );
        LocalizedDelegatePaginator<Signalement> paginator = this.getPaginator( request, resultListSignalement, URL_JSP_DISPLAY_SIGNALEMENTS, totalResult );

        _anomaliesCount = totalResult;

        // the paginator
        model.put( SignalementConstants.MARK_NB_ITEMS_PER_PAGE, StringUtils.EMPTY + _nItemsPerPage );
        model.put( SignalementConstants.MARK_PAGINATOR, paginator );

        // workflow : recovery of states and possible actions for page report
        Map<String, String> mapStates = new HashMap<>( );
        WorkflowService workflowService = WorkflowService.getInstance( );
        Integer signalementWorkflowId = _signalementWorkflowService.getSignalementWorkflowId( );
        Map<String, Action> mapActionRequalifier = new HashMap<>( );
        if ( workflowService.isAvailable( ) )
        {
            for ( Signalement signalement : paginator.getPageItems( ) )
            {
                // state
                State state = workflowService.getState( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE, signalementWorkflowId, null );
                List<Action> lstAction = _signalementWorkflowService.findActionByName( SignalementConstants.WORKFLOW_ACTION_NAME_REQUALIFIER );
                Optional<Action> optAction = lstAction.stream( ).filter( action -> action.getStateBefore( ).getId( ) == state.getId( ) ).findFirst( );
                if ( optAction.isPresent( ) )
                {
                    mapActionRequalifier.put( signalement.getId( ).toString( ), optAction.get( ) );
                }

                mapStates.put( signalement.getId( ).toString( ), state == null ? "Non défini" : state.getName( ) );
            }
        }

        // Display of advanced options again
        boolean hasCriteresAvances = hasCriteresAvances( filter, domaineFonc.getArrondissementsIds( ) ) || filtreQuartier;

        model.put( MARK_HAS_ADVANCED_CRITERIAS, hasCriteresAvances );

        model.put( MARK_SIGNALEMENT_LIST, paginator.getPageItems( ) );
        model.put( MARK_ACTIONS, mapActionRequalifier );
        model.put( MARK_ETATS, mapStates );
        model.put( MARK_MAP_MAX_RESULTS, AppPropertiesService.getPropertyInt( PROPERTY_MAP_MAX_RESULTS, 0 ) );

        listIdDirection = filter.getListIdDirection();
        if ( CollectionUtils.isNotEmpty(listIdDirection) && listIdDirection.contains( 1 ) )
        {

            filter.getListIdDirection( ).removeIf( idDirection -> idDirection.equals( 1 ) );
            filter.getListIdDirection( ).add( SEJ_ID );
        }

        model.put( MARK_FILTER, filter );
        _signalementFilter = filter;

        return getAdminPage( getTemplate( TEMPLATE_DISPLAY_SIGNALEMENT, model ) );

    }

    /**
     * Get the domain.
     *
     * @param request
     *            HttpServletRequest
     * @param response
     *            HTTPServletResponse
     * @throws IOException
     *             throws IOException
     */
    public void getDomain( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        int idDomaine = Integer.parseInt( request.getParameter( ID_DOMAINE ) );
        DomaineFonctionnel domFonc = _domaineFonctionnelService.getById( idDomaine );

        ReferenceList arrondissementRefList = _listUtils.toReferenceList( _arrondissements, "id", NUMBER, null );
        ReferenceList quartierRefList = _listUtils.toReferenceList( _conseilQuartier.selectQuartiersList( ), "idConsqrt", "nomConsqrt", null );
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
                if ( ( o1 == null ) || ( o2 == null ) )
                {
                    return 0;
                }

                return o1.getLabel( ).compareTo( o2.getLabel( ) );
            }

        } );
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
            ReferenceList directionRefList = ReferenceList.convert( directions, ID_UNIT, LABEL, true );

            directionRefList.add( 0, defaultItem );
            domFonc.setDirectionsRefList( directionRefList );
        }
        else
        {
            domFonc.setDirectionsRefList( emptyList );
        }

        // Districts
        if ( CollectionUtils.isEmpty( domFonc.getArrondissementsIds( ) ) )
        {
            domFonc.setArrondissementsRefList( emptyList );
        }
        else
        {
            domFonc.setArrondissementsRefList( _listUtils.retainReferenceList( arrondissementRefList, domFonc.getArrondissementsIds( ), false ) );
        }

        // Neighborhoods
        if ( CollectionUtils.isEmpty( domFonc.getQuartiersIds( ) ) )
        {
            domFonc.setQuartierRefList( emptyList );
        }
        else
        {
            domFonc.setQuartierRefList( _listUtils.retainReferenceList( quartierRefList, domFonc.getQuartiersIds( ), false ) );
        }

        // Anomaly types
        if ( CollectionUtils.isEmpty( domFonc.getTypesSignalementIds( ) ) )
        {
            domFonc.setTypesAnomalieRefList( emptyList );
        }
        else
        {
            ReferenceList typesAnomaliesList = new ReferenceList( );
            for ( TypeSignalement typeSignalement : _typesAnomalies )
            {
                if ( CollectionUtils.isNotEmpty( domFonc.getTypesSignalementIds( ) )
                        && ( domFonc.getTypesSignalementIds( ).contains( typeSignalement.getIdCategory( ) )
                                || domFonc.getTypesSignalementIds( ).contains( typeSignalement.getId( ) ) ) )
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

        // Sector
        if ( CollectionUtils.isNotEmpty( directions ) )
        {
            List<Unit> directionList = new ArrayList<>( );
            directionList.addAll( directions );
            List<Sector> sectors = getSectorsByUnits( directionList );
            ReferenceList secteursRefList = ReferenceList.convert( sectors, ID_SECTOR, "name", true );
            secteursRefList.add( 0, defaultItem );
            domFonc.setSecteursRefList( secteursRefList );
        }
        else
        {
            domFonc.setSecteursRefList( emptyList );
        }

        // Tour sheets
        if ( CollectionUtils.isEmpty( domFonc.getUnitIds( ) ) )
        {
            domFonc.setFeuillesDeTourneeRefList( emptyList );
        }
        else
        {
            ReferenceList feuilleDeTourneeRefList = new ReferenceList( );
            for (FeuilleDeTournee fdt : _feuillesDeTournee)
            {
                if (domFonc.getUnitIds( ).contains(fdt.getIdDirection( )))
                {
                    feuilleDeTourneeRefList.addItem( fdt.getId( ), fdt.getNom( ) );
                }
            }
            domFonc.setFeuillesDeTourneeRefList( feuilleDeTourneeRefList );
        }

        ObjectMapper mapper = new ObjectMapper( );
        String domFoncJson = mapper.writeValueAsString( domFonc );
        response.setContentType( EXTENSION_APPLICATION_JSON );
        try
        {
            response.getWriter( ).print( domFoncJson );
        }
        catch( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
    }

    /**
     * Returns a json, containing all domains, with their criteria.
     *
     * @param request
     *            the HttpServletRequest
     * @param response
     *            the HttpServletResponse
     * @throws IOException
     *             throws IOException
     */
    public void initDomains( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        AdminUser adminUser = AdminUserService.getAdminUser( request );

        int idDomaine = Integer.parseInt( request.getParameter( ID_DOMAINE ) );

        // Retrieving user domains
        Collection<DomaineFonctionnel> domainesFonctionnels = _domaineFonctionnelService.getAllDomainesFonctionnelActifs( );
        domainesFonctionnels = RBACService.getAuthorizedCollection( domainesFonctionnels,
                DomaineFonctionnelSignalementResourceIdService.PERMISSION_CONSULT_SIGNALEMENT, adminUser );

        // District
        _arrondissements = _arrondissementService.getAllArrondissement( );
        ReferenceList arrondissementRefList = _listUtils.toReferenceList( _arrondissements, "id", NUMBER, null );

        // Neighborhoods
        ReferenceList quartierRefList = _listUtils.toReferenceList( _conseilQuartier.selectQuartiersList( ), "idConsqrt", "nomConsqrt", null );

        // Anomaly types
        _typesAnomalies = _typeSignalementService.getAllTypeSignalementActif( );

        // Tour sheets
        _feuillesDeTournee = _feuilleTourneeService.getAllFeuilleDeTourneeAutorise( adminUser );
        // ReferenceList feuilleDeTourneeRefList = _listUtils.toReferenceList( _feuillesDeTournee, "id", "nom", null );

        ReferenceItem defaultItem = new ReferenceItem( );
        defaultItem.setName( "" );
        defaultItem.setCode( "-1" );

        ReferenceList emptyList = new ReferenceList( );

        // we only get the complete information of the 1st domain or the selected domain
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
                    ReferenceList directionRefList = ReferenceList.convert( directions, ID_UNIT, LABEL, true );

                    directionRefList.add( 0, defaultItem );
                    domFonc.setDirectionsRefList( directionRefList );
                }
                else
                {
                    domFonc.setDirectionsRefList( emptyList );
                }

                // Districts
                if ( CollectionUtils.isEmpty( domFonc.getArrondissementsIds( ) ) )
                {
                    domFonc.setArrondissementsRefList( emptyList );
                }
                else
                {
                    domFonc.setArrondissementsRefList( _listUtils.retainReferenceList( arrondissementRefList, domFonc.getArrondissementsIds( ), false ) );
                }

                // Neighborhoods
                if ( CollectionUtils.isEmpty( domFonc.getQuartiersIds( ) ) )
                {
                    domFonc.setQuartierRefList( emptyList );
                }
                else
                {
                    domFonc.setQuartierRefList( _listUtils.retainReferenceList( quartierRefList, domFonc.getQuartiersIds( ), false ) );
                }

                // Anomaly types
                if ( CollectionUtils.isEmpty( domFonc.getTypesSignalementIds( ) ) )
                {
                    domFonc.setTypesAnomalieRefList( emptyList );
                }
                else
                {
                    ReferenceList typesAnomaliesList = new ReferenceList( );
                    for ( TypeSignalement typeSignalement : _typesAnomalies )
                    {
                        if ( CollectionUtils.isNotEmpty( domFonc.getTypesSignalementIds( ) )
                                && ( domFonc.getTypesSignalementIds( ).contains( typeSignalement.getIdCategory( ) )
                                        || domFonc.getTypesSignalementIds( ).contains( typeSignalement.getId( ) ) ) )
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

                // Sectors
                if ( CollectionUtils.isNotEmpty( directions ) )
                {
                    List<Unit> directionList = new ArrayList<>( );
                    directionList.addAll( directions );
                    List<Sector> sectors = getSectorsByUnits( directionList );
                    ReferenceList secteursRefList = ReferenceList.convert( sectors, ID_SECTOR, "name", true );
                    secteursRefList.add( 0, defaultItem );
                    domFonc.setSecteursRefList( secteursRefList );
                }
                else
                {
                    domFonc.setSecteursRefList( emptyList );
                }

                // Tour sheets
                if ( CollectionUtils.isEmpty( domFonc.getUnitIds( ) ) )
                {
                    domFonc.setFeuillesDeTourneeRefList( emptyList );
                }
                else
                {
                    ReferenceList feuilleDeTourneeRefList = new ReferenceList( );
                    for (FeuilleDeTournee fdt : _feuillesDeTournee)
                    {
                        if (domFonc.getUnitIds( ).contains(fdt.getIdDirection( )))
                        {
                            feuilleDeTourneeRefList.addItem( fdt.getId( ), fdt.getNom( ) );
                        }
                    }
                    domFonc.setFeuillesDeTourneeRefList( feuilleDeTourneeRefList );
                }

                isDomaineCompletRenseigne = true;
            }
        }

        ObjectMapper mapper = new ObjectMapper( );
        String domainesFoncJson = mapper.writeValueAsString( domainesFonctionnels );
        response.setContentType( EXTENSION_APPLICATION_JSON );
        try
        {
            response.getWriter( ).print( domainesFoncJson );
        }
        catch( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
    }

    /**
     * Gets all Sectors based on Units.
     *
     * @param listUnits
     *            the units list
     * @return a sector list
     */
    public List<Sector> getSectorsByUnits( List<Unit> listUnits )
    {
        List<Sector> listSectors = new ArrayList<>( );
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
     * Get the sector list attach to the selected unit.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     */
    public void getSectorListByIdDirectionForDisplay( HttpServletRequest request, HttpServletResponse response )
    {
        int idDomaine = Integer.parseInt( request.getParameter( ID_DOMAINE ) );
        DomaineFonctionnel domFonc = _domaineFonctionnelService.getById( idDomaine );

        String strDirectionId = request.getParameter( PARAMETER_DIRECTION_ID );

        JSONBuilder jsonStringer;
        response.setContentType( EXTENSION_APPLICATION_JSON );
        try
        {
            jsonStringer = new JSONBuilder( response.getWriter( ) );
            jsonStringer.object( );
            try
            {
                List<Unit> units = new ArrayList<>( );

                if ( CollectionUtils.isNotEmpty( domFonc.getUnitIds( ) ) )
                {
                    Integer directionId = Integer.parseInt( strDirectionId );
                    for ( Integer idUnit : domFonc.getUnitIds( ) )
                    {
                        Unit unitSelected = _unitService.getUnit( idUnit, false );
                        if ( unitSelected.getIdParent( ) == directionId )
                        {
                            units.add( unitSelected );
                        }
                    }
                }

                if ( CollectionUtils.isNotEmpty( units ) )
                {
                    List<Sector> listSectorsForSelectedUnit = getSectorsByUnits( units );

                    ReferenceList refListSectorsOfUnit = _listUtils.toReferenceList( listSectorsForSelectedUnit, ID_SECTOR, "name", StringUtils.EMPTY, true );

                    jsonStringer.key( MARK_SECTEUR_LIST ).array( );
                    for ( ReferenceItem sector : refListSectorsOfUnit )
                    {
                        jsonStringer.object( ).key( JSON_KEY_ID ).value( sector.getCode( ) ).key( "value" ).value( sector.getName( ) ).endObject( );
                    }
                    jsonStringer.endArray( );
                }
            }
            catch( NumberFormatException e )
            {
                jsonStringer.object( ).key( "errors" ).array( ).value( e.getMessage( ) ).endArray( );
            }
            jsonStringer.endObject( );
        }
        catch( IOException e1 )
        {
            AppLogService.error( e1 );
        }
    }

    /**
     * List of states available for filtering.
     *
     * @return a state list
     */
    private List<State> getListeEtats( )
    {
        List<State> listeEtat = _cachesService.getAllStateByWorkflow( getUser( ) );
        List<State> listeTemp = new ArrayList<>( );
        String [ ] idStateNotDisplay = AppPropertiesService.getProperty( STATE_NOT_DISPLAY ).split( "," );

        if ( idStateNotDisplay.length > 0 )
        {
            List<Integer> listTmp = Arrays.asList( idStateNotDisplay ).stream( ).map( Integer::parseInt ).collect( Collectors.toList( ) );
            listTmp.stream( ).forEach( ( Integer idState ) -> {
                for ( State state : listeEtat )
                {
                    if ( state.getId( ) == idState.intValue( ) )
                    {
                        listeTemp.add( state );
                    }
                }
            } );
        }
        listeEtat.removeAll( listeTemp );

        listeEtat.sort( ( o1, o2 ) -> Integer.valueOf( o1.getOrder( ) ).compareTo( Integer.valueOf( o2.getOrder( ) ) ) );

        return listeEtat;
    }

    /**
     * FInds all the units matchings the localization and within the specified radius.
     *
     * @param request
     *            the HttpServletRequest
     * @param response
     *            the HttpServletResponse
     * @throws IOException
     *             throws IOException
     */
    public void getSectorsByGeomAndUnits( HttpServletRequest request, HttpServletResponse response ) throws IOException
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
        }
        catch( NumberFormatException e )
        {
            throw new FunctionnalException( SignalementConstants.MESSAGE_ERROR_OCCUR );
        }

        try
        {
            if ( !StringUtils.isEmpty( strLng ) )
            {
                nLng = Double.parseDouble( strLng );
            }
        }
        catch( NumberFormatException e )
        {
            throw new FunctionnalException( SignalementConstants.MESSAGE_ERROR_OCCUR );
        }

        try
        {
            if ( !StringUtils.isEmpty( strTypeSignalementId ) )
            {
                nTypeSignalementId = Integer.parseInt( strTypeSignalementId );
            }
        }
        catch( NumberFormatException e )
        {
            throw new FunctionnalException( SignalementConstants.MESSAGE_ERROR_OCCUR );
        }

        Integer radius = AppPropertiesService.getPropertyInt( PROPERTY_UNITS_RADIUS, 0 );

        // Entity calculated by default of the anomaly
        Unit defaultUnit = _unitSiraService.findUnitByGeomAndTypeSignalement( nLng, nLat, nTypeSignalementId );
        // Recovery of the sectors linked to this direction and that of DEVE
        List<Integer> idUnits = new ArrayList<>( );
        if ( defaultUnit != null )
        {
            idUnits.add( defaultUnit.getIdUnit( ) );
        }
        idUnits.add( Integer.parseInt( SignalementConstants.UNIT_DEVE ) );

        // Default sector of the anomaly calculated by default
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

        response.setContentType( EXTENSION_APPLICATION_JSON );
        try
        {
            response.getWriter( ).print( result.toString( ) );
        }
        catch( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }

    }

    /**
     * Init paginator index to void.
     */
    public void reinitPaginatorCurrentPageIndex( )
    {
        _strCurrentPageIndex = "";
    }

    /**
     * Gets the export for map.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void getExportForMap( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        SignalementFilter filter = _signalementFilter;
        List<SignalementExportCSVDTO> listeSignalementExportCSVDTO;

        if ( filter != null )
        {
            List<Integer> signalementIds = _signalementService.getIdsSignalementByFilter( filter );
            // tri par id croissant
            signalementIds = signalementIds.stream( ).sorted( ).collect( Collectors.toList( ) );
            int [ ] signalementIdsArray = signalementIds.stream( ).mapToInt( i -> i ).toArray( );
            listeSignalementExportCSVDTO = _signalementExportService.findByIds( signalementIdsArray );

            String [ ] datas;
            try
            {
                CSVWriter writer = null;
                response.setCharacterEncoding( CSV_ISO );
                writer = new CSVWriter( response.getWriter( ), CSV_SEPARATOR );

                writer.writeNext( new String [ ] {
                        "Numéro de pin", "Identifiant technique", "Numéro", "Priorité", "Type", "Alias", "Alias mobile", "Direction", "Quartier", "Adresse",
                        "Coordonnée X", "Coordonnée Y", "Arrondissement", "Secteur d'affectation", "Date de création", "Heure de création", "Etat",
                        "Mail usager", "Commentaire usager", "Nombre de photos", "Raisons de rejet", "Nombre de suivis", "Nombre de félicitations",
                        "Date de clôture", "Présence de photos de service fait", "Mail du destinataire du courriel", "Expéditeur courriel",
                        "Date envoi courriel", "Identifiant type de message service fait", "Nom exécuteur service fait", "Date de dernière prise en compte",
                        "Date de programmation", "Commentaire agent terrain", "Rejeté par", "Sous Surveillance par", "Nombre de requalifications"
                } );
                int numPin = 1;
                for ( SignalementExportCSVDTO signalementDTO : listeSignalementExportCSVDTO )
                {
                    datas = signalementDTO.getTabAllDatasWithNumPin( numPin );
                    writer.writeNext( datas );
                    numPin++;
                }

                writer.close( );

            }
            catch( IOException e )
            {
                AppLogService.error( e );
            }
        }

    }

    /**
     * Get as json all reports in purpose to display them as marker on the map.
     *
     * @param request
     *            the HttpServletRequest
     * @param response
     *            the HttpServletResponse
     * @throws IOException
     *             throws IOException
     */
    public void getSignalementsJsonForMap( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        // Executes only if nomaly count < max results
        int maxResults = AppPropertiesService.getPropertyInt( PROPERTY_MAP_MAX_RESULTS, 0 );
        if ( ( _anomaliesCount != null ) && ( _anomaliesCount > maxResults ) )
        {
            response.setContentType( EXTENSION_APPLICATION_JSON );
            response.getWriter( ).print( "" );
            return;
        }

        SignalementFilter filter = _signalementFilter;
        List<SignalementMapMarkerDTO> signalementMarkers = new ArrayList<>( );

        List<Signalement> signalements = new ArrayList<>( );

        if ( CollectionUtils.isNotEmpty( _dashboardSignalementList ) )
        {
            signalements = _signalementService.getByIds( _dashboardSignalementList, null, null );
        }
        else
            if ( filter != null )
            {
                Integer totalResult = _signalementExportService.countSearchResult( filter );
                signalements = _signalementExportService.findByFilterSearch( filter, null );
            }

        for ( Signalement signalement : signalements )
        {
            SignalementMapMarkerDTO sigMarker = new SignalementMapMarkerDTO( );
            sigMarker.setIdSignalement( signalement.getId( ) );

            String typeSignalement;
            String dateCreation;

            if ( CollectionUtils.isNotEmpty( _dashboardSignalementList ) )
            {
                typeSignalement = signalement.getTypeSignalement( ).getLibelle( );
                dateCreation = signalement.getDateCreation( ) + " " + _dateUtils.getHourFr( signalement.getHeureCreation( ) );
                sigMarker.setLng( signalement.getAdresses( ).get( 0 ).getLng( ) );
                sigMarker.setLat( signalement.getAdresses( ).get( 0 ).getLat( ) );
            }
            else
            {
                typeSignalement = signalement.getType( );
                dateCreation = signalement.getDateCreation( );
                sigMarker.setLng( signalement.getAdresses( ).get( 0 ).getLat( ) );
                sigMarker.setLat( signalement.getAdresses( ).get( 0 ).getLng( ) );
            }

            // Creation date
            String dateCreationTitle = I18nService.getLocalizedString( "dansmarue.map.tooltips.datecreation", request.getLocale( ) );
            sigMarker.addTooltipText( dateCreationTitle, dateCreation );

            // Address
            String adresse = signalement.getAdresses( ).get( 0 ).getAdresse( );
            String adresseTitle = I18nService.getLocalizedString( "dansmarue.map.tooltips.adresse", request.getLocale( ) );
            sigMarker.addTooltipText( adresseTitle, adresse );

            // Priorities
            String priorite = signalement.getPrioriteName( );
            String prioriteTitle = I18nService.getLocalizedString( "dansmarue.map.tooltips.priorite", request.getLocale( ) );
            sigMarker.addTooltipText( prioriteTitle, priorite );

            // Report type
            String typeSignalementTitle = I18nService.getLocalizedString( "dansmarue.map.tooltips.typeSignalement", request.getLocale( ) );
            sigMarker.addTooltipText( typeSignalementTitle, typeSignalement );

            // State
            State stateSignalement = WorkflowService.getInstance( ).getState( signalement.getId( ).intValue( ), Signalement.WORKFLOW_RESOURCE_TYPE,
                    _signalementWorkflowService.getSignalementWorkflowId( ), null );
            if ( _listGreenMarkerStates.contains( stateSignalement.getId( ) ) )
            {
                sigMarker.setIconName( LeafletDansMaRueConstants.GREEN_ICON );
            }
            else
                if ( _listYellowMarkerStates.contains( stateSignalement.getId( ) ) )
                {
                    sigMarker.setIconName( LeafletDansMaRueConstants.YELLOW_ICON );
                }
                else
                {
                    sigMarker.setIconName( LeafletDansMaRueConstants.RED_ICON );
                }

            signalementMarkers.add( sigMarker );
        }
        response.setContentType( EXTENSION_APPLICATION_JSON );
        ObjectMapper mapper = new ObjectMapper( );
        String sigMarkers = mapper.writeValueAsString( signalementMarkers );

        try
        {
            response.getWriter( ).print( sigMarkers );
        }
        catch( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
    }

    /**
     * Returns if the user has selected advanced criteria.
     *
     * @param filter
     *            the report filter
     * @param availableArrondissementIds
     *            the available districts ids
     * @return true if the user has selected advanced criterias
     */
    private boolean hasCriteresAvances( SignalementFilter filter, List<Integer> availableArrondissementIds )
    {
        boolean hasCritereAdresse = StringUtils.isNotBlank( filter.getAdresse( ) );
        boolean hasCritereMail = StringUtils.isNotBlank( filter.getMail( ) );
        boolean hasCritereCommentaire = StringUtils.isNotBlank( filter.getCommentaire( ) );
        boolean hasCritereArrondissement = CollectionUtils.isNotEmpty( filter.getListIdArrondissements( ) )
                && !( filter.getListIdArrondissements( ).containsAll( availableArrondissementIds ) );
        boolean hasCritereQuartier = CollectionUtils.isNotEmpty( filter.getListIdQuartier( ) );
        boolean hasCritereCommentaireAgent = StringUtils.isNotBlank( filter.getCommentaireAgentTerrain( ) );
        boolean hasCritereDateServiceDone = StringUtils.isNotBlank( filter.getDateDoneBegin( ) ) || StringUtils.isNotBlank( filter.getDateDoneEnd( ) );
        boolean hasCritereMailDernierTraitement = StringUtils.isNotBlank( filter.getMailDernierIntervenant( ) );

        return hasCritereAdresse || hasCritereMail || hasCritereCommentaire || hasCritereArrondissement || hasCritereQuartier || hasCritereCommentaireAgent
                || hasCritereDateServiceDone || hasCritereMailDernierTraitement;
    }

    /**
     * Encode URI component.
     *
     * @param component
     *            the component
     * @return the string
     */
    private String encodeURIComponent( String component )
    {

        String result = null;

        try
        {
            result = URLEncoder.encode( component, "UTF-8" ).replaceAll( "\\%28", "(" ).replaceAll( "\\%29", ")" ).replaceAll( "\\+", "%20" )
                    .replaceAll( "\\%27", "'" ).replaceAll( "\\%21", "!" ).replaceAll( "\\%7E", "~" );
        }
        catch( UnsupportedEncodingException e )
        {
            AppLogService.error( e.getMessage( ), e );
            result = component;
        }

        return result;

    }

}
