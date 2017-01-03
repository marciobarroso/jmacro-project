package com.icodeuplay.jmacro.common.exceptions;

public class JMacroException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JMacroException() {
		super();
	}

	public JMacroException(String message) {
		super(message);
	}

	public JMacroException(Throwable cause) {
		super(cause);
	}

	public JMacroException(String message, Throwable cause) {
		super(message, cause);
	}

	public JMacroException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
