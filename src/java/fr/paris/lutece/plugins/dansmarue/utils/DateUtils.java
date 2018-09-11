/*
 * Copyright (c) 2002-2010, Mairie de Paris
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

package fr.paris.lutece.plugins.dansmarue.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.date.DateUtil;

public final class DateUtils
{
    private static final int             YEAR_1900                               = 1900;
    private static SimpleDateFormat      _sdfAnnee;
    public static final int              DATE_CRITERE_ENTRE_LE                   = 0;
    public static final int              DATE_CRITERE_ET_LE                      = 1;
    public static final int              DATE_CRITERE_OU_LE                      = 2;
    public static final String           DATE_FR                                 = "dd/MM/yyyy";
    public static final String           HOUR_FR                                 = "HH:mm";
    public static final String           HOUR_FR_SANS_COLONNE                    = "HHmm";
    public static final String           HOUR_FR_AVEC_SECONDES                   = "HH:mm:ss";
    public static final String           DATE_FR_HR_SANS_COLONNE                 = "dd/MM/yyyyHHmm";
    public static final String           DATE_FR_HR_SANS_COLONNE_AVEC_DEUX_POINT = "dd/MM/yyyyHH:mm";

    public static final SimpleDateFormat XML_DATE_FORMAT                         = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'" );

    /**
     * Empty constructor
     */
    private DateUtils( )
    {
        // Constructor
    }

    /**
     * Convert a date from the dd/MM/yyyyy format to the dd-MM-yyyyy format.
     * 
     * @param dateAnglaise
     *            date in dd/MM/yyyyyy format
     * @return date in dd-MM-yyyyyy format
     * @throws ParseException
     *             throws if the entry date is not valid.
     */
    public static String converteDateAnglais( String dateAnglaise ) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-yyyy" );
        SimpleDateFormat sdf2 = new SimpleDateFormat( DATE_FR );
        return sdf.format( sdf2.parse( dateAnglaise ) );
    }

    /**
     * Returns a date year
     * 
     * @param date
     *            date
     * @return year
     */
    public static synchronized String getAnnee( Date date )
    {
        if ( _sdfAnnee == null )
        {
            _sdfAnnee = new SimpleDateFormat( "yyyy" );
        }
        return _sdfAnnee.format( date );
    }

    /**
     * Returns the current year
     * 
     * @return current year
     */
    public static int getAnneeEnCours( )
    {
        Calendar caldate = new GregorianCalendar( );
        caldate.setTime( new Date( ) );
        return caldate.get( Calendar.YEAR );
    }

    public static int getAnneeInt( Date date )
    {
        Calendar calendar = Calendar.getInstance( );
        calendar.setTime( date );

        return calendar.get( Calendar.YEAR );
    }

    /**
     * Returns the current date
     * 
     * @return the current date
     */
    public static Timestamp getCurrentDate( )
    {
        return new Timestamp( GregorianCalendar.getInstance( ).getTimeInMillis( ) );
    }

    /**
     * Returns the current date
     *
     * @return the current date
     */
    public static String getCurrentDateString( )
    {
        return new SimpleDateFormat( DATE_FR ).format( new Timestamp( System.currentTimeMillis( ) ) );
    }

    /**
     * Returns the current date
     * 
     * @param strPattern
     *            the current date format
     * @return the current date
     */
    public static String getCurrentDateString( String strPattern )
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat( strPattern, Locale.FRENCH );

        return dateFormat.format( new Timestamp( System.currentTimeMillis( ) ) );
    }

    /**
     * Gets the current hour and minute.
     *
     * @return the current hour and minute
     */
    public static Timestamp getCurrentHourAndMinute( )
    {
        Calendar calendar = Calendar.getInstance( );
        calendar.setTime( new Date( ) );
        calendar.set( Calendar.YEAR, YEAR_1900 );
        calendar.set( Calendar.MONTH, Calendar.JANUARY );
        calendar.set( Calendar.DAY_OF_MONTH, 1 );
        return new Timestamp( calendar.getTimeInMillis( ) );
    }

    /**
     * Returns the date in the format defined by strPattern
     * 
     * @param date
     *            date
     * @param strPattern
     *            the desired format for the date
     * @return the date in EEEE dd MMMMM yyyyy format
     */
    public static String getDate( Date date, String strPattern )
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat( strPattern, Locale.FRENCH );
        return dateFormat.format( date );
    }

    /**
     * Transforms a date in string format of type dd/MM/yyyyyy to Timestamp object
     * 
     * @param strDate
     *            Date to be transformed
     * @param isStartOfDayHour
     *            TRUE if the time is to be 00:01 FALSE if the time is to be 23:59
     * @return objet Timestamp corresponding to the date given in parameter
     */
    public static Timestamp getDate( String strDate, boolean isStartOfDayHour )
    {
        if ( ( strDate == null ) )
        {
            return null;
        }

        DateFormat dateFormat = new SimpleDateFormat( DATE_FR );
        dateFormat.setLenient( false );

        Date date;

        try
        {
            date = dateFormat.parse( strDate.trim( ) );
        } catch ( ParseException e )
        {
            return null;
        }

        Calendar caldate = new GregorianCalendar( );
        caldate.setTime( date );
        caldate.set( Calendar.MILLISECOND, 0 );
        caldate.set( Calendar.SECOND, 0 );

        if ( isStartOfDayHour )
        {
            caldate.set( Calendar.HOUR_OF_DAY, caldate.getActualMinimum( Calendar.HOUR_OF_DAY ) );
            caldate.set( Calendar.MINUTE, caldate.getActualMinimum( Calendar.MINUTE ) );
        } else
        {
            caldate.set( Calendar.HOUR_OF_DAY, caldate.getActualMaximum( Calendar.HOUR_OF_DAY ) );
            caldate.set( Calendar.MINUTE, caldate.getActualMaximum( Calendar.MINUTE ) );
            caldate.set( Calendar.SECOND, caldate.getActualMaximum( Calendar.SECOND ) );
        }

        return new Timestamp( caldate.getTimeInMillis( ) );
    }

    /**
     * Returns a timestamp whose time must be 00:01 FALSE if the time must be 23:59
     * 
     * @param date
     *            Date to be transformed
     * @param isStartOfDayHour
     *            true if the time is to be 00h01 FALSE if the time is to be 23h59
     * @return objet Timestamp corresponding to the date given in parameter
     */
    public static Timestamp getDate( Timestamp date, boolean isStartOfDayHour )
    {
        Calendar caldate = new GregorianCalendar( );
        caldate.setTime( date );
        caldate.set( Calendar.MILLISECOND, 0 );
        caldate.set( Calendar.SECOND, 0 );

        if ( isStartOfDayHour )
        {
            caldate.set( Calendar.HOUR_OF_DAY, caldate.getActualMinimum( Calendar.HOUR_OF_DAY ) );
            caldate.set( Calendar.MINUTE, caldate.getActualMinimum( Calendar.MINUTE ) );
        } else
        {
            caldate.set( Calendar.HOUR_OF_DAY, caldate.getActualMaximum( Calendar.HOUR_OF_DAY ) );
            caldate.set( Calendar.MINUTE, caldate.getActualMaximum( Calendar.MINUTE ) );
            caldate.set( Calendar.SECOND, caldate.getActualMaximum( Calendar.SECOND ) );
        }

        return new Timestamp( caldate.getTimeInMillis( ) );
    }

    /**
     * Returns the date in the format defined by strPattern
     * 
     * @param date
     *            the date
     * @param strPattern
     *            the format wishes from the date
     * @return the date in EEEE dd MMMMM yyyyy format
     */
    public static String getDate( Timestamp date, String strPattern )
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat( strPattern, Locale.FRENCH );
        return dateFormat.format( date );
    }

    /**
     * Return string for date dd/MM/yyyy
     * 
     * @param date
     *            the date
     * @return date dd/MM/yyyy
     */
    public static String getDateFr( Date date )
    {
        if ( date == null )
        {
            return null;
        }
        return new SimpleDateFormat( DATE_FR ).format( date );
    }

    /**
     * Transforms a date in string format of type HH:mm into a date object
     * 
     * @param strHour
     *            Date to be transformed
     * @return objet date corresponding to the date given in parameter
     */
    public static Timestamp getHour( String strHour )
    {
        if ( ( strHour == null ) )
        {
            return null;
        }

        DateFormat dateFormat = new SimpleDateFormat( HOUR_FR );
        dateFormat.setLenient( false );

        Date date;
        Timestamp sqlDate;

        try
        {
            date = dateFormat.parse( strHour.trim( ) );
            sqlDate = new Timestamp( date.getTime( ) );
        } catch ( ParseException e )
        {
            return null;
        }

        return sqlDate;
    }

    /**
     * Return string for date HH:mm
     * 
     * @param date
     *            the date
     * @return date HH:mm
     */
    public static String getHourFr( Date date )
    {
        return new SimpleDateFormat( HOUR_FR ).format( date );
    }

    /**
     * Return string for date HH:mm:ss
     * 
     * @param timestamp
     *            the hours to read
     * @return the formated hours
     */
    public static String getHourWithSecondsFr( Date timestamp )
    {
        return new SimpleDateFormat( HOUR_FR_AVEC_SECONDES ).format( timestamp );
    }

    /**
     * Get a timestamp with hour setted and date 01/01/1970.
     *
     * @param hour
     *            hour to set to the date
     * @return the hour without date
     */
    public static Date getHourWithoutDate( Date hour )
    {
        Calendar calDate = new GregorianCalendar( );
        calDate.setTime( new Date( 0 ) );

        Calendar calHour = new GregorianCalendar( );
        calHour.setTime( hour );

        calDate.set( Calendar.HOUR_OF_DAY, calHour.get( Calendar.HOUR_OF_DAY ) );
        calDate.set( Calendar.MINUTE, calHour.get( Calendar.MINUTE ) );

        return calDate.getTime( );
    }

    /**
     * Returns the year of a date
     * 
     * @param date
     *            date
     * @return year
     */
    public static String getMois( Date date )
    {
        SimpleDateFormat sdfMois = new SimpleDateFormat( "MM" );
        return sdfMois.format( date );
    }

    public static int getMoisInt( Date date )
    {
        Calendar calendar = Calendar.getInstance( );
        calendar.setTime( date );

        return calendar.get( Calendar.MONTH );
    }

    public static String getString( Date date )
    {
        String ret;
        if ( date != null )
        {
            SimpleDateFormat sdf = new SimpleDateFormat( DATE_FR );
            ret = sdf.format( date );
        } else
        {
            ret = "";
        }
        return ret;
    }

    /**
     *
     * Checks if the date is <strong>STRICTLY</strong> after the reference date
     * 
     * @param strDateToCheck
     *            the date to check
     * @param strReferenceDateDate
     *            the date to be checked against
     * @return true if the the date to check is after the reference date
     *
     */
    public static boolean isAfter( String strDateToCheck, String strReferenceDateDate )
    {
        // get times...
        long lDateToCheck = DateUtil.formatDate( strDateToCheck, Locale.FRANCE ).getTime( );
        long lDateReference = DateUtil.formatDate( strReferenceDateDate, Locale.FRANCE ).getTime( );

        // check values
        return lDateToCheck >= lDateReference;
    }

    /**
     * Checks if the first date is after the second one.
     * 
     * @param strDateToCheck
     *            the date to check
     * @param strReferenceDateDate
     *            the date to be checked against
     * @return true if strDateToCheck greater or equals to strReferenceDateDate
     */
    public static boolean isEqualOrAfter( String strDateToCheck, String strReferenceDateDate )
    {
        // get times...
        long lDateToCheck = DateUtil.formatDate( strDateToCheck, Locale.FRANCE ).getTime( );
        long lDateReference = DateUtil.formatDate( strReferenceDateDate, Locale.FRANCE ).getTime( );

        // check values
        return lDateToCheck >= lDateReference;
    }

    /**
     * Set the given date hour with the given hour.
     *
     * @param date
     *            date
     * @param hour
     *            hour to set to the date
     * @return the date
     */
    public static Date mergeDateHour( Date date, Date hour )
    {
        Calendar calDate = new GregorianCalendar( );
        calDate.setTime( date );

        Calendar calHour = new GregorianCalendar( );
        calHour.setTime( hour );

        calDate.set( Calendar.HOUR_OF_DAY, calHour.get( Calendar.HOUR_OF_DAY ) );
        calDate.set( Calendar.MINUTE, calHour.get( Calendar.MINUTE ) );

        return calDate.getTime( );
    }

    private static Date parse( String strDate, String format )
    {
        Date ret;
        if ( strDate != null )
        {
            SimpleDateFormat sdf = new SimpleDateFormat( format );
            try
            {
                ret = sdf.parse( strDate );
            } catch ( ParseException e )
            {
                ret = null;
            }
        } else
        {
            ret = null;
        }
        return ret;
    }

    /**
     * Parses the string to fr date (dd/MM/yyyy).
     *
     * @param strDate
     *            the str date
     * @return the date
     */
    public static Date parseDate( String strDate )
    {
        return parse( strDate, DATE_FR );
    }

    public static Date parseHour( String hour )
    {
        return parse( hour, HOUR_FR );
    }

    /**
     * Validate a date
     *
     * @param date
     *            la date
     * @return boolean return true if the field is formated with dd/mm/yyyy
     */
    public static boolean validateDate( String date )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( DATE_FR );
        boolean hasError = true;

        if ( !"".equals( date ) )
        {
            try
            {
                sdf.setLenient( false );
                Date parse = sdf.parse( date );
                Calendar c = Calendar.getInstance( );
                c.setLenient( false );
                c.setTime( parse );
                c.getTime( );
            } catch ( Exception ex )
            {
                AppLogService.error( ex.getMessage( ), ex );
                hasError = false;
            }
        }

        return hasError;
    }

    /**
     * checks a trigram of dates between the... and the... or the.
     *
     * @param dateEffetRecherche
     *            the date effet recherche
     * @param obligatoire
     *            if mandatory is true the method will also check if at least one value is entered.
     * @return true if the dates are valid (and if a date is entered).
     */
    public static boolean valideDateEntreLeEtLeOuLe( List<String> dateEffetRecherche, boolean obligatoire )
    {
        boolean ret = true;

        if ( dateEffetRecherche != null )
        {
            int size = dateEffetRecherche.size( );
            boolean dateEffetValide = true;
            boolean donneesPresentes = false;
            if ( size > 0 )
            {
                String dateTmp = dateEffetRecherche.get( 0 );
                if ( StringUtils.isNotEmpty( dateTmp ) )
                {
                    donneesPresentes = true;
                    dateEffetValide = DateUtils.validateDate( dateTmp );
                }

            }

            if ( ( size > 1 ) && dateEffetValide )
            {
                String dateTmp = dateEffetRecherche.get( 1 );
                if ( StringUtils.isNotEmpty( dateTmp ) )
                {
                    donneesPresentes = true;
                    dateEffetValide = DateUtils.validateDate( dateTmp );
                }
            }

            if ( ( size > 2 ) && dateEffetValide )
            {
                String dateTmp = dateEffetRecherche.get( 2 );
                if ( StringUtils.isNotEmpty( dateTmp ) )
                {
                    donneesPresentes = true;
                    dateEffetValide = DateUtils.validateDate( dateTmp );
                }

            }

            ret = dateEffetValide && ( !obligatoire || ( obligatoire && donneesPresentes ) );
        } else if ( obligatoire )
        {
            ret = false;
        }
        return ret;
    }

    /**
     * Checks that the time is in HH:mm format.
     *
     * @param sHeure
     *            string hour
     * @return true, if successful
     */
    public static boolean verifierHeure( String sHeure )
    {
        if ( StringUtils.isNotEmpty( sHeure ) )
        {
            String[] sHeureSplit = sHeure.split( ":" );
            if ( sHeureSplit.length == 2 )
            {
                try
                {
                    int heures = Integer.parseInt( sHeureSplit[0] );
                    int minutes = Integer.parseInt( sHeureSplit[1] );
                    if ( ( heures >= 0 ) && ( heures < 24 ) && ( minutes >= 0 ) && ( minutes < 60 ) )
                    {
                        return true;
                    }
                } catch ( NumberFormatException e )
                {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Same date or after.
     *
     * @param date
     *            the date
     * @param dateRef
     *            the date ref
     * @return true, if successful date is same day of after dateRef
     */
    public static boolean sameDateOrAfter( Date date, Date dateRef )
    {
        return DateUtils.sameDate( date, dateRef ) || date.after( dateRef );
    }

    /**
     * Same hour.
     *
     * @param date1
     *            the date1
     * @param date2
     *            the date2
     * @return true, if successful dates have same hour and same minutes
     */
    public static boolean sameHourOrAfter( Date date1, Date date2 )
    {
        Calendar cal1 = Calendar.getInstance( );
        Calendar cal2 = Calendar.getInstance( );
        cal1.setTime( date1 );
        cal2.setTime( date2 );
        return ( cal1.get( Calendar.HOUR_OF_DAY ) >= cal2.get( Calendar.HOUR_OF_DAY ) ) && ( cal1.get( Calendar.MINUTE ) >= cal2.get( Calendar.MINUTE ) );
    }

    /**
     * Same date.
     *
     * @param date
     *            the date
     * @param dateRef
     *            the date ref
     * @return true, if dates have same year, date and month
     */
    public static boolean sameDate( Date date, Date dateRef )
    {
        Calendar cal1 = Calendar.getInstance( );
        Calendar cal2 = Calendar.getInstance( );
        cal1.setTime( date );
        cal2.setTime( dateRef );
        return ( cal1.get( Calendar.YEAR ) == cal2.get( Calendar.YEAR ) ) && ( cal1.get( Calendar.DAY_OF_YEAR ) == cal2.get( Calendar.DAY_OF_YEAR ) );
    }

    /**
     * Return string for date HHmm
     * 
     * @param date
     *            the date
     * @return date HH:mm
     */
    public static String getHourFrSansColonne( Date date )
    {
        return new SimpleDateFormat( HOUR_FR_SANS_COLONNE ).format( date );
    }

    public static java.sql.Timestamp formatDateSqlWithTime( String dateString ) throws ParseException
    {

        Date dateValue = null;

        if ( dateString.contains( ":" ) )
        {
            dateValue = new SimpleDateFormat( DateUtils.DATE_FR_HR_SANS_COLONNE_AVEC_DEUX_POINT ).parse( dateString );

        } else
        {
            dateValue = new SimpleDateFormat( DateUtils.DATE_FR_HR_SANS_COLONNE ).parse( dateString );
        }

        return new java.sql.Timestamp( dateValue.getTime( ) );
    }

}
