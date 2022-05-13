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
package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.util.List;

import javax.inject.Inject;

import fr.paris.lutece.plugins.dansmarue.business.dao.IMessageTypologieDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.MessageTypologie;
import fr.paris.lutece.plugins.dansmarue.business.entities.MessageTypologieExport;
import fr.paris.lutece.plugins.dansmarue.service.IMessageTypologieService;

/**
 * The Class TypeSignalementService.
 */
public class MessageTypologieService implements IMessageTypologieService
{

    /** The message typologie DAO. */
    @Inject
    private IMessageTypologieDAO _messageTypologieDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MessageTypologie> loadAllByIdType( Integer lIdTypeSignalement )
    {
        return _messageTypologieDAO.loadAllByIdType( lIdTypeSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MessageTypologie> loadAllMessageActifByIdType( Integer lIdTypeSignalement )
    {
        return _messageTypologieDAO.loadAllMessageActifByIdType( lIdTypeSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMessageTypologie( MessageTypologie messageTypologie )
    {
        _messageTypologieDAO.updateMessageTypologie( messageTypologie );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseOrdreMessageTypologie( Integer ordreInitial, MessageTypologie messageTypologie )
    {
        _messageTypologieDAO.increaseOrdreMessageTypologie( ordreInitial, messageTypologie );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreaseOrdreMessageTypologie( Integer ordreInitial, MessageTypologie messageTypologie )
    {
        _messageTypologieDAO.decreaseOrdreMessageTypologie( ordreInitial, messageTypologie );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMessageTypologie( Integer messageId )
    {
        _messageTypologieDAO.removeMessageTypologie( messageId );
    }

    /**
     * Removes the message typologie by id type signalement.
     *
     * @param typeSignalementId
     *            the type signalement id
     */
    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.dansmarue.service.IMessageTypologieService#removeMessageTypologieByIdTypeSignalement(java.lang.Integer)
     */
    @Override
    public void removeMessageTypologieByIdTypeSignalement( Integer typeSignalementId )
    {
        _messageTypologieDAO.removeMessageTypologieByIdTypeSignalement( typeSignalementId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createMessageTypologie( MessageTypologie messageTypologie )
    {
        _messageTypologieDAO.createMessageTypologie( messageTypologie );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageTypologie loadMessageTypologie( Integer messageId )
    {
        return _messageTypologieDAO.loadMessageTypologie( messageId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseOrdreMessageTypologieUnder( MessageTypologie messageTypologie )
    {
        _messageTypologieDAO.increaseOrdreMessageTypologieUnder( messageTypologie );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MessageTypologieExport> getExportMessages( )
    {
        return _messageTypologieDAO.getExportMessages( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMessageActifForTypeSignalement( Integer nIdTypeSignalement )
    {
        return _messageTypologieDAO.isMessageActifForTypeSignalement( nIdTypeSignalement );
    }
}
