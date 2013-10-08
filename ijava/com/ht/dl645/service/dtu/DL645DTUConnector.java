package com.ht.dl645.service.dtu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.dl645.codec.DL645RequestEncoder;
import com.ht.dl645.codec.DL645ResponseDecoder;
import com.ht.dl645.service.AbstractIoConnector;
import com.ht.dl645.service.IDL645Decoder;
import com.ht.dl645.service.IDL645Encoder;
import com.htong.communication.CommunicationController;
import com.htong.status.DTUStatus;
import com.htong.ui.MainWindow;
import com.htong.util.DateUtil;

public class DL645DTUConnector extends AbstractIoConnector {

	private static final Logger log = LoggerFactory
			.getLogger(DL645DTUConnector.class);

	private IDL645Decoder decoder;
	private IDL645Encoder encoder;

	private int timeout = 500; // ms
	private boolean open = false;

	private String id = null;
	// private String deviceId;
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;

	public static Map<String, DL645DTUConnector> dtuMap = new ConcurrentHashMap<String, DL645DTUConnector>();

	public DL645DTUConnector(int timeout, Socket socket, boolean isMaster)
			throws IOException {
		super();
		if (isMaster) {
			encoder = new DL645RequestEncoder();
			decoder = new DL645ResponseDecoder();
		} else {
			System.out.println("暂不支持从站模式");
			assert false;
		}

		this.timeout = timeout;// 读写超时时间
		this.socket = socket;
		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();
		
//		outputStream.write(1);
//		outputStream.flush();
//		log.debug("发送一个东西");

		long endTime = System.currentTimeMillis() + 15000;// 3秒的超时时间
		String temp = "";
		while (true) {
			int bufLen = inputStream.available();
			log.debug("缓冲区字节个数：" + String.valueOf(bufLen));
			if (bufLen > 0) {
				byte[] data = new byte[bufLen];
				int readLen = inputStream.read(data);
				if (readLen > 0) {
					temp += new String(Arrays.copyOf(data, readLen)).trim();
					log.debug(temp);
					if (temp.trim().startsWith("<id>")
							&& temp.trim().endsWith("</id>")) {

						this.id = temp.trim().replaceAll("<id>", "")
								.replaceAll("</id>", "");
						log.info("已与DTU设备【" + id + "】建立连接。");
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								MainWindow.addText("与DTU【" + id + "】已建立连接。");
							}
						});
						
						//连接状态
						DTUStatus.instance.putDtuStatusMap(id, true);
						break;
					}					
				}
			}
			if (endTime < System.currentTimeMillis()) {
				break;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (this.id != null) {
			this.open = true;
			dtuMap.put(id, this);
		} else {
			log.debug("超时");
			socket.close();
			this.socket = null;
			this.inputStream = null;
			this.outputStream = null;
		}

	}

	public void checkHeartBeat(String heartBeat) {

		if (this.inputStream == null || this.id == null) {
			return;
		}

		try {
			long endTime = System.currentTimeMillis() + 200000;// 此处时间应大于天诚航宇和蓝迪等厂家的DTU时间间隔，否则不会收到心跳信号
			String temp = "";
			while (true) {
				int bufLen = inputStream.available();
				//log.debug("检查心跳信号");
				if (bufLen > 0) {
					byte[] data = new byte[bufLen];
					int readLen = inputStream.read(data);
					if (readLen > 0) {
						temp += new String(Arrays.copyOf(data, readLen)).trim();
						log.debug("心跳：" + temp);
						if (temp.trim().equals(heartBeat)) {
							//System.out.println(id + "心跳：" + heartBeat);
							DTUStatus.instance.putHeartBeatMap(id, DateUtil.getCurrentTime());							
							break;
						}
					}
				}
				//若程序关闭，则跳出循环，结束该线程
				if(!CommunicationController.INSTANCE.isRuning()) {
					break;
				}
				if (endTime < System.currentTimeMillis()) {
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
		}
	}

	@Override
	public IDL645Decoder getDecoder() {
		return decoder;
	}

	@Override
	public IDL645Encoder getEncoder() {
		return encoder;
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
	public void open() {
		if (this.isOpen()) {

			if (socket != null) {
				try {
//					log.debug("发送FF");
					socket.sendUrgentData(0xFF);
					return;
				} catch (IOException e) {
					log.debug(e.getMessage());
					this.close();
				}
			} else {
				this.close();
			}
		}
	}

	@Override
	public void close() {
		if (open) {
			log.debug("关闭连接");
			DTUStatus.instance.getDtuStatusMap().put(id, false);
			
			synchronized (dtuMap) {
				DL645DTUConnector connector = dtuMap.get(id);
				if (connector != null && connector.equals(this)) {
					dtuMap.remove(id);
				}
			}
			this.open = false;
			if (socket != null) {
				try {
					socket.close();
					socket = null;
					inputStream = null;
					outputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
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

	public String getId() {
		return id;
	}
}
