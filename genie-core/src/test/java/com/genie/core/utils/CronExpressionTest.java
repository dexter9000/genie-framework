package com.genie.core.utils;


import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;


public class CronExpressionTest {
    private static final String[] VERSIONS = new String[]{"1.5.2"};

    private static final TimeZone EST_TIME_ZONE = TimeZone.getTimeZone("US/Eastern");

    /*
     * Test method for 'org.quartz.CronExpression.isSatisfiedBy(Date)'.
     */
    @Test
    public void testIsSatisfiedBy() throws Exception {
        CronExpression cronExpression = new CronExpression("0 15 10 * * ? 2005");

        Calendar cal = Calendar.getInstance();

        cal.set(2005, Calendar.JUNE, 1, 10, 15, 0);
        assertTrue(cronExpression.isSatisfiedBy(cal.getTime()));

        cal.set(Calendar.YEAR, 2006);
        assertFalse(cronExpression.isSatisfiedBy(cal.getTime()));

        cal = Calendar.getInstance();
        cal.set(2005, Calendar.JUNE, 1, 10, 16, 0);
        assertFalse(cronExpression.isSatisfiedBy(cal.getTime()));

        cal = Calendar.getInstance();
        cal.set(2005, Calendar.JUNE, 1, 10, 14, 0);
        assertFalse(cronExpression.isSatisfiedBy(cal.getTime()));
    }

    @Test
    public void testGetNextInvalidTimeAfter() throws Exception {
        CronExpression cronExpression = new CronExpression("1 15 10 * * ? 2005");

        Calendar cal = Calendar.getInstance();

        cal.set(2005, Calendar.JUNE, 1, 10, 15, 0);
        Date date = cronExpression.getNextInvalidTimeAfter(cal.getTime());
        assertFalse(cal.getTime().equals(date));

        cal = Calendar.getInstance();
        cal.set(2005, Calendar.JUNE, 1, 11, 15, 0);
        date = cronExpression.getNextInvalidTimeAfter(cal.getTime());
        cal.set(Calendar.SECOND,+1);
        cal.set(Calendar.MILLISECOND, 0);
        assertEquals(cal.getTime(),date);

        CronExpression cronExpressionYear3000 = new CronExpression("0 15 10 * * ? 3000");
        cal.set(3000, Calendar.JUNE, 1, 10, 15, 0);
        date = cronExpressionYear3000.getNextInvalidTimeAfter(cal.getTime());
        cal.set(Calendar.SECOND,+1);
        cal.set(Calendar.MILLISECOND, 0);
        assertEquals(cal.getTime(),date);

        CronExpression cronExpressionMonth = new CronExpression("0 15 10 L * ? 2005");
        cal.set(2005, Calendar.JUNE, 1, 11, 15, 0);
        date = cronExpressionMonth.getNextInvalidTimeAfter(cal.getTime());
        cal.set(Calendar.SECOND,+1);
        cal.set(Calendar.MILLISECOND, 0);
        assertEquals(cal.getTime(),date);
    }

    @Test
    public void testGetTimeAfter() throws Exception {
        CronExpression cronExpression = new CronExpression("0 15 10 L-4 * ? 2005");
        Calendar cal = Calendar.getInstance();
        cal.set(2005, Calendar.DECEMBER, 29, 11, 15, 0);
        Date date = cronExpression.getNextInvalidTimeAfter(cal.getTime());
        assertFalse(cal.getTime().equals(date));
        CronExpression cronExpression1 = new CronExpression("0 15 10 ? * MONL 2005");
        cal = Calendar.getInstance();
        cal.set(2005, Calendar.DECEMBER, 29, 11, 15, 0);
        date = cronExpression1.getNextInvalidTimeAfter(cal.getTime());
        assertFalse(cal.getTime().equals(date));
        CronExpression cronExpression2 = new CronExpression("0 15 10 ? * MON#3 2005");
        cal = Calendar.getInstance();
        cal.set(2005, Calendar.DECEMBER, 29, 11, 15, 0);
        date = cronExpression2.getNextInvalidTimeAfter(cal.getTime());
        assertFalse(cal.getTime().equals(date));
        CronExpression cronExpression3 = new CronExpression("0 15 10 ? * MON 2005");
        cal = Calendar.getInstance();
        cal.set(2005, Calendar.DECEMBER, 29, 11, 15, 0);
        date = cronExpression3.getNextInvalidTimeAfter(cal.getTime());
        assertFalse(cal.getTime().equals(date));
    }

