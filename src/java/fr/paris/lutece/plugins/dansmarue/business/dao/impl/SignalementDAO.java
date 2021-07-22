/*
 * Copyright (c) 2002-2021, City of Paris
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
package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import static fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants.PERIODE_MAP;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.ISignalementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.DashboardPeriod;
import fr.paris.lutece.plugins.dansmarue.business.entities.EtatSignalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Priorite;
import fr.paris.lutece.plugins.dansmarue.business.entities.ServiceFaitMasseFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementDashboardFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementRequalification;
import fr.paris.lutece.plugins.dansmarue.business.entities.TableauDeBordFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.commons.Order;
import fr.paris.lutece.plugins.dansmarue.commons.dao.PaginationProperties;
import fr.paris.lutece.plugins.dansmarue.service.dto.DashboardSignalementDTO;
import fr.paris.lutece.plugins.dansmarue.service.dto.DossierSignalementDTO;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.DateUtils;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.business.sector.Sector;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * The Class SignalementDAO.
 */
public class SignalementDAO implements ISignalementDAO
{

    /** The Constant SEJ_ID. */
    private static final int SEJ_ID = 94;

    /** The Constant SQL_QUERY_AND. */
    private static final String SQL_QUERY_AND = " AND ";

    /** The Constant SQL_QUERY_INNER_JOIN. */
    private static final String SQL_QUERY_INNER_JOIN = " INNER JOIN ";

    /** The Constant SIGNALEUR_MAIL. */
    private static final String SIGNALEUR_MAIL = "signaleur.mail";

    /** The Constant ADRESSE. */
    private static final String ADRESSE = "adr.adresse";

    /** The Constant NUM_SIGNALEMENT. */
    private static final String NUM_SIGNALEMENT = "numSignalement";

    /** The Constant SQL_QUERY_NEW_PK. */
    private static final String SQL_QUERY_NEW_PK = "SELECT nextval('seq_signalement_signalement_id_signalement')";

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_signalement (id_signalement, suivi, date_creation, commentaire, annee, mois, numero, PREFIX, fk_id_priorite, fk_id_type_signalement, fk_id_arrondissement, fk_id_sector, is_doublon, token, commentaire_agent_terrain) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /** The Constant SQL_QUERY_DELETE. */
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_signalement WHERE id_signalement = ?";

    /** The Constant SQL_QUERY_SELECT. */
    private static final String SQL_QUERY_SELECT = "SELECT id_signalement, suivi, date_creation, date_prevue_traitement, commentaire, annee, mois, numero, prefix, fk_id_priorite, fk_id_arrondissement, fk_id_type_signalement, fk_id_sector, is_doublon, token, service_fait_date_passage, felicitations, date_mise_surveillance, date_rejet, courriel_destinataire, courriel_expediteur, courriel_date, is_send_ws, commentaire_agent_terrain FROM signalement_signalement WHERE id_signalement = ?";

    /** The Constant SQL_QUERY_SELECT_BY_NUMBER. */
    private static final String SQL_QUERY_SELECT_BY_NUMBER = "SELECT id_signalement, suivi, date_creation, date_prevue_traitement, commentaire, annee, mois, numero, prefix, fk_id_priorite, fk_id_arrondissement, fk_id_type_signalement, fk_id_sector, is_doublon, token, service_fait_date_passage, felicitations, date_mise_surveillance, date_rejet, courriel_destinataire, courriel_expediteur, courriel_date, is_send_ws, commentaire_agent_terrain FROM signalement_signalement WHERE prefix || annee || mois || numero = ?";

    /** The Constant SQL_QUERY_SELECT_BY_STATUS. */
    private static final String SQL_QUERY_SELECT_BY_STATUS = "SELECT signalement.id_signalement, signalement.suivi, signalement.date_creation, signalement.date_prevue_traitement, signalement.commentaire, signalement.annee,  signalement.mois, signalement.numero, signalement.prefix, signalement.fk_id_priorite,   signalement.fk_id_arrondissement,  signalement.fk_id_type_signalement,  signalement.fk_id_sector,   signalement.is_doublon, signalement.service_fait_date_passage FROM signalement_signalement AS signalement  INNER JOIN workflow_resource_workflow AS resource_workflow ON resource_workflow.id_resource = signalement.id_signalement  INNER JOIN workflow_resource_history AS resource_history ON resource_history.id_resource = signalement.id_signalement INNER JOIN workflow_action AS action ON action.id_action = resource_history.id_action WHERE resource_workflow.resource_type = ''SIGNALEMENT_SIGNALEMENT''  AND resource_history.resource_type = ''SIGNALEMENT_SIGNALEMENT''  AND resource_workflow.id_state = ? AND action.id_state_after = ? AND resource_history.creation_date + '''{0} ''days''::interval < now();";
    /** The Constant SQL_QUERY_UPDATE. */
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_signalement SET id_signalement=?, suivi=?, date_creation=?, date_prevue_traitement=?, commentaire=? , fk_id_priorite=?, fk_id_type_signalement=?, fk_id_arrondissement = ?, fk_id_sector = ?, is_doublon = ?, service_fait_date_passage = ?, courriel_destinataire = ?, courriel_expediteur = ?, courriel_date = ?, is_send_ws = ?, commentaire_agent_terrain=? WHERE id_signalement=?";

    /** The Constant SQL_QUERY_SELECT_ALL. */
    private static final String SQL_QUERY_SELECT_ALL = "SELECT id_signalement, suivi, date_creation, date_prevue_traitement, commentaire, annee, mois, numero, prefix, fk_id_priorite, fk_id_arrondissement, fk_id_type_signalement, fk_id_sector, is_doublon, is_send_ws FROM signalement_signalement";

    /** The Constant SQL_QUERY_ADD_FILTER_SECTOR_ALLOWED. */
    private static final String SQL_QUERY_ADD_FILTER_SECTOR_ALLOWED = " fk_id_sector IN (SELECT s.id_sector FROM unittree_sector s INNER JOIN unittree_unit_sector u ON s.id_sector = u.id_sector WHERE u.id_unit in ({0}))";

    /** The Constant SQL_QUERY_SELECT_SIGNALEMENT_BY_TOKEN. */
    private static final String SQL_QUERY_SELECT_SIGNALEMENT_BY_TOKEN = "SELECT id_signalement, token FROM signalement_signalement WHERE token = ?";

    /** SQL QUERY FOR WebServicePartnerDaemon. */
    private static final String SQL_QUERY_SELECT_ID_SIGNALEMENT_FOR_PARTNER_DEAMON = "SELECT DISTINCT id_signalement FROM signalement_signalement sis, workflow_resource_workflow wrw, workflow_resource_history wrh WHERE sis.id_signalement = wrw.id_resource AND wrw.id_state = ? AND sis.id_signalement = wrh.id_resource AND wrh.creation_date > current_date - integer 'NB_DAYS' ORDER BY sis.id_signalement DESC";

    // FOR THE FILTERS
    /** The Constant SQL_QUERY_PART_SELECT. */
    private static final String SQL_QUERY_PART_SELECT = " signalement.id_signalement, priorite.libelle, signalement.fk_id_type_signalement ";

    /** The Constant SQL_QUERY_FROM_ALL. */
    private static final String SQL_QUERY_FROM_ALL = " FROM signalement_signalement AS signalement INNER JOIN workflow_resource_workflow AS workflow ON workflow.id_resource = signalement.id_signalement INNER JOIN unittree_unit_sector unit_sector ON unit_sector.id_sector = signalement.fk_id_sector INNER JOIN unittree_unit direction_unit ON direction_unit.id_unit = unit_sector.id_unit AND direction_unit.id_parent = 0 ";

    /** The Constant SQL_QUERY_FROM_SIGNALEMENT_TYPE. */
    private static final String SQL_QUERY_FROM_SIGNALEMENT_TYPE = " INNER JOIN signalement_type_signalement AS type ON type.id_type_signalement = signalement.fk_id_type_signalement ";

    /** The Constant SQL_QUERY_FROM_VIEW_TYPE_SIGNALEMENT_W_PARENT_LINK. */
    private static final String SQL_QUERY_FROM_VIEW_TYPE_SIGNALEMENT_W_PARENT_LINK = " INNER JOIN v_signalement_type_signalement_with_parents_links AS vstswpl ON vstswpl.id_type_signalement= signalement.fk_id_type_signalement ";

    /** The Constant SQL_QUERY_FROM_SIGNALEMENT_CATEGORY. */
    private static final String SQL_QUERY_FROM_SIGNALEMENT_CATEGORY = " INNER JOIN v_signalement_type_signalement_with_parents_links AS vstswp ON vstswp.id_type_signalement = signalement.fk_id_type_signalement AND vstswp.is_parent_a_category=1";

    /** The Constant SQL_QUERY_FROM_UNITTREE. */
    private static final String SQL_QUERY_FROM_UNITTREE = " INNER JOIN unittree_unit_sector uus ON signalement.fk_id_sector = uus.id_sector ";

    /** The Constant SQL_QUERY_FROM_SIGNALEUR. */
    private static final String SQL_QUERY_FROM_SIGNALEUR = " LEFT OUTER JOIN signalement_signaleur AS signaleur ON signalement.id_signalement = signaleur.fk_id_signalement ";

    /** The Constant SQL_QUERY_FROM_PHOTO. */
    private static final String SQL_QUERY_FROM_PHOTO = " LEFT OUTER JOIN signalement_photo AS photo ON signalement.id_signalement = photo.fk_id_signalement ";

    /** The Constant SQL_QUERY_FROM_ADRESS. */
    private static final String SQL_QUERY_FROM_ADRESS = " INNER JOIN signalement_adresse as adr on signalement.id_signalement = adr.fk_id_signalement ";

    /** The Constant SQL_QUERY_FROM_PRORITE. */
    private static final String SQL_QUERY_FROM_PRORITE = " INNER JOIN signalement_priorite as priorite on priorite.id_priorite = signalement.fk_id_priorite  ";

    /** The Constant SQL_QUERY_ADD_FILTER_LIST_TYPE_SIGNALEMENT. */
    private static final String SQL_QUERY_ADD_FILTER_LIST_TYPE_SIGNALEMENT = " signalement.fk_id_type_signalement IN ({0}) ";

    /** The Constant SQL_QUERY_ADD_FILTER_TYPE_SIGNALEMENT. */
    private static final String SQL_QUERY_ADD_FILTER_TYPE_SIGNALEMENT = " vstswpl.id_parent = ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_NUMERO. */
    private static final String SQL_QUERY_ADD_FILTER_NUMERO = " CASE " + " WHEN (RIGHT(?, 1) = '*')  " + " THEN CASE "
            + " WHEN (length(?) = 2) THEN signalement.prefix LIKE REPLACE (?, '*', '%') "
            + " WHEN (length(?) = 6) THEN signalement.prefix || signalement.annee LIKE REPLACE (?, '*', '%') "
            + " WHEN (length(?) = 7) THEN signalement.prefix || signalement.annee || signalement.mois LIKE REPLACE (?, '*', '%') "
            + " WHEN (length(?) > 7) THEN signalement.prefix || signalement.annee || signalement.mois || signalement.numero LIKE REPLACE (?, '*', '%') "
            + " end " + " ELSE signalement.prefix || signalement.annee || signalement.mois || signalement.numero like ? " + " END ";

    /** The Constant SQL_QUERY_ADD_FILTER_DIRECTION. */
    private static final String SQL_QUERY_ADD_FILTER_DIRECTION = " uus.id_unit = ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_LIST_ARRONDISSEMENT. */
    private static final String SQL_QUERY_ADD_FILTER_LIST_ARRONDISSEMENT = " signalement.fk_id_arrondissement IN ({0}) ";

    /** The Constant SQL_QUERY_ADD_FILTER_LIST_QUARTIER. */
    private static final String SQL_QUERY_ADD_FILTER_LIST_QUARTIER = " ST_Intersects(adr.geom::geometry, (select ST_Union(geom) from signalement_conseil_quartier  where id_consqrt in ({0}))::geometry) ";

    /** The Constant SQL_QUERY_ADD_FILTER_ARRONDISSEMENT. */
    private static final String SQL_QUERY_ADD_FILTER_ARRONDISSEMENT = " signalement.fk_id_arrondissement = ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_SECTOR. */
    private static final String SQL_QUERY_ADD_FILTER_SECTOR = " signalement.fk_id_sector = ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_ETAT. */
    private static final String SQL_QUERY_ADD_FILTER_ETAT = " workflow.id_state = ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_ADRESSE. */
    private static final String SQL_QUERY_ADD_FILTER_ADRESSE = "lower_unaccent(adr.adresse) LIKE lower_unaccent(?) ";

    /** The Constant SQL_QUERY_ADD_FILTER_MAIL. */
    private static final String SQL_QUERY_ADD_FILTER_MAIL = "lower_unaccent(signaleur.mail) LIKE lower_unaccent(?) ";

    /** The Constant SQL_QUERY_ADD_FILTER_COMMENTAIRE. */
    private static final String SQL_QUERY_ADD_FILTER_COMMENTAIRE = " lower_unaccent(signalement.commentaire) LIKE lower_unaccent(?) ";

