package com.ht.dl645.msg.request;

import java.util.Calendar;

import com.ht.dl645.FunctionCode;
import com.ht.dl645.util.MessageUtils;

public class DateAdjustRequest extends AbstractDL645Request {
	private int cs;
	
	public DateAdjustRequest() {
		super("999999999999", FunctionCode.MASTER_DATE_ADJUST_REQUEST);
		
		Calendar datetime = Calendar.getInstance();
		// ss
		baos.write(MessageUtils.encodeBCDByte(Integer.toString(datetime.get(Calendar.SECOND))) + 0x33);
		// mm
		baos.write(MessageUtils.encodeBCDByte(Integer.toString(datetime.get(Calendar.MINUTE))) + 0x33);
		// hh
		baos.write(MessageUtils.encodeBCDByte(Integer.toString(datetime.get(Calendar.HOUR_OF_DAY))) + 0x33);
		// day
		baos.write(MessageUtils.encodeBCDByte(Integer.toString(datetime.get(Calendar.DATE))) + 0x33);
		// month
		baos.write(MessageUtils.encodeBCDByte(Integer.toString(datetime.get(Calendar.MONTH) + 1)) + 0x33);
		// year
		baos.write(MessageUtils.encodeBCDByte(Integer.toString(datetime.get(Calendar.YEAR)).substring(2)) + 0x33);
		
		this.cs = MessageUtils.calculateCS(getMessageData());
		baos.write(cs);
		baos.write(0x16);
	}

	@Override
	public int getCS() {
		return cs;
	}

}
