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
package fr.paris.lutece.plugins.dansmarue.business.entities;

/**
 * The Class SiraUser.
 */
public class SiraUser
{

    /** The id. */
    private Long _id;

    /** The str guid. */
    private String _strGuid;

    /** The str mail. */
    private String _strMail;

    /** The str udid. */
    private String _strUdid;

    /** The str device. */
    private String _strDevice;

    /** The str token. */
    private String _strToken;

    /**
     * Gets the guid.
     *
     * @return the guid
     */
    public String getGuid( )
    {
        return _strGuid;
    }

    /**
     * Sets the guid.
     *
     * @param guid
     *            the guid to set
     */
    public void setGuid( String guid )
    {
        _strGuid = guid;
    }

    /**
     * Gets the mail.
     *
     * @return the mail
     */
    public String getMail( )
    {
        return _strMail;
    }

    /**
     * Sets the mail.
     *
     * @param mail
     *            the mail to set
     */
    public void setMail( String mail )
    {
        _strMail = mail;
    }

    /**
     * Gets the udid.
     *
     * @return the udid
     */
    public String getUdid( )
    {
        return _strUdid;
    }

    /**
     * Sets the udid.
     *
     * @param udid
     *            the udid to set
     */
    public void setUdid( String udid )
    {
        _strUdid = udid;
    }

    /**
     * Gets the device.
     *
     * @return the device
     */
    public String getDevice( )
    {
        return _strDevice;
    }

    /**
     * Sets the device.
     *
     * @param device
     *            the device to set
     */
    public void setDevice( String device )
    {
        _strDevice = device;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId( )
    {
        return _id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the id to set
     */
    public void setId( Long id )
    {
        _id = id;
    }

    /**
     * Gets the token.
     *
     * @return the token
     */
    public String getToken( )
    {
        return _strToken;
    }

    /**
     * Sets the token.
     *
     * @param token
     *            the token to set
     */
    public void setToken( String token )
    {
        _strToken = token;
    }

}
