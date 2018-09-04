package fr.paris.lutece.plugins.dansmarue.web;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.DashboardPeriod;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementDashboardFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.UnitNode;
import fr.paris.lutece.plugins.dansmarue.service.IArrondissementService;
import fr.paris.lutece.plugins.dansmarue.service.IDashboardPeriodService;
import fr.paris.lutece.plugins.dansmarue.service.IDashboardUserPreferencesService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementUnitService;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.service.IWorkflowService;
import fr.paris.lutece.plugins.dansmarue.service.dto.DashboardSignalementDTO;
import fr.paris.lutece.plugins.dansmarue.service.role.SignalementResourceIdService;
import fr.paris.lutece.plugins.dansmarue.service.role.SignalementViewRoleService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.ListUtils;
import fr.paris.lutece.plugins.dansmarue.utils.UnitUtils;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.service.sector.ISectorService;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.business.rbac.AdminRole;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

/**
 * The Class SignalementDashboardJspBean.
 */
public class SignalementDashboardJspBean extends AbstractJspBean {

  //SESSION
  private static final String SESSION_ATTRIBUTE_SIGNALEMENT = "signalement";
	
  // RIGHTS
  /** The Constant RIGHT_MANAGE_SIGNALEMENT. */
  public static final String RIGHT_MANAGE_SIGNALEMENT = "SIGNALEMENT_DASHBOARD";

  // TEMPLATES
  /** The Constants TEMPLATE_MANAGE_SIGNALEMENT_DASHBOARD */
  private static final String TEMPLATE_MANAGE_SIGNALEMENT_DASHBOARD = "admin/plugins/signalement/manage_signalement_dashboard.html";
  
  // JSP
  /** The Constant JSP_MANAGE_SIGNALEMENT. */
  private static final String JSP_MANAGE_SIGNALEMENT = "jsp/admin/plugins/signalement/ManageSignalement.jsp";

  // PARAMETERS
  /** The Constant PARAMETER_SIGNALEMENT_ID. */
  public static final String PARAMETER_SIGNALEMENT_ID = "signalement_id";
  public static final String PARAMETER_SELECTED_STATE = "selectedState";
  public static final String PARAMETER_SELECTED_PERIOD = "selectedPeriod";
  public static final String PARAMETER_DISPLAY = "display";
  public static final String PARAMETER_STATE_ID = "state_id";
  public static final String PARAMETER_UPDATE_DASHBOARD_USER_PREFERENCES = "updateUserDashboardPreferences";
  public static final String PARAMETER_ACTION = "action";
  
  // MARKERS
  private static final String MARK_ARRONDISSEMENT_LIST = "arrondissement_list";
  private static final String MARK_CATEGORY_LIST = "category_list";
  private static final String MARK_DASHBOARD_VISIBILE_UNITS = "visible_units";
  private static final String MARK_DASHBOARD_USER_UNIT_TREE = "user_unit_tree";
  private static final String MARK_DASHBOARD_PERIOD_CRITERIAS = "period_criterias";
  private static final String MARK_DISPLAYED_STATES_LIST = "displayed_states_list";
  private static final String MARK_COLLAPSED_STATES_LIST = "collapsed_states_list";
  
  private static final String MARK_FILTER = "filter";

  //PROPERTIES
  private static final String PROPERTY_ID_WORKFLOW_SIGNALEMENT = "signalement.idWorkflow";
  private static final String PROPERTY_DASHBOARD_STATES = "signalement.dashboard.states";
  private static final String PROPERTY_DASHBOARD_PLANNED = "signalement.dashboard.planned";
  private static final String PROPERTY_DASHBOARD_DEFAULT_STATES = "signalement.dashboard.default.displayed.states";
  private static final String PROPERTY_DASHBOARD_STATES_MISE_EN_SURVEILLANCE = "signalement.dashboard.states.miseSurveillance";
  
  // CONSTANTS
  private static final Integer ID_WORKFLOW_SIGNALEMENT = AppPropertiesService.getPropertyInt(PROPERTY_ID_WORKFLOW_SIGNALEMENT, -1);
  private static final String DASHBOARD_CATEGORY_OTHER = "other";
  private static final String DASHBOARD_CATEGORY_PLANNED = "planned";
  private static final String CRITERIA_SEPARATOR = " - ";
  private static final String ITEM_SEPARATOR = ", ";
  
