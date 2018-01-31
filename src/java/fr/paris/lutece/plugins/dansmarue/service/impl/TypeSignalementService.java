
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

    //MESSAGES
    private static final String MESSAGE_ERROR_TYPE_SIGNALEMENT_MUST_BE_UNIQUE = "dansmarue.message.errortypesignalement.alreadyexists";
    private static final String MESSAGE_ERROR_TYPE_NON_ACTIF_RATTACHE_A_DIRECTION = "dansmarue.message.errortypesignalement.actifwithnounit";

    @Inject
    private ITypeSignalementDAO _typesignalementDAO;

    @Override
    public List<TypeSignalement> getAllTypeSignalement( )
    {
        return _typesignalementDAO.getAllTypeSignalement( );
    }

    @Override
    public List<TypeSignalement> getAll( )
    {
        return _typesignalementDAO.getAll( );
    }

    @Override
    public List<TypeSignalement> getAllSousTypeSignalement( Integer nIdTypeSignalement )
    {
        return _typesignalementDAO.getAllSousTypeSignalement( nIdTypeSignalement );
    }

    @Override
    public List<TypeSignalement> getAllSousTypeSignalementActifs(Integer nIdTypeSignalement){
    	return _typesignalementDAO.getAllSousTypeSignalementActifs( nIdTypeSignalement );
    }
    
    @Override
    public TypeSignalement getByIdTypeSignalement( Integer lIdTypeSignalement )
    {
        Plugin plugin = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
        return _typesignalementDAO.load( lIdTypeSignalement, plugin );
    }

    @Override
    public List<TypeSignalement> getAllTypeSignalementWithoutChildren( )
    {
        return _typesignalementDAO.getAllTypeSignalementWithoutChildren( );
    }

    @Override
    public List<TypeSignalement> getAllTypeSignalementWithoutParent( )
    {
        return _typesignalementDAO.getAllTypeSignalementWithoutParent( );
    }

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
            Integer maxOrderInTypeSignalement = _typesignalementDAO
                    .getMaximumOrderInHierarchyTypeSignalement( typeSignalement.getTypeSignalementParent( ) );
            if ( typeSignalement.getOrdre( ) > maxOrderInTypeSignalement )
            {
                typeSignalement.setOrdre( maxOrderInTypeSignalement + 1 );
            }

            // if the order that the user wants is already used, we add +1 to all the others after
            if ( existsOrdre( typeSignalement.getOrdre( ), typeSignalement.getTypeSignalementParent( ) ) )
            {

                _typesignalementDAO.updateOrderGreaterThan( typeSignalement.getOrdre( ),
                        typeSignalement.getTypeSignalementParent( ) );

            }
            //then we insert the new type signalement
            Integer idTypeSignalement =  _typesignalementDAO.insert( typeSignalement, plugin );
            _typesignalementDAO.insertAlias(typeSignalement, plugin );
            //Refresh the view with the new type
            _typesignalementDAO.refreshViewTypesWithParentsLinks();
            return idTypeSignalement;
        }
        else
        {
            throw new BusinessException( typeSignalement, MESSAGE_ERROR_TYPE_SIGNALEMENT_MUST_BE_UNIQUE );
        }



    }

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

            //then we insert the new type signalement
            int returnCode =  _typesignalementDAO.insertWithoutParent( typeSignalement, plugin );
            //Refresh the view with the new type
            _typesignalementDAO.refreshViewTypesWithParentsLinks();
            return returnCode;
        }
        else
        {
            throw new BusinessException( typeSignalement, MESSAGE_ERROR_TYPE_SIGNALEMENT_MUST_BE_UNIQUE );
        }
    }

    @Override
    public TypeSignalement getParent( Integer nIdTypeSignalement )
    {
        Plugin plugin = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
        return _typesignalementDAO.getParent( nIdTypeSignalement, plugin );
    }

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
            _typesignalementDAO.deleteAlias(typeSignalement.getId(), plugin);
            if(noSousType){
            	_typesignalementDAO.insertAlias(typeSignalement, plugin);
            }
            _typesignalementDAO.refreshViewTypesWithParentsLinks();
        }
        else
        {
            throw new BusinessException( typeSignalement, MESSAGE_ERROR_TYPE_SIGNALEMENT_MUST_BE_UNIQUE );
        }
    }

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
            _typesignalementDAO.deleteAlias(typeSignalement.getId(), plugin);
            if(noSousType){
            	_typesignalementDAO.insertAlias(typeSignalement, plugin);
            }
            _typesignalementDAO.refreshViewTypesWithParentsLinks();
        }
        else
        {
            throw new BusinessException( typeSignalement, MESSAGE_ERROR_TYPE_SIGNALEMENT_MUST_BE_UNIQUE );
        }

    }

    @Override
    public boolean existsSaveTypeSignalement( TypeSignalement typeSignalement )
    {
        return _typesignalementDAO.existsSaveTypeSignalement( typeSignalement );
    }

    @Override
    public TypeSignalement findByIdTypeSignalement( Integer nIdTypeSignalement )
    {
        return _typesignalementDAO.findByIdTypeSignalement( nIdTypeSignalement );
    }

    @Override
    public void delete( Integer nIdTypeSignalement )
    {
        Plugin plugin = PluginService.getPlugin( SignalementPlugin.PLUGIN_NAME );
        _typesignalementDAO.deleteAlias(nIdTypeSignalement, plugin);
        _typesignalementDAO.remove( nIdTypeSignalement, plugin );

        //Refresh the view without the type
        _typesignalementDAO.refreshViewTypesWithParentsLinks();
    }

    @Override
    public boolean findByIdSignalement( Integer nIdSignalement )
    {
        return _typesignalementDAO.findByIdSignalement( nIdSignalement );
    }

    @Override
    public TypeSignalement getTypeSignalement( Integer nId )
    {
        return _typesignalementDAO.getTypeSignalement( nId );
    }

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
    
    @Override
    public List<TypeSignalement> getAllTypeSignalementActif( )
    {
        List<TypeSignalement> typeSignalementList =  _typesignalementDAO.getAllTypeSignalementEvenWithoutUnit();
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
            }
            catch ( IllegalAccessException e )
            {
                AppLogService.error( e );
            }
            catch ( InvocationTargetException e )
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
     * @param parentType id true return only actives types, else return all
     *            types.
     * @param actif the actif
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
            }
            catch ( IllegalAccessException e )
            {
                AppLogService.error( e );
            }
            catch ( InvocationTargetException e )
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

    @Override
    public TypeSignalement getTypeSignalementByIdWithParents( Integer nIdTypeSignalement )
    {
        return this._typesignalementDAO.getTypeSignalementByIdWithParents( nIdTypeSignalement );
    }

    public int getIdTypeSignalementOrdreInferieur( TypeSignalement typeSignalement )
    {
        return _typesignalementDAO.getIdTypeSignalementOrdreInferieur( typeSignalement );
    }

    @Override
    public void updateOrdre( TypeSignalement typeSignalement )
    {
        _typesignalementDAO.updateOrdre( typeSignalement );

    }

    @Override
    public int getIdTypeSignalementOrdreSuperieur( TypeSignalement typeSignalement )
    {
        return _typesignalementDAO.getIdTypeSignalementOrdreSuperieur( typeSignalement );
    }

    @Override
    public boolean existsOrdre( int ordre, TypeSignalement typeSignalementParent )
    {
        return _typesignalementDAO.existsOrdre( ordre, typeSignalementParent );
    }

    @Override
    public void updateOrderGreaterThan( Integer ordre, TypeSignalement typeSignalementParent )
    {
        _typesignalementDAO.updateOrderGreaterThan( ordre, typeSignalementParent );
        
    }

    @Override
    public Integer getMinimumOrderAfterGivenOrder( Integer order, TypeSignalement typeSignalementParent )
    {
        return _typesignalementDAO.getMinimumOrderAfterGivenOrder( order, typeSignalementParent );
    }

    @Override
    public void updateOrderSuperiorAfterDeletingTypeSignalement( Integer order, TypeSignalement typeSignalementParent )
    {
        _typesignalementDAO.updateOrderSuperiorAfterDeletingTypeSignalement( order, typeSignalementParent );

    }

    @Override
    public Integer getMaximumOrderInHierarchyTypeSignalement( TypeSignalement typeSignalementParent )
    {
        return _typesignalementDAO.getMaximumOrderInHierarchyTypeSignalement( typeSignalementParent );
    }

    public ImageResource getImageResource( int nKey )
    {
        return _typesignalementDAO.loadImage( nKey );
    }
    

    /**
     * Method to get the last type signalement version 
     * @return last type signalement version
     */
    public double findLastVersionTypeSignalement(  )
    {
    	return _typesignalementDAO.findLastVersionTypeSignalement(  );
    }
    
    /**
     * Gets all the type signalement root (level 0, without parent) which are actif
     * @return
     */
    public List<TypeSignalement> getAllTypeSignalementActifWithoutParent( ){
    	return _typesignalementDAO.getAllTypeSignalementActifWithoutParent();
    }
    
    /**
     * Retrieves a category id, of a type signalement id
     * @param idTypeSignalement
     * @return
     * 		The id of the category of the type signalement
     * 		-1 if not found
     */
    @Override
    public int getCategoryFromTypeId(Integer idTypeSignalement){
    	return _typesignalementDAO.getCategoryFromTypeId(idTypeSignalement);
    }
}
