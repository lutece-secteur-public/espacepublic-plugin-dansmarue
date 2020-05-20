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
package fr.paris.lutece.plugins.dansmarue.upload.handler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;

/**
 * The Class DansMaRueUploadHandler.
 */
public class DansMaRueUploadHandler extends AbstractUploadHandler
{

    /** The Constant DMR_UPLOAD_HANDLER. */
    public static final String                              DMR_UPLOAD_HANDLER    = "DMRUploadHandler";

    /** The Constant SESSION_DEMANDE_ID. */
    public static final String                              SESSION_DEMANDE_ID    = "demandeSignalement";

    /** The map asynchronous upload. */
    private static Map<String, Map<String, List<FileItem>>> mapAsynchronousUpload = new ConcurrentHashMap<>( );

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.dansmarue.upload.handler.AbstractUploadHandler#getIdInSession(javax.servlet.http.HttpSession)
     */
    @Override
    public String getIdInSession( HttpSession session )
    {
        Object numDemande = session.getAttribute( SESSION_DEMANDE_ID );
        return numDemande != null ? numDemande.toString( ) : null;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.dansmarue.upload.handler.AbstractUploadHandler#getUploadDirectory()
     */
    @Override
    public String getUploadDirectory( )
    {
        return uploadDirectory;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.dansmarue.upload.handler.AbstractUploadHandler#getMapAsynchronousUpload()
     */
    @Override
    Map<String, Map<String, List<FileItem>>> getMapAsynchronousUpload( )
    {
        return mapAsynchronousUpload;
    }

}
