package fi.paris.lutece.plugins.dansmarue.service.output;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.operator.OperatorProcessor;
import org.springframework.util.DigestUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.service.ISignalementService;
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

    /** The _signalement service. */
    private ISignalementService                    _signalementService     = ( ISignalementService ) SpringContextService.getBean( "signalementService" );

    @Override
    public void process( PDFOperator operator, List<COSBase> arguments ) throws IOException
    {
        // TODO Auto-generated method stub

    }

    /**
     * Sauvegarder signalement. Prepars a demandeSignalement to be saved.
     *
     * @param demandeSignalement
     *            the demande signalement
     */
    public boolean sauvegarderSignalement( Signalement demandeSignalement, LuteceUser user )
    {
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<Adresse>( );
        Adresse adresse = demandeSignalement.getAdresses( ).get( 0 );
        adresses.add( adresse );

        Arrondissement arrondissement = arrondissementService.getArrondissementByGeom( adresse.getLng( ), adresse.getLat( ) );

        demandeSignalement.setArrondissement( arrondissement );

        Calendar calendarInstance = Calendar.getInstance( );
        demandeSignalement.setAnnee( calendarInstance.get( Calendar.YEAR ) );

        demandeSignalement.setMois( SignalementService.getLetterByMonth( calendarInstance.get( Calendar.MONTH ) ) );

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
            // TODO : voir pour la réinitialisation d'une demande
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
            throw new BusinessException( demandeSignalement, MESSAGE_ERROR_NO_SECTOR );
        }
        return true;
    }

    public boolean sauvegarderSignalementFromWS( Signalement demandeSignalement, String userName, String userMail )
    {
        Signalement signalement = new Signalement( );
        List<Adresse> adresses = new ArrayList<Adresse>( );
        Adresse adresse = demandeSignalement.getAdresses( ).get( 0 );
        adresses.add( adresse );

        Arrondissement arrondissement = arrondissementService.getArrondissementByGeom( adresse.getLng( ), adresse.getLat( ) );

        demandeSignalement.setArrondissement( arrondissement );

        Calendar calendarInstance = Calendar.getInstance( );
        demandeSignalement.setAnnee( calendarInstance.get( Calendar.YEAR ) );

        demandeSignalement.setMois( SignalementService.getLetterByMonth( calendarInstance.get( Calendar.MONTH ) ) );

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
            // TODO : voir pour la réinitialisation d'une demande
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
            throw new BusinessException( demandeSignalement, MESSAGE_ERROR_NO_SECTOR );
        }
        return true;
    }

}
