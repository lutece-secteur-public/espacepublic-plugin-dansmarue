/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.plugins.dansmarue.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.FunctionnalException;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.service.impl.ImageObjetService;
import fr.paris.lutece.plugins.dansmarue.util.constants.SignalementConstants;
import fr.paris.lutece.plugins.dansmarue.utils.ListUtils;
import fr.paris.lutece.plugins.dansmarue.utils.SignalementUtils;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.portal.service.fileupload.FileUploadService;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

public class TypeSignalementJspBean extends AbstractJspBean
{
    private static final String     ID_TYPE_SIGNALEMENT_PARENT                   = "idTypeSignalementParent";

    private static final long       serialVersionUID                             = 600249327679754376L;

    // RIGHT
    public static final String      RIGHT_MANAGE_TYPE_SIGNALEMENT                = "REFERENTIEL_MANAGEMENT_SIGNALEMENT";

    // Markers
    private static final String     MARK_TYPE_SIGNALEMENT_LIST                   = "typesignalement_list";
    private static final String     MARK_TYPE_SIGNALEMENT_ID                     = "typesignalement_id";
    private static final String     MARK_TYPE_SIGNALEMENT                        = "typesignalement";
    private static final String     MARK_UNIT_LIST                               = "unit_list";
    private static final String     MARK_TYPE_SIGNALEMENT_PARENT_ID              = "typesignalementparent_id";
    private static final String     MARK_NO_SOUS_TYPE                            = "nosoustype";
    private static final String     MARK_TYPE_SIGNALEMENT_LIBELLE                = "typesignalement_libelle";
    private static final String     MARK_IMAGE_ADDRESS                           = "image_address";

    // CONSTANTS
    private static final String     EMPTY_STRING                                 = "";

    // Messages
    private static final String     MESSAGE_TITLE_DELETE_TYPE_SIGNALEMENT        = "dansmarue.message.deletetypesignalement.confirmation";
    private static final String     MESSAGE_CONFIRMATION_DELETE_TYPE_SIGNALEMENT = "dansmarue.messagetitle.deletetypesignalement.confirmation";
    private static final String     MESSAGE_ERROR_IS_USED_IN_SIGNALEMENT         = "dansmarue.message.deletetypesignalement.error.isusedinsignalement";
    private static final String     MESSAGE_ERROR_HAS_SOUS_TYPES                 = "dansmarue.message.deletetypesignalement.error.hassoustypes";
    private static final String     MESSAGE_ERROR_LIBELLE_NULL                   = "dansmarue.message.modifyTypeSignalement.error.libelleNull";

    // JSP
    private static final String     JSP_MANAGE_TYPE_SIGNALEMENT                  = "jsp/admin/plugins/signalement/ManageTypeSignalement.jsp";
    private static final String     JSP_MANAGE_TYPE_SIGNALEMENT_SHORT            = "ManageTypeSignalement.jsp";
    private static final String     JSP_MODIFY_TYPE_SIGNALEMENT                  = "ModifyTypeSignalement.jsp";
    private static final String     JSP_SAVE_TYPE_SIGNALEMENT                    = "SaveTypeSignalement.jsp";
    private static final String     JSP_DELETE_TYPE_SIGNALEMENT                  = "jsp/admin/plugins/signalement/DoDeleteTypeSignalement.jsp";

    // Parameters
    private static final String     PARAMETER_TYPE_SIGNALEMENT_ID                = "typesignalement_id";
    private static final String     PARAMETER_TYPE_SIGNALEMENT_PARENT_ID         = "typesignalementparent_id";
    private static final String     PARAMETER_ACTION                             = "action";
    private static final String     ACTION_MONTER                                = "monter";
    private static final String     ACTION_DESCENDRE                             = "descendre";
    private static final String     PARAMETER_IMAGE                              = "image";

