package com.htong.util;

public class HandleString {
	/**
	 * 将设备地址转成12位，来适应dl645库
	 * @param slaveId
	 * @return
	 */
	public static String handleSlaveIdTo12 (String slaveId) {
		int i = 12-slaveId.length();
		for(int j = 0;j<i;j++) {
			slaveId = "0" + slaveId;
		}
		return slaveId;
	}
}
