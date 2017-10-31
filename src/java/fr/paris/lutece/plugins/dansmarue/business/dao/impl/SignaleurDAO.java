package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import fr.paris.lutece.plugins.dansmarue.business.dao.ISignaleurDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.dansmarue.business.entities.Signaleur;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


public class SignaleurDAO implements ISignaleurDAO
{

    private static final String SQL_QUERY_NEW_PK = "SELECT nextval('seq_signalement_signaleur_id_signaleur')";
    private static final String SQL_QUERY_INSERT = "INSERT INTO signalement_signaleur(id_signaleur, mail, id_telephone, fk_id_signalement, guid) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM signalement_signaleur WHERE id_signaleur = ?";
    private static final String SQL_QUERY_SELECT = "SELECT id_signaleur, mail, fk_id_signalement, guid, id_telephone FROM signalement_signaleur WHERE id_signaleur = ?";
    private static final String SQL_QUERY_SELECT_BY_SIGNALEMENT = "SELECT id_signaleur, mail, fk_id_signalement, guid, id_telephone FROM signalement_signaleur WHERE fk_id_signalement = ?";
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_signaleur SET id_signaleur=?, mail = ?, id_telephone=?, fk_id_signalement=?, guid=? WHERE id_signaleur=?";
    private static final String SQL_QUERY_SELECT_LIST_SIGNALEURS = "SELECT id_signaleur, mail, fk_id_signalement, guid, id_telephone FROM signalement_signaleur WHERE fk_id_signalement = ?";

    /**
     * Generates a new primary key
     * 
     * @param plugin the plugin
     * @return The new primary key
     */
    private Long newPrimaryKey( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery( );
        Long nKey = null;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getLong( 1 );
        }
        daoUtil.free( );
        return nKey;
    }

    /**
     * Upadate a signaleur
     * 
     * @param signaleur the signaleur to update
     */
    public void update( Signaleur signaleur )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, signaleur.getId( ) );
        daoUtil.setString( nIndex++, signaleur.getMail( ) );
        daoUtil.setString( nIndex++, signaleur.getIdTelephone( ) );
        
        daoUtil.setLong( nIndex++, signaleur.getSignalement( ).getId( ) );
        daoUtil.setString(nIndex++, signaleur.getGuid());
        
        daoUtil.setLong( nIndex++, signaleur.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Save a new signaleur
     * 
     */
    public Long insert( Signaleur signaleur )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        if ( signaleur.getId( ) == null || signaleur.getId( ) == 0 )
        {
            signaleur.setId( newPrimaryKey( ) );
        }
        int nIndex = 1;
        daoUtil.setLong( nIndex++, signaleur.getId( ) );
        daoUtil.setString( nIndex++, signaleur.getMail( ) );
        daoUtil.setString( nIndex++, signaleur.getIdTelephone( ) );
        daoUtil.setLong( nIndex++, signaleur.getSignalement( ).getId( ) );
        daoUtil.setString( nIndex++, signaleur.getGuid());

        daoUtil.executeUpdate( );
        daoUtil.free( );

        return signaleur.getId( );
    }

    /**
     * Delete an signaleur
     * 
     * @param lId the signaleur id
     */
    public void remove( long lId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setLong( 1, lId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Load a signaleur
     * 
     * @param lId the signaleur id
     */
    public Signaleur load( long lId )
    {
        Signaleur signaleur = new Signaleur( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setLong( 1, lId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            signaleur.setId( daoUtil.getLong( nIndex++ ) );
            signaleur.setMail( daoUtil.getString( nIndex++ ) );
            Signalement signalement = new Signalement( );
            signalement.setId( daoUtil.getLong( nIndex++ ) );
            signaleur.setGuid(daoUtil.getString(nIndex++));
            signaleur.setIdTelephone(daoUtil.getString(nIndex++));
            signaleur.setSignalement( signalement );
        }

        daoUtil.free( );

        return signaleur;
    }

    /**
     * Load a signaleur by the signalement id
     * 
     * @param lId the signaleur id
     */
    public Signaleur loadByIdSignalement( long lId )
    {
        Signaleur signaleur = new Signaleur( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_SIGNALEMENT );
        daoUtil.setLong( 1, lId );
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            signaleur.setId( daoUtil.getLong( nIndex++ ) );
            signaleur.setMail( daoUtil.getString( nIndex++ ) );
            
            Signalement signalement = new Signalement( );

            signalement.setId(  daoUtil.getLong( nIndex++ ) );
            
            signaleur.setSignalement( signalement );
            
            signaleur.setGuid( daoUtil.getString(nIndex++));
            signaleur.setIdTelephone(daoUtil.getString(nIndex++));

        }

        daoUtil.free( );

        return signaleur;
    }

    /**
     * Store a signaleur
     * 
     * @param signaleur the signaleur object
     */
    public void store( Signaleur signaleur )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nIndex = 1;
        daoUtil.setLong( nIndex++, signaleur.getId( ) );
        daoUtil.setString( nIndex++, signaleur.getMail( ) );
        daoUtil.setString( nIndex++, signaleur.getIdTelephone( ) );
        daoUtil.setLong( nIndex++, signaleur.getSignalement( ).getId( ) );
        daoUtil.setString(nIndex++, signaleur.getGuid());
        //WHERE
        daoUtil.setLong( nIndex++, signaleur.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Find signaleurs for a Signalement id
     * @param lIdSignalement the signalement id
     * @return list of signaleurs
     * 
     */
    public List<Signaleur> findBySignalementId( long lIdSignalement )
    {
        List<Signaleur> result = new ArrayList<Signaleur>( );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LIST_SIGNALEURS );
        daoUtil.setLong( 1, lIdSignalement );

        daoUtil.executeQuery( );

        // Pour chaque resultat retourne
        while ( daoUtil.next( ) )
        {
            Signaleur signaleur = new Signaleur( );
            int nIndex = 1;
            signaleur.setId( daoUtil.getLong( nIndex++ ) );
            signaleur.setMail( daoUtil.getString( nIndex++ ) );
            Signalement signalement = new Signalement( );
            signalement.setId( daoUtil.getLong( nIndex++ ) );
            signaleur.setSignalement( signalement );
            signaleur.setGuid(daoUtil.getString(nIndex++));
            signaleur.setIdTelephone(daoUtil.getString(nIndex++));
            result.add( signaleur );
        }

        daoUtil.free( );

        return result;
    }
}
