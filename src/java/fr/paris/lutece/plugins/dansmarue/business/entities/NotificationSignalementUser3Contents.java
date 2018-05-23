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
package fr.paris.lutece.plugins.dansmarue.business.entities;

import fr.paris.lutece.plugins.workflowcore.business.config.TaskConfig;

/**
 * NotificationSignalementUserTaskConfig class
 * 
 */
public class NotificationSignalementUser3Contents extends TaskConfig
{
    private String _strSubject;
    private String _strMessage1;
    private String _strMessage2;
    private String _strSender;
    private String _strTitle1;
    private String _strTitle2;
    private String _strSubjectRamen;
    private String _strMessage1Ramen;
    private String _strMessage2Ramen;
    

    /**
     * 
     * @return the subject of the message
     */
    public String getSubject( )
    {
        return _strSubject;
    }

    /**
     * set the subject of the message
     * @param subject the subject of the message
     */
    public void setSubject( String subject )
    {
        _strSubject = subject;
    }

    /**
     * 
     * @return the message 1 of the notification
     */
    public String getMessage1( )
    {
        return _strMessage1;
    }

    /**
     * set the message 1 of the notification
     * @param message the message of the notification
     */
    public void setMessage1( String message )
    {
        _strMessage1 = message;
    }

    /**
     * 
     * @return the message 2 of the notification
     */
    public String getMessage2( )
    {
        return _strMessage2;
    }

    /**
     * set the message 2 of the notification
     * @param message the message of the notification
     */
    public void setMessage2( String message )
    {
        _strMessage2 = message;
    }

    /**
     * return the sender of the notification
     * @return _strSender
     */
    public String getSender( )
    {
        return _strSender;
    }

    /**
     * set the sender of the notification
     * @param sender the sender
     */
    public void setSender( String sender )
    {
        this._strSender = sender;
    }

    /**
     * return the title1 of the notification
     * @return _strTitle1
     */
    public String getTitle1( )
    {
        return _strTitle1;
    }

    /**
     * set the title1 of the notification
     * @param title1 the title
     */
    public void setTitle1( String title1 )
    {
        this._strTitle1 = title1;
    }
    
    /**
     * return the title2 of the notification
     * @return _strTitle2
     */
    public String getTitle2( )
    {
        return _strTitle2;
    }

    /**
     * set the title2 of the notification
     * @param title2 the title
     */
    public void setTitle2( String title2 )
    {
        this._strTitle2 = title2;
    }

    
    /**
     * Get the ramen subject of the notification
     * @return subjectRamen the notification subject for ramen
     */
	public String getSubjectRamen() {
		return _strSubjectRamen;
	}

	 /**
     * Set the ramen subject of the notification
     * @param subjectRamen the notification subject for ramen
     */
	public void setSubjectRamen(String subjectRamen) {
		this._strSubjectRamen = subjectRamen;
	}

	/**
     * Get the ramen Message1 of the notification
     * @return ramenMessage1 the notification message1 for ramen
     */
	public String getMessage1Ramen() {
		return _strMessage1Ramen;
	}

	/**
     * Set the ramen Message1 of the notification
     * param message1Ramen the notification message1 for ramen
     */
	public void setMessage1Ramen(String message1Ramen) {
		this._strMessage1Ramen = message1Ramen;
	}

	/**
     * Get the ramen Message2 of the notification
     * @return ramenMessage2 the notification message2 for ramen
     */
	public String getMessage2Ramen() {
		return _strMessage2Ramen;
	}

	/**
     * Set the ramen Message2 of the notification
     * param message2Ramen the notification message2 for ramen
     */
	public void setMessage2Ramen(String message2Ramen) {
		this._strMessage2Ramen = message2Ramen;
	}

}

