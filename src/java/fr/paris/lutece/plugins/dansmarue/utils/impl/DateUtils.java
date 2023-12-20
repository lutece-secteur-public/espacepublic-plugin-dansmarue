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
package fr.paris.lutece.plugins.dansmarue.utils.impl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.dansmarue.util.constants.DateConstants;
import fr.paris.lutece.plugins.dansmarue.utils.IDateUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.date.DateUtil;

/**
 * DateUtils
 */
public final class DateUtils implements IDateUtils
{

    /** The Constant YEAR_1900. */
    private final int YEAR_1900 = 1900;

    /**
     * {@inheritDoc}
     */
    @Override
    public String converteDateAnglais( String dateAnglaise ) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-yyyy" );
        SimpleDateFormat sdf2 = new SimpleDateFormat( DateConstants.DATE_FR );
        return sdf.format( sdf2.parse( dateAnglaise ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String getAnnee( Date date )
    {

        SimpleDateFormat sdfAnnee = new SimpleDateFormat( "yyyy" );

        return sdfAnnee.format( date );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAnneeEnCours( )
    {
        Calendar caldate = new GregorianCalendar( );
        caldate.setTime( new Date( ) );
        return caldate.get( Calendar.YEAR );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAnneeInt( Date date )
    {
        Calendar calendar = Calendar.getInstance( );
        calendar.setTime( date );

        return calendar.get( Calendar.YEAR );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp getCurrentDate( )
    {
        return new Timestamp( Calendar.getInstance( ).getTimeInMillis( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentDateString( )
    {
        return new SimpleDateFormat( DateConstants.DATE_FR ).format( new Timestamp( System.currentTimeMillis( ) ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentDateString( String strPattern )
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat( strPattern, Locale.FRENCH );

        return dateFormat.format( new Timestamp( System.currentTimeMillis( ) ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp getCurrentHourAndMinute( )
    {
        Calendar calendar = Calendar.getInstance( );
        calendar.setTime( new Date( ) );
        calendar.set( Calendar.YEAR, YEAR_1900 );
        calendar.set( Calendar.MONTH, Calendar.JANUARY );
        calendar.set( Calendar.DAY_OF_MONTH, 1 );
        return new Timestamp( calendar.getTimeInMillis( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDate( Date date, String strPattern )
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat( strPattern, Locale.FRENCH );
        return dateFormat.format( date );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp getDate( String strDate, boolean isStartOfDayHour )
    {
        if ( ( strDate == null ) )
        {
            return null;
        }

        DateFormat dateFormat = new SimpleDateFormat( DateConstants.DATE_FR );
        dateFormat.setLenient( false );

        Date date;

        try
        {
            date = dateFormat.parse( strDate.trim( ) );
        }
        catch( ParseException e )
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
        }
        else
        {
            caldate.set( Calendar.HOUR_OF_DAY, caldate.getActualMaximum( Calendar.HOUR_OF_DAY ) );
            caldate.set( Calendar.MINUTE, caldate.getActualMaximum( Calendar.MINUTE ) );
            caldate.set( Calendar.SECOND, caldate.getActualMaximum( Calendar.SECOND ) );
        }

        return new Timestamp( caldate.getTimeInMillis( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp getDate( Timestamp date, boolean isStartOfDayHour )
    {
        Calendar caldate = new GregorianCalendar( );
        caldate.setTime( date );
        caldate.set( Calendar.MILLISECOND, 0 );
        caldate.set( Calendar.SECOND, 0 );

        if ( isStartOfDayHour )
        {
            caldate.set( Calendar.HOUR_OF_DAY, caldate.getActualMinimum( Calendar.HOUR_OF_DAY ) );
            caldate.set( Calendar.MINUTE, caldate.getActualMinimum( Calendar.MINUTE ) );
        }
        else
        {
            caldate.set( Calendar.HOUR_OF_DAY, caldate.getActualMaximum( Calendar.HOUR_OF_DAY ) );
            caldate.set( Calendar.MINUTE, caldate.getActualMaximum( Calendar.MINUTE ) );
            caldate.set( Calendar.SECOND, caldate.getActualMaximum( Calendar.SECOND ) );
        }

        return new Timestamp( caldate.getTimeInMillis( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDate( Timestamp date, String strPattern )
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat( strPattern, Locale.FRENCH );
        return dateFormat.format( date );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDateFr( Date date )
    {
        if ( date == null )
        {
            return null;
        }
        return new SimpleDateFormat( DateConstants.DATE_FR ).format( date );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp getHour( String strHour )
    {
        if ( ( strHour == null ) )
        {
            return null;
        }

        DateFormat dateFormat = new SimpleDateFormat( DateConstants.HOUR_FR );
        dateFormat.setLenient( false );

        Date date;
        Timestamp sqlDate;

        try
        {
            date = dateFormat.parse( strHour.trim( ) );
            sqlDate = new Timestamp( date.getTime( ) );
        }
        catch( ParseException e )
        {
            return null;
        }

        return sqlDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHourFr( Date date )
    {
        return new SimpleDateFormat( DateConstants.HOUR_FR ).format( date );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHourWithSecondsFr( Date timestamp )
    {
        return new SimpleDateFormat( DateConstants.HOUR_FR_AVEC_SECONDES ).format( timestamp );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getHourWithoutDate( Date hour )
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
     * {@inheritDoc}
     */
    @Override
    public String getMois( Date date )
    {
        SimpleDateFormat sdfMois = new SimpleDateFormat( "MM" );
        return sdfMois.format( date );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMoisInt( Date date )
    {
        Calendar calendar = Calendar.getInstance( );
        calendar.setTime( date );

        return calendar.get( Calendar.MONTH );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString( Date date )
    {
        String ret;
        if ( date != null )
        {
            SimpleDateFormat sdf = new SimpleDateFormat( DateConstants.DATE_FR );
            ret = sdf.format( date );
        }
        else
        {
            ret = "";
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAfter( String strDateToCheck, String strReferenceDateDate )
    {
        // get times...
        long lDateToCheck = DateUtil.formatDate( strDateToCheck, Locale.FRANCE ).getTime( );
        long lDateReference = DateUtil.formatDate( strReferenceDateDate, Locale.FRANCE ).getTime( );

        // check values
        return lDateToCheck > lDateReference;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEqualOrAfter( String strDateToCheck, String strReferenceDateDate )
    {
        // get times...
        long lDateToCheck = DateUtil.formatDate( strDateToCheck, Locale.FRANCE ).getTime( );
        long lDateReference = DateUtil.formatDate( strReferenceDateDate, Locale.FRANCE ).getTime( );

        // check values
        return lDateToCheck >= lDateReference;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date mergeDateHour( Date date, Date hour )
    {
        Calendar calDate = new GregorianCalendar( );
        calDate.setTime( date );

        Calendar calHour = new GregorianCalendar( );
        calHour.setTime( hour );

        calDate.set( Calendar.HOUR_OF_DAY, calHour.get( Calendar.HOUR_OF_DAY ) );
        calDate.set( Calendar.MINUTE, calHour.get( Calendar.MINUTE ) );

        return calDate.getTime( );
    }

    /**
     * Parses the.
     *
     * @param strDate
     *            the str date
     * @param format
     *            the format
     * @return the date
     */
    private Date parse( String strDate, String format )
    {
        Date ret;
        if ( strDate != null )
        {
            SimpleDateFormat sdf = new SimpleDateFormat( format );
            try
            {
                ret = sdf.parse( strDate );
            }
            catch( ParseException e )
            {
                ret = null;
            }
        }
        else
        {
            ret = null;
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date parseDate( String strDate )
    {
        return parse( strDate, DateConstants.DATE_FR );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date parseHour( String hour )
    {
        return parse( hour, DateConstants.HOUR_FR );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateDate( String date )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( DateConstants.DATE_FR );
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
            }
            catch( Exception ex )
            {
                AppLogService.error( ex.getMessage( ), ex );
                hasError = false;
            }
        }

        return hasError;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean valideDateEntreLeEtLeOuLe( List<String> dateEffetRecherche, boolean obligatoire )
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
                    dateEffetValide = validateDate( dateTmp );
                }

            }

            if ( ( size > 1 ) && dateEffetValide )
            {
                String dateTmp = dateEffetRecherche.get( 1 );
                if ( StringUtils.isNotEmpty( dateTmp ) )
                {
                    donneesPresentes = true;
                    dateEffetValide = validateDate( dateTmp );
                }
            }

            if ( ( size > 2 ) && dateEffetValide )
            {
                String dateTmp = dateEffetRecherche.get( 2 );
                if ( StringUtils.isNotEmpty( dateTmp ) )
                {
                    donneesPresentes = true;
                    dateEffetValide = validateDate( dateTmp );
                }

            }

            ret = dateEffetValide && ( !obligatoire || ( donneesPresentes ) );
        }
        else
            if ( obligatoire )
            {
                ret = false;
            }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifierHeure( String sHeure )
    {
        if ( StringUtils.isNotEmpty( sHeure ) )
        {
            String [ ] sHeureSplit = sHeure.split( ":" );
            if ( sHeureSplit.length == 2 )
            {
                try
                {
                    int heures = Integer.parseInt( sHeureSplit [0] );
                    int minutes = Integer.parseInt( sHeureSplit [1] );
                    if ( ( heures >= 0 ) && ( heures < 24 ) && ( minutes >= 0 ) && ( minutes < 60 ) )
                    {
                        return true;
                    }
                }
                catch( NumberFormatException e )
                {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean sameDateOrAfter( Date date, Date dateRef )
    {
        return sameDate( date, dateRef ) || date.after( dateRef );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean sameHourOrAfter( Date date1, Date date2 )
    {
        Calendar cal1 = Calendar.getInstance( );
        Calendar cal2 = Calendar.getInstance( );
        cal1.setTime( date1 );
        cal2.setTime( date2 );
        return ( cal1.get( Calendar.HOUR_OF_DAY ) >= cal2.get( Calendar.HOUR_OF_DAY ) ) && ( cal1.get( Calendar.MINUTE ) >= cal2.get( Calendar.MINUTE ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean sameDate( Date date, Date dateRef )
    {
        Calendar cal1 = Calendar.getInstance( );
        Calendar cal2 = Calendar.getInstance( );
        cal1.setTime( date );
        cal2.setTime( dateRef );
        return ( cal1.get( Calendar.YEAR ) == cal2.get( Calendar.YEAR ) ) && ( cal1.get( Calendar.DAY_OF_YEAR ) == cal2.get( Calendar.DAY_OF_YEAR ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHourFrSansColonne( Date date )
    {
        return new SimpleDateFormat( DateConstants.HOUR_FR_SANS_COLONNE ).format( date );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public java.sql.Timestamp formatDateSqlWithTime( String dateString ) throws ParseException
    {

        Date dateValue = null;

        if ( dateString.contains( ":" ) )
        {
            dateValue = new SimpleDateFormat( DateConstants.DATE_FR_HR_SANS_COLONNE_AVEC_DEUX_POINT ).parse( dateString );

        }
        else
        {
            dateValue = new SimpleDateFormat( DateConstants.DATE_FR_HR_SANS_COLONNE ).parse( dateString );
        }

        return new java.sql.Timestamp( dateValue.getTime( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDateMoreThanXDay( String strDateCreation, Integer nbDay )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( DateConstants.DATE_FR );

        Date currentDate = new Date( );
        LocalDateTime localDateTime = currentDate.toInstant( ).atZone( ZoneId.systemDefault( ) ).toLocalDateTime( );
        localDateTime = localDateTime.minusDays( nbDay );

        Date currentDatePlusXDay = Date.from( localDateTime.atZone( ZoneId.systemDefault( ) ).toInstant( ) );
        try
        {
            Date dateCreation = sdf.parse( strDateCreation );
            return dateCreation.compareTo( currentDatePlusXDay ) < 0;
        }
        catch( ParseException e )
        {
            return false;
        }
    }

}
