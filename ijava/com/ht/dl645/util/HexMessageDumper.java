package com.ht.dl645.util;

public class HexMessageDumper {

    /**
     * Initialize lookup tables.
     */
    private final static byte[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    
    public static String getHexdump(byte[] data) {
    	StringBuilder out = new StringBuilder();
    	for (int i = 0; i < data.length; i++) {
    		out.append(" ");
    		byte b = data[i];
			out.append((char)digits[(b >>> 4) & 0x0F]);
			out.append((char)digits[b & 0x0F]);
		}
    	return out.toString();
    }
    
    
    		
}
