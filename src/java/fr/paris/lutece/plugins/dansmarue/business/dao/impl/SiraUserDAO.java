/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

import fr.paris.lutece.plugins.dansmarue.business.dao.ISiraUserDAO;
import fr.paris.lutece.plugins.dansmarue.business.entities.SiraUser;
import fr.paris.lutece.util.sql.DAOUtil;

public class SiraUserDAO implements ISiraUserDAO {

	private static final String SQL_QUERY_NEW_PK = "SELECT nextval('seq_sira_user_id')";
    private static final String SQL_QUERY_INSERT = "INSERT INTO sira_user(id_sira_user, user_guid, user_udid, user_device, user_email, user_token) VALUES (?, ?, ?, ?, ?,?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM sira_user WHERE id_sira_user = ?";
    private static final String SQL_QUERY_SELECT = "SELECT id_sira_user, user_guid, user_udid, user_device, user_email, user_token FROM sira_user WHERE id_sira_user = ?";
    private static final String SQL_QUERY_UPDATE = "UPDATE signalement_signaleur SET user_guid=?, user_udid=?, user_device=?, user_email=?, user_token=? WHERE id_sira_user=?";
    private static final String SQL_QUERY_SELECT_BY_GUID_AND_TOKEN = "SELECT id_sira_user, user_guid, user_udid, user_device, user_email, user_token FROM sira_user WHERE user_guid=? AND user_token=?";
	
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
	 * {@inheritDoc}
	 */
	@Override
	public Long insert(SiraUser siraUser) {
		 DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
         if ( siraUser.getId( ) == null || siraUser.getId( ) == 0 )
         {
        	siraUser.setId( newPrimaryKey( ) );
         }
         int nIndex = 1;
         
         daoUtil.setLong(nIndex++,siraUser.getId());
         daoUtil.setString(nIndex++,siraUser.getGuid());
         daoUtil.setString(nIndex++,siraUser.getUdid());
         daoUtil.setString(nIndex++,siraUser.getDevice());
         daoUtil.setString(nIndex++,siraUser.getMail());
         daoUtil.setString(nIndex++, siraUser.getToken());
         
         daoUtil.executeUpdate();
         
         daoUtil.free();
         
         return siraUser.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(long lId) {
		DAOUtil daoUtil = new DAOUtil (SQL_QUERY_DELETE);
		daoUtil.setLong(1, lId);
		daoUtil.executeUpdate();
        daoUtil.free();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SiraUser load(long lId) {
		DAOUtil daoUtil = new DAOUtil (SQL_QUERY_SELECT);
		
		int nIndex = 1;
		
		daoUtil.setLong(nIndex++, lId);
		
		daoUtil.executeQuery();
		
		SiraUser siraUser = null;
		
		if(daoUtil.next()){
			nIndex = 1;
			siraUser = new SiraUser();
			siraUser.setId(daoUtil.getLong(nIndex++));
			siraUser.setGuid(daoUtil.getString(nIndex++));
			siraUser.setUdid(daoUtil.getString(nIndex++));
			siraUser.setDevice(daoUtil.getString(nIndex++));
			siraUser.setMail(daoUtil.getString(nIndex++));
	        siraUser.setToken(daoUtil.getString(nIndex++));
		}

        daoUtil.free();
		return siraUser;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(SiraUser siraUser) {
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_UPDATE);
        
		int nIndex = 1;
		
        daoUtil.setString(nIndex++,siraUser.getGuid());
        daoUtil.setString(nIndex++,siraUser.getUdid());
        daoUtil.setString(nIndex++,siraUser.getDevice());
        daoUtil.setString(nIndex++,siraUser.getMail());
        daoUtil.setString(nIndex++, siraUser.getToken());
        
        //WHERE
        daoUtil.setLong(nIndex++,siraUser.getId());
        
        daoUtil.executeUpdate();
        daoUtil.free();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SiraUser findByGuidAndToken(String guid, String token) {
		DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_BY_GUID_AND_TOKEN);
		int nIndex = 1;
		
		daoUtil.setString(nIndex++, guid);
		daoUtil.setString(nIndex++, token);
		
		daoUtil.executeQuery();
		
		SiraUser siraUser = null;
		if(daoUtil.next()){
			nIndex = 1;
			siraUser = new SiraUser();
			siraUser.setId(daoUtil.getLong(nIndex++));
			siraUser.setGuid(daoUtil.getString(nIndex++));
			siraUser.setUdid(daoUtil.getString(nIndex++));
			siraUser.setDevice(daoUtil.getString(nIndex++));
			siraUser.setMail(daoUtil.getString(nIndex++));
			siraUser.setToken(daoUtil.getString(nIndex++));
		}
		
		daoUtil.free();
		
		return siraUser;
	}

}
