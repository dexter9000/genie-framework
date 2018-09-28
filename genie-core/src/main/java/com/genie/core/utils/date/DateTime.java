package com.genie.core.utils.date;


import com.genie.core.utils.DateUtil;
import com.genie.core.utils.date.format.DateParser;
import com.genie.core.utils.date.format.DatePrinter;
import com.genie.core.utils.date.format.FastDateFormat;
import org.apache.commons.lang3.ObjectUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 包装java.util.Date
 *
 * @author xiaoleilu
 *
 */
public class DateTime extends Date {
	private static final long serialVersionUID = -5395712593979185936L;

	/** 是否可变对象 */
	private boolean mutable = true;

	/**
	 * 转换JDK date为 DateTime
	 *
	 * @param date JDK Date
	 * @return DateTime
	 */
	public static DateTime of(Date date) {
		return new DateTime(date);
	}

	/**
	 * 转换 {@link Calendar} 为 DateTime
	 *
	 * @param calendar {@link Calendar}
	 * @return DateTime
	 */
	public static DateTime of(Calendar calendar) {
		return new DateTime(calendar);
	}

	/**
	 * 构造
	 * @see DatePattern
	 * @param dateStr Date字符串
	 * @param format 格式
	 */
	public static DateTime of(String dateStr, String format){
		return new DateTime(dateStr, format);
	}

	/**
	 * 现在的时间
	 *
	 * @return 现在的时间
	 */
	public static DateTime now() {
		return new DateTime();
	}

	// -------------------------------------------------------------------- Constructor start
	/**
	 * 当前时间
	 */
	public DateTime() {
		super();
	}

	/**
	 * 给定日期的构造
	 *
	 * @param date 日期
	 */
	public DateTime(Date date) {
		this(date.getTime());
	}

	/**
	 * 给定日期的构造
	 *
	 * @param calendar {@link Calendar}
	 */
	public DateTime(Calendar calendar) {
		this(calendar.getTime());
	}

	/**
	 * 给定日期毫秒数的构造
	 *
	 * @param timeMillis 日期毫秒数
	 */
	public DateTime(long timeMillis) {
		super(timeMillis);
	}

	/**
	 * 构造
	 * @see DatePattern
	 * @param dateStr Date字符串
	 * @param format 格式
	 */
	public DateTime(String dateStr, String format){
		this(dateStr, FastDateFormat.getInstance(format));
	}

	/**
	 * 构造
	 * @see DatePattern
	 * @param dateStr Date字符串
	 * @param dateFormat 格式化器 {@link SimpleDateFormat}
	 */
	public DateTime(String dateStr, DateFormat dateFormat) {
		this(parse(dateStr, dateFormat));
	}

	/**
	 * 构造
	 * @see DatePattern
	 * @param dateStr Date字符串
	 * @param dateParser 格式化器 {@link DateParser}，可以使用 {@link FastDateFormat}
	 */
	public DateTime(String dateStr, DateParser dateParser) {
		this(parse(dateStr, dateParser));
	}

	// -------------------------------------------------------------------- Constructor end

	// -------------------------------------------------------------------- offsite start
	/**
	 * 调整日期和时间<br>
	 *
	 * @param datePart 调整的部分 {@link DateField}
	 * @param offsite 偏移量，正数为向后偏移，负数为向前偏移
	 * @return 如果此对象为可变对象，返回自身，否则返回新对象
	 */
	public DateTime offsite(DateField datePart, int offsite) {
		final Calendar cal = toCalendar();
		cal.add(datePart.getValue(), offsite);

		DateTime dt = this;
		if(false == mutable){
            ObjectUtils.clone(this);
		}
		return dt.setTimeInternal(cal.getTimeInMillis());
	}
	// -------------------------------------------------------------------- offsite end

	// -------------------------------------------------------------------- Part of Date start
	/**
	 * 获得日期的某个部分<br>
	 * 例如获得年的部分，则使用 getField(DatePart.YEAR)
	 * @param field 表示日期的哪个部分的枚举 {@link DateField}
	 * @return 某个部分的值
	 */
	public int getField(DateField field){
		return getField(field.getValue());
	}

	/**
	 * 获得日期的某个部分<br>
	 * 例如获得年的部分，则使用 getField(Calendar.YEAR)
	 * @param field 表示日期的哪个部分的int值 {@link Calendar}
	 * @return 某个部分的值
	 */
	public int getField(int field){
		return toCalendar().get(field);
	}

	/**
	 * 设置日期的某个部分
	 * @param field 表示日期的哪个部分的枚举 {@link DateField}
	 * @param value 值
	 * @return {@link DateTime}
	 */
	public DateTime setField(DateField field, int value){
		return setField(field.getValue(), value);
	}

