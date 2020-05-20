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

import java.io.Serializable;

/**
 * The Class ServiceFaitMasseFilter.
 */
public class ServiceFaitMasseFilter implements Serializable
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3607921200266975320L;

    /** The str date begin. */
    private String            _strDateBegin;

    /** The str date end. */
    private String            _strDateEnd;

    /** The str commentaire. */
    private String            _strCommentaire;

    /** The id etats. */
    private Integer[]         _idEtats;

    /** The id type signalements. */
    private Integer[]         _idTypeSignalements;

    /**
     * Gets the id type signalements.
     *
     * @return the idTypeSignalement
     */

    public Integer[] getIdTypeSignalements( )
    {
        return _idTypeSignalements;
    }

    /**
     * Sets the id type signalements.
     *
     * @param idTypeSignalements
     *            the new id type signalements
     */

    public void setIdTypeSignalements( Integer[] idTypeSignalements )
    {
        _idTypeSignalements = idTypeSignalements;
    }

    /**
     * Gets the date begin.
     *
     * @return the DateBegin
     */
    public String getDateBegin( )
    {
        return _strDateBegin;
    }

    /**
     * Sets the date begin.
     *
     * @param pDateBegin            the DateBegin to set
     */
    public void setDateBegin( String pDateBegin )
    {
        _strDateBegin = pDateBegin;
    }

    /**
     * Gets the date end.
     *
     * @return the DateEnd
     */
    public String getDateEnd( )
    {
        return _strDateEnd;
    }

    /**
     * Sets the date end.
     *
     * @param pDateEnd            the DateEnd to set
     */
    public void setDateEnd( String pDateEnd )
    {
        _strDateEnd = pDateEnd;
    }

    /**
     * Gets the list id type etats.
     *
     * @return the list id type etats
     */
    public Integer[] getIdEtats( )
    {
        return _idEtats;
    }

    /**
     * Sets the list id etats.
     *
     * @param idEtats
     *            the new id etats
     */
    public void setIdEtats( Integer[] idEtats )
    {
        _idEtats = idEtats;
    }

    /**
     * Gets the commentaire.
     *
     * @return the commentaire
     */
    public String getCommentaire( )
    {
        return _strCommentaire;
    }

    /**
     * Sets the commentaire.
     *
     * @param _strCommentaire
     *            the new commentaire
     */
    public void setCommentaire( String _strCommentaire )
    {
        this._strCommentaire = _strCommentaire;
    }

}