    @Test
    public void testGetFinalFireTimeAndgetTimeBefore() throws Exception {
        CronExpression cronExpression = new CronExpression("0 15 10 ? * MONL 2005");
        assertNull(cronExpression.getFinalFireTime());
        Calendar cal = Calendar.getInstance();
        cal.set(2005, Calendar.DECEMBER, 29, 11, 15, 0);
        Date date = cal.getTime();
        assertNull(cronExpression.getTimeBefore(date));
        String cron = cronExpression.toString();
        assertEquals("0 15 10 ? * MONL 2005",cron);
        String newcron = "01 15 10 ? * MONL 2005";
        CronExpression.validateExpression(newcron);
        cal = Calendar.getInstance();
        cal.set(2005, Calendar.DECEMBER, 29, 11, 15, 0);
        date = cronExpression.getNextInvalidTimeAfter(cal.getTime());
        assertFalse(cal.getTime().equals(date));
    }

    @Test
    public void testGetTimeAfter3() throws Exception {
        CronExpression cronExpression = new CronExpression("0 15 10 1W * ? 2005");
        Calendar cal = Calendar.getInstance();
        cal.set(2005, Calendar.JUNE, 1, 10, 15, 0);
        Date date = cronExpression.getNextInvalidTimeAfter(cal.getTime());
        assertFalse(cal.getTime().equals(date));
    }

    @Test
    public void testGetExpressionSetSummaryList() throws Exception {
//        Class<CronExpression> cronExpressionClass = CronExpression.class;
        CronExpression cronExpression = new CronExpression("0 15 10 1W * ? 2005");
        Method getExpressionSetSummary = CronExpression.class.getDeclaredMethod("getExpressionSetSummary", ArrayList.class);
        getExpressionSetSummary.setAccessible(true);
        List<Integer> testnumber = new ArrayList<>();
        testnumber.add(2006);
        String year = (String) getExpressionSetSummary.invoke(cronExpression,testnumber);
        assertEquals("2006",year);
    }

    @Test
    public void testGetTimeAfter1() throws Exception {
        CronExpression cronExpression = new CronExpression("0 15 10 ? * SUNL 2005");
        Calendar cal = Calendar.getInstance();
        cal.set(2005, Calendar.JUNE, 1, 10, 15, 0);
        Date date = cronExpression.getNextInvalidTimeAfter(cal.getTime());
        assertFalse(cal.getTime().equals(date));
    }

    @Test
    public void testGetTimeAfter2() throws Exception {
        CronExpression cronExpression = new CronExpression("0 15 10 ? * SUN 2005");
        Calendar cal = Calendar.getInstance();
        cal.set(2005, Calendar.JUNE, 1, 10, 15, 0);
        Date date = cronExpression.getNextInvalidTimeAfter(cal.getTime());
        assertFalse(cal.getTime().equals(date));
    }

    @Test
    public void testIsValidExpression() throws Exception {
        boolean result = CronExpression.isValidExpression("0 15 10 * * ? 2005");
        assertTrue(result);
        result = CronExpression.isValidExpression("62 15 10 * * ? 2005");
        assertFalse(result);
    }

    @Test
    public void testGetExpressionSummary() throws Exception {
        CronExpression cronExpression = new CronExpression("0 15 10 * * ? 2005");
        String result = cronExpression.getExpressionSummary();
        StringBuilder buf = new StringBuilder();
        buf.append("seconds: ");
        buf.append(0);
        buf.append("\n");
        buf.append("minutes: ");
        buf.append(15);
        buf.append("\n");
        buf.append("hours: ");
        buf.append(10);
        buf.append("\n");
        buf.append("daysOfMonth: ");
        buf.append("*");
        buf.append("\n");
        buf.append("months: ");
        buf.append("*");
        buf.append("\n");
        buf.append("daysOfWeek: ");
        buf.append("?");
        buf.append("\n");
        buf.append("lastdayOfWeek: ");
        buf.append("false");
        buf.append("\n");
        buf.append("nearestWeekday: ");
        buf.append("false");
        buf.append("\n");
        buf.append("NthDayOfWeek: ");
        buf.append(0);
        buf.append("\n");
        buf.append("lastdayOfMonth: ");
        buf.append("false");
        buf.append("\n");
        buf.append("years: ");
        buf.append(2005);
        buf.append("\n");
        assertEquals(buf.toString(),result);
    }

