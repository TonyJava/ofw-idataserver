package com.ht.dl645.msg.response;

import java.io.ByteArrayOutputStream;

import org.apache.commons.lang.StringUtils;

import com.ht.dl645.util.HexMessageDumper;
import com.ht.dl645.util.MessageUtils;

public abstract class AbstractDL645Response implements IDL645Response {
	protected ByteArrayOutputStream baos = new ByteArrayOutputStream();
	protected String slave;
	protected int funCode;
	protected int len;

	public AbstractDL645Response(String slave, int funCode, int len) {
		this.funCode = funCode;
		this.len = len;
		
		assert slave.length() == 12;
		this.slave = slave;
		
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

}
