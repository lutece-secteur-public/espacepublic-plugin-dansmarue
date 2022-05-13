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
package fr.paris.lutece.plugins.dansmarue.service.dto;

/**
 * The Class TypeSignalementExportDTO.
 */
public class TypeSignalementExportDTO
{

    /** The str libelle niveau 1. */
    private String _strLibelleNiveau1;

    /** The str libelle niveau 2. */
    private String _strLibelleNiveau2;

    /** The str libelle niveau 3. */
    private String _strLibelleNiveau3;

    /** The str libelle concat. */
    private String _strLibelleConcat;

    /** The str alias. */
    private String _strAlias;

    /** The str alias mobile. */
    private String _strAliasMobile;

    /** The str direction. */
    private String _strDirection;

    /** The str actif. */
    private Boolean _strActif;

    /** The str pour agent. */
    private Boolean _strPourAgent;

    private Integer _nId;

    /**
     * Gets the libelle niveau 1.
     *
     * @return the libelle niveau 1
     */
    public String getLibelleNiveau1( )
    {
        return _strLibelleNiveau1;
    }

    /**
     * Sets the libelle niveau 1.
     *
     * @param _strLibelleNiveau1
     *            the new libelle niveau 1
     */
    public void setLibelleNiveau1( String _strLibelleNiveau1 )
    {
        this._strLibelleNiveau1 = _strLibelleNiveau1;
    }

    /**
     * Gets the libelle niveau 2.
     *
     * @return the libelle niveau 2
     */
    public String getLibelleNiveau2( )
    {
        return _strLibelleNiveau2;
    }

    /**
     * Sets the libelle niveau 2.
     *
     * @param _strLibelleNiveau2
     *            the new libelle niveau 2
     */
    public void setLibelleNiveau2( String _strLibelleNiveau2 )
    {
        this._strLibelleNiveau2 = _strLibelleNiveau2;
    }

    /**
     * Gets the libelle niveau 3.
     *
     * @return the libelle niveau 3
     */
    public String getLibelleNiveau3( )
    {
        return _strLibelleNiveau3;
    }

    /**
     * Sets the libelle niveau 3.
     *
     * @param _strLibelleNiveau3
     *            the new libelle niveau 3
     */
    public void setLibelleNiveau3( String _strLibelleNiveau3 )
    {
        this._strLibelleNiveau3 = _strLibelleNiveau3;
    }

    /**
     * Gets the libelle concat.
     *
     * @return the libelle concat
     */
    public String getLibelleConcat( )
    {
        return _strLibelleConcat;
    }

    /**
     * Sets the libelle concat.
     *
     * @param _strLibelleConcat
     *            the new libelle concat
     */
    public void setLibelleConcat( String _strLibelleConcat )
    {
        this._strLibelleConcat = _strLibelleConcat;
    }

    /**
     * Gets the alias.
     *
     * @return the alias
     */
    public String getAlias( )
    {
        return _strAlias;
    }

    /**
     * Sets the alias.
     *
     * @param _strAlias
     *            the new alias
     */
    public void setAlias( String _strAlias )
    {
        this._strAlias = _strAlias;
    }

    /**
     * Gets the alias mobile.
     *
     * @return the alias mobile
     */
    public String getAliasMobile( )
    {
        return _strAliasMobile;
    }

    /**
     * Sets the alias mobile.
     *
     * @param _strAliasMobile
     *            the new alias mobile
     */
    public void setAliasMobile( String _strAliasMobile )
    {
        this._strAliasMobile = _strAliasMobile;
    }

    /**
     * Gets the direction.
     *
     * @return the direction
     */
    public String getDirection( )
    {
        return _strDirection;
    }

    /**
     * Sets the direction.
     *
     * @param _strDirection
     *            the new direction
     */
    public void setDirection( String _strDirection )
    {
        this._strDirection = _strDirection;
    }

    /**
     * Gets the actif.
     *
     * @return the actif
     */
    public String getActif( )
    {
        if ( ( _strActif == null ) || !_strActif )
        {
            return "Non";
        }
        else
        {
            return "Oui";
        }
    }

    /**
     * Sets the actif.
     *
     * @param _strActif
     *            the new actif
     */
    public void setActif( Boolean _strActif )
    {
        this._strActif = _strActif;
    }

    /**
     * Gets the pour agent.
     *
     * @return the pour agent
     */
    public String getPourAgent( )
    {
        if ( ( _strPourAgent == null ) || !_strPourAgent )
        {
            return "Non";
        }
        else
        {
            return "Oui";
        }
    }

    /**
     * Sets the pour agent.
     *
     * @param _strPourAgent
     *            the new pour agent
     */
    public void setPourAgent( Boolean _strPourAgent )
    {
        this._strPourAgent = _strPourAgent;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId( )
    {
        return _nId;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId( Integer id )
    {
        _nId = id;
    }

    /**
     * Gets the tab all datas.
     *
     * @return the tab all datas
     */
    public String [ ] getTabAllDatas( )
    {
        return new String [ ] {
                getId( ) != null ? String.valueOf( getId( ) ) : "", getLibelleNiveau1( ), getLibelleNiveau2( ), getLibelleNiveau3( ), getLibelleConcat( ),
                getAlias( ), getAliasMobile( ), getDirection( ), getActif( ), getPourAgent( )
        };
    }
}