  // SERVICES
  /** The _signalement service. */
  private ISignalementService _signalementService;

  /** The _type signalement service. */
  private ITypeSignalementService _typeSignalementService;

  /** The _unit service. */
  private IUnitService _unitService;

  /** The _signalement workflow service. */
  private IWorkflowService _signalementWorkflowService;

  /** The _arrondissement service. */
  private IArrondissementService _arrondissementService;

  /** The _signalementViewRole service */
  private SignalementViewRoleService _signalementViewRoleService;
  
  /** The _sector service. */
  private ISectorService _sectorService;
  
  /** The signalementUnitService */
  private ISignalementUnitService _signalementUnitService;
  
  /** The dashboardPeriodService */
  private IDashboardPeriodService _dashboardPeriodService;
  
  /** The dashboardUserPreferencesService **/
  private IDashboardUserPreferencesService _dashboardUserPreferencesService;

  //MEMBERS
  private Map<State,Map<DashboardPeriod,List<Integer>>> dashboard;
  private SignalementDashboardFilter dashboardFilter;
  
  @Override
  public void init(HttpServletRequest request, String strRight) throws AccessDeniedException {
    super.init(request, strRight);
    this.initServices();
  }

  /**
   * Inits the services.
   */
  public void initServices() {
    this._signalementService = (ISignalementService) SpringContextService
        .getBean("signalementService");
    this._typeSignalementService = (ITypeSignalementService) SpringContextService
        .getBean("typeSignalementService");
    this._unitService = (IUnitService) SpringContextService.getBean("unittree.unitService");
    this._signalementWorkflowService = (IWorkflowService) SpringContextService
        .getBean("signalement.workflowService");
    this._sectorService = (ISectorService) SpringContextService
        .getBean("unittree-dansmarue.sectorService");
    this._arrondissementService = (IArrondissementService) SpringContextService
        .getBean("signalement.arrondissementService");
    this._signalementViewRoleService = (SignalementViewRoleService) SpringContextService
        .getBean("signalement.signalementViewRoleService");
    this._signalementUnitService = (ISignalementUnitService) SpringContextService
    	.getBean( "signalementUnitService" );
    this._dashboardPeriodService = (IDashboardPeriodService) SpringContextService
    	.getBean("signalement.dashboardPeriodService");
    this._dashboardUserPreferencesService = (IDashboardUserPreferencesService) SpringContextService
    	.getBean("signalement.dashboardUserPreferencesService");
  }

  /**
   * Instantiates a new signalement jsp bean.
   */
  public SignalementDashboardJspBean() {
    super();
  }
  	
  public Map<State,Map<DashboardPeriod,List<Integer>>> getDashboard(){
	  if(this.dashboard == null){
		  dashboard = new LinkedHashMap<>();
	  }
	  return dashboard;
  }
  
