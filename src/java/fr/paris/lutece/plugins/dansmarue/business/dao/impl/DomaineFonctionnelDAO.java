package fr.paris.lutece.plugins.dansmarue.business.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import fr.paris.lutece.plugins.dansmarue.business.dao.IDomaineFonctionnelDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.DomaineFonctionnel;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

public class DomaineFonctionnelDAO implements IDomaineFonctionnelDAO{

	private static final String SQL_QUERY_INSERT_DOMAINE = "INSERT INTO signalement_domaine_fonctionnel(id_domaine_fonctionnel,libelle,actif) VALUES (?,?,?)";
	private static final String SQL_QUERY_DELETE_DOMAINE = "DELETE FROM signalement_domaine_fonctionnel WHERE id_domaine_fonctionnel=?";
    private static final String SQL_QUERY_UPDATE_DOMAINE = "UPDATE signalement_domaine_fonctionnel SET libelle=?, actif=? WHERE id_domaine_fonctionnel = ?";
    private static final String SQL_QUERY_SELECT_DOMAINE = "SELECT id_domaine_fonctionnel, libelle, actif FROM signalement_domaine_fonctionnel WHERE id_domaine_fonctionnel=?";
    private static final String SQL_QUERY_SELECT_ALL_DOMAINES = "SELECT id_domaine_fonctionnel, libelle, actif FROM signalement_domaine_fonctionnel";
    private static final String SQL_QUERY_SELECT_ALL_DOMAINES_ACTIFS = "SELECT id_domaine_fonctionnel, libelle, actif FROM signalement_domaine_fonctionnel WHERE actif=1";
    private static final String SQL_QUERY_NEW_PK = " SELECT nextval('seq_signalement_domaine_fonctionnel')";
    
    private static final String SQL_QUERY_DELETE_FROM_ROLE_RESOURCE = "DELETE FROM core_admin_role_resource WHERE resource_type=? AND resource_id=?";
    
    // Link with arrondissement 0..N
    private static final String SQL_QUERY_INSERT_DOMAINE_ARRONDISSEMENT = "INSERT INTO signalement_domaine_arrondissement (fk_id_domaine_fonctionnel,fk_id_arrondissement) VALUES (?,?)";
    private static final String SQL_QUERY_DELETE_ARRONDISSEMENT_BY_DOMAINE = "DELETE FROM signalement_domaine_arrondissement WHERE fk_id_domaine_fonctionnel = ?";
    private static final String SQL_QUERY_SELECT_ARRONDISSEMENTS_BY_DOMAINE = "SELECT fk_id_arrondissement FROM signalement_domaine_arrondissement WHERE fk_id_domaine_fonctionnel=?";
    
    // Quartier
    private static final String SQL_QUERY_INSERT_QUARTIER_ARRONDISSEMENT = "INSERT INTO signalement_domaine_conseil_quartier (fk_id_domaine_fonctionnel,fk_id_quartier) VALUES (?,?)";
    private static final String SQL_QUERY_SELECT_QUARTIERS_BY_DOMAINE = "SELECT fk_id_quartier FROM signalement_domaine_conseil_quartier WHERE fk_id_domaine_fonctionnel=?";
    private static final String SQL_QUERY_DELETE_QUARTIER_BY_DOMAINE = "DELETE FROM signalement_domaine_conseil_quartier WHERE fk_id_domaine_fonctionnel = ?";
    
    // Link with type signalement 0..N
    private static final String SQL_QUERY_INSERT_DOMAINE_TYPE_SIGNALEMENT = "INSERT INTO signalement_domaine_type_signalement (fk_id_domaine_fonctionnel,fk_id_type_signalement) VALUES (?,?)";
    private static final String SQL_QUERY_DELETE_TYPE_SIGNALEMENT_BY_DOMAINE = "DELETE FROM signalement_domaine_type_signalement WHERE fk_id_domaine_fonctionnel = ?";
    private static final String SQL_QUERY_SELECT_TYPES_SIGNALEMENT_BY_DOMAINE = "SELECT fk_id_type_signalement FROM signalement_domaine_type_signalement WHERE fk_id_domaine_fonctionnel=?";
    
