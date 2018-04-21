package ru.sawasemykin.maven.quickstart;

import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * Created by Alexander Semykin on 01.12.2016.
 */
public class PeriodTest {

    @Test
    public void periodToString() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, Calendar.DECEMBER, 03, 9, 39, 0);
        calendar.set(Calendar.MILLISECOND,0);
        Period period = new Period(5,calendar);
        assertEquals("03.12.2016 08:35:00.000 - 08:40", period.toString("dd.MM.yyyy HH:mm:ss.SSS", "HH:mm", TimeZone.getTimeZone("Europe/Kiev")));
    }

    @Test
    public void fromString() throws Exception {
        Period actualPeriod = Period.fromString("03.12.2016 09:39:55.936", "dd.MM.yyyy HH:mm:ss.SSS", 5);
        assertEquals("03.12.2016 09:35:00.000 - 09:40", actualPeriod.toString("dd.MM.yyyy HH:mm:ss.SSS", "HH:mm",TimeZone.getTimeZone("Europe/Moscow")));
    }

    @Test
    public void getStartEndTime() throws Exception {
    	String[][] source = {
    			{"03.12.2016 09:12:36.832", "dd.MM.yyyy HH:mm:ss.SSS"},
    			{"03.12.2016 09:00:00.000", "dd.MM.yyyy HH:mm:ss.SSS"},
    			{"03.12.2016 09:29:59.999", "dd.MM.yyyy HH:mm:ss.SSS"},
    			{"03.12.2016 09:23:36.832", "dd.MM.yyyy HH:mm:ss.SSS"},
    			{"03.12.2016 09:30:00.000", "dd.MM.yyyy HH:mm:ss.SSS"},
    			{"03.12.2016 09:42:18.059", "dd.MM.yyyy HH:mm:ss.SSS"},
    			{"03.12.2016 09:45:00.000", "dd.MM.yyyy HH:mm:ss.SSS"},
    			{"03.12.2016 09:58:23.498+0100", "dd.MM.yyyy HH:mm:ss.SSSz"},    			
    	};
    	
    	String[][] expectedPeriods = {
    			{"03.12.2016 09:00:00.000 - 09:15", "dd.MM.yyyy HH:mm:ss.SSS", "HH:mm"},
    			{"03.12.2016 09:00:00.000 - 09:15", "dd.MM.yyyy HH:mm:ss.SSS", "HH:mm"},
    			{"03.12.2016 09:15:00.000 - 09:30", "dd.MM.yyyy HH:mm:ss.SSS", "HH:mm"},
    			{"03.12.2016 09:15:00.000 - 09:30", "dd.MM.yyyy HH:mm:ss.SSS", "HH:mm"},
    			{"03.12.2016 09:30:00.000 - 09:45", "dd.MM.yyyy HH:mm:ss.SSS", "HH:mm"},
    			{"03.12.2016 09:30:00.000 - 09:45", "dd.MM.yyyy HH:mm:ss.SSS", "HH:mm"},
    			{"03.12.2016 09:45:00.000 - 10:00", "dd.MM.yyyy HH:mm:ss.SSS", "HH:mm"},
    			{"03.12.2016 11:45:00.000MSK - 12:00", "dd.MM.yyyy HH:mm:ss.SSSz", "HH:mm"},
    	};
    	
    	String[] timeZones = {
    			"Europe/Moscow",
    			"Europe/Moscow",
    			"Europe/Moscow",
    			"Europe/Moscow",
    			"Europe/Moscow",
    			"Europe/Moscow",
    			"Europe/Moscow",
    			"Europe/Moscow",
    	};
    	
    	for(int i = 0; i < expectedPeriods.length; ++i){
    		Period actualPeriod = Period.fromString(source[i][0], source[i][1], 15);
    		assertEquals(expectedPeriods[i][0], actualPeriod.toString(expectedPeriods[i][1], expectedPeriods[i][2], TimeZone.getTimeZone(timeZones[i])));
    	}        
    }

    @Test
    public void getPreviousPeriod() throws Exception {
        Period period = Period.fromString("04.12.2016 12:33:18.059", "dd.MM.yyyy HH:mm:ss.SSS", 15);
        Period theFirstPreviousPeriod = period.getPreviousPeriod(1);

        assertEquals("04.12.2016 12:15:00.000 - 12:30", theFirstPreviousPeriod.toString("dd.MM.yyyy HH:mm:ss.SSS", "HH:mm", TimeZone.getTimeZone("Europe/Moscow")));

        period = Period.fromString("04.12.2016 00:13:15.009", "dd.MM.yyyy HH:mm:ss.SSS", 15);
        theFirstPreviousPeriod = period.getPreviousPeriod(1);

        assertEquals("03.12.2016 23:45:00.000 - 04.12.2016 00:00",
                theFirstPreviousPeriod.toString("dd.MM.yyyy HH:mm:ss.SSS", "dd.MM.yyyy HH:mm", TimeZone.getTimeZone("Europe/Moscow")));
    }
    
    @Test
    public void getNthPreviousPeriod() throws Exception{
    	Period period = Period.fromString("04.12.2016 12:33:18.059", "dd.MM.yyyy HH:mm:ss.SSS", 5);
    	Period theThirdPreviousPeriod = period.getPreviousPeriod(3);
    	assertEquals("04.12.2016 12:15:00.000 - 12:20", theThirdPreviousPeriod.toString("dd.MM.yyyy HH:mm:ss.SSS", "HH:mm", TimeZone.getTimeZone("Europe/Moscow")));
    }

    @Test
    public void getNextPeriod() throws Exception {
        Period period = Period.fromString("04.12.2016 12:23:18.999", "dd.MM.yyyy HH:mm:ss.SSS", 15);
        Period theFirstNextPeriod = period.getNextPeriod(1);

        assertEquals("04.12.2016 12:30:00.000 - 12:45", theFirstNextPeriod.toString("dd.MM.yyyy HH:mm:ss.SSS", "HH:mm", TimeZone.getTimeZone("Europe/Moscow")));

        period = Period.fromString("04.12.2016 23:59:59.999", "dd.MM.yyyy HH:mm:ss.SSS", 15);
        theFirstNextPeriod = period.getNextPeriod(1);

        assertEquals("05.12.2016 00:00:00.000 - 05.12.2016 00:15",
                theFirstNextPeriod.toString("dd.MM.yyyy HH:mm:ss.SSS", "dd.MM.yyyy HH:mm", TimeZone.getTimeZone("Europe/Moscow")));
    }
    
    @Test
    public void getNthNextPeriod() throws Exception{
    	Period period = Period.fromString("04.12.2016 12:33:18.059", "dd.MM.yyyy HH:mm:ss.SSS", 5);
    	Period theThirdPreviousPeriod = period.getNextPeriod(3);
    	assertEquals("04.12.2016 12:45:00.000 - 12:50", theThirdPreviousPeriod.toString("dd.MM.yyyy HH:mm:ss.SSS", "HH:mm", TimeZone.getTimeZone("Europe/Moscow")));
    }
}