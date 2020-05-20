/*
 * Copyright (c) 2002-2020, City of Paris
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import fr.paris.lutece.plugins.dansmarue.business.dao.IAdresseDAO;
import fr.paris.lutece.plugins.dansmarue.business.dao.IArrondissementDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.dansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.dansmarue.service.IAdresseService;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.business.sector.ISectorDAO;
import fr.paris.lutece.plugins.unittree.modules.dansmarue.business.sector.Sector;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

/**
 * The Class AdresseService.
 */
public class AdresseService implements IAdresseService
{

    /** The adresse signalement DAO. */
    @Inject
    @Named( "signalementAdresseDAO" )
    private IAdresseDAO         _adresseSignalementDAO;

    /** The arrondissement DAO. */
    @Inject
    @Named( "signalement.arrondissementDAO" )
    private IArrondissementDAO  _arrondissementDAO;

    /** The sector DAO. */
    @Inject
    @Named( "unittree-dansmarue.sectorDAO" )
    private ISectorDAO          _sectorDAO;

    /** The Constant URL_REVERSE_GEOCODING. */
    private static final String URL_REVERSE_GEOCODING           = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&email=xxxx.xxxx@accenture.com&&addressdetails=1&zoom=18";

    /** The Constant URL_REVERSE_GEOCODING_STORE_ADR. */
    private static final String URL_REVERSE_GEOCODING_STORE_ADR = "signalement.storeAdr.url";

    /**
     * {@inheritDoc}
     */
    @Override
    public Long insert( Adresse adresse )
    {
        return _adresseSignalementDAO.insert( adresse );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( long lId )
    {
        _adresseSignalementDAO.remove( lId );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Adresse load( long lId )
    {
        return _adresseSignalementDAO.load( lId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( Adresse adresse )
    {
        _adresseSignalementDAO.store( adresse );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Adresse loadByIdSignalement( long lId )
    {
        return _adresseSignalementDAO.loadByIdSignalement( lId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update( Adresse adresse )
    {
        _adresseSignalementDAO.update( adresse );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAdresse( Adresse adresse )
    {
        _adresseSignalementDAO.updateAdresse( adresse );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Arrondissement getArrondissementByGeom( Double lng, Double lat )
    {
        return _arrondissementDAO.getArrondissementByGeom( lng, lat );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Sector getSecteurByGeomAndTypeSignalement( Double lng, Double lat, Integer idTypeSignalement )
    {
        return _sectorDAO.getSectorByGeomAndTypeSignalement( lng, lat, idTypeSignalement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Sector getSectorByGeomAndIdUnitParent( Double lng, Double lat, Integer idUnitParent )
    {
        return _sectorDAO.getSectorByGeomAndIdUnitParent( lng, lat, idUnitParent );
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.dansmarue.service.IAdresseService#findWrongAdresses()
     */
    @Override
    public List<Adresse> findWrongAdresses( )
    {
        return _adresseSignalementDAO.findWrongAdresses( );
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.dansmarue.service.IAdresseService#getAdresseByPosition(fr.paris.lutece.plugins.dansmarue.business.entities.Adresse)
     */
    @Override
    public String getAdresseByPosition( Adresse adresse )
    {
        try
        {
            // Appel à storeAdr pour récupérer l'adresse
            String adresseCorrigee = getAdresseFromStoreAdr( adresse.getLat( ), adresse.getLng( ) );

            if ( adresseCorrigee.isEmpty( ) )
            {
                // Si pas de résultat, appel à openstreetmap
                adresse = setCoordonateLambert93ToWSG84( adresse );
                adresseCorrigee = getAdresseFromOpenStreetMap( adresse.getLat( ), adresse.getLng( ) );
            }

            AppLogService.info( "Correction de l'adresse " + adresse.getId( ) + ": OK" );
            AppLogService.info( "Adresse initiale : " + adresse.getAdresse( ) );
            AppLogService.info( "Adresse mise à jour : " + adresseCorrigee );

            return adresseCorrigee;

        }
        catch ( Exception e )
        {
            AppLogService.error( "Erreur lors de la récupération de l'adresse via WS : ", e );
            AppLogService.info( "Correction de l'adresse " + adresse.getId( ) + " KO" );
            return adresse.getAdresse( );
        }
    }

    /**
     * Gets the adresse from store adr.
     *
     * @param lat the lat
     * @param lng the lng
     * @return the adresse from store adr
     * @throws HttpAccessException the http access exception
     */
    private String getAdresseFromStoreAdr( Double lat, Double lng ) throws HttpAccessException
    {
        HttpAccess http = new HttpAccess( );
        String answer = http.doGet( AppPropertiesService.getProperty( URL_REVERSE_GEOCODING_STORE_ADR ) + "StoreAdr/rest/AdressesPostales/R59/xy/(" + lng + "," + lat + ",5)" );

        Map<String, ArrayList> answerMap = new Gson( ).fromJson( answer, Map.class );

        String result = "";
        if ( answerMap.containsKey( "Features" ) )
        {
            JSONArray jsonArr = new JSONArray( answerMap.get( "Features" ) );
            if ( ( jsonArr.length( ) > 0 ) && jsonArr.getJSONObject( 0 ).has( "properties" ) )
            {
                JSONObject jsonObject = jsonArr.getJSONObject( 0 ).getJSONObject( "properties" );
                if ( jsonObject.has( "Adressetypo" ) )
                {
                    result = jsonObject.get( "Adressetypo" ).toString( );
                }
            }
        }
        return result;

    }

    /**
     * Gets the adresse from open street map.
     *
     * @param lat the lat
     * @param lng the lng
     * @return the adresse from open street map
     * @throws HttpAccessException the http access exception
     */
    private String getAdresseFromOpenStreetMap( Double lat, Double lng ) throws HttpAccessException
    {
        HttpAccess http = new HttpAccess( );
        // Pour visualiser le retour du WS, mettre le parametre format=html (dans un navigateur)
        String answer = http.doGet( URL_REVERSE_GEOCODING + "&lat=" + lat + "&lon=" + lng );

        Map<String, String> answerMap = new Gson( ).fromJson( answer, Map.class );

        JSONObject answerJson = new JSONObject( new JSONObject( answerMap ).get( "address" ).toString( ) );

        String numeroRue = answerJson.has( "house_number" ) ? answerJson.get( "house_number" ).toString( ) : "";
        String rue = answerJson.has( "road" ) ? answerJson.get( "road" ).toString( ) : answerJson.get( "suburb" ).toString( );
        String codePostal = answerJson.get( "postcode" ).toString( );
        return numeroRue + " " + rue + ", " + codePostal + " PARIS";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fixAdresses( )
    {
        // adresses avec E-Arrondissement
        _adresseSignalementDAO.fixSyntaxeArrondissement( );
        // adresses en Parigi pour la ville
        _adresseSignalementDAO.fixVille( );
        // adresses qui n'ont pas de virgule avant le code postale
        _adresseSignalementDAO.fixVirguleCP( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Adresse setCoordonateLambert93ToWSG84( Adresse adresse )
    {
        return _adresseSignalementDAO.setCoordonateLambert93ToWSG84( adresse );
    }

}
