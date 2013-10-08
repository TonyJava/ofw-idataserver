package com.ht.dl645.msg.request;

import java.io.ByteArrayOutputStream;

import org.apache.commons.lang.StringUtils;

import com.ht.dl645.FunctionCode;
import com.ht.dl645.util.HexMessageDumper;
import com.ht.dl645.util.MessageUtils;

public abstract class AbstractDL645Request implements IDL645Request {
	protected ByteArrayOutputStream baos = new ByteArrayOutputStream();
	protected String slave;// 3个字节
	protected int funCode;
	protected int len = 0;

	public AbstractDL645Request(String slave, int funCode) {
		slave = slave.replaceAll("[^\\d|A|a]", "");
		
		assert slave.length() == 12;
		this.slave = slave;
		
		this.funCode = funCode;
		switch (funCode) {
		case FunctionCode.MASTER_READ_REQUEST:// 01
		case FunctionCode.MASTER_READ_FOLLOW_REQUEST:// 02
			len = 2;
			break;
		case FunctionCode.MASTER_RTRY_REQUEST:// 03
			len = 0;
			break;
		case FunctionCode.MASTER_DATE_ADJUST_REQUEST:// 08
			len = 6;
			break;
		default:
			break;
		}

		// 68 100000 68 01 di0 di1
		baos.write(0x68);
		String slaveRevers = StringUtils.reverse(slave);
		for (int i = 0; i < slaveRevers.length(); i+=2) {
			String b = new String(new char[]{slaveRevers.charAt(i + 1), slaveRevers.charAt(i)});
			baos.write(MessageUtils.encodeBCDByte(b));
		}
		baos.write(0x68);

		baos.write(this.funCode);
		baos.write(this.len);

	}

	@Override
	public int getMessageDataSize() {
		return baos.size();
	}

	@Override
	public byte[] getMessageData() {
		return baos.toByteArray();
	}
	
	@Override
	public String toHexString() {
		return HexMessageDumper.getHexdump(this.getMessageData());
	}

	public String getSlave() {
		return slave;
	}

	public void setSlave(String slave) {
		this.slave = slave;
	}

	public int getFunCode() {
		return funCode;
	}

	public void setFunCode(int funCode) {
		this.funCode = funCode;
	}

	
	@Override
	public int getLen() {
		return len;
	}
	
	public void setLen(int len) {
		this.len = len;
	}

}
