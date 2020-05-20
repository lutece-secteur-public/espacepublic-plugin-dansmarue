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
package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.util.List;

import javax.inject.Inject;

import fr.paris.lutece.plugins.dansmarue.business.dao.IObservationRejetDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.ObservationRejet;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.service.IObservationRejetService;
import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

/**
 * The Class ObservationRejetService.
 */
public class ObservationRejetService implements IObservationRejetService
{

    /** The observation rejet DAO. */
    @Inject
    private IObservationRejetDAO _observationRejetDAO;

    /** The Constant MESSAGE_ERROR_OBSERVATION_REJET_NAME_MUST_BE_UNIQUE. */
    // MESSAGES
    private static final String  MESSAGE_ERROR_OBSERVATION_REJET_NAME_MUST_BE_UNIQUE = "dansmarue.message.observationRejet.error.alreadyExists";

    /** The Constant MESSAGE_ERROR_OBSERVATION_REJET_ORDER_INVALID. */
    private static final String  MESSAGE_ERROR_OBSERVATION_REJET_ORDER_INVALID       = "dansmarue.message.observationRejet.error.order.invalid";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ObservationRejet> getAllObservationRejet( )
    {
        Plugin pluginSignalement = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
        return _observationRejetDAO.getAllObservationRejet( pluginSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doDeleteObservationRejet( int nIdObservationRejet )
    {
        // check if the observation can be removed
        if ( !( _observationRejetDAO.countByIdObservationRejet( nIdObservationRejet ) > 0 ) )
        {

            // Update order
            _observationRejetDAO.decreaseOrdreOfAllNext( nIdObservationRejet );

            // Remove the nature object
            _observationRejetDAO.remove( nIdObservationRejet );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSaveObservationRejet( ObservationRejet observationRejet )
    {

        if ( _observationRejetDAO.existsObservationRejet( observationRejet ) )
        {
            throw new BusinessException( observationRejet, MESSAGE_ERROR_OBSERVATION_REJET_NAME_MUST_BE_UNIQUE );
        }
        else
        {
            if ( ( observationRejet.getId( ) != null ) && ( observationRejet.getId( ) > 0 ) )
            {
                _observationRejetDAO.store( observationRejet );
            }
            else
            {
                validateRejetOrder( observationRejet );
                if ( observationRejet.getOrdre( ) == null )
                {
                    int ordre = _observationRejetDAO.getObservationRejetCount( ) + 1;
                    observationRejet.setOrdre( ordre );
                }
                int idObservationRejet = _observationRejetDAO.insert( observationRejet );
                _observationRejetDAO.increaseOrdreOfAllNext( idObservationRejet );
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObservationRejet getById( Integer nIdObservationRejet )
    {

        return _observationRejetDAO.load( nIdObservationRejet );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ObservationRejet> getAllObservationRejetActif( )
    {
        return _observationRejetDAO.getAllObservationRejetActif( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateObservationRejetOrdre( ObservationRejet observationRejet )
    {
        _observationRejetDAO.updateObservationRejetOrdre( observationRejet );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseOrdreOfRejet( ObservationRejet observationRejet )
    {
        int observationRejetCount = _observationRejetDAO.getObservationRejetCount( );
        if ( ( observationRejet != null ) && ( observationRejet.getOrdre( ) < observationRejetCount ) )
        {
            _observationRejetDAO.decreaseOrdreOfNextRejet( observationRejet );
            observationRejet.setOrdre( observationRejet.getOrdre( ) + 1 );
            _observationRejetDAO.updateObservationRejetOrdre( observationRejet );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreaseOrdreOfRejet( ObservationRejet observationRejet )
    {
        if ( ( observationRejet != null ) && ( observationRejet.getOrdre( ) > 1 ) )
        {
            _observationRejetDAO.increaseOrdreOfPreviousRejet( observationRejet );
            observationRejet.setOrdre( observationRejet.getOrdre( ) - 1 );
            _observationRejetDAO.updateObservationRejetOrdre( observationRejet );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseOrdreOfAllNext( int nIdObservationRejet )
    {
        _observationRejetDAO.increaseOrdreOfAllNext( nIdObservationRejet );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreaseOrdreOfAllNext( int nIdObservationRejet )
    {
        _observationRejetDAO.decreaseOrdreOfAllNext( nIdObservationRejet );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countByIdObservationRejet( int nIdObservationRejet )
    {
        return _observationRejetDAO.countByIdObservationRejet( nIdObservationRejet );
    }

    /**
     * Checks if the order is valid.
     *
     * @param observationRejet the observation rejet
     * @return true if test pass false otherwise
     */
    private boolean validateRejetOrder( ObservationRejet observationRejet )
    {
        if ( observationRejet.getOrdre( ) == null )
        {
            throw new BusinessException( observationRejet, MESSAGE_ERROR_OBSERVATION_REJET_ORDER_INVALID );
        }
        int count = getObservationRejetCount( );
        if ( ( observationRejet.getOrdre( ) < 1 ) || ( observationRejet.getOrdre( ) > ( count + 1 ) ) )
        {
            throw new BusinessException( observationRejet, MESSAGE_ERROR_OBSERVATION_REJET_ORDER_INVALID );
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getObservationRejetCount( )
    {
        return _observationRejetDAO.getObservationRejetCount( );
    }

}
