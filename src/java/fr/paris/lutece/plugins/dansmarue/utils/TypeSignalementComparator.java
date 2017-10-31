package fr.paris.lutece.plugins.dansmarue.utils;

import java.util.Comparator;
import java.util.Stack;

import fr.paris.lutece.plugins.sira.business.entities.TypeSignalement;

public class TypeSignalementComparator implements Comparator<TypeSignalement>{
	
	@Override
    public int compare( TypeSignalement o1, TypeSignalement o2 )
    {
        // créer une pile fifo pour o1
        Stack<Integer> stack1 = initializeStack( o1 );

        // créer une pile fifo pour o2
        Stack<Integer> stack2 = initializeStack( o2 );

        boolean stop = false;
        int ret = 0;

        while ( !stack1.empty( ) && !stack2.empty( ) && !stop )
        {
            Integer ord1 = stack1.pop( );
            Integer ord2 = stack2.pop( );
            // comparaison
            int compareTo = ord1.compareTo( ord2 );
            if ( compareTo != 0 )
            {
                ret = compareTo;
                stop = true;
            }
        }

        // si un des deux vide, regarde lequel et faire en fonction
        if ( !stop )
        {
            if ( stack1.isEmpty( ) && stack2.isEmpty( ) )
            {
                ret = o1.getLibelle( ).compareTo( o2.getLibelle( ) );
            }
            else if ( stack1.isEmpty( ) )
            {
                ret = -1;
            }
            else
            {
                ret = 1;
            }
        }

        // dépiler les 2 jusqu'à :
        //  1 qu'un des deux soit null (vide)
        //  2 qu'ils soient différents
        // retourner la différence des valeurs en haut de pile, ou -1 si la pile de o1 est null, sinon 1

        return ret;
    }

    private Stack<Integer> initializeStack( TypeSignalement typeSignalement )
    {
        Stack<Integer> stack = new Stack<Integer>( );
        TypeSignalement temp = typeSignalement;
        while ( temp != null )
        {
            stack.push( temp.getOrdre( ) );
            temp = temp.getTypeSignalementParent( );

        }
        return stack;
    }

}
