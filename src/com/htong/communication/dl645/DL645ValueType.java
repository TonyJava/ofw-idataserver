package com.htong.communication.dl645;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public enum DL645ValueType {
	DATETIME, // 华通油井时间标签
	BCD, 
	INT32, 
	INT64;

	private final SimpleDateFormat SDF = new SimpleDateFormat("yyMMddHHmmss");

	public Object parseValue(String datas) {
		switch (this) {
		case DATETIME:

			try {
				return SDF.parse(datas);
			} catch (ParseException e) {
			}

			break;
		case BCD:
			return datas;
		case INT32:
			try {
				Integer i = Integer.parseInt(datas);
				return i;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			break;
		case INT64:
			
			try {
				Long l = Long.parseLong(datas);
				return l;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}

		return null;
	}

}