    // Link with type signalement 0..N
    private static final String SQL_QUERY_INSERT_DOMAINE_UNIT = "INSERT INTO signalement_domaine_unit (fk_id_domaine_fonctionnel,fk_id_unit) VALUES (?,?)";
    private static final String SQL_QUERY_DELETE_UNIT_BY_DOMAINE = "DELETE FROM signalement_domaine_unit WHERE fk_id_domaine_fonctionnel = ?";
    private static final String SQL_QUERY_SELECT_UNIT_BY_DOMAINE = "SELECT fk_id_unit FROM signalement_domaine_unit WHERE fk_id_domaine_fonctionnel=?";
    
    
	@Override
	public synchronized Integer insert(DomaineFonctionnel domaineFonctionnel, Plugin plugin) {
		Integer idDomaineFonctionnel = newPrimaryKey( plugin );
		domaineFonctionnel.setId(idDomaineFonctionnel);
		
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_DOMAINE );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, domaineFonctionnel.getId( ) );
        daoUtil.setString( nIndex++, domaineFonctionnel.getLibelle( ) );
        daoUtil.setBoolean(nIndex++, domaineFonctionnel.isActif());
        daoUtil.executeUpdate( );
        daoUtil.free( );
        
        //Inserts arrondissements
        List<Integer> arrondissementsIds = domaineFonctionnel.getArrondissementsIds();
        if(CollectionUtils.isNotEmpty(arrondissementsIds)){
        	for(Integer idArrondissement:arrondissementsIds){
	        	daoUtil = new DAOUtil(SQL_QUERY_INSERT_DOMAINE_ARRONDISSEMENT);
	        	nIndex = 1;
	        	daoUtil.setInt(nIndex++, idDomaineFonctionnel);
	        	daoUtil.setInt(nIndex++, idArrondissement);
	        	daoUtil.executeUpdate( );
	            daoUtil.free( );
        	}
        }
        
        //Inserts quartier
        List<Integer> quartiersIds = domaineFonctionnel.getQuartiersIds( );
        if(CollectionUtils.isNotEmpty(quartiersIds)){
            for(Integer idQuartier:quartiersIds){
                daoUtil = new DAOUtil(SQL_QUERY_INSERT_QUARTIER_ARRONDISSEMENT);
                nIndex = 1;
                daoUtil.setInt(nIndex++, domaineFonctionnel.getId());
                daoUtil.setInt(nIndex++, idQuartier);
                daoUtil.executeUpdate( );
                daoUtil.free( );
            }
        }
        
        //Inserts types
        List<Integer> typesIds = domaineFonctionnel.getTypesSignalementIds();
        if(CollectionUtils.isNotEmpty(typesIds)){
        	for(Integer idType:typesIds){
	        	daoUtil = new DAOUtil(SQL_QUERY_INSERT_DOMAINE_TYPE_SIGNALEMENT);
	        	nIndex = 1;
	        	daoUtil.setInt(nIndex++, idDomaineFonctionnel);
	        	daoUtil.setInt(nIndex++, idType);
	        	daoUtil.executeUpdate( );
	            daoUtil.free( );
        	}
        }
        
        //Inserts types
        List<Integer> unitIds = domaineFonctionnel.getUnitIds();
        if(CollectionUtils.isNotEmpty(unitIds)){
        	for(Integer idUnit:unitIds){
	        	daoUtil = new DAOUtil(SQL_QUERY_INSERT_DOMAINE_UNIT);
	        	nIndex = 1;
	        	daoUtil.setInt(nIndex++, idDomaineFonctionnel);
	        	daoUtil.setInt(nIndex++, idUnit);
	        	daoUtil.executeUpdate( );
	            daoUtil.free( );
        	}
        }
        
        return domaineFonctionnel.getId( );
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery( );
        Integer nKey = null;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 );
        }
        daoUtil.free( );
        return nKey.intValue( );
    }
	
	@Override
	public synchronized void remove(long lId) {
		//Remove arrondissements
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_DELETE_ARRONDISSEMENT_BY_DOMAINE);
		daoUtil.setLong(1, lId);
		daoUtil.executeUpdate( );
        daoUtil.free( );
        
        //Remove quartier
        daoUtil = new DAOUtil(SQL_QUERY_DELETE_QUARTIER_BY_DOMAINE);
        daoUtil.setLong(1, lId);
        daoUtil.executeUpdate( );
        daoUtil.free( );
        
		//Remove types
        daoUtil = new DAOUtil(SQL_QUERY_DELETE_TYPE_SIGNALEMENT_BY_DOMAINE);
		daoUtil.setLong(1, lId);
		daoUtil.executeUpdate( );
        daoUtil.free( );
        
        //Remove units
        daoUtil = new DAOUtil(SQL_QUERY_DELETE_UNIT_BY_DOMAINE);
        daoUtil.setLong(1,  lId);
		daoUtil.executeUpdate( );
        daoUtil.free( );
        
        //Remove from role resource
        daoUtil = new DAOUtil(SQL_QUERY_DELETE_FROM_ROLE_RESOURCE);
        daoUtil.setString(1,  DomaineFonctionnel.RESOURCE_TYPE);
        daoUtil.setString(2, Long.toString(lId));
        daoUtil.executeUpdate();
        daoUtil.free();
        
        //Finally Remove the domaine
		daoUtil = new DAOUtil( SQL_QUERY_DELETE_DOMAINE );
        daoUtil.setLong( 1, lId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
	}

	@Override
	public DomaineFonctionnel getById(Integer lId) {
		DomaineFonctionnel domaineFonctionnel = null;
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_DOMAINE);
		int nIndex = 1;
		daoUtil.setLong(nIndex++, lId);
		daoUtil.executeQuery();
		
		if(daoUtil.next()){
			nIndex = 1;
			domaineFonctionnel = new DomaineFonctionnel();
			domaineFonctionnel.setId(daoUtil.getInt(nIndex++));
			domaineFonctionnel.setLibelle(daoUtil.getString(nIndex++));
			domaineFonctionnel.setActif(daoUtil.getBoolean(nIndex++));
		}
		daoUtil.free();
		return domaineFonctionnel;
	}

	@Override
	public void store(DomaineFonctionnel domaineFonctionnel) {
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_DOMAINE );
        int nIndex = 1;
        daoUtil.setString( nIndex++, domaineFonctionnel.getLibelle( ) );
        daoUtil.setBoolean(nIndex++, domaineFonctionnel.isActif());
        
        //WHERE
        daoUtil.setLong( nIndex++, domaineFonctionnel.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
        
        //Remove arrondissements
  		daoUtil = new DAOUtil(SQL_QUERY_DELETE_ARRONDISSEMENT_BY_DOMAINE);
  		daoUtil.setLong(1, domaineFonctionnel.getId());
  		daoUtil.executeUpdate( );
        daoUtil.free( );
        
        //Remove quartier
        daoUtil = new DAOUtil(SQL_QUERY_DELETE_QUARTIER_BY_DOMAINE);
        daoUtil.setLong(1, domaineFonctionnel.getId());
        daoUtil.executeUpdate( );
        daoUtil.free( );
          
  		//Remove types
        daoUtil = new DAOUtil(SQL_QUERY_DELETE_TYPE_SIGNALEMENT_BY_DOMAINE);
  		daoUtil.setLong(1, domaineFonctionnel.getId());
  		daoUtil.executeUpdate( );
        daoUtil.free( );
        
        //Remove units
        daoUtil = new DAOUtil(SQL_QUERY_DELETE_UNIT_BY_DOMAINE);
  		daoUtil.setLong(1, domaineFonctionnel.getId());
  		daoUtil.executeUpdate( );
        daoUtil.free( );
        
        //Inserts arrondissements
        List<Integer> arrondissementsIds = domaineFonctionnel.getArrondissementsIds();
        if(CollectionUtils.isNotEmpty(arrondissementsIds)){
        	for(Integer idArrondissement:arrondissementsIds){
	        	daoUtil = new DAOUtil(SQL_QUERY_INSERT_DOMAINE_ARRONDISSEMENT);
	        	nIndex = 1;
	        	daoUtil.setInt(nIndex++, domaineFonctionnel.getId());
	        	daoUtil.setInt(nIndex++, idArrondissement);
	        	daoUtil.executeUpdate( );
	            daoUtil.free( );
        	}
        }
        
        //Inserts quartier
        List<Integer> quartiersIds = domaineFonctionnel.getQuartiersIds( );
        if(CollectionUtils.isNotEmpty(quartiersIds)){
            for(Integer idQuartier:quartiersIds){
                daoUtil = new DAOUtil(SQL_QUERY_INSERT_QUARTIER_ARRONDISSEMENT);
                nIndex = 1;
                daoUtil.setInt(nIndex++, domaineFonctionnel.getId());
                daoUtil.setInt(nIndex++, idQuartier);
                daoUtil.executeUpdate( );
                daoUtil.free( );
            }
        }
        
        //Inserts types
        List<Integer> typesIds = domaineFonctionnel.getTypesSignalementIds();
        if(CollectionUtils.isNotEmpty(typesIds)){
        	for(Integer idType:typesIds){
	        	daoUtil = new DAOUtil(SQL_QUERY_INSERT_DOMAINE_TYPE_SIGNALEMENT);
	        	nIndex = 1;
	        	daoUtil.setInt(nIndex++, domaineFonctionnel.getId());
	        	daoUtil.setInt(nIndex++, idType);
	        	daoUtil.executeUpdate( );
	            daoUtil.free( );
        	}
        }
        
        //Inserts units
        List<Integer> unitsIds = domaineFonctionnel.getUnitIds();
        if(CollectionUtils.isNotEmpty(unitsIds)){
        	for(Integer idUnit:unitsIds){
	        	daoUtil = new DAOUtil(SQL_QUERY_INSERT_DOMAINE_UNIT);
	        	nIndex = 1;
	        	daoUtil.setInt(nIndex++, domaineFonctionnel.getId());
	        	daoUtil.setInt(nIndex++, idUnit);
	        	daoUtil.executeUpdate( );
	            daoUtil.free( );
        	}
        }
        
	}

	@Override
	public List<DomaineFonctionnel> getAllDomainesFonctionnel() {
		DomaineFonctionnel domaineFonctionnel = null;
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_ALL_DOMAINES);
		int nIndex = 1;
		daoUtil.executeQuery();
		
		List<DomaineFonctionnel> domainesFonctionnels = new ArrayList<DomaineFonctionnel>();
		while(daoUtil.next()){
			domaineFonctionnel = new DomaineFonctionnel();
			nIndex = 1;
			domaineFonctionnel.setId(daoUtil.getInt(nIndex++));
			domaineFonctionnel.setLibelle(daoUtil.getString(nIndex++));
			domaineFonctionnel.setActif(daoUtil.getBoolean(nIndex++));
			domainesFonctionnels.add(domaineFonctionnel);
		}
		daoUtil.free();
		return domainesFonctionnels;
	}
	
	@Override
	public List<DomaineFonctionnel> getAllDomainesFonctionnelActifs() {
		DomaineFonctionnel domaineFonctionnel = null;
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_ALL_DOMAINES_ACTIFS);
		int nIndex = 1;
		daoUtil.executeQuery();
		
		List<DomaineFonctionnel> domainesFonctionnels = new ArrayList<DomaineFonctionnel>();
		while(daoUtil.next()){
			nIndex = 1;
			domaineFonctionnel = new DomaineFonctionnel();
			domaineFonctionnel.setId(daoUtil.getInt(nIndex++));
			domaineFonctionnel.setLibelle(daoUtil.getString(nIndex++));
			domaineFonctionnel.setActif(daoUtil.getBoolean(nIndex++));
			domainesFonctionnels.add(domaineFonctionnel);
		}
		daoUtil.free();
		return domainesFonctionnels;
	}
	
	@Override
	public List<Integer> getArrondissementsIdsByDomaineId(int idDomaine){
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_ARRONDISSEMENTS_BY_DOMAINE);
		int nIndex=1;
		daoUtil.setInt(nIndex++, idDomaine);
		daoUtil.executeQuery();
		
		List<Integer> arrondissementsIds = new ArrayList<Integer>();
		while(daoUtil.next()){
			arrondissementsIds.add(daoUtil.getInt(1));
		}
		
		daoUtil.free();
		return arrondissementsIds;
	}
	
	@Override
    public List<Integer> getQuartiersIdsByDomaineId(int idDomaine){
        DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_QUARTIERS_BY_DOMAINE);
        int nIndex=1;
        daoUtil.setInt(nIndex++, idDomaine);
        daoUtil.executeQuery();
        
        List<Integer> quartiersIds = new ArrayList<Integer>();
        while(daoUtil.next()){
            quartiersIds.add(daoUtil.getInt(1));
        }
        
        daoUtil.free();
        return quartiersIds;
    }
	
	@Override
	public List<Integer> getTypesSignalementIdsByDomaineId(int idDomaine){
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_TYPES_SIGNALEMENT_BY_DOMAINE);
		int nIndex=1;
		daoUtil.setInt(nIndex++, idDomaine);
		daoUtil.executeQuery();
		
		List<Integer> typeSignalementIds = new ArrayList<Integer>();
		while(daoUtil.next()){
			typeSignalementIds.add(daoUtil.getInt(1));
		}
		
		daoUtil.free();
		return typeSignalementIds;
	}
	
	@Override
	public List<Integer> getUnitsIdsByDomaineId(int idDomaine){
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_UNIT_BY_DOMAINE);
		int nIndex=1;
		daoUtil.setInt(nIndex++, idDomaine);
		daoUtil.executeQuery();
		
		List<Integer> unitsIds = new ArrayList<Integer>();
		while(daoUtil.next()){
			unitsIds.add(daoUtil.getInt(1));
		}
		
		daoUtil.free();
		return unitsIds;
	}
}
