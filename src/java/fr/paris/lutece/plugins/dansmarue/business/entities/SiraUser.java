/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

public class SiraUser
{

    private Long   _id;
    private String _strGuid;
    private String _strMail;
    private String _strUdid;
    private String _strDevice;
    private String _strToken;

    /**
     * @return the guid
     */
    public String getGuid( )
    {
        return _strGuid;
    }

    /**
     * @param guid
     *            the guid to set
     */
    public void setGuid( String guid )
    {
        this._strGuid = guid;
    }

    /**
     * @return the mail
     */
    public String getMail( )
    {
        return _strMail;
    }

    /**
     * @param mail
     *            the mail to set
     */
    public void setMail( String mail )
    {
        this._strMail = mail;
    }

    /**
     * @return the udid
     */
    public String getUdid( )
    {
        return _strUdid;
    }

    /**
     * @param udid
     *            the udid to set
     */
    public void setUdid( String udid )
    {
        this._strUdid = udid;
    }

    /**
     * @return the device
     */
    public String getDevice( )
    {
        return _strDevice;
    }

    /**
     * @param device
     *            the device to set
     */
    public void setDevice( String device )
    {
        this._strDevice = device;
    }

    /**
     * @return the id
     */
    public Long getId( )
    {
        return _id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId( Long id )
    {
        this._id = id;
    }

    /**
     * @return the token
     */
    public String getToken( )
    {
        return _strToken;
    }

    /**
     * @param token
     *            the token to set
     */
    public void setToken( String token )
    {
        this._strToken = token;
    }

}
