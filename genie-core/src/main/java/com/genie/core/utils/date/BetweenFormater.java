package com.genie.core.utils.date;

/**
 * 时长格式化器
 *
 */
public class BetweenFormater {

	/** 时长毫秒数 */
	private long betweenMs;
	/** 格式化级别 */
	private Level level;

	/**
	 * 构造
	 * @param betweenMs 日期间隔
	 * @param level 级别，按照天、小时、分、秒、毫秒分为5个等级，根据传入等级，格式化到相应级别
	 */
	public BetweenFormater(long betweenMs, Level level) {
		this.betweenMs = betweenMs;
		this.level = level;
	}

	/**
	 * 格式化日期间隔输出<br>
	 *
	 * @return 格式化后的字符串
	 */
	public String format(){
		if(betweenMs == 0){
			return "0";
		}

		long day = betweenMs / DateUnit.DAY.getMillis();
		long hour = betweenMs / DateUnit.HOUR.getMillis() - day * 24;
		long minute = betweenMs / DateUnit.MINUTE.getMillis() - day * 24 * 60 - hour * 60;
		long second = betweenMs / DateUnit.SECOND.getMillis() - ((day * 24 + hour) * 60 + minute) * 60;
		long millisecond = betweenMs - (((day * 24 + hour) * 60 + minute) * 60 + second) * 1000;

		StringBuilder sb = new StringBuilder();
		final int levelValue = this.level.value;
		if(0 != day && levelValue > 0){
			sb.append(day).append("天");
		}
		if(0 != hour && levelValue > 1){
			sb.append(hour).append("小时");
		}
		if(0 != minute && levelValue > 2){
			sb.append(minute).append("分");
		}
		if(0 != second && levelValue > 3){
			sb.append(second).append("秒");
		}
		if(0 != millisecond && levelValue > 4){
			sb.append(millisecond).append("毫秒");
		}

		return sb.toString();
	}

	/**
	 * 获得 时长毫秒数
	 * @return 时长毫秒数
	 */
	public long getBetweenMs() {
		return betweenMs;
	}

	/**
	 * 设置 时长毫秒数
	 * @param betweenMs 时长毫秒数
	 */
	public void setBetweenMs(long betweenMs) {
		this.betweenMs = betweenMs;
	}

	/**
	 * 获得 格式化级别
	 * @return 格式化级别
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * 设置格式化级别
	 * @param level 格式化级别
	 */
	public void setLevel(Level level) {
		this.level = level;
	}

	/**
	 * 格式化等级枚举<br>
	 */
	public static enum Level {

		/** 天 */
		DAY(1),
		/** 小时 */
		HOUR(2),
		/** 分钟 */
		MINUTE(3),
		/** 秒 */
		SECOND(4),
		/** 毫秒 */
		MILLSECOND(5);

		private int value;

		private Level(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}
}
