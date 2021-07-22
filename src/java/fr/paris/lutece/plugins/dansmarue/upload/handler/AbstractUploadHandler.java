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
package fr.paris.lutece.plugins.dansmarue.upload.handler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

import fr.paris.lutece.plugins.asynchronousupload.service.AbstractAsynchronousUploadHandler;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.filesystem.UploadUtil;

/**
 * The Class AbstractUploadHandler.
 */
public abstract class AbstractUploadHandler extends AbstractAsynchronousUploadHandler
{

    /** The upload directory. */
    @Value( "${signalement.directory.upload}" )
    protected String uploadDirectory;

    /** The max file size. */
    @Value( "${signalement.upload.max.file.size}" )
    private String maxFileSize;

    /** The max category files count. */
    @Value( "${signalement.upload.max.category.files.count}" )
    private Integer maxCategoryFilesCount;

    /** The Constant PREFIX_ENTRY_ID. */
    private static final String PREFIX_ENTRY_ID = "dansmarue_";

    /** The Constant LOGGER. */
    private static final Log LOGGER = LogFactory.getLog( AbstractUploadHandler.class );

    /** The Constant ERROR_EXTENSION. */
    public static final String ERROR_EXTENSION = "dansmarue.message.upload.file.error.extension";

    /** The Constant ERROR_FILES_COUNT. */
    public static final String ERROR_FILES_COUNT = "dansmarue.message.upload.file.error.files.count";

    /** The Constant HANDLER_NAME. */
    private static final String HANDLER_NAME = "DansmarueAsynchronousUploadHandler";

    /** The extension list. */
    private static List<String> extensionList = Arrays.asList( "jpeg", "png", "jpg" );

    /**
     * Instantiates a new abstract upload handler.
     */
    public AbstractUploadHandler( )
    {
        // Constructor
    }

    /**
     * Gets the max file size.
     *
     * @return the max file size
     */
    public String getMaxFileSize( )
    {
        return maxFileSize;
    }

    /**
     * Adds the file item to uploaded files list.
     *
     * @param fileItem
     *            the file item
     * @param strFieldName
     *            the str field name
     * @param request
     *            the request
     */
    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.asynchronousupload.service.IAsyncUploadHandler#addFileItemToUploadedFilesList(org.apache.commons.fileupload.FileItem,
     * java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void addFileItemToUploadedFilesList( FileItem fileItem, String strFieldName, HttpServletRequest request )
    {
        String strFileName = UploadUtil.cleanFileName( fileItem.getName( ).trim( ) );

        initMap( request.getSession( ), PREFIX_ENTRY_ID + strFieldName );

        List<FileItem> uploadedFiles = getListUploadedFiles( strFieldName, request.getSession( ) );

        if ( uploadedFiles != null )
        {
            boolean bNew = true;

            if ( !uploadedFiles.isEmpty( ) )
            {
                Iterator<FileItem> iterUploadedFiles = uploadedFiles.iterator( );

                while ( bNew && iterUploadedFiles.hasNext( ) )
                {
                    FileItem uploadedFile = iterUploadedFiles.next( );
                    String strUploadedFileName = UploadUtil.cleanFileName( uploadedFile.getName( ).trim( ) );
                    bNew = !( StringUtils.equals( strUploadedFileName, strFileName ) && ( uploadedFile.getSize( ) == fileItem.getSize( ) ) );
                }
            }

            if ( bNew )
            {
                uploadedFiles.add( fileItem );
            }
        }
    }

