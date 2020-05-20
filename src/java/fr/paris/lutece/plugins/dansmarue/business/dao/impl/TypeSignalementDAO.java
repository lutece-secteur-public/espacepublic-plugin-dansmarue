/*
 * Copyright (c) 2002-2020, City of Paris
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

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.ITypeSignalementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Source;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalementWithDepth;
import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;
import fr.paris.lutece.plugins.dansmarue.service.dto.TypeSignalementDTO;
import fr.paris.lutece.plugins.dansmarue.service.impl.ImageObjetService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.TypeSignalementComparator;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.sql.DAOUtil;
import fr.paris.lutece.util.url.UrlItem;

/**
 * The Class TypeSignalementDAO.
 */
public class TypeSignalementDAO implements ITypeSignalementDAO
{

    /** The Constant SQL_WHERE_IS_NULL. */
    private static final String SQL_WHERE_IS_NULL                                          = "is null";

    /** The Constant SQL_WHERE_FK_ID_TYPE_SIGNALEMENT. */
    private static final String SQL_WHERE_FK_ID_TYPE_SIGNALEMENT                           = " AND fk_id_type_signalement ";

    // Constants
    /** The Constant SQL_QUERY_GET_ID_PARENT. */
    private static final String SQL_QUERY_GET_ID_PARENT                                    = "SELECT fk_id_type_signalement FROM signalement_type_signalement WHERE id_type_signalement =?";

    /** The Constant SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT. */
    private static final String SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT                      = "SELECT signalement_type_signalement.id_type_signalement, libelle, signalement_type_signalement.actif, signalement_type_signalement.fk_id_type_signalement, fk_id_unit, unittree_unit.id_parent, label, description, ordre, image_url, image_content, image_mime_type, vstswpl.id_parent, alias, alias_mobile, horsdmr, messagehorsdmr FROM signalement_type_signalement LEFT OUTER JOIN unittree_unit ON signalement_type_signalement.fk_id_unit = unittree_unit.id_unit LEFT JOIN v_signalement_type_signalement_with_parents_links vstswpl ON vstswpl.id_type_signalement= signalement_type_signalement.id_type_signalement AND vstswpl.is_parent_a_category=1 LEFT JOIN signalement_type_signalement_alias signalement_alias ON signalement_alias.fk_id_type_signalement = signalement_type_signalement.id_type_signalement ORDER BY ordre";

    /** The Constant SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT_WITHOUT_CHILDREN. */
    private static final String SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT_WITHOUT_CHILDREN     = "SELECT id_type_signalement, libelle, actif, signalement_type_signalement.fk_id_type_signalement, fk_id_unit, id_parent, label, description, alias, alias_mobile, type_signalement.isAgent FROM signalement_type_signalement, horsdmr, messagehorsdmr  LEFT JOIN signalement_type_signalement_alias signalement_alias ON signalement_alias.fk_id_type_signalement = signalement_type_signalement.id_type_signalement , unittree_unit WHERE id_unit=fk_id_unit AND id_type_signalement NOT IN (SELECT fk_id_type_signalement FROM signalement_type_signalement) ORDER BY libelle";

    /** The Constant SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT_WITHOUT_PARENT. */
    private static final String SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT_WITHOUT_PARENT       = "SELECT id_type_signalement, libelle, actif, type_signalement.fk_id_type_signalement, fk_id_unit, id_parent, label, description, ordre, image_url, image_content, image_mime_type, alias, alias_mobile, type_signalement.isAgent, horsdmr, messagehorsdmr  FROM signalement_type_signalement type_signalement LEFT OUTER JOIN unittree_unit unit ON type_signalement.fk_id_unit=unit.id_unit LEFT JOIN signalement_type_signalement_alias signalement_alias ON signalement_alias.fk_id_type_signalement = type_signalement.id_type_signalement WHERE type_signalement.fk_id_type_signalement IS NULL ORDER BY ordre";

    /** The Constant SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT_ACTIF_WITHOUT_PARENT. */
    private static final String SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT_ACTIF_WITHOUT_PARENT = "SELECT id_type_signalement, libelle, actif, type_signalement.fk_id_type_signalement, fk_id_unit, id_parent, label, description, ordre, image_url, image_content, image_mime_type, alias, alias_mobile, type_signalement.isAgent, horsdmr, messagehorsdmr  FROM signalement_type_signalement type_signalement LEFT OUTER JOIN unittree_unit unit ON type_signalement.fk_id_unit=unit.id_unit LEFT JOIN signalement_type_signalement_alias signalement_alias ON signalement_alias.fk_id_type_signalement = type_signalement.id_type_signalement WHERE type_signalement.fk_id_type_signalement IS NULL AND type_signalement.actif=1 ORDER BY ordre";

    /** The Constant SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT_ACTIF_WITH_DEPTH. */
    private static final String SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT_ACTIF_WITH_DEPTH     = "select id_type_signalement, fk_id_type_signalement, libelle, fk_id_unit, depth from ( with recursive descendants as( select id_type_signalement, fk_id_type_signalement, libelle, actif, fk_id_unit, 0 depth from signalement_type_signalement where fk_id_type_signalement is null union select p.id_type_signalement, p.fk_id_type_signalement, p.libelle, p.actif, p.fk_id_unit, d.depth + 1 from signalement_type_signalement p inner join descendants d on p.fk_id_type_signalement = d.id_type_signalement ) select p.id_type_signalement, p.fk_id_type_signalement, p.libelle, p.actif, p.fk_id_unit, d.depth from descendants d inner join descendants p on d.id_type_signalement = p.id_type_signalement order by d.depth, p.fk_id_type_signalement asc ) as query where actif = 1";

    /** The Constant SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT_BY_IS_AGENT. */
    private static final String SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT_BY_IS_AGENT          = "SELECT id_type_signalement, libelle, actif, type_signalement.fk_id_type_signalement, fk_id_unit, id_parent, label, description, ordre, image_url, image_content, image_mime_type, alias, alias_mobile, type_signalement.isAgent, horsdmr, messagehorsdmr FROM signalement_type_signalement type_signalement  LEFT OUTER JOIN unittree_unit unit ON type_signalement.fk_id_unit=unit.id_unit  LEFT JOIN signalement_type_signalement_alias signalement_alias ON signalement_alias.fk_id_type_signalement = type_signalement.id_type_signalement WHERE isagent = ? ";

    /** The Constant SQL_QUERY_SELECT_ALL. */
    private static final String SQL_QUERY_SELECT_ALL                                       = "SELECT id_type_signalement, libelle, actif, type_signalement.fk_id_type_signalement, fk_id_unit, id_parent, label, description, ordre, image_url, image_content, image_mime_type, alias, alias_mobile, type_signalement.isAgent, horsdmr, messagehorsdmr  FROM signalement_type_signalement type_signalement LEFT OUTER JOIN unittree_unit unit ON type_signalement.fk_id_unit=unit.id_unit LEFT JOIN signalement_type_signalement_alias signalement_alias ON signalement_alias.fk_id_type_signalement = type_signalement.id_type_signalement ORDER BY ordre";

    /** The Constant SQL_QUERY_SELECT_ALL_SOUS_TYPE_SIGNALEMENT. */
    private static final String SQL_QUERY_SELECT_ALL_SOUS_TYPE_SIGNALEMENT                 = "SELECT id_type_signalement, libelle, actif, type_signalement.fk_id_type_signalement, fk_id_unit, id_parent, label, description, ordre, image_url, image_content, image_mime_type, alias, alias_mobile, type_signalement.isAgent, horsdmr, messagehorsdmr  FROM signalement_type_signalement type_signalement LEFT OUTER JOIN unittree_unit unit ON type_signalement.fk_id_unit=unit.id_unit LEFT JOIN signalement_type_signalement_alias signalement_alias ON signalement_alias.fk_id_type_signalement = type_signalement.id_type_signalement WHERE type_signalement.fk_id_type_signalement = ? ORDER BY ordre";

    /** The Constant SQL_QUERY_SELECT_ALL_SOUS_TYPE_SIGNALEMENT. */
    private static final String SQL_QUERY_SELECT_ALL_SOUS_TYPE_SIGNALEMENT_ACTIFS          = "SELECT id_type_signalement, libelle, actif, type_signalement.fk_id_type_signalement, fk_id_unit, id_parent, label, description, ordre, image_url, image_content, image_mime_type, alias, alias_mobile, type_signalement.isAgent, horsdmr, messagehorsdmr  FROM signalement_type_signalement type_signalement LEFT OUTER JOIN unittree_unit unit ON type_signalement.fk_id_unit=unit.id_unit LEFT JOIN signalement_type_signalement_alias signalement_alias ON signalement_alias.fk_id_type_signalement = type_signalement.id_type_signalement WHERE type_signalement.fk_id_type_signalement = ? AND type_signalement.actif = 1 ORDER BY ordre";