    // TEMPLATES
    private static final String     TEMPLATE_MANAGE_TYPE_SIGNALEMENT             = "admin/plugins/signalement/manage_typesignalement.html";
    private static final String     TEMPLATE_MODIFY_TYPE_SIGNALEMENT             = "admin/plugins/signalement/modify_typesignalement.html";
    private static final String     TEMPLATE_SAVE_TYPE_SIGNALEMENT               = "admin/plugins/signalement/save_typesignalement.html";

    // SERVICES
    private ITypeSignalementService _typeSignalementService                      = ( ITypeSignalementService ) SpringContextService.getBean( "typeSignalementService" );
    private IUnitService            _unitService                                 = ( IUnitService ) SpringContextService.getBean( "unittree.unitService" );

    /**
     * Get the manage reporting type page
     * 
     * @param request
     *            the request
     * @return the page with reporting type list
     */
    public String getManageTypeSignalement( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>( );
        Collection<TypeSignalement> listeTypeSignalement;
        String strIdTypeSignalement = request.getParameter( PARAMETER_TYPE_SIGNALEMENT_ID );
        String action = request.getParameter( PARAMETER_ACTION );

        // Display of the main page of the types of reports
        if ( strIdTypeSignalement == null )
        {
            listeTypeSignalement = _typeSignalementService.getAllTypeSignalementWithoutParent( );
            // Replaces "SEJ" (idUnit=94) by "DEVE" (idUnit=1) the types of incidents related to DEVE to go into SEJ
            this.changeLabelUnitSEJintoDEVE( listeTypeSignalement );
            model.put( MARK_TYPE_SIGNALEMENT_LIST, listeTypeSignalement );
        } else
        {
            // Display of subtypes of a type (whose id is in the query parameter))

            Integer idTypeSignalement = Integer.parseInt( strIdTypeSignalement );

            // ACTION_MONTER
            if ( action != null )
            {
                if ( action.equals( ACTION_MONTER ) )
                {
                    TypeSignalement typeSignalement = _typeSignalementService.findByIdTypeSignalement( idTypeSignalement );
                    int nIdTypeSignalementOrdreInferieur = _typeSignalementService.getIdTypeSignalementOrdreInferieur( typeSignalement );
                    TypeSignalement typeSignalementOrdreInferieur = _typeSignalementService.findByIdTypeSignalement( nIdTypeSignalementOrdreInferieur );

                    if ( nIdTypeSignalementOrdreInferieur > 0 )
                    {
                        int temp = typeSignalement.getOrdre( );
                        typeSignalement.setOrdre( typeSignalementOrdreInferieur.getOrdre( ) );
                        typeSignalementOrdreInferieur.setOrdre( temp );
                        _typeSignalementService.updateOrdre( typeSignalement );
                        _typeSignalementService.updateOrdre( typeSignalementOrdreInferieur );

                    }

                    UrlItem url = new UrlItem( JSP_MANAGE_TYPE_SIGNALEMENT_SHORT );
                    TypeSignalement typeSignalementParent = _typeSignalementService.getParent( typeSignalement.getId( ) );

                    if ( typeSignalementParent != null )
                    {
                        url.addParameter( PARAMETER_TYPE_SIGNALEMENT_ID, typeSignalementParent.getId( ) );
                    }

                    return url.getUrl( );

                } else if ( action.equals( ACTION_DESCENDRE ) )
                // ACTION_DESCENDRE
                {
                    TypeSignalement typeSignalement = _typeSignalementService.findByIdTypeSignalement( idTypeSignalement );
                    int nIdTypeSignalementOrdreSuperieur = _typeSignalementService.getIdTypeSignalementOrdreSuperieur( typeSignalement );
                    TypeSignalement typeSignalementOrdreSuperieur = _typeSignalementService.findByIdTypeSignalement( nIdTypeSignalementOrdreSuperieur );

                    if ( nIdTypeSignalementOrdreSuperieur > 0 )
                    {
                        int temp = typeSignalement.getOrdre( );
                        typeSignalement.setOrdre( typeSignalementOrdreSuperieur.getOrdre( ) );
                        typeSignalementOrdreSuperieur.setOrdre( temp );
                        _typeSignalementService.updateOrdre( typeSignalement );
                        _typeSignalementService.updateOrdre( typeSignalementOrdreSuperieur );
                    }

                    UrlItem url = new UrlItem( JSP_MANAGE_TYPE_SIGNALEMENT_SHORT );
                    TypeSignalement typeSignalementParent = _typeSignalementService.getParent( typeSignalement.getId( ) );

                    if ( typeSignalementParent != null )
                    {
                        url.addParameter( PARAMETER_TYPE_SIGNALEMENT_ID, typeSignalementParent.getId( ) );
                    }

                    return url.getUrl( );

                }
            } else
            {
                model.put( MARK_TYPE_SIGNALEMENT_ID, strIdTypeSignalement );
                TypeSignalement typeSignalement = _typeSignalementService.findByIdTypeSignalement( idTypeSignalement );
                model.put( MARK_TYPE_SIGNALEMENT_LIBELLE, typeSignalement.getLibelle( ) );
                Integer nIdTypeSignalement = Integer.parseInt( strIdTypeSignalement );
                listeTypeSignalement = _typeSignalementService.getAllSousTypeSignalement( nIdTypeSignalement );
                // Replaces "SEJ" (idUnit=94) by "DEVE" (idUnit=1) the types of incidents related to DEVE to go into SEJ
                this.changeLabelUnitSEJintoDEVE( listeTypeSignalement );
                model.put( MARK_TYPE_SIGNALEMENT_LIST, listeTypeSignalement );

            }

        }

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MANAGE_TYPE_SIGNALEMENT, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Replace "SEJ" (idUnit=94) with "DEVE" (idUnit=1)
     * 
     * @param listeTypeSignalement
     *            the list of reporting types
     */
    private void changeLabelUnitSEJintoDEVE( Collection<TypeSignalement> listeTypeSignalement )
    {
        Integer idSEJ;
        Integer idDEVE;

        try
        {
            idSEJ = Integer.valueOf( AppPropertiesService.getProperty( SignalementConstants.UNIT_ATELIER_JARDINAGE ) );
            idDEVE = Integer.valueOf( SignalementConstants.UNIT_DEVE );
        } catch ( NumberFormatException e )
        {
            idSEJ = -1;
            idDEVE = -1;
        }
        Unit unitDEVE = this._unitService.getUnit( idDEVE, false );

        for ( TypeSignalement typeSignalement : listeTypeSignalement )
        {
            if ( typeSignalement.getUnit( ) != null && typeSignalement.getUnit( ).getIdUnit( ) == idSEJ )
            {
                typeSignalement.getUnit( ).setLabel( unitDEVE.getLabel( ) );
            }
        }
    }

    /**
     * Returns the form for reporting type creation
     * 
     * @param request
     *            The HTTP request
     * @return HTML Form
     */

    public String getSaveTypeSignalement( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        TypeSignalement typeSignalement = new TypeSignalement( );

        String strIdTypeSignalementParent = request.getParameter( PARAMETER_TYPE_SIGNALEMENT_PARENT_ID );

        Integer nIdTypeSignalementParent = 0;

        if ( strIdTypeSignalementParent != null )
        {
            nIdTypeSignalementParent = Integer.parseInt( strIdTypeSignalementParent );
            typeSignalement.setIdTypeSignalementParent( nIdTypeSignalementParent );
        }

        // Manage validation errors
        FunctionnalException ve = getErrorOnce( request );

        if ( ve != null )
        {
            typeSignalement = ( TypeSignalement ) ve.getBean( );
            model.put( "error", getHtmlError( ve ) );
            nIdTypeSignalementParent = typeSignalement.getIdTypeSignalementParent( );
        }

        model.put( MARK_TYPE_SIGNALEMENT, typeSignalement );

        model.put( MARK_TYPE_SIGNALEMENT_PARENT_ID, nIdTypeSignalementParent );

        ReferenceList listeUnits = ListUtils.toReferenceList( _unitService.getUnitsFirstLevel( false ), "idUnit", "label", "" );
        SignalementUtils.changeUnitDEVEIntoSEJ( listeUnits );

        model.put( MARK_UNIT_LIST, listeUnits );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_SAVE_TYPE_SIGNALEMENT, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Save a reporting type
     * 
     * @param request
     *            The HTTP request
     * @return redirection url
     */
    public String doSaveTypeSignalement( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( "cancel" ) ) )
        {
            UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MANAGE_TYPE_SIGNALEMENT );
            if ( !"0".equals( request.getParameter( ID_TYPE_SIGNALEMENT_PARENT ) ) )
            {
                urlItem.addParameter( PARAMETER_TYPE_SIGNALEMENT_ID, request.getParameter( ID_TYPE_SIGNALEMENT_PARENT ) );
            }

            return urlItem.getUrl( );
        }

