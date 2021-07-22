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
package fr.paris.lutece.plugins.dansmarue.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.tika.io.IOUtils;

import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.string.StringUtil;

/**
 * The Class FileMessageCreationService.
 */
public class FileMessageCreationService implements IFileMessageCreationService
{

    /** The Constant DOT. */
    private static final String DOT = ".";

    /** The Constant UNDERSCORE. */
    private static final String UNDERSCORE = "_";

    /** The Constant REGEX. */
    private static final String REGEX = "\\W";

    /** The Constant RETOUR_CHARIOT. */
    private static final String RETOUR_CHARIOT = "<br/>";

    /**
     * Private constructor.
     */
    private FileMessageCreationService( )
    {
    }

    /**
     * Create a file.
     *
     * @param strFolderPath
     *            the folder path
     * @param strFileName
     *            the file name
     * @param strFileOutPut
     *            the file output
     * @param strEncoding
     *            the encoding
     * @throws IOException
     *             exception if there is an error during the deletion
     */
    @Override
    public void createFile( String strFolderPath, String strFileName, String strFileOutPut, String strEncoding ) throws IOException
    {
        File file = new File( strFolderPath + strFileName );

        // Delete the file if it exists
        deleteFile( strFolderPath, strFileName );

        org.apache.commons.io.FileUtils.writeStringToFile( file, strFileOutPut, strEncoding );
    }

    /**
     * Delete a file.
     *
     * @param strFolderPath
     *            the folder path
     * @param strFileName
     *            the file name
     * @throws IOException
     *             exception if there is an error during the deletion
     */
    @Override
    public void deleteFile( String strFolderPath, String strFileName ) throws IOException
    {
        File file = new File( strFolderPath + strFileName );

        if ( file.exists( ) && !file.delete( ) )
        {
            throw new IOException( "ERROR when deleting the file or folder " + strFolderPath + strFileName );
        }
    }

    /**
     * Build the file name.
     *
     * @param strFileName
     *            the filename to set
     * @param strFormatExtension
     *            the format extension
     * @return the file name
     */
    @Override
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
     * Read content from the given file.
     *
     * @param strFile
     *            the file absolute path (ex : /home/filetopath/file.txt)
     * @return the string, an empty string if the file does not exists
     */
    @Override
    public String readFile( String strFile )
    {
        FileInputStream in = null;

        try
        {
            in = new FileInputStream( strFile );
        }
        catch( FileNotFoundException e )
        {

            AppLogService.error( e.getMessage( ), e );

            return StringUtils.EMPTY;
        }

        String strContent = StringUtils.EMPTY;
        try ( BufferedReader br = new BufferedReader( new InputStreamReader( in ) ) )
        {
            StringBuilder buf = new StringBuilder( );

            while ( br.ready( ) )
            {
                String strLine = br.readLine( );
                buf.append( RETOUR_CHARIOT );
                if ( SignalementUtils.isWindows( ) && strLine.equals( StringUtils.EMPTY ) )
                {
                    buf.append( RETOUR_CHARIOT );
                }
                buf.append( strLine );
            }

            strContent = buf.toString( );
        }
        catch( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        finally
        {
            IOUtils.closeQuietly( in );
        }

        return strContent;
    }

    /**
     * Write to the given file.
     *
     * @param strContent
     *            the content to write
     * @param strFile
     *            the file
     */
    @Override
    public void writeToFile( String strContent, String strFile )
    {

        try ( FileWriter fw = new FileWriter( strFile, false ) ; )
        {
            fw.write( strContent );
        }
        catch( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
    }

}
