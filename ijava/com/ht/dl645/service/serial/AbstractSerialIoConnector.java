package com.ht.dl645.service.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.dl645.service.AbstractIoConnector;

public abstract class AbstractSerialIoConnector extends AbstractIoConnector {
	private static final Logger log = LoggerFactory.getLogger(AbstractIoConnector.class);
	
	private int timeout = 500; //ms
	private boolean open = false;
	
	private SerialAddress serialAddress;
	private SerialPort serialPort;
	private InputStream inputStream;
	private OutputStream outputStream;

	public AbstractSerialIoConnector(int timeout, SerialAddress serialAddress) {
		super();
		this.timeout = timeout;
		this.serialAddress = serialAddress;
	}

	@Override
	public int getTimeout() {
		return this.timeout;
	}

	@Override
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	@Override
	public boolean isOpen() {
		return this.open;
	}
	
	@Override
	public void open() throws SerialPortUnavailableException {
		if (this.isOpen()) {
			return;
		}

		CommPortIdentifier portId;
		Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers();

		// looping around found ports
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(serialAddress.getName())) {
					try {

						serialPort = (SerialPort) portId.open(
								"DDHT", (int) this.timeout);

						serialPort.setSerialPortParams(
								serialAddress.getBauds(),
								serialAddress.getDataBitsForRXTX(),
								serialAddress.getStopBitsForRXTX(),
								serialAddress.getParityForRXTX());

						serialPort.setFlowControlMode(serialAddress
								.getFLowControlForRXTX());

						serialPort.notifyOnDataAvailable(true);

						serialPort.setInputBufferSize(8);
						serialPort.setOutputBufferSize(8);
						serialPort.disableReceiveThreshold();

						this.inputStream = serialPort.getInputStream();
						this.outputStream = serialPort.getOutputStream();

						this.open = true;
						return;
					} catch (PortInUseException e) {
						log.error(e.getMessage(), e);
					} catch (UnsupportedCommOperationException e) {
						log.error(e.getMessage(), e);
					} catch (IOException e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		}

		throw new SerialPortUnavailableException("Serial port not found");
	}

	@Override
	public void close() {
		if (open) {
			this.open = false;
			try {
				getInputStream().close();
				getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (serialPort != null) {
				serialPort.close();
				serialPort = null;
			}
		}
	}

	@Override
	public InputStream getInputStream() {
		return this.inputStream;
	}

	@Override
	public OutputStream getOutputStream() {
		return this.outputStream;
	}
}
