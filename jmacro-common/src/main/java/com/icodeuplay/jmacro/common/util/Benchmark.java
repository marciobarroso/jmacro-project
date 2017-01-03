package com.icodeuplay.jmacro.common.util;

import java.util.Calendar;

/**
 * Class to benchmark executions
 */
public class Benchmark {

	private Calendar start;
	private Calendar end;

	public Benchmark() {
		this.start = Calendar.getInstance();
	}

	public void end() {
		this.end = Calendar.getInstance();
	}

	public String getPartialTime() {
		Calendar now = Calendar.getInstance();
		return DateUtils.getTimeDifferenceAsString(this.start.getTime(), now.getTime());
	}

	public String getTime() {
		if (this.end == null) {
			this.end();
		}
		return DateUtils.getTimeDifferenceAsString(this.start.getTime(), this.end.getTime());
	}

}
