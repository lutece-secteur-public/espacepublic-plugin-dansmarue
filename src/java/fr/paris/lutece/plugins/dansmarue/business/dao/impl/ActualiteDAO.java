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

import fr.paris.lutece.plugins.dansmarue.business.dao.IActualiteDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Actualite;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

public class ActualiteDAO implements IActualiteDAO
{

    /** The Constant SQL_QUERY_NEW_PK. */
    private static final String SQL_QUERY_NEW_PK = "SELECT nextval('seq_signalement_actualite_id_actualite')";

    /** The Constant SQL_QUERY_INSERT. */
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_actualite(id_actualite, libelle, texte, image_content, image_mime_type, actif, ordre) VALUES (?, ?, ?, ?, ?, ?, (select count(id_actualite)+1 FROM signalement_actualite))";

    /** The Constant SQL_QUERY_DELETE. */
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_actualite WHERE id_actualite = ?";

    /** The Constant SQL_QUERY_SELECT. */
    private static final String SQL_QUERY_SELECT = "SELECT id_actualite, libelle, texte, image_content, image_mime_type, actif, ordre FROM signalement_actualite WHERE id_actualite = ?";

    /** The Constant SQL_QUERY_UPDATE. */
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_actualite SET id_actualite=?, libelle=?, texte=?, image_content=?, image_mime_type=?, actif=? WHERE id_actualite = ?";

    private static final String SQL_QUERY_UPDATE_WITHOUT_IMAGE = "UPDATE signalement_actualite SET id_actualite=?, libelle=?, texte=?, actif=? WHERE id_actualite = ?";

    /** The Constant SQL_QUERY_SELECT_ALL_ACTUALITE. */
    private static final String SQL_QUERY_SELECT_ALL_ACTUALITE = "SELECT id_actualite, libelle, texte, image_content, image_mime_type, actif, ordre FROM signalement_actualite ORDER BY ordre";

    /** The Constant SQL_QUERY_SELECT_ALL_ACTUALITE_ACTIF. */
    private static final String SQL_QUERY_SELECT_ALL_ACTUALITE_ACTIF = "SELECT id_actualite, libelle, texte, image_content, image_mime_type, actif, ordre FROM signalement_actualite WHERE actif=1 ORDER BY ordre";

    /** The Constant SQL_QUERY_EXISTS_ACTUALITE. */
    private static final String SQL_QUERY_EXISTS_ACTUALITE = "SELECT id_actualite FROM signalement_actualite WHERE libelle=?";

    /** The Constant SQL_QUERY_EXISTS_ACTUALITE_WITH_ID. */
    private static final String SQL_QUERY_EXISTS_ACTUALITE_WITH_ID = "SELECT id_actualite FROM signalement_actualite WHERE libelle=? AND NOT id_actualite=? ";

    /** The Constant SQL_QUERY_UPDATE_ACTUALITE_ORDRE. */
    private static final String SQL_QUERY_UPDATE_ACTUALITE_ORDRE = "UPDATE signalement_actualite SET ordre = ? WHERE id_actualite = ?";

    /** The Constant SQL_QUERY_DECREASE_ORDER_OF_NEXT. */
    private static final String SQL_QUERY_DECREASE_ORDER_OF_NEXT = "UPDATE signalement_actualite SET ordre = ordre-1 WHERE ordre= (? + 1)";

    /** The Constant SQL_QUERY_INCREASE_ORDER_OF_PREVIOUS. */
    private static final String SQL_QUERY_INCREASE_ORDER_OF_PREVIOUS = "UPDATE signalement_actualite SET ordre = ordre+1 WHERE ordre= (? - 1)";

    /** The Constant SQL_QUERY_INCREASE_ORDER_OF_ALL_NEXT. */
    private static final String SQL_QUERY_INCREASE_ORDER_OF_ALL_NEXT = "UPDATE signalement_actualite SET ordre = ordre+1 WHERE ordre >= "
            + "(SELECT ordre FROM signalement_actualite WHERE id_actualite = ?) AND id_actualite != ?";

    /** The Constant SQL_QUERY_DECREASE_ORDER_OF_ALL_NEXT. */
    private static final String SQL_QUERY_DECREASE_ORDER_OF_ALL_NEXT = "UPDATE signalement_actualite SET ordre = ordre-1 WHERE ordre >= "
            + "(SELECT ordre FROM signalement_actualite WHERE id_actualite = ?)";

