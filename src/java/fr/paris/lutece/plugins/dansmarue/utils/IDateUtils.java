package fr.paris.lutece.plugins.dansmarue.utils;

import fr.paris.lutece.plugins.dansmarue.utils.impl.DateUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.date.DateUtil;
import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * IDateUtils
 */
public interface IDateUtils
{
    /**
     * Convert a date from the dd/MM/yyyyy format to the dd-MM-yyyyy format.
     *
     * @param dateAnglaise
     *            date in dd/MM/yyyyyy format
     * @return date in dd-MM-yyyyyy format
     * @throws ParseException
     *             throws if the entry date is not valid.
     */
    String converteDateAnglais( String dateAnglaise ) throws ParseException;

    /**
     * Returns a date year.
     *
     * @param date
     *            date
     * @return year
     */
    String getAnnee( Date date );

    /**
     * Returns the current year.
     *
     * @return current year
     */
    int getAnneeEnCours( );

    /**
     * Gets the annee int.
     *
     * @param date
     *            the date
     * @return the annee int
     */
    int getAnneeInt( Date date );

    /**
     * Returns the current date.
     *
     * @return the current date
     */
    Timestamp getCurrentDate( );

    /**
     * Returns the current date.
     *
     * @return the current date
     */
    String getCurrentDateString( );

    /**
     * Returns the current date.
     *
     * @param strPattern
     *            the current date format
     * @return the current date
     */
    String getCurrentDateString( String strPattern );

    /**
     * Gets the current hour and minute.
     *
     * @return the current hour and minute
     */
    Timestamp getCurrentHourAndMinute( );

    /**
     * Returns the date in the format defined by strPattern.
     *
     * @param date
     *            date
     * @param strPattern
     *            the desired format for the date
     * @return the date in EEEE dd MMMMM yyyyy format
     */
    String getDate( Date date, String strPattern );

    /**
     * Transforms a date in string format of type dd/MM/yyyyyy to Timestamp object.
     *
     * @param strDate
     *            Date to be transformed
     * @param isStartOfDayHour
     *            TRUE if the time is to be 00:01 FALSE if the time is to be 23:59
     * @return objet Timestamp corresponding to the date given in parameter
     */
    Timestamp getDate( String strDate, boolean isStartOfDayHour );

    /**
     * Returns a timestamp whose time must be 00:01 FALSE if the time must be 23:59.
     *
     * @param date
     *            Date to be transformed
     * @param isStartOfDayHour
     *            true if the time is to be 00h01 FALSE if the time is to be 23h59
     * @return objet Timestamp corresponding to the date given in parameter
     */
    Timestamp getDate( Timestamp date, boolean isStartOfDayHour );

    /**
     * Returns the date in the format defined by strPattern.
     *
     * @param date
     *            the date
     * @param strPattern
     *            the format wishes from the date
     * @return the date in EEEE dd MMMMM yyyyy format
     */
    String getDate( Timestamp date, String strPattern );

    /**
     * Return string for date dd/MM/yyyy.
     *
     * @param date
     *            the date
     * @return date dd/MM/yyyy
     */
    String getDateFr( Date date );

    /**
     * Transforms a date in string format of type HH:mm into a date object.
     *
     * @param strHour
     *            Date to be transformed
     * @return objet date corresponding to the date given in parameter
     */
    Timestamp getHour( String strHour );

    /**
     * Return string for date HH:mm.
     *
     * @param date
     *            the date
     * @return date HH:mm
     */
    String getHourFr( Date date );

    /**
     * Return string for date HH:mm:ss.
     *
     * @param timestamp
     *            the hours to read
     * @return the formated hours
     */
    String getHourWithSecondsFr( Date timestamp );

    /**
     * Get a timestamp with hour setted and date 01/01/1970.
     *
     * @param hour
     *            hour to set to the date
     * @return the hour without date
     */
    Date getHourWithoutDate( Date hour );

    /**
     * Returns the year of a date.
     *
     * @param date
     *            date
     * @return year
     */
    String getMois( Date date );

    /**
     * Gets the mois int.
     *
     * @param date
     *            the date
     * @return the mois int
     */
    int getMoisInt( Date date );

    /**
     * Gets the string.
     *
     * @param date
     *            the date
     * @return the string
     */
    String getString( Date date );

    /**
     * Checks if the date is <strong>STRICTLY</strong> after the reference date.
     *
     * @param strDateToCheck
     *            the date to check
     * @param strReferenceDateDate
     *            the date to be checked against
     * @return true if the the date to check is after the reference date
     */
    boolean isAfter( String strDateToCheck, String strReferenceDateDate );

    /**
     * Checks if the first date is after the second one.
     *
     * @param strDateToCheck
     *            the date to check
     * @param strReferenceDateDate
     *            the date to be checked against
     * @return true if strDateToCheck greater or equals to strReferenceDateDate
     */
    boolean isEqualOrAfter( String strDateToCheck, String strReferenceDateDate );

    /**
     * Set the given date hour with the given hour.
     *
     * @param date
     *            date
     * @param hour
     *            hour to set to the date
     * @return the date
     */
    Date mergeDateHour( Date date, Date hour );

    /**
     * Parses the string to fr date (dd/MM/yyyy).
     *
     * @param strDate
     *            the str date
     * @return the date
     */
    Date parseDate( String strDate );

    /**
     * Parses the hour.
     *
     * @param hour
     *            the hour
     * @return the date
     */
    Date parseHour( String hour );

    /**
     * Validate a date.
     *
     * @param date
     *            la date
     * @return boolean return true if the field is formated with dd/mm/yyyy
     */
    boolean validateDate( String date );

    /**
     * checks a trigram of dates between the... and the... or the.
     *
     * @param dateEffetRecherche
     *            the date effet recherche
     * @param obligatoire
     *            if mandatory is true the method will also check if at least one value is entered.
     * @return true if the dates are valid (and if a date is entered).
     */
    boolean valideDateEntreLeEtLeOuLe( List<String> dateEffetRecherche, boolean obligatoire );

    /**
     * Checks that the time is in HH:mm format.
     *
     * @param sHeure
     *            string hour
     * @return true, if successful
     */
    boolean verifierHeure( String sHeure );

    /**
     * Same date or after.
     *
     * @param date
     *            the date
     * @param dateRef
     *            the date ref
     * @return true, if successful date is same day of after dateRef
     */
    boolean sameDateOrAfter( Date date, Date dateRef );

    /**
     * Same hour.
     *
     * @param date1
     *            the date1
     * @param date2
     *            the date2
     * @return true, if successful dates have same hour and same minutes
     */
    boolean sameHourOrAfter( Date date1, Date date2 );

    /**
     * Same date.
     *
     * @param date
     *            the date
     * @param dateRef
     *            the date ref
     * @return true, if dates have same year, date and month
     */
    boolean sameDate( Date date, Date dateRef );

    /**
     * Return string for date HHmm.
     *
     * @param date
     *            the date
     * @return date HH:mm
     */
    String getHourFrSansColonne( Date date );

    /**
     * Format date sql with time.
     *
     * @param dateString
     *            the date string
     * @return the java.sql. timestamp
     * @throws ParseException
     *             the parse exception
     */
    java.sql.Timestamp formatDateSqlWithTime( String dateString ) throws ParseException;

    /**
     * Checks if is date more than X day.
     *
     * @param strDateCreation
     *            the str date creation
     * @param nbDay
     *            the nb day
     * @return true, if is date more than X day
     */
    boolean isDateMoreThanXDay( String strDateCreation, Integer nbDay );
}