  	/**
  	 * View for the signalement management dashboard
  	 * @param request
  	 */
  	public String doGetManageSignalementDashboard(HttpServletRequest request){
  	    AdminUser adminUser = AdminUserService.getAdminUser(request);	
  	   
  		Map<String,Object> model = new HashMap<>();
  		
  	    Map<String, AdminRole> userRoles = adminUser.getRoles();
		AdminRole observateurMairieRole = userRoles.get(SignalementConstants.ROLE_OBSERVATEUR_MAIRIES);
		boolean isObsMairie = (observateurMairieRole != null);
		 
		//Préférences utilisateur
  	    List<Integer> userDashboardStatesId = _dashboardUserPreferencesService.findUserDashboardStates(adminUser.getUserId());
		if(userDashboardStatesId.isEmpty()){
			String dashboardStates = AppPropertiesService.getProperty(PROPERTY_DASHBOARD_DEFAULT_STATES);
			if(StringUtils.isNotBlank(dashboardStates)){
				String[] dashboardStatesIdArr = dashboardStates.split(",");
				userDashboardStatesId = ListUtils.getListOfIntFromStrArray(dashboardStatesIdArr);
			}
		}
		
  		//Période
  		List<DashboardPeriod> dashboardPeriodCriterias = _dashboardPeriodService.getDashboardPeriodCriterias();
  		ReferenceList dashboardPeriodCriteriasList = ListUtils.toReferenceList(dashboardPeriodCriterias,
  				"id", "libelle", null);
  		model.put(MARK_DASHBOARD_PERIOD_CRITERIAS, dashboardPeriodCriteriasList);
  		
  		//Arrondissements
  		//Récupération des arrondissements restreints à l'utilisateur : 
  		List<Integer> idArrondissements = _signalementViewRoleService.getUserRestrictedArrondissementList(adminUser.getUserId());
  		List<Integer> listAllowedArrond = new ArrayList<>();
  		ReferenceList listeArrondissement = new ReferenceList();
  		
        boolean isChildOfDpeDeveDvdOther = false;

    	List<Unit> userUnits = _unitService.getUnitsByIdUser(adminUser.getUserId(), false);
    	
        //Détermination si l'utilisateur appartient à l'une des directions DPE, DEVE, DVD,...
        if(!isObsMairie){
        	if(CollectionUtils.isNotEmpty(userUnits)){
        		Unit temp = userUnits.get(0);
        		while (temp.getIdUnit() != 0 && !isChildOfDpeDeveDvdOther) {
        			if (temp.getIdParent() == 108 || temp.getIdParent() == 1 || temp.getIdParent() == 174
		              || temp.getIdParent() == 185) {
		            isChildOfDpeDeveDvdOther = true;
		          }
		
		          temp = _unitService.getUnit(temp.getIdParent(), true);
		        }
	
		        ////////////////////////////////
		        // the arrondissement list ///
		        ////////////////////////////////
        		Unit userUnit = userUnits.get(0);
		        if (isChildOfDpeDeveDvdOther) {
		          List<Integer> listIdAllowedSectors = _sectorService
		              .getIdsSectorByIdUnit(userUnit.getIdUnit());
		
		          for (Integer nArrId : _arrondissementService
		              .getArrondissementsInSector(listIdAllowedSectors)) {
		            if (!listAllowedArrond.contains(nArrId)) {
		            	listAllowedArrond.add(nArrId);
		            }
		          }
		        } else {
		          listeArrondissement = ListUtils.toReferenceList(
		              this._arrondissementService.getAllArrondissement(), "id", "numero",
		              null);
		        }
	    	}

	  	    // If we didn't added every arrondissement we add the selected ones
	  	    if (listeArrondissement.isEmpty()) {
	  	      List<Arrondissement> listAllowedArrondissements = new ArrayList<Arrondissement>();
	  	      for (Integer nArrId : listAllowedArrond) {
	  	        listAllowedArrondissements.add(_arrondissementService.getByIdArrondissement(nArrId));
	  	      }
	  	      Collections.sort(listAllowedArrondissements);
	  	      listeArrondissement.addAll(
	  	          ListUtils.toReferenceList(listAllowedArrondissements, "id", "numero", null));
	  	    }
	  	    
	  	  listeArrondissement = ListUtils.retainReferenceList(listeArrondissement, idArrondissements, false);
  		
  		}else{
  			 List<Arrondissement> listAllowedArrondissements = new ArrayList<Arrondissement>();
	  	      for (Integer nArrId : idArrondissements) {
	  	        listAllowedArrondissements.add(_arrondissementService.getByIdArrondissement(nArrId));
	  	      }
	  	      Collections.sort(listAllowedArrondissements);
	  	      listeArrondissement.addAll(
	  	          ListUtils.toReferenceList(listAllowedArrondissements, "id", "numero", null));
  		}
        
        model.put(MARK_ARRONDISSEMENT_LIST, listeArrondissement);
        
  		//Liste des catégories
  	    List<Integer> userAllowedCategories = _signalementViewRoleService.getUserRestrictedCategorySignalementList(adminUser.getUserId()
  	    		, request);
  	    
  		List<TypeSignalement> categories = _typeSignalementService.getAllTypeSignalementActifWithoutParent();
  		ReferenceList categoriesList = ListUtils.toReferenceList(categories, "id", "libelle",
  		        null, false);
  		categoriesList = ListUtils.retainReferenceList(categoriesList, userAllowedCategories, false);
  		model.put(MARK_CATEGORY_LIST, categoriesList);
  		
  		//Entités - A afficher si profil != observateur mairie
  		if(!isObsMairie){
  			//1 seule entité par utilisateur
  			if(CollectionUtils.isNotEmpty(userUnits)){
  				Unit unit = userUnits.get(0);
  				//Construction de l'arbre à partir de cette entité
  				UnitNode unitNode = new UnitNode(unit);
  				UnitUtils.buildTree(unitNode);
  				model.put(MARK_DASHBOARD_USER_UNIT_TREE, unitNode);
  			}
  		}
  		

		List<Integer> visibleUnitsIds = _signalementUnitService.getVisibleUnitsIds();
		model.put(MARK_DASHBOARD_VISIBILE_UNITS, visibleUnitsIds);
  		
		String action = request.getParameter(PARAMETER_ACTION);
		
		if(StringUtils.isBlank(action) || !action.equals(PARAMETER_UPDATE_DASHBOARD_USER_PREFERENCES)){
	  		//Récupération des paramètres de filtrage
	  		dashboardFilter = new SignalementDashboardFilter();
	  		populate(dashboardFilter,request);  	
	  		
	  		//Recherche des anomalies
	  		Integer idUnit = dashboardFilter.getUnitId();
	  		Integer userIdUnit = userUnits.get(0).getIdUnit();
	  		
	  		if(idUnit == null){
	  			dashboardFilter.setUnitId(userIdUnit);
	  			idUnit = dashboardFilter.getUnitId();
	  		}
	  		
	  		if(!visibleUnitsIds.contains(idUnit)){
	  			dashboardFilter.setUnitId(userIdUnit);
	  			idUnit = dashboardFilter.getUnitId();
	  		}
	  		
	  		if(dashboardFilter.getDepth() == null){
	  			dashboardFilter.setDepth(0);
	  		}
	  		
	  		
	  		//Si idUnit renseigné, on vérifie qu'elle est autorisée pour le user.
	  		if(idUnit != userIdUnit){
	  			boolean foundValue = false;
	  			Unit unit = _unitService.getUnit(idUnit, false);
	  			while(!foundValue && userIdUnit != unit.getIdUnit()){
	  				if(unit.getIdUnit() == idUnit){
	  					foundValue = true;
	  				}else{
	  					unit = _unitService.getUnit(unit.getIdParent(), false);
	  				}
	  			}
	  			if(!foundValue){
	  				dashboardFilter.setUnitId(userIdUnit);
	  			}
	  		}
	  		
	  		if(dashboardFilter.getPeriodId() == null){
	  			dashboardFilter.setPeriodId(dashboardPeriodCriterias.get(0).getId().intValue());
	  		}
	  		
	  		
	  		boolean wasArrondissementEmpty = false;
	  		if(ArrayUtils.isEmpty(dashboardFilter.getArrondissementIds())){
	  			Integer[] userArrondissements = new Integer[listeArrondissement.size()];
	  			int nIndex = 0;
	  			for(ReferenceItem arrondissement : listeArrondissement){
	  				userArrondissements[nIndex++] = Integer.parseInt(arrondissement.getCode());
	  			}
	  			dashboardFilter.setArrondissementIds(userArrondissements);
	  			wasArrondissementEmpty= true;
	  		}
	  		
	  		//Récupération des signalements correspondants
	  		List<DashboardSignalementDTO> dashboardSignalementDTOList = _signalementService.findByDashboardFilter(dashboardFilter);
	  		
	  		if(wasArrondissementEmpty){
	  			dashboardFilter.setArrondissementIds(null);
	  		}
	  		
	  		List<DashboardPeriod> periods = _dashboardPeriodService.getAllDashboardPeriods();
	  		
	  		
	  		//Periods
	  		List<DashboardPeriod> otherPeriods = new ArrayList<>();
	  		List<DashboardPeriod> plannedPeriods = new ArrayList<>();
	  		
	  		for(DashboardPeriod period:periods){
	  			if(StringUtils.equals(period.getCategory(),DASHBOARD_CATEGORY_OTHER)){
	  				otherPeriods.add(period);
	  			}
	  			if(StringUtils.equals(period.getCategory(),DASHBOARD_CATEGORY_PLANNED)){
	  				plannedPeriods.add(period);
	  			}
	  		}
	  		
	  		
	  		List<State> stateList = (List<State>) fr.paris.lutece.portal.service.workflow.WorkflowService.getInstance().getAllStateByWorkflow(ID_WORKFLOW_SIGNALEMENT, adminUser);
	  		
	  		// Etat -> Période (Répartition) -> Id des anomalies
	  		dashboard = new LinkedHashMap<>();
	  		String strDashboardStates = AppPropertiesService.getProperty(PROPERTY_DASHBOARD_STATES);
	  		String strPlannedStates = AppPropertiesService.getProperty(PROPERTY_DASHBOARD_PLANNED);
	  		
	  		String[] dashboardStatesArr = strDashboardStates.split(",");
	  		String[] plannedStatesArr = strPlannedStates.split(",");
	  		
	  		String strIdStateMiseEnSurveillance = AppPropertiesService.getProperty(PROPERTY_DASHBOARD_STATES_MISE_EN_SURVEILLANCE);
	  		
	  		for(State state:stateList){
	  			Integer stateId = state.getId();
	  			if(ArrayUtils.contains(dashboardStatesArr, Integer.toString(stateId))){
	  				Map<DashboardPeriod,List<Integer>> mapPeriodAnomalies = new LinkedHashMap<>();
	  				dashboard.put(state, mapPeriodAnomalies);
	  				
	  				if(!ArrayUtils.contains(plannedStatesArr, Integer.toString(stateId))){
	  					for(DashboardPeriod period : otherPeriods){
	  						List<Integer> anomaliesIds = new ArrayList<>();
	  						
	  						Integer lowerBound = period.getLowerBound();
	  						Integer higherBound = period.getHigherBound();
	  						ChronoUnit unit = ChronoUnit.valueOf(period.getUnit());
	  						
	  						LocalDate dateLowerBound = null;
	  						if(lowerBound != null){
	  							dateLowerBound = LocalDate.now().plus(new Long(lowerBound),unit);
	  						}
	  						LocalDate dateHigherBound = null;
	  						if(null != higherBound){
	  							dateHigherBound = LocalDate.now().plus(new Long(higherBound),unit);
	  						}
	  						
	  						
	  						
	  						Iterator<DashboardSignalementDTO> iterator = dashboardSignalementDTOList.iterator();
	  						while(iterator.hasNext()){
	  							DashboardSignalementDTO dsd = iterator.next();
	  							if(dsd.getIdStatus() == stateId){
	  								//Comparing dates
	  								LocalDate creationDate = dsd.getCreationDate();
	  								if(stateId == Integer.parseInt(strIdStateMiseEnSurveillance.trim( ))) {
	  								   //Mise en surveillance
	  								  creationDate = dsd.getDateMiseEnSurveillance( );
	  								}
	  								if(null != dateLowerBound && null != dateHigherBound 
	  										&& !creationDate.isBefore(dateLowerBound) && !creationDate.isAfter(dateHigherBound)){
	  									anomaliesIds.add(dsd.getIdSignalement());
	  									iterator.remove();
	  								}
	  								else if(null != dateLowerBound && null == dateHigherBound &&
	  										!creationDate.isBefore(dateLowerBound)){
	  									anomaliesIds.add(dsd.getIdSignalement());
	  									iterator.remove();
	  								}
	  								else if (null != dateHigherBound && null == dateLowerBound
	  										&& !creationDate.isAfter(dateHigherBound)){
	  									anomaliesIds.add(dsd.getIdSignalement());
	  									iterator.remove();
	  								}
	  							}
	  						}
	  						mapPeriodAnomalies.put(period, anomaliesIds);
	  					}
	  				}else{
	  					for(DashboardPeriod period : plannedPeriods){
	  						
	  						List<Integer> anomaliesIds = new ArrayList<>();
	  						
	  						Integer lowerBound = period.getLowerBound();
	  						Integer higherBound = period.getHigherBound();
	  						ChronoUnit unit = ChronoUnit.valueOf(period.getUnit());
	  						LocalDate now = LocalDate.now();
	  						Iterator<DashboardSignalementDTO> iterator = dashboardSignalementDTOList.iterator();
	  						while(iterator.hasNext()){
	  							DashboardSignalementDTO dsd = iterator.next();
	  							if(dsd.getDatePrevueTraitement() == null){
	  								continue;
	  							}
	  	  						LocalDate dateLowerBound = null;
	  	  						if(null != lowerBound){
	  	  							dateLowerBound = now.plus(new Long(lowerBound),unit);
	  	  						}
	  	  						LocalDate dateHigherBound = null;
	  	  						if(null != higherBound){
	  	  							dateHigherBound = now.plus(new Long(higherBound),unit);
	  	  						}
	  							if(dsd.getIdStatus() == stateId){
	  								//Comparing dates
	  								if(null != dateLowerBound && null != dateHigherBound 
	  										&& !dsd.getDatePrevueTraitement().isBefore(dateLowerBound) && !dsd.getDatePrevueTraitement().isAfter(dateHigherBound)){
	  									anomaliesIds.add(dsd.getIdSignalement());
	  									iterator.remove();
	  								}
	  								else if(null != dateLowerBound && null == dateHigherBound &&
	  										!dsd.getDatePrevueTraitement().isBefore(dateLowerBound)){
	  									anomaliesIds.add(dsd.getIdSignalement());
	  									iterator.remove();
	  								}
	  								else if (null != dateHigherBound && null == dateLowerBound
	  										&& !dsd.getDatePrevueTraitement().isAfter(dateHigherBound)){
	  									anomaliesIds.add(dsd.getIdSignalement());
	  									iterator.remove();
	  								}
	  							}
	  						}
	  						mapPeriodAnomalies.put(period, anomaliesIds);
	  					}
	  				}
	  			}
	  		}
		}

  		
  		//Parcours des states et mise dans les listes correspondantes
  		List<Entry<State,Map<DashboardPeriod,List<Integer>>>> listDisplayedStates = new ArrayList<>();
  		List<Entry<State,Map<DashboardPeriod,List<Integer>>>> listCollapsedStates = new ArrayList<>();
  		
  		for(Entry<State, Map<DashboardPeriod, List<Integer>>> dashboardEntry:dashboard.entrySet()){
  			/*State state = dashboardEntry.getKey();
  			if(userDashboardStatesId.contains(state.getId())){*/
  				listDisplayedStates.add(dashboardEntry);
  			/*}else{
  				listCollapsedStates.add(dashboardEntry);
  			}*/
  		}
  		
  		model.put(MARK_COLLAPSED_STATES_LIST, listCollapsedStates);
  		model.put(MARK_DISPLAYED_STATES_LIST, listDisplayedStates);
  		
  		model.put(MARK_FILTER, dashboardFilter);
  		
  		return this.getAdminPage(this.getTemplate(TEMPLATE_MANAGE_SIGNALEMENT_DASHBOARD, model));
  	}
  	
