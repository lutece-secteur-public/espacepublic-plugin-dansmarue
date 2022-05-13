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
package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.IMessageTypologieDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.MessageTypologie;
import fr.paris.lutece.plugins.dansmarue.business.entities.MessageTypologieExport;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * The Class TypeSignalementDAO.
 */
public class MessageTypologieDAO implements IMessageTypologieDAO
{

    /** The Constant SQL_SELECT_BY_ID_TYPE. */
    private static final String SQL_SELECT_BY_ID_TYPE = "SELECT id_message, fk_id_type_signalement, type_message, contenu_message, ordre, actif FROM signalement_message_typologie where fk_id_type_signalement=? order by ordre asc";

    /** The Constant SQL_SELECT_MESSAGE_ACTIF_BY_ID_TYPE. */
    private static final String SQL_SELECT_MESSAGE_ACTIF_BY_ID_TYPE = "SELECT id_message, fk_id_type_signalement, type_message, contenu_message, ordre, actif FROM signalement_message_typologie where fk_id_type_signalement=? and actif=1 order by ordre asc";

    /** The Constant SQL_UPDATE. */
    private static final String SQL_UPDATE = "UPDATE signalement_message_typologie SET fk_id_type_signalement=?, type_message=?, contenu_message=?, ordre=?, actif=? WHERE id_message=?";

    /** The Constant SQL_DELETE. */
    private static final String SQL_DELETE = "DELETE FROM signalement_message_typologie WHERE id_message=?";

    /** The Constant SQL_DELETE_BY_ID_TYPE_SIGNALEMENT. */
    private static final String SQL_DELETE_BY_ID_TYPE_SIGNALEMENT = "DELETE FROM signalement_message_typologie WHERE fk_id_type_signalement=?";

    /** The Constant SQL_CREATE. */
    private static final String SQL_CREATE = "INSERT INTO signalement_message_typologie (id_message, fk_id_type_signalement, type_message, contenu_message, actif, ordre) VALUES((select COALESCE (max (id_message)+1, 1) from signalement_message_typologie), ?, ?, ?, ?, COALESCE((select max(ordre+1) from signalement_message_typologie where fk_id_type_signalement=?),1))";

    /** The Constant SQL_SELECT. */
    private static final String SQL_SELECT = "SELECT id_message, fk_id_type_signalement, type_message, contenu_message, ordre, actif FROM signalement_message_typologie where id_message=?";

    /** The Constant SQL_DECREASE_ORDRE. */
    private static final String SQL_DECREASE_ORDRE = "update signalement_message_typologie set ordre = (ordre+1) where fk_id_type_signalement=? and ordre>=? and ordre<? and id_message!=?";

    /** The Constant SQL_INCREASE_ORDRE. */
    private static final String SQL_INCREASE_ORDRE = "update signalement_message_typologie set ordre = (ordre-1) where fk_id_type_signalement=? and ordre<=? and ordre>?  and id_message!=?";

    /** The Constant SQL_INCREASE_ORDRE_UNDER. */
    private static final String SQL_INCREASE_ORDRE_UNDER = "update signalement_message_typologie set ordre = (ordre-1) where fk_id_type_signalement=? and ordre>=?";

    /** The Constant SQL_EXPORT_MESSAGES. */
    private static final String SQL_EXPORT_MESSAGES = "SELECT message.fk_id_type_signalement, message.type_message, message.contenu_message, (select count(*) from signalement_message_typologie where fk_id_type_signalement=message.fk_id_type_signalement) ,message.actif  "
            + "FROM signalement_message_typologie message "
            + "join signalement_type_signalement typeSig on typeSig.id_type_signalement = message.fk_id_type_signalement "
            + "order by message.fk_id_type_signalement, message.ordre asc";

    /** The Constant SQL_IS_MESSAGE_ACTIF_FOR_TYPE. */
    private static final String SQL_IS_MESSAGE_ACTIF_FOR_TYPE = "select count(id_message) from signalement_message_typologie where actif=1 and fk_id_type_signalement=?";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MessageTypologie> loadAllByIdType( Integer lIdTypeSignalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_SELECT_BY_ID_TYPE );
        int nIndex = 1;

        daoUtil.setLong( nIndex, lIdTypeSignalement );

        daoUtil.executeQuery( );

        List<MessageTypologie> messageTypologieList = new ArrayList<>( );
        while ( daoUtil.next( ) )
        {
            messageTypologieList.add( messageTypologieMapper( daoUtil ) );
        }
        daoUtil.close( );
        return messageTypologieList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MessageTypologie> loadAllMessageActifByIdType( Integer lIdTypeSignalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_SELECT_MESSAGE_ACTIF_BY_ID_TYPE );
        int nIndex = 1;

        daoUtil.setLong( nIndex, lIdTypeSignalement );

        daoUtil.executeQuery( );

