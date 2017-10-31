package fr.paris.lutece.plugins.dansmarue.service;

import java.io.IOException;



public interface IFileMessageCreationService
{

    /**
     * Create a file
     * @param strFolderPath the folder path
     * @param strFileName the file name
     * @param strFileOutPut the file output
     * @param strEncoding the encoding
     * @throws IOException exception if there is an error during the deletion
     */
    void createFile( String strFolderPath, String strFileName, String strFileOutPut, String strEncoding )
            throws IOException;

    /**
     * Delete a file
     * @param strFolderPath the folder path
     * @param strFileName the file name
     * @throws IOException exception if there is an error during the deletion
     */
    void deleteFile( String strFolderPath, String strFileName ) throws IOException;

    /**
     * Build the file name
     * @param strFileName the file name to set
     * @param strFormatExtension the format extension
     * @return the file name
     */
    String buildFileName( String strFileName, String strFormatExtension );

    /**
     * Read content from the given file
     * @param strFile the file absolute path (ex : /home/filetopath/file.txt)
     * @return the string, an empty string if the file does not exists
     */
    String readFile( String strFile );

    /**
     * Write to the given file
     * @param strContent the content to write
     * @param strFile the file
     */
    void writeToFile( String strContent, String strFile );

}