    /** The Constant SQL_QUERY_ADD_FILTER_COMMENTAIRE_AGENT_TERRAIN. */
    private static final String SQL_QUERY_ADD_FILTER_COMMENTAIRE_AGENT_TERRAIN = " lower_unaccent(signalement.commentaire_agent_terrain) LIKE lower_unaccent(?) ";

    /** The Constant SQL_QUERY_ADD_FILTER_DATE_BEGIN. */
    private static final String SQL_QUERY_ADD_FILTER_DATE_BEGIN = "date_trunc('day', signalement.date_creation) >= ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_DATE_END. */
    private static final String SQL_QUERY_ADD_FILTER_DATE_END = "date_trunc('day', signalement.date_creation) <= ? ";

    /** The Constant SQL_QUERY_ADD_FILTER_CATEGORY. */
    private static final String SQL_QUERY_ADD_FILTER_CATEGORY = " vstswp.id_parent IN ({0}) ";

    /** The Constant SQL_QUERY_SELECT_ALL_ID_SIGNALEMENT. */
    private static final String SQL_QUERY_SELECT_ALL_ID_SIGNALEMENT = "select id_signalement from signalement_signalement";

    /** The Constant SQL_UPDATE_SUIVI_PLUS_ONE. */
    private static final String SQL_UPDATE_SUIVI_PLUS_ONE = "UPDATE signalement_signalement SET suivi=suivi+1 WHERE id_signalement=?";

    /** The Constant SQL_UPDATE_SUIVI_PLUS_ONE. */
    private static final String SQL_UPDATE_SUIVI_MINUS_ONE = "UPDATE signalement_signalement SET suivi=suivi-1 WHERE id_signalement=?";

    /** The Constant SQL_QUERY_FELICITATIONS_INCREMENT *. */
    private static final String SQL_QUERY_FELICITATIONS_INCREMENT = "UPDATE signalement_signalement SET felicitations=felicitations+1 WHERE id_signalement=?";

    /** The Constant SQL_QUERY_UPDATE_COMMENT_AGENT *. */
    private static final String SQL_QUERY_UPDATE_COMMENT_AGENT = "UPDATE signalement_signalement SET commentaire_agent_terrain=? WHERE id_signalement=?";

    /** The Constant SQL_SELECT_BY_ID_TELEPHONE. */
    private static final String SQL_QUERY_SELECT_BY_ID_TELEPHONE = "SELECT fk_id_signalement FROM signalement_signaleur WHERE id_telephone=?";

    /** The Constant SQL_QUERY_INSERT_MESSAGE_CREATION. */
    private static final String SQL_QUERY_INSERT_MESSAGE_CREATION = "INSERT INTO signalement_message_creation VALUES (1,?)";

    /** The Constant SQL_QUERY_UPDATE_MESSAGE_CREATION. */
    private static final String SQL_QUERY_UPDATE_MESSAGE_CREATION = "UPDATE signalement_message_creation SET contenu=? WHERE id_message=1";

    /** The Constant SQL_QUERY_SELECT_MESSAGE_CREATION. */
    private static final String SQL_QUERY_SELECT_MESSAGE_CREATION = "SELECT contenu FROM signalement_message_creation WHERE id_message=1";

    /** The Constant SQL_QUERY_DELETE_MESSAGE_CREATION. */
    private static final String SQL_QUERY_DELETE_MESSAGE_CREATION = "DELETE FROM signalement_message_creation WHERE id_message=1";

    /** The Constant SQL_QUERY_UPDATE_DATE_MISE_SURVEILLANCE. */
    private static final String SQL_QUERY_UPDATE_DATE_MISE_SURVEILLANCE = "UPDATE signalement_signalement SET date_mise_surveillance =? WHERE id_signalement=?";

    /** The Constant SQL_QUERY_UPDATE_DATE_REJET. */
    private static final String SQL_QUERY_UPDATE_DATE_REJET = "UPDATE signalement_signalement SET date_rejet =? WHERE id_signalement=?";

    /** The Constant SQL_WHERE. */
    private static final String SQL_WHERE = " WHERE ";

    /** The Constant SQL_AND. */
    private static final String SQL_AND = SQL_QUERY_AND;

    /** The Constant SQL_OR. */
    private static final String SQL_OR = " OR ";

    /** The Constant SQL_GROUP_BY. */
    private static final String SQL_GROUP_BY = " GROUP BY ";

    /** The Constant SQL_SELECT. */
    private static final String SQL_SELECT = "SELECT ";

    /** The Constant SQL_SELECT_COUNT_ID_SIGNALEMENT. */
    private static final String SQL_SELECT_COUNT_ID_SIGNALEMENT = "SELECT COUNT (DISTINCT signalement.id_signalement) ";

    /** The Constant SQL_SELECT_ID_SIGNALEMENT. */
    private static final String SQL_SELECT_ID_SIGNALEMENT = "SELECT DISTINCT signalement.id_signalement";

    /** The Constant COMA_SEPARATOR. */
    private static final String COMA_SEPARATOR = ", ";

    /** The Constant DISTINCT. */
    private static final String SQL_DISTINCT = " DISTINCT ";

    /** The Constant SQL_QUERY_ID_MAIL_BY_ID. */
    private static final String SQL_QUERY_ID_MAIL_BY_ID = "select value.id_message, history.id_resource from signalement_workflow_notifuser_multi_contents_value value "
            + "inner join workflow_resource_history history on history.id_history = value.id_history " + "where history.id_resource=? "
            + "and value.id_history = (select max(h.id_history) from signalement_workflow_notifuser_multi_contents_value v "
            + "inner join workflow_resource_history h on h.id_history = v.id_history " + "where h.id_resource= ?)";

    /** The Constant SQL_QUERY_INSERT_REQUALIFICATION. */
    private static final String SQL_QUERY_INSERT_REQUALIFICATION = "INSERT INTO signalement_requalification(id_signalement,id_type_signalement,adresse,id_sector,date_requalification,id_task,commentaire_agent_terrain) VALUES (?,?,?,?,?,?,?)";

    /** The Constant SQL_QUERY_SELECT_REQUALIFICATION. */
    private static final String SQL_QUERY_SELECT_REQUALIFICATION = "SELECT * from signalement_requalification where id_signalement = ? ORDER BY date_requalification DESC";

    /** The Constant SQL_QUERY_SELECT_REQUALIFICATION_BY_ID_TASK_HISTORY. */
    private static final String SQL_QUERY_SELECT_REQUALIFICATION_BY_ID_TASK_HISTORY = "SELECT id_signalement, id_type_signalement, adresse, id_sector, date_requalification, id_task, commentaire_agent_terrain from signalement_requalification where id_history = ? and id_task = ?";

    /** The Constant SQL_QUERY_UPDATE_REQUALIFICATION. */
    private static final String SQL_QUERY_UPDATE_REQUALIFICATION = "UPDATE signalement_requalification t1 set id_history = ? where t1.id_signalement = ? and t1.id_task = ? and date_requalification = ( select max(t2.date_requalification) from signalement_requalification t2 where t1.id_signalement = t2.id_signalement )";

    /** The Constant SQL_QUERY_UPDATE_REQUALIFICATION_HISTORY_TASK. */
    private static final String SQL_QUERY_UPDATE_REQUALIFICATION_HISTORY_TASK = "UPDATE signalement_requalification t1 set id_history = ?, id_task = ? where t1.id_signalement = ? and date_requalification = ( select max(t2.date_requalification) from signalement_requalification t2 where t1.id_signalement = t2.id_signalement )";

    /** The Constant SQL_QUERY_GET_SIGNALEMENT_TDT_SELECT. */
    private static final String SQL_QUERY_GET_SIGNALEMENT_TDT_SELECT = "select state.id_state, case when state.id_state not in(9,21) then case when sig.date_creation >=( now()::date - interval ''2 DAY''::interval) then ''2'' when (sig.date_creation > now()- interval ''11 DAY''::interval) AND (sig.date_creation < now()- interval ''3 DAY''::interval) then ''1'' else ''0'' "
            + "end when state.id_state in (9,21) THEN case when sig.date_prevue_traitement > ( select  now()  ) and sig.date_prevue_traitement < ( select  now() + interval ''2 DAY''::interval ) then ''2'' when (sig.date_prevue_traitement > now()- interval ''11 DAY''::interval) and (sig.date_prevue_traitement < now()) then ''1'' when (sig.date_prevue_traitement < now()- ''11 days''::interval ) then ''0'' else ''-1'' end END AS tranche_date_creation,  count(sig.id_signalement) from signalement_signalement sig"
            + " join workflow_resource_workflow resource on resource.id_resource=sig.id_signalement join workflow_state state on state.id_state = resource.id_state "
            + "join unittree_unit_sector uus on sig.fk_id_sector = uus.id_sector " + "where date_creation > (now() - ''{0} days''::interval) ";

    /** The Constant SQL_QUERY_GET_ID_SIGNALEMENT_TDT_SELECT. */
    private static final String SQL_QUERY_GET_ID_SIGNALEMENT_TDT_SELECT = "select sig.id_signalement " + "from signalement_signalement sig "
            + "join workflow_resource_workflow resource on resource.id_resource=sig.id_signalement "
            + "join workflow_state state on state.id_state = resource.id_state " + "join unittree_unit_sector uus on sig.fk_id_sector = uus.id_sector ";

    /** The Constant SQL_QUERY_GET_SIGNALEMENT_TDT_GROUP. */
    private static final String SQL_QUERY_GET_SIGNALEMENT_TDT_GROUP = " group by state.id_state, tranche_date_creation "
            + "order by state.id_state, tranche_date_creation asc";

    /** The Constant SQL_QUERY_FIND_ANO_WITHOUT_STATES. */
    private static final String SQL_QUERY_FIND_ANO_WITHOUT_STATES = "select id_signalement, date_creation from signalement_signalement s left join workflow_resource_workflow w on s.id_signalement = w.id_resource where w.id_resource is null order by date_creation desc limit 10";

    /** The Constant SQL_QUERY_GET_REPARTITION_SERVICE_FAIT_MASSE. */
    private static final String SQL_QUERY_GET_REPARTITION_SERVICE_FAIT_MASSE = "select type.libelle, count(signalement.id_signalement) from signalement_signalement signalement join workflow_resource_workflow workflow on workflow.id_resource=signalement.id_signalement join signalement_type_signalement type on type.id_type_signalement = signalement.fk_id_type_signalement";

    /** The Constant SQL_QUERY_GET_ID_SIGNALEMENT_SERVICE_FAIT_MASSE. */
    private static final String SQL_QUERY_GET_ID_SIGNALEMENT_SERVICE_FAIT_MASSE = "select signalement.id_signalement from signalement_signalement signalement join workflow_resource_workflow workflow on workflow.id_resource = signalement.id_signalement join signalement_type_signalement type on type.id_type_signalement = signalement.fk_id_type_signalement";

    /** The Constant SQL_QUERY_INSERT_HISTORY_SERVICE_FAIT_MASSE_PART_1. */
    private static final String SQL_QUERY_INSERT_HISTORY_SERVICE_FAIT_MASSE_PART_1 = "WITH insHistory AS ( insert into workflow_resource_history (id_resource, resource_type, id_workflow, id_action, creation_date, user_access_code) select id_signalement, 'SIGNALEMENT_SIGNALEMENT', 2, 70, localtimestamp, 'admin' from signalement_signalement where signalement_signalement.id_signalement in ( ";

    /** The Constant SQL_QUERY_INSERT_HISTORY_SERVICE_FAIT_MASSE_PART_2. */
    private static final String SQL_QUERY_INSERT_HISTORY_SERVICE_FAIT_MASSE_PART_2 = " ) RETURNING id_history ) insert into workflow_task_comment_value (id_history, id_task, comment_value) select id_history, 150, ? from insHistory ";

    /** The Constant SQL_QUERY_UPDATE_STATE_SERVICE_FAIT_MASSE. */
    private static final String SQL_QUERY_UPDATE_STATE_SERVICE_FAIT_MASSE = " update workflow_resource_workflow set id_state = 10 ";

    /** The Constant SQL_QUERY_UPDATE_DATE_PASSAGE_FAIT_MASSE. */
    private static final String SQL_QUERY_UPDATE_DATE_PASSAGE_FAIT_MASSE = " update signalement_signalement set service_fait_date_passage = (select current_timestamp) ";

    /** The Constant SQL_QUERY_GET_SIGNALEMENTS_PROGRAMME. */
    private static final String SQL_QUERY_GET_SIGNALEMENTS_PROGRAMME = "select id_signalement from signalement_signalement join workflow_resource_workflow w on w.id_resource = id_signalement where date_prevue_traitement = NOW()::date and w.id_state=9";

    /** The Constant SQL_QUERY_GET_SIGNALEMENTS_PROGRAMME_TIERS. */
    private static final String SQL_QUERY_GET_SIGNALEMENTS_PROGRAMME_TIERS = "select id_signalement from signalement_signalement join workflow_resource_workflow w on w.id_resource = id_signalement where date_prevue_traitement = NOW()::date and w.id_state=21";

    /** The Constant SQL_QUERY_FIND_LABEL_PRESTATAIRE. */
    private static final String SQL_QUERY_FIND_LABEL_PRESTATAIRE = "select uu.label from signalement_signalement ss, unittree_unit_sector uus , unittree_unit uu , signalement_workflow_webservice_config_unit swwcu "
            + "where ss.fk_id_sector = uus.id_sector and uus.id_unit = uu.id_unit and uu.id_unit = swwcu.id_unit and ss.id_signalement = ? and swwcu.urlprestataire is not null limit 1";

