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
package fr.paris.lutece.plugins.dansmarue.service.deamon;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.dansmarue.service.IPhotoService;
import fr.paris.lutece.plugins.dansmarue.utils.StockagePhotoUtils;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 *
 *Demon de migration des photos de la BDD vers serveur Netapp.
 *
 */
public class MigrationPhotosDaemon extends Daemon
{
    private IPhotoService _photoService = SpringContextService.getBean( "photoService" );

    private StockagePhotoUtils _stockagePhotoUtils =  SpringContextService.getBean( "signalement.stockagePhotoUtils" );

    /**
     * The constant SITELABELS_SITE_PROPERTY_MIGRATION_PHOTOS_NB_PHOTO_TO_MIGRATE.
     */
    public static final String SITELABELS_SITE_PROPERTY_MIGRATION_PHOTOS_NB_PHOTO_TO_MIGRATE = "sitelabels.site_property.migration.photos.nb_photo_to_migrate";

    /** The Constant PROPERTY_GENERATED_SQL_DIRECTORY. */
    private static final String PROPERTY_GENERATED_SQL_DIRECTORY = "migration.daemon.generatedSql.directory";

    private static final int MAX_SIZE_BLOCK = 10;

    private static final String ERROR= "Erreur MigrationPhotosDaemon";

    @Override
    public void run( )
    {
        AppLogService.info( "Debut du demon migration des photos" );

        String strNbPhotoToMigrate = DatastoreService.getDataValue ( SITELABELS_SITE_PROPERTY_MIGRATION_PHOTOS_NB_PHOTO_TO_MIGRATE, "1" );
        int nNbPhotoToMigrate = MAX_SIZE_BLOCK;

        try {
            nNbPhotoToMigrate = Integer.parseInt( strNbPhotoToMigrate );
        } catch(NumberFormatException e) {
            AppLogService.error( "Erreur à la récupération du paramètre du nombre de photo à migrer ", e );
        }
        int nbPhotoMigrate = 0;

        List<String> sqlCommands = new ArrayList<>( );

        while (nNbPhotoToMigrate > 0) {

            List<PhotoDMR> listPhotosToMigrate = _photoService.findPhotosToMigrate(nNbPhotoToMigrate < MAX_SIZE_BLOCK  ? nNbPhotoToMigrate : MAX_SIZE_BLOCK );

            for ( PhotoDMR photoToMigrate : listPhotosToMigrate )
            {
                if(_stockagePhotoUtils.savePhotoOnNetAppServeur( photoToMigrate )) {
                    _photoService.removePhotoFromDatabase( photoToMigrate );
                    sqlCommands.add( "UPDATE ramen_demande SET lien_photo = '" + photoToMigrate.getImageUrl( ) + "' WHERE id_photo = " + photoToMigrate.getId( ) + ";" );
                    sqlCommands.add( "UPDATE ramen_demande SET lien_photo_2 = '"+ photoToMigrate.getImageUrl( ) + "' WHERE id_photo_2 = " + photoToMigrate.getId( ) + ";" );
                    nbPhotoMigrate++;
                } else {
                    photoToMigrate.setCheminPhoto( ERROR );
                    photoToMigrate.setCheminPhotoMiniature( ERROR );
                    _photoService.update( photoToMigrate );
                    AppLogService.error( ERROR +" photoId "+ photoToMigrate.getId( )    );
                }

            }

            nNbPhotoToMigrate = nNbPhotoToMigrate - MAX_SIZE_BLOCK;
        }

        try {
            String generatedSqlPath = AppPropertiesService.getProperty(PROPERTY_GENERATED_SQL_DIRECTORY);
            Files.createDirectories( Paths.get(generatedSqlPath) );
            Path sqlFilePath = Paths.get( generatedSqlPath + "migration_photos_update_liens_ramen_" + LocalDate.now().format( DateTimeFormatter.BASIC_ISO_DATE ) + ".sql" );
            Files.write( sqlFilePath, sqlCommands, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND );
        } catch ( IOException e ) {
            AppLogService.error( "Erreur à l'écriture du script SQL pour la mise à jour des liens photos Ramen", e );
        }

        AppLogService.info( "Fin du demon migration des photos " + nbPhotoMigrate + " phtotos de migrees" );
    }
}
