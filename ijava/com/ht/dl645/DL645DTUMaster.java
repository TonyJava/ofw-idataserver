package com.ht.dl645;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.dl645.exception.DL645TimeOutException;
import com.ht.dl645.msg.request.AbstractDL645Request;
import com.ht.dl645.msg.request.DateAdjustRequest;
import com.ht.dl645.msg.request.DeviceResetRequest;
import com.ht.dl645.msg.request.ReadRequest;
import com.ht.dl645.msg.response.AbstractDL645Response;
import com.ht.dl645.msg.response.ReadResponse;
import com.ht.dl645.service.dtu.DL645DTUConnector;

public class DL645DTUMaster extends AbstractDL645Master {

	private static final Logger log = LoggerFactory
			.getLogger(DL645DTUMaster.class);
	private String dtuId;
	private String heartBeat;
	private DL645DTUConnector connector = null;
	private int timeout = 500;

	public DL645DTUMaster(String dtuId, String heartBeat, int timeout, int interval) {
		this.interval = interval;
		
		this.dtuId = dtuId;//dtuId.replaceAll("[^\\d|A|a]", "");
		this.heartBeat = heartBeat;
		
		this.timeout = timeout;
		connector = DL645DTUConnector.dtuMap.get(this.dtuId);
	}

	@Override
	public void open() {
		if (connector == null)
			connector = DL645DTUConnector.dtuMap.get(dtuId);

		if (connector != null) {
			connector.setTimeout(this.timeout);
			this.connector.open();
			if (!this.connector.isOpen()) {
				this.connector = DL645DTUConnector.dtuMap.get(dtuId);
			}
		}
	}

	@Override
	public boolean isOpened() {
		connector = DL645DTUConnector.dtuMap.get(dtuId);
		
		if (connector != null) {
			System.out.println("发送FF");
			connector.open();
			return this.connector.isOpen();
		} else {
			return false;
		}
	}

	@Override
	public void close() {
		if (connector != null) {
			this.connector.close();
			this.connector = null;
		}
	}
	
	public void checkHeartBeat() {
		
		if (!this.isOpened()) {
			
			this.open();
		}

		if (connector == null) {
			return;
		}
		
		connector.checkHeartBeat(heartBeat);
	}

	@Override
	public void sendMessage(byte[] data) throws Exception {
		throw new Exception("DTUMaster not supported");
	}

	@Override
	public void resetDevice(String slaveId) {

		if (!this.isOpened()) {
			this.open();
		}

		if (connector == null) {
			return;
		}

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

		if (!this.isOpened()) {
			this.open();
		}

		if (connector == null) {
			return;
		}

		synchronized (connector) {

			connector.getIoSession().setAttribute("DECODER.STATE", null);

			this.connector.write(new DateAdjustRequest());

			if (this.interval > 0) {
				try {
					Thread.sleep(this.interval);
				} catch (InterruptedException e) {
				}
			}

		}
	}

	@Override
	public AbstractDL645Response sendRequest(AbstractDL645Request request)
			throws Exception {

		if (!this.isOpened()) {
			this.open();
		}

		if (connector == null) {
			if (this.interval > 0)// 帧间隔
				Thread.sleep(500);
			throw new DL645TimeOutException("read time out");
		}

		synchronized (connector) {

			connector.getIoSession().setAttribute("DECODER.STATE", null);

			this.connector.write(request);

			// if (this.interval > 0)// 帧间隔
			// Thread.sleep(this.interval);

			Object response = this.connector.read();

			if (response == null) {
				this.connector.open();
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
		if (connector != null)
			return this.connector.getTimeout();
		else
			return this.timeout;
	}

	@Override
	public void setTimeout(int timeout) {
		this.timeout = timeout;
		if (connector != null)
			this.connector.setTimeout(timeout);
	}

}
