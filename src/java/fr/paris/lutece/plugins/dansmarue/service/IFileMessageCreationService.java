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
package fr.paris.lutece.plugins.dansmarue.service;

import java.io.IOException;

/**
 * The Interface IFileMessageCreationService.
 */
public interface IFileMessageCreationService
{

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
    void createFile( String strFolderPath, String strFileName, String strFileOutPut, String strEncoding ) throws IOException;

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
    void deleteFile( String strFolderPath, String strFileName ) throws IOException;

    /**
     * Build the file name.
     *
     * @param strFileName
     *            the file name to set
     * @param strFormatExtension
     *            the format extension
     * @return the file name
     */
    String buildFileName( String strFileName, String strFormatExtension );

    /**
     * Read content from the given file.
     *
     * @param strFile
     *            the file absolute path (ex : /home/filetopath/file.txt)
     * @return the string, an empty string if the file does not exists
     */
    String readFile( String strFile );

    /**
     * Write to the given file.
     *
     * @param strContent
     *            the content to write
     * @param strFile
     *            the file
     */
    void writeToFile( String strContent, String strFile );

}
