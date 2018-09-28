package com.genie.core.utils;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CronUtilTest {

    @Test
    public void getNextExecuteTime() {
        String cronExpressionstr = "0 15 10 * * ? *";
        Date date = CronUtil.getNextExecuteTime(cronExpressionstr);
        Calendar c = Calendar.getInstance();
        Calendar standard = Calendar.getInstance();
        standard.set(Calendar.SECOND, 0);
        standard.set(Calendar.MINUTE, 15);
        standard.set(Calendar.HOUR_OF_DAY, 10);
        if(c.getTime().after(standard.getTime())){
            standard.add(Calendar.DAY_OF_MONTH, 1);
        }
        Date dateStandard = standard.getTime();
        assertEquals(dateStandard.toString(), date.toString());
    }

    @Test
    public void isValid() {
        String cronExpressionstr = "0 15 10 * * ? 2018";
        boolean cronExpressionbool = CronUtil.isValid(cronExpressionstr);
        assertTrue(cronExpressionbool);
    }
}