        List<MessageTypologie> messageTypologieList = new ArrayList<>( );
        while ( daoUtil.next( ) )
        {
            messageTypologieList.add( messageTypologieMapper( daoUtil ) );
        }
        daoUtil.close( );
        return messageTypologieList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMessageTypologie( MessageTypologie messageTypologie )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_UPDATE );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, messageTypologie.getFkIdTypeSignalement( ) );
        daoUtil.setString( nIndex++, messageTypologie.getTypeMessage( ) );
        daoUtil.setString( nIndex++, messageTypologie.getContenuMessage( ) );
        daoUtil.setInt( nIndex++, messageTypologie.getOrdre( ) != null ? messageTypologie.getOrdre( ) : 0 );
        daoUtil.setBoolean( nIndex++, messageTypologie.getActif( ) );
        // WHERE
        daoUtil.setLong( nIndex, messageTypologie.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMessageTypologie( Integer messageId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_DELETE );
        daoUtil.setLong( 1, messageId );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMessageTypologieByIdTypeSignalement( Integer typeSignalementId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_DELETE_BY_ID_TYPE_SIGNALEMENT );
        daoUtil.setLong( 1, typeSignalementId );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createMessageTypologie( MessageTypologie messageTypologie )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_CREATE );

        int nIndex = 1;
        daoUtil.setInt( nIndex++, messageTypologie.getFkIdTypeSignalement( ) );
        daoUtil.setString( nIndex++, messageTypologie.getTypeMessage( ) );
        daoUtil.setString( nIndex++, messageTypologie.getContenuMessage( ) );
        daoUtil.setBoolean( nIndex++, messageTypologie.getActif( ) );
        daoUtil.setInt( nIndex, messageTypologie.getFkIdTypeSignalement( ) );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageTypologie loadMessageTypologie( Integer messageId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_SELECT );
        int nIndex = 1;
        daoUtil.setLong( nIndex, messageId );

        daoUtil.executeQuery( );

        MessageTypologie messageTypologie = new MessageTypologie( );
        while ( daoUtil.next( ) )
        {
            messageTypologie = messageTypologieMapper( daoUtil );
        }
        daoUtil.close( );
        return messageTypologie;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseOrdreMessageTypologie( Integer ordreInitial, MessageTypologie messageTypologie )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_INCREASE_ORDRE );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, messageTypologie.getFkIdTypeSignalement( ) );
        daoUtil.setInt( nIndex++, messageTypologie.getOrdre( ) );
        daoUtil.setInt( nIndex++, ordreInitial );
        daoUtil.setInt( nIndex, messageTypologie.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreaseOrdreMessageTypologie( Integer ordreInitial, MessageTypologie messageTypologie )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_DECREASE_ORDRE );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, messageTypologie.getFkIdTypeSignalement( ) );
        daoUtil.setInt( nIndex++, messageTypologie.getOrdre( ) );
        daoUtil.setInt( nIndex++, ordreInitial );
        daoUtil.setInt( nIndex, messageTypologie.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseOrdreMessageTypologieUnder( MessageTypologie messageTypologie )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_INCREASE_ORDRE_UNDER );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, messageTypologie.getFkIdTypeSignalement( ) );
        daoUtil.setInt( nIndex, messageTypologie.getOrdre( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MessageTypologieExport> getExportMessages( )
    {
        // SQL_EXPORT_MESSAGES
        DAOUtil daoUtil = new DAOUtil( SQL_EXPORT_MESSAGES );
        int nIndex = 1;

        daoUtil.executeQuery( );

        List<MessageTypologieExport> messageTypologieExportList = new ArrayList<>( );
        while ( daoUtil.next( ) )
        {
            nIndex = 1;
            MessageTypologieExport messageTypologieExport = new MessageTypologieExport( );
            messageTypologieExport.setFkIdTypeSignalement( daoUtil.getInt( nIndex++ ) );
            messageTypologieExport.setTypeMessage( daoUtil.getString( nIndex++ ) );
            messageTypologieExport.setContenuMessage( StringEscapeUtils.unescapeHtml( daoUtil.getString( nIndex++ ) ) );
            messageTypologieExport.setNbMessage( daoUtil.getInt( nIndex++ ) );
            messageTypologieExport.setActif( daoUtil.getBoolean( nIndex ) );

            messageTypologieExportList.add( messageTypologieExport );
        }
        daoUtil.close( );
        return messageTypologieExportList;
    }

    /**
     * Message typologie mapper.
     *
     * @param daoUtil
     *            the dao util
     * @return the message typologie
     */
    private MessageTypologie messageTypologieMapper( DAOUtil daoUtil )
    {
        MessageTypologie messageTypologie = new MessageTypologie( );

        int nIndex = 1;
        messageTypologie.setId( daoUtil.getInt( nIndex++ ) );
        messageTypologie.setFkIdTypeSignalement( daoUtil.getInt( nIndex++ ) );
        messageTypologie.setTypeMessage( daoUtil.getString( nIndex++ ) );
        messageTypologie.setContenuMessage( daoUtil.getString( nIndex++ ) );
        messageTypologie.setOrdre( daoUtil.getInt( nIndex++ ) );
        messageTypologie.setActif( daoUtil.getBoolean( nIndex ) );

        return messageTypologie;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMessageActifForTypeSignalement( Integer nIdTypeSignalement )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_IS_MESSAGE_ACTIF_FOR_TYPE );
        int nIndex = 1;
        daoUtil.setLong( nIndex, nIdTypeSignalement );

        daoUtil.executeQuery( );

        boolean isMessageActif = false;

        while ( daoUtil.next( ) )
        {
            isMessageActif = daoUtil.getInt( nIndex ) > 0;
        }
        daoUtil.close( );

        return isMessageActif;
    }
}