    @Test
    public void testStoreExpressionVals() throws Exception {
        CronExpression cronExpression = new CronExpression("0 0-30/10 10 ? OCT 2#2 2005");
        Calendar cal = Calendar.getInstance();

        cal.set(2005, Calendar.OCTOBER, 1, 10, 15, 0);
        Date date = cronExpression.getNextInvalidTimeAfter(cal.getTime());
        assertFalse(cal.getTime().equals(date));
    }

    @Test
    public void testStoreExpressionValsForException() {
        try {
            String testNull = null;
            CronExpression cronExpression = new CronExpression(testNull);
        } catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
        }
        try{
            CronExpression cronExpression = new CronExpression("0 15 10 ? OCT SUN-TUB 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 15 10 ? OCT MON#6 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 15 10 ? OCT");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 15 10 ? OCT SUN OCT");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 15 10 ???? OCT SUN 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 15 ? ? OCT SUN 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 15 10 10-32/1L OCT ? 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("20-14/2 15-2/2 10-2/2 10-2/2 OCT ? 2009-2005/2");
        } catch (Exception ex) {
            assertTrue(ex instanceof Exception);
        }
        try {
            CronExpression cronExpression = new CronExpression("15-10/2 15 10 10 OCT ? 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof Exception);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 0-30/10 10 ? OCT SUN### 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 0-30/10 10 ? OCT MOE 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 0-30/10 10 ? OCT ? 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 0-30/10 10L ? OCT MON 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 0-30/10 10W ? OCT MON 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 0-30/10 10 ? OCT 1#7 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 0-30 10 ? OCT 1#7 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 0/10 10 ? OCT 1#7 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 0/1# 10 ? OCT 1#7 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 0& 10 ? OCT 1#7 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 0 25-20/2 ? OCT 1#7 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 0 10 ? 14-12/2 1#7 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 0 10 ? OCT 1-8/2 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 0 10 ? 12-11/2 SUN 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 0 10 ? OCT 6-1/2 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
        try {
            CronExpression cronExpression = new CronExpression("0 0 10 *#2 OCT ? 2005");
        } catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }
    }

    @Test
    public void testLastDayOffset() throws Exception {
        CronExpression cronExpression = new CronExpression("0 15 10 L-2 * ? 2010");

        Calendar cal = Calendar.getInstance();

        cal.set(2010, Calendar.OCTOBER, 29, 10, 15, 0); // last day - 2
        assertTrue(cronExpression.isSatisfiedBy(cal.getTime()));

        cal.set(2010, Calendar.OCTOBER, 28, 10, 15, 0);
        assertFalse(cronExpression.isSatisfiedBy(cal.getTime()));

        cronExpression = new CronExpression("0 15 10 L-5W * ? 2010");

        cal.set(2010, Calendar.OCTOBER, 26, 10, 15, 0); // last day - 5
        assertTrue(cronExpression.isSatisfiedBy(cal.getTime()));

        cronExpression = new CronExpression("0 15 10 L-1 * ? 2010");

        cal.set(2010, Calendar.OCTOBER, 30, 10, 15, 0); // last day - 1
        assertTrue(cronExpression.isSatisfiedBy(cal.getTime()));

        cronExpression = new CronExpression("0 15 10 L-1W * ? 2010");

        cal.set(2010, Calendar.OCTOBER, 29, 10, 15, 0); // nearest weekday to last day - 1 (29th is a friday in 2010)
        assertTrue(cronExpression.isSatisfiedBy(cal.getTime()));

    }

    /*
     * QUARTZ-571: Showing that expressions with months correctly serialize.
     */
    @Test
    public void testQuartz571() throws Exception {
        CronExpression cronExpression = new CronExpression("19 15 10 4 Apr ? ");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(cronExpression);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        CronExpression newExpression = (CronExpression) ois.readObject();

        assertEquals(newExpression.getCronExpression(), cronExpression.getCronExpression());

        // if broken, this will throw an exception
        newExpression.getNextValidTimeAfter(new Date());
    }

    /*
     * QUARTZ-574: Showing that storeExpressionVals correctly calculates the month number
     */
    @Test
    public void testQuartz574() {
        try {
            new CronExpression("* * * * Foo ? ");
            fail("Expected ParseException did not fire for non-existent month");
        } catch (ParseException pe) {
            assertTrue("Incorrect ParseException thrown",
                    pe.getMessage().startsWith("Invalid Month value:"));
        }

        try {
            new CronExpression("* * * * Jan-Foo ? ");
            fail("Expected ParseException did not fire for non-existent month");
        } catch (ParseException pe) {
            assertTrue("Incorrect ParseException thrown",
                    pe.getMessage().startsWith("Invalid Month value:"));
        }
    }

