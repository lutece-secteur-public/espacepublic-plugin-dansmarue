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

package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.dao.IAideDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Aide;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for Aide objects
 */
public final class AideDAO implements IAideDAO
{
    // Constants
    /** The Constant SQL_QUERY_NEW_PK. */
    private static final String SQL_QUERY_NEW_PK = "SELECT nextval('seq_signalement_aide_id_aide')";

    private static final String SQL_QUERY_SELECT = "SELECT id_aide, libelle, url_hypertexte, image_content, image_mime_type, actif, ordre FROM signalement_aide WHERE id_aide = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_aide (id_aide, libelle, url_hypertexte, image_content, image_mime_type, actif, ordre ) VALUES ( ?, ?, ?, ?, ?, ?, (select count(id_aide)+1 FROM signalement_aide) ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_aide WHERE id_aide = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_aide SET id_aide = ?, libelle = ?, url_hypertexte = ?, image_content=?, image_mime_type=?, actif = ? WHERE id_aide = ?";
    private static final String SQL_QUERY_UPDATE_WITHOUT_IMAGE = "UPDATE signalement_aide SET id_aide = ?, libelle = ?, url_hypertexte = ?, actif = ? WHERE id_aide = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_aide, libelle, url_hypertexte, actif, ordre FROM signalement_aide ORDER BY ordre ASC";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_aide FROM signalement_aide";

    /** The Constant SQL_QUERY_SELECT_ALL_AIDE_ACTIF. */
    private static final String SQL_QUERY_SELECT_ALL_AIDE_ACTIF = "SELECT id_aide, libelle, url_hypertexte, image_content, image_mime_type, actif, ordre FROM signalement_aide WHERE actif=1 ORDER BY ordre";

    private static final String SQL_QUERY_EXISTS_AIDE = "SELECT id_aide FROM signalement_aide WHERE libelle=?";

    private static final String SQL_QUERY_EXISTS_AIDE_WITH_ID = "SELECT id_aide FROM signalement_aide WHERE libelle=? AND NOT id_aide=? ";

    /** The Constant SQL_QUERY_UPDATE_AIDE_ORDRE. */
    private static final String SQL_QUERY_UPDATE_AIDE_ORDRE = "UPDATE signalement_aide SET ordre = ? WHERE id_aide = ?";

    /** The Constant SQL_QUERY_DECREASE_ORDER_OF_NEXT. */
    private static final String SQL_QUERY_DECREASE_ORDER_OF_NEXT = "UPDATE signalement_aide SET ordre = ordre-1 WHERE ordre= (? + 1)";

    /** The Constant SQL_QUERY_INCREASE_ORDER_OF_PREVIOUS. */
    private static final String SQL_QUERY_INCREASE_ORDER_OF_PREVIOUS = "UPDATE signalement_aide SET ordre = ordre+1 WHERE ordre= (? - 1)";

    /** The Constant SQL_QUERY_INCREASE_ORDER_OF_ALL_NEXT. */
    private static final String SQL_QUERY_INCREASE_ORDER_OF_ALL_NEXT = "UPDATE signalement_aide SET ordre = ordre+1 WHERE ordre >= "
            + "(SELECT ordre FROM signalement_aide WHERE id_aide = ?) AND id_aide != ?";

    /** The Constant SQL_QUERY_DECREASE_ORDER_OF_ALL_NEXT. */
    private static final String SQL_QUERY_DECREASE_ORDER_OF_ALL_NEXT = "UPDATE signalement_aide SET ordre = ordre-1 WHERE ordre >= "
            + "(SELECT ordre FROM signalement_aide WHERE id_aide = ?)";

    /** The Constant SQL_QUERY_GET_AIDE_COUNT. */
    private static final String SQL_QUERY_GET_AIDE_COUNT = "SELECT count(id_aide) FROM signalement_aide";

    private static final String SQL_QUERY_UPDATE_VERSION_AIDE = "update signalement_versions set \"version\" = \"version\"+1 where \"table\" = 'aides'";

    private static final String SQL_QUERY_SELECT_VERSION_AIDE = "select \"version\" from signalement_versions where \"table\" = 'aides'";

    private static final String SQL_QUERY_FIND_IMAGE_BY_PRIMARY_KEY = "SELECT image_content, image_mime_type FROM signalement_aide WHERE id_aide=? ";

