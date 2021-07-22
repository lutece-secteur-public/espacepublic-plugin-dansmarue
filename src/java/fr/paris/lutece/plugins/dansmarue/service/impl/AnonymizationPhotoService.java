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
package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.anonymizationphoto.business.ResponseAnonymizationPhoto;
import fr.paris.lutece.plugins.anonymizationphoto.service.IAnonymizationPhotoService;
import fr.paris.lutece.plugins.dansmarue.business.dao.impl.AnonymizationPhotoDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.util.image.ImageUtil;

/**
 * The Class AnonymizationPhotoService.
 */
public class AnonymizationPhotoService implements IAnonymizationPhotoService
{

    /** The Constant PROPERTY_ID_ACTION_TRANSFERT. */
    private static final int PROPERTY_ID_ACTION_TRANSFERT = AppPropertiesService.getPropertyInt( "signalement.anonymization.id.action.transfert", -1 );

    /** The anonymization photo dao. */
    @Inject
    @Named( "anonymizationPhotoDAO" )
    private AnonymizationPhotoDAO _anonymizationPhotoDao;

    /**
     * Post treatment.
     *
     * @param responseAnonymizationPhoto
     *            the response anonymization photo
     */
    @Override
    public void postTreatment( ResponseAnonymizationPhoto responseAnonymizationPhoto )
    {
        if ( StringUtils.isBlank( responseAnonymizationPhoto.getErrorMessage( ) ) )
        {
            byte [ ] photoAnonymized = responseAnonymizationPhoto.getPhotoAnonymized( );

            // créer la miniature
            byte [ ] resizeImage = ImageUtil.resizeImage( photoAnonymized,
                    AppPropertiesService.getProperty( SignalementConstants.IMAGE_THUMBNAIL_RESIZE_WIDTH ),
                    AppPropertiesService.getProperty( SignalementConstants.IMAGE_THUMBNAIL_RESIZE_HEIGHT ), 1 );

            // insérer les photos
            _anonymizationPhotoDao.insertAnonymizedPhoto( photoAnonymized, resizeImage, responseAnonymizationPhoto.getIdResource( ) );

            int idSignalement = findSignalementToTransfer( responseAnonymizationPhoto.getIdResource( ) );
            if ( idSignalement > 0 )
            {
                WorkflowService workflowService = WorkflowService.getInstance( );
                workflowService.doProcessAction( idSignalement, Signalement.WORKFLOW_RESOURCE_TYPE, PROPERTY_ID_ACTION_TRANSFERT, null, null, Locale.FRANCE,
                        true );
            }
        }

    }

    /**
     * Find id Signalement for signalement can be send to partner.
     *
     * @param idPhoto
     *            id photo
     * @return id signalement or null
     */
    private int findSignalementToTransfer( int idPhoto )
    {

        List<PhotoDMR> listPhoto = _anonymizationPhotoDao.findPhotosSignalement( idPhoto );
        int idSignalement = -1;
        for ( PhotoDMR photo : listPhoto )
        {
            if ( !photo.isAnonymized( ) )
            {
                return -1;
            }
            idSignalement = photo.getSignalement( ).getId( ).intValue( );
        }

        return idSignalement;
    }

}