    /** The Constant SQL_QUERY_SELECT. */
    private static final String SQL_QUERY_SELECT                                           = " SELECT type_signalement.id_type_signalement, type_signalement.libelle, type_signalement.actif, type_signalement.fk_id_type_signalement, type_signalement.fk_id_unit, type_signalement.isAgent, unit.id_parent, unit.label, unit.description, ordre, image_url, image_content, image_mime_type, alias, alias_mobile, horsdmr, messagehorsdmr  FROM signalement_type_signalement type_signalement LEFT OUTER JOIN unittree_unit unit ON type_signalement.fk_id_unit=unit.id_unit LEFT JOIN signalement_type_signalement_alias signalement_alias ON signalement_alias.fk_id_type_signalement = type_signalement.id_type_signalement WHERE id_type_signalement =? ORDER BY libelle";

    /** The Constant SQL_QUERY_GET_SIGNALEMENT_WITHOUT_PHOTO. */
    private static final String SQL_QUERY_GET_SIGNALEMENT_WITHOUT_PHOTO                    = "SELECT id_type_signalement, libelle, actif, signalement_type_signalement.fk_id_type_signalement, fk_id_unit, id_parent, label, description, ordre, image_url, alias, alias_mobile FROM signalement_type_signalement LEFT OUTER JOIN unittree_unit ON signalement_type_signalement.fk_id_unit = unittree_unit.id_unit LEFT JOIN signalement_type_signalement_alias signalement_alias ON signalement_alias.fk_id_type_signalement = signalement_type_signalement.id_type_signalement WHERE id_type_signalement = ?";

    /** The Constant SQL_QUERY_NEW_PK. */
    private static final String SQL_QUERY_NEW_PK                                           = " SELECT nextval('seq_signalement_type_signalement_id_type_signalement')";

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT                                           = " INSERT INTO signalement_type_signalement(id_type_signalement, libelle, actif, fk_id_type_signalement, fk_id_unit, ordre, image_url, image_content, image_mime_type, isAgent) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /** The Constant SQL_QUERY_INSERT_WITHOUT_PARENT. */
    private static final String SQL_QUERY_INSERT_WITHOUT_PARENT                            = "INSERT INTO signalement_type_signalement(id_type_signalement, libelle, actif, fk_id_unit, ordre, isagent, image_url, image_content, image_mime_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /** The Constant SQL_QUERY_DELETE. */
    private static final String SQL_QUERY_DELETE                                           = " DELETE FROM signalement_type_signalement WHERE id_type_signalement=? ";

    /** The Constant SQL_QUERY_UPDATE. */
    private static final String SQL_QUERY_UPDATE                                           = "UPDATE signalement_type_signalement SET id_type_signalement=?, libelle=?, actif=?, fk_id_type_signalement=?, fk_id_unit=?, image_url=?, image_content=?, image_mime_type=?, isAgent=?, horsdmr=?, messagehorsdmr=? WHERE id_type_signalement=?";

    /** The Constant SQL_QUERY_UPDATE. */
    private static final String SQL_QUERY_UPDATE_WITHOUT_IMAGE                             = "UPDATE signalement_type_signalement SET id_type_signalement=?, libelle=?, actif=?, fk_id_type_signalement=?, fk_id_unit=?, isAgent=?, horsdmr=?, messagehorsdmr=?  WHERE id_type_signalement=?";

    /** The Constant SQL_QUERY_UPDATE_WITHOUT_PARENT. */
    private static final String SQL_QUERY_UPDATE_WITHOUT_PARENT                            = "UPDATE signalement_type_signalement SET id_type_signalement=?, libelle=?, actif=?, fk_id_unit=?, image_url=?, image_content=?, image_mime_type=?, isAgent=?, horsdmr=?, messagehorsdmr=? WHERE id_type_signalement = ?";

    /** The Constant SQL_QUERY_UPDATE_WITHOUT_PARENT. */
    private static final String SQL_QUERY_UPDATE_WITHOUT_PARENT_WITHOUT_IMAGE              = "UPDATE signalement_type_signalement SET id_type_signalement=?, libelle=?, actif=?, fk_id_unit=?, isAgent=?, horsdmr=?, messagehorsdmr=? WHERE id_type_signalement = ?";

    /** The Constant SQL_QUERY_EXISTS_TYPE_SIGNALEMENT. */
    private static final String SQL_QUERY_EXISTS_TYPE_SIGNALEMENT                          = "SELECT id_type_signalement FROM signalement_type_signalement WHERE libelle=?";

    /** The Constant SQL_QUERY_SELECT_BY_ID. */
    private static final String SQL_QUERY_SELECT_BY_ID                                     = "SELECT id_type_signalement, libelle, ordre, image_url, image_content, image_mime_type, alias, alias_mobile, signalement_type_signalement.fk_id_type_signalement FROM signalement_type_signalement LEFT JOIN signalement_type_signalement_alias signalement_alias ON signalement_alias.fk_id_type_signalement = signalement_type_signalement.id_type_signalement WHERE id_type_signalement=?";

    /** The Constant SQL_SELECT_SIGNALEMENT_BY_ID_TYPE_SIGNALEMENT. */
    private static final String SQL_SELECT_SIGNALEMENT_BY_ID_TYPE_SIGNALEMENT              = "SELECT id_signalement FROM signalement_signalement WHERE fk_id_type_signalement=?;";

    /** The Constant SQL_QUERY_EXISTS_TYPE_SIGNALEMENT_WITH_ID. */
    private static final String SQL_QUERY_EXISTS_TYPE_SIGNALEMENT_WITH_ID                  = "SELECT id_type_signalement FROM signalement_type_signalement WHERE libelle=? AND NOT id_type_signalement=? ";

    /** The Constant SQL_QUERY_UPDATE_ORDRE. */
    private static final String SQL_QUERY_UPDATE_ORDRE                                     = "UPDATE signalement_type_signalement SET ordre=? WHERE id_type_signalement=?";

    /**  The Constant SQL_QUERY_FIND_IMAGE_BY_PRIMARY_KEY. */
    private static final String SQL_QUERY_FIND_IMAGE_BY_PRIMARY_KEY                        = "SELECT image_content, image_mime_type FROM signalement_type_signalement WHERE id_type_signalement=? ";

    /**  The Constant SQL_QUERY_FIND_LAST_VERSION_TYPE_SIGNALEMENT. */
    private static final String SQL_QUERY_FIND_LAST_VERSION_TYPE_SIGNALEMENT               = "SELECT version FROM signalement_type_signalement_version ORDER BY version DESC LIMIT 1;";

    /**  The Constant SQL_QUERY_UPDATE_VERSION_TYPE_SIGNALEMENT. */
    private static final String SQL_QUERY_ADD_NEW_VERSION_TYPE_SIGNALEMENT                 = "INSERT INTO signalement_type_signalement_version (version) VALUES ( ? );";

    /**  The Constant SQL_QUERY_INSERT_INTO_TYPE_SIGNALEMENT_ALIAS *. */
    private static final String SQL_QUERY_INSERT_TYPE_SIGNALEMENT_ALIAS                    = "INSERT INTO signalement_type_signalement_alias(fk_id_type_signalement,alias,alias_mobile) VALUES (?,?,?)";

    /**  The Constant SQL_QUERY_UPDATE_TYPE_SIGNALEMENT_ALIAS *. */
    private static final String SQL_QUERY_UPDATE_TYPE_SIGNALEMENT_ALIAS                    = "UPDATE signalement_type_signalement_alias SET alias = ?, alias_mobile=? WHERE fk_id_type_signalement=?";

    /**  The Constant SQL_QUERY_DELETE_TYPE_SIGNALEMENT_ALIAS *. */
    private static final String SQL_QUERY_DELETE_TYPE_SIGNALEMENT_ALIAS                    = "DELETE FROM signalement_type_signalement_alias WHERE fk_id_type_signalement=?";

    /**  The Constant SQL_QUERY_REFRESH_VIEW_TYPES_WITH_HIERARCHY *. */
    private static final String SQL_QUERY_REFRESH_VIEW_TYPES_WITH_PARENTS_LINKS            = "REFRESH MATERIALIZED VIEW v_signalement_type_signalement_with_parents_links";

    /** The Constant SQL_QUERY_CATEGORY_FROM_TYPE_ID. */
    private static final String SQL_QUERY_CATEGORY_FROM_TYPE_ID                            = "SELECT id_parent FROM v_signalement_type_signalement_with_parents_links WHERE id_type_signalement=? AND is_parent_a_category=1";

    /** The Constant SQL_QUERY_UPDATE_PARENT. */
    private static final String SQL_QUERY_UPDATE_PARENT                                    = "UPDATE signalement_type_signalement set fk_id_type_signalement=?, ordre= (select max(ordre)+1 from signalement_type_signalement where fk_id_type_signalement=?) where id_type_signalement=?";

