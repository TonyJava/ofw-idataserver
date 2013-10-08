package com.ht.dl645.msg.request;

import org.apache.commons.lang.StringUtils;

import com.ht.dl645.util.MessageUtils;


public class ReadRequest extends AbstractDL645Request {
	private String dataid;
	private int cs;

	public ReadRequest(String slave, int funCode, String dataid) {
		super(slave, funCode);
		
		assert dataid.length() == 4;
		this.dataid = dataid;
		
		String dataidRevers = StringUtils.reverse(dataid);
		for (int i = 0; i < dataidRevers.length(); i+=2) {
			String b = new String(new char[]{dataidRevers.charAt(i + 1), dataidRevers.charAt(i)});
			baos.write(MessageUtils.encodeBCDByte(b) + 0x33);
		}
		
		this.cs = MessageUtils.calculateCS(getMessageData());
		baos.write(cs);
		baos.write(0x16);
	}
	
	public String getDataid() {
		return dataid;
	}
	
	@Override
	public int getCS() {
		return this.cs;
	}
}
