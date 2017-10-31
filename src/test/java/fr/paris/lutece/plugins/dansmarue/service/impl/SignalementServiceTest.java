package fr.paris.lutece.plugins.dansmarue.service.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import fr.paris.lutece.plugins.dansmarue.service.impl.SignalementService;


@RunWith( JUnit4.class )
public class SignalementServiceTest
{

    @Test
    public void getLetterByMonthTest( )
    {
        assertTrue( SignalementService.getLetterByMonth( 3 ).equals( "D" ) );
        assertTrue( SignalementService.getLetterByMonth( 0 ).equals( "A" ) );
        assertTrue( SignalementService.getLetterByMonth( 11 ).equals( "L" ) );
        assertTrue( SignalementService.getLetterByMonth( 4 ).equals( "E" ) );
        assertTrue( SignalementService.getLetterByMonth( 1 ).equals( "B" ) );
        assertTrue( SignalementService.getLetterByMonth( 5 ).equals( "F" ) );
    }
}