    /** The Constant SQL_QUERY_SELECT_LAST_LEVEL_WITH_PARENT. */
    private static final String SQL_QUERY_SELECT_LAST_LEVEL_WITH_PARENT                    = "select id_type_signalement, libelle, nb_message, actif from "
            + " (WITH RECURSIVE t(id_type_signalement, libelle, fk_id_type_signalement, nb_message, actif) AS ("
            + " SELECT type1.id_type_signalement, type1.libelle::TEXT, type1.fk_id_type_signalement, "
            + " (select count(id_message) from signalement_message_typologie message where  message.fk_id_type_signalement = type1.id_type_signalement), actif "
            + " FROM signalement_type_signalement AS type1 " + " WHERE type1.fk_id_type_signalement IS NULL" + " UNION ALL"
            + " SELECT type2.id_type_signalement, recurs.libelle::TEXT || '->' || type2.libelle::TEXT, type2.fk_id_type_signalement, "
            + " (select count(id_message) from signalement_message_typologie message where  message.fk_id_type_signalement = type2.id_type_signalement), type2.actif "
            + " FROM signalement_type_signalement as type2, t as recurs" + " WHERE type2.fk_id_type_signalement =recurs.id_type_signalement" + " )"
            + " SELECT * FROM t where (select count(s2.id_type_signalement) from signalement_type_signalement s2 where s2.fk_id_type_signalement=t.id_type_signalement) = 0" + " order by libelle asc "
            + " ) as res";

    /** The Constant SQL_QUERY_SELECT_LAST_LEVEL_WITH_PARENT_NOT_IN_SOURCE. */
    private static final String SQL_QUERY_SELECT_LAST_LEVEL_WITH_PARENT_NOT_IN_SOURCE      = "select id_type_signalement, libelle, actif, fk_id_type_signalement from (select id_type_signalement, libelle, actif, fk_id_type_signalement from ( "
            + "with recursive t(id_type_signalement,libelle,fk_id_type_signalement,nb_message,actif) as( " + "select	type1.id_type_signalement,type1.libelle::text, type1.fk_id_type_signalement,( "
            + "select count( id_message ) from	signalement_message_typologie message where	message.fk_id_type_signalement = type1.id_type_signalement),actif "
            + "from signalement_type_signalement as type1 where type1.fk_id_type_signalement is null union all select type2.id_type_signalement, recurs.libelle::text || '->' || type2.libelle::text, "
            + "type2.fk_id_type_signalement,(select count( id_message ) from signalement_message_typologie message	where message.fk_id_type_signalement = type2.id_type_signalement), type2.actif "
            + "from signalement_type_signalement as type2, t as recurs	where type2.fk_id_type_signalement = recurs.id_type_signalement) select	* from	t where	(select	count( s2.id_type_signalement ) "
            + "from signalement_type_signalement s2 where s2.fk_id_type_signalement = t.id_type_signalement )= 0 order by libelle asc ) as res) as r "
            + "where id_type_signalement not in (select fk_id_type_signalement from signalement_type_signalement_source where id_source=?) and fk_id_type_signalement is not null";

    /** The Constant SQL_QUERY_INSERT_TYPE_SOURCE. */
    private static final String SQL_QUERY_INSERT_TYPE_SOURCE                               = "INSERT INTO public.signalement_type_signalement_source (fk_id_type_signalement, id_source) VALUES(?, ?)";

    /** The Constant SQL_QUERY_REMOVE_TYPE_SOURCE. */
    private static final String SQL_QUERY_REMOVE_TYPE_SOURCE                               = "DELETE FROM  public.signalement_type_signalement_source WHERE fk_id_type_signalement=? AND id_source=?";

    /** The Constant SQL_QUERY_SELECT_BY_SOURCE. */
    private static final String SQL_QUERY_SELECT_BY_SOURCE                                 = "SELECT id_type_signalement, libelle, actif, sig.fk_id_type_signalement, ordre, image_url, image_content, image_mime_type, isagent, horsdmr, messagehorsdmr  FROM signalement_type_signalement sig "
            + "inner join signalement_type_signalement_source s on s.fk_id_type_signalement = sig.id_type_signalement " + "where s.id_source=? and sig.actif=1";

    /** The Constant SQL_QUERY_SELECT_BY_SOURCE_WITH_FULL_LIBELLE. */
    private static final String SQL_QUERY_SELECT_BY_SOURCE_WITH_FULL_LIBELLE               = "select id_type_signalement, libelle, actif, fk_id_type_signalement from (select id_type_signalement, libelle, actif, fk_id_type_signalement from ( "
            + "with recursive t(id_type_signalement,libelle,fk_id_type_signalement,nb_message,actif) as( " + "select	type1.id_type_signalement,type1.libelle::text, type1.fk_id_type_signalement,( "
            + "select count( id_message ) from	signalement_message_typologie message where	message.fk_id_type_signalement = type1.id_type_signalement),actif "
            + "from signalement_type_signalement as type1 where type1.fk_id_type_signalement is null union all select type2.id_type_signalement, recurs.libelle::text || '->' || type2.libelle::text, "
            + "type2.fk_id_type_signalement,(select count( id_message ) from signalement_message_typologie message	where message.fk_id_type_signalement = type2.id_type_signalement), type2.actif "
            + "from signalement_type_signalement as type2, t as recurs	where type2.fk_id_type_signalement = recurs.id_type_signalement) select	* from	t where	(select	count( s2.id_type_signalement ) "
            + "from signalement_type_signalement s2 where s2.fk_id_type_signalement = t.id_type_signalement )= 0 order by libelle asc ) as res) as r "
            + "where id_type_signalement in (select fk_id_type_signalement from signalement_type_signalement_source where id_source=?) and fk_id_type_signalement is not null";

    /** The Constant SQL_QUERY_SELECT_TYPE_BY_ID_SIGNALEMENT. */
    private static final String SQL_QUERY_SELECT_TYPE_BY_ID_SIGNALEMENT                    = "select id_type_signalement, libelle, actif, fk_id_type_signalement, ordre, image_url, image_content, image_mime_type, isagent, horsdmr, messagehorsdmr from signalement_type_signalement where id_type_signalement=?";

    /** The Constant SQL_QUERY_LIST_IDS_CHILDREN. */
    private static final String SQL_QUERY_LIST_IDS_CHILDREN                                = "select distinct(s2.id_type_signalement) " + "from signalement_type_signalement s1 "
            + "join signalement_type_signalement s2 on s2.fk_id_type_signalement = s1.id_type_signalement " + "where s1.id_type_signalement in ({0})";

    /** The Constant SQL_QUERY_SELECT_SOURCE. */
    private static final String SQL_QUERY_SELECT_SOURCE                                    = "select id_source, libelle, description, commentaire from signalement_source order by id_source asc";

    /** The Constant SQL_QUERY_ADD_SOURCE. */
    private static final String SQL_QUERY_ADD_SOURCE                                       = "INSERT INTO signalement_source (libelle, description, commentaire) VALUES(?, ?, ?)";

    /** The Constant SQL_QUERY_REMOVE_SOURCE. */
    private static final String SQL_QUERY_REMOVE_SOURCE                                    = "DELETE FROM signalement_source WHERE id_source=?";

    /** The Constant SQL_QUERY_GET_SOURCE_BY_ID. */
    private static final String SQL_QUERY_GET_SOURCE_BY_ID                                 = "SELECT id_source, libelle, description, commentaire FROM signalement_source WHERE id_source=?";

