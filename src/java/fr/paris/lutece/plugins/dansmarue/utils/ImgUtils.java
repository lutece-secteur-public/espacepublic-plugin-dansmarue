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
package fr.paris.lutece.plugins.dansmarue.utils;

import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.image.ImageUtil;

/**
 * The Class ImgUtils.
 */
public class ImgUtils
{

    /** The Constant VALUE_0_85F. */
    private static final float VALUE_0_85F = 0.85f;

    /**
     * Instantiates a new list ImgUtils.
     */
    private ImgUtils( )
    {
        // does nothing
    }

    /**
     * Checks that the image quality does not exceed the maximum size in parameter (default 300kb).
     *
     * @param blobImage
     *            the blob image
     * @return the byte[]
     */
    public static byte [ ] checkQuality( byte [ ] blobImage )
    {
        String width = AppPropertiesService.getProperty( SignalementConstants.IMAGE_RESIZE_WIDTH );
        String height = AppPropertiesService.getProperty( SignalementConstants.IMAGE_RESIZE_HEIGHT );

        // Image resizing and compression
        return ImageUtil.resizeImage( blobImage, width, height, VALUE_0_85F );
    }

}
