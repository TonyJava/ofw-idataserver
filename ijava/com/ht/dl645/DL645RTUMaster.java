package com.ht.dl645;

import com.ht.dl645.exception.DL645TimeOutException;
import com.ht.dl645.msg.request.AbstractDL645Request;
import com.ht.dl645.msg.request.DateAdjustRequest;
import com.ht.dl645.msg.request.DeviceResetRequest;
import com.ht.dl645.msg.request.ReadRequest;
import com.ht.dl645.msg.response.AbstractDL645Response;
import com.ht.dl645.msg.response.ReadResponse;
import com.ht.dl645.service.serial.AbstractSerialIoConnector;
import com.ht.dl645.service.serial.DL645SerialConnector;
import com.ht.dl645.service.serial.SerialAddress;
import com.ht.dl645.service.serial.SerialPortUnavailableException;

public class DL645RTUMaster extends AbstractDL645Master {

	private AbstractSerialIoConnector connector;

	public DL645RTUMaster(SerialAddress portAddress, int timeout, int interval) {
		this.interval = interval;
		connector = new DL645SerialConnector(timeout, portAddress, true);
	}

	public DL645RTUMaster(SerialAddress portAddress, int timeout) {
		this(portAddress, timeout, 50);
	}

	public DL645RTUMaster(SerialAddress portAddress) {
		this(portAddress, 1000);
	}

	@Override
	public void open() throws SerialPortUnavailableException {
		this.connector.open();
	}

	@Override
	public boolean isOpened() {
		return this.connector.isOpen();
	}

	@Override
	public void close() {
		this.connector.close();
	}

	@Override
	public void sendMessage(byte[] data) throws Exception {
		synchronized (connector) {
			if (!this.isOpened()) {
				this.open();
			}
			this.connector.write(data);
		}
	}
	
	@Override
	public void resetDevice(String slaveId) {

		synchronized (connector) {

			connector.getIoSession().setAttribute("DECODER.STATE", null);

			this.connector.write(new DeviceResetRequest(slaveId));

			if (this.interval > 0) {
				try {
					Thread.sleep(this.interval);
				} catch (InterruptedException e) {
				}
			}

		}
	}
	
	@Override
	public void dateAdjust() {
		
		synchronized (connector) {

			if (!this.isOpened()) {
				this.open();
			}

			connector.getIoSession().setAttribute("DECODER.STATE", null);
			
			this.connector.write(new DateAdjustRequest());

			if (this.interval > 0) {
				try { Thread.sleep(this.interval); } catch (InterruptedException e) {}
			}

		}
	}

	@Override
	public AbstractDL645Response sendRequest(
			AbstractDL645Request request) throws Exception {
		synchronized (connector) {

			if (!this.isOpened()) {
				this.open();
			}

			connector.getIoSession().setAttribute("DECODER.STATE", null);
			
			this.connector.write(request);

			// if (this.interval > 0)// 帧间隔
			// Thread.sleep(this.interval);

			Object response = this.connector.read();

			if (response == null) {
				throw new DL645TimeOutException("read time out");
			} else if (response instanceof Exception) {
				if (this.interval > 0)// 帧间隔
					Thread.sleep(this.interval);
				throw (Exception) response;
			}

			AbstractDL645Response resp = (AbstractDL645Response) response;
			if (!resp.getSlave().equals(request.getSlave())) {// 设备ID不一致
				response = this.connector.read();

				if (response == null) {
					this.connector.open();
					throw new DL645TimeOutException("read time out");
				} else if (response instanceof Exception) {
					if (this.interval > 0)// 帧间隔
						Thread.sleep(this.interval);
					throw (Exception) response;
				}
			} else if ((resp instanceof ReadResponse)
					&& (request instanceof ReadRequest)
					&& !((ReadResponse) resp).getDataid().equals(
							((ReadRequest) request).getDataid())) {// 数据ID不一致
				response = this.connector.read();

				if (response == null) {
					this.connector.open();
					throw new DL645TimeOutException("read time out");
				} else if (response instanceof Exception) {
					if (this.interval > 0)// 帧间隔
						Thread.sleep(this.interval);
					throw (Exception) response;
				}
			}

			if (this.interval > 0)// 帧间隔
				Thread.sleep(this.interval);

			return (AbstractDL645Response) response;
		}
	}

	@Override
	public int getTimeout() {
		return this.connector.getTimeout();
	}

	@Override
	public void setTimeout(int timeout) {
		this.connector.setTimeout(timeout);
	}

}