	/**
	 * 设置日期的某个部分
	 * @param field 表示日期的哪个部分的int值 {@link Calendar}
	 * @param value 值
	 * @return {@link DateTime}
	 */
	public DateTime setField(int field, int value){
		Calendar calendar = toCalendar();
		calendar.set(field, value);

		DateTime dt = this;
		if(false == mutable){
			dt = ObjectUtils.clone(this);
		}
		return dt.setTimeInternal(calendar.getTimeInMillis());
	}

	@Override
	public void setTime(long time) {
		if(mutable){
			super.setTime(time);
		}else{
			throw new DateException("This is not a mutable object !");
		}
	}

	/**
	 * 获得年的部分
	 *
	 * @return 年的部分
	 */
	public int year() {
		return getField(DateField.YEAR);
	}

	/**
	 * 获得当前日期所属季度<br>
	 * 1：第一季度<br>
	 * 2：第二季度<br>
	 * 3：第三季度<br>
	 * 4：第四季度<br>
	 *
	 * @return 第几个季度
	 */
	public int season() {
		return monthStartFromOne() /3 + 1;
	}

	/**
	 * 获得当前日期所属季度<br>
	 * @return 第几个季度 {@link Season}
	 */
	public Season seasonEnum() {
		return Season.of(season());
	}

	/**
	 * 获得月份，从0开始计数
	 *
	 * @return 月份
	 */
	public int month() {
		return getField(DateField.MONTH);
	}

	/**
	 * 获得月份，从1开始计数<br>
	 * 由于{@link Calendar} 中的月份按照0开始计数，导致某些需求容易误解，因此如果想用1表示一月，2表示二月则调用此方法
	 *
	 * @return 月份
	 */
	public int monthStartFromOne() {
		return month() +1;
	}

	/**
	 * 获得月份
	 *
	 * @return {@link Month}
	 */
	public Month monthEnum() {
		return Month.of(month());
	}

	/**
	 * 获得指定日期是所在年份的第几周<br>
	 *
	 * @return 周
	 */
	public int weekOfYear() {
		return getField(DateField.WEEK_OF_YEAR);
	}

	/**
	 * 获得指定日期是所在月份的第几周<br>
	 *
	 * @return 周
	 */
	public int weekOfMonth() {
		return getField(DateField.WEEK_OF_MONTH);
	}

	/**
	 * 获得指定日期是这个日期所在月份的第几天<br>
	 *
	 * @return 天
	 */
	public int dayOfMonth() {
		return getField(DateField.DAY_OF_MONTH);
	}

	/**
	 * 获得指定日期是星期几
	 *
	 * @return 天
	 */
	public int dayOfWeek() {
		return getField(DateField.DAY_OF_WEEK);
	}

	/**
	 * 获得天所在的周是这个月的第几周
	 *
	 * @return 天
	 */
	public int dayOfWeekInMonth() {
		return getField(DateField.DAY_OF_WEEK_IN_MONTH);
	}

	/**
	 * 获得指定日期是星期几
	 *
	 * @return {@link Week}
	 */
	public Week dayOfWeekEnum() {
		return Week.of(dayOfWeek());
	}

	/**
	 * 获得指定日期的小时数部分<br>
	 *
	 * @param is24HourClock 是否24小时制
	 * @return 小时数
	 */
	public int hour(boolean is24HourClock) {
		return getField(is24HourClock ? DateField.HOUR_OF_DAY : DateField.HOUR);
	}

	/**
	 * 获得指定日期的分钟数部分<br>
	 * 例如：10:04:15.250 -> 4
	 *
	 * @return 分钟数
	 */
	public int minute() {
		return getField(DateField.MINUTE);
	}

	/**
	 * 获得指定日期的秒数部分<br>
	 *
	 * @return 秒数
	 */
	public int second() {
		return getField(DateField.SECOND);
	}

	/**
	 * 获得指定日期的毫秒数部分<br>
	 *
	 * @return 毫秒数
	 */
	public int millsecond() {
		return getField(DateField.MILLISECOND);
	}

	/**
	 * 是否为上午
	 *
	 * @return 是否为上午
	 */
	public boolean isAM() {
		return Calendar.AM == getField(DateField.AM_PM);
	}

	/**
	 * 是否为下午
	 *
	 * @return 是否为下午
	 */
	public boolean isPM() {
		return Calendar.PM == getField(DateField.AM_PM);
	}
	// -------------------------------------------------------------------- Part of Date end

	/**
	 * 是否闰年
	 * @see DateUtil#isLeapYear(int)
	 * @return 是否闰年
	 */
	public boolean isLeapYear() {
		return DateUtil.isLeapYear(year());
	}

	/**
	 * 转换为Calendar，默认{@link TimeZone}，默认 {@link Locale}
	 *
	 * @return {@link Calendar}
	 */
	public Calendar toCalendar() {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(this);
		return cal;
	}

	/**
	 * 转换为Calendar
	 * @param locale 地域 {@link Locale}
	 * @return {@link Calendar}
	 */
	public Calendar toCalendar(Locale locale) {
		final Calendar cal = Calendar.getInstance(locale);
		cal.setTime(this);
		return cal;
	}

