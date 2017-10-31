package fr.paris.lutece.plugins.dansmarue.service.dto;

import fr.paris.lutece.plugins.sira.business.entities.TypeSignalement;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;

import java.util.ArrayList;
import java.util.List;


/**
 * Almost the same as TypeSignalement but with a list of children.
 */
public class TypeSignalementDTO
{

    /** The _actif. */
    private boolean _actif;

    /** The id. */
    private Integer _id;

    /** The id type signalement parent. */
    private Integer _idTypeSignalementParent;

    /** The _is selected. */
    private boolean _isSelected;

    /** The libelle. */
    private String _libelle;

    /** The imageUrl. */
    private String _imageUrl;

    /** The list child. */
    private List<TypeSignalementDTO> _listChild = new ArrayList<TypeSignalementDTO>( );

    /** The _minus. */
    private boolean _minus;

    /** The type signalement parent. */
    private TypeSignalement _typeSignalementParent;

    /** The unit. */
    private Unit _unit;

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public Integer getId( )
    {
        return _id;
    }

    /**
     * Gets the id type signalement parent.
     * 
     * @return the id type signalement parent
     */
    public Integer getIdTypeSignalementParent( )
    {
        return _idTypeSignalementParent;
    }

    /**
     * Gets the libelle.
     * 
     * @return the libelle
     */
    public String getLibelle( )
    {
        return _libelle;
    }

    /**
     * Gets the imageUrl.
     * 
     * @return the imageUrl
     */
    public String getImageUrl( )
    {
        return _imageUrl;
    }

    /**
     * Gets the list child.
     * 
     * @return the list child
     */
    public List<TypeSignalementDTO> getListChild( )
    {
        return _listChild;
    }

    /**
     * Gets the type signalement parent.
     * 
     * @return the type signalement parent
     */
    public TypeSignalement getTypeSignalementParent( )
    {
        return _typeSignalementParent;
    }

    /**
     * Gets the unit.
     * 
     * @return the unit
     */
    public Unit getUnit( )
    {
        return _unit;
    }

    /**
     * Checks if is actif.
     * 
     * @return true, if is actif
     */
    public boolean isActif( )
    {
        return _actif;
    }

    /**
     * Checks if is minus.
     * 
     * @return true, if is minus
     */
    public boolean isMinus( )
    {
        return _minus;
    }

    /**
     * Checks if is selected.
     * 
     * @return true, if is selected
     */
    protected boolean isSelected( )
    {
        return _isSelected;
    }

    /**
     * Sets the actif.
     * 
     * @param actif the new actif
     */
    public void setActif( boolean actif )
    {
        this._actif = actif;
    }

    /**
     * Sets the id.
     * 
     * @param id the new id
     */
    public void setId( Integer id )
    {
        this._id = id;
    }


    /**
     * Sets the id type signalement parent.
     * 
     * @param idTypeSignalementParent the new id type signalement parent
     */
    public void setIdTypeSignalementParent( Integer idTypeSignalementParent )
    {
        this._idTypeSignalementParent = idTypeSignalementParent;
    }


    /**
     * Sets the libelle.
     * 
     * @param libelle the new libelle
     */
    public void setLibelle( String libelle )
    {
        this._libelle = libelle;
    }

    /**
     * Sets the Image url.
     * 
     * @param imageUrl the new image url
     */
    public void setImageUrl( String imageUrl )
    {
        this._imageUrl = imageUrl;
    }

    /**
     * Sets the list child.
     * 
     * @param listChild the new list child
     */
    public void setListChild( List<TypeSignalementDTO> listChild )
    {
        this._listChild = listChild;
    }

    /**
     * Sets the minus.
     * 
     * @param minus the new minus
     */
    public void setMinus( boolean minus )
    {
        this._minus = minus;
    }

    /**
     * Sets the selected.
     * 
     * @param isSelected the new selected
     */
    protected void setSelected( boolean isSelected )
    {
        this._isSelected = isSelected;
    }

    /**
     * Sets the type signalement parent.
     * 
     * @param typeSignalementParent the new type signalement parent
     */
    public void setTypeSignalementParent( TypeSignalement typeSignalementParent )
    {
        this._typeSignalementParent = typeSignalementParent;
    }

    /**
     * Sets the unit.
     * 
     * @param unit the new unit
     */
    public void setUnit( Unit unit )
    {
        this._unit = unit;
    }
}