    /**
     * Can upload files.
     *
     * @param request
     *            the request
     * @param strFieldName
     *            the str field name
     * @param listFileItemsToUpload
     *            the list file items to upload
     * @param locale
     *            the locale
     * @return the string
     */
    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.asynchronousupload.service.IAsyncUploadHandler#canUploadFiles(javax.servlet.http.HttpServletRequest, java.lang.String,
     * java.util.List, java.util.Locale)
     */
    @Override
    public String canUploadFiles( HttpServletRequest request, String strFieldName, List<FileItem> listFileItemsToUpload, Locale locale )
    {
        if ( StringUtils.isNotBlank( strFieldName ) )
        {

            if ( getListUploadedFiles( strFieldName, request.getSession( ) ).size( ) >= maxCategoryFilesCount )
            {
                return I18nService.getLocalizedString( ERROR_FILES_COUNT, new Object [ ] {
                        maxCategoryFilesCount
                }, locale );
            }

            for ( FileItem fileItem : listFileItemsToUpload )
            {
                if ( !extensionList.contains( FilenameUtils.getExtension( fileItem.getName( ).toLowerCase( ) ) ) )
                {
                    return I18nService.getLocalizedString( ERROR_EXTENSION, locale ) + StringUtils.join( extensionList.toArray( ), ", " );
                }
            }
        }
        return null;
    }

    /**
     * Gets the handler name.
     *
     * @return the handler name
     */
    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.asynchronousupload.service.IAsyncUploadHandler#getHandlerName()
     */
    @Override
    public String getHandlerName( )
    {
        return HANDLER_NAME;
    }

    /**
     * Gets the file.
     *
     * @param request
     *            the request
     * @param strFieldName
     *            the str field name
     * @return the file
     */
    public FileItem getFile( HttpServletRequest request, String strFieldName )
    {
        if ( StringUtils.isNotBlank( strFieldName ) )
        {
            List<FileItem> listUploadedFileItems = getListUploadedFiles( strFieldName, request.getSession( ) );

            if ( !listUploadedFileItems.isEmpty( ) )
            {
                return listUploadedFileItems.get( 0 );
            }
        }
        return null;
    }

    /**
     * Gets the list uploaded files.
     *
     * @param strFieldName
     *            the str field name
     * @param session
     *            the session
     * @return the list uploaded files
     */
    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.asynchronousupload.service.IAsyncUploadHandler#getListUploadedFiles(java.lang.String, javax.servlet.http.HttpSession)
     */
    @Override
    public List<FileItem> getListUploadedFiles( String strFieldName, HttpSession session )
    {
        if ( StringUtils.isBlank( strFieldName ) )
        {
            throw new AppException( "id field name is not provided for the current file upload" );
        }

        initMap( session, strFieldName );

        Map<String, List<FileItem>> mapFileItemsSession = getMapAsynchronousUpload( ).get( session.getId( ) );

        return mapFileItemsSession.get( strFieldName );
    }

    /**
     * Removes the file item.
     *
     * @param strFieldName
     *            the str field name
     * @param session
     *            the session
     * @param nIndex
     *            the n index
     */
    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.asynchronousupload.service.IAsyncUploadHandler#removeFileItem(java.lang.String, javax.servlet.http.HttpSession, int)
     */
    @Override
    public void removeFileItem( String strFieldName, HttpSession session, int nIndex )
    {
        List<FileItem> uploadedFiles = getListUploadedFiles( strFieldName, session );
        remove( strFieldName, session, nIndex, uploadedFiles );

    }

    /**
     * Inits the map.
     *
     * @param session
     *            the session
     * @param strFieldName
     *            the str field name
     */
    private void initMap( HttpSession session, String strFieldName )
    {
        String strSessionId = session.getId( );
        Map<String, List<FileItem>> mapFileItemsSession = getMapAsynchronousUpload( ).get( strSessionId );

        if ( mapFileItemsSession == null )
        {
            synchronized( this )
            {
                mapFileItemsSession = getMapAsynchronousUpload( ).get( strSessionId );

                if ( mapFileItemsSession == null )
                {
                    mapFileItemsSession = new ConcurrentHashMap<String, List<FileItem>>( );
                    getMapAsynchronousUpload( ).put( strSessionId, mapFileItemsSession );
                }
            }
        }

        mapFileItemsSession.computeIfAbsent( strFieldName, k -> new ArrayList<>( ) );

    }

    /**
     * Removes the file.
     *
     * @param session
     *            the session
     * @param fileToSave
     *            the file to save
     * @param categoryName
     *            the category name
     */
    private void removeFile( HttpSession session, FileItem fileToSave, String categoryName )
    {
        String id = getIdInSession( session );
        if ( ( null == id ) || ( null == fileToSave ) || StringUtils.isEmpty( categoryName ) )
        {
            return;
        }

        String categoryPath = FilenameUtils.concat( getUploadDirectory( ), id );
        categoryPath = FilenameUtils.concat( categoryPath, categoryName );

        String fileName = FilenameUtils.getName( fileToSave.getName( ) );
        try
        {
            String filePath = FilenameUtils.concat( categoryPath, fileName );
            Path path = Paths.get( filePath );
            Files.deleteIfExists( path );

            LOGGER.info( "Fichier supprimé avec succès : " + fileName );
        }
        catch( IOException ex )
        {
            LOGGER.error( "Impossible de supprimer le fichier : " + fileName, ex );
        }
    }

    /**
     * Removes the.
     *
     * @param strFieldName
     *            the str field name
     * @param session
     *            the session
     * @param nIndex
     *            the n index
     * @param uploadedFiles
     *            the uploaded files
     */
    private void remove( String strFieldName, HttpSession session, int nIndex, List<FileItem> uploadedFiles )
    {
        if ( ( uploadedFiles != null ) && !uploadedFiles.isEmpty( ) && ( uploadedFiles.size( ) > nIndex ) )
        {
            FileItem fileItem = uploadedFiles.remove( nIndex );
            removeFile( session, fileItem, strFieldName );
            fileItem.delete( );
        }
    }

    /**
     * Checks for file.
     *
     * @param request
     *            the request
     * @param strFieldName
     *            the str field name
     * @return true, if successful
     */
    public boolean hasFile( HttpServletRequest request, String strFieldName )
    {
        if ( StringUtils.isNotBlank( strFieldName ) )
        {
            List<FileItem> listUploadedFileItems = getListUploadedFiles( strFieldName, request.getSession( ) );

            if ( !listUploadedFileItems.isEmpty( ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the id in session.
     *
     * @param session
     *            the session
     * @return the id in session
     */
    public abstract String getIdInSession( HttpSession session );

    /**
     * Gets the upload directory.
     *
     * @return the upload directory
     */
    public abstract String getUploadDirectory( );

    /**
     * Gets the map asynchronous upload.
     *
     * @return the map asynchronous upload
     */
    abstract Map<String, Map<String, List<FileItem>>> getMapAsynchronousUpload( );
}
