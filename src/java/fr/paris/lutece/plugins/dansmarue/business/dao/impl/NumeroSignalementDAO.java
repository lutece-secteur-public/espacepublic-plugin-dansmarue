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
package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import fr.paris.lutece.plugins.dansmarue.business.dao.INumeroSignalementDAO;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * The Class NumeroSignalementDAO.
 */
public class NumeroSignalementDAO implements INumeroSignalementDAO
{

    /** The Constant SELECT_NUMBER_LOCKED. */
    private static final String SELECT_NUMBER_LOCKED = "SELECT numero FROM signalement_numero_signalement WHERE mois = ? AND annee = ? FOR UPDATE";

    /** The Constant UPDATE_INCREMENT_NUMBER. */
    private static final String UPDATE_INCREMENT_NUMBER = "UPDATE signalement_numero_signalement SET numero = numero + 1 WHERE mois = ? AND annee = ?";

    /** The Constant INSERT_NEW_COMBINATION. */
    private static final String INSERT_NEW_COMBINATION = "INSERT INTO signalement_numero_signalement(mois,annee,numero) VALUES (?,?,?)";

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Long findByMonthYear( String strMonth, int nYear )
    {
        // Checks if the line already exists
        Long dossierNumber = null;

        dossierNumber = getNumber( strMonth, nYear );

        // Line does not exists, creating a new one
        if ( dossierNumber == null )
        {
            try
            {
                insertNewCombinationLine( strMonth, nYear );
            }
            catch( AppException ex )
            {
                AppLogService.error( "Error when initializing a number line signalement_numero_signalement : " + ex.getMessage( ), ex );
            }
            dossierNumber = getNumber( strMonth, nYear );
        }

        if ( dossierNumber != null )
        {
            incrementNumber( strMonth, nYear );
        }
        else
        {
            throw new AppException( "Error when generating the report number" );
        }

        return dossierNumber;
    }

    /**
     * Inserts a new combination line.
     *
     * @param strMonth
     *            Month value
     * @param nYear
     *            Year
     */
    private void insertNewCombinationLine( String strMonth, int nYear )
    {
        DAOUtil daoUtil = new DAOUtil( INSERT_NEW_COMBINATION );
        int nIndex = 1;
        daoUtil.setString( nIndex++, strMonth );
        daoUtil.setInt( nIndex++, nYear );
        daoUtil.setLong( nIndex, SignalementConstants.START_SIGNALEMENT_NUMERO );

        daoUtil.executeUpdate( );

        daoUtil.close( );
    }

    /**
     * Gets the next number to use based on the given combination.
     *
     * @param strMonth
     *            Month value
     * @param nYear
     *            Year
     * @return The next file number Null if combination line does not exists
     */
    private Long getNumber( String strMonth, int nYear )
    {
        DAOUtil daoUtil = new DAOUtil( SELECT_NUMBER_LOCKED );

        fillDAOWithCombination( strMonth, nYear, daoUtil );

        daoUtil.executeQuery( );

        Long dossierNumber = null;

        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            dossierNumber = daoUtil.getLong( nIndex );
        }
        daoUtil.close( );
        return dossierNumber;
    }

    /**
     * Increments the number for a given combination.
     *
     * @param strMonth
     *            Month value
     * @param nYear
     *            Year
     */
    private void incrementNumber( String strMonth, int nYear )
    {
        DAOUtil daoUtil = new DAOUtil( UPDATE_INCREMENT_NUMBER );
        fillDAOWithCombination( strMonth, nYear, daoUtil );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    /**
     * Fills the dao with the required parameters.
     *
     * @param strMonth
     *            the str month
     * @param nYear
     *            the n year
     * @param daoUtil
     *            the dao util
     */
    private void fillDAOWithCombination( String strMonth, int nYear, DAOUtil daoUtil )
    {
        int nIndex = 1;
        daoUtil.setString( nIndex++, strMonth );
        daoUtil.setInt( nIndex, nYear );
    }

}
