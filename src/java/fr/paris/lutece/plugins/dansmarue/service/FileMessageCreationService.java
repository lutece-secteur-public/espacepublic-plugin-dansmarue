package fr.paris.lutece.plugins.dansmarue.service;

import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.string.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.tika.io.IOUtils;


public class FileMessageCreationService implements IFileMessageCreationService
{

    private static final String DOT = ".";
    private static final String UNDERSCORE = "_";
    private static final String REGEX = "\\W";
    private static final String RETOUR_CHARIOT = "<br/>";

    /**
     * Private constructor
     */
    private FileMessageCreationService( )
    {
    }

        /**
     * Create a file
     * @param strFolderPath the folder path
     * @param strFileName the file name
     * @param strFileOutPut the file output
     * @param strEncoding the encoding
     * @throws IOException exception if there is an error during the deletion
     */
    public void createFile( String strFolderPath, String strFileName, String strFileOutPut, String strEncoding )
            throws IOException
    {
        File file = new File( strFolderPath + strFileName );

        // Delete the file if it exists
        deleteFile( strFolderPath, strFileName );

        org.apache.commons.io.FileUtils.writeStringToFile( file, strFileOutPut, strEncoding );
    }

    /**
     * Delete a file
     * @param strFolderPath the folder path
     * @param strFileName the file name
     * @throws IOException exception if there is an error during the deletion
     */
    public void deleteFile( String strFolderPath, String strFileName ) throws IOException
    {
        File file = new File( strFolderPath + strFileName );

        if ( file.exists( ) )
        {
            if ( !file.delete( ) )
            {
                throw new IOException( "ERROR when deleting the file or folder " + strFolderPath + strFileName );
            }
        }
    }

    /**
     * Build the file name
     * @param strFileName the filename to set
     * @param strFormatExtension the format extension
     * @return the file name
     */
    public String buildFileName( String strFileName, String strFormatExtension )
    {
        if ( StringUtils.isNotBlank( strFileName ) )
        {
            String strFullName = StringUtil.replaceAccent( strFileName ).replaceAll( REGEX, UNDERSCORE );

            if ( StringUtils.isNotBlank( strFormatExtension ) )
            {
                return strFullName + DOT + strFormatExtension;
            }

            return strFullName;
        }

        return StringUtils.EMPTY;
    }

    /**
     * Read content from the given file
     * @param strFile the file absolute path (ex : /home/filetopath/file.txt)
     * @return the string, an empty string if the file does not exists
     */
    public String readFile( String strFile )
    {
        FileInputStream in = null;

        try
        {
            in = new FileInputStream( strFile );
        }
        catch ( FileNotFoundException e )
        {
            if ( in != null )
            {
                try
                {
                    in.close( );
                }
                catch ( IOException e1 )
                {
                    AppLogService.error( e.getMessage( ), e );
                }
            }

            return StringUtils.EMPTY;
        }

        String strContent = StringUtils.EMPTY;
        BufferedReader br = new BufferedReader( new InputStreamReader( in ) );
        String strTmp = StringUtils.EMPTY;

        try
        {
            while ( br.ready( ) )
            {
                String strLine = br.readLine( );
                strTmp += RETOUR_CHARIOT;
                if (SignalementUtils.isWindows( ))
                {
                    if ( strLine.equals( StringUtils.EMPTY ) )
                    {
                        strTmp += RETOUR_CHARIOT;
                    }   
                }
                strTmp += strLine;
            }
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        finally
        {
            IOUtils.closeQuietly( in );
        }

        strContent = strTmp;

        return strContent;
    }

    /**
     * Write to the given file
     * @param strContent the content to write
     * @param strFile the file
     */
    public void writeToFile( String strContent, String strFile )
    {
        FileWriter fw = null;

        try
        {
            fw = new FileWriter( strFile, false );
            fw.write( strContent );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        finally
        {
            IOUtils.closeQuietly( fw );
        }
    }


}
