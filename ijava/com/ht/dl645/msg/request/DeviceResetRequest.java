package com.ht.dl645.msg.request;

import com.ht.dl645.FunctionCode;
import com.ht.dl645.util.MessageUtils;

public class DeviceResetRequest extends AbstractDL645Request {
	private int cs;
	
	public DeviceResetRequest(String deviceId) {
		super(deviceId, FunctionCode.MASTER_DEVICE_RESET_REQUEST);
		
		baos.write(0x06);
		baos.write(0x51);
		baos.write(0xF8);
		baos.write(0x33);
		baos.write(0x33);
		baos.write(0x33);
		baos.write(0x33);
		
		this.cs = MessageUtils.calculateCS(getMessageData());
		baos.write(cs);
		baos.write(0x16);
	}

	@Override
	public int getCS() {
		return cs;
	}

}
