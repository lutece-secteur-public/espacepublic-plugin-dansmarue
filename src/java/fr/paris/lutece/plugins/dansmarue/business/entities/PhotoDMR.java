package fr.paris.lutece.plugins.dansmarue.business.entities;

import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.image.ImageResourceProvider;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.url.UrlItem;

public class PhotoDMR
{
    public static final Integer VUE_D_ENSEMBLE  = 1;
    public static final Integer VUE_DETAILLE    = 0;
    private Long                _lId;
    private ImageResource       _image          = null;
    private ImageResource       _imageThumbnail = new ImageResource( );
    private Signalement         _signalement;
    private String              _date;
    private Integer             _nVue;

    public Long getId( )
    {
        return _lId;
    }

    public void setId( Long id )
    {
        _lId = id;
    }

    public ImageResource getImage( )
    {
        return _image;
    }

    public void setImage( ImageResource image )
    {
        _image = image;
    }

    public String getImageUrl( )
    {
        String strResourceType = ( ( ImageResourceProvider ) SpringContextService.getBean( "signalement.imageService" ) ).getResourceTypeId( );
        UrlItem url = new UrlItem( Parameters.IMAGE_SERVLET );
        url.addParameter( Parameters.RESOURCE_TYPE, strResourceType );
        url.addParameter( Parameters.RESOURCE_ID, Long.toString( _lId ) );
        return url.getUrlWithEntity( );
    }

    public String getImageThumbnailUrl( )
    {
        String strResourceType = ( ( ImageResourceProvider ) SpringContextService.getBean( "signalement.imageThumbnailService" ) ).getResourceTypeId( );
        UrlItem url = new UrlItem( Parameters.IMAGE_SERVLET );
        url.addParameter( Parameters.RESOURCE_TYPE, strResourceType );
        url.addParameter( Parameters.RESOURCE_ID, Long.toString( _lId ) );
        return url.getUrlWithEntity( );
    }

    public Signalement getSignalement( )
    {
        return _signalement;
    }

    public void setSignalement( Signalement signalement )
    {
        _signalement = signalement;
    }

    public void setMimeType( String strMimeType )
    {
        _image.setMimeType( strMimeType );
    }

    public void setImageContent( byte[] imageContent )
    {
        _image.setImage( imageContent );
    }

    /**
     * Sets the image thumbnail.
     *
     * @param imageContent
     *            the new image thumbnail
     */
    public void setImageThumbnail( byte[] imageContent )
    {
        _imageThumbnail.setImage( imageContent );

    }

    /**
     * @return the _imageThumbnail
     */
    public ImageResource getImageThumbnail( )
    {
        return _imageThumbnail;
    }

    public String getDate( )
    {
        return _date;
    }

    public void setDate( String date )
    {
        _date = date;
    }

    public Integer getVue( )
    {
        return _nVue;
    }

    public void setVue( Integer vue )
    {
        _nVue = vue;
    }

}
