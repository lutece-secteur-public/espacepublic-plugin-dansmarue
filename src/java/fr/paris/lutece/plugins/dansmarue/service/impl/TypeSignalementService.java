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
package fr.paris.lutece.plugins.dansmarue.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.ITypeSignalementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Source;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalementWithDepth;
import fr.paris.lutece.plugins.dansmarue.commons.exceptions.BusinessException;
import fr.paris.lutece.plugins.dansmarue.service.IMessageTypologieService;
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

    /** The Constant MESSAGE_ERROR_TYPE_SIGNALEMENT_MUST_BE_UNIQUE. */
    // MESSAGES
    private static final String MESSAGE_ERROR_TYPE_SIGNALEMENT_MUST_BE_UNIQUE = "dansmarue.message.errortypesignalement.alreadyexists";

    /** The Constant MESSAGE_ERROR_TYPE_NON_ACTIF_RATTACHE_A_DIRECTION. */
    private static final String MESSAGE_ERROR_TYPE_NON_ACTIF_RATTACHE_A_DIRECTION = "dansmarue.message.errortypesignalement.actifwithnounit";

    /** The Constant MESSAGE_ERROR_TYPE_HORS_DMR_NON_FINAL. */
    private static final String MESSAGE_ERROR_TYPE_HORS_DMR_NON_FINAL = "dansmarue.message.errortypesignalement.horsDMR.notfinal";

    /** The Constant MESSAGE_ERROR_TYPE_HORS_DMR_MISSING_FIELD. */
    private static final String MESSAGE_ERROR_TYPE_HORS_DMR_MISSING_FIELD = "dansmarue.message.errortypesignalement.horsDMR.missingfield";

    /** The typesignalement DAO. */
    @Inject
    private ITypeSignalementDAO _typesignalementDAO;

    /** The message typologie service. */
    @Inject
    private IMessageTypologieService _messageTypologieService;

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
    public List<TypeSignalement> getAllTypeSignalementByIsAgent( boolean isAgent )
    {
        return _typesignalementDAO.getAllTypeSignalementByIsAgent( isAgent );
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
        if ( typeSignalement.getActif( ) && ( typeSignalement.getUnit( ).getIdUnit( ) == 0 ) )
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
        }
        else
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
        if ( typeSignalement.getActif( ) && ( typeSignalement.getUnit( ).getIdUnit( ) == 0 ) )
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
        }
        else
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
        boolean noSousType = getAllSousTypeSignalement( typeSignalement.getId( ) ).isEmpty( );

        if ( typeSignalement.isHorsDMR( ) && !noSousType )
        {
            throw new BusinessException( typeSignalement, MESSAGE_ERROR_TYPE_HORS_DMR_NON_FINAL );
        }

        if ( ( typeSignalement.isHorsDMR( ) && StringUtils.isBlank( typeSignalement.getMessageHorsDMR( ) ) )
                || ( !typeSignalement.isHorsDMR( ) && StringUtils.isNotBlank( typeSignalement.getMessageHorsDMR( ) ) ) )
        {
            throw new BusinessException( typeSignalement, MESSAGE_ERROR_TYPE_HORS_DMR_MISSING_FIELD );
        }

        if ( typeSignalement.getActif( ) && ( typeSignalement.getUnit( ).getIdUnit( ) == 0 ) && noSousType )
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
        }
        else
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
        boolean noSousType = getAllSousTypeSignalement( typeSignalement.getId( ) ).isEmpty( );

        if ( typeSignalement.getActif( ) && ( typeSignalement.getUnit( ).getIdUnit( ) == 0 ) && noSousType )
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
        }
        else
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
        List<TypeSignalement> listeChildren = getAllSousTypeSignalement( typeSignalement.getId( ) );
        for ( TypeSignalement child : listeChildren )
        {
            child.setActif( false );
            update( child );
            disableChildren( child );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllTypeSignalementActifLinkedToUnit( )
    {
        List<TypeSignalement> listAllTypes = getAllTypeSignalement( );
        List<TypeSignalement> listResult = new ArrayList<TypeSignalement>( );
        for ( TypeSignalement type : listAllTypes )
        {
            if ( type.getActif( ) && ( type.getUnit( ).getIdUnit( ) != 0 )  && !type.isHorsDMR( ) )
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
        List<TypeSignalement> listAllTypes = getAllTypeSignalement( );
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
        List<TypeSignalement> allSousTypeSignalement = getAllSousTypeSignalement( typeSignalementId );
        if ( allSousTypeSignalement != null )
        {
            for ( TypeSignalement typeSignalement : allSousTypeSignalement )
            {
                listeTypeSignalement.add( typeSignalement );
                getAllSousTypeSignalementCascade( typeSignalement.getId( ), listeTypeSignalement );
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalementDTO> getTypeSignalementTree( boolean actif )
    {
        List<TypeSignalementDTO> ret = new ArrayList<>( );
        List<TypeSignalement> roots = getAllTypeSignalementWithoutParent( );
        for ( TypeSignalement typeSignalement : roots )
        {

            TypeSignalementDTO dest = new TypeSignalementDTO( );
            try
            {
                BeanUtils.copyProperties( dest, typeSignalement );
            }
            catch( IllegalAccessException | InvocationTargetException e )
            {
                AppLogService.error( e );
            }
            if ( !actif || dest.isActif( ) )
            {
                ret.add( dest );
            }
            fillDestWithChildsCascade( dest, actif );
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalementDTO> getTypeSignalementTreeFromSource( Integer nIdSource )
    {
        /* 3 niveau max de type de signalement - On récupère les 2 niveaux supérieurs */

        // Récupération des types de signalement configurés dans le BO - Ce sont forcément des types de dernier niveau (sans enfant) avec parent
        List<TypeSignalementDTO> typeConfigures = getAllTypeSignalementDTOBySource( nIdSource );

        // Récupération des types de niveau +1
        List<TypeSignalementDTO> listNiveauPlus1 = getListParentFromListEnfant( typeConfigures );

        // Récupération des types de niveau +2
        List<TypeSignalementDTO> listNiveauPlus2 = getListParentFromListEnfant( listNiveauPlus1 );

        // Insertion de tous les types dans une map, avec l'id du type en clé
        Map<Integer, TypeSignalementDTO> mapAllType = new HashMap<>( );

        typeConfigures.forEach( type -> mapAllType.put( type.getId( ), type ) );
        listNiveauPlus1.forEach( type -> mapAllType.put( type.getId( ), type ) );
        listNiveauPlus2.forEach( type -> mapAllType.put( type.getId( ), type ) );

        // Map des id des enfants du 1er niveau (root)
        LinkedHashSet<Integer> idsRoot = new LinkedHashSet<>( );

        // Pour chaque type, si il a un type signalement parent, on ajoute le type dans la liste des enfants du parent
        for ( Map.Entry<Integer, TypeSignalementDTO> entry : mapAllType.entrySet( ) )
        {
            TypeSignalementDTO type = entry.getValue( );

            // Le type a un parent
            if ( type.getIdTypeSignalementParent( ) != 0 )
            {
                // Ajout de l'enfant au parent
                mapAllType.get( type.getIdTypeSignalementParent( ) ).getListChild( ).add( type );
            }
            else
            {
                // Ajout des types de 1er niveau
                idsRoot.add( type.getId( ) );
            }
        }

        List<TypeSignalementDTO> typeSignalementTree = new ArrayList<>( );
        idsRoot.forEach( id -> typeSignalementTree.add( mapAllType.get( id ) ) );

        return typeSignalementTree;
    }

    /**
     * Gets the list parent from list enfant.
     *
     * @param listTypeSignalement
     *            the list type signalement
     * @return the list parent from list enfant
     */
    private List<TypeSignalementDTO> getListParentFromListEnfant( List<TypeSignalementDTO> listTypeSignalement )
    {
        // Pour chaque type, récupération de l'id parent
        LinkedHashSet<Integer> idsParent = new LinkedHashSet<>( );
        listTypeSignalement.forEach( ( TypeSignalementDTO type ) -> {
            if ( type.getIdTypeSignalementParent( ) != 0 )
            {
                idsParent.add( type.getIdTypeSignalementParent( ) );
            }
        } );

        // Récupération des types parent via leur id
        List<TypeSignalementDTO> listTypeSignalementParent = new ArrayList<>( );

        idsParent.forEach( id -> listTypeSignalementParent.add( _typesignalementDAO.getTypeSignalementById( id ) ) );

        return listTypeSignalementParent;
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
        List<TypeSignalement> allSousTypeSignalement = getAllSousTypeSignalement( parentType.getId( ) );
        for ( TypeSignalement typeSignalement : allSousTypeSignalement )
        {

            TypeSignalementDTO dest = new TypeSignalementDTO( );
            try
            {
                BeanUtils.copyProperties( dest, typeSignalement );
            }
            catch( IllegalAccessException | InvocationTargetException e )
            {
                AppLogService.error( e );
            }
            if ( !actif || dest.isActif( ) )
            {
                parentType.getListChild( ).add( dest );
                fillDestWithChildsCascade( dest, actif );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSignalement getTypeSignalementByIdWithParents( Integer nIdTypeSignalement )
    {
        return _typesignalementDAO.getTypeSignalementByIdWithParents( nIdTypeSignalement );
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
    public List<TypeSignalementWithDepth> getAllTypeSignalementActifWithDepth( Integer unitId )
    {
        return _typesignalementDAO.getAllTypeSignalementActifWithDepth( unitId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCategoryFromTypeId( Integer idTypeSignalement )
    {
        return _typesignalementDAO.getCategoryFromTypeId( idTypeSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSignalement getTypeSignalementByIdWithParentsWithoutUnit( Integer id )
    {
        return _typesignalementDAO.getTypeSignalementByIdWithParentsWithoutUnit( id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateParent( Integer nIdTypeSignalement, Integer nIdParent )
    {
        _typesignalementDAO.updateParent( nIdTypeSignalement, nIdParent );
        _typesignalementDAO.refreshViewTypesWithParentsLinks( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getListTypeSignalementLastLevelWithMessage( )
    {
        List<TypeSignalement> listTypeSignalementWithMessage = new ArrayList<>( );

        _typesignalementDAO.getListTypeSignalementLastLevel( ).forEach( ( TypeSignalement typeSignalement ) -> {
            if ( typeSignalement.getNbMessages( ) > 0 )
            {
                typeSignalement.setActif( _messageTypologieService.isMessageActifForTypeSignalement( typeSignalement.getId( ) ) );
                listTypeSignalementWithMessage.add( typeSignalement );
            }
        } );

        return listTypeSignalementWithMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getListTypeSignalementLastLevelWithoutMessage( )
    {
        List<TypeSignalement> listTypeSignalementWithMessage = new ArrayList<>( );

        _typesignalementDAO.getListTypeSignalementLastLevel( ).forEach( ( TypeSignalement typeSignalement ) -> {
            if ( typeSignalement.getNbMessages( ) == 0 )
            {
                listTypeSignalementWithMessage.add( typeSignalement );
            }
        } );

        return listTypeSignalementWithMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertTypeSignalementSource( Integer idType, Integer idSource )
    {
        _typesignalementDAO.insertTypeSignalementSource( idType, idSource );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTypeSignalementSource( Integer idType, Integer idSource )
    {
        _typesignalementDAO.removeTypeSignalementSource( idType, idSource );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalement> getAllTypeSignalementBySource( Integer idSource )
    {
        return _typesignalementDAO.getAllTypeSignalementBySource( idSource );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalementDTO> getAllTypeSignalementDTOBySource( Integer idSource )
    {
        return _typesignalementDAO.getAllTypeSignalementDTOBySource( idSource );
    }

    /**
     * Gets the all type lastlevel not in source.
     *
     * @param idSource
     *            the id source
     * @return the all type lastlevel not in source
     */
    /*
     * (non-Javadoc)
     *
     * @see fr.paris.lutece.plugins.dansmarue.service.ITypeSignalementService#getAllTypeLastlevelNotInSource(java.lang.Integer)
     */
    @Override
    public List<TypeSignalement> getAllTypeLastlevelNotInSource( Integer idSource )
    {
        return _typesignalementDAO.getAllTypeLastlevelNotInSource( idSource );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getListIdsChildren( List<Integer> idParent )
    {
        return _typesignalementDAO.getListIdsChildren( idParent );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Source> getListSource( )
    {
        return _typesignalementDAO.getListSource( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSource( Source source )
    {
        _typesignalementDAO.addSource( source );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSource( Integer idSource )
    {
        _typesignalementDAO.removeSource( idSource );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Source getSourceById( Integer idSource )
    {
        return _typesignalementDAO.getSourceById( idSource );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSource( Source source )
    {
        _typesignalementDAO.updateSource( source );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTypeSignalementSelectable( int idTypeSignalement )
    {
        return _typesignalementDAO.isTypeSignalementSelectable( idTypeSignalement );
    }
}
