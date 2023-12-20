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
package fr.paris.lutece.plugins.dansmarue.service.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.paris.lutece.plugins.dansmarue.service.impl.ImageService;
import fr.paris.lutece.plugins.dansmarue.service.impl.ImageThumbnailService;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.image.ImageResourceManager;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.LocalVariables;

public class ImageSignalementServlet extends HttpServlet
{
    private static final long serialVersionUID = -5713203328367191909L;
    private static final String ERROR_MSG = "ImageServlet error : {}";
    public static final String PARAMETER_RESOURCE_TYPE = "resource_type";
    public static final String PARAMETER_ID = "id";
    private static final String PROPERTY_PATH_IMAGES = "path.images.root";
    private static final String PROPERTY_IMAGE_PAGE_DEFAULT = "image.page.default";

    private ImageService _imageService = ( ImageService ) SpringContextService.getBean( "signalement.imageService" );
    private ImageThumbnailService _imageThumbnailService = ( ImageThumbnailService ) SpringContextService.getBean( "signalement.imageThumbnailService" );

    public ImageSignalementServlet() {
    }

    protected void processRequest( HttpServletRequest request, HttpServletResponse response) {
        String strResourceId = request.getParameter("id");
        String strResourceTypeId = request.getParameter("resource_type");
        LocalVariables.setLocal(getServletConfig(), request, response);

        try {
            if ( strResourceId != null )
            {
                ImageResource image;
                String[] lIdToken = strResourceId.split( "_", 2 );
                if ( ( lIdToken.length == 2 ) && strResourceTypeId.equals( _imageService.getResourceTypeId( ) ) )
                {
                    try
                    {
                        int nResourceId = Integer.parseInt( lIdToken[0] );
                        image = _imageService.getImageResource( nResourceId, lIdToken[1] );
                    } catch ( NumberFormatException e )
                    {
                        image = new ImageResource( );
                    }

                } else if ( ( lIdToken.length == 2 ) && strResourceTypeId.equals( _imageThumbnailService.getResourceTypeId( ) ) )
                {
                    try
                    {
                        int nResourceId = Integer.parseInt( lIdToken[0] );
                        image = _imageThumbnailService.getImageResource( nResourceId, lIdToken[1] );
                    } catch ( NumberFormatException e )
                    {
                        image = new ImageResource( );
                    }
                } else
                {
                    try
                    {
                        int nResourceId = Integer.parseInt( strResourceId );
                        image = ImageResourceManager.getImageResource( strResourceTypeId, nResourceId );
                    } catch ( NumberFormatException e )
                    {
                        image = new ImageResource( );
                    }
                }
                if (getImageExist(image)) {
                    response.setContentType(image.getMimeType());

                    try {
                        OutputStream out = response.getOutputStream();
                        Throwable var8 = null;

                        try {
                            out.write(image.getImage());
                        } catch (Throwable var81) {
                            var8 = var81;
                            throw var81;
                        } finally {
                            if (out != null) {
                                if (var8 != null) {
                                    try {
                                        out.close();
                                    } catch (Throwable var80) {
                                        var8.addSuppressed(var80);
                                    }
                                } else {
                                    out.close();
                                }
                            }

                        }
                    } catch ( IOException var83) {
                        AppLogService.error("ImageServlet error : {}", new Object[]{var83.getMessage(), var83});
                    }
                } else {
                    ServletContext sc = getServletContext();
                    String strImageUrl = AppPathService.getAbsolutePathFromRelativePath( AppPropertiesService.getProperty("path.images.root") + "/" + AppPropertiesService.getProperty("image.page.default"));
                    response.setContentType(sc.getMimeType(strImageUrl));
                    File file = new File(strImageUrl);
                    response.setContentLength((int)file.length());

                    try {
                        FileInputStream in = new FileInputStream(file);
                        Throwable var11 = null;

                        try {
                            OutputStream out = response.getOutputStream();
                            Throwable var13 = null;

                            try {
                                byte[] buf = new byte[1024];

                                int count;
                                while((count = in.read(buf)) >= 0) {
                                    out.write(buf, 0, count);
                                }
                            } catch (Throwable var84) {
                                var13 = var84;
                                throw var84;
                            } finally {
                                if (out != null) {
                                    if (var13 != null) {
                                        try {
                                            out.close();
                                        } catch (Throwable var79) {
                                            var13.addSuppressed(var79);
                                        }
                                    } else {
                                        out.close();
                                    }
                                }

                            }
                        } catch (Throwable var86) {
                            var11 = var86;
                            throw var86;
                        } finally {
                            if (in != null) {
                                if (var11 != null) {
                                    try {
                                        in.close();
                                    } catch (Throwable var78) {
                                        var11.addSuppressed(var78);
                                    }
                                } else {
                                    in.close();
                                }
                            }

                        }
                    } catch (IOException var88) {
                        AppLogService.error("ImageServlet error : {}", new Object[]{var88.getMessage(), var88});
                    }
                }
            }
        } finally {
            LocalVariables.setLocal(( ServletConfig )null, (HttpServletRequest)null, (HttpServletResponse)null);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet serving images content";
    }

    private boolean getImageExist(ImageResource image) {
        if (( image != null ) && ( image.getImage() != null )) {
            int nImageLength = image.getImage().length;
            if (nImageLength >= 1) {
                return true;
            }
        }

        return false;
    }
}