    @Test
    public void testQuartz621() {
        try {
            new CronExpression("0 0 * * * *");
            fail("Expected ParseException did not fire for wildcard day-of-month and day-of-week");
        } catch (ParseException pe) {
            assertTrue("Incorrect ParseException thrown",
                    pe.getMessage().startsWith("Support for specifying both a day-of-week AND a day-of-month parameter is not implemented."));
        }
        try {
            new CronExpression("0 0 * 4 * *");
            fail("Expected ParseException did not fire for specified day-of-month and wildcard day-of-week");
        } catch (ParseException pe) {
            assertTrue("Incorrect ParseException thrown",
                    pe.getMessage().startsWith("Support for specifying both a day-of-week AND a day-of-month parameter is not implemented."));
        }
        try {
            new CronExpression("0 0 * * * 4");
            fail("Expected ParseException did not fire for wildcard day-of-month and specified day-of-week");
        } catch (ParseException pe) {
            assertTrue("Incorrect ParseException thrown",
                    pe.getMessage().startsWith("Support for specifying both a day-of-week AND a day-of-month parameter is not implemented."));
        }
    }

    @Test
    public void testQuartz640() throws ParseException {
        try {
            new CronExpression("0 43 9 1,5,29,L * ?");
            fail("Expected ParseException did not fire for L combined with other days of the month");
        } catch (ParseException pe) {
            assertTrue("Incorrect ParseException thrown",
                    pe.getMessage().startsWith("Support for specifying 'L' and 'LW' with other days of the month is not implemented"));
        }
        try {
            new CronExpression("0 43 9 ? * SAT,SUN,L");
            fail("Expected ParseException did not fire for L combined with other days of the week");
        } catch (ParseException pe) {
            assertTrue("Incorrect ParseException thrown",
                    pe.getMessage().startsWith("Support for specifying 'L' with other days of the week is not implemented"));
        }
        try {
            new CronExpression("0 43 9 ? * 6,7,L");
            fail("Expected ParseException did not fire for L combined with other days of the week");
        } catch (ParseException pe) {
            assertTrue("Incorrect ParseException thrown",
                    pe.getMessage().startsWith("Support for specifying 'L' with other days of the week is not implemented"));
        }
        try {
            new CronExpression("0 43 9 ? * 5L");
        } catch (ParseException pe) {
            fail("Unexpected ParseException thrown for supported '5L' expression.");
        }
    }


    @Test
    public void testQtz96() throws ParseException {
        try {
            new CronExpression("0/5 * * 32W 1 ?");
            fail("Expected ParseException did not fire for W with value larger than 31");
        } catch (ParseException pe) {
            assertTrue("Incorrect ParseException thrown",
                    pe.getMessage().startsWith("The 'W' option does not make sense with values larger than"));
        }
    }

    @Test
    public void testQtz395_CopyConstructorMustPreserveTimeZone() throws ParseException {
        TimeZone nonDefault = TimeZone.getTimeZone("Europe/Brussels");
        if (nonDefault.equals(TimeZone.getDefault())) {
            nonDefault = EST_TIME_ZONE;
        }
        CronExpression cronExpression = new CronExpression("0 15 10 * * ? 2005");
        cronExpression.setTimeZone(nonDefault);

        CronExpression copyCronExpression = new CronExpression(cronExpression);
        assertEquals(nonDefault, copyCronExpression.getTimeZone());
    }

    // Issue #58
    @Test
    public void testSecRangeIntervalAfterSlash() throws Exception {
        // Test case 1
        try {
            new CronExpression("/120 0 8-18 ? * 2-6");
            fail("Cron did not validate bad range interval in '_blank/xxx' form");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Increment > 60 : 120");
        }

        // Test case 2
        try {
            new CronExpression("0/120 0 8-18 ? * 2-6");
            fail("Cron did not validate bad range interval in in '0/xxx' form");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Increment > 60 : 120");
        }

        // Test case 3
        try {
            new CronExpression("/ 0 8-18 ? * 2-6");
            fail("Cron did not validate bad range interval in '_blank/_blank'");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "'/' must be followed by an integer.");
        }

