/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fi.paris.lutece.plugins.dansmarue.service.output;

import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.service.impl.ArrondissementService;
import fr.paris.lutece.plugins.dansmarue.service.impl.PrioriteService;
import fr.paris.lutece.plugins.dansmarue.service.impl.SignalementService;
import fr.paris.lutece.plugins.dansmarue.service.impl.SignalementSuiviService;
import fr.paris.lutece.plugins.dansmarue.service.impl.TypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.upload.handler.DansMaRueUploadHandler;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.apache.commons.lang.RandomStringUtils;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.operator.OperatorProcessor;

import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SignalementOutputPrcessor extends OperatorProcessor
{
    // MESSAGES
    private static final String                    MESSAGE_ERROR_NO_SECTOR = "dansmarue.message.error.aucunSecteur";
    private static final int                       TOKEN_NB_RANDOM_CHAR    = 100;
    protected static final SignalementService      signalementService      = SpringContextService.getBean( "signalementService" );
    protected static final TypeSignalementService  typeSignalementService  = SpringContextService.getBean( "typeSignalementService" );
    protected static final PrioriteService         prioriteService         = SpringContextService.getBean( "prioriteService" );
    protected static final DansMaRueUploadHandler  dansmarueUploadHandler  = SpringContextService.getBean( "dansmarueUploadHandler" );
    protected static final SignalementSuiviService signalementSuiviService = SpringContextService.getBean( "signalementSuiviService" );
    protected static final ArrondissementService   arrondissementService   = SpringContextService.getBean( "signalement.arrondissementService" );

    @Override
    public void process( PDFOperator operator, List<COSBase> arguments ) throws IOException
    {
        // Auto-generated method stub
    }

    /**
     * Save signalement. Prepars a demandeSignalement to be saved.
     *
     * @param demandeSignalement
     *            the demande signalement
     * @param user
     *            the Lutece user
     * @return a boolean
     */
    public boolean sauvegarderSignalement( Signalement demandeSignalement, LuteceUser user )
    {
        List<Adresse> adresses = new ArrayList<Adresse>( );
        Adresse adresse = demandeSignalement.getAdresses( ).get( 0 );
        adresses.add( adresse );

        Arrondissement arrondissement = arrondissementService.getArrondissementByGeom( adresse.getLng( ), adresse.getLat( ) );

        demandeSignalement.setArrondissement( arrondissement );

        Calendar calendarInstance = Calendar.getInstance( );
        demandeSignalement.setAnnee( calendarInstance.get( Calendar.YEAR ) );

        demandeSignalement.setMois( signalementService.getLetterByMonth( calendarInstance.get( Calendar.MONTH ) ) );

        demandeSignalement.setDateCreation( new SimpleDateFormat( "dd/MM/yyyy" ).format( calendarInstance.getTime( ) ) );

        demandeSignalement.setPrefix( SignalementConstants.SIGNALEMENT_PREFIX_TELESERVICE );

        demandeSignalement.setIsDoublon( false );

        // Creation of the unique token for the signalement
        try
        {
            StringBuilder token = new StringBuilder( RandomStringUtils.randomAlphanumeric( TOKEN_NB_RANDOM_CHAR ) );
            token.append( new Date( ).getTime( ) );
            demandeSignalement.setToken( new String( DigestUtils.md5DigestAsHex( token.toString( ).getBytes( "UTF-8" ) ) ) );
        } catch ( UnsupportedEncodingException e )
        {
            AppLogService.error( e );

            return false;
        }

        try
        {
            if ( demandeSignalement.getId( ) != null )
            {
                demandeSignalement.setId( null );
                adresse.setId( null );

                if ( null != demandeSignalement.getPhotos( ) )
                {
                    for ( PhotoDMR photo : demandeSignalement.getPhotos( ) )
                    {
                        if ( photo != null )
                        {
                            photo.setId( null );
                        }
                    }
                }
            }

            Long signalementId = signalementService.insertWithPeripheralDatas( demandeSignalement );

            if ( null != user )
            {
                signalementService.addFollower( signalementId, user.getName( ), "", user.getUserInfo( LuteceUser.BUSINESS_INFO_ONLINE_EMAIL ), "", "", true );
            }
        } catch ( BusinessException e )
        {
            AppLogService.error( e.getMessage( ), e );
            throw new BusinessException( demandeSignalement, MESSAGE_ERROR_NO_SECTOR );
        }

        return true;
    }

    /**
     * Save report coming from WebService. Prepars a demandeSignalement to be saved.
     *
     * @param demandeSignalement
     *            the report demande
     * @param userName
     *            the user name
     * @param userMail
     *            the user mail
     * @return a boolean
     */
    public boolean sauvegarderSignalementFromWS( Signalement demandeSignalement, String userName, String userMail )
    {
        List<Adresse> adresses = new ArrayList<Adresse>( );
        Adresse adresse = demandeSignalement.getAdresses( ).get( 0 );
        adresses.add( adresse );

        Arrondissement arrondissement = arrondissementService.getArrondissementByGeom( adresse.getLng( ), adresse.getLat( ) );

        demandeSignalement.setArrondissement( arrondissement );

        Calendar calendarInstance = Calendar.getInstance( );
        demandeSignalement.setAnnee( calendarInstance.get( Calendar.YEAR ) );

        demandeSignalement.setMois( signalementService.getLetterByMonth( calendarInstance.get( Calendar.MONTH ) ) );

        demandeSignalement.setDateCreation( new SimpleDateFormat( "dd/MM/yyyy" ).format( calendarInstance.getTime( ) ) );

        demandeSignalement.setPrefix( SignalementConstants.SIGNALEMENT_PREFIX_TELESERVICE );

        demandeSignalement.setIsDoublon( false );

        // Creation of the unique token for the report
        try
        {
            StringBuilder token = new StringBuilder( RandomStringUtils.randomAlphanumeric( TOKEN_NB_RANDOM_CHAR ) );
            token.append( new Date( ).getTime( ) );
            demandeSignalement.setToken( new String( DigestUtils.md5DigestAsHex( token.toString( ).getBytes( "UTF-8" ) ) ) );
        } catch ( UnsupportedEncodingException e )
        {
            AppLogService.error( e );

            return false;
        }

        try
        {
            if ( demandeSignalement.getId( ) != null )
            {
                demandeSignalement.setId( null );
                adresse.setId( null );

                if ( null != demandeSignalement.getPhotos( ) )
                {
                    for ( PhotoDMR photo : demandeSignalement.getPhotos( ) )
                    {
                        if ( photo != null )
                        {
                            photo.setId( null );
                        }
                    }
                }
            }

            Long signalementId = signalementService.insertWithPeripheralDatas( demandeSignalement );

            if ( ( null != userName ) && ( null != userMail ) )
            {
                signalementService.addFollower( signalementId, userName, "", userMail, "", "", true );
            }
        } catch ( BusinessException e )
        {
            AppLogService.error( e.getMessage( ), e );
            throw new BusinessException( demandeSignalement, MESSAGE_ERROR_NO_SECTOR );
        }

        return true;
    }
}