  	/**
  	 * Redirects to manage signalement page, with signalements list
  	 * @param request
  	 * @throws IOException 
  	 * @throws AccessDeniedException 
  	 */
  	public void redirectToManageSignalement(HttpServletRequest request) throws IOException, AccessDeniedException{
  		Integer selectedState = Integer.parseInt(request.getParameter(PARAMETER_SELECTED_STATE));
  		Integer selectedPeriod = Integer.parseInt(request.getParameter(PARAMETER_SELECTED_PERIOD));
  		List<Integer> signalementsIds = null;
  		
  		List<Arrondissement> arrondissements = _arrondissementService.getAllArrondissement();
  		
  		State state = null;
  		DashboardPeriod period = null;
  		
  		for(Entry<State, Map<DashboardPeriod, List<Integer>>> entry:this.dashboard.entrySet()){
  			if(entry.getKey().getId() == selectedState){
  				state = entry.getKey();
  				for(Entry<DashboardPeriod, List<Integer>> periodEntry:entry.getValue().entrySet()){
  					if(periodEntry.getKey().getId().intValue() == selectedPeriod){
  						period = periodEntry.getKey();
  						signalementsIds = periodEntry.getValue();
  					}
  				}
  			}
  		}

  		String periodFilter = dashboardFilter.getDashboardPeriod().getLibelle();
  		Integer[] arrondissementIds = dashboardFilter.getArrondissementIds();
  		Integer[] categoryIds = dashboardFilter.getCategoryIds();
  		
  		Map<String,List<String>> criterias = new LinkedHashMap<>();
  		
  		// Période
  		List<String> items = new ArrayList<>();
  		items.add(periodFilter);
  		criterias.put("#i18n{dansmarue.dashboard.criterias.period}",items);
  		
  		// Entité
  		if(dashboardFilter.getUnitId() != null){
  			items = new ArrayList<>();
  	  		Unit unit = _unitService.getUnit(dashboardFilter.getUnitId(),false);
  	  		items.add(unit.getLabel());
  	  		criterias.put("#i18n{dansmarue.dashboard.criterias.unit}",items);
  		}
  		
  		//Arrondissements
  		if(!ArrayUtils.isEmpty(arrondissementIds)){
  			items = new ArrayList<>();
  			for(Integer arrondissementId:arrondissementIds){
  	  			for(Arrondissement arrondissement : arrondissements){
  	  				if(arrondissementId == arrondissement.getId().intValue()){
  	  					items.add(arrondissement.getNumero());
  	  				}
  	  			}
  	  		}
  	  		criterias.put("#i18n{dansmarue.dashboard.criterias.arrondissements}",items);
  		}
  		
  		
  		//Catégories
  		if(!ArrayUtils.isEmpty(categoryIds)){
  			items = new ArrayList<>();
  			for(Integer categoryId :categoryIds){
  	  			TypeSignalement typeSig = _typeSignalementService.getByIdTypeSignalement(categoryId);
  	  			items.add(typeSig.getLibelle());  	  			
  	  		}
  	  		criterias.put("#i18n{dansmarue.dashboard.criterias.categories}",items);
  		}
  		
  		//Statut
		items = new ArrayList<>();
		items.add(state.getName());
	  	criterias.put("#i18n{dansmarue.dashboard.criterias.status}",items);
  		
  		//Période
	  	items = new ArrayList<>();
		items.add(period.getLibelle());
	  	criterias.put("#i18n{dansmarue.dashboard.criterias.repartition}",items);
  		
  		SignalementJspBean signalement = (SignalementJspBean)request.getSession().getAttribute(SESSION_ATTRIBUTE_SIGNALEMENT);
  		if(null == signalement){
  			signalement = new SignalementJspBean();
  			signalement.init( request,  "SIGNALEMENT_MANAGEMENT", SignalementResourceIdService.KEY_ID_RESOURCE, SignalementResourceIdService.PERMISSION_RECHERCHER_SIGNALEMENT );
  			request.getSession().setAttribute(SESSION_ATTRIBUTE_SIGNALEMENT,signalement);
  		}
  		
  		signalement.setDashboardCriterias(criterias);
		signalement.setDashboardSignalementList(signalementsIds);
		signalement.reinitPaginatorCurrentPageIndex();
  	}
  	
