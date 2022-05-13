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
package fr.paris.lutece.plugins.dansmarue.util.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * SignalementAdresseConstants.
 */
public final class SignalementAdresseConstants
{
    public static final Map<String, String> ABREVIATION_MAP = new HashMap<>( );
    static
    {
        ABREVIATION_MAP.put( "All. ", "allée " );
        ABREVIATION_MAP.put( "Av. ", "avenue " );
        ABREVIATION_MAP.put( "Ave ", "avenue " );
        ABREVIATION_MAP.put( "Bd ", "boulevard " );
        ABREVIATION_MAP.put( "Blvd ", "boulevard " );
        ABREVIATION_MAP.put( "Cr ", "cours " );
        ABREVIATION_MAP.put( "Ch. ", "Charles " );
        ABREVIATION_MAP.put( "Chem. ", "Chemin " );
        ABREVIATION_MAP.put( "Dr ", "docteur " );
        ABREVIATION_MAP.put( "Espl. ", "esplanade " );
        ABREVIATION_MAP.put( "Frme ", "Ferme " );
        ABREVIATION_MAP.put( "Imp. ", "impasse " );
        ABREVIATION_MAP.put( "Pass. ", "passage " );
        ABREVIATION_MAP.put( "Pl. ", "place " );
        ABREVIATION_MAP.put( "Prom. ", "promenade " );
        ABREVIATION_MAP.put( "Prte ", "porte " );
        ABREVIATION_MAP.put( "R. ", "rue " );
        ABREVIATION_MAP.put( "Rdpt ", "rond-point " );
        ABREVIATION_MAP.put( "Rte ", "route " );
        ABREVIATION_MAP.put( "Sent. ", "sentier " );
        ABREVIATION_MAP.put( "Sq. ", "square " );
        ABREVIATION_MAP.put( "St ", "Saint " );
        ABREVIATION_MAP.put( "Ste ", "Sainte " );
        ABREVIATION_MAP.put( "Vla ", "villa " );
    }

    /**
     * Utility class - empty constructor.
     */
    private SignalementAdresseConstants( )
    {
        // nothing
    }

}
