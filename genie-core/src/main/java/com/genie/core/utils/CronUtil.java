package com.genie.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;


/**
 * cron表达式相关工具类
 *
 */
public final class CronUtil {

    private CronUtil() {
    }

    private final static Logger cronUtilLog = LoggerFactory.getLogger(CronUtil.class);

    /**
     * 获取cron表达式下一次执行时间
     *
     * @param cron
     * @return
     * @throws ParseException
     */
    public static Date getNextExecuteTime(String cron) {
        try {
            CronExpression c = new CronExpression(cron);
            Date date = c.getTimeAfter(new Date());
            return date;
        } catch (Exception e) {
            cronUtilLog.error("cron expression is invalid:cron:{}", cron);
            return null;
        }
    }

    /**
     * 验证是否有效cron表达式
     *
     * @param cron
     * @return
     */
    public static boolean isValid(String cron) {
        return CronExpression.isValidExpression(cron);
    }

}
