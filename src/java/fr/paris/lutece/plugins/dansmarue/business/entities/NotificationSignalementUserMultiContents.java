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
package fr.paris.lutece.plugins.dansmarue.business.entities;

import fr.paris.lutece.plugins.workflowcore.business.config.TaskConfig;


/**
 * The Class NotificationSignalementUserMultiContents.
 */
public class NotificationSignalementUserMultiContents extends TaskConfig
{

    /** The n id task. */
    private Integer _nIdTask;

    /** The n id message. */
    private Long    _nIdMessage;

    /** The str subject. */
    private String  _strSubject;

    /** The str sender. */
    private String  _strSender;

    /** The str title. */
    private String  _strTitle;

    /** The str message. */
    private String  _strMessage;

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflowcore.business.config.TaskConfig#getIdTask()
     */
    @Override
    public int getIdTask( )
    {
        return _nIdTask;
    }

    /**
     * Sets the id task.
     *
     * @param nIdTask the new id task
     */
    public void setIdTask( Integer nIdTask )
    {
        _nIdTask = nIdTask;
    }

    /**
     * Gets the id message.
     *
     * @return the id message
     */
    public Long getIdMessage( )
    {
        return _nIdMessage;
    }

    /**
     * Sets the id message.
     *
     * @param nIdMessage the new id message
     */
    public void setIdMessage( Long nIdMessage )
    {
        _nIdMessage = nIdMessage;
    }

    /**
     * Gets the subject.
     *
     * @return the subject
     */
    public String getSubject( )
    {
        return _strSubject;
    }

    /**
     * Sets the subject.
     *
     * @param strSubject the new subject
     */
    public void setSubject( String strSubject )
    {
        _strSubject = strSubject;
    }

    /**
     * Gets the sender.
     *
     * @return the sender
     */
    public String getSender( )
    {
        return _strSender;
    }

    /**
     * Sets the sender.
     *
     * @param strSender the new sender
     */
    public void setSender( String strSender )
    {
        _strSender = strSender;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle( )
    {
        return _strTitle;
    }

    /**
     * Sets the title.
     *
     * @param strTitle the new title
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage( )
    {
        return _strMessage;
    }

    /**
     * Sets the message.
     *
     * @param strMessage the new message
     */
    public void setMessage( String strMessage )
    {
        _strMessage = strMessage;
    }

}
