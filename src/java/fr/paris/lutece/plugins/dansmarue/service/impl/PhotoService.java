package fr.paris.lutece.plugins.dansmarue.service.impl;

import fr.paris.lutece.plugins.dansmarue.business.dao.IPhotoDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.service.IPhotoService;
import fr.paris.lutece.portal.service.image.ImageResource;

import java.util.List;

import javax.inject.Inject;


public class PhotoService implements IPhotoService
{
    @Inject
    private IPhotoDAO _photoDAO;

    public Long insert( PhotoDMR photo )
    {
        return _photoDAO.insert( photo );
    }


    public void remove( long lId )
    {
        _photoDAO.remove( lId );
        
    }


    public PhotoDMR load( long lId )
    {
        return _photoDAO.load( lId );
    }


    public void store( PhotoDMR photo )
    {
        _photoDAO.store( photo );
        
    }

    public ImageResource getImageResource( int nKey )
    {
        return _photoDAO.loadPhoto( nKey );
    }

    @Override
    public ImageResource getImageThumbnailResource( int nIdPhoto )
    {
        return _photoDAO.loadPhotoThumbnail( nIdPhoto );
    }

    public PhotoDMR loadByIdSignalement( long lId )
    {
        return _photoDAO.loadByIdSignalement( lId );
    }

    @Override
    public ImageResource loadPhoto( int nIdPhoto )
    {
        // TODO Auto-generated method stub
        return null;
    }

    public List<PhotoDMR> findBySignalementId( long lIdSignalement )
    {
        return _photoDAO.findBySignalementId( lIdSignalement );
    }

    public List<PhotoDMR> findWithFullPhotoBySignalementId( long lIdSignalement )
    {
        return _photoDAO.findWithFullPhotoBySignalementId( lIdSignalement );
    }
}