	/**
	 * 转换为Calendar
	 * @param zone 时区 {@link TimeZone}
	 * @return {@link Calendar}
	 */
	public Calendar toCalendar(TimeZone zone) {
		return toCalendar(zone, Locale.getDefault(Locale.Category.FORMAT));
	}

	/**
	 * 转换为Calendar
	 * @param zone 时区 {@link TimeZone}
	 * @param locale 地域 {@link Locale}
	 * @return {@link Calendar}
	 */
	public Calendar toCalendar(TimeZone zone, Locale locale) {
		final Calendar cal = Calendar.getInstance(zone, locale);
		cal.setTime(this);
		return cal;
	}

	/**
	 * 计算相差时长
	 * @param date 对比的日期
	 * @return {@link DateBetween}
	 */
	public DateBetween between(Date date){
		return new DateBetween(this, date);
	}

	/**
	 * 计算相差时长
	 * @param date 对比的日期
	 * @param unit 单位 {@link DateUnit}
	 * @return 相差时长
	 */
	public long between(Date date, DateUnit unit){
		return new DateBetween(this, date).between(unit);
	}

	/**
	 * 计算相差时长
	 * @param date 对比的日期
	 * @param formatLevel 格式化级别
	 * @return 相差时长
	 */
	public String between(Date date, BetweenFormater.Level formatLevel){
		return new DateBetween(this, date).toString(formatLevel);
	}

	/**
	 * 对象是否可变<br>
	 * 如果为不可变对象，以下方法将返回新方法：
	 * <ul>
	 * 	<li>{@link DateTime#offsite(DateField, int)}</li>
	 * 	<li>{@link DateTime#setField(DateField, int)}</li>
	 * 	<li>{@link DateTime#setField(int, int)}</li>
	 * </ul>
	 * 如果为不可变对象，{@link DateTime#setTime(long)}将抛出异常
	 *
	 * @return 对象是否可变
	 */
	public boolean isMutable() {
		return mutable;
	}

	/**
	 * 设置对象是否可变
	 * 如果为不可变对象，以下方法将返回新方法：
	 * <ul>
	 * 	<li>{@link DateTime#offsite(DateField, int)}</li>
	 * 	<li>{@link DateTime#setField(DateField, int)}</li>
	 * 	<li>{@link DateTime#setField(int, int)}</li>
	 * </ul>
	 * 如果为不可变对象，{@link DateTime#setTime(long)}将抛出异常
	 *
	 * @param mutable 是否可变
	 * @return this
	 */
	public DateTime setMutable(boolean mutable) {
		this.mutable = mutable;
		return this;
	}

	// -------------------------------------------------------------------- toString start
	@Override
	public String toString() {
		return toString(DatePattern.NORM_DATETIME_FORMAT);
	}

	/**
	 * 转为字符串
	 * @param format 日期格式，常用格式见： {@link DatePattern}
	 * @return String
	 */
	public String toString(String format) {
		return toString(FastDateFormat.getInstance(format));
	}

	/**
	 * 转为字符串
	 * @param format {@link DatePrinter} 或 {@link FastDateFormat}
	 * @return String
	 */
	public String toString(DatePrinter format) {
		return format.format(this);
	}

	/**
	 * 转为字符串
	 * @param format {@link SimpleDateFormat}
	 * @return String
	 */
	public String toString(DateFormat format) {
		return format.format(this);
	}

	/**
	 * @return 输出精确到毫秒的标准日期形式
	 */
	public String toMsStr() {
		return toString(DatePattern.NORM_DATETIME_MS_FORMAT);
	}
	// -------------------------------------------------------------------- toString end

	/**
	 * 转换字符串为Date
	 * @param dateStr 日期字符串
	 * @param dateFormat {@link SimpleDateFormat}
	 * @return {@link Date}
	 */
	private static Date parse(String dateStr, DateFormat dateFormat){
		try {
			return dateFormat.parse(dateStr);
		} catch (Exception e) {
			String pattern;
			if(dateFormat instanceof SimpleDateFormat){
				pattern = ((SimpleDateFormat)dateFormat).toPattern();
			}else{
				pattern = dateFormat.toString();
			}
			throw new DateException("Parse ["+dateStr+"] with format ["+pattern+"] error!", e);
		}
	}

	/**
	 * 转换字符串为Date
	 * @param dateStr 日期字符串
	 * @param parser {@link FastDateFormat}
	 * @return {@link Date}
	 */
	private static Date parse(String dateStr, DateParser parser){
		try {
			return parser.parse(dateStr);
		} catch (Exception e) {
			throw new DateException("Parse ["+dateStr+"] with format ["+parser.getPattern()+"] error!", e);
		}
	}

	/**
	 * 设置日期时间
	 * @param time 日期时间毫秒
	 * @return this
	 */
	private DateTime setTimeInternal(long time) {
		super.setTime(time);
		return this;
	}
}