  	/**
  	 * Updates the user dashboard preferences (if the state must be collapsed or not)
  	 * @param request the http request
  	 * @return the template content
  	 */
  	public String doUpdateUserDashboardPreferences(HttpServletRequest request){
  		String display = request.getParameter(PARAMETER_DISPLAY);
  		String idState = request.getParameter(PARAMETER_STATE_ID);
  		
  		AdminUser user = getUser();
  		List<Integer> userDisplayedStates = _dashboardUserPreferencesService.findUserDashboardStates(user.getUserId());
  		
  		//Si l'utilisateur n'a jamais changé ses préférences, sauvegarder ceux par défaut
  		if(userDisplayedStates.isEmpty()){
  			String dashboardStates = AppPropertiesService.getProperty(PROPERTY_DASHBOARD_DEFAULT_STATES);
  	  		List<Integer> defaultDisplayedStates = null;
  			if(StringUtils.isNotBlank(dashboardStates)){
  				String[] dashboardStatesIdArr = dashboardStates.split(",");
  				defaultDisplayedStates = ListUtils.getListOfIntFromStrArray(dashboardStatesIdArr);
  			}
  			if(defaultDisplayedStates != null){
  				for(Integer displayedStateId :defaultDisplayedStates ){
  					_dashboardUserPreferencesService.insert(user.getUserId(), displayedStateId);
  				}
  			}
  		}
  		
  		//Mise à jour des préférences
  		if(BooleanUtils.toBoolean(display)){
  			_dashboardUserPreferencesService.insert(user.getUserId(), Integer.parseInt(idState));
  		}else{
  			_dashboardUserPreferencesService.remove(user.getUserId(), Integer.parseInt(idState));
  		}
  		
  		return doGetManageSignalementDashboard(request);
  	}
  	
}