    /**
     * Generates a new primary key.
     *
     * @return The new primary key
     */
    private Integer newPrimaryKey( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery( );
        Integer nKey = null;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 );
        }
        else
        {
            nKey = 1;
        }
        daoUtil.close( );

        return nKey.intValue( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Integer insert( Aide aide )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) )
        {
            if ( ( aide.getId( ) == null ) || ( aide.getId( ) == 0 ) )
            {
                aide.setId( newPrimaryKey( ) );
            }

            int nIndex = 1;
            daoUtil.setLong( nIndex++, aide.getId( ) );
            daoUtil.setString( nIndex++, aide.getLibelle( ) );
            daoUtil.setString( nIndex++, aide.getHypertexteUrl( ) );
            daoUtil.setBytes( nIndex++, aide.getImage( ).getImage( ) );
            daoUtil.setString( nIndex++, aide.getImage( ).getMimeType( ) );
            daoUtil.setBoolean( nIndex, aide.getActif( ) );
            daoUtil.executeUpdate( );

            updateVersionAide( );

            return aide.getId( );
        }

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Aide load( Integer nIdAide )
    {
        Aide aide = new Aide( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setLong( 1, nIdAide );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            aide.setId( daoUtil.getInt( nIndex++ ) );
            aide.setLibelle( daoUtil.getString( nIndex++ ) );
            aide.setHypertexteUrl( daoUtil.getString( nIndex++ ) );
            Object oImageContent = daoUtil.getBytes( nIndex++ );
            aide.setImage( new ImageResource( ) );
            aide.setImageContent( (byte [ ]) oImageContent );
            aide.setMimeType( daoUtil.getString( nIndex++ ) );
            aide.setActif( daoUtil.getBoolean( nIndex++ ) );
            aide.setOrdre( daoUtil.getInt( nIndex ) );
        }

        daoUtil.close( );

        return aide;

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( long nIdAide )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setLong( 1, nIdAide );
            daoUtil.executeUpdate( );
            daoUtil.free( );

            updateVersionAide( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Aide aide )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, aide.getId( ) );
        daoUtil.setString( nIndex++, aide.getLibelle( ) );
        daoUtil.setString( nIndex++, aide.getHypertexteUrl( ) );
        daoUtil.setBytes( nIndex++, aide.getImage( ).getImage( ) );
        daoUtil.setString( nIndex++, aide.getImage( ).getMimeType( ) );
        daoUtil.setBoolean( nIndex++, aide.getActif( ) );

        // WHERE
        daoUtil.setLong( nIndex, aide.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );

        updateVersionAide( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Aide> selectAidesList( Plugin plugin )
    {
        List<Aide> aideList = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            Aide aide = new Aide( );
            int nIndex = 1;

            aide.setId( daoUtil.getInt( nIndex++ ) );
            aide.setLibelle( daoUtil.getString( nIndex++ ) );
            aide.setHypertexteUrl( daoUtil.getString( nIndex++ ) );
            aide.setActif( daoUtil.getBoolean( nIndex++ ) );
            aide.setOrdre( daoUtil.getInt( nIndex ) );

            aideList.add( aide );
        }

        daoUtil.close( );

        return aideList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdAidesList( Plugin plugin )
    {
        List<Integer> aideList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                aideList.add( daoUtil.getInt( 1 ) );
            }

            daoUtil.free( );
            return aideList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectAidesReferenceList( Plugin plugin )
    {
        ReferenceList aideList = new ReferenceList( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                aideList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }

            daoUtil.free( );
            return aideList;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Aide> getAllAideActif( )
    {
        List<Aide> listResult = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_AIDE_ACTIF );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;

            Aide aide = new Aide( );
            aide.setId( daoUtil.getInt( nIndex++ ) );
            aide.setLibelle( daoUtil.getString( nIndex++ ) );
            aide.setHypertexteUrl( daoUtil.getString( nIndex++ ) );
            Object oImageContent = daoUtil.getBytes( nIndex++ );
            aide.setImage( new ImageResource( ) );
            aide.setImageContent( (byte [ ]) oImageContent );
            aide.setMimeType( daoUtil.getString( nIndex++ ) );
            aide.setActif( daoUtil.getBoolean( nIndex++ ) );
            aide.setOrdre( daoUtil.getInt( nIndex ) );
            listResult.add( aide );
        }

        daoUtil.close( );

        return listResult;
    }

    @Override
    public boolean existsAide( Aide aide )
    {
        boolean existsAide = false;

        DAOUtil daoUtil;

        if ( ( aide.getId( ) != null ) && ( aide.getId( ) > 0 ) )
        {
            int nIndex = 1;

            daoUtil = new DAOUtil( SQL_QUERY_EXISTS_AIDE_WITH_ID );
            daoUtil.setString( nIndex++, aide.getLibelle( ) );
            daoUtil.setLong( nIndex, aide.getId( ) );
        }
        else
        {
            daoUtil = new DAOUtil( SQL_QUERY_EXISTS_AIDE );
            daoUtil.setString( 1, aide.getLibelle( ) );
        }

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            existsAide = true;
        }

        daoUtil.close( );

        return existsAide;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreaseOrdreOfNextAide( Aide aide )
    {
        DAOUtil daoUtil;
        if ( aide.getOrdre( ) != null )
        {
            int nIndex = 1;
            daoUtil = new DAOUtil( SQL_QUERY_DECREASE_ORDER_OF_NEXT );
            daoUtil.setInt( nIndex, aide.getOrdre( ) );
            daoUtil.executeUpdate( );
            daoUtil.close( );

            updateVersionAide( );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseOrdreOfPreviousAide( Aide aide )
    {
        DAOUtil daoUtil;
        if ( aide.getOrdre( ) != null )
        {
            int nIndex = 1;
            daoUtil = new DAOUtil( SQL_QUERY_INCREASE_ORDER_OF_PREVIOUS );
            daoUtil.setInt( nIndex, aide.getOrdre( ) );
            daoUtil.executeUpdate( );
            daoUtil.close( );

            updateVersionAide( );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAideOrdre( Aide aide )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_AIDE_ORDRE );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, aide.getOrdre( ) );
        daoUtil.setLong( nIndex, aide.getId( ) );
        daoUtil.executeUpdate( );
        daoUtil.close( );

        updateVersionAide( );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseOrdreOfAllNext( long nIdAide )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INCREASE_ORDER_OF_ALL_NEXT );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, nIdAide );
        daoUtil.setLong( nIndex, nIdAide );
        daoUtil.executeUpdate( );
        daoUtil.close( );

        updateVersionAide( );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreaseOrdreOfAllNext( long nIdAide )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DECREASE_ORDER_OF_ALL_NEXT );
        int nIndex = 1;
        daoUtil.setLong( nIndex, nIdAide );
        daoUtil.executeUpdate( );
        daoUtil.close( );

        updateVersionAide( );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAideCount( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GET_AIDE_COUNT );
        daoUtil.executeQuery( );
        int aideCount = 0;
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            aideCount = daoUtil.getInt( nIndex );

        }
        daoUtil.close( );
        return aideCount;
    }

    /**
     * Update version aide.
     */
    private void updateVersionAide( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_VERSION_AIDE );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    @Override
    public int getVersionAide( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_VERSION_AIDE );
        daoUtil.executeQuery( );
        int version = 0;
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            version = daoUtil.getInt( nIndex );

        }
        daoUtil.close( );

        return version;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageResource getImageResource( int nIdAide )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_IMAGE_BY_PRIMARY_KEY );
        daoUtil.setInt( 1, nIdAide );
        daoUtil.executeQuery( );

        ImageResource image = new ImageResource( );

        int nIndex = 1;

        if ( daoUtil.next( ) )
        {
            image.setImage( daoUtil.getBytes( nIndex++ ) );
            image.setMimeType( daoUtil.getString( nIndex ) );
        }

        daoUtil.close( );

        return image;
    }

    @Override
    public void storeWithoutImage( Aide aide )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_WITHOUT_IMAGE );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, aide.getId( ) );
        daoUtil.setString( nIndex++, aide.getLibelle( ) );
        daoUtil.setString( nIndex++, aide.getHypertexteUrl( ) );
        daoUtil.setBoolean( nIndex++, aide.getActif( ) );

        // WHERE
        daoUtil.setLong( nIndex, aide.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );

        updateVersionAide( );

    }
}
