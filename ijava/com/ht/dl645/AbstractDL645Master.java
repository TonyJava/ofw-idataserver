package com.ht.dl645;

import com.ht.dl645.msg.request.AbstractDL645Request;
import com.ht.dl645.msg.request.ReadRequest;
import com.ht.dl645.msg.response.IDL645Response;

/**
 * @author BoChengwen
 * 
 */
public abstract class AbstractDL645Master {
	protected int interval = 50;// ms

	abstract protected IDL645Response sendRequest(
			AbstractDL645Request request) throws Exception;

	abstract public void sendMessage(byte[] data) throws Exception;

	abstract public void open() throws Exception;

	abstract public boolean isOpened();

	abstract public void close();

	abstract public int getTimeout();

	abstract public void setTimeout(int timeout);
	
	abstract public void dateAdjust();

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	abstract public void resetDevice(String slaveId);
	
	/**
	 * @param slave
	 * @param funCode
	 * @param dataid
	 * @return
	 * @throws Exception
	 */
	public IDL645Response sendReadRequest(String slave, int funCode,
			String dataid) throws Exception {
		assert funCode == FunctionCode.MASTER_READ_REQUEST || funCode == FunctionCode.MASTER_READ_FOLLOW_REQUEST;
		return this.sendRequest(new ReadRequest(slave, funCode, dataid));
	}
}
