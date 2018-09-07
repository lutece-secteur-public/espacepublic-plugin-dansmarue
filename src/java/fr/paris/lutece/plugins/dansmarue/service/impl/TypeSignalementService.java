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
package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.ITypeSignalementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService;
import fr.paris.lutece.plugins.dansmarue.service.SignalementPlugin;
import fr.paris.lutece.plugins.dansmarue.service.dto.TypeSignalementDTO;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * The Class TypeSignalementService.
 */
public class TypeSignalementService implements ITypeSignalementService
{

    // MESSAGES
    private static final String MESSAGE_ERROR_TYPE_SIGNALEMENT_MUST_BE_UNIQUE     = "dansmarue.message.errortypesignalement.alreadyexists";
    private static final String MESSAGE_ERROR_TYPE_NON_ACTIF_RATTACHE_A_DIRECTION = "dansmarue.message.errortypesignalement.actifwithnounit";

    @Inject
    private ITypeSignalementDAO _typesignalementDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllTypeSignalement( )
    {
        return _typesignalementDAO.getAllTypeSignalement( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAll( )
    {
        return _typesignalementDAO.getAll( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllSousTypeSignalement( Integer nIdTypeSignalement )
    {
        return _typesignalementDAO.getAllSousTypeSignalement( nIdTypeSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllSousTypeSignalementActifs( Integer nIdTypeSignalement )
    {
        return _typesignalementDAO.getAllSousTypeSignalementActifs( nIdTypeSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSignalement getByIdTypeSignalement( Integer lIdTypeSignalement )
    {
        Plugin plugin = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
        return _typesignalementDAO.load( lIdTypeSignalement, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllTypeSignalementWithoutChildren( )
    {
        return _typesignalementDAO.getAllTypeSignalementWithoutChildren( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllTypeSignalementWithoutParent( )
    {
        return _typesignalementDAO.getAllTypeSignalementWithoutParent( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer insert( TypeSignalement typeSignalement )
    {
        if ( typeSignalement.getActif( ) && typeSignalement.getUnit( ).getIdUnit( ) == 0 )
        {
            throw new BusinessException( typeSignalement, MESSAGE_ERROR_TYPE_NON_ACTIF_RATTACHE_A_DIRECTION );
        }
        Plugin plugin = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );

        if ( !existsSaveTypeSignalement( typeSignalement ) )
        {

            // we test if the chosen order is superior to the last one, if it's that, we change this order to the max one + 1
            Integer maxOrderInTypeSignalement = _typesignalementDAO.getMaximumOrderInHierarchyTypeSignalement( typeSignalement.getTypeSignalementParent( ) );
            if ( typeSignalement.getOrdre( ) > maxOrderInTypeSignalement )
            {
                typeSignalement.setOrdre( maxOrderInTypeSignalement + 1 );
            }

            // if the order that the user wants is already used, we add +1 to all the others after
            if ( existsOrdre( typeSignalement.getOrdre( ), typeSignalement.getTypeSignalementParent( ) ) )
            {

                _typesignalementDAO.updateOrderGreaterThan( typeSignalement.getOrdre( ), typeSignalement.getTypeSignalementParent( ) );

            }
            // then we insert the new report type
            Integer idTypeSignalement = _typesignalementDAO.insert( typeSignalement, plugin );
            _typesignalementDAO.insertAlias( typeSignalement, plugin );
            // Refresh the view with the new type
            _typesignalementDAO.refreshViewTypesWithParentsLinks( );
            return idTypeSignalement;
        } else
        {
            throw new BusinessException( typeSignalement, MESSAGE_ERROR_TYPE_SIGNALEMENT_MUST_BE_UNIQUE );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer insertWithoutParent( TypeSignalement typeSignalement )
    {
        if ( typeSignalement.getActif( ) && typeSignalement.getUnit( ).getIdUnit( ) == 0 )
        {
            throw new BusinessException( typeSignalement, MESSAGE_ERROR_TYPE_NON_ACTIF_RATTACHE_A_DIRECTION );
        }

        Plugin plugin = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );

        if ( !existsSaveTypeSignalement( typeSignalement ) )
        {
            // we test if the chosen order is superior to the last one, if it's that, we change this order to the max one + 1
            Integer maxOrderInTypeSignalement = _typesignalementDAO.getMaximumOrderInHierarchyTypeSignalement( null );
            if ( typeSignalement.getOrdre( ) > maxOrderInTypeSignalement )
            {
                typeSignalement.setOrdre( maxOrderInTypeSignalement + 1 );
            }

            // if the order that the user wants is already used, we add +1 to all the others after
            if ( existsOrdre( typeSignalement.getOrdre( ), null ) )
            {

                _typesignalementDAO.updateOrderGreaterThan( typeSignalement.getOrdre( ), null );
            }

            // then we insert the new report type
            int returnCode = _typesignalementDAO.insertWithoutParent( typeSignalement, plugin );
            // Refresh the view with the new type
            _typesignalementDAO.refreshViewTypesWithParentsLinks( );
            return returnCode;
        } else
        {
            throw new BusinessException( typeSignalement, MESSAGE_ERROR_TYPE_SIGNALEMENT_MUST_BE_UNIQUE );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSignalement getParent( Integer nIdTypeSignalement )
    {
        Plugin plugin = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
        return _typesignalementDAO.getParent( nIdTypeSignalement, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update( TypeSignalement typeSignalement )
    {
        boolean noSousType = this.getAllSousTypeSignalement( typeSignalement.getId( ) ).isEmpty( );

        if ( typeSignalement.getActif( ) && typeSignalement.getUnit( ).getIdUnit( ) == 0 && noSousType )
        {
            throw new BusinessException( typeSignalement, MESSAGE_ERROR_TYPE_NON_ACTIF_RATTACHE_A_DIRECTION );
        }

        Plugin plugin = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
        if ( !existsSaveTypeSignalement( typeSignalement ) )
        {
            _typesignalementDAO.store( typeSignalement, plugin );
            _typesignalementDAO.deleteAlias( typeSignalement.getId( ), plugin );
            if ( noSousType )
            {
                _typesignalementDAO.insertAlias( typeSignalement, plugin );
            }
            _typesignalementDAO.refreshViewTypesWithParentsLinks( );
        } else
        {
            throw new BusinessException( typeSignalement, MESSAGE_ERROR_TYPE_SIGNALEMENT_MUST_BE_UNIQUE );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateWithoutParent( TypeSignalement typeSignalement )
    {
        boolean noSousType = this.getAllSousTypeSignalement( typeSignalement.getId( ) ).isEmpty( );

        if ( typeSignalement.getActif( ) && typeSignalement.getUnit( ).getIdUnit( ) == 0 && noSousType )
        {
            throw new BusinessException( typeSignalement, MESSAGE_ERROR_TYPE_NON_ACTIF_RATTACHE_A_DIRECTION );
        }

        Plugin plugin = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );

        if ( !existsSaveTypeSignalement( typeSignalement ) )
        {
            _typesignalementDAO.updateWithoutParent( typeSignalement, plugin );
            _typesignalementDAO.deleteAlias( typeSignalement.getId( ), plugin );
            if ( noSousType )
            {
                _typesignalementDAO.insertAlias( typeSignalement, plugin );
            }
            _typesignalementDAO.refreshViewTypesWithParentsLinks( );
        } else
        {
            throw new BusinessException( typeSignalement, MESSAGE_ERROR_TYPE_SIGNALEMENT_MUST_BE_UNIQUE );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsSaveTypeSignalement( TypeSignalement typeSignalement )
    {
        return _typesignalementDAO.existsSaveTypeSignalement( typeSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSignalement findByIdTypeSignalement( Integer nIdTypeSignalement )
    {
        return _typesignalementDAO.findByIdTypeSignalement( nIdTypeSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( Integer nIdTypeSignalement )
    {
        Plugin plugin = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
        _typesignalementDAO.deleteAlias( nIdTypeSignalement, plugin );
        _typesignalementDAO.remove( nIdTypeSignalement, plugin );

        // Refresh the view without the type
        _typesignalementDAO.refreshViewTypesWithParentsLinks( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean findByIdSignalement( Integer nIdSignalement )
    {
        return _typesignalementDAO.findByIdSignalement( nIdSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSignalement getTypeSignalement( Integer nId )
    {
        return _typesignalementDAO.getTypeSignalement( nId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableChildren( TypeSignalement typeSignalement )
    {
        List<TypeSignalement> listeChildren = this.getAllSousTypeSignalement( typeSignalement.getId( ) );
        for ( TypeSignalement child : listeChildren )
        {
            child.setActif( false );
            this.update( child );
            this.disableChildren( child );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllTypeSignalementActifLinkedToUnit( )
    {
        List<TypeSignalement> listAllTypes = this.getAllTypeSignalement( );
        List<TypeSignalement> listResult = new ArrayList<TypeSignalement>( );
        for ( TypeSignalement type : listAllTypes )
        {
            if ( type.getActif( ) && type.getUnit( ).getIdUnit( ) != 0 )
            {
                listResult.add( type );
            }
        }

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllTypeSignalementActif( )
    {
        List<TypeSignalement> typeSignalementList = _typesignalementDAO.getAllTypeSignalementEvenWithoutUnit( );
        List<TypeSignalement> listResult = new ArrayList<TypeSignalement>( );
        for ( TypeSignalement type : typeSignalementList )
        {
            if ( type.getActif( ) )
            {
                listResult.add( type );
            }
        }

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllTypeSignalementLinkedToUnit( )
    {
        List<TypeSignalement> listAllTypes = this.getAllTypeSignalement( );
        List<TypeSignalement> listResult = new ArrayList<TypeSignalement>( );
        for ( TypeSignalement type : listAllTypes )
        {
            if ( type.getUnit( ).getIdUnit( ) != 0 )
            {
                listResult.add( type );
            }
        }

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getAllSousTypeSignalementCascade( Integer typeSignalementId, List<TypeSignalement> listeTypeSignalement )
    {
        List<TypeSignalement> allSousTypeSignalement = this.getAllSousTypeSignalement( typeSignalementId );
        if ( allSousTypeSignalement != null )
        {
            for ( TypeSignalement typeSignalement : allSousTypeSignalement )
            {
                listeTypeSignalement.add( typeSignalement );
                this.getAllSousTypeSignalementCascade( typeSignalement.getId( ), listeTypeSignalement );
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalementDTO> getTypeSignalementTree( boolean actif )
    {
        List<TypeSignalementDTO> ret = new ArrayList<TypeSignalementDTO>( );
        List<TypeSignalement> roots = this.getAllTypeSignalementWithoutParent( );
        for ( TypeSignalement typeSignalement : roots )
        {

            TypeSignalementDTO dest = new TypeSignalementDTO( );
            try
            {
                BeanUtils.copyProperties( dest, typeSignalement );
            } catch ( IllegalAccessException e )
            {
                AppLogService.error( e );
            } catch ( InvocationTargetException e )
            {
                AppLogService.error( e );
            }
            if ( !actif || dest.isActif( ) )
            {
                ret.add( dest );
            }
            this.fillDestWithChildsCascade( dest, actif );
        }
        return ret;
    }

    /**
     * Fill dest with childs cascade.
     * 
     * @param parentType
     *            id true return only actives types, else return all types.
     * @param actif
     *            the actif
     */
    private void fillDestWithChildsCascade( TypeSignalementDTO parentType, boolean actif )
    {
        List<TypeSignalement> allSousTypeSignalement = this.getAllSousTypeSignalement( parentType.getId( ) );
        for ( TypeSignalement typeSignalement : allSousTypeSignalement )
        {

            TypeSignalementDTO dest = new TypeSignalementDTO( );
            try
            {
                BeanUtils.copyProperties( dest, typeSignalement );
            } catch ( IllegalAccessException e )
            {
                AppLogService.error( e );
            } catch ( InvocationTargetException e )
            {
                AppLogService.error( e );
            }
            if ( !actif || dest.isActif( ) )
            {
                parentType.getListChild( ).add( dest );
                this.fillDestWithChildsCascade( dest, actif );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSignalement getTypeSignalementByIdWithParents( Integer nIdTypeSignalement )
    {
        return this._typesignalementDAO.getTypeSignalementByIdWithParents( nIdTypeSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIdTypeSignalementOrdreInferieur( TypeSignalement typeSignalement )
    {
        return _typesignalementDAO.getIdTypeSignalementOrdreInferieur( typeSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateOrdre( TypeSignalement typeSignalement )
    {
        _typesignalementDAO.updateOrdre( typeSignalement );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIdTypeSignalementOrdreSuperieur( TypeSignalement typeSignalement )
    {
        return _typesignalementDAO.getIdTypeSignalementOrdreSuperieur( typeSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsOrdre( int ordre, TypeSignalement typeSignalementParent )
    {
        return _typesignalementDAO.existsOrdre( ordre, typeSignalementParent );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateOrderGreaterThan( Integer ordre, TypeSignalement typeSignalementParent )
    {
        _typesignalementDAO.updateOrderGreaterThan( ordre, typeSignalementParent );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getMinimumOrderAfterGivenOrder( Integer order, TypeSignalement typeSignalementParent )
    {
        return _typesignalementDAO.getMinimumOrderAfterGivenOrder( order, typeSignalementParent );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateOrderSuperiorAfterDeletingTypeSignalement( Integer order, TypeSignalement typeSignalementParent )
    {
        _typesignalementDAO.updateOrderSuperiorAfterDeletingTypeSignalement( order, typeSignalementParent );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getMaximumOrderInHierarchyTypeSignalement( TypeSignalement typeSignalementParent )
    {
        return _typesignalementDAO.getMaximumOrderInHierarchyTypeSignalement( typeSignalementParent );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageResource getImageResource( int nKey )
    {
        return _typesignalementDAO.loadImage( nKey );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double findLastVersionTypeSignalement( )
    {
        return _typesignalementDAO.findLastVersionTypeSignalement( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllTypeSignalementActifWithoutParent( )
    {
        return _typesignalementDAO.getAllTypeSignalementActifWithoutParent( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCategoryFromTypeId( Integer idTypeSignalement )
    {
        return _typesignalementDAO.getCategoryFromTypeId( idTypeSignalement );
    }
}