    /**
     * Makes references between client sort keyword and actual sql joins / columns names.
     */
    private Map<String, String> _ordersMap;

    /** The Constant SQL_WHERE_DATE_CREATION. */
    private static final String SQL_WHERE_DATE_CREATION = "where date_creation > (now() - ''{0} days''::interval)";

    /**
     * Instantiates a new report dao.
     */
    public SignalementDAO( )
    {
        super( );

        // Initializes the list of keys / value used for sorting columns
        // by concerns of non-regression the current keys are the direct paths to the attributes used previously.
        _ordersMap = new HashMap<>( );
        _ordersMap.put( "signalement.suivi", "signalement.suivi" );
        _ordersMap.put( NUM_SIGNALEMENT, NUM_SIGNALEMENT );
        _ordersMap.put( SIGNALEUR_MAIL, SIGNALEUR_MAIL );
        _ordersMap.put( "priorite.libelle", "priorite.libelle" );
        _ordersMap.put( "type.libelle", "type.libelle" );
        _ordersMap.put( "direction_unit.label", "direction_unit.label" );
        _ordersMap.put( ADRESSE, ADRESSE );
        _ordersMap.put( "signalement.commentaire", "signalement.commentaire" );
        _ordersMap.put( "signalement.date_creation", "signalement.date_creation" );
        _ordersMap.put( "photo.id_photo", "photo.id_photo" );
    }

