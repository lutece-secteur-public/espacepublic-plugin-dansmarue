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
package fr.paris.lutece.plugins.dansmarue.business.entities;

/**
 * The Class SignalementDashboardFilter.
 */
public class SignalementDashboardFilter
{

    /** The n period id. */
    private Integer _nPeriodId;

    /** The n unit id. */
    private Integer _nUnitId;

    /** The category ids. */
    private Integer [ ] _categoryIds;

    /** The arrondissement ids. */
    private Integer [ ] _arrondissementIds;

    /** The dashboard period. */
    private DashboardPeriod _dashboardPeriod;

    /** The n depth. */
    private Integer _nDepth;

    /**
     * Instantiates a new signalement dashboard filter.
     */
    public SignalementDashboardFilter( )
    {
        // Constructor
    }

    /**
     * Getter for the periodId.
     *
     * @return the periodId
     */
    public Integer getPeriodId( )
    {
        return _nPeriodId;
    }

    /**
     * Setter for the periodId.
     *
     * @param periodId
     *            the periodId to set
     */
    public void setPeriodId( Integer periodId )
    {
        _nPeriodId = periodId;
    }

    /**
     * Getter for the unitId.
     *
     * @return the unitId
     */
    public Integer getUnitId( )
    {
        return _nUnitId;
    }

    /**
     * Setter for the unitId.
     *
     * @param unitId
     *            the unitId to set
     */
    public void setUnitId( Integer unitId )
    {
        _nUnitId = unitId;
    }

    /**
     * Getter for the categoryIds.
     *
     * @return the categoryIds
     */
    public Integer [ ] getCategoryIds( )
    {
        return _categoryIds;
    }

    /**
     * Setter for the categoryIds.
     *
     * @param categoryIds
     *            the categoryIds to set
     */
    public void setCategoryIds( Integer [ ] categoryIds )
    {
        _categoryIds = categoryIds;
    }

    /**
     * Getter for the districts ids.
     *
     * @return the districts ids
     */
    public Integer [ ] getArrondissementIds( )
    {
        return _arrondissementIds;
    }

    /**
     * Setter for the districts ids.
     *
     * @param arrondissementIds
     *            the disctricts ids to set
     */
    public void setArrondissementIds( Integer [ ] arrondissementIds )
    {
        _arrondissementIds = arrondissementIds;
    }

    /**
     * Gets the dashboard period.
     *
     * @return the dashboard period
     */
    public DashboardPeriod getDashboardPeriod( )
    {
        return _dashboardPeriod;
    }

    /**
     * Sets the dashboard period.
     *
     * @param dashboardPeriod
     *            the new dashboard period
     */
    public void setDashboardPeriod( DashboardPeriod dashboardPeriod )
    {
        _dashboardPeriod = dashboardPeriod;
    }

    /**
     * Gets the depth.
     *
     * @return the depth
     */
    public Integer getDepth( )
    {
        return _nDepth;
    }

    /**
     * Sets the depth.
     *
     * @param depth
     *            the new depth
     */
    public void setDepth( Integer depth )
    {
        _nDepth = depth;
    }

}
