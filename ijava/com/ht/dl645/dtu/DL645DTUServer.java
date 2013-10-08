package com.ht.dl645.dtu;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.dl645.service.dtu.DL645DTUConnector;

public class DL645DTUServer extends Thread {
	private static Logger log = LoggerFactory.getLogger(DL645DTUServer.class);

	private boolean open = false;
	private ServerSocket serverSocket;
	
	public static Map<Integer, DL645DTUServer> serverPortMap = Collections.synchronizedMap(new HashMap<Integer, DL645DTUServer>());
	
	public static synchronized void listenPort(int port) {
		
		if (!serverPortMap.containsKey(port)) {
			log.info("DTUServer监听端口：" + port);
			try {
				DL645DTUServer dtuServer = new DL645DTUServer(port);
				dtuServer.start();
				serverPortMap.put(port, dtuServer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closePort(int port) {
		DL645DTUServer server =	serverPortMap.get(port);
		if (server != null) {
			server.close();
		}
	}

	private DL645DTUServer(int port) throws IOException {
		this.setDaemon(false);
		serverSocket = new ServerSocket(port);
		//serverSocket.setSoTimeout(10 * 60 * 1000);// socket超时时间
	}
	
	public void close() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.open = false;
	}

	@Override
	public void run() {
		this.open = true;
		while (open) {
		//if (open) {	
			try {
				Socket socket = serverSocket.accept();
				log.debug("已与客户端建立连接："
						+ socket.getRemoteSocketAddress().toString());
				new DL645DTUConnector(1000, socket, true);
				
			} catch (SocketException e) {
			} catch (IOException e) {
				log.warn("监听客户端请求超时。");
			}
			
			try {
				sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
	
}
