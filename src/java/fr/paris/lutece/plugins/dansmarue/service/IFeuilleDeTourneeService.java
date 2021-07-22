/*
 * Copyright (c) 2002-2021, City of Paris
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
package fr.paris.lutece.plugins.dansmarue.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.paris.lutece.plugins.dansmarue.business.entities.FeuilleDeTournee;
import fr.paris.lutece.plugins.dansmarue.business.entities.FeuilleDeTourneeFilter;
import fr.paris.lutece.plugins.dansmarue.business.entities.FeuilleDeTourneeFilterSearch;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementBean;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementFilter;
import fr.paris.lutece.plugins.dansmarue.service.dto.SignalementExportCSVDTO;
import fr.paris.lutece.plugins.dansmarue.service.dto.SignalementMapMarkerDTO;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.portal.business.user.AdminUser;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;

/**
 * The Interface IFeuilleDeTourneeService.
 */
public interface IFeuilleDeTourneeService
{
    /**
     * Load.
     *
     * @param id
     *            the id
     * @return the feuille de tournee
     */
    FeuilleDeTournee load( Integer id );

    /**
     * Insert.
     *
     * @param feuilleDeTournee
     *            the feuille de tournee
     * @return the integer
     */
    Integer insert( FeuilleDeTournee feuilleDeTournee );

    /**
     * Save a search filter.
     *
     * @param creator
     *            search filter creator
     * @param name
     *            search filter name
     * @param comment
     *            search filter comment
     * @param jsonFiltertoSave
     *            search filter value
     * @param idUnit
     *            creator id unit
     * @param isUpdate
     *            is an update
     */
    void saveFilterSearch( String creator, String name, String comment, String jsonFiltertoSave, int idUnit, boolean isUpdate );

    /**
     * Load search filter.
     *
     * @param idUnit
     *            id unit to search
     * @return search filter found
     */
    Map<Integer, String> loadSearchFilter( int idUnit );

    /**
     * Delete search filter.
     *
     * @param idFilterToDelete
     *            id filter
     */
    void deleteFilterSearch( int idFilterToDelete );

    /**
     * Find Filter search.
     * 
     * @param nameToSearch
     *            name of filter
     * @return FilterSearch find
     */
    FeuilleDeTourneeFilterSearch findFilterSearch( String nameToSearch );

    /**
     * Load search filter.
     *
     * @param idToLoad
     *            id to load
     * @return filter value
     */
    String loadSearchFilterById( int idToLoad );

    /**
     * Load signalement bean list.
     *
     * @param idFeuilleDeTournee
     *            the id feuille de tournee
     * @return the list
     */
    List<SignalementBean> loadSignalementBeanList( int idFeuilleDeTournee );

    /**
     * Gets the destinataires from param.
     *
     * @param parameter
     *            the parameter
     * @return the destinataires from param
     */
    List<String> getDestinatairesFromParam( String parameter );

    /**
     * Generate template data source.
     *
     * @param feuilleDeTourneeId
     *            the feuille de tournee id
     * @param exportWithMap
     *            display map in jasper report
     * @param base64map
     *            map
     * @return the JR data source
     */
    JRDataSource generateTemplateDataSource( Integer feuilleDeTourneeId, boolean exportWithMap, String base64map );

    /**
     * Generate template bytes data.
     *
     * @param dataSource
     *            the data source
     * @param pathToTemplate
     *            the path to template
     * @return the byte[]
     * @throws JRException
     *             the JR exception
     */
    byte [ ] generateTemplateBytesData( JRDataSource dataSource, String pathToTemplate ) throws JRException;

    /**
     * Send feuille de tournee by mail.
     *
     * @param destinairesList
     *            the destinaires list
     * @param bytesData
     *            the bytes data
     * @param nomExport
     *            export file name
     */
    void sendFeuilleDeTourneeByMail( List<String> destinairesList, byte [ ] bytesData, String nomExport );

