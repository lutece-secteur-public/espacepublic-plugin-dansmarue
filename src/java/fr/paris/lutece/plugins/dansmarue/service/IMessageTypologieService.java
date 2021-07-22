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
package fr.paris.lutece.plugins.dansmarue.service;

import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.MessageTypologie;
import fr.paris.lutece.plugins.dansmarue.business.entities.MessageTypologieExport;

/**
 * The Interface IMessageTypoogieService.
 */
public interface IMessageTypologieService
{
    /**
     * Load all by id type.
     *
     * @param lIdTypeSignalement
     *            the l id type signalement
     * @return the list
     */
    List<MessageTypologie> loadAllByIdType( Integer lIdTypeSignalement );

    /**
     * Load all actif by id type.
     *
     * @param lIdTypeSignalement
     *            the l id type signalement
     * @return the list
     */
    List<MessageTypologie> loadAllMessageActifByIdType( Integer lIdTypeSignalement );

    /**
     * Update message typologie.
     *
     * @param messageTypologie
     *            the message typologie
     */
    void updateMessageTypologie( MessageTypologie messageTypologie );

    /**
     * Increase ordre message typologie.
     *
     * @param ordreInitial
     *            the ordre initial
     * @param messageTypologie
     *            the message typologie
     */
    void increaseOrdreMessageTypologie( Integer ordreInitial, MessageTypologie messageTypologie );

    /**
     * Decrease ordre message typologie.
     *
     * @param ordreInitial
     *            the ordre initial
     * @param messageTypologie
     *            the message typologie
     */
    void decreaseOrdreMessageTypologie( Integer ordreInitial, MessageTypologie messageTypologie );

    /**
     * Removes the message typologie.
     *
     * @param messageId
     *            the message id
     */
    void removeMessageTypologie( Integer messageId );

    /**
     * Removes the message typologie.
     *
     * @param typeSignalementId
     *            the type signalement id
     */
    void removeMessageTypologieByIdTypeSignalement( Integer typeSignalementId );

    /**
     * Creates the message typologie.
     *
     * @param messageTypologie
     *            the message typologie
     */
    void createMessageTypologie( MessageTypologie messageTypologie );

    /**
     * Increase ordre message typologie under.
     *
     * @param messageTypologie
     *            the message typologie
     */
    void increaseOrdreMessageTypologieUnder( MessageTypologie messageTypologie );

    /**
     * Load message typologie.
     *
     * @param messageId
     *            the message id
     * @return the message typologie
     */
    MessageTypologie loadMessageTypologie( Integer messageId );

    /**
     * Load the messages for the export.
     *
     * @return the export messages
     */
    List<MessageTypologieExport> getExportMessages( );

    /**
     * Checks if is message actif for type signalement.
     *
     * @param nIdTypeSignalement
     *            the n id type signalement
     * @return true, if is message actif for type signalement
     */
    boolean isMessageActifForTypeSignalement( Integer nIdTypeSignalement );
}
