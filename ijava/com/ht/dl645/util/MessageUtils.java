package com.ht.dl645.util;



public class MessageUtils {
	
	private final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
        '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	
	private static int getBCDValue(char c) {
    	for (int i = 0; i < digits.length; i++) {
    		if (digits[i] == c) {
    			return i;
    		}
    	}
    	return -1;
	}
	
	/**
	 * 将给定的BCD格式的数据编码为16进制数
	 * 如：encodeBCDByte("78")将返回0x78, encodeBCDByte("8")将返回0x8
	 * @param bcd
	 * @return
	 */
	public static int encodeBCDByte(String bcd) {
		if (bcd.length() == 1) {
			return getBCDValue(bcd.charAt(0));
		}
    	return encodeBCDByte(bcd, false);
    }
	
	/**
	 * 将给定的BCD格式的数据编码为16进制数, 如果revers==true，则将高低位进行反转再编码
	 * 如：encodeBCDByte("78", true)将返回0x87, encodeBCDByte("78", false)将返回0x78
	 * @param bcd
	 * @param revers 是否反转
	 * @return
	 */
	public static int encodeBCDByte(String bcd, boolean revers) {
		if (bcd.length() == 1) {
			bcd = "0".concat(bcd);
		}
		assert bcd.length() == 2;
		if (revers) {
			return (getBCDValue(bcd.charAt(1)) << 4) + getBCDValue(bcd.charAt(0));
		} else {
			return (getBCDValue(bcd.charAt(0)) << 4) + getBCDValue(bcd.charAt(1));
		}
    }
    
    /**
     * 高位在前， 低位在后，如：decodeBCDByte(0x78)将返回"78"
     * @param i
     * @return 
     */
    public static String decodeBCDByte(int i) {
    	return decodeBCDByte(i, false);
    }
    
    /**
     * 高位在前， 低位在后.
     * 如：decodeBCDByte(0x78, false)将返回"78", decodeBCDByte(0x78, true)将返回"87"
     * @param i
     * @param revers 是否反转显示
     * @return 
     */
    public static String decodeBCDByte(int i, boolean revers) {
    	char[] ret = new char[2];
    	if(revers) {
	    	ret[1] = digits[(i >>> 4) & 0xF];
	    	ret[0] = digits[i & 0xF];
    	} else {
	    	ret[0] = digits[(i >>> 4) & 0xF];
	    	ret[1] = digits[i & 0xF];
    	}
    	return new String(ret);
    }
	
	public static int calculateCS(byte[] byteArray) {
		int ret = 0;
		for ( byte b : byteArray) {
			ret += (b % 256);
		}
		return ret & 0xFF;
	}

}