    /** The Constant SQL_QUERY_GET_ACTUALITE_COUNT. */
    private static final String SQL_QUERY_GET_ACTUALITE_COUNT = "SELECT count(id_actualite) FROM signalement_actualite";

    private static final String SQL_QUERY_FIND_IMAGE_BY_PRIMARY_KEY = "SELECT image_content, image_mime_type FROM signalement_actualite WHERE id_actualite=? ";

    private static final String SQL_QUERY_UPDATE_VERSION_ACTUALITE = "update signalement_versions set \"version\" = \"version\"+1 where \"table\" = 'actualites'";

    private static final String SQL_QUERY_SELECT_VERSION_ACTUALITE = "select \"version\" from signalement_versions where \"table\" = 'actualites'";

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
     * {@inheritDoc}
     */
    @Override
    public Integer insert( Actualite actualite )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) )
        {
            if ( ( actualite.getId( ) == null ) || ( actualite.getId( ) == 0 ) )
            {
                actualite.setId( newPrimaryKey( ) );
            }
            int nIndex = 1;
            daoUtil.setLong( nIndex++, actualite.getId( ) );
            daoUtil.setString( nIndex++, actualite.getLibelle( ) );
            daoUtil.setString( nIndex++, actualite.getTexte( ) );
            daoUtil.setBytes( nIndex++, actualite.getImage( ).getImage( ) );
            daoUtil.setString( nIndex++, actualite.getImage( ).getMimeType( ) );
            daoUtil.setBoolean( nIndex, actualite.getActif( ) );
            daoUtil.executeUpdate( );

            updateVersionActualite( );

            return actualite.getId( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( long lId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setLong( 1, lId );
        daoUtil.executeUpdate( );
        daoUtil.close( );

        updateVersionActualite( );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Actualite load( Integer lId )
    {
        Actualite actualite = new Actualite( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setLong( 1, lId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            actualite.setId( daoUtil.getInt( nIndex++ ) );
            actualite.setLibelle( daoUtil.getString( nIndex++ ) );
            actualite.setTexte( daoUtil.getString( nIndex++ ) );
            Object oImageContent = daoUtil.getBytes( nIndex++ );
            actualite.setImage( new ImageResource( ) );
            actualite.setImageContent( (byte [ ]) oImageContent );
            actualite.setMimeType( daoUtil.getString( nIndex++ ) );
            actualite.setActif( daoUtil.getBoolean( nIndex++ ) );
            actualite.setOrdre( daoUtil.getInt( nIndex ) );
        }

        daoUtil.close( );

        return actualite;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( Actualite actualite )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, actualite.getId( ) );
        daoUtil.setString( nIndex++, actualite.getLibelle( ) );
        daoUtil.setString( nIndex++, actualite.getTexte( ) );
        daoUtil.setBytes( nIndex++, actualite.getImage( ).getImage( ) );
        daoUtil.setString( nIndex++, actualite.getImage( ).getMimeType( ) );
        daoUtil.setBoolean( nIndex++, actualite.getActif( ) );

        // WHERE
        daoUtil.setLong( nIndex, actualite.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );

        updateVersionActualite( );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storeWithoutImage( Actualite actualite )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_WITHOUT_IMAGE );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, actualite.getId( ) );
        daoUtil.setString( nIndex++, actualite.getLibelle( ) );
        daoUtil.setString( nIndex++, actualite.getTexte( ) );
        daoUtil.setBoolean( nIndex++, actualite.getActif( ) );

        // WHERE
        daoUtil.setLong( nIndex, actualite.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.close( );

        updateVersionActualite( );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Actualite> getAllActualite( Plugin plugin )
    {
        List<Actualite> listResult = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_ACTUALITE, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;

            Actualite actualite = new Actualite( );
            actualite.setId( daoUtil.getInt( nIndex++ ) );
            actualite.setLibelle( daoUtil.getString( nIndex++ ) );
            actualite.setTexte( daoUtil.getString( nIndex++ ) );
            Object oImageContent = daoUtil.getBytes( nIndex++ );
            actualite.setImage( new ImageResource( ) );
            actualite.setImageContent( (byte [ ]) oImageContent );
            actualite.setMimeType( daoUtil.getString( nIndex++ ) );
            actualite.setActif( daoUtil.getBoolean( nIndex++ ) );
            actualite.setOrdre( daoUtil.getInt( nIndex ) );
            listResult.add( actualite );
        }

        daoUtil.close( );

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Actualite> getAllActualiteActif( )
    {
        List<Actualite> listResult = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_ACTUALITE_ACTIF );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;

            Actualite actualite = new Actualite( );
            actualite.setId( daoUtil.getInt( nIndex++ ) );
            actualite.setLibelle( daoUtil.getString( nIndex++ ) );
            actualite.setTexte( daoUtil.getString( nIndex++ ) );
            Object oImageContent = daoUtil.getBytes( nIndex++ );
            actualite.setImage( new ImageResource( ) );
            actualite.setImageContent( (byte [ ]) oImageContent );
            actualite.setMimeType( daoUtil.getString( nIndex++ ) );
            actualite.setActif( daoUtil.getBoolean( nIndex++ ) );
            actualite.setOrdre( daoUtil.getInt( nIndex ) );
            listResult.add( actualite );
        }

        daoUtil.close( );

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsActualite( Actualite actualite )
    {
        boolean existsActualite = false;

        DAOUtil daoUtil;

        if ( ( actualite.getId( ) != null ) && ( actualite.getId( ) > 0 ) )
        {
            int nIndex = 1;

            daoUtil = new DAOUtil( SQL_QUERY_EXISTS_ACTUALITE_WITH_ID );
            daoUtil.setString( nIndex++, actualite.getLibelle( ) );
            daoUtil.setLong( nIndex, actualite.getId( ) );
        }
        else
        {
            daoUtil = new DAOUtil( SQL_QUERY_EXISTS_ACTUALITE );
            daoUtil.setString( 1, actualite.getLibelle( ) );
        }

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            existsActualite = true;
        }

        daoUtil.close( );

        return existsActualite;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreaseOrdreOfNextActualite( Actualite actualite )
    {
        DAOUtil daoUtil;
        if ( actualite.getOrdre( ) != null )
        {
            int nIndex = 1;
            daoUtil = new DAOUtil( SQL_QUERY_DECREASE_ORDER_OF_NEXT );
            daoUtil.setInt( nIndex, actualite.getOrdre( ) );
            daoUtil.executeUpdate( );
            daoUtil.close( );

            updateVersionActualite();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseOrdreOfPreviousActualite( Actualite actualite )
    {
        DAOUtil daoUtil;
        if ( actualite.getOrdre( ) != null )
        {
            int nIndex = 1;
            daoUtil = new DAOUtil( SQL_QUERY_INCREASE_ORDER_OF_PREVIOUS );
            daoUtil.setInt( nIndex, actualite.getOrdre( ) );
            daoUtil.executeUpdate( );
            daoUtil.close( );

            updateVersionActualite();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateActualiteOrdre( Actualite actualite )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_ACTUALITE_ORDRE );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, actualite.getOrdre( ) );
        daoUtil.setLong( nIndex, actualite.getId( ) );
        daoUtil.executeUpdate( );
        daoUtil.close( );

        updateVersionActualite();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseOrdreOfAllNext( int nIdActualite )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INCREASE_ORDER_OF_ALL_NEXT );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, nIdActualite );
        daoUtil.setLong( nIndex, nIdActualite );
        daoUtil.executeUpdate( );
        daoUtil.close( );

        updateVersionActualite();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreaseOrdreOfAllNext( int nIdActualite )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DECREASE_ORDER_OF_ALL_NEXT );
        int nIndex = 1;
        daoUtil.setLong( nIndex, nIdActualite );
        daoUtil.executeUpdate( );
        daoUtil.close( );

        updateVersionActualite();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getActualiteCount( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GET_ACTUALITE_COUNT );
        daoUtil.executeQuery( );
        int actualiteCount = 0;
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            actualiteCount = daoUtil.getInt( nIndex );

        }
        daoUtil.close( );
        return actualiteCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageResource getImageResource( int nIdActualite )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_IMAGE_BY_PRIMARY_KEY );
        daoUtil.setInt( 1, nIdActualite );
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

    /**
     * Update version actualite.
     */
    private void updateVersionActualite( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_VERSION_ACTUALITE );
        daoUtil.executeUpdate( );
        daoUtil.close( );
    }

    @Override
    public int getVersionActualite( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_VERSION_ACTUALITE );
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

}
