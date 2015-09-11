package com.ustc.gcsj.function;

import java.util.Calendar;
import java.util.Date;

public class Function {
	// 计算两个时间的周数
	public int computeWeeks(Date startDate, Date endDate) {

		int weeks = 0;

		Calendar beginCalendar = Calendar.getInstance();

		beginCalendar.setTime(startDate);

		Calendar endCalendar = Calendar.getInstance();

		endCalendar.setTime(endDate);

		while (beginCalendar.before(endCalendar)) {

			// 如果开始日期和结束日期在同年、同月且当前月的同一周时结束循环

			if (beginCalendar.get(Calendar.YEAR) == endCalendar

			.get(Calendar.YEAR)

			&& beginCalendar.get(Calendar.MONTH) == endCalendar

			.get(Calendar.MONTH)

			&& beginCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) == endCalendar

			.get(Calendar.DAY_OF_WEEK_IN_MONTH)) {

				break;

			} else {

				beginCalendar.add(Calendar.DAY_OF_YEAR, 7);

				weeks += 1;

			}

		}

		return weeks;
	}

	public boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}
}