        // Test case 4
        try {
            new CronExpression("0/ 0 8-18 ? * 2-6");
            fail("Cron did not validate bad range interval in '0/_blank'");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "'/' must be followed by an integer.");
        }
    }


    // Issue #58
    @Test
    public void testMinRangeIntervalAfterSlash() throws Exception {
        // Test case 1
        try {
            new CronExpression("0 /120 8-18 ? * 2-6");
            fail("Cron did not validate bad range interval in '_blank/xxx' form");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Increment > 60 : 120");
        }

        // Test case 2
        try {
            new CronExpression("0 0/120 8-18 ? * 2-6");
            fail("Cron did not validate bad range interval in in '0/xxx' form");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Increment > 60 : 120");
        }

        // Test case 3
        try {
            new CronExpression("0 / 8-18 ? * 2-6");
            fail("Cron did not validate bad range interval in '_blank/_blank'");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "'/' must be followed by an integer.");
        }

        // Test case 4
        try {
            new CronExpression("0 0/ 8-18 ? * 2-6");
            fail("Cron did not validate bad range interval in '0/_blank'");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "'/' must be followed by an integer.");
        }
    }

    // Issue #58
    @Test
    public void testHourRangeIntervalAfterSlash() throws Exception {
        // Test case 1
        try {
            new CronExpression("0 0 /120 ? * 2-6");
            fail("Cron did not validate bad range interval in '_blank/xxx' form");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Increment > 24 : 120");
        }

        // Test case 2
        try {
            new CronExpression("0 0 0/120 ? * 2-6");
            fail("Cron did not validate bad range interval in in '0/xxx' form");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Increment > 24 : 120");
        }

        // Test case 3
        try {
            new CronExpression("0 0 / ? * 2-6");
            fail("Cron did not validate bad range interval in '_blank/_blank'");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "'/' must be followed by an integer.");
        }

        // Test case 4
        try {
            new CronExpression("0 0 0/ ? * 2-6");
            fail("Cron did not validate bad range interval in '0/_blank'");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "'/' must be followed by an integer.");
        }
    }

    // Issue #58
    @Test
    public void testDayOfMonthRangeIntervalAfterSlash() throws Exception {
        // Test case 1
        try {
            new CronExpression("0 0 0 /120 * 2-6");
            fail("Cron did not validate bad range interval in '_blank/xxx' form");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Increment > 31 : 120");
        }

        // Test case 2
        try {
            new CronExpression("0 0 0 0/120 * 2-6");
            fail("Cron did not validate bad range interval in in '0/xxx' form");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Increment > 31 : 120");
        }

        // Test case 3
        try {
            new CronExpression("0 0 0 / * 2-6");
            fail("Cron did not validate bad range interval in '_blank/_blank'");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "'/' must be followed by an integer.");
        }

        // Test case 4
        try {
            new CronExpression("0 0 0 0/ * 2-6");
            fail("Cron did not validate bad range interval in '0/_blank'");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "'/' must be followed by an integer.");
        }
    }

    // Issue #58
    @Test
    public void testMonthRangeIntervalAfterSlash() throws Exception {
        // Test case 1
        try {
            new CronExpression("0 0 0 ? /120 2-6");
            fail("Cron did not validate bad range interval in '_blank/xxx' form");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Increment > 12 : 120");
        }

        // Test case 2
        try {
            new CronExpression("0 0 0 ? 0/120 2-6");
            fail("Cron did not validate bad range interval in in '0/xxx' form");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Increment > 12 : 120");
        }

        // Test case 3
        try {
            new CronExpression("0 0 0 ? / 2-6");
            fail("Cron did not validate bad range interval in '_blank/_blank'");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "'/' must be followed by an integer.");
        }

        // Test case 4
        try {
            new CronExpression("0 0 0 ? 0/ 2-6");
            fail("Cron did not validate bad range interval in '0/_blank'");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "'/' must be followed by an integer.");
        }
    }


    // Issue #58
    @Test
    public void testDayOfWeekRangeIntervalAfterSlash() throws Exception {
        // Test case 1
        try {
            new CronExpression("0 0 0 ? * /120");
            fail("Cron did not validate bad range interval in '_blank/xxx' form");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Increment > 7 : 120");
        }

        // Test case 2
        try {
            new CronExpression("0 0 0 ? * 0/120");
            fail("Cron did not validate bad range interval in in '0/xxx' form");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Increment > 7 : 120");
        }

        // Test case 3
        try {
            new CronExpression("0 0 0 ? * /");
            fail("Cron did not validate bad range interval in '_blank/_blank'");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "'/' must be followed by an integer.");
        }

        // Test case 4
        try {
            new CronExpression("0 0 0 ? * 0/");
            fail("Cron did not validate bad range interval in '0/_blank'");
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "'/' must be followed by an integer.");
        }
    }

}