    /**
     * Generates a new primary key.
     *
     * @return The new primary key
     */
    private Long newPrimaryKey( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery( );
        Long nKey = null;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getLong( 1 );
        }
        daoUtil.close( );
        return nKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long insert( Signalement signalement )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) ; )
        {
            if ( ( signalement.getId( ) == null ) || signalement.getId( ).equals( 0L ) )
            {
                signalement.setId( newPrimaryKey( ) );
            }
            int nIndex = 1;
            daoUtil.setLong( nIndex++, signalement.getId( ) );
            daoUtil.setInt( nIndex++, signalement.getSuivi( ) );
            daoUtil.setTimestamp( nIndex++, new Timestamp( System.currentTimeMillis( ) ) );
            daoUtil.setString( nIndex++, signalement.getCommentaire( ) );
            daoUtil.setInt( nIndex++, signalement.getAnnee( ) );
            daoUtil.setString( nIndex++, signalement.getMois( ) );
            daoUtil.setInt( nIndex++, signalement.getNumero( ) );
            daoUtil.setString( nIndex++, signalement.getPrefix( ) );
            daoUtil.setLong( nIndex++, signalement.getPriorite( ).getId( ) );
            daoUtil.setLong( nIndex++, signalement.getTypeSignalement( ).getId( ) );

            Arrondissement arrondissement = signalement.getArrondissement( );
            if ( ( arrondissement != null ) && ( arrondissement.getId( ) != null ) )
            {
                daoUtil.setLong( nIndex++, arrondissement.getId( ) );
            }
            else
            {
                daoUtil.setInt( nIndex++, 0 );
            }
            Sector secteur = signalement.getSecteur( );
            Integer idSector;
            if ( secteur != null )
            {
                idSector = secteur.getIdSector( );
            }
            else
            {
                idSector = 0;
            }
            daoUtil.setInt( nIndex++, idSector );
            daoUtil.setBoolean( nIndex++, signalement.isDoublon( ) );
            daoUtil.setString( nIndex++, signalement.getToken( ) );
            daoUtil.setString( nIndex, signalement.getCommentaireAgentTerrain( ) );

            daoUtil.executeUpdate( );
        }

        return signalement.getId( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( long lId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setLong( 1, lId );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Signalement load( long nId )
    {
        Signalement signalement = new Signalement( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setLong( 1, nId );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {

            int nIndex = 1;
            signalement.setId( daoUtil.getLong( nIndex++ ) );
            signalement.setSuivi( daoUtil.getInt( nIndex++ ) );
            signalement.setDateCreation( DateUtils.getDateFr( daoUtil.getDate( nIndex ) ) );
            signalement.setHeureCreation( daoUtil.getTimestamp( nIndex++ ) );
            signalement.setDatePrevueTraitement( DateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );
            signalement.setCommentaire( daoUtil.getString( nIndex++ ) );
            signalement.setAnnee( daoUtil.getInt( nIndex++ ) );
            signalement.setMois( daoUtil.getString( nIndex++ ) );
            signalement.setNumero( daoUtil.getInt( nIndex++ ) );
            signalement.setPrefix( daoUtil.getString( nIndex++ ) );

            if ( daoUtil.getLong( nIndex++ ) > 0 )
            {
                Priorite priorite = new Priorite( );
                priorite.setId( daoUtil.getLong( nIndex - 1 ) );
                signalement.setPriorite( priorite );
            }
            if ( daoUtil.getLong( nIndex++ ) > 0 )
            {
                Arrondissement arrondissement = new Arrondissement( );
                arrondissement.setId( daoUtil.getLong( nIndex - 1 ) );
                signalement.setArrondissement( arrondissement );
            }
            if ( daoUtil.getLong( nIndex++ ) > 0 )
            {
                TypeSignalement typeSignalement = new TypeSignalement( );
                typeSignalement.setId( daoUtil.getInt( nIndex - 1 ) );
                signalement.setTypeSignalement( typeSignalement );
            }
            if ( daoUtil.getInt( nIndex++ ) > 0 )
            {
                Sector sector = new Sector( );
                sector.setIdSector( daoUtil.getInt( nIndex - 1 ) );
                signalement.setSecteur( sector );
            }

            signalement.setIsDoublon( daoUtil.getBoolean( nIndex++ ) );
            signalement.setToken( daoUtil.getString( nIndex++ ) );
            Date serviceFaitTraitement = daoUtil.getTimestamp( nIndex++ );
            if ( serviceFaitTraitement != null )
            {
                signalement.setDateServiceFaitTraitement( DateUtils.getDateFr( serviceFaitTraitement ) );
                signalement.setHeureServiceFaitTraitement( DateUtils.getHourFrSansColonne( serviceFaitTraitement ) );
            }
            signalement.setFelicitations( daoUtil.getInt( nIndex++ ) );
            Date miseEnSurveillance = daoUtil.getTimestamp( nIndex++ );
            if ( miseEnSurveillance != null )
            {
                signalement.setDateMiseEnSurveillance( DateUtils.getDateFr( miseEnSurveillance ) );
            }
            Date rejet = daoUtil.getTimestamp( nIndex++ );
            if ( rejet != null )
            {
                signalement.setDateRejet( DateUtils.getDateFr( rejet ) );
            }
            signalement.setCourrielDestinataire( daoUtil.getString( nIndex++ ) );
            signalement.setCourrielExpediteur( daoUtil.getString( nIndex++ ) );
            signalement.setCourrielDate( daoUtil.getTimestamp( nIndex++ ) );
            signalement.setSendWs( daoUtil.getBoolean( nIndex++ ) );
            signalement.setCommentaireAgentTerrain( daoUtil.getString( nIndex ) );

        }

        daoUtil.close( );

        return signalement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update( Signalement signalement )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, signalement.getId( ) );
        daoUtil.setLong( nIndex++, signalement.getSuivi( ) );
        daoUtil.setTimestamp( nIndex++, new Timestamp( signalement.getHeureCreation( ).getTime( ) ) );
        daoUtil.setDate( nIndex++, DateUtil.formatDateSql( signalement.getDatePrevueTraitement( ), Locale.FRENCH ) );
        daoUtil.setString( nIndex++, signalement.getCommentaire( ) );

        daoUtil.setLong( nIndex++, signalement.getPriorite( ).getId( ) );
        daoUtil.setLong( nIndex++, signalement.getTypeSignalement( ).getId( ) );

        daoUtil.setLong( nIndex++, signalement.getArrondissement( ).getId( ) );
        Sector secteur = signalement.getSecteur( );
        Integer idSector;
        if ( secteur != null )
        {
            idSector = secteur.getIdSector( );
        }
        else
        {
            idSector = 0;
        }
        daoUtil.setInt( nIndex++, idSector );

        daoUtil.setBoolean( nIndex++, signalement.isDoublon( ) );
        if ( StringUtils.isNotBlank( signalement.getDateServiceFaitTraitement( ) ) && StringUtils.isNotBlank( signalement.getHeureServiceFaitTraitement( ) ) )
        {
            String dateTraitementString = signalement.getDateServiceFaitTraitement( ) + signalement.getHeureServiceFaitTraitement( );
            try
            {
                daoUtil.setTimestamp( nIndex++, DateUtils.formatDateSqlWithTime( dateTraitementString ) );
            }
            catch( ParseException e )
            {
                AppLogService.error( e );
            }
        }
        else
        {
            daoUtil.setDate( nIndex++, null );
        }

        daoUtil.setString( nIndex++, signalement.getCourrielDestinataire( ) );
        daoUtil.setString( nIndex++, signalement.getCourrielExpediteur( ) );
        daoUtil.setTimestamp( nIndex++, signalement.getCourrielDate( ) );
        daoUtil.setBoolean( nIndex++, signalement.getIsSendWS( ) );
        daoUtil.setString( nIndex++, signalement.getCommentaireAgentTerrain( ) );
        daoUtil.setLong( nIndex, signalement.getId( ) );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Signalement loadById( long nId )
    {
        Signalement signalement = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setLong( 1, nId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            signalement = fillSignalement( daoUtil );
        }

        daoUtil.close( );

        return signalement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Signalement getAnomalieByNumber( String number )
    {
        Signalement signalement = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_NUMBER );
        daoUtil.setString( 1, number );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            signalement = fillSignalement( daoUtil );
        }

        daoUtil.close( );

        return signalement;
    }

    /**
     * Fill signalement.
     *
     * @param daoUtil
     *            the dao util
     * @return the signalement
     */
    private Signalement fillSignalement( DAOUtil daoUtil )
    {
        Signalement signalement = new Signalement( );

        int nIndex = 1;
        signalement.setId( daoUtil.getLong( nIndex++ ) );
        signalement.setSuivi( daoUtil.getInt( nIndex++ ) );
        signalement.setDateCreation( DateUtils.getDateFr( daoUtil.getDate( nIndex ) ) );
        signalement.setHeureCreation( daoUtil.getTimestamp( nIndex++ ) );
        signalement.setDatePrevueTraitement( DateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );
        signalement.setCommentaire( daoUtil.getString( nIndex++ ) );

        signalement.setAnnee( daoUtil.getInt( nIndex++ ) );
        signalement.setMois( daoUtil.getString( nIndex++ ) );
        signalement.setNumero( daoUtil.getInt( nIndex++ ) );
        signalement.setPrefix( daoUtil.getString( nIndex++ ) );

        if ( daoUtil.getLong( nIndex++ ) > 0 )
        {
            Priorite priorite = new Priorite( );
            priorite.setId( daoUtil.getLong( nIndex - 1 ) );
            signalement.setPriorite( priorite );
        }
        if ( daoUtil.getLong( nIndex++ ) > 0 )
        {
            Arrondissement arrondissement = new Arrondissement( );
            arrondissement.setId( daoUtil.getLong( nIndex - 1 ) );
            signalement.setArrondissement( arrondissement );
        }
        if ( daoUtil.getLong( nIndex++ ) > 0 )
        {
            TypeSignalement typeSignalement = new TypeSignalement( );
            typeSignalement.setId( daoUtil.getInt( nIndex - 1 ) );
            signalement.setTypeSignalement( typeSignalement );
        }
        if ( daoUtil.getInt( nIndex++ ) > 0 )
        {
            Sector sector = new Sector( );
            sector.setIdSector( daoUtil.getInt( nIndex - 1 ) );
            signalement.setSecteur( sector );
        }

        signalement.setIsDoublon( daoUtil.getBoolean( nIndex++ ) );
        signalement.setToken( daoUtil.getString( nIndex++ ) );
        Date serviceFaitTraitement = daoUtil.getTimestamp( nIndex++ );
        if ( serviceFaitTraitement != null )
        {
            signalement.setDateServiceFaitTraitement( DateUtils.getDateFr( serviceFaitTraitement ) );
            signalement.setHeureServiceFaitTraitement( DateUtils.getHourFrSansColonne( serviceFaitTraitement ) );
        }
        signalement.setFelicitations( daoUtil.getInt( nIndex++ ) );
        Date miseEnSurveillance = daoUtil.getTimestamp( nIndex++ );
        if ( miseEnSurveillance != null )
        {
            signalement.setDateMiseEnSurveillance( DateUtils.getDateFr( miseEnSurveillance ) );
        }
        Date rejet = daoUtil.getTimestamp( nIndex++ );
        if ( rejet != null )
        {
            signalement.setDateRejet( DateUtils.getDateFr( rejet ) );
        }

        signalement.setCourrielDestinataire( daoUtil.getString( nIndex++ ) );
        signalement.setCourrielExpediteur( daoUtil.getString( nIndex++ ) );

        Timestamp courrielDate = daoUtil.getTimestamp( nIndex++ );
        if ( courrielDate != null )
        {
            signalement.setCourrielDate( courrielDate );
        }
        signalement.setSendWs( daoUtil.getBoolean( nIndex++ ) );
        signalement.setCommentaireAgentTerrain( daoUtil.getString( nIndex ) );

        return signalement;
    }

    /**
     * Update select part.
     *
     * @param filter
     *            the report filter
     * @return the string builder
     */
    private StringBuilder updateSelectPart( SignalementFilter filter )
    {
        StringBuilder sbSQL = new StringBuilder( );
        List<Order> listeOrders = convertOrderToClause( filter.getOrders( ) );
        if ( !listeOrders.isEmpty( ) )
        {
            sbSQL.append( " , " );
            int index = 1;
            for ( Order order : listeOrders )
            {
                if ( order.getName( ).equals( NUM_SIGNALEMENT ) )
                {
                    sbSQL.append( "signalement.prefix, signalement.annee, signalement.mois, signalement.numero " );
                }
                else
                    if ( ADRESSE.equals( order.getName( ) ) )
                    {
                        sbSQL.append( order.getName( ) + ", naturalsort(adresse) " );
                    }
                    else
                    {
                        sbSQL.append( order.getName( ) + " " );
                    }

                if ( index < listeOrders.size( ) )
                {
                    sbSQL.append( ", " );
                    index++;
                }
            }
        }
        return sbSQL;

    }

    /**
     * replaces sort keys with sql paths to columns to sort.
     *
     * @param orders
     *            the orders
     * @return the list
     */
    private List<Order> convertOrderToClause( List<Order> orders )
    {
        List<Order> ret = new ArrayList<>( );
        for ( Order order : orders )
        {
            String string = _ordersMap.get( order.getName( ) );
            if ( string != null )
            {
                ret.add( new Order( string, order.getOrder( ) ) );
            }
        }
        return ret;
    }

    /**
     * Build the SQL query with filter.
     *
     * @param filter
     *            the filter
     * @return a SQL query
     */
    private String buildSQLQuery( SignalementFilter filter )
    {
        StringBuilder sbSQL = new StringBuilder( SQL_SELECT );
        sbSQL.append( SQL_DISTINCT );
        sbSQL.append( SQL_QUERY_PART_SELECT );
        sbSQL.append( updateSelectPart( filter ) );
        sbSQL.append( SQL_QUERY_FROM_ALL );
        sbSQL.append( SQL_QUERY_FROM_PRORITE );

        boolean bHasOrderUnit = false;
        boolean bHasOrderSignalementType = false;
        boolean bHasOrderSignaleur = false;
        boolean bHasOrderPhoto = false;
        for ( Order order : filter.getOrders( ) )
        {
            if ( order.getName( ) != null )
            {
                if ( order.getName( ).startsWith( "unit." ) )
                {
                    bHasOrderUnit = true;
                }
                else
                    if ( order.getName( ).startsWith( "type." ) )
                    {
                        bHasOrderSignalementType = true;
                    }
                    else
                        if ( order.getName( ).startsWith( "signaleur." ) )
                        {
                            bHasOrderSignaleur = true;
                        }
                        else
                            if ( order.getName( ).startsWith( "photo." ) )
                            {
                                bHasOrderPhoto = true;
                            }
            }
        }

        boolean bHasFilterSignalementType = filter.getIdTypeSignalement( ) > 0;
        boolean bHasFilterUnit = filter.getIdDirection( ) > 0;
        boolean bHasFilerMail = ( filter.getMail( ) != null ) && !StringUtils.isBlank( filter.getMail( ) );
        boolean bHasFilterCategory = !CollectionUtils.isEmpty( filter.getListIdCategories( ) );
        boolean bHasFilterAdress = !StringUtils.isEmpty( filter.getAdresse( ) );
        boolean bHasFilterQuatier = !CollectionUtils.isEmpty( filter.getListIdQuartier( ) );

        List<Order> listeOrders = convertOrderToClause( filter.getOrders( ) );
        if ( !listeOrders.isEmpty( ) && !bHasFilterAdress )
        {
            bHasFilterAdress = listeOrders.stream( ).anyMatch( o -> ADRESSE.equals( o.getName( ) ) );
        }

        // Units requires type
        if ( bHasFilterSignalementType || bHasOrderSignalementType || bHasFilterUnit || bHasOrderUnit )
        {
            sbSQL.append( SQL_QUERY_FROM_SIGNALEMENT_TYPE );
        }

        if ( bHasFilterSignalementType )
        {
            sbSQL.append( SQL_QUERY_FROM_VIEW_TYPE_SIGNALEMENT_W_PARENT_LINK );
        }

        if ( bHasFilterUnit || bHasOrderUnit )
        {
            sbSQL.append( SQL_QUERY_FROM_UNITTREE );
        }

        if ( bHasFilerMail || bHasOrderSignaleur )
        {
            sbSQL.append( SQL_QUERY_FROM_SIGNALEUR );
        }

        if ( bHasOrderPhoto )
        {
            sbSQL.append( SQL_QUERY_FROM_PHOTO );
        }

        if ( bHasFilterCategory )
        {
            sbSQL.append( SQL_QUERY_FROM_SIGNALEMENT_CATEGORY );
        }

        if ( bHasFilterAdress || bHasFilterQuatier )
        {
            sbSQL.append( SQL_QUERY_FROM_ADRESS );
        }

        int nIndex = 1;

        // Type
        if ( bHasFilterSignalementType )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_TYPE_SIGNALEMENT );
        }

        if ( ( filter.getListIdTypeSignalements( ) != null ) && !filter.getListIdTypeSignalements( ).isEmpty( ) )
        {
            int listeLength = filter.getListIdTypeSignalements( ).size( );
            Character [ ] array = new Character [ listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array [i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_LIST_TYPE_SIGNALEMENT, unionQuery ) );
        }

        // Report number
        if ( ( filter.getNumero( ) != null ) && !StringUtils.isBlank( filter.getNumero( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_NUMERO );
        }

        // Board
        if ( bHasFilterUnit )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_DIRECTION );
        }

        // district
        if ( filter.getIdArrondissement( ) > 0 )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_ARRONDISSEMENT );
        }
        if ( ( filter.getListIdArrondissements( ) != null ) && !filter.getListIdArrondissements( ).isEmpty( ) )
        {
            int listeLength = filter.getListIdArrondissements( ).size( );
            Character [ ] array = new Character [ listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array [i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_LIST_ARRONDISSEMENT, unionQuery ) );
        }

        // neighborhood
        if ( ( filter.getListIdQuartier( ) != null ) && !filter.getListIdQuartier( ).isEmpty( ) )
        {
            int listeLength = filter.getListIdQuartier( ).size( );
            Character [ ] array = new Character [ listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array [i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_LIST_QUARTIER, unionQuery ) );
        }

        // SECTOR
        if ( filter.getIdSector( ) > 0 )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_SECTOR );
        }

        // Allowed sectors

        if ( !filter.getListIdUnit( ).isEmpty( ) )
        {
            int listeLength = filter.getListIdUnit( ).size( );
            Character [ ] array = new Character [ listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array [i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_SECTOR_ALLOWED, unionQuery ) );

        }
        else
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( "1 = 2" );
        }

        // Address
        if ( bHasFilterAdress )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_ADRESSE );
        }

        // Mail
        if ( bHasFilerMail )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_MAIL );
        }

        // Commentary
        if ( ( filter.getCommentaire( ) != null ) && !StringUtils.isBlank( filter.getCommentaire( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_COMMENTAIRE );
        }

        if ( ( filter.getCommentaireAgentTerrain( ) != null ) && !StringUtils.isBlank( filter.getCommentaireAgentTerrain( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_COMMENTAIRE_AGENT_TERRAIN );
        }

        // Creation date
        boolean dateBeginNotEmpty = ( filter.getDateBegin( ) != null ) && !StringUtils.isBlank( filter.getDateBegin( ) );
        boolean dateEndNotEmpty = ( filter.getDateEnd( ) != null ) && !StringUtils.isBlank( filter.getDateEnd( ) );

        if ( dateBeginNotEmpty )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_DATE_BEGIN );
        }

        if ( dateEndNotEmpty )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_DATE_END );
        }

        // State
        if ( filter.getEtats( ) != null )
        {
            int i = filter.getEtats( ).size( );

            for ( EtatSignalement etatSign : filter.getEtats( ) )
            {
                if ( etatSign.getCoche( ) )
                {

                    if ( nIndex == 1 )
                    {
                        sbSQL.append( SQL_WHERE );
                    }
                    else
                    {
                        if ( i == filter.getEtats( ).size( ) )
                        {
                            sbSQL.append( SQL_AND );
                        }
                        else
                        {
                            sbSQL.append( SQL_OR );
                        }

                    }
                    nIndex++;

                    if ( i == filter.getEtats( ).size( ) )
                    {
                        sbSQL.append( " ( " );
                    }
                    sbSQL.append( SQL_QUERY_ADD_FILTER_ETAT );
                    i--;
                    if ( i < 1 )
                    {

                        sbSQL.append( " ) " );
                    }

                }

            }
        }

        // ADDING CATEGORY FILTER
        if ( bHasFilterCategory )
        {
            int listeLength = filter.getListIdCategories( ).size( );
            Character [ ] array = new Character [ listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array [i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_CATEGORY, unionQuery ) );
        }

        // ADD THE GROUP BY
        if ( bHasFilerMail )
        {
            sbSQL.append( SQL_GROUP_BY );
            sbSQL.append( SQL_QUERY_PART_SELECT );
            sbSQL.append( updateSelectPart( filter ) );
        }
        // ADD ORDERS
        if ( ( listeOrders != null ) && !listeOrders.isEmpty( ) )
        {
            sbSQL.append( " ORDER BY " );
            int index = 1;
            for ( Order order : listeOrders )
            {
                if ( NUM_SIGNALEMENT.equals( order.getName( ) ) )
                {
                    sbSQL.append( " signalement.prefix " + order.getOrder( ) + ", signalement.annee " + order.getOrder( ) + ", signalement.mois "
                            + order.getOrder( ) + ", signalement.numero " + order.getOrder( ) + " " );
                }
                else
                    if ( SIGNALEUR_MAIL.equals( order.getName( ) ) )
                    {
                        sbSQL.append( order.getName( ) + " " + order.getOrder( ) + " NULLS LAST " );
                    }
                    else
                        if ( ADRESSE.equals( order.getName( ) ) )
                        {
                            sbSQL.append( " naturalsort(adresse) " + " " + order.getOrder( ) + " " );
                        }
                        else
                        {
                            sbSQL.append( order.getName( ) + " " + order.getOrder( ) + " " );
                        }
                if ( index < listeOrders.size( ) )
                {
                    sbSQL.append( ", " );
                    index++;
                }
            }
        }

        return sbSQL.toString( );
    }

    /**
     * Set the filter values on the DAOUtil.
     *
     * @param filter
     *            the filter
     * @param daoUtil
     *            the DAOUtil
     */
    private void setFilterValues( SignalementFilter filter, DAOUtil daoUtil )
    {
        int nIndex = 1;

        // Type
        if ( filter.getIdTypeSignalement( ) > 0 )
        {
            daoUtil.setLong( nIndex++, filter.getIdTypeSignalement( ) );
        }
        if ( ( filter.getListIdTypeSignalements( ) != null ) && !filter.getListIdTypeSignalements( ).isEmpty( ) )
        {
            for ( Integer nIdTypeSignalement : filter.getListIdTypeSignalements( ) )
            {
                daoUtil.setLong( nIndex++, nIdTypeSignalement );
            }
        }

        // Report number
        if ( ( filter.getNumero( ) != null ) && !StringUtils.isBlank( filter.getNumero( ) ) )
        {
            daoUtil.setString( nIndex++, ( filter.getNumero( ) ) );
            daoUtil.setString( nIndex++, ( filter.getNumero( ) ) );
            daoUtil.setString( nIndex++, ( filter.getNumero( ) ) );
            daoUtil.setString( nIndex++, ( filter.getNumero( ) ) );
            daoUtil.setString( nIndex++, ( filter.getNumero( ) ) );
            daoUtil.setString( nIndex++, ( filter.getNumero( ) ) );
            daoUtil.setString( nIndex++, ( filter.getNumero( ) ) );
            daoUtil.setString( nIndex++, ( filter.getNumero( ) ) );
            daoUtil.setString( nIndex++, ( filter.getNumero( ) ) );
            daoUtil.setString( nIndex++, ( filter.getNumero( ) ) );
        }

        // Board
        if ( filter.getIdDirection( ) > 0 )
        {
            daoUtil.setLong( nIndex++, filter.getIdDirection( ) );
        }

        // District
        if ( filter.getIdArrondissement( ) > 0 )
        {
            daoUtil.setLong( nIndex++, filter.getIdArrondissement( ) );
        }
        if ( ( filter.getListIdArrondissements( ) != null ) && !filter.getListIdArrondissements( ).isEmpty( ) )
        {
            for ( Integer nIdArrondissement : filter.getListIdArrondissements( ) )
            {
                daoUtil.setLong( nIndex++, nIdArrondissement );
            }
        }

        // neighborhood
        if ( ( filter.getListIdQuartier( ) != null ) && !filter.getListIdQuartier( ).isEmpty( ) )
        {
            for ( Integer nIdQuartier : filter.getListIdQuartier( ) )
            {
                daoUtil.setLong( nIndex++, nIdQuartier );
            }
        }

        // Sector
        if ( filter.getIdSector( ) > 0 )
        {
            daoUtil.setLong( nIndex++, filter.getIdSector( ) );
        }

        // daoUtil.setLong() as many times as there are elems in the list of authorized sectors.
        if ( ( filter.getListIdUnit( ) != null ) && !filter.getListIdUnit( ).isEmpty( ) )
        {
            for ( Integer nIdSecteur : filter.getListIdUnit( ) )
            {
                daoUtil.setLong( nIndex++, nIdSecteur );
            }

        }

        // Address
        boolean bHasFilterAdress = !StringUtils.isEmpty( filter.getAdresse( ) );
        List<Order> listeOrders = convertOrderToClause( filter.getOrders( ) );
        if ( !listeOrders.isEmpty( ) && !bHasFilterAdress )
        {
            bHasFilterAdress = listeOrders.stream( ).anyMatch( o -> ADRESSE.equals( o.getName( ) ) );
        }

        if ( bHasFilterAdress )
        {
            daoUtil.setString( nIndex++, addPercent( filter.getAdresse( ) ) );
        }

        // Mail
        if ( ( filter.getMail( ) != null ) && !StringUtils.isBlank( filter.getMail( ) ) )
        {
            daoUtil.setString( nIndex++, addPercent( filter.getMail( ) ) );
        }

        // Commentary
        if ( ( filter.getCommentaire( ) != null ) && !StringUtils.isBlank( filter.getCommentaire( ) ) )
        {
            daoUtil.setString( nIndex++, addPercent( filter.getCommentaire( ) ) );
        }

        if ( ( filter.getCommentaireAgentTerrain( ) != null ) && !StringUtils.isBlank( filter.getCommentaireAgentTerrain( ) ) )
        {
            daoUtil.setString( nIndex++, addPercent( filter.getCommentaireAgentTerrain( ) ) );
        }

        // Creation date
        boolean dateBeginNotEmpty = ( filter.getDateBegin( ) != null ) && !StringUtils.isBlank( filter.getDateBegin( ) );
        boolean dateEndNotEmpty = ( filter.getDateEnd( ) != null ) && !StringUtils.isBlank( filter.getDateEnd( ) );

        if ( dateBeginNotEmpty )
        {
            daoUtil.setDate( nIndex++, DateUtil.formatDateSql( filter.getDateBegin( ), Locale.FRENCH ) );
        }

        if ( dateEndNotEmpty )
        {
            daoUtil.setDate( nIndex++, DateUtil.formatDateSql( filter.getDateEnd( ), Locale.FRENCH ) );
        }

        // State
        if ( ( filter.getEtats( ) != null ) && !filter.getEtats( ).isEmpty( ) )
        {
            for ( EtatSignalement etatSign : filter.getEtats( ) )
            {
                if ( etatSign.getCoche( ) )
                {
                    daoUtil.setLong( nIndex++, etatSign.getId( ) );
                }
            }
        }

        // CATEGORY
        if ( !CollectionUtils.isEmpty( filter.getListIdCategories( ) ) )
        {
            for ( Integer nIdCategory : filter.getListIdCategories( ) )
            {
                daoUtil.setLong( nIndex++, nIdCategory );
            }

        }

    }

    /**
     * Add % before and after the string.
     *
     * @param name
     *            the string
     * @return the string with %
     */
    private String addPercent( String name )
    {
        return "%" + name + "%";
    }

    /**
     * Add a <b>WHERE</b> or a <b>OR</b> depending of the index. <br/>
     * <ul>
     * <li>if <code>nIndex</code> == 1, then we add a <b>WHERE</b></li>
     * <li>if <code>nIndex</code> != 1, then we add a <b>OR</b> or a <b>AND</b> depending of the wide search characteristic</li>
     * </ul>
     *
     * @param bIsWideSearch
     *            true if it is a wide search, false otherwise
     * @param sbSQL
     *            the SQL query
     * @param nIndex
     *            the index
     * @return the new index
     */
    private int addSQLWhereOr( boolean bIsWideSearch, StringBuilder sbSQL, int nIndex )
    {
        if ( nIndex == 1 )
        {
            sbSQL.append( SQL_WHERE );
        }
        else
        {
            sbSQL.append( bIsWideSearch ? SQL_OR : SQL_AND );
        }

        return nIndex + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Signalement> findByFilter( SignalementFilter filter, PaginationProperties paginationProperties, Plugin plugin )
    {

        List<Signalement> listSignalements = new ArrayList<>( );
        StringBuilder sbSQL = new StringBuilder( buildSQLQuery( filter ) );

        if ( paginationProperties != null )
        {
            sbSQL.append( "LIMIT " + paginationProperties.getItemsPerPage( ) );
            sbSQL.append( " OFFSET " + ( ( paginationProperties.getPageIndex( ) - 1 ) * paginationProperties.getItemsPerPage( ) ) );
        }

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );

        // Special case Specificity for DEVE entity, change the id from SEJ to DEVE
        if ( filter.getIdDirection( ) == SEJ_ID )
        {
            filter.setIdDirection( 1 );
        }

        setFilterValues( filter, daoUtil );

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;

            Signalement signalement = load( daoUtil.getLong( nIndex++ ) );

            // Priorities
            Priorite priorite = new Priorite( );
            priorite.setLibelle( daoUtil.getString( nIndex++ ) );

            // Report type
            TypeSignalement typeSignalement = new TypeSignalement( );
            typeSignalement.setId( daoUtil.getInt( nIndex ) );

            signalement.setPriorite( priorite );
            signalement.setTypeSignalement( typeSignalement );

            listSignalements.add( signalement );
        }

        daoUtil.close( );

        return listSignalements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Signalement> getAllSignalement( )
    {
        List<Signalement> listResult = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            Signalement signalement = getSignalementFromDaoUtil( daoUtil );

            listResult.add( signalement );
        }

        daoUtil.close( );

        return listResult;
    }

    /**
     * Gets the reports from dao util.
     *
     * @param daoUtil
     *            the dao util
     * @return the reports from dao util
     */
    private Signalement getSignalementFromDaoUtil( DAOUtil daoUtil )
    {
        int nIndex;
        nIndex = 1;
        Signalement signalement = new Signalement( );

        signalement.setId( daoUtil.getLong( nIndex++ ) );
        signalement.setSuivi( daoUtil.getInt( nIndex++ ) );
        signalement.setDateCreation( DateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );
        signalement.setDatePrevueTraitement( DateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );
        signalement.setCommentaire( daoUtil.getString( nIndex++ ) );
        signalement.setAnnee( daoUtil.getInt( nIndex++ ) );
        signalement.setMois( daoUtil.getString( nIndex++ ) );
        signalement.setNumero( daoUtil.getInt( nIndex++ ) );
        signalement.setPrefix( daoUtil.getString( nIndex++ ) );

        if ( daoUtil.getLong( nIndex++ ) > 0 )
        {
            Priorite priorite = new Priorite( );
            priorite.setId( daoUtil.getLong( nIndex - 1 ) );
            signalement.setPriorite( priorite );
        }
        if ( daoUtil.getLong( nIndex++ ) > 0 )
        {
            Arrondissement arrondissement = new Arrondissement( );
            arrondissement.setId( daoUtil.getLong( nIndex - 1 ) );
            signalement.setArrondissement( arrondissement );
        }
        if ( daoUtil.getLong( nIndex++ ) > 0 )
        {
            TypeSignalement typeSignalement = new TypeSignalement( );
            typeSignalement.setId( daoUtil.getInt( nIndex - 1 ) );
            signalement.setTypeSignalement( typeSignalement );
        }
        if ( daoUtil.getInt( nIndex++ ) > 0 )
        {
            Sector sector = new Sector( );
            sector.setIdSector( daoUtil.getInt( nIndex - 1 ) );
            signalement.setSecteur( sector );
        }
        signalement.setIsDoublon( daoUtil.getBoolean( nIndex++ ) );
        signalement.setSendWs( daoUtil.getBoolean( nIndex ) );
        return signalement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Signalement> getSignalementsArchivableByStatusId( Integer statusId, Integer limit )
    {
        DAOUtil daoUtil = new DAOUtil( MessageFormat.format( SQL_QUERY_SELECT_BY_STATUS, limit ) );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, statusId );
        daoUtil.setInt( nIndex, statusId );
        daoUtil.executeQuery( );
        List<Signalement> listResult = new ArrayList<>( );
        while ( daoUtil.next( ) )
        {
            Signalement signalement = getSignalementFromDaoUtil( daoUtil );

            listResult.add( signalement );
        }

        daoUtil.close( );

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getAllIdSignalement( )
    {
        List<Integer> listResult = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_ID_SIGNALEMENT );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            listResult.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.close( );

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void incrementeSuiviByIdSignalement( Integer nIdSignalement )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_UPDATE_SUIVI_PLUS_ONE );
        daoUtil.setInt( 1, nIdSignalement );
        daoUtil.executeUpdate( );

        daoUtil.close( );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void decrementSuiviByIdSignalement( Integer nIdSignalement )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_UPDATE_SUIVI_MINUS_ONE );
        daoUtil.setInt( 1, nIdSignalement );
        daoUtil.executeUpdate( );

        daoUtil.close( );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> findAllSignalementInPerimeter( Double lat, Double lng, Integer radius, List<Integer> listStatus )
    {

        StringBuilder stringBuilder = new StringBuilder( );
        stringBuilder.append( "SELECT id_signalement FROM signalement_adresse " );
        stringBuilder.append( "INNER JOIN signalement_signalement AS signalement ON signalement_adresse.fk_id_signalement = signalement.id_signalement " );
        stringBuilder.append( "INNER JOIN workflow_resource_workflow AS resource_workflow ON resource_workflow.id_resource = signalement.id_signalement " );
        stringBuilder.append( "WHERE ST_Within(ST_Transform( signalement_adresse.geom,3857), ST_Buffer(ST_Transform(ST_SetSRID(ST_MakePoint(" );
        stringBuilder.append( lng );
        stringBuilder.append( ", " );
        stringBuilder.append( lat );
        stringBuilder.append( "),4326),3857)," );
        stringBuilder.append( radius );
        stringBuilder.append( ")) = true " );
        stringBuilder.append( "AND resource_workflow.id_workflow = 2 " );
        stringBuilder.append( "AND resource_workflow.resource_type = 'SIGNALEMENT_SIGNALEMENT' " );

        if ( ( listStatus != null ) && !listStatus.isEmpty( ) )
        {
            Object [ ] qMarks = new Object [ listStatus.size( )];
            for ( int i = 0; i < listStatus.size( ); i++ )
            {
                qMarks [i] = '?';
            }
            String joinQuestionMarks = StringUtils.join( qMarks, ", " );
            stringBuilder.append( "AND resource_workflow.id_state IN (" + joinQuestionMarks + ") " );
        }

        DAOUtil daoUtil = new DAOUtil( stringBuilder.toString( ) );

        List<Integer> listResult = new ArrayList<>( );
        int index = 1;
        if ( ( listStatus != null ) && !listStatus.isEmpty( ) )
        {
            for ( Integer idStatus : listStatus )
            {
                daoUtil.setInt( index++, idStatus );
            }
        }

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            listResult.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.close( );
        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DossierSignalementDTO> findAllSignalementInPerimeterWithDTO( Double lat, Double lng, Integer radius, List<Integer> listStatus )
    {

        StringBuilder stringBuilder = new StringBuilder( );
        stringBuilder.append(
                "SELECT id_signalement, adresse, date_creation, commentaire, ST_X(geom), ST_Y(geom), signalement.fk_id_type_signalement, signalement.prefix, vstswp.id_parent FROM signalement_adresse " );
        stringBuilder.append( "INNER JOIN signalement_signalement AS signalement ON signalement_adresse.fk_id_signalement = signalement.id_signalement " );
        stringBuilder.append( "INNER JOIN signalement_type_signalement AS type ON type.id_type_signalement = signalement.fk_id_type_signalement " );
        stringBuilder.append(
                "INNER JOIN v_signalement_type_signalement_with_parents_links AS vstswp ON vstswp.id_type_signalement = signalement.fk_id_type_signalement AND vstswp.is_parent_a_category=1 " );
        stringBuilder.append( "INNER JOIN workflow_resource_workflow AS resource_workflow ON resource_workflow.id_resource = signalement.id_signalement " );
        stringBuilder.append( "WHERE ST_Within(ST_Transform( signalement_adresse.geom,3857), ST_Buffer(ST_Transform(ST_SetSRID(ST_MakePoint(" );
        stringBuilder.append( lng );
        stringBuilder.append( ", " );
        stringBuilder.append( lat );
        stringBuilder.append( "),4326),3857)," );
        stringBuilder.append( radius );
        stringBuilder.append( ")) = true " );
        stringBuilder.append( "AND resource_workflow.id_workflow = 2 " );
        stringBuilder.append( "AND resource_workflow.resource_type = 'SIGNALEMENT_SIGNALEMENT' " );

        if ( ( listStatus != null ) && !listStatus.isEmpty( ) )
        {
            Object [ ] qMarks = new Object [ listStatus.size( )];
            for ( int i = 0; i < listStatus.size( ); i++ )
            {
                qMarks [i] = '?';
            }
            String joinQuestionMarks = StringUtils.join( qMarks, ", " );
            stringBuilder.append( "AND resource_workflow.id_state IN (" + joinQuestionMarks + ") " );
        }

        DAOUtil daoUtil = new DAOUtil( stringBuilder.toString( ) );

        int index = 1;
        if ( ( listStatus != null ) && !listStatus.isEmpty( ) )
        {
            for ( Integer idStatus : listStatus )
            {
                daoUtil.setInt( index++, idStatus );
            }
        }

        daoUtil.executeQuery( );

        List<DossierSignalementDTO> listResult = new ArrayList<>( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;

            DossierSignalementDTO dossierSignalementDTO = new DossierSignalementDTO( );
            dossierSignalementDTO.setId( (long) daoUtil.getInt( nIndex++ ) );
            dossierSignalementDTO.setAdresse( daoUtil.getString( nIndex++ ) );
            dossierSignalementDTO.setDateCreation( DateUtils.getDateFr( daoUtil.getDate( nIndex ) ) );
            dossierSignalementDTO.setHeureCreation( DateUtils.getHourFr( daoUtil.getTimestamp( nIndex++ ) ) );
            dossierSignalementDTO.setCommentaire( daoUtil.getString( nIndex++ ) );
            dossierSignalementDTO.setLng( daoUtil.getDouble( nIndex++ ) );
            dossierSignalementDTO.setLat( daoUtil.getDouble( nIndex++ ) );
            dossierSignalementDTO.setType( daoUtil.getString( nIndex++ ) );
            dossierSignalementDTO.setPrefix( daoUtil.getString( nIndex++ ) );
            dossierSignalementDTO.setIdCategory( daoUtil.getInt( nIndex ) );
            listResult.add( dossierSignalementDTO );
        }

        daoUtil.close( );
        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getDistanceBetweenSignalement( double lat1, double lng1, double lat2, double lng2 )
    {
        String query = "SELECT ST_Distance(ST_GeographyFromText('POINT(" + lng1 + "" + lat1 + ")'), " + "ST_GeographyFromText('POINT(" + lng2 + "" + lat2
                + ")')) ";

        Integer distance = 0;

        DAOUtil daoUtil = new DAOUtil( query );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            distance = daoUtil.getInt( 1 );
        }

        daoUtil.close( );
        return distance;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> findByIdTelephone( String idTelephone )
    {

        List<Integer> listResult = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ID_TELEPHONE );
        daoUtil.setString( 1, idTelephone );

        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            listResult.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.close( );

        return listResult;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Signalement getSignalementByToken( String token, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_SIGNALEMENT_BY_TOKEN );
        daoUtil.setString( 1, token );
        daoUtil.executeQuery( );
        Signalement signalement = null;
        while ( daoUtil.next( ) )
        {
            int nIndex = 1;

            signalement = new Signalement( );
            signalement.setId( daoUtil.getLong( nIndex++ ) );
            signalement.setToken( daoUtil.getString( nIndex ) );
        }

        daoUtil.close( );
        return signalement;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double [ ] getGeomFromLambertToWgs84( Double dLatLambert, Double dLngLambert )
    {

        String query = "SELECT ST_X(ST_Transform(ST_GeomFromText('POINT(" + dLatLambert + " " + dLngLambert
                + ")',27561),4326)), ST_Y(ST_Transform(ST_GeomFromText('POINT(" + dLatLambert + " " + dLngLambert + ")',27561),4326))";

        Double geom[] = new Double [ 2];
        DAOUtil daoUtil = new DAOUtil( query );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;

            geom [0] = daoUtil.getDouble( nIndex++ );
            geom [1] = daoUtil.getDouble( nIndex );
        }

        daoUtil.close( );
        return geom;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double [ ] getGeomFromLambert93ToWgs84( Double dLatLambert, Double dLngLambert )
    {

        String query = "SELECT ST_X(ST_Transform(ST_GeomFromText('POINT(" + dLatLambert + " " + dLngLambert
                + ")',2154),4326)), ST_Y(ST_Transform(ST_GeomFromText('POINT(" + dLatLambert + " " + dLngLambert + ")',2154),4326))";

        Double geom[] = new Double [ 2];
        DAOUtil daoUtil = new DAOUtil( query );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;

            geom [0] = daoUtil.getDouble( nIndex++ );
            geom [1] = daoUtil.getDouble( nIndex );
        }

        daoUtil.close( );
        return geom;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer countIdSignalementByFilter( SignalementFilter filter, Plugin plugin )
    {
        StringBuilder sbSQL = new StringBuilder( buildSQLQueryForCount( filter ) );
        Integer nNbIdSignalement = 0;

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );
        setFilterValues( filter, daoUtil );

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            nNbIdSignalement = daoUtil.getInt( 1 );
        }

        daoUtil.close( );

        return nNbIdSignalement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String buildSQLQueryForCount( SignalementFilter filter )
    {
        StringBuilder sbSQL = new StringBuilder( SQL_SELECT_COUNT_ID_SIGNALEMENT );
        addFilterCriterias( filter, sbSQL );

        return sbSQL.toString( );
    }

    /**
     * Adds filter criterias to query.
     *
     * @param filter
     *            the report filter
     * @param sbSQL
     *            the quey
     */
    private void addFilterCriterias( SignalementFilter filter, StringBuilder sbSQL )
    {
        sbSQL.append( SQL_QUERY_FROM_ALL );

        boolean bHasFilerMail = ( filter.getMail( ) != null ) && !StringUtils.isBlank( filter.getMail( ) );
        boolean bHasFilterSignalementType = filter.getIdTypeSignalement( ) > 0;
        boolean bHasFilterUnit = filter.getIdDirection( ) > 0;
        boolean bHasFilterCategory = !CollectionUtils.isEmpty( filter.getListIdCategories( ) );
        boolean bHasFilterAdress = !StringUtils.isEmpty( filter.getAdresse( ) );
        boolean bHasFilterQuatier = !CollectionUtils.isEmpty( filter.getListIdQuartier( ) );

        List<Order> listeOrders = convertOrderToClause( filter.getOrders( ) );
        if ( !listeOrders.isEmpty( ) && !bHasFilterAdress )
        {
            bHasFilterAdress = listeOrders.stream( ).anyMatch( o -> ADRESSE.equals( o.getName( ) ) );
        }

        // Units requires type
        if ( bHasFilterSignalementType || bHasFilterUnit )
        {
            sbSQL.append( SQL_QUERY_FROM_SIGNALEMENT_TYPE );
        }

        if ( bHasFilterSignalementType )
        {
            sbSQL.append( SQL_QUERY_FROM_VIEW_TYPE_SIGNALEMENT_W_PARENT_LINK );
        }

        if ( bHasFilterUnit )
        {
            sbSQL.append( SQL_QUERY_FROM_UNITTREE );
        }

        if ( bHasFilerMail )
        {
            sbSQL.append( SQL_QUERY_FROM_SIGNALEUR );
        }

        if ( bHasFilterCategory )
        {
            sbSQL.append( SQL_QUERY_FROM_SIGNALEMENT_CATEGORY );
        }

        if ( bHasFilterAdress || bHasFilterQuatier )
        {
            sbSQL.append( SQL_QUERY_FROM_ADRESS );
        }

        int nIndex = 1;

        // Type
        if ( filter.getIdTypeSignalement( ) > 0 )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_TYPE_SIGNALEMENT );
        }
        if ( ( filter.getListIdTypeSignalements( ) != null ) && !filter.getListIdTypeSignalements( ).isEmpty( ) )
        {
            int listeLength = filter.getListIdTypeSignalements( ).size( );
            Character [ ] array = new Character [ listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array [i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_LIST_TYPE_SIGNALEMENT, unionQuery ) );
        }

        // Report number
        if ( ( filter.getNumero( ) != null ) && !StringUtils.isBlank( filter.getNumero( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_NUMERO );
        }

        // Board
        if ( filter.getIdDirection( ) > 0 )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_DIRECTION );
        }

        // District
        if ( filter.getIdArrondissement( ) > 0 )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_ARRONDISSEMENT );
        }
        if ( ( filter.getListIdArrondissements( ) != null ) && !filter.getListIdArrondissements( ).isEmpty( ) )
        {
            int listeLength = filter.getListIdArrondissements( ).size( );
            Character [ ] array = new Character [ listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array [i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_LIST_ARRONDISSEMENT, unionQuery ) );
        }

        // Neighborhood
        if ( ( filter.getListIdQuartier( ) != null ) && !filter.getListIdQuartier( ).isEmpty( ) )
        {
            int listeLength = filter.getListIdQuartier( ).size( );
            Character [ ] array = new Character [ listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array [i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_LIST_QUARTIER, unionQuery ) );
        }

        // SECTOR
        if ( filter.getIdSector( ) > 0 )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_SECTOR );
        }

        // Allowed sectors

        if ( !filter.getListIdUnit( ).isEmpty( ) )
        {
            int listeLength = filter.getListIdUnit( ).size( );
            Character [ ] array = new Character [ listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array [i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_SECTOR_ALLOWED, unionQuery ) );
        }
        else
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( "1 = 2" );
        }

        // Address
        if ( bHasFilterAdress )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_ADRESSE );
        }

        // Mail
        if ( bHasFilerMail )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_MAIL );
        }

        // Commentary
        if ( ( filter.getCommentaire( ) != null ) && !StringUtils.isBlank( filter.getCommentaire( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_COMMENTAIRE );
        }

        if ( ( filter.getCommentaireAgentTerrain( ) != null ) && !StringUtils.isBlank( filter.getCommentaireAgentTerrain( ) ) )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_COMMENTAIRE_AGENT_TERRAIN );
        }

        // Creation date
        boolean dateBeginNotEmpty = ( filter.getDateBegin( ) != null ) && !StringUtils.isBlank( filter.getDateBegin( ) );
        boolean dateEndNotEmpty = ( filter.getDateEnd( ) != null ) && !StringUtils.isBlank( filter.getDateEnd( ) );

        if ( dateBeginNotEmpty )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_DATE_BEGIN );
        }

        if ( dateEndNotEmpty )
        {
            nIndex = addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( SQL_QUERY_ADD_FILTER_DATE_END );
        }

        // State
        if ( filter.getEtats( ) != null )
        {
            int i = filter.getEtats( ).size( );

            for ( EtatSignalement etatSign : filter.getEtats( ) )
            {
                if ( etatSign.getCoche( ) )
                {

                    if ( nIndex == 1 )
                    {
                        sbSQL.append( SQL_WHERE );
                    }
                    else
                    {
                        if ( i == filter.getEtats( ).size( ) )
                        {
                            sbSQL.append( SQL_AND );
                        }
                        else
                        {
                            sbSQL.append( SQL_OR );
                        }

                    }
                    nIndex++;

                    if ( i == filter.getEtats( ).size( ) )
                    {
                        sbSQL.append( " ( " );
                    }
                    sbSQL.append( SQL_QUERY_ADD_FILTER_ETAT );
                    i--;
                    if ( i < 1 )
                    {

                        sbSQL.append( " ) " );
                    }

                }

            }
        }

        // ADDING CATEGORY FILTER
        if ( bHasFilterCategory )
        {
            int listeLength = filter.getListIdCategories( ).size( );
            Character [ ] array = new Character [ listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array [i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            addSQLWhereOr( false, sbSQL, nIndex );
            sbSQL.append( MessageFormat.format( SQL_QUERY_ADD_FILTER_CATEGORY, unionQuery ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertMessageCreationSignalement( String messageCreation )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_MESSAGE_CREATION );
        daoUtil.setString( 1, messageCreation );
        daoUtil.executeQuery( );
        daoUtil.close( );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMessageCreationSignalement( String messageCreation )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_MESSAGE_CREATION );
        daoUtil.setString( 1, messageCreation );
        daoUtil.executeUpdate( );
        daoUtil.close( );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String loadMessageCreationSignalement( )
    {
        String strMessageCreation = StringUtils.EMPTY;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_MESSAGE_CREATION );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            strMessageCreation = daoUtil.getString( 1 );
        }
        daoUtil.close( );

        return strMessageCreation;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMessageCreationSignalement( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_MESSAGE_CREATION );
        daoUtil.executeUpdate( );
        daoUtil.close( );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void incrementFelicitationsByIdSignalement( int idSignalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FELICITATIONS_INCREMENT );
        int nIndex = 1;
        daoUtil.setInt( nIndex, idSignalement );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DashboardSignalementDTO> findByDashboardFilter( SignalementDashboardFilter filter, Plugin pluginSignalement )
    {
        StringBuilder query = new StringBuilder( );
        query.append( SQL_SELECT );
        query.append( "s.id_signalement" ).append( COMA_SEPARATOR );
        query.append( "s.date_creation" ).append( COMA_SEPARATOR );
        query.append( "id_state" ).append( COMA_SEPARATOR );
        query.append( "date_prevue_traitement" ).append( COMA_SEPARATOR );
        query.append( "date_mise_surveillance" );

        /// FROM
        query.append( " FROM " );
        query.append( "signalement_signalement s" );

        // JOIN
        query.append( SQL_QUERY_INNER_JOIN );
        query.append( " workflow_resource_workflow wrw " );
        query.append( " ON " );
        query.append( " wrw.id_resource = s.id_signalement " );

        query.append( SQL_QUERY_INNER_JOIN );
        query.append( "v_signalement_type_signalement_with_parents_links vstsawpl" );
        query.append( " ON " );
        query.append( " s.fk_id_type_signalement = vstsawpl.id_type_signalement " );
        query.append( SQL_QUERY_AND );
        query.append( " vstsawpl.is_parent_a_category = 1 " );

        query.append( SQL_QUERY_INNER_JOIN );
        query.append( " unittree_unit_sector uus" );
        query.append( " ON " );
        query.append( " s.fk_id_sector = uus.id_sector" );

        int nIndex = 1;

        nIndex = addSQLWhereOr( false, query, nIndex );
        query.append( " wrw.resource_type = ? " );
        nIndex = addSQLWhereOr( false, query, nIndex );
        query.append( " wrw.id_workflow = ? " );

        // FILTER BY PERIOD
        DashboardPeriod period = filter.getDashboardPeriod( );
        Integer higherBound = null;
        Integer lowerBound = null;

        if ( period != null )
        {
            higherBound = period.getHigherBound( );
            lowerBound = period.getLowerBound( );

            if ( higherBound != null )
            {
                // < à higherBound
                nIndex = addSQLWhereOr( false, query, nIndex );
                query.append( " date_trunc('day', s.date_creation) < ? " );
            }

            if ( lowerBound != null )
            {
                // > à lowerBound
                nIndex = addSQLWhereOr( false, query, nIndex );
                query.append( " date_trunc('day', s.date_creation) >= ? " );
            }
        }

        // FILTER BY UNIT (mandatory)
        nIndex = addSQLWhereOr( false, query, nIndex );
        query.append( SQL_QUERY_ADD_FILTER_DIRECTION );

        // FILTER BY CATEGORY
        if ( !ArrayUtils.isEmpty( filter.getCategoryIds( ) ) )
        {
            int listeLength = filter.getCategoryIds( ).length;
            Character [ ] array = new Character [ listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array [i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            nIndex = addSQLWhereOr( false, query, nIndex );
            query.append( MessageFormat.format( " vstsawpl.id_parent IN ({0}) ", unionQuery ) );
        }

        // FILTER BY DISTRICT
        if ( !ArrayUtils.isEmpty( filter.getArrondissementIds( ) ) )
        {
            int listeLength = filter.getArrondissementIds( ).length;
            Character [ ] array = new Character [ listeLength];
            for ( int i = 0; i < listeLength; i++ )
            {
                array [i] = '?';
            }
            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );
            addSQLWhereOr( false, query, nIndex );
            query.append( MessageFormat.format( " fk_id_arrondissement IN ({0}) ", unionQuery ) );
        }

        query.append( " ORDER BY id_state ASC, date_creation DESC " );

        DAOUtil daoUtil = new DAOUtil( query.toString( ) );

        nIndex = 1;

        daoUtil.setString( nIndex++, Signalement.WORKFLOW_RESOURCE_TYPE );
        daoUtil.setInt( nIndex++, SignalementConstants.SIGNALEMENT_WORKFLOW_ID );

        // Adding search arguments
        if ( period != null )
        {
            if ( higherBound != null )
            {
                LocalDate higherBoundDate = LocalDate.now( );
                higherBoundDate = higherBoundDate.plus( higherBound, ChronoUnit.MONTHS );
                daoUtil.setDate( nIndex++, java.sql.Date.valueOf( higherBoundDate ) );
            }

            if ( lowerBound != null )
            {
                // manage current month
                if ( lowerBound == -1 )
                {
                    lowerBound = lowerBound + 1;
                }
                LocalDate lowerBoundDate = LocalDate.now( );
                lowerBoundDate = lowerBoundDate.plus( lowerBound, ChronoUnit.MONTHS );
                // current month = first day of the month
                if ( lowerBound == 0 )
                {
                    lowerBoundDate = lowerBoundDate.withDayOfMonth( 1 );
                }
                daoUtil.setDate( nIndex++, java.sql.Date.valueOf( lowerBoundDate ) );
            }
        }

        // Entities
        daoUtil.setInt( nIndex++, filter.getUnitId( ) );

        // Categories
        if ( !ArrayUtils.isEmpty( filter.getCategoryIds( ) ) )
        {
            for ( Integer categoryId : filter.getCategoryIds( ) )
            {
                daoUtil.setInt( nIndex++, categoryId );
            }
        }

        // Districts
        if ( !ArrayUtils.isEmpty( filter.getArrondissementIds( ) ) )
        {
            for ( Integer arrondissementId : filter.getArrondissementIds( ) )
            {
                daoUtil.setInt( nIndex++, arrondissementId );
            }
        }

        daoUtil.executeQuery( );

        return fillDashboardDTO( daoUtil );
    }

    private List<DashboardSignalementDTO> fillDashboardDTO( DAOUtil daoUtil )
    {

        List<DashboardSignalementDTO> dashboardSigList = new ArrayList<>( );
        while ( daoUtil.next( ) )
        {
            int nIndex = 1;
            DashboardSignalementDTO dashboardSigDTO = new DashboardSignalementDTO( );
            dashboardSigDTO.setIdSignalement( daoUtil.getInt( nIndex++ ) );
            java.sql.Date creationDate = daoUtil.getDate( nIndex++ );
            if ( null != creationDate )
            {
                LocalDate localDate = creationDate.toLocalDate( );
                dashboardSigDTO.setCreationDate( localDate );
            }
            dashboardSigDTO.setIdStatus( daoUtil.getInt( nIndex++ ) );
            java.sql.Date datePrevueTraitement = daoUtil.getDate( nIndex++ );
            if ( null != datePrevueTraitement )
            {
                LocalDate localDate = datePrevueTraitement.toLocalDate( );
                dashboardSigDTO.setDatePrevueTraitement( localDate );
            }
            java.sql.Date dateMiseSurveillance = daoUtil.getDate( nIndex );
            if ( null != dateMiseSurveillance )
            {
                LocalDate localDate = dateMiseSurveillance.toLocalDate( );
                dashboardSigDTO.setDateMiseEnSurveillance( localDate );
            }
            dashboardSigList.add( dashboardSigDTO );
        }

        daoUtil.close( );

        return dashboardSigList;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getIdsSignalementByFilter( SignalementFilter filter, Plugin plugin )
    {
        StringBuilder sbSQL = new StringBuilder( buildSQLQueryForIds( filter ) );

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );
        setFilterValues( filter, daoUtil );

        daoUtil.executeQuery( );

        List<Integer> signalementsIds = new ArrayList<>( );

        while ( daoUtil.next( ) )
        {
            signalementsIds.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.close( );

        return signalementsIds;
    }

    /**
     * Build the query to get the reports ids matching the filter.
     *
     * @param filter
     *            the report filter
     * @return the query
     */
    private String buildSQLQueryForIds( SignalementFilter filter )
    {
        StringBuilder sbSQL = new StringBuilder( SQL_SELECT_ID_SIGNALEMENT );
        addFilterCriterias( filter, sbSQL );
        return sbSQL.toString( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMiseEnSurveillanceDate( int idSignalement, String dateMiseEnSurveillance )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_DATE_MISE_SURVEILLANCE );
        int nIndex = 1;
        daoUtil.setDate( nIndex++, DateUtil.formatDateSql( dateMiseEnSurveillance, Locale.FRENCH ) );
        daoUtil.setInt( nIndex, idSignalement );
        daoUtil.executeUpdate( );
        daoUtil.close( );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDateRejet( int idSignalement, String dateRejet )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_DATE_REJET );
        int nIndex = 1;
        daoUtil.setDate( nIndex++, DateUtil.formatDateSql( dateRejet, Locale.FRENCH ) );
        daoUtil.setInt( nIndex, idSignalement );
        daoUtil.executeUpdate( );
        daoUtil.close( );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> findIdsSingalementForWSPartnerDeamon( int signalementState, int nbDays )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ID_SIGNALEMENT_FOR_PARTNER_DEAMON.replace( "NB_DAYS", String.valueOf( nbDays ) ) );
        daoUtil.setInt( 1, signalementState );

        daoUtil.executeQuery( );

        List<Integer> signalementsIds = new ArrayList<>( );

        while ( daoUtil.next( ) )
        {
            signalementsIds.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.close( );

        return signalementsIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getIdMailServiceFait( Long idSignalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ID_MAIL_BY_ID );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, idSignalement );
        daoUtil.setLong( nIndex, idSignalement );
        daoUtil.executeQuery( );
        Integer idMail = null;
        while ( daoUtil.next( ) )
        {
            idMail = daoUtil.getInt( 1 );
        }

        daoUtil.close( );
        return idMail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveRequalification( long lIdSignalement, Integer idTypeSignalement, String adresse, Integer idSector, Integer idTask,
            String commentaireAgentTerrain )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_REQUALIFICATION );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, lIdSignalement );
        daoUtil.setInt( nIndex++, idTypeSignalement );
        daoUtil.setString( nIndex++, adresse );
        daoUtil.setInt( nIndex++, idSector );
        daoUtil.setTimestamp( nIndex++, new Timestamp( Calendar.getInstance( ).getTimeInMillis( ) ) );
        daoUtil.setInt( nIndex++, idTask );
        daoUtil.setString( nIndex, commentaireAgentTerrain );

        daoUtil.executeUpdate( );

        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateRequalification( long lIdSignalement, int idTask, int idHistory )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_REQUALIFICATION );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, idHistory );
        daoUtil.setLong( nIndex++, lIdSignalement );
        daoUtil.setInt( nIndex, idTask );
        daoUtil.executeUpdate( );

        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateRequalificationHistoryTask( long lIdSignalement, int idTask, int idHistory )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_REQUALIFICATION_HISTORY_TASK );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, idHistory );
        daoUtil.setInt( nIndex++, idTask );
        daoUtil.setLong( nIndex, lIdSignalement );

        daoUtil.executeUpdate( );

        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SignalementRequalification> getRequalification( long lIdSignalement )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_REQUALIFICATION );
        daoUtil.setLong( 1, lIdSignalement );

        daoUtil.executeQuery( );

        List<SignalementRequalification> listRequalification = new ArrayList<>( );

        int nIndex;

        while ( daoUtil.next( ) )
        {
            nIndex = 1;
            SignalementRequalification signalementRequalification = new SignalementRequalification( );
            signalementRequalification.setIdSignalement( daoUtil.getLong( nIndex++ ) );
            signalementRequalification.setIdTypeSignalement( daoUtil.getInt( nIndex++ ) );
            signalementRequalification.setAdresse( daoUtil.getString( nIndex++ ) );
            signalementRequalification.setIdSector( daoUtil.getInt( nIndex++ ) );
            signalementRequalification.setDateRequalification( DateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );
            signalementRequalification.setIdTask( daoUtil.getInt( nIndex ) );

            listRequalification.add( signalementRequalification );
        }

        daoUtil.close( );

        return listRequalification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SignalementRequalification getSignalementRequalificationByTaskHistory( int idHistory, int idTask )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_REQUALIFICATION_BY_ID_TASK_HISTORY );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, idHistory );
        daoUtil.setInt( nIndex, idTask );

        daoUtil.executeQuery( );

        SignalementRequalification signalementRequalification = new SignalementRequalification( );

        while ( daoUtil.next( ) )
        {
            nIndex = 1;
            signalementRequalification = new SignalementRequalification( );
            signalementRequalification.setIdSignalement( daoUtil.getLong( nIndex++ ) );
            signalementRequalification.setIdTypeSignalement( daoUtil.getInt( nIndex++ ) );
            signalementRequalification.setAdresse( daoUtil.getString( nIndex++ ) );
            signalementRequalification.setIdSector( daoUtil.getInt( nIndex++ ) );
            signalementRequalification.setDateRequalification( DateUtils.getDateFr( daoUtil.getDate( nIndex++ ) ) );
            signalementRequalification.setIdTask( daoUtil.getInt( nIndex++ ) );
            signalementRequalification.setCommentaireAgentTerrain( daoUtil.getString( nIndex ) );
        }

        daoUtil.close( );

        return signalementRequalification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, Map<Integer, Integer>> getSignalementsTDB( TableauDeBordFilter tableauDeBordFilter )
    {
        // Les signalement sont stockés dans une map par statut, puis dans une sous map par tranche de date de création
        Map<Integer, Map<Integer, Integer>> signalementsMap = new HashMap<>( );

        StringBuilder query = new StringBuilder( );
        query.append( MessageFormat.format( SQL_QUERY_GET_SIGNALEMENT_TDT_SELECT, PERIODE_MAP.get( tableauDeBordFilter.getPeriodId( ) ) ) );

        int nIndex = 1;

        if ( tableauDeBordFilter.getUnitId( ) != null )
        {
            query.append( " and uus.id_unit = " + tableauDeBordFilter.getUnitId( ) );
        }

        if ( tableauDeBordFilter.getArrondissementIds( ) != null )
        {
            query.append( " and sig.fk_id_arrondissement in ( " );
            query.append( StringUtils.join( tableauDeBordFilter.getArrondissementIds( ), "," ) );
            query.append( " ) " );
        }

        if ( tableauDeBordFilter.getCategoryIds( ) != null )
        {
            query.append( " and sig.fk_id_type_signalement in ( " );
            query.append( StringUtils.join( tableauDeBordFilter.getCategoryIds( ), "," ) );
            query.append( " ) " );
        }

        query.append( SQL_QUERY_GET_SIGNALEMENT_TDT_GROUP );

        DAOUtil daoUtil = new DAOUtil( query.toString( ) );

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            nIndex = 1;

            // Si le statut n'est pas présent dans la map on l'ajoute
            Integer statut = daoUtil.getInt( nIndex++ );
            signalementsMap.computeIfAbsent( statut, k -> new HashMap<>( ) );
            // Ajout des signalement
            Integer trancheCreation = daoUtil.getInt( nIndex++ );
            Integer nbSignalement = daoUtil.getInt( nIndex );

            signalementsMap.get( statut ).put( trancheCreation, nbSignalement );
        }

        daoUtil.close( );

        return signalementsMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getIdSignalementsTDB( TableauDeBordFilter tableauDeBordFilter )
    {
        // Les signalement sont stockés dans une map par statut, puis dans une sous map par tranche de date de création
        List<Integer> ids = new ArrayList<>( );

        StringBuilder query = new StringBuilder( );
        query.append( SQL_QUERY_GET_ID_SIGNALEMENT_TDT_SELECT );

        if ( tableauDeBordFilter.getTrancheId( ) != null )
        {
            addQueryTranche( tableauDeBordFilter, query );
        }
        else
        {
            // Utilisation de la période
            query.append( MessageFormat.format( SQL_WHERE_DATE_CREATION, PERIODE_MAP.get( tableauDeBordFilter.getPeriodId( ) ) ) );
        }

        if ( tableauDeBordFilter.getUnitId( ) != null )
        {
            query.append( " and uus.id_unit = " + tableauDeBordFilter.getUnitId( ) );
        }

        if ( tableauDeBordFilter.getArrondissementIds( ) != null )
        {
            query.append( " and sig.fk_id_arrondissement in ( " );
            query.append( StringUtils.join( tableauDeBordFilter.getArrondissementIds( ), "," ) );
            query.append( " ) " );
        }

        if ( tableauDeBordFilter.getCategoryId( ) != null )
        {
            query.append(
                    " and sig.fk_id_type_signalement in ( SELECT id_type_signalement FROM v_signalement_type_signalement_with_parents_links WHERE id_parent = "
                            + tableauDeBordFilter.getCategoryId( ) + " ) " );
        }

        if ( tableauDeBordFilter.getState( ) != null )
        {
            query.append( " and state.id_state =  " + tableauDeBordFilter.getState( ) );
        }

        query.append( " order by id_signalement desc " );
        DAOUtil daoUtil = new DAOUtil( query.toString( ) );

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            ids.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.close( );

        return ids;
    }

    /**
     * Add build query part for tranche.
     *
     * @param tableauDeBordFilter
     *            filter
     * @param query
     *            query
     */
    private void addQueryTranche( TableauDeBordFilter tableauDeBordFilter, StringBuilder query )
    {

        boolean isProgramme = ( ( tableauDeBordFilter.getState( ).intValue( ) == SignalementConstants.ID_STATE_PROGRAMME.intValue( ) )
                || ( tableauDeBordFilter.getState( ).intValue( ) == SignalementConstants.ID_STATE_PROGRAMME_PRESTATAIRE.intValue( ) ) );

        if ( isProgramme )
        {
            // utilisation de la période
            query.append( MessageFormat.format( SQL_WHERE_DATE_CREATION, PERIODE_MAP.get( tableauDeBordFilter.getPeriodId( ) ) ) );
        }

        switch( tableauDeBordFilter.getTrancheId( ) )
        {
            case 2:
                if ( isProgramme )
                {
                    // A echeance dans 48h
                    query.append( " and date_prevue_traitement > now()" );
                    query.append( " and date_prevue_traitement < (now() + '2 days'::interval)" );
                }
                else
                {
                    // moins de 48h
                    query.append( "where date_creation > (now() - '2 days'::interval) " );
                }
                break;
            case 1:
                if ( isProgramme )
                {
                    // retard de 10j ou moins
                    query.append( " and date_prevue_traitement > (now()- '11 days'::interval)" );
                    query.append( " and date_prevue_traitement < now()" );
                }
                else
                {
                    // moins de 10j
                    query.append( "where date_creation > (now() - '10 days'::interval) " );
                }
                break;
            default:
                if ( isProgramme )
                {
                    // retard de + 10 jours
                    query.append( " and date_prevue_traitement < (now()- '11 days'::interval)" );
                }
                else
                {
                    // utilisation de la période
                    query.append( MessageFormat.format( SQL_WHERE_DATE_CREATION, PERIODE_MAP.get( tableauDeBordFilter.getPeriodId( ) ) ) );
                    // plus de 10j
                    query.append( " and date_creation < (now()- '10 DAY days'::interval) " );
                }
                break;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCommentaireAgent( String commentaireAgent, long lIdSignalement )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_COMMENT_AGENT );
        int nIndex = 1;
        daoUtil.setString( nIndex++, commentaireAgent );
        daoUtil.setLong( nIndex, lIdSignalement );

        daoUtil.executeUpdate( );

        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> findAnoWithoutState( )
    {
        List<Long> listAnomaliesId = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ANO_WITHOUT_STATES );

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            listAnomaliesId.add( daoUtil.getLong( 1 ) );
        }

        daoUtil.close( );

        return listAnomaliesId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Integer> getRepartitionServiceFaitMasse( ServiceFaitMasseFilter filter )
    {
        Map<String, Integer> repartition = new HashMap<>( );
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_GET_REPARTITION_SERVICE_FAIT_MASSE );

        // Ajout des conditions
        getConditionsServiceFaitMasse( filter, sbSQL );
        sbSQL.append( " group by type.libelle" );

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ) );

        // Ajout des valeurs aux conditions
        setValuesToConditions( daoUtil, filter, false );

        // Execution de la requête
        daoUtil.executeQuery( );

        // Récupération des résultats
        while ( daoUtil.next( ) )
        {
            int nIndex = 1;
            repartition.put( daoUtil.getString( nIndex++ ), daoUtil.getInt( nIndex ) );
        }

        daoUtil.close( );

        return repartition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addServiceFaitHistoMasse( ServiceFaitMasseFilter filter )
    {
        // Début de la requete d'insert
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_INSERT_HISTORY_SERVICE_FAIT_MASSE_PART_1 );

        // Ajout de l'id des signalements via le filtre
        sbSQL.append( SQL_QUERY_GET_ID_SIGNALEMENT_SERVICE_FAIT_MASSE );

        // Ajout des conditions
        getConditionsServiceFaitMasse( filter, sbSQL );

        // Fin de la requete d'insert
        sbSQL.append( SQL_QUERY_INSERT_HISTORY_SERVICE_FAIT_MASSE_PART_2 );

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ) );

        // Ajout des valeurs aux conditions
        setValuesToConditions( daoUtil, filter, true );

        // Execution de la requête
        daoUtil.executeUpdate( );

        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateStateServiceFaitMasse( ServiceFaitMasseFilter filter )
    {
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_UPDATE_STATE_SERVICE_FAIT_MASSE );
        sbSQL.append( " where id_resource in ( " );

        // Ajout de l'id des signalements via le filtre
        sbSQL.append( SQL_QUERY_GET_ID_SIGNALEMENT_SERVICE_FAIT_MASSE );

        // Ajout des conditions
        getConditionsServiceFaitMasse( filter, sbSQL );

        sbSQL.append( ")" );

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ) );

        // Ajout des valeurs aux conditions
        setValuesToConditions( daoUtil, filter, false );

        // Execution de la requête
        daoUtil.executeUpdate( );

        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDatePassageServiceFaitMasse( ServiceFaitMasseFilter filter )
    {
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_UPDATE_DATE_PASSAGE_FAIT_MASSE );
        sbSQL.append( " where id_signalement in ( " );

        // Ajout de l'id des signalements via le filtre
        sbSQL.append( SQL_QUERY_GET_ID_SIGNALEMENT_SERVICE_FAIT_MASSE );

        // Ajout des conditions
        getConditionsServiceFaitMasse( filter, sbSQL );

        sbSQL.append( ")" );

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ) );

        // Ajout des valeurs aux conditions
        setValuesToConditions( daoUtil, filter, false );

        // Execution de la requête
        daoUtil.executeUpdate( );

        daoUtil.close( );
    }

    /**
     * Gets the conditions service fait masse.
     *
     * @param filter
     *            the filter
     * @param sbSQL
     *            the sb SQL
     * @return the conditions service fait masse
     */
    private void getConditionsServiceFaitMasse( ServiceFaitMasseFilter filter, StringBuilder sbSQL )
    {
        boolean dateBeginNotEmpty = ( filter.getDateBegin( ) != null ) && !StringUtils.isBlank( filter.getDateBegin( ) );
        boolean dateEndNotEmpty = ( filter.getDateEnd( ) != null ) && !StringUtils.isBlank( filter.getDateEnd( ) );
        boolean isStateNotEmpty = !ArrayUtils.isEmpty( filter.getIdEtats( ) );
        boolean isTypeAnoNotEmpty = !ArrayUtils.isEmpty( filter.getIdTypeSignalements( ) );

        List<String> conditionsList = new ArrayList<>( );

        if ( dateBeginNotEmpty )
        {
            conditionsList.add( SQL_QUERY_ADD_FILTER_DATE_BEGIN );
        }

        if ( dateEndNotEmpty )
        {
            conditionsList.add( SQL_QUERY_ADD_FILTER_DATE_END );
        }

        if ( isStateNotEmpty )
        {
            StringBuilder workflowCondition = new StringBuilder( " workflow.id_state in (" );

            int listeLength = filter.getIdEtats( ).length;
            Character [ ] array = new Character [ listeLength];

            Arrays.fill( array, '?' );

            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );

            workflowCondition.append( unionQuery ).append( " ) " );
            conditionsList.add( workflowCondition.toString( ) );

        }

        if ( isTypeAnoNotEmpty )
        {
            StringBuilder typeCondition = new StringBuilder( " signalement.fk_id_type_signalement in (" );

            int listeLength = filter.getIdTypeSignalements( ).length;
            Character [ ] array = new Character [ listeLength];

            Arrays.fill( array, '?' );

            String unionQuery = StringUtils.join( array, COMA_SEPARATOR );

            typeCondition.append( unionQuery ).append( " ) " );
            conditionsList.add( typeCondition.toString( ) );
        }

        if ( dateBeginNotEmpty || dateEndNotEmpty || isStateNotEmpty || isTypeAnoNotEmpty )
        {
            sbSQL.append( " where " ).append( StringUtils.join( conditionsList, " and " ) );
        }
    }

    /**
     * Sets the values to conditions.
     *
     * @param daoUtil
     *            the dao util
     * @param filter
     *            the filter
     * @param addComment
     *            the add comment
     */
    private void setValuesToConditions( DAOUtil daoUtil, ServiceFaitMasseFilter filter, boolean addComment )
    {
        int nIndex = 1;
        boolean dateBeginNotEmpty = ( filter.getDateBegin( ) != null ) && !StringUtils.isBlank( filter.getDateBegin( ) );
        boolean dateEndNotEmpty = ( filter.getDateEnd( ) != null ) && !StringUtils.isBlank( filter.getDateEnd( ) );
        boolean isStateNotEmpty = !ArrayUtils.isEmpty( filter.getIdEtats( ) );
        boolean isTypeAnoNotEmpty = !ArrayUtils.isEmpty( filter.getIdTypeSignalements( ) );

        if ( dateBeginNotEmpty )
        {
            daoUtil.setDate( nIndex++, DateUtil.formatDateSql( filter.getDateBegin( ), Locale.FRENCH ) );
        }

        if ( dateEndNotEmpty )
        {
            daoUtil.setDate( nIndex++, DateUtil.formatDateSql( filter.getDateEnd( ), Locale.FRENCH ) );
        }

        if ( isStateNotEmpty )
        {
            for ( Integer idEtat : filter.getIdEtats( ) )
            {
                daoUtil.setInt( nIndex++, idEtat );
            }
        }

        if ( isTypeAnoNotEmpty )
        {
            for ( Integer idType : filter.getIdTypeSignalements( ) )
            {
                daoUtil.setInt( nIndex++, idType );
            }
        }

        if ( addComment )
        {
            daoUtil.setString( nIndex, filter.getCommentaire( ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getSignalementsServiceProgrammeIds( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GET_SIGNALEMENTS_PROGRAMME );
        daoUtil.executeQuery( );

        List<Integer> ids = new ArrayList<>( );

        while ( daoUtil.next( ) )
        {
            ids.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.close( );

        return ids;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getSignalementsServiceProgrammeTierIds( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GET_SIGNALEMENTS_PROGRAMME_TIERS );
        daoUtil.executeQuery( );

        List<Integer> ids = new ArrayList<>( );

        while ( daoUtil.next( ) )
        {
            ids.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.close( );

        return ids;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String findLabelPrestataireSignalement( int idSignalement )
    {

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_LABEL_PRESTATAIRE ) ; )
        {

            int nIndex = 1;

            daoUtil.setInt( nIndex, idSignalement );

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                return daoUtil.getString( 1 );
            }
            return null;

        }

    }
}
