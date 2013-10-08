package com.ht.dl645;

import com.ht.dl645.exception.ModbusSerialPortException;
import com.ht.dl645.msg.request.IDL645Request;
import com.ht.dl645.service.IoHandler;
import com.ht.dl645.service.serial.AbstractSerialIoConnector;
import com.ht.dl645.service.serial.DL645SerialConnector;
import com.ht.dl645.service.serial.SerialAddress;
import com.ht.dl645.service.serial.SerialPortUnavailableException;

public class DL645Slaver implements IDL645Slaver {
	private AbstractSerialIoConnector connector;
	private IoHandler ioHandler;

	public DL645Slaver(IoHandler ioHandler, SerialAddress portAddress, int timeout)
			throws InterruptedException, ModbusSerialPortException {
		this.ioHandler = ioHandler;
		connector = new DL645SerialConnector(timeout, portAddress, false);
		this.open();
	}

	public void open() throws SerialPortUnavailableException {
		this.connector.open();
	}

	public int getTimeout() {
		return connector.getTimeout();
	}

	/**
	 * read timeout, TimeUnit.MICROSECONDS 以ms为单位
	 * 
	 * @param timeout
	 */
	public void setTimeout(int timeout) {
		this.connector.setTimeout(timeout);
	}

	@Override
	public boolean isOpened() {
		return this.connector.isOpen();
	}

	@Override
	public void stop() {
		connector.close();
	}

	/*
	 * rtu模式下只允许连接一个主站
	 * 
	 * @see com.ht.modbus.ModbusSlaver#getConnectedMasterCount()
	 */
	@Override
	public int getConnectedMasterCount() {
		return 1;
	}

	@Override
	public void run() {
		while (this.isOpened()) {
			connector.getIoSession().setAttribute("DECODER.STATE", null);
			Object message = this.connector.read();

			if (message != null && message instanceof IDL645Request) {
				Object response = this.ioHandler
						.messageReceived((IDL645Request) message);

				if (response != null) {
//					int crc = MessageUtils
//							.calculateCRC(((IDL645Message) response)
//									.getMessageData());
//					connector.write(new DL645Message((IDL645Message) response,
//							crc));
				}

			}
		}
	}
}
