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

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

import fr.paris.lutece.plugins.dansmarue.business.entities.TypeSignalement;

public class TypeSignalementComparator implements Comparator<TypeSignalement>
{

    @Override
    public int compare( TypeSignalement o1, TypeSignalement o2 )
    {
        // create a fifo stack for o1
        Deque<Integer> stack1 = initializeStack( o1 );

        // create a fifo stack for o2
        Deque<Integer> stack2 = initializeStack( o2 );

        boolean stop = false;
        int ret = 0;

        while ( !stack1.isEmpty( ) && !stack2.isEmpty( ) && !stop )
        {
            Integer ord1 = stack1.pop( );
            Integer ord2 = stack2.pop( );
            // compare
            int compareTo = ord1.compareTo( ord2 );
            if ( compareTo != 0 )
            {
                ret = compareTo;
                stop = true;
            }
        }

        // if one of the two is empty, look at which one and do the following
        if ( !stop )
        {
            if ( stack1.isEmpty( ) && stack2.isEmpty( ) )
            {
                ret = o1.getLibelle( ).compareTo( o2.getLibelle( ) );
            } else if ( stack1.isEmpty( ) )
            {
                ret = -1;
            } else
            {
                ret = 1;
            }
        }

        // Unstack the 2 until:
        // 1 that one of them is null (empty)
        // 2 that they are different
        // return the difference of the values at the top of the stack, or -1 if the stack of o1 is null, otherwise 1

        return ret;
    }

    private Deque<Integer> initializeStack( TypeSignalement typeSignalement )
    {
        Deque<Integer> stack = new ArrayDeque<Integer>( );
        TypeSignalement temp = typeSignalement;
        while ( temp != null )
        {
            stack.push( temp.getOrdre( ) );
            temp = temp.getTypeSignalementParent( );

        }
        return stack;
    }

}