    /**
     * Control format email.
     *
     * @param destinairesList
     *            the destinaires list
     * @return true, if successful
     */
    boolean isFormatEmailOK( List<String> destinairesList );

    /**
     * Inits the fdt filter.
     *
     * @param feuilleDeTourneeFilter
     *            the feuille de tournee filter
     * @return the feuille de tournee filter
     */
    FeuilleDeTourneeFilter initFdtFilter( FeuilleDeTourneeFilter feuilleDeTourneeFilter );

    /**
     * Load fdt by filter.
     *
     * @param feuilleDeTourneeFilter
     *            the feuille de tournee filter
     * @return the list
     */
    List<FeuilleDeTournee> loadFdtByFilter( FeuilleDeTourneeFilter feuilleDeTourneeFilter );

    /**
     * Gets the signalements map marker DTO.
     *
     * @param signalementFilter
     *            the signalement filter
     * @param locale
     *            the locale
     * @return the signalements map marker DTO
     */
    List<SignalementMapMarkerDTO> getSignalementsMapMarkerDTO( SignalementFilter signalementFilter, Locale locale );

    /**
     * Gets the noms existant.
     *
     * @return the noms existant
     */
    List<String> getNomsExistant( );

    /**
     * Find FDT by name.
     *
     * @param nomFDT
     *            the nom FDT
     * @return the feuille de tournee
     */
    FeuilleDeTournee findFDTByName( String nomFDT );

    /**
     * Update.
     *
     * @param feuilleDeTournee
     *            the feuille de tournee
     */
    void update( FeuilleDeTournee feuilleDeTournee );

    /**
     * Update by name.
     *
     * @param feuilleDeTournee
     *            the feuille de tournee
     * @return the integer
     */
    Integer updateByName( FeuilleDeTournee feuilleDeTournee );

    /**
     * Gets the signalements map marker DTO from signalements consultation.
     *
     * @param listSignalements
     *            the list signalements
     * @param locale
     *            the locale
     * @return the signalements map marker DTO from signalements consultation
     */
    List<SignalementMapMarkerDTO> getSignalementsMapMarkerDTOFromSignalementsConsultation( List<SignalementExportCSVDTO> listSignalements, Locale locale );

    /**
     * Gets the signalements map marker DTO from signalements edition.
     *
     * @param listSignalements
     *            the list signalements
     * @param locale
     *            the locale
     * @return the signalements map marker DTO from signalements edition
     */
    List<SignalementMapMarkerDTO> getSignalementsMapMarkerDTOFromSignalementsEdition( List<SignalementExportCSVDTO> listSignalements, Locale locale );

    /**
     * Delete.
     *
     * @param nFeuilleDeTourneeId
     *            the n feuille de tournee id
     */
    void delete( int nFeuilleDeTourneeId );

    /**
     * Gets the feuille de tournee autorise.
     *
     * @param feuilleDeTourneeList
     *            the feuille de tournee list
     * @param adminUser
     *            the admin user
     * @return the feuille de tournee autorise
     */
    List<FeuilleDeTournee> getFeuilleDeTourneeAutorise( List<FeuilleDeTournee> feuilleDeTourneeList, AdminUser adminUser );

    /**
     * Gets the all feuille de tournee autorise.
     *
     * @param connectedUser
     *            the connected user
     * @return the all feuille de tournee autorise
     */
    List<FeuilleDeTournee> getAllFeuilleDeTourneeAutorise( AdminUser connectedUser );

    /**
     * Checks if is unit is authorized to user.
     *
     * @param user
     *            the user
     * @param unit
     *            the unit
     * @return true, if is unit is authorized to user
     */
    boolean isUnitAuthorizedToUser( AdminUser user, Unit unit );

    /**
     * Delete old feuille de tournee.
     *
     * @param jourDelaiSuppression
     *            the jour delai suppression
     */
    void deleteOldFeuilleDeTournee( int jourDelaiSuppression );
}