    /** The Constant SQL_QUERY_UPDATE_SOURCE. */
    private static final String SQL_QUERY_UPDATE_SOURCE                                    = "UPDATE signalement_source SET description=?, commentaire=? WHERE id_source=?";

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSignalement getTypeSignalement( Integer nId )
    {
        List<TypeSignalement> allTypes;
        allTypes = getAllTypeSignalement( );

        if ( allTypes != null )
        {
            for ( TypeSignalement type : allTypes )
            {
                if ( type.getId( ).equals( nId ) )
                {
                    return type;
                }
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSignalement getTypeSignalementWithoutUnit( Integer nId )
    {
        List<TypeSignalement> allTypes;
        allTypes = getAllTypeSignalementEvenWithoutUnit( );

        if ( allTypes != null )
        {
            for ( TypeSignalement type : allTypes )
            {
                if ( type.getId( ).equals( nId ) )
                {
                    return type;
                }
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSignalement getTypeSignalementWithoutPhoto( Integer nId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GET_SIGNALEMENT_WITHOUT_PHOTO );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery( );
        TypeSignalement typeSignalement = null;
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            typeSignalement = new TypeSignalement( );
            typeSignalement.setId( daoUtil.getInt( nIndex++ ) );
            typeSignalement.setLibelle( daoUtil.getString( nIndex++ ) );
            typeSignalement.setActif( daoUtil.getBoolean( nIndex++ ) );

            // get the parent
            typeSignalement.setIdTypeSignalementParent( daoUtil.getInt( nIndex++ ) );

            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( nIndex++ ) );
            unit.setIdParent( daoUtil.getInt( nIndex++ ) );
            unit.setLabel( daoUtil.getString( nIndex++ ) );
            unit.setDescription( daoUtil.getString( nIndex++ ) );
            typeSignalement.setUnit( unit );

            typeSignalement.setOrdre( daoUtil.getInt( nIndex++ ) );

            typeSignalement.setImageUrl( daoUtil.getString( nIndex++ ) );

            typeSignalement.setAlias( daoUtil.getString( nIndex++ ) );
            typeSignalement.setAliasMobile( daoUtil.getString( nIndex ) );
        }

        daoUtil.close( );
        if ( ( typeSignalement != null ) && ( typeSignalement.getIdTypeSignalementParent( ) != null ) && ( typeSignalement.getIdTypeSignalementParent( ) > 0 ) )
        {
            typeSignalement.setTypeSignalementParent( getTypeSignalementWithoutPhoto( typeSignalement.getIdTypeSignalementParent( ) ) );
        }

        return typeSignalement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllTypeSignalementWithoutChildren( )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT_WITHOUT_CHILDREN );
        return getListTypeSignalement( daoUtil );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllTypeSignalementWithoutParent( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT_WITHOUT_PARENT );
        return getListTypeSignalement( daoUtil );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllTypeSignalementActifWithoutParent( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT_ACTIF_WITHOUT_PARENT );
        return getListTypeSignalement( daoUtil );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalementWithDepth> getAllTypeSignalementActifWithDepth( Integer unitId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT_ACTIF_WITH_DEPTH );

        List<TypeSignalementWithDepth> listResult = new ArrayList<>( );
        daoUtil.executeQuery( );
        int nIndex;
        while ( daoUtil.next( ) )
        {
            nIndex = 1;

            TypeSignalementWithDepth typeSignalementWithDepth = new TypeSignalementWithDepth( );

            typeSignalementWithDepth.setId( daoUtil.getInt( nIndex++ ) );
            typeSignalementWithDepth.setIdParent( daoUtil.getInt( nIndex++ ) );
            typeSignalementWithDepth.setLibelle( daoUtil.getString( nIndex++ ) );
            typeSignalementWithDepth.setIdUnit( daoUtil.getInt( nIndex++ ) );
            typeSignalementWithDepth.setDepth( daoUtil.getInt( nIndex ) );

            listResult.add( typeSignalementWithDepth );
        }

        daoUtil.close( );

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllTypeSignalementEvenWithoutUnit( )
    {
        Plugin pluginSignalement = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT, pluginSignalement );

        return getTypesWithParentLink( daoUtil, false );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllTypeSignalement( )
    {
        Plugin pluginSignalement = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT, pluginSignalement );

        return getTypesWithParentLink( daoUtil, true );

    }

    /**
     * Finds report types with their parent informations.
     *
     * @param daoUtil the dao util
     * @param linkedToUnit the linked to unit
     * @return a list of report types
     */
    private List<TypeSignalement> getTypesWithParentLink( DAOUtil daoUtil, boolean linkedToUnit )
    {

        List<TypeSignalement> listResult = new ArrayList<>( );

        Map<Integer, Object[]> mapParents = new HashMap<>( );

        daoUtil.executeQuery( );
        int nIndex;

        while ( daoUtil.next( ) )
        {
            nIndex = 1;
            TypeSignalement typeSignalement = new TypeSignalement( );
            typeSignalement.setId( daoUtil.getInt( nIndex++ ) );
            typeSignalement.setLibelle( daoUtil.getString( nIndex++ ) );
            typeSignalement.setActif( daoUtil.getBoolean( nIndex++ ) );

            // get the parent
            TypeSignalement typeSignalementParent = new TypeSignalement( );
            typeSignalementParent.setId( daoUtil.getInt( nIndex++ ) );
            typeSignalement.setTypeSignalementParent( typeSignalementParent );

            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( nIndex++ ) );
            unit.setIdParent( daoUtil.getInt( nIndex++ ) );
            unit.setLabel( daoUtil.getString( nIndex++ ) );
            unit.setDescription( daoUtil.getString( nIndex++ ) );
            typeSignalement.setUnit( unit );

            typeSignalement.setOrdre( daoUtil.getInt( nIndex++ ) );

            typeSignalement.setImageUrl( daoUtil.getString( nIndex++ ) );
            Object oImageContent = daoUtil.getBytes( nIndex++ );

            typeSignalement.setImage( new ImageResource( ) );
            typeSignalement.setImageContent( ( byte[] ) oImageContent );
            typeSignalement.setMimeType( daoUtil.getString( nIndex++ ) );

            typeSignalement.setIdCategory( daoUtil.getInt( nIndex++ ) );
            typeSignalement.setAlias( daoUtil.getString( nIndex++ ) );
            typeSignalement.setAliasMobile( daoUtil.getString( nIndex++ ) );

            typeSignalement.setHorsDMR( daoUtil.getBoolean( nIndex++ ) );
            typeSignalement.setMessageHorsDMR( daoUtil.getString( nIndex ) );

            mapParents.put( typeSignalement.getId( ), new Object[] { typeSignalement, 0L } );
        }

        daoUtil.close( );

        // here we have a list of all types of child or parent reports

        // we count the number of children in each element
        for ( Entry<Integer, Object[]> entrySet : mapParents.entrySet( ) )
        {
            Integer idTypeSignalementParent = ( ( TypeSignalement ) entrySet.getValue( )[0] ).getTypeSignalementParent( ).getId( );
            if ( idTypeSignalementParent != null )
            {
                Object[] objects = mapParents.get( idTypeSignalementParent );
                if ( objects != null )
                {
                    objects[1] = ( Long ) mapParents.get( idTypeSignalementParent )[1] + 1;
                }
            }
        }

        // children are assigned to parents by searching for elements that have not been counted as parents of another element
        // then by successive search of all the upper parents of each "leaf" element.
        for ( Entry<Integer, Object[]> entrySet : mapParents.entrySet( ) )
        {
            // if you are in the presence of a child (because there are no sons).
            if ( ( ( Long ) entrySet.getValue( )[1] ).equals( 0L ) )
            {
                TypeSignalement typeSignalement = ( TypeSignalement ) entrySet.getValue( )[0];
                Integer idTypeSignalementParent = typeSignalement.getTypeSignalementParent( ).getId( );

                Object[] objects = mapParents.get( idTypeSignalementParent );
                TypeSignalement typeSignalementParent;
                if ( objects != null )
                {
                    typeSignalementParent = ( TypeSignalement ) objects[0];
                }
                else
                {
                    typeSignalementParent = null;
                }

                // Only types attached to a board should be included in the list
                if ( !linkedToUnit || ( typeSignalement.getUnit( ).getIdUnit( ) != 0 ) )
                {
                    listResult.add( typeSignalement );
                }

                typeSignalement.setTypeSignalementParent( typeSignalementParent );
                while ( typeSignalementParent != null )
                {
                    TypeSignalement current = typeSignalementParent;

                    if ( ( typeSignalementParent.getTypeSignalementParent( ) != null ) && ( mapParents.get( typeSignalementParent.getTypeSignalementParent( ).getId( ) ) != null ) )
                    {
                        typeSignalementParent = ( TypeSignalement ) mapParents.get( typeSignalementParent.getTypeSignalementParent( ).getId( ) )[0];
                    }
                    else
                    {
                        typeSignalementParent = null;
                    }
                    current.setTypeSignalementParent( typeSignalementParent );
                }
            }
            else if ( !linkedToUnit )
            {
                // If you are not only looking for leafs (and therefore also categories, subcategories)
                TypeSignalement typeSignalement = ( TypeSignalement ) entrySet.getValue( )[0];
                listResult.add( typeSignalement );
            }
        }

        TypeSignalementComparator compareSignalements = new TypeSignalementComparator( );
        Collections.sort( listResult, compareSignalements );

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAll( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL );
        return getListTypeSignalement( daoUtil );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllSousTypeSignalement( Integer lIdTypeSignalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_SOUS_TYPE_SIGNALEMENT );
        daoUtil.setLong( 1, lIdTypeSignalement );
        return getListTypeSignalement( daoUtil );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllSousTypeSignalementActifs( Integer lIdTypeSignalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_SOUS_TYPE_SIGNALEMENT_ACTIFS );
        daoUtil.setLong( 1, lIdTypeSignalement );
        return getListTypeSignalement( daoUtil );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllTypeSignalementByIsAgent( boolean isAgent )
    {
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_ALL_TYPE_SIGNALEMENT_BY_IS_AGENT );
        if ( !isAgent )
        {
            sbSQL.append( " OR isagent is null" );
        }
        sbSQL.append( " ORDER BY ordre" );
        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ) );
        daoUtil.setBoolean( 1, isAgent );

