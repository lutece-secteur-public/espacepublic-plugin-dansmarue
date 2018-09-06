/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.plugins.dansmarue.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import fr.paris.lutece.plugins.dansmarue.business.entities.UnitNode;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class UnitUtils
{

    private static IUnitService _unitService = SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );

    private UnitUtils( )
    {
        // Constructor
    }

    public static void buildTree( UnitNode startNode )
    {
        List<Unit> units = _unitService.getSubUnits( startNode.getUnit( ).getIdUnit( ), false );
        if ( CollectionUtils.isNotEmpty( units ) )
        {
            List<UnitNode> unitNodes = new ArrayList<UnitNode>( );
            for ( Unit unit : units )
            {
                UnitNode unitNode = new UnitNode( unit );
                buildTree( unitNode );
                unitNodes.add( unitNode );
            }
            startNode.setSubUnits( unitNodes );
        }
    }

    public static void buildTree( UnitNode startNode, int depth )
    {
        List<Unit> units = _unitService.getSubUnits( startNode.getUnit( ).getIdUnit( ), false );
        if ( CollectionUtils.isNotEmpty( units ) && depth > 0 )
        {
            List<UnitNode> unitNodes = new ArrayList<UnitNode>( );
            for ( Unit unit : units )
            {
                UnitNode unitNode = new UnitNode( unit );
                buildTree( unitNode, depth - 1 );
                unitNodes.add( unitNode );
            }
            startNode.setSubUnits( unitNodes );
        }
    }

    public static void populateSubUnitsSet( Unit startNode, Set<Unit> units )
    {
        List<Unit> subUnits = _unitService.getSubUnits( startNode.getIdUnit( ), false );
        units.addAll( subUnits );
        for ( Unit subUnit : subUnits )
        {
            populateSubUnitsSet( subUnit, units );
        }
    }
}
