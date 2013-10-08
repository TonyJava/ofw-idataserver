package com.ht.dl645.msg.response;

import org.apache.commons.lang.StringUtils;

import com.ht.dl645.util.MessageUtils;


public class ReadResponse extends AbstractDL645Response {
	
	private String dataid;
	private String datas;
	private int cs;

	public ReadResponse(String slave, int funCode, int byteLen, String dataid, String datas) {
		super(slave, funCode, byteLen);
		
		// 处理数据区 
		assert (dataid.length() + datas.length()) / 2 == byteLen;
		
		// 数据标识编码
		assert dataid.length() == 4;
		this.dataid = dataid;
		
		String dataidRevers = StringUtils.reverse(dataid);
		for (int i = 0; i < dataidRevers.length(); i+=2) {
			String b = new String(new char[]{dataidRevers.charAt(i + 1), dataidRevers.charAt(i)});
			baos.write(MessageUtils.encodeBCDByte(b) + 0x33);
		}
		
		// 数据项
		this.datas = datas;
		String datasRevers = StringUtils.reverse(datas);
		for (int i = 0; i < datasRevers.length(); i+=2) {
			String b = new String(new char[]{datasRevers.charAt(i + 1), datasRevers.charAt(i)});
			baos.write(MessageUtils.encodeBCDByte(b) + 0x33);
		}
		
		// cs校验
		this.cs = MessageUtils.calculateCS(getMessageData());
		baos.write(cs);
		// 结束
		baos.write(0x16);
	}
	
	public String getDataid() {
		return dataid;
	}

	@Override
	public String getDatas() {
		return datas;
	}
	
	@Override
	public int getCS() {
		return this.cs;
	}

}