        return getListTypeSignalement( daoUtil );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery( );
        Integer nKey = null;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 );
        }
        else
        {
            nKey = 1;
        }
        daoUtil.close( );
        return nKey.intValue( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Integer insert( TypeSignalement typeSignalement, Plugin plugin )
    {
        Integer nIndex = 1;
        Integer nIdTypeSignalement = newPrimaryKey( plugin );
        typeSignalement.setId( nIdTypeSignalement );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setInt( nIndex++, typeSignalement.getId( ) );
        daoUtil.setString( nIndex++, typeSignalement.getLibelle( ) );
        daoUtil.setBoolean( nIndex++, typeSignalement.getActif( ) );
        daoUtil.setInt( nIndex++, typeSignalement.getTypeSignalementParent( ).getId( ) );
        daoUtil.setInt( nIndex++, typeSignalement.getUnit( ).getIdUnit( ) );
        daoUtil.setInt( nIndex++, typeSignalement.getOrdre( ) );

        String strResourceType = ImageObjetService.getInstance( ).getResourceTypeId( );
        UrlItem url = new UrlItem( Parameters.IMAGE_SERVLET );
        url.addParameter( Parameters.RESOURCE_TYPE, strResourceType );
        url.addParameter( Parameters.RESOURCE_ID, Integer.toString( typeSignalement.getId( ) ) );
        typeSignalement.setImageUrl( url.getUrlWithEntity( ) );

        if ( typeSignalement.getImage( ) != null )
        {
            daoUtil.setString( nIndex++, typeSignalement.getImageUrl( ) );
            daoUtil.setBytes( nIndex++, typeSignalement.getImage( ).getImage( ) );
            daoUtil.setString( nIndex++, typeSignalement.getImage( ).getMimeType( ) );
        }
        else
        {
            byte[] baImageNull = null;
            daoUtil.setString( nIndex++, "" );
            daoUtil.setBytes( nIndex++, baImageNull );
            daoUtil.setString( nIndex++, "" );
        }

        daoUtil.setBoolean( nIndex, typeSignalement.getIsAgent( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );

        insertNewVersionTypeSignalement( findLastVersionTypeSignalement( ) );

        return nIdTypeSignalement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSignalement findByIdTypeSignalement( Integer nIdTypeSignalement )
    {
        TypeSignalement typeSignalement = null;
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ID );
        daoUtil.setInt( nIndex, nIdTypeSignalement );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            nIndex = 1;
            typeSignalement = new TypeSignalement( );
            typeSignalement.setId( daoUtil.getInt( nIndex++ ) );
            typeSignalement.setLibelle( daoUtil.getString( nIndex++ ) );
            typeSignalement.setOrdre( daoUtil.getInt( nIndex++ ) );
            typeSignalement.setImageUrl( daoUtil.getString( nIndex++ ) );
            Object oImageContent = daoUtil.getBytes( nIndex++ );

            typeSignalement.setImage( new ImageResource( ) );
            typeSignalement.setImageContent( ( byte[] ) oImageContent );
            typeSignalement.setMimeType( daoUtil.getString( nIndex++ ) );

            typeSignalement.setAlias( daoUtil.getString( nIndex++ ) );
            typeSignalement.setAliasMobile( daoUtil.getString( nIndex++ ) );
            typeSignalement.setIdTypeSignalementParent( daoUtil.getInt( nIndex ) );

        }

        daoUtil.close( );

        return typeSignalement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSignalement load( Integer nIdTypeSignalement, Plugin plugin )
    {
        TypeSignalement typeSignalement = null;
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setLong( nIndex, nIdTypeSignalement );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            nIndex = 1;
            typeSignalement = new TypeSignalement( );
            typeSignalement.setId( daoUtil.getInt( nIndex++ ) );
            typeSignalement.setLibelle( daoUtil.getString( nIndex++ ) );
            typeSignalement.setActif( daoUtil.getBoolean( nIndex++ ) );

            TypeSignalement typeSignalementParent = new TypeSignalement( );
            typeSignalementParent.setId( daoUtil.getInt( nIndex++ ) );
            typeSignalement.setTypeSignalementParent( typeSignalementParent );

            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( nIndex++ ) );

            typeSignalement.setIsAgent( daoUtil.getBoolean( nIndex++ ) );

            unit.setIdParent( daoUtil.getInt( nIndex++ ) );
            unit.setLabel( daoUtil.getString( nIndex++ ) );
            unit.setDescription( daoUtil.getString( nIndex++ ) );
            typeSignalement.setUnit( unit );

            typeSignalement.setOrdre( daoUtil.getInt( nIndex++ ) );
            typeSignalement.setImageUrl( daoUtil.getString( nIndex++ ) );
            Object oImageContent = daoUtil.getBytes( nIndex++ );

            typeSignalement.setImage( new ImageResource( ) );
            typeSignalement.setImageContent( ( byte[] ) oImageContent );
            typeSignalement.setMimeType( daoUtil.getString( nIndex++ ) );
            typeSignalement.setAlias( daoUtil.getString( nIndex++ ) );
            typeSignalement.setAliasMobile( daoUtil.getString( nIndex++ ) );

            typeSignalement.setHorsDMR( daoUtil.getBoolean( nIndex++ ) );
            typeSignalement.setMessageHorsDMR( daoUtil.getString( nIndex ) );
        }

        daoUtil.close( );

        return typeSignalement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( Integer nIdTypeSignalement, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdTypeSignalement );
        daoUtil.executeUpdate( );
        daoUtil.close( );

        insertNewVersionTypeSignalement( findLastVersionTypeSignalement( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSignalement getParent( Integer nIdTypeSignalement, Plugin plugin )
    {
        TypeSignalement typeSignalement = null;
        Integer nIdParent = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GET_ID_PARENT, plugin );
        daoUtil.setLong( 1, nIdTypeSignalement );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            nIdParent = daoUtil.getInt( 1 );
            typeSignalement = load( nIdParent, plugin );
        }

        daoUtil.close( );
        return typeSignalement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( TypeSignalement typeSignalement, Plugin plugin )
    {
        DAOUtil daoUtil;
        if ( typeSignalement.getImage( ) != null )
        {
            daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        }
        else
        {
            daoUtil = new DAOUtil( SQL_QUERY_UPDATE_WITHOUT_IMAGE, plugin );
        }
        int nIndex = 1;
        daoUtil.setInt( nIndex++, typeSignalement.getId( ) );
        daoUtil.setString( nIndex++, typeSignalement.getLibelle( ) );
        daoUtil.setBoolean( nIndex++, typeSignalement.getActif( ) );
        daoUtil.setInt( nIndex++, typeSignalement.getTypeSignalementParent( ).getId( ) );
        daoUtil.setInt( nIndex++, typeSignalement.getUnit( ).getIdUnit( ) );

        String strResourceType = ImageObjetService.getInstance( ).getResourceTypeId( );
        UrlItem url = new UrlItem( Parameters.IMAGE_SERVLET );
        url.addParameter( Parameters.RESOURCE_TYPE, strResourceType );
        url.addParameter( Parameters.RESOURCE_ID, Integer.toString( typeSignalement.getId( ) ) );
        typeSignalement.setImageUrl( url.getUrlWithEntity( ) );

        if ( typeSignalement.getImage( ) != null )
        {
            daoUtil.setString( nIndex++, typeSignalement.getImageUrl( ) );
            daoUtil.setBytes( nIndex++, typeSignalement.getImage( ).getImage( ) );
            daoUtil.setString( nIndex++, typeSignalement.getImage( ).getMimeType( ) );
        }

        daoUtil.setBoolean( nIndex++, typeSignalement.getIsAgent( ) );
        daoUtil.setBoolean( nIndex++, typeSignalement.isHorsDMR( ) );
        daoUtil.setString( nIndex++, typeSignalement.getMessageHorsDMR( ) );

        // WHERE
        daoUtil.setLong( nIndex, typeSignalement.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );

        insertNewVersionTypeSignalement( findLastVersionTypeSignalement( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsSaveTypeSignalement( TypeSignalement typeSignalement )
    {
        boolean existsTypeSignalement = false;

        DAOUtil daoUtil;

        if ( ( typeSignalement.getId( ) != null ) && ( typeSignalement.getId( ) > 0 ) )
        {
            StringBuilder request = new StringBuilder( );
            request.append( SQL_QUERY_EXISTS_TYPE_SIGNALEMENT_WITH_ID );

            if ( ( typeSignalement.getTypeSignalementParent( ) != null ) && ( typeSignalement.getTypeSignalementParent( ).getId( ) != 0 ) )
            {
                request.append( " and fk_id_type_signalement=" + typeSignalement.getTypeSignalementParent( ).getId( ) );
            }
            else
            {
                request.append( " and fk_id_type_signalement is null " );
            }

            daoUtil = new DAOUtil( request.toString( ) );

            int nIndex = 1;
            daoUtil.setString( nIndex++, typeSignalement.getLibelle( ) );
            daoUtil.setLong( nIndex, typeSignalement.getId( ) );
        }
        else
        {
            StringBuilder request = new StringBuilder( );
            request.append( SQL_QUERY_EXISTS_TYPE_SIGNALEMENT );
            if ( typeSignalement.getIdTypeSignalementParent( ) != 0 )
            {
                request.append( " and fk_id_type_signalement=" + typeSignalement.getIdTypeSignalementParent( ) );
            }
            else
            {
                request.append( " and fk_id_type_signalement is null " );
            }
            daoUtil = new DAOUtil( request.toString( ) );
            daoUtil.setString( 1, typeSignalement.getLibelle( ) );
        }
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            existsTypeSignalement = true;
        }

        daoUtil.close( );

        return existsTypeSignalement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean findByIdSignalement( Integer nIdSignalement )
    {
        boolean isUsed = false;

        DAOUtil daoUtil = new DAOUtil( SQL_SELECT_SIGNALEMENT_BY_ID_TYPE_SIGNALEMENT );

        daoUtil.setInt( 1, nIdSignalement );

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            isUsed = true;
        }

        daoUtil.close( );
        return isUsed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateWithoutParent( TypeSignalement typeSignalement, Plugin plugin )
    {
        DAOUtil daoUtil;
        if ( typeSignalement.getImage( ) != null )
        {
            daoUtil = new DAOUtil( SQL_QUERY_UPDATE_WITHOUT_PARENT, plugin );
        }
        else
        {
            daoUtil = new DAOUtil( SQL_QUERY_UPDATE_WITHOUT_PARENT_WITHOUT_IMAGE, plugin );
        }
        int nIndex = 1;
        daoUtil.setInt( nIndex++, typeSignalement.getId( ) );
        daoUtil.setString( nIndex++, typeSignalement.getLibelle( ) );
        daoUtil.setBoolean( nIndex++, typeSignalement.getActif( ) );
        daoUtil.setInt( nIndex++, typeSignalement.getUnit( ).getIdUnit( ) );

        String strResourceType = ImageObjetService.getInstance( ).getResourceTypeId( );
        UrlItem url = new UrlItem( Parameters.IMAGE_SERVLET );
        url.addParameter( Parameters.RESOURCE_TYPE, strResourceType );
        url.addParameter( Parameters.RESOURCE_ID, Integer.toString( typeSignalement.getId( ) ) );
        typeSignalement.setImageUrl( url.getUrlWithEntity( ) );

        if ( typeSignalement.getImage( ) != null )
        {
            daoUtil.setString( nIndex++, typeSignalement.getImageUrl( ) );
            daoUtil.setBytes( nIndex++, typeSignalement.getImage( ).getImage( ) );
            daoUtil.setString( nIndex++, typeSignalement.getImage( ).getMimeType( ) );
        }

        daoUtil.setBoolean( nIndex++, typeSignalement.getIsAgent( ) );
        daoUtil.setBoolean( nIndex++, typeSignalement.isHorsDMR( ) );
        daoUtil.setString( nIndex++, typeSignalement.getMessageHorsDMR( ) );

        daoUtil.setLong( nIndex, typeSignalement.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );

        insertNewVersionTypeSignalement( findLastVersionTypeSignalement( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Integer insertWithoutParent( TypeSignalement typeSignalement, Plugin plugin )
    {
        Integer nIndex = 1;
        Integer nIdTypeSignalement = newPrimaryKey( plugin );
        typeSignalement.setId( nIdTypeSignalement );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_WITHOUT_PARENT, plugin );
        daoUtil.setInt( nIndex++, typeSignalement.getId( ) );
        daoUtil.setString( nIndex++, typeSignalement.getLibelle( ) );
        daoUtil.setBoolean( nIndex++, typeSignalement.getActif( ) );
        daoUtil.setInt( nIndex++, typeSignalement.getUnit( ).getIdUnit( ) );
        daoUtil.setInt( nIndex++, typeSignalement.getOrdre( ) );
        daoUtil.setBoolean( nIndex++, typeSignalement.getIsAgent( ) );

        String strResourceType = ImageObjetService.getInstance( ).getResourceTypeId( );
        UrlItem url = new UrlItem( Parameters.IMAGE_SERVLET );
        url.addParameter( Parameters.RESOURCE_TYPE, strResourceType );
        url.addParameter( Parameters.RESOURCE_ID, Integer.toString( typeSignalement.getId( ) ) );
        typeSignalement.setImageUrl( url.getUrlWithEntity( ) );

        if ( typeSignalement.getImage( ) != null )
        {
            daoUtil.setString( nIndex++, typeSignalement.getImageUrl( ) );
            daoUtil.setBytes( nIndex++, typeSignalement.getImage( ).getImage( ) );
            daoUtil.setString( nIndex, typeSignalement.getImage( ).getMimeType( ) );
        }
        else
        {
            byte[] baImageNull = null;
            daoUtil.setString( nIndex++, "" );
            daoUtil.setBytes( nIndex++, baImageNull );
            daoUtil.setString( nIndex, "" );
        }

        daoUtil.executeUpdate( );
        daoUtil.close( );

        insertNewVersionTypeSignalement( findLastVersionTypeSignalement( ) );

        return nIdTypeSignalement;
    }

    /**
     * Returns a list of report types from the daoUtil.
     *
     * @param daoUtil            the daoUtil object
     * @return a list of report types
     */
    private List<TypeSignalement> getListTypeSignalement( DAOUtil daoUtil )
    {
        List<TypeSignalement> listResult = new ArrayList<>( );
        daoUtil.executeQuery( );
        int nIndex;
        while ( daoUtil.next( ) )
        {
            nIndex = 1;

            TypeSignalement typeSignalement = new TypeSignalement( );
            typeSignalement.setId( daoUtil.getInt( nIndex++ ) );
            typeSignalement.setLibelle( daoUtil.getString( nIndex++ ) );
            typeSignalement.setActif( daoUtil.getBoolean( nIndex++ ) );

            TypeSignalement typeSignalementParent = new TypeSignalement( );
            typeSignalementParent.setId( daoUtil.getInt( nIndex++ ) );
            typeSignalement.setTypeSignalementParent( typeSignalementParent );

            Unit unit = new Unit( );
            unit.setIdUnit( daoUtil.getInt( nIndex++ ) );
            unit.setIdParent( daoUtil.getInt( nIndex++ ) );
            unit.setLabel( daoUtil.getString( nIndex++ ) );
            unit.setDescription( daoUtil.getString( nIndex++ ) );
            typeSignalement.setUnit( unit );

            typeSignalement.setOrdre( daoUtil.getInt( nIndex++ ) );

            typeSignalement.setImageUrl( daoUtil.getString( nIndex++ ) );
            Object oImageContent = daoUtil.getBytes( nIndex++ );

            typeSignalement.setImage( new ImageResource( ) );
            typeSignalement.setImageContent( ( byte[] ) oImageContent );
            typeSignalement.setMimeType( daoUtil.getString( nIndex++ ) );

            typeSignalement.setAlias( daoUtil.getString( nIndex++ ) );
            typeSignalement.setAliasMobile( daoUtil.getString( nIndex++ ) );

            typeSignalement.setIsAgent( daoUtil.getBoolean( nIndex++ ) );

            typeSignalement.setHorsDMR( daoUtil.getBoolean( nIndex++ ) );
            typeSignalement.setMessageHorsDMR( daoUtil.getString( nIndex ) );

            listResult.add( typeSignalement );
        }

        daoUtil.close( );

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSignalement getTypeSignalementByIdWithParents( Integer nIdTypeSignalement )
    {
        TypeSignalement ret = getTypeSignalement( nIdTypeSignalement );
        if ( ret.getIdTypeSignalementParent( ) != null )
        {
            this.getTypeSignalementByIdWithParents( ret, ret.getIdTypeSignalementParent( ) );
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSignalement getTypeSignalementByIdWithParentsWithoutUnit( Integer nIdTypeSignalement )
    {
        TypeSignalement ret = getTypeSignalementWithoutUnit( nIdTypeSignalement );
        if ( ret.getIdTypeSignalementParent( ) != null )
        {
            this.getTypeSignalementByIdWithParents( ret, ret.getIdTypeSignalementParent( ) );
        }
        return ret;
    }

    /**
     * Gets the report type by id with parents.
     *
     * @param typeSignalement
     *            the report type
     * @param idTypeSignalementParent
     *            the parent report type id
     * @return the report type by id with parents
     */
    private void getTypeSignalementByIdWithParents( TypeSignalement typeSignalement, Integer idTypeSignalementParent )
    {
        if ( ( typeSignalement != null ) && ( idTypeSignalementParent != null ) )
        {
            TypeSignalement parent = getTypeSignalement( idTypeSignalementParent );
            typeSignalement.setTypeSignalementParent( parent );
            if ( ( parent != null ) && ( parent.getIdTypeSignalementParent( ) != null ) )
            {
                this.getTypeSignalementByIdWithParents( parent, parent.getIdTypeSignalementParent( ) );
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIdTypeSignalementOrdreInferieur( TypeSignalement typeSignalement )
    {
        TypeSignalement typeSignalementParent = getParent( typeSignalement.getId( ), PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME ) );
        StringBuilder query = new StringBuilder( );
        query.append( "SELECT id_type_signalement FROM signalement_type_signalement WHERE ordre=(SELECT max(ordre) FROM signalement_type_signalement WHERE ordre< " );
        query.append( typeSignalement.getOrdre( ) );
        if ( typeSignalementParent == null )
        {
            query.append( " AND fk_id_type_signalement is null" );
        }
        else
        {
            query.append( " AND fk_id_type_signalement=" );
            query.append( typeSignalementParent.getId( ) );
        }
        query.append( " ) AND " );

        if ( typeSignalementParent == null )
        {
            query.append( "fk_id_type_signalement is null" );
        }
        else
        {
            query.append( "fk_id_type_signalement=" );
            query.append( typeSignalementParent.getId( ) );
        }

        DAOUtil daoUtil = new DAOUtil( query.toString( ) );
        daoUtil.executeQuery( );
        int idTypeSignalementOrdreInferieur = 0;
        if ( daoUtil.next( ) )
        {
            idTypeSignalementOrdreInferieur = daoUtil.getInt( 1 );
        }
        daoUtil.close( );
        return idTypeSignalementOrdreInferieur;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIdTypeSignalementOrdreSuperieur( TypeSignalement typeSignalement )
    {
        TypeSignalement typeSignalementParent = getParent( typeSignalement.getId( ), PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME ) );
        StringBuilder query = new StringBuilder( );
        query.append( "SELECT id_type_signalement FROM signalement_type_signalement WHERE ordre=(SELECT min(ordre) FROM signalement_type_signalement WHERE ordre> " );
        query.append( typeSignalement.getOrdre( ) );
        if ( typeSignalementParent == null )
        {
            query.append( " AND fk_id_type_signalement is null" );
        }
        else
        {
            query.append( " AND fk_id_type_signalement=" );
            query.append( typeSignalementParent.getId( ) );
        }
        query.append( " ) AND " );

        if ( typeSignalementParent == null )
        {
            query.append( "fk_id_type_signalement is null" );
        }
        else
        {
            query.append( "fk_id_type_signalement=" );
            query.append( typeSignalementParent.getId( ) );
        }

        DAOUtil daoUtil = new DAOUtil( query.toString( ) );
        daoUtil.executeQuery( );
        int idTypeSignalementOrdreInferieur = 0;
        if ( daoUtil.next( ) )
        {
            idTypeSignalementOrdreInferieur = daoUtil.getInt( 1 );
        }
        daoUtil.close( );
        return idTypeSignalementOrdreInferieur;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateOrdre( TypeSignalement typeSignalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_ORDRE );

        int nIndex = 1;
        daoUtil.setInt( nIndex++, typeSignalement.getOrdre( ) );
        // WHERE
        daoUtil.setInt( nIndex, typeSignalement.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsOrdre( int ordre, TypeSignalement typeSignalementParent )
    {

        StringBuilder query = new StringBuilder( );

        query.append( "SELECT id_type_signalement FROM signalement_type_signalement WHERE ordre=" );
        query.append( ordre );
        query.append( SQL_WHERE_FK_ID_TYPE_SIGNALEMENT );

        if ( typeSignalementParent == null )
        {
            query.append( SQL_WHERE_IS_NULL );
        }
        else
        {
            query.append( "=" );
            query.append( typeSignalementParent.getId( ) );
        }

        DAOUtil daoUtil = new DAOUtil( query.toString( ) );
        boolean exists = false;
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            exists = true;
        }

        daoUtil.close( );
        return exists;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateOrderGreaterThan( Integer ordre, TypeSignalement typeSignalementParent )
    {

        StringBuilder query = new StringBuilder( );
        query.append( "UPDATE signalement_type_signalement SET ordre=ordre+1 WHERE ordre>=" );
        query.append( ordre );
        query.append( SQL_WHERE_FK_ID_TYPE_SIGNALEMENT );

        if ( typeSignalementParent == null )
        {
            query.append( SQL_WHERE_IS_NULL );
        }
        else
        {
            query.append( " = " );
            query.append( typeSignalementParent.getId( ) );
        }

        DAOUtil daoUtil = new DAOUtil( query.toString( ) );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateOrderSuperiorAfterDeletingTypeSignalement( Integer order, TypeSignalement typeSignalementParent )
    {
        StringBuilder query = new StringBuilder( );
        query.append( "UPDATE signalement_type_signalement SET ordre=ordre-1 WHERE ordre>" );
        query.append( order );
        query.append( SQL_WHERE_FK_ID_TYPE_SIGNALEMENT );

        if ( typeSignalementParent == null )
        {
            query.append( SQL_WHERE_IS_NULL );
        }
        else
        {
            query.append( " = " );
            query.append( typeSignalementParent.getId( ) );
        }

        DAOUtil daoUtil = new DAOUtil( query.toString( ) );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getMaximumOrderInHierarchyTypeSignalement( TypeSignalement typeSignalementParent )
    {
        StringBuilder query = new StringBuilder( );
        Integer maximumOrder = null;
        query.append( "SELECT max(ordre) FROM signalement_type_signalement WHERE " );
        query.append( "fk_id_type_signalement " );

        if ( typeSignalementParent == null )
        {
            query.append( SQL_WHERE_IS_NULL );
        }
        else
        {
            query.append( " = " );
            query.append( typeSignalementParent.getId( ) );
        }

        DAOUtil daoUtil = new DAOUtil( query.toString( ) );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            maximumOrder = daoUtil.getInt( 1 );
        }

        daoUtil.close( );
        return maximumOrder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getMinimumOrderAfterGivenOrder( Integer order, TypeSignalement typeSignalementParent )
    {
        StringBuilder query = new StringBuilder( );
        Integer minimumOrder = null;
        query.append( "SELECT min(ordre) FROM signalement_type_signalement WHERE ordre>" );
        query.append( order );
        query.append( SQL_WHERE_FK_ID_TYPE_SIGNALEMENT );

        if ( typeSignalementParent == null )
        {
            query.append( SQL_WHERE_IS_NULL );
        }
        else
        {
            query.append( " = " );
            query.append( typeSignalementParent.getId( ) );
        }

        DAOUtil daoUtil = new DAOUtil( query.toString( ) );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            minimumOrder = daoUtil.getInt( 1 );
        }

        daoUtil.close( );
        return minimumOrder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageResource loadImage( int nIdTypeObjet )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_IMAGE_BY_PRIMARY_KEY );
        daoUtil.setInt( 1, nIdTypeObjet );
        daoUtil.executeQuery( );

        ImageResource image = new ImageResource( );

        int nIndex = 1;

        if ( daoUtil.next( ) )
        {
            image.setImage( daoUtil.getBytes( nIndex++ ) );
            image.setMimeType( daoUtil.getString( nIndex ) );
        }

        daoUtil.close( );

        return image;
    }

    /**
     * Method to get the last report type version.
     *
     * @return last report type version
     */
    @Override
    public double findLastVersionTypeSignalement( )
    {
        double dVersion = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_LAST_VERSION_TYPE_SIGNALEMENT );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            dVersion = daoUtil.getDouble( 1 );
        }

        daoUtil.close( );

        return dVersion;
    }

    /**
     * Method to add new version when user updates or add report type update.
     *
     * @param dVersion the d version
     */
    private void insertNewVersionTypeSignalement( double dVersion )
    {
        String strIncVersion = AppPropertiesService.getProperty( SignalementConstants.INC_VERSION_TYPE_SIGNALEMENT );
        double dIncVersion = 0.01;
        try
        {
            dIncVersion = Double.parseDouble( strIncVersion );
        }
        catch ( NumberFormatException e )
        {
            AppLogService.error( e );
        }

        dVersion = dVersion + dIncVersion;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ADD_NEW_VERSION_TYPE_SIGNALEMENT );
        daoUtil.setDouble( 1, dVersion );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer insertAlias( TypeSignalement typeSignalement, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_TYPE_SIGNALEMENT_ALIAS, plugin );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, typeSignalement.getId( ) );
        daoUtil.setString( nIndex++, typeSignalement.getAlias( ) );
        daoUtil.setString( nIndex, typeSignalement.getAliasMobile( ) );
        daoUtil.executeUpdate( );
        daoUtil.close( );

        return typeSignalement.getId( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAlias( TypeSignalement typeSignalement, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_TYPE_SIGNALEMENT_ALIAS, plugin );
        int nIndex = 1;
        daoUtil.setString( nIndex++, typeSignalement.getAlias( ) );
        daoUtil.setString( nIndex++, typeSignalement.getAliasMobile( ) );

        // WHERE
        daoUtil.setLong( nIndex, typeSignalement.getId( ) );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAlias( Integer idTypeSignalement, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_TYPE_SIGNALEMENT_ALIAS, plugin );
        int nIndex = 1;
        // WHERE
        daoUtil.setLong( nIndex, idTypeSignalement );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshViewTypesWithParentsLinks( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REFRESH_VIEW_TYPES_WITH_PARENTS_LINKS );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCategoryFromTypeId( Integer idTypeSignalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CATEGORY_FROM_TYPE_ID );
        int nIndex = 1;
        daoUtil.setInt( nIndex, idTypeSignalement );
        daoUtil.executeQuery( );
        int idCategory = -1;
        if ( daoUtil.next( ) )
        {
            idCategory = daoUtil.getInt( 1 );
        }
        daoUtil.close( );
        return idCategory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateParent( Integer nIdTypeSignalement, Integer nIdParent )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_PARENT );
        int nIndex = 1;

        if ( nIdParent > 0 )
        {
            daoUtil.setInt( nIndex++, nIdParent );
        }
        else
        {
            daoUtil.setIntNull( nIndex++ );
        }
        daoUtil.setInt( nIndex++, nIdParent );
        daoUtil.setInt( nIndex, nIdTypeSignalement );
        daoUtil.executeUpdate( );
        daoUtil.close( );

        insertNewVersionTypeSignalement( findLastVersionTypeSignalement( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getListTypeSignalementLastLevel( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LAST_LEVEL_WITH_PARENT );
        daoUtil.executeQuery( );

        List<TypeSignalement> listResult = new ArrayList<>( );
        daoUtil.executeQuery( );
        int nIndex;
        while ( daoUtil.next( ) )
        {
            nIndex = 1;

            TypeSignalement typeSignalement = new TypeSignalement( );
            typeSignalement.setId( daoUtil.getInt( nIndex++ ) );
            typeSignalement.setLibelle( daoUtil.getString( nIndex++ ) );
            typeSignalement.setNbMessages( daoUtil.getInt( nIndex++ ) );
            typeSignalement.setActif( daoUtil.getBoolean( nIndex ) );

            listResult.add( typeSignalement );
        }
        daoUtil.close( );

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertTypeSignalementSource( Integer idType, Integer idSource )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_TYPE_SOURCE );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, idType );
        daoUtil.setInt( nIndex, idSource );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTypeSignalementSource( Integer idType, Integer idSource )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_TYPE_SOURCE );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, idType );
        daoUtil.setInt( nIndex, idSource );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllTypeSignalementBySource( Integer idSource )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_SOURCE_WITH_FULL_LIBELLE );
        daoUtil.setInt( 1, idSource );
        daoUtil.executeQuery( );

        List<TypeSignalement> listResult = new ArrayList<>( );
        int nIndex;

        while ( daoUtil.next( ) )
        {
            nIndex = 1;

            TypeSignalement typeSignalement = new TypeSignalement( );
            typeSignalement.setId( daoUtil.getInt( nIndex++ ) );
            typeSignalement.setLibelle( daoUtil.getString( nIndex++ ) );
            typeSignalement.setActif( daoUtil.getBoolean( nIndex++ ) );
            typeSignalement.setIdTypeSignalementParent( daoUtil.getInt( nIndex ) );

            listResult.add( typeSignalement );
        }
        daoUtil.close( );

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalementDTO> getAllTypeSignalementDTOBySource( Integer idSource )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_SOURCE );
        daoUtil.setInt( 1, idSource );
        daoUtil.executeQuery( );

        List<TypeSignalementDTO> listResult = new ArrayList<>( );

        while ( daoUtil.next( ) )
        {
            TypeSignalement typeSignalement = fillTypeSignalementWithoutUnit( daoUtil );

            TypeSignalementDTO dto = new TypeSignalementDTO( );

            try
            {
                BeanUtils.copyProperties( dto, typeSignalement );
            }
            catch ( IllegalAccessException | InvocationTargetException e )
            {
                AppLogService.error( e );
            }

            listResult.add( dto );
        }
        daoUtil.close( );

        return listResult;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.dansmarue.business.dao.ITypeSignalementDAO#getAllTypeLastlevelNotInSource(java.lang.Integer)
     */
    @Override
    public List<TypeSignalement> getAllTypeLastlevelNotInSource( Integer idSource )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LAST_LEVEL_WITH_PARENT_NOT_IN_SOURCE );
        daoUtil.setInt( 1, idSource );
        daoUtil.executeQuery( );

        List<TypeSignalement> listResult = new ArrayList<>( );
        daoUtil.executeQuery( );
        int nIndex;
        while ( daoUtil.next( ) )
        {
            nIndex = 1;

            TypeSignalement typeSignalement = new TypeSignalement( );
            typeSignalement.setId( daoUtil.getInt( nIndex++ ) );
            typeSignalement.setLibelle( daoUtil.getString( nIndex++ ) );
            typeSignalement.setActif( daoUtil.getBoolean( nIndex ) );

            listResult.add( typeSignalement );
        }

        daoUtil.close( );

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSignalementDTO getTypeSignalementById( Integer nId )
    {
        TypeSignalement typeSignalement = new TypeSignalement( );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_TYPE_BY_ID_SIGNALEMENT );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            typeSignalement = fillTypeSignalementWithoutUnit( daoUtil );
        }

        daoUtil.close( );

        TypeSignalementDTO dto = new TypeSignalementDTO( );
        try
        {
            BeanUtils.copyProperties( dto, typeSignalement );
        }
        catch ( IllegalAccessException | InvocationTargetException e )
        {
            AppLogService.error( e );
        }

        return dto;
    }

    /**
     * Fill type signalement without unit.
     *
     * @param daoUtil the dao util
     * @return the type signalement
     */
    private TypeSignalement fillTypeSignalementWithoutUnit( DAOUtil daoUtil )
    {
        TypeSignalement typeSignalement = new TypeSignalement( );

        int nIndex = 1;

        nIndex = 1;
        typeSignalement.setId( daoUtil.getInt( nIndex++ ) );
        typeSignalement.setLibelle( daoUtil.getString( nIndex++ ) );
        typeSignalement.setActif( daoUtil.getBoolean( nIndex++ ) );

        // get the parent
        TypeSignalement typeSignalementParent = new TypeSignalement( );
        typeSignalementParent.setId( daoUtil.getInt( nIndex++ ) );
        typeSignalement.setTypeSignalementParent( typeSignalementParent );
        typeSignalement.setIdTypeSignalementParent( typeSignalementParent.getId( ) );

        typeSignalement.setOrdre( daoUtil.getInt( nIndex++ ) );

        typeSignalement.setImageUrl( daoUtil.getString( nIndex++ ) );
        Object oImageContent = daoUtil.getBytes( nIndex++ );

        typeSignalement.setImage( new ImageResource( ) );
        typeSignalement.setImageContent( ( byte[] ) oImageContent );
        typeSignalement.setMimeType( daoUtil.getString( nIndex++ ) );

        typeSignalement.setIsAgent( daoUtil.getBoolean( nIndex++ ) );
        typeSignalement.setHorsDMR( daoUtil.getBoolean( nIndex++ ) );
        typeSignalement.setMessageHorsDMR( daoUtil.getString( nIndex ) );

        return typeSignalement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getListIdsChildren( List<Integer> idsParent )
    {
        String intIdsParent = StringUtils.join( idsParent.stream( ).toArray( ), "," );

        DAOUtil daoUtil = new DAOUtil( MessageFormat.format( SQL_QUERY_LIST_IDS_CHILDREN, intIdsParent ) );

        daoUtil.executeQuery( );

        List<Integer> listResult = new ArrayList<>( );
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
    public List<Source> getListSource( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_SOURCE );

        daoUtil.executeQuery( );

        List<Source> listResult = new ArrayList<>( );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            int nIndex = 0;
            Source source = new Source( );

            source.setId( daoUtil.getInt( ++nIndex ) );
            source.setLibelle( daoUtil.getString( ++nIndex ) );
            source.setDescription( daoUtil.getString( ++nIndex ) );
            source.setCommentaire( daoUtil.getString( ++nIndex ) );

            listResult.add( source );
        }
        daoUtil.close( );

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSource( Source source )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ADD_SOURCE );
        int nIndex = 0;

        daoUtil.setString( ++nIndex, source.getLibelle( ) );
        daoUtil.setString( ++nIndex, source.getDescription( ) );
        daoUtil.setString( ++nIndex, source.getCommentaire( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSource( Integer idSource )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_SOURCE );
        daoUtil.setInt( 1, idSource );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Source getSourceById( Integer idSource )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GET_SOURCE_BY_ID );
        daoUtil.setInt( 1, idSource );
        daoUtil.executeQuery( );
        Source source = new Source( );
        if ( daoUtil.next( ) )
        {
            int nIndex = 0;

            source.setId( daoUtil.getInt( ++nIndex ) );
            source.setLibelle( daoUtil.getString( ++nIndex ) );
            source.setDescription( daoUtil.getString( ++nIndex ) );
            source.setCommentaire( daoUtil.getString( ++nIndex ) );
        }

        daoUtil.close( );
        return source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSource( Source source )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_SOURCE );

        int nIndex = 0;
        daoUtil.setString( ++nIndex, source.getDescription( ) );
        daoUtil.setString( ++nIndex, source.getCommentaire( ) );

        // WHERE
        daoUtil.setInt( ++nIndex, source.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );

    }

}