        TypeSignalement typeSignalement = new TypeSignalement( );
        populate( typeSignalement, request );

        MultipartHttpServletRequest multiPartRequest = ( MultipartHttpServletRequest ) request;
        FileItem imageSource = multiPartRequest.getFile( PARAMETER_IMAGE );
        String strImageName = FileUploadService.getFileNameOnly( imageSource );

        // image treatement
        byte[] baImageSource = imageSource.get( );

        ImageResource image = new ImageResource( );

        if ( ( strImageName != null ) && !strImageName.equals( EMPTY_STRING ) )
        {
            image.setImage( baImageSource );
            image.setMimeType( imageSource.getContentType( ) );
            typeSignalement.setImage( image );
        } else
        {
            typeSignalement.setImage( null );
            typeSignalement.setImageUrl( EMPTY_STRING );
        }

        try
        {
            if ( typeSignalement.getUnit( ).getIdUnit( ) == -1 )
            {
                typeSignalement.getUnit( ).setIdUnit( 0 );
            }

            if ( typeSignalement.getLibelle( ).equals( EMPTY_STRING ) )
            {
                throw new BusinessException( typeSignalement, MESSAGE_ERROR_LIBELLE_NULL );
            }

            if ( typeSignalement.getIdTypeSignalementParent( ) != 0 )
            {
                TypeSignalement typeSignalementParent = new TypeSignalement( );
                typeSignalementParent.setId( typeSignalement.getIdTypeSignalementParent( ) );
                typeSignalement.setTypeSignalementParent( typeSignalementParent );

                if ( !( typeSignalement.getOrdre( ) > 0 ) )
                {
                    Integer maxOrderToSet = _typeSignalementService.getMaximumOrderInHierarchyTypeSignalement( typeSignalementParent );
                    if ( maxOrderToSet != null )
                    {
                        typeSignalement.setOrdre( maxOrderToSet + 1 );
                    } else
                    {
                        typeSignalement.setOrdre( 1 );
                    }

                }

                _typeSignalementService.insert( typeSignalement );
            } else
            {

                if ( !( typeSignalement.getOrdre( ) > 0 ) )
                {
                    Integer maxOrderToSet = _typeSignalementService.getMaximumOrderInHierarchyTypeSignalement( null );
                    if ( maxOrderToSet != null )
                    {
                        typeSignalement.setOrdre( maxOrderToSet + 1 );
                    } else
                    {
                        typeSignalement.setOrdre( 1 );
                    }
                }

                _typeSignalementService.insertWithoutParent( typeSignalement );
            }

            updateActifForParentsAndChildren( typeSignalement );

        } catch ( FunctionnalException e )
        {
            return manageFunctionnalException( request, e, JSP_SAVE_TYPE_SIGNALEMENT );
        }

        UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MANAGE_TYPE_SIGNALEMENT );
        if ( !"0".equals( request.getParameter( ID_TYPE_SIGNALEMENT_PARENT ) ) )
        {
            urlItem.addParameter( PARAMETER_TYPE_SIGNALEMENT_ID, request.getParameter( ID_TYPE_SIGNALEMENT_PARENT ) );
        }

        return urlItem.getUrl( );
    }

    /**
     * Returns the form for reporting type modification
     * 
     * @param request
     *            The HTTP request
     * @return HTML Form
     */
    public String getModifyTypeSignalement( HttpServletRequest request )
    {

        Map<String, Object> model = new HashMap<String, Object>( );

        TypeSignalement typeSignalement = new TypeSignalement( );

        String strIdTypeSignalement = request.getParameter( PARAMETER_TYPE_SIGNALEMENT_ID );

        int nIdTypeSignalement = 0;

        try
        {
            nIdTypeSignalement = Integer.parseInt( strIdTypeSignalement );
        } catch ( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        String strResourceType = ImageObjetService.getInstance( ).getResourceTypeId( );
        UrlItem url = new UrlItem( Parameters.IMAGE_SERVLET );
        url.addParameter( Parameters.RESOURCE_TYPE, strResourceType );
        url.addParameter( Parameters.RESOURCE_ID, Integer.toString( nIdTypeSignalement ) );

        String strImageAddress = "<img src='" + url.getUrlWithEntity( ) + "'" + "alt='image' />";

        typeSignalement = _typeSignalementService.getByIdTypeSignalement( nIdTypeSignalement );
        model.put( MARK_TYPE_SIGNALEMENT, typeSignalement );
        model.put( MARK_IMAGE_ADDRESS, strImageAddress );

        // Manage validation errors
        FunctionnalException ve = getErrorOnce( request );
        if ( ve != null )
        {
            typeSignalement = ( TypeSignalement ) ve.getBean( );
            model.put( "error", getHtmlError( ve ) );
        }

        ReferenceList listeUnits = ListUtils.toReferenceList( _unitService.getUnitsFirstLevel( false ), "idUnit", "label", "" );
        SignalementUtils.changeUnitDEVEIntoSEJ( listeUnits );

        model.put( MARK_UNIT_LIST, listeUnits );

        boolean noSousType = _typeSignalementService.getAllSousTypeSignalement( typeSignalement.getId( ) ).isEmpty( );

        model.put( MARK_NO_SOUS_TYPE, noSousType );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MODIFY_TYPE_SIGNALEMENT, getLocale( ), model );

        return getAdminPage( t.getHtml( ) );
    }

    /**
     * Update the reporting type
     * 
     * @param request
     *            The HTTP request
     * @return HTML Form
     */
    public String doModifyTypeSignalement( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( "cancel" ) ) )
        {
            UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MANAGE_TYPE_SIGNALEMENT );
            if ( !"0".equals( request.getParameter( ID_TYPE_SIGNALEMENT_PARENT ) ) )
            {
                urlItem.addParameter( PARAMETER_TYPE_SIGNALEMENT_ID, request.getParameter( ID_TYPE_SIGNALEMENT_PARENT ) );
            }

            return urlItem.getUrl( );
        }

        TypeSignalement typeSignalement = new TypeSignalement( );
        populate( typeSignalement, request );

        MultipartHttpServletRequest multiPartRequest = ( MultipartHttpServletRequest ) request;
        FileItem imageSource = multiPartRequest.getFile( PARAMETER_IMAGE );
        String strImageName = FileUploadService.getFileNameOnly( imageSource );

        // image treatement
        byte[] baImageSource = imageSource.get( );

        ImageResource image = new ImageResource( );

        if ( ( strImageName != null ) && !strImageName.equals( EMPTY_STRING ) )
        {
            image.setImage( baImageSource );
            image.setMimeType( imageSource.getContentType( ) );
            typeSignalement.setImage( image );
        } else
        {
            typeSignalement.setImage( null );
            typeSignalement.setImageUrl( EMPTY_STRING );
        }
        try
        {
            if ( typeSignalement.getUnit( ).getIdUnit( ) == -1 )
            {
                typeSignalement.getUnit( ).setIdUnit( 0 );
            }

            if ( typeSignalement.getLibelle( ).equals( EMPTY_STRING ) )
            {
                throw new BusinessException( typeSignalement, MESSAGE_ERROR_LIBELLE_NULL );
            }

            if ( typeSignalement.getIdTypeSignalementParent( ) != 0 )
            {
                TypeSignalement typeSignalementParent = new TypeSignalement( );
                typeSignalementParent.setId( typeSignalement.getIdTypeSignalementParent( ) );
                typeSignalement.setTypeSignalementParent( typeSignalementParent );

                _typeSignalementService.update( typeSignalement );
            } else
            {
                _typeSignalementService.updateWithoutParent( typeSignalement );
            }

            updateActifForParentsAndChildren( typeSignalement );

        } catch ( FunctionnalException e )
        {
            UrlItem urlItem = new UrlItem( JSP_MODIFY_TYPE_SIGNALEMENT );
            urlItem.addParameter( PARAMETER_TYPE_SIGNALEMENT_ID, typeSignalement.getId( ) );
            return manageFunctionnalException( request, e, urlItem.getUrl( ) );
        }

        UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MANAGE_TYPE_SIGNALEMENT );
        if ( typeSignalement.getIdTypeSignalementParent( ) != 0 )
        {
            urlItem.addParameter( PARAMETER_TYPE_SIGNALEMENT_ID, typeSignalement.getIdTypeSignalementParent( ) );
        }
        return urlItem.getUrl( );
    }

    /**
     * If the report type is active, put all parents active If the report type is put inactive, put all children inactive, and parents who do not have active subtypes
     * 
     * @param report
     *            type
     */
    private void updateActifForParentsAndChildren( TypeSignalement typeSignalement )
    {
        boolean possedeParent = true;
        boolean typeActif = typeSignalement.getActif( );
        TypeSignalement typeSignalementTemp = typeSignalement;

        // children treatment
        if ( !typeActif )
        {
            // Deactivation of children
            _typeSignalementService.disableChildren( typeSignalement );
        }

        // parents treatment
        while ( possedeParent && _typeSignalementService.getParent( typeSignalementTemp.getId( ) ) != null )
        {
            // If the parent is not the root <===> the parent of the parent != null
            if ( _typeSignalementService.getParent( typeSignalementTemp.getId( ) ).getId( ) != null )
            {
                // typeSignalementTemp becomes its parent
                typeSignalementTemp = _typeSignalementService.getParent( typeSignalementTemp.getId( ) );
                // If you have chosen to put the report on active
                if ( typeActif )
                {
                    // If the parent is inactive: it is activated
                    if ( !typeSignalementTemp.getActif( ) )
                    {
                        typeSignalementTemp.setActif( true );
                        if ( _typeSignalementService.getParent( typeSignalementTemp.getId( ) ) != null )
                        {
                            _typeSignalementService.update( typeSignalementTemp );
                        } else
                        {
                            _typeSignalementService.updateWithoutParent( typeSignalementTemp );
                        }
                    }
                } else
                {
                    // If you have chosen to inactivate the report
                    if ( typeSignalementTemp.getActif( ) )
                    {

                        // if the parent is still active, check if all its subtypes are inactive: if so, put it inactive
                        boolean allInactifs = true;
                        List<TypeSignalement> listeSousTypes = _typeSignalementService.getAllSousTypeSignalement( typeSignalementTemp.getId( ) );
                        for ( TypeSignalement type : listeSousTypes )
                        {
                            if ( type.getActif( ) )
                            {
                                allInactifs = false;
                            }
                        }
                        // We put it inactive unless it's the root.
                        if ( allInactifs )
                        {
                            typeSignalementTemp.setActif( false );
                            if ( _typeSignalementService.getParent( typeSignalementTemp.getId( ) ) != null )
                            {
                                _typeSignalementService.update( typeSignalementTemp );
                            } else
                            {
                                _typeSignalementService.updateWithoutParent( typeSignalementTemp );
                            }
                        }
                    }
                }
            } else
            {
                possedeParent = false;
            }
        }
    }

    /**
     * Returns the confirmation message to delete a report type
     * 
     * @param request
     *            The Http request
     * @return the html code message
     */
    public String getDeleteTypeSignalement( HttpServletRequest request )
    {

        String strJspBack = JSP_MANAGE_TYPE_SIGNALEMENT;

        String strTypeSignalement = request.getParameter( PARAMETER_TYPE_SIGNALEMENT_ID );

        Integer nIdTypeSignalement = 0;

        try
        {
            nIdTypeSignalement = Integer.parseInt( strTypeSignalement );
        } catch ( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        Map<String, Object> urlParam = new HashMap<String, Object>( );
        urlParam.put( PARAMETER_TYPE_SIGNALEMENT_ID, nIdTypeSignalement );
        urlParam.put( PARAMETER_TYPE_SIGNALEMENT_PARENT_ID, request.getParameter( PARAMETER_TYPE_SIGNALEMENT_PARENT_ID ) );

        // Delete the report typeif it does not have subtypes and is not linked to an alert
        if ( _typeSignalementService.getAllSousTypeSignalement( nIdTypeSignalement ).isEmpty( ) )
        {
            if ( !( _typeSignalementService.findByIdSignalement( nIdTypeSignalement ) ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_DELETE_TYPE_SIGNALEMENT, null, MESSAGE_CONFIRMATION_DELETE_TYPE_SIGNALEMENT, JSP_DELETE_TYPE_SIGNALEMENT, "_self",
                        AdminMessage.TYPE_CONFIRMATION, urlParam, strJspBack );
            } else
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_IS_USED_IN_SIGNALEMENT, AdminMessage.TYPE_STOP );
            }
        } else
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_HAS_SOUS_TYPES, AdminMessage.TYPE_STOP );
        }

    }

    /**
     * Delete a report type
     * 
     * @param request
     *            The HTTP request
     * @return redirection url
     */
    public String doDeleteTypeSignalement( HttpServletRequest request )
    {

        String strIdTypeSignalement = request.getParameter( PARAMETER_TYPE_SIGNALEMENT_ID );

        Integer nIdTypeSignalement = 0;

        try
        {
            nIdTypeSignalement = Integer.parseInt( strIdTypeSignalement );
        } catch ( NumberFormatException e )
        {

            return AdminMessageService.getMessageUrl( request, SignalementConstants.MESSAGE_ERROR_OCCUR, AdminMessage.TYPE_STOP );
        }

        // setting active to false and update in order to reuse the method updateActifForParentsAndChildren
        TypeSignalement typeSignalement = _typeSignalementService.getByIdTypeSignalement( nIdTypeSignalement );
        typeSignalement.setActif( false );
        TypeSignalement typeSignalementParent = _typeSignalementService.getParent( typeSignalement.getId( ) );
        if ( typeSignalementParent != null )
        {
            _typeSignalementService.update( typeSignalement );
        } else
        {
            _typeSignalementService.updateWithoutParent( typeSignalement );
        }
        updateActifForParentsAndChildren( typeSignalement );

        // get the minimum order after a given order to update after deleting a report type
        Integer minimumOrderAfterThisOrderInHierarchy = _typeSignalementService.getMinimumOrderAfterGivenOrder( typeSignalement.getOrdre( ), typeSignalementParent );

        _typeSignalementService.delete( nIdTypeSignalement );

        // after deleting the report type we update the others one's orders to order - 1 (except if it was the last one)
        if ( minimumOrderAfterThisOrderInHierarchy != null )
        {
            _typeSignalementService.updateOrderSuperiorAfterDeletingTypeSignalement( typeSignalement.getOrdre( ), typeSignalementParent );
        }

        UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MANAGE_TYPE_SIGNALEMENT );
        if ( StringUtils.isNotBlank( request.getParameter( PARAMETER_TYPE_SIGNALEMENT_PARENT_ID ) ) )
        {
            urlItem.addParameter( PARAMETER_TYPE_SIGNALEMENT_ID, request.getParameter( PARAMETER_TYPE_SIGNALEMENT_PARENT_ID ) );
        }

        return urlItem.getUrl( );

    }

}
