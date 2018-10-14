package com.genie.core.utils.date;

/**
 * 工具类异常
 */
public class DateException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public DateException(Throwable e) {
		super(e.getMessage(), e);
	}

	public DateException(String message) {
		super(message);
	}

	public DateException(String messageTemplate, Object... params) {
		super(messageTemplate+" "+params);
	}

	public DateException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DateException(Throwable throwable, String messageTemplate, Object... params) {
		super(messageTemplate+" "+params, throwable);
	}
}